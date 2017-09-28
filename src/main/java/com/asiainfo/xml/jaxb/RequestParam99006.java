package com.asiainfo.xml.jaxb;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * @Description: TODO
 * 
 * @author       zq
 * @date         2017年5月27日  下午4:39:14
 * Copyright: 	  北京亚信智慧数据科技有限公司
 */
@XmlType(propOrder={
		"caseBO", "requestHeader"})
@XmlRootElement(name="str:args0")
public class RequestParam99006 implements Serializable {

	private static final long serialVersionUID = 1L;
	private CaseBO caseBO;
	private RequestHeader requestHeader;
	
	@XmlElement(name="xsd:caseBO")
	public CaseBO getCaseBO() {
		return caseBO;
	}
	public void setCaseBO(CaseBO caseBO) {
		this.caseBO = caseBO;
	}
	
	@XmlElement(name="xsd:requestHeader")
	public RequestHeader getRequestHeader() {
		return requestHeader;
	}
	public void setRequestHeader(RequestHeader requestHeader) {
		this.requestHeader = requestHeader;
	}
	@Override
	public String toString() {
		return "RequestParam99006 [caseBO=" + caseBO + ", requestHeader=" + requestHeader + "]";
	}
}
