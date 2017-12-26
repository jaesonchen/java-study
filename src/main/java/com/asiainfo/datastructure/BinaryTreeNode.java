package com.asiainfo.datastructure;

/**
 * 二分数节点
 * 
 * @author       zq
 * @date         2017年12月25日  下午5:15:04
 * Copyright: 	  北京亚信智慧数据科技有限公司
 */
public class BinaryTreeNode<T> implements Node<T> {
    
	private T data;                    //数据域
	private BinaryTreeNode<T> parent;  //父结点
	private BinaryTreeNode<T> left;    //左孩子
	private BinaryTreeNode<T> right;   //右孩子
	private int height;                //以该结点为根的子树的高度
	private int size;                  //该结点子孙数（包括结点本身）

	public BinaryTreeNode() {
	    this(null);
	}
	public BinaryTreeNode(T data) {
	    height = 1;
	    size = 1;
		this.data = data;
	}
	
	@Override
	public T getData() {
	    return data;
	}
	@Override
	public void setData(T data) {
	    this.data = data;
	}

	//判断是否有父亲
	public boolean hasParent() {
	    return parent != null;
	}
	//判断是否有左孩子
	public boolean hasLeft() {
	    return left != null;
	}
	//判断是否有右孩子
	public boolean hasRight() {
	    return right != null;
	}
	//判断是否为叶子结点
	public boolean isLeaf() {
	    return !hasLeft() && !hasRight();
	}
	//判断是否为某结点的左孩子
	public boolean isLeft() {
	    return hasParent() && this == parent.left;
	}
	//判断是否为某结点的右孩子
	public boolean isRight() {
	    return hasParent() && this == parent.right;
	}
	//取结点的高度,即以该结点为根的树的高度
	public int height() {
	    return height;
	}
	//更新当前结点及其祖先的高度
	public void updateHeight() {
	    
		int newHeight = 0;
		newHeight = hasLeft() ? Math.max(newHeight, 1 + left.height()) : newHeight;
		newHeight = hasRight() ? Math.max(newHeight, 1 + right.height()) : newHeight;
		//高度没有发生变化则直接返回
		if (newHeight == height) {
		    return;
		}
		height = newHeight;
		if (hasParent()) {
		    parent.updateHeight();
		}
	}

	//取以该结点为根的树的结点数
	public int size() {
	    return size;
	}
	//更新当前结点及其祖先的子孙数
	public void updateSize() {
	    
		size = 1;
		size += hasLeft() ? left.size() : 0;
		size += hasRight() ? right.size() : 0;
		if (hasParent()) {
		    parent().updateSize();
		}
	}

	//取父结点
	public BinaryTreeNode<T> parent() {
	    return parent;
	}
	//断开与父亲的关系
	public void breakOff() {
	    
		if (!hasParent()) {
		    return;
		}
		if (isLeft()) {
		    parent.left = null;
		} else {
		    parent.right = null;
		}
		parent.updateHeight();    //更新父结点及其祖先高度
		parent.updateSize();      //更新父结点及其祖先规模
		parent = null;
	}

	//取左孩子
	public BinaryTreeNode<T> getLeft() {
	    return left;
	}
	//设置当前结点的左孩子,返回原左孩子
	public BinaryTreeNode<T> setLeft(BinaryTreeNode<T> left) {
	    
		BinaryTreeNode<T> old = this.left;
		if (left != null) {
			left.breakOff();    //断开与其父结点的关系
		}
        this.left = left;
        left.parent = this;
        this.updateHeight();    //更新当前结点及其祖先高度
        this.updateSize();      //更新当前结点及其祖先规模
		return old;
	}

	//取右孩子
	public BinaryTreeNode<T> getRight() {
	    return right;
	}
	//设置当前结点的右孩子,返回原右孩子
	public BinaryTreeNode<T> setRight(BinaryTreeNode<T> right) {
	    
		BinaryTreeNode<T> old = this.right;
		if (right != null) {
		    right.breakOff();      //断开当前右孩子与结点的关系
		}
		this.right = right;
		right.parent = this;
	    this.updateHeight();       //更新当前结点及其祖先高度
	    this.updateSize();         //更新当前结点及其祖先规模
		return old;
	}
}
