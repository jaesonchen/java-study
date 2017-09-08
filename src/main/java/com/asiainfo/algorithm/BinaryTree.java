package com.asiainfo.algorithm;

@SuppressWarnings("unused")
public class BinaryTree { 
	private BinaryTreeNode root;
	private Strategy strategy;
	
	public BinaryTree() { this(null, null); }
	public BinaryTree(BinaryTreeNode root, Strategy strategy) {
		this.root = root;
		this.strategy = strategy;
	}
	
	//先序遍历二叉树
	public Iterator preOrder() {
		LinkedList list = new LinkedListDLNode();
		preOrderRecursion(this.root, list);
		//preOrderTraverse (root,list);
		return list.elements();
	}
	//先序遍历的递归算法
	private void preOrderRecursion(BinaryTreeNode rt, LinkedList list) {
		if (rt == null) return; //递归基,空树直接返回
		list.insertLast(rt); //访问根结点
		preOrderRecursion(rt.getLChild(), list); //遍历左子树
		preOrderRecursion(rt.getRChild(), list); //遍历右子树
	}
	//先序遍历的非递归算法
	private void preOrderTraverse(BinaryTreeNode rt, LinkedList list) {
		if (rt == null) return;
		BinaryTreeNode p = rt;
		Stack s = new StackLinked();
		while (p != null) {
			while (p != null) { //向左走到尽头
				list.insertLast(p);	//当前遍历节点
				if (p.hasRChild())
					s.push(p.getRChild()); //右子树根结点入栈
				p = p.getLChild();
			}
			if (!s.isEmpty()) p = (BinaryTreeNode) s.pop(); //右子树根退栈遍历右子树
		}
	}
	//中序遍历二叉树
	public Iterator inOrder() {
		LinkedList list = new LinkedListDLNode();
		inOrderTraverse(this.root, list);
		return list.elements();
	}
	//中序遍历的非递归算法
	private void inOrderTraverse(BinaryTreeNode rt, LinkedList list) {
		if (rt == null) return;
		BinaryTreeNode p = rt;
		Stack s = new StackLinked();
		while (p != null || !s.isEmpty()) {
			while (p != null) { //一直向左走
				s.push(p); //将根结点入栈
				p = p.getLChild();
			}
			if (!s.isEmpty()) {
				p = (BinaryTreeNode) s.pop();//取出栈顶根结点访问之
				list.insertLast(p);
				p = p.getRChild(); //转向根的右子树进行遍历
			}//if
		}//out while
	}
	//后序遍历二叉树
	public Iterator postOrder() {
		LinkedList list = new LinkedListDLNode();
		postOrderTraverse (this.root,list);
		return list.elements();
	}
	//后序遍历的非递归算法
	private void postOrderTraverse(BinaryTreeNode rt, LinkedList list) {
		if (rt == null) return;
		BinaryTreeNode p = rt;
		Stack s = new StackLinked();
		while(p != null || !s.isEmpty()) {
			while (p != null){ //先左后右不断深入
				s.push(p); //将根节点入栈
				if (p.hasLChild()) 
					p = p.getLChild();
				else 
					p = p.getRChild();
			}
			if (!s.isEmpty()) {
				p = (BinaryTreeNode) s.pop(); //取出栈顶根结点访问之
				list.insertLast(p);
			}
			//满足条件时，说明栈顶根节点右子树已访问，应出栈访问之
			while (!s.isEmpty() && ((BinaryTreeNode) s.peek()).getRChild() == p) {
				p = (BinaryTreeNode)s.pop();
				list.insertLast(p);
			}
			//转向栈顶根结点的右子树继续后序遍历
			if (!s.isEmpty()) 
				p = ((BinaryTreeNode) s.peek()).getRChild();
			else 
				p = null;
		}
	}
	//按层遍历二叉树
	public Iterator levelOrder() {
		LinkedList list = new LinkedListDLNode();
		levelOrderTraverse(this.root, list);
		return list.elements();
	}
	//使用队列完成二叉树的按层遍历
	private void levelOrderTraverse(BinaryTreeNode rt, LinkedList list) {
		if (rt == null) return;
		Queue q = new QueueArray();
		q.enqueue(rt); //根结点入队
		while (!q.isEmpty()) {
			BinaryTreeNode p = (BinaryTreeNode) q.dequeue(); //取出队首结点p 并访问
			list.insertLast(p);
			if (p.hasLChild()) 
				q.enqueue(p.getLChild());//将p 的非空左右孩子依次入队
			if (p.hasRChild()) 
				q.enqueue(p.getRChild());
		}
	}
	//在树中查找元素e，返回其所在结点
	public BinaryTreeNode find(Object e) {
		return searchElement(this.root, e);
	}
	//递归查找元素e
	private BinaryTreeNode searchElement(BinaryTreeNode rt, Object e) {
		if (rt == null) 
			return null;
		if (strategy.equal(rt.getData(), e)) 
			return rt; //如果是根结点，返回根
		BinaryTreeNode result = searchElement(rt.getLChild(), e); //否则在左子树中找
		if (result == null)
			result = searchElement(rt.getRChild(), e); //没找到，在右子树中找
		return result;
	}
	private Node binaryTreeSearch(BinaryTreeNode rt, Object ele) {
		if (rt == null) return null;
		switch(strategy.compare(ele, rt.getData())) {
			case 0: return rt; //等于
			case -1: return binaryTreeSearch(rt.getLChild(), ele); //小于
			default: return binaryTreeSearch(rt.getRChild(), ele); //大于
		}
	}
	//在二叉查找树中插入ele
	public void insert(Object ele) {
		BinaryTreeNode p = null;
		BinaryTreeNode current = this.root;
		while (current != null) { //找到待插入位置
			p = current;
			if (strategy.compare(ele, current.getData()) < 0)
				current = current.getLChild();
			else
				current = current.getRChild();
		}
		if (p == null)
			root = new BinaryTreeNode(ele); //树为空
		else if (strategy.compare(ele, p.getData()) < 0)
			p.setLChild(new BinaryTreeNode(ele));
		else
			p.setRChild(new BinaryTreeNode(ele));
	}
	//在v 为根的二叉查找树中最小元素的位置
	public Node max(BinaryTreeNode v) {
		if (v != null)
			while (v.hasRChild()) 
				v = v.getRChild();
		return v;
	}
	//在v 为根的二叉查找树中最大元素的位置
	public Node min(BinaryTreeNode v){
		if (v != null)
			while (v.hasLChild()) 
				v = v.getLChild();
		return v;
	}
	//返回v 在中序遍历序列中的后续结点
	private BinaryTreeNode getSuccessor(BinaryTreeNode v){
		if (v == null) 
			return null;
		if (v.hasRChild()) 
			return (BinaryTreeNode) min(v.getRChild());
		while (v.isRChild()) 
			v = v.getParent();
		return v.getParent();
	}
	private BinaryTreeNode getPredecessor(BinaryTreeNode v){
		if (v == null) return null;
		if (v.hasLChild()) return (BinaryTreeNode) max(v.getLChild());
		while (v.isLChild()) v = v.getParent();
		return v.getParent();
	}
	//在二叉查找树中删除ele
	public Object remove(Object ele){
		BinaryTreeNode result = (BinaryTreeNode) binaryTreeSearch(this.root, ele);
		if (result == null) return null; //查找失败
		BinaryTreeNode del = null; //待删结点
		BinaryTreeNode subT = null; //待删结点的子树
		if (!result.hasLChild() || !result.hasRChild()) //确定待删结点
			del = result;
		else {
			del = getPredecessor(result);
			Object old = result.getData();
			result.setData(del.getData());
			del.setData(old);
		}
		//startBN = del.getParent(); //待平衡出发点 ＊
		//此时待删结点只有左子树或右子树
		if (del.hasLChild())
			subT = del.getLChild();
		else
			subT = del.getRChild();
		if (del == root) { //若待删结点为根
			if (subT != null) subT.sever();
				root = subT;
		} else {
			if (subT != null) {
				//del为非叶子结点
				if (del.isLChild()) 
					del.getParent().setLChild(subT);
				else 
					del.getParent().setRChild(subT);
			} else//del为叶子结点
				del.sever();
		}
		return del.getData();
	}
}
