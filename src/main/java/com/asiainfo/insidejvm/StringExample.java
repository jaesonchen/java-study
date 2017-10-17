package com.asiainfo.insidejvm;

import java.lang.reflect.*;

/**
 * 
 * @Description: TODO
 * 
 * @author       zq
 * @date         2017年10月16日  下午4:41:34
 * Copyright: 	  北京亚信智慧数据科技有限公司
 */
public class StringExample {
	
    public static void main(String[] args) throws Exception {
    	
        String a = "chenssy";
        String b = "chenssy";
        //public String(String original) { this.value = original.value; }
        String c = new String("chenssy");

        System.out.println("--------------修改前值-------------------");
        System.out.println("a = " + a);
        System.out.println("b = " + b);
        System.out.println("c = " + c);
        //修改String的值
        Field aField = String.class.getDeclaredField("value");
        aField.setAccessible(true);
        char[] value = (char[]) aField.get(a);
        //修改a所指向的值
        value[4] = '_';
        
        System.out.println("--------------修改后值-------------------");
        System.out.println("a = " + a);
        System.out.println("b = " + b);
        System.out.println("chenssy");
        System.out.println("c = " + c);
        
        
        String aa = "string";
        String bb = "str";
        String cc = bb + "ing";
        System.out.println("--------------修改前-------------------");
        System.out.println("aa = " + aa);
        System.out.println("cc = " + cc);
        value = (char[]) aField.get(aa);
        value[4]='_';

        System.out.println("--------------修改后-------------------");
        System.out.println("aa = " + aa);
        System.out.println("cc = " + cc);
        
        
        String bbb = "hello";
        //优化为：StringBuilder.append(bbb).append("world");
        String ccc = bbb + "world";
        System.out.println("--------------修改前-------------------");
        System.out.println("ccc = " + ccc);
        value = (char[]) aField.get(ccc);
        value[4]='_';

        System.out.println("--------------修改后-------------------");
        System.out.println("helloworld");
        System.out.println("ccc = " + ccc);
        

        final String bbbb = "hello";
        //等价于：cccc = "helloworld";
        String cccc = bbbb + "world";
        System.out.println("--------------修改前-------------------");
        System.out.println("cccc = " + cccc);
        value = (char[]) aField.get(cccc);
        value[4]='_';

        System.out.println("--------------修改后-------------------");
        System.out.println("helloworld");
        System.out.println("cccc = " + cccc);
    }
}