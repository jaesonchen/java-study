package com.asiainfo.designpattern.architecture.servicelocator;

import java.util.HashMap;
import java.util.Map;

/**
 * TODO
 * 
 * @author       zq
 * @date         2018年4月19日  下午4:52:31
 * Copyright: 	  北京亚信智慧数据科技有限公司
 */
public class AppContext {

    private Map<String, Object> nameMap = new HashMap<>();
    private Map<Class<?>, Object> clazzMap = new HashMap<>();
    
    // add
    public void addBean(Object obj) {
        if (null == obj) {
            throw new NullPointerException();
        }
        Class<?> clazz = obj.getClass();
        nameMap.put(clazz.getSimpleName().toLowerCase(), obj);
        clazzMap.put(clazz, obj);
    }
    
    // byName
    public Object getBean(String name) {
        if (null == name) {
            throw new NullPointerException();
        }
        for (Map.Entry<String, Object> entry : nameMap.entrySet()) {
            if (name.toLowerCase().equals(entry.getKey().toLowerCase())) {
                return entry.getValue();
            }
        }
        return null;
    }
    
    // byType
    public Object getBean(Class<?> clazz) {
        if (null == clazz) {
            throw new NullPointerException();
        }
        for (Map.Entry<Class<?>, Object> entry : clazzMap.entrySet()) {
            if (clazz.isAssignableFrom(entry.getKey())) {
                return entry.getValue();
            }
        }
        return null;
    }
}
