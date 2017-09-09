package org.menina.tone.client.spring;

import org.menina.tone.client.properties.TonePropertyConfiguration;
import org.menina.tone.client.support.Constant;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.Ordered;
import org.springframework.core.PriorityOrdered;
import org.springframework.core.env.*;
import org.springframework.util.Assert;

import java.util.Map;

/**
 * Created by Menina on 2017/6/10.
 */
public class TonePropertySourcesPlaceholderConfigurer extends PropertySourcesPlaceholderConfigurer implements PriorityOrdered{

    private TonePropertyConfiguration tonePropertyConfiguration;

    private MutablePropertySources propertySources;

    private PropertySources appliedPropertySources;

    private PropertySourceLoader propertySourceLoader;

    private boolean allowOverride;

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        if(this.propertySources == null){
            this.propertySources = new MutablePropertySources();
        }

        Map<String, String> propertySource = this.propertySourceLoader.loadResource(this.tonePropertyConfiguration.makePaths().toArray(new String[]{}));
        this.propertySources.addFirst(new PropertySource<Map<String, String>>(Constant.TONE_PROPERTY_SOURCE, propertySource){
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

    public void setAllowOverride(boolean allowOverride) {
        this.allowOverride = allowOverride;
    }
}
