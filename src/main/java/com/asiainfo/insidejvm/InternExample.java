package com.asiainfo.insidejvm;

/**
 * 
 * 命令行：java com.jaeson.javastudy.insidejvm.InternExample helloworld
 * 
 * @author       zq
 * @date         2017年10月16日  下午4:59:39
 * Copyright: 	  北京亚信智慧数据科技有限公司
 */
public class InternExample {

	public static void main(String[] args) {
		
		String argsZero = args[0];
		String literalString = "helloworld";
		
		if (argsZero == literalString) {
			System.out.println("they are the same string object");
		} else {
			System.out.println("they are different string object");
		}
		argsZero = argsZero.intern();
		System.out.println("after intern args[0]");
		if (argsZero == literalString) {
			System.out.println("they are the same string object");
		} else {
			System.out.println("they are different string object");
		}
	}
}
