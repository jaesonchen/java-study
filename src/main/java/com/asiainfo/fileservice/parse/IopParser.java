package com.asiainfo.fileservice.parse;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @Description: TODO
 * 
 * @author       zq
 * @date         2017年6月10日  下午5:20:11
 * Copyright: 	  北京亚信智慧数据科技有限公司
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE})
public @interface IopParser {
	int value();
	Class<?>[] clazz() default Null2DefaultParseServiceImpl.class;
}
