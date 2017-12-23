package com.asiainfo.designpattern.behavior;

/**
 * 策略模式属于对象的行为模式。
 * 其用意是针对一组算法，将每一个算法封装到具有共同接口的独立的类中，从而使得它们可以相互替换。
 * 策略模式使得算法可以在不影响到客户端的情况下发生变化。
 * 
 * @author       zq
 * @date         2017年12月21日  下午5:36:51
 * Copyright: 	  北京亚信智慧数据科技有限公司
 */
public class Strategy {

    /** 
     * TODO
     * 
     * @param args
     */
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
    interface Sort {
        int[] sort(int[] arr);
    }
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
    static class QuickSort implements Sort {
        
        public int[] sort(int[] arr) {
            sort(arr, 0, arr.length - 1);
            return arr;
        }
        public void sort(int[] arr, int p, int r) {
            
            int q = 0;
            if (p < r) {
                q = partition(arr, p, r);
                sort(arr, p, q - 1);
                sort(arr, q + 1, r);
            }
        }
        public int partition(int[] a, int p, int r) {
            
            int x = a[r];
            int j = p - 1;
            for (int i = p; i <= r - 1; i++) {
                if (a[i] <= x) {
                    j++;
                    swap(a, j, i);
                }
            }
            swap(a, j + 1, r);
            return j + 1; 
        }
        public void swap(int[] a, int i, int j) {   
            int t = a[i];   
            a[i] = a[j];   
            a[j] = t;   
        }
    }
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
