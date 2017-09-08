package com.asiainfo.mybatis.service.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;

import com.asiainfo.mybatis.dao.StudentMapper;
import com.asiainfo.mybatis.model.Student;
import com.asiainfo.mybatis.service.StudentService;

@Service
public class StudentServiceImpl implements StudentService {

	private static final Logger logger = LoggerFactory.getLogger(StudentServiceImpl.class);
	
	@Autowired
	private StudentMapper mapper;
	
	@Override
	public int save(Student student) {
		logger.info("save student(id={}, name={})", student.getId(), student.getName());
		return mapper.save(student);
	}

	@Override
	public int update(Student student) {
		
		logger.info("update student(id={}, name={})", student.getId(), student.getName());
		return mapper.update(student);
	}

	@Override
	public int delete(String id) {
		
		logger.info("delete student(id={})", id);
		return mapper.delete(id);
	}

	@Override
	public Student getStudent(String id) {
		
		logger.info("get student(id={})", id);
		return mapper.getStudent(id);
	}

	@Override
	public List<Student> getAll() {
		
		logger.info("getAll()");
		return mapper.getAll();
	}

}
