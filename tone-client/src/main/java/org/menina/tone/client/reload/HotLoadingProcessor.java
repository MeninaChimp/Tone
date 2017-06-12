package org.menina.tone.client.reload;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Set;

/**
 * Created by Menina on 2017/6/10.
 */
@Slf4j
@Component
public class HotLoadingProcessor implements HotLoading, ApplicationContextAware{

    private static ApplicationContext act;

    @Override
    public void reload(String node, String newValue) {
        Map<String, Set<String>> propertyAndBeanNameMap = ConfiguredBeansHolder.getConfiguredBeans(node);
        for(String propertyName : propertyAndBeanNameMap.keySet()){
            for(String beanName : propertyAndBeanNameMap.get(propertyName)){
                Object bean = act.getBean(beanName);
                String oldValue;
                if(bean != null){
                    try{
                        BeanWrapper beanWrapper = new BeanWrapperImpl(bean);
                        oldValue = beanWrapper.getPropertyValue(propertyName).toString();
                        beanWrapper.setPropertyValue(propertyName, newValue);
                        log.info(String.format("Update property success, property: %s, { %s --> %s }", node, oldValue, newValue));
                    }catch (Throwable t){
                        log.error(String.format("Failed to hotLoading property, name: %s, newValue: %s, %s", propertyName, newValue, t.getMessage()), t);
                    }
                }
            }
        }
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        act = applicationContext;
    }
}
