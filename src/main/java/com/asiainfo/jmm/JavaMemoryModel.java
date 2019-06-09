package com.asiainfo.jmm;

/**
 * volatile / synchronized 的内存屏障
 * 
 * @author       zq
 * @date         2017年9月17日  上午9:35:56
 * Copyright: 	  北京亚信智慧数据科技有限公司
 */
public class JavaMemoryModel {
	 
	int a, b;
	volatile int v, u;

	void readAndWrite() {
		
		int i, j;
		// nomal read
		i = a;	// load a
		j = b;	// load b
		
		// volatile read
		i = v;	// load v
			  	// LoadLoad
		        // 忽略LoadStore，因为后面是个volatile read
		
		j = u;	// load u
		        // 忽略LoadLoad，因为后面指令序列里没有nomal read指令
				// LoadStore
		
		// normal write
		a = i;	// store a
		b = j;	// store b
		
		// volatile write
				// StoreStore
		v = i;	// store v
		        // StoreLoad
		
				// StoreStore
		u = j;	// store u
				// StoreLoad
		        // x86下的汇编代码: 0x00000000029d3044: lock add dword ptr [rsp],0h;*putfield u
		        // lock的作用是使得本CPU的Cache写入了内存，该写入动作也会引起别的CPU invalidate其Cache。
		        // 1. 锁住主存 
		        // 2. 任何读必须在写完成之后再执行
		        // 3. 使其它线程这个值的栈缓存失效
	}
}

class X {
	
	int a;
	volatile int v;
	void f() {
		
		int i;
		synchronized (this) { // enter EnterLoad EnterStore
			i = a;// load a
			a = i;// store a
		}// LoadExit StoreExit exit ExitEnter

		synchronized (this) {// enter ExitEnter
			synchronized (this) {// enter
			}// EnterExit exit
		}// ExitExit exit ExitEnter ExitLoad

		i = v;// load v
		synchronized (this) {// LoadEnter enter
		} // exit ExitEnter ExitStore

		v = i; // store v
		synchronized (this) { // StoreEnter enter
		} // EnterExit exit
	}
}

