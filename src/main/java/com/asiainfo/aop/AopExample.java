package com.asiainfo.aop;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
@Aspect
public class AopExample {
	
	private static final Logger logger = LoggerFactory.getLogger(AopExample.class);

	@Pointcut("execution(* com.asiainfo..dao..*.*(..))")
    public void dataAccessOperation() {}
	
    @Pointcut("execution(* com.asiainfo..service..*.*(..))")
    public void businessService() {}
    
    @Pointcut("execution(* com.asiainfo.aop.*Service.*(..)))")
    public void aspect() {}

    /*
	 * 配置前置通知,使用在方法businessService()上注册的切入点
	 * 同时接受JoinPoint切入点对象,可以没有该参数
	 */
	@Before("businessService()")
	public void before(JoinPoint joinPoint) {
		
		logger.info("before {}", joinPoint);
	}

	//配置后置通知,使用在方法businessService()上注册的切入点
	@After("businessService()")
	public void after(JoinPoint joinPoint) {

			logger.info("after {}", joinPoint);
	}

	//配置环绕通知,使用在方法dataAccessOperation()上注册的切入点
	//@Around的返回类型必须为Object，否则在织入非void返回类型的切入点时会抛出异常：
	//Null return value from advice does not match primitive return type for:
	@Around("dataAccessOperation()")
	public Object around(JoinPoint joinPoint) throws Throwable {
		
		Object result = null;
		long start = System.currentTimeMillis();
		
		logger.info("begin around {} !", joinPoint);
		result = ((ProceedingJoinPoint) joinPoint).proceed();
		long end = System.currentTimeMillis();
		logger.info("end around {} Use time : {} ms!", joinPoint, (end - start));
		
		return result;
	}

	//配置后置返回通知,使用在方法businessService()上注册的切入点
	@AfterReturning("businessService()")
	public void afterReturn(JoinPoint joinPoint) {

		logger.info("afterReturn {}", joinPoint);
	}

	//配置抛出异常后通知,使用在方法businessService()上注册的切入点
	@AfterThrowing(pointcut="businessService()", throwing="ex")
	public void afterThrow(JoinPoint joinPoint, RuntimeException ex) {
		
		logger.info("afterThrow {} with exception : {}", joinPoint, ex.getMessage());
	}
	
	//配置前置通知,拦截返回值类型为com.jaeson.hibernatestudy.bean.User的方法
	@Before("execution(com.jaeson.hibernatestudy.bean.User com.jaeson.springstudy.aop.*Service.*(..))")
	public void beforeReturnUser(JoinPoint joinPoint) {

		logger.info("beforeReturnUser {}", joinPoint);
	}

	//配置前置通知,拦截参数类型为com.jaeson.hibernatestudy.bean.User的方法
	@Before("execution(* com.jaeson.springstudy.aop.*Service.*(com.jaeson.hibernatestudy.bean.User))")
	public void beforeArgUser(JoinPoint joinPoint) {

		logger.info("beforeArgUser {}", joinPoint);
	}

	//配置前置通知,拦截含有long类型参数的方法,并将参数值注入到当前方法的形参id中
	@Before("aspect() && args(id)")
	public void beforeArgId(JoinPoint joinPoint, long id) {

		logger.info("beforeArgId {} ({})", joinPoint, id);
	}
}
