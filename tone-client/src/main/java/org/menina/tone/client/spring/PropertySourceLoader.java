package org.menina.tone.client.spring;


import java.util.Map;

/**
 * Created by Menina on 2017/6/11.
 */
public interface PropertySourceLoader {

    Map<String, String> loadResource(String... paths);

}
