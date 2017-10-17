package com.asiainfo.jediscluster;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;

import com.asiainfo.util.ThreadPoolUtils;

import redis.clients.jedis.JedisCluster;

/**
 * @Description: TODO
 * 
 * @author       zq
 * @date         2017年9月21日  下午3:22:50
 * Copyright: 	  北京亚信智慧数据科技有限公司
 */
public class RecommendExample {

	static Serializer<Object> serializer = new JdkSerializer();
	
	/** 
	 * @Description: TODO
	 * 
	 * @param args
	 * @throws InterruptedException 
	 */
	public static void main(String[] args) throws InterruptedException {
		
		final ExecutorService service = ThreadPoolUtils.getInstance().fixedThreadPool(10);
		final JedisCluster jedisCluster = JedisClusterFactory.getJedisCluster();
		
		RecommendExample test = new RecommendExample();
		List<String> lastRecommendList = test.getLastRecommend(jedisCluster, "13522587602");
		System.out.println("lastRecommendList=" + lastRecommendList);
		List<String> recommendList = new ArrayList<>(Arrays.asList(new String[] {
				"600000940495", "600000379484", "610008883838", "600000379293", "610008883838", "610008883838", "600000940495"
		}));
		System.out.println("recommendList=" + recommendList);
		List<String> result = test.filter(recommendList, lastRecommendList, 1);
		System.out.println("result=" + result);
		
		lastRecommendList = test.offerLastRecommend(result, lastRecommendList, 100);
		System.out.println("lastRecommendList=" + lastRecommendList);
		service.execute(new AsyncUpdateTask(jedisCluster, lastRecommendList, "13522587602"));
		Thread.sleep(5000);
		service.shutdown();
		
/*		List<String> recommendedList = new ArrayList<>(Arrays.asList(new String[] {"1", "6", "2", "4", "3", "5"}));
		List<String> recommendList = new ArrayList<>(Arrays.asList(new String[] {"6", "5", "4", "3", "2", "1"}));
		for (int i = 0; i < 10; i++) {
			List<String> result = test.filter(recommendList, recommendedList, 1);
			System.out.println("result=" + result);
			recommendedList = test.offerLastRecommend(result, recommendedList, 100);
			System.out.println("recommendedList=" + recommendedList);
		}*/
	}
	
	/**
	 * 
	 * @Description: 获取本次循环推荐列表
	 * 
	 * @param list
	 * @param recommendedList
	 * @param num
	 * @return
	 */
	protected List<String> filter(List<String> list, List<String> recommendedList, final int num) {
		
		//待推荐列表为空
		if (null == list || list.isEmpty()) {
			return new ArrayList<>();
		}
		
		//待推荐列表少于要推荐的数量
		if (list.size() <= num) {
			return new ArrayList<>(list);
		}
		
		int recommendNum = list.size() < num ? list.size() : num;
		//已推荐列表为空
		if (null == recommendedList || recommendedList.isEmpty()) {
			return new ArrayList<>(list.subList(0, recommendNum));
		}
		
		List<String> result = new ArrayList<>();
		List<String> copyList = new ArrayList<>(list);
		List<String> copyRecommendedList = new ArrayList<>(recommendedList);
		
		//判断是否有新加入活动
		copyList.removeAll(copyRecommendedList);
		if (copyList.size() > 0) {
			for (String recommend : copyList) {
				result.add(recommend);
				copyRecommendedList.add(0, recommend);
				if (--recommendNum == 0) {
					break;
				}
			}
		}
		
		//本次推荐的活动（需要考虑上一个推荐的刚好是新活动，此时刚好完成一轮，下一轮需要从高优先级开始）
		copyList = new ArrayList<>(list);
		//删除所有已完成、停止的已推荐活动
		copyRecommendedList.retainAll(copyList);
		for (int i = 0; i < recommendNum; i++) {
			String nextRecommand = getNextRecommend(copyList, copyRecommendedList);
			result.add(nextRecommand);
			copyRecommendedList.add(0, nextRecommand);
		}
		return result;
	}
	
	/**
	 * 
	 * @Description: 追加本次推荐产品到已推荐列表的头部
	 * 
	 * @param recommendList
	 * @param lastRecommendList
	 * @param limit
	 * @return
	 */
	protected List<String> offerLastRecommend(final List<String> recommendList, final List<String> lastRecommendList, final int limit) {
		
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
	 * 
	 * @Description: 返回下一个待推荐活动，需要判断是否刚推荐过
	 * 
	 * @param list
	 * @param recommendedList
	 * @return
	 */
	protected String getNextRecommend(List<String> list, List<String> recommendedList) {
		
		String nextRecommand = nextRecommend(list, recommendedList);
		int index = list.indexOf(nextRecommand);
		for (int i = 0; index < list.size() && i < recommendedList.size(); i++, index++) {
			if (!list.get(index).equals(recommendedList.get(i))) {
				return list.get(index);
			}
		}
		return list.get(0);
	}
	
	/**
	 * 
	 * @Description: 返回下一个待推荐活动(需要考虑刚好完成一轮的情况)
	 * 
	 * @param list
	 * @param recommendedList
	 * @return
	 */
	protected String nextRecommend(List<String> list, List<String> recommendedList) {
		
		//已推荐列表为空，直接返回第一个
		if (recommendedList.size() == 0) {
			return list.get(0);
		}
		
		//判断上次推荐活动是否是个只推荐过一次的活动
		if (isNewRecommend(recommendedList, recommendedList.get(0))) {
			//返回上上次推荐的产品
			return nextRecommend(list, new ArrayList<>(recommendedList.subList(1, recommendedList.size())));
		}
		
		//上次推荐活动不止推荐过一次，说明当前已推荐活动在第2 - n 轮推荐序列中，从待推荐列表中找到该活动index，继续往后推荐
		int index = list.indexOf(recommendedList.get(0));
		//已是最后一个待推荐活动，直接返回第一个
		if (index == (list.size() - 1)) {
			return list.get(0);
		}
		
		//取下一个推荐序列
		while ((index + 1) < list.size() 
				&& isNewRecommend(recommendedList, list.get(index + 1))) {
			
			if (!isFullRound(list, recommendedList, index)) {
				index++;
				continue;
			}
			return list.get(index + 1);
		}
		if (index == (list.size() - 1)) {
			return list.get(0);
		}
		return list.get(index + 1);
	}
	
	/**
	 * 
	 * @Description: 判断当前推荐活动是否只推荐过一次
	 * 
	 * @param recommendedList
	 * @param recommend
	 * @return
	 */
	protected boolean isNewRecommend(List<String> recommendedList, String recommend) {
		
		if (recommendedList.size() == 1) {
			return true;
		}
		List<String> copyRecommandedList = new ArrayList<>(recommendedList);
		copyRecommandedList.remove(recommend);
		return !copyRecommandedList.contains(recommend);
	}	
	
	/**
	 * 
	 * @Description: 当前轮次是否被插入新活动
	 * 
	 * @param list
	 * @param recommandedList
	 * @param index
	 * @return
	 */
	protected boolean isFullRound(List<String> list, List<String> recommandedList, int index) {
		
		for (int i = index, j = 0; i >= 0 && j < recommandedList.size(); i--, j++) {
			if (!list.get(i).equals(recommandedList.get(j))) {
				return false;
			}
		}
		return true;
	}
	
	
	/**
	 * @Description: 获取redis中已推荐列表
	 * 
	 * @param phone
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<String> getLastRecommend(final JedisCluster jedisCluster, final String phone) {
		return (List<String>) serializer.deserialize(jedisCluster.get(("recommend:" + phone).getBytes()));
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
