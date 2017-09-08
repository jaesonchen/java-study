package com.asiainfo.jdbc.converter;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @Description: TODO
 * 
 * @author       zq
 * @date         2017年6月25日  上午11:52:03
 * Copyright: 	  北京亚信智慧数据科技有限公司
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE})
public @interface Converter {
	Class<?>[] value();
}
