package com.asiainfo.zookeeper.cluster;

import java.io.IOException;
import java.util.UUID;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooDefs.Ids;
import org.apache.zookeeper.ZooKeeper;

/**
 * @Description: TODO
 * 
 * @author       zq
 * @date         2017年8月26日  下午6:02:58
 * Copyright: 	  北京亚信智慧数据科技有限公司
 */
public class DataUpdater implements Watcher {

	final static int SESSION_TIMEOUT = 60000;
	final String root = "/members";
	ZooKeeper zk;
	
	public DataUpdater() throws KeeperException, IOException, InterruptedException {
		this.zk = new ZooKeeper("192.168.131.130:2181", SESSION_TIMEOUT, this);
		try {
			// Ensure the parent znode exists
			if (this.zk.exists(root, false) == null) {
				this.zk.create(root, "ClusterMonitorRoot".getBytes(), Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
			}
			this.zk.exists(root, true);
		} catch (KeeperException | InterruptedException ex) {
			ex.printStackTrace();
		}
	}
	
	/* 
	 * @Description: TODO
	 * @param arg0
	 * @see org.apache.zookeeper.Watcher#process(org.apache.zookeeper.WatchedEvent)
	 */
	@Override
	public void process(WatchedEvent event) {
		System.out.println("Event Received: " + event.toString());
		try {
			// Set a watch on the znode, reset the watch
			this.zk.getData(root, this, null);
			//this.zk.exists(root, true);
		} catch (KeeperException | InterruptedException ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * @Description: TODO
	 * 
	 * @throws InterruptedException
	 * @throws KeeperException
	 */
	public void run() throws InterruptedException, KeeperException {
		
		int count = 0;
		while (true && ++count < 5) {
			String uuid = UUID.randomUUID().toString();
			System.out.println("uuid=" + uuid);
			byte zoo_data[] = uuid.getBytes();
			zk.setData(root, zoo_data, -1);
			try {
				Thread.sleep(2000);
			} catch (InterruptedException ex) {
				ex.printStackTrace();
			}
		}
	}
	
	/** 
	 * @Description: TODO
	 * 
	 * @param args
	 * @throws IOException 
	 * @throws KeeperException 
	 * @throws InterruptedException 
	 */
	public static void main(String[] args) throws Exception {
		new DataUpdater().run();
	}
}
