package org.menina.tone.client.source;

import lombok.Data;
import org.menina.tone.client.listener.ListenerChain;
import org.menina.tone.client.reload.HotLoading;

import java.util.Map;

/**
 * Created by Menina on 2017/6/10.
 */
@Data
public abstract class PropertyChangeListenerAdapter implements PropertyChangeListener{

    private ResourceLoader resourceLoader;

    private ListenerChain listenerChain;

    private HotLoading hotLoading;

    public abstract void notifier(Map.Entry<String, String> data);
}
