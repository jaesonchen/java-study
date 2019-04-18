package com.asiainfo.designpattern.architecture.abstractdocument;

import java.util.stream.Stream;

/**
 * TODO
 * 
 * @author       zq
 * @date         2018年4月25日  下午6:46:05
 * Copyright: 	  北京亚信智慧数据科技有限公司
 */
public interface HasParts extends Document {

    String PROPERTY = "parts";
    
    /**
     * 获取子类型
     * 
     * @return
     */
    default Stream<Part> getParts() {
        return children(PROPERTY, Part::new);
    }
}
