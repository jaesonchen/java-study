package com.asiainfo.compiler.test.bean;

/**
 * 
 * @Description: Sample class as JavaBean.
 * 
 * @author       zq
 * @date         2017年9月15日  下午5:20:19
 * Copyright: 	  北京亚信智慧数据科技有限公司
 */
public class User {

	private String id;
	private String name;
	private long created;

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
	public long getCreated() {
		return created;
	}
	public void setCreated(long created) {
		this.created = created;
	}
	@Override
	public String toString() {
		return "User [id=" + id + ", name=" + name + ", created=" + created + "]";
	}
}
