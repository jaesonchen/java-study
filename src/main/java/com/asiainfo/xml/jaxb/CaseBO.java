package com.asiainfo.xml.jaxb;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * @Description: TODO
 * 
 * @author       zq
 * @date         2017年5月22日  下午5:36:48
 * Copyright: 	  北京亚信智慧数据科技有限公司
 */

@XmlType(propOrder={
		"caseAttrMap", "caseId", "caseObjective", "desc", "keyPoint", "keyword", 
		"kpi", "mind", "proId", "result", "title", "upDateTime"})
@XmlRootElement(name="xsd:caseBO")
public class CaseBO implements Serializable {

	private static final long serialVersionUID = 1L;
	
	/**
	 * 案例编号，统一编码：proId+系统生成的案例编号
	 */
	private String caseId;
	
	/**
	 * 上报时间 ，yyyymmddHHMMSS
	 */
	private String upDateTime;
	
	/**
	 * 省份
	 */
	private String proId;
	
	/**
	 * 标题
	 */
	private String title;
	
	/**
	 * 关键字
	 */
	private String keyword;
	
	/**
	 * 描述
	 */
	private String desc;
	
	/**
	 * 目的
	 */
	private String caseObjective;
	
	/**
	 * 分析思路
	 */
	private String mind;
	
	/**
	 * 亮点
	 */
	private String keyPoint;
	
	/**
	 * 效果
	 */
	private String kpi;
	
	/**
	 * 结论
	 */
	private String result;
	
	/**
	 * 扩展Map，key： attachmentName(附件名称)，省份上传时，将自己文件名前增加proId_支持10个附件，附件名称用英文;隔开
	 */
	private CaseAttrMap caseAttrMap;

	@XmlElement(name="xsd:caseId")
	public String getCaseId() {
		return caseId;
	}
	public void setCaseId(String caseId) {
		this.caseId = caseId;
	}

	@XmlElement(name="xsd:upDateTime")
	public String getUpDateTime() {
		return upDateTime;
	}
	public void setUpDateTime(String upDateTime) {
		this.upDateTime = upDateTime;
	}

	@XmlElement(name="xsd:proId")
	public String getProId() {
		return proId;
	}
	public void setProId(String proId) {
		this.proId = proId;
	}

	@XmlElement(name="xsd:title")
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}

	@XmlElement(name="xsd:keyword")
	public String getKeyword() {
		return keyword;
	}
	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

	@XmlElement(name="xsd:desc")
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}

	@XmlElement(name="xsd:caseObjective")
	public String getCaseObjective() {
		return caseObjective;
	}
	public void setCaseObjective(String caseObjective) {
		this.caseObjective = caseObjective;
	}

	@XmlElement(name="xsd:mind")
	public String getMind() {
		return mind;
	}
	public void setMind(String mind) {
		this.mind = mind;
	}

	@XmlElement(name="xsd:keyPoint")
	public String getKeyPoint() {
		return keyPoint;
	}
	public void setKeyPoint(String keyPoint) {
		this.keyPoint = keyPoint;
	}

	@XmlElement(name="xsd:kpi")
	public String getKpi() {
		return kpi;
	}
	public void setKpi(String kpi) {
		this.kpi = kpi;
	}

	@XmlElement(name="xsd:result")
	public String getResult() {
		return result;
	}
	public void setResult(String result) {
		this.result = result;
	}

	@XmlElement(name="xsd:caseAttrMap")
	public CaseAttrMap getCaseAttrMap() {
		return caseAttrMap;
	}
	public void setCaseAttrMap(CaseAttrMap caseAttrMap) {
		this.caseAttrMap = caseAttrMap;
	}
	
	@Override
	public String toString() {
		return "CaseBO [caseId=" + caseId + ", upDateTime=" + upDateTime + ", proId=" + proId + ", title=" + title
				+ ", keyword=" + keyword + ", desc=" + desc + ", caseObjective=" + caseObjective + ", mind=" + mind
				+ ", keyPoint=" + keyPoint + ", kpi=" + kpi + ", result=" + result + ", caseAttrMap=" + caseAttrMap
				+ "]";
	}
}
