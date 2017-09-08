package com.asiainfo.insidejvm.oom;

import java.util.ArrayList;
import java.util.List;

/**
 * @Description: VM Args:-XX:PermSize=10M -XX:MaxPermSize=10M
 * 
 * @author       zq
 * @date         2017年9月5日  上午11:00:00
 * Copyright: 	  北京亚信智慧数据科技有限公司
 */
public class RuntimeConstantPoolOOM {

	/** 
	 * @Description: TODO
	 * 
	 * @param args
	 */
	public static void main(String[] args) {

		List<String> strings = new ArrayList<String>();
        int i = 0;
        while(true){
        	
           strings.add(String.valueOf(i++).intern());
        }
	}

}
