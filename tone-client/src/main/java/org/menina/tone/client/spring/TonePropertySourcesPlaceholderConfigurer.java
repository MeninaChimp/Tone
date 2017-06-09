package org.menina.tone.client.spring;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanInitializationException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.Ordered;
import org.springframework.core.PriorityOrdered;
import org.springframework.core.env.*;
import org.springframework.util.Assert;

import java.io.IOException;

/**
 * Created by Menina on 2017/6/10.
 */
public class TonePropertySourcesPlaceholderConfigurer extends PropertySourcesPlaceholderConfigurer implements PriorityOrdered{

    private MutablePropertySources propertySources;

    private PropertySources appliedPropertySources;

    private Environment environment;

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        if (this.propertySources == null) {
            this.propertySources = new MutablePropertySources();
            if (this.environment != null) {
                this.propertySources.addLast(
                        new PropertySource<Environment>(ENVIRONMENT_PROPERTIES_PROPERTY_SOURCE_NAME, this.environment) {
                            @Override
                            public String getProperty(String key) {
                                return this.source.getProperty(key);
                            }
                        }
                );
            }
            try {
                PropertySource<?> localPropertySource =
                        new PropertiesPropertySource(LOCAL_PROPERTIES_PROPERTY_SOURCE_NAME, mergeProperties());
                if (this.localOverride) {
                    this.propertySources.addFirst(localPropertySource);
                }
                else {
                    this.propertySources.addLast(localPropertySource);
                }
            }
            catch (IOException ex) {
                throw new BeanInitializationException("Could not load properties", ex);
            }
        }

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
}
