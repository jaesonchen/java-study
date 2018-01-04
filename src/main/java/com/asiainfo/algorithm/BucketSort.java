package com.asiainfo.algorithm;

/**
 * 桶排序:
 *  桶排序是计数排序的升级版。
 *  工作原理：假设输入数据服从均匀分布，将数据分到有限数量的桶里，每个桶再分别排序。
 *  
 *  具体算法描述如下：
 *  <1>.设置一个定量的数组当作空桶；
 *  <2>.遍历输入数据，并且把数据一个一个放到对应的桶里去；
 *  <3>.对每个不是空的桶进行排序；
 *  <4>.从不是空的桶里把排好序的数据拼接起来。
 * 
 * @author       zq
 * @date         2017年12月28日  下午5:48:49
 * Copyright: 	  北京亚信智慧数据科技有限公司
 */
public class BucketSort {
    
	public static void bucketSort(Integer[] arr) {
	    
		long beginTime = System.nanoTime();
		int[] result = new int[51];
		for (int i : arr) {
		    result[i] = i;
		}
		int i = 0;
		for (int num : result) {
		    if (num > 0) {
		        arr[i++] = num;
		    }
		}
		System.out.println("排序算法耗时: " + (System.nanoTime() - beginTime) + " ns");
	}
	
	//
	public static void main(String[] args) {
		Integer[] arr = new Integer[]{3,44,38,5,47,15,36,26,27,2,46,4,19,50,48};
		BucketSort.bucketSort(arr);
		printArray(arr);
	}
	
	private static <T> void printArray(T[] t) {
		StringBuilder sb = new StringBuilder("[");
		for(int i = 0, len = t.length; i < len; i++) {
			if(i == 0)
				sb.append(t[i]);
			else
				sb.append("," + t[i]);
		}
		sb.append("]");
		System.out.println(sb.toString());
	}
}
