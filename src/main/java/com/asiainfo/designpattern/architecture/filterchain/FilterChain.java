package com.asiainfo.designpattern.architecture.filterchain;

import java.util.Collections;
import java.util.List;

/**
 * FilterChain 调用链模式
 * 
 * @author       zq
 * @date         2018年5月4日  下午10:35:05
 * Copyright: 	  北京亚信智慧数据科技有限公司
 */
public class FilterChain {

    private int index;
    private List<Filter> list;
    public FilterChain(List<Filter> filters) {
        index = 0;
        list = Collections.unmodifiableList(filters);
    }
    
    public void doFilter(Request request, Response response) throws Exception {
        
        if (list.isEmpty()) {
            return;
        }
        if (index >= list.size()) {
            return;
        }
        list.get(index++).doFilter(request, response, this);
    }
}
