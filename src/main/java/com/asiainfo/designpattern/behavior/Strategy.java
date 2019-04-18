package com.asiainfo.designpattern.behavior;

/**
 * 策略模式属于对象的行为模式。
 * 其用意是针对一组算法，将每一个算法封装到具有共同接口的独立的实现类中，从而使得它们可以相互替换。
 * 策略模式使得算法可以在不影响到客户端的情况下发生变化。
 * 
 * @author       zq
 * @date         2017年12月21日  下午5:36:51
 * Copyright: 	  北京亚信智慧数据科技有限公司
 */
public class Strategy {

    public static void main(String[] args) {

        SortService service = new SortService();
        int[] arr = {1, 4, 6, 2, 5, 3, 7, 8, 10, 9};
        service.setAlgorithm(new InsertionSort());
        print(service.sort(arr));
        
        arr = new int[] {9, 4, 6, 2, 8, 1, 3, 7, 10, 5};
        service.setAlgorithm(new QuickSort());
        print(service.sort(arr));
    }
    
    static void print(int[] arr) {
        for (int i : arr) {
            System.out.print(i);
            System.out.print(" ");
        }
        System.out.println();
    }
    
    static class SortService {
        
        private Sort algorithm;
        public void setAlgorithm(Sort algorithm) {
            this.algorithm = algorithm;
        }
        public int[] sort(int[] arr) {
            return this.algorithm.sort(arr);
        }
    }
    // 策略接口
    interface Sort {
        int[] sort(int[] arr);
    }
    // 插入排序实现，把一个数插入到前面已拍好的子序列中，插入前可能需要移动多个数
    static class InsertionSort implements Sort {
        
        public int[] sort(int[] arr) {
            
           int len = arr.length;
           for (int i = 1; i < len; i++) {
               int j;
               int temp = arr[i];
               for (j = i; j > 0; j--) {
                  if (arr[j-1] > temp) {
                      arr[j] = arr[j-1];
                  } else {
                      break;
                  }
               }
               arr[j] = temp;
            }
            return arr;
        }
    }
    // 选择排序实现，从后面的序列中选择最小的一个与当前的数交换，只交换一次
    static class SelectionSort implements Sort {
        
        public int[] sort(int[] arr) {
            
           int len = arr.length;
           int temp;
           for (int i = 0; i < len; i++) {
               temp = arr[i];
               int j;
               int min = i;
               for (j = i + 1; j < len; j++) {
                  if (arr[j] < temp) {
                      temp = arr[j];
                      min = j;
                  }
               }
               arr[min] = arr[i];
               arr[i] = temp;
           }
           return arr;
        }
    }
    // 快速排序实现，取一个数作为基准，把小的都交换到左边序列，大的都交换到右边序列，再分成2个子序列进行排队
    static class QuickSort implements Sort {
        
        public int[] sort(int[] arr) {
            sort(arr, 0, arr.length - 1);
            return arr;
        }
        public void sort(int[] arr, int start, int end) {
            
            int mid = 0;
            if (start < end) {
                mid = partition(arr, start, end);
                sort(arr, start, mid - 1);
                sort(arr, mid + 1, end);
            }
        }
        // 分区，把所有比最后一个数小的都交换到序列左边，再把最后一个数交换到中间的位置
        public int partition(int[] a, int start, int end) {
            
            int base = a[end];
            int j = start - 1;
            for (int i = start; i <= end - 1; i++) {
                if (a[i] <= base) {
                    j++;
                    swap(a, j, i);
                }
            }
            swap(a, j + 1, end);
            return j + 1; 
        }
        public void swap(int[] a, int i, int j) {
            if (i == j) {
                return;
            }
            int t = a[i];   
            a[i] = a[j];   
            a[j] = t;   
        }
    }
    // 冒泡排序实现，选择一个数与后面的所有数比较，如果比后面的数大，则交换；可能交换多次，最后后面序列最小的被交换到当前位置
    static class BubbleSort implements Sort {
        
        public int[] sort(int[] arr) {
            
           int len = arr.length;
           for (int i = 0; i < len; i++) {
               for (int j = i + 1; j < len; j++) {
                  int temp;
                  if (arr[i] > arr[j]) {
                      temp = arr[j];
                      arr[j] = arr[i];
                      arr[i] = temp;
                  }
               }
            }
            return arr;
        }
    }
}
