package com.asiainfo.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * TODO
 * 
 * @author       zq
 * @date         2017年11月15日  下午4:33:59
 * Copyright: 	  北京亚信智慧数据科技有限公司
 */
public class CollectionUtil {

    /**
     * 集合是否为空
     * 
     * @param col
     * @return
     */
    public static boolean isEmpty(Collection<?> col) {
        return null == col || col.isEmpty();
    }
    
    /**
     * 不为空
     * 
     * @param col
     * @return
     */
    public static boolean isNotEmpty(Collection<?> col) {
        return !isEmpty(col);
    }
    
    /**
     * 集合是否为空
     * 
     * @param map
     * @return
     */
    public static boolean isEmpty(Map<?, ?> map) {
        return null == map || map.isEmpty();
    }
    
    /**
     * 不为空
     * 
     * @param map
     * @return
     */
    public static boolean isNotEmpty(Map<?, ?> map) {
        return !isEmpty(map);
    }
    
    /**
     * array转list
     * 
     * @param arr
     * @return
     */
    public static <T> List<T> arrayToList(T[] arr) {
        return null == arr ? new ArrayList<T>() : new ArrayList<T>(Arrays.asList(arr));
    }
    
    /**
     * list转array
     * 
     * @param list
     * @return
     */
    @SuppressWarnings("unchecked")
    public static <T> T[] listToArray(List<T> list) {
        
        T[] array = (T[]) new Object[null == list ? 0 : list.size()];
        return list.toArray(array);
    }
    
    /**
     * 获取Map属性值，忽略属性名称的大小写
     * 
     * @param map
     * @param propertyName
     * @param requiredType
     * @return
     */
    @SuppressWarnings("unchecked")
    public static <E> E getInsensitive(final Map<String, ? extends Object> map, final String propertyName, final Class<E> requiredType) {
        
        if (isEmpty(map) || StringUtil.isBlank(propertyName)) {
            return null;
        }
        for (Map.Entry<String, ? extends Object> entry : map.entrySet()) {
            if (StringUtil.isBlank(entry.getKey())) {
                continue;
            }
            if (entry.getKey().equalsIgnoreCase(propertyName)) {
                return (E) entry.getValue();
            }
        }
        return null;
    }
}
