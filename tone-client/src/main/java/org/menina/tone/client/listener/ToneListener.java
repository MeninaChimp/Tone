package org.menina.tone.client.listener;

import lombok.extern.slf4j.Slf4j;

import java.util.Map;

/**
 * Created by Menina on 2017/6/10.
 */
@Slf4j
public class ToneListener implements Listener {

    @Override
    public void propertyChange(ListenerChain chain, Map<String, String> data) {
        for (Map.Entry<String, String> entry : data.entrySet()) {
            log.info(String.format("property changed -- key: %s, newValue: %s"), entry.getKey(), entry.getValue());
        }

        chain.invoke(data);
    }
}
