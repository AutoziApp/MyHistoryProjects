package com.mapuni.android.base.service;

import com.mapuni.android.base.util.BaseAutoUpdate;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

/**
 * FileName: BaseBroadcastReceiver.java
 * Description:Base�Ļ����㲥��
 * @author Administrator
 * @Version 1.3.4
 * @Copyright �п���ͼ���¿Ƽ����޹�˾<br>
 * Create at: 2013-3-6 ����05:29:16
 */
public class BaseBroadcastReceiver extends BroadcastReceiver {
	public static String BASE_UPDATE = "com.mapuni.android.checkUpdate";

	@Override
	public void onReceive(Context context, Intent intent) {

		/**
		 * �汾����
		 */
		if (BASE_UPDATE.equals(intent.getAction())) {
			BaseAutoUpdate update = new BaseAutoUpdate();
			if (!update.updateCheck(context)) {
				Toast.makeText(context, "���޸�����Ϣ��", 0).show();
			}
			;
		}
	}

}
