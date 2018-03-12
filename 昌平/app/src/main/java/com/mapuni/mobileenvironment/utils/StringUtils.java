package com.mapuni.mobileenvironment.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.regex.Pattern;

/**
 * 字符串操作工具包
 * 
 * @author liux (http://my.oschina.net/liux)
 * @version 1.0
 * @created 2012-3-21
 */
public class StringUtils {
	private final static Pattern emailer = Pattern.compile("\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*");

	/**
	 * 判断给定字符串是否空白串。 空白串是指由空格、制表符、回车符、换行符组成的字符串 若输入字符串为null或空字符串，返回true
	 * 
	 * @param input
	 * @return boolean
	 */
	public static boolean isEmpty(String input) {
		if (input == null || "".equals(input) || "null".equals(input))
			return true;

		for (int i = 0; i < input.length(); i++) {
			char c = input.charAt(i);
			if (c != ' ' && c != '\t' && c != '\r' && c != '\n') {
				return false;
			}
		}
		return true;
	}

	/**
	 * 判断是不是一个合法的电子邮件地址
	 * 
	 * @param email
	 * @return
	 */
	public static boolean isEmail(String email) {
		if (email == null || email.trim().length() == 0)
			return false;
		return emailer.matcher(email).matches();
	}

	/**
	 * 字符串转整数
	 * 
	 * @param str
	 * @param defValue
	 * @return
	 */
	public static int toInt(String str, int defValue) {
		try {
			return Integer.parseInt(str);
		} catch (Exception e) {
		}
		return defValue;
	}

	/**
	 * 对象转整数
	 * 
	 * @param obj
	 * @return 转换异常返回 0
	 */
	public static int toInt(Object obj) {
		if (obj == null)
			return 0;
		return toInt(obj.toString(), 0);
	}

	/**
	 * 对象转整数
	 * 
	 * @param obj
	 * @return 转换异常返回 0
	 */
	public static long toLong(String obj) {
		try {
			return Long.parseLong(obj);
		} catch (Exception e) {
		}
		return 0;
	}

	/**
	 * 字符串转布尔值
	 * 
	 * @param b
	 * @return 转换异常返回 false
	 */
	public static boolean toBool(String b) {
		try {
			return Boolean.parseBoolean(b);
		} catch (Exception e) {
		}
		return false;
	}

    /**
     * @Description：转化日期为字符串 根据格式pattern
     * @param date 日期
     * @param pattern 转化为字符串的格式
     * @return 日期输入错误则返回null
     *
     * @author：刘少凯 liusk@mapuni.com
     * @create at：2014-12-10 上午11:57:30
     * @modifier：
     * @modify at：
     */
    public static String FormatDateToStr(Date date, String pattern){
        if(date == null||pattern == null)
            return null;
        SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
        return dateFormat.format(date);
    }
    /**
     * @Description：转化日期为字符串 默认格式为 yyyy-MM-dd HH:mm:ss
     * @param date 日期
     * @return 转化后的字符串 日期输入错误 返回null
     *
     * @author：刘少凯 liusk@mapuni.com
     * @create at：2014-12-10 上午11:56:18
     * @modifier：
     * @modify at：
     */
    public static String FormatDateToStr(Date date){
        return FormatDateToStr(date, "yyyy-MM-dd HH:mm:ss");
    }

    /**
     * 将从后台获取的不规范的字符串 转换为规范的字符串
     * @param timeStr 输入字符串
     * @return 规范后的字符串
     */
    public static String FormatStrToStr(String timeStr) {
        StringBuilder strObj = new StringBuilder();
        if (timeStr.contains(" ")) {
            String strs[] = timeStr.split(" ");
            if (strs != null && strs.length > 1) {
                if (strs[0].contains("/")) {
                    String str1[] = strs[0].split("/");
                    if (str1.length == 3) {
                        strObj.append(str1[0]);
                        if (str1[1].length() == 1) {
                            strObj.append("-0");
                        }else{
                            strObj.append("-");
                        }
                        strObj.append(str1[1]);
                        if (str1[2].length() == 1) {
                            strObj.append("-0");
                        }else {
                            strObj.append("-");
                        }
                        strObj.append(str1[2]);
                    }
                }else{
                    strObj.append(strs[0]);
                }

                strObj.append(" ");
                if(strs[1].contains(":")){
                    String[] str2 = strs[1].split(":");
                    if(str2 != null && str2.length == 3){
                        if(str2[0].length()==1){
                            strObj.append("0");
                        }
                        strObj.append(str2[0]);
                        if(str2[1].length()==1){
                            strObj.append(":0");
                        }else{
                            strObj.append(":");
                        }
                        strObj.append(str2[1]);
                        if(str2[2].length()==1){
                            strObj.append(":0");
                        }else{
                            strObj.append(":");
                        }
                        strObj.append(str2[2]);
                    }
                }else{
                    strObj.append(strs[1]);
                }
                if(strs.length == 3){
                    strObj.append(" ");
                    strObj.append(strs[2]);
                }
                return strObj.toString();
            }
        }
        return timeStr;
    }

	public static HashMap<String,Object> formatData(HashMap<String,Object> map){
		if(map.containsKey("DataTime"))
			map.put("DataTime", StringUtils.FormatStrToStr(map.get("DataTime").toString()));
		if(map.containsKey("AlarmTime"))
			map.put("AlarmTime", StringUtils.FormatStrToStr(map.get("AlarmTime").toString()));
		if(map.containsKey("monitoringTime"))
			map.put("monitoringTime", StringUtils.FormatStrToStr(map.get("monitoringTime").toString()));
		if(map.containsKey("StartTime"))
			map.put("StartTime", StringUtils.FormatStrToStr(map.get("StartTime").toString()));
		if(map.containsKey("EndTime"))
			map.put("EndTime", StringUtils.FormatStrToStr(map.get("EndTime").toString()));
		if(map.containsKey("DateTime"))
			map.put("DateTime", StringUtils.FormatStrToStr(map.get("DateTime").toString()));
		if(map.containsKey("UploadataTime"))
			map.put("UploadataTime", StringUtils.FormatStrToStr(map.get("UploadataTime").toString()));
		if(map.containsKey("Uploadatatime"))
			map.put("Uploadatatime", StringUtils.FormatStrToStr(map.get("Uploadatatime").toString()));
		if(map.containsKey("UploadDataTime"))
			map.put("UploadDataTime", StringUtils.FormatStrToStr(map.get("UploadDataTime").toString()));
		//后台返回的字段中多了一个rownumber 在此处做处理
		if(map!=null&&map.size()>0&&map.containsKey("Rownumber"))
			map.remove("Rownumber");
		return map;
	}
	public static String toDecode(String str){
		String s = "";
		try {
			str = URLDecoder.decode(str, "gbk");
		}
		catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		s= str;
		return  s;
	}

}
