package com.asiainfo.xml.jaxb;

/**
 * @Description: TODO
 * 
 * @author       zq
 * @date         2017年5月27日  下午2:04:26
 * Copyright: 	  北京亚信智慧数据科技有限公司
 */

public class IopService {

	/** 
	 * @Description: TODO
	 * 
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {

		/*System.out.println(JaxbUtil.fromXml("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>"
				+ "<xsd:user><xsd:userid>1001</xsd:userid><xsd:sex>100</xsd:sex><xsd:username>chenzq</xsd:username>"
				+ "</xsd:user>", User.class));*/
		//test90003();
		//test99007();
		//test99008();
		test99006();
	}
	
	public static void test90003() throws Exception {
		String xmlRequest = "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:iop=\"http://iop.cmcc.com\" xmlns:xsd=\"http://iop.cmcc.com/xsd\">"
				+ "<soapenv:Header/><soapenv:Body>"
				+ "<iop:g2PNetContentFeedback>"
				+ "<!--Optional:-->"
				+ "<iop:args0>"
				+ "<!--Optional:-->"
				+ "<xsd:activityId>activityId</xsd:activityId>"
				+ "<!--Optional:-->"
				+ "<xsd:approveStatus>approveStatus</xsd:approveStatus>"
				+ "<!--Optional:-->"
				+ "<xsd:channelSourceType>channelSourceType</xsd:channelSourceType>"
				+ "<!--Optional:-->"
				+ "<xsd:description>description</xsd:description>"
				+ "<!--Optional:-->"
				+ "<xsd:requestHeader>"
				+ "<!--Optional:-->"
				+ "<xsd:accessChannel>2</xsd:accessChannel>"
				+ "<!--Optional:-->"
				+ "<xsd:beId>101</xsd:beId>"
				+ "<!--Optional:-->"
				+ "<xsd:language>2</xsd:language>"
				+ "<!--Optional:-->"
				+ "<xsd:operator>Campaign</xsd:operator>"
				+ "<!--Optional:-->"
				+ "<xsd:password>q3geiItxj4ljNLkI6OINDA==</xsd:password>"
				+ "<!--Optional:-->"
				+ "<xsd:transactionId>1</xsd:transactionId>"
				+ "</xsd:requestHeader>"
				+ "</iop:args0>"
				+ "</iop:g2PNetContentFeedback>"
				+ "</soapenv:Body>"
				+ "</soapenv:Envelope>";
		
		xmlRequest = JaxbUtil.getRequestParam("iop", "g2PNetContentFeedback", xmlRequest);
		RequestParam90003 param = JaxbUtil.fromXml(xmlRequest, RequestParam90003.class);
		System.out.println(param);
	}

	public static void test99007() throws Exception {
		String xmlRequest = "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:iop=\"http://iop.cmcc.com\" xmlns:xsd=\"http://iop.cmcc.com/xsd\">"
				+ "<soapenv:Header/><soapenv:Body>"
				+ "<str:g2PCaseFeedback>"
				+ "<!--Optional:-->"
				+ "<str:args0>"
				+ "<!--Optional:-->"
				+ "<xsd:caseId>caseId</xsd:caseId>"
				+ "<!--Optional:-->"
				+ "<xsd:approveStatus>approveStatus</xsd:approveStatus>"
				+ "<!--Optional:-->"
				+ "<xsd:description>description</xsd:description>"
				+ "<!--Optional:-->"
				+ "<xsd:requestHeader>"
				+ "<!--Optional:-->"
				+ "<xsd:accessChannel>2</xsd:accessChannel>"
				+ "<!--Optional:-->"
				+ "<xsd:beId>101</xsd:beId>"
				+ "<!--Optional:-->"
				+ "<xsd:language>2</xsd:language>"
				+ "<!--Optional:-->"
				+ "<xsd:operator>Campaign</xsd:operator>"
				+ "<!--Optional:-->"
				+ "<xsd:password>q3geiItxj4ljNLkI6OINDA==</xsd:password>"
				+ "<!--Optional:-->"
				+ "<xsd:transactionId>1</xsd:transactionId>"
				+ "</xsd:requestHeader>"
				+ "</str:args0>"
				+ "</str:g2PCaseFeedback>"
				+ "</soapenv:Body>"
				+ "</soapenv:Envelope>";
		
		xmlRequest = JaxbUtil.getRequestParam("str", "g2PCaseFeedback", xmlRequest);
		//System.out.println(xmlRequest);
		RequestParam99007 param = JaxbUtil.fromXml(xmlRequest, RequestParam99007.class);
		System.out.println(param);
	}
	
	public static void test99008() throws Exception {
		String xmlRequest = "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:iop=\"http://iop.cmcc.com\" xmlns:xsd=\"http://iop.cmcc.com/xsd\">"
				+ "<soapenv:Header/><soapenv:Body>"
				+ "<str:p2GCaseDownload>"
				+ "<str:args0>"
			    + "<xsd:caseBO>"
			    + "<xsd:caseAttrMap>"
			    + "<xsd:key>attachFileNames</xsd:key>"
			    + "<xsd:value>file1,file2,file3</xsd:value>"
			    + "</xsd:caseAttrMap>"
			    + "<xsd:caseId>id1001</xsd:caseId>"
			    + "<xsd:caseObjective>CaseObjective</xsd:caseObjective>"
			    + "<xsd:desc>case desc</xsd:desc>"
			    + "<xsd:keyPoint>keyPoint</xsd:keyPoint>"
			    + "<xsd:keyword>keyword</xsd:keyword>"
			    + "<xsd:kpi>kpi</xsd:kpi>"
			    + "<xsd:mind>mind</xsd:mind>"
			    + "<xsd:proId>101</xsd:proId>"
			    + "<xsd:result>result</xsd:result>"
			    + "<xsd:title>title</xsd:title>"
			    + "<xsd:upDateTime>20170527150430</xsd:upDateTime>"
			    + "</xsd:caseBO>"
				+ "<xsd:requestHeader>"
				+ "<xsd:accessChannel>2</xsd:accessChannel>"
				+ "<xsd:beId>101</xsd:beId>"
				+ "<xsd:language>2</xsd:language>"
				+ "<xsd:operator>Campaign</xsd:operator>"
				+ "<xsd:password>q3geiItxj4ljNLkI6OINDA==</xsd:password>"
				+ "<xsd:transactionId>1</xsd:transactionId>"
				+ "</xsd:requestHeader>"
				+ "</str:args0>"
				+ "</str:p2GCaseDownload>"
				+ "</soapenv:Body>"
				+ "</soapenv:Envelope>";
		
		xmlRequest = JaxbUtil.getRequestParam("str", "p2GCaseDownload", xmlRequest);
		System.out.println(xmlRequest);
		RequestParam99006 param = JaxbUtil.fromXml(xmlRequest, RequestParam99006.class);
		System.out.println(param);
	}
	
	public static void test99006() throws Exception {
		
		CaseBO caseBO = new CaseBO();
		caseBO.setCaseId("id1001");
		caseBO.setCaseObjective("CaseObjective");
		caseBO.setCaseAttrMap(new CaseAttrMap("attachFileNames", "file1,file2,file3"));
		caseBO.setDesc("case desc");
		caseBO.setKeyPoint("keyPoint");
		caseBO.setKeyword("keyword");
		caseBO.setKpi("kpi");
		caseBO.setMind("mind");
		caseBO.setProId("101");
		caseBO.setResult("result");
		caseBO.setTitle("title");
		caseBO.setUpDateTime("20170527150430");
		RequestParam99006 param = new RequestParam99006();
		param.setCaseBO(caseBO);
		param.setRequestHeader(JaxbUtil.getRequestHeader());
		
		System.out.println(JaxbUtil.toXml(param));
		/*String xmlStr = JaxbUtil.getPrefix("str", "p2GCaseSync") + JaxbUtil.toXml(param).substring(56) 
				+ JaxbUtil.getSuffix("str", "p2GCaseSync");
		System.out.println(xmlStr);
		
		xmlStr = xmlStr.substring(xmlStr.indexOf("<xsd:caseBO>"));
		xmlStr = xmlStr.substring(0, xmlStr.indexOf("<xsd:requestHeader>"));
		//System.out.println(xmlStr);
		CaseBO xmlBO = JaxbUtil.fromXml(xmlStr, CaseBO.class);
		System.out.println(xmlBO);*/
	}
}
