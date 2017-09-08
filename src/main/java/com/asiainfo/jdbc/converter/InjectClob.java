package com.asiainfo.jdbc.converter;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @Description: TODO
 * 
 * @author       zq
 * @date         2017年7月2日  上午9:33:31
 * Copyright: 	  北京亚信智慧数据科技有限公司
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE})
public @interface InjectClob {

}
