package com.asiainfo.fileservice.transfer;

import java.io.File;
import java.io.RandomAccessFile;
import java.lang.reflect.Method;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.asiainfo.fileservice.formatter.FormatService;
import com.asiainfo.fileservice.formatter.Formatter;
import com.asiainfo.fileservice.parser.ErrorCodes;
import com.asiainfo.fileservice.parser.ParseError;
import com.asiainfo.fileservice.parser.ParseException;
import com.asiainfo.fileservice.parser.ParseResult;
import com.asiainfo.fileservice.parser.ParseService;
import com.asiainfo.fileservice.parser.Parser;
import com.asiainfo.fileservice.parser.String2IntParseServiceImpl;


/**
 * @Description: TODO
 * 
 * @author       zq
 * @date         2017年6月4日  下午2:44:03
 * Copyright: 	  北京亚信智慧数据科技有限公司
 */
public class FileReaderWriterUtil {
	
	static final Logger logger = LoggerFactory.getLogger(FileReaderWriterUtil.class);
	
	static final byte SEPERATOR = (byte) 0x80;
	static final byte[] NEW_LINE = new byte[] {0x0d, 0x0a};
	
	public static int countRow(File file) throws Exception {
		
		int count = 0;
		RandomAccessFile raf = new RandomAccessFile(file, "r");
		FileChannel channel = raf.getChannel();
		ByteBuffer buff = ByteBuffer.allocate(10240);
		byte prefix = 0;
		//读取数据到Buffer
		while (channel.read(buff) != -1) {
			buff.flip();
			while(buff.hasRemaining()) {
				byte bt = buff.get();
				//分隔符
				if (bt == SEPERATOR) {
					prefix = bt;
				//\r
				} else if (bt == NEW_LINE[0]) {
					prefix = bt;
				//\n
				} else if (bt == NEW_LINE[1]) {
					//\r\n
					if (prefix == NEW_LINE[0]) {
						prefix = 0;
						count++;
					} else {
						prefix = bt;
					}
				//content
				} else {
					prefix = bt;
				}
			}
			buff.clear();
		}
		raf.close();
		//最后一行可能没有换行，直接是eof，也可能最后一个直接是分隔符
		if (prefix == SEPERATOR) {
			count++;
		}
		return count;
	}
	
	/**
	 * @Description: 读取指定行数的记录
	 * 
	 * @param file
	 * @param encode
	 * @param limit
	 * @throws Exception
	 */
	public static List<List<String>> read(FileChannel channel, String encode, int limit) throws Exception {
		
		List<List<String>> result = new ArrayList<List<String>>();
		ByteBuffer buff = ByteBuffer.allocate(10240);		
		List<String> line = new ArrayList<String>();
		byte[] word = new byte[10240];
		int index = 0;
		byte prefix = 0;
		//读取数据到Buffer
		while (channel.read(buff) != -1) {
			buff.flip();
			while(buff.hasRemaining()) {
				byte bt = buff.get();
				//分隔符
				if (bt == SEPERATOR) {
					line.add(index == 0 ? null : new String(word, 0, index, encode));
					word = new byte[10240];
					prefix = bt;
					index = 0;
				//\r
				} else if (bt == NEW_LINE[0]) {
					if (prefix == NEW_LINE[0]) {
						word[index++] = prefix;
					} else {
						prefix = bt;
					}
				//\n
				} else if (bt == NEW_LINE[1]) {
					//\r\n
					if (prefix == NEW_LINE[0]) {
						prefix = 0;
						line.add(index == 0 ? null : new String(word, 0, index, encode));
						result.add(line);
						line = new ArrayList<String>();
						word = new byte[10240];
						index = 0;
						//读取到指定行数，直接返回，小于等于0表示全部读取
						if (result.size() >= limit && limit > 0) {
							return result;
						}
					} else {
						word[index++] = bt;
						prefix = bt;
					}
				//content
				} else {
					word[index++] = bt;
					prefix = bt;
				}
			}
			buff.clear();
		}
		//最后一行可能没有换行，直接是eof，也可能最后一个直接是分隔符
		if (index > 0 || prefix == SEPERATOR) {
			line.add(index == 0 ? null : new String(word, 0, index, encode));
			result.add(line);
		}
		return result;
	}
	
	/**
	 * @Description: 组装BO对象列表
	 * 
	 * @param rowList
	 * @param parseList
	 * @param propertyList
	 * @param clazz
	 * @return
	 * @throws Exception
	 */
	public static <T> ParseResult<T> assembly(List<List<String>> rowList, Class<T> clazz) throws Exception {
		
		ParseResult<T> result = new ParseResult<T>();
		//获取bo的setter列表和parse列表
		List<Method> methodList = generateSetterMethod(clazz);
		List<ParseService<?>> parseList = generateParseService(methodList);
		for (int i = 0; i < rowList.size(); i++) {
			try {
				result.getResult().add(injection(rowList.get(i), methodList, parseList, clazz));
			} catch (ParseException ex) {
				int row = 0;
				try {
					row = new String2IntParseServiceImpl().parse(rowList.get(i).get(0));
				} catch (Exception e) {
					row = i + 1;
				}
				result.getError().add(new ParseError(row, ex.getColumn() < 0 ? 0 : ex.getColumn(), 
						ex.getErrorCode(), ex.getMessage()));
				logger.error("行校验时出现错误，错误信息：{}", ex);
			} catch (Exception ex) {
				result.getError().add(new ParseError(i + 1, ErrorCodes.RECORD_RESULTCODE_ERROR_TYPE, ex.getMessage()));
				logger.error("行校验时出现错误，错误信息：{}", ex);
			}
		}
		return result;
	}
	
	/**
	 * @Description: 注入1行解析后的数据到指定类型BO
	 * 
	 * @param row
	 * @param propertyList
	 * @param clazz
	 * @return
	 * @throws Exception
	 */
	public static <T> T injection(List<String> row, List<Method> methodList, List<ParseService<?>> parseList, Class<T> clazz) throws Exception {
		
		T result = clazz.newInstance();
		if (row.isEmpty()) {
			return result;
		}
		List<Object> columns = parse(row, parseList);
		for (int i = 0; i < methodList.size(); i++) {
			if (null == methodList.get(i)) {
				continue;
			}
			if (null == row.get(i)) {
				continue;
			}
			methodList.get(i).invoke(result, columns.get(i));
		}
		return result;
	}

	/**
	 * @Description: 解析一行数据
	 * 
	 * @param columns
	 * @param parseList
	 * @return
	 */
	public static List<Object> parse(List<String> columns, List<ParseService<?>> parseList) {
		
		List<Object> result = new ArrayList<Object>();
		for (int i = 0; i < parseList.size(); i++) {
			try {
				if (null == parseList.get(i)) {
					result.add(columns.get(i));
				} else {
					result.add(parseList.get(i).parse(columns.get(i)));
				}
			} catch (ParseException ex) {
				throw new ParseException(i + 1, ex.getErrorCode(), ex);
			}
		}
		return result;
	}
	
	/**
	 * 
	 * @Description: 反射获得文件列对应的bo属性set方法
	 * 
	 * @param t
	 * @return
	 */
	public static List<Method> generateSetterMethod(Class<?> t) {

		Map<Integer, Method> map = new HashMap<Integer, Method>(16);
		for (Method method : t.getDeclaredMethods()) {
			Parser parser = method.getAnnotation(Parser.class);
			if (null == parser) {
				continue;
			}
			map.put(parser.value(), method);
		}
		List<Integer> columnNoList = new ArrayList<Integer>(map.keySet());
		Collections.sort(columnNoList);
		//声明的数组长度为最大的列号（第一列1）
		Method[] methods = new Method[columnNoList.get(columnNoList.size() - 1).intValue()];
		for (Integer columnNo : columnNoList) {
			methods[columnNo.intValue() - 1] = map.get(columnNo);
		}
		return Arrays.asList(methods);
	}
	
	/**
	 * 
	 * @Description: 方法对应的parseservice列表
	 * 
	 * @param methodList
	 * @return
	 * @throws Exception
	 */
	public static List<ParseService<?>> generateParseService(List<Method> methodList) throws Exception {

		List<ParseService<?>> result = new ArrayList<ParseService<?>>();
		for (int i = 0; i < methodList.size(); i++) {
			if (null == methodList.get(i)) {
				result.add(null);
				continue;
			}
			Class<?>[] clazzArray = methodList.get(i).getAnnotation(Parser.class).clazz();
			ParseService<?> prefixService = null;
			ParseService<?> service = null;
			for (Class<?> serviceClazz : clazzArray) {
				service = (ParseService<?>) serviceClazz.newInstance();
				if (null != prefixService) {
					service.setDelegate(prefixService);
				}
				prefixService = service;
			}
			result.add(service);
		}
		return result;
	}
	
	/**
	 * 
	 * @Description: 把字符串列表写入指定的输出流中
	 * 
	 * @param channel
	 * @param rowList
	 * @param encode
	 * @param needRowId
	 * @param startIndex
	 * @throws Exception
	 */
	public static <T> void write(FileChannel channel, List<List<String>> rowList, String encode, boolean needRowId, int startIndex) throws Exception {
		
		int rowId = startIndex < 0 ? 1 : startIndex;
		for (List<String> row : rowList) {
			ByteBuffer buff = ByteBuffer.allocate(10240);
			buff.clear();
			//是否设置行号
			if (needRowId) {
				buff.put(String.valueOf(rowId++).getBytes(encode));
				buff.put(SEPERATOR);
			}
			//写一行
			for (int i = 0; i < row.size(); ) {
				if (StringUtils.isNotEmpty(row.get(i))) {
					buff.put(row.get(i).getBytes(encode));
				}
				//最后一列
				if (++i == row.size()) {
					buff.put(NEW_LINE);
				} else {
					buff.put(SEPERATOR);
				}
			}
			//把buff写入channel
			buff.flip();
			while (buff.hasRemaining()) {
				channel.write(buff);
			}
		}
		
	}

	/**
	 * 
	 * @Description: 把对象列表写入指定的输出流中
	 * 
	 * @param channel
	 * @param list
	 * @param encode
	 * @param needRowId
	 * @param startIndex
	 * @throws Exception
	 */
	public static <T> void write(FileChannel channel, List<T> list, String encode, boolean needRowId, int startIndex, Class<T> clazz) throws Exception {
		
		List<List<String>> rowList = convert(list, clazz);
		int rowId = startIndex < 0 ? 1 : startIndex;
		for (List<String> row : rowList) {
			ByteBuffer buff = ByteBuffer.allocate(10240);
			buff.clear();
			//是否设置行号
			if (needRowId) {
				buff.put(String.valueOf(rowId++).getBytes(encode));
				buff.put(SEPERATOR);
			}
			//写一行
			for (int i = 0; i < row.size(); ) {
				if (StringUtils.isNotEmpty(row.get(i))) {
					buff.put(row.get(i).getBytes(encode));
				}
				//最后一列
				if (++i == row.size()) {
					buff.put(NEW_LINE);
				} else {
					buff.put(SEPERATOR);
				}
			}
			//把buff写入channel
			buff.flip();
			while (buff.hasRemaining()) {
				channel.write(buff);
			}
		}
		
	}
	
	/**
	 * 
	 * @Description: 转换对象属性为string列表
	 * 
	 * @param list
	 * @return
	 */
	public static <T> List<List<String>> convert(List<T> list, Class<T> clazz) throws Exception {
		
		List<List<String>> result = new ArrayList<List<String>>();
		if (null == list || list.isEmpty()) {
			return result;
		}
		//获得属性getter方法
		List<Method> methodList = generateGetterMethod(clazz);
		List<FormatService> formatList = generateFormatService(methodList);
		//反射对象属性，按顺序号、格式化实现类，返回一行string列数据，null转换为""
		for (T t : list) {
			result.add(convertBO2String(t, methodList, formatList));
		}
		return result;
	}
	
	/**
	 * 
	 * @Description: 转换一个bo成为文件列字符串
	 * 
	 * @param t
	 * @param methodList
	 * @param formatList
	 * @return
	 * @throws Exception
	 */
	public static <T> List<String> convertBO2String(T t, List<Method> methodList, List<FormatService> formatList) throws Exception {
		
		List<String> result = new ArrayList<String>();
		for (int i = 0; i < methodList.size(); i++) {
			result.add(formatList.get(i).format(methodList.get(i).invoke(t, new Object[0])));
		}
		return result;
	}
	
	/**
	 * 
	 * @Description: 反射获得文件列对应的bo属性get方法
	 * 
	 * @param t
	 * @return
	 */
	public static List<Method> generateGetterMethod(Class<?> t) {

		List<Method> result = new ArrayList<Method>();
		Map<Integer, Method> map = new HashMap<Integer, Method>(16);
		for (Method method : t.getDeclaredMethods()) {
			Formatter formatter = method.getAnnotation(Formatter.class);
			if (null == method.getAnnotation(Formatter.class)) {
				continue;
			}
			map.put(formatter.value(), method);
		}
		List<Integer> columnNoList = new ArrayList<Integer>(map.keySet());
		Collections.sort(columnNoList);
		for (Integer columnNo : columnNoList) {
			result.add(map.get(columnNo));
		}
		return result;
	}
	
	/**
	 * 
	 * @Description: 方法对应的formatservice列表
	 * 
	 * @param methodList
	 * @return
	 * @throws Exception
	 */
	public static List<FormatService> generateFormatService(List<Method> methodList) throws Exception {

		List<FormatService> result = new ArrayList<FormatService>();
		for (Method method : methodList) {
			FormatService service = (FormatService) method.getAnnotation(Formatter.class).clazz().newInstance();
			result.add(service);
		}
		return result;
	}
}
