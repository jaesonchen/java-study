package com.asiainfo.algorithm;

/**
 * 打印字符串中的字符的排列组合
 */
public class PermutationAndCombination {

	public static void main(String[] args) {
		
		String str = "ABC";
		permutationAndCombination(str);
	}
	
	public static void permutationAndCombination(String str) {
		printCombination("", str);
	}
	
	private static void printCombination(String start, String str) {
		
		if (str.length() == 0) {
			System.out.println(start);
			return;
		}
		
		for (int i = 0, len = str.length(); i < len; i++) {
			
			String s = String.valueOf(str.charAt(i));
			StringBuilder builder = new StringBuilder(str);
			builder.deleteCharAt(i);
			printCombination(start + s, builder.toString()); 
		}
	}
}
