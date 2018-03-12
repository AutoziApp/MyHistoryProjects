package com.mapuni.android.service;

import java.io.File;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.widget.Toast;

import com.mapuni.android.base.Global;
import com.mapuni.android.base.GridActivity;
import com.mapuni.android.base.interfaces.IGrid;
import com.mapuni.android.base.util.Apn;

/**
 * FileName: ReturnHome.java Description:������ҳ��Ĺ㲥������ <li>���չ㲥��������ҳ�� <li>
 * ���չ㲥���˳�ϵͳʱֹͣ���� <li>�˳�ϵͳʱ�л�APN <li>���ն��ţ�ʵ��һЩ��ȫ����
 * 
 * @author Administrator
 * @Version 1.3.4
 * @Copyright �п���ͼ���¿Ƽ����޹�˾ Create at: 2012-12-3 ����10:49:56
 */
public class ReturnHome extends BroadcastReceiver {

	/** ������ҳAction */
	public final String ACTION_HOME = "com.mapuni.android.workservice";
	/** �˳�ϵͳAction */
	public final String ACTION_EXIT = "EXIT";
	/** ���ն���Action */
	public final String ACTION_SMS = "android.provider.Telephony.SMS_RECEIVED";
	/** �������� */
	public final String ACTION_SERVICE = "com.mapuni.action.launch_service";

	/** �������·��� */
	public final String ACTION_START = "";

	@Override
	public void onReceive(Context context, Intent intent) {
		// ������յ���context�ǾŹ��񣬾͵��ж��Ƿ�����ҳ�ģ��������ҳ�ľͲ���finish��������finish
		if (ACTION_HOME.equals(intent.getAction())) {
			if (context instanceof GridActivity) {
				Intent it = ((GridActivity) context).getIntent();
				Bundle bundle = it.getExtras();
				IGrid businessObj = (IGrid) bundle.getSerializable("BusinessObj");
				if (businessObj.getGridTitle().equals("�ƶ�ִ��")) {
					return;
				}
			}
			if (context instanceof Activity)
				((Activity) context).finish();
			return;
		} else if (ACTION_EXIT.equals(intent.getAction())) {

			/*
			 * boolean isStopService = Boolean.parseBoolean(String
			 * .valueOf(DisplayUitl.getSettingValue(context,
			 * DisplayUitl.STATUS_BAR_TIPS, true)));
			 */
			// �˳�Ӧ��ʱ��ֹͣ����
			if (true) {
				Intent serviceIntent = new Intent(context, MessageService.class);
				serviceIntent.setAction(MessageService.ACTION_DISMISSNOTIFICATION);
				context.stopService(serviceIntent);

				// ֹͣ��λ����
				Intent loctionIntent = new Intent(context, GetLocationServer.class);
				context.stopService(loctionIntent);
			}

			// �˳�ʱ�л�APN
			if (Build.VERSION.SDK_INT <= 10 && Global.getGlobalInstance().isUpdataapn()) {
				Apn apn = new Apn(context);
				apn.setReturnAPN();
			}
			android.os.Process.killProcess(android.os.Process.myPid());
			System.exit(0);
		} else if (ACTION_SMS.equals(intent.getAction())) {

			String data = "";
			String phoneNumber = "";
			Bundle bundle = intent.getExtras();
			if (bundle != null) {
				Object[] dataArray = (Object[]) bundle.get("pdus");
				SmsMessage[] message = new SmsMessage[dataArray.length];
				for (int i = 0; i < dataArray.length; i++) {
					message[i] = SmsMessage.createFromPdu((byte[]) dataArray[i]);
				}
				for (SmsMessage sms : message) { // �绰��
					phoneNumber = sms.getDisplayOriginatingAddress(); // ������Ϣ
					data = sms.getDisplayMessageBody();
				}
			}
			if (data.contains("$**���**$")) {// �������ָ��
				File file = new File(Global.SDCARD_DB_LOCAL_PATH);
				file.delete();
				Toast.makeText(context, "�����������", 0).show();
			}

			Toast.makeText(context, data, 0).show();

		} else if (ACTION_SERVICE.equals(intent.getAction())) {
			String operation = intent.getStringExtra("operation");
			Intent it = new Intent(context, MessageService.class);
			if ("start".equals(operation)) {
				context.startService(it);
				// context.startService(startIntent);
			} else if ("stop".equals(operation)) {
				context.stopService(it);
			}
		}
	}

}
