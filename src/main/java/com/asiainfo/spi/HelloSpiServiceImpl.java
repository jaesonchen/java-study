package com.asiainfo.spi;

/**
 * @Description: TODO
 * 
 * @author       zq
 * @date         2017年8月23日  下午2:56:03
 * Copyright: 	  北京亚信智慧数据科技有限公司
 */
public class HelloSpiServiceImpl implements SpiService {

	/* 
	 * @Description: TODO
	 * @param name
	 * @see com.asiainfo.spi.SpiService#sayHello(java.lang.String)
	 */
	@Override
	public void sayHello(String name) {
		System.out.println("hello " + name);
	}
}
