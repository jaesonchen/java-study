package com.asiainfo.hibernate.bean;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Id;
import javax.persistence.GeneratedValue;
import javax.persistence.Column;
import javax.persistence.JoinColumn;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "department", catalog = "db4myeclipse")
public class Department implements java.io.Serializable {

	// Fields
	private static final long serialVersionUID = 1L;
	private String id;
	private String deptName;
	private Department parentDept;
	private Set<User> users = new HashSet<User>(0);
	private Set<Department> childDepts = new HashSet<Department>(0);

	// Constructors

	/** default constructor */
	public Department() {
	}

	/** minimal constructor */
	public Department(String id, String deptName) {
		this.id = id;
		this.deptName = deptName;
	}

	/** full constructor */
	public Department(String id, Department parentDept, String deptName,
			Set<User> users, Set<Department> childDepts) {
		this.id = id;
		this.parentDept = parentDept;
		this.deptName = deptName;
		this.users = users;
		this.childDepts = childDepts;
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

	@ManyToOne
	@JoinColumn(name = "parent_id")
	public Department getParentDept() {
		return this.parentDept;
	}

	public void setParentDept(Department parentDept) {
		this.parentDept = parentDept;
	}

	@Column(name = "deptName", nullable = false, length = 32)
	public String getDeptName() {
		return this.deptName;
	}

	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}

	@OneToMany(mappedBy = "department")
	public Set<User> getUsers() {
		return this.users;
	}

	public void setUsers(Set<User> users) {
		this.users = users;
	}

	@OneToMany(mappedBy = "parentDept")
	public Set<Department> getChildDepts() {
		return this.childDepts;
	}

	public void setChildDepts(Set<Department> childDepts) {
		this.childDepts = childDepts;
	}

}