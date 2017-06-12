package org.menina.tone.client.spring;

import com.google.common.reflect.ClassPath;
import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.api.CuratorEvent;
import org.apache.curator.framework.api.CuratorListener;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.menina.tone.client.listener.ToneListener;
import org.menina.tone.client.listener.Listener;
import org.menina.tone.client.properties.TonePropertyConfiguration;
import org.menina.tone.client.source.PropertyChangeListener;
import org.menina.tone.client.source.ResourceLoader;
import org.menina.tone.client.source.zookeeper.ZookeeperPropertyChangeListener;
import org.menina.tone.client.source.zookeeper.ZookeeperResourceLoader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;


/**
 * Created by Menina on 2017/6/10.
 */
@Slf4j
@Configuration
@PropertySource(value = "classpath:tone.properties", ignoreResourceNotFound = true)
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

    @Conditional(InterceptEnvironmentProperty.class)
    @Bean
    public TonePropertyConfiguration interceptEnvironmentProperty() {
        return new TonePropertyConfiguration();
    }

    private static PropertySourceLoader springPropertySourceLoader() {
        if (TonePropertyConfiguration.getResourceLoader() != null) {
            Object userDefinedResourceLoader = null;
            PropertySourceLoader propertySourceLoader;
            try {
                Class<?> clz;
                try {
                    clz = Class.forName(TonePropertyConfiguration.getResourceLoader());
                } catch (ClassNotFoundException e) {
                    throw new RuntimeException(String.format("The given user-defined resourceLoader path not found, %s", e.getMessage()), e);
                }

                userDefinedResourceLoader = clz.newInstance();
                if (!(userDefinedResourceLoader instanceof ResourceLoader)) {
                    throw new RuntimeException("The user-defined resourceLoader must implement \"ResourceLoader\" interface");
                }
            } catch (InstantiationException e) {
                log.error(String.format("Failed to create resourceLoader instance by given implement, %s"), e.getMessage(), e);
            } catch (IllegalAccessException e) {
                log.error(e.getMessage(), e);
            }

            propertySourceLoader = new SpringPropertySourceLoader();
            propertySourceLoader.setResourceLoader((ResourceLoader) userDefinedResourceLoader);
            return propertySourceLoader;
        }

        return initDefaultPropertySourceLoader();
    }

    private static PropertySourceLoader initDefaultPropertySourceLoader() {
        PropertySourceLoader propertySourceLoader = new SpringPropertySourceLoader();
        ResourceLoader<CuratorListener> resourceLoader = new ZookeeperResourceLoader();
        final PropertyChangeListener propertyChangeListener = new ZookeeperPropertyChangeListener();
        resourceLoader.setUrl(TonePropertyConfiguration.getAddress());
        resourceLoader.setResourceContainerListener(new CuratorListener() {
            @Override
            public void eventReceived(CuratorFramework curatorFramework, CuratorEvent curatorEvent) throws Exception {
                final WatchedEvent watchedEvent = curatorEvent.getWatchedEvent();
                if (watchedEvent != null) {
                    if (watchedEvent.getState() == Watcher.Event.KeeperState.SyncConnected) {
                        switch (watchedEvent.getType()) {
                            case NodeDataChanged:
                                propertyChangeListener.reloadProperty(watchedEvent.getPath());
                                break;
                            default:
                                break;
                        }
                    }
                }
            }
        });

        propertyChangeListener.setResourceLoader(resourceLoader);
        propertySourceLoader.setResourceLoader(resourceLoader);
        return propertySourceLoader;
    }
}
