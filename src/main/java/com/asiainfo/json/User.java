package com.asiainfo.json;

import java.io.Serializable;
import java.util.List;

/**
 * @Description: TODO
 * 
 * @author       zq
 * @date         2017年9月12日  下午1:44:10
 * Copyright: 	  北京亚信智慧数据科技有限公司
 */
public class User implements Serializable {

	/** serialVersionUID */
	private static final long serialVersionUID = 1L;

	private String userName;
	private int age;
	private Gender gender;
	private List<Account> accounts;
	
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public int getAge() {
		return age;
	}
	public void setAge(int age) {
		this.age = age;
	}
	public Gender getGender() {
		return gender;
	}
	public void setGender(Gender gender) {
		this.gender = gender;
	}
	public List<Account> getAccounts() {
		return accounts;
	}
	public void setAccounts(List<Account> accounts) {
		this.accounts = accounts;
	}
	@Override
	public String toString() {
		return "User [userName=" + userName + ", age=" + age + ", gender=" + gender + ", accounts=" + accounts + "]";
	}
}
