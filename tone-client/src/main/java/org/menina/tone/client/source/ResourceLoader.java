package org.menina.tone.client.source;

import java.util.Map;
import java.util.Map.Entry;

/**
 * Created by Menina on 2017/6/10.
 */
public interface ResourceLoader{

    Map<String, String> load(String path);

}
