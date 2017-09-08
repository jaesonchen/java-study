package com.asiainfo.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @Description: TODO
 * 
 * @author       zq
 * @date         2017年3月27日  下午5:47:11
 * Copyright: 	  北京亚信智慧数据科技有限公司
 */
@Component
public class DatabaseUtil {

	public static final String ORACLE = "ORACLE";
	public static final String MYSQL = "MYSQL";
	public static final String DB2 = "DB2";
	
	@Value("${env.mcd.db.type}")
	public static String dbType = ORACLE;
	public void setDbType(String db) {
		dbType = db;
	}
	
	/**
	 * @Description: 转换长时间格式为短日期格式
	 * 
	 * @param column
	 * @return
	 */
	public static String getDate(String column) {
		
		if (ORACLE.equalsIgnoreCase(dbType)) {
			return "TRUNC(" + column + ")";
		} else if (MYSQL.equalsIgnoreCase(dbType)) {
			return "DATE_FORMAT(" + column + ", '%Y-%m-%d')";
		}
		//默认oracle
		return "TRUNC(" + column + ")";
	}
	
	/**
	 * @Description: 表是否存在
	 * 
	 * @param table
	 * @return
	 */
	public static String getTableExist(String table) {
		
		if (ORACLE.equalsIgnoreCase(dbType)) {
			return "select count(*) from all_tables where table_name = '" + (StringUtils.isEmpty(table) ? "" : table.toUpperCase()) + "'";
		} else if (MYSQL.equalsIgnoreCase(dbType)) {
			// TABLE_SCHEMA='FQC' and
			return "select count(*) from INFORMATION_SCHEMA.TABLES where TABLE_NAME='" 
					+ (StringUtils.isEmpty(table) ? "" : table.toUpperCase()) + "'";
		}
		//默认oracle
		return "select count(*) from all_tables where table_name = '" + table.toUpperCase() + "'";
	}
	
	/**
	 * 
	 * @Description: 查询所有schema下是否存在表
	 * 
	 * @param table
	 * @return
	 */
	public static String getAllSchemaTableExist(String table) {
		
		if (ORACLE.equalsIgnoreCase(dbType)) {
			return "select count(*) from all_tables where table_name = '" + (StringUtils.isEmpty(table) ? "" : table.toUpperCase()) + "'";
		} else if (MYSQL.equalsIgnoreCase(dbType)) {
			return "select count(*) from INFORMATION_SCHEMA.TABLES where TABLE_NAME='" 
					+ (StringUtils.isEmpty(table) ? "" : table.toUpperCase()) + "'";
		}
		//默认oracle
		return "select count(*) from all_tables where table_name = '" + table.toUpperCase() + "'";
	}
	
	/**
	 * 
	 * @Description: 查询表分区名称，带占位符
	 * 
	 * @return
	 */
	public static String getTablePartitionWithPlaceholder() {
		
		if (ORACLE.equalsIgnoreCase(dbType)) {
			return "SELECT partition_name FROM user_tab_partitions WHERE table_name=? and partition_name=?";
		} else if (MYSQL.equalsIgnoreCase(dbType)) {
			return "SELECT partition_name FROM INFORMATION_SCHEMA.partitions WHERE TABLE_SCHEMA=schema() AND TABLE_NAME=? and partition_name=?";
		}
		//默认oracle
		return "SELECT partition_name FROM user_tab_partitions WHERE table_name=? and partition_name=?";
	}
	
	/**
	 * 
	 * @Description: 查询表分区名称
	 * 
	 * @param table
	 * @param partition
	 * @param keyPartition		mysql版本是否使用了key分区
	 * @return
	 */
	public static String getTablePartition(String table, String partition, boolean keyPartition) {
		
		if (ORACLE.equalsIgnoreCase(dbType)) {
			return "SELECT partition_name FROM user_tab_partitions WHERE table_name='" + table + "' and partition_name='" + partition + "'";
		} else if (MYSQL.equalsIgnoreCase(dbType)) {
			if (keyPartition) {
				return "SELECT partition_name FROM INFORMATION_SCHEMA.partitions WHERE TABLE_SCHEMA=schema() AND TABLE_NAME='" + table + "'";
			} else {
				return "SELECT partition_name FROM INFORMATION_SCHEMA.partitions WHERE TABLE_SCHEMA=schema() AND TABLE_NAME='" + table + "' and partition_name='" + partition + "'";
			}
		}
		//默认oracle
		return "SELECT partition_name FROM user_tab_partitions WHERE table_name='" + table + "' and partition_name='" + partition + "'";
	}
	
	/**
	 * 
	 * @Description: 拆分分区
	 * 
	 * @param table
	 * @param originPartition
	 * @param toPartition
	 * @param splitValue
	 * @param keyPartition		mysql版本是否使用了key分区
	 * @return
	 */
	public static String getTablePartitionSplit(String table, String originPartition, String toPartition, String splitValue, boolean keyPartition) {
		
		if (ORACLE.equalsIgnoreCase(dbType)) {
			return "alter table " + table + " split partition " + originPartition + " at ('" + splitValue + "') into (partition " + toPartition + ", partition p_max)";
		} else if (MYSQL.equalsIgnoreCase(dbType)) {
			//mysql必须转换为int类型分区，5.5以后支持date分区
			if (keyPartition) {
				return "ALTER TABLE " + table + " add PARTITION partitions 1";
			} else {
				return "alter table " + table + " reorganize partition " + originPartition 
						+ " into (partition " + toPartition + " values less than ('" + splitValue + "'), "
						+ " partition  " + originPartition + " values less than (maxvalue))";
			}
		}
		//默认oracle
		return "alter table " + table + " split partition " + originPartition + " at ('" + splitValue + "') into (partition " + toPartition + ", partition p_max)";
	}
	
	/**
	 * 
	 * @Description: 分区创建语句
	 * 
	 * @param column
	 * @param createDefault
	 * @param partitionArr
	 * @return
	 */
	public static String getTablePartitionCreate(String column, boolean createDefault, String[] partitionArr) {
		
		StringBuilder result = new StringBuilder();
		if (ORACLE.equalsIgnoreCase(dbType)) {
			result.append(" partition by range (" + column + ") ( ");
			for (String partition : partitionArr) {
				result.append(" partition p_" + partition + " values less than ('" + partition + "'),");
			}
			if (createDefault) {
				result.append(" partition p_max values less than (maxvalue))");
			} else {
				result.replace(result.length() - 1, result.length(), ")");
			}
		} else if (MYSQL.equalsIgnoreCase(dbType)) {
			//mysql必须转换为int类型分区，5.5以后支持date分区，为了兼容低版本string转换为int
			/*result.append(" partition by range (cast(" + column + " as unsigned)) ( ");
			for (String partition : partitionArr) {
				result.append(" partition p_" + partition + " values less than (" + Long.parseLong(partition) + "),");
			}
			if (createDefault) {
				result.append(" partition p_max values less than maxvalue)");
			} else {
				result.replace(result.length() - 1, result.length(), ")");
			}*/
			//mysql改用key分区，默认3个分区
			result.append(" partition by key (" + column + ") partitions ").append(partitionArr.length < 3 ? 3 : partitionArr.length);
		}
		return result.toString();
	}
	
	/**
	 * 
	 * @Description: 基于date类型的自动分区
	 * 
	 * @param column
	 * @param interval
	 * @return
	 */
	public static String getTableDatetimeAutoPartitionCreate(String column, String interval) {
		
		StringBuilder result = new StringBuilder();
		if (ORACLE.equalsIgnoreCase(dbType)) {
			result.append(" partition by range (" + column + ") interval(numtodsinterval(1,'" + interval + "')) ( ")
				.append(" partition part_t01 values less than (to_date('").append(new SimpleDateFormat("yyyy-MM-dd").format(new Date()))
				.append("', 'yyyy-mm-dd')))");
		} else if (MYSQL.equalsIgnoreCase(dbType)) {
			//mysql改用key分区，默认31个分区
			result.append(" partition by key (" + column + ") partitions ").append(31);
		}
		return result.toString();
	}
	
	/**
	 * @Description: TODO
	 * 
	 * @param column
	 * @return
	 */
	public static String getSqlEqualNullEmptyString(String column) {
		
		StringBuilder result = new StringBuilder();
		if (ORACLE.equalsIgnoreCase(dbType)) {
			result.append(column).append(" is null");
		} else if (MYSQL.equalsIgnoreCase(dbType)) {
			result.append("(").append(column).append(" is null or ").append(column).append("='')");
		}
		return result.toString();
	}

	/**
	 * @Description: TODO
	 * 
	 * @param column
	 * @return
	 */
	public static String getSqlNotEqualNullEmptyString(String column) {
		
		StringBuilder result = new StringBuilder();
		if (ORACLE.equalsIgnoreCase(dbType)) {
			result.append(column).append(" is not null");
		} else if (MYSQL.equalsIgnoreCase(dbType)) {
			result.append("(").append(column).append(" is not null and ").append(column).append("<>'')");
		}
		return result.toString();
	}

	/**
	 * 
	 * @Description: TODO
	 * 
	 * @param isTable
	 * @param table
	 * @param resultSet
	 * @param tableAlias
	 * @param groupBy
	 * @param orderBy
	 * @param asc
	 * @param rankAlias
	 * @return
	 */
	public static String getRowNumberPartitionBySql(boolean isTable, String table, String resultSet, String tableAlias, String groupBy, String orderBy, String asc, String rankAlias) {
		
		StringBuilder result = new StringBuilder();
		if (ORACLE.equalsIgnoreCase(dbType)) {
			result.append("SELECT ROW_NUMBER() OVER(PARTITION BY ").append(tableAlias).append(".").append(groupBy).append(" ORDER BY ")
				.append(tableAlias).append(".").append(orderBy).append(") ").append(rankAlias).append(", ").append(tableAlias).append(".* FROM ");
			if (isTable) {
				result.append(table).append(" ").append(tableAlias);
			} else {
				result.append("(").append(resultSet).append(") ").append(tableAlias);
			}
		} else if (MYSQL.equalsIgnoreCase(dbType)) {
			result.append("select if(@groupby=mysql_rn_1.").append(groupBy).append(", @row_rank:=@row_rank+1, @row_rank:=1) as ").append(rankAlias).append(", ")
				.append("mysql_rn_1.*, ")
				.append("@groupby:=mysql_rn_1.").append(groupBy).append(" from (select * from ");
			if (isTable) {
				result.append(table).append(" ").append(tableAlias).append(" order by ").append(tableAlias).append(".").append(groupBy).append(" ").append(asc)
					.append(", ").append(tableAlias).append(".").append(orderBy).append(" ").append(asc).append(") mysql_rn_1, ")
					.append("(select @row_rank:=0 , @groupby:=null) mysql_rn_2");
			} else {
				result.append("(").append(resultSet).append(") ").append(tableAlias).append(" order by ").append(tableAlias).append(".").append(groupBy).append(" ").append(asc)
					.append(", ").append(tableAlias).append(".").append(orderBy).append(" ").append(asc).append(") mysql_rn_1, ")
					.append("(select @row_rank:=0 , @groupby:=null) mysql_rn_2");
			}
		}
		return result.toString();
	}
	
	/**
	 * @Description: 取拼接列的数据库函数实现，默认oracle的
	 * 
	 * @param column
	 * @return
	 */
	public static String getGroupConcat(String column) {
		
		if (ORACLE.equalsIgnoreCase(dbType)) {
			return "wm_concat(" + column + ")";
		} else if (MYSQL.equalsIgnoreCase(dbType)) {
			return "GROUP_CONCAT(" + column + ")";
		}
		//默认oracle
		return "wmsys.wm_concat(" + column + ")";
	}
	
	/**
	 * @Description: 取字符串连接的实现
	 * 
	 * @param column 连接的参数中如果有不是列名的字符串，请把单引号带上('str')
	 * @return
	 */
	public static String getConcat(String[] columns) {
		
		StringBuilder result = new StringBuilder();
		if (MYSQL.equalsIgnoreCase(dbType)) {
			result.append("CONCAT(");
			for (int i = 0; i < columns.length; i++) {
				if (i > 0) {
					result.append(", ");
				}
				result.append(columns[i]);
			}
			result.append(")");
			return result.toString();
		}
		//默认oracle
		for (int i = 0; i < columns.length; i++) {
			if (i > 0) {
				result.append(" || ");
			}
			result.append(columns[i]);
		}
		return result.toString();
	}
	
	/**
	 * 
	 * @Description: 数字格式化为字符串，保留小数点后几位，四舍五入
	 * 
	 * @param column 要格式化的列
	 * @param format oracle格式参数，该参数不需要带单引号
	 * @param digit  mysql格式参数
	 * @return
	 */
	public static String numberFormat(String column, String format, int digit) {
		
		if (ORACLE.equalsIgnoreCase(dbType)) {
			return "to_char(" + column + ", '" + format + "')";
		} else if (MYSQL.equalsIgnoreCase(dbType)) {
			return "FORMAT(" + column + ", " + digit + ")";
		}
		//默认oracle
		return "to_char(" + column + ", '" + format + "')";
	}
	
	/**
	 * 
	 * @Description: 日期格式化成字符串yyyy-MM-dd
	 * 
	 * @param column
	 * @return
	 */
	public static String dateFormatToStr(String column) {
		
		if (DB2.equalsIgnoreCase(dbType)) {
	         return "ts_fmt(" + column + ",'yyyy-mm-dd')";
	     } else if (ORACLE.equalsIgnoreCase(dbType)) {
	         return "to_char(" + column + ",'yyyy-MM-dd')";
	     }else if(MYSQL.equalsIgnoreCase(dbType)){
			 return "date_format(" + column + ",'%Y-%m-%d')";
	     }
		//默认oracle
		return "to_char(" + column + ",'YYYY-mm-dd')";
	}
	
	/**
	 * 获取当前时间
	 * 默认DbType为MYSQL
	 * @return
	 */
	 public static String getCurrentDate(){
		 //
	     if (DB2.equalsIgnoreCase(dbType)) {
	         return "current timestamp";
	     } else if (ORACLE.equalsIgnoreCase(dbType)) {
	         return "sysdate";
	     }else if(MYSQL.equalsIgnoreCase(dbType)){
			 return "now()";
	     }else{
	         return "now()";
	     }
	 }

	 /**
	  * 取得各种数据库子串的函数。
	  * 
	  * @param strColName
	  *            字段名称
	  * @param pos
	  *            起始位置
	  * @param len
	  *            子串长度
	  * @return 函数
	  * @throws RuntimeException
	  *             不支持此类DBMS
	*/
	public static String getSubString(String strColName, int pos, int len) throws RuntimeException {

		String strRet = "";
		if (MYSQL.equalsIgnoreCase(dbType)) {
			if (len == -1) {
				strRet = "substring(" + strColName + "," + pos + ")";
			} else {
				strRet = "substring(" + strColName + "," + pos + "," + len + ")";
			}
		} else if (ORACLE.equalsIgnoreCase(dbType) || DB2.equalsIgnoreCase(dbType)) {
			if (len == -1) {
				strRet = "substr(" + strColName + "," + pos + ")";
			} else {
				strRet = "substr(" + strColName + "," + pos + "," + len + ")";
			}
		} else {
			throw new RuntimeException("不能取得函数定义");
		}
		return strRet;
	}

	/**
	 * 取得各种数据库子串的函数。
	 * 
	 * @param strColName
	 *            字段名称
	 * @param pos
	 *            起始位置
	 * @return 函数
	 * @throws RuntimeException
	 *             不支持此类DBMS
	 */
	public static String getSubString(String strColName, int pos) throws RuntimeException {
		return getSubString(strColName, pos, -1);
	}
	
	/**
	 * @Description: decode函数sql
	 * 
	 * @param column	列名
	 * @param condition 条件值
	 * @param value1	条件为true时的值
	 * @param value2	条件为false时的值
	 * @return
	 */
	public static String getDecode(String column, String condition, String value1, String value2) {
		
		if (ORACLE.equalsIgnoreCase(dbType)) {
			return "DECODE(" + column + ", " + condition + ", " + value1 + ", " + value2 + ")";
		} else if (MYSQL.equalsIgnoreCase(dbType)) {
			return "IF(" + column + "=" + condition + ", " + value1 + ", " + value2 + ")";
		}
		//默认oracle
		return "DECODE(" + column + ", " + condition + ", " + value1 + ", " + value2 + ")";
	}
	
	/**
	  * 根据（数据库类型和）字符串长度获取字符串字段的声明字符串
	  * @param length
	  * @return
	  */
	 public static String getStrColumnDataType(long length){
		if (ORACLE.equalsIgnoreCase(dbType)) {
	         return "VARCHAR2(" + length + ")";
	     }else if(MYSQL.equalsIgnoreCase(dbType) || DB2.equalsIgnoreCase(dbType)){
	    	 return "VARCHAR(" + length + ")";
	     }else{
	         return "VARCHAR(" + length + ")";
	     }
	 }
	 
	 /**
	  * 根据（数据库类型和）数字长度获取数字字段的声明字符串
	  * @param length
	  * @return
	  */
	 public static String getIntColumnDataType(long length){
		 if (ORACLE.equalsIgnoreCase(dbType)) {
	         return "NUMBER(" + length + ")";
	     }else if(MYSQL.equalsIgnoreCase(dbType) || DB2.equalsIgnoreCase(dbType)){
	    	 return "INT";
	     }else{
	         return "INT";
	     }
	 }
	 
	 
	 public static String nologging(){
		 if (ORACLE.equalsIgnoreCase(dbType)) {
	         return "NOLOGGING";
	     }else if(MYSQL.equalsIgnoreCase(dbType) || DB2.equalsIgnoreCase(dbType)){
	    	 return "";
	     }else{
	         return "";
	     }
	 }
	 
	 /**
	  * 
	  * @Description: 解析字符串为date对象，format为空时，分别parse "yyyy-MM-dd hh:mm:ss" 和 "yyyy-MM-dd"
	  * 
	  * @param dateStr
	  * @param format
	  * @return
	 * @throws ParseException 
	  */
	 public static java.util.Date parseDate(String dateStr, String format) {
		 
		 if (StringUtils.isEmpty(dateStr)) {
			 return null;
		 }
		 
		 //格式为空时，尝试格式化"yyyy-MM-dd hh:mm:ss" 和 "yyyy-MM-dd"
		 Pattern pTime1 = Pattern.compile("^\\d{4}-\\d{1,2}-\\d{1,2}\\s+\\d{1,2}:\\d{1,2}:\\d{1,2}");
		 Pattern pTime2 = Pattern.compile("^\\d{4}/\\d{1,2}/\\d{1,2}\\s+\\d{1,2}:\\d{1,2}:\\d{1,2}");
		 Pattern pDate1 = Pattern.compile("^\\d{4}-\\d{1,2}-\\d{1,2}");
		 Pattern pDate2 = Pattern.compile("^\\d{4}/\\d{1,2}/\\d{1,2}");
		 try {
			 if (StringUtils.isNotEmpty(format)) {
				 return new SimpleDateFormat(format).parse(dateStr);
			 }
			 
			 if (pTime1.matcher(dateStr).find()) {
				 return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(dateStr);
			 } else if (pTime2.matcher(dateStr).find()) {
				 return new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").parse(dateStr);
			 } else if (pDate1.matcher(dateStr).find()) {
				 return new SimpleDateFormat("yyyy-MM-dd").parse(dateStr);
			 } else if (pDate2.matcher(dateStr).find()) {
				 return new SimpleDateFormat("yyyy/MM/dd").parse(dateStr);
			 }
		 } catch (Exception ex) {
			 ex.printStackTrace();
			 return null;
		 }
		 return null;
	 }
	 
	 /**
	  * 
	  * @Description: 转换成支持oracle的rownum属性
	  * 
	  * @param table
	  * @param alias
	  * @return
	  */
	 public static String convert2RownumTable(String table) {
		 
		 StringBuilder result = new StringBuilder();
		 if (MYSQL.equalsIgnoreCase(dbType)) {
			 result.append("(SELECT  @rownum := @rownum+1 AS rownum,").append(table).append(".* ")
			 		.append("FROM (SELECT @rownum:=0) rownum_alias, ").append(table).append(") ");
	     } else {
	         return table;
	     }
		 return result.toString();
	 }
	 
	 public static void main(String[] args){
		 //System.out.println(parseDate("2017/03/30 10:51:01", null));
		 //setDataBaseType("mysql");
		 //System.out.println(convert2RownumTable("mcd_phone_userid"));
		 //System.out.println(getTablePartitionSplit("mcd_phone_userid", "p_max", "p_201708", "201708", true));
		 //System.out.println(getTablePartitionCreate("DATA_DATE", true, new String[] {"201708"}));
		 System.out.println(getSqlEqualNullEmptyString("CC.CEP_EVENT_ID"));
		 System.out.println(getSqlNotEqualNullEmptyString("CC.CEP_EVENT_ID"));
	 }
}
