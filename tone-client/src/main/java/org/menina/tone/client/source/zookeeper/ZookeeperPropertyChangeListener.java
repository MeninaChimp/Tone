package org.menina.tone.client.source.zookeeper;

import lombok.NoArgsConstructor;
import org.apache.curator.framework.api.CuratorListener;
import org.menina.tone.client.listener.ListenerAsyncProcessExecutor;
import org.menina.tone.client.listener.ListenerChainFactory;
import org.menina.tone.client.source.PropertyChangeListenerAdapter;

import java.util.Map;

/**
 * Created by Menina on 2017/6/10.
 */
@NoArgsConstructor
public class ZookeeperPropertyChangeListener extends PropertyChangeListenerAdapter {

    @Override
    public void reloadProperty(String nodePath) {
        Map.Entry<String, String> data = this.getResourceLoader().loadProperty(nodePath);
        this.notifier(data);
        this.getHotLoading().reload(data.getKey(), data.getValue());
    }

    @Override
    public void notifier(Map.Entry<String, String> data) {
        ListenerAsyncProcessExecutor.commit(new ListenerChainFactory().getListenerChain(), data);
    }
}
