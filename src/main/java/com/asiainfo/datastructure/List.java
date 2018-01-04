package com.asiainfo.datastructure;

/**
 * list接口
 * 
 * @author       zq
 * @date         2017年12月25日  上午10:11:52
 * Copyright: 	  北京亚信智慧数据科技有限公司
 */
public interface List<E> extends Iterable<E> {
    
	/**
	 * 返回线性表的大小，即数据元素的个数。
	 * 
	 * @return
	 */
	int size();
	
	/**
	 * 线性表为空返回true，否则返回false。
	 * 
	 * @return
	 */
	boolean isEmpty();
	
	/**
	 * 判断线性表是否包含数据元素
	 * 
	 * @param obj
	 * @return
	 */
	boolean contains(Object obj);
	
	/**
	 * 返回数据元素在线性表中的坐标，不存在返回-1
	 * 
	 * @param obj
	 * @return
	 */
	int indexOf(Object obj);
	
	/**
	 * 将数据元素插入到线性表中index 位置
	 * 
	 * @param index
	 * @param element
	 * @throws OutOfBoundaryException
	 */
	void add(int index, E element);
	
	/**
	 * 添加多个
	 * 
	 * @param it
	 */
	void addAll(Iterable<? extends E> it);

    /**
     * 添加元素
     * 
     * @param obj
     */
    boolean add(E element);
    
    /**
     * 返回线性表中序号为index 的数据元素
     * 
     * @param index
     * @return
     * @throws OutOfBoundaryException
     */
    E get(int index);
	
	/**
	 * 删除线性表中序号为index 的元素, 并返回该元素
	 * 
	 * @param index
	 * @return
	 */
	E remove(int index);
	
	/**
	 * 删除线性表中第一个与obj相同的元素
	 * 
	 * @param e
	 * @return
	 */
	boolean remove(Object obj);
	
	/**
	 * 清除列表
	 *
	 */
	void clear();
	
    /**
     * 返回迭代器
     * 
     * @return
     */
    MyIterator<E> iterator();
    
    /**
     * 返回数组
     * 
     * @return
     */
    E[] toArray();
}

