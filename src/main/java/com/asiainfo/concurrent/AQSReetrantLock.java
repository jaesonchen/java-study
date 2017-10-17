package com.asiainfo.concurrent;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.AbstractQueuedSynchronizer;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

/**
 * 
 * @Description: 基于AbstractQueuedSynchronizer实现简单的独占锁
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
