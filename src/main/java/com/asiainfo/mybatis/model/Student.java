package com.asiainfo.mybatis.model;

public class Student implements java.io.Serializable {

	// Fields
	private static final long serialVersionUID = 1L;
	private String id;
	private Clazz clazz;
	private String name;
	private Integer sex;

	// Constructors

	/** default constructor */
	public Student() {
	}

	/** full constructor */
	public Student(Clazz clazz, String name, Integer sex) {
		this.clazz = clazz;
		this.name = name;
		this.sex = sex;
	}

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Clazz getClazz() {
		return this.clazz;
	}

	public void setClazz(Clazz clazz) {
		this.clazz = clazz;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getSex() {
		return this.sex;
	}

	public void setSex(Integer sex) {
		this.sex = sex;
	}
}