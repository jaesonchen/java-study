package com.asiainfo.xml.jaxb;

import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
//import java.util.UUID;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.transform.Source;
import javax.xml.transform.sax.SAXSource;

import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

/**
 * @Description: TODO
 * 
 * @author       zq
 * @date         2017年5月27日  下午2:22:03
 * Copyright: 	  北京亚信智慧数据科技有限公司
 */
public class JaxbUtil {

	public static String toXml(Object obj) throws Exception {
		
		Writer writer = new StringWriter();
		JAXBContext context = JAXBContext.newInstance(obj.getClass());
		Marshaller marshaller = context.createMarshaller();
		//默认编码是utf-8
		marshaller.setProperty(Marshaller.JAXB_ENCODING, "utf-8");
		//格式化生成的xml
		marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
		//是否省略xml头信息
		marshaller.setProperty(Marshaller.JAXB_FRAGMENT, Boolean.TRUE);
		marshaller.marshal(obj, writer);
		return writer.toString();
	}
	
	@SuppressWarnings("unchecked")
	public static <T> T fromXml(String xml, Class<T> t) throws Exception {
		
		JAXBContext context = JAXBContext.newInstance(t);
		Unmarshaller unmarshaller = context.createUnmarshaller();
		StringReader reader = new StringReader(xml);

		SAXParserFactory sax = SAXParserFactory.newInstance();
		sax.setNamespaceAware(false);
		XMLReader xmlReader = sax.newSAXParser().getXMLReader();

		Source source = new SAXSource(xmlReader, new InputSource(reader));
		return (T) unmarshaller.unmarshal(source);
	}
	
	public static String getRequestParam(String namespace, String serviceName, String request) {
		String xmlRequest = request.substring(request.indexOf("<" + namespace + ":args0>"));
		xmlRequest = xmlRequest.substring(0, xmlRequest.indexOf("</" + namespace + ":" + serviceName + ">"));
		return xmlRequest;
	}
	
	public static String getPrefix(String namespace, String serviceName) {
		return "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" "
				+ "xmlns:str=\"http://strategy.iop.cmcc.com\" xmlns:xsd=\"http://strategy.iop.cmcc.com/xsd\">\n"
				+ "<soapenv:Header/>\n<soapenv:Body>\n<" + namespace + ":" + serviceName + ">\n";
	}
	
	public static String getSuffix(String namespace, String serviceName) {
		return "</" + namespace + ":" + serviceName + ">\n</soapenv:Body>\n</soapenv:Envelope>";
	}
	
	public static RequestHeader getRequestHeader() {
		
		RequestHeader header = new RequestHeader();
		header.setAccessChannel("2");
		header.setBeId("101");
		header.setLanguage("2");
		header.setOperator("Campaign");
		header.setPassword("q3geiItxj4ljNLkI6OINDA==");
		header.setTransactionId("1");
		//header.setTransactionId(UUID.randomUUID().toString());
		return header;
	}
}
