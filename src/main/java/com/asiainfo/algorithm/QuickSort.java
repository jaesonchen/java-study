package com.asiainfo.algorithm;

/**
 * 快速排序：
 *  通过一趟排序将待排记录分隔成独立的两部分，其中一部分记录的关键字均比另一部分的关键字小，则可分别对这两部分记录继续进行排序，以达到整个序列有序。
 * 
 * 具体算法描述如下：
 * <1>.从数列中挑出一个元素，称为 “基准”（pivot）；
 * <2>.重新排序数列，所有元素比基准值小的摆放在基准前面，所有元素比基准值大的摆在基准的后面（相同的数可以到任一边）。在这个分区
 *      退出之后，该基准就处于数列的中间位置。这个称为分区（partition）操作；
 * <3>.递归地（recursive）把小于基准值元素的子数列和大于基准值元素的子数列排序。
 * 
 * @author       zq
 * @date         2017年12月28日  下午5:11:00
 * Copyright: 	  北京亚信智慧数据科技有限公司
 */
public class QuickSort {
    
    /**
     * 从start、end两个方向搜索不满足条件的值并交换，基准值为start
     */
    public static <T extends Comparable<T>> void quickSort(T[] unsorted, int start, int end) {
        
        //基准值
        T pivot = unsorted[start];
        int i = start, j = end;
        while (i < j) {
            //从后往前搜索比基准值小的
            while (i < j && unsorted[j].compareTo(pivot) >= 0) {
                j--;
            }
            if (i < j) {
                //比基准值小的交换到前半队列
                unsorted[i] = unsorted[j];
                i++;
            }
            //从前搜索比基准值大的，此处=可以减少移动的次数
            while (i < j && unsorted[i].compareTo(pivot) <= 0) {
                i++;
            }
            if (i < j) {
                //比基准值大的交换到后半队列
                unsorted[j] = unsorted[i];
                j--;
            }
        }
        //此时i==j
        unsorted[i] = pivot;
        //基准值前面的队列排序
        if (start < (i - 1)) {
            quickSort(unsorted, start, i - 1);
        }
        //基准值后面的队列排序
        if (end > (i + 1)) {
            quickSort(unsorted, i + 1, end);
        }
    }
	
	public static void main(String[] args) {
	    
	    Integer[] arr = new Integer[] {44, 3, 38, 5, 47, 15, 36, 26, 27, 2, 46, 4, 19, 50, 48};
	    long start = System.nanoTime();
	    quickSort(arr, 0, arr.length - 1);
		Sorts.printArray(arr);
		System.out.println(System.nanoTime() - start);
	}
}
