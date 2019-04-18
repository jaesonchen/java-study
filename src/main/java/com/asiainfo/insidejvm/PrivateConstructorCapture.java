package com.asiainfo.insidejvm;

/**
 * 私有构造器捕获（Private Constructor Capture）是一种非常有用的模式。
 * 这个解决方案使用了交替构造器调用机制（alternate constructor invocation）。
 * 这个特征允许一个类中的某个构造器链接调用同一个类中的另一个构造器。
 * 
 * @author chenzq  
 * @date 2019年3月22日 下午7:53:09
 * @version V1.0
 * @Copyright: Copyright(c) 2019 jaesonchen.com Inc. All rights reserved.
 */
public class PrivateConstructorCapture {

	public static void main(String[] args) {
		System.out.println(new PrivateConstructorCapture().new MyThing());
	}

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
		//私有构造器捕获
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
