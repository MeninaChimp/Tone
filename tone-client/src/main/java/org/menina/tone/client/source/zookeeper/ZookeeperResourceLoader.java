package org.menina.tone.client.source.zookeeper;

import com.google.common.base.Charsets;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.api.CuratorEvent;
import org.apache.curator.framework.api.CuratorListener;
import org.apache.curator.framework.api.GetChildrenBuilder;
import org.apache.curator.framework.api.GetDataBuilder;
import org.apache.curator.framework.state.ConnectionState;
import org.apache.curator.framework.state.ConnectionStateListener;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.curator.utils.ZKPaths;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.menina.tone.client.source.GrayscaleReleaseSupport;
import org.menina.tone.client.source.PropertyChangeProcessor;
import org.menina.tone.client.source.ResourceLoader;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by Menina on 2017/6/10.
 */
@Slf4j
public class ZookeeperResourceLoader implements ResourceLoader, CuratorListener {

    private CuratorFramework client;

    private PropertyChangeProcessor propertyChangeProcessor;

    private GrayscaleReleaseSupport grayscaleReleaseSupport;

    private ZookeeperFailbackRegistry zookeeperFailbackRegistry;

    public ZookeeperResourceLoader(String zookeeperAdddress) {
        this.client = CuratorFrameworkFactory.newClient(zookeeperAdddress, 2000, 3000, new ExponentialBackoffRetry(1000, 3));
        this.client.start();
        this.client.getCuratorListenable().addListener(this);
        this.client.getConnectionStateListenable().addListener(new ConnectionStateListener() {
            @Override
            public void stateChanged(CuratorFramework client, ConnectionState newState) {
                if (newState == ConnectionState.RECONNECTED) {
                    zookeeperFailbackRegistry.recover();
                }
            }
        });
        this.propertyChangeProcessor = new ZookeeperPropertyChangeProcessor(this);
        this.grayscaleReleaseSupport = new ZookeeperGrayscaleReleaseSupport(this.client);
        this.zookeeperFailbackRegistry = new ZookeeperFailbackRegistry(this.client);
    }

    @Override
    public Map<String, String> load(String path) {
        GetChildrenBuilder childrenBuilder = this.client.getChildren();
        Map<String, String> propertiesMap = Maps.newHashMap();
        try {
            List<String> nodes = childrenBuilder.forPath(path);
            if (nodes != null && nodes.size() != 0) {
                for (String node : nodes) {
                    String leaf = ZKPaths.makePath(path, node);
                    this.grayscaleReleaseSupport.subscribe(leaf);
                    propertiesMap.putAll(this.loadProperty(leaf));
                }
            }
        } catch (Throwable t) {
            throw new RuntimeException(String.format("No children for given path, %s", t.getMessage()), t);
        }

        this.log(propertiesMap);
        return propertiesMap;
    }

    public Map<String, String> loadProperty(String nodePath) {
        try {
            String propertyName = ZKPaths.getNodeFromPath(nodePath);
            GetDataBuilder data = this.client.getData();
            String propertyValue = new String(data.watched().forPath(nodePath), Charsets.UTF_8);
            Map<String, String> map = new HashMap<>();
            map.put(propertyName, propertyValue);
            return map;
        } catch (Throwable t) {
            throw new RuntimeException(String.format("Failed to load properties by given path, %s", t.getMessage()), t);
        }
    }

    private void log(Map<String, String> propertiesMap) {
        Set<String> keys = propertiesMap.keySet();
        for (String key : keys) {
            log.info(String.format("Tone Properties{ %s --> %s }", key, propertiesMap.get(key)));
        }
    }

    @Override
    public void eventReceived(CuratorFramework client, CuratorEvent event) throws Exception {
        final WatchedEvent watchedEvent = event.getWatchedEvent();
        if (watchedEvent != null) {
            if (watchedEvent.getState() == Watcher.Event.KeeperState.SyncConnected) {
                switch (watchedEvent.getType()) {
                    case NodeDataChanged:
                        this.propertyChangeProcessor.notifier(this.propertyChangeProcessor.reloadProperty(watchedEvent.getPath()));
                        break;
                    default:
                        break;
                }
            }
        }
    }
}
