package com.asiainfo.jdbc;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Transient;

import org.apache.commons.beanutils.PropertyUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.SingleColumnRowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

import com.asiainfo.jdbc.util.BatchUpdateSetter;
import com.asiainfo.jdbc.util.CamelUtil;
import com.asiainfo.jdbc.util.ModelSqlUtil;
import com.asiainfo.jdbc.util.SqlMethodPairs;
import com.asiainfo.jdbc.util.SqlPropertyDecriptorPairs;
import com.asiainfo.jdbc.util.UpdateSetter;

public class JdbcTemplateTool {

	protected final Logger logger = LoggerFactory.getLogger(getClass());
	
	protected JdbcTemplate jdbcTemplate;
	public JdbcTemplateTool(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	//简单保存方法，属性类型与数据库类型完全一致
	public void simpleSave(Object obj) {
			
		SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(this.jdbcTemplate);
		simpleJdbcInsert.withTableName(ModelSqlUtil.getTableName(obj.getClass()));
		Map<String, Object> map = null;
		try {
			map = PropertyUtils.describe(obj);
		} catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException ex) {
			logger.error("error describe bean:", ex);
			throw new RuntimeException("error describe bean", ex);
		}
		
		if (map != null) {
			Field[] fields = obj.getClass().getDeclaredFields();
			for (int i = 0; i < fields.length; i++) {
				if (fields[i].getAnnotation(Transient.class) != null) {
					map.remove(fields[i].getName());
					continue;
				}
				Column column = fields[i].getAnnotation(Column.class);
				if (column != null) {
					map.put(column.name(), map.get(fields[i].getName()));
				} else {
					map.put(CamelUtil.camel2underscore(fields[i].getName()), map.get(fields[i].getName()));
				}
				map.remove(fields[i].getName());
			}
			simpleJdbcInsert.compile();
			simpleJdbcInsert.execute(map);
		}
	};
	
	//单个对象保存方法，属性类型与数据库类型之间需要转换
	public void save(Object obj) {
		
		SqlPropertyDecriptorPairs sqlPd = ModelSqlUtil.generateInsertSql(obj.getClass());
		this.jdbcTemplate.update(sqlPd.getSql(), new UpdateSetter(obj, sqlPd.getPdList(), sqlPd.getEmbeddedMap()));
	}
	
	//批量插入
	public int batchUpdate(List<Object> list) {
		
		if (null == list || list.isEmpty()) {
			return 0;
		}
		Class<?> clazz = list.get(0).getClass();
		SqlPropertyDecriptorPairs sqlPd = ModelSqlUtil.generateInsertSql(clazz);
		
		return this.jdbcTemplate.batchUpdate(sqlPd.getSql(), new BatchUpdateSetter(list, sqlPd.getPdList(), sqlPd.getEmbeddedMap())).length;
	}
	
	
	public <T> T get(Class<T> clazz, Object... ids) {
		
		SqlMethodPairs sqlParam = ModelSqlUtil.generateSelectSql(clazz);
		List<T> list = this.jdbcTemplate.query(sqlParam.getSql(), ids, new EnhanceBeanPropertyRowMapper<T>(clazz));
		return (null == list || list.isEmpty()) ? null : list.get(0);
	}
	
	public <T> T get(String sql, Class<T> clazz) {
		
		List<T> list = jdbcTemplate.query(sql, new EnhanceBeanPropertyRowMapper<T>(clazz));
		return (null == list || list.isEmpty()) ? null : list.get(0);
	}

	public <T> T get(String sql, Object[] params, Class<T> clazz) {
		
		if (params == null || params.length == 0) {
			return get(sql, clazz);
		}
		List<T> list = jdbcTemplate.query(sql, params, new EnhanceBeanPropertyRowMapper<T>(clazz));
		return (null == list || list.isEmpty()) ? null : list.get(0);
	}

	public <T> List<T> list(String sql, Class<T> clazz) {
		return jdbcTemplate.query(sql, new EnhanceBeanPropertyRowMapper<T>(clazz));
	}

	public <T> List<T> list(String sql, Object[] params, Class<T> clazz) {

		if (params == null || params.length == 0) {
			return list(sql, clazz);
		} else {
			return jdbcTemplate.query(sql, params, new EnhanceBeanPropertyRowMapper<T>(clazz));
		}
	}
	
	public <T> List<T> listSingleColumn(String sql, Class<T> clazz) {
		List<T> list = jdbcTemplate.query(sql, new SingleColumnRowMapper<T>(clazz));
		return list;
	}

	public <T> List<T> listSingleColumn(String sql, Object[] params, Class<T> clazz) {
		
		if (params == null || params.length == 0) {
			return listSingleColumn(sql, clazz);
		} else {
			return jdbcTemplate.query(sql, params, new SingleColumnRowMapper<T>(clazz));
		}
	}
	
	public long count(String sql) {
		return jdbcTemplate.queryForObject(sql, Long.class);
	}

	public long count(String sql, Object[] params) {
		
		long count = 0;
		if (params == null || params.length == 0) {
			count = count(sql);
		} else {
			count = jdbcTemplate.queryForObject(sql, params, Long.class);
		}
		return count;
	}
}

