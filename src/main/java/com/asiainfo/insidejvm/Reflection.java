package com.asiainfo.insidejvm;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.InvocationTargetException;
 
/**
 * @author zq
 * JDK1.5中一个变化是类 java.lang.Class是泛型化的。
 *  现在，Class有一个类型参数T, 你很可能会问，T 代表什么？它代表Class对象代表的类型。比如说，
 *  String.class类型代表 Class<String>。
 * 
 * 由于Class类没有构造方法，所以实例化Class类的方式有点特殊，有三种方式：
 * 对象.getClass()
 * 类.Class
 * forName() 会抛出ClassNotFoundException异常
 * classloader.loadClass(name)
 * 
 * Class.newInstance()，Class对象包含的内容就是反射好的那个类，用来创建一个Class类对象的新实例。
 * Class的 newInstance() 方法现在返回一个T, 你可以在使用反射创建对象时得到更精确的类型。
 * 抛出InstantiationException和IllegalAccessException
 * 
 */
public class Reflection {

	public static void main(String[] args) {
		
		ReflectPerson p = new ReflectPerson("chenzq", 20);
		Class<? extends ReflectPerson> c1 = p.getClass();
		System.out.println(c1);

		Class<String> c2 = String.class;
		System.out.println(c2);
		
		Class<?> c3 = null;
        try {
            c3 = Class.forName("com.jaeson.javastudy.ReflectPerson");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        System.out.println(c3);
		
		//Class类的有参构造对象
        Class<?> c4 = null;
        ReflectPerson person = null;
        try {
            c4 = Class.forName("com.jaeson.javastudy.ReflectPerson");
            Constructor<?> con = c4.getConstructor(new Class[]{String.class, int.class});
            person = (ReflectPerson) con.newInstance("jaesonchen", 30);
            System.out.println(person);
            
            System.out.println(c4.getClassLoader()); 
        	System.out.println(c4.getInterfaces()); 
        	System.out.println(c4.getConstructors()); 
        	
        	Method[] m = c4.getDeclaredMethods();
        	for (int i = 0; i < m.length; i++) {
        		System.out.println(m[i]);
        	}
        	
        	Field f = c4.getDeclaredField("name");
        	//name是私有属性，所以要设置安全检查为true
        	f.setAccessible(true);
        	System.out.println("name:" + (String) f.get(person));
        	
        	f.set(person, "chenzq");
        	System.out.println(person);
        	
        	Method m2 = c4.getMethod("show", String.class);
        	m2.invoke(person, "hello world");
        	
        } catch (IllegalArgumentException e) { 
        	e.printStackTrace();
        } catch (InvocationTargetException e) { 
        	e.printStackTrace();
        } catch (NoSuchFieldException e) {
        	e.printStackTrace();
        } catch (NoSuchMethodException e) {
        	e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } 
         
	}

}

class ReflectPerson {
	private String name;
	private int age;
	public ReflectPerson(String name, int age) {
		this.name = name;
		this.age = age;
	}
	public void show(String str) {
		System.out.println(str);
	}
	public String toString() {
		return "name：" + this.name + ",age：" + this.age;
	}
	
}
