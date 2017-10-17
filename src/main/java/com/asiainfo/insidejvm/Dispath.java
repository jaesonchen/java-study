package com.asiainfo.insidejvm;

/**
 * @Description: TODO
 * 
 * @author       zq
 * @date         2017年9月5日  下午3:05:05
 * Copyright: 	  北京亚信智慧数据科技有限公司
 */
public class Dispath {

	/** 
	 * @Description: TODO
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		
		Father father = new Father();
		Father son = new Son();
		father.hello("jaeson");
		son.hello("chenzq");
		
		Dispath dispath = new Dispath();
		dispath.invoke(father, "jaeson");
		dispath.invoke(son, "chenzq");
	}

	public void invoke(Father father, String name) {
		father.hello("F:" + name);
	}
	
	public void invoke(Son son, String name) {
		son.hello("S:" + name);
	}
	
	static class Father {
		void hello(String name) {
			System.out.println("Father say hello " + name);
		}
	}
	
	static class Son extends Father {
		
		@Override
		void hello(String name) {
			System.out.println("Son say hello " + name);
		}
	}
}
