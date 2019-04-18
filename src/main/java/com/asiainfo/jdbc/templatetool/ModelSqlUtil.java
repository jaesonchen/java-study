package com.asiainfo.jdbc.templatetool;

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
 * @Description: model、sql转换工具
 * 
 * @author       zq
 * @date         2017年7月1日  下午12:32:05
 * Copyright: 	  北京亚信智慧数据科技有限公司
 */
public class ModelSqlUtil {

	static final Logger logger = LoggerFactory.getLogger(ModelSqlUtil.class);
	
	/**
	 * @Description: 返回类型的select 语句和id参数的getter method
	 * @author chenzq
	 * @date 2019年3月27日 下午3:04:01
	 * @param clazz
	 * @return
	 */
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
		result.setMethods(methodList.toArray(new Method[methodList.size()]));
		return result;
	}

	/**
     * @Description: 返回类型的update语句、pd、embedded pd，支持值类型的注解转换，包括embedded对象
     * @author chenzq
     * @date 2019年3月27日 下午3:50:01
     * @param clazz
     * @return
     */
    public static SqlPropertyDecriptorPairs generateUpdateSql(Class<?> clazz) {
        // 读取类型所有属性对应的列
        Map<String, PropertyDescriptor> columnMap = getTableColumns(clazz);
        StringBuilder sql = new StringBuilder();
        sql.append("update ").append(getTableName(clazz)).append(" set ");
        List<PropertyDescriptor> pdList = new ArrayList<>();
        Map<PropertyDescriptor, PropertyDescriptor> embeddedMap = new HashMap<>();
        for (Map.Entry<String, PropertyDescriptor> entry : columnMap.entrySet()) {
            // 忽略标注Id注解的列
            if (null != entry.getValue().getReadMethod().getAnnotation(Id.class) 
                    || null != getFieldAnnotation(clazz, entry.getValue().getName(), Id.class)) {
                continue;
            }
            // 处理Embedded注解的嵌入类型
            if (null != getFieldAnnotation(clazz, entry.getValue().getName(), Embedded.class)) {
                // 读取嵌入类型所有属性对应的列
                Map<String, PropertyDescriptor> embeddedColumnMap = getTableColumns(entry.getValue().getPropertyType());
                for (Map.Entry<String, PropertyDescriptor> embeddedEntry : embeddedColumnMap.entrySet()) {
                    if (pdList.size() > 0) {
                        sql.append(", ");
                    }
                    sql.append(embeddedEntry.getKey()).append("=?");
                    pdList.add(embeddedEntry.getValue());
                    // embedded列对应的embedded类型
                    embeddedMap.put(embeddedEntry.getValue(), entry.getValue());
                }
            } else {
                if (pdList.size() > 0) {
                    sql.append(", ");
                }
                sql.append(entry.getKey()).append("=?");
                pdList.add(entry.getValue());
            }
        }
        sql.append(" where ");
        int i = 0;
        for (Map.Entry<String, PropertyDescriptor> entry : getTableId(clazz).entrySet()) {
            sql.append(i == 0 ? "" : "and ").append(entry.getKey()).append("=? ");
            pdList.add(entry.getValue());
        }
        return new SqlPropertyDecriptorPairs(sql.toString(), pdList, embeddedMap);
    }
    
	/**
	 * @Description: 返回类型的insert语句、pd、embedded pd，支持值类型的注解转换，包括embedded对象
	 * @author chenzq
	 * @date 2019年3月27日 下午3:50:01
	 * @param clazz
	 * @return
	 */
	public static SqlPropertyDecriptorPairs generateInsertSql(Class<?> clazz) {
		// 读取类型所有属性对应的列
		Map<String, PropertyDescriptor> columnMap = getTableColumns(clazz);
		StringBuilder sql = new StringBuilder();
		sql.append("insert into ").append(getTableName(clazz)).append("(");
		List<PropertyDescriptor> pdList = new ArrayList<>();
		Map<PropertyDescriptor, PropertyDescriptor> embeddedMap = new HashMap<>();
		for (Map.Entry<String, PropertyDescriptor> entry : columnMap.entrySet()) {
		    // 处理Embedded注解的嵌入类型
			if (null != getFieldAnnotation(clazz, entry.getValue().getName(), Embedded.class)) {
			    // 读取嵌入类型所有属性对应的列
				Map<String, PropertyDescriptor> embeddedColumnMap = getTableColumns(entry.getValue().getPropertyType());
				for (Map.Entry<String, PropertyDescriptor> embeddedEntry : embeddedColumnMap.entrySet()) {
					if (pdList.size() > 0) {
						sql.append(", ");
					}
					sql.append(embeddedEntry.getKey());
					pdList.add(embeddedEntry.getValue());
					// embedded列对应的embedded类型
					embeddedMap.put(embeddedEntry.getValue(), entry.getValue());
				}
			} else {
				if (pdList.size() > 0) {
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
	
	/**
	 * @Description: 返回属性对应的列值，如果有Converter注解，需要读取注解配置的转换类进行转换
	 * @author chenzq
	 * @date 2019年3月27日 下午3:48:27
	 * @param obj
	 * @param getter
	 * @return
	 * @throws Exception
	 */
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
	
	/**
	 * @Description: 返回Converter注解设置的转换类型实例，如果有多个，则把前一个作为第二个的delegate配置转换链
	 * @author chenzq
	 * @date 2019年3月27日 下午3:46:32
	 * @param convertAnnotation
	 * @return
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 */
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
	
	/**
	 * @Description: 返回类型对应的表名，由类名根据驼峰转换，或者由Table注解提供
	 * @author chenzq
	 * @date 2019年3月27日 下午3:04:54
	 * @param clazz
	 * @return
	 */
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
    
    /**
     * @Description: 返回类型所有id的列名和对应的属性描述
     * @author chenzq
     * @date 2019年3月27日 下午3:09:17
     * @param clazz
     * @return
     */
    public static Map<String, PropertyDescriptor> getTableId(Class<?> clazz) {
        
    	Map<String, PropertyDescriptor> result = new HashMap<>();
		PropertyDescriptor[] pds = BeanUtils.getPropertyDescriptors(clazz);
		for (PropertyDescriptor pd : pds) {
		    // 是否存在Id注解
			if (pd.getReadMethod() != null 
			        && (pd.getReadMethod().getAnnotation(Id.class) != null
			                || getFieldAnnotation(clazz, pd.getName(), Id.class) != null)) {
				result.put(getPropertyColumnName(clazz, pd.getName()), pd);
			}
		}
    	return result;
    }
    
    /**
     * @Description: 返回类型所有的列名和属性描述
     * @author chenzq
     * @date 2019年3月27日 下午3:15:52
     * @param clazz
     * @return
     */
    public static Map<String, PropertyDescriptor> getTableColumns(Class<?> clazz) {
    	
    	Map<String, PropertyDescriptor> result = new HashMap<>();
		PropertyDescriptor[] pds = BeanUtils.getPropertyDescriptors(clazz);
		for (PropertyDescriptor pd : pds) {
		    // 忽略Class类型和没有getter的属性
			if (!"class".equalsIgnoreCase(pd.getName()) && pd.getReadMethod() != null) {
			    // 放弃标注为Transient的字段
				if (null != getFieldAnnotation(clazz, pd.getName(), Transient.class)) {
					continue;
				}
				result.put(getPropertyColumnName(clazz, pd.getName()), pd);
			}
		}
    	return result;
    }
    
    /**
     * @Description: 返回类型属性对应的数据库列名
     * @author chenzq
     * @date 2019年3月27日 下午3:12:55
     * @param clazz
     * @param propertyName
     * @return
     */
    public static String getPropertyColumnName(Class<?> clazz, String propertyName) {
    	// 取属性对应的Column注解
    	Column column = getFieldAnnotation(clazz, propertyName, Column.class);
    	if (null != column) {
    		return column.name();
    	}
    	return CamelUtil.camel2underscore(propertyName);
    }
    
    /**
     * @Description: 返回类属性的注解类型
     * @author chenzq
     * @date 2019年3月27日 下午3:14:34
     * @param clazz
     * @param propertyName
     * @param annotationClazz
     * @return
     */
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
