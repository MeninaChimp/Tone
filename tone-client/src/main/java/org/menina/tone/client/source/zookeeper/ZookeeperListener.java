package org.menina.tone.client.source.zookeeper;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.api.CuratorEvent;
import org.apache.curator.framework.api.CuratorListener;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;

/**
 * Created by Menina on 2017/6/10.
 */
public class ZookeeperListener implements CuratorListener{

    @Override
    public void eventReceived(CuratorFramework curatorFramework, CuratorEvent curatorEvent) throws Exception {
        final WatchedEvent watchedEvent = curatorEvent.getWatchedEvent();
        if (watchedEvent != null) {
            if (watchedEvent.getState() == Watcher.Event.KeeperState.SyncConnected) {
                switch (watchedEvent.getType()) {
                    case NodeChildrenChanged:
                        break;
                    case NodeDataChanged:
                        break;
                    default:
                        break;
                }
            }
        }
    }
}
