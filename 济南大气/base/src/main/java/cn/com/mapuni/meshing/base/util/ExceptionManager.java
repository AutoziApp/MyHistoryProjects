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
 * Description:崩溃异常的捕获类，捕获后执行一些方法
 * @author 柳思远
 * @Version 1.3.4
 * @Copyright 中科宇图天下科技有限公司<br>
 * Create at: 2012-12-7 上午11:41:27
 */
public class ExceptionManager implements UncaughtExceptionHandler {
	/**标签*/
	public static final String TAG = "ExceptionManager";
	
	/**
	 * Description:记录异常日志
	 * @param e			捕获的异常
	 * @param ClassName	抛异常的类名
	 * @author Administrator<br>
	 * Create at: 2012-12-7 上午11:46:18
	 */
	public synchronized  static void WriteCaughtEXP(Throwable e, String ClassName) {
		RecordLog.WriteCaughtEXP(e, ClassName);
	}
	
	/** 系统默认的UncaughtException处理类 */
	private Thread.UncaughtExceptionHandler mDefaultHandler;
	/** CrashHandler实例 */
	private static ExceptionManager INSTANCE;
	/** 程序的Context对象 */
	private Context mContext;

	/** 保证只有一个CrashHandler实例 */
	private ExceptionManager() {
	}

	/** 
	 * Description: 获取CrashHandler实例 ,单例模式
	 * @return		返回类的实例
	 * RecordLog
	 * @author 柳思远
	 * Create at: 2012-12-6 下午04:36:43
	 */
	public static ExceptionManager getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new ExceptionManager();
		}
		return INSTANCE;
	}

	/**
	 * Description: 初始化,注册Context对象, 
	 * 获取系统默认的UncaughtException处理器, 设置该CrashHandler为程序的默认处理器
	 * @param ctx 上下文
	 * @author 柳思远
	 * Create at: 2012-12-6 下午05:05:05
	 */
	public void init(Context ctx) {
		mContext = ctx;
		mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
		Thread.setDefaultUncaughtExceptionHandler(this);
	}

	/**
	 * 当UncaughtException发生时会转入该函数来处理
	 * <li>休眠三秒，停止进程
	 */
	@Override
	public void uncaughtException(Thread thread, Throwable ex) {
		if (!handleException(ex) && mDefaultHandler != null) {
			/** 如果用户没有处理则让系统默认的异常处理器来处理 */
			mDefaultHandler.uncaughtException(thread, ex);
		} else {
			/** Sleep一会后结束程序 */
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
	 * Description: 自定义错误处理,收集错误信息 发送错误报告等操作均在此完成. 开发者可以根据自己的情况来自定义异常处理逻辑
	 * @param ex 上下文
	 * @return true:如果处理了该异常信息;否则返回false
	 * boolean
	 * @author 柳思远
	 * Create at: 2012-12-6 下午05:06:19
	 */
	private boolean handleException(Throwable ex) {
		if (ex == null) {
			return true;
		}
		WriteCaughtEXP(ex, "Not CaughtException");
		
		ex.printStackTrace();
		LogUtil.e(TAG, ex.getMessage());
		final String msg = ex.getLocalizedMessage();
		/** 使用Toast来显示异常信息 */
		new Thread() {
			@Override
			public void run() {
				Looper.prepare();
				//Toast.makeText(mContext, "数据异常，详情请看日志！", Toast.LENGTH_LONG)
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
