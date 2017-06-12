package org.menina.tone.client.reload;

import org.springframework.beans.BeansException;
import org.springframework.beans.PropertyValue;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.core.Ordered;
import org.springframework.core.PriorityOrdered;
import org.springframework.stereotype.Component;

import java.util.regex.Pattern;

/**
 * Created by Menina on 2017/6/10.
 */
@Component
public class RecordConfiguredBeansPostProcessor implements BeanFactoryPostProcessor, PriorityOrdered {

    private Pattern pattern = Pattern.compile("^\\$\\{.*\\}$");

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        String[] names = beanFactory.getBeanDefinitionNames();
        for(String name : names){
            BeanDefinition beanDefinition = beanFactory.getBeanDefinition(name);
            if(beanDefinition.getPropertyValues().size() != 0){
                for(PropertyValue propertyValue : beanDefinition.getPropertyValues().getPropertyValueList()){
                    String value = propertyValue.getValue().toString().trim();
                    if(pattern.matcher(value).find()){
                        ConfiguredBeansHolder.recordConfiguredBean(value.substring(2, value.length() - 1), propertyValue.getName(), name);
                    }
                }
            }
        }
    }

    @Override
    public int getOrder() {
        return Ordered.LOWEST_PRECEDENCE;
    }
}
