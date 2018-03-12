/**
 * 
 */
package com.mapuni.shangluo.utils;

import android.annotation.SuppressLint;
import android.text.format.Time;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * @author zhouxl
 * 
 */
public class TimeUtil {
	public static String format2YMD(long millis) {
		Time time = new Time();
		time.set(millis);
		return time.format3339(true);
	}

	public static String format2YMDHMS(long millis) {
		Time time = new Time();
		time.set(millis);
		return time.format2445();
	}

	/***
	 * yyyy年MM月dd日
	 * @param date
	 * @return
     */
	public static String formatDate(Date date) {
		return new SimpleDateFormat("yyyy年MM月dd日").format(date);
	}

	public static String formatDate(Date date,String format) {
		return new SimpleDateFormat(format).format(date);
	}

	public static String format(long millis, String format) {

		return new SimpleDateFormat(format).format(new Date(millis));
	}

	public static String DATE_FORMAT = "yyyy-MM-dd";

	public static String TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";

	public static String TIME_FORMAT_M_D_H_M = "MM-dd HH:mm";

	public static String TIME_FORMAT_YMDHM = "yyyy-MM-dd HH:mm";

	/**
	 * 
	 * @param date
	 * @param format
	 * @return
	 */
	public static long convert2long(String date, String format) {

		SimpleDateFormat sf = new SimpleDateFormat(format);
		try {
			return sf.parse(date).getTime();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return 0l;
	}

	/**
	 * 
	 * @param time
	 * @return
	 */
	@SuppressLint("SimpleDateFormat")
	public static String convert2String(long time) {
		if (time > 0l) {
			String format = TIME_FORMAT;
			SimpleDateFormat sf = new SimpleDateFormat(format);
			Date date = new Date(time);
			return sf.format(date);
		}
		return "";
	}

	@SuppressLint("SimpleDateFormat")
	public static String convert2String2(long time) {
		if (time > 0l) {
			String format = TIME_FORMAT_M_D_H_M;
			SimpleDateFormat sf = new SimpleDateFormat(format);
			Date date = new Date(time);
			return sf.format(date);
		}
		return "";
	}

	@SuppressLint("SimpleDateFormat")
	public static String convert2StringYMD(long time) {
		if (time > 0l) {
			String format = DATE_FORMAT;
			SimpleDateFormat sf = new SimpleDateFormat(format);
			Date date = new Date(time);
			return sf.format(date);
		}
		return "";
	}

	@SuppressLint("SimpleDateFormat")
	public static String convert2StringYMDHM(long time) {
		if (time > 0l) {
			String format = TIME_FORMAT_YMDHM;
			SimpleDateFormat sf = new SimpleDateFormat(format);
			Date date = new Date(time);
			return sf.format(date);
		}
		return "";
	}

	/**
	 * 
	 * @return
	 */
	public static long curTimeMillis() {
		return System.currentTimeMillis();
	}
	/***
	 * yyyy-MM-dd HH:mm:ss
	 * @return
	 */
	public static String getTime() {
		return getTime(TIME_FORMAT);
	}
	
	public static String getTime(String format){
		return new SimpleDateFormat(format).format(new Date());
	}

	/**
	 * 获取当月天数
	 * @return
     */
	public static int getCurrentMonthDayCount() {

		Calendar a = Calendar.getInstance();
		a.set(Calendar.DATE, 1);
		a.roll(Calendar.DATE, -1);
		int maxDate = a.get(Calendar.DATE);
		return maxDate;
	}


	/**
	      *
	      * @param 要转换的毫秒数
	      * @return 该毫秒数转换为 * days * hours * minutes * seconds 后的格式
	      * @author fy.zhang
	      */
	 public static String formatDuring(long mss) {

		 long minutes = (mss % (1000 * 60 * 60)) / (1000 * 60);
		 long seconds = (mss % (1000 * 60)) / 1000;
		 return   minutes + " 分 "  + seconds + " 秒 ";
		 }


}
