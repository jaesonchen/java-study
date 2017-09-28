package com.asiainfo.xml.jaxb;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * @Description: TODO
 * 
 * @author       zq
 * @date         2017年5月27日  下午4:50:23
 * Copyright: 	  北京亚信智慧数据科技有限公司
 */
@XmlType(propOrder={
		"caseId", "approveStatus", "description", "requestHeader"})
@XmlRootElement(name="str:args0")
public class RequestParam99007 implements Serializable {

	private static final long serialVersionUID = 1L;
	private String caseId;
	private String approveStatus;
	private String description;
	private RequestHeader requestHeader;
	
	@XmlElement(name="xsd:caseId")
	public String getCaseId() {
		return caseId;
	}
	public void setCaseId(String caseId) {
		this.caseId = caseId;
	}
	
	@XmlElement(name="xsd:approveStatus")
	public String getApproveStatus() {
		return approveStatus;
	}
	public void setApproveStatus(String approveStatus) {
		this.approveStatus = approveStatus;
	}
	
	@XmlElement(name="xsd:description")
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
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
		return "RequestParam99007 [caseId=" + caseId + ", approveStatus=" + approveStatus + ", description="
				+ description + ", requestHeader=" + requestHeader + "]";
	}
}
