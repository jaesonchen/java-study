package com.asiainfo.jmm;

/**
 * 
 * - 在新的内存模型下，volatile变量仍然不能彼此重排序。和旧模型的时候不同，volatile周围的普通字段的也不再能够随便的重排序了。
 * - 写入一个volatile字段和释放监视器有相同的内存影响，而且读取volatile字段和获取监视器也有相同的内存影响。
 * 
 * - volatile 变量具有 synchronized 的可见性特性，但是不具备原子特性。
 * - 要使 volatile 变量提供理想的线程安全，必须同时满足下面两个条件：
 * 1. 对变量的写操作不依赖于当前值。
 * 2. 该变量没有包含在具有其他变量的不变式中。
 * 
 * - volatile 操作不会像锁一样造成阻塞，因此，在能够安全使用 volatile 的情况下，volatile 可以提供一些优于锁的可伸缩特性。
 * -  如果读操作的次数要远远超过写操作，与锁相比，volatile 变量通常能够减少同步的性能开销。
 * 
 * @author       zq
 * @date         2017年9月27日  下午4:31:54
 * Copyright: 	  北京亚信智慧数据科技有限公司
 */
public class VolatileExample {

	int x = 0;
	volatile boolean v = false;
	//如果v不是volatile变量，那么，编译器可以在writer线程中重排序写入操作，那么reader线程中的读取x变量的操作可能会看到0。
	public void writer() {
		x = 42;
		v = true;
	}

	public void reader() {
		//如果reader线程看到了v的值为true，那么，它也保证能够看到在之前发生的写入42这个操作。
		if (v == true) {
			//uses x - guaranteed to see 42.
		}
	}
}
