package org.menina.tone.client.source.zookeeper;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.curator.utils.ZKPaths;
import org.menina.tone.client.source.ResourceLoaderAdapter;

import java.util.Map;

/**
 * Created by Menina on 2017/6/10.
 */
public class ZookeeperResourceLoader extends ResourceLoaderAdapter{

    private volatile static CuratorFramework client;

    @Override
    public Map<String, String> loads(String path) {
        return null;
    }

    @Override
    public Map.Entry<String, String> loadProperty(String nodePath) {
        return null;
    }


    private CuratorFramework instance(){
        if(null == client){
            synchronized (ZookeeperResourceLoader.class){
                CuratorFramework client = CuratorFrameworkFactory.newClient(
                        this.getUrl(),
                        2000,
                        2000,
                        new ExponentialBackoffRetry(1000, 3));

                client.start();
            }
        }

        return client;
    }

    private String makePath(){
        return ZKPaths.makePath(this.getVersion(), this.getPath());
    }
}
