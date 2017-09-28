package com.asiainfo.xml.xstream.convert;

import com.asiainfo.xml.xstream.model.Author;
import com.thoughtworks.xstream.converters.SingleValueConverter;

public class AuthorConverter implements SingleValueConverter {

	@Override
	public boolean canConvert(@SuppressWarnings("rawtypes") Class type) {
		return type.equals(Author.class);
	}

	@Override
	public Object fromString(String name) {
		return new Author(name);
	}

	@Override
	public String toString(Object obj) {
		return ((Author) obj).getName();
	}

}
