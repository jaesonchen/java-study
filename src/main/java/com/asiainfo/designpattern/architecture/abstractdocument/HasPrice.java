package com.asiainfo.designpattern.architecture.abstractdocument;

import java.util.Optional;

/**
 * TODO
 * 
 * @author       zq
 * @date         2018年4月25日  下午6:43:44
 * Copyright: 	  北京亚信智慧数据科技有限公司
 */
public interface HasPrice extends Document {

    String PROPERTY = "price";

    /**
     * 子类型特有属性
     * 
     * @return
     */
    default Optional<Number> getPrice() {
        return Optional.ofNullable((Number) get(PROPERTY));
    }
}
