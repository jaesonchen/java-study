package com.asiainfo.designpattern.architecture.callback;

/**
 * TODO
 * 
 * @author       zq
 * @date         2018年5月5日  下午6:53:31
 * Copyright: 	  北京亚信智慧数据科技有限公司
 */
public class CallbackTest {
    
    public static void main(String[] args) {
        Task task = new Task();
        task.execute(() -> System.out.println("callback is invoked!"));
    }
}
