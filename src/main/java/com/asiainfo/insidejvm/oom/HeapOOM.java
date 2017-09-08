package com.asiainfo.insidejvm.oom;

import java.util.ArrayList;
import java.util.List;

/**
 * @Described：堆溢出测试OutOfMemoryError
 * @VM args:-Xms10M -Xmx10M -XX:+HeapDumpOnOutOfMemoryError
 * @VM args:-verbose:gc -Xms10M -Xmx10M -XX:+PrintGCDetails
 * -Xms初始内存 -Xmx最大可用内存 通常设置相同值，避免每次垃圾回收完成后JVM重新分配内存。
 */
public class HeapOOM {

	public static void main(String[] args) {
		
		List<Object> list = new ArrayList<Object>();

		while(true){
			list.add(new Object());
		}
	}
}
