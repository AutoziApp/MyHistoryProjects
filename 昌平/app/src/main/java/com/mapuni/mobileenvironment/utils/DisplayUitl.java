package com.mapuni.mobileenvironment.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Point;
import android.util.Log;
import android.view.Display;
import android.view.View;

/**
 * 工具类
 * 1、获取系统长款
 * 2、连点判断
 */
public class DisplayUitl {
	/** 同步数据*/
	public static final String SYNCDATA = "sync_data";
	/** 状态栏任务提示 */
	public static final String STATUS_BAR_TIPS = "status_bar_tips";
	/** 语音播报 */
	public static final String VOICE = "voice";
	/** 人脸识别*/
	public static final String FACEDECTOR = "facedector";
	/** 自动登录*/
	public static final String AUTOLOGIN = "auto_login";
	/** 刷新数据 */
	public static final String REFRESHDATA = "refresh_data";
	/** 菜单样式 */
	public static final String MENUSTYLE = "menustyle";
	/** 任务管理样式 */
	public static final String TASKMANAGERSTYLE = "taskmanagerstyle";
	/** 列表字体大小 */
	public static final String TEXTSIZE = "textsize";
	/** 列表一次加载数据条数 */
	public static final String LISTLOADNUM = "listloadnum";
	/** 同步数据一次请求条数 */
	public static final String SYNCDATANUM = "syncdatanum";
	/** 自动同步时间间隔 */
	public static final String AUTOASYNCDURATION = "autosyncduration";

	/** button最后点击时间 */
	private static long lastClickTime;

    public  static int ScreenWidth ;
    public  static int ScreenHeight;

	/**
	 * 返回屏幕分辨率的数组，0位置为宽度，1位置为高度
	 * @param activity 上下文
	 * @return 屏幕分辨率的数组
	 */
	public static void getMobileResolution(Activity activity) {
		Display disp = activity.getWindowManager().getDefaultDisplay();
		Point outP = new Point();
		disp.getSize(outP);
		ScreenWidth = outP.x;
		ScreenHeight = outP.y;
		int statusHeight = 0;
		try {
			Class clazz = Class.forName("com.android.internal.R$dimen");
			Object object = clazz.newInstance();
			int height = Integer.parseInt(clazz.getField("status_bar_height")
					.get(object).toString());
			statusHeight = activity.getResources().getDimensionPixelSize(height);
		} catch (Exception e) {
			e.printStackTrace();
		}
		ScreenHeight -=statusHeight;
	}

	/**
	 * Description:判断按钮是不是有效点击 ---》作用：button连续多次点击会触发多次点击事件引起一些问题
	 * @return 是否快速点击
     * */
	public static boolean isFastDoubleClick() {
		long time = System.currentTimeMillis();
		long timeD = time - lastClickTime;
		if (0 < timeD && timeD < 500) {
			return true;
		}
		lastClickTime = time;
		return false;
	}

	/**
	 * Description:判断按钮是不是有效点击 ---》作用：button避免附件多次下载
     * @return
     */
	public static boolean isDownFileClick() {
		long time = System.currentTimeMillis();
		long timeD = time - lastClickTime;
		if (0 < timeD && timeD < 5000) {
			return true;
		}
		lastClickTime = time;
		return false;
	}

    /**
     * 获取一个月内有多少天
     * @param year 年
     * @param month 月
     * @return 天数
     */
    public static int getDaysOfMonth(int year, int month){
        int days = 0;
        if(month == 1||month == 3|| month == 5||month == 7||month == 8||month == 10||month == 12){
            days = 31;
        }else if(month == 2){
            if((year%400 ==0)||(year%4 == 0 && year%100 !=0)){
                days = 29;
            }else{
                days = 28;
            }
        }else{
            days = 30;
        }
        return days;
    }
	/**
	 * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
	 */
	public static int dip2px(Context context, float dpValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dpValue * scale + 0.5f);
	}

	/**
	 * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
	 */
	public static int px2dip(Context context, float pxValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (pxValue / scale + 0.5f);
	}

	public static int[] getViewScreen( View view){
		int w = View.MeasureSpec.makeMeasureSpec(0,
				View.MeasureSpec.UNSPECIFIED);
		int h = View.MeasureSpec.makeMeasureSpec(0,
				View.MeasureSpec.UNSPECIFIED);
		view.measure(w,h);

		int height = view.getMeasuredHeight();
		int width = view.getMeasuredWidth();
		int[] array=new int[]{width,height};
		Log.i("Lybin",height+":"+width);
		return array;
	}
}
