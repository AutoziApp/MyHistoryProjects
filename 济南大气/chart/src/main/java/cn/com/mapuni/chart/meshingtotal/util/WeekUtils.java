package cn.com.mapuni.chart.meshingtotal.util;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by 15225 on 2017/7/20.
 * http://blog.csdn.net/enterys/article/details/49911609
 */

public class WeekUtils {


    private static Calendar getCalendarFormYear(int year) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        cal.set(Calendar.YEAR, year);
        return cal;
    }


    public static String getStartDayOfWeekNo(int year, int weekNo) {
        Calendar cal = getCalendarFormYear(year);
        cal.set(Calendar.WEEK_OF_YEAR, weekNo);
        String day=cal.get(Calendar.DAY_OF_MONTH)<10?"0"+cal.get(Calendar.DAY_OF_MONTH):""+cal.get(Calendar.DAY_OF_MONTH);
        String month=(cal.get(Calendar.MONTH)+1)<10?"0"+(cal.get(Calendar.MONTH)+1):""+(cal.get(Calendar.MONTH)+1);
        return cal.get(Calendar.YEAR) + "-" + month + "-" +day;

    }


    public static String getEndDayOfWeekNo(int year, int weekNo) {
        Calendar cal = getCalendarFormYear(year);
        cal.set(Calendar.WEEK_OF_YEAR, weekNo);
        cal.add(Calendar.DAY_OF_WEEK, 6);
        String day=cal.get(Calendar.DAY_OF_MONTH)<10?"0"+cal.get(Calendar.DAY_OF_MONTH):""+cal.get(Calendar.DAY_OF_MONTH);
        String month=(cal.get(Calendar.MONTH)+1)<10?"0"+(cal.get(Calendar.MONTH)+1):""+(cal.get(Calendar.MONTH)+1);
        return cal.get(Calendar.YEAR) + "-" + month + "-" + day;
    }

    /**
     *
     * 获取某年某个季度的第一天
     *
     * @param year
     */
    public static String getFirstDayOfJidu(int year,int index){
        switch (index){
            case 1:
                return getFirstDayOfMonth(year,1);
            case 2:
                return getFirstDayOfMonth(year,4);
            case 3:
                return getFirstDayOfMonth(year,7);
            case 4:
                return getFirstDayOfMonth(year,10);
        }
        return getFirstDayOfMonth(year,1);
    }

    /**
     *
     * 获取某年某个季度的最后一天
     *
     * @param year
     */
    public static String getLastDayOfJidu(int year,int index){
        switch (index){
            case 1:
                return getLastDayOfMonth(year,3);
            case 2:
                return getLastDayOfMonth(year,6);
            case 3:
                return getLastDayOfMonth(year,9);
            case 4:
                return getLastDayOfMonth(year,12);
        }
        return getLastDayOfMonth(year,3);
    }
    /**
     *
     * 获取某年某月的第一天
     *
     * @param year
     */
    public static String getFirstDayOfMonth(int year,int month){
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR,year);
        cal.set(Calendar.MONTH,month-1);
        String month1=(cal.get(Calendar.MONTH)+1)<10?"0"+(cal.get(Calendar.MONTH)+1):""+(cal.get(Calendar.MONTH)+1);
        return cal.get(Calendar.YEAR) + "-" + month1 + "-01";
    }
    /**
     *
     * 获取某年某月的最后一天
     *
     * @param year
     */
    public static String getLastDayOfMonth(int year,int month){
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR,year);
        cal.set(Calendar.MONTH,month-1);
        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
        String month1=(cal.get(Calendar.MONTH)+1)<10?"0"+(cal.get(Calendar.MONTH)+1):""+(cal.get(Calendar.MONTH)+1);
        return cal.get(Calendar.YEAR) + "-" + month1 +"-"+ cal.get(Calendar.DAY_OF_MONTH);
    }
    /**
     *
     * 获取某年的周list
     *
     * @param year
     */
    public static List<String> getWeeksByYear(int year){
        List<String> list=new ArrayList<>();
        for (int i=1;i<=53;i++){
            String index=i<10?"0"+i:""+i;
            String str="第"+index+"周"+getStartDayOfWeekNo(year,i)+"--"+getEndDayOfWeekNo(year,i);
            list.add(str);
        }
        return list;
    }

    /**
     *
     * 获取从2013年至今年的年份
     *
     */
    public static List<String> getYearList(){
        List<String> list=new ArrayList<>();
        Calendar cal = Calendar.getInstance();
        int nowYear=cal.get(Calendar.YEAR);
        for (int i=nowYear;i>=2013;i--){
            list.add(i+"年");
        }
        return list;
    }

    /**
     *
     * 获取1-12月份
     *
     */
    public static List<String> getMonthList(){
        List<String> list=new ArrayList<>();
        for (int i=1;i<=12;i++){
            list.add(i+"月");
        }
        return list;
    }

    /**
     *
     * 获取1-4季度
     *
     */
    public static List<String> getJiduList(){
        List<String> list=new ArrayList<>();
        list.add("第一季度");
        list.add("第二季度");
        list.add("第三季度");
        list.add("第四季度");
        return list;
    }
}
