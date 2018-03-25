package com.asiainfo.json.jackson;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @Description: TODO
 * 
 * @author       zq
 * @date         2017年9月12日  下午1:57:57
 * Copyright: 	  北京亚信智慧数据科技有限公司
 */
public class Account implements Serializable {

	/** serialVersionUID */
	private static final long serialVersionUID = 1L;

	private String accountId;
	private BigDecimal balance;
	private Date date;
	
	public String getAccountId() {
		return accountId;
	}
	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}
	public BigDecimal getBalance() {
		return balance;
	}
	public void setBalance(BigDecimal balance) {
		this.balance = balance;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	@Override
	public String toString() {
		return "Account [accountId=" + accountId + ", balance=" + balance + ", date=" + date + "]";
	}
}
