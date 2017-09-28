package com.asiainfo.xml.xstream.model;

import java.io.Serializable;

public class Author implements Serializable {

	private static final long serialVersionUID = 1L;

	private String name;
	
    public Author(String name) {
            this.name = name;
    }
    public String getName() {
            return name;
    }
}
