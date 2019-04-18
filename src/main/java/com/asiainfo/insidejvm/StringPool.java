package com.asiainfo.insidejvm;

import java.lang.reflect.*;

/**
 * 
 * @Description: String 字符串池、intern
 * 
 *               Java在设计时为了提高String类的使用效率，使用String Pool的机制进行String管理。
 *               String 是常量，创建之后无法修改（反射除外）。字面量的 + 操作编译时会被转换成 StringBuilder.append()。
 *               字符串字面量总是有一个来自字符串常量池的引用，所以它们不会被垃圾回收。
 * 
 *               在 Jdk6 以及以前的版本中，字符串的常量池是放在堆的 Perm 区的，Perm 区是一个类静态的区域，主要存储一些加载类的信息，
 *               常量池，方法片段等内容，默认大小只有4m，一旦常量池中大量使用 intern 是会直接产生java.lang.OutOfMemoryError: PermGen space错误的。 
 *               所以在 jdk7 的版本中，字符串常量池已经从 Perm 区移到正常的 Java Heap 区域了。
 *               在不同版本jdk中字符串 == 和intern()的表现可能不相同。
 *               
 *               intern()方法调用底层的c++实现的StringTable，类似于HashMap，默认大小是1009，如果放进String Pool的String非常多，
 *               就会造成Hash冲突严重，从而导致链表会很长，而链表长了后直接会造成的影响就是当调用String.intern时性能会大幅下降。
 *               jdk6中 StringTable时固定大小的，在jdk7中，StringTable的长度可以通过一个参数指定：-XX:StringTableSize=99991
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

        // jdk7以后字符串字面量不是在class加载时就放入字符串常量池，否则下面这段说不通
        // s1 在堆中普通对象区，编译器优化成：String s1 = StringBuilder().append(new String("1")).append(new String("1"));
        String s1 = new String("1") + new String("1");
        // "11" 放入字符串常量池
        s1.intern();
        // 字符串常量池已存在"11"，返回引用给s2
        String s2 = "11";
        System.out.println(s1 == s2); // true
        
        // 改变intern()的位置
        String s3 = new String("3") + new String("3");
        String s4 = "33";
        s3.intern();
        System.out.println(s3 == s4); // false
    }
}