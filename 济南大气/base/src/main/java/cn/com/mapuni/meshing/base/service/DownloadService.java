package cn.com.mapuni.meshing.base.service;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Service;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.ConditionVariable;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import cn.com.mapuni.meshing.netprovider.Net;

import cn.com.mapuni.meshing.base.Global;
import cn.com.mapuni.meshing.base.R;
import cn.com.mapuni.meshing.base.TextProgressBar;
import cn.com.mapuni.meshing.base.business.DataSyncModel;
import cn.com.mapuni.meshing.base.interfaces.IList;
import cn.com.mapuni.meshing.base.util.LogUtil;


/**
 * FileName: DownloadService.java Description:同步服务
 * 
 * @author 王留庚
 * @Version 1.3.4
 * @Copyright 中科宇图天下科技有限公司 Create at: 2012-12-3 上午09:36:31
 */
public class DownloadService extends Service {
	/** 日志记录标签 */
	private final String TAG = "DownloadService";

	/** 消息状态常量值 */
	private final int CHANGE_STATE = 0; // 状态发生改变
	private final int DOWNLOAD_FAIL = 1; // 状态发生改变
	private final int COMPLETE_ALL = 2; // 数据表全部同步完成

	private String notificationTile = ""; // 状态栏的标题
	private String currentState = ""; // 下载状态

	private boolean isComplete = false; // 是否下载完成

	private Intent intent = null;
	private ConditionVariable mCondition = null;
	protected IList businessObj = null;
	private Bundle bundle = null;
	private int res;

	private TextProgressBar bar;
	private AlertDialog dialog;

	boolean updateOrFetchAllData;
	/** 数据同步失败日志 */
	private StringBuffer syncLog = new StringBuffer();

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		LayoutInflater inflater = LayoutInflater.from(this);
		final View view = inflater.inflate(R.layout.progressbar_layout, null);

		bar = (TextProgressBar) view.findViewById(R.id.progressBar1);
		Builder builder = new Builder(this);
		builder.setView(view);
		builder.setCancelable(false);

		dialog = builder.create();
		dialog.getWindow().setType((WindowManager.LayoutParams.TYPE_SYSTEM_ALERT));
		dialog.show();

		// 获取传值
		if (intent == null) {
			return super.onStartCommand(intent, flags, startId);

		}
		this.intent = intent;

		bundle = intent.getExtras();
		businessObj = (IList) bundle.getSerializable("BusinessObj");

		notificationTile = bundle.getString("notificationTile");

		boolean isLaunch = bundle.getBoolean("islaunch");// 刚进入软件时启动服务的标志

		if (!isLaunch) {

			// 同步数据时启动服务下载数据进行同步
			// 初始化状态栏

			// 设置下载过程中，点击通知栏，回到主界面

			// 控制下载图标的变化
			Thread notifyingThread = new Thread(null, mTask, "DownloadService");
			mCondition = new ConditionVariable(false);
			notifyingThread.start();

			// 开启一个新的线程下载，如果使用Service同步下载，会导致ANR问题，Service本身也会阻塞
			new Thread(new DownloadRunnable()).start();

		}

		return super.onStartCommand(intent, flags, startId);
	}

	/** 接收消息的Handler */
	private Handler downloadHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {

			switch (msg.what) {
			case CHANGE_STATE:
				// 信息提示当前状态
				// notification.defaults = Notification.DEFAULT_SOUND;//铃声提醒
				break;
			case DOWNLOAD_FAIL:
				isComplete = true;
				Global.IsDataSync = false;
				// 信息提示当前状态
				Toast.makeText(getApplicationContext(), "同步发生异常，已暂停同步,请查看网络状态是否正常！", Toast.LENGTH_LONG).show();
				if (dialog != null) {
					dialog.dismiss();
				}
				showSyncLog();
				stopService(intent);
				break;
			case COMPLETE_ALL:
				// 修改状态栏图标为已同步完成状态
				isComplete = true;
				Global.IsDataSync = false;
				Toast.makeText(getApplicationContext(), "同步成功", Toast.LENGTH_LONG).show();
				// 下载已经为最新，没有要同步的数据
				if (dialog != null) {
					dialog.dismiss();
				}
				showSyncLog();
				stopService(intent);
				break;
			default:
				isComplete = true;
				stopService(intent);
				break;
			}
		}
	};

	/**
	 * 通知栏下载图标，匿名线程控制
	 */
	private Runnable mTask = new Runnable() {
		public void run() {
			long blockTime = 150;
			long beginend = 120;
			while (!isComplete) {// 未下载完成则刷新通知
				/* 下载图标的动态控制 */
				if (mCondition.block(beginend) && !isComplete)
					break;
				if (mCondition.block(blockTime) && !isComplete)
					break;
				if (mCondition.block(blockTime) && !isComplete)
					break;
				if (mCondition.block(blockTime) && !isComplete)
					break;
				if (mCondition.block(blockTime) && !isComplete)
					break;
				if (mCondition.block(beginend) && !isComplete)
					break;
			}
		}
	};

	/**
	 * FileName: DownloadService.java Description:下载线程
	 * 
	 * @author Administrator
	 * @Version 1.3.4
	 * @Copyright 中科宇图天下科技有限公司 Create at: 2012-12-3 上午09:58:01
	 */
	class DownloadRunnable implements Runnable {

		/* 获取消息 */
		Message message = downloadHandler.obtainMessage();

		@Override
		public void run() {

			// 同步表数据操作
			updateOrFetchAllData = bundle.getBoolean("updateOrFetchAllData");
			String tables = bundle.getString("tables");
			// BaseDataSync dataSync = (BaseDataSync) businessObj;
			DataSyncModel dataSync = new DataSyncModel();
			String[] tablesArray = tables.split(",");

			for (int count = 0; count < tablesArray.length; count++) {
				// String table=tablesArray[count];
				String tablename = tablesArray[count];
				try {
					// 在通知上显示同步表中文名字

					// 提示当前同步的表
					message = downloadHandler.obtainMessage();
					message.what = CHANGE_STATE;
					notificationTile = "[" + tablename + "] (" + (count + 1) + "/" + tablesArray.length + ")";
					// notificationTile ="(" + (count + 1)
					// + "/" + tablesArray.length + ")";
					downloadHandler.sendMessage(message);

					res = dataSync.synchronizeFetchServerData(updateOrFetchAllData, tablename);

					// 同步完成，发送提示消息
					switch (res) {
					case DataSyncModel.SYNC_FAIL:
						if (Net.checkURL(Global.getGlobalInstance().getSystemurl())) {// 能联网，继续同步剩下的表
							message = downloadHandler.obtainMessage();
							message.what = CHANGE_STATE;
							notificationTile = "数据表[" + tablename + "](" + (count + 1) + "/" + tablesArray.length + ")--同步失败";
							syncLog.append(notificationTile + "\n");
						} else {
							message = downloadHandler.obtainMessage();
							message.what = DOWNLOAD_FAIL;
							notificationTile = "数据表[" + tablename + "](" + (count + 1) + "/" + tablesArray.length + ")--同步失败";
							syncLog.append(notificationTile + "\n");
							return;
						}

						break;
					case DataSyncModel.SYNC_SERVICE_ERR:
						if (Net.checkURL(Global.getGlobalInstance().getSystemurl())) {// 能联网，继续同步剩下的表
							message = downloadHandler.obtainMessage();
							message.what = CHANGE_STATE;
							notificationTile = "数据表[" + tablename + "](" + (count + 1) + "/" + tablesArray.length + ")--获取服务端数据异常";
							syncLog.append(notificationTile + "\n");
							downloadHandler.sendMessage(message);
						} else {
							message = downloadHandler.obtainMessage();
							message.what = DOWNLOAD_FAIL;
							notificationTile = "数据表[" + tablename + "](" + (count + 1) + "/" + tablesArray.length + ")--获取服务端数据异常";
							syncLog.append(notificationTile + "\n");
							downloadHandler.sendMessage(message);
							return;
						}

						break;
					case DataSyncModel.SYNC_TIMEOUT:// 超时
						message = downloadHandler.obtainMessage();
						message.what = DOWNLOAD_FAIL;
						notificationTile = "数据表[" + tablename + "](" + (count + 1) + "/" + tablesArray.length + ")--连接服务器超时";
						syncLog.append(notificationTile + "\n");
						downloadHandler.sendMessage(message);
						return;

					case 0:
						// 没有要同步的数据
						message = downloadHandler.obtainMessage();
						message.what = CHANGE_STATE;
						notificationTile = "数据表[" + tablename + "] (" + (count + 1) + "/" + tablesArray.length + ")--数据已最新";
						syncLog.append(notificationTile + "\n");
						downloadHandler.sendMessage(message);
						break;

					default:
						// 下载成功
						message = downloadHandler.obtainMessage();
						message.what = CHANGE_STATE;

						notificationTile = "数据表[" + tablename + "](" + (count + 1) + "/" + tablesArray.length + ")--同步成功";
						syncLog.append(notificationTile + " 受影响行数" + res + "\n");
						downloadHandler.sendMessage(message);

						break;
					}

				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					LogUtil.i(TAG, tablename + "同步异常");
					message = downloadHandler.obtainMessage();
					message.what = CHANGE_STATE;
					notificationTile = "数据表[" + tablename + "](" + (count + 1) + "/" + tablesArray.length + ")--同步失败";
					syncLog.append(notificationTile + "\n");
				}

				bar.setProgress((int) (((count + 0.0) / tablesArray.length) * 100));
			}
			// 信息提示同步完所有的表

			message = downloadHandler.obtainMessage();
			message.what = COMPLETE_ALL;
			notificationTile = "数据同步完成";
			downloadHandler.sendMessage(message);

		}

	}

	/**
	 * 展示同步日志
	 */
	private void showSyncLog() {
		if (syncLog.length() == 0) {
			return;
		}
		AlertDialog.Builder ab = new AlertDialog.Builder(DownloadService.this);
		ab.setTitle("同步日志");
		ab.setIcon(DownloadService.this.getResources().getDrawable(R.drawable.yutu));
		ScrollView avScView = new ScrollView(DownloadService.this);
		avScView.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.FILL_PARENT));

		TextView syncLogTv = new TextView(DownloadService.this);
		syncLogTv.setLayoutParams(new ScrollView.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.FILL_PARENT));
		// syncLogTv.setGravity(Gravity.CENTER);
		syncLogTv.setBackgroundColor(Color.WHITE);
		syncLogTv.setTextColor(Color.BLACK);
		syncLogTv.setTextSize(20.0f);
		syncLogTv.setPadding(3, 0, 3, 0);
		syncLogTv.setText(syncLog.toString());

		ab.setNegativeButton("确定", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// stopService(intent);
			}

		});
		avScView.addView(syncLogTv);
		ab.setView(avScView);
		AlertDialog ad = ab.create();
		ad.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
		ad.show();
	}

	@Override
	public void onDestroy() {
		LogUtil.i(TAG, "DownloadService被销毁了");
		super.onDestroy();
		this.stopSelf();
	}

}
