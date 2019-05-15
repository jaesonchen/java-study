package com.asiainfo.proxy;

import java.lang.reflect.Method;

import net.sf.cglib.proxy.Enhancer; 
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

/**
 * 
 * CGLIB代理：实现原理类似于JDK动态代理，只是它在运行期间生成的代理对象是针对目标类扩展的子类。
 * CGLIB是高效的代码生成包，底层是依靠ASM（开源的java字节码编辑类库）操作字节码实现的，性能比JDK强。
 * CGLIB是针对类来实现代理的，他的原理是对指定的目标类生成一个子类，并覆盖其中方法实现增强，
 * 但因为采用的是继承，所以不能对final修饰的方法进行代理。 
 * CGLib采用非常底层的字节码技术，可以为一个类创建一个子类，并在子类中采用方法拦截的技术拦截所有父类方法的调用，
 * 并顺势植入横切逻辑。
 * 
 * 在我们实际开发当中,在使用动态代理进行方法请求拦截时,可能会需要判断调用的方法然后决定拦截的逻辑,也就是同一个代理类在调用不同的方法时拦截的逻辑都不相同，
 * CGLIB提供了CallbackFilter来帮助我们实现这一功能。
 */
//直接使用代理类的父类作为目标业务对象。
public class BusinessCglibProxy1 implements MethodInterceptor {

	private static BusinessCglibProxy1 interceptor = new BusinessCglibProxy1();
	private BusinessCglibProxy1() {}
	
	//创建代理对象 
	public static Object getCglibProxy(Class<?> clazz) {
		//创建一个织入器 
		Enhancer enhancer = new Enhancer();
		//设置父类 
		enhancer.setSuperclass(clazz);  
		//设置需要织入的逻辑
		enhancer.setCallback(interceptor);  
		// 创建代理对象  //使用织入器创建子类
		return enhancer.create();
	}
	//由于CGLIB可以不需要实现接口来实现动态代理,其原理是通过字节码生成类的一个子类来完成的,
	//那就有可能出现需要动态代理对象不存在一个无参构造函数,那么CGLIB在生成子类并实例化时将会产生错误。
	//创建带参数的代理对象 
	public static Object getCglibProxy(Class<?> clazz, Class<?>[] argClazz, Object[] args) {
		//创建一个织入器 
		Enhancer enhancer = new Enhancer();
		//设置父类 
		enhancer.setSuperclass(clazz);  
		// 设置需要织入的逻辑
		enhancer.setCallback(interceptor);  
		// 创建带参数的代理对象  //使用织入器创建子类
		return enhancer.create(argClazz, args);
	}
	
	/**
	 * obj：cglib动态生成的代理类实例，业务类的子类的实例
	 * method：业务类方法的引用
	 * args：调用参数数组
	 * proxy：代理类对业务类方法的代理引用，是业务类的方法的代理
	 */
	@Override
	public Object intercept(Object obj, Method method, Object[] args,
			MethodProxy proxy) throws Throwable {
		
		//obj 是cglib动态生成的代理实例，是BusinessImpl的子类的实例
		System.out.println("Cglib动态代理类:" + obj.getClass().getName());

		System.out.println("Before " + method.getName() + " ..");
		
		//proxy 这里是BusinessImpl的service方法Method的代理
		//因为动态生成的代理类是子类或者是实现类，proxy.invokeSuper(Object obj, Object[] args)
		//调用的是代理类obj的父类BusinessImpl的service方法。
		//那么proxy.invoke(Object obj, Object[] args)方法是做什么的，javadoc上说这个方法可以用于 相同类中的其他对象的方法执行，
		//也就是说这个方法中的obj需要传入相同一个类的另一个对象，否则会进入无限递归循环。
		//Object result = method.invoke(new BusinessImpl(), args);
		Object result = proxy.invokeSuper(obj, args);
		System.out.println("End "+ method.getName() + " ..");
		return result;
	}

	public static void main(String[] args) {
		
		BusinessImpl proxy = (BusinessImpl) BusinessCglibProxy1.getCglibProxy(BusinessImpl.class);
		proxy.service();
		
		//带参数的业务类
		BusinessImpl proxy1 = (BusinessImpl) BusinessCglibProxy1.getCglibProxy(BusinessImpl.class, 
				new Class[] {String.class}, new Object[] {"jaesonchen"});
		proxy1.service();	
	}
}
