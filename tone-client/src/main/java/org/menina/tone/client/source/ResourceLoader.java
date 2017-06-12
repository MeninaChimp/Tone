package org.menina.tone.client.source;

import java.util.Map;
import java.util.Map.Entry;

/**
 * Created by Menina on 2017/6/10.
 */
public interface ResourceLoader<T> {

    void setUrl(String url);

    void setResourceContainerListener(T t);

    Map<String, String> loads(String path);

    Entry<String, String> loadProperty(String nodePath);

}
