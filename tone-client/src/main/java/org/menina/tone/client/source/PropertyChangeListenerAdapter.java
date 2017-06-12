package org.menina.tone.client.source;

import lombok.Data;
import org.menina.tone.client.listener.ListenerChain;
import org.menina.tone.client.listener.ListenerChainFactory;
import org.menina.tone.client.reload.HotLoading;
import org.menina.tone.client.reload.HotLoadingProcessor;

import java.util.Map;

/**
 * Created by Menina on 2017/6/10.
 */
@Data
public abstract class PropertyChangeListenerAdapter implements PropertyChangeListener{

    private ResourceLoader resourceLoader;

    private ListenerChain listenerChain = new ListenerChainFactory().getListenerChain();

    private HotLoading hotLoading = new HotLoadingProcessor();

    public abstract void notifier(Map.Entry<String, String> data);
}
