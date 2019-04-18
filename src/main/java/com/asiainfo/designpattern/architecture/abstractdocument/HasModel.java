package com.asiainfo.designpattern.architecture.abstractdocument;

import java.util.Optional;

/**
 * TODO
 * 
 * @author       zq
 * @date         2018年4月25日  下午6:41:44
 * Copyright: 	  北京亚信智慧数据科技有限公司
 */
public interface HasModel extends Document {

    String PROPERTY = "model";
    
    /**
     * 子类型特有属性
     * 
     * @return
     */
    default Optional<String> getModel() {
        return Optional.ofNullable((String) get(PROPERTY));
    }
}
