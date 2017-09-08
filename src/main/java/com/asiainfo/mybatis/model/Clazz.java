package com.asiainfo.mybatis.model;
public class Clazz implements java.io.Serializable {

	// Fields
	private static final long serialVersionUID = 1L;
	private String id;
	private String name;

	// Constructors

	/** default constructor */
	public Clazz() {
	}

	/** minimal constructor */
	public Clazz(String id, String name) {
		this.id = id;
		this.name = name;
	}

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}
}