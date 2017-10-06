package org.menina.tone.client.source.zookeeper;

import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.utils.ZKPaths;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.menina.tone.client.source.GrayscaleReleaseSupport;
import org.menina.tone.common.utils.Constant;
import org.menina.tone.common.utils.NetUtils;

/**
 * Created by Menina on 2017/10/4.
 */
@Slf4j
public class ZookeeperGrayscaleReleaseSupport implements GrayscaleReleaseSupport {

    private CuratorFramework client;

    public ZookeeperGrayscaleReleaseSupport(CuratorFramework client) {
        this.client = client;
    }

    @Override
    public void subscribe(String key) {
        String grayIp = Constant.GRAY_SCALA_APP_PATH + Constant.SEPARATOR + key.split("/")[3] + Constant.SEPARATOR + NetUtils.getLocalAddress().getHostAddress();
        String leaf = key + Constant.IPS + Constant.SEPARATOR + NetUtils.getLocalAddress().getHostAddress() + Constant.SEPARATOR + ZKPaths.getNodeFromPath(key);
        ZookeeperFailbackRegistry.register(grayIp);
        ZookeeperFailbackRegistry.register(leaf);
        while (true) {
            try {
                this.client.getData().watched().forPath(leaf);
                break;
            } catch (KeeperException.NoNodeException e) {
                try {
                    this.client.create().creatingParentsIfNeeded().withMode(CreateMode.EPHEMERAL).forPath(leaf);
                    this.client.create().creatingParentsIfNeeded().withMode(CreateMode.EPHEMERAL).forPath(grayIp);
                }catch (Exception t){
                    // ignore
                }
            } catch (KeeperException.NodeExistsException e) {
                // ignore
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }
        }
    }


    @Override
    public void notifier(String key) {
        return;
    }
}
