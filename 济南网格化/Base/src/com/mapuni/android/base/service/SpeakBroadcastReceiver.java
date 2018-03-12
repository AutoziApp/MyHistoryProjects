package com.mapuni.android.base.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.mapuni.android.base.Global;
import com.mapuni.android.base.util.DisplayUitl;

/**
 * FileName: ReturnHome.java Description:返回主页面的广播接收器 <li>接收广播，返回主页面 <li>
 * 接收广播，退出系统时停止服务 <li>退出系统时切换APN <li>接收短信，实现一些安全机制
 * 
 * @author Administrator
 * @Version 1.3.4
 * @Copyright 中科宇图天下科技有限公司 Create at: 2012-12-3 上午10:49:56
 */
public class SpeakBroadcastReceiver extends BroadcastReceiver {
	/** 登陆欢迎Action */
	public final String ACTION_LOGIN_SPEAK = "com.mapuni.android.loginwelcome.speak";
	/** 新任务提醒Action */
	public final String ACTION_NEWTASK_SPEAK = "com.mapuni.android.newtask.speak";

	private String userID = "";

	@Override
	public void onReceive(Context context, Intent intent) {
		userID = Global.getGlobalInstance().getUserid();
		System.out.println(userID + "    语音播报进来了吗？");
		if (ACTION_LOGIN_SPEAK.equals(intent.getAction())) {
			if (!userID.equals("")) {
				int taskSize = new com.mapuni.android.base.business.BaseUsers(context).getTaskByUserIDandStatus(userID, "002").size();
				System.out.println(taskSize + "    语音播报进来了吗？");
				// 语音播报
				String info = "欢迎登陆移动执法系统，您的待执行任务有" + taskSize + "条";
				speakNotification(context, info);
			}
		}
	}

	/**
	 * Description:调用语音播报
	 * 
	 * @param info
	 * @author Administrator<br>
	 *         Create at: 2013-2-22 上午10:52:39
	 */
	private void speakNotification(Context context, String info) {
		// 权限，是否打开
		boolean isSpeak = Boolean.parseBoolean(String.valueOf(DisplayUitl.getSettingValue(context, DisplayUitl.VOICE, true)));
		if (isSpeak) {
			Intent it = new Intent("ACTION_LOGIN");
			it.putExtra("operation", "speak");
			it.putExtra("data", info);
			context.startService(it);
		}
	}

}
