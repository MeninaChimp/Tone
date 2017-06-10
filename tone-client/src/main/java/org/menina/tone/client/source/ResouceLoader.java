package org.menina.tone.client.source;

import java.util.Map;
import java.util.Map.Entry;

/**
 * Created by Menina on 2017/6/10.
 */
public interface ResouceLoader<K, V> {

    Map<String, String> load();

    Entry<String, String> loadKey(String key);
}
