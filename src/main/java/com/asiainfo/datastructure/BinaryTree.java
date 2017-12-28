package com.asiainfo.datastructure;

import java.util.Comparator;

/**
 * 二叉树
 * 
 * @author       zq
 * @date         2017年12月25日  下午4:43:17
 * Copyright: 	  北京亚信智慧数据科技有限公司
 */
public class BinaryTree<T extends Comparable<T>> {
    
    private Comparator<T> comparator;
	private BinaryTreeNode<T> root;
	
	public BinaryTree() {
	    this(null);
	}
	public BinaryTree(BinaryTreeNode<T> root) {
		this(root, null);
	}
	public BinaryTree(BinaryTreeNode<T> root, Comparator<T> comparator) {
        this.root = root;
        this.comparator = comparator;
    }
	
	public Comparator<T> getComparator() {
        return comparator;
    }
    public void setComparator(Comparator<T> comparator) {
        this.comparator = comparator;
    }
    public BinaryTreeNode<T> getRoot() {
        return root;
    }
    public void setRoot(BinaryTreeNode<T> root) {
        this.root = root;
    }
    
    /**
     * 先序遍历(中左右)
     * 
     * @return
     */
	public List<BinaryTreeNode<T>> preOrder() {
		
	    List<BinaryTreeNode<T>> list = new LinkedList<>();
		preOrderRecursion(this.root, list);
		//preOrderTraverse (root,list);
		return list;
	}
	/**
	 * 先序遍历的递归算法
	 * 
	 * @param node
	 * @param list
	 */
	protected void preOrderRecursion(BinaryTreeNode<T> node, List<BinaryTreeNode<T>> list) {
	    
		if (null == node) {
		    return;
		}
		list.add(node);
		preOrderRecursion(node.getLeft(), list);
		preOrderRecursion(node.getRight(), list);
	}
	/**
	 * 先序遍历的非递归算法
	 * 
	 * @param node
	 * @param list
	 */
	protected void preOrderTraverse(BinaryTreeNode<T> node, List<BinaryTreeNode<T>> list) {
	    
		if (null == node) {
		    return;
		}
		BinaryTreeNode<T> current = node;
		Stack<BinaryTreeNode<T>> stack = new ArrayStack<>();
		while (current != null || !stack.isEmpty()) {
			while (current != null) {
				list.add(current);
				if (current.getRight() != null) {
				    stack.push(current.getRight());
				}
				current = current.getLeft();
			}
			if (!stack.isEmpty()) {
			    current = stack.pop();
			}
		}
	}
	/**
	 * 中序遍历(左中右)
	 * 
	 * @return
	 */
	public List<BinaryTreeNode<T>> midOrder() {
	    
	    List<BinaryTreeNode<T>> list = new LinkedList<>();
	    midOrderTraverse(this.root, list);
		return list;
	}
	/**
	 * 中序遍历的非递归算法
	 * 
	 * @param node
	 * @param list
	 */
	protected void midOrderTraverse(BinaryTreeNode<T> node, List<BinaryTreeNode<T>> list) {
	    
	    if (null == node) {
            return;
        }
	    BinaryTreeNode<T> current = node;
        Stack<BinaryTreeNode<T>> stack = new ArrayStack<>();
        while (current != null || !stack.isEmpty()) {
            while (current != null) {
                stack.push(current);
                current = current.getLeft();
            }
            if (!stack.isEmpty()) {
                current = stack.pop();
                list.add(current);
                current = current.getRight();
            }
        }
	}
	/**
	 * 后序遍历(左右中)
	 * 
	 * @return
	 */
	public List<BinaryTreeNode<T>> postOrder() {

	    List<BinaryTreeNode<T>> list = new LinkedList<>();
		postOrderTraverse (this.root,list);
		return list;
	}
	/**
	 * 后序遍历的非递归算法
	 * 
	 * @param node
	 * @param list
	 */
	protected void postOrderTraverse(BinaryTreeNode<T> node, List<BinaryTreeNode<T>> list) {
	    
	    if (null == node) {
            return;
        }
        BinaryTreeNode<T> current = node;
        Stack<BinaryTreeNode<T>> stack = new ArrayStack<>();
        while (current != null || !stack.isEmpty()) {
            while (current != null) {
                stack.push(current);
                current = null != current.getLeft() ? current.getLeft() : current.getRight();
            }
            if (!stack.isEmpty()) {
                current = stack.pop();
                list.add(current);
            }
            while (!stack.isEmpty() && (stack.peek()).getRight() == current) {
                current = stack.pop();
                list.add(current);
            }
            current = stack.isEmpty() ? null : stack.peek().getRight();
        }
	}
	/**
	 * 按层遍历
	 * 
	 * @return
	 */
	public List<BinaryTreeNode<T>> levelOrder() {

	    List<BinaryTreeNode<T>> list = new LinkedList<>();
		levelOrderTraverse(this.root, list);
		return list;
	}
	/**
	 * 使用队列完成二叉树的按层遍历
	 * 
	 * @param node
	 * @param list
	 */
	protected void levelOrderTraverse(BinaryTreeNode<T> node, List<BinaryTreeNode<T>> list) {

        if (null == node) {
            return;
        }
        Queue<BinaryTreeNode<T>> queue = new LinkedQueue<>();
        queue.offer(node);
		while (!queue.isEmpty()) {
			BinaryTreeNode<T> current = queue.poll();
			list.add(current);
			if (null != current.getLeft()) {
			    queue.offer(current.getLeft());
			}
			if (null != current.getRight()) {
			    queue.offer(current.getRight());
			}
		}
	}
	/**
	 * 在树中查找元素，返回其所在结点
	 * 
	 * @param element
	 * @return
	 */
	public BinaryTreeNode<T> find(T element) {
		return searchElement(this.root, element);
	}
	/**
	 * 递归查找元素
	 * 
	 * @param node
	 * @param element
	 * @return
	 */
	protected BinaryTreeNode<T> searchElement(BinaryTreeNode<T> node, T element) {
	    
		if (null == node) {
			return null;
		}
		if (null == element && null == node.getData() 
		        || element.equals(node.getData())) {
		    return node;
		}
		BinaryTreeNode<T> result = searchElement(node.getLeft(), element);
		if (result == null) {
			result = searchElement(node.getRight(), element);
		}
		return result;
	}
	/**
	 * 二叉树查找
	 * 
	 * @param node
	 * @param element
	 * @param comparator
	 * @return
	 */
	protected BinaryTreeNode<T> binarySearch(BinaryTreeNode<T> node, T element) {
	    
		if (null == node)  {
		    return null;
		}
		//Comparator && Comparable
		int cmp = compare(element, node.getData());
    	if (cmp > 0) {
    		return binarySearch(node.getRight(), element);
    	} else if (cmp < 0) {
    		return binarySearch(node.getLeft(), element);
    	} else {
    		return node;
    	}
	}
	/**
	 * 节点值比较
	 * 
	 * @param element
	 * @param data
	 * @return
	 */
	protected int compare(T element, T data) {
	    return null != this.comparator ? comparator.compare(element, data) : element.compareTo(data);
	}
	/**
	 * 在二叉查找树中插入节点
	 * 
	 * @param element
	 * @param allowDup
	 * @return
	 */
	public BinaryTreeNode<T> insert(T element, boolean allowDup) {
	    
		BinaryTreeNode<T> parent = null;
		BinaryTreeNode<T> current = this.root;
		while (current != null) {
		    parent = current;
		    int cmp = compare(element, current.getData());
			if (cmp < 0) {
				current = current.getLeft();
			} else if (cmp > 0) {
			    current = current.getRight();
			} else if (allowDup) {
			    current = current.getRight();
			} else {
			    current.setData(element);
			    return null;
			}
		}
		BinaryTreeNode<T> newNode = new BinaryTreeNode<T>(element);
		newNode.setParent(parent);
		if (null == parent) {
			this.root = newNode;
		} else if (compare(element, parent.getData()) < 0) {
		    parent.setLeft(newNode);
		} else {
		    parent.setRight(newNode);
		}
		return newNode;
	}
	/**
	 * 删除节点
	 * 
	 * @param element
	 * @return
	 */
	public BinaryTreeNode<T> remove(T element) {
	    
	    BinaryTreeNode<T> node = find(element);
	    if (null != node) {
	        BinaryTreeNode<T> subRoot = removeNode(node);
	        if (node == root) {
	            subRoot.setParent(null);
	            root = subRoot;
	        } else if (node.isLeft()) {
	            subRoot.setParent(node.getParent());
	            node.getParent().setLeft(subRoot);
	        } else {
	            subRoot.setParent(node.getParent());
                node.getParent().setRight(subRoot);
	        }
	    }
	    return node;
	}
	/**
	 * 删除节点，返回重排序后的节点树
	 * 
	 * @param node
	 * @return
	 */
	protected BinaryTreeNode<T> removeNode(BinaryTreeNode<T> node) {
	    
	    if (null == node.getRight()) {
	        return node.getLeft();
	    }
	    if (null == node.getLeft()) {
	        return node.getRight();
	    }
	    List<BinaryTreeNode<T>> list = new ArrayList<>();
	    this.midOrderTraverse(node, list);
	    list.remove(node);
	    BinaryTree<T> tree = new BinaryTree<>(list.get(0));
	    for (int i = 1; i < list.size(); i++) {
	        tree.insert(list.get(i).getData(), true);
	    }
	    return tree.getRoot();
	}	
	/**
	 * 二叉查找树中最大节点
	 * 
	 * @param node
	 * @return
	 */
	protected BinaryTreeNode<T> max(BinaryTreeNode<T> node) {
	    
	    BinaryTreeNode<T> current = node;
		if (current != null) {
			while (current.getRight() != null) {
			    current = current.getRight();
			}
		}
		return current;
	}
	/**
	 * 二叉查找树中最小节点
	 * 
	 * @param node
	 * @return
	 */
	protected BinaryTreeNode<T> min(BinaryTreeNode<T> node) {

        BinaryTreeNode<T> current = node;
        if (current != null) {
            while (current.getLeft() != null) {
                current = current.getLeft();
            }
        }
        return current;
	}
	/**
	 * 在中序遍历序列中的后续结点
	 * 
	 * @param node
	 * @return
	 */
	public BinaryTreeNode<T> successor(BinaryTreeNode<T> node) {
	    
		if (null == node) {
		    return null;
		}
		if (null != node.getRight()) {
		    return min(node.getRight());
		}
		BinaryTreeNode<T> current = node;
		while (current.isRight()) {
		    current = current.getParent();
		}
		return current.getParent();
	}
	/**
	 * 在中序遍历序列中的前续结点
	 * 
	 * @param node
	 * @return
	 */
	public BinaryTreeNode<T> predecessor(BinaryTreeNode<T> node) {
	    
        if (null == node) {
            return null;
        }
		if (null != node.getLeft()) {
		    return max(node.getLeft());
		}
		BinaryTreeNode<T> current = node;
		while (current.isLeft()) {
		    current = current.getParent();
		}
		return current.getParent();
	}
}
