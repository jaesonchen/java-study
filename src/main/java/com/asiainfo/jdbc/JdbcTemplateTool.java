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

import com.asiainfo.jdbc.templatetool.BatchUpdateSetter;
import com.asiainfo.jdbc.templatetool.CamelUtil;
import com.asiainfo.jdbc.templatetool.ModelSqlUtil;
import com.asiainfo.jdbc.templatetool.SqlMethodPairs;
import com.asiainfo.jdbc.templatetool.SqlPropertyDecriptorPairs;
import com.asiainfo.jdbc.templatetool.UpdateSetter;

/**
 * @Description: JdbcTemplate 增强工具，用于自动转换对象到sql语句
 * 
 * @author chenzq  
 * @date 2019年3月20日 下午9:33:29
 * @version V1.0
 * @Copyright: Copyright(c) 2019 jaesonchen.com Inc. All rights reserved.
 */
public class JdbcTemplateTool {

	protected final Logger logger = LoggerFactory.getLogger(getClass());
	
	protected JdbcTemplate jdbcTemplate;
	public JdbcTemplateTool(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	/**
	 * @Description: 简单保存方法，属性类型与数据库类型完全一致
	 * @author chenzq
	 * @date 2019年3月27日 下午4:53:53
	 * @param obj
	 */
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
				Object fieldProperties = map.get(fields[i].getName());
				map.remove(fields[i].getName());
				if (column != null) {
					map.put(column.name(), fieldProperties);
				} else {
					map.put(CamelUtil.camel2underscore(fields[i].getName()), fieldProperties);
				}
			}
			simpleJdbcInsert.compile();
			simpleJdbcInsert.execute(map);
		}
	};
	
	/**
	 * @Description: 单个对象插入，自动生成insert sql，属性类型与数据库类型之间需要转换
	 * @author chenzq
	 * @date 2019年3月27日 下午3:39:41
	 * @param obj
	 */
	public void save(Object obj) {
		SqlPropertyDecriptorPairs sqlPd = ModelSqlUtil.generateInsertSql(obj.getClass());
		this.jdbcTemplate.update(sqlPd.getSql(), new UpdateSetter(obj, sqlPd.getPdList(), sqlPd.getEmbeddedMap()));
	}
	
	/**
	 * @Description: 更新记录，自动生成update sql，属性类型与数据库类型之间需要转换
	 * @author chenzq
	 * @date 2019年3月27日 下午4:36:42
	 * @param obj
	 */
	public void update(Object obj) {
	    SqlPropertyDecriptorPairs sqlPd = ModelSqlUtil.generateUpdateSql(obj.getClass());
        this.jdbcTemplate.update(sqlPd.getSql(), new UpdateSetter(obj, sqlPd.getPdList(), sqlPd.getEmbeddedMap()));
	}
	
	/**
	 * @Description: 批量插入，自动生成insert sql，属性类型与数据库类型之间需要转换
	 * @author chenzq
	 * @date 2019年3月27日 下午3:36:43
	 * @param list
	 * @return
	 */
	public int batchUpdate(List<Object> list) {
		if (null == list || list.isEmpty()) {
			return 0;
		}
		Class<?> clazz = list.get(0).getClass();
		SqlPropertyDecriptorPairs sqlPd = ModelSqlUtil.generateInsertSql(clazz);
		return this.jdbcTemplate.batchUpdate(sqlPd.getSql(), new BatchUpdateSetter(list, sqlPd.getPdList(), sqlPd.getEmbeddedMap())).length;
	}

	/**
	 * @Description: 指定id查询，自动生成select sql，model需要注解支持
	 * @author chenzq
	 * @date 2019年3月27日 下午3:35:50
	 * @param clazz
	 * @param ids
	 * @return
	 */
	public <T> T get(Class<T> clazz, Object... ids) {
		SqlMethodPairs sqlParam = ModelSqlUtil.generateSelectSql(clazz);
		List<T> list = this.jdbcTemplate.query(sqlParam.getSql(), ids, new EnhanceBeanPropertyRowMapper<T>(clazz));
		return (null == list || list.isEmpty()) ? null : list.get(0);
	}
	
	/**
	 * @Description: 返回一条指定类型记录
	 * @author chenzq
	 * @date 2019年3月27日 下午3:34:42
	 * @param sql
	 * @param clazz
	 * @return
	 */
	public <T> T get(String sql, Class<T> clazz) {
		List<T> list = jdbcTemplate.query(sql, new EnhanceBeanPropertyRowMapper<T>(clazz));
		return (null == list || list.isEmpty()) ? null : list.get(0);
	}

	/**
	 * @Description: 返回一条指定类型记录
	 * @author chenzq
	 * @date 2019年3月27日 下午3:35:00
	 * @param sql
	 * @param params
	 * @param clazz
	 * @return
	 */
	public <T> T get(String sql, Object[] params, Class<T> clazz) {
		if (params == null || params.length == 0) {
			return get(sql, clazz);
		}
		List<T> list = jdbcTemplate.query(sql, params, new EnhanceBeanPropertyRowMapper<T>(clazz));
		return (null == list || list.isEmpty()) ? null : list.get(0);
	}

	/**
	 * @Description: list查询，返回指定类型
	 * @author chenzq
	 * @date 2019年3月27日 下午3:33:49
	 * @param sql
	 * @param clazz
	 * @return
	 */
	public <T> List<T> list(String sql, Class<T> clazz) {
		return jdbcTemplate.query(sql, new EnhanceBeanPropertyRowMapper<T>(clazz));
	}

	/**
	 * @Description: list查询，返回指定类型
	 * @author chenzq
	 * @date 2019年3月27日 下午3:33:02
	 * @param sql
	 * @param params
	 * @param clazz
	 * @return
	 */
	public <T> List<T> list(String sql, Object[] params, Class<T> clazz) {
		if (params == null || params.length == 0) {
			return list(sql, clazz);
		} else {
			return jdbcTemplate.query(sql, params, new EnhanceBeanPropertyRowMapper<T>(clazz));
		}
	}
	
	/**
	 * @Description: 只有返回1列结果的查询，clazz指定那一列的类型
	 * @author chenzq
	 * @date 2019年3月27日 下午3:29:21
	 * @param sql
	 * @param clazz
	 * @return
	 */
	public <T> List<T> listSingleColumn(String sql, Class<T> clazz) {
		List<T> list = jdbcTemplate.query(sql, new SingleColumnRowMapper<T>(clazz));
		return list;
	}

	/**
	 * @Description: 只有返回1列结果的查询，clazz指定那一列的类型
	 * @author chenzq
	 * @date 2019年3月27日 下午3:30:45
	 * @param sql
	 * @param params
	 * @param clazz
	 * @return
	 */
	public <T> List<T> listSingleColumn(String sql, Object[] params, Class<T> clazz) {
		if (params == null || params.length == 0) {
			return listSingleColumn(sql, clazz);
		} else {
			return jdbcTemplate.query(sql, params, new SingleColumnRowMapper<T>(clazz));
		}
	}
	
	/**
	 * @Description: count类型sql
	 * @author chenzq
	 * @date 2019年3月27日 下午3:30:57
	 * @param sql
	 * @return
	 */
	public long count(String sql) {
		return jdbcTemplate.queryForObject(sql, Long.class);
	}

	/**
	 * @Description: count类型sql
	 * @author chenzq
	 * @date 2019年3月27日 下午3:31:23
	 * @param sql
	 * @param params
	 * @return
	 */
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

