package com.asiainfo.jdbc.templatetool;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.Arrays;

/**
 * @Description: sql和对应的值获取Method，用于select语句
 * 
 * @author       zq
 * @date         2017年7月1日  下午12:56:54
 * Copyright: 	  北京亚信智慧数据科技有限公司
 */
public class SqlMethodPairs implements Serializable {

	/** serialVersionUID */
	private static final long serialVersionUID = 1L;

	private String sql;
	private Method[] methods;
	
	public SqlMethodPairs() {}
	public SqlMethodPairs(String sql, Method[] methods) {
		this.sql = sql;
		this.methods = methods;
	}
	
	public String getSql() {
		return sql;
	}
	public void setSql(String sql) {
		this.sql = sql;
	}
	public Method[] getMethods() {
		return methods;
	}
	public void setMethods(Method[] methods) {
		this.methods = methods;
	}
	@Override
	public String toString() {
		return "SqlMethodPairs [sql=" + sql + ", methods=" + Arrays.toString(methods) + "]";
	}
}
