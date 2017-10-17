package com.asiainfo.zookeeper.lock;

import java.io.IOException;
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

import com.asiainfo.util.ThreadPoolUtils;


/**
 * @Description: 利用节点名称的唯一性来实现共享锁
 * 				 ZooKeeper抽象出来的节点结构是一个和unix文件系统类似的小型的树状的目录结构。ZooKeeper机制规定：同一个目录下只能有一个唯一的文件名。
 * 				 例如：我们在Zookeeper目录/lock_node目录下创建，两个客户端创建一个名为Lock节点，只有一个能够成功。
 * 				 算法思路: 利用名称唯一性，加锁操作时，只需要所有客户端一起创建/lock_node/Lock节点，只有一个创建成功，成功者获得锁。
 * 				 解锁时，只需删除/lock_node/Lock节点，其余客户端再次进入竞争创建节点，直到所有客户端都获得锁。
 * 
 * @author       zq
 * @date         2017年8月26日  下午10:48:51
 * Copyright: 	  北京亚信智慧数据科技有限公司
 */
public class UniqueDistributedLock implements Watcher, DistributedLock {

	final Logger logger = LoggerFactory.getLogger(getClass());
	
	static final int SESSION_TIMEOUT = 60000;
	static final String BASE_PATH = "/distributed_lock";
	static final String LOCK_NAME ="lock-unique";
	final String lockName = BASE_PATH + "/" + LOCK_NAME;
	final Object obj = new Object();
	ZooKeeper zk;
	
	public UniqueDistributedLock() throws KeeperException, IOException, InterruptedException {
		
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
		
		logger.info("trying acquire lock!");
		while (true) {
			try {
				String lockId = this.zk.create(lockName, null, Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
				logger.info("acquire success, lockId={}!", lockId);
				return;
			} catch (KeeperException | InterruptedException e) {
				logger.error("acquire lock failed!");
			}

			synchronized(obj) {
				//watch for parent node
				this.zk.getChildren(BASE_PATH, this);
				//wait for lock release
				obj.wait();
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
		
		logger.info("release lock!");
		this.zk.delete(lockName, -1);
		this.zk.close();
	}


	/* 
	 * @Description: TODO
	 * @param event
	 * @see org.apache.zookeeper.Watcher#process(org.apache.zookeeper.WatchedEvent)
	 */
	@Override
	public void process(WatchedEvent event) {
		
		if (event.getType() == EventType.NodeChildrenChanged) {
			logger.info("lock released, wakeup wait!");
			synchronized (obj) {
				obj.notifyAll();
			}
		}
	}
	
	public static void main(String[] args) throws Exception {
		
		ThreadPoolUtils.getInstance().newThread(new LockThread(new UniqueDistributedLock())).start();
		ThreadPoolUtils.getInstance().newThread(new LockThread(new UniqueDistributedLock())).start();
		ThreadPoolUtils.getInstance().newThread(new LockThread(new UniqueDistributedLock())).start();
		ThreadPoolUtils.getInstance().newThread(new LockThread(new UniqueDistributedLock())).start();
		ThreadPoolUtils.getInstance().newThread(new LockThread(new UniqueDistributedLock())).start();
	}
	
	static class LockThread implements Runnable {

		UniqueDistributedLock uniqueLock;
		LockThread(UniqueDistributedLock uniqueLock) {
			this.uniqueLock = uniqueLock;
		}
		
		/* 
		 * @Description: TODO
		 * @see java.lang.Runnable#run()
		 */
		@Override
		public void run() {
			
			try {
				this.uniqueLock.acquire();
				Thread.sleep(1000);
				this.uniqueLock.release();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
