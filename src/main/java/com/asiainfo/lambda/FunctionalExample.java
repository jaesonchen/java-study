package com.asiainfo.lambda;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * Function: compose/andThen 接受接受一个参数 同时提供一个返回结果
 * 
 * Predicate: and/or/negate 相当于是Function的特殊形式 返回结果是boolean类型
 * 
 * Consumer: andThen 处理参数不提供返回结果的函数式接口
 * 
 * 
 * @author       zq
 * @date         2018年4月12日  下午3:14:50
 * Copyright: 	  北京亚信智慧数据科技有限公司
 */
public class FunctionalExample {

    // 接受一个参数，同时提供一个返回结果
    public static <E, R> Collection<R> replaceAll(Collection<E> col, Function<E, R> fun) {
        
        Collection<R> list = new ArrayList<>();
        for (E e : col) {
            list.add(fun.apply(e));
        }
        @SuppressWarnings("unchecked")
        Collection<R> result = (Collection<R>) col;
        result.clear();
        result.addAll(list);
        return result;
    }
    
    // 相当于是Function的特殊形式，返回结果是boolean类型
    public static <E> Collection<E> filter(Collection<E> col, Predicate<? super E> predicate) {
        
        Iterator<E> it = col.iterator();
        while (it.hasNext()) {
            E e = it.next();
            if (!predicate.test(e)) {
                it.remove();
            }
        }
        return col;
    }
    
    // 处理参数不提供返回结果的函数式接口
    public static <E> void forEach(Collection<E> col, Consumer<? super E> action) {
        for (E e : col) {
            action.accept(e);
        }
    }
    
    public static void main(String[] args) {
        
        // Function
        System.out.println(replaceAll(new ArrayList<Integer>(Arrays.asList(new Integer[] {1, 2, 3, 4, 5})), 
                i -> "i=" + String.valueOf(i)));
        Function<Integer, String> function = i -> "=" + String.valueOf(i);
        System.out.println(replaceAll(new ArrayList<Integer>(Arrays.asList(new Integer[] {1, 2, 3, 4, 5})), 
                function.andThen(i -> "j" + i)));
        
        // Predicate
        System.out.println(filter(new ArrayList<Integer>(Arrays.asList(new Integer[] {1, 2, 3, 4, 5})), i -> i > 4));
        Predicate<Integer> predicate = i -> i > 4;
        System.out.println(filter(new ArrayList<Integer>(Arrays.asList(new Integer[] {1, 2, 3, 4, 5})), 
                predicate.or(i -> i < 3)));
        
        // Consumer
        forEach(new ArrayList<Integer>(Arrays.asList(new Integer[] {1, 2, 3, 4, 5})), System.out::println);
        forEach(new ArrayList<Integer>(Arrays.asList(new Integer[] {1, 2, 3, 4, 5})), i -> System.out.println("i=" + i));
    }
}
