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
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.support.lob.DefaultLobHandler;
import org.springframework.jdbc.support.lob.LobHandler;

import com.asiainfo.jdbc.converter.InjectBlob;
import com.asiainfo.jdbc.converter.InjectClob;

/**
 * @Description: 多条记录sql的 ? 参数设置
 * 
 * @author       zq
 * @date         2017年7月1日  下午10:51:46
 * Copyright: 	  北京亚信智慧数据科技有限公司
 */
public class BatchUpdateSetter implements BatchPreparedStatementSetter {

	protected final Logger logger = LoggerFactory.getLogger(getClass());
	protected final LobHandler lobHandler = new DefaultLobHandler();
	
	private List<Object> list;
	private List<PropertyDescriptor> pdList;
	private Map<PropertyDescriptor, PropertyDescriptor> embeddedMap;
	public BatchUpdateSetter(List<Object> list, List<PropertyDescriptor> pdList, Map<PropertyDescriptor, PropertyDescriptor> embeddedMap) {
		this.list = list;
		this.pdList = pdList;
		this.embeddedMap = (null == embeddedMap) ? new HashMap<PropertyDescriptor, PropertyDescriptor>() : embeddedMap;
	}
	
	/* 
	 * @Description: TODO
	 * @param ps
	 * @param i
	 * @throws SQLException
	 * @see org.springframework.jdbc.core.BatchPreparedStatementSetter#setValues(java.sql.PreparedStatement, int)
	 */
	@Override
	public void setValues(PreparedStatement ps, int i) throws SQLException {
		Object obj = list.get(i);
		try {
			for (int j = 0; j < this.pdList.size(); j++) {
			    Object value = null;
                PropertyDescriptor pd = this.pdList.get(j);
                Method getter = pd.getReadMethod();
                // 非embedded字段
				if (null == this.embeddedMap.get(pd)) {
					value = ModelSqlUtil.getColumnValue(obj, getter);
				// embedded字段
				} else {
                    // embedded 对象
                    Object embeddedObj = this.embeddedMap.get(pd).getReadMethod().invoke(obj, new Object[0]);
                    // 如果embedded 对象为空，则new一个空对象，使sql注入默认值，避免为空时基本数据类型注入null报错
                    embeddedObj = (null == embeddedObj) ? BeanUtils.instantiate(this.embeddedMap.get(pd).getPropertyType()) : embeddedObj;
                    // embedded 对象的属性值
                    value =ModelSqlUtil.getColumnValue(embeddedObj, getter);
				}
				// 处理clob注入
                if (null != getter.getAnnotation(InjectClob.class)) {
                    lobHandler.getLobCreator().setClobAsString(ps, j + 1, null == value ? null : String.valueOf(value));
                // 处理blob注入
                } else if (null != getter.getAnnotation(InjectBlob.class)) {
                    lobHandler.getLobCreator().setBlobAsBytes(ps, j + 1, null == value ? null : (byte[]) value);
                } else {
                    ps.setObject(j + 1, value);
                }
			}
		} catch (Exception ex) {
			logger.error("error on setValues:", ex);
			throw new RuntimeException("error on setValues", ex);
		}
	}

	/* 
	 * @Description: TODO
	 * @return
	 * @see org.springframework.jdbc.core.BatchPreparedStatementSetter#getBatchSize()
	 */
	@Override
	public int getBatchSize() {
		return null == this.list ? 0 : this.list.size();
	}
}
