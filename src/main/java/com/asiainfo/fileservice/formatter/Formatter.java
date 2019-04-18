package com.asiainfo.fileservice.formatter;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @Description: 对象属性到数据文件行字段的映射，包含属性写入行后的坐标
 *               注解标在get方法上
 * 
 * @author       zq
 * @date         2017年6月10日  下午2:48:00
 * Copyright: 	  北京亚信智慧数据科技有限公司
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE})
public @interface Formatter {
	int value();
	Class<?> clazz() default DefaultStringFormatServiceImpl.class;
}
