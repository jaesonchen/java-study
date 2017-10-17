package com.asiainfo.proxy;

import java.lang.reflect.Method;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;
import net.sf.cglib.proxy.Callback;
import net.sf.cglib.proxy.CallbackFilter;

/**
 * 在真实开发当中,在使用动态代理进行方法请求拦截时,可能会需要判断调用的方法然后决定拦截的逻辑,
 * 也就是同一个代理类在调用不同的方法时拦截的逻辑都不相同,CGLIB提供了CallbackFilter来帮助我们实现这一功能 
 * 
 * @author       zq
 * @date         2017年10月16日  下午1:39:25
 * Copyright: 	  北京亚信智慧数据科技有限公司
 */
public class BusinessCglibProxyFilter {

	private static Callback callback1 = new MethodInterceptor() {
		
		@Override  
		public Object intercept(Object obj, Method method, Object[] args,  
				MethodProxy proxy) throws Throwable {
			
			System.out.println("before callback1 ..");
			Object result = proxy.invokeSuper(obj, args);
			System.out.println("after callback1 ..");
			return result;  
		}  
	};

	private static Callback callback2 = new MethodInterceptor() {
		
		@Override  
		public Object intercept(Object obj, Method method, Object[] args,  
				MethodProxy proxy) throws Throwable {
			
			System.out.println("before callback2 ..");
			Object result = proxy.invokeSuper(obj, args);
			System.out.println("after callback2 ..");
			return result;  
		}  
	};
	
	private static CallbackFilter callbackFilter = new CallbackFilter() {  
		
		static final int EXECUTE_METHOD = 0;  
		static final int OTHER_METHOD = 1;  

		/* 
		 * accept需要返回一個int类型, 
		 * 该int类型为上文中setCallbacks设置的多个 
		 * Callback处理逻辑的数组的下标
		 */
		@Override  
		public int accept(Method method) {
			
			/* 
			 * Method参数代表代理类的执行方法,  
			 * 以下logic为 判断执行方法名称是否为execute, 
			 * 是则执行callback1,也就是数组下标为0的逻辑,  
			 * 否则执行Other逻辑 
			 */  
			if ("execute".equals(method.getName())) {
				return EXECUTE_METHOD;
			}
			else {
				return OTHER_METHOD;
			}
		}  
	};
	
	private BusinessCglibProxyFilter() { }

	//创建代理对象 
	public static Object getCglibProxy(Class<?> clazz) {
		
		//创建一个织入器 
		Enhancer enhancer = new Enhancer();
		//设置父类 
		enhancer.setSuperclass(clazz);  
		//设置需要织入的逻辑
		enhancer.setCallbacks(new Callback[] { callback1, callback2 });
		//设置回调方法过滤
		enhancer.setCallbackFilter(callbackFilter);
		// 创建代理对象  //使用织入器创建子类
		return enhancer.create();
	}
	
	//由于CGLIB可以不需要实现接口来实现动态代理,其原理是通过字节码生成类的一个子类来完成的,
	//那就有可能出现需要动态代理对象不存在一个无参构造函数,
	//那么CGLIB在生成子类并实例化时将会产生错误。
	//创建带参数的代理对象 
	public static Object getCglibProxy(Class<?> clazz, Class<?>[] args, Object[] argsValue) {
		
		//创建一个织入器 
		Enhancer enhancer = new Enhancer();
		//设置父类 
		enhancer.setSuperclass(clazz);  
		//设置需要织入的逻辑
		enhancer.setCallbacks(new Callback[] { callback1, callback2 });
		//设置回调方法过滤
		enhancer.setCallbackFilter(callbackFilter);
		// 创建带参数的代理对象  //使用织入器创建子类
		return enhancer.create(args, argsValue);
	}
	
	public static void main(String[] args) {
		BusinessImpl proxy = (BusinessImpl) BusinessCglibProxyFilter.getCglibProxy(BusinessImpl.class);
		proxy.service();
		proxy.execute();
		//带参数的业务类
		BusinessImpl proxy1 = (BusinessImpl) BusinessCglibProxyFilter.getCglibProxy(BusinessImpl.class, 
				new Class[] {String.class}, new Object[] {"jaesonchen"});
		proxy1.service();
		proxy1.execute();
	}
}
