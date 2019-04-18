package com.asiainfo.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.lang.reflect.Method;

/**
 * 
 * jdk动态代理：
 * jdk动态代理在运行期间动态生成实现业务类的所有接口的代理类com.sun.proxy.$Proxy，代理类持有实现AOP逻辑的handler，handler持有被代理的业务类对象。
 * 当通过代理类调用业务方法时，代理类$Proxy构筑业务类的方法的Method结构信息，回调handler的invoke方法，实现AOP切面逻辑并调用业务类的实际方法。
 * 
 */
public class BusinessJdkProxy {

	public static void main(String[] args) {
		
		Business business = new BusinessImpl("chenzq");
		InvocationHandler handler = new BusinessHandler(business);
		Business proxy = (Business) Proxy.newProxyInstance(
				business.getClass().getClassLoader(), 
				business.getClass().getInterfaces(),
				handler);
		
		proxy.service();		
	}
}
class BusinessHandler implements InvocationHandler {
	
	private Object target;
	public BusinessHandler(Object target) {
		this.target = target;
	}
	
	public Object getTarget() {
		return this.target;
	}

	/*
	 * @param proxy  jdk动态代理类的实例
	 * @param method 包含业务类的service方法的结构信息
	 * @param args   调用参数
	 * @return
	 * @throws Throwable
	 */
	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		
		System.out.println("JDK动态代理类：" + proxy.getClass().getName());
		System.out.println("Before " + method.getName() + " ..");
		Object result = method.invoke(target, args);
		System.out.println("End "+ method.getName() + " ..");
		return result;
	}
}
