package com.asiainfo.util;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * TODO
 * 
 * @author       zq
 * @date         2017年11月15日  下午4:12:33
 * Copyright: 	  北京亚信智慧数据科技有限公司
 */
public class BeanUtil {
    
    static final Logger logger = LoggerFactory.getLogger(BeanUtil.class);

    /**
     * Bean转Map
     * 
     * @param obj
     * @return
     */
    public static Map<String, Object> bean2Map(Object obj) {
        
        if (null == obj) {
            return null;
        }

        Map<String, Object> map = new HashMap<>();
        try {
            BeanInfo bean = Introspector.getBeanInfo(obj.getClass());
            PropertyDescriptor[] propertyDescriptors = bean.getPropertyDescriptors();
            for (PropertyDescriptor property : propertyDescriptors) {
                // 过滤class属性
                if (!"class".equals(property.getName())) {
                    // property对应的getter方法
                    Method getter = property.getReadMethod();
                    Object value = getter.invoke(obj);
                    map.put(property.getName(), value);
                }
            }
        } catch (Exception ex) {
            logger.error("bean转map时发生异常!", ex);
        }
        return map;
    }

    /**
     * Map转换为Bean
     * 
     * @param map
     * @param object
     * @return
     */
    public static <T> T map2Bean(Map<String, ? extends Object> map, Class<T> clazz) {
        
        if (null == map) {
            return null;
        }
        
        try {
            T result = clazz.newInstance();
            BeanInfo bean = Introspector.getBeanInfo(clazz);
            PropertyDescriptor[] propertyDescriptors = bean.getPropertyDescriptors();
            for (PropertyDescriptor property : propertyDescriptors) {
                if (map.containsKey(property.getName())) {
                    Object value = map.get(property.getName());
                    // property对应的setter方法
                    Method setter = property.getWriteMethod();
                    setter.invoke(result, value);
                }
            }
            return result;
        } catch (InstantiationException e) {
            logger.error("实例化对象时发生异常！", e);
        } catch (IntrospectionException e) {
            logger.error("获得bean属性时发生异常！", e);
        } catch (InvocationTargetException e) {
            logger.error("setter bean属性时发生异常！", e);
        } catch (IllegalAccessException e) {
            logger.error("非法访问异常！", e);
        }
        return null;
    }
    
    /**
     * Map转换为Bean
     * 
     * @param map
     * @param obj
     * @return
     */
    public static Object map2Bean(Map<String, ? extends Object> map, Object obj) {
        
        if (null == map || null == obj) {
            return obj;
        }
        
        try {
            BeanInfo bean = Introspector.getBeanInfo(obj.getClass());
            PropertyDescriptor[] propertyDescriptors = bean.getPropertyDescriptors();
            for (PropertyDescriptor property : propertyDescriptors) {
                if (map.containsKey(property.getName())) {
                    Object value = map.get(property.getName());
                    // property对应的setter方法
                    Method setter = property.getWriteMethod();
                    setter.invoke(obj, value);
                }
            }
        } catch (IntrospectionException e) {
            logger.error("获得bean属性时发生异常！", e);
        } catch (InvocationTargetException e) {
            logger.error("setter bean属性时发生异常！", e);
        } catch (IllegalAccessException e) {
            logger.error("非法访问异常！", e);
        }
        return obj;
    }
}
