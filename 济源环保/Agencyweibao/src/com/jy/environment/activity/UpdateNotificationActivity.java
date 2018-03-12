package com.jy.environment.activity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.jy.environment.R;
import com.jy.environment.base.ActivityBase;
import com.jy.environment.controls.WeiBaoApplication;
import com.jy.environment.services.DownloadService;
import com.jy.environment.services.DownloadService.DownloadBinder;
import com.jy.environment.util.SharedPreferencesUtil;
/**
 * 更新Notification
 * @author baiyuchuan
 *
 */
public class UpdateNotificationActivity extends ActivityBase {
	private Button btn_cancel;// btn_update,
	private TextView tv_progress;
	private DownloadBinder binder;
	private boolean isBinded;
	private ProgressBar mProgressBar;
	// 获取到下载url后，直接复制给MapApp,里面的全局变量
	private String downloadUrl;
	//
	private boolean isDestroy = true;
	private WeiBaoApplication app;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.update);
		app = WeiBaoApplication.getInstance();
		// btn_update = (Button) findViewById(R.id.update);
		btn_cancel = (Button) findViewById(R.id.cancel);
		tv_progress = (TextView) findViewById(R.id.currentPos);
		mProgressBar = (ProgressBar) findViewById(R.id.progressbar1);
		btn_cancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				binder.cancel();
				binder.cancelNotification();
				finish();
			}
		});
	}

	ServiceConnection conn = new ServiceConnection() {

		@Override
		public void onServiceDisconnected(ComponentName name) {
			// TODO Auto-generated method stub
			isBinded = false;
		}

		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			// TODO Auto-generated method stub
			binder = (DownloadBinder) service;
		
			// 开始下载
			isBinded = true;
			binder.addCallback(callback);
			binder.start();

		}
	};

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
//		if (isDestroy ) {
			Intent it = new Intent(UpdateNotificationActivity.this, DownloadService.class);
			startService(it);
			bindService(it, conn, Context.BIND_AUTO_CREATE);
			finish();
//		}
		
	}


	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();

	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		isDestroy = false;
	
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (isBinded) {
		
			unbindService(conn);
		}
		if (binder != null && binder.isCanceled()) {
		
			Intent it = new Intent(this, DownloadService.class);
			stopService(it);
		}
	}

	private ICallbackResult callback = new ICallbackResult() {

		@Override
		public void OnBackResult(Object result) {
			// TODO Auto-generated method stub
			if ("finish".equals(result)) {
				finish();
				// 升级成功，最高权限开关设置为关闭
				SharedPreferencesUtil sp = SharedPreferencesUtil.getInstance(UpdateNotificationActivity.this);
//				if (sp.getQuanXian()) {
//					sp.setQuanXian(false);
//					showToastLong("最高权限模式自动关闭");
//				}
				return;
			}
			int i = (Integer) result;
			mProgressBar.setProgress(i);
			mHandler.sendEmptyMessage(i);
		}

	};

	private Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			tv_progress.setText("当前进度 ： " + msg.what + "%");

		};
	};

	public interface ICallbackResult {
		public void OnBackResult(Object result);
	}
}
