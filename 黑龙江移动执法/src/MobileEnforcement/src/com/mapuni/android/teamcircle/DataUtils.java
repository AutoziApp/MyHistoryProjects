package com.mapuni.android.teamcircle;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DataUtils {

	/**
	 * strTime要转换的string类型的时间，formatType要转换的格式yyyy-MM-dd HH:mm:ss//yyyy年MM月dd日
	 * HH时mm分ss秒， strTime的时间格式必须要与formatType的时间格式相同
	 * 
	 * @param strTime
	 * @param formatType
	 * @return
	 * @throws ParseException
	 */
	public static Date stringToDate(String strTime, String formatType)
			throws ParseException {
		SimpleDateFormat formatter = new SimpleDateFormat(formatType);
		Date date = null;
		date = formatter.parse(strTime);
		return date;
	}

	/**
	 * date要转换的date类型的时间
	 * 
	 * @param date
	 * @return
	 */
	public static long dateToLong(Date date) {
		return date.getTime();
	}

	/**
	 * 得到当天零点的毫秒值
	 * 
	 * @return
	 */
	public static long getTodayZero() {
		Date date = new Date();
		long l = 24 * 60 * 60 * 1000; // 每天的毫秒数
		// date.getTime()是现在的毫秒数，它 减去 当天零点到现在的毫秒数（
		// 现在的毫秒数%一天总的毫秒数，取余。），理论上等于零点的毫秒数，不过这个毫秒数是UTC+0时区的。
		// 减8个小时的毫秒值是为了解决时区的问题。
		return (date.getTime() - (date.getTime() % l) - 8 * 60 * 60 * 1000);
	}

	/**
	 * 将毫秒值转换为时间
	 * 
	 * @param time
	 * @return
	 */
	public static String getTime(long time) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date d1 = new Date(time);
		return format.format(d1);
	}
	/**
	 * 将毫秒值转换为年月日
	 * 
	 * @param time
	 * @return
	 */
	public static String getYMD() {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		Date d1 = new Date(System.currentTimeMillis());
		return format.format(d1);
	}

	/**
	 * 得到零点的时间
	 * 
	 * @return
	 */
	public static String getToadyZeroTime() {
		return getTime(getTodayZero());
	}

	/**
	 * 得到系统当前时间
	 * 
	 * @return
	 */
	public static String getCurrentDayAndTime() {
		return new SimpleDateFormat("yyyy-MM-dd HH:mm").format(new Date());
	}
	
	public static String getCurrentTime() {
		return new SimpleDateFormat("HH:mm").format(new Date());
	}

	/**
	 * strTime转Long类型的时间
	 * 
	 * @param strTime
	 * @param formatType
	 * @return
	 */
	public static Long getLongToString(String strTime, String formatType) {
		try {
			return dateToLong(stringToDate(strTime, formatType));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

}
