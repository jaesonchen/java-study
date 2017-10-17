package com.asiainfo.excel;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service("loadExcelService")
public class LoadExcelService {

	private JdbcTemplate jdbcTemplate;
	@Autowired
	public void setDataSource(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}
	
	public void insert(String id, String name, String type) {
		
		String sql = "insert into MCD_CHN_BJ_914_OVERLAY(plan_id, plan_name, type) values(?, ?, ?)";
		this.jdbcTemplate.update(sql, new Object[]{ id, name, Integer.parseInt(type)});
	}
}
