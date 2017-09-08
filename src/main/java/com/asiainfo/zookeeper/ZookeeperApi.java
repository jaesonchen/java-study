package com.asiainfo.zookeeper;

import java.util.concurrent.CountDownLatch;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.Watcher.Event.KeeperState;
import org.apache.zookeeper.ZooDefs.Ids;
import org.apache.zookeeper.ZooKeeper;

/**
 * @Description: ZooKeeper API基本的操作示例
 * 
 * @author       zq
 * @date         2017年8月24日  上午10:08:27
 * Copyright: 	  北京亚信智慧数据科技有限公司
 */
public class ZookeeperApi {

	final static int SESSION_TIMEOUT = 60000;
	
	/** 
	 * @Description: TODO
	 * 
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
		
		final CountDownLatch countDownLatch = new CountDownLatch(1);
		// 创建一个服务器的连接
		ZooKeeper zk = new ZooKeeper("192.168.131.130:2181", SESSION_TIMEOUT, new Watcher() {
			@Override
			public void process(WatchedEvent event) {
				KeeperState state = event.getState();
				if (state == KeeperState.SyncConnected) {
					countDownLatch.countDown();
				}
				System.out.println("已经触发了" + event.getType() + "事件!");
			}
		});
		countDownLatch.await();
		// 创建一个目录节点
		String rPath = zk.create("/testRootPath", "testRootData".getBytes(), Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
		System.out.println("创建节点成功 [" + rPath + "]");
		// 创建一个子目录节点
		String childOnePath = zk.create("/testRootPath/testChildPathOne", "testChildDataOne".getBytes(),
				Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
		System.out.println(
				"创建子节点成功 [" + childOnePath + "] [" + new String(zk.getData("/testRootPath", false, null)) + "]");
		// 取出子目录节点列表
		System.out.println(zk.getChildren("/testRootPath", true));
		//修改子目录节点数据
		zk.setData("/testRootPath/testChildPathOne", "modifyChildDataOne".getBytes(), -1);
		System.out.println("目录节点状态：[" + zk.exists("/testRootPath", true) + "]");
		//创建另外一个子目录结点
		String childTwoPath = zk.create("/testRootPath/testChildPathTwo", "testChildDataTwo".getBytes(),
				Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
		System.out.println(
				"创建子节点成功 [" + childTwoPath + "] [" + new String(zk.getData("/testRootPath/testChildPathTwo", true, null)) + "]");
		//删除子目录结点
		zk.delete("/testRootPath/testChildPathTwo", -1);
		zk.delete("/testRootPath/testChildPathOne", -1);
		//删除父目录结点
		zk.delete("/testRootPath", -1);
		//关闭连接
		zk.close();
	}
}
