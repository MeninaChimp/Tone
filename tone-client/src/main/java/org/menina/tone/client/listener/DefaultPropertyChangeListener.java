package org.menina.tone.client.listener;

import lombok.extern.slf4j.Slf4j;

import java.util.Map.Entry;

/**
 * Created by Menina on 2017/6/10.
 */
@Slf4j
public class DefaultPropertyChangeListener implements Listener{
    @Override
    public void propertyChange(Entry<String, String> data) {
        log.info(String.format("property changed -- key: %s, newValue: %s"), data.getKey(), data.getValue());
    }
}
