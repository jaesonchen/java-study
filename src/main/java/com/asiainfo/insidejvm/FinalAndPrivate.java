package com.asiainfo.insidejvm;

/**
 * 
 * static方法不能被覆盖，因为方法覆盖是基于运行时动态绑定的，而static方法是编译时静态绑定的。
 * 子类中可以创建相同签名static的方法，但是不会覆盖父类的static方法，只会隐藏父类的方法。
 * 不能创建相同签名的非static方法，编译器会认为试图覆盖static方法而报错。
 * 
 * 方法重载的选择是在编译时静态绑定的，方法覆盖的选择是在运行时根据具体类型动态绑定的。
 * 编译时会根据方法调用时使用的接口或者基类的方法签名决定调用哪一个接口方法，运行时再根据当前对象的具体类型，
 * 判断接口方法在当前对象所属类的实现中是否被覆盖过，如果覆盖了就调用覆盖过的版本，否则调用接口方法。
 * 
 * final方法和private方法的区别:
 *  类中的private方法默认是private final的，因为你不能访问一个private方法，所以你不能覆盖它。
 *  final方法通常不会是private的，所以一般在子类或者类外部可以访问，由于其在子类中可见，所以不能创建相同签名的方法。
 *  final的方法可以内联(inline)优化，private和static的方法也可以进行同样的优化。
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
		System.out.println("private Parent.show() is calling...");
	}
	public void call() {
		System.out.println("public Parent.call() is calling...");
		this.show();
	}
	
	private final void show2() {
		System.out.println("private final Parent.show2() is calling...");
	}
	
	public void call2() {
		System.out.println("public Parent.call2() is calling...");
		this.show2();
	}
	
	public final void print() {
		System.out.println("public final Parent.print() is calling...");
	}
	
	public static void staticMethod() {
		System.out.println("Parent.staticMethod() is calling...");
	}
}
class FAP extends FAPParent {
	
    // 相同签名，不是覆盖
	private void show() {
		System.out.println("private FAP.show() is calling...");
	}
	
	@Override
	public void call() {
		System.out.println("public FAP.call() is calling...");
		this.show();
		//编译错误，super.show() is not visible
		//super.show();
	}
	
	public void show2() {
		System.out.println("public FAP.show2() is calling...");
	}
	
	/* 
	 * 编译错误，不能覆盖(override)超类的final方法
	public final void print() {
		System.out.println("FAP.print() is calling...");
	}*/
	//可以重载(overload)
	public final void print(String str) {
		System.out.println("public final FAP.print(String str) is calling... message = " + str);
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