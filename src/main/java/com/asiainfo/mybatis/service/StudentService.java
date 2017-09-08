package com.asiainfo.mybatis.service;

import java.util.List;

import com.asiainfo.mybatis.model.Student;

public interface StudentService {
	
	public int save(Student student);
	public int update(Student student);
	public int delete(String id);
	public Student getStudent(String id);
	public List<Student> getAll();
}
