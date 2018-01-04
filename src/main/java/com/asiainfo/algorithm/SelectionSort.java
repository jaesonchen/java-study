package com.asiainfo.algorithm;

/**
 * 
 * 选择排序:
 *  选择排序(Selection-sort)是一种简单直观的排序算法。它的工作原理：首先在未排序序列中找到最小元素的坐标，如果不是第一个则与第一个元素交换位置；
 *  以此类推，直到所有元素均排序完毕。
 * 
 * 
 * @author       zq
 * @date         2017年12月29日  下午3:30:14
 * Copyright: 	  北京亚信智慧数据科技有限公司
 */
public class SelectionSort {
    
    /**
     * 选择排序
     * 
     * @param arr
     */
	public static <T extends Object & Comparable<T>> void selectionSort(T[] arr) {
	    
		int len = arr.length;
		int minIndex;
		for (int i= 0; i < len - 1; i++) {
			minIndex = i;
			for (int j = i + 1; j < len; j++) {
				if (arr[j].compareTo(arr[minIndex]) < 0) { //寻找最小数
					minIndex = j; //保存最小数索引
				}
			}
			//swap
			T temp = arr[i];
			arr[i] = arr[minIndex];
			arr[minIndex] = temp;
		}
	}
	
	public static void main(String[] args) {
	    
	    Integer[] arr = new Integer[] {3, 44, 38, 5, 47, 15, 36, 26, 27, 2, 46, 4, 19, 50, 48};
        //1.原始版本
        long start = System.nanoTime();
        selectionSort(arr);
        System.out.println(System.nanoTime() - start);
        Sorts.printArray(arr);
	}
}
