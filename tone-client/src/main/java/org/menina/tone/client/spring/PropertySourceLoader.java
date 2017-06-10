package org.menina.tone.client.spring;

import org.menina.tone.client.source.ResourceLoader;

import java.util.Map;

/**
 * Created by Menina on 2017/6/11.
 */
public interface PropertySourceLoader {

    Map<String, String> loadResource(String... paths);

    void setResourceLoader(ResourceLoader resourceLoader);
}
