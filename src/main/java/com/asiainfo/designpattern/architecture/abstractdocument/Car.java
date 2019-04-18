package com.asiainfo.designpattern.architecture.abstractdocument;

import java.util.Map;

/**
 * TODO
 * 
 * @author       zq
 * @date         2018年4月25日  下午6:51:45
 * Copyright: 	  北京亚信智慧数据科技有限公司
 */
public class Car extends AbstractDocument implements HasModel, HasPrice, HasParts {

    /**
     * Car实例化方法
     * 
     * @param properties
     */
    public Car(Map<String, Object> properties) {
        super(properties);
    }
}
