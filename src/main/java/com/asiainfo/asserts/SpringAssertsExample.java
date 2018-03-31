package com.asiainfo.asserts;

//import static org.springframework.util.Assert.*;

/**
 * Spring Assert throw IllegalArgumentException、IllegalStateException
 * 
 * @author       zq
 * @date         2018年3月30日  下午2:41:22
 * Copyright: 	  北京亚信智慧数据科技有限公司
 */
public class SpringAssertsExample {

    /** 
     * TODO
     * 
     * @param args
     */
    public static void main(String[] args) {
        
        // void isTrue(boolean expression, String message)
        // void isNull(Object object, String message)
        // void notNull(Object object, String message)
        
        // 不为null且字符串长度大于0
        // void hasLength(String text, String message)
        // 至少拥有一个不是空格的字符
        // void hasText(String text, String message)
        
        // 数组不为本身不为null，且至少拥有一个长度
        // void notEmpty(Object[] array, String message)
        // 数组所有元素都不为null，array本身为null是不抛出异常
        // void noNullElements(Object[] array, String message)
        
        // 集合不为null且至少拥有一个元素
        // void notEmpty(Collection<?> collection, String message)
        // void notEmpty(Map<?, ?> map, String message)
        
        // void isInstanceOf(Class<?> type, Object obj, String message)
        // 判断是否是子类型
        // void isAssignable(Class<?> superType, Class<?> subType)
        
        // 表达式是否为true，抛出IllegalStateException
        // void state(boolean expression, String message)
    }
}
