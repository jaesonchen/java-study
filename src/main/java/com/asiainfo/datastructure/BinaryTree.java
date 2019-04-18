package com.asiainfo.datastructure;

/**
 * 二叉树是每个节点最多有两个子树的树结构。
 * 
 * 二叉查找树(Binary Search Tree)，又被称为二叉搜索树。
 * 它是特殊的二叉树：对于二叉树，假设x为二叉树中的任意一个结点，x节点包含关键字key，节点x的key值记为key[x]。
 * 如果y是x的左子树中的一个结点，则key[y] <= key[x]；
 * 如果y是x的右子树的一个结点，则key[y] >= key[x]。
 * 
 * 在二叉查找树中：
 * (1) 若任意节点的左子树不空，则左子树上所有结点的值均小于它的根结点的值；
 * (2) 任意节点的右子树不空，则右子树上所有结点的值均大于它的根结点的值；
 * (3) 任意节点的左、右子树也分别为二叉查找树。
 * (4) 没有键值相等的节点（no duplicate nodes）。
 * 
 * 
 * @author       zq
 * @date         2017年12月25日  下午4:43:17
 * Copyright: 	  北京亚信智慧数据科技有限公司
 */
public class BinaryTree<T extends Comparable<T>> {
    
	private BinaryTreeNode<T> root;
	
	public BinaryTree() {
	    this(null);
	}
	public BinaryTree(BinaryTreeNode<T> root) {
	    this.root = root;
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
                current = (null != current.getLeft()) ? current.getLeft() : current.getRight();
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
	 * 非递归查找元素
	 * 
	 * @param node
	 * @param element
	 * @return
	 */
	protected BinaryTreeNode<T> searchElement(BinaryTreeNode<T> node, T element) {
	    
	    BinaryTreeNode<T> current = node;
	    while (null != current) {
	        // Comparable
	        int cmp = compare(element, current.getData());
	        if (cmp > 0) {
	            current = current.getRight();
	        } else if (cmp < 0) {
	            current = current.getLeft();
	        } else {
	            return current;
	        }
	    }
		return null;
	}
	
	/**
	 * 比较两个元素大小，默认null较小
	 * 
	 * @param e1
	 * @param e2
	 * @return
	 */
	protected int compare(T e1, T e2) {
	    if (e1 == null) {
	        return e1 == e2 ? 0 : -1;
	    }
	    return e1.compareTo(e2);
	}
	/**
	 * 二叉树查找递归算法
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
		// Comparable
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
	 * 在二叉查找树中插入节点
	 * 
	 * @param element
	 * @param allowDup
	 * @return
	 */
	public BinaryTreeNode<T> insert(T element) {
	    
	    if (this.root == null) {
	        this.root = new BinaryTreeNode<T>(element);
	        return this.root;
	    }
		BinaryTreeNode<T> parent = null;
		BinaryTreeNode<T> current = this.root;
		while (current != null) {
		    parent = current;
		    int cmp = compare(element, current.getData());
			if (cmp < 0) {
				current = current.getLeft();
			} else if (cmp > 0) {
			    current = current.getRight();
			} else {
			    current.setData(element);
			    return current;
			}
		}
		BinaryTreeNode<T> newNode = new BinaryTreeNode<T>(element);
		newNode.setParent(parent);
		if (compare(element, parent.getData()) < 0) {
		    parent.setLeft(newNode);
		} else {
		    parent.setRight(newNode);
		}
		return newNode;
	}
	/**
	 * 删除节点
	 * a. 如果没有左右子节点，则直接删除即可
	 * b. 如果只有左或者右子节点，直接取子节点替代删除节点位置
	 * c. 如果有左右子节点，则复制 前趋/后续 节点 替代删除节点位置，再删除 前驱/后续节点，此时的前驱/后续节点无双子或者只有一个子节点
	 * 
	 * @param element
	 * @return
	 */
	public BinaryTreeNode<T> remove(T element) {
	    
	    BinaryTreeNode<T> node = find(element);
	    if (null != node) {
	        removeNode(node);
	    }
	    return node;
	}
	
	/**
	 * 删除节点
	 * 
	 * @param node
	 * @return
	 */
	protected BinaryTreeNode<T> removeNode(BinaryTreeNode<T> node) {
	    
	    if (null != node) {
            // 叶子节点
            if (node.isLeaf()) {
                replaceNode(node, null);
            // 只有右子节点
            } else if (node.getLeft() == null && node.getRight() != null) {
                replaceNode(node, node.getRight());
                node.getRight().setParent(node.getParent());
             // 只有左子节点
            } else if (node.getLeft() != null && node.getRight() == null) {
                replaceNode(node, node.getLeft());
                node.getLeft().setParent(node.getParent());
            // 拥有左右子节点，取前驱节点替代删除的节点
            } else {
                // 前驱节点
                BinaryTreeNode<T> predNode = predecessor(node);
                // 前驱节点替换要删除的节点
                node.setData(predNode.getData());
                // 删除前驱节点
                return removeNode(predNode);
            }
            //help gc
            node.setParent(null);
            node.setRight(null);
            node.setLeft(null);
        }
	    return node;
	}
    /**
     * 替换节点
     * 
     * @param node
     * @param child
     */
    protected void replaceNode(BinaryTreeNode<T> node, BinaryTreeNode<T> child) {
        if (node == this.root) {
            this.root = child;
        } else if (node.isLeft()) {
            node.getParent().setLeft(child);
        } else {
            node.getParent().setRight(child);
        }
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
