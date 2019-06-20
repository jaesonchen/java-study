package com.asiainfo.insidejvm;

/**
 * final & private:
 * 1. static方法不能被覆盖，因为方法覆盖是基于运行时动态绑定的，而static方法是编译时静态绑定的。
 * 2. 子类中可以创建相同签名static的方法，但是不会覆盖父类的static方法，只会隐藏父类的方法。
 * 3. 子类不能创建相同签名的非static方法，编译器会认为试图覆盖static方法而报错。
 * 
 *  - 方法重载的选择是在编译时静态绑定的，方法覆盖的选择是在运行时根据具体类型动态绑定的。
 *  - 编译时会根据方法调用时使用的接口或者基类的方法签名决定调用哪一个接口方法，运行时再根据当前对象的具体类型，
 *  - 判断接口方法在当前对象所属类的实现中是否被覆盖过，如果覆盖了就调用覆盖过的版本，否则调用接口方法。
 * 
 * final方法和private方法的区别：
 * 1. private方法默认是private final的，因为你不能在子类中访问父类的private方法（不可见），所以可以在子类中创建相同签名的方法。
 * 2. final方法通常不会是private的，所以一般在子类或者类外部可以访问，由于其在子类中可见，所以不能创建相同签名的方法。
 * 3. final的方法可以内联(inline)优化，private和static的方法也可以进行同样的优化。
 * 
 * this & super：
 * this 指向当前的对象，如果是父类里的方法代码，其this指向父类的对象，如果调用的是field或者private/final的method，则直接调用父类的；
 *  -   如果是被override过的方法，则按照多态调用。
 *  
 * super 指向是父类的引用( super() )
 * 
 * 
 * @author       zq
 * @date         2017年10月16日  下午4:54:25
 * Copyright: 	  北京亚信智慧数据科技有限公司
 */
public class FinalAndPrivate {
	
	@SuppressWarnings("static-access")
    public static void main(String[] args) {
		
		FAPParent p = new FAPParent();
		p.call();
		p.call2();
		p.print();
		p.staticMethod();
		System.out.println("=====================================");
		
		FAPParent psub = new FAPDerived();
		psub.call();
		psub.call2();
		psub.print();
		psub.staticMethod();
		System.out.println("=====================================");
		
		FAPDerived fap = new FAPDerived();
		fap.call();
		fap.call2();
		fap.print();
		fap.print("hello world");
		fap.staticMethod();
		((FAPDerived) null).staticMethod();
	}

}
class FAPParent {
	
    public String msg = "Parent msg";
    
	private void show() {
		System.out.println("private Parent.show() is calling...");
	}

	public void call() {
		System.out.println("public Parent.call() is calling...");
		this.show();
	}
	
	public void show2() {
		System.out.println("public Parent.show2() is calling...");
		System.out.println(this.msg);
	}
	
	public void call2() {
		System.out.println("public Parent.call2() is calling...");
		System.out.println("in Parent.call2(): " + this.msg);
		this.show2();
	}
	
	public final void print() {
		System.out.println("public final Parent.print() is calling...");
	}
	
	public static void staticMethod() {
		System.out.println("Parent.staticMethod() is calling...");
	}
}
class FAPDerived extends FAPParent {
	
    public String msg = "Derived msg";
    
    // 超类private不可见，可以在子类定义相同签名方法
	private void show() {
		System.out.println("private Derived.show() is calling...");
	}
	
	@Override
	public void call() {
		System.out.println("public Derived.call() is calling...");
		this.show();
	}
	
	@Override
	public void show2() {
		System.out.println("public Derived.show2() is calling...");
		System.out.println("in Derived.show2(): " + this.msg);
	}
	
	// 编译错误，不能重写(override)超类的final方法
	// public final void print() {}
	
	// 可以重载(overload)
	public final void print(String str) {
		System.out.println("public final Derived.print(String str) is calling... message = " + str);
	}
	
	// 编译错误，不能重写(override)超类的static方法
	// public void staticMethod() {}
	
	// 可以定义子类相同签名的static方法，通过子类引用超类方法时，实际上会在编译时绑定为超类符号引用
	public static void staticMethod() {
		System.out.println("Derived.staticMethod() is calling...");
	}
}