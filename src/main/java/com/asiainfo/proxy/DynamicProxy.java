package com.asiainfo.proxy;

/**
 * 动态代理常被应用到以下几种情况中
 * 数据库连接以及事物管理
 * web controller --> proxy.execute(...);
 * proxy --> connection.setAutoCommit(false);
 * proxy --> realAction.execute(); //realAction does database work;
 * proxy --> connection.commit();
 * 
 * 单元测试中的动态Mock对象
 * 自定义工厂与依赖注入（DI）容器之间的适配器
 * 类似AOP的方法拦截器
 * 
 * @author       zq
 * @date         2017年9月16日  下午9:28:14
 * Copyright: 	  北京亚信智慧数据科技有限公司
 */
public class DynamicProxy {
    
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}
}
