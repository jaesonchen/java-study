package com.asiainfo.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Description: TODO
 * 
 * @author       zq
 * @date         2017年5月24日  下午2:33:10
 * Copyright: 	  北京亚信智慧数据科技有限公司
 */
public class ValidatorUtil {

	public static boolean isEmpty(String str) {
		return (null == str || "".equals(str));
	}
	
	public static boolean isEmptyIgnoreBlank(String str) {
		return (null == str || "".equals(str.trim()));
	}
	
	public static boolean isNotEmpty(String str) {
		return !isEmpty(str);
	}
	
	public static boolean isNotEmptyIgnoreBlank(String str) {
        return !isEmptyIgnoreBlank(str);
    }
	
	/** 
     * 判断是否为浮点数或者整数 
     * @param str 
     * @return true Or false 
     */
	public static boolean isNumeric(String str) {
		if (isEmpty(str)) {
			return false;
		}
		return Pattern.matches("^(-?\\d+)(\\.\\d+)?$", str);
	}
	
    /** 
     * 判断是否为数字 
     * @param str 
     * @return 
     */
    public static boolean isNumber(String str) {
    	if (isEmpty(str)) {
			return false;
		}
    	return Pattern.matches("\\-?[1-9]\\d*", str);
    }
	
    /** 
     * 验证整数（正整数和负整数） 
     * @param digit 一位或多位0-9之间的整数 
     * @return 验证成功返回true，验证失败返回false 
     */
    public static boolean checkDigit(String digit) {
        String regex = "\\-?[1-9]\\d+";
        return Pattern.matches(regex,digit);
    }
    
    /** 
     * 判断字符串是否为合法手机号 11位 13 14 15 18开头 
     * @param phone 
     * @return boolean 
     */
    public static boolean isPhoneNo(String phone) {
        if (isEmpty(phone)) {
            return false;
        }
        return phone.matches("^(13|14|15|18)\\d{9}$");
    }
    
    /** 
     * 验证手机号码（支持国际格式，+86135xxxx...（中国内地），+00852137xxxx...（中国香港）） 
     * @param mobile 移动、联通、电信运营商的号码段 
     *<p>移动的号段：134(0-8)、135、136、137、138、139、147（预计用于TD上网卡） 
     *、150、151、152、157（TD专用）、158、159、187（未启用）、188（TD专用）</p> 
     *<p>联通的号段：130、131、132、155、156（世界风专用）、185（未启用）、186（3g）</p> 
     *<p>电信的号段：133、153、180（未启用）、189</p> 
     * @return 验证成功返回true，验证失败返回false 
     */   
    public static boolean isMobile(String mobile) {
        String regex = "(\\+\\d+)?1[3458]\\d{9}$";
        return Pattern.matches(regex,mobile);
    }
    
    /** 
     * 验证固定电话号码 
     * @param phone 电话号码，格式：国家（地区）电话代码 + 区号（城市代码） + 电话号码，如：+8602085588447 
     * <p><b>国家（地区） 代码 ：</b>标识电话号码的国家（地区）的标准国家（地区）代码。它包含从 0 到 9 的一位或多位数字， 
     *  数字之后是空格分隔的国家（地区）代码。</p> 
     * <p><b>区号（城市代码）：</b>这可能包含一个或多个从 0 到 9 的数字，地区或城市代码放在圆括号—— 
     * 对不使用地区或城市代码的国家（地区），则省略该组件。</p> 
     * <p><b>电话号码：</b>这包含从 0 到 9 的一个或多个数字 </p> 
     * @return 验证成功返回true，验证失败返回false 
     */   
    public static boolean checkPhone(String phone) {
        String regex = "(\\+\\d+)?(\\d{3,4}\\-?)?\\d{7,8}$";
        return Pattern.matches(regex, phone);
    }
    
    /** 
     * 验证Email 
     * @param email email地址，格式：aaa@asiainfo.com，aa@xxx.com.cn，xxx代表邮件服务商 
     * @return 验证成功返回true，验证失败返回false 
     */
    public static boolean checkEmail(String email) {
        String regex = "\\w+@\\w+\\.[a-z]+(\\.[a-z]+)?";
        return Pattern.matches(regex, email);
    }
    
    /** 
     * 验证身份证号码 
     * @param idCard 居民身份证号码15位或18位，最后一位可能是数字或字母 
     * @return 验证成功返回true，验证失败返回false 
     */
    public static boolean checkIdCard(String idCard) {
        String regex = "[1-9]\\d{13,16}[a-zA-Z0-9]{1}";
        return Pattern.matches(regex, idCard);
    }
    
    /** 
     * 验证中文 
     * @param chinese 中文字符 
     * @return 验证成功返回true，验证失败返回false 
     */
    public static boolean checkChinese(String chinese) {
        String regex = "^[\u4E00-\u9FA5]+$";
        return Pattern.matches(regex,chinese);
    }
    
    /** 
     * 验证URL地址 
     * @param url 格式：http://blog.csdn.net:80/xyang81/article/details/7705960? 或 http://www.csdn.net:80 
     * @return 验证成功返回true，验证失败返回false 
     */
    public static boolean checkURL(String url) {
        String regex = "(https?://(w{3}\\.)?)?\\w+\\.\\w+(\\.[a-zA-Z]+)*(:\\d{1,5})?(/\\w*)*(\\??(.+=.*)?(&.+=.*)?)?";
        return Pattern.matches(regex, url);
    }
    
    /** 
     * <pre> 
     * 获取网址 URL 的一级域名 
     * http://www.zuidaima.com/share/1550463379442688.htm ->> zuidaima.com 
     * </pre> 
     *  
     * @param url 
     * @return 
     */
    public static String getDomain(String url) {
        Pattern p = Pattern.compile("(?<=http://|\\.)[^.]*?\\.(com|cn|net|org|biz|info|cc|tv)", Pattern.CASE_INSENSITIVE);
        // 获取完整的域名 
        // Pattern p=Pattern.compile("[^//]*?\\.(com|cn|net|org|biz|info|cc|tv)", Pattern.CASE_INSENSITIVE);
        Matcher matcher = p.matcher(url);
        matcher.find();
        return matcher.group();
    }
    
    /** 
     * 匹配IP地址(简单匹配，格式，如：192.168.1.1，127.0.0.1，没有匹配IP段的大小) 
     * @param ipAddress IPv4标准地址 
     * @return 验证成功返回true，验证失败返回false 
     */
    public static boolean checkIpAddress(String ipAddress) {
        String regex = "[1-9](\\d{1,2})?\\.(0|([1-9](\\d{1,2})?))\\.(0|([1-9](\\d{1,2})?))\\.(0|([1-9](\\d{1,2})?))";
        return Pattern.matches(regex, ipAddress);
    }
}
