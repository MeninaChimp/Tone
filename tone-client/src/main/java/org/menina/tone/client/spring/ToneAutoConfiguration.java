package org.menina.tone.client.spring;

import lombok.extern.slf4j.Slf4j;
import org.menina.tone.client.listener.ToneListener;
import org.menina.tone.client.listener.Listener;
import org.menina.tone.client.properties.TonePropertyConfiguration;
import org.menina.tone.client.source.ResourceLoader;
import org.menina.tone.client.source.zookeeper.ZookeeperResourceLoader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;

/**
 * Created by Menina on 2017/6/10.
 */
@Slf4j
@Configuration
public class ToneAutoConfiguration {

    @Bean
    public Listener defaultListener() {
        return new ToneListener();
    }

    @Bean
    public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
        TonePropertySourcesPlaceholderConfigurer tonePropertySourcesPlaceholderConfigurer = new TonePropertySourcesPlaceholderConfigurer();
        tonePropertySourcesPlaceholderConfigurer.setAllowOverride(false);
        tonePropertySourcesPlaceholderConfigurer.setIgnoreUnresolvablePlaceholders(true);
        tonePropertySourcesPlaceholderConfigurer.setPropertySourceLoader(springPropertySourceLoader());
        tonePropertySourcesPlaceholderConfigurer.setTonePropertyConfiguration(new TonePropertyConfiguration());
        return tonePropertySourcesPlaceholderConfigurer;
    }

    private static PropertySourceLoader springPropertySourceLoader() {
        if (TonePropertyConfiguration.getInstance().getResourceLoader() != null) {
            Object userDefinedResourceLoader = null;
            try {
                Class<?> clz;
                try {
                    clz = Class.forName(TonePropertyConfiguration.getInstance().getResourceLoader());
                } catch (ClassNotFoundException e) {
                    throw new RuntimeException(String.format("The given user-defined resourceLoader path not found, %s", e.getMessage()), e);
                }

                userDefinedResourceLoader = clz.newInstance();
                if (!(userDefinedResourceLoader instanceof ResourceLoader)) {
                    throw new RuntimeException("The user-defined resourceLoader must instance of \"org.menina.tone.client.source.ResourceLoader\"");
                }
            } catch (InstantiationException e) {
                log.error(String.format("Failed to create resourceLoader instance by given implement, %s"), e.getMessage(), e);
            } catch (IllegalAccessException e) {
                log.error(e.getMessage(), e);
            }

            SpringPropertySourceLoader springPropertySourceLoader = new SpringPropertySourceLoader();
            springPropertySourceLoader.setResourceLoader((ResourceLoader) userDefinedResourceLoader);
            return springPropertySourceLoader;
        }

        return initDefaultPropertySourceLoader();
    }

    private static PropertySourceLoader initDefaultPropertySourceLoader() {
        SpringPropertySourceLoader propertySourceLoader = new SpringPropertySourceLoader();
        ZookeeperResourceLoader zookeeperResourceLoader = new ZookeeperResourceLoader(TonePropertyConfiguration.getInstance().getAddress());
        propertySourceLoader.setResourceLoader(zookeeperResourceLoader);
        return propertySourceLoader;
    }
}
