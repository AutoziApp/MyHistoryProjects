package cn.com.mapuni.meshing.base.util;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.net.ParseException;

/**
 * 时间格式转换
 * 
 * @author yangjunying 2012/4/15
 * 
 */
public class DateTimeHelper {

	private static final  String dateFormat="yyyy-MM-dd";

	/*
	* 获取当前日期
	* */
	public static String getNowDate(){
		SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);
		return formatter.format(new Date());
	}


	/**
	 * 将未指定格式的字符串转换成日期类型
	 * 
	 * @param date
	 * @return
	 * @throws ParseException
	 */
	public static Date parseStringToDate(String date) throws ParseException {
		Date result = null;
		String parse = date;
		parse = parse.replaceFirst("^[0-9]{4}([^0-9]?)", "yyyy$1");
		parse = parse.replaceFirst("^[0-9]{2}([^0-9]?)", "yy$1");
		parse = parse.replaceFirst("([^0-9]?)[0-9]{1,2}([^0-9]?)", "$1MM$2");
		parse = parse.replaceFirst("([^0-9]?)[0-9]{1,2}( ?)", "$1dd$2");
		parse = parse.replaceFirst("( )[0-9]{1,2}([^0-9]?)", "$1HH$2");
		parse = parse.replaceFirst("([^0-9]?)[0-9]{1,2}([^0-9]?)", "$1mm$2");
		parse = parse.replaceFirst("([^0-9]?)[0-9]{1,2}([^0-9]?)", "$1ss$2");

		SimpleDateFormat format = new SimpleDateFormat(parse);
		try {
			result = format.parse(date);
		} catch (java.text.ParseException e) {
			// TODO Auto-generated catch block
			System.out.println("时间格式转换错误");
			e.printStackTrace();
		}

		return result;
	}

	/**
	 * 将日期以指定格式输出
	 * 
	 * @param date
	 * @param format
	 *            格式 "yyyy-MM-dd hh:mm:ss"
	 * @return String
	 */
	public static String formatToString(Date date, String format) {
		try {
			java.text.SimpleDateFormat sdf = new SimpleDateFormat(format);
			return sdf.format(date);
		} catch (Exception e) {
			System.out.println("时间格式转换错误");
			e.printStackTrace();
		}
		return "";
	}

	/**
	 * 将日期格式的字符串以指定格式输出
	 * 
	 * @param date
	 * @param format
	 *            格式 "yyyy-MM-dd hh:mm:ss"
	 * @return
	 */
	public static String formatToString(String date, String format) {
		try {
			Date dt = DateTimeHelper.parseStringToDate(date);
			java.text.SimpleDateFormat sdf = new SimpleDateFormat(format);
			return sdf.format(dt);
		} catch (ParseException e) {
			System.out.println("时间格式转换错误");
			e.printStackTrace();
		}
		return date;
	}

	/**
	 * 将2013-06-27T00:00:00+08:00 格式转换为yyyy-MM-DD hh:mm:ss格式
	 * 
	 * @param date
	 * @return
	 */
	public static String formatToStringT(String cellValue) {
		if (cellValue.contains("T")) {
			if(cellValue.contains(".")){
				cellValue = cellValue.replace("T", " ");
				String[] values = cellValue.split("\\.");
				cellValue = values[0];
			} else {
				String values = (String) cellValue.subSequence(0, cellValue.length() - 6);
				cellValue = values.replace("T", "  ");
			}
		}
		return cellValue;
	}
}
