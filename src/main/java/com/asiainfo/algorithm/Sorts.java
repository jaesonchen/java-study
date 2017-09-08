package com.asiainfo.algorithm;

public class Sorts {
	private Strategy strategy;
	
	/* 直接插入排序是一种最简单的插入排序方法，它的基本思想是：仅有一个元素的序列总
	 * 是有序的，因此，对n 个记录的序列，可从第二个元素开始直到第n 个元素，逐个向有序序
	 * 列中执行插入操作，从而得到n 个元素按关键字有序的序列。
	 */
	public void insertSort(Object[] r, int low, int high){
		for (int i = low + 1; i <= high; i++)
			if (strategy.compare(r[i], r[i-1]) < 0) { //小于时，需将r[i]插入有序表
				Object temp = r[i];

				r[i] = r[i - 1];
				int j = i - 2;
				for ( ; j >= low && strategy.compare(temp, r[j]) < 0; j--)
					r[j+1] = r[j]; //记录后移
				r[j+1] = temp; //插入到正确位置
			}
	}
	
	//折半插入排序
	public void binInsertSort(Object[] r, int low, int high) {
		for (int i = low + 1; i <= high; i++) {
			Object temp = r[i]; //保存待插入元素
			int hi = i-1; int lo = low; //设置初始区间
			while (lo <= hi) { //折半确定插入位置
				int mid = (lo + hi) / 2;
				if(strategy.compare(temp, r[mid]) < 0)
					hi = mid - 1;
				else 
					lo = mid + 1;
			}
			for (int j = i - 1; j > hi; j--) r[j+1] = r[j]; //移动元素
				r[hi + 1] = temp; //插入元素
		}//for
	}

	/*
	 * 起泡排序的思想非常简单。首先，将n 个元素中的第一个和第二个进行比较，如果两个
	 * 元素的位置为逆序，则交换两个元素的位置；进而比较第二个和第三个元素关键字，如此类
	 * 推，直到比较第n-1 个元素和第n 个元素为止。
	 */
	public void bubbleSort(Object[] r, int low, int high) {
		int n = high - low + 1;
		for (int i = 1; i < n; i++)
			for (int j = low; j <= high - i; j++)
				if (strategy.compare(r[j], r[j + 1]) > 0) {
					Object temp = r[j];
					r[j] = r[j + 1];
					r[j + 1] = temp;
				}
	}//end of bubbleSort

	/*
	 * 快速排序的基本思想是：通过一个枢轴（pivot）元素将n 个元素的序列分为左、右两个子序列Ll 和Lr，
	 * 其中子序列Ll中的元素均比枢轴元素小，而子序列Lr 中的元素均比枢轴元素大，然后对左、右子序列分
	 * 别进行快速排序，在将左、右子序列排好序后，则整个序列有序，而对左右子序列的排序过
	 * 程直到子序列中只包含一个元素时结束，此时左、右子序列由于只包含一个元素则自然有序。
	 */
	private int partition(Object[] r, int low, int high){
		Object pivot = r[low]; //使用r[low]作为枢轴元素
		while (low < high) { //从两端交替向内扫描
			while(low < high && strategy.compare(r[high], pivot) >= 0) 
				high--;
			r[low] = r[high]; //将比pivot 小的元素移向低端
			while(low < high && strategy.compare(r[low], pivot) <=0 ) 
				low++;
			r[high] = r[low]; //将比pivot 大的元素移向高端
		}
		r[low] = pivot; //设置枢轴
		return low; //返回枢轴元素位置
	}
	public void quickSort(Object[] r, int low, int high) {
		if (low < high) {
			int pa = partition(r, low, high);
			quickSort(r, low, pa - 1);
			quickSort(r, pa + 1, high);
		}
	}
	/*
	 * 简单选择排序的基本思想非常简单，即：第一趟，从n 个元素中找出关键字最小的元素
	 * 与第一个元素交换；第二趟，在从第二个元素开始的n-1 个元素中再选出关键字最小的元素
	 * 与第二个元素交换；如此，第k 趟，则从第k 个元素开始的n-k+1 个元素中选出关键字最小
	 * 的元素与第k 个元素交换，直到整个序列按关键字有序。
	 */
	public void selectSort(Object[] r, int low, int high) {
		for (int k = low; k < high - 1; k++){ //作n-1 趟选取
			int min = k;
			for (int i = min + 1; i <= high; i++) //选择关键字最小的元素
				if (strategy.compare(r[i], r[min]) < 0) 
					min = i;
			if (k != min) {
				Object temp = r[k]; //关键字最小的元素与元素r[k]交换
				r[k] = r[min];
				r[min] = temp;
			}//end of if
		}//end of for(int k=0…
	}//end of selectSort
}
