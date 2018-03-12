package com.mapuni.android.widget;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import com.mapuni.android.MobileEnforcement.R;
import com.mapuni.android.base.util.LogUtil;

/**
 * FileName: MobileEnforcementWidget.java
 * Description:Widget�ؼ�
 * <li>��ʾ��ǰ�û�������
 * <li>��ʾ�������֪ͨ����
 * <li>��ʾ���칫��
 * @author Liusy
 * @Version 1.3.4
 * @Copyright �п���ͼ���¿Ƽ����޹�˾
 * Create at: 2012-10-25 ����10:46:08
 */
public class MobileEnforcementWidget extends AppWidgetProvider {
	/**��־��ӡ��ʶ*/
	public  final static String TAG = "MobileEnforcementWidget";
	
	/**һϵ�ж�����Action*/
	public  final static String CHANGE_ACTION = "com.mapuni.adnroid.MobileEnforcement.widget.change";
	public  final static String UP_ACTION = "com.mapuni.adnroid.MobileEnforcement.widget.up";
	public  final static String DOWN_ACTION = "com.mapuni.adnroid.MobileEnforcement.widget.down";	
	public  final static String TEXT1_ACTION = "com.mapuni.adnroid.MobileEnforcement.widget.text1";
	public  final static String TEXT2_ACTION = "com.mapuni.adnroid.MobileEnforcement.widget.text2";
	public  final static String TEXT3_ACTION = "com.mapuni.adnroid.MobileEnforcement.widget.text3";
	
	private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
	 
	@Override
	public void onReceive(Context context, Intent intent) {
		super.onReceive(context, intent);
	}
	
	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager,
			int[] appWidgetIds) {
		LogUtil.i(TAG, "onUpdate");
		Timer timer = new Timer();
		timer.scheduleAtFixedRate(new MyWidgetTask(context, appWidgetManager), 1000, 60000);
		super.onUpdate(context, appWidgetManager, appWidgetIds);
	}

	

	@Override
	public void onDeleted(Context context, int[] appWidgetIds) {
		LogUtil.i(TAG, "onDeleted");
		super.onDeleted(context, appWidgetIds);
	}

	/**
	 * FileName: MobileEnforcementWidget.java
	 * Description:��ť�¼����첽����
	 * @author Administrator
	 * @Version 1.3.4
	 * @Copyright �п���ͼ���¿Ƽ����޹�˾<br>
	 * Create at: 2012-12-4 ����02:29:20
	 */
	private class MyWidgetTask extends TimerTask {
		RemoteViews remoteViews;
		AppWidgetManager appWidgetManager;
		ComponentName thisWidget;

		public MyWidgetTask(Context context, AppWidgetManager appWidgetManager) {
			this.appWidgetManager = appWidgetManager;
			remoteViews = new RemoteViews(context.getPackageName(), R.layout.mobileenforcement_widget);
			thisWidget = new ComponentName(context, MobileEnforcementWidget.class);
			//����¼�
			//1.�Ҽ�ͷ���л�
			Intent intentChange = new Intent(CHANGE_ACTION);
			PendingIntent pendingIntentChange = PendingIntent.getService(context, 0, intentChange, 0);
			remoteViews.setOnClickPendingIntent(R.id.widget_head_turn, pendingIntentChange);
			//2.�ϼ�ͷ: ���Ϸ�ҳ
			Intent intentUp = new Intent(UP_ACTION);
			PendingIntent pendingIntentUp = PendingIntent.getService(context, 0, intentUp, 0);
			remoteViews.setOnClickPendingIntent(R.id.widget_bottom_up, pendingIntentUp);
			//3.�¼�ͷ: ���·�ҳ
			Intent intentDown = new Intent(DOWN_ACTION);
			PendingIntent pendingIntentDown = PendingIntent.getService(context, 0, intentDown, 0);
			remoteViews.setOnClickPendingIntent(R.id.widget_bottom_down, pendingIntentDown);
			//4.TextButton 1
			Intent textButton1 = new Intent(TEXT1_ACTION);
			PendingIntent pendingIntentText1 = PendingIntent.getService(context, 0, textButton1, 0);
			remoteViews.setOnClickPendingIntent(R.id.widget_middle_text1, pendingIntentText1);
			//5.TextButton 2
			Intent textButton2 = new Intent(TEXT2_ACTION);
			PendingIntent pendingIntentText2 = PendingIntent.getService(context, 0, textButton2, 0);
			remoteViews.setOnClickPendingIntent(R.id.widget_middle_text2, pendingIntentText2);
			//6.TextButton 3
			Intent textButton3 = new Intent(TEXT3_ACTION);
			PendingIntent pendingIntentText3 = PendingIntent.getService(context, 0, textButton3, 0);
			remoteViews.setOnClickPendingIntent(R.id.widget_middle_text3, pendingIntentText3);
		}

		
		public void run() {
			remoteViews.setTextViewText(R.id.widget_head_title, "�������");
			remoteViews.setTextViewText(R.id.widget_head_time,  dateFormat.format(new Date()));
			//remoteViews.setTextViewText(R.id.widget_middle_text1, "��������");
			appWidgetManager.updateAppWidget(thisWidget, remoteViews);
		}

	}

}