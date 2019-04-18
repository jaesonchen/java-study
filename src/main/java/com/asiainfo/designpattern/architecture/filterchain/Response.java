package com.asiainfo.designpattern.architecture.filterchain;

import java.io.OutputStreamWriter;
import java.io.Writer;

/**
 * TODO
 * 
 * @author       zq
 * @date         2018年5月4日  下午10:37:21
 * Copyright: 	  北京亚信智慧数据科技有限公司
 */
public class Response {

    public Writer getWriter() {
        return new OutputStreamWriter(System.out);
    }
}
