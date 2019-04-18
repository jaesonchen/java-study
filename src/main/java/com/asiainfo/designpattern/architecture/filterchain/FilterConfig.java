package com.asiainfo.designpattern.architecture.filterchain;

import java.util.HashMap;
import java.util.Map;

/**
 * TODO
 * 
 * @author       zq
 * @date         2018年5月4日  下午10:50:44
 * Copyright: 	  北京亚信智慧数据科技有限公司
 */
public class FilterConfig {

    private String filterClass;
    private Map<String, Object> properties = new HashMap<>();
    public FilterConfig(String filterClass) {
        this.filterClass = filterClass;
    }
    
    public void setParam(String paramName, Object paramValue) {
        properties.put(paramName, paramValue);
    }
    public Object getParam(String paramName) {
        return properties.get(paramName);
    }
    public void setFilterClass(String filterClass) {
        this.filterClass = filterClass;
    }
    public String getFilterClass() {
        return filterClass;
    }

    @Override
    public String toString() {
        return "FilterConfig [filterClass=" + filterClass + ", properties=" + properties + "]";
    }
}
