package com.yutu.car.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DataUtils {


	public static Date stringToDate(String strTime, String formatType)
			throws ParseException {
		SimpleDateFormat formatter = new SimpleDateFormat(formatType);
		Date date = null;
		date = formatter.parse(strTime);
		return date;
	}

	public static String getYesterdayDate() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		return sdf.format(new Date(System.currentTimeMillis() - 3600000 * 24));
	}
	public static String getNowDate() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return sdf.format(new Date(System.currentTimeMillis()));
	}


	public static long dateToLong(Date date) {
		return date.getTime();
	}


	public static long getTodayZero() {
		Date date = new Date();
		long l = 24 * 60 * 60 * 1000;
		return (date.getTime() - (date.getTime() % l) - 8 * 60 * 60 * 1000);
	}


	public static String getTime(long time) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		Date d1 = new Date(time);
		return format.format(d1);
	}


	public static String getToadyZeroTime() {
		return getTime(getTodayZero());
	}


	public static String getCurrentDayAndTime() {
		return new SimpleDateFormat("yyyy-MM-dd HH:mm").format(new Date());
	}


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
