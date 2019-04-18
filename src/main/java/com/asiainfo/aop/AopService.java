package com.asiainfo.aop;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.framework.AopContext;
import org.springframework.stereotype.Service;

@Service
public class AopService {
	
	final Logger logger = LoggerFactory.getLogger(getClass());
	
	//自调用无法触发aop的解决办法
	//开启暴露Aop代理到ThreadLocal支持
	//<aop:aspectj-autoproxy expose-proxy="true"/> <!—注解风格支持--> 
	//<aop:config expose-proxy="true"> <!—xml风格支持-->   
	public void selfInvocation() {
		logger.info("AopService.selfInvocation() method . . .");
		((AopService) AopContext.currentProxy()).get(0L);
	}
	
	
	//非aop方法的自调用不会触发this方法的aop
	public void selfInvoke() {
		logger.info("AopService.selfInvoke() method . . .");
		this.get(0L);
	}
	//aop方法的自调用不会触发this方法的aop
	public void selfInvoke(User user) {
		logger.info("AopService.selfInvoke(User) method . . .");
		this.save(user);
	}
	
	
	public User get(long id) {
		logger.info("AopService.get(Long) method . . .");
		return new User();
	}

	public void save(User user) {
		logger.info("AopService.save(User) method . . .");
	}

	public void delete(long id) throws RuntimeException {
		logger.info("AopService.delete(Long) method . . .");
		throw new UnsupportedOperationException("AopService.delete(Long) throw UnsupportedOperationException");
	}
}
