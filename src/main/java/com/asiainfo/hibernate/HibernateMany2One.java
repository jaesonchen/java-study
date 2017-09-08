package com.asiainfo.hibernate;

import org.hibernate.Session;

import com.asiainfo.hibernate.bean.Clazz;
import com.asiainfo.hibernate.bean.Student;

public class HibernateMany2One {
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		//one2many();
		//many2one();
		many2one2way();
		//testmany2one2way();
	}
	
	/**
	 * inverse 只对one2many many2many有效，对many2one one2one 无效。
	 * cascade 一般不对many2one，many2many，constrained=true的one2one 设置级联删除。
	 * 
	 * one2many 单向关联：
	 * 
	 * 若inverse="true"时，将由多方Student维护关联关系，由于是单向关联，所以多方Student将无法维护外键，即clazz_id将为null。
	 * inverse="false" 表示由一方Clazz维护外键关联关系，所以在保存时会发出update语句用于更新Student的外键clazz_id。
	 * 
	 * 当执行session.save(student)时，若clazz_id为not-null，此时将会出项插入异常，所以在one2many单向关联时需要设置外键clazz_id的属性为not-null="false"。
	 * cascade="save-update" 表示保存Clazz时级联保存Set里关联的Student对象，通常用于<Set>等集合关联。
	 * 若没有设置cascade属性，则必须显式保存Student对象session.save(student)，否则Clazz在维护关联关系时会抛出org.hibernate.TransientObjectException异常。
	 * 
	 * Hibernate: insert into db4myeclipse.clazz (name, id) values (?, ?)
	 * Hibernate: insert into db4myeclipse.student (name, sex, id) values (?, ?, ?)
	 * Hibernate: update db4myeclipse.student set clazz_id=? where id=?
	 * 
	 * <set name="students" cascade="save-update">
     *      <key>
     *          <column name="clazz_id" length="32" />
     *      </key>
     *      <one-to-many class="com.jaeson.hibernatestudy.bean.Student" />
     * </set>
	 */
	public static void one2many() {
		Session session = null;
		try {
			session = HibernateSessionFactory.getSession();
			session.beginTransaction();
			
			Student student = new Student();
			student.setName("chenzq");
			student.setSex(1);
			
			Clazz clazz = new Clazz();
			clazz.setId("1001");
			clazz.setName("第1001班");
			clazz.getStudents().add(student);
			 
			//当设置cascade="save-update"时，可以不用执行session.save(student)
			//session.save(student);
			session.save(clazz);
			
			session.getTransaction().commit();
			
		} catch(Exception e) {
			e.printStackTrace();  
			session.getTransaction().rollback();  
		} finally {  
			HibernateSessionFactory.closeSession();  
		}
	}
	
	/**
	 * many2one 单向关联：
	 * fetch="join"时，lazy="true"失去效果，将会和Student一起进行inner join查询。
	 * 由于是由多方Student维护外键关联关系，所以外键clazz_id可以设置为not-null="true"。
	 * 
	 * 在保存时需要先保存关联对象Clazz，否则Student在维护关联关系时会抛出org.hibernate.action.internal.UnresolvedEntityInsertActions异常。
	 * Attempting to save one or more entities that have a non-nullable association with an unsaved transient entity
	 * session.save(clazz);
	 * session.save(student);
	 * 
	 * Hibernate: insert into db4myeclipse.clazz (name, id) values (?, ?)
	 * Hibernate: insert into db4myeclipse.student (clazz_id, name, sex, id) values (?, ?, ?, ?)
	 * 
	 * <many-to-one name="clazz" class="com.jaeson.hibernatestudy.bean.Clazz" fetch="select">
     * 		<column name="clazz_id" length="32" not-null="true" />
	 * </many-to-one>
	 */
	public static void many2one() {
		Session session = null;
		try {
			session = HibernateSessionFactory.getSession();
			session.beginTransaction();
			
			Student student = new Student();
			student.setName("chenzq");
			student.setSex(1);
			
			Clazz clazz = new Clazz();
			clazz.setId("1010");
			clazz.setName("第1001班");
			student.setClazz(clazz);
			
			//需要先保存一方Clazz，再保存多方Student
			session.save(clazz);
			session.save(student);
			//clazz.setId("1008");
			//session.save(clazz);
			
			session.getTransaction().commit();
			
		} catch(Exception e) {
			e.printStackTrace();  
			session.getTransaction().rollback();  
		} finally {  
			HibernateSessionFactory.closeSession();  
		}
	}
	
	/**
	 * many2one 双向关联：
	 * fetch="join"时，lazy="true"失去效果，将会和Student一起进行inner join查询。
	 * 由于设置了inverse="true"，所以将由多方Student维持关联关系。
	 * 
	 * 必须先保存Clazz对象，再保存Student对象；否则会抛出org.hibernate.action.internal.UnresolvedEntityInsertActions异常。
	 * Attempting to save one or more entities that have a non-nullable association with an unsaved transient entity
	 * 若Clazz设置了cascade="save-update"时，可以只执行session.save(clazz)。
	 * session.save(clazz);
	 * session.save(student);
	 * 
	 * Hibernate: insert into db4myeclipse.clazz (name, id) values (?, ?)
	 * Hibernate: insert into db4myeclipse.student (clazz_id, name, sex, id) values (?, ?, ?, ?)
	 * 
	 * <set name="students" cascade="save-update" inverse="true">
     *      <key>
     *          <column name="clazz_id" length="32" not-null="true" />
     *      </key>
     *      <one-to-many class="com.jaeson.hibernatestudy.bean.Student" />
     * </set>
	 * <many-to-one name="clazz" class="com.jaeson.hibernatestudy.bean.Clazz" fetch="select">
     * 		<column name="clazz_id" length="32" not-null="true" />
	 * </many-to-one>
	 */
	public static void many2one2way() {
		Session session = null;
		try {
			session = HibernateSessionFactory.getSession();
			session.beginTransaction();
			
			Student student = new Student();
			student.setName("chenzq");
			student.setSex(1);
			
			Clazz clazz = new Clazz();
			clazz.setId("1005");
			clazz.setName("第1001班");
			student.setClazz(clazz);
			clazz.getStudents().add(student);
			
			//需要先保存一方Clazz，再保存多方Student，这样只会有2条insert sql。否则会引发2条insert和一个update关联关系的sql。
			//当设置了Clazz的cascade="save-update"时，可以只执行session.save(clazz)
			session.save(clazz);
			//session.save(student);
			
			
			session.getTransaction().commit();
			
		} catch(Exception e) {
			e.printStackTrace();  
			session.getTransaction().rollback();  
		} finally {  
			HibernateSessionFactory.closeSession();  
		}
	}
	public static void testmany2one2way() {
		Session session = null;
		try {
			session = HibernateSessionFactory.getSession();
			session.beginTransaction();
		
			
			Clazz clazz = (Clazz) session.load(Clazz.class, "1002");
			//clazz.setId("1005");
			//clazz.setName("第1001班");
			//student.setClazz(clazz);
			clazz.setStudents(null);
			
			//需要先保存一方Clazz，再保存多方Student，这样只会有2条insert sql。否则会引发2条insert和一个update关联关系的sql。
			//当设置了Clazz的cascade="save-update"时，可以只执行session.save(clazz)
			session.save(clazz);
			//session.save(student);
			
			
			session.getTransaction().commit();
			
		} catch(Exception e) {
			e.printStackTrace();  
			session.getTransaction().rollback();  
		} finally {  
			HibernateSessionFactory.closeSession();  
		}
	}	
}
