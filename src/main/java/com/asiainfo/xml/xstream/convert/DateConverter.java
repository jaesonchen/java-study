package com.asiainfo.xml.xstream.convert;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.thoughtworks.xstream.converters.ConversionException;
import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;

public class DateConverter implements Converter {
    
	@Override
	public boolean canConvert(@SuppressWarnings("rawtypes") Class clazz) {
		//return Calendar.class.isAssignableFrom(clazz);
		return clazz.equals(Date.class);
	}

	@Override
	public void marshal(Object obj, HierarchicalStreamWriter writer, MarshallingContext context) {
		
		writer.setValue(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format((Date) obj));
	}

	@Override
	public Object unmarshal(HierarchicalStreamReader reader, UnmarshallingContext context) {

        try {
        	
        	return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(reader.getValue());
        } catch (ParseException e) {
                throw new ConversionException(e.getMessage(), e);
        }   
	}

}
