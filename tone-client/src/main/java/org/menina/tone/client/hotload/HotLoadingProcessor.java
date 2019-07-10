package org.menina.tone.client.hotload;


import lombok.extern.slf4j.Slf4j;
import org.menina.tone.client.support.ToneSpringBeanUtils;
import org.springframework.aop.framework.Advised;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

import java.util.Map;
import java.util.Set;

/**
 * Created by Menina on 2017/6/10.
 */
@Slf4j
public class HotLoadingProcessor implements HotLoading {

    @Override
    public void reload(String key, String value) {
        Map<String, Set<String>> propertyAndBeanNameMap = RefreshBeanHolder.get(key);
        if (propertyAndBeanNameMap == null) {
            log.info(String.format("No bean is listening the change of config key \'%s\', need @Refresh(%s) above the field and bean should inject into Spring.", key, key));
            return;
        }

        for (String propertyName : propertyAndBeanNameMap.keySet()) {
            for (String beanName : propertyAndBeanNameMap.get(propertyName)) {
                try {
                    Object bean = this.getTarget(ToneSpringBeanUtils.getBean(beanName));
                    BeanWrapper beanWrapper = new BeanWrapperImpl(bean);
                    beanWrapper.setPropertyValue(propertyName, value);
                    log.info(String.format("HotLoading success, { %s, %s --> %s }", beanName, key, value));
                } catch (Throwable t) {
                    log.error(String.format("Failed to hotLoading property, name: %s, newValue: %s, %s", propertyName, value, t.getMessage()), t);
                }
            }
        }
    }

    @SuppressWarnings({"unchecked"})
    private Object getTarget(Object proxy) throws Exception {
        if (AopUtils.isJdkDynamicProxy(proxy)) {
            return ((Advised) proxy).getTargetSource().getTarget();
        } else {
            // expected to be cglib proxy then, which is simply a specialized class
            return proxy;
        }
    }
}
