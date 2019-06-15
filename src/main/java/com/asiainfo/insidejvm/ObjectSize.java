package com.asiainfo.insidejvm;

import static net.sourceforge.sizeof.SizeOf.sizeOf;

import java.lang.reflect.Field;

import sun.misc.Unsafe;

/**
 * @VM args: -javaagent:SizeOf.jar -XX:+UseCompressedOops
 *           
 *                              
 * Java对象大小的计算方法：
 * 1. 通过java.lang.instrument.Instrumentation的getObjectSize(obj)直接获取对象的大小（不计算所引用的对象的实际大小）
 *    a. 定义一个类，提供一个premain方法: public static void premain(String agentArgs, Instrumentation instP)
 *    b. 创建META-INF/MANIFEST.MF文件，内容是指定PreMain的类是哪个： Premain-Class: sizeof.MySizeOf
 *    c. 把这个类打成jar，然后用java -javaagent XXXX.jar 的方式执行
 *    
 * 2. 通过sun.misc.Unsafe对象的objectFieldOffset(field)等方法结合反射来计算对象的大小
 *    a. 通过反射获得一个类的Field
 *    b. 通过Unsafe的objectFieldOffset()获得每个Field的offSet
 *    c. 对Field按照offset排序，取得最大的offset，然后加上这个field的长度，再加上Padding对齐
 *  
 *  
 * @author       zq
 * @date         2017年10月16日  下午4:56:47
 * Copyright: 	  北京亚信智慧数据科技有限公司
 */
@SuppressWarnings("all")
public class ObjectSize {

    static Unsafe unsafe;
    static int objectRefSize;
    static {
        try {
            Field field = Unsafe.class.getDeclaredField("theUnsafe");
            field.setAccessible(true);
            ObjectSize.unsafe = (Unsafe) field.get(null);
            objectRefSize = unsafe.arrayIndexScale(Object[].class);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

	public static void main(String[] args) {
		
	    // 引用大小
	    System.out.println("是否开启oop，引用大小=" + unsafe.arrayIndexScale(Object[].class));
	    // 对象字段在内存中的排列，从long --> byte/boolean --> padding --> reference/array
	    Field[] fields = FieldOffset.class.getDeclaredFields();
        for(Field f: fields) {
            System.out.println(f.getName() + " offset: " + unsafe.objectFieldOffset(f));
        }
        
        System.out.println("================================");
        // 超类的字段在基类字段之前，并且超类需要与8的倍数对齐
        fields = E.class.getFields();
        for(Field f: fields) {
            System.out.println("e." + f.getName() + " offset: " + unsafe.objectFieldOffset(f));
        }
        
        System.out.println("================================");
	    System.out.println("sizeOf new A()=" + sizeOf(new A()));
	    System.out.println("sizeOf new B()=" + sizeOf(new B()));
	    System.out.println("sizeOf new C()=" + sizeOf(new C()));
	    System.out.println("sizeOf new D()=" + sizeOf(new D()));
	    System.out.println("sizeOf new E()=" + sizeOf(new E()));
	    /** 
	     * -XX:+UseCompressedOops: mark/8 + oop/4 + length/4 + data/44 + padding/4 = 64 
	     * -XX:-UseCompressedOops: mark/8 + oop/8 + length/4 + data/44 = 64 
	     */
	    System.out.println("sizeOf new int[11]=" + sizeOf(new int[11]));
	}
	
	// 内存中的字段排列顺序
	static class FieldOffset {
	    
	    byte b = 1;
	    long l = 1L;
	    int i = 1;
	    short s = 0;
	    double d = 1.0;
	    float f = 1.0f;
	    char c = 'a';
	    boolean flag = false;
	    int[] iArr = new int[5];
	    A a = new A();
	}
    // 继承关系的类字段在内存中的排列
    /** 
     * -XX:+UseCompressedOops: mark/8 + oop/4 + sPP/2 + padding/2 + iP/4 + bP/1 + padding/3 + rP/4 + s/2 + b/1 + padding/1 + j/8 + eArray/4 + padding/4 = 48 
     * -XX:-UseCompressedOops: mark/8 + oop/8 + sPP/2 + padding/6 + iP/4 + bP/1 + padding/3 + rP/4 + padding/4 + j/8 + s/2 + b/1 + padding/5 + eArray/4 + padding/4 = 64 
     */
    static class E extends P {
        public long j;
        public short s;
        public byte b; 
        public P[] eArray = new P[3];
 
        E() {
            for (int i = 0; i < eArray.length; i++) {
                eArray[i] = new P();
            }
        }
    }
    static class P extends PP {
        public int iP;
        public byte bP;
        public B rP;
    }
    static class PP {
        public short sPP;
    }
	
    /** 
     * -XX:+UseCompressedOops: mark/4 + metedata/8 + field/4 = 16 
     * -XX:-UseCompressedOops: mark/8 + metedata/8 + field/4 + padding/4 = 24 
     */  
    static class A {
        public int a;
    }
  
    /** 
     * -XX:+UseCompressedOops: mark/4 + metedata/8 + field/4 + field/4 + padding/4 = 24 
     * -XX:-UseCompressedOops: mark/8 + metedata/8 + field/4 + field/4 = 24 
     */  
    static class B {
        int b1;
        int b2;
    }
  
    /** 
     * -XX:+UseCompressedOops: mark/4 + metedata/8 + field/4 + reference/4 + padding/4 = 24 
     * -XX:-UseCompressedOops: mark/8 + metedata/8 + field/4 + reference/8 + padding/4 = 32 
     */  
    static class C {
        int c1;
        A c2 = new A();
    }

    /** 
     * -XX:+UseCompressedOops: mark/4 + metedata/8 + reference/4 = 16 
     * -XX:-UseCompressedOops: mark/8 + metedata/8 + reference/8 = 24
     */ 
    static class D {
        int[] iArray = new int[10];
    }
}
