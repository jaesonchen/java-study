package com.asiainfo.insidejvm;

public class PrivateConstructorCapture {

	public static void main(String[] args) {
		
		System.out.println(new PrivateConstructorCapture().new MyThing());
	}
	//私有构造器捕获（Private Constructor Capture）惯用法是一种非常有用的模式。
	//这个解决方案使用了交替构造器调用机制（alternate constructor invocation）。
	//这个特征允许一个类中的某个构造器链接调用同一个类中的另一个构造器。
	//在这个私有构造器中，表达式Helper.getFinal()的值已经被捕获到了变量i 中，
	//并且它可以在超类构造器返回之后存储到final类型的域i 中。
	class Thing {
		Thing(int i) {
			System.out.println("Thing.i = " + i);
		}
	}
	class MyThing extends Thing {
		final int i;
		public MyThing() {
			//明确调用构造器时不能指向一个实例变量
			//this(i = Helper.getFinal());
			this(Helper.getFinal());
		}
		private MyThing(int i) {
			super(i);
			this.i = i;
		}
		@Override public String toString() {
			return "MyThing.i = " + this.i;
		}
	}
	static class Helper {
		static int getFinal() {
			return 100;
		}
	}
}
