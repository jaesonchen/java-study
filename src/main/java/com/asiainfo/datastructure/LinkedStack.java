package com.asiainfo.datastructure;

/**
 * TODO
 * 
 * @author       zq
 * @date         2017年12月25日  下午4:25:25
 * Copyright: 	  北京亚信智慧数据科技有限公司
 */
public class LinkedStack<E> extends LinkedDLNodeList<E> implements Stack<E> {

    private int capacity = Integer.MAX_VALUE - 8;
    public LinkedStack() {}
    public LinkedStack(int capacity) {
        this.capacity = capacity;
    }
    
    /* 
     * TODO
     * @param element
     * @see com.asiainfo.datastructure.Stack#push(java.lang.Object)
     */
    @Override
    public void push(E element) {
        
        if (size() == capacity) {
            throw new RuntimeException("stack size=" + size() + " larger than capacity=" + capacity);
        }
        add(element);
    }

    /* 
     * TODO
     * @return
     * @see com.asiainfo.datastructure.Stack#pop()
     */
    @Override
    public E pop() {
        
        if (size() == 0) {
            throw new RuntimeException("stack size=" + size() + " no more elements!");
        }
        return remove(size() - 1);
    }

    /* 
     * TODO
     * @return
     * @see com.asiainfo.datastructure.Stack#peek()
     */
    @Override
    public E peek() {
        
        if (size() == 0) {
            throw new RuntimeException("stack size=" + size() + " no more elements!");
        }
        return get(size() - 1);
    }

    public static void main(String[] args) {
        
        Stack<Integer> stack = new LinkedStack<>();
        stack.push(11);
        stack.push(12);
        stack.push(13);
        System.out.println(stack.size());
        System.out.println(stack.peek());
        while (!stack.isEmpty()) {
            System.out.println(stack.pop());
        }
    }
}
