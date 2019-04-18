package com.asiainfo.datastructure;

/**
 * Set 接口
 * 
 * @author       zq
 * @date         2017年12月26日  上午11:21:36
 * Copyright: 	  北京亚信智慧数据科技有限公司
 */
public interface Set<E> extends Iterable<E> {

    int size();
    boolean isEmpty();
    boolean contains(Object o);
    MyIterator<E> iterator();
    E[] toArray();
    boolean add(E e);
    boolean addAll(Iterable<? extends E> it);
    boolean remove(Object o);
    void clear();
}
