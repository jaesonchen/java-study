package com.asiainfo.insidejvm;

/**
 * 分配对象空间并初始化 --> clinit() --> init()
 * 
 * 
 * java继承类的初始化顺序：父类（静态变量、静态初始化块） > 子类 （静态变量、静态初始化块） > 父类 （变量、初始化块） > 父类构造器
 *      > 子类（变量、初始化块） > 子类构造器
 *      
 *      
 * 设计构建器时一个特别有效的规则是：用尽可能简单的方法使对象进入就绪状态；如果可能，避免调用任何方法。
 * 在构建器内唯一能够安全调用的是在基础类中具有final 属性的那些方法（也适用于private方法，它们自动具有final 属性）。
 * 
 * 
 * @author       zq
 * @date         2017年9月5日  下午4:47:44
 * Copyright: 	  北京亚信智慧数据科技有限公司
 */
public class InitialOrder {
    
    public static void main(String[] args) {

        System.out.println("main start");
        new Son();
        System.out.println("main end");
    }
}

class Son extends Parent {
    
    // 静态变量
    public static String sStaticField = "subclass--static variable";
    // 变量
    public String sField = "subclass--variable";
     // 静态初始化块
     static {
         System.out.println(sStaticField);
         System.out.println("subclass--static block init");
     }
     // 初始化块
     {
         System.out.println(sField);
         System.out.println("subclass--block init");
     }
     // 构造器
     public Son() {
         System.out.println("subclass--constructor");
         System.out.println("i=" + i + ",j=" + j);   
     }
}
class Parent {
	
	 // 静态变量
	 public static String p_StaticField = "parent--static variable";
	 // 变量
	 public String pField = "parent--variable";
	 protected int i = 1;
	 protected int j = 1;
	 // 静态初始化块
	 static {
		 System.out.println(p_StaticField);
		 System.out.println("parent--static block init");
	 }
	 // 初始化块
	 {
		 System.out.println(pField);
		 System.out.println("parent--block init");
	 }
	 // 构造器
	 public Parent() {
		 System.out.println("parent--constructor");
		 System.out.println("i=" + i + ", j=" + j);
		 j = 2;
	 }
}
