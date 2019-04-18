package com.asiainfo.designpattern.architecture.abstractdocument;

import java.util.Map;
import java.util.function.Function;
import java.util.stream.Stream;

/**
 * 抽象文档模式是一种面向对象结构设计模式。模式中采用key-value形式存储对象的属性，且确保类型不相关，暴露类型相关的属性数据。
 * 模式的意图是为强类型语言构建高灵活性的组件管理，保证新的属性可以自由的添加到对象中，且不丢失类型安全。模式使用trait接口，将不同的属性划分到不同的接口中。
 * 当遍历构建document树时，使用者需要为下一级的实现类指定构造器。实现类通常是一些实现了document接口的实现类的集合，从而使这些对象能够自己处理属性的获取和设置。
 * 
 * 适用范围: 迫切的需要添加新属性、更灵活的处理树形结构、你想要更松散的耦合系统
 * 
 * @author       zq
 * @date         2018年4月25日  下午6:25:14
 * Copyright: 	  北京亚信智慧数据科技有限公司
 */
public interface Document {

    /**
     * Puts the value related to the key
     * 
     * @param key
     * @param value
     * @return
     */
    Object put(String key, Object value);
    
    /**
     * Gets the value for the key
     * 
     * @param key
     * @return
     */
    Object get(String key);
    
    /**
     * Gets the stream of child documents
     * 
     * @param key
     * @param constructor   constructor of child class
     * @return
     */
    <T> Stream<T> children(String key, Function<Map<String, Object>, T> constructor);
}
