package com.asiainfo.datastructure;

/**
 * fifo队列接口
 * 
 * @author       zq
 * @date         2017年12月25日  下午4:27:41
 * Copyright: 	  北京亚信智慧数据科技有限公司
 */
public interface Queue<E> {
    
	/**
	 * 返回队列的大小
	 * 
	 * @return
	 */
	int size();
	
	/**
	 * 判断队列是否为空
	 * 
	 * @return
	 */
	boolean isEmpty();
	
	/**
	 * 数据元素入队
	 * 
	 * @param element
	 */
	void offer(E element);
	
	/**
	 * 队首元素出队
	 * 
	 * @return
	 */
	E poll();
	
	/**
	 * 取队首元素
	 * 
	 * @return
	 */
	E peek();
}
