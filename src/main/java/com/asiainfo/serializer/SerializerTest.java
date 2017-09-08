package com.asiainfo.serializer;

import java.io.Serializable;

/**
 * @Description: TODO
 * 
 * @author       zq
 * @date         2017年9月3日  上午10:26:20
 * Copyright: 	  北京亚信智慧数据科技有限公司
 */
public class SerializerTest {

	/** 
	 * @Description: TODO
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		
		SerializerBean bean  = new SerializerBean("1001", "chenzq", 37);
		KryoSerializer kryo = new KryoSerializer();
		HessianSerializer hessian = new HessianSerializer();
		FastjsonSerializer fast = new FastjsonSerializer();
		JdkSerializer jdk = new JdkSerializer();
		
		long start = System.currentTimeMillis();
		byte[] result = jdk.serialize(bean);
		System.out.println(jdk.deserialize(result));
		System.out.println("jdk length=" + result.length);
		System.out.println("jdk time=" + (System.currentTimeMillis() - start));
		
		start = System.currentTimeMillis();
		result = kryo.serialize(bean);
		System.out.println(kryo.deserialize(result));
		System.out.println("kryo length=" + result.length);
		System.out.println("kryo time=" + (System.currentTimeMillis() - start));
		
		start = System.currentTimeMillis();
		result = hessian.serialize(bean);
		System.out.println(hessian.deserialize(result));
		System.out.println("hessian length=" + result.length);
		System.out.println("hessian time=" + (System.currentTimeMillis() - start));
		
		start = System.currentTimeMillis();
		result = fast.serialize(bean);
		System.out.println(fast.deserialize(result));
		System.out.println("fast length=" + result.length);
		System.out.println("fast time=" + (System.currentTimeMillis() - start));
		

	}

}
class SerializerBean implements Serializable {

	/** serialVersionUID */
	private static final long serialVersionUID = 1L;
	
	private String id;
	private String name;
	private int age;
	
	public SerializerBean() {}
	public SerializerBean(String id, String name, int age) {
		this.id = id;
		this.name = name;
		this.age = age;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getAge() {
		return age;
	}
	public void setAge(int age) {
		this.age = age;
	}
	@Override
	public String toString() {
		return "SerializerBean [id=" + id + ", name=" + name + ", age=" + age + "]";
	}
}
