package com.asiainfo.datastructure;

/**
 * 单向节点
 * 
 * @author       zq
 * @date         2017年12月25日  上午10:40:46
 * Copyright: 	  北京亚信智慧数据科技有限公司
 */
public class SLNode<T> implements Node<T> {
    
	private T element;
	private SLNode<T> next;
	
	public SLNode() {
		this(null, null);
	}
	public SLNode(T element) {
        this(element, null);
    }
	public SLNode(T element, SLNode<T> next) {
		this.element = element;
		this.next = next;
	}
	
	public SLNode<T> getNext() {
		return next;
	}
	public void setNext(SLNode<T> next) {
		this.next = next;
	}
	
	@Override
	public T getData() {
		return element;
	}
	@Override
	public void setData(T data) {
		element = data;
	}
}
