package com.asiainfo.spi;

/**
 * @Description: java spi(服务发现机制)的具体约定为:当服务的提供者，提供了服务接口的一种实现之后，在jar包的META-INF/services/目录里同时创建一个以服务接口命名的文件。
 * 				  该文件里就是实现该服务接口的具体实现类。而当外部程序装配这个模块的时候，就能通过该jar包META-INF/services/里的配置文件找到具体的实现类名，
 * 				  并装载实例化，完成模块的注入。
 * 
 * @author       zq
 * @date         2017年8月23日  下午2:55:00
 * Copyright: 	  北京亚信智慧数据科技有限公司
 */
public interface SpiService {
	void sayHello(String name);
}
