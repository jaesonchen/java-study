package com.asiainfo.json.gson;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * 设置排除策略：
 * GsonBuilder#setExclusionStrategies(ExclusionStrategy...)
 * GsonBuilder#addDeserializationExclusionStrategy(ExclusionStrategy)
 * GsonBuilder#addSerializationExclusionStrategy(ExclusionStrategy)
 * 
 * 设置属性名称序列化转换：@SerializedName注解拥有最高优先级，在加有@SerializedName注解的字段上FieldNamingStrategy不生效！
 * GsonBuilder.setFieldNamingPolicy()
 * 
 * @author       zq
 * @date         2018年3月25日  下午4:19:31
 * Copyright: 	  北京亚信智慧数据科技有限公司
 */
public class ExclusionStrategyExample {

    /** 
     * TODO
     * 
     * @param args
     */
    public static void main(String[] args) {

        Gson gson = new GsonBuilder()
                .addSerializationExclusionStrategy(new ExclusionStrategy() {
                    @Override
                    public boolean shouldSkipField(FieldAttributes f) {
                        // 这里作判断，决定要不要排除该字段, return true为排除
                        if ("age".equals(f.getName())) {
                            return true;
                        }
                        return false;
                    }
                    @Override
                    public boolean shouldSkipClass(Class<?> clazz) {
                        // 直接排除某个类 ，return true为排除
                        return (clazz == int.class || clazz == Integer.class);
                    }
                })
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)    //属性名称转换策略
                .create();
        gson.toJson(new User("chenzq", 24, "chenzq@163.com"), System.out);
    }
}
