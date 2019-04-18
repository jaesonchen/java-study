package com.asiainfo.concurrent;

import java.util.Arrays;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 
 * @Description: Condition示例
 * 
 * @author       zq
 * @date         2017年9月3日  下午2:45:17
 * Copyright: 	  北京亚信智慧数据科技有限公司
 */
public class ConditionExample<E> {
	
    private Lock lock = new ReentrantLock();
	private Condition notEmpty = lock.newCondition();
    private Condition notFull = lock.newCondition();
    private Object[] items;
    private int addIndex, removeIndex, count;
    
    public ConditionExample(int size) {
        items = new Object[size];
    }
    
    public static void main(String[] args) throws InterruptedException {
        
        ConditionExample<String> con = new ConditionExample<>(10);
        con.add("111");
        con.add("222");
        con.add("333");
        con.remove();
        System.out.println(con);
    }

	public void add(E e) throws InterruptedException {
		
		lock.lock();
		try{
		    while (count == items.length) {
                notFull.await();
            }
		    items[addIndex] = e;
		    if (++addIndex == items.length) {
                addIndex = 0;
            }
		    ++count;
            notEmpty.signal();
		} finally {
			lock.unlock();
		}
	}
	
	@SuppressWarnings("unchecked")
    public E remove() throws InterruptedException {
		
		lock.lock();
		try{
		    while (count == 0) {
                notEmpty.await();
            }
		    Object x = items[removeIndex];
		    items[removeIndex] = null;
		    if (++removeIndex == items.length) {
                removeIndex = 0;
            }
            --count;
            notFull.signal();
            return (E) x;
		} finally {
			lock.unlock();
		}
	}

    @Override
    public String toString() {
        return "ConditionExample [items=" + Arrays.toString(items) + ", count=" + count + "]";
    }
}
