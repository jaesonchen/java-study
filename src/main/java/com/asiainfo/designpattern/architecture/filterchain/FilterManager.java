package com.asiainfo.designpattern.architecture.filterchain;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Filter管理器，负责FilterChain的初始化和销毁，每个请求进来时调用doFilter启动filterChain
 * 
 * @author       zq
 * @date         2018年5月4日  下午10:38:43
 * Copyright: 	  北京亚信智慧数据科技有限公司
 */
public class FilterManager {
    
    private static List<Filter> filters = new ArrayList<>();
    
    // filter初始化
    public static void init(Collection<FilterConfig> configs) throws ReflectiveOperationException {
        
        for (FilterConfig config : configs) {
            Class<?> clazz = Class.forName(config.getFilterClass());
            Filter filter = (Filter) clazz.newInstance();
            filter.init(config);
            filters.add(filter);
        }
    }
    // filter销毁
    public static void destroy() {
        
        for (Filter filter : filters) {
            filter.destroy();
        }
        filters.clear();
    }
    // 启动filter chain
    public static void doFilter(Request request, Response response) throws Exception {
        
        FilterChain chain = new FilterChain(filters);
        chain.doFilter(request, response);
    }
}
