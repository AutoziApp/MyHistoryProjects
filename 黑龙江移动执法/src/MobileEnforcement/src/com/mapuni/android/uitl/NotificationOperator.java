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
 * ���ܣ�           ֪ͨ��
 * ����ʱ�䣺2012-6-29
 * Author��    liugx 
 * 
 * */
public class NotificationOperator {

	// �����Ķ���
	private Context _Context;
	
	//Intent����
	private Intent _Intent;
	
	//PendingIntent����
	private PendingIntent _PendingIntent;

	// ֪ͨ���������
	private NotificationManager _NotificationManager;

	// ֪ͨ������
	private Notification _Notification;
	
	//�޸�֪ͨ��ͼ���̶߳���
	private Thread _Thread;
	
	//����֪ͨ����Ϣ���
	private ConditionVariable _ConditionVariable;
	
	//�Ƿ���ɱ�ʾ
	public boolean _IsComplete=false;
	
	public String _TickerText="�ϴ�����";

	// ���췽��
	public NotificationOperator(Context context,Intent intent) {
		setContext(context);
		_Intent=intent;
	}
	public void InitNotification(int _ICon,String _ContentText) {
		
		// ����һ��֪ͨ���������
		_NotificationManager = (NotificationManager) _Context.getSystemService(Context.NOTIFICATION_SERVICE);
		
		// ֪ͨ������
		_Notification = new Notification();
		
		//��ȡPaddingIntent����
		_PendingIntent=PendingIntent.getActivity(_Context, 0, _Intent, 0);
		
		StartNotification(_ICon,_ContentText,false);
		
		//����״̬��ͼ��
		UpdateNotificationIcon();
	}
	
	/*
	 * Function:����״̬��ͼ��
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
			
			//֪ͨ�����ʱ�������ͼ��   
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
	 * �޸�֪ͨ��ͼ��
	 */
	public void StartNotificationIcon(int _Icon)
	{
		_Notification.icon =_Icon;
		_Notification.tickerText=_TickerText;
		//����֪ͨ
		_NotificationManager.notify(0, _Notification);
	}
	
	/*
	 *  Function:��ʼ��
	 *  Create Date:2012-6-19 
	 *  Author:liugx
	 */
	public void StartNotification(int _ICon,String _ContentText,boolean _Status) {
		
		_IsComplete=_Status;
		_Notification.icon =_ICon;
		_Notification.tickerText=_TickerText;
		
		//��֪ͨ���ϵ����֪ͨ���Զ������֪ͨ 
		_Notification.flags=Notification.FLAG_AUTO_CANCEL;
		
		//���ü���ִ�е��¼�
		_Notification.setLatestEventInfo(_Context, _TickerText, _ContentText, _PendingIntent);
		
		//����֪ͨ
		_NotificationManager.notify(0, _Notification);
		
		UpdateNotificationIcon();
		
	}
	
	/*
	 *  Function:ȡ��֪ͨ����ɾ���߳�
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
