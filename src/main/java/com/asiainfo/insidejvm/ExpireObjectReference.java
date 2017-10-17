package com.asiainfo.insidejvm;

import java.util.Arrays;

/**
 * 
 * 消除过期的对象引用：
 * 
 * 所谓的过期引用是指永远也不会再被间接引用的引用。
 * 内存泄漏是很隐蔽的（称这类内存为  无意识的对象保持  更恰当）。
 * 如果一个对象引用被无意识的保留起来了，那么gc不仅不会处理这个对象，也不会处理被这个对象引用的其他对象。
 * 这类问题的修复很简单：只要清空这些引用就可以。
 * 清空过期引用的另一个好处是：如果它们以后又被错误地间接引用，程序就会抛出NullPointerException。
 * 
 * @author       zq
 * @date         2017年10月16日  下午4:35:35
 * Copyright: 	  北京亚信智慧数据科技有限公司
 */
public class ExpireObjectReference {

	public static void main(String[] args) {
		

	}

}

/**
 * 
 * @Description: TODO
 * 
 * @author       zq
 * @date         2017年10月16日  下午4:35:45
 * Copyright: 	  北京亚信智慧数据科技有限公司
 */
class Stack<T> {
	private Object[] elements;
	private int size = 0;
	private static final int DEFAULT_INITIAL_CAPACITY = 16;
	
	public Stack() {

		this.elements = new Object[DEFAULT_INITIAL_CAPACITY];
	}
	
	public void push(T e) {
		this.ensureCapacity();
		this.elements[size++] = e;
	}
	
	@SuppressWarnings("unchecked")
	public T pop() {
		if (this.size == 0) {
			throw new RuntimeException("Stack is null, can not pop a element...");
		}
		//这里会发生内存泄漏，elements[size]对象引用还存在，不会被gc回收
		//return (T) this.elements[--size];
		T result = (T) this.elements[--size];
		//eliminate obsolete reference
		this.elements[this.size] = null;
		return result;
	}
	
	public boolean isEmpty() {
		return this.size == 0;
	}
	
	private void ensureCapacity() {
		if (this.size == this.elements.length) {
			elements = Arrays.copyOf(this.elements, this.size * 2);
		}
	}
}