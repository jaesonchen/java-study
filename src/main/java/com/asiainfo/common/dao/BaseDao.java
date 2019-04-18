package com.asiainfo.common.dao;

import java.util.List; 

/**
 * 
 * @Description: hibernate dao 公共父类
 * 
 * @author chenzq  
 * @date 2019年3月21日 下午1:05:58
 * @version V1.0
 * @Copyright: Copyright(c) 2019 jaesonchen.com Inc. All rights reserved.
 */
public interface BaseDao<M extends java.io.Serializable, PK extends java.io.Serializable> {

	/** 
	 * 按主键取记录
	 * @param id 主键值  
	 * @return 记录实体对象，如果没有符合主键条件的记录，则返回null 
	 */  
	public M findById(PK id); 
	
	/**
	 * 
	 * @param queryString 查询语句
	 * @param params 查询参数数组
	 * @return 返回一个list集合数据 
	 */
	public List<M> findByQueryString(String queryString, Object... params);

	/** 
	 * 修改一个实体对象（UPDATE一条记录）
	 * @param entity 实体对象 
	 */  
	public void update(M entity);  
	
	/**
	 * 插入一个实体（在数据库INSERT一条记录）
	 * @param entity 实体对象    
	 */  
	public PK save(M entity);  

	/** 
	 * 增加或更新实体 
	 * @param entity 实体对象 
	 */  
	public void saveOrUpdate(M entity);  
	
	/** 
	 * 删除指定的实体 
	 * @param entity 实体对象 
	 */  
	public void delete(M entity);
	
	/** 
	 * 删除指定id的实体 
	 * @param id pk 
	 */
	public void deleteById(PK id);
}
