package com.asiainfo.concurrent;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.AbstractQueuedSynchronizer;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

/**
 * 基于AbstractQueuedSynchronizer实现简单的独占锁
 * 
 * ReentrantLock如何保证可见性：
 *  尽管ReentrantLock保证了state的可见性，但是lock 和  unlock 之间的临界区代码，是如何保证对其他线程的可见性的？
 *  无论是lock 或者 unlock 都会对 state（volatile修饰）做修改，volatile变量就会影响指令重排，最后volatile汇编后的x86 lock; addl $0,0(%%esp) 会把之前修改过得缓存置为无效，导致其他CPU去主存读取。
 * 
 *  从JDK 5开始，JSR-133增强了volatile语义，禁止了volatile变量与普通变量之间的重排序。
 * 
 * Happens-before规则：一个volatile变量的写操作发生在这个volatile变量随后的读操作之前。
 *  它的含义是当一个线程执行写入volatile变量的操作后，另一个线程执行读取volatile变量的操作，
 *  此时jmm会将第一个线程写volatile之前的所有共享变量(包括非volatile)的修改刷新到主存中，并将修改过的内容的缓存置为无效，导致其他CPU去主存读取。
 *  从而保证了共享变量的可见性。
 * 
 * @author       zq
 * @date         2017年9月3日  下午9:48:35
 * Copyright: 	  北京亚信智慧数据科技有限公司
 */
public class AQSReetrantLock implements Lock , Serializable {

	/** serialVersionUID */
	private static final long serialVersionUID = 1L;
	
	// Our internal helper class
	private static class Sync extends AbstractQueuedSynchronizer {
		
		/** serialVersionUID */
		private static final long serialVersionUID = 1L;

		// Report whether in locked state
		@Override
		protected boolean isHeldExclusively() {
			return getState() == 1;
		}

		// Acquire the lock if state is zero
		@Override
		public boolean tryAcquire(int acquires) {
			assert acquires == 1; // Otherwise unused
			if (compareAndSetState(0, 1)) {
				setExclusiveOwnerThread(Thread.currentThread());
				return true;
			}
			return false;
		}

		// Release the lock by setting state to zero
		@Override
		protected boolean tryRelease(int releases) {
			assert releases == 1; // Otherwise unused
			if (getState() == 0) {
				throw new IllegalMonitorStateException();
			}
			setExclusiveOwnerThread(null);
			setState(0);
			return true;
		}

		// Provide a Condition
		Condition newCondition() { return new ConditionObject(); }

		// Deserialize properly
		private void readObject(ObjectInputStream s) throws IOException, ClassNotFoundException {
			s.defaultReadObject();
			setState(0); // reset to unlocked state
		}
	}

	// The sync object does all the hard work. We just forward to it.
	private final Sync sync = new Sync();
	
	@Override public void lock()                { sync.acquire(1); }
	@Override public boolean tryLock()          { return sync.tryAcquire(1); }
	@Override public void unlock()              { sync.release(1); }
	@Override public Condition newCondition()   { return sync.newCondition(); }
	public boolean isLocked()         { return sync.isHeldExclusively(); }
	public boolean hasQueuedThreads() { return sync.hasQueuedThreads(); }
	@Override public void lockInterruptibly() throws InterruptedException {
		sync.acquireInterruptibly(1);
	}
	@Override public boolean tryLock(long timeout, TimeUnit unit) throws InterruptedException {
		return sync.tryAcquireNanos(1, unit.toNanos(timeout));
	}
}
