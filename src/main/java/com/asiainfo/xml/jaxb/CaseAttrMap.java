package com.asiainfo.xml.jaxb;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlElement;

/**
 * @Description: TODO
 * 
 * @author       zq
 * @date         2017年5月27日  下午3:34:43
 * Copyright: 	  北京亚信智慧数据科技有限公司
 */
public class CaseAttrMap implements Serializable {

	private static final long serialVersionUID = 1L;
	private String key;
	private String value;
	
	public CaseAttrMap() {}
	public CaseAttrMap(String key, String value) {
		this.key = key;
		this.value = value;
	}
	
	@XmlElement(name="xsd:key")
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	
	@XmlElement(name="xsd:value")
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	@Override
	public String toString() {
		return "CaseAttrMap [key=" + key + ", value=" + value + "]";
	}
}
