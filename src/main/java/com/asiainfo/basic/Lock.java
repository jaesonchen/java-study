package com.asiainfo.basic;

/**   
 * Lock的种类：自旋锁、阻塞锁、可重入锁
 * 1. 自旋锁是采用让当前线程不停地的在循环体内执行CAS实现的，当循环的条件被其他线程改变时 才能进入临界区
 * 2. 阻塞锁是让线程进入阻塞状态进行等待，当获得相应的信号（唤醒，时间） 时，才可以进入线程的准备就绪状态，准备就绪状态的所有线程，通过竞争，进入运行状态。
 *  -  阻塞锁的优势在于，阻塞的线程不会占用cpu时间， 不会导致 CPu占用率过高，但进入时间以及恢复时间都要比自旋锁略慢。在竞争激烈的情况下 阻塞锁的性能要明显高于 自旋锁。
 * 3. 可重入锁，也叫做递归锁，指的是同一线程 外层函数获得锁之后 ，内层递归函数仍然有获取该锁的代码，但不受影响。在JAVA环境下 ReentrantLock 和synchronized 都是 可重入锁
 * 
 * JAVA中，能够进入\退出、阻塞状态或包含阻塞锁的方法有:
 * synchronized 关键字（其中的重量锁）
 * ReentrantLock
 * Object.wait() / notify()
 * LockSupport.park() / unpart() (j.u.c经常使用)
 * 
 * @author chenzq  
 * @date 2019年5月13日 下午9:45:28
 * @version V1.0
 * @Copyright: Copyright(c) 2019 jaesonchen.com Inc. All rights reserved. 
 */
public class Lock {

}
