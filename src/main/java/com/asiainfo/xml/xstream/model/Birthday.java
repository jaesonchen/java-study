package com.asiainfo.xml.xstream.model;

import java.io.Serializable;
import java.util.Date;

public class Birthday implements Serializable {

	private static final long serialVersionUID = 1L;

	private Date date;

	public Birthday(Date date) {
		this.date = date;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}
	
}
