package com.asiainfo.jdbc.util;

import java.beans.PropertyDescriptor;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.support.lob.DefaultLobHandler;
import org.springframework.jdbc.support.lob.LobHandler;

import com.asiainfo.jdbc.converter.InjectClob;

/**
 * @Description: TODO
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
				if (null == this.embeddedMap.get(this.pdList.get(j))) {
					value = ModelSqlUtil.getColumnValue(obj, this.pdList.get(j).getReadMethod());
				} else {
					Object embeddedValue = this.embeddedMap.get(this.pdList.get(j)).getReadMethod().invoke(obj, new Object[0]);
					value = (null == embeddedValue) ? null : ModelSqlUtil.getColumnValue(embeddedValue, this.pdList.get(j).getReadMethod());
				}

				if (null != this.pdList.get(j).getReadMethod().getAnnotation(InjectClob.class)) {
					lobHandler.getLobCreator().setClobAsString(ps, j + 1, null == value ? null : String.valueOf(value));
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
