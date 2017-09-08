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
@Table(name = "role", catalog = "db4myeclipse")
public class Role implements java.io.Serializable {

	// Fields
	private static final long serialVersionUID = 1L;
	private Integer id;
	private String roleName;
	private String roleDesc;
	private Set<User> users = new HashSet<User>(0);

	// Constructors

	/** default constructor */
	public Role() {
	}

	/** minimal constructor */
	public Role(String roleName) {
		this.roleName = roleName;
	}

	/** full constructor */
	public Role(String roleName, String roleDesc, Set<User> users) {
		this.roleName = roleName;
		this.roleDesc = roleDesc;
		this.users = users;
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

	@Column(name = "roleName", nullable = false, length = 32)
	public String getRoleName() {
		return this.roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	@Column(name = "roleDesc", length = 128)
	public String getRoleDesc() {
		return this.roleDesc;
	}

	public void setRoleDesc(String roleDesc) {
		this.roleDesc = roleDesc;
	}

	@ManyToMany(
			targetEntity = com.asiainfo.hibernate.bean.User.class,
			mappedBy = "roles")
	public Set<User> getUsers() {
		return this.users;
	}

	public void setUsers(Set<User> users) {
		this.users = users;
	}
 
}