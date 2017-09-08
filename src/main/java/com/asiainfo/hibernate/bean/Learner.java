package com.asiainfo.hibernate.bean;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.ManyToMany;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Id;
import javax.persistence.GeneratedValue;
import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "learner", catalog = "db4myeclipse")
public class Learner implements java.io.Serializable {

	// Fields
	private static final long serialVersionUID = 1L;
	private Integer id;
	private String name;
	private Set<Course> courses = new HashSet<Course>(0);

	// Constructors

	/** default constructor */
	public Learner() {
	}

	/** minimal constructor */
	public Learner(String name) {
		this.name = name;
	}

	/** full constructor */
	public Learner(String name, Set<Course> courses) {
		this.name = name;
		this.courses = courses;
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
			targetEntity=com.asiainfo.hibernate.bean.Course.class,
			cascade=CascadeType.ALL)
	@JoinTable(
			name="learner_course",
			joinColumns=@JoinColumn(name="learner_id"),
			inverseJoinColumns=@JoinColumn(name="course_id")
			)
	public Set<Course> getCourses() {
		return this.courses;
	}

	public void setCourses(Set<Course> courses) {
		this.courses = courses;
	}

}