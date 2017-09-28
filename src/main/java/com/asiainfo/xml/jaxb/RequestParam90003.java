package com.asiainfo.xml.jaxb;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * @Description: TODO
 * 
 * @author       zq
 * @date         2017年5月27日  下午4:12:01
 * Copyright: 	  北京亚信智慧数据科技有限公司
 */
@XmlType(propOrder={
		"activityId", "approveStatus", "channelSourceType", "description", "requestHeader"})
@XmlRootElement(name="iop:args0")
public class RequestParam90003 implements Serializable {

	private static final long serialVersionUID = 1L;
	private String activityId;
	private String approveStatus;
	private String channelSourceType;
	private String description;
	private RequestHeader requestHeader;
	
	@XmlElement(name="xsd:activityId")
	public String getActivityId() {
		return activityId;
	}
	public void setActivityId(String activityId) {
		this.activityId = activityId;
	}
	
	@XmlElement(name="xsd:approveStatus")
	public String getApproveStatus() {
		return approveStatus;
	}
	public void setApproveStatus(String approveStatus) {
		this.approveStatus = approveStatus;
	}
	
	@XmlElement(name="xsd:channelSourceType")
	public String getChannelSourceType() {
		return channelSourceType;
	}
	public void setChannelSourceType(String channelSourceType) {
		this.channelSourceType = channelSourceType;
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
		return "RequestParam [activityId=" + activityId + ", approveStatus=" + approveStatus + ", channelSourceType="
				+ channelSourceType + ", description=" + description + ", requestHeader=" + requestHeader + "]";
	}
}
