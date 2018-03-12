package cn.com.mapuni.meshing.base.util;

import java.lang.Thread.UncaughtExceptionHandler;

import android.content.Context;
import android.content.Intent;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import cn.com.mapuni.meshing.base.dataprovider.RecordLog;

/**
 * FileName: CrashExceptionCatcher.java
 * Description:�����쳣�Ĳ����࣬�����ִ��һЩ����
 * @author ��˼Զ
 * @Version 1.3.4
 * @Copyright �п���ͼ���¿Ƽ����޹�˾<br>
 * Create at: 2012-12-7 ����11:41:27
 */
public class ExceptionManager implements UncaughtExceptionHandler {
	/**��ǩ*/
	public static final String TAG = "ExceptionManager";
	
	/**
	 * Description:��¼�쳣��־
	 * @param e			������쳣
	 * @param ClassName	���쳣������
	 * @author Administrator<br>
	 * Create at: 2012-12-7 ����11:46:18
	 */
	public synchronized  static void WriteCaughtEXP(Throwable e, String ClassName) {
		RecordLog.WriteCaughtEXP(e, ClassName);
	}
	
	/** ϵͳĬ�ϵ�UncaughtException������ */
	private Thread.UncaughtExceptionHandler mDefaultHandler;
	/** CrashHandlerʵ�� */
	private static ExceptionManager INSTANCE;
	/** �����Context���� */
	private Context mContext;

	/** ��ֻ֤��һ��CrashHandlerʵ�� */
	private ExceptionManager() {
	}

	/** 
	 * Description: ��ȡCrashHandlerʵ�� ,����ģʽ
	 * @return		�������ʵ��
	 * RecordLog
	 * @author ��˼Զ
	 * Create at: 2012-12-6 ����04:36:43
	 */
	public static ExceptionManager getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new ExceptionManager();
		}
		return INSTANCE;
	}

	/**
	 * Description: ��ʼ��,ע��Context����, 
	 * ��ȡϵͳĬ�ϵ�UncaughtException������, ���ø�CrashHandlerΪ�����Ĭ�ϴ�����
	 * @param ctx ������
	 * @author ��˼Զ
	 * Create at: 2012-12-6 ����05:05:05
	 */
	public void init(Context ctx) {
		mContext = ctx;
		mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
		Thread.setDefaultUncaughtExceptionHandler(this);
	}

	/**
	 * ��UncaughtException����ʱ��ת��ú���������
	 * <li>�������룬ֹͣ����
	 */
	@Override
	public void uncaughtException(Thread thread, Throwable ex) {
		if (!handleException(ex) && mDefaultHandler != null) {
			/** ����û�û�д�������ϵͳĬ�ϵ��쳣������������ */
			mDefaultHandler.uncaughtException(thread, ex);
		} else {
			/** Sleepһ���������� */
			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				Log.e(TAG, "Error : ", e);
			}
			android.os.Process.killProcess(android.os.Process.myPid());
			System.exit(10);
		}
	}

	/**
	 * Description: �Զ��������,�ռ�������Ϣ ���ʹ��󱨸�Ȳ������ڴ����. �����߿��Ը����Լ���������Զ����쳣�����߼�
	 * @param ex ������
	 * @return true:��������˸��쳣��Ϣ;���򷵻�false
	 * boolean
	 * @author ��˼Զ
	 * Create at: 2012-12-6 ����05:06:19
	 */
	private boolean handleException(Throwable ex) {
		if (ex == null) {
			return true;
		}
		WriteCaughtEXP(ex, "Not CaughtException");
		
		ex.printStackTrace();
		LogUtil.e(TAG, ex.getMessage());
		final String msg = ex.getLocalizedMessage();
		/** ʹ��Toast����ʾ�쳣��Ϣ */
		new Thread() {
			@Override
			public void run() {
				Looper.prepare();
				//Toast.makeText(mContext, "�����쳣�������뿴��־��", Toast.LENGTH_LONG)
					//	.show();
				//Intent serviceIntent = new Intent(mContext, MessageService.class);
				//serviceIntent.setAction(MessageService.ACTION_DISMISSNOTIFICATION);
				//mContext.stopService(serviceIntent);
				Looper.loop();
			}

		}.start();
		
		Intent intent = new Intent("com.mapuni.action.launch_service");
		intent.putExtra("operation", "stop");
		mContext.sendBroadcast(intent);

		return true;
	}

}
