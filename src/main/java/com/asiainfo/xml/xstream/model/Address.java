package com.asiainfo.xml.xstream.model;

import java.io.Serializable;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("address")
public class Address implements Serializable {

	private static final long serialVersionUID = -2302664521469447477L;

	@XStreamAlias("telphone")
	private String phone;
	@XStreamAlias("floor")
	private String addr;
	@XStreamAlias("code")
	private String zipcode;
	
	public Address() {}
	
	public Address(String phone, String addr, String zipcode) {

		this.phone = phone;
		this.addr = addr;
		this.zipcode = zipcode;
	}
	
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getAddr() {
		return addr;
	}
	public void setAddr(String addr) {
		this.addr = addr;
	}
	public String getZipcode() {
		return zipcode;
	}
	public void setZipcode(String zipcode) {
		this.zipcode = zipcode;
	}
	
	@Override
	public String toString() {
		
		StringBuilder sb = new StringBuilder();
		sb.append("(addr=").append(this.addr).append(", phone=").append(this.phone)
			.append(", zipcode=").append(this.zipcode).append(")");
		
		return sb.toString();
	}
}
