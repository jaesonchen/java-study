package com.asiainfo.algorithm;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;

import com.asiainfo.datastructure.ArrayList;
import com.asiainfo.datastructure.HashSet;
import com.asiainfo.datastructure.List;
import com.asiainfo.datastructure.Set;

/**
 * 协同推荐slopeone算法
 * rb = (n * (ra - R(A->B)) + m * (rc + R(B->C)))/(m+n)
 * 
 * @author       zq
 * @date         2018年1月3日  下午5:02:33
 * Copyright: 	  北京亚信智慧数据科技有限公司
 */
public class SlopeOne {

    public static void main(String[] args) throws Exception {
        
        List<Rating> ratings = generateRating();
        
        System.out.println(recommend("user1", 10006, ratings));
        System.out.println(recommend("user999", 10001, ratings));
    }
    
    /**
     * 评分数据
     * 
     * @return
     * @throws Exception 
     */
    protected static List<Rating> generateRating() throws Exception {
        
        BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream("src/main/java/com/asiainfo/algorithm/slopeone.data")));
        List<Rating> list = new ArrayList<>();
        String line = null;
        while ((line = reader.readLine()) != null) {
            String[] fields = line.split("\\s+");
            Rating rating = new Rating(fields[0], Long.parseLong(fields[1]), Double.parseDouble(fields[2]));
            if (!list.contains(rating)) {
                list.add(rating);
            }
        }
        reader.close();
        return list;
    }

    /**
     * slopeone推荐评分
     * 
     * @param userId
     * @param item
     * @param data
     * @return
     */
    public static double recommend(String userId, long item, List<Rating> data) {
        
        Set<Rating> userRatings = userRating(userId, data);
        Set<Diff> allDiff = allDiff(data);
        Set<Long> allItem = allItem(userRatings);
        List<AverageDiff> averageList = new ArrayList<>();
        for (Long origin : allItem) {
            if (origin.longValue() != item) {
                AverageDiff averageDiff = averageDiff(origin, item, allDiff);
                if (null != averageDiff) {
                    averageList.add(averageDiff);
                }
            }
        }
        double total = 0.0;
        long count = 0L;
        for (AverageDiff average : averageList) {
            System.out.println(average);
            Rating userRating = userItemRating(userId, average.origin, userRatings);
            if (null != userRating) {
                count += average.count;
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
            if (origin == diff.origin && target == diff.target) {
                count++;
                total += diff.diff;
            }
        }
        return 0 == count ? null : new AverageDiff(origin, target, total / count, count);
    }
    
    /**
     * 所有评分差值
     * 
     * @param list
     * @return
     */
    protected static Set<Diff> allDiff(List<Rating> list) {
        
        Set<Diff> result = new HashSet<>();
        Set<String> allUser = allUser(list);
        for (String userId : allUser) {
            Set<Rating> userRatings = userRating(userId, list);
            result.addAll(calculate(userRatings));
        }
        return result;
    }
    
    /**
     * 计算用户的评分差值
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
     * 计算单用户的评分差值
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
     * user可以参考的所有item
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
            return origin == other.origin && target == other.target 
                    && (userId == null && other.userId == null || userId != null && userId.equals(other.userId));
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
            return item == other.item && (userId == null && other.userId == null || 
                    userId != null && userId.equals(other.userId));
        }
        @Override
        public String toString() {
            return "[" + userId + ", " + item + ", " + rating + "]";
        }
    }
}