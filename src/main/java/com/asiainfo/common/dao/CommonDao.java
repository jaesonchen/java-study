package com.asiainfo.common.dao;

import java.io.Serializable;
import java.util.List;

public interface CommonDao {
	
    public <T extends Serializable> Serializable save(T model);

    public <T extends Serializable> void saveOrUpdate(T model);
    
    public <T extends Serializable> void update(T model);
    
    public <T extends Serializable> void merge(T model);
    
    public <T extends Serializable> void delete(T model);

    public <T extends Serializable, PK extends Serializable> void delete(Class<T> entityClass, PK id);

    public <T extends Serializable, PK extends Serializable> T get(Class<T> entityClass, PK id);
    
    public <T extends Serializable> List<T> listAll(Class<T> entityClass);
    
    //public <T extends Serializable> List<T> listAll(Class<T> entityClass, int pn);
    
    //public <T extends Serializable> List<T> listAll(Class<T> entityClass, int pn, int pageSize);
    
}
