package org.menina.tone.client.load;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/**
 * Created by Menina on 2017/6/10.
 */
@Configuration
@PropertySource(value = "classpath:tone.properties", ignoreResourceNotFound = true)
public class TonePropertyConfiguration {

    @Value("${zookeeper}")
    private String zookeeper;

    @Value("${root}")
    private String root;

    @Value("${version}")
    private Float version;

    @Value("${node}")
    private String node;

    public String getZookeeper() {
        return zookeeper;
    }

    public String getRoot() {
        return root;
    }

    public Float getVersion() {
        return version;
    }

    public String getNode() {
        return node;
    }
}
