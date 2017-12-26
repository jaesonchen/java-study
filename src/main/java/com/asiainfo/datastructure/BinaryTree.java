package com.asiainfo.datastructure;

import java.util.Comparator;

/**
 * 二分数
 * 
 * @author       zq
 * @date         2017年12月25日  下午4:43:17
 * Copyright: 	  北京亚信智慧数据科技有限公司
 */
public class BinaryTree<T> {
    
	private BinaryTreeNode<T> root;
	
	public BinaryTree() {
	    this(null);
	}
	public BinaryTree(BinaryTreeNode<T> root) {
		this.root = root;
	}
	
	//先序遍历二叉树
	public MyIterator<BinaryTreeNode<T>> preOrder() {
		
	    List<BinaryTreeNode<T>> list = new LinkedList<>();
		preOrderRecursion(this.root, list);
		//preOrderTraverse (root,list);
		return list.iterator();
	}
	//先序遍历的递归算法
	protected void preOrderRecursion(BinaryTreeNode<T> current, List<BinaryTreeNode<T>> list) {
	    
		if (current == null) {
		    return;
		}
		list.add(current);
		preOrderRecursion(current.getLeft(), list);
		preOrderRecursion(current.getRight(), list);
	}
	//先序遍历的非递归算法
	protected void preOrderTraverse(BinaryTreeNode<T> current, List<BinaryTreeNode<T>> list) {
	    
		if (current == null) {
		    return;
		}
		BinaryTreeNode<T> node = current;
		Stack<BinaryTreeNode<T>> stack = new ArrayStack<>();
		while (node != null || !stack.isEmpty()) {
			while (node != null) {
				list.add(node);
				if (node.hasRight()) {
				    stack.push(node.getRight());
				}
				node = node.getLeft();
			}
			if (!stack.isEmpty()) {
			    node = stack.pop();
			}
		}
	}
	//中序遍历二叉树
	public MyIterator<BinaryTreeNode<T>> inOrder() {
	    
	    List<BinaryTreeNode<T>> list = new LinkedList<>();
		inOrderTraverse(this.root, list);
		return list.iterator();
	}
	//中序遍历的非递归算法
	private void inOrderTraverse(BinaryTreeNode<T> current, List<BinaryTreeNode<T>> list) {
	    
	    if (current == null) {
            return;
        }
	    BinaryTreeNode<T> node = current;
        Stack<BinaryTreeNode<T>> stack = new ArrayStack<>();
        while (node != null || !stack.isEmpty()) {
            while (node != null) {
                stack.push(node);
                node = node.getLeft();
            }
            if (!stack.isEmpty()) {
                node = stack.pop();
                list.add(node);
                node = node.getRight();
            }
        }
	}
	//后序遍历二叉树
	public MyIterator<BinaryTreeNode<T>> postOrder() {

	    List<BinaryTreeNode<T>> list = new LinkedList<>();
		postOrderTraverse (this.root,list);
		return list.iterator();
	}
	//后序遍历的非递归算法
	private void postOrderTraverse(BinaryTreeNode<T> current, List<BinaryTreeNode<T>> list) {
	    
	    if (current == null) {
            return;
        }
        BinaryTreeNode<T> node = current;
        Stack<BinaryTreeNode<T>> stack = new ArrayStack<>();
        while (node != null || !stack.isEmpty()) {
            while (node != null) {
                stack.push(node);
                node = node.hasLeft() ? node.getLeft() : node.getRight();
            }
            if (!stack.isEmpty()) {
                node = stack.pop();
                list.add(node);
            }
            while (!stack.isEmpty() && ((BinaryTreeNode<T>) stack.peek()).getRight() == node) {
                node = stack.pop();
                list.add(node);
            }
            node = stack.isEmpty() ? null : stack.peek().getRight();
        }
	}
	//按层遍历二叉树
	public MyIterator<BinaryTreeNode<T>> levelOrder() {

	    List<BinaryTreeNode<T>> list = new LinkedList<>();
		levelOrderTraverse(this.root, list);
		return list.iterator();
	}
	//使用队列完成二叉树的按层遍历
	private void levelOrderTraverse(BinaryTreeNode<T> current, List<BinaryTreeNode<T>> list) {

        if (current == null) {
            return;
        }
        Queue<BinaryTreeNode<T>> queue = new LinkedQueue<>();
        queue.offer(current);
		while (!queue.isEmpty()) {
			BinaryTreeNode<T> node = queue.poll();
			list.add(node);
			if (node.hasLeft()) {
			    queue.offer(node.getLeft());
			}
			if (node.hasRight()) {
			    queue.offer(node.getRight());
			}
		}
	}
	//在树中查找元素e，返回其所在结点
	public BinaryTreeNode<T> find(T element) {
		return searchElement(this.root, element);
	}
	//递归查找元素e
	private BinaryTreeNode<T> searchElement(BinaryTreeNode<T> current, T element) {
	    
		if (current == null) {
			return null;
		}
		if (null == element && null == current.getData() 
		        || element.equals(current.getData())) {
		    return current;
		}
		BinaryTreeNode<T> result = searchElement(current.getLeft(), element);
		if (result == null) {
			result = searchElement(current.getRight(), element);
		}
		return result;
	}
	//二叉树查找
	private Node<T> binaryTreeSearch(BinaryTreeNode<T> current, T element, Comparator<T> comparator) {
	    
		if (current == null)  {
		    return null;
		}
		switch(comparator.compare(element, current.getData())) {
			case 0 : return current;
			case -1: return binaryTreeSearch(current.getLeft(), element, comparator);
			default: return binaryTreeSearch(current.getRight(), element, comparator);
		}
	}
	//在二叉查找树中插入ele
	public void insert(T element, Comparator<T> comparator) {
	    
		BinaryTreeNode<T> node = null;
		BinaryTreeNode<T> current = this.root;
		while (current != null) {
		    node = current;
			if (comparator.compare(element, current.getData()) < 0) {
				current = current.getLeft();
			} else {
			    current = current.getRight();
			}
		}
		BinaryTreeNode<T> newNode = new BinaryTreeNode<T>(element);
		if (node == null) {
			this.root = newNode;
		} else if (comparator.compare(element, node.getData()) < 0) {
		    node.setLeft(newNode);
		} else {
		    node.setRight(newNode);
		}
		newNode.updateHeight();
	}
	//在v 为根的二叉查找树中最小元素的位置
	public Node<T> max(BinaryTreeNode<T> element) {
	    
	    BinaryTreeNode<T> node = element;
		if (node != null) {
			while (node.hasRight()) {
			    node = node.getRight();
			}
		}
		return node;
	}
	//在v 为根的二叉查找树中最大元素的位置
	public Node<T> min(BinaryTreeNode<T> element) {

        BinaryTreeNode<T> node = element;
        if (node != null) {
            while (node.hasLeft()) {
                node = node.getLeft();
            }
        }
        return node;
	}
	//返回v 在中序遍历序列中的后续结点
	protected BinaryTreeNode<T> getSuccessor(BinaryTreeNode<T> element) {
	    
		if (element == null) {
		    return null;
		}
		if (element.hasRight()) {
		    return (BinaryTreeNode<T>) min(element.getRight());
		}
		BinaryTreeNode<T> node = element;
		while (node.isRight()) {
		    node = node.getRight();
		}
		return node.parent();
	}
	protected BinaryTreeNode<T> getPredecessor(BinaryTreeNode<T> element) {
	    
		if (element == null) {
		    return null;
		}
		if (element.hasLeft()) {
		    return (BinaryTreeNode<T>) max(element.getLeft());
		}
		BinaryTreeNode<T> node = element;
		while (node.isLeft()) {
		    node = node.parent();
		}
		return node.parent();
	}
	//在二叉查找树中删除ele
	public T remove(T element) {
	    
	    BinaryTreeNode<T> result = (BinaryTreeNode<T>) binaryTreeSearch(this.root, element, (Comparator<T>) null);
		if (result == null) {
		    return null;
		}
		BinaryTreeNode<T> del = null;
		BinaryTreeNode<T> subTree = null;
		if (!result.hasLeft() || !result.hasRight()) {
			del = result;
		} else {
			del = getPredecessor(result);
			T old = result.getData();
			result.setData(del.getData());
			del.setData(old);
		}
		if (del.hasLeft()) {
		    subTree = del.getLeft();
		} else {
		    subTree = del.getRight();
		}
		if (del == root) {
			if (subTree != null) {
			    subTree.breakOff();
			}
			root = subTree;
		} else {
			if (subTree != null) {
				if (del.isLeft()) {
					del.parent().setLeft(subTree);
				} else {
					del.parent().setRight(subTree);
				}
			} else {
				del.breakOff();
			}
		}
		return del.getData();
	}
}
