package com.asiainfo.insidejvm;

import java.lang.reflect.*;

public class StringTest {
	
    public static void main(String[] args) throws Exception {
    	
        String a = "chenssy";
        String b = "chenssy";
        String c = new String("chenssy");	//public String(String original) { this.value = original.value; }

        System.out.println("--------------修改前值-------------------");
        System.out.println("a = " + a);
        System.out.println("b = " + b);
        System.out.println("c = " + c);
        //修改String的值
        Field a_ = String.class.getDeclaredField("value");
        a_.setAccessible(true);
        char[] value = (char[])a_.get(a);
        value[4] = '_';   //修改a所指向的值
        
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
        value = (char[])a_.get(aa);
        value[4]='_';

        System.out.println("--------------修改后-------------------");
        System.out.println("aa = " + aa);
        System.out.println("cc = " + cc);
        
        
        String bbb = "hello";
        String ccc = bbb + "world";		//优化为：StringBuilder.append(bbb).append("world");
        System.out.println("--------------修改前-------------------");
        System.out.println("ccc = " + ccc);
        value = (char[])a_.get(ccc);
        value[4]='_';

        System.out.println("--------------修改后-------------------");
        System.out.println("helloworld");
        System.out.println("ccc = " + ccc);
        

        final String bbbb = "hello";
        String cccc = bbbb + "world";	//等价于：cccc = "helloworld";
        System.out.println("--------------修改前-------------------");
        System.out.println("cccc = " + cccc);
        value = (char[])a_.get(cccc);
        value[4]='_';

        System.out.println("--------------修改后-------------------");
        System.out.println("helloworld");
        System.out.println("cccc = " + cccc);
    }
}