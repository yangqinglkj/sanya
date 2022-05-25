package org.springblade.common.utils;

import org.apache.poi.ss.formula.functions.T;
import org.springblade.modules.backstage.service.TouristImageDataHandler;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class SpringContextUtil implements ApplicationContextAware {
	private static ApplicationContext applicationContext;

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		SpringContextUtil.applicationContext = applicationContext;
	}
	public static Map<String,?> getBeanListOfType(Class<?> clazz) {
		return applicationContext.getBeansOfType(clazz);
	}
}
