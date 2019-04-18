package com.asiainfo.designpattern.architecture.filterchain;

/**
 * filter模式
 * 
 * @author       zq
 * @date         2018年5月4日  下午10:36:14
 * Copyright: 	  北京亚信智慧数据科技有限公司
 */
public interface Filter {

    public void init(FilterConfig config);
    public void doFilter(Request request, Response response, FilterChain chain) throws Exception;
    public void destroy();
}
