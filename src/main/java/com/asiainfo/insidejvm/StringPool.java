package com.asiainfo.insidejvm;

import java.lang.reflect.*;

/**
 * String 字符串池、intern
 * 
 *               
 * @author       zq
 * @date         2017年10月16日  下午4:41:34
 * Copyright: 	  北京亚信智慧数据科技有限公司
 */
public class StringPool {
	
    public static void main(String[] args) throws Exception {
        
        // 字面量 String str = "abc"; 等价于 char data[] = {'a', 'b', 'c'}; new String(data); 但又不同于new创建的字符串对象。
        // 字面量 创建的对象在堆中的字符串池，如果已存在则返回引用。new 创建的对象在堆中的普通对象区。
        String a = "chenzq";
        // public String(String original) { this.value = original.value; }
        // new 时传入的是字符串池的对象引用，所以他们公用一个char[]序列，但是对象引用不一样
        String b = new String("chenzq");
        System.out.println(a == b);             // false
        
        // String.intern() 如果字符串不在常量池中，则将堆中的引用放入字符串池的Hashtable数据结构中；如果已经在字符串池中，返回字符串池中的对象引用
        b.intern();
        System.out.println(a == b);             // false
        System.out.println(a == b.intern());    //true
        System.out.println(a == a.intern());    //true
        
        // 可以通过反射修改String内部的char[] value序列来修改String的值
        Field fValue = String.class.getDeclaredField("value");
        fValue.setAccessible(true);
        char[] value = (char[]) fValue.get(a);
        value[4] = '_';
        System.out.println("a = " + a);   // chen_q
        System.out.println("b = " + b);   // chen_q

        // + 连接符优化
        // + 连接String对象时，编译器优化为：String s1 = StringBuilder().append(new String("1")).append(new String("1"));
        String s1 = new String("1") + new String("1");
        // "11" 的引用放入字符串常量池
        String s3 = s1.intern();
        // 字符串常量池已存在"11"，直接返回引用给s2
        String s2 = "11";
        System.out.println(s1 == s2);   // true
        System.out.println(s1 == s3);   // true
        System.out.println(s2 == s3);   // true
        
        // 改变intern()调用的位置
        String s4 = new String("3") + new String("3");
        String s5 = "33";
        s4.intern();
        System.out.println(s4 == s5);   // false
        
        // + 连接字面量时，编译器优化为：String s6 = "hello";
        String s6 = "he" + "llo";
        String s7 = new String("he") + new String("llo");
        System.out.println(s6 == s7.intern());  // true
        
        // + 连接变量代表的字面量时，优化为： String s10 = StringBuilder().append(s8).append(s9); 
        String s8 = "hello";
        String s9 = " world";
        // 这里不会因为两个变量的值是字面量而直接合并成字符串字面量
        String s10 = s8 + s9;
        String s11 = new String("hello") + new String(" world");
        System.out.println(s10.equals(s11));        // true
        System.out.println(s11 == s11.intern());    // true
    }
    
    String t;
    public void set() {
        this.t = "dd";
    }
}