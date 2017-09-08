package com.asiainfo.zookeeper.barrier;

import java.io.IOException;
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

/**
 * @Description: 在分布式系统中，屏障是这样一种语义: 客户端需要等待多个进程完成各自的任务，然后才能继续往前进行下一步。
 * 	Client在ZooKeeper上创建屏障结点/barrier/my_barrier，并启动执行各个任务的进程Client通过exist()来Watch /barrier/my_barrier结点。
 *  每个任务进程在完成任务后，去检查是否达到指定的条件，如果没达到就啥也不做，如果达到了就把/barrier/my_barrier结点删除，
 *  Client收到/barrier/my_barrier被删除的通知，屏障消失，继续下一步任务。
 * 
 * @author       zq
 * @date         2017年8月27日  下午10:44:01
 * Copyright: 	  北京亚信智慧数据科技有限公司
 */
public class DistributedBarrier implements Watcher, Barrier {

	final Logger logger = LoggerFactory.getLogger(getClass());
	
	static final int SESSION_TIMEOUT = 60000;
	static final String BASE_PATH = "/barrier";
	static final String BARRIER_PREFIX ="bn-";
	static final String BARRIER = "start";
	final Object obj = new Object();
	ZooKeeper zk;
	int barrierSize;
	
	public DistributedBarrier(int barrierSize) throws KeeperException, IOException, InterruptedException {
		
		this.barrierSize = barrierSize;
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
		// Ensure the barrier znode exists
		if (this.zk.exists(BASE_PATH + "/" + BARRIER, false) == null) {
			this.zk.create(BASE_PATH + "/" + BARRIER, null, Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
			logger.info("barrier {} created!", BASE_PATH + "/" + BARRIER);
		}
	}
	
	/* 
	 * @Description: TODO
	 * @param arg0
	 * @see org.apache.zookeeper.Watcher#process(org.apache.zookeeper.WatchedEvent)
	 */
	@Override
	public void process(WatchedEvent event) {

		if (event.getType() == EventType.NodeDeleted) {
			logger.info("barrier ready, wakeup wait!");
			synchronized (this.obj) {
				this.obj.notifyAll();
			}
		}
	}

	/* 
	 * @Description: TODO
	 * @throws Exception
	 * @see com.asiainfo.zookeeper.barrier.Barrier#start()
	 */
	@Override
	public void start() throws Exception {
		
		String barrierId = this.zk.create(BASE_PATH + "/" + BARRIER_PREFIX, new byte[0], Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL_SEQUENTIAL);
		logger.info("{} join start!", barrierId);
		List<String> children = zk.getChildren(BASE_PATH, false);
		children.remove(BARRIER);
		logger.info("children={}", children);
		
		if (children.size() >= this.barrierSize) {
			//delete start znode
			try {
				this.zk.delete(BASE_PATH + "/" + BARRIER, -1);
				logger.info("barrier is ready, barrier {} delete success!", BASE_PATH + "/" + BARRIER);
			} catch (InterruptedException | KeeperException e) {
				logger.error("delete barrier {} failed!", BASE_PATH + "/" + BARRIER);
				throw e;
			}
			//return to start work
			return;
		}
		
		synchronized (this.obj) {
			//watch for barrier
			this.zk.exists(BASE_PATH + "/" + BARRIER, this);
			this.obj.wait();
			logger.info("{} wakeup!", barrierId);
		}
	}
	
	public static void main(String[] args) throws Exception {
		
		new Thread(new LockThread(new DistributedBarrier(5))).start();
		Thread.sleep(1000);
		new Thread(new LockThread(new DistributedBarrier(5))).start();
		Thread.sleep(1000);
		new Thread(new LockThread(new DistributedBarrier(5))).start();
		Thread.sleep(1000);
		new Thread(new LockThread(new DistributedBarrier(5))).start();
		Thread.sleep(1000);
		new Thread(new LockThread(new DistributedBarrier(5))).start();
	}
	
	static class LockThread implements Runnable {

		DistributedBarrier barrier;
		LockThread(DistributedBarrier barrier) {
			this.barrier = barrier;
		}
		
		/* 
		 * @Description: TODO
		 * @see java.lang.Runnable#run()
		 */
		@Override
		public void run() {
			
			try {
				this.barrier.start();
				barrier.logger.info("start work!");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
