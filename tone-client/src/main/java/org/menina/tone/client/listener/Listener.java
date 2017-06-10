package org.menina.tone.client.listener;

import java.util.Map.Entry;

/**
 * Created by Menina on 2017/6/10.
 */
public interface Listener {

    void propertyChange(Entry<String, String> data);
}
