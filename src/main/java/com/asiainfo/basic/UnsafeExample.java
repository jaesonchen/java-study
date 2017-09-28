package com.asiainfo.basic;

import java.lang.reflect.Field;

import sun.misc.Unsafe;

/**
 * @Description: TODO
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
	 */
	public static void main(String[] args) throws NoSuchFieldException, IllegalAccessException, InstantiationException {

		// 通过反射得到theUnsafe对应的Field对象
		Field field = Unsafe.class.getDeclaredField("theUnsafe");
		// 设置该Field为可访问
		field.setAccessible(true);
		// 通过Field得到该Field对应的具体对象，传入null是因为该Field为static的
		Unsafe unsafe = (Unsafe) field.get(null);
		System.out.println(unsafe);

		//通过allocateInstance直接创建对象
		User user = (User) unsafe.allocateInstance(User.class);
		Class<?> userClass = user.getClass();
		Field name = userClass.getDeclaredField("name");
		Field age = userClass.getDeclaredField("age");
		Field userId = userClass.getDeclaredField("userId");

		//获取实例变量name和age在对象内存中的偏移量并设置值
		unsafe.putInt(user, unsafe.objectFieldOffset(age), 18);
		unsafe.putObject(user, unsafe.objectFieldOffset(name), "android TV");

		// 这里返回 User.class，
		Object staticBase = unsafe.staticFieldBase(userId);
		System.out.println("staticBase:" + staticBase);

		//获取静态变量id的偏移量staticOffset
		long staticOffset = unsafe.staticFieldOffset(userClass.getDeclaredField("userId"));
		//获取静态变量的值
		System.out.println("设置前的ID:" + unsafe.getObject(staticBase, staticOffset));
		//设置值
		unsafe.putObject(staticBase, staticOffset, "SSSSSSSS");
		//获取静态变量的值
		System.out.println("设置后的ID:" + unsafe.getObject(staticBase, staticOffset));
		//输出USER
		System.out.println("输出USER:" + user.toString());

		long data = 1000;
		byte size = 1;//单位字节

		//调用allocateMemory分配内存,并获取内存地址memoryAddress
		long memoryAddress = unsafe.allocateMemory(size);
		//直接往内存写入数据
		unsafe.putAddress(memoryAddress, data);
		//获取指定内存地址的数据
		long addrData=unsafe.getAddress(memoryAddress);
		System.out.println("addrData:" + addrData);
	}
}

class User {
	
	private static String userId = "USER_ID";
	private String name;
    private int age;
    
    public User() {
		System.out.println("user 构造方法被调用");
	}
    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", age=" + age +'\'' +
                ", userId=" + userId +'\'' +
                '}';
    }
}