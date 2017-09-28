package com.asiainfo.xml.xstream;

import com.asiainfo.xml.xstream.model.Address;
import com.asiainfo.xml.xstream.model.Person;
import com.thoughtworks.xstream.XStream;

public class XstreamAnnotation {

	public static void main(String[] args) {
		
		XStream xstream = new XStream();
		xstream.autodetectAnnotations(true);
		//xstream.processAnnotations(Person.class);
		//xstream.processAnnotations(Address.class);
		
		Person person = new Person("jaeson", 25, new Address("13522587602", "changping", "100021"), "contenta", "contentb");
		System.out.println(xstream.toXML(person));
		
	}
}
