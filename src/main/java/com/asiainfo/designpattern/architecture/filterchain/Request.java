package com.asiainfo.designpattern.architecture.filterchain;

import java.util.HashMap;
import java.util.Map;

/**
 * TODO
 * 
 * @author       zq
 * @date         2018年5月4日  下午10:37:09
 * Copyright: 	  北京亚信智慧数据科技有限公司
 */
public class Request {

    Map<String, Object> param = new HashMap<>();
    
    public void setParameter(String name, Object value) {
        param.put(name, value);
    }
    public Object getParameter(String name) {
        return param.get(name);
    }
}
