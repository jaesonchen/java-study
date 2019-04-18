package com.asiainfo.spi;

import java.util.ServiceLoader;

/**
 * @Description: TODO
 * 
 * @author       zq
 * @date         2017年8月23日  下午2:57:36
 * Copyright: 	  北京亚信智慧数据科技有限公司
 */
public class SpiServiceExample {

	/** 
	 * @Description: TODO
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		
		ServiceLoader<SpiService> loaders = ServiceLoader.load(SpiService.class);
		for (SpiService h : loaders) {
			h.sayHello("jaeson");
		}
	}
}
