package com.asiainfo.algorithm;

/**
 * 计数排序： 计数排序是一个非基于比较的排序算法，它的优势在于在对一定范围内的整数排序时，它的复杂度为Ο(n+k)（其中k是整数的范围），快于任何比较排序算法。
 * 这是一种牺牲空间换取时间的做法，而且当O(k)>O(n*log(n))的时候其效率反而不如基于比较的排序（基于比较的排序的时间复杂度在理论上的下限是O(n*log(n))。
 * 计数排序是一种非常快捷的稳定性强的排序方法，时间复杂度O(n+k),其中n为要排序的数的个数，k为要排序的数的组大值。
 * 计数排序对一定量的整数排序时候的速度非常快，一般快于其他排序算法。但计数排序局限性比较大，只限于对整数进行排序。
 * 
 * 具体算法描述如下：
 *  <1>. 找出待排序的数组中最大和最小的元素； 
 *  <2>. 统计数组中每个值为i的元素出现的次数，存入数组C的第i项；
 *  <3>. 对所有的计数累加（从C中的第一个元素开始，每一项和前一项相加）； 
 *  <4>. 反向填充目标数组：将每个元素i放在新数组的第C(i)项，每放一个元素就将C(i)减去1。
 * 
 * @author       zq
 * @date         2017年12月29日  下午3:58:24
 * Copyright: 	  北京亚信智慧数据科技有限公司
 */
public class CountingSort {

	public static void countingSort(Integer[] arr) {
	    
		long beginTime = System.nanoTime();
		int len = arr.length, min = arr[0], max = min;
		// 1.找出最大/最小元素
		for (int i = 0; i < len; i++) {
			min = (min <= arr[i] ? min : arr[i]);
			max = (max >= arr[i] ? max : arr[i]);
		}
		// 2.构建新数组，统计数组中每个值为i的元素出现的次数，存入数组C的第i项
		Integer[] C = new Integer[max - min + 1];
		for (int i = 0; i < len; i++) {
			int index = arr[i] - min;
			C[index] = C[index] != null ? C[index] + 1 : 1;
		}
		// 3.反向填充
		for (int k = (max - min); k >= 0; k--) {
			while (C[k] != null && C[k] != 0) {
				arr[--len] = k + min;
				C[k]--;
			}
		}
		System.out.println("排序算法耗时: " + (System.nanoTime() - beginTime) + " ns");
	}

	public static void main(String[] args) {
		Integer[] arr = new Integer[] {2, 2, 3, 8, 7, 1, 2, 2, 2, 7, 3, 9, 8, 2, 1, 4, 2, 4, 6, 9, 2};
		CountingSort.countingSort(arr);
		Sorts.printArray(arr);
	}
}
