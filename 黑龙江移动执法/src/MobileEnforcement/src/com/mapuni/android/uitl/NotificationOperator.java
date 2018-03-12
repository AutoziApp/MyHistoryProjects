package com.mapuni.android.uitl;


import java.util.ArrayList;
import java.util.List;

import com.mapuni.android.MobileEnforcement.R;

import android.app.NotificationManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.os.ConditionVariable;
import android.content.Context;
import android.content.Intent;

/*
 * 功能：           通知栏
 * 创建时间：2012-6-29
 * Author：    liugx 
 * 
 * */
public class NotificationOperator {

	// 上下文对象
	private Context _Context;
	
	//Intent对象
	private Intent _Intent;
	
	//PendingIntent对象
	private PendingIntent _PendingIntent;

	// 通知栏管理对象
	private NotificationManager _NotificationManager;

	// 通知栏对象
	private Notification _Notification;
	
	//修改通知栏图标线程对象
	private Thread _Thread;
	
	//控制通知栏消息变更
	private ConditionVariable _ConditionVariable;
	
	//是否完成标示
	public boolean _IsComplete=false;
	
	public String _TickerText="上传任务";

	// 构造方法
	public NotificationOperator(Context context,Intent intent) {
		setContext(context);
		_Intent=intent;
	}
	public void InitNotification(int _ICon,String _ContentText) {
		
		// 创建一个通知栏管理对象
		_NotificationManager = (NotificationManager) _Context.getSystemService(Context.NOTIFICATION_SERVICE);
		
		// 通知栏对象
		_Notification = new Notification();
		
		//获取PaddingIntent对象
		_PendingIntent=PendingIntent.getActivity(_Context, 0, _Intent, 0);
		
		StartNotification(_ICon,_ContentText,false);
		
		//更新状态栏图标
		UpdateNotificationIcon();
	}
	
	/*
	 * Function:更新状态栏图标
	 *  Create Date:2012-6-19 
	 *  Author:liugx 
	 */
	public void UpdateNotificationIcon()
	{
		_Thread=new Thread(null,_Runnable,"NotificationIcon");
		_ConditionVariable=new ConditionVariable(false);
		_Thread.start();
	}
	
	private Runnable _Runnable=new Runnable() {
		
		@Override
		public void run() {

			long blockTime = 150;
			List<Integer> _List=GetIconList();
			int index=1;
			while (!_IsComplete) {	
				StartNotificationIcon(_List.get(index));
				_ConditionVariable.block(blockTime);
				index++;
				
				if(index>=_List.size())
				{
					index=1;
				}
			}
			
			//通知栏完成时设置完成图标   
			if(_IsComplete==true)
			{
				StartNotificationIcon(_List.get(0));
			}
				
		}
	};
	
	public List<Integer> GetIconList()
	{
		List<Integer> _List=new ArrayList<Integer>();
		_List.add(R.drawable.stat_sys_upload_anim0);
		_List.add(R.drawable.stat_sys_upload_anim1);
		_List.add(R.drawable.stat_sys_upload_anim2);
		_List.add(R.drawable.stat_sys_upload_anim3);
		_List.add(R.drawable.stat_sys_upload_anim4);
		_List.add(R.drawable.stat_sys_upload_anim5);
		
		return _List;
	}
	
	/*
	 * 修改通知栏图标
	 */
	public void StartNotificationIcon(int _Icon)
	{
		_Notification.icon =_Icon;
		_Notification.tickerText=_TickerText;
		//发出通知
		_NotificationManager.notify(0, _Notification);
	}
	
	/*
	 *  Function:初始化
	 *  Create Date:2012-6-19 
	 *  Author:liugx
	 */
	public void StartNotification(int _ICon,String _ContentText,boolean _Status) {
		
		_IsComplete=_Status;
		_Notification.icon =_ICon;
		_Notification.tickerText=_TickerText;
		
		//在通知栏上点击此通知后自动清除此通知 
		_Notification.flags=Notification.FLAG_AUTO_CANCEL;
		
		//设置即将执行的事件
		_Notification.setLatestEventInfo(_Context, _TickerText, _ContentText, _PendingIntent);
		
		//发出通知
		_NotificationManager.notify(0, _Notification);
		
		UpdateNotificationIcon();
		
	}
	
	/*
	 *  Function:取消通知，并删除线程
	 *  Create Date:2012-6-19 
	 *  Author:liugx
	 */
	public void StopNotification()
	{
		_NotificationManager.cancel(0);
//		_Thread.destroy();
	}

	public void setContext(Context _Context) {
		this._Context = _Context;
	}

	public Context getContext() {
		return _Context;
	}
}
