package com.asiainfo.zookeeper.cluster;

import java.io.IOException;
import java.util.List;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.Watcher.Event.EventType;
import org.apache.zookeeper.Watcher.Event.KeeperState;
import org.apache.zookeeper.ZooDefs.Ids;
import org.apache.zookeeper.ZooKeeper;

/**
 * @Description: TODO
 * 
 * @author       zq
 * @date         2017年8月26日  下午5:05:58
 * Copyright: 	  北京亚信智慧数据科技有限公司
 */
public class ClusterMonitor implements Watcher, Runnable {

	static final int SESSION_TIMEOUT = 60000;
	final String root = "/members";
	final Object obj = new Object();
	ZooKeeper zk;

	public ClusterMonitor() throws KeeperException, IOException, InterruptedException {
		this.zk = new ZooKeeper("192.168.131.130:2181", SESSION_TIMEOUT, this);
	}
	
	/* 
	 * @Description: TODO
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		
		synchronized (this.obj) {
			while (true) {
				try {
					this.obj.wait();
					System.out.println("wakeup by watcher!");;
				} catch (InterruptedException ex) {
					ex.printStackTrace();
				}
			}
		}
	}
	
	/* 
	 * @Description: TODO
	 * @param arg0
	 * @see org.apache.zookeeper.Watcher#process(org.apache.zookeeper.WatchedEvent)
	 */
	@Override
	public void process(WatchedEvent event) {
		
		try {
			if (event.getType() == EventType.None && event.getState() == KeeperState.SyncConnected) {
				System.out.println("Event Received:" + event.toString());
				// Ensure the parent znode exists
				if (this.zk.exists(root, false) == null) {
					this.zk.create(root, "ClusterMonitorRoot".getBytes(), Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
				}
				// Set a watch on the znode
				this.zk.getData(root, this, null);
				// Set a watch on the parent znode
				List<String> children = this.zk.getChildren(root, this);
				System.out.println("Members:" + children);
			}
			
			if (event.getType() == EventType.NodeDataChanged) {
				System.out.println("Cluster NodeDataChanged");
				synchronized (this.obj) {
					//trigger only once, reset the watch
					this.zk.getData(root, this, null);
					//wakeup wait
					this.obj.notifyAll();
				}
			}
	
			if (event.getType() == EventType.NodeChildrenChanged) {
				System.out.println("!!!Cluster Membership Change!!!");
				// Get current list of child znode, reset the watch
				List<String> children = this.zk.getChildren(root, this);
				System.out.println("Members:" + children);
			}
		} catch (KeeperException | InterruptedException ex) {
			ex.printStackTrace();
		}
	}
	
	public static void main(String[] args) throws Exception {
		new ClusterMonitor().run();
	}
}
