package org.menina.tone.client.hotload;


import lombok.extern.slf4j.Slf4j;
import org.menina.tone.client.support.ToneSpringBeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

import java.util.Map;
import java.util.Set;

/**
 * Created by Menina on 2017/6/10.
 */
@Slf4j
public class HotLoadingProcessor implements HotLoading{

    @Override
    public void reload(String key, String value) {
        Map<String, Set<String>> propertyAndBeanNameMap = RefreshBeanHolder.get(key);
        for(String propertyName : propertyAndBeanNameMap.keySet()){
            for(String beanName : propertyAndBeanNameMap.get(propertyName)){
                Object bean = ToneSpringBeanUtils.getBean(beanName);
                if(bean != null){
                    try{
                        BeanWrapper beanWrapper = new BeanWrapperImpl(bean);
                        beanWrapper.setPropertyValue(propertyName, value);
                        log.info(String.format("HotLoading success, { %s, %s --> %s }", beanName, key, value));
                    }catch (Throwable t){
                        log.error(String.format("Failed to hotLoading property, name: %s, newValue: %s, %s", propertyName, value, t.getMessage()), t);
                    }
                }
            }
        }
    }
}
