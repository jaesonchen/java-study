package com.asiainfo.designpattern.architecture.doublechecklock;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 并发场景下的双重锁检查，避免无谓的占有锁
 * 
 * @author       zq
 * @date         2018年4月19日  上午10:41:57
 * Copyright: 	  北京亚信智慧数据科技有限公司
 */
public class Inventory<E extends java.io.Serializable> {

    final Logger logger = LoggerFactory.getLogger(getClass());
    
    final Lock lock = new ReentrantLock();
    List<E> items;
    int capacity;
    
    public Inventory(int capacity) {
        this.capacity = capacity;
        this.items = new ArrayList<>(this.capacity);
    }
    
    public boolean addItem(E item) {
        
        // 并发场景下减少无谓的锁占有
        if (items.size() < this.capacity) {
            lock.lock();
            try {
                // 双重检查
                if (items.size() < this.capacity) {
                    items.add(item);
                    logger.info("{}: items.size()={}, inventorySize={}", Thread.currentThread(), items.size(), capacity);
                    return true;
                }
                logger.info("fail to addItem, with lock!");
            } finally {
                lock.unlock();
            }
        }
        logger.info("fail to addItem, without lock!");
        return false;
    }
    
    public final List<E> getItems() {
        return Collections.unmodifiableList(items);
    }
}
