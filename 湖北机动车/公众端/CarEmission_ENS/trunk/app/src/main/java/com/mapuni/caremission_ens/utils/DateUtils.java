package com.mapuni.caremission_ens.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * 时间全局处理工具类
 *
 * @author Sahadev
 */
public class DateUtils {
    private static SimpleDateFormat mDateFormat = new SimpleDateFormat("MM-dd HH:mm");
    private static SimpleDateFormat mDateFormat2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private static SimpleDateFormat mDateFormat3 = new SimpleDateFormat("yyyy-MM-dd");
    private final static ThreadLocal<SimpleDateFormat> dateFormater = new ThreadLocal<SimpleDateFormat>() {
        @Override
        protected SimpleDateFormat initialValue() {
            return new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.CHINA);
        }
    };

    private final static ThreadLocal<SimpleDateFormat> dateFormater2 = new ThreadLocal<SimpleDateFormat>() {
        @Override
        protected SimpleDateFormat initialValue() {
            return new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.CHINA);
        }
    };


    public static String long2DateString(long time) {
        Date date = new Date(time);
        return mDateFormat2.format(date);
    }

    public static String long2DateString2(long time) {
        Date date = new Date(time);
        return mDateFormat3.format(date);
    }

    /**
     * Description: 获取前一天时间，返回格式like this : yyyy-MM-dd HH:mm
     *
     * @return 时间字符串 格式为 yyyy-MM-dd HH:mm:ss String
     */
    public static String getYesterdayDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        return sdf.format(new Date(System.currentTimeMillis() - 3600000 * 24));
    }
    public static String getNowDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(new Date(System.currentTimeMillis()));
    }

    /**
     * Description: 获取前一天时间，返回格式like this : yyyy-MM-dd
     *
     * @return 时间字符串 格式为 yyyy-MM-dd HH:mm:ss String
     */
    public static String getLastXDaysDate(int x) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, -x); //得到前一天
        Date date = calendar.getTime();
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        return df.format(date);
    }


    /**
     * 获取当前时间
     *
     * @param
     * @return
     */
    public static String getToday() {
        Date dt = new Date();//如果不需要格式,可直接用dt,dt就是当前系统时间
        return mDateFormat3.format(dt);
    }

    /**
     * 将字符串转位日期类型
     *
     * @param sdate
     * @return
     */
    public static Date toDate(String sdate) {
        try {
            return dateFormater.get().parse(sdate);
        } catch (ParseException e) {
            return null;
        }
    }

    /**
     * 日期包含T +08:00转化争取格式
     *
     * @param sdate
     * @return
     */
    public static String toTrueDate(String sdate) {
        if (sdate.contains("T") && sdate.contains("+08:00")) {
            return sdate.replace("T", " ").replace("+08:00", "");
        } else {
            return sdate;
        }

    }

    /**
     * 获取当前时间
     *
     * @param
     * @return
     */
    public static String getSystemNowTime() {
        Date dt = new Date();//如果不需要格式,可直接用dt,dt就是当前系统时间
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置显示格式


        return df.format(dt);
    }
    public static String getSystemDate() {
        Date dt = new Date();//如果不需要格式,可直接用dt,dt就是当前系统时间
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");//设置显示格式
        return df.format(dt);
    }

    public static String formatData(String s) {
        Date dt = new Date();//如果不需要格式,可直接用dt,dt就是当前系统时间
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置显示格式
        Date d = null;
        try {
            d = df.parse(s);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return df.format(d);
    }

    public static String formatUpdateTime(String s) {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");//设置显示格式
        Long lg = new Long(s);
        Date d = new Date(lg);
        return df.format(d);
    }

    /**
     * Description: 获取当前系统时间，返回格式like this : yyyy-MM-dd HH:mm
     *
     * @return 时间字符串 格式为 yyyy-MM-dd HH:mm:ss String
     */
    public static String getDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
//		sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        return sdf.format(new Date(System.currentTimeMillis()));
    }

    /**
     * Description: 获取当前系统时间，返回格式like this : yyyy-MM-dd HH:mm
     *
     * @return 时间字符串 格式为 yyyy-MM-dd HH:mm:ss String
     */
    public static String getDateWithSecond() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        return sdf.format(new Date());
    }

    /**
     * 以友好的方式显示时间
     *
     * @param sdate
     * @return 还剩XX天
     */
    public static String timeFriendly(String sdate) {
        Date time = toDate(sdate);
        if (time == null) {
            return "Unknown";
        }
        String ftime = "";
        Calendar cal = Calendar.getInstance();

        // 判断是否是同一天
        String curDate = dateFormater2.get().format(cal.getTime());
        String paramDate = dateFormater2.get().format(time);
        if (curDate.equals(paramDate)) {
            int hour = (int) ((cal.getTimeInMillis() - time.getTime()) / 3600000);
            if (hour == 0)
                ftime = Math.max((cal.getTimeInMillis() - time.getTime()) / 60000, 1) + "分钟前";
            else
                ftime = hour + "小时前";
            return ftime;
        }

        long lt = time.getTime() / 86400000;
        long ct = cal.getTimeInMillis() / 86400000;
        int days = (int) (ct - lt);
        if (days == 0) {
            int hour = (int) ((cal.getTimeInMillis() - time.getTime()) / 3600000);
            if (hour == 0)
                ftime = Math.max((cal.getTimeInMillis() - time.getTime()) / 60000, 1) + "分钟前";
            else
                ftime = hour + "小时前";
        } else if (days == 1) {
            ftime = "昨天";
        } else if (days == 2) {
            ftime = "前天";
        } else if (days > 2 && days <= 15) {
            ftime = days + "天前";
        } else if (days > 15) {
            ftime = dateFormater2.get().format(time);
        }
        return ftime;
    }

    /**
     * 判断给定字符串时间是否为今日
     *
     * @param sdate
     * @return boolean
     */
    public static boolean isToday(String sdate) {
        boolean b = false;
        Date time = toDate(sdate);
        Date today = new Date();
        if (time != null) {
            String nowDate = dateFormater2.get().format(today);
            String timeDate = dateFormater2.get().format(time);
            if (nowDate.equals(timeDate)) {
                b = true;
            }
        }
        return b;
    }

    public static String formatDateTime(long time) {
        if (0 == time) {
            return "";
        }

        return mDateFormat.format(new Date(time));
    }

    /**
     * 将未指定格式的字符串转换成日期类型
     *
     * @param date
     * @return
     * @throws ParseException
     */
    public static Date parseStringToDate(String date) {
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
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            System.out.println("时间格式转换错误");
            e.printStackTrace();
        }

        return result;
    }

    public static String formatToString(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        return sdf.format(date);
    }

    public static int compareData(String d1, String d2) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.US);

        Calendar c1 = Calendar.getInstance();
        Calendar c2 = Calendar.getInstance();

        try {
            c1.setTime(sdf.parse(d1));
            c2.setTime(sdf.parse(d2));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        int i = c1.compareTo(c2);
        if (i > 0) {
            return -1;
        } else if (i == 0) {
            return 0;
        } else if (i < 0) {
            return 1;
        }
        return -1;
    }
}
