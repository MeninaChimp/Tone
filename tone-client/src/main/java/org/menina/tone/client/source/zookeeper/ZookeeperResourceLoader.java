package org.menina.tone.client.source.zookeeper;

import com.google.common.base.Charsets;
import com.google.common.base.Throwables;
import com.google.common.collect.Maps;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.api.GetChildrenBuilder;
import org.apache.curator.framework.api.GetDataBuilder;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.curator.utils.ZKPaths;
import org.menina.tone.client.source.ResourceLoaderAdapter;

import java.util.List;
import java.util.Map;

/**
 * Created by Menina on 2017/6/10.
 */
public class ZookeeperResourceLoader extends ResourceLoaderAdapter {

    private volatile static CuratorFramework client;

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


    private CuratorFramework instance() {
        if (null == client) {
            synchronized (ZookeeperResourceLoader.class) {
                CuratorFramework client = CuratorFrameworkFactory.newClient(
                        this.getUrl(),
                        2000,
                        2000,
                        new ExponentialBackoffRetry(1000, 3));

                client.start();
            }
        }

        client.getCuratorListenable().addListener(new ZookeeperListener());
        return client;
    }
}
