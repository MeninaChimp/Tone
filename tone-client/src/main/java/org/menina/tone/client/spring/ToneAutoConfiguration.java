package org.menina.tone.client.spring;

import org.menina.tone.client.listener.ToneListener;
import org.menina.tone.client.listener.Listener;
import org.menina.tone.client.properties.TonePropertyConfiguration;
import org.menina.tone.client.source.PropertyChangeListener;
import org.menina.tone.client.source.ResourceLoader;
import org.menina.tone.client.source.zookeeper.ZookeeperResourceLoader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;


/**
 * Created by Menina on 2017/6/10.
 */
@Configuration
public class ToneAutoConfiguration {

    @Autowired(required = false)
    private PropertyChangeListener propertyChangeListener;

    @Autowired(required = false)
    private ResourceLoader resourceLoader;

    @Autowired
    private TonePropertyConfiguration tonePropertyConfiguration;

    @Bean
    public Listener defaultListener(){
        return new ToneListener();
    }

    @Bean
    private PropertySourceLoader springPropertySourceLoader(){
        PropertySourceLoader propertySourceLoader = new SpringPropertySourceLoader();
        if(null == this.resourceLoader){
            this.resourceLoader = new ZookeeperResourceLoader();
            this.resourceLoader.setUrl(tonePropertyConfiguration.getAddress());
        }

        propertySourceLoader.setResourceLoader(this.resourceLoader);
        return propertySourceLoader;
    }

    @Bean
    public PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer(){
        TonePropertySourcesPlaceholderConfigurer tonePropertySourcesPlaceholderConfigurer = new TonePropertySourcesPlaceholderConfigurer();
        tonePropertySourcesPlaceholderConfigurer.setAllowOverride(false);
        tonePropertySourcesPlaceholderConfigurer.setPropertySourceLoader(this.springPropertySourceLoader());
        tonePropertySourcesPlaceholderConfigurer.setTonePropertyConfiguration(this.tonePropertyConfiguration);
        return tonePropertySourcesPlaceholderConfigurer;
    }
}
