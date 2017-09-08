package com.asiainfo.jdbc.caseinsensitive;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.CallableStatementCreator;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.SqlParameter;

/**
 * @Description: TODO
 * 
 * @author       zq
 * @date         2017年3月31日  下午5:33:49
 * Copyright: 	  北京亚信智慧数据科技有限公司
 */
public class CaseInsensitiveJdbcTemplate extends JdbcTemplate {

	@Override
	public Map<String, Object> queryForMap(String sql) throws DataAccessException {
		
		Map<String, Object> result = super.queryForMap(sql);
		return this.generateCaseInsensitiveMap(result);
	}
	
	@Override
	public Map<String, Object> queryForMap(String sql, Object[] args, int[] argTypes) throws DataAccessException {

		Map<String, Object> result = super.queryForMap(sql, args, argTypes);
		return this.generateCaseInsensitiveMap(result);
	}
	
	@Override
	public Map<String, Object> queryForMap(String sql, Object... args) throws DataAccessException {

		Map<String, Object> result = super.queryForMap(sql, args);
		return this.generateCaseInsensitiveMap(result);
	}
	
	@Override
	public Map<String, Object> call(CallableStatementCreator csc, List<SqlParameter> declaredParameters)
			throws DataAccessException {

		Map<String, Object> result = super.call(csc, declaredParameters);
		return this.generateCaseInsensitiveMap(result);
	}
	
	@Override
	public List<Map<String, Object>> queryForList(String sql) throws DataAccessException {
		
		List<Map<String, Object>> result = super.queryForList(sql);
		return this.generateCaseInsensitiveMapList(result);
	}
	
	@Override
	public List<Map<String, Object>> queryForList(String sql, Object[] args, int[] argTypes) throws DataAccessException {

		List<Map<String, Object>> result = super.queryForList(sql, args, argTypes);
		return this.generateCaseInsensitiveMapList(result);
	}
	
	@Override
	public List<Map<String, Object>> queryForList(String sql, Object... args) throws DataAccessException {

		List<Map<String, Object>> result = super.queryForList(sql, args);
		return this.generateCaseInsensitiveMapList(result);
	}
	
	//生成忽略大小写的Map，map中的key都转换成大写
	private Map<String, Object> generateCaseInsensitiveMap(Map<String, Object> map) {
		return map == null ? null : new CaseInsensitiveHashMap(map);
	}
	
	//生成忽略大小写的Map，map中的key都转换成大写
	private List<Map<String, Object>> generateCaseInsensitiveMapList(List<Map<String, Object>> mapList) {
		
		List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
		for (Map<String, Object> map : mapList) {
			result.add(this.generateCaseInsensitiveMap(map));
		}
		return result;
	}
}
