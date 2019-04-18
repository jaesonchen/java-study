package com.asiainfo.basic;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.concurrent.locks.LockSupport;

import com.asiainfo.util.ServiceUtil;

import sun.misc.Unsafe;

/**
 * Unsafe 用于直接调用底层的方法，包括内存分配、内存读写、compareAndSwap、defineClass、park/unpark
 * 
 * @author       zq
 * @date         2017年9月18日  下午2:42:55
 * Copyright: 	  北京亚信智慧数据科技有限公司
 */
@SuppressWarnings("restriction")
public class UnsafeExample {

	/** 
	 * @Description: TODO
	 * 
	 * @param args
	 * @throws InterruptedException 
	 * @throws IOException 
	 */
	public static void main(String[] args) throws NoSuchFieldException, IllegalAccessException, InstantiationException, InterruptedException, IOException {
	    
	    // theUnsafe
		// 通过反射得到theUnsafe对应的Field对象，static的getUnsafe()会抛出SecurityException
		Field field = Unsafe.class.getDeclaredField("theUnsafe");
		field.setAccessible(true);
		// 通过Field得到该Field对应的具体对象，传入null是因为该Field为static的
		Unsafe unsafe = (Unsafe) field.get(null);
		System.out.println("theUnsafe:" + unsafe);

	    // allocateMemory
		// 分配一块内存空间，返回非零的内存起始地址，可能包含垃圾数据(没有自动清零)，失败抛出OutOfMemoryError
		long bytes = 100L;
		long address = unsafe.allocateMemory(bytes);
		System.out.println("allocateMemory:");
        for (int i = 0; i < bytes; i++) {
            System.out.print(unsafe.getByte(address + i) + " ");
        }
        System.out.println();
        // setMemory
        // 初始化内存值
        unsafe.setMemory(address, bytes, (byte) 0);
        System.out.println("after setMemory:");
        for (int i = 0; i < bytes; i++) {
            System.out.print(unsafe.getByte(address + i) + " ");
        }
        System.out.println();
		// putByte/getByte
		// 直接操作address绝对内存地址上的字节内容
		for (int i = 0; i < bytes; i++) {
		    unsafe.putByte(address + i, (byte) i);
		}
		System.out.println("after putByte:");
		for (int i = 0; i < bytes; i++) {
		    System.out.print(unsafe.getByte(address + i) + " ");
		}
		System.out.println();
        // copyMemory
        // 内存块复制
		long copyBytes = 150L;
		long copyAddress = unsafe.allocateMemory(copyBytes);
		unsafe.copyMemory(address, copyAddress, bytes);
		System.out.println("after copyMemory:");
        for (int i = 0; i < copyBytes; i++) {
            System.out.print(unsafe.getByte(copyAddress + i) + " ");
        }
		System.out.println();
		// freeMemory
		// 释放address内存地址上的空间，不会清理垃圾数据
		unsafe.freeMemory(address);
		System.out.println("after freeMemory:");
		for (int i = 0; i < bytes; i++) {
            System.out.print(unsafe.getByte(address + i) + " ");
        }
		System.out.println();
		
		// allocateInstance
		// 通过allocateInstance直接创建对象，不会调用构造函数，但会调用类初始化并初始化final字段(构造函数里初始化的final不会被调用)
		User user = (User) unsafe.allocateInstance(User.class);
		System.out.println("allocateInstance:" + user);
		
		Class<User> clazz = User.class;
		Field fName = clazz.getDeclaredField("name");
		Field fAge = clazz.getDeclaredField("age");
		Field fCountry = clazz.getDeclaredField("country");
		Field fAddress = clazz.getDeclaredField("address");
		Field fZipcode = clazz.getDeclaredField("zipcode");
		Field fUserId = clazz.getDeclaredField("userId");
		
		// staticFieldBase
		// 静态字段的起始基准对象，相当于User.class
		Object staticFieldBase = unsafe.staticFieldBase(clazz.getDeclaredField("userId"));
		// staticFieldOffset
		// 静态字段偏移量，配合staticFieldBase使用
		// 相当于unsafe.putObject(User.class, unsafe.staticFieldOffset(fUserId), "chenzq");
		unsafe.putObject(staticFieldBase, unsafe.staticFieldOffset(fUserId), "chenzq");
		System.out.println("staticFieldOffset:" + unsafe.getObject(staticFieldBase, unsafe.staticFieldOffset(fUserId)));
		// objectFieldOffset
		// 实例字段偏移量
		System.out.println("objectFieldOffset:" + unsafe.getObject(user, unsafe.objectFieldOffset(fName)));
		
		// putInt/putObject
		// 获取实例变量name和age在对象内存中的偏移量并设置值
		unsafe.putInt(user, unsafe.objectFieldOffset(fAge), 18);
		unsafe.putObject(user, unsafe.objectFieldOffset(fName), "android TV");
		System.out.println("putInt/putObject:" + user);
		// 已初始化的final字段无法修改
		unsafe.putObject(user, unsafe.objectFieldOffset(fCountry), "hongkong");
		System.out.println("putObject(final):" + user);
		// 未初始化的final字段可以设置多次
		unsafe.putObject(user, unsafe.objectFieldOffset(fAddress), "bj changping");
		System.out.println("putObject(final):" + user);
        unsafe.putObject(user, unsafe.objectFieldOffset(fAddress), "bj haidian");
        System.out.println("putObject(final):" + user);
        
        // getInt/getObject
        System.out.println("getInt:" + unsafe.getInt(user, unsafe.objectFieldOffset(fAge)));
        System.out.println("getObject:" + unsafe.getObject(user, unsafe.objectFieldOffset(fName)));
        
        // putLongVolatile/getLongVolatile
        // 支持Volatile语义
        unsafe.putLongVolatile(user, unsafe.objectFieldOffset(fZipcode), 1000L);
        System.out.println("getLongVolatile:" + unsafe.getLongVolatile(user, unsafe.objectFieldOffset(fZipcode)));

        // 数据的操作
        String[] array = new String[] {"123", "456", "789"};
		// arrayBaseOffset
        // 数组的第一个元素访问地址
        long arrayBaseOffset = unsafe.arrayBaseOffset(String[].class);
		// arrayIndexScale
        // 数组元素地址的间隔，取决于jdk的位数(32/64)以及是否开启了压缩指针
        int arrayIndexScale = unsafe.arrayIndexScale(String[].class);
        System.out.println("arrayIndexScale:" + arrayIndexScale);
        for (int i = 0; i < array.length; i++) {
            System.out.print(unsafe.getObject(array, arrayBaseOffset + i * arrayIndexScale) + " ");
        }
        System.out.println();
        
		// compareAndSwapInt/compareAndSwapObject
        // cas 实现原理
        System.out.println("before compareAndSwapInt:" + user);
        unsafe.compareAndSwapInt(user, unsafe.objectFieldOffset(fAge), 18, 20);
        System.out.println("after compareAndSwapInt:" + user);
        unsafe.compareAndSwapInt(user, unsafe.objectFieldOffset(fAge), 18, 22);
        System.out.println("after unexpected compareAndSwapInt:" + user);
        
        // ClassLoader实现原理，defineClass
        byte[] content = getClassContent();
        Class<?> dfClazz = unsafe.defineClass(null, content, 0, content.length, UnsafeExample.class.getClassLoader(), null);
        System.out.println("defineClass:" + dfClazz);
        
		// 线程同步实现原理，park/unpark
        // 调用 park后，线程将一直阻塞直到超时或者中断等条件出现。unpark可以终止一个挂起的线程，使其恢复正常。
        // 整个并发框架中对线程的挂起操作被封装在 LockSupport类中，LockSupport类中有各种版本park方法，最终都调用了Unsafe.park()方法。
        Thread parkThread = new Thread(() -> {
            System.out.println("parkThread running...");
            LockSupport.parkNanos(1000000000L * 60);
            System.out.println("parkThread finish...");
        });
        parkThread.start();
        Thread unparkThread = new Thread(() -> {
            ServiceUtil.waitFor(3000L);
            System.out.println("unparkThread running...");
            LockSupport.unpark(parkThread);
        });
        unparkThread.start();
        parkThread.join();
	}
	
	// 读取字节码
	static byte[] getClassContent() throws IOException {
	    
	    File f = new File("target/classes/com/asiainfo/basic/DefineClass.class");
	    FileInputStream input = new FileInputStream(f);
	    byte[] content = new byte[(int) f.length()];
	    input.read(content);
	    input.close();
	    return content;
	}
}

class DefineClass {
    private String id;
    private String name;
    @Override
    public String toString() {
        return "DefineClass [id=" + id + ", name=" + name + "]";
    }
}

class User {
	
	static String userId = "USER_ID";
	final String country = "chn";
	final String address;
	private String name = "jaeson";
    private int age;
    private volatile long zipcode;
    
    public User() {
        this("default");
	}
    
    public User(String address) {
        this.address = address;
        System.out.println("user 构造方法被调用");
    }

    @Override
    public String toString() {
        return "User [userId=" + userId + ", country=" + country + ", address=" + address + ", name=" + name + ", age=" + age + ", zipcode="
                + zipcode + "]";
    }
}