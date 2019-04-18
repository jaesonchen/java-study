package com.asiainfo.lambda;

import java.util.NoSuchElementException;
import java.util.Optional;

/**
 * Optional: 这是一个可以为null的容器对象。如果值存在则isPresent()方法会返回true，调用get()方法会返回该对象。
 * of/ofNullable/get/isPresent/ifPresent/orElse/filter/map
 * 
 * @author       zq
 * @date         2018年4月12日  下午3:14:37
 * Copyright: 	  北京亚信智慧数据科技有限公司
 */
public class OpetionalExample {

    public static void main(String[] args) {
        
        // of 为非null的值创建一个Optional，参数为null，抛出NullPointerException。
        Optional<String> name = Optional.of("Sanaulla");
        
        // ofNullable 为指定的值创建一个Optional，如果指定的值为null，则返回一个空的Optional。
        Optional<String> empty = Optional.ofNullable(null);
        
        // get 如果Optional有值则将其返回，否则抛出NoSuchElementException。
        System.out.println(name.get());
        try {
            empty.get();
        } catch (NoSuchElementException ex) {
            ex.printStackTrace();
        }
        
        // isPresent() 如果值存在返回true，否则返回false。
        System.out.println(name.isPresent());
        System.out.println(empty.isPresent());

        // ifPresent(Comsumer) 如果Optional实例有值则为其调用consumer，否则不做处理。
        name.ifPresent(System.out::println);
        empty.ifPresent(System.out::println);
        
        // orElse 如果有值则将其返回，否则返回指定的其它值。
        System.out.println(empty.orElse("There is no value present!"));
        
        // orElseThrow 如果有值则将其返回，否则抛出supplier接口创建的异常。
        try {
            empty.orElseThrow(NoSuchElementException::new);
        } catch (NoSuchElementException ex) {
            ex.printStackTrace();
        }
        
        // map 如果有值，则对其执行调用mapping函数得到返回值。如果返回值不为null，则创建包含mapping返回值的Optional作为map方法返回值，否则返回空Optional。
        Optional<String> upperName = name.map((value) -> value.toUpperCase());
        System.out.println(upperName.orElse("No value found"));
        upperName = empty.map((value) -> value.toUpperCase());
        System.out.println(upperName.orElse("No value found"));
        
        // filter 如果有值并且满足断言条件返回包含该值的Optional，否则返回空Optional。
        Optional<String> lenth = name.filter((value) -> value.length() > 6);
        System.out.println(lenth.orElse("The name is less than 6 characters"));
        lenth = empty.filter((value) -> value.length() > 6);
        System.out.println(lenth.orElse("The name is less than 6 characters"));
    }
}
