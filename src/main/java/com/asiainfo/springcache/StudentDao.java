package com.asiainfo.springcache;

import java.util.List;

import com.asiainfo.common.dao.BaseDao;

public interface StudentDao extends BaseDao<Student, String> {

	public void studentMethod();
	
	public List<Student> findByClassid(String id);
}
