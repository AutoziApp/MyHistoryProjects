/**
 *
 */
package com.mapuni.administrator.utils;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.text.format.Time;
import android.view.View;
import android.widget.TextView;

import com.bigkoo.pickerview.TimePickerView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * @author zhouxl
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
     *
     * @param date
     * @return
     */
    public static String formatDate(Date date) {
        return new SimpleDateFormat("yyyy年MM月dd日").format(date);
    }

    public static String formatDate(Date date, String format) {
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
     * @return
     */
    public static long curTimeMillis() {
        return System.currentTimeMillis();
    }

    /***
     * yyyy-MM-dd HH:mm:ss
     *
     * @return
     */
    public static String getTime() {
        return getTime(TIME_FORMAT);
    }

    public static String getTime(String format) {
        return new SimpleDateFormat(format).format(new Date());
    }

    /**
     * 获取当月天数
     *
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
     *      *
     *      * @param 要转换的毫秒数
     *      * @return 该毫秒数转换为 * days * hours * minutes * seconds 后的格式
     *      * @author fy.zhang
     *      
     */
    public static String formatDuring(long mss) {

        long minutes = (mss % (1000 * 60 * 60)) / (1000 * 60);
        long seconds = (mss % (1000 * 60)) / 1000;
        return minutes + " 分 " + seconds + " 秒 ";
    }
    /**
     *  @author Tianfy
     *  @time 2017/9/4  13:58
     *  @describe 获取当天零点的毫秒值
     */
    public static long getTodayZero() {
        Date date = new Date();
        long l = 24 * 60 * 60 * 1000; //每天的毫秒数 
        //date.getTime()是现在的毫秒数，它 减去 当天零点到现在的毫秒数（ 现在的毫秒数%一天总的毫秒数，取余。），理论上等于零点的毫秒数，不过这个毫秒数是UTC+0时区的。 
        //减8个小时的毫秒值是为了解决时区的问题。
        return (date.getTime() - (date.getTime() % l) - 8 * 60 * 60 * 1000);
    }
    /**
     *  @author Tianfy
     *  @time 2017/9/4  14:02
     *  @describe 获取当前时间的年月日
     */
    public static String getStrToday(){
        return getTime(DATE_FORMAT);
    }
    /**
     * 得到几天前的时间 
     * @param day
     * @return
     */
    public static String getDateBefore(int day){
        Calendar now =Calendar.getInstance();
        now.setTime(new Date());
        now.set(Calendar.DATE,now.get(Calendar.DATE)-day);
        return formatDate(now.getTime(),DATE_FORMAT);
    }
    

    /**
     * 得到几天后的时间 
     * @param d
     * @param day
     * @return
     */
    public static Date getDateAfter(Date d,int day){
        Calendar now =Calendar.getInstance();
        now.setTime(d);
        now.set(Calendar.DATE,now.get(Calendar.DATE)+day);
        return now.getTime();
    }

    /**
     *获取今天的时间的凌晨 到 23点 
     *
     */
//    private void initTime(){
//        Calendar cal = Calendar.getInstance();
//        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
//        this.startTime=sdf.format(cal.getTime())+" 00:00:00";
//        this.endTime=sdf.format(cal.getTime())+" 23:59:59";
//    }
}
