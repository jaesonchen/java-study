package com.asiainfo.algorithm;

/**
 * 
 * n 阶汉诺塔问题可以描述为：假设有X、Y、Z 三个塔座，初始时有n 个大小不一的
 * 盘子按照从小到大的次序放在塔座X 上。现在要求将塔座X 上的所有盘子移动到塔座Z 上
 * 并保持原来的顺序，在移动过程中要满足以下要求：在塔座之间一次只能移动一个盘子并且
 * 任何时候大盘子都不能放到小盘子上。
 * 
 * @author       zq
 * @date         2017年12月29日  上午9:07:40
 * Copyright: 	  北京亚信智慧数据科技有限公司
 */
public class Hanio {
	
	public static void hanio (int n, char x, char y, char z) {
	    
		if (n == 1) {
			move(x, n, z);
		} else {
			hanio (n - 1, x, z, y);
			move(x, n, z);
			hanio(n - 1, y, x, z);
		}
	}
	
	private static void move(char x, int n, char y) {
		System.out.println ("Move " + n + " from " + x + " to " + y);
	}
	
	public static void main(String[] args) {
		hanio(10, 'A', 'B', 'C');
	}
}
