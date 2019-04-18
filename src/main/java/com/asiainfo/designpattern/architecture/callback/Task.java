package com.asiainfo.designpattern.architecture.callback;

/**
 * 由工作流决定Callback运行的时机
 * 
 * @author       zq
 * @date         2018年5月5日  下午6:51:14
 * Copyright: 	  北京亚信智慧数据科技有限公司
 */
public class Task {

    public void execute(Callback callback) {
        System.out.println("do something");
        callback.call();
        System.out.println("do something else");
    }
}
