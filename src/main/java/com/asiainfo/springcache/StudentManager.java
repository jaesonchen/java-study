package com.asiainfo.springcache;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
//import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
//@CacheConfig(cacheNames="studentCache", key="#p0")
public class StudentManager {
	
	@Autowired
	@Qualifier("jdbcTemplateStudentDao")
	private StudentDao studentDao;
	
	public void setStudentDao(StudentDao studentDao) {
		this.studentDao = studentDao;
	}
	
	//对于使用@Cacheable标注的方法，Spring在每次执行前都会检查Cache中是否存在相同key的缓存元素，
	//如果存在就不再执行该方法，而是直接从缓存中获取结果进行返回，否则才会执行并将返回结果存入指定的缓存中。
	//不指定key时，使用默认的key生成策略KeyGenerator。
	//方法没有参数时，使用SimpleKey.EMPTY
	//只有一个参数时，使用该参数作为key
	//多于一个参数时，使用包含所有参数的SimpleKey（所有参数的hashCode）
	@Cacheable("studentCache")
	public Student findById(String id) {
		
		return this.studentDao.findById(id);
	}
	
	//将返回结果放入cache中，并使用EL指定的key生成策略
	@Cacheable(cacheNames="studentCache", key="#id")
	public List<Student> findByClassid(String id) {
		
		return this.studentDao.findByClassid(id);
	}
	
	//key="#p0.id" 表示第一个参数的id属性
	@Cacheable(cacheNames="studentCache", key="#p0.id")
	public List<Student> findByClass(Clazz clazz) {
		
		return this.studentDao.findByClassid(clazz.getId());
	}
	
	//与@Cacheable不同的是使用@CachePut标注的方法在执行前不会去检查缓存中是否存在之前执行过的结果，
	//而是每次都会执行该方法，并将执行结果以键值对的形式存入指定的缓存中。
	@CachePut(cacheNames="studentCache", key="#entity.id")
	public Student save(Student entity) {
		
		entity.setId(this.studentDao.save(entity));
		return entity;
	}
	
	//每次都直接执行update方法，并将结果放入cache
	@CachePut(cacheNames="studentCache", key="#entity.id")
	public Student update(Student entity) {
		
		this.studentDao.update(entity);
		return entity;
	}
	
	//@CacheEvict移除cache中相应的缓存项，evict操作默认在方法返回时进行，如果方法执行中抛出异常，则evict操作不会发生。
	//beforeInvocation=true表示在方法调用之前先进行cache的evict操作，allEntries=true表示清除所有的缓存项。
	@CacheEvict(cacheNames="studentCache", key="#entity.id", beforeInvocation=true)
	public void delete(Student entity) {
		
		this.studentDao.delete(entity);
	}
	
	//移除cache中相应的缓存项
	@CacheEvict("studentCache")
	public void deleteById(String id) {
		
		this.studentDao.deleteById(id);
	}
	
	//移除指定cache中的所有缓存项
	@CacheEvict(cacheNames="studentCache", allEntries=true)
	public void removeAll() {
		
		System.out.println("removeAll is called, evict all cache entry");
	}
}
