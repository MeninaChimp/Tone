package org.menina.tone.client.support;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import javax.annotation.PostConstruct;

/**
 * Created by Menina on 2017/9/9.
 */
public class ToneSpringBeanUtils implements ApplicationContextAware {

	private static ToneSpringBeanUtils toneSpringBeanUtils;
	private ApplicationContext act;

	private ToneSpringBeanUtils(){}
	
	@PostConstruct
	public void init(){
		toneSpringBeanUtils = this;
		toneSpringBeanUtils.act = this.act;
	}

	public static Object getBean(String beanName){
		return toneSpringBeanUtils.act.getBean(beanName);
	}
	
	public static <T> T getBean(Class<T> clazz){
		return toneSpringBeanUtils.act.getBean(clazz);
	}
	
	public static <T> T getBean(Class<T> clazz, String beanName){
		return toneSpringBeanUtils.act.getBean(beanName, clazz);
	}

	public static <T> T getBean(Class<T> clazz, boolean ignoreNotFound){
		T bean = null;
		try {
			bean = toneSpringBeanUtils.act.getBean(clazz);
		}catch (NoSuchBeanDefinitionException e){
			if(ignoreNotFound){
				return bean;
			}

			throw e;
		}

		return bean;
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.act = applicationContext;
	}
}
