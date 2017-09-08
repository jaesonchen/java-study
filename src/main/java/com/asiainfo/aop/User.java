package com.asiainfo.aop;

import java.io.Serializable;

/**
 * @Description: TODO
 * 
 * @author       zq
 * @date         2017年9月2日  上午10:33:26
 * Copyright: 	  北京亚信智慧数据科技有限公司
 */
public class User implements Serializable {

	/** serialVersionUID */
	private static final long serialVersionUID = 1L;
	
	private String userId;
	private String userName;
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	@Override
	public String toString() {
		return "User [userId=" + userId + ", userName=" + userName + "]";
	}
}
