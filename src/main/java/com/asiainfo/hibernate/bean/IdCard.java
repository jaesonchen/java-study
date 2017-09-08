package com.asiainfo.hibernate.bean;

import java.sql.Timestamp;

import javax.persistence.OneToOne;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Id;
import javax.persistence.GeneratedValue;
import javax.persistence.Column;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "idcard", catalog = "db4myeclipse")
public class IdCard implements java.io.Serializable {

	// Fields
	private static final long serialVersionUID = 1L;
	private String id;
	private String cardNo;
	private Timestamp authDate;
	private User user;

	// Constructors

	/** default constructor */
	public IdCard() {
	}

	/** full constructor */
	public IdCard(String cardNo, Timestamp authDate, User user) {
		this.cardNo = cardNo;
		this.authDate = authDate;
		this.user = user;
	}

	@GenericGenerator(name = "idGenerator", strategy = "uuid")
	@Id
	@GeneratedValue(generator = "idGenerator")
	@Column(name = "id", unique = true, nullable = false)
	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@Column(name = "cardNo", nullable = false, length = 32)
	public String getCardNo() {
		return this.cardNo;
	}

	public void setCardNo(String cardNo) {
		this.cardNo = cardNo;
	}

	@Column(name = "authDate", nullable = false)
	public Timestamp getAuthDate() {
		return this.authDate;
	}

	public void setAuthDate(Timestamp authDate) {
		this.authDate = authDate;
	}
	
	@OneToOne(mappedBy = "idCard")
	public User getUser() {
		return this.user;
	}

	public void setUser(User user) {
		this.user = user;
	}
}