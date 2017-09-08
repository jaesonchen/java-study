package com.asiainfo.hibernate.bean;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

/**
 * @javax.persistence.*
 * 默认情况下hbm文件中的声明比类中的注解元数据具有更高的优先级。
 * @Entity注解将一个类声明为一个实体bean(即一个持久化POJO类), 
 * @Id注解则声明了该实体bean的标识属性. 
 * 其他的映射定义是隐式的.这种以隐式映射为主体,以显式映射为例外的配置方式在新的EJ3规范中处于非常重要的位置.
 * 在对一个类进行注解时,你可以选择对它的的属性或者方法进行注解,根据你的选择,Hibernate的访问类型分别为 field或property.
 * 
 * @Table是类一级的注解, 通过@Table注解可以为实体bean映射指定表(table),目录(catalog)和schema的名字. 
 * 如果没有定义@Table,那么系统自动使用默认值：实体的短类名(不附带包名).
 * @Table元素包括了一个schema 和一个 catalog属性,如果需要可以指定相应的值. 
 * 
 * @Id注解可以将实体bean中的某个属性定义为标识符(identifier). 该属性的值可以通过应用自身进行设置, 也可以通过Hiberante生成(推荐). 
 * 使用 @GeneratedValue注解可以定义该标识符的生成策略: 
 * strategy 指定生成的策略,默认是GenerationType.AUTO
 * generator 指定生成主键使用的生成器
 * • AUTO 	- 可以是identity column类型,或者sequence类型或者table类型,取决于不同的底层数据库. 
 * • TABLE 	- 使用表保存id值 
 * • IDENTITY - 主键由数据库自动生成,主要是自动增长类型
 * • SEQUENCE - sequence 
 * @org.hibernate.annotations.GenericGenerator 允许你定义一个Hibernate特定的id生成器.
 * @GenericGenerator(name="generator", strategy = "uuid")
 * @Id
 * @GeneratedValue(generator="generator")
 * 
 * @Basic所有没有定义注解的属性等价于在其上面添加了@Basic注解. 通过 @Basic注解可以声明属性的获取策略(fetch strategy),FetchType.LAZY 或者 FetchType.EAGER.
 * 
 * @Column 注解可将属性映射到列.
 * @Column(
 * 		name="columnName";
 * 		boolean unique() default false; 
 * 		boolean nullable() default true;
 * 		int length() default 255)
 * 
 *  组件映射:
 *  组件类必须在类一级定义@Embeddable注解. 在特定的实体的关联属性上使用@Embedded和 @AttributeOverride注解可以覆盖该属性对应的嵌入式对象的列映射：
 *  @Embedded
 *  @AttributeOverrides( {
 *  	@AttributeOverride(name="address",	column = @Column(name="address") ),
 *  	@AttributeOverride(name="zipCode",	column = @Column(name="zipCode") ),
 *  	@AttributeOverride(name="phone", 	column = @Column(name="phone") ),
 *  })
 *  
 * cascade：级联,它可以有有五个值可选,分别是：
 * CascadeType.PERSIST：级联新建
 * CascadeType.REMOVE : 级联删除
 * CascadeType.REFRESH：级联刷新
 * CascadeType.MERGE  ： 级联更新
 * CascadeType.ALL    ： 以上全部四项
 * 
 *  @OneToOne唯一外键关联:一对一关联可能是双向的.在双向关联中, 有且仅有一端是作为主体(owner)端存在的：主体端负责维护联接列(即更新). 
 *  对于不需要维护这种关系的从表则通过mappedBy属性进行声明.mappedBy的值指向主体的关联属性. 
 *  注释五个属性：targetEntity、cascade、fetch、optional 和mappedBy，
 *  fetch属性默认值是FetchType.EAGER。
 *  optional = true设置外键属性可以为null
 *  targetEntity属性:Class类型的属性。定义关系类的类型，默认是该成员属性对应的类类型
 *  cascade属性：CascadeType[]类型。
 *  
 *  外键端	@OneToOne(cascade = CascadeType.ALL)
 *  		@JoinColumn(name="card_id")
 *  主键端	@OneToOne(mappedBy = "idCard")
 *  
 *  Many-to-one:@JoinColumn是可选的,关联字段默认值为：主体的关联属性名＋下划线＋被关联端的主键列名. 
 *  fetch属性默认值是FetchType.EAGER。
 *  @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
 *  @JoinColumn(name="dept_id")
 *  @OneToMany(mappedBy="department")
 *  
 *  One-to-many:在EJB3规范中多对一这端几乎总是双向关联中的主体(owner)端, 而一对多这端的关联注解为@OneToMany( mappedBy="department")
 *  fetch属性默认值是FetchType.LAZY。
 *  Many-to-many:通过@ManyToMany注解可定义的多对多关联. 同时,你也需要通过注解@JoinTable描述关联表和关联条件. 如果是双向关联,其中一段必须定义为owner,另一端必须定义为inverse
 *  fetch属性默认值是FetchType.LAZY。
 *  @ManyToMany(
 *  	targetEntity=com.jaeson.hibernatestudy.bean.Role.class,
 *      cascade={CascadeType.PERSIST, CascadeType.MERGE}
 *  )
 *  @JoinTable(
 *      name="user_role",
 *      joinColumns=@JoinColumn(name="user_id"),
 *      inverseJoinColumns=@JoinColumn(name="role_id")
 *  )
 *  @ManyToMany(
 *      mappedBy = "roles",
 *      targetEntity = com.jaeson.hibernatestudy.bean.User.class
 *  )
 *
 */

@Entity
@Table(name = "user", catalog = "db4myeclipse")
public class User implements java.io.Serializable {

	// Fields
	private static final long serialVersionUID = 1L;
	private String id;
	private IdCard idCard;
	private Department department;
	private String userName;
	private Address address;
	private Set<Role> roles = new HashSet<Role>(0);

	// Constructors

	/** default constructor */
	public User() {
	}

	/** minimal constructor */
	public User(IdCard idCard, Department department, String userName, Address address) {
		this.idCard = idCard;
		this.department = department;
		this.userName = userName;
		this.address = address;
	}

	/** full constructor */
	public User(IdCard idCard, Department department, String userName,
			Address address, Set<Role> roles) {
		this.idCard = idCard;
		this.department = department;
		this.userName = userName;
		this.address = address;
		this.roles = roles;
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

	@OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinColumn(name="card_id", unique=true)
	public IdCard getIdCard() {
		return this.idCard;
	}

	public void setIdCard(IdCard idCard) {
		this.idCard = idCard;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "dept_id")
	public Department getDepartment() {
		return this.department;
	}

	public void setDepartment(Department department) {
		this.department = department;
	}

	@Column(name = "userName", nullable = false, length = 32)
	public String getUserName() {
		return this.userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	@Embedded
	@AttributeOverrides({
		@AttributeOverride(name="address",	column = @Column(name="address") ),
		@AttributeOverride(name="zipCode",	column = @Column(name="zipCode") ),
		@AttributeOverride(name="phone", 	column = @Column(name="phone") ),
		})
	public Address getAddress() {
		return this.address;
	}

	public void setAddress(Address address) {
		this.address = address;
	}
 
	@ManyToMany(
			targetEntity=com.asiainfo.hibernate.bean.Role.class,
			cascade=CascadeType.ALL)
	@JoinTable(
			name="user_role",
			joinColumns=@JoinColumn(name="user_id"),
			inverseJoinColumns=@JoinColumn(name="role_id")
			)
	public Set<Role> getRoles() {
		return this.roles;
	}

	public void setRoles(Set<Role> roles) {
		this.roles = roles;
	}

}