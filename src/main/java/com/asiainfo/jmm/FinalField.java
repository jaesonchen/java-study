package com.asiainfo.jmm;

/**
 * 一个对象的final字段值是在它的构造方法里面设置的。假设对象被正确的构造了，一旦对象被构造，在构造方法里面设置给final字段的的值在没有同步的情况下对所有其他的线程都会可见。
 * 另外，引用这些final字段的对象或数组都将会看到final字段的最新值。这儿的“正确的”的意思是“对象构造方法结尾的最新的值”而不是“最新可用的值”。
 * 
 * 对一个对象来说，被正确的构造意味着这个正在构造的对象的引用在构造期间没有被允许逸出。
 * 换句话说，不要让其他线程在其他地方能够看见一个构造期间的对象引用。不要指派给一个静态字段，不要作为一个listener注册给其他对象等等。
 * 这些操作应该在构造方法之后完成，而不是构造方法中来完成。
 * 
 * @author       zq
 * @date         2017年9月27日  下午4:04:23
 * Copyright: 	  北京亚信智慧数据科技有限公司
 */
public class FinalField {

	final int x;
	int y;
	static FinalField f;
	
	public FinalField() {
		x = 3;
		y = 4;
	}
	
	public FinalField(Object obj) { // bad!
		x = 3;
		y = 4;
		// bad construction - allowing this to escape
		//从global.obj中读取this的引用线程不会保证读取到的Global.obj.x的值为3
		Global.obj = this;
	}

	static void writer() {
		f = new FinalField();
	}

	static void reader() {
		if (f != null) {
			//一个正在执行reader方法的线程保证看到f.x的值为3，因为它是final字段。它不保证看到f.y的值为4，因为f.y不是final字段。
			System.out.println(f.x);
			System.out.println(f.y);
		}
	}
}
class Global {
	static FinalField obj;
}
