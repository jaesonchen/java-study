package com.asiainfo.util;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * @Description: 接口数据文件重传序列号生成工具
 * 
 * @author       zq
 * @date         2017年5月16日  上午10:05:39
 * Copyright: 	  北京亚信智慧数据科技有限公司
 */
@Component
@Transactional
public class SequenceUtil {
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	private static final Map<String, Object> MAP = new HashMap<String, Object>();
	
	private synchronized static Object getLock(String key) {
		if (null == MAP.get(key)) {
			MAP.put(key, new Object());
		}
		return MAP.get(key);
	}
	
	
	/**
	 * 
	 * @Description: 生成重传序号列表
	 * 
	 * @param fileNamePrefix	文件名前缀（当前批次的唯一标识）
	 * @param num				需要返回的序列批次数
	 * @return					返回  重传序列号_批次号 的列表
	 */
	public List<String> generateFileNameSequenceList(String fileNamePrefix, int num) {
		
		List<String> result = new ArrayList<String>();
		String sequence = formatSequence(getNextSequence(fileNamePrefix, 0), 2);
		for (int i = 0; i < num; ) {
			result.add(sequence + "_" + formatSequence(++i, 3));
		}
		return result;
	}
	
	/**
	 * 
	 * @Description: 生成重传序号
	 * 
	 * @param fileNamePrefix	文件名前缀（当前批次的唯一标识）
	 * @return					返回重传序列号
	 */
	public String generateFileNameSequence(String fileNamePrefix) {
		return formatSequence(getNextSequence(fileNamePrefix, 0), 2);
	}
	
	/**
	 * 
	 * @Description: 生成sequence
	 * 
	 * @param key	序列号的唯一标识
	 * @param start	序列号的初始值
	 * @return
	 */
	public int getNextSequence(String key, int start) {
		
		synchronized(getLock(key)) {
			Integer current = getCurrentSequence(key);
			int next = (null == current) ? start : (current.intValue() + 1);
			saveCurrentSequency(key, next, null == current);
			return next;
		}
	}
	
	/**
	 * 
	 * @Description: 格式化为指定长度序列号，不足的位数在前面补0
	 * 
	 * @param seq		序列号
	 * @param length	序列号的长度
	 * @return			返回格式化后的序列号
	 */
	public static String formatSequence(int seq, int length) {
		
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < length; i++) {
			sb.append("0");
		}
		return new DecimalFormat(sb.toString()).format(seq);
	}
	
	/**
	 * 
	 * @Description: 生成uuid
	 * 
	 * @return
	 */
	public static String generateUUID() {
		return UUID.randomUUID().toString();
	}
	
	/**
	 * 
	 * @Description: 从数据库中取得当前的重传序列号，没有时为0，并修改数据库序列号
	 * 
	 * @param key
	 * @return
	 */
	private Integer getCurrentSequence(String key) {
		
		String sql = "select sequence_value from iop_sequence_info where sequence_key=?";
		List<Map<String, Object>> list = null;
		try {
			list = this.jdbcTemplate.queryForList(sql, new Object[] {key});
			/*	@Override
				public Integer mapRow(ResultSet rs, int rowNum) throws SQLException {
					return rs.getInt("sequence_value");
				}
			});*/
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new RuntimeException(ex.getCause());
		}
		return (null == list || list.isEmpty()) ? null : new Integer(String.valueOf(list.get(0).get("sequence_value")));
	}
	
	/**
	 * 
	 * @Description: 修改数据库中序列号的当前值
	 * 
	 * @param key
	 * @param sequence
	 * @param insert
	 */
	private void saveCurrentSequency(String key, int sequence, boolean insert) {
		
		try {
			String sql =  insert ? "insert into iop_sequence_info(sequence_value, sequence_key) values(?, ?)" 
					: "update iop_sequence_info set sequence_value=? where sequence_key=?";
			//执行更新
			this.jdbcTemplate.update(sql, new Object[] {sequence, key});
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new RuntimeException(ex.getCause());
		}
	}
	
	public static void main(String[] args) {

		//System.out.println(formatSequence(123, 5));
		//System.out.println(generateFileNameSequence("chenzq_test_key1"));
		//System.out.println(generateFileNameSequenceList("chenzq_test_key2", 3));
	}
}
