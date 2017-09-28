package com.asiainfo.xml.xstream.model;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import com.asiainfo.xml.xstream.convert.SingleValueCalendarConverter;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamConverter;
import com.thoughtworks.xstream.annotations.XStreamImplicit;
import com.thoughtworks.xstream.annotations.XStreamOmitField;
import com.thoughtworks.xstream.converters.basic.BooleanConverter;

@XStreamAlias("employ")
public class Person implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@XStreamAlias("username")
	private String name;
	@XStreamOmitField
	private int age;
	@XStreamAlias("address")
	private Address address;
	@XStreamImplicit(itemFieldName="part")
	private List<String> content;
	@XStreamConverter(SingleValueCalendarConverter.class)
	private Calendar birthday = new GregorianCalendar();
	@XStreamConverter(value=BooleanConverter.class, booleans={false}, strings={"yes", "no"})
	private boolean important;
	
	public Person() {}

	public Person(String name, int age, Address address,  String... content) {

		this.name = name;
		this.age = age;
		this.address = address;
		this.content = Arrays.asList(content);
	}

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getAge() {
		return age;
	}
	public void setAge(int age) {
		this.age = age;
	}
	public Address getAddress() {
		return address;
	}
	public void setAddress(Address address) {
		this.address = address;
	}
	
	public List<String> getContent() {
		return content;
	}

	public void setContent(List<String> content) {
		this.content = content;
	}

	@Override
	public String toString() {
		
		StringBuilder sb = new StringBuilder();
		sb.append("name=").append(this.name).append(", age=").append(this.age)
			.append(", address=").append(this.address);
		
		return sb.toString();
	}
}
