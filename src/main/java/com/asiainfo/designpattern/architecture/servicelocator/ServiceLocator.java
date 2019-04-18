package com.asiainfo.designpattern.architecture.servicelocator;

/**
 * ServiceLocator 通常用于spring的bean查找
 * 
 * @author       zq
 * @date         2018年4月19日  下午5:05:28
 * Copyright: 	  北京亚信智慧数据科技有限公司
 */
public class ServiceLocator {

    private static AppContext context;
    public static void setAppContext(AppContext context) {
        ServiceLocator.context = context;
    }
    
    public static Object getBean(String name) {
        return context.getBean(name);
    }
    
    @SuppressWarnings("unchecked")
    public static <T> T getBean(String name, Class<T> requiredType) {
        return (T) context.getBean(name);
    }
    
    @SuppressWarnings("unchecked")
    public static <T> T getBean(Class<T> requiredType) {
        return (T) context.getBean(requiredType);
    }
}
