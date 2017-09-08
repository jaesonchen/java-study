package com.asiainfo.zookeeper.lock;

/**
 * @Description: TODO
 * 
 * @author       zq
 * @date         2017年8月26日  下午10:46:49
 * Copyright: 	  北京亚信智慧数据科技有限公司
 */
public interface DistributedLock {

	void acquire() throws Exception;
	
	void release() throws Exception;
}
