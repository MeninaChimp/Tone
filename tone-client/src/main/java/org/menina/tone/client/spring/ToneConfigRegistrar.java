package org.menina.tone.client.spring;

import lombok.extern.slf4j.Slf4j;
import org.menina.tone.client.hotload.RefreshBeanPostProcessor;
import org.menina.tone.client.listener.ToneListener;
import org.menina.tone.client.support.BeanRegistrationUtil;
import org.menina.tone.client.support.ToneSpringBeanUtils;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.type.AnnotationMetadata;

/**
 * Created by Menina on 2017/6/10.
 */
@Slf4j
public class ToneConfigRegistrar implements ImportBeanDefinitionRegistrar {

    protected static final String DEFAULT_LISTENER = "defaultListener";
    protected static final String REFRESH_BEAN_PPSTPROCESSOR = "RefreshBeanPostProcessor";
    protected static final String TONE_PROPERTY_SOURCES_PLACEHOLDER_CONFIGURER = "tonePropertySourcesPlaceholderConfigurer";
    protected static final String TONE_SPRING_BEAN_UTILS = "toneSpringBeanUtils";

    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
        BeanRegistrationUtil.registerBeanDefinitionIfNotExists(registry, DEFAULT_LISTENER, ToneListener.class);
        BeanRegistrationUtil.registerBeanDefinitionIfNotExists(registry, TONE_PROPERTY_SOURCES_PLACEHOLDER_CONFIGURER, TonePropertySourcesPlaceholderConfigurer.class);
        BeanRegistrationUtil.registerBeanDefinitionIfNotExists(registry, REFRESH_BEAN_PPSTPROCESSOR, RefreshBeanPostProcessor.class);
        BeanRegistrationUtil.registerBeanDefinitionIfNotExists(registry, TONE_SPRING_BEAN_UTILS, ToneSpringBeanUtils.class);
    }
}
