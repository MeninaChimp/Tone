package org.menina.tone.client.listener;

import java.util.Map;

/**
 * Created by Menina on 2017/6/10.
 */
public interface Listener {

    void propertyChange(ListenerChain chain, Map<String, String> data);
}
