package com.asiainfo.algorithm;

/**
 * 插入排序:
 *  通过构建有序序列，对于未排序数据，在已排序序列中从后向前扫描，找到相应位置并插入。插入排序在实现上，通常采用
 *  in-place排序（即只需乃至O(1)的额外空间的排序），因而在从后向前扫描过程中，需要反复把已排序元素逐步往后
 *  挪位，为最新元素提供插入空间。
 *  
 *  具体算法描述如下：
 *  <1>.从第一个元素开始，该元素可以认为已经被排序；
 *  <2>.取出下一个元素，在已经排序的元素序列中从后向前扫描；
 *  <3>.如果该元素（已排序）大于新元素，将该元素移到下一位置；
 *  <4>.重复步骤3，直到找到已排序的元素小于或者等于新元素的位置；
 *  <5>.将新元素插入到该位置后；
 *  <6>.重复步骤2~5。
 * 
 * @author       zq
 * @date         2017年12月28日  下午4:59:49
 * Copyright: 	  北京亚信智慧数据科技有限公司
 */
public class InsertionSort {
	
    /**
     * 简单插入排序
     * 
     * @param arr
     */
	public static <T extends Object & Comparable<T>> void insertionSort(T[] arr) {
	    
		for (int i = 1, len = arr.length; i < len; i++) {
			T key = arr[i];
			int j = i - 1;
			while (j >= 0 && arr[j].compareTo(key) > 0) {
				arr[j + 1] = arr[j];
				j--;
			}
			arr[j + 1] = key;
		}
	}
	
	/**
	 * 改进：查找插入位置时使用二分查找方式
	 * 
	 * @param arr
	 */
	public static <T extends Object & Comparable<T>> void insertionSort2(T[] arr) {
	    
		for (int i = 1, len = arr.length; i < len; i++) {
			T key = arr[i];
			int left = 0, right = i - 1;
			while (left <= right) {
				int mid = (left + right) >> 1;
				if (key.compareTo(arr[mid]) < 0) {
					right = mid - 1;
				} else {
					left = mid + 1;
				}
			}
			for (int j = i - 1; j >= left; j--) {
				arr[j + 1] = arr[j];
			}
			arr[left] = key;
		}
	}
	
	public static void main(String[] args) {
	    
        Integer[] arr = new Integer[] {3, 44, 38, 5, 47, 15, 36, 26, 27, 2, 46, 4, 19, 50, 48};
        //1.原始版本
        long start = System.nanoTime();
        insertionSort(arr);
        System.out.println(System.nanoTime() - start);
        Sorts.printArray(arr);
		
		//改进后算法
		arr = new Integer[] {3, 44, 38, 5, 47, 15, 36, 26, 27, 2, 46, 4, 19, 50, 48};
		start = System.nanoTime();
		insertionSort2(arr);
		System.out.println(System.nanoTime() - start);
		Sorts.printArray(arr);
	}
}
