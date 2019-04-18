package com.asiainfo.datastructure;

/**
 * 双向节点
 * 
 * @author       zq
 * @date         2017年12月25日  上午10:41:01
 * Copyright: 	  北京亚信智慧数据科技有限公司
 */
public class DLNode<T> implements Node<T> {
    
	private T element;
	private DLNode<T> prev;
	private DLNode<T> next;
	public DLNode() {
		this(null, null, null);
	}
	public DLNode(T element) {
        this(element, null, null);
    }
	public DLNode(T element, DLNode<T> prev, DLNode<T> next) {
		this.element = element;
		this.prev = prev;
		this.next = next;
	}
	public DLNode<T> getNext() {
		return next;
	}
	public void setNext(DLNode<T> next) {
		this.next = next;
	}
	public DLNode<T> getPrev(){
		return prev;
	}
	public void setPrev(DLNode<T> prev) {
		this.prev = prev;
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
