package com.asiainfo.xml.xstream;

import com.asiainfo.xml.xstream.convert.AuthorConverter;
import com.asiainfo.xml.xstream.model.Author;
import com.asiainfo.xml.xstream.model.Blog;
import com.asiainfo.xml.xstream.model.Entry;
import com.thoughtworks.xstream.XStream;

public class XstreamAlias {

	public static void main(String[] args) {
		
		XStream xstream = new XStream();
        xstream.alias("blog", Blog.class);
        xstream.alias("entry", Entry.class);
        xstream.addImplicitCollection(Blog.class, "entries");
        xstream.useAttributeFor(Blog.class, "writer");
        xstream.aliasField("author", Blog.class, "writer");
        xstream.registerConverter(new AuthorConverter());
        
		Blog teamBlog = new Blog(new Author("Guilherme Silveira"));
        teamBlog.add(new Entry("first","My first blog entry."));
        teamBlog.add(new Entry("tutorial", 
                "Today we have developed a nice alias tutorial. Tell your friends! NOW!"));

        System.out.println(xstream.toXML(teamBlog));
	}
}
