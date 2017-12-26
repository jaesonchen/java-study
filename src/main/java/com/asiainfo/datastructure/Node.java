package com.asiainfo.datastructure;

/**
 * 
 * 
 * @author       zq
 * @date         2017年12月25日  上午10:36:43
 * Copyright: 	  北京亚信智慧数据科技有限公司
 */
public interface Node<T> {
    
	/**
	 * 获取结点数据域
	 * 
	 * @return
	 */
	T getData();
	
	/**
	 * 设置结点数据域
	 * 
	 * @param data
	 */
	void setData(T data);
}
