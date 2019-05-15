package com.asiainfo.executor;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * ThreadFactory 工厂类，用于自定义线程的名称
 * 
 * @author       zq
 * @date         2017年10月16日  下午4:08:08
 * Copyright: 	  北京亚信智慧数据科技有限公司
 */
public class CustomThreadFactory implements ThreadFactory {

	private AtomicInteger counter = new AtomicInteger(0);
	private String prefix = "default";
	public CustomThreadFactory() {}
	public CustomThreadFactory(String prefix) {
		this.prefix = prefix;
	}

	@Override
	public Thread newThread(Runnable r) {
		return new Thread(r, prefix + "-" + counter.getAndIncrement());
	}
}
