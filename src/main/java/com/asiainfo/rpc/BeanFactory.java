package com.asiainfo.rpc;

import java.util.HashMap;
import java.util.Map;

import com.asiainfo.rpc.test.HelloServiceImpl;

/**
 * @Description: TODO
 * 
 * @author       zq
 * @date         2017年8月22日  上午10:56:21
 * Copyright: 	  北京亚信智慧数据科技有限公司
 */
public class BeanFactory {

	private static Map<String, Object> beanFactory = new HashMap<String, Object>();
	static {
		beanFactory.put(HelloServiceImpl.class.getInterfaces()[0].getName(), new HelloServiceImpl());
	}
	
	private BeanFactory() {}
	
	@SuppressWarnings("unchecked")
	public static <T> T getBean(Class<T> t) {
		return (T) beanFactory.get(t.getSimpleName());
	}
	
	public static Object getBean(String beanName) {
		return beanFactory.get(beanName);
	}
}
