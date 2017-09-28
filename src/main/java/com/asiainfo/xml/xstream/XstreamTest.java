package com.asiainfo.xml.xstream;

import com.asiainfo.xml.xstream.model.Address;
import com.asiainfo.xml.xstream.model.Person;
import com.thoughtworks.xstream.XStream;

public class XstreamTest {


	public static void main(String[] args) {
		
		XStream xstream = new XStream();
		//不设置别名会输出全限定类名<com.asiainfo.xstream.model.Person>
		xstream.alias("person", Person.class);
		xstream.alias("address", Address.class);
		
		Person person = new Person("jaeson", 25, new Address("13522587602", "changping", "100021"));
		String xmlStr = xstream.toXML(person);
		
		System.out.println(xmlStr);
		
		Person back = (Person) xstream.fromXML(xmlStr);
		System.out.println(back);
	}
}
