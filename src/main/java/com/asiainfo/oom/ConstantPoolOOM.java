package com.asiainfo.oom;

import java.util.ArrayList;
import java.util.List;

/**
 * @Description: 常量池内存溢出，jdk7以后Perm区移到堆中，很难再测试PermOutOfMemory
 * @VM args : -XX:PermSize=10M -XX:MaxPermSize=10M 
 * 
 * @author       zq
 * @date         2017年9月5日  上午11:00:00
 * Copyright: 	  北京亚信智慧数据科技有限公司
 */
public class ConstantPoolOOM {

	public static void main(String[] args) {

		List<String> strings = new ArrayList<String>();
        int i = 0;
        while(true){
           strings.add(String.valueOf(i++).intern());
        }
	}
}
