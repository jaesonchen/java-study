package com.asiainfo.jdbc.templatetool;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.support.lob.DefaultLobHandler;
import org.springframework.jdbc.support.lob.LobHandler;

import com.asiainfo.jdbc.converter.InjectBlob;
import com.asiainfo.jdbc.converter.InjectClob;

/**
 * @Description: 单条记录sql的 ? 参数设置
 * 
 * @author       zq
 * @date         2017年7月2日  上午11:28:54
 * Copyright: 	  北京亚信智慧数据科技有限公司
 */
public class UpdateSetter implements PreparedStatementSetter {

	protected final Logger logger = LoggerFactory.getLogger(getClass());
	protected final LobHandler lobHandler = new DefaultLobHandler();
	
	private Object obj;
	private List<PropertyDescriptor> pdList;
	private Map<PropertyDescriptor, PropertyDescriptor> embeddedMap;
	public UpdateSetter(Object obj, List<PropertyDescriptor> pdList, Map<PropertyDescriptor, PropertyDescriptor> embeddedMap) {
		this.obj = obj;
		this.pdList = pdList;
		this.embeddedMap = (null == embeddedMap) ? new HashMap<>() : embeddedMap;
	}
	
	/* 
	 * @Description: TODO
	 * @param ps
	 * @throws SQLException
	 * @see org.springframework.jdbc.core.PreparedStatementSetter#setValues(java.sql.PreparedStatement)
	 */
	@Override
	public void setValues(PreparedStatement ps) throws SQLException {
		try {
			for (int i = 0; i < this.pdList.size(); i++) {
				Object value = null;
				PropertyDescriptor pd = this.pdList.get(i);
				Method getter = pd.getReadMethod();
				// 非embedded字段
				if (null == this.embeddedMap.get(pd)) {
					value = ModelSqlUtil.getColumnValue(this.obj, getter);
				// embedded字段
				} else {
				    // embedded 对象
					Object embeddedObj = this.embeddedMap.get(pd).getReadMethod().invoke(this.obj, new Object[0]);
					// 如果embedded 对象为空，则new一个空对象，使sql注入默认值，避免为空时基本数据类型注入null报错
					embeddedObj = (null == embeddedObj) ? BeanUtils.instantiate(this.embeddedMap.get(pd).getPropertyType()) : embeddedObj;
					// embedded 对象的属性值
					value =ModelSqlUtil.getColumnValue(embeddedObj, getter);
				}
				// 处理clob注入
				if (null != getter.getAnnotation(InjectClob.class)) {
					lobHandler.getLobCreator().setClobAsString(ps, i + 1, null == value ? null : String.valueOf(value));
				// 处理blob注入
				} else if (null != getter.getAnnotation(InjectBlob.class)) {
				    lobHandler.getLobCreator().setBlobAsBytes(ps, i + 1, null == value ? null : (byte[]) value);
				} else {
					ps.setObject(i + 1, value);
				}
			}
		} catch (Exception ex) {
			logger.error("error on setValues:", ex);
			throw new RuntimeException("error on setValues", ex);
		}
	}
}
