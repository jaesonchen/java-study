package com.asiainfo.collection.blockingqueue;

/**
 * LinkedBlockingQueue和ArrayBlockingQueue的区别：
 * 
 * 1.队列大小有所不同，ArrayBlockingQueue是有界的初始化必须指定大小，而LinkedBlockingQueue可以是有界的也可以是无界的(Integer.MAX_VALUE)，
 *   对于后者而言，当添加速度大于移除速度时，在无界的情况下，可能会造成内存溢出等问题。
 * 2.数据存储容器不同，ArrayBlockingQueue采用的是数组作为数据存储容器，而LinkedBlockingQueue采用的则是以Node节点作为连接对象的链表。
 * 3.由于ArrayBlockingQueue采用的是数组的存储容器，因此在插入或删除元素时不会产生或销毁任何额外的对象实例，而LinkedBlockingQueue则会生成一个额外的Node对象。
 *   这可能在长时间内需要高效并发地处理大批量数据的时，对于GC可能存在较大影响。
 * 4.两者的实现队列添加或移除的锁不一样，ArrayBlockingQueue实现的队列中的锁是没有分离的，即添加操作和移除操作采用的同一个ReenterLock锁，
 *   而LinkedBlockingQueue实现的队列中的锁是分离的，其添加采用的是putLock，移除采用的则是takeLock，这样能大大提高队列的吞吐量，
 *   也意味着在高并发的情况下生产者和消费者可以并行地操作队列中的数据，以此来提高整个队列的并发性能。


 * 
 * @author       zq
 * @date         2017年9月19日  上午9:07:01
 * Copyright: 	  北京亚信智慧数据科技有限公司
 */
public class BlockingQueue {

	/** 
	 * @Description: TODO
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
