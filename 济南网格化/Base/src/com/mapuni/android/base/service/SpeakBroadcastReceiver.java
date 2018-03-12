package com.mapuni.android.base.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.mapuni.android.base.Global;
import com.mapuni.android.base.util.DisplayUitl;

/**
 * FileName: ReturnHome.java Description:������ҳ��Ĺ㲥������ <li>���չ㲥��������ҳ�� <li>
 * ���չ㲥���˳�ϵͳʱֹͣ���� <li>�˳�ϵͳʱ�л�APN <li>���ն��ţ�ʵ��һЩ��ȫ����
 * 
 * @author Administrator
 * @Version 1.3.4
 * @Copyright �п���ͼ���¿Ƽ����޹�˾ Create at: 2012-12-3 ����10:49:56
 */
public class SpeakBroadcastReceiver extends BroadcastReceiver {
	/** ��½��ӭAction */
	public final String ACTION_LOGIN_SPEAK = "com.mapuni.android.loginwelcome.speak";
	/** ����������Action */
	public final String ACTION_NEWTASK_SPEAK = "com.mapuni.android.newtask.speak";

	private String userID = "";

	@Override
	public void onReceive(Context context, Intent intent) {
		userID = Global.getGlobalInstance().getUserid();
		System.out.println(userID + "    ����������������");
		if (ACTION_LOGIN_SPEAK.equals(intent.getAction())) {
			if (!userID.equals("")) {
				int taskSize = new com.mapuni.android.base.business.BaseUsers(context).getTaskByUserIDandStatus(userID, "002").size();
				System.out.println(taskSize + "    ����������������");
				// ��������
				String info = "��ӭ��½�ƶ�ִ��ϵͳ�����Ĵ�ִ��������" + taskSize + "��";
				speakNotification(context, info);
			}
		}
	}

	/**
	 * Description:������������
	 * 
	 * @param info
	 * @author Administrator<br>
	 *         Create at: 2013-2-22 ����10:52:39
	 */
	private void speakNotification(Context context, String info) {
		// Ȩ�ޣ��Ƿ��
		boolean isSpeak = Boolean.parseBoolean(String.valueOf(DisplayUitl.getSettingValue(context, DisplayUitl.VOICE, true)));
		if (isSpeak) {
			Intent it = new Intent("ACTION_LOGIN");
			it.putExtra("operation", "speak");
			it.putExtra("data", info);
			context.startService(it);
		}
	}

}
