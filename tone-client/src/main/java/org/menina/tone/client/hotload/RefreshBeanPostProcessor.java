package org.menina.tone.client.hotload;

import org.menina.tone.client.annotation.Refresh;
import org.menina.tone.client.listener.Listener;
import org.menina.tone.client.listener.ListenerChainFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import static org.springframework.beans.BeanUtils.findPropertyForMethod;

/**
 * Created by Menina on 2017/9/8.
 */
@Component
public class RefreshBeanPostProcessor implements BeanPostProcessor {

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        if (bean instanceof Listener) {
            ListenerChainFactory.ListenerHolder.register((Listener) bean);
        }

        Field[] fields = bean.getClass().getDeclaredFields();
        if (fields != null) {
            for (Field field : fields) {
                Refresh annotation = field.getAnnotation(Refresh.class);
                if (annotation == null) {
                    continue;
                }

                if(null == BeanUtils.getPropertyDescriptor(bean.getClass(), field.getName())){
                    throw new RuntimeException(String.format("The field \'%s\' of bean \'%s\' is not writable or has an invalid setter method, need a setter method and the method's modifier must be public.", field.getName(), beanName));
                }

                RefreshBeanHolder.register(annotation.value(), field.getName(), beanName);
            }
        }

        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }
}
