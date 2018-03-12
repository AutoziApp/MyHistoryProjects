package com.mapuni.android.ui;

import android.annotation.SuppressLint;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Timeuitl {
	
	/**
	 * 获取系统当前的时间
	 * 
	 * */
	
	@SuppressLint("SimpleDateFormat")
	public static String getsystemtine(){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//日期格式   2013-04-02 14:22:22 

        String str = sdf.format(new Date());//当前时间
		return str;
		
	}

}
