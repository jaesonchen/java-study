package com.asiainfo.hibernate.bean;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.OneToMany;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Id;
import javax.persistence.GeneratedValue;
import javax.persistence.Column;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "clazz", catalog = "db4myeclipse")
public class Clazz implements java.io.Serializable {

	// Fields
	private static final long serialVersionUID = 1L;
	private String id;
	private String name;
	private Set<Student> students = new HashSet<Student>(0);

	// Constructors

	/** default constructor */
	public Clazz() {
	}

	/** minimal constructor */
	public Clazz(String id, String name) {
		this.id = id;
		this.name = name;
	}

	/** full constructor */
	public Clazz(String id, String name, Set<Student> students) {
		this.id = id;
		this.name = name;
		this.students = students;
	}

	@GenericGenerator(name = "idGenerator", strategy = "assigned")
	@Id
	@GeneratedValue(generator = "idGenerator")
	@Column(name = "id", unique = true, nullable = false)
	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@Column(name = "name", nullable = false, length = 32)
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "clazz")
	public Set<Student> getStudents() {
		return this.students;
	}

	public void setStudents(Set<Student> students) {
		this.students = students;
	}

}