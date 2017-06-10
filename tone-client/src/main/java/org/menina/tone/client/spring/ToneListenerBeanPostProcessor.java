package org.menina.tone.client.spring;

import org.menina.tone.client.listener.Listener;
import org.menina.tone.client.listener.ListenerHolder;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;

/**
 * Created by Menina on 2017/6/10.
 */
@Component
public class ToneListenerBeanPostProcessor implements BeanPostProcessor{
    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        if(bean instanceof Listener){
            ListenerHolder.register((Listener) bean);
        }

        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }
}
