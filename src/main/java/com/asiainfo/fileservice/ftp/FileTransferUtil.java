package com.asiainfo.fileservice.ftp;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.asiainfo.ftp.SFTPChannel;
import com.asiainfo.ftp.SFTPConstants;
import com.asiainfo.util.PropertyReaderUtil;
import com.jcraft.jsch.SftpException;

/**
 * @Description: TODO
 * 
 * @author       zq
 * @date         2017年6月8日  下午4:49:17
 * Copyright: 	  北京亚信智慧数据科技有限公司
 */
public class FileTransferUtil {

	static final Logger logger = LoggerFactory.getLogger(FileTransferUtil.class);
	
	public static List<String> getFilelistInDirectory(String remotePath) {

		SFTPChannel sftp = null;
		try {
			Map<String, String> config = getSftpConfig();
			sftp = openSFTPChannel(config, 60000);
			return sftp.listFileInDir(remotePath);
		} catch (Exception ex) {
			logger.error("获取目录：{}下的文件时发生异常，异常信息：\n{}", remotePath, ex);
		} finally {
			
		}
		return null;
	}
	
	public static boolean download(String remotePath, String localPath, String remoteFileName) {
		
		SFTPChannel sftp = null;
		String remoteFilePath = null;
		try {
			remoteFilePath = (remotePath.endsWith("/")) ? remotePath : (remotePath + "/");
			remoteFilePath += (StringUtils.isEmpty(remoteFileName) ? "" : remoteFileName);
			Map<String, String> config = getSftpConfig();
			sftp = openSFTPChannel(config, 60000);
			logger.debug("remotePath:{}, localPath:{}", remoteFilePath, localPath);
			//先创建本地目录
			if (!FileUtil.createDir(localPath)) {
				logger.error("创建本地目录：{}发生异常，请联系管理员！", localPath);
			}
			return sftp.downloadFile(remoteFilePath, localPath);
		} catch (Exception ex) {
			logger.error("下载文件：{}发生异常，异常信息：\n{}", remoteFilePath, ex);
		} finally {
			closeSFTPChannel(sftp);
		}
		return false;
	}
	
	public static boolean upload(String remotePath, String localPath, String localFileName) {
		
		SFTPChannel sftp = null;
		String localFilePath = null;
		try {
			localFilePath = (localPath.endsWith("/")) ? localPath : (localPath + "/");
			localFilePath += (StringUtils.isEmpty(localFileName) ? "" : localFileName);
			Map<String, String> config = getProvinceSftpConfig();
			sftp = openSFTPChannel(config, 60000);
			logger.debug("localFilePath={}, remoteFilePath={}", localFilePath, remotePath);
			//sftp.changeAndMakeDir(remotePath);
			changeAndMakeDir(sftp, remotePath);
			return sftp.uploadFile(localFilePath, remotePath);
		} catch (Exception ex) {
			logger.error("上传文件：{}到:{}发生异常，异常信息：\n{}", localFilePath, remotePath, ex);
		} finally {
			closeSFTPChannel(sftp);
		}
		return false;
	}
	
	public static void changeAndMakeDir(SFTPChannel sftp, String remotePath) throws SftpException {
		
		final String pwd = sftp.getWorkingDirectory();
		final String mkPath = remotePath.substring(remotePath.indexOf(pwd) + pwd.length());
		String[] dirs = mkPath.split("/");
		for (String dir : dirs) {
			if (StringUtils.isEmpty(dir)) {
				continue;
			}
			sftp.createDirectory(dir);
			sftp.changeDir(dir);
		}
	}
	
	public static SFTPChannel openSFTPChannel(Map<String, String> config, int timeout) {
		
		SFTPChannel sftp = null;
		try {
			sftp = new SFTPChannel(config, timeout);
			return sftp;
		} catch (Exception ex) {
			logger.error("打开sftp通道发生异常，异常信息如下：\n", ex);
			throw new RuntimeException("打开sftp通道发生异常！");
		}
	}

	public static void closeSFTPChannel(SFTPChannel sftp) {
		
		try {
			if (null != sftp) {
				sftp.closeChannel();
			}
		} catch (Exception ex) {}
	}
	
	public static Map<String, String> getSftpConfig() {
		
		Map<String, String> result = new HashMap<String, String>();
		result.put(SFTPConstants.SFTP_REQ_HOST, PropertyReaderUtil.getInstance().getProperty("IOP.GROUP.FILE.SFTP.HOST"));
		result.put(SFTPConstants.SFTP_REQ_PORT, PropertyReaderUtil.getInstance().getProperty("IOP.GROUP.FILE.SFTP.PORT"));
		result.put(SFTPConstants.SFTP_REQ_USERNAME, PropertyReaderUtil.getInstance().getProperty("IOP.GROUP.FILE.SFTP.USERNAME"));
		result.put(SFTPConstants.SFTP_REQ_PASSWORD, PropertyReaderUtil.getInstance().getProperty("IOP.GROUP.FILE.SFTP.PASSWORD"));
		return result;
	}
	
	public static Map<String, String> getProvinceSftpConfig() {
		
		Map<String, String> result = new HashMap<String, String>();
		result.put(SFTPConstants.SFTP_REQ_HOST, PropertyReaderUtil.getInstance().getProperty("IOP.PROVINCE.FILE.SFTP.HOST"));
		result.put(SFTPConstants.SFTP_REQ_PORT, PropertyReaderUtil.getInstance().getProperty("IOP.PROVINCE.FILE.SFTP.PORT"));
		result.put(SFTPConstants.SFTP_REQ_USERNAME, PropertyReaderUtil.getInstance().getProperty("IOP.PROVINCE.FILE.SFTP.USERNAME"));
		result.put(SFTPConstants.SFTP_REQ_PASSWORD, PropertyReaderUtil.getInstance().getProperty("IOP.PROVINCE.FILE.SFTP.PASSWORD"));
		return result;
	}
}
