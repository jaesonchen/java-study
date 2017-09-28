package com.asiainfo.xml.xstream.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Blog implements Serializable {

	private static final long serialVersionUID = 220443567714916059L;

	private Author writer;
    private List<Entry> entries = new ArrayList<Entry>();

    public Blog(Author writer) {
            this.setWriter(writer);
    }

    public void add(Entry entry) {
            entries.add(entry);
    }

    public List<Entry> getContent() {
            return entries;
    }

	public Author getWriter() {
		return writer;
	}

	public void setWriter(Author writer) {
		this.writer = writer;
	}
}
