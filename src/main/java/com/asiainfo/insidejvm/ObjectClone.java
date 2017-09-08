package com.asiainfo.insidejvm;

/**
 * Object.clone()会检查原先的对象有多大，再为新对象腾出足够多的内存，将所有二进制位从原来的对象复制到新对象。
 * 
 * 对于基本数据类型，直接复制，对于引用类型，如果引用类型或其基类实现了Cloneable接口，则调用该对象的clone复制引用类型对象，
 * 如果没有实现Cloneable接口，则直接复制对象句柄，实际上复制后的对象和原对象指向同一个引用类型。
 * 
 * 类或者基类必须实现标志空接口Cloneable，否则在调用clone方法时会抛出CloneNotSupportedException异常。
 * 
 * 克隆时要注意的两个关键问题是：几乎肯定要调用super.clone()，以及注意将克隆设为public。
 * 
 * 调用Object.clone()时，根类中的clone()方法负责建立正确的存储容量，并通过“按位复制”
 * 二进制位从原始对象中复制到新对象的存储空间。也就是说，它并不只是预留存储空间以及复制一个对象
 * ——实际需要调查出欲复制之对象的准确大小，然后复制那个对象。
 * 
 * 不管我们要做什么，克隆过程的第一个部分通常都应该是调用super.clone()。通过进行一次准确的复制，
 * 这样做可为后续的克隆进程建立起一个良好的基础。随后，可采取另一些必要的操作，以完成最终的克隆。
 * 
 */
public class ObjectClone implements Cloneable {
	private int i;
	private Handler handler = new Handler(101, "chenzq");
	public ObjectClone(int i) {
		this.i = i;
	}
	public void increase() {
		this.i++;
	}
	public void changeHandler(String str) {
		this.handler.setStr(str);
	}
	public int getI() {
		return this.i;
	}
	public Handler getHandler() {
		return this.handler;
	}
	@Override public Object clone() {
		ObjectClone o = null;
		try {
			o = (ObjectClone) super.clone();
		} catch (CloneNotSupportedException ex) {
			ex.printStackTrace();
		}
		o.handler = (Handler) o.handler.clone();
		return o;
	}
	public static void main(String[] args) {
		
		ObjectClone o = new ObjectClone(100);
		ObjectClone oc = (ObjectClone) o.clone();
		o.increase();
		System.out.println("o.i = " + o.getI() + ", oc.i = " + oc.getI());
		o.changeHandler("jaesonchen");
		System.out.println("o.handler = " + o.handler);
		System.out.println("oc.handler = " + oc.handler);
	}
}

class Handler implements Cloneable {
	private int j = 100;
	private String str = "default";
	public Handler() { }
	public Handler(int j, String str) {
		this.j = j;
		this.str = str;
	}
	public void setStr(String str) {
		this.str = str;
	}
	@Override public Object clone() {
		Object o = null;
		try {
			o = super.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		return o;
	}
	@Override public String toString() {
		return "Handler.j = " + j + ", Handler.str = " + str;
	}
}