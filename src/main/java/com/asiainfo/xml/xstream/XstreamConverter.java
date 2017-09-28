package com.asiainfo.xml.xstream;

import java.util.Date;

import com.asiainfo.xml.xstream.convert.DateConverter;
import com.asiainfo.xml.xstream.model.Birthday;
import com.thoughtworks.xstream.XStream;

public class XstreamConverter {

	public static void main(String[] args) {

		XStream xstream = new XStream();
		xstream.alias("birth", Birthday.class);
		xstream.registerConverter(new DateConverter());
		System.out.println(xstream.toXML(new Birthday(new Date())));
		
		String xmlStr = "<birth><date>2017-01-04 12:35:21</date></birth>";
		Birthday birth = (Birthday) xstream.fromXML(xmlStr);
		System.out.println(birth.getDate());
	}
}
