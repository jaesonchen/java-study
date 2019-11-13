package com.asiainfo.arm;

import java.io.IOException;

/**
 * 
 * try finally特性：
 * 
 * try中的return语句会将返回结果/抛出异常压栈，然后转入到finally子过程，等到finally子过程执行完毕之后（没有return），再返回/抛出异常。
 * 
 * finally的语句是在方法return之前执行的，而且如果finally中有return语句的话，方法直接结束，不再返回栈中的值/异常。
 * 
 * finally中return会吃掉try中抛出的异常，方法不需要声明异常。
 * 
 * finally中抛出异常会抑制try中抛出的异常，方法需要声明finally抛出得异常类型。
 * 
 * 
 * @author       zq
 * @date         2017年10月16日  下午4:38:11
 * Copyright: 	  北京亚信智慧数据科技有限公司
 */
public class TryCatchFinally {
	
	public static void main(String[] args) throws Exception {
		
		System.out.println("test1:" + testFinal1());
		System.out.println("test2:" + testFinal2());
		System.out.println("test3:" + testFinal3());
		System.out.println("test4:" + testFinal4());
		System.out.println("test5:" + testFinal5());
		testFinal6();
	}

	static int testFinal1() {
		int i = 1;
		try {
			return i;
		} finally {
			System.out.println("in testFinal1():finally 修改基本类型不起作用！");
			i = 48;
		}
	}

	static String testFinal2() {
		String str = "try";  
		try {
			return str;  
		} finally {
			System.out.println("in testFinal2():finally 修改引用不起作用！");
			str = "finally";
		}
	}

	static StringBuilder testFinal3() {
		StringBuilder build = new StringBuilder("try ");
		try {
			return build;
		} finally {
			System.out.println("in testFinal3():finally 修改引用类型的值！");
			build.append("finally");
			build = new StringBuilder("你猜我是谁！");
		}
	}

	@SuppressWarnings("finally")
	static String testFinal4() {
		try {
			return "return in try";
		} finally {
			System.out.println("in testFinal4():finally 直接返回！");
			return "return in finally";
		}
	}
	
	// 在finally中return时，不需要声明异常处理，finally会吃掉try抛出的异常
	@SuppressWarnings("finally")
	static String testFinal5() {
		try {
			throw new Exception("ex");
		} finally {
			System.out.println("in testFinal5():finally 吃掉异常！");
			return "return in finally";
		}
	}
	
	// 在finally里抛出的异常会抑制try抛出的异常
	@SuppressWarnings("finally")
    static void testFinal6() throws IOException {
        try {
            throw new Exception("ex");
        } finally {
            System.out.println("in testFinal6():finally 抑制异常！");
            throw new IOException("ex");
        }
	}
}
