package com.mapuni.android.teamcircle;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DataUtils {

	/**
	 * strTimeҪת����string���͵�ʱ�䣬formatTypeҪת���ĸ�ʽyyyy-MM-dd HH:mm:ss//yyyy��MM��dd��
	 * HHʱmm��ss�룬 strTime��ʱ���ʽ����Ҫ��formatType��ʱ���ʽ��ͬ
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
	 * dateҪת����date���͵�ʱ��
	 * 
	 * @param date
	 * @return
	 */
	public static long dateToLong(Date date) {
		return date.getTime();
	}

	/**
	 * �õ��������ĺ���ֵ
	 * 
	 * @return
	 */
	public static long getTodayZero() {
		Date date = new Date();
		long l = 24 * 60 * 60 * 1000; // ÿ��ĺ�����
		// date.getTime()�����ڵĺ��������� ��ȥ ������㵽���ڵĺ�������
		// ���ڵĺ�����%һ���ܵĺ�������ȡ�ࡣ���������ϵ������ĺ����������������������UTC+0ʱ���ġ�
		// ��8��Сʱ�ĺ���ֵ��Ϊ�˽��ʱ�������⡣
		return (date.getTime() - (date.getTime() % l) - 8 * 60 * 60 * 1000);
	}

	/**
	 * ������ֵת��Ϊʱ��
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
	 * ������ֵת��Ϊ������
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
	 * �õ�����ʱ��
	 * 
	 * @return
	 */
	public static String getToadyZeroTime() {
		return getTime(getTodayZero());
	}

	/**
	 * �õ�ϵͳ��ǰʱ��
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
	 * strTimeתLong���͵�ʱ��
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
