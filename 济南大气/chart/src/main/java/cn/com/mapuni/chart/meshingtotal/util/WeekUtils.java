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
     * ��ȡĳ��ĳ�����ȵĵ�һ��
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
     * ��ȡĳ��ĳ�����ȵ����һ��
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
     * ��ȡĳ��ĳ�µĵ�һ��
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
     * ��ȡĳ��ĳ�µ����һ��
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
     * ��ȡĳ�����list
     *
     * @param year
     */
    public static List<String> getWeeksByYear(int year){
        List<String> list=new ArrayList<>();
        for (int i=1;i<=53;i++){
            String index=i<10?"0"+i:""+i;
            String str="��"+index+"��"+getStartDayOfWeekNo(year,i)+"--"+getEndDayOfWeekNo(year,i);
            list.add(str);
        }
        return list;
    }

    /**
     *
     * ��ȡ��2013������������
     *
     */
    public static List<String> getYearList(){
        List<String> list=new ArrayList<>();
        Calendar cal = Calendar.getInstance();
        int nowYear=cal.get(Calendar.YEAR);
        for (int i=nowYear;i>=2013;i--){
            list.add(i+"��");
        }
        return list;
    }

    /**
     *
     * ��ȡ1-12�·�
     *
     */
    public static List<String> getMonthList(){
        List<String> list=new ArrayList<>();
        for (int i=1;i<=12;i++){
            list.add(i+"��");
        }
        return list;
    }

    /**
     *
     * ��ȡ1-4����
     *
     */
    public static List<String> getJiduList(){
        List<String> list=new ArrayList<>();
        list.add("��һ����");
        list.add("�ڶ�����");
        list.add("��������");
        list.add("���ļ���");
        return list;
    }
}
