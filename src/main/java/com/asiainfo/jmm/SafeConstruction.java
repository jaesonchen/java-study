package com.asiainfo.jmm;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * Don't publish the "this" reference during construction
 * 不要在构造函数运行期间向外传递this引用。
 * 
 * @author       zq
 * @date         2017年9月27日  下午3:50:41
 * Copyright: 	  北京亚信智慧数据科技有限公司
 */
public class SafeConstruction {
	
	protected Object me;
	private Set<Object> set = new HashSet<>();
	private Thread thread;
	 
	public SafeConstruction() {
		
		// Safe because "me" is not visible from any other thread
		me = this;
	 
	    // Safe because "set" is not visible from any other thread
	    set.add(this);
	 
	    // Safe because MyThread won't start until construction is complete
	    // and the constructor doesn't publish the reference
	    thread = new MyThread(this);
	}

	public void start() {
		thread.start();
	}
}

class MyThread extends Thread {
	
    Object theObject;
    public MyThread(Object o) {
    	this.theObject = o;
    }
}

class UnsafeConstruction {
	
	public static UnsafeConstruction anInstance;
	public static Set<Object> set = new HashSet<>();
	private Set<Object> mySet = new HashSet<>();
	protected Thread thread;
	
	public UnsafeConstruction() {
		
		// Unsafe because anInstance is globally visible
	    UnsafeConstruction.anInstance = this;
	 
	    // Unsafe because SomeOtherClass.anInstance is globally visible
	    SomeOtherClass.anInstance = this;

	    // Unsafe because SomeOtherClass might save the "this" reference
	    // where another thread could see it
	    SomeOtherClass.registerObject(this);
	 
	    // Unsafe because set is globally visible 
	    set.add(this);
	 
	    // Unsafe because we are publishing a reference to mySet
	    mySet.add(this);
	    SomeOtherClass.someMethod(mySet);
	 
	    // Unsafe because the "this" object will be visible from the new thread
	    // before the constructor completes
	    thread = new MyThread(this);
	    thread.start();
	}

	public UnsafeConstruction(Collection<Object> c) {
		// Unsafe because "c" may be visible from other threads
		c.add(this);
	}
}

class SomeOtherClass {
	static UnsafeConstruction anInstance;
	static void registerObject(UnsafeConstruction anInstance) {
		//...
	}
	static void someMethod(Set<Object> set) {
		//...
	}
}
