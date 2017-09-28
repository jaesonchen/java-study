package com.asiainfo.insidejvm;

/**
 * 初始化的实际过程是这样的：
 * (1) 在采取其他任何操作之前，为对象分配的存储空间初始化成二进制零。
 * (2) 就象前面叙述的那样，调用基础类构建器。此时，被覆盖的draw()方法会得到调用，此时会发现radius 的值为0，这是由于步骤(1)造成的。
 * (3) 按照原先声明的顺序调用成员初始化代码。
 * (4) 调用衍生类构建器的主体。
 * 
 * 设计构建器时一个特别有效的规则是：用尽可能简单的方法使对象进入就绪状态；如果可能，避免调
 * 用任何方法。在构建器内唯一能够安全调用的是在基础类中具有final 属性的那些方法（也适用于private方法，它们自动具有final 属性）。
 * 
 * 
 * java继承类的初始化顺序：父类（静态变量、静态初始化块） > 子类 （静态变量、静态初始化块） > 父类 （变量、初始化块） > 父类构造器
 * 		> 子类（变量、初始化块） > 子类构造器
 * 
 * @author       zq
 * @date         2017年9月5日  下午4:47:44
 * Copyright: 	  北京亚信智慧数据科技有限公司
 */
public class InitialOrder extends Parent {

	// 静态变量
	 public static String s_StaticField = "subclass--static variable";
	 // 变量
	 public String s_Field = "subclass--variable";
	 // 静态初始化块
	 static {
		 System.out.println(s_StaticField);
		 System.out.println("subclass--static block init");
	 }
	 // 初始化块
	 {
		 System.out.println(s_Field);
		 System.out.println("subclass--block init");
	 }
	
	// 构造器
	public InitialOrder() {
		System.out.println("subclass--constructor");
		System.out.println("i=" + i + ",j=" + j);	
	}
	
	/** 
	 * @Description: TODO
	 * 
	 * @param args
	 */
	public static void main(String[] args) {

		System.out.println("subclass main");
		new InitialOrder();
	}
}

class Parent {
	
	 // 静态变量
	 public static String p_StaticField = "parentclass--static variable";
	 // 变量
	 public String p_Field = "parentclass--variable";
	 protected int i = 1;
	 protected int j = 1;
	 // 静态初始化块
	 static {
		 System.out.println(p_StaticField);
		 System.out.println("parentclass--static block init");
	 }
	 // 初始化块
	 {
		 System.out.println(p_Field);
		 System.out.println("parentclass--block init");
	 }
	 // 构造器
	 public Parent() {
		 System.out.println("parentclass--constructor");
		 System.out.println("i=" + i + ", j=" + j);
		 j = 2;
	 }
}
