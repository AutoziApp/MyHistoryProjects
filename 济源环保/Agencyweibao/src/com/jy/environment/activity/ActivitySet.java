package com.jy.environment.activity;

import com.jy.environment.R;
import com.jy.environment.base.ActivityBase;
import com.jy.environment.business.BusinessSearch;
import com.jy.environment.controls.AsyncTask;
import com.jy.environment.model.Update;
import com.jy.environment.util.CommonUtil;
import com.jy.environment.util.DataCleanManager;
import com.jy.environment.util.ExitAppUtils;
import com.jy.environment.util.MyLog;
import com.jy.environment.util.SharedPreferencesUtil;
import com.jy.environment.util.UpdateManagerUtil;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ActivitySet extends ActivityBase implements OnClickListener {
	// private TextView tuichu;
	private TextView qinglihuancun;
	private TextView size;
	// private TextView fankui;
	private TextView getxin_value;
	private TextView gengxin;
	private ImageView back;
	private ImageView ic;
	private DataCleanManager dataCleanManager;
	private String sizeValue = "0.0";
	private Context context;
	private TextView version;
	public Dialog dialog1;
	public UpdateManagerUtil managerUtil;
	public SharedPreferencesUtil preferencesUtil;
	private LinearLayout version_lay;
	private TextView version_text;

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.activity_set);
		context = this;
		dataCleanManager = new DataCleanManager();
		ExitAppUtils.getInstance().addActivity(this);
		dialog1 = CommonUtil.getCustomeDialog(this, R.style.load_dialog, R.layout.custom_progress_dialog);
		TextView titleTxtv = (TextView) dialog1.findViewById(R.id.dialogText);
		titleTxtv.setText("正在清理");
		initview();
		managerUtil = new UpdateManagerUtil();
		GetUpdateTask getUpdateTask = new GetUpdateTask();
		getUpdateTask.execute();
		preferencesUtil = SharedPreferencesUtil.getInstance(context);
		// if(preferencesUtil.getQuanXian()){
		// showToastLong("您已进入最高权限模式");
		// }else{
		// showToastLong("您不再进入最高权限模式");
		// }
	}

	private void initview() {
		// private LinearLayout version_lay;
		// private TextView version_text;
		// tuichu = (TextView) findViewById(R.id.tuichu);
		
		version_lay = (LinearLayout) findViewById(R.id.version_lay);
		version_text = (TextView) findViewById(R.id.version_text);
		qinglihuancun = (TextView) findViewById(R.id.qinglihuancun);
		size = (TextView) findViewById(R.id.size);
		// fankui = (TextView) findViewById(R.id.fankui);
		getxin_value = (TextView) findViewById(R.id.getxin_value);
		gengxin = (TextView) findViewById(R.id.gengxin);
		back = (ImageView) findViewById(R.id.back);
		version = (TextView) findViewById(R.id.version);
		ic=(ImageView) findViewById(R.id.ic);
		ic.setOnClickListener(this);
		back.setOnClickListener(this);
		// tuichu.setOnClickListener(this);
		qinglihuancun.setOnClickListener(this);
		// fankui.setOnClickListener(this);
		gengxin.setOnClickListener(this);
		try {
			sizeValue = dataCleanManager.getTotalCacheSize(context);
		} catch (Exception e) {
			e.printStackTrace();
		}
		size.setText(sizeValue);
//		version.setOnClickListener(this);
//		version_lay.setOnClickListener(this);
//		version_text.setOnClickListener(this);
	}

	/**
	 * 记录上次点击时间
	 */
	private static long mExitTime = 0;
	/**
	 * 记录点击次数
	 */
	private static int dianjicishu = 0;
	
	/**
	 * 记录上次点击时间
	 */
	private static long mExitTime_old = 0;
	/**
	 * 记录点击次数
	 */
	private static int dianjicishu_old = 0;

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.back:
			finish();
			break;
		case R.id.version:
		case R.id.version_lay:
		case R.id.version_text:
			if ((System.currentTimeMillis() - mExitTime) > 2000 && dianjicishu < 3) {
				dianjicishu = 0;
				mExitTime = System.currentTimeMillis();
			} else if (dianjicishu >= 3) {
				dianjicishu = 0;
				if (preferencesUtil.getQuanXian()) {
					preferencesUtil.setQuanXian(false);
					showToastLong("您已关闭最高权限模式");
				} else {
					/*preferencesUtil.setQuanXian(true);
					showToastLong("您已进入最高权限模式");*/
					
					openActivity(AuthorityActivity.class);
				}
			} else {
				dianjicishu++;
				mExitTime = System.currentTimeMillis();

			}

			break;
		// case R.id.tuichu:
		// dialog_Exit(context);
		// break;
		case R.id.qinglihuancun:
			dialog1.show();
			new Thread() {
				public void run() {
					dataCleanManager.clearAllCache(context, handler);
				};
			}.start();

			break;
		// case R.id.fankui:
		// openActivity(ActivityUserFeedback.class);
		// break;
		case R.id.gengxin:
			managerUtil.checkAppUpdate(context, true);
			break;
		case R.id.ic:
			if ((System.currentTimeMillis() - mExitTime_old) > 2000 && dianjicishu_old < 3) {
				dianjicishu_old = 0;
				mExitTime_old = System.currentTimeMillis();
			} else if (dianjicishu_old >= 3) {
				dianjicishu_old = 0;
				if (preferencesUtil.getoldshow()) {
					preferencesUtil.setoldshow(false);
					showToastLong("您已关闭浏览旧版数据模式");
				} else {
					/*preferencesUtil.setQuanXian(true);
					showToastLong("您已进入最高权限模式");*/
					openActivity(AuthorityOldActivity.class);
					
				}
				
//				if (preferencesUtil.getoldshow()) {
//					preferencesUtil.setoldshow(false);
//					showToastLong("您已关闭浏览旧版数据模式");
//				}else{
//					preferencesUtil.setoldshow(true);
//					showToastLong("您已开启浏览旧版数据模式");
//				}
			} else {
				dianjicishu_old++;
				mExitTime_old = System.currentTimeMillis();

			}
			break;
		default:
			break;
		}

	}

	public static final int tag = 101;
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case tag:
				if (dialog1.isShowing()) {
					dialog1.dismiss();

				}
				try {
					sizeValue = dataCleanManager.getTotalCacheSize(context);
				} catch (Exception e) {
					e.printStackTrace();
				}
				size.setText(sizeValue);
				showToastShort("清除缓存成功");
				break;

			default:
				break;
			}
		};
	};

	public static void dialog_Exit(Context context) {
		AlertDialog.Builder builder = new Builder(context);
		builder.setMessage("确定要退出吗?");
		builder.setTitle("提示");
		builder.setIcon(android.R.drawable.ic_dialog_alert);
		builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				ExitAppUtils.getInstance().exit();
				android.os.Process.killProcess(android.os.Process.myPid());
			}
		});

		builder.setNegativeButton("取消", new android.content.DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});

		builder.create().show();
	}

	public class GetUpdateTask extends AsyncTask<String, Void, Update> {
		@Override
		protected void onPreExecute() {
			// if (!NetUtil.isNetworkConnected(context)) {
			// ToastUtil.showShort(context, "无网络");
			// if (null != dialog && dialog.isShowing()) {
			// dialog.dismiss();
			// }
			// return;
			// }
			// if (null != dialog && !dialog.isShowing()) {
			// dialog.show();
			// }
			super.onPreExecute();
		}

		@Override
		protected Update doInBackground(String... params) {
			String url = "http://125.46.1.154:9090/api/v1/PdsApp/getVersion";//YYF
			BusinessSearch search = new BusinessSearch();
			Update result = null;
			try {
				result = search.getupdate(getCurrentVersion(url));
			} catch (Exception e) {
				e.printStackTrace();
				finish();
				return result;
			}
			return result;
		}

		@Override
		protected void onPostExecute(Update result) {
			super.onPostExecute(result);
			version.setText("版本号" + curVersionName);
			// if (null != dialog && dialog.isShowing()) {
			// dialog.dismiss();
			// }
			if (null == result) {
				return;
			}
			if (Double.parseDouble(curVersionName) < result.getVersionCode()) {
				getxin_value.setText("有新版本可用");
			} else {
				getxin_value.setText("已是最新版");
			}

		}
	};

	String curVersionName;
	double curVersionCode;

	/**
	 * 获取当前客户端版本信息
	 */
	private String getCurrentVersion(String url) {
		try {

			PackageInfo info = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
			curVersionName = info.versionName;
			curVersionCode = info.versionCode;
			if (!url.contains("key=8763apk0998")) {
				url = url + "?version=" + curVersionName + "&key=8763apk0998";
			}
			MyLog.i("url:" + url);
		} catch (NameNotFoundException e) {
			e.printStackTrace(System.err);
		}
		return url;
	}

}
