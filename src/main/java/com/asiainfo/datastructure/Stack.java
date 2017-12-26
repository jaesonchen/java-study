package com.asiainfo.datastructure;

/**
 * filo栈接口
 * 
 * @author       zq
 * @date         2017年12月25日  下午4:04:01
 * Copyright: 	  北京亚信智慧数据科技有限公司
 */
public interface Stack<E> {

	/**
	 * 返回堆栈的大小
	 * 
	 * @return
	 */
	int size();
	
	/**
	 * 判断堆栈是否为空
	 * 
	 * @return
	 */
	boolean isEmpty();
	
	/**
	 * 数据元素入栈 
	 * 
	 * @param element
	 */
	void push(E element);
	
	/**
	 * 栈顶元素出栈 
	 * 
	 * @return
	 */
	E pop();
	
	/**
	 * 取栈顶元素 
	 * 
	 * @return
	 */
	E peek();
	
	/**
	 * 清空
	 *
	 */
	void clear();
}
