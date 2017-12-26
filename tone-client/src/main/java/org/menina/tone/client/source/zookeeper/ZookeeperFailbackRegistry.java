package org.menina.tone.client.source.zookeeper;

import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.CuratorFramework;
import org.apache.zookeeper.CreateMode;
import org.menina.tone.common.utils.NetUtils;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by menina on 2017/10/5.
 */
@Slf4j
public class ZookeeperFailbackRegistry {

    private CuratorFramework client;
    private static Set<String> recoverRegistered = new HashSet<>();

    public ZookeeperFailbackRegistry(CuratorFramework client){
        this.client = client;
    }

    public static void register(String path){
        recoverRegistered.add(path);
    }

    public static void cancel(String path){
        recoverRegistered.remove(path);
    }

    public static Set<String> getRecoverRegistered(){
        return recoverRegistered;
    }

    public void recover() {
        for (String node : ZookeeperFailbackRegistry.recoverRegistered) {
            try {
                if (node.contains(NetUtils.getLocalAddress().getHostAddress())) {
                    this.client.create().creatingParentsIfNeeded().withMode(CreateMode.EPHEMERAL).forPath(node);
                    this.client.getData().watched().forPath(node);
                    log.info(String.format("Recover node and watcher for the path %s because net issue occur on %s", node, NetUtils.getLocalAddress().getHostAddress()));
                }
            } catch (Exception e) {
                log.error(String.format("Failed to recover node and watcher for the path %s, %s", node, e.getMessage()), e);
            }
        }
    }
}
