package com.asiainfo.designpattern.architecture.filterchain;

import java.util.Arrays;

/**   
 * @Description: 模拟web 容器启动
 * 
 * @author chenzq  
 * @date 2019年3月25日 下午3:59:52
 * @version V1.0
 * @Copyright: Copyright(c) 2019 jaesonchen.com Inc. All rights reserved. 
 */
public class FilterChainTest {

    
    public static void main(String[] args) throws Exception {
        
        FilterConfig authConfig = new FilterConfig("com.asiainfo.designpattern.architecture.filterchain.AuthFilter");
        authConfig.setParam("name", "authFilter");
        FilterConfig logConfig = new FilterConfig("com.asiainfo.designpattern.architecture.filterchain.LogFilter");
        logConfig.setParam("name", "logFilter");
        
        // init
        FilterManager.init(Arrays.asList(new FilterConfig[] {authConfig, logConfig}));
        
        // request 1
        Request request = new Request();
        request.setParameter("userid", "jaeson");
        // doFilter
        FilterManager.doFilter(request, new Response());
        
        // request 2
        request = new Request();
        request.setParameter("userid", "chenzq");
        // doFilter
        FilterManager.doFilter(request, new Response());
        
        // destroy
        FilterManager.destroy();
    }
}
