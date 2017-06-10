package org.menina.tone.client.load;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/**
 * Created by Menina on 2017/6/10.
 */
@Getter
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
}
