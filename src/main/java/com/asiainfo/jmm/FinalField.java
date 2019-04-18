package com.asiainfo.jmm;

/**
 * 一个对象的final字段值是在它的构造方法里面设置的(static 在clinit()，非static 在init())。
 * 假设对象被正确的构造了，一旦对象被构造，在构造方法里面设置给final字段的的值在没有同步的情况下对所有其他的线程都会可见。
 * 
 * 对一个对象来说，被正确的构造意味着这个正在构造的对象的引用在构造期间没有被允许逸出。
 * 换句话说，不要让其他线程在其他地方能够看见一个构造期间的对象引用。不要指派给一个静态字段，不要作为一个listener注册给其他对象等等。
 * 这些操作应该在构造方法之后完成，而不是构造方法中来完成。
 * 
 * 
 * JSR-133为什么要增强final的语义:
 * 在旧的Java内存模型中 ，最严重的一个缺陷就是线程可能看到final域的值会改变。比如，一个线程当前看到一个整形final域的值为0（还未初始化之前的默认值），
 * 过一段时间之后这个线程再去读这个final域的值时，却发现值变为了1（被某个线程初始化之后的值）。
 * 为了修补这个漏洞，JSR-133增强了final的语义。通过为final域增加写和读重排序规则，可以为java程序员提供初始化安全保证：
 * 只要对象是正确构造的（被构造对象的引用在构造函数中没有“逸出”），那么不需要使用同步（指lock和volatile的使用），
 * 就可以保证任意线程都能看到这个final域在构造函数中被初始化之后的值。
 * 
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
