package com.asiainfo.hibernate;

import org.hibernate.Hibernate;
import org.hibernate.Session;

import com.asiainfo.hibernate.bean.Address;
import com.asiainfo.hibernate.bean.Department;
import com.asiainfo.hibernate.bean.IdCard;
import com.asiainfo.hibernate.bean.Role;
import com.asiainfo.hibernate.bean.User;

public class HibernateTest {
 
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Session session = null;
		try {
			session = HibernateSessionFactory.getSession();
			session.beginTransaction();
			
			User user = (User) session.load(
					"com.jaeson.hibernatestudy.bean.User", "402881e552a65bf90152a65bfa210001");
			
			user.getId();
			
			user.getUserName();
			
			//脱离session前必须强制加载proxy or persistent collection，否则脱离session在使用时会抛出异常。
			//Hibernate.initialize(Object proxy);
			Hibernate.initialize(user.getDepartment());
			Hibernate.initialize(user.getRoles());
			
			user.getDepartment().getDeptName();
			
			IdCard idcard = user.getIdCard();
			System.out.println(idcard.getUser().getUserName());
			
			user.getRoles().size();
			
			user.getRoles().toArray();
			
			session.getTransaction().commit();
			
		} catch(Exception e) {
			e.printStackTrace();  
			session.getTransaction().rollback();  
		} finally {  
			HibernateSessionFactory.closeSession();  
		}
		
	}	
	
	//one to one:
	public static void one2oneSave() {
		
		Session session = null;
		try {
			session = HibernateSessionFactory.getSession();
			session.beginTransaction();
			
			User user = (User) session.get(
					"com.jaeson.hibernatestudy.bean.User", "402881e5529691a701529691a8920000");
			
			IdCard idCard = new IdCard();
			idCard.setAuthDate(new java.sql.Timestamp(new java.util.Date().getTime()));
			idCard.setCardNo("350322197912233031");
			user.setIdCard(idCard);
			
			//idCard是transient对象，在保存user之前必须手动保存idCard
			session.save(idCard);
			session.saveOrUpdate(user);
			session.getTransaction().commit();
			
		} catch(Exception e) {
			e.printStackTrace();  
			session.getTransaction().rollback();  
		} finally {  
			HibernateSessionFactory.closeSession();  
		}
	}
	
	//many to one:load user and update department
	public static void loadAndUpdate() {
		
		Session session = null;
		try {
			session = HibernateSessionFactory.getSession();
			session.beginTransaction();
			
			User user = (User) session.get(
					"com.jaeson.hibernatestudy.bean.User", "402881e5529691a701529691a8920000");
			
			Department dept = new Department();
			dept.setId("10004");
			dept.setDeptName("销售部");
			dept.getUsers().add(user);
			user.setDepartment(dept);
			
			//dept是transient对象，在保存user之前必须手动保存dept
			session.save(dept);
			session.saveOrUpdate(user);
			session.getTransaction().commit();
			
		} catch(Exception e) {
			e.printStackTrace();  
			session.getTransaction().rollback();  
		} finally {  
			HibernateSessionFactory.closeSession();  
		}
	}
	
	//many to one:save user and update department
	public static void saveAndUpdate() {
		Session session = null;
		try {
			session = HibernateSessionFactory.getSession();
			session.beginTransaction();
			
			User user = new User();
			user.setUserName("chenzq1223");
			
			Address address = new Address("北街家园8区", 100086, "13522587602");
			user.setAddress(address);
			
			Department dept = (Department) session.get(
					"com.jaeson.hibernatestudy.bean.Department", "10002");
			dept.setDeptName("研发部");
			dept.getUsers().add(user);
			user.setDepartment(dept);
			
			//dept是persistence对象，可以直接保存user，flush/commit时也会同时保存dept的修改
			//session.save(dept);
			session.save(user);
			session.getTransaction().commit();
			
		} catch(Exception e) {
			e.printStackTrace();  
			session.getTransaction().rollback();  
		} finally {  
			HibernateSessionFactory.closeSession();  
		}
	}
	
	public static void saveRole() {
		Session session = null;
		try {
			session = HibernateSessionFactory.getSession();
			session.beginTransaction();
			
			Role role = new Role();
			role.setRoleName("管理员");
			role.setRoleDesc("拥有最大权限的用户");
			session.save(role);
			session.getTransaction().commit();
			
		} catch(Exception e) {
			e.printStackTrace();  
			session.getTransaction().rollback();  
		} finally {  
			HibernateSessionFactory.closeSession();  
		}
	}
	
	//many to many 映射关系保存
	public static void saveUserRole() {
		
		Session session = null;
		try {
			
			session = HibernateSessionFactory.getSession();
			session.beginTransaction();
			User user = new User();
			user.setUserName("jaeson");
			
			Address address = new Address("北街家园8区", 100080, "13522587602");
			user.setAddress(address);
			
			Department dept = (Department) session.get(
					"com.jaeson.hibernatestudy.bean.Department", "10002");
			dept.getUsers().add(user);
			user.setDepartment(dept);
			
			IdCard idCard = new IdCard();
			idCard.setAuthDate(new java.sql.Timestamp(new java.util.Date().getTime()));
			idCard.setCardNo("350322197912233031");
			user.setIdCard(idCard);
			
			Role role = new Role();
			role.setRoleName("部门经理");
			role.setRoleDesc("拥有部门管理权限的用户");
			role.getUsers().add(user);
			user.getRoles().add(role);
			
			session.save(idCard);
			session.save(role);
			session.save(user);
			
			session.getTransaction().commit();
			
		} catch(Exception e) {
			e.printStackTrace();  
			session.getTransaction().rollback();  
		} finally {  
			HibernateSessionFactory.closeSession();  
		}

	}

	//many to many 映射关系 可以从任何一端保存
	public static void saveUserRole1() {
		
		Session session = null;
		try {
			
			session = HibernateSessionFactory.getSession();
			session.beginTransaction();
			User user = (User) session.get(
					"com.jaeson.hibernatestudy.bean.User", "402881e5529691a701529691a8920000");
			
			Role role = (Role) session.get(
					"com.jaeson.hibernatestudy.bean.Role", 2);
			role.setRoleName("部门经理");
			role.getUsers().add(user);
			user.getRoles().add(role);
			
			//session.saveOrUpdate(role);
			session.saveOrUpdate(user);
			
			session.getTransaction().commit();
			
		} catch(Exception e) {
			e.printStackTrace();  
			session.getTransaction().rollback();  
		} finally {  
			HibernateSessionFactory.closeSession();  
		}

	}
}


