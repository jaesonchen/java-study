package com.asiainfo.insidejvm;

/**
 * 
 * static方法不能被覆盖，因为方法覆盖是基于运行时动态绑定的，而static方法是编译时静态绑定的。
 * 子类中可以创建相同签名static的方法，但是不会覆盖父类的static方法，只会隐藏父类的方法。
 * 不能创建相同签名的非static方法，编译器会认为试图覆盖static方法而报错。
 * 一旦一个方法或者域在子类中被覆写，你就不能在子类的实例上调用它了（除了在子类内部，通过使用super 关键字来方法）。
 * 
 * 方法重载的选择是在编译时静态绑定的，方法覆盖的选择是在运行时根据具体类型动态绑定的。
 * 说白了，编译时会根据方法调用时使用的接口或者基类的方法签名决定调用哪一个接口方法，运行时再根据当前对象的具体类型，
 * 判断接口方法在当前对象所属类的实现中是否被覆盖过，如果覆盖了就调用覆盖过的版本，否则调用接口方法。
 * 
 * final方法和private方法的区别:
 * 
 * 类中任何private方法默认是private final的，因为你不能访问一个private方法，所以你不能覆盖它。
 * （虽然当你试图覆盖一个private方法时，编译器没有给出错提示，但你并没有覆盖这个方法，你只是创建了一个新的方法而已。）
 * final方法通常不会是private的，所以一般在子类或者类外部可以访问，由于其在子类中可见，所以不能创建相同签名的方法。
 * 
 * 说白了private方法只可以在类的内部使用，在类外根本访问不到，而final方法可以在类外访问，
 * 但是不可以重写该方法（在子类中试图覆盖时编译器会给出错误提示，申明final方法不能被覆盖。），
 * 就是说可以使用该方法的功能但是不可以改变其功能，这就是private方法和final方法的最大区别。
 * 
 * final的方法可以内联(inline)优化，private的和static的也可以进行同样的优化。
 * 
 * @author       zq
 * @date         2017年10月16日  下午4:54:25
 * Copyright: 	  北京亚信智慧数据科技有限公司
 */
public class FinalAndPrivate {
	
	@SuppressWarnings("all")
	public static void main(String[] args) {
		
		FAPParent p = new FAPParent();
		p.call();
		p.call2();
		p.print();
		p.staticMethod();
		System.out.println("=====================================");
		
		FAPParent psub = new FAP();
		psub.call();
		psub.call2();
		psub.print();
		psub.staticMethod();
		System.out.println("=====================================");
		
		FAP fap = new FAP();
		fap.call();
		fap.call2();
		fap.print();
		fap.print("hello world");
		fap.staticMethod();
	}

}
class FAPParent {
	
	private void show() {
		System.out.println("super.show() is calling...");
	}
	public void call() {
		System.out.println("super.call() is calling...");
		this.show();
	}
	
	private final void show2() {
		System.out.println("super.show2() is calling...");
	}
	public void call2() {
		System.out.println("super.call2() is calling...");
		this.show2();
	}
	
	public final void print() {
		System.out.println("super.print() is calling...");
	}
	
	public static void staticMethod() {
		System.out.println("super.staticMethod() is calling...");
	}
}
class FAP extends FAPParent {
	
	private void show() {
		System.out.println("FAP.show() is calling...");
	}
	
	@Override
	public void call() {
		System.out.println("FAP.call() is calling...");
		this.show();
		//编译错误，super.show() is not visible
		//super.show();
	}
	
	public void show2() {
		System.out.println("FAP.show2() is calling...");
	}
	
	/* 
	 * 编译错误，不能覆盖(override)超类的final方法
	public final void print() {
		System.out.println("super.print() is calling...");
	}*/
	//可以重载(overload)
	public final void print(String str) {
		System.out.println("FAP.print(String str) is calling... message = " + str);
	}
	
	/* 
	 * 编译错误，不能覆盖(override)超类的static方法
	public void staticMethod() {
		System.out.println("FAP.staticMethod() is calling...");
	}
	 */
	public static void staticMethod() {
		System.out.println("FAP.staticMethod() is calling...");
	}
}