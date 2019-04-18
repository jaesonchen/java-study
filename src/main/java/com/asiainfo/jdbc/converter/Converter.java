package com.asiainfo.jdbc.converter;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @Description: 数据转换注解，用于getter/setter上，多个转换器组成转换链时，从第一个开始执行
 * 
 * @author       zq
 * @date         2017年6月25日  上午11:52:03
 * Copyright: 	  北京亚信智慧数据科技有限公司
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface Converter {
	Class<?>[] value();
}
