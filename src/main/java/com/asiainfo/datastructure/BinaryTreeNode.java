package com.asiainfo.datastructure;

/**
 * 二叉树节点
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

	public BinaryTreeNode(T data) {
		this(data, null);
	}
    public BinaryTreeNode(T data, BinaryTreeNode<T> parent) {
        this.data = data;
        this.parent = parent;
    }
    
	@Override
	public T getData() {
	    return data;
	}
	@Override
	public void setData(T data) {
	    this.data = data;
	}
	/**
	 * 左节点
	 * 
	 * @return
	 */
	public boolean isLeft() {
	    return null != parent && parent.left == this;
	}
	/**
	 * 右节点
	 * 
	 * @return
	 */
	public boolean isRight() {
	    return null != parent && parent.right == this;
	}
	/**
	 * 叶子节点
	 * 
	 * @return
	 */
	public boolean isLeaf() {
	    return null == right && null == left;
	}
	/**
	 * 完整子节点
	 * 
	 * @return
	 */
	public boolean fullChild() {
	    return null != right && null != left;
	}
	
    public BinaryTreeNode<T> getParent() {
        return parent;
    }
    public void setParent(BinaryTreeNode<T> parent) {
        this.parent = parent;
    }
    public BinaryTreeNode<T> getLeft() {
        return left;
    }
    public void setLeft(BinaryTreeNode<T> left) {
        this.left = left;
    }
    public BinaryTreeNode<T> getRight() {
        return right;
    }
    public void setRight(BinaryTreeNode<T> right) {
        this.right = right;
    }
    @Override
    public String toString() {
        return "BinaryTreeNode [data=" + data + ", parent=" + parent + ", left=" + left + ", right=" + right + "]";
    }
}
