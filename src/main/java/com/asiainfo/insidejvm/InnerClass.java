package com.asiainfo.insidejvm;

/**
 * 
 * 内部类可以随意使用外部类的成员变量（包括private）而不用生成外部类的对象，这也是内部类的唯一优点。
 * 
 * 普通（非内部）类不可设为private 或protected，只允许public 或者“友好的”。
 * 
 * 非static内部类不可拥有static 成员或static 内部类。
 * 
 * 内部类拥有对外部类所有成员的访问权限，持有Outer.this。
 * 
 * 外部类拥有对内部类所有成员的访问权限, 在实例化内部类时，外部类实例会与内部类实例关联。
 * 
 * 内部类拥有对其他内部类的所有成员的访问权限。
 * 
 * 在继承类中使用同名内部类不会覆盖基类的内部类，必须使用SubInner extends Parent.Inner。
 * 
 * @author       zq
 * @date         2017年10月16日  下午4:55:50
 * Copyright: 	  北京亚信智慧数据科技有限公司
 */
public class InnerClass {
 
	public static void main(String[] args) {
		
		// 必须先有外部类的对象才能生成内部类的对象，因为内部类的作用就是为了访问外部类中的成员变量
		Outer.Inner inner = new Outer().new Inner();
		inner.show();
		inner.show1();
		
		new Outer().show2();
		new Outer().show3(166);
		
		Test test = new Outer().getInnerPrivate();
		test.test();
		
		Test annoymous = new Outer().getAnonymous();
		annoymous.test();
		
		// 可以直接new static内部类
		new Outer.InnerStatic().show();
	}

}
interface Test {
	public void test();
}
//外部类
class Outer {
	private int age = 20;
	private String name = "Outer.name";
	private static String sex = "Outer.static.sex";
	
	//friendly内部类
	class Inner {
		//不能在非static内部类中持有static成员
		//private static int aa = 1;
		private int age = 30;
		public void show() {
			//内部类在没有同名成员变量和局部变量的情况下，内部类会直接访问外部类的成员变量，而无需指定Outer.this.属性名
			System.out.println("内部类Inner直接访问外部类Outer的成员变量：" + name);
		}
		public void show1() {
			int age = 40;
			System.out.println("内部类Inner.show1()的局部变量：" + age);
			System.out.println("内部类Inner成员变量：" + this.age);
			System.out.println("内部类Inner指定Outer.this.属性名访问外部类成员变量：" + Outer.this.age);
		}
	}
	
	//private内部类
	//如果一个内部类只希望被外部类中的方法操作，那么可以使用private声明内部类
	private class InnerPrivate implements Test {
		private String innerPrivate = "inner private member";
		private InnerPrivate() {}
		private void show() {
			System.out.println("私有内部类InnerPrivate的private方法show()");
		}
		@Override
		public void test() {
			System.out.println("私有内部类InnerPrivate的public方法test()访问外部类Outer的私有变量" + name);
		}
	}
	//返回私有内部类的实例，在外部类的外面访问外部类的私有变量
	public Test getInnerPrivate() {
		return new InnerPrivate();
	}
	//在外部类内部，可以访问内部类的private成员
	public void show2() {
		new InnerPrivate().show();
	}
	//方法内部类
	//方法内部类只能访问外部成员函数的final局部变量
	public void show3(final int height) {
		final int iValue = 100;
		class InnerMethod {
			public void show() {
				System.out.println("方法内部类InnerMethod访问外部类方法Outer.show3()的final参数：" + height);
				System.out.println("方法内部类InnerMethod访问外部类方法Outer.show3()的final局部变量：" + iValue);
				System.out.println("方法内部类InnerMethod访问外部类Outer的成员变量：" + name);
			}
		}
		new InnerMethod().show();
	}
	//匿名内部类
	//内部类可以访问其他内部类的所有成员
	public Test getAnonymous() {
		final int iValue = 100;
		return new Test() {
			private String str;
			private String name;
			//一个实例初始化模块就是一个匿名内部类的构建器。缺点是不能重载。
			{
				this.str = "by anonymous class initial block";
				this.name = "anonymous name";
				System.out.println("匿名内部类实例初始化：" + this.str + ", " + this.name);
			}
			@Override
			public void test() {
				System.out.println("匿名内部类的public方法test()访问外部类Outer的成员变量：" + Outer.this.name);
				//访问在匿名内部类外部定义的一个对象，则编译器要求外部对象为final属性。
				System.out.println("匿名内部类访问外部类方法Outer.getAnonymous()的final局部变量：" + iValue);
				System.out.println("匿名内部类访问外部类的其他内部类Outer.InnerPrivate的私有成员：" + new InnerPrivate().innerPrivate);
			}
		};
	}
	
	//静态内部类
	//静态内部类只能访问外部类的静态成员变量，可以直接用 new Out.InStatic()实例化
	static class InnerStatic {
		static int iValue = 1;
		public void show() {
			System.out.println("静态内部类InnerStatic访问外部类Outer的静态成员变量：" + sex);
		}
	}
}

