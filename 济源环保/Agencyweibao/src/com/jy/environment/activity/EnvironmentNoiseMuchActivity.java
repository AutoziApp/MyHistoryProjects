package com.jy.environment.activity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jy.environment.R;
import com.jy.environment.business.BusinessSearch;
import com.jy.environment.controls.AsyncTask;
import com.jy.environment.controls.WeiBaoApplication;
import com.jy.environment.database.dal.SQLiteDALModelNoiseHistory;
import com.jy.environment.database.model.ModelNoiseHistory;
import com.jy.environment.model.RecordData;
import com.jy.environment.model.uploadRecordresult;
import com.jy.environment.util.CommonUtil;
import com.jy.environment.util.KjhttpUtils;
import com.jy.environment.util.MyLog;
import com.jy.environment.util.SharedPreferencesUtil;
import com.jy.environment.util.KjhttpUtils.DownGet;
import com.jy.environment.webservice.UrlComponent;
import com.jy.environment.widget.SwitchButton;
import com.umeng.analytics.MobclickAgent;

public class EnvironmentNoiseMuchActivity extends Activity implements
		OnClickListener, OnCheckedChangeListener {

	private ImageView back_btn, noise_much_mbtn;
	private TextView clearbtn;
	private ProgressDialog prDialog;
	private SQLiteDALModelNoiseHistory mSqLiteDALModelNoiseHistory;
	private String userid;
	private WeiBaoApplication mApplication;
	private SwitchButton noise_much_autobtn;
	private SharedPreferencesUtil mSpUtil;
	private String dataCount, dataCount2;
	private TextView noise_much_datacount;
	private RelativeLayout noise_much_mlayout;
	public static final String ACTION_NAME = "LOGINED";
	private boolean isnoise_logined = false;
	private InputMethodManager imm;
	private KjhttpUtils http;
	private UploadDataTask uploadDataTask;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.noise_history_much);
		http = new KjhttpUtils(this, null);
		mSqLiteDALModelNoiseHistory = new SQLiteDALModelNoiseHistory(
				ModelNoiseHistory.class);
		mSpUtil = SharedPreferencesUtil
				.getInstance(EnvironmentNoiseMuchActivity.this);
		mApplication = WeiBaoApplication.getInstance();
		userid = mApplication.getUserId();
		uploadDataTask = new UploadDataTask();
		mApplication.setIsnoisemuch(true);
		initView();
		initListener();
		DataCountTask dataCountTask = new DataCountTask();
		dataCountTask.execute();
		if ("noise".equals(getIntent().getStringExtra("from"))) {
			userid = getIntent().getStringExtra("userId");
			// UploadDataTask uploadDataTask = new UploadDataTask();
			// uploadDataTask.execute();
			upLoadTast();
		}
		// 注册广播
	}

	private void initView() {
		back_btn = (ImageView) findViewById(R.id.noise_much_back);
		clearbtn = (TextView) findViewById(R.id.noise_history_delete);
		noise_much_mbtn = (ImageView) findViewById(R.id.noise_much_mbtn);
		noise_much_autobtn = (SwitchButton) findViewById(R.id.noise_much_autobtn);
		noise_much_datacount = (TextView) findViewById(R.id.noise_much_datacount);
		noise_much_mlayout = (RelativeLayout) findViewById(R.id.noise_much_mlayout);
		if (userid.equals("") || userid == null) {
			mSpUtil.setIsAutoUpload(false);
		}
	}

	private void upLoadTast() {
		if (AsyncTask.Status.PENDING == uploadDataTask.getStatus()) {
			uploadDataTask.execute();
		} else if (AsyncTask.Status.RUNNING == uploadDataTask.getStatus()) {
		} else if (AsyncTask.Status.FINISHED == uploadDataTask.getStatus()) {
			uploadDataTask = new UploadDataTask();
			uploadDataTask.execute();
		}
	}

	private void initListener() {
		back_btn.setOnClickListener(this);
		clearbtn.setOnClickListener(this);
		noise_much_mlayout.setOnClickListener(this);
		noise_much_autobtn.setChecked(mSpUtil.getIsAutoUpload());
		noise_much_autobtn.setOnCheckedChangeListener(this);
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
		boolean isOpen = imm.isActive();
		if (isOpen) {
			// 输入法隐藏
			imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
		}
		isnoise_logined = mApplication.getIsnoise_logined();
		MyLog.i("=====isnoise_logined=== " + isnoise_logined);
		if (mSpUtil.getIsAutoUpload() && isnoise_logined) {
			MobclickAgent.onEvent(EnvironmentNoiseMuchActivity.this,
					"HJSynNoiseAutoTrue");
			Toast.makeText(getApplicationContext(), "自动同步数据已开启", 0).show();
		} else if (mApplication.getUserId().equals("")) {
			noise_much_autobtn.setChecked(false);
			MobclickAgent.onEvent(EnvironmentNoiseMuchActivity.this,
					"HJSynNoiseAutoFalse");
		}
		userid = mApplication.getUserId();
		if (isnoise_logined) {
			// UploadDataTask uploadDataTask = new UploadDataTask();
			// uploadDataTask.execute();
			upLoadTast();
		}
		mApplication.setIsnoise_logined(false);
		MobclickAgent.onResume(this);
	}

	@Override
	protected void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.noise_much_back:
			finish();
			break;
		case R.id.noise_history_delete:
			if (dataCount2.equals("0")) {
				Toast.makeText(EnvironmentNoiseMuchActivity.this, "本地无历史数据",
						Toast.LENGTH_SHORT).show();
			} else {
				MobclickAgent.onEvent(EnvironmentNoiseMuchActivity.this,
						"HJClearLocalNoise");
				ClearDataTask cleardata = new ClearDataTask();
				cleardata.execute();
			}
			break;
		case R.id.noise_much_mlayout:
			if (login()) {
			} else {
				if (CommonUtil.isFastDoubleClick()) {
					return;
				}

				MobclickAgent.onEvent(EnvironmentNoiseMuchActivity.this,
						"HJSynNoiseHM");
				// UploadDataTask uploadDataTask = new UploadDataTask();
				// uploadDataTask.execute();
				upLoadTast();
			}
		}

	}

	public class UploadDataTask extends AsyncTask<Void, Void, Boolean> {
		List<ModelNoiseHistory> list = new ArrayList<ModelNoiseHistory>();
		List<ModelNoiseHistory> list2 = new ArrayList<ModelNoiseHistory>();
		RecordData recordData;

		@Override
		protected void onPreExecute() {
			prDialog = new ProgressDialog(EnvironmentNoiseMuchActivity.this);
			prDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			prDialog.setMessage("本地数据同步中···");
			// 进度条是否不明确
			prDialog.setIndeterminate(true);
			prDialog.setCancelable(true);
			prDialog.show();
			super.onPreExecute();
		}

		@Override
		protected Boolean doInBackground(Void... params) {
			try {
				list = mSqLiteDALModelNoiseHistory.selectAllHistoryMuch();
				list2 = mSqLiteDALModelNoiseHistory.selectAllHistoryupload();
				if (list == null) {
					return false;
				} else if (list2 != null && list2.size() > 0) {
					String url = UrlComponent.uploadRecordData();
					BusinessSearch search = new BusinessSearch();
					uploadRecordresult _Result = null;
					for (int i = 0; i < list.size(); i++) {
						if (list.get(i).getIsupload().equals("0")) {
							recordData = new RecordData(userid, list.get(i)
									.getmResult(), list.get(i).getLocation(),
									list.get(i).getLatitude(), list.get(i)
											.getLongitude(), list.get(i)
											.getTime());
							try {
								_Result = search.uploadrecordpost(url,
										recordData);
							} catch (IOException e) {
								e.printStackTrace();
							}
						}
					}
					mSqLiteDALModelNoiseHistory.clearTable();
					// mSqLiteDALModelNoiseHistory.execSQL("update ModelNoiseHistory set isupload = '1'");
					return true;
				}
			} catch (Exception e) {
			}
			return false;
		}

		@Override
		protected void onPostExecute(Boolean result) {
			try {
				MyLog.i("weibao result:" + result);
			if (result) {
				dataCount = "0";
				noise_much_datacount.setVisibility(View.GONE);
				Toast.makeText(EnvironmentNoiseMuchActivity.this, "同步完成",
						Toast.LENGTH_LONG).show();
				String url = UrlComponent.getShare(userid, "noise");
				if (!userid.equals("")) {
					http.getString(url, 0, new DownGet() {

						@Override
						public void downget(String arg0) {
							// TODO Auto-generated method stub

						}
					});
				}
				Intent mIntent = new Intent(
						EnvironmentNoiseHistoryActivity.ACTION_NAME);
				sendBroadcast(mIntent);
			} else {
				Toast.makeText(getApplicationContext(), "无数据需要同步", 0).show();
			}
			prDialog.cancel();
		} catch (Exception e) {
			MyLog.e("weibao Exception", e);
		}
		}

	}

	public class ClearDataTask extends AsyncTask<Void, Void, Void> {

		@Override
		protected void onPreExecute() {
			prDialog = new ProgressDialog(EnvironmentNoiseMuchActivity.this);
			prDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			prDialog.setMessage("本地历史数据清除中···");
			// 进度条是否不明确
			prDialog.setIndeterminate(true);
			prDialog.setCancelable(true);
			prDialog.show();
			super.onPreExecute();
		}

		@Override
		protected Void doInBackground(Void... params) {
			try {
				mSqLiteDALModelNoiseHistory.clearTable();
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			try {
				MyLog.i("weibao result:" + result);
			prDialog.cancel();
			dataCount = "0";
			dataCount2 = "0";
			noise_much_datacount.setVisibility(View.GONE);
			Toast.makeText(EnvironmentNoiseMuchActivity.this, "本地历史数据已清空", 3000)
					.show();
			Intent mIntent = new Intent(
					EnvironmentNoiseHistoryActivity.ACTION_NAME);
			sendBroadcast(mIntent);
			} catch (Exception e) {
				MyLog.e("weibao Exception", e);
			}
		}

	}

	// 登录模块
	private boolean login() {
		if ("".equals(userid)) {
			Toast.makeText(getApplicationContext(), "请先登录", Toast.LENGTH_LONG)
					.show();
			;
			new Thread() {
				@Override
				public void run() {
					try {
						Intent intentlogin = new Intent(
								getApplicationContext(),
								UserLoginActivity.class);
						intentlogin.putExtra("from", "noise");
						startActivity(intentlogin);
					} catch (Exception e) {
						e.printStackTrace();
					}
				};
			}.start();
			return true;
		}
		return false;
	}

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		switch (buttonView.getId()) {
		case R.id.noise_much_autobtn:
			mSpUtil.setIsAutoUpload(isChecked);
			if (isChecked) {
				if (login()) {
				} else {
					//
					// uploadDataTask.execute();
					upLoadTast();
				}
			} else {
				Toast.makeText(getApplicationContext(), "自动同步数据已关闭", 0).show();
			}
			break;
		default:
			break;
		}

	}

	public class DataCountTask extends AsyncTask<Void, Void, String> {
		List<ModelNoiseHistory> list = new ArrayList<ModelNoiseHistory>();
		List<ModelNoiseHistory> list2 = new ArrayList<ModelNoiseHistory>();

		@Override
		protected String doInBackground(Void... params) {
			list = mSqLiteDALModelNoiseHistory.selectAllHistoryupload();
			list2 = mSqLiteDALModelNoiseHistory.selectAllHistory();
			if (list != null) {
				dataCount = "" + list.size();
			} else {
				dataCount = "0";
			}
			if (list2 != null) {
				dataCount2 = "" + list2.size();
			} else {
				dataCount2 = "0";
			}
			return dataCount;
		}

		@Override
		protected void onPostExecute(String result) {
			 try {
					MyLog.i("weibao result:" + result);
			if (!result.equals("0")) {
				noise_much_datacount.setText(result);
				noise_much_datacount.setVisibility(View.VISIBLE);
			} else {
				noise_much_datacount.setVisibility(View.GONE);
			}
		} catch (Exception e) {
			MyLog.e("weibao Exception", e);
		}
		}

	}

}
