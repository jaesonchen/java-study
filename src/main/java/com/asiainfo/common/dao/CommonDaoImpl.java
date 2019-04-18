package com.asiainfo.common.dao;

import java.io.Serializable;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

@Repository("commonDao")
public class CommonDaoImpl implements CommonDao {

	@Autowired
    @Qualifier("sessionFactory")
	private SessionFactory sessionFactory;

    public Session getSession() {
        return sessionFactory.getCurrentSession();
    }
    
	@Override
	public <T extends Serializable> Serializable save(T model) {
		return this.getSession().save(model);
	}

	@Override
	public <T extends Serializable> void saveOrUpdate(T model) {
		this.getSession().saveOrUpdate(model);
	}

	@Override
	public <T extends Serializable> void update(T model) {
		this.getSession().update(model);
	}

	@Override
	public <T extends Serializable> void merge(T model) {
		this.getSession().merge(model);
	}

	@Override
	public <T extends Serializable> void delete(T model) {
		this.getSession().delete(model);
	}

	@Override
	public <T extends Serializable, PK extends Serializable> void delete(
			Class<T> entityClass, PK id) {
	    
		this.delete(this.get(entityClass, id));
	}

	@Override
	@SuppressWarnings("unchecked")
	public <T extends Serializable, PK extends Serializable> T get(
			Class<T> entityClass, PK id) {

		return (T) this.getSession().get(entityClass, id);
	}

	@Override
	@SuppressWarnings("unchecked")
	public <T extends Serializable> List<T> listAll(Class<T> entityClass) {

		Criteria criteria = getSession().createCriteria(entityClass);
        return criteria.list();
	}
}
