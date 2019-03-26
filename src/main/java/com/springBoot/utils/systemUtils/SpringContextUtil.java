package com.springBoot.utils.systemUtils;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * @author chenkuan
 * @version v1.0
 * @desc 根据bean的class来查找所有的对象(包括子类)
 * @date 2019/3/1 001 17:01
 */
@Component
@SuppressWarnings({"rawtypes", "unchecked"})
public class SpringContextUtil implements ApplicationContextAware {

	private static ApplicationContext applicationContext;

	@Override
	public void setApplicationContext(ApplicationContext app) throws BeansException {
		applicationContext = app;
	}

	/**
	 * 获取applicationContext对象
	 */
	public static ApplicationContext getApplicationContext() {
		return applicationContext;
	}

	/**
	 * 获取所有BeanNames
	 */
	public static List<String> getAllBeanNames() {
		return Arrays.asList(applicationContext.getBeanDefinitionNames());
	}

	/**
	 * 根据bean的id来查找对象
	 */
	public static Object getBeanById(String id) {
		return applicationContext.getBean(id);
	}

	/**
	 * 根据beanName来查找对象
	 */
	public static Object getBean(String beanName) {
		return applicationContext.getBean(beanName);
	}

	/**
	 * 根据bean的class来查找对象
	 */
	public static Object getBean(Class c) {
		return applicationContext.getBean(c);
	}

	/**
	 * 根据bean的class来查找所有的对象(包括子类)
	 */
	public static <T> Map<String, T> getBeansByClass(Class c) {
		return applicationContext.getBeansOfType(c);
	}
}
