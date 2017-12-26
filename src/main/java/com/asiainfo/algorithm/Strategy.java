package com.asiainfo.algorithm;

public interface Strategy<T> {

	/**
	* 比较两个数据元素的大小
	* 如果t1 < t2 返回-1
	* 如果t1 = t2 返回0
	* 如果t1 > t2 返回1
	*/
	int compare(T t1, T t2);
}