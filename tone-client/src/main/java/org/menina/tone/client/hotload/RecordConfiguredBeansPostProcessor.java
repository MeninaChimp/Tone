package org.menina.tone.client.hotload;

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
 *
 * Deprecated since 1.1.0
 * 通过记录配置占位符的beanDefinition(通过properties，xml创建的beanDefinition)，在配置变更时，刷新bean的值。
 * 但对于从properties中读取占位符的bean不能跟踪。
 * 不能做到无感知，那么就只能由使用者告诉Tone那么属性需要热加载。
 */
//@Component
@Deprecated
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
                        RefreshBeanHolder.register(value.substring(2, value.length() - 1), propertyValue.getName(), name);
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
