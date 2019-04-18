package com.asiainfo.fileservice.transfer;

import java.io.File;
import java.text.DecimalFormat;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Description: TODO
 * 
 * @author       zq
 * @date         2017年6月10日  下午9:26:36
 * Copyright: 	  北京亚信智慧数据科技有限公司
 */
public class FileUtil {
	
	static final Logger logger = LoggerFactory.getLogger(FileUtil.class);
	
	public static boolean createDir(String destDirName) {
		
		try {
			File dir = new File(destDirName);
			if (dir.exists()) {// 判断目录是否存在
				return true;
			}
			if (dir.mkdirs()) {// 创建目标目录
				logger.debug("目录：{}创建成功！", destDirName);
				return true;
			} else {
				logger.debug("目录：{}创建失败！", destDirName);
				return false;
			}
		} catch (Exception ex) {
			logger.debug("创建目录：{}时发生异常，异常信息：\n{}", destDirName, ex);
			return false;
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
}
