package org.menina.tone.client.spring;

import lombok.extern.slf4j.Slf4j;
import org.menina.tone.client.properties.TonePropertyConfiguration;
import org.menina.tone.client.source.ResourceLoader;
import org.menina.tone.client.source.zookeeper.ZookeeperResourceLoader;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.Ordered;
import org.springframework.core.PriorityOrdered;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.env.PropertySource;
import org.springframework.core.env.PropertySources;
import org.springframework.core.env.PropertySourcesPropertyResolver;
import org.springframework.util.Assert;

import java.util.Map;

/**
 * Created by Menina on 2017/6/10.
 */
@Slf4j
public class TonePropertySourcesPlaceholderConfigurer extends PropertySourcesPlaceholderConfigurer implements PriorityOrdered{

    protected static final String TONE_PROPERTY_SOURCE = "TONE_PROPERTY_SOURCE";

    private TonePropertyConfiguration tonePropertyConfiguration;

    private MutablePropertySources propertySources;

    private PropertySources appliedPropertySources;

    private PropertySourceLoader propertySourceLoader;

    public TonePropertySourcesPlaceholderConfigurer(){
        super();
        super.setIgnoreUnresolvablePlaceholders(true);
        this.setPropertySourceLoader(this.springPropertySourceLoader());
        this.setTonePropertyConfiguration(TonePropertyConfiguration.getInstance());
    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        if(this.propertySources == null){
            this.propertySources = new MutablePropertySources();
        }

        Map<String, String> propertySource = this.propertySourceLoader.loadResource(this.tonePropertyConfiguration.makePaths().toArray(new String[]{}));
        this.propertySources.addFirst(new PropertySource<Map<String, String>>(TONE_PROPERTY_SOURCE, propertySource){
            @Override
            public Object getProperty(String name) {
                return this.getSource().get(name);
            }
        });

        processProperties(beanFactory, new PropertySourcesPropertyResolver(this.propertySources));
        this.appliedPropertySources = this.propertySources;
    }

    @Override
    public int getOrder() {
        return Ordered.LOWEST_PRECEDENCE;
    }

    public PropertySources getAppliedPropertySources() throws IllegalStateException {
        Assert.state(this.appliedPropertySources != null, "PropertySources have not get been applied");
        return this.appliedPropertySources;
    }

    public void setTonePropertyConfiguration(TonePropertyConfiguration tonePropertyConfiguration) {
        this.tonePropertyConfiguration = tonePropertyConfiguration;
    }

    public void setPropertySourceLoader(PropertySourceLoader propertySourceLoader) {
        this.propertySourceLoader = propertySourceLoader;
    }

    private PropertySourceLoader springPropertySourceLoader() {
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

    private PropertySourceLoader initDefaultPropertySourceLoader() {
        SpringPropertySourceLoader propertySourceLoader = new SpringPropertySourceLoader();
        ZookeeperResourceLoader zookeeperResourceLoader = new ZookeeperResourceLoader(TonePropertyConfiguration.getInstance().getAddress());
        propertySourceLoader.setResourceLoader(zookeeperResourceLoader);
        return propertySourceLoader;
    }

}
