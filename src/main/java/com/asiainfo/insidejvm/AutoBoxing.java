package com.asiainfo.insidejvm;

/**
 * 避免创建不必要的对象：
 * 
 * jdk1.5引入自动装箱/拆箱（autoboxing），它允许程序员将基本类型和基本类型对象混用，按需自动装箱/拆箱。
 * 
 * 自动装箱/拆箱使基本类型和基本类型对象之间的差别变得模糊起来，但它们在语义上还有着微妙的差别，在性能上也有着比较明显的差别。
 *
 */
public class AutoBoxing {
	
    //使用基本数据类型
	public static void sum() {
		
		long start = System.currentTimeMillis();
		long sum = 0L;
		for(long i = 0; i < Integer.MAX_VALUE; i++) {
			sum += i;
		}
		long end = System.currentTimeMillis();
		System.out.println("total = " + sum + ",time = " + (end - start) + "ms");
	}
	
	// 使用封装数据类型
	public static void sumAutoBoxing() {
		
		long start = System.currentTimeMillis();
		//使用对象，每次计算时会自动装箱，性能差异巨大
		Long sum = 0L;
		for(long i = 0; i < Integer.MAX_VALUE; i++) {
			sum += i;
		}
		long end = System.currentTimeMillis();
		System.out.println("total = " + sum + ",time = " + (end - start) + "ms");
	}

	public static void main(String[] args) {
		sum();
		sumAutoBoxing();
	}
}
