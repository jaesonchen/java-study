package com.asiainfo.clazz;

/**
 * @Description: TODO
 * 
 * @author       zq
 * @date         2017年9月5日  下午12:27:02
 * Copyright: 	  北京亚信智慧数据科技有限公司
 */
public class TestClass {
	
	private String id;
	private String name = "貂蝉";
	private int age = 100;
	
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
		return "TestClass [id=" + id + ", name=" + name + ", age=" + age + "]";
	}
}
