package com.asiainfo.zookeeper;

/**
 * @Description: 
 * IZkStateListener接口，当状态发生改变时进行通知，如果状态是过期，那么会进行重连，重连成功建立新会话后会进行通知，重连失败后无法建立会话也会进行通知
 * 
 * IZkDataListener接口，当节点发生改变时进行通知，包括节点的创建、更新和删除
 * 
 * IZkChildListener接口，当子节点发生改变时进行通知父节点，包括子节点的创建和删除，不包括子节点数据的更新
 * 
 * IZkConnection接口，逻辑上是一个到zk的连接，完成对zk的所有操作，包括节点的创建、删除和更新等等
 * 
 * ZkConnection类是对IZkConnection接口的实现， 通过把所有的操作委托给Apache的ZooKeeper类来实现
 * 
 * ZkEventThread类继承了Thread，单独的一个线程进行事件通知，避免阻塞zk通知ZkClient的线程。该线程把要通知的内容封装到一个ZkEvent对象中，
 * 然后把该对象添加到事件线程的阻塞队列中，事件线程负责从阻塞队列中取出对象并执行对象的方法，完成具体的通知动作
 * 
 * ZkClient类实现了Apache的Watcher接口，所以本身是一个Watcher，当zk的状态改变或有节点操作时其实通知的是ZkClient自己，执行的是自己实现的Watcher接口的process方法，
 * 在这里对状态和事件类型进行解析然后再把通知内容封装成ZkEvent对象放入事件线程的阻塞队列，由事件线程完成后续的通知。
 * 
 * 在实例化ZkClient时会去连接zk，实际是委托给Apache的ZooKeeper来实现。由于连接是异步的，需要一个Watcher来被告知连接成功还是失败，这个Watcher就是正在实例化的ZkClient对象
 * 
 * 首先在主线程实例化ZkClient，在创建了到zk的连接后就阻塞自己；当状态发生改变后zk会通过另一个zookeeper事件线程来通知ZkClient对象调用process方法，
 * 把zk返回的状态设置到ZkClient对象，并发信号解除主线程的阻塞，主线程判断该状态，如果成功继续执行，如果失败则再次阻塞，
 * 因为Apache的ZooKeeper具有重连功能。然后就重复以上过程，直到成功或等到超时后还未成功就抛出异常
 * 
 * 所有的操作在主线程进行，遇到连接丢失或会话过期时都会进行阻塞等待，因为Apache的ZooKeeper会进行重连，成功后通过zookeeper的事件线程通知ZkClient，
 * 继而会发信号解除主线程阻塞，或者因为超时主线程自动解除阻塞，主线程并不去判断是否连接上而是直接执行操作，然后重复以上过程 
 * 
 * @author       zq
 * @date         2017年8月29日  下午1:34:20
 * Copyright: 	  北京亚信智慧数据科技有限公司
 */
public class ZkClientExample {

	/** 
	 * @Description: TODO
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}
}
