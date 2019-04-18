package com.asiainfo.algorithm;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

import com.asiainfo.datastructure.ArrayList;
import com.asiainfo.datastructure.HashSet;
import com.asiainfo.datastructure.List;
import com.asiainfo.datastructure.Set;

/**
 * 基于内容的推荐引擎：它将计算得到并推荐给用户一些与该用户已选择过的项目相似的内容。
 * 例如，当你在网上购书时，你总是购买与历史相关的书籍，那么基于内容的推荐引擎就会给你推荐一些热门的历史方面的书籍。
 * 
 * 基于协同过滤的推荐引擎：它将推荐给用户一些与该用户品味相似的其他用户喜欢的内容。
 * 例如，当你在网上买衣服时，基于协同过滤的推荐引擎会根据你的历史购买记录或是浏览记录，分析出你的穿衣品位，
 * 并找到与你品味相似的一些用户，将他们浏览和购买的衣服推荐给你。
 * 
 * 协同过滤推荐算法slopeone (Item-based)
 * 协同过滤推荐（Collaborative Filtering recommendation）是在信息过滤和信息系统中正迅速成为一项很受欢迎的技术。
 * 与传统的基于内容过滤直接分析内容进行推荐不同，协同过滤分析用户兴趣，在用户群中找到指定用户的相似（兴趣）用户，
 * 综合这些相似用户对某一信息的评价，形成系统对该指定用户对此信息的喜好程度预测。
 * 
 * R(C) = (n * (R(A) - R(AC)) + m * (R(B) - R(BC))) / (m + n)
 * R(A)   当前用户对A的评分。
 * R(AC)  其他用户对A、C的评分差值平均值。
 * m/n    算评分差值平均值时的评分用户数。
 * 
 * Step1:计算物品之间的评分差的均值，记为物品间的评分偏差。
 * Step2:根据物品间的评分偏差和用户的历史评分，预测用户对未评分的物品的评分。
 * Step3:将预测评分排序，取topN对应的物品推荐给用户。
 * 
 * @author       zq
 * @date         2018年1月3日  下午5:02:33
 * Copyright: 	  北京亚信智慧数据科技有限公司
 */
public class SlopeOne {

    public static void main(String[] args) throws Exception {
        Map<String, Set<Rating>> ratings = generateRating();
        System.out.println(recommend("user1", 10006, ratings));
        System.out.println(recommend("user999", 10001, ratings));
    }
    
    /**
     * 读取评分数据
     * 
     * @return
     * @throws Exception 
     */
    protected static Map<String, Set<Rating>> generateRating() throws Exception {
        
        BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream("src/main/java/com/asiainfo/algorithm/slopeone.data")));
        Map<String, Set<Rating>> map = new HashMap<>();
        String line = null;
        while ((line = reader.readLine()) != null) {
            String[] fields = line.split("\\s+");
            Rating rating = new Rating(fields[0], Long.parseLong(fields[1]), Double.parseDouble(fields[2]));
            if (null == map.get(fields[0])) {
                map.put(fields[0], new HashSet<>());
            }
            Set<Rating> set = map.get(fields[0]);
            set.add(rating);
        }
        reader.close();
        return map;
    }

    /**
     * slopeone推荐评分
     * 
     * @param userId
     * @param item
     * @param data
     * @return
     */
    public static double recommend(String userId, long item, Map<String, Set<Rating>> data) {

        // 所有用户、所有item的评分差值（同一个用户对不同item的评分差值）
        Map<Long, Set<Diff>> diffMap = allDiff(data);
        // 用户的所有评分
        Set<Rating> userRatings = data.get(userId);
        // 用户可以参与评分的所有item
        Set<Long> allItem = allItem(userRatings);
        List<AverageDiff> averageList = new ArrayList<>();
        for (Long origin : allItem) {
            if (origin.longValue() != item) {
                // 其他用户对origin与item评分的平均差值
                AverageDiff averageDiff = averageDiff(origin, item, diffMap.get(origin));
                if (null != averageDiff) {
                    averageList.add(averageDiff);
                }
            }
        }
        double total = 0.0;
        long count = 0L;
        for (AverageDiff average : averageList) {
            System.out.println(average);
            // 用户对origin的评分
            Rating userRating = userItemRating(userId, average.origin, userRatings);
            if (null != userRating) {
                count += average.count;
                // (用户对origin的评分 - 其他用户origin与item评分差值平均值) * 其他用户评价数
                total += (userRating.rating - average.average) * average.count;
            }
        }
        return 0 == count ? 0 : total / count;
    }
    
    /**
     * 平均差值
     * 
     * @param origin
     * @param target
     * @param set
     * @return
     */
    protected static AverageDiff averageDiff(long origin, long target, Set<Diff> set) {
        
        double total = 0.0;
        long count = 0L;
        for (Diff diff : set) {
            if (target == diff.target) {
                count++;
                total += diff.diff;
            }
        }
        return 0 == count ? null : new AverageDiff(origin, target, total / count, count);
    }
    
    /**
     * 所有评分差值，
     * 
     * @param data
     * @return
     */
    protected static Map<Long, Set<Diff>> allDiff(Map<String, Set<Rating>> data) {
        
        Map<Long, Set<Diff>> map = new HashMap<>();
        for (Map.Entry<String, Set<Rating>> entry : data.entrySet()) {
            Set<Diff> set = calculate(entry.getValue());
            for (Diff diff : set) {
                if (map.get(diff.origin) == null) {
                    map.put(diff.origin, new HashSet<>());
                }
                map.get(diff.origin).add(diff);
            }
        }
        return map;
    }
    
    /**
     * 用户的评分差值
     * 
     * @param set
     * @return
     */
    protected static Set<Diff> calculate(Set<Rating> set) {
        
        Set<Diff> result = new HashSet<>();
        for (Rating rating : set) {
            result.addAll(diff(rating, set));
        }
        return result;
    }
    
    /**
     * 计算用户的评分差值，一个item评分 与 其他item的评分差值
     * 
     * @param target
     * @param set
     * @return
     */
    protected static Set<Diff> diff(Rating target, Set<Rating> set) {
        
        Set<Diff> result = new HashSet<>();
        for (Rating origin : set) {
            if (origin.item != target.item) {
                result.add(new Diff(target.userId, origin.item, target.item, origin.rating - target.rating));
            }
        }
        return result;
    }
    
    /**
     * 所有用户
     * 
     * @param list
     * @return
     */
    protected static Set<String> allUser(List<Rating> list) {
        
        Set<String> result = new HashSet<>();
        for (Rating rating : list) {
            result.add(rating.userId);
        }
        return result;
    }
    
    /**
     * 用户可以参考的所有item
     * 
     * @param userRatings
     * @return
     */
    protected static Set<Long> allItem(Set<Rating> userRatings) {
        
        Set<Long> result = new HashSet<>();
        for (Rating rating : userRatings) {
            result.add(rating.item);
        }
        return result;
    }
    
    /**
     * item的所有评分
     * 
     * @param item
     * @param list
     * @return
     */
    protected static Set<Rating> itemRating(long item, List<Rating> list) {
        
        Set<Rating> set = new HashSet<>();
        for (Rating rating : list) {
            if (item == rating.item) {
                set.add(rating);
            }
        }
        return set;
    }
    
    /**
     * 用户的所有评分
     * 
     * @param userId
     * @param list
     * @return
     */
    protected static Set<Rating> userRating(String userId, List<Rating> list) {
        
        Set<Rating> set = new HashSet<>();
        for (Rating rating : list) {
            if (null != userId && userId.equals(rating.userId)) {
                set.add(rating);
            }
        }
        return set;
    }
    
    /**
     * 用户的item评分
     * 
     * @param userId
     * @param item
     * @param set
     * @return
     */
    protected static Rating userItemRating(String userId, long item, Set<Rating> set) {
        
        for (Rating rating : set) {
            if (item == rating.item && userId.equals(rating.userId)) {
                return rating;
            }
        }
        return null;
    }
    
    static class AverageDiff {
        long origin;
        long target;
        double average;
        long count;
        public AverageDiff(long origin, long target, double average, long count) {
            this.origin = origin;
            this.target = target;
            this.average = average;
            this.count = count;
        }
        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + (int) (origin ^ (origin >>> 32));
            result = prime * result + (int) (target ^ (target >>> 32));
            return result;
        }
        @Override
        public boolean equals(Object obj) {

            if (this == obj) {
                return true;
            }
            if (!(obj instanceof AverageDiff)) {
                return false;
            }
            AverageDiff other = (AverageDiff) obj;
            return origin == other.origin && target == other.target;
        }
        @Override
        public String toString() {
            return "[origin=" + origin + ", target=" + target + ", average=" + average + ", count=" + count + "]";
        }
    }
    static class Diff {
        String userId;
        long origin;
        long target;
        double diff;
        public Diff(String userId, long origin, long target, double diff) {
            this.userId = userId;
            this.origin = origin;
            this.target = target;
            this.diff = diff;
        }
        @Override
        public int hashCode() {
            
            final int prime = 31;
            int result = 1;
            result = prime * result + (int) (origin ^ (origin >>> 32));
            result = prime * result + (int) (target ^ (target >>> 32));
            result = prime * result + ((userId == null) ? 0 : userId.hashCode());
            return result;
        }
        @Override
        public boolean equals(Object obj) {

            if (this == obj) {
                return true;
            }
            if (!(obj instanceof Diff)) {
                return false;
            }
            Diff other = (Diff) obj;
            return origin == other.origin && target == other.target && userId.equals(other.userId);
        }
    }
    
    static class Rating {
        String userId;
        long item;
        double rating;
        public Rating(String userId, long item) {
            this.userId = userId;
            this.item = item;
        }
        public Rating(String userId, long item, double rating) {
            this.userId = userId;
            this.item = item;
            this.rating = rating;
        }
        @Override
        public int hashCode() {
            
            final int prime = 31;
            int result = 1;
            result = prime * result + (int) (item ^ (item >>> 32));
            result = prime * result + ((userId == null) ? 0 : userId.hashCode());
            return result;
        }
        @Override
        public boolean equals(Object obj) {
            
            if (this == obj) {
                return true;
            }
            if (!(obj instanceof Rating)) {
                return false;
            }
            Rating other = (Rating) obj;
            return item == other.item && userId.equals(other.userId);
        }
        @Override
        public String toString() {
            return "[" + userId + ", " + item + ", " + rating + "]";
        }
    }
}
