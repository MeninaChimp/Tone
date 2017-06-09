package org.menina.tone.client.source;

import java.util.Map;

/**
 * Created by Menina on 2017/6/10.
 */
public interface ResouceLoader<K, V> {

    Map<K, V> load();
}
