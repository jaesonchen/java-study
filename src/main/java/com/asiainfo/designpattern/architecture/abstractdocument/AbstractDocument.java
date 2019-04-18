package com.asiainfo.designpattern.architecture.abstractdocument;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Stream;

/**
 * 抽象实现，具体的类型和子类型都必须继承该类
 * 
 * @author       zq
 * @date         2018年4月25日  下午6:35:25
 * Copyright: 	  北京亚信智慧数据科技有限公司
 */
public class AbstractDocument implements Document {

    private final Map<String, Object> properties;
    
    protected AbstractDocument(Map<String, Object> properties) {
        Objects.requireNonNull(properties, "properties map is required");
        this.properties = properties;
    }
    
    /* 
     * TODO
     * @param key
     * @param value
     * @return
     * @see com.asiainfo.designpattern.architecture.abstractdocument.Document#put(java.lang.String, java.lang.Object)
     */
    @Override
    public Object put(String key, Object value) {
        properties.put(key, value);
        return value;
    }

    /* 
     * TODO
     * @param key
     * @return
     * @see com.asiainfo.designpattern.architecture.abstractdocument.Document#get(java.lang.String)
     */
    @Override
    public Object get(String key) {
        return properties.get(key);
    }

    /* 
     * TODO
     * @param key
     * @param constructor
     * @return
     * @see com.asiainfo.designpattern.architecture.abstractdocument.Document#children(java.lang.String, java.util.function.Function)
     */
    @Override
    public <T> Stream<T> children(String key, Function<Map<String, Object>, T> constructor) {

        @SuppressWarnings("unchecked")
        Optional<List<Map<String, Object>>> any = Stream.of(get(key))
                .filter(el -> el != null)
                .map(el -> (List<Map<String, Object>>) el)
                .findAny();
        return any.isPresent() ? any.get().stream().map(constructor) : Stream.empty();
    }
    
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append(getClass().getName()).append("[");
        properties.entrySet()
            .forEach(e -> builder.append("[").append(e.getKey()).append(" : ").append(e.getValue()).append("]"));
        builder.append("]");
        return builder.toString();
    }
}
