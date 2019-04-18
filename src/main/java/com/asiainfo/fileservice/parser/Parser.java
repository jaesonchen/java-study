package com.asiainfo.fileservice.parser;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @Description: 数据文件行字段到封装对象属性类型的映射，包含字段对应的行记录坐标
 *               支持多个解析器的嵌套，注解标在set方法上
 * 
 * @author       zq
 * @date         2017年6月10日  下午5:20:11
 * Copyright: 	  北京亚信智慧数据科技有限公司
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE})
public @interface Parser {
	int value();
	Class<?>[] clazz() default Null2DefaultParseServiceImpl.class;
}
