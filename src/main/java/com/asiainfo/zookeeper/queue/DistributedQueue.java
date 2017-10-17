package com.asiainfo.zookeeper.queue;

import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.CountDownLatch;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.Watcher.Event.EventType;
import org.apache.zookeeper.Watcher.Event.KeeperState;
import org.apache.zookeeper.ZooDefs.Ids;
import org.apache.zookeeper.ZooKeeper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.asiainfo.rpc.JdkSerializer;
import com.asiainfo.rpc.Serializer;
import com.asiainfo.util.ThreadPoolUtils;

/**
 * @Description: 基于zookeeper 提供的一致性保证，实现分布式队列DistributedQueue。
 * 
 * @author       zq
 * @date         2017年8月27日  下午5:27:53
 * Copyright: 	  北京亚信智慧数据科技有限公司
 */
public class DistributedQueue<E> implements Watcher, Queue<E> {

	final Logger logger = LoggerFactory.getLogger(getClass());
	
	static final int SESSION_TIMEOUT = 60000;
	static final String BASE_PATH = "/queue";
	static final String QUEUE_PREFIX ="qn-";
	final int capacity = 5;
	final Object obj = new Object();
	Serializer<E> serializer;
	ZooKeeper zk;
	
	
	public DistributedQueue(Serializer<E> serializer) throws KeeperException, IOException, InterruptedException {
		
		this.serializer = serializer;
		final CountDownLatch latch = new CountDownLatch(1);
		this.zk = new ZooKeeper("192.168.131.130:2181", SESSION_TIMEOUT, new Watcher() {
			@Override
			public void process(WatchedEvent event) {
				KeeperState state = event.getState();
				if (state == KeeperState.SyncConnected) {
					latch.countDown();
				}
				logger.info("zookeeper connected!");
			}
		});
		latch.await();
		// Ensure the parent znode exists
		if (this.zk.exists(BASE_PATH, false) == null) {
			this.zk.create(BASE_PATH, null, Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
			logger.info("{} created!", BASE_PATH);
		}
	}
	
	/* 
	 * @Description: TODO
	 * @param arg0
	 * @see org.apache.zookeeper.Watcher#process(org.apache.zookeeper.WatchedEvent)
	 */
	@Override
	public void process(WatchedEvent event) {
		
		if (event.getType() == EventType.NodeChildrenChanged) {
			logger.info("queue Node Changed!");
			synchronized (obj) {
				obj.notifyAll();
			}
		}
	}

	/* 
	 * @Description: TODO
	 * @return
	 * @see java.util.Collection#size()
	 */
	@Override
	public int size() {

		try {
			List<String> children = this.zk.getChildren(BASE_PATH, false);
			return null == children ? 0 : children.size();
		} catch (KeeperException | InterruptedException ex) {
			logger.error("error on getChildren!", ex);
		}
		return 0;
	}

	/* 
	 * @Description: TODO
	 * @return
	 * @see java.util.Collection#isEmpty()
	 */
	@Override
	public boolean isEmpty() {
		return size() == 0;
	}

	/* 
	 * @Description: TODO
	 * @param e
	 * @return
	 * @see java.util.Queue#add(java.lang.Object)
	 */
	@Override
	public boolean add(E e) {

		logger.info("trying add element:{}!", e);
		if (this.capacity <= this.size()) {
			throw new IllegalStateException("queue is larger than " +  this.capacity);
		}
		final String queueName = BASE_PATH + "/" + QUEUE_PREFIX;
		try {
			byte[] data = (null == e) ? new byte[0] : this.serializer.serialize(e);
			String queueId = this.zk.create(queueName, data, Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT_SEQUENTIAL);
			logger.info("queue offer success, queueId={}!", queueId);
			return true;
		} catch (KeeperException | InterruptedException ex) {
			logger.error("queue offer failed!", ex);
		}
		return false;
	}

	/* 
	 * @Description: TODO
	 * @param e
	 * @return
	 * @see java.util.Queue#offer(java.lang.Object)
	 */
	@Override
	public boolean offer(E e) {

		logger.info("trying offer element:{}!", e);
		final String queueName = BASE_PATH + "/" + QUEUE_PREFIX;
		while (true) {
			if (this.capacity <= this.size()) {
				try {
					synchronized(this.obj) {
						this.zk.getChildren(BASE_PATH, this);
						this.obj.wait();
					}
				} catch (InterruptedException | KeeperException ex) {
					logger.error("error on getChildren!", ex);
					return false;
				}
			} else {
				try {
					byte[] data = (null == e) ? new byte[0] : this.serializer.serialize(e);
					String queueId = this.zk.create(queueName, data, Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT_SEQUENTIAL);
					logger.info("queue offer success, queueId={}!", queueId);
					return true;
				} catch (KeeperException | InterruptedException ex) {
					logger.error("queue offer failed!", ex);
					return false;
				}
			}
		}
	}

	/* 
	 * @Description: TODO
	 * @return
	 * @see java.util.Queue#remove()
	 */
	@Override
	public E remove() {
		// TODO Auto-generated method stub
		return null;
	}

	/* 
	 * @Description: TODO
	 * @return
	 * @see java.util.Queue#poll()
	 */
	@Override
	public E poll() {

		logger.info("trying poll element!");
		while (true) {
			try {
				List<String> children = this.zk.getChildren(BASE_PATH, false);
				logger.info("children={}", children);
				Collections.sort(children);
				if (null == children || children.isEmpty()) {
					return null;
				}
				String queueId = BASE_PATH + "/" + children.get(0);
				logger.info("queue poll element, queueId={}!", queueId);
				byte[] data = this.zk.getData(queueId, false, null);
				this.zk.delete(queueId, -1);
				return this.serializer.deserialize(data);
			} catch (KeeperException | InterruptedException ex) {
				logger.error("queue poll failed!", ex);
			}
		}
	}

	/* 
	 * @Description: TODO
	 * @return
	 * @see java.util.Queue#element()
	 */
	@Override
	public E element() {
		// TODO Auto-generated method stub
		return null;
	}

	/* 
	 * @Description: TODO
	 * @return
	 * @see java.util.Queue#peek()
	 */
	@Override
	public E peek() {
		// TODO Auto-generated method stub
		return null;
	}

	/* 
	 * @Description: TODO
	 * @param o
	 * @return
	 * @see java.util.Collection#contains(java.lang.Object)
	 */
	@Override
	public boolean contains(Object o) {
		// TODO Auto-generated method stub
		return false;
	}

	/* 
	 * @Description: TODO
	 * @return
	 * @see java.util.Collection#iterator()
	 */
	@Override
	public Iterator<E> iterator() {
		// TODO Auto-generated method stub
		return null;
	}

	/* 
	 * @Description: TODO
	 * @return
	 * @see java.util.Collection#toArray()
	 */
	@Override
	public Object[] toArray() {
		// TODO Auto-generated method stub
		return null;
	}

	/* 
	 * @Description: TODO
	 * @param a
	 * @return
	 * @see java.util.Collection#toArray(java.lang.Object[])
	 */
	@Override
	public <T> T[] toArray(T[] a) {
		// TODO Auto-generated method stub
		return null;
	}

	/* 
	 * @Description: TODO
	 * @param o
	 * @return
	 * @see java.util.Collection#remove(java.lang.Object)
	 */
	@Override
	public boolean remove(Object o) {
		// TODO Auto-generated method stub
		return false;
	}

	/* 
	 * @Description: TODO
	 * @param c
	 * @return
	 * @see java.util.Collection#containsAll(java.util.Collection)
	 */
	@Override
	public boolean containsAll(Collection<?> c) {
		// TODO Auto-generated method stub
		return false;
	}

	/* 
	 * @Description: TODO
	 * @param c
	 * @return
	 * @see java.util.Collection#addAll(java.util.Collection)
	 */
	@Override
	public boolean addAll(Collection<? extends E> c) {
		// TODO Auto-generated method stub
		return false;
	}

	/* 
	 * @Description: TODO
	 * @param c
	 * @return
	 * @see java.util.Collection#removeAll(java.util.Collection)
	 */
	@Override
	public boolean removeAll(Collection<?> c) {
		// TODO Auto-generated method stub
		return false;
	}

	/* 
	 * @Description: TODO
	 * @param c
	 * @return
	 * @see java.util.Collection#retainAll(java.util.Collection)
	 */
	@Override
	public boolean retainAll(Collection<?> c) {
		// TODO Auto-generated method stub
		return false;
	}

	/* 
	 * @Description: TODO
	 * @see java.util.Collection#clear()
	 */
	@Override
	public void clear() {
		// TODO Auto-generated method stub
		
	}
	
	public static void main(String[] args) throws Exception {
		
		Serializer<Object> serial = new JdkSerializer();
		ThreadPoolUtils.getInstance().newThread(
				new LockThread<>(new DistributedQueue<Object>(serial), new QueueElement("1", "11"))).start();
		ThreadPoolUtils.getInstance().newThread(
				new LockThread<>(new DistributedQueue<Object>(serial), new QueueElement("2", "22"))).start();
		ThreadPoolUtils.getInstance().newThread(
				new LockThread<>(new DistributedQueue<Object>(serial), new QueueElement("3", "33"))).start();
		ThreadPoolUtils.getInstance().newThread(
				new LockThread<>(new DistributedQueue<Object>(serial), new QueueElement("4", "44"))).start();
		ThreadPoolUtils.getInstance().newThread(
				new LockThread<>(new DistributedQueue<Object>(serial), new QueueElement("5", "55"))).start();
	}
	
	static class LockThread<T> implements Runnable {

		DistributedQueue<T> queue;
		T element;
		LockThread(DistributedQueue<T> queue, T element) {
			this.queue = queue;
			this.element = element;
		}
		
		/* 
		 * @Description: TODO
		 * @see java.lang.Runnable#run()
		 */
		@Override
		public void run() {
			
			try {
				this.queue.offer(element);
				this.queue.offer(element);
				
				Thread.sleep(1000);
				this.queue.poll();
				Thread.sleep(1000);
				this.queue.poll();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
