package com.asiainfo.hibernate;

import org.hibernate.Session;

import com.asiainfo.hibernate.bean.Course;
import com.asiainfo.hibernate.bean.Learner;

public class HibernateMany2Many {
	public static void main(String[] args) {
		//testmany2many();
		//many2many();
		many2manyCascade();
	}
	public static void testmany2many() {
		Session session = null;
		try {
			session = HibernateSessionFactory.getSession();
			session.beginTransaction();
			
			Learner learner = (Learner) session.load(Learner.class, 13);
			Course course = (Course) session.load(Course.class, 11);
			Course course1 = (Course) session.load(Course.class, 12);
			
			System.out.println(learner.getName());
			learner.setName("chenzq6");
			learner.getCourses().add(course);
			learner.getCourses().remove(course1);
			
			session.delete(learner);
			session.flush();
			
			session.getTransaction().commit();
			
		} catch(Exception e) {
			e.printStackTrace();  
			session.getTransaction().rollback();  
		} finally {  
			HibernateSessionFactory.closeSession();  
		}
	}
	
	/**
	 * inverse控制的关联关系更新在session.flush时执行，所以更新关联关系的update sql总是在最后执行。删除时总是在最前面执行。
	 * cascade几种取值：
	 * save-update:       级联保存(load以后如果子对象发生了更新,也会级联更新). 但它不会级联删除
	 * delete:            级联删除, 但不具备级联保存和更新
	 * all-delete-orphan: 在解除父子关系时,自动删除不属于父对象的子对象, 也支持级联删除和级联保存更新.
	 * all:               级联删除, 级联更新,但解除父子关系时不会自动删除子对象. 
	 * delete-orphan:     删除所有和当前对象解除关联关系的对象
	 */
	public static void many2many() {
		Session session = null;
		try {
			session = HibernateSessionFactory.getSession();
			session.beginTransaction();
			
			Learner learner = new Learner();
			learner.setName("chenzq");
			
			Course course = new Course();
			course.setName("语文");
			
			learner.getCourses().add(course);
			course.getLearners().add(learner);
			
			//learner 和course的保存总是在learner_course之前
			session.save(course);
			session.save(learner);
			
			session.getTransaction().commit();
			
		} catch(Exception e) {
			e.printStackTrace();  
			session.getTransaction().rollback();  
		} finally {  
			HibernateSessionFactory.closeSession();  
		}
	}
	
	/**
	 * cascade级联更新
	 * cascade="all-delete-orphan" 在save/update/flush/commit的时候不能直接设置learner.setCourses(null);
	 * 会引发异常：A collection with cascade="all-delete-orphan" was no longer referenced by the owning entity instance。
	 * delete操作时，可以直接设置null，这样只会删除learner表和learner_course表记录。
	 */
	public static void many2manyCascade() {
		Session session = null;
		try {
			session = HibernateSessionFactory.getSession();
			session.beginTransaction();
			
			Learner learner = (Learner) session.load(
					"com.jaeson.hibernatestudy.bean.Learner", 14);
			
			//cascade="all-delete-orphan"时，移除set的元素在session.flush时会引发级联操作，直接删除关联learner_course表记录和set元素对应的course表记录
			/*java.util.Set courses = learner.getCourses();
			Course course = courses.iterator().hasNext() ? (Course) courses.iterator().next() : null;
			if(course != null) {
				learner.getCourses().remove(course);
			}
			*/
			
			learner.setCourses(null);
			//cascade="all-delete-orphan"时，session.saveOrUpdate(learner)会引发异常，session.delete(learner)可以正常执行删除操作
			//session.saveOrUpdate(learner);
			session.delete(learner);
			session.getTransaction().commit();
			
		} catch(Exception e) {
			e.printStackTrace();
			session.getTransaction().rollback();  
		} finally {
			HibernateSessionFactory.closeSession();  
		}
	}
}
