package com.asiainfo.designpattern.architecture.callback;

/**
 * 回调函数只是一个功能片段，由用户按照回调函数调用约定来实现的一个函数。回调函数是一个工作流的一部分，由工作流来决定函数的调用（回调）时机。
 * 回调函数包含下面几个特性： 
 * 1、属于工作流的一个部分；
 * 2、必须按照工作流指定的调用约定来申明（定义）；
 * 3、他的调用时机由工作流决定，回调函数的实现者不能直接调用回调函数来实现工作流的功能；
 * 
 * @author       zq
 * @date         2018年5月5日  下午6:49:32
 * Copyright: 	  北京亚信智慧数据科技有限公司
 */
public interface Callback {

    void call();
}
