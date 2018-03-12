package cn.com.mapuni.meshing.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * 閺冨爼妫块崗銊ョ湰婢跺嫮鎮婂銉ュ徔缁拷
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
     * Description: 閼惧嘲褰囬崜宥勭婢垛晜妞傞梻杈剧礉鏉╂柨娲栭弽鐓庣础like this : yyyy-MM-dd HH:mm
     *
     * @return 閺冨爼妫跨�涙顑佹稉锟� 閺嶇厧绱℃稉锟� yyyy-MM-dd HH:mm:ss String
     */
    public static String getYesterdayDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        return sdf.format(new Date(System.currentTimeMillis() - 3600000 * 24));
    }
    public static String getNowDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        return sdf.format(new Date(System.currentTimeMillis()));
    }

    /**
     * Description: 閼惧嘲褰囬崜宥勭婢垛晜妞傞梻杈剧礉鏉╂柨娲栭弽鐓庣础like this : yyyy-MM-dd
     *
     * @return 閺冨爼妫跨�涙顑佹稉锟� 閺嶇厧绱℃稉锟� yyyy-MM-dd HH:mm:ss String
     */
    public static String getLastXDaysDate(int x) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, -x); //瀵版鍩岄崜宥勭婢讹拷
        Date date = calendar.getTime();
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        return df.format(date);
    }


    /**
     * 閼惧嘲褰囪ぐ鎾冲閺冨爼妫�
     *
     * @param
     * @return
     */
    public static String getToday() {
        Date dt = new Date();//婵″倹鐏夋稉宥夋付鐟曚焦鐗稿锟�,閸欘垳娲块幒銉ф暏dt,dt鐏忚鲸妲歌ぐ鎾冲缁崵绮洪弮鍫曟？
        return mDateFormat3.format(dt);
    }

    /**
     * 鐏忓棗鐡х粭锔胯鏉烆兛缍呴弮銉︽埂缁鐎�
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
     * 閺冦儲婀￠崠鍛儓T +08:00鏉烆剙瀵叉禍澶婂絿閺嶇厧绱�
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
     * 閼惧嘲褰囪ぐ鎾冲閺冨爼妫�
     *
     * @param
     * @return
     */
    public static String getSystemNowTime() {
        Date dt = new Date();//婵″倹鐏夋稉宥夋付鐟曚焦鐗稿锟�,閸欘垳娲块幒銉ф暏dt,dt鐏忚鲸妲歌ぐ鎾冲缁崵绮洪弮鍫曟？
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//鐠佸墽鐤嗛弰鍓с仛閺嶇厧绱�


        return df.format(dt);
    }
    public static String getSystemDate() {
        Date dt = new Date();//婵″倹鐏夋稉宥夋付鐟曚焦鐗稿锟�,閸欘垳娲块幒銉ф暏dt,dt鐏忚鲸妲歌ぐ鎾冲缁崵绮洪弮鍫曟？
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");//鐠佸墽鐤嗛弰鍓с仛閺嶇厧绱�
        return df.format(dt);
    }

    public static String formatData(String s) {
        Date dt = new Date();//婵″倹鐏夋稉宥夋付鐟曚焦鐗稿锟�,閸欘垳娲块幒銉ф暏dt,dt鐏忚鲸妲歌ぐ鎾冲缁崵绮洪弮鍫曟？
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//鐠佸墽鐤嗛弰鍓с仛閺嶇厧绱�
        Date d = null;
        try {
            d = df.parse(s);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return df.format(d);
    }

    public static String formatUpdateTime(String s) {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");//鐠佸墽鐤嗛弰鍓с仛閺嶇厧绱�
        Long lg = new Long(s);
        Date d = new Date(lg);
        return df.format(d);
    }

    /**
     * Description: 閼惧嘲褰囪ぐ鎾冲缁崵绮洪弮鍫曟？閿涘矁绻戦崶鐐寸壐瀵紗ike this : yyyy-MM-dd HH:mm
     *
     * @return 閺冨爼妫跨�涙顑佹稉锟� 閺嶇厧绱℃稉锟� yyyy-MM-dd HH:mm:ss String
     */
    public static String getDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
//		sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        return sdf.format(new Date(System.currentTimeMillis()));
    }

    /**
     * Description: 閼惧嘲褰囪ぐ鎾冲缁崵绮洪弮鍫曟？閿涘矁绻戦崶鐐寸壐瀵紗ike this : yyyy-MM-dd HH:mm
     *
     * @return 閺冨爼妫跨�涙顑佹稉锟� 閺嶇厧绱℃稉锟� yyyy-MM-dd HH:mm:ss String
     */
    public static String getDateWithSecond() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        return sdf.format(new Date());
    }

    /**
     * 娴犮儱寮告總鐣屾畱閺傜懓绱￠弰鍓с仛閺冨爼妫�
     *
     * @param sdate
     * @return 鏉╂ê澧縓X婢讹拷
     */
    public static String timeFriendly(Long sdate) {
        Date time = new Date(sdate);
        if (time == null) {
            return "Unknown";
        }
        String ftime = "";
        Calendar cal = Calendar.getInstance();

        // 閸掋倖鏌囬弰顖氭儊閺勵垰鎮撴稉锟芥径锟�
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
     * 閸掋倖鏌囩紒娆忕暰鐎涙顑佹稉鍙夋闂傚瓨妲搁崥锔胯礋娴犲﹥妫�
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
     * 鐏忓棙婀幐鍥х暰閺嶇厧绱￠惃鍕摟缁楋缚瑕嗘潪顒佸床閹存劖妫╅張鐔鸿閸拷
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
            System.out.println("閺冨爼妫块弽鐓庣础鏉烆剚宕查柨娆掝嚖");
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
