package com.asiainfo.asserts;

import static org.springframework.util.Assert.*;

import java.io.BufferedReader;
import java.io.Reader;
import java.util.Arrays;

/**
 * Spring Assert throw IllegalArgumentException、IllegalStateException
 * 
 * @author       zq
 * @date         2018年3月30日  下午2:41:22
 * Copyright: 	  北京亚信智慧数据科技有限公司
 */
public class SpringAssertsExample {

    public static void main(String[] args) {
        
        // void isTrue(boolean expression, String message)
        // void isNull(Object object, String message)
        // void notNull(Object object, String message)
        isNull(null, "object not null");
        
        // 不为null且字符串长度大于0
        // void hasLength(String text, String message)
        hasLength("str", "length <= 0");
        // 至少拥有一个不是空格的字符
        // void hasText(String text, String message)
        hasText(" str", "must has non-space char");
        
        // 数组不为本身不为null，且至少拥有一个长度
        // void notEmpty(Object[] array, String message)
        // 数组所有元素都不为null，array本身为null是不抛出异常
        // void noNullElements(Object[] array, String message)
        noNullElements(null, "Null Element");
        
        // 集合不为null且至少拥有一个元素
        // void notEmpty(Collection<?> collection, String message)
        // void notEmpty(Map<?, ?> map, String message)
        notEmpty(Arrays.asList(new Object[] {new Object()}), "must has at least 1 element");
        
        // void isInstanceOf(Class<?> type, Object obj, String message)
        isInstanceOf(Object.class, "str", "not string");
        // 判断是否是子类型
        // void isAssignable(Class<?> superType, Class<?> subType, String message)
        isAssignable(Reader.class, BufferedReader.class, "not Reader");
        // 等同于Class的isAssignableFrom
        isTrue(Reader.class.isAssignableFrom(BufferedReader.class), "not Reader");
        
        // 表达式是否为true，抛出IllegalStateException
        // void state(boolean expression, String message)
        state(1 == 2, "1 != 2");
    }
}
