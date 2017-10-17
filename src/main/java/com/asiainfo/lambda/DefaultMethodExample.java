package com.asiainfo.lambda;

/**
 * 为什么要有Default方法
 * 在Java8发布之际，有件事情就显得非常重要，即在不破坏Java现有实现架构的情况下能往接口里增加新方法。
 * 引入Default方法到Java8，正是为了这个目的：优化接口的同时，避免跟现有实现架构的兼容问题。
 * 
 * foreach方法既没有在java.util.List中声明，也没有在java.util.Collection中声明。
 * （如果要使上面代码生效）容易想到的方案是在现有的接口中新增foreach方法，并在JDK中必要的地方实现foreach。
 * 然而，一经发布，要想在某个接口中增加方法，而不修改现该接口现有的实现类，这是不可能做到的。
 * 这样，即使我们把Lambda表达式引入到java8中，但是因为不能牺牲向后兼容，而不可以把Lambda表达式和标准集合类库结合使用的话，会真的让人很泄气。
 * 为了解决上述问题，引入了一个新的概念，即虚拟扩展方法（Virtual extension methods），通常也称之为 defender 方法，它目前可以添加到接口中，为声明的方法提供默认的实现。
 * 尽管如此，Default 方法不适合过多使用，但是对于Java集合API的优化升级，并达到无缝地结合Lambda表达式来说，Default 方法是至关重要的特性。
 * 
 * 当一个类实现的多个接口中有相同签名的default方法时，必须在实现类中重新实现该default方法，否则编译报错，
 * 需要引用接口default实现时，使用  InterfaceName.super.method()引用。
 * 
 * @author       zq
 * @date         2017年9月5日  上午9:32:22
 * Copyright: 	  北京亚信智慧数据科技有限公司
 */
public class DefaultMethodExample {

	/** 
	 * @Description: TODO
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		
		A di = new A() {};
		di.show();
		
		new C().show();
	}
}

interface A {
	default void show() {
		System.out.println("default method A.show()");
	}
}

interface B {
	default void show() {
		System.out.println("default method B.show()");
	}
}

class C implements A, B {
	
	@Override
	public void show() {
		System.out.println("default method C.show()");
		A.super.show();
		B.super.show();
	}
}





