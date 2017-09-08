package com.asiainfo.hibernate.bean;

import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.FetchType;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Id;
import javax.persistence.GeneratedValue;
import javax.persistence.Column;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "student", catalog = "db4myeclipse")
public class Student implements java.io.Serializable {

	// Fields
	private static final long serialVersionUID = 1L;
	private String id;
	private Clazz clazz;
	private String name;
	private Integer sex;

	// Constructors

	/** default constructor */
	public Student() {
	}

	/** full constructor */
	public Student(Clazz clazz, String name, Integer sex) {
		this.clazz = clazz;
		this.name = name;
		this.sex = sex;
	}

	@GenericGenerator(name = "idGenerator", strategy = "uuid")
	@Id
	@GeneratedValue(generator = "idGenerator")
	@Column(name = "id", unique = true, nullable = false)
	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "clazz_id")
	public Clazz getClazz() {
		return this.clazz;
	}

	public void setClazz(Clazz clazz) {
		this.clazz = clazz;
	}

	@Column(name = "name", nullable = false, length = 32)
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "sex")
	public Integer getSex() {
		return this.sex;
	}

	public void setSex(Integer sex) {
		this.sex = sex;
	}

}