package com.asiainfo.algorithm;

public class ListLinked implements List {
	private Strategy strategy; //数据元素比较策略
	private SLNode head; //单链表首结点引用
	private int size; //线性表中数据元素的个数
	private static class DefaultStrategy implements Strategy {
		public boolean equal(Object obj1, Object obj2) {
			return true;
		}
		public int compare(Object obj1, Object obj2) {
			return 0;
		}
	}
	public ListLinked () {
		this(new DefaultStrategy());
	}
	public ListLinked (Strategy strategy) {
		this.strategy = strategy;
		head = new SLNode();
		size = 0;
	}
	//辅助方法：获取数据元素e 所在结点的前驱结点
	private SLNode getPreNode(Object e){
		SLNode p = head;
		while (p.getNext() != null)
			if (strategy.equal(p.getNext().getData(), e))
				return p;
			else
				p = p.getNext();
		return null;
	}
	//辅助方法：获取序号为0<=i<size 的元素所在结点的前驱结点
	private SLNode getPreNode(int i){
		SLNode p = head;
		for ( ; i > 0; i--)
			p = p.getNext();
		return p;
	}
	//获取序号为0<=i<size 的元素所在结点
	private SLNode getNode(int i){
		SLNode p = head.getNext();
		for ( ; i > 0; i--)
			p = p.getNext();
		return p;
	}
	//返回线性表的大小，即数据元素的个数。
	public int getSize() {
		return size;
	}
	//如果线性表为空返回true，否则返回false。
	public boolean isEmpty() {
		return size == 0;
	}
	//判断线性表是否包含数据元素e
	public boolean contains(Object e) {
		SLNode p = head.getNext();
		while (p != null)
		if (strategy.equal(p.getData(), e))
			return true;
		else
			p = p.getNext();
		return false;
	}
	//返回数据元素e 在线性表中的序号
	public int indexOf(Object e) {
		SLNode p = head.getNext();
		int index = 0;
		while (p != null)
			if (strategy.equal(p.getData(), e))
				return index;
			else {
				index++;
				p = p.getNext();
			}
		return -1;
	}
	//将数据元素e 插入到线性表中i 号位置
	public void insert(int i, Object e) throws OutOfBoundaryException {
		if (i < 0 || i > size)
			throw new OutOfBoundaryException("插入序号越界");
		SLNode p = getPreNode(i);
		SLNode q = new SLNode(e, p.getNext());
		p.setNext(q);
		size++;
		return;
	}
	//将数据元素e 插入到元素obj 之前
	public boolean insertBefore(Object obj, Object e) {
		SLNode p = getPreNode(obj);
		if (p != null) {
			SLNode q = new SLNode(e, p.getNext());
			p.setNext(q);
			size++;
			return true;
		}
		return false;
	}
	//将数据元素e 插入到元素obj 之后
	public boolean insertAfter(Object obj, Object e) {
		SLNode p = head.getNext();
		while (p != null)
			if (strategy.equal(p.getData(), obj)){
				SLNode q = new SLNode(e, p.getNext());
				p.setNext(q);
				size++;
				return true;
			} else {
				p = p.getNext();
			}
		return false;
	}
	//删除线性表中序号为i 的元素,并返回之
	public Object remove(int i) throws OutOfBoundaryException {
		if (i < 0 || i >= size)
			throw new OutOfBoundaryException("删除序号越界");
		SLNode p = getPreNode(i);
		Object obj = p.getNext().getData();
		p.setNext(p.getNext().getNext());
		size--;
		return obj;
	}
	//删除线性表中第一个与e 相同的元素
	public boolean remove(Object e) {
		SLNode p = getPreNode(e);
		if (p != null){
			p.setNext(p.getNext().getNext());
			size--;
			return true;
		}
		return false;
	}
	//替换线性表中序号为i 的数据元素为e，返回原数据元素
	public Object replace(int i, Object e)
			throws OutOfBoundaryException {
		if (i < 0 || i >= size)
			throw new OutOfBoundaryException("序号越界");
		SLNode p = getNode(i);
		Object obj = p.getData();
		p.setData(e);
		return obj;
	}
	//返回线性表中序号为i 的数据元素
	public Object get(int i) throws OutOfBoundaryException {
		if (i < 0 || i >= size)
			throw new OutOfBoundaryException("序号越界");
		SLNode p = getNode(i);
		return p.getData();
	}
}