package com.asiainfo.common.dao;

import java.lang.reflect.ParameterizedType;
import java.util.List;

import javax.annotation.Resource;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

public abstract class BaseDaoImpl<M extends java.io.Serializable, PK extends java.io.Serializable> implements BaseDao<M, PK> {

	
	protected Class<M> entityClass;

	/**
	 * 向DAO层注入SessionFactory 
	 */
	@Resource
	protected SessionFactory sessionFactory;
	
	/**
	 * 构造方法，根据实例类自动获取实体类类型 
	 */
	@SuppressWarnings("unchecked")
	public BaseDaoImpl() {
		ParameterizedType type = (ParameterizedType) this.getClass().getGenericSuperclass();  
		this.entityClass = (Class<M>) type.getActualTypeArguments()[0];
	}  

	/**
	 * 继承类取得session
	 * @return
	 */
	protected Session currentSession() {  
		//事务必须是开启的(Required)，否则获取不到
		return this.sessionFactory.getCurrentSession();  
	}  

	@SuppressWarnings("unchecked")
	@Override
	public M findById(PK id) {
		return (M) this.currentSession().get(entityClass, id);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<M> findByQueryString(String hql, Object... params) {
		
		Query query = this.currentSession().createQuery(hql);  
		for (int i = 0; params != null && i < params.length; i++) { 
			query.setParameter(i, params[i]);
		}
		return query.list();  
	}

	@Override
	public void update(M entity) {
		this.currentSession().update(entity);
	}

	@SuppressWarnings("unchecked")
	@Override
	public PK save(M entity) {
		PK id = (PK) this.currentSession().save(entity);
		return id;
	}

	@Override
	public void saveOrUpdate(M entity) {		
		this.currentSession().saveOrUpdate(entity);
	}

	@Override
	public void delete(M entity) {		
		this.currentSession().delete(entity);
	}
	
	@Override
	public void deleteById(PK id) {
		this.currentSession().delete(this.findById(id));
	}
}
