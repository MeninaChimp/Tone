package org.menina.tone.client.source.zookeeper;

import lombok.Data;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.api.CuratorEvent;
import org.apache.curator.framework.api.CuratorListener;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.menina.tone.client.source.PropertyChangeListener;

/**
 * Created by Menina on 2017/6/10.
 */
@Data
public class ZookeeperListener implements CuratorListener{

    private PropertyChangeListener propertyChangeListener = new ZookeeperPropertyChangeListener();

    @Override
    public void eventReceived(CuratorFramework curatorFramework, CuratorEvent curatorEvent) throws Exception {
        final WatchedEvent watchedEvent = curatorEvent.getWatchedEvent();
        if (watchedEvent != null) {
            if (watchedEvent.getState() == Watcher.Event.KeeperState.SyncConnected) {
                switch (watchedEvent.getType()) {
                    case NodeDataChanged:
                        propertyChangeListener.reloadProperty(watchedEvent.getPath());
                        break;
                    default:
                        break;
                }
            }
        }
    }
}
