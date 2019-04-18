package com.asiainfo.jdbc.templatetool;

import java.beans.PropertyDescriptor;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * @Description: sql和对应的值获取PropertyDescriptor，包括Embedded对象，用于insert语句
 * 
 * @author       zq
 * @date         2017年7月2日  上午11:35:03
 * Copyright: 	  北京亚信智慧数据科技有限公司
 */
public class SqlPropertyDecriptorPairs implements Serializable {

	/** serialVersionUID */
	private static final long serialVersionUID = 1L;

	private String sql;
	private List<PropertyDescriptor> pdList;
	private Map<PropertyDescriptor, PropertyDescriptor> embeddedMap;
	
	public SqlPropertyDecriptorPairs() {}
	public SqlPropertyDecriptorPairs(String sql, List<PropertyDescriptor> pdList,
			Map<PropertyDescriptor, PropertyDescriptor> embeddedMap) {
		this.sql = sql;
		this.pdList = pdList;
		this.embeddedMap = embeddedMap;
	}
	public String getSql() {
		return sql;
	}
	public void setSql(String sql) {
		this.sql = sql;
	}
	public List<PropertyDescriptor> getPdList() {
		return pdList;
	}
	public void setPdList(List<PropertyDescriptor> pdList) {
		this.pdList = pdList;
	}
	public Map<PropertyDescriptor, PropertyDescriptor> getEmbeddedMap() {
		return embeddedMap;
	}
	public void setEmbeddedMap(Map<PropertyDescriptor, PropertyDescriptor> embeddedMap) {
		this.embeddedMap = embeddedMap;
	}
	@Override
	public String toString() {
		return "SqlPropertyDecriptorPairs [sql=" + sql + ", pdList=" + pdList + ", embeddedMap=" + embeddedMap + "]";
	}
}
