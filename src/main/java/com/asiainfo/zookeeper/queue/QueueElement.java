package com.asiainfo.zookeeper.queue;

import java.io.Serializable;

/**
 * @Description: TODO
 * 
 * @author       zq
 * @date         2017年8月27日  下午6:44:06
 * Copyright: 	  北京亚信智慧数据科技有限公司
 */
public class QueueElement implements Serializable {

	/** serialVersionUID */
	private static final long serialVersionUID = 1L;
	
	private String id;
	private String name;
	
	public QueueElement(String id, String name) {
		this.id = id;
		this.name = name;
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
	
	@Override
	public String toString() {
		return "QueueElement [id=" + id + ", name=" + name + "]";
	}
}
