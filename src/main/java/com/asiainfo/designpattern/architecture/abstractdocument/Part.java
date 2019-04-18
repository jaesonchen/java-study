package com.asiainfo.designpattern.architecture.abstractdocument;

import java.util.Map;

/**
 * 子类型封装类
 * 
 * @author       zq
 * @date         2018年4月25日  下午6:47:43
 * Copyright: 	  北京亚信智慧数据科技有限公司
 */
public class Part extends AbstractDocument implements HasPrice, HasModel, HasType {

    /**
     * Part实例化方法
     * 
     * @param properties
     */
    protected Part(Map<String, Object> properties) {
        super(properties);
    }
}
