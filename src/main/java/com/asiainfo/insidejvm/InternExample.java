package com.asiainfo.insidejvm;

/**
 * 命令行：java com.jaeson.javastudy.insidejvm.InternExample helloworld
 */
public class InternExample {

	public static void main(String[] args) {
		
		String argsZero = args[0];
		String literalString = "helloworld";
		
		if (argsZero == literalString)
			System.out.println("they are the same string object");
		else
			System.out.println("they are different string object");
		
		argsZero = argsZero.intern();
		System.out.println("after intern args[0]");
		if (argsZero == literalString)
			System.out.println("they are the same string object");
		else
			System.out.println("they are different string object");
	}
}
