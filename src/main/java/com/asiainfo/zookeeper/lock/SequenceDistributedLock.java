package com.asiainfo.zookeeper.lock;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.Watcher.Event.EventType;
import org.apache.zookeeper.Watcher.Event.KeeperState;
import org.apache.zookeeper.ZooDefs.Ids;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.asiainfo.util.ThreadPoolUtils;

/**
 * @Description:  利用临时顺序节点实现共享锁
 * 	Zookeeper中有一种节点叫做顺序节点，ZooKeeper集群会按照提起创建的顺序来创建节点，还有一种名为临时节点的节点，临时节点由某个客户端创建，
 *  当客户端与ZooKeeper集群断开连接，则开节点自动被删除。
 *  算法思路：对于加锁操作，可以让所有客户端都去/lock目录下创建临时顺序节点，如果创建的客户端发现自身创建节点序列号是/lock/目录下最小的节点，
 *  则获得锁。否则，监视比自己创建节点的序列号小的节点（比自己创建的节点小的最大节点），进入等待。
 * 
 * 	1.保证锁节点（lock root node）这个父根节点的存在，这个节点是每个要获取lock客户端共用的，这个节点是PERSISTENT的。
 * 	2.第一次需要创建本客户端要获取lock的节点，调用 create( )，并设置 节点为EPHEMERAL_SEQUENTIAL类型，表示该节点为临时的和顺序的。
 *    如果获取锁的节点挂掉，则该节点自动失效，可以让其他节点获取锁。
 *  3.在父锁节点（lock root node）上调用 getChildren( ) ，不需要设置监视标志。 (为了避免“羊群效应”)。
 *  4.按照Fair竞争的原则，将步骤3中的子节点（要获取锁的节点）按照节点顺序的大小做排序，取出编号最小的一个节点做为lock的owner，
 *    判断自己的节点id是否就为owner id，如果是则返回，lock成功。如果不是则调用 exists( )监听比自己小的前一位的id，关注它锁释放的操作（也就是exist watch）。
 *  5.如果第4步监听exist的watch被触发，则继续按4中的原则判断自己是否能获取到lock。
 * 
 * @author       zq
 * @date         2017年8月26日  下午10:49:19
 * Copyright: 	  北京亚信智慧数据科技有限公司
 */
public class SequenceDistributedLock implements Watcher, DistributedLock {

	final Logger logger = LoggerFactory.getLogger(getClass());
	
	static final int SESSION_TIMEOUT = 60000;
	static final String BASE_PATH = "/sequence_lock";
	static final String LOCK_PREFIX ="lock-";
	final Object obj = new Object();
	ZooKeeper zk;
	String lockId;
	
	public SequenceDistributedLock() throws KeeperException, IOException, InterruptedException {
		
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
	 * @throws Exception
	 * @see com.asiainfo.zookeeper.lock.DistributedLock#acquire()
	 */
	@Override
	public void acquire() throws Exception {
		
		this.lockId = this.zk.create(BASE_PATH + "/" + LOCK_PREFIX, null, Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL_SEQUENTIAL);
		this.lockId = this.lockId.substring(BASE_PATH.length() + 1);
		logger.info("lockId={}", this.lockId);
		while (true) {
			String waitNode = "";
			try {
				List<String> children = zk.getChildren(BASE_PATH, false);
				logger.info("children={}", children);
				Collections.sort(children);
				if (children.get(0).equals(this.lockId)) {
					logger.info("success acquire lock!");
					return;
				}
				waitNode = children.get((children.indexOf(this.lockId) == -1) ? 0 :  (children.indexOf(this.lockId) - 1));
			} catch (KeeperException | InterruptedException e) {
				logger.error("acquire lock failed!");
			}
			
			synchronized(this.obj) {
				//watch for pre node
				this.zk.exists(BASE_PATH + "/" + waitNode, this);
				logger.info("watch node:{}", waitNode);
				//wait for lock release
				this.obj.wait();
			}
		}
	}

	/* 
	 * @Description: TODO
	 * @throws Exception
	 * @see com.asiainfo.zookeeper.lock.DistributedLock#release()
	 */
	@Override
	public void release() throws Exception {
		
		logger.info("release lock:{}!", BASE_PATH + "/" + this.lockId);
		this.zk.delete(BASE_PATH + "/" + this.lockId, -1);
		this.zk.close();
	}

	/* 
	 * @Description: TODO
	 * @param event
	 * @see org.apache.zookeeper.Watcher#process(org.apache.zookeeper.WatchedEvent)
	 */
	@Override
	public void process(WatchedEvent event) {

		if (event.getType() == EventType.NodeDeleted) {
			logger.info("lock released, wakeup wait!");
			synchronized (this.obj) {
				this.obj.notifyAll();
			}
		}
	}
	
	public static void main(String[] args) throws Exception {
		
		ThreadPoolUtils.getInstance().newThread(new LockThread(new SequenceDistributedLock())).start();
		ThreadPoolUtils.getInstance().newThread(new LockThread(new SequenceDistributedLock())).start();
		ThreadPoolUtils.getInstance().newThread(new LockThread(new SequenceDistributedLock())).start();
		ThreadPoolUtils.getInstance().newThread(new LockThread(new SequenceDistributedLock())).start();
		ThreadPoolUtils.getInstance().newThread(new LockThread(new SequenceDistributedLock())).start();
	}
	
	static class LockThread implements Runnable {

		SequenceDistributedLock sequenceLock;
		LockThread(SequenceDistributedLock sequenceLock) {
			this.sequenceLock = sequenceLock;
		}
		
		/* 
		 * @Description: TODO
		 * @see java.lang.Runnable#run()
		 */
		@Override
		public void run() {
			
			try {
				this.sequenceLock.acquire();
				Thread.sleep(1000);
				this.sequenceLock.release();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
