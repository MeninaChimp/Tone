package org.menina.tone.client.source.zookeeper;

import com.google.common.base.Charsets;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.api.CuratorListener;
import org.apache.curator.framework.api.GetChildrenBuilder;
import org.apache.curator.framework.api.GetDataBuilder;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.curator.utils.ZKPaths;
import org.menina.tone.client.source.PropertyChangeListener;
import org.menina.tone.client.source.ResourceLoaderAdapter;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by Menina on 2017/6/10.
 */
@Slf4j
public class ZookeeperResourceLoader extends ResourceLoaderAdapter<CuratorListener> {

    private volatile static CuratorFramework client;

    private CuratorFramework instance() {
        if (null == client) {
            synchronized (ZookeeperResourceLoader.class) {
                if(null == client){
                    client = CuratorFrameworkFactory.newClient(this.getUrl(), new ExponentialBackoffRetry(1000, 3));
                    client.start();
                    client.getCuratorListenable().addListener(super.getResourceContainerListener());
                }
            }
        }

        return client;
    }

    private void logProperties(Map<String, String> propertiesMap){
        Set<String> keys = propertiesMap.keySet();
        for (String key : keys){
            log.info(String.format("Tone Properties{ %s --> %s }", key, propertiesMap.get(key)));
        }
    }

    @Override
    public Map<String, String> loads(String path) {
        GetChildrenBuilder childrenBuilder = this.instance().getChildren();
        Map<String, String> propertiesMap = Maps.newHashMap();
        try {
            List<String> nodes = childrenBuilder.forPath(path);
            if (nodes != null) {
                for (String node : nodes) {
                    Map.Entry<String, String> data = this.loadProperty(ZKPaths.makePath(path, node));
                    if (data != null) {
                        propertiesMap.put(data.getKey(), data.getValue());
                    }
                }
            }
        } catch (Throwable t) {
            throw new RuntimeException(String.format("Failed to load properties by given path, %s", t.getMessage()), t);
        }

        this.logProperties(propertiesMap);
        return propertiesMap;
    }

    @Override
    public Map.Entry<String, String> loadProperty(String nodePath) {
        String propertyName = ZKPaths.getNodeFromPath(nodePath);
        GetDataBuilder data = this.instance().getData();
        try {
            String propertyValue = new String(data.watched().forPath(nodePath), Charsets.UTF_8);
            return Maps.immutableEntry(propertyName, propertyValue);
        }catch (Throwable t) {
            throw new RuntimeException(String.format("Failed to load property by given nodePath, %s", t.getMessage()), t);
        }
    }
}
