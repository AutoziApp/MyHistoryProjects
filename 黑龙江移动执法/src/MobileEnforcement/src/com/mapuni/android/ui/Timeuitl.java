package com.mapuni.android.ui;

import android.annotation.SuppressLint;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Timeuitl {
	
	/**
	 * ��ȡϵͳ��ǰ��ʱ��
	 * 
	 * */
	
	@SuppressLint("SimpleDateFormat")
	public static String getsystemtine(){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//���ڸ�ʽ   2013-04-02 14:22:22 

        String str = sdf.format(new Date());//��ǰʱ��
		return str;
		
	}

}
