package com.mapuni.android.base;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.mapuni.android.base.business.DataSyncModel;
import com.mapuni.android.base.interfaces.IList;
import com.mapuni.android.base.util.LogUtil;
import com.mapuni.android.base.util.OtherTools;
import com.mapuni.android.netprovider.Net;

public class DownloadActivity extends Activity {

	/** 日志记录标签 */
	private final String TAG = "DownloadActivity";

	/** 消息状态常量值 */
	private final int CHANGE_STATE = 0; // 状态发生改变
	private final int DOWNLOAD_FAIL = 1; // 状态发生改变
	private final int COMPLETE_ALL = 2; // 数据表全部同步完成

	private String notificationTile = ""; // 状态栏的标题
	private String currentState = ""; // 下载状态

	private Intent intent = null;
	protected IList businessObj = null;
	private Bundle bundle = null;
	private int res;

	private TextProgressBar bar;

	boolean updateOrFetchAllData;
	Button button;

	Boolean cancel = true;
	/** 数据同步失败日志 */
	private StringBuffer syncLog = new StringBuffer();

	// 封装一个方法
	public void cancel(Boolean flag) {
		cancel = flag;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.progressbar_layout);

		bar = (TextProgressBar) findViewById(R.id.progressBar1);
		button = (Button) findViewById(R.id.cancel_sync);

		button.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				cancel(false);
				OtherTools.showToast(DownloadActivity.this, "正在取消...");
				Global.IsDataSync = false;
			}
		});

		this.intent = getIntent();

		bundle = intent.getExtras();
		businessObj = (IList) bundle.getSerializable("BusinessObj");

		notificationTile = bundle.getString("notificationTile");

		boolean isLaunch = bundle.getBoolean("islaunch");// 刚进入软件时启动服务的标志

		if (!isLaunch) {
			// 开启一个新的线程下载，如果使用Service同步下载，会导致ANR问题，Service本身也会阻塞
			new Thread(new DownloadRunnable()).start();

		}
	}

	/** 接收消息的Handler */
	private Handler downloadHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {

			switch (msg.what) {
			case CHANGE_STATE:
				break;
			case DOWNLOAD_FAIL:
				Global.IsDataSync = false;
				// 信息提示当前状态
				Toast.makeText(getApplicationContext(), "同步发生异常，已暂停同步,请查看网络状态是否正常！", Toast.LENGTH_LONG).show();
				showSyncLog();
				break;
			case COMPLETE_ALL:
				// 修改状态栏图标为已同步完成状态
				Global.IsDataSync = false;
				if (!cancel) {
					OtherTools.showToast(getApplicationContext(), "同步已取消");
					finish();
				} else {
					OtherTools.showToast(getApplicationContext(), "同步成功");
				}
				showSyncLog();
				break;
			default:
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

			for (int count = 0; count < tablesArray.length && cancel; count++) {
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

				bar.setProgress((int) (((count + 1.0) / tablesArray.length) * 100));
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
		if (syncLog.length() == 0 || !cancel) {
			return;
		}
		AlertDialog.Builder ab = new AlertDialog.Builder(DownloadActivity.this);
		ab.setTitle("同步日志");
		ab.setIcon(DownloadActivity.this.getResources().getDrawable(R.drawable.yutu));
		ScrollView avScView = new ScrollView(DownloadActivity.this);
		avScView.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.FILL_PARENT));

		TextView syncLogTv = new TextView(DownloadActivity.this);
		syncLogTv.setLayoutParams(new ScrollView.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.FILL_PARENT));
		syncLogTv.setBackgroundColor(Color.WHITE);
		syncLogTv.setTextColor(Color.BLACK);
		syncLogTv.setTextSize(20.0f);
		syncLogTv.setPadding(3, 0, 3, 0);
		syncLogTv.setText(syncLog.toString());

		ab.setNegativeButton("确定", new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				DownloadActivity.this.finish();
			}
		});
		avScView.addView(syncLogTv);
		ab.setView(avScView);

		AlertDialog ad = ab.create();
		ad.setCancelable(false);
		// ad.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
		ad.show();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			return cancel;
		}
		return false;
	}

}