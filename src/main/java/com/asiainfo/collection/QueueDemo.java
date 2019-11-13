package com.asiainfo.collection;

import java.util.ArrayDeque;
import java.util.LinkedList;
import java.util.PriorityQueue;

/**
 * 由于null在某些方法中作为特殊返回值，所以不建议插入null 元素。
 * 
 * Queue extends Collection: PriorityQueue
 * add、remove、element: 失败时抛出异常IllegalStateException(容量限制)、NoSuchElementException(isEmpty)
 * offer、poll、peek: 失败时返回特定值(false、null)
 * 
 * Deque(double ended queue) extends Queue: ArrayDeque、LinkedList
 * addFirst、addLast、removeFirst、removeLast、getFirst、getLast: 失败时抛出异常IllegalStateException(容量限制)、NoSuchElementException(isEmpty)
 * offerFirst、offerLast、pollFirst、pollLast、peekFirst、peekLast: 失败时返回特定值(false、null)
 * LIFO/Stack: addFirst/push、removeFirst/pop、peekFirst/peek
 * 
 * 
 * @author       zq
 * @date         2018年4月10日  下午4:33:36
 * Copyright: 	  北京亚信智慧数据科技有限公司
 */
public class QueueDemo {

    public static void main(String[] args) {
        
        // PriorityQueue 优先级队列，按照Comparable元素的自然排序或者创建队列时传递的Comparator排序，不允许null元素
        // 基于二叉堆实现，迭代时输出的是存储序列非排序序列，队首是最小的元素，结构变更时重排序队首
        PriorityQueue<Integer> pQueue = new PriorityQueue<>();
        pQueue.add(1);
        pQueue.add(9);
        pQueue.add(4);
        pQueue.add(7);
        pQueue.add(5);
        pQueue.add(3);
        for (Integer i : pQueue) {
            System.out.println(i);
        }
        System.out.println(pQueue);
        System.out.println(pQueue.poll());
        System.out.println(pQueue);
        System.out.println(pQueue.poll());
        System.out.println(pQueue);
        System.out.println(pQueue.poll());
        System.out.println(pQueue);
        System.out.println(pQueue.poll());
        System.out.println("================================");
        
        // ArrayDeque当做栈来操作
        ArrayDeque<Integer> stack = new ArrayDeque<>();
        stack.push(2);
        stack.push(5);
        stack.push(7);
        stack.push(12);
        System.out.println(stack);
        System.out.println(stack.peek());
        System.out.println(stack);
        System.out.println(stack.pop());
        System.out.println(stack);
        System.out.println("================================");
        
        // LinkedList
        LinkedList<String> link = new LinkedList<>();
        link.offer("chenzq");
        link.push("czq");
        link.offerFirst("jaesonchen");
        link.offerLast("coolrice");
        System.out.println(link);
        System.out.println(link.peekFirst());
        System.out.println(link.peekLast());
        System.out.println(link);
        System.out.println(link.pollLast());
        System.out.println(link);
    }
}
