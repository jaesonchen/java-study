package com.asiainfo.algorithm;

/**
 * 桶排序:
 *  桶排序是计数排序的升级版, 适用于排序数比较小的排序（不能有负数）。
 *  工作原理：假设输入数据服从均匀分布，将数据分到有限数量的桶里，每个桶再分别排序。
 *  
 *  具体算法描述如下：
 *  <1>.设置一个定量的数组当作空桶；数组下标就是对应的值，数组大小大于最大的排序数
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
	    
		int[] result = new int[max(arr) + 1];
		for (int i : arr) {
		    result[i]++;
		}
		int i = 0;
		for (int j = 0; j < result.length; j++) {
		    while (result[j] > 0) {
		        arr[i++] = j;
		        result[j]--;
		    }
		}
	}
	
	// 取数组中最大的数
	private static int max(Integer[] arr) {
	    int max = 0;
	    for (int i : arr) {
	        if (i > max) {
	            max = i;
	        }
	    }
	    return max;
	}
	
	public static void main(String[] args) {
		Integer[] arr = new Integer[] {3, 44, 38, 5, 19, 47, 15, 36, 26, 27, 2, 46, 4, 19, 50, 48};
		BucketSort.bucketSort(arr);
		Sorts.printArray(arr);
	}
}
