package com.asiainfo.designpattern.architecture.filterchain;

import java.io.Writer;

/**
 * TODO
 * 
 * @author       zq
 * @date         2018年5月5日  下午4:10:15
 * Copyright: 	  北京亚信智慧数据科技有限公司
 */
public class LogFilter implements Filter {

    /* 
     * TODO
     * @param config
     * @see com.asiainfo.designpattern.architecture.filter.Filter#init(com.asiainfo.designpattern.architecture.filter.FilterConfig)
     */
    @Override
    public void init(FilterConfig config) {
        System.out.println("LogFilter init:" + config);
    }

    /* 
     * TODO
     * @param request
     * @param response
     * @param chain
     * @throws Exception
     * @see com.asiainfo.designpattern.architecture.filter.Filter#doFilter(com.asiainfo.designpattern.architecture.filter.Request, com.asiainfo.designpattern.architecture.filter.Response, com.asiainfo.designpattern.architecture.filter.FilterChain)
     */
    @Override
    public void doFilter(Request request, Response response, FilterChain chain) throws Exception {
        
        System.out.println("LogFilter doFilter!");
        Writer out = response.getWriter();
        out.write("hello world!\n");
        out.flush();
        chain.doFilter(request, response);
    }

    /* 
     * TODO
     * @see com.asiainfo.designpattern.architecture.filter.Filter#destroy()
     */
    @Override
    public void destroy() {
        System.out.println("LogFilter destroy!");
    }
}
