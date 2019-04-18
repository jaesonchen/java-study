package com.asiainfo.designpattern.architecture.abstractdocument;

import java.util.Optional;

/**
 * TODO
 * 
 * @author       zq
 * @date         2018年4月25日  下午6:44:39
 * Copyright: 	  北京亚信智慧数据科技有限公司
 */
public interface HasType extends Document {

    String PROPERTY = "type";
    
    /**
     * 子类型特有属性
     * 
     * @return
     */
    default Optional<String> getType() {
        return Optional.ofNullable((String) get(PROPERTY));
    }
}
