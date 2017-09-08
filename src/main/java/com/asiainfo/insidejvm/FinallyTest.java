package com.asiainfo.insidejvm;

/*
 * try finally特性：
 * 
 * 在try中的return语句会将返回结果值压栈，然后转入到finally子过程，等到finally子过程执行完毕之后（没有return），再返回。
 * 
 * finally的语句是在方法return之前执行的，而且如果finally中有return语句的话，方法直接结束，不再返回栈中的值。
 * 
 * java中方法调用都是采用传值模式，所以如果在finally中改变了引用类型的对象的值，则return返回后的结果也被改变。
 * （实际上返回的是对象引用，并没有被改变，在finally中是直接对对象引用指向的对象进行了修改）
 */
public class FinallyTest {
	public static void main(String[] args) {
		System.out.println("test1:" + testFinal1());
		System.out.println("test2:" + testFinal2());
		System.out.println("test3:" + testFinal3());
		System.out.println("test4:" + testFinal4());
	}

	static int testFinal1() {
		int i = 1;
		try {
			return i;
		} finally {
			System.out.println("in testFinal1():finally 肯定会被执行的！");
			i = 48;
		}
	}

	static String testFinal2() {
		String str = "try";  
		try {
			return str;  
		} finally {
			System.out.println("in testFinal2():finally 肯定会被执行的！");
			str = "finally";
		}
	}

	static StringBuilder testFinal3() {
		StringBuilder build = new StringBuilder("try ");
		try {
			return build;
		} finally {
			System.out.println("in testFinal3():finally 肯定会被执行的！");
			build.append("finally");
			build = new StringBuilder("你猜我是谁！");
		}
	}

	@SuppressWarnings("all")
	static String testFinal4() {
		try {
			return "return in try";
		} finally {
			System.out.println("in testFinal4():finally 肯定会被执行的！");
			return "return in finally";
		}
	}
}
