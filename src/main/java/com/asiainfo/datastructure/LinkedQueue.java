package com.asiainfo.datastructure;

/**
 * TODO
 * 
 * @author       zq
 * @date         2017年12月25日  下午4:36:42
 * Copyright: 	  北京亚信智慧数据科技有限公司
 */
public class LinkedQueue<E> extends LinkedDLNodeList<E> implements Queue<E> {

    private int capacity = Integer.MAX_VALUE - 8;
    public LinkedQueue() {}
    public LinkedQueue(int capacity) {
        this.capacity = capacity;
    }
    
    /* 
     * TODO
     * @param element
     * @see com.asiainfo.datastructure.Queue#offer(java.lang.Object)
     */
    @Override
    public void offer(E element) {

        if (size() == capacity) {
            throw new RuntimeException("queue size=" + size() + " larger than capacity=" + capacity);
        }
        add(element);
    }

    /* 
     * TODO
     * @return
     * @see com.asiainfo.datastructure.Queue#poll()
     */
    @Override
    public E poll() {

        if (size() == 0) {
            throw new RuntimeException("queue size=" + size() + " no more elements!");
        }
        return remove(0);
    }

    /* 
     * TODO
     * @return
     * @see com.asiainfo.datastructure.Queue#peek()
     */
    @Override
    public E peek() {

        if (size() == 0) {
            throw new RuntimeException("queue size=" + size() + " no more elements!");
        }
        return get(0);
    }
    
    public static void main(String[] args) {
        
        Queue<String> queue = new LinkedQueue<>();
        queue.offer("11");
        queue.offer("12");
        queue.offer("13");
        System.out.println(queue.size());
        System.out.println(queue.peek());
        while (!queue.isEmpty()) {
            System.out.println(queue.poll());
        }
    }
}
