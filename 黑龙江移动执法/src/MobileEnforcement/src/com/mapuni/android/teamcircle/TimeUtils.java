package com.mapuni.android.teamcircle;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class TimeUtils {


    private  static SimpleDateFormat sf = null;
    /*获取系统时间 格式为："yyyy/MM/dd "*/
    public static String getCurrentDate() {
        Date d = new Date();
        sf = new SimpleDateFormat("yyyy-MM-ddTHH:mm:dd");
        return sf.format(d);
    }

    /*将字符串转为时间戳*/
    public static long getStringToDate(String time) {
        SimpleDateFormat   sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:dd");
        Date date = new Date();
        try{
            date = sdf.parse(time);
        } catch(ParseException e) {
            e.printStackTrace();
        }
        return date.getTime();
    }


    private static final long MINUTE_SECONDS = 60; //1分钟多少秒
    private static final long HOUR_SECONDS = MINUTE_SECONDS*60;
    private static final long DAY_SECONDS = HOUR_SECONDS*24;
    private static final long YEAR_SECONDS = DAY_SECONDS*365;

    public static String testPassedTime(long nowMilliseconds, long oldMilliseconds) {
        long passed = (nowMilliseconds-oldMilliseconds) /1000;//转为秒
        if (passed > YEAR_SECONDS) {
            return passed/YEAR_SECONDS+"年前";
        } else if (passed > DAY_SECONDS) {
            return passed/DAY_SECONDS+"天前";
        } else if (passed > HOUR_SECONDS) {
            return passed/HOUR_SECONDS+"小时前";
        } else if (passed > MINUTE_SECONDS) {
            return passed/MINUTE_SECONDS+"分钟前";
        } else {
            return "刚刚";
        }
    }



}
