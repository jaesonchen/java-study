package com.asiainfo.basic;

import java.io.Serializable;
import java.util.concurrent.locks.LockSupport;

import sun.misc.Unsafe;

/**
 * AQS 数据结构：
 * 
 * Node:
 * volatile Node prev, next;
 * volatile int waitStatus;
 * volatile Thread thread;
 * 
 * AQS:
 * volatile Node head, tail;
 * volatile int state;
 * 
 *      +------+  prev +------+  next +------+
 * head | null | <---- | node | ----> | node |  tail
 *      +------+       +------+       +------+
 * 
 * @author       zq
 * @date         2017年9月22日  下午3:42:00
 * Copyright: 	  北京亚信智慧数据科技有限公司
 */
@SuppressWarnings("all")
public abstract class AbstractQueuedSynchronizer implements Serializable {

	/** serialVersionUID */
	private static final long serialVersionUID = 1L;

	static final class Node {

		/** waitStatus value to indicate thread has cancelled */
		static final int CANCELLED =  1;
		/** waitStatus value to indicate successor's thread needs unparking */
		static final int SIGNAL    = -1;
		/** waitStatus value to indicate thread is waiting on condition */
		static final int CONDITION = -2;
		/** waitStatus value to indicate the next acquireShared should unconditionally propagate */
		static final int PROPAGATE = -3;
	        
		volatile int waitStatus;
		volatile Node prev;
		volatile Node next;
		volatile Thread thread;

		final Node predecessor() throws NullPointerException {
	        	
			Node p = prev;
			if (p == null) {
				throw new NullPointerException();
			}
			else {
				return p;
			}
		}

		Node() {}

		Node(Thread thread) {
			this.thread = thread;
		}

		Node(Thread thread, int waitStatus) { // Used by Condition
			this.waitStatus = waitStatus;
			this.thread = thread;
		}
	}
	
	private static Unsafe unsafe = Unsafe.getUnsafe();
    private static long stateOffset;
    private static long headOffset;
    private static long tailOffset;
    private static long waitStatusOffset;
    private static long nextOffset;

    static {
        try {
            stateOffset = unsafe.objectFieldOffset
                (AbstractQueuedSynchronizer.class.getDeclaredField("state"));
            headOffset = unsafe.objectFieldOffset
                (AbstractQueuedSynchronizer.class.getDeclaredField("head"));
            tailOffset = unsafe.objectFieldOffset
                (AbstractQueuedSynchronizer.class.getDeclaredField("tail"));
            waitStatusOffset = unsafe.objectFieldOffset
                (Node.class.getDeclaredField("waitStatus"));
            nextOffset = unsafe.objectFieldOffset
                (Node.class.getDeclaredField("next"));
        } catch (Exception ex) { throw new Error(ex); }
    }
    
    /**
     * CAS head field. Used only by enq.
     */
    private final boolean compareAndSetHead(Node update) {
        return unsafe.compareAndSwapObject(this, headOffset, null, update);
    }
    /**
     * CAS tail field. Used only by enq.
     */
    private final boolean compareAndSetTail(Node expect, Node update) {
        return unsafe.compareAndSwapObject(this, tailOffset, expect, update);
    }

    /**
     * CAS waitStatus field of a node.
     */
    private static final boolean compareAndSetWaitStatus(Node node, int expect, int update) {
        return unsafe.compareAndSwapInt(node, waitStatusOffset, expect, update);
    }
    /**
     * CAS next field of a node.
     */
    private static final boolean compareAndSetNext(Node node, Node expect, Node update) {
        return unsafe.compareAndSwapObject(node, nextOffset, expect, update);
    }
    
    /**
     * Convenience method to interrupt current thread.
     */
    static void selfInterrupt() {
        Thread.currentThread().interrupt();
    }
    
    private transient Thread exclusiveOwnerThread;
	private transient volatile Node head;
	private transient volatile Node tail;
	private volatile int state;
	
    protected final void setExclusiveOwnerThread(Thread thread) {
        exclusiveOwnerThread = thread;
    }
    protected final Thread getExclusiveOwnerThread() {
        return exclusiveOwnerThread;
    }
    protected final int getState() {
        return state;
    }
    protected final void setState(int newState) {
        state = newState;
    }
    protected final boolean compareAndSetState(int expect, int update) {
        return unsafe.compareAndSwapInt(this, stateOffset, expect, update);
    }
    
    /**
     * 节点入队，非阻塞往tail插入节点
     */
    protected Node enq(final Node node) {
    	
        for (;;) {
            Node t = tail;
            // Must initialize
            if (t == null) {
                if (compareAndSetHead(new Node())) {
                    tail = head;
                }
            } else {
                node.prev = t;
                if (compareAndSetTail(t, node)) {
                    t.next = node;
                    return t;
                }
            }
        }
    }
    
    /**
     * 增加同步等待节点
     */
    protected Node addWaiter() {
    	
        Node node = new Node(Thread.currentThread());
        // Try the fast path of enq; backup to full enq on failure
        Node pred = tail;
        if (pred != null) {
            node.prev = pred;
            if (compareAndSetTail(pred, node)) {
                pred.next = node;
                return node;
            }
        }
        enq(node);
        return node;
    }
    
    protected void setHead(Node node) {
        head = node;
        node.thread = null;
        node.prev = null;
    }
    
    /**
     * 尝试获得状态锁，失败则加入等待节点，加入等待队列后再确认一次，如果是第一个节点则再次尝试获取锁，失败则park线程。
     * 成功获取状态锁后，需要判断在park时是否被中断过，如果中断过则设置当前线程的中断状态
     */
    public final void acquire(int arg) {
        if (!tryAcquire(arg) &&
            acquireQueued(addWaiter(), arg)) {
            selfInterrupt();
        }
    }

    /**
     * 释放状态锁，并唤醒等待队列的下一个非cancel的节点线程
     */
    public final boolean release(int arg) {
        if (tryRelease(arg)) {
            Node h = head;
            if (h != null && h.waitStatus != 0) {
                unparkSuccessor(h);
            }
            return true;
        }
        return false;
    }
    
    /**
     * 查找head节点的下一个非cancel的等待节点，并唤醒该节点的线程，如果next为null或者状态为cancel则从tail往前找
     */
    protected void unparkSuccessor(Node node) {
    	
        /*
         * If status is negative (i.e., possibly needing signal) try
         * to clear in anticipation of signalling.  It is OK if this
         * fails or if status is changed by waiting thread.
         */
        int ws = node.waitStatus;
        if (ws < 0) {
            compareAndSetWaitStatus(node, ws, 0);
        }

        /*
         * Thread to unpark is held in successor, which is normally
         * just the next node.  But if cancelled or apparently null,
         * traverse backwards from tail to find the actual
         * non-cancelled successor.
         */
        Node s = node.next;
        if (s == null || s.waitStatus > 0) {
            s = null;
            for (Node t = tail; t != null && t != node; t = t.prev) {
                if (t.waitStatus <= 0) {
                    s = t;
                }
            }
        }
        if (s != null) {
            LockSupport.unpark(s.thread);
        }
    }
    
    /**
     * 如果是等待队列的第一个节点，则再次尝试获得锁，否则park当前线程
     */
    final boolean acquireQueued(final Node node, int arg) {
    	
        boolean failed = true;
        try {
            boolean interrupted = false;
            for (;;) {
                final Node p = node.predecessor();
                if (p == head && tryAcquire(arg)) {
                    setHead(node);
                    // help GC
                    p.next = null;
                    failed = false;
                    return interrupted;
                }
                if (shouldParkAfterFailedAcquire(p, node) &&
                    parkAndCheckInterrupt()) {
                    interrupted = true;
                }
            }
        } finally {
            // 异常退出时需要清理等待节点。
            if (failed) {
                cancelAcquire(node);
            }
        }
    }
    
    /**
     * park当前线程，返回线程在park期间是否被中断
     */
    private final boolean parkAndCheckInterrupt() {
        LockSupport.park(this);
        return Thread.interrupted();
    }
    
    /**
     * 判断是否需要在失败后park线程
     * a. 如果前一个节点被标记为signal，则park线程
     * b. 如果前一个节点被标记为cancel，则循环找到非cancel的节点，返回acquireQueued继续尝试
     * c. 如果前一个节点被标记为0、PROPAGATE，则更新其状态为signal，返回acquireQueued继续尝试
     */
    private static boolean shouldParkAfterFailedAcquire(Node pred, Node node) {
    	
        int ws = pred.waitStatus;
        if (ws == Node.SIGNAL) {
            /*
             * This node has already set status asking a release
             * to signal it, so it can safely park.
             */
            return true;
        }
        if (ws > 0) {
            /*
             * Predecessor was cancelled. Skip over predecessors and
             * indicate retry.
             */
            do {
                node.prev = pred = pred.prev;
            } while (pred.waitStatus > 0);
            pred.next = node;
        } else {
            /*
             * waitStatus must be 0 or PROPAGATE.  Indicate that we
             * need a signal, but don't park yet.  Caller will need to
             * retry to make sure it cannot acquire before parking.
             */
            compareAndSetWaitStatus(pred, ws, Node.SIGNAL);
        }
        return false;
    }

    private void cancelAcquire(Node node) {
    	
        // Ignore if node doesn't exist
        if (node == null) {
            return;
        }
        node.thread = null;

        // Skip cancelled predecessors
        Node pred = node.prev;
        while (pred.waitStatus > 0) {
            node.prev = pred = pred.prev;
        }

        // predNext is the apparent node to unsplice. CASes below will
        // fail if not, in which case, we lost race vs another cancel
        // or signal, so no further action is necessary.
        Node predNext = pred.next;

        // Can use unconditional write instead of CAS here.
        // After this atomic step, other Nodes can skip past us.
        // Before, we are free of interference from other threads.
        node.waitStatus = Node.CANCELLED;

        // If we are the tail, remove ourselves.
        if (node == tail && compareAndSetTail(node, pred)) {
            compareAndSetNext(pred, predNext, null);
        } else {
            // If successor needs signal, try to set pred's next-link
            // so it will get one. Otherwise wake it up to propagate.
            int ws;
            if (pred != head &&
                ((ws = pred.waitStatus) == Node.SIGNAL ||
                 (ws <= 0 && compareAndSetWaitStatus(pred, ws, Node.SIGNAL))) &&
                pred.thread != null) {
                Node next = node.next;
                if (next != null && next.waitStatus <= 0) {
                    compareAndSetNext(pred, predNext, next);
                }
            } else {
                unparkSuccessor(node);
            }
            // help GC
            node.next = node;
        }
    }
    
    /**
     * 尝试获得状态锁，通常是compareAndSwap state状态
     */
    protected boolean tryAcquire(int arg) {
        throw new UnsupportedOperationException();
    }
    
    /**
     * 尝试释放状态锁，通常是重置state状态
     */
    protected boolean tryRelease(int arg) {
        throw new UnsupportedOperationException();
    }
    
    public final boolean hasQueuedThreads() {
        return head != tail;
    }
    
    public final boolean hasContended() {
        return head != null;
    }
}
