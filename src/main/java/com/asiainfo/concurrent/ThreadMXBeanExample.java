package com.asiainfo.concurrent;

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadInfo;
import java.lang.management.ThreadMXBean;

/**
 * @Description: 使用JMX查看一个普通java程序包括哪些线程
 * // 基于JDK1.7.0_79出的结果：
 * [5] Attach Listener		//处理attach请求的线程
 * [4] Signal Dispatcher	//分发处理发送给JVM信号的线程
 * [3] Finalizer			//调用对象finalize方法的线程
 * [2] Reference Handler	//清除Reference的线程
 * [1] main					//main线程，用户程序入口
 * 
 * @author       zq
 * @date         2017年9月3日  下午6:12:29
 * Copyright: 	  北京亚信智慧数据科技有限公司
 */
public class ThreadMXBeanExample {

	public static void main(String[] args) {

		//获取java线程管理MXBean
		ThreadMXBean threadMXBean = ManagementFactory.getThreadMXBean();
		//不需要获取同步的monitor和synchronizer信息，仅获取线程和线程堆栈信息
		ThreadInfo[] threadInfos = threadMXBean.dumpAllThreads(false, false);
		//遍历线程信息，仅打印线程id和线程名信息
		for(ThreadInfo threadInfo : threadInfos) {
			System.out.println("[" + threadInfo.getThreadId()+"] " + threadInfo.getThreadName());
		}
	}
}
