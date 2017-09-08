package com.asiainfo.hibernate.bean;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.ManyToMany;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Id;
import javax.persistence.GeneratedValue;
import javax.persistence.Column;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "course", catalog = "db4myeclipse")
public class Course implements java.io.Serializable {

	// Fields
	private static final long serialVersionUID = 1L;
	private Integer id;
	private String name;
	private Set<Learner> learners = new HashSet<Learner>(0);

	// Constructors

	/** default constructor */
	public Course() {
	}

	/** minimal constructor */
	public Course(String name) {
		this.name = name;
	}

	/** full constructor */
	public Course(String name, Set<Learner> learners) {
		this.name = name;
		this.learners = learners;
	}

	@GenericGenerator(name = "idGenerator", strategy = "native")
	@Id
	@GeneratedValue(generator = "idGenerator")
	@Column(name = "id", unique = true, nullable = false)
	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@Column(name = "name", nullable = false, length = 32)
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@ManyToMany(
			targetEntity = com.asiainfo.hibernate.bean.Learner.class,
			mappedBy = "courses")
	public Set<Learner> getLearners() {
		return this.learners;
	}

	public void setLearners(Set<Learner> learners) {
		this.learners = learners;
	}

}