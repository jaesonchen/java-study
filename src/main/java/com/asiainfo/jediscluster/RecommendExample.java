package com.asiainfo.jediscluster;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import redis.clients.jedis.JedisCluster;

/**
 * @Description: TODO
 * 
 * @author       zq
 * @date         2017年8月28日  上午10:47:13
 * Copyright: 	  北京亚信智慧数据科技有限公司
 */
public class RecommendExample {

	static final Serializer<Object> serializer = new JdkSerializer();
	static final int lastRecommendLimit = 5;
	static final int recommendNum = 1;
	/** 
	 * @Description: TODO
	 * 
	 * @param args
	 * @throws InterruptedException 
	 */
	public static void main(String[] args) throws InterruptedException {
		
		final ExecutorService service = Executors.newFixedThreadPool(10);
		final JedisCluster jedisCluster = JedisClusterFactory.getJedisCluster();
		
		List<String> lastRecommendList = getLastRecommend(jedisCluster, "13522587602");
		/*List<String> lastRecommendList = new ArrayList<>(Arrays.asList(new String[] {
				"600000379484", "600000940495", "600000940495", "610008883838", "610008883838", "600000379293", 
				"610008883838", "600000379484", "600000940495", "600000940495", "610008883838", "610008883838", 
				"600000379293", "610008883838", "600000379484", "600000940495", "600000940495", "610008883838"}));*/
		System.out.println("lastRecommendList=" + lastRecommendList);
		List<String> recommendList = new ArrayList<>(Arrays.asList(new String[] {
				"600000940495", "600000379484", "610008883838", "600000379293", "610008883838", "610008883838", "600000940495"
		}));
		System.out.println("recommendList=" + recommendList);

		//List<String> result = filter(recommendList, lastRecommendList, recommendNum);
		FilterResult result = filter(recommendList, lastRecommendList, recommendNum);
		
		System.out.println("result=" + result.getResult());
		System.out.println("index=" + result.getIndex());
		List<String> lastResult = offerLastRecommend(result.getResult(), lastRecommendList, lastRecommendLimit);
		System.out.println("lastResult=" + lastResult);
		service.execute(new AsyncUpdateTask(jedisCluster, lastResult, "13522587602"));
		Thread.sleep(5000);
		service.shutdown();
	}
	

	
	/**
	 * @Description: 循环推荐过滤
	 * 
	 * @param recommendList			待推荐产品列表
	 * @param lastRecommendList		已推荐产品列表
	 * @param recommendNum			本次推荐数量
	 * @return
	 */
	public static FilterResult filter(final List<String> recommendList, final List<String> lastRecommendList, final int recommendNum) {
		
		if (null == recommendList || recommendList.isEmpty()) {
			return new FilterResult();
		}
		
		List<String> recommendListCopy = new ArrayList<>(recommendList);
		final List<Integer> indexList = new ArrayList<>();
		for (int i = 0; i < recommendListCopy.size(); i++) {
			indexList.add(i);
		}
		if (null == lastRecommendList || lastRecommendList.isEmpty()) {
			int length = recommendListCopy.size() > recommendNum ? recommendNum : recommendListCopy.size();
			return new FilterResult(new ArrayList<>(recommendListCopy.subList(0, length))
					, new ArrayList<>(indexList.subList(0, length)));
		}
		if (recommendListCopy.size() <= recommendNum) {
			return new FilterResult(recommendListCopy, indexList);
		}

		for (String lastRecommend : lastRecommendList) {
			int index = recommendListCopy.indexOf(lastRecommend);
			if (index != -1) {
				indexList.remove(index);
				recommendListCopy.remove(lastRecommend);
				if (recommendListCopy.size() <= recommendNum) {
					return new FilterResult(recommendListCopy, indexList);
				}
			}
		}
		return new FilterResult(new ArrayList<>(recommendListCopy.subList(0, recommendNum)), indexList);
	}
	
	/**
	 * @Description: 追加本次推荐产品到已推荐列表的头部
	 * 
	 * @param recommendList			本次推荐产品列表
	 * @param lastRecommendList		已推荐产品列表
	 * @param limit					已推荐产品保存长度限制
	 * @return
	 */
	public static List<String> offerLastRecommend(final List<String> recommendList, final List<String> lastRecommendList, final int limit) {
		
		if (null == recommendList || recommendList.isEmpty()) {
			return lastRecommendList;
		}
		List<String> recommendListCopy = new ArrayList<>(recommendList);
		Collections.reverse(recommendListCopy);
		if (null == lastRecommendList || lastRecommendList.isEmpty()) {
			return recommendListCopy;
		}
		List<String> lastRecommendListCopy = new ArrayList<>(lastRecommendList);
		lastRecommendListCopy.addAll(0, recommendListCopy);
		return new ArrayList<>(lastRecommendListCopy.subList(0, lastRecommendListCopy.size() > limit ? limit : lastRecommendListCopy.size()));
	}
	
	/**
	 * @Description: 获取redis中已推荐列表
	 * 
	 * @param phone
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static List<String> getLastRecommend(final JedisCluster jedisCluster, final String phone) {
		return (List<String>) serializer.deserialize(jedisCluster.get(("recommend:" + phone).getBytes()));
	}

	/**
	 * @Description: 过滤结果
	 * 
	 * @author       zq
	 * @date         2017年8月31日  下午1:15:30
	 * Copyright: 	  北京亚信智慧数据科技有限公司
	 */
	static class FilterResult {
		
		List<String> result;
		List<Integer> index;
		
		FilterResult() {
			this.result = new ArrayList<>();
			this.index = new ArrayList<>();
		}
		FilterResult(List<String> result, List<Integer> index) {
			this.result = result;
			this.index = index;
		}
		public List<String> getResult() {
			return result;
		}
		public void setResult(List<String> result) {
			this.result = result;
		}
		public List<Integer> getIndex() {
			return index;
		}
		public void setIndex(List<Integer> index) {
			this.index = index;
		}
	}
	
	/**
	 * @Description: 异步更新已推荐列表
	 * 
	 * @author       zq
	 * @date         2017年8月28日  下午1:10:04
	 * Copyright: 	  北京亚信智慧数据科技有限公司
	 */
	static class AsyncUpdateTask implements Runnable {

		JedisCluster jedisCluster;
		List<String> lastRecommendList;
		String phone;
		
		AsyncUpdateTask(JedisCluster jedisCluster, List<String> lastRecommendList, String phone) {
			this.jedisCluster = jedisCluster;
			this.lastRecommendList = lastRecommendList;
			this.phone = phone;
		}
		
		/* 
		 * @Description: TODO
		 * @see java.lang.Runnable#run()
		 */
		@Override
		public void run() {
			
			try {
				this.jedisCluster.set(("recommend:" + phone).getBytes(), serializer.serialize(lastRecommendList));
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
	}
}
