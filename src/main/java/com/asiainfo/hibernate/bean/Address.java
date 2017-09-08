package com.asiainfo.hibernate.bean;

import java.io.Serializable;
import javax.persistence.Embeddable;

@Embeddable
public class Address implements Serializable {
	
	private static final long serialVersionUID = 1L;
	private String address;
	private Integer zipCode;
	private String phone;
	
	public Address() {}
	
	public Address(String address, Integer zipCode, String phone) {
		this.address = address;
		this.zipCode = zipCode;
		this.phone = phone;
	}
	
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public Integer getZipCode() {
		return zipCode;
	}
	public void setZipCode(Integer zipCode) {
		this.zipCode = zipCode;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
}
