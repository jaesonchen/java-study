package com.asiainfo.concurrent;

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
public class ConditionExample {
	
	Lock lock = new ReentrantLock();
	Condition condition = lock.newCondition();
	
	public void conditionWait() throws InterruptedException {
		
		lock.lock();
		try{
			condition.await();
		} finally {
			lock.unlock();
		}
	}
	
	public void conditionSignal() {
		
		lock.lock();
		try{
			condition.signal();
		} finally {
			lock.unlock();
		}
	}
}
