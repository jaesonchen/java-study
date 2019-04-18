package com.asiainfo.designpattern.architecture.retry;

import java.io.Serializable;

/**
 * TODO
 * 
 * @author       zq
 * @date         2018年4月10日  上午10:53:35
 * Copyright: 	  北京亚信智慧数据科技有限公司
 */
public class User implements Serializable {

    /** serialVersionUID */
    private static final long serialVersionUID = 1L;

    private String userId;
    private int age;
    
    public User() {
    }
    public User(String userId, int age) {
        this.userId = userId;
        this.age = age;
    }
    public String getUserId() {
        return userId;
    }
    public void setUserId(String userId) {
        this.userId = userId;
    }
    public int getAge() {
        return age;
    }
    public void setAge(int age) {
        this.age = age;
    }
    @Override
    public String toString() {
        return "User [userId=" + userId + ", age=" + age + "]";
    }
}
