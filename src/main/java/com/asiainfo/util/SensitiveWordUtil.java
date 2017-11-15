package com.asiainfo.util;

import java.util.*;

/**
 * 过滤敏感词语
 * 
 * @author       zq
 * @date         2017年11月15日  下午5:12:21
 * Copyright: 	  北京亚信智慧数据科技有限公司
 */
public class SensitiveWordUtil {

	private static Map<String, Node> sensitiveKeyMap;
	
	//初始化敏感词语列表
	public static void initSensitiveWord(Set<String> keySet) {
		
		sensitiveKeyMap = new HashMap<String, Node>(keySet.size());
		
		Map<String, Node> current;
		Iterator<String> it = keySet.iterator();
		while (it.hasNext()) {
			current = sensitiveKeyMap;
			//敏感词语
			String keyWord = it.next();
			for (int i = 0; i < keyWord.length(); i++) {
				Node node = current.get(String.valueOf(keyWord.charAt(i)));
				if (node == null) {
					
					node = new Node();
					node.setMap(new HashMap<String, Node>());
					current.put(String.valueOf(keyWord.charAt(i)), node);
				}
				
				current = node.getMap();
				if (i == (keyWord.length() - 1)) {
					node.setEndOfWord(true);
					break;
				}
			}
		}
	}
	
	//判断是否含有敏感词语
	public static boolean hasSensitiveWord(String str) {
		
		Map<String, Node> current;
		Node node;
		for (int i = 0; i < str.length(); i++) {
			
			node = sensitiveKeyMap.get(String.valueOf(str.charAt(i)));
			if (node == null) {
				continue;
			}
			
			if (node.isEndOfWord()) {
				return true;
			}
			
			current = node.getMap();
			int j = i;
			while (current != null && ++j < str.length()) {
				
				node = current.get(String.valueOf(str.charAt(j)));
				if (node == null) {
					break;
				}
				
				if (node.isEndOfWord()) {
					return true;
				}
				current = node.getMap();
			}
		}
		return false;
	}
	
	//返回敏感词语列表
	public static List<String> getSensitiveWord(String str) {
		
		List<String> result = new ArrayList<String>();
		
		Map<String, Node> current;
		StringBuilder keyWord;
		Node node;
		for (int i = 0; i < str.length(); i++) {
			keyWord = new StringBuilder();
			node = sensitiveKeyMap.get(String.valueOf(str.charAt(i)));
			if (node == null) {
				continue;
			}
			
			keyWord.append(str.charAt(i));
			if (node.isEndOfWord()) {
				result.add(keyWord.toString());
			}
			
			current = node.getMap();
			int j = i;
			while (current != null && ++j < str.length()) {
				
				node = current.get(String.valueOf(str.charAt(j)));
				if (node == null) {
					break;
				}
				
				keyWord.append(str.charAt(j));
				if (node.isEndOfWord()) {
					result.add(keyWord.toString());
				}
				current = node.getMap();
			}
		}
		
		return result;
	}
	
	private static String list2String(List<String> list) {
		
		StringBuilder sb = new StringBuilder();
		for (String str : list) {
			sb.append(str).append(" ");
		}
		return sb.toString();
	}
	
	private static class Node {
		
		boolean endOfWord;
		Map<String, Node> map;
		
		public Map<String, Node> getMap() {
			return this.map;
		}
		public boolean isEndOfWord() {
			return this.endOfWord;
		}
		
		public void setMap(Map<String, Node> map) {
			this.map = map;
		}
		public void setEndOfWord(boolean endOfWord) {
			this.endOfWord = endOfWord;
		}
	}
	
	@SuppressWarnings("serial")
	public static void main(String[] args) {

		Set<String> keyWord = new HashSet<String>() {
			{
				add("陈志权");
				add("jaeson");
				add("jaeso");
				add("chen");
				add("google");
				add("baidu");
			}
		};
		
		initSensitiveWord(keyWord);
		String test1 = "啊发射点法陈志权啊发fda阿德";
		String test2 = "啊发射点法jason啊发fda阿德";
		String test3 = "chen啊发fda阿德";
		String test4 = "发射点法啊发fdbaidua阿德";
		String test5 = "发射点法啊发googlefda阿德";
		
		long start = System.currentTimeMillis();
		
		System.out.println(hasSensitiveWord(test1));
		System.out.println(hasSensitiveWord(test2));
		System.out.println(hasSensitiveWord(test3));
		System.out.println(hasSensitiveWord(test4));
		System.out.println(hasSensitiveWord(test5));
		
		String test = "啊发射jaeson点法陈陈志权啊发jaesjaesojaesonfda阿德googlebaid啊发射jaeson点法陈陈志权啊发jaesjaesojaesonfda"
				+ "阿德googlebaid啊发射jaeson点法陈陈志权啊发jaesjaesojaesonfda阿德googlebaid啊发射jaeson点法陈陈志权啊发jaes"
				+ "jaesojaesonfda阿德googlebaid啊发射jaeson点法陈陈志权啊发jaesjaesojaesonfda阿德googlebaid啊发射jaeson点法"
				+ "陈陈志权啊发jaesjaesojaesonfda阿德googlebaid啊发射jaeson点法陈陈志权啊发jaesjaesojaesonfda阿德googlebaid啊"
				+ "发射jaeson点法陈陈志权啊发jaesjaesojaesonfda阿德googlebaid啊发射jaeson点法陈陈志权啊发jaesjaesojaesonfda阿德"
				+ "googlebaid啊发射jaeson点法陈陈志权啊发jaesjaesojaesonfda阿德googlebaid啊发射jaeson点法陈陈志权啊发jaesjaesojaesonfda"
				+ "阿德googlebaid啊发射jaeson点法陈陈志权啊发jaesjaesojaesonfda阿德googlebaid啊发射jaeson点法陈陈志权啊发jaesja"
				+ "esojaesonfda阿德googlebaid啊发射jaeson点法陈陈志权啊发jaesjaesojaesonfda阿德googlebaid啊发射jaeson点法陈陈志权啊发j"
				+ "aesjaesojaesonfda阿德googlebaid";
		System.out.println(list2String(getSensitiveWord(test)));
		
		long end = System.currentTimeMillis();
		System.out.println(end - start);
	}
	
	
}
