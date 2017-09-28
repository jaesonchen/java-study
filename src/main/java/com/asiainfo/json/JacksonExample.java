package com.asiainfo.json;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @Description: TODO
 * 
 * @author       zq
 * @date         2017年9月12日  下午1:59:31
 * Copyright: 	  北京亚信智慧数据科技有限公司
 */
public class JacksonExample {

	/** 
	 * @Description: TODO
	 * 
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws Exception {
		
		List<Account> accounts = new ArrayList<>();
		Account account = new Account();
		account.setAccountId("10001");
		account.setBalance(BigDecimal.valueOf(10000.0));
		account.setDate(new Date());
		accounts.add(account);
		account = new Account();
		account.setAccountId("10002");
		account.setBalance(BigDecimal.valueOf(20000.0));
		account.setDate(new Date());
		accounts.add(account);
		
		User user = new User();
		user.setUserName("jaesonchen");
		user.setGender(Gender.MALE);
		user.setAge(20);
		user.setAccounts(accounts);
		
		ObjectMapper mapper = new ObjectMapper();
		String json = mapper.writeValueAsString(user);
		System.out.println("json=" + json);
		
		user = mapper.readValue(json, User.class);
		System.out.println("user=" + user);
		
		
		//read generic
		List<User> list = new ArrayList<>();
		list.add(user);
		String listJson = mapper.writeValueAsString(list);
		System.out.println("list json=" + listJson);
		System.out.println("list user=" + readGeneric(listJson, List.class, User.class));
		
		System.out.println("list user=" + readGeneric(listJson, list.getClass()));
	}
	
	public static <T> T readGeneric(String jsonStr, Class<?> collectionClass, Class<?>... elementClasses) throws Exception {
		
	       ObjectMapper mapper = new ObjectMapper();
	       JavaType javaType = mapper.getTypeFactory().constructParametricType(collectionClass, elementClasses);
	       return mapper.readValue(jsonStr, javaType);
	}
	public static <T> T readGeneric(String jsonStr, Class<T> clazz) throws Exception {
		
	       ObjectMapper mapper = new ObjectMapper();
	       return mapper.readValue(jsonStr, clazz);
	}
}
