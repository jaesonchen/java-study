package com.asiainfo.util;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;

/**
 * spring properties读取工具类，通常在xml里替代PropertyPlaceholderConfigurer
 * 
 * @author       zq
 * @date         2017年1月17日  下午1:49:09
 * Copyright: 	  北京亚信智慧数据科技有限公司
 */
public class ContextPropertyManager extends PropertyPlaceholderConfigurer {

	private static Map<String, Object> ctxPropertiesMap;
	
	@Override
	protected void processProperties(ConfigurableListableBeanFactory beanFactoryToProcess, Properties props)
			throws BeansException {

        super.processProperties(beanFactoryToProcess, props);
        // load properties to ctxPropertiesMap
        ctxPropertiesMap = new HashMap<String, Object>();
        for (Object key : props.keySet()) {
            String keyStr = key.toString();
            String value = props.getProperty(keyStr);
            ctxPropertiesMap.put(keyStr, value);
        }
    }

    // static method for accessing context properties
    public static Object getContextProperty(String name) {
        return ctxPropertiesMap.get(name);
    }
    
    @SuppressWarnings("unchecked")
	public static <T> T getContextProperty(String name, Class<T> requireType) {
        return (T) ctxPropertiesMap.get(name);
    }
}