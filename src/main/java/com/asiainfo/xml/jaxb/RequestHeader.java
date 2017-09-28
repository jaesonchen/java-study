package com.asiainfo.xml.jaxb;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlType(propOrder={
		"accessChannel", "beId", "language", "operator", "password", "transactionId"})
@XmlRootElement(name="xsd:requestHeader")
public class RequestHeader implements Serializable {
	
	private static final long serialVersionUID = 1L;
	private String accessChannel;
    private String beId;
    private String language;
    private String operator;
    private String password;
    private String transactionId;

    @XmlElement(name="xsd:accessChannel")
    public String getAccessChannel() {
        return accessChannel;
    }
    public void setAccessChannel(String accessChannel) {
        this.accessChannel = accessChannel;
    }

    @XmlElement(name="xsd:beId")
    public String getBeId() {
        return beId;
    }
    public void setBeId(String beId) {
        this.beId = beId;
    }

    @XmlElement(name="xsd:language")
    public String getLanguage() {
        return language;
    }
    public void setLanguage(String language) {
        this.language = language;
    }

    @XmlElement(name="xsd:operator")
    public String getOperator() {
        return operator;
    }
    public void setOperator(String operator) {
        this.operator = operator;
    }

    @XmlElement(name="xsd:password")
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }

    @XmlElement(name="xsd:transactionId")
    public String getTransactionId() {
        return transactionId;
    }
    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }
	@Override
	public String toString() {
		return "RequestHeader [accessChannel=" + accessChannel + ", beId=" + beId + ", language=" + language
				+ ", operator=" + operator + ", password=" + password + ", transactionId=" + transactionId + "]";
	}
}
