package com.asiainfo.insidejvm;

import java.lang.reflect.*;

/**
 * String 字符串池、intern
 * 
 * JDK1.7的HotSpot中，已经把原来存放在方法区中的字符串常量池移出。在JDK1.7以后的版本中字符串常量池移到堆内存区域；
 *  同时在jdk1.8中移除整个永久代，取而代之的是一个叫元空间（Metaspace）的区域。
 *  
 * 
 *  JAVA 语言中有8中基本类型和一种比较特殊的类型String。这些类型为了使他们在运行过程中速度更快，更节省内存，都提供了一种常量池的概念。
 *   常量池就类似一个JAVA系统级别提供的缓存。8种基本类型的常量池都是系统协调的，String类型的常量池比较特殊。
 *   它的主要使用方法有两种：
 *  a. 直接使用双引号声明出来的String对象会直接存储在常量池中。
 *  b. 如果不是用双引号声明的String对象，可以使用String提供的intern方法。intern 方法会从字符串常量池中查询当前字符串是否存在，若不存在就会将当前字符串放入常量池中。
 * 
 *  String 是常量，字面量的 + 操作编译时会被转换成 StringBuilder.append()。 字符串字面量总是有一个来自字符串常量池的引用，所以它们不会被垃圾回收。
 *      
 * String.intern()
 * JDK1.7当调用 intern方法时，如果池已经包含一个等于此String对象的字符串（用equals(oject)方法确定），则返回池中的字符串引用。
 *  否则，将此String对象引用添加到池中，并返回此String对象的引用。
 *  
 * intern()方法调用底层的c++实现的StringTable，类似于HashMap，默认大小是1009，如果放进String Pool的String非常多，
 *     就会造成Hash冲突严重，从而导致链表会很长，而链表长了后直接会造成的影响就是当调用String.intern时性能会大幅下降。
 * jdk6中 StringTable时固定大小的，在jdk7中，StringTable的长度可以通过一个参数指定：-XX:StringTableSize=99991
 * 
 *               
 * @author       zq
 * @date         2017年10月16日  下午4:41:34
 * Copyright: 	  北京亚信智慧数据科技有限公司
 */
public class StringPool {
	
    public static void main(String[] args) throws Exception {
        
        // 字面量 String str = "abc";等价于 char data[] = {'a', 'b', 'c'}; new String(data); 但又不同于new创建的字符串对象。
        // 字面量 创建的对象在堆中的字符串池，如果已存在则返回引用。new 创建的对象在堆中的普通对象区。
        String a = "chenzq";
        // public String(String original) { this.value = original.value; }
        // new 时传入的是字符串池的对象引用，所以他们公用一个char[]序列，但是对象引用不一样
        String b = new String("chenzq");
        System.out.println(a == b); // false
        // intern() 如果字符串不在常量池中，则将堆中的引用放入字符串池；如果已经在字符串池中，返回字符串池中的对象引用
        b.intern();
        System.out.println(a == b); // false
        System.out.println(a == b.intern()); //true
        System.out.println(a == a.intern()); //true
        
        // 可以通过反射修改String内部的char[] value序列来修改String的值
        Field fValue = String.class.getDeclaredField("value");
        fValue.setAccessible(true);
        char[] value = (char[]) fValue.get(a);
        value[4] = '_';
        System.out.println("a=" + a); // chen_q
        System.out.println("b=" + b); // chen_q

        // s1 在堆中普通对象区，编译器优化成：String s1 = StringBuilder().append(new String("1")).append(new String("1"));
        String s1 = new String("1") + new String("1");
        // "11" 的引用放入字符串常量池
        String s3 = s1.intern();
        // 字符串常量池已存在"11"，返回引用给s2
        String s2 = "11";
        System.out.println(s1 == s2); // true
        System.out.println(s1 == s3); // true
        System.out.println(s2 == s3); // true
        
        // 改变intern()的位置
        String s4 = new String("3") + new String("3");
        String s5 = "33";
        s4.intern();
        System.out.println(s4 == s5); // false
    }
}