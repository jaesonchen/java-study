package com.asiainfo.hibernate;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.LogicalExpression;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import com.asiainfo.hibernate.bean.Department;
import com.asiainfo.hibernate.bean.Role;
import com.asiainfo.hibernate.bean.Student;
import com.asiainfo.hibernate.bean.User;

/**
 * @author zq
 *	Session session = SessionFactory.openSession();
 *	Transaction tx = session.beginTransaction();
 *	for ( int i=0; i<100000; i++ ) {
 *    	Employee employee = new Employee(.....);
 *   	session.save(employee);
 *		if( i % 50 == 0 ) { // Same as the JDBC batch size
 *       //flush a batch of inserts and release memory:
 *        session.flush();
 *        session.clear();
 *    	}
 *	}
 *	tx.commit();
 *	session.close();
 */
@SuppressWarnings("all")
public class HibernateHQL {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		testHQL();
		//testCriteria();
		//testSQLQuery();
	}


	/**
	 *  HQL: Hibernate Query Language. 
	 *  特点：  
	 *  >> 1，与SQL相似，SQL中的语法基本上都可以直接使用。  
	 *  >> 2，SQL查询的是表和表中的列；HQL查询的是对象与对象中的属性。  
	 *  >> 3，HQL的关键字不区分大小写，类名与属性名是区分大小写的。  
	 *  >> 4，SELECT可以省略.        
	 */   
	public static void testHQL() {
		
		Session session = null;
		try {
			session = HibernateSessionFactory.getSessionFactory().getCurrentSession();
			session.beginTransaction();
			
			//FROM子句
			//String hql = "FROM com.jaeson.hibernatestudy.bean.User";
			//简单的查询，Employee为实体名而不是数据库中的表名(面向对象特性)  
			//hql = "FROM Employee";  
			//hql = "FROM Employee AS e"; // 使用别名  
			//hql = "FROM Employee e"; // 使用别名，as关键字可省略  
			String hql = "FROM User";
			Query query = session.createQuery(hql);
			List results = query.list();
			System.out.println(results.size());
			
			//WHERE子句
			// 带上过滤条件的（可以使用别名）：Where  
			//hql = "FROM Employee WHERE id < 10";  
			//hql = "FROM Employee e WHERE e.id < 10";  
			//hql = "FROM Employee e WHERE e.id < 10 AND e.id > 5";  
			hql = "FROM Role r WHERE r.id = 1";
			query = session.createQuery(hql);
			results = query.list();
			if(results != null & results.size() > 0)
				System.out.println(((Role) results.get(0)).getRoleName());
			
			//ORDER BY 子句
			//带上排序条件的：Order By  
			//hql = "FROM Employee e WHERE e.id < 10 ORDER BY e.name";  
			//hql = "FROM Employee e WHERE e.id < 10 ORDER BY e.name DESC";  
			//hql = "FROM Employee e WHERE e.id < 10 ORDER BY e.name DESC, e.id ASC";  
			hql = "FROM Role r WHERE r.id > 0 "
					+ "ORDER BY r.id DESC, r.roleName ASC ";
			query = session.createQuery(hql);
			results = query.list();
			System.out.println(results.size());
			
			//指定select子句（不可以使用select *）  
			//hql = "SELECT e FROM Employee e"; // 相当于"FROM Employee e"  
			//hql = "SELECT e.name FROM Employee e"; // 只查询一个列，返回的集合的元素类型就是这个属性的类型  
			//hql = "SELECT e.id, e.name FROM Employee e"; // 查询多个列，返回的集合的元素类型是Object数组  
			//hql = "SELECT new Employee(e.id, e.name) FROM Employee e"; // 可以使用new语法，指定把查询出的部分属性封装到对象中  
			hql = "SELECT u.userName FROM User u";
			query = session.createQuery(hql);
			//分页查询
			query.setFirstResult(0); 
			query.setMaxResults(10);
			results = query.list();
			for (Object obj : results) {
				String str = (String) obj;
				System.out.println(str);
			}
			
			//聚集函数：count(), max(), min(), avg(), sum()  
			//hql = "SELECT COUNT(*) FROM Employee"; // 返回的结果是Long型的  
			//hql = "SELECT min(id) FROM Employee"; // 返回的结果是id属性的类型  

			//GROUP BY  Having 
			/*
			hql = "SELECT e.name, COUNT(e.id) FROM Employee e GROUP BY e.name";  
			hql = "SELECT e.name, COUNT(e.id) FROM Employee e GROUP BY e.name HAVING count(e.id) > 1";  
			hql = "SELECT e.name, COUNT(e.id) FROM Employee e WHERE id < 9 GROUP BY e.name HAVING count(e.id) > 1";  
			hql = "SELECT e.name, COUNT(e.id) " +   
	   				"FROM Employee e " + 
					"WHERE id < 9 " +  
					"GROUP BY e.name " +   
					"HAVING count(e.id) > 1 " + 
					"ORDER BY count(e.id) ASC";  
			hql = "SELECT e.name,COUNT(e.id) AS c " + 
					"FROM Employee e " + 
					"WHERE id < 9 " + 
					"GROUP BY e.name " + 
					"HAVING count(e.id) > 1 " + 	// 在having子句中不能使用列别名  
					"ORDER BY c ASC"; 			// 在orderby子句中可以使用列别名  

			 */
			hql = "SELECT COUNT(*), U.userName FROM User U "
					+ "GROUP BY U.userName ";
			query = session.createQuery(hql);
			results = query.list();
			Object[] arr = (Object[]) results.get(0);
			System.out.println(arr[0] + ", " + arr[1]);
			
			//连接查询 / HQL是面向对象的查询  
			//>> 内连接（inner关键字可以省略）  
			//hql = "SELECT e.id, e.name, d.name FROM Employee e JOIN e.department d";  
			//hql = "SELECT e.id, e.name, d.name FROM Employee e INNER JOIN e.department d";  
			//>> 左外连接（outer关键字可以省略）  
			//hql = "SELECT e.id, e.name, d.name FROM Employee e LEFT OUTER JOIN e.department d";  
			//>> 右外连接（outer关键字可以省略）  
			//hql = "SELECT e.id, e.name, d.name FROM Employee e RIGHT JOIN e.department d";  
			//可以使用更方便的方法  
			//hql = "SELECT e.id, e.name, e.department.name FROM Employee e";  

			
			//查询时使用参数  
			// >> 方式一：使用'?'占位  
			/*
			hql = "FROM Employee e WHERE id BETWEEN ? AND ?";  
			List list2 = session.createQuery(hql)
					.setParameter(0, 5)			// 设置参数，第1个参数的索引为0。  
					.setParameter(1, 15)
					.list();  
			
			// >> 方式二：使用变量名  
			hql = "FROM Employee e WHERE id BETWEEN :idMin AND :idMax";  
			List list3 = session.createQuery(hql)
					.setParameter("idMax", 15)
					.setParameter("idMin", 5)
					.list();  
			
			// 当参数是集合时，一定要使用setParameterList()设置参数值  
			hql = "FROM Employee e WHERE id IN (:ids)";  
			List list4 = session.createQuery(hql)
					.setParameterList("ids", new Object[] { 1, 2, 3, 5, 8, 100 })
					.list();
			*/
			
			//update与delete，不会通知Session缓存  
			/*
			// >> Update
			int result = session.createQuery(
					"UPDATE Employee e SET e.name = ? WHERE id > 15")
					.setParameter(0, "无名氏")
					.executeUpdate(); 			// 返回int型的结果，表示影响了多少行。  
			// >> Delete  
			int result1 = session.createQuery(
					"DELETE FROM Employee e WHERE id > 15")
					.executeUpdate();			// 返回int型的结果，表示影响了多少行。  
			*/
			
			session.getTransaction().commit();
			
		} catch(Exception e) {
			e.printStackTrace();  
			session.getTransaction().rollback();  
		} finally {  
			HibernateSessionFactory.closeSession();  
		}
	}
	
	/**
	 * Criteria:
	 * org.hibernate.criterion.Criterion是Hibernate提供的一个面向对象查询条件接口，一个单独的查询就是Criterion接口的一个实例，
	 * 用于限制Criteria对象的查询，在Hibernate中Criterion对象的创建通常是通过Restrictions 工厂类完成的。
	 * org.hibernate.criterion.Order用于排序操作, Criteria 接口提供addOrder(Order order)用于生成排序SQL。
	 * Restrictions
	 * 
	 * HQL运算符 QBC运算符 含义：
	 * =  Restrictions.eq() 等于 
	 * <> Restrictions.not(Exprission.eq()) 不等于 
	 * >  Restrictions.gt() 大于 
	 * >= Restrictions.ge() 大于等于
	 * <  Restrictions.lt() 小于 
	 * <= Restrictions.le() 小于等于 
	 * is null Restrictions.isnull() 等于空值 
	 * is not null Restrictions.isNotNull() 非空值 
	 * like Restrictions.like() 字符串模式匹配 
	 * and Restrictions.and() 逻辑与
	 * and Restrictions.conjunction() 逻辑与 
	 * or Restrictions.or() 逻辑或 
	 * or Restrictions.disjunction() 逻辑或 
	 * not Restrictions.not() 逻辑非 
	 * in(列表) Restrictions.in() 等于列表中的某一个值
	 */
	public static void testCriteria() {
		
		Session session = null;
		try {
			session = HibernateSessionFactory.getSession();
			session.beginTransaction();
			
			//当session.createCriteria(实体类.class) 就会产生一条select所有列from 表；SQL语句，查询实体类对应数据表的所有记录，
			//然后我们就可以在这个Criteria对象上进行条件查询、分页查询、多表关联查询、投影查询、子查询等一系列操作……
			Criteria criteria = session.createCriteria(User.class);
			List<User> results = criteria.list();
			System.out.println(results.size());
			
			//对查询添加条件对象Criterion
			criteria = session.createCriteria(User.class);
			criteria.add(Restrictions.like("userName", "jaeson%"));
			results = criteria.list();
			System.out.println(results.size());
			
			//AND或OR使用LogicalExpression限制条件：
			criteria = session.createCriteria(User.class);
			Criterion zipCode = Restrictions.gt("address.zipCode", 2000);
			Criterion userName = Restrictions.like("userName","jaeson%");
			//对于多个查询条件，Restrictions提供了逻辑组合查询方法。
			//and(Criterion lhs, Criterion rhs) 用于生成多个条件and关系SQL语句； 
			//or(Criterion lhs, Criterion rhs) 用于生成多个条件or关系SQL语句；
			LogicalExpression orExp = Restrictions.and(zipCode, userName);
			criteria.add( orExp );
			//分页操作 firstResult和maxResults 
			criteria.setFirstResult(0);
			criteria.setMaxResults(10);
			//排序操作 Order 
			criteria.addOrder(Order.desc("userName"));
			criteria.addOrder(Order.asc("id"));
			results = criteria.list();
			System.out.println(results.size());
			
			//多表关联操作 createAlias和createCriteria 
			//Criteria接口提供createAlias 和 createCriteria 两组方法用于完成多表关联查询。
			//createAlias(String associationPath, String alias) 采用内连接关联。
			//createAlias(String associationPath, String alias, int joinType) 可以通过joinType指定连接类型。
			//createCriteria(String associationPath) 采用内连接关联（返回新的Criteria对象）。
			//createCriteria(String associationPath, int joinType) 可以通过joinType指定关联类型（返回新的Criteria对象 ）。 
			
			//方法一：使用createCriteria方法
			criteria = session.createCriteria(User.class);
			//通过User类的department属性返回新的Criteria
			Criteria criteria2 = criteria.createCriteria("department");
			criteria2.add(Restrictions.eq("deptName", "行政部"));
			results = criteria.list();
			System.out.println(results.size());
			
			//方法二：使用createAlias 方法
			//使用createAlias方法不会像createCriteria那样返回一个新的Criteria对象，alias只是对关联表进行别名设置，通过别名引用设置属性。 
			criteria = session.createCriteria(User.class);
			//进行表关联，设置User类的属性department的别名
			criteria.createAlias("department", "d");
			criteria.add(Restrictions.eq("d.deptName", "行政部"));
			results = criteria.list();
			System.out.println(results.size());
			
			//投影、分组查询 Projection 
			//在实际开发中，进行查询是：可能只需要返回表中的指定列信息（投影）或者进行统计查询（count、avg、sum、min、max），
			//Criteria接口提供setProjection(Projection projection)方法用于实现投影查询操作。 
			//org.hibernate.criterion.Projections工厂类用于返回Projection投影查询对象。
			criteria = session.createCriteria(User.class);
			//通过Projections.property指定查询哪些属性
			criteria.setProjection(Projections.projectionList()
					.add(Projections.property("id"))
					.add(Projections.property("userName")));
			List<Object []> list = criteria.list();
			System.out.println(((Object []) list.get(0))[1]);
			
			//Projections提供了分组函数的查询方法： 
			//rowCount() 查询记录总数量；
			//count(String propertyName) 统计某列数量；
			//countDistinct(String propertyName) 统计某列数量（排除重复）；
			//avg(String propertyName) 统计某列平均值； 
			//sum(String propertyName) 对某列值求和；
			//max(String propertyName) 求某列最大值； 
			//min(String propertyName) 求某列最小值。
			criteria = session.createCriteria(User.class);
			criteria.setProjection(Projections.rowCount());
			Long count = (Long) criteria.uniqueResult();
			System.out.println(count);
			
			criteria = session.createCriteria(User.class);
			criteria.setProjection(Projections.projectionList()
					.add(Projections.groupProperty("department"))
					.add(Projections.count("id")));
			List<Object []> listGourp = criteria.list();
			Object [] countarr = (Object []) listGourp.get(0);
			System.out.println(countarr[1] + "=" + ((Department) countarr[0]).getDeptName());
			
			session.getTransaction().commit();
			
		} catch(Exception e) {
			e.printStackTrace();  
			session.getTransaction().rollback();  
		} finally {  
			HibernateSessionFactory.closeSession();  
		}
	}
	
	/**
	 * 对原生SQL查询执行的控制是通过SQLQuery接口进行的，通过执行Session.createSQLQuery()获取这个接口。
	 */
	public static void testSQLQuery() {
		Session session = null;
		try {
			session = HibernateSessionFactory.getSession();
			session.beginTransaction();
			
			//标量查询（Scalar queries） 
			//它们都将返回一个Object数组(Object[])组成的List，数组每个元素都是user表的一个字段值。
			//Hibernate会使用ResultSetMetadata来判定返回的标量值的实际顺序和类型。 
			//session.createSQLQuery("SELECT * FROM user").list();  
			//session.createSQLQuery("SELECT id, userName FROM user").list(); 
			//SQLQuery query = session.createSQLQuery("select * from user where id = ?");
			//query.setParameter(0, 12);

			List results = session.createSQLQuery("SELECT id, userName FROM user").list(); 
			System.out.println(((Object[]) results.get(0))[1]);
			
			//实体查询(Entity queries) 
			//通过addEntity()让原生查询返回实体对象。 
			//session.createSQLQuery("SELECT * FROM user").addEntity(User.class);  
			//session.createSQLQuery("SELECT id, userName FROM user").addEntity(User.class); 
			//假若实体在映射时有一个many-to-one的关联指向另外一个实体，在查询时必须也返回那个实体，否则会导致发生一个"column not found"的数据库错误。
			results = session.createSQLQuery("SELECT id, name, sex, clazz_id FROM student").addEntity(Student.class).list();
			for(Iterator iterator = results.iterator(); iterator.hasNext(); ) {
				Student stu = (Student) iterator.next();
				System.out.println("deptName=" + stu.getClazz().getName());
			}
			
			//处理关联和集合类 
			//join查询会在每行返回多个实体对象，处理时需要注意 。 
			results = session.createSQLQuery("select {user.*}, {dept.*} from user user, department dept where user.dept_id = dept.id")
						.addEntity("user", User.class)
						.addJoin("dept", "user.department")
						.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP)
						.list();
			Map map = (HashMap) results.get(0);
			User user = (User) map.get("user");
			System.out.println(user.getUserName());
			System.out.println(user.getDepartment().getDeptName());
			
			results = session.createSQLQuery("select {user.*}, {dept.*} from user user, department dept where user.dept_id = dept.id")
					.addEntity("user", User.class)
					.addJoin("dept", "user.department")
					.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY)
					.list();
			Department dept = (Department) results.get(0);
			System.out.println(dept.getDeptName());
			
			session.getTransaction().commit();
			
		} catch(Exception e) {
			e.printStackTrace();  
			session.getTransaction().rollback();  
		} finally {  
			HibernateSessionFactory.closeSession();  
		}
	}
}
