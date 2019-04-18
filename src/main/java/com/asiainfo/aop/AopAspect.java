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

/**
 * @Description: spring aop，方法内的this自调用需要开启aspectj的expose-proxy
 * @author chenzq  
 * @date 2019年3月3日 下午6:48:41
 * @version V1.0
 */
@Component
@Aspect
public class AopAspect {
	
	final Logger logger = LoggerFactory.getLogger(getClass());

	@Pointcut("execution(* com.asiainfo..dao..*.*(..))")
    public void dataAccessOperation() {}
	
    @Pointcut("execution(* com.asiainfo..service..*.*(..))")
    public void businessService() {}
    
    @Pointcut("execution(* com.asiainfo.aop.*Service.*(..)))")
    public void aspect() {}

    // 前置通知，使用在方法businessService()上注册的切入点，同时接受JoinPoint切入点对象，可以没有该参数
	@Before("businessService()")
	public void before(JoinPoint joinPoint) {
		logger.info("before {}", joinPoint);
	}

	// 后置通知
	@After("businessService()")
	public void after(JoinPoint joinPoint) {
	    logger.info("after {}", joinPoint);
	}

	// 环绕通知
	// @Around的返回类型必须为Object，否则在织入非void返回类型的切入点时会抛出异常：
	// Null return value from advice does not match primitive return type for:
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

	// 后置返回通知
	@AfterReturning("businessService()")
	public void afterReturn(JoinPoint joinPoint) {
		logger.info("afterReturn {}", joinPoint);
	}

	// 抛出异常后通知
	@AfterThrowing(pointcut = "businessService()", throwing = "ex")
	public void afterThrow(JoinPoint joinPoint, RuntimeException ex) {
		logger.info("afterThrow {} with exception : {}", joinPoint, ex.getMessage());
	}
	
	// 前置通知，拦截返回值类型为com.jaeson.aop.User的方法
	@Before("execution(com.jaeson.aop.User com.jaeson.aop.*Service.*(..))")
	public void beforeReturnUser(JoinPoint joinPoint) {
		logger.info("beforeReturnUser {}", joinPoint);
	}

	// 前置通知，拦截参数类型为com.jaeson.aop.User的方法
	@Before("execution(* com.jaeson.op.*Service.*(com.jaeson.aop.User))")
	public void beforeArgUser(JoinPoint joinPoint) {
		logger.info("beforeArgUser {}", joinPoint);
	}

	// 前置通知，拦截含有long类型参数的方法，并将参数值注入到当前方法的形参id中
	@Before("aspect() && args(id)")
	public void beforeArgId(JoinPoint joinPoint, long id) {
		logger.info("beforeArgId {} ({})", joinPoint, id);
	}
}
