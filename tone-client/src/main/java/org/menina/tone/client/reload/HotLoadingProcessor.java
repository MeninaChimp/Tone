package org.menina.tone.client.reload;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.util.Set;

/**
 * Created by Menina on 2017/6/10.
 */
@Slf4j
@Component
public class HotLoadingProcessor implements HotLoading, ApplicationContextAware{

    private ApplicationContext act;

    @Override
    public void reload(String propertyName, String newValue) {
        Set<String> beanNames = ConfiguredBeansHolder.getConfiguredBeans(propertyName);
        for(String beanName : beanNames){
            Object bean = act.getBean(beanName);
            String oldValue;
            if(bean != null){
                try{
                    BeanWrapper beanWrapper = new BeanWrapperImpl(bean);
                    oldValue = beanWrapper.getPropertyValue(propertyName).toString();
                    beanWrapper.setPropertyValue(propertyName, newValue);
                    log.info(String.format("update property: %s success, oldValue: %s, new Value: %s"), propertyName, oldValue, newValue);
                }catch (Throwable t){
                    log.error(String.format("Failed to hot-reload property: %s value to newValue: %s, %s", propertyName, newValue, t.getMessage()), t);
                }
            }
        }
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.act = applicationContext;
    }
}
