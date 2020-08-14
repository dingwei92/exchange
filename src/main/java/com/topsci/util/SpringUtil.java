package com.topsci.util;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * Created by Dongguabai on 2018-06-22 14:44
 */
@Component
@Lazy(false)
public class SpringUtil implements ApplicationContextAware {

	private static ApplicationContext applicationContext;

	// 获取applicationContext
	public static ApplicationContext getApplicationContext() {
		return applicationContext;
	}

	// 通过name获取 Bean.
	public static <T> T  getBean(String name) {
		return (T)getApplicationContext().getBean(name);
	}

	// 通过class获取Bean.
	public static <T> T getBean(Class<T> clazz) {
		return getApplicationContext().getBean(clazz);
	}

	// 通过name,以及Clazz返回指定的Bean
	public static <T> T getBean(String name, Class<T> clazz) {
		return getApplicationContext().getBean(name, clazz);
	}
	// 通过name,以及Clazz返回指定的Bean
	public static <T> Map<String, T> getBeanOfType(Class<T> clazz) {
		return getApplicationContext().getBeansOfType(clazz);
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		if (SpringUtil.applicationContext == null) {
			SpringUtil.applicationContext = applicationContext;
		}
	}

	private SpringUtil() {
	}
}