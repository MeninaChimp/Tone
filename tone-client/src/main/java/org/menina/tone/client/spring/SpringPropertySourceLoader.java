package org.menina.tone.client.spring;

import com.google.common.collect.Maps;
import org.menina.tone.client.source.ResourceLoader;

import java.util.Map;

/**
 * Created by Menina on 2017/6/11.
 */
public class SpringPropertySourceLoader implements PropertySourceLoader{

    private ResourceLoader resourceLoader;

    public Map<String, String> loadResource(String... paths){
        Map<String, String> propertySource = Maps.newHashMap();
        for(String path : paths){
            propertySource.putAll(this.resourceLoader.loads(path));
        }

        return propertySource;
    }

    public void setResourceLoader(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }
}
