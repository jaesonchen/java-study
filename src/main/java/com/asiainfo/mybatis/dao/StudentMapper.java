package com.asiainfo.mybatis.dao;

import java.util.List;

import com.asiainfo.mybatis.model.Student;

public interface StudentMapper {

	public int save(Student student);
	public int update(Student student);
	public int delete(String id);
	public Student getStudent(String id);
	public List<Student> getAll();
}
