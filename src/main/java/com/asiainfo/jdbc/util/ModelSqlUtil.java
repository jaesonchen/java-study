package com.asiainfo.jdbc.util;

import java.beans.PropertyDescriptor;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;

import com.asiainfo.jdbc.converter.Converter;
import com.asiainfo.jdbc.converter.IConvertService;

/**
 * @Description: TODO
 * 
 * @author       zq
 * @date         2017年7月1日  下午12:32:05
 * Copyright: 	  北京亚信智慧数据科技有限公司
 */
public class ModelSqlUtil {

	static final Logger logger = LoggerFactory.getLogger(ModelSqlUtil.class);
	
	//返回类的select 语句和getter method
	public static SqlMethodPairs generateSelectSql(Class<?> clazz) {
		
		SqlMethodPairs result = new SqlMethodPairs();
		StringBuilder sql = new StringBuilder();
		sql.append("select * from " + getTableName(clazz) + " where ");
		List<Method> methodList = new ArrayList<Method>();
		for (Map.Entry<String, PropertyDescriptor> entry : getTableId(clazz).entrySet()) {
			sql.append(methodList.isEmpty() ? "" : "and ").append(entry.getKey()).append("=? ");
			methodList.add(entry.getValue().getReadMethod());
		}
		result.setSql(sql.toString());
		result.setMethods(methodList.toArray(new Method[0]));
		return result;
	}
	
	//返回类的insert语句、pd、embedded pd
	public static SqlPropertyDecriptorPairs generateInsertSql(Class<?> clazz) {
		
		Map<String, PropertyDescriptor> columnMap = ModelSqlUtil.getTableColumns(clazz);
		StringBuilder sql = new StringBuilder();
		sql.append("insert into ").append(ModelSqlUtil.getTableName(clazz)).append("(");
		List<PropertyDescriptor> pdList = new ArrayList<PropertyDescriptor>();
		Map<PropertyDescriptor, PropertyDescriptor> embeddedMap = new HashMap<PropertyDescriptor, PropertyDescriptor>();
		for (Map.Entry<String, PropertyDescriptor> entry : columnMap.entrySet()) {
			if (null != ModelSqlUtil.getFieldAnnotation(clazz, entry.getValue().getName(), Embedded.class)) {
				Map<String, PropertyDescriptor> embeddedColumnMap = ModelSqlUtil.getTableColumns(entry.getValue().getPropertyType());
				for (Map.Entry<String, PropertyDescriptor> embeddedEntry : embeddedColumnMap.entrySet()) {
					if (!pdList.isEmpty()) {
						sql.append(", ");
					}
					sql.append(embeddedEntry.getKey());
					pdList.add(embeddedEntry.getValue());
					embeddedMap.put(embeddedEntry.getValue(), entry.getValue());
				}
			} else {
				if (!pdList.isEmpty()) {
					sql.append(", ");
				}
				sql.append(entry.getKey());
				pdList.add(entry.getValue());
			}
		}
		sql.append(") values(");
		for (int i = 0; i < pdList.size(); i++) {
			if (i > 0) {
				sql.append(", ");
			}
			sql.append("?");
		}
		sql.append(") ");
		
		return new SqlPropertyDecriptorPairs(sql.toString(), pdList, embeddedMap);
	}
	
	//返回属性对应的列值
	public static Object getColumnValue(Object obj, Method getter) throws Exception {
		
		Converter convert = getter.getAnnotation(Converter.class);
		try {
			Object value = getter.invoke(obj, new Object[0]);
			if (null == convert) {
				return value;
			}
			IConvertService<?> service = generateConvertService(convert);
			return service.convert(value);
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
			logger.error("error on convert2columnType:", ex);
			throw ex;
		}
	}
	
	//返回Converter注解的注册类实例
	public static IConvertService<?> generateConvertService(Converter convertAnnotation) throws InstantiationException, IllegalAccessException {
		
		Class<?>[] clazzArray = convertAnnotation.value();
		IConvertService<?> prefixService = null;
		IConvertService<?> service = null;
		for (Class<?> serviceClazz : clazzArray) {
			service = (IConvertService<?>) serviceClazz.newInstance();
			if (null != prefixService) {
				service.setDelegate(prefixService);
			}
			prefixService = service;
		}
		return service;
	}
	
	//返回类的表名
    public static String getTableName(Class<?> clazz) {

        Table table = clazz.getAnnotation(Table.class);
        if (table != null) {
            if (StringUtils.isNotEmpty(table.catalog())) {
                return table.catalog() + "." + table.name();
            }
            return table.name();
        }
        return CamelUtil.camel2underscore(clazz.getSimpleName());
    }
    
  //返回类所有id的列名和属性描述
    public static Map<String, PropertyDescriptor> getTableId(Class<?> clazz) {
    	
    	Map<String, PropertyDescriptor> result = new HashMap<>();
		PropertyDescriptor[] pds = BeanUtils.getPropertyDescriptors(clazz);
		for (PropertyDescriptor pd : pds) {
			if (pd.getReadMethod() != null) {
				if (null == pd.getReadMethod().getAnnotation(Id.class)) {
					continue;
				}
				result.put(getPropertyColumnName(clazz, pd.getName()), pd);
			}
		}
    	return result;
    }
    
    //返回类所有的列名和属性描述
    public static Map<String, PropertyDescriptor> getTableColumns(Class<?> clazz) {
    	
    	Map<String, PropertyDescriptor> result = new HashMap<>();
		PropertyDescriptor[] pds = BeanUtils.getPropertyDescriptors(clazz);
		for (PropertyDescriptor pd : pds) {
			if (!"class".equalsIgnoreCase(pd.getName()) && pd.getReadMethod() != null) {
				if (null != getFieldAnnotation(clazz, pd.getName(), Transient.class)) {
					continue;
				}
				result.put(getPropertyColumnName(clazz, pd.getName()), pd);
			}
		}
    	return result;
    }
    
    //返回类属性的列名
    public static String getPropertyColumnName(Class<?> clazz, String propertyName) {
    	
    	Column column = getFieldAnnotation(clazz, propertyName, Column.class);
    	if (null != column) {
    		return column.name();
    	}
    	return CamelUtil.camel2underscore(propertyName);
    }
    
    //返回类属性的注解类型
	public static <M extends Annotation> M getFieldAnnotation(Class<?> clazz, String propertyName, Class<M> annotationClazz) {
		Field[] fields = clazz.getDeclaredFields();
		for (Field field : fields) {
			if (field.getName().equals(propertyName)) {
				return field.getAnnotation(annotationClazz);
			}
		}
		return null;
	}
}
