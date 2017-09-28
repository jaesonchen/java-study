package com.asiainfo.proxy;

import java.lang.reflect.InvocationHandler;  
import java.lang.reflect.Method;  
import java.lang.reflect.Proxy;  
import java.lang.reflect.UndeclaredThrowableException; 

/**
 * @author zq
 * jdk动态代理在运行期生成的字节码代理类源码
 */
public class BusinessProxyGenerateByJdk extends Proxy implements Business {
 
	private static final long serialVersionUID = 1L;
	private static Method m1;  
	private static Method m0;  
	private static Method m3;  
	private static Method m2;  
	private static Method m4;

	static {  
		try {  
			m1 = Class.forName("java.lang.Object").getMethod("equals",  
					new Class[] { Class.forName("java.lang.Object") });  
			m0 = Class.forName("java.lang.Object").getMethod("hashCode",  
					new Class[0]);  
			m3 = Class.forName("com.jaeson.javastudy.Business").getMethod("service",  
					new Class[0]);  
			m4 = Class.forName("com.jaeson.javastudy.Business").getMethod("execute",  
					new Class[0]); 
			m2 = Class.forName("java.lang.Object").getMethod("toString",  
					new Class[0]);  
		} catch (NoSuchMethodException nosuchmethodexception) {  
			throw new NoSuchMethodError(nosuchmethodexception.getMessage());  
		} catch (ClassNotFoundException classnotfoundexception) {  
			throw new NoClassDefFoundError(classnotfoundexception.getMessage());  
		}  
	}  

	public BusinessProxyGenerateByJdk(InvocationHandler invocationhandler) {  
		super(invocationhandler);  
	}  
 
	@Override  
	public final boolean equals(Object obj) {  
		try {  
			return ((Boolean) super.h.invoke(this, m1, new Object[] { obj }))  
					.booleanValue();  
		} catch (Throwable throwable) {  
			throw new UndeclaredThrowableException(throwable);  
		}  
	}  
	
	@Override  
	public final int hashCode() {  
		try {  
			return ((Integer) super.h.invoke(this, m0, null)).intValue();  
		} catch (Throwable throwable) {  
			throw new UndeclaredThrowableException(throwable);  
		}  
	}  

	@Override
	public final void service() {  
		try {  
			super.h.invoke(this, m3, null);  
			return;
		} catch (Error e) {  
		} catch (Throwable throwable) {  
			throw new UndeclaredThrowableException(throwable);  
		}  
	}
	
	@Override
	public final void execute() {  
		try {  
			super.h.invoke(this, m4, null);  
			return;
		} catch (Error e) {  
		} catch (Throwable throwable) {  
			throw new UndeclaredThrowableException(throwable);  
		}  
	}
	
	@Override  
	public final String toString() {  
		try {  
			return (String) super.h.invoke(this, m2, null);  
		} catch (Throwable throwable) {  
			throw new UndeclaredThrowableException(throwable);  
		}  
	}
	
	public static void main(String[] args) {
		
		BusinessHandler handler = new BusinessHandler(new BusinessImpl());
		new BusinessProxyGenerateByJdk(handler).service();
		new BusinessProxyGenerateByJdk(handler).execute();
	}
}  
