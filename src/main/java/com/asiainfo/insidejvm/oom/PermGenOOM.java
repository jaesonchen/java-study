package com.asiainfo.insidejvm.oom;

import java.lang.reflect.Method;

import net.sf.cglib.proxy.Enhancer;  
import net.sf.cglib.proxy.MethodInterceptor;  
import net.sf.cglib.proxy.MethodProxy;  
  
public class PermGenOOM {
  
    /**
     * @Described：常量池溢出
     * @VM args : -XX:PermSize=10M -XX:MaxPermSize=10M
     */  
    public static void main(String[] args) throws InterruptedException {
    	
    	 while(true){

             Enhancer enhancer = new Enhancer();
             enhancer.setSuperclass(OOMObject.class);
             enhancer.setUseCache(false);
             enhancer.setCallback(new MethodInterceptor() {
                //@Override
                public Object intercept(Object arg0, Method arg1, Object[] arg2, MethodProxy arg3) throws Throwable {
                	return arg3.invokeSuper(arg0, arg2);
                }
             });
             enhancer.create();
         }
    }  
}
class OOMObject {}