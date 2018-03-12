package com.jy.environment.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.List;

import okhttp3.Call;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.jy.environment.R;
import com.jy.environment.activity.UpdateNotificationActivity;
import com.jy.environment.activity.UpdateNotificationActivity.ICallbackResult;
import com.jy.environment.controls.WeiBaoApplication;
import com.jy.environment.model.Update;
import com.jy.environment.services.DownloadService.DownloadBinder;
import com.jy.environment.webservice.UrlComponent;
import com.zhy.http.okhttp.callback.Callback;
import com.zhy.http.okhttp.callback.FileCallBack;

/**
 * 应用程序更新工具包
 * 
 * @author liux (http://my.oschina.net/liux)
 * @version 1.1
 * @created 2012-6-29
 */
public class UpdateManagerUtil {

	private static final int DOWN_NOSDCARD = 0;
	private static final int DOWN_UPDATE = 1;
	private static final int DOWN_OVER = 2;

	private static final int DIALOG_TYPE_LATEST = 0;
	private static final int DIALOG_TYPE_FAIL = 1;
	private DownloadBinder binder;
	private boolean isBinded;
	private static UpdateManagerUtil updateManager;

	private SharedPreferences preferences;
	private SharedPreferences.Editor editor;
	private Context mContext;
	// 通知对话框
	private Dialog noticeDialog;
	// 下载对话框
	private Dialog downloadDialog;
	// '已经是最新' 或者 '无法获取最新版本' 的对话框
	private Dialog latestOrFailDialog;
	// 进度条
	private ProgressBar mProgress;
	// 显示下载数值
	private TextView mProgressText;
	// 查询动画
	private ProgressDialog mProDialog;
	// 进度值
	private int progress;
	// 下载线程
	private Thread downLoadThread;
	// 终止标记
	private boolean interceptFlag;
	// 提示语
	private String updateMsg = "";
	// 返回的安装包url
	private String apkUrl = "";
	// 下载包保存路径
	private String savePath = Environment.getExternalStorageDirectory()
			.toString() + "/weibao_agency_update/";

	private String apkName = "weibao_agency_AppUpdate.apk";
	private final String saveFileName = savePath
			+ "weibao_agency_AppUpdate.apk";
	// apk保存完整路径
	private String apkFilePath = "";
	// 临时下载文件路径
	private String tmpFilePath = "";
	// 下载文件大小
	private String apkFileSize;
	// 已下载文件大小
	private String tmpFileSize;

	private String curVersionName = "";
	private double curVersionCode;
	private Update mUpdate;
	private WeiBaoApplication app;
	private SharedPreferences shares;
	private final String SHARE_LOGIN_TAG = "MAP_SHARE_LOGIN_TAG";
	private Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case DOWN_UPDATE:
				mProgress.setProgress(progress);
				mProgressText.setText(tmpFileSize + "/" + apkFileSize);
				break;
			case DOWN_OVER:
				downloadDialog.dismiss();
				installApk();
				break;
			case DOWN_NOSDCARD:
				downloadDialog.dismiss();
				Toast.makeText(mContext, "无法下载安装文件，请检查SD卡是否挂载", 3000).show();
				break;
			}
		};
	};

	public static UpdateManagerUtil getUpdateManager() {
		if (updateManager == null) {
			updateManager = new UpdateManagerUtil();
		}
		updateManager.interceptFlag = false;
		return updateManager;
	}

	/**
	 * 检查App更新
	 * 
	 * @param context
	 * @param isShowMsg
	 *            是否显示提示消息
	 */
	public void checkAppUpdate(final Context context, final boolean isShowMsg) {
		this.mContext = context;
		getCurrentVersion();
		if (isShowMsg) {
			if (mProDialog == null)
				mProDialog = ProgressDialog.show(mContext, null, "正在检测，请稍后...",
						true, true);
			else if (mProDialog.isShowing()
					|| (latestOrFailDialog != null && latestOrFailDialog
							.isShowing()))
				return;
		}
		final Handler handler = new Handler() {
			public void handleMessage(Message msg) {
				// 进度条对话框不显示 - 检测结果也不显示
				if (mProDialog != null && !mProDialog.isShowing()) {
					return;
				}
				// 关闭并释放释放进度条对话框
				if (isShowMsg && mProDialog != null) {
					mProDialog.dismiss();
					mProDialog = null;
				}
				// 显示检测结果
				if (msg.what == 1) {
					mUpdate = (Update) msg.obj;
					if (mUpdate != null) {
						if (curVersionName.contains("beta")) {
							curVersionName = curVersionName.substring(0,
									curVersionName.lastIndexOf("beta"));
						}
						shares = mContext.getSharedPreferences(SHARE_LOGIN_TAG,
								0);
						Editor editor = shares.edit();
						if (Double.parseDouble(curVersionName) < mUpdate
								.getVersionCode()) {
							editor.putBoolean("isUpdate", true).commit();
							apkUrl = mUpdate.getDownloadUrl();
							updateMsg = mUpdate.getUpdateLog();
							UpdateManagerUtil.getUpdateManager().setmUpdate(
									mUpdate);
//							showNoticeDialog();// YYF
							showNoticeDialogYYF(apkUrl);// YYF

						} else if (isShowMsg) {
							editor.putBoolean("isUpdate", false).commit();
							showLatestOrFailDialog(context, DIALOG_TYPE_LATEST);
						}
					}
				} else if (isShowMsg) {
					showLatestOrFailDialog(context, DIALOG_TYPE_FAIL);
				}
			}
		};
		new Thread() {
			public void run() {
				Message msg = new Message();
				try {
					// String urlUp =
					// "http://1.192.88.18:8115/hnAqi/version/versionCheck?version="+curVersionName+"&key=8763apk0998";
					String urlUp = "http://125.46.1.154:9090/api/v1/PdsApp/getVersion?version="
							+ curVersionName + "&key=8763apk0998";
					MyLog.i(">>>>>>>urlUp" + urlUp);
					// Update update =
					// Update.parse(http_get("http://192.168.7.22:8080/hbzh/servlet/Version?versionCode"));
					Update update = Update.parse(http_get(urlUp));
					msg.what = 1;
					msg.obj = update;
				} catch (Exception e) {
					e.printStackTrace();
				}
				handler.sendMessage(msg);
			}
		}.start();
	}

	public void checkAppNot(final Context context, final boolean isShowMsg) {
		this.mContext = context;
		getCurrentVersion();
		final Handler handler = new Handler() {
			public void handleMessage(Message msg) {

				// 显示检测结果
				if (msg.what == 1) {
					mUpdate = (Update) msg.obj;
					if (mUpdate != null) {
						if (curVersionName.contains("beta")) {
							curVersionName = curVersionName.substring(0,
									curVersionName.lastIndexOf("beta"));
						}
						shares = mContext.getSharedPreferences(SHARE_LOGIN_TAG,
								0);
						Editor editor = shares.edit();
						if (Double.parseDouble(curVersionName) < mUpdate
								.getVersionCode()) {
							apkUrl = mUpdate.getDownloadUrl();
							updateMsg = mUpdate.getUpdateLog();
							// NotificationUtils.updateNot(context, true);
							editor.putBoolean("isUpdate", true).commit();
						} else {
							editor.putBoolean("isUpdate", false).commit();
						}
					}
				}
			}
		};
		new Thread() {
			public void run() {
				Message msg = new Message();
				try {

					// Update update =
					// Update.parse(http_get("http://192.168.7.22:8080/hbzh/servlet/Version?versionCode"));
					// Update update =
					// Update.parse(http_get("http://61.148.202.154:8080/epservice/api/app/newest"));
					String urlUp = "http://1.192.88.18:8115/hnAqi/version/versionCheck?version="
							+ curVersionName + "&key=8763apk0998";
					MyLog.i(">>>>>>>urlUp" + urlUp);
					// Update update =
					// Update.parse(http_get("http://192.168.7.22:8080/hbzh/servlet/Version?versionCode"));
					Update update = Update.parse(http_get(urlUp));
					// String url = UrlComponent.getUpdateVerson_Get;
					msg.what = 1;
					msg.obj = update;
				} catch (Exception e) {
					e.printStackTrace();
				}
				handler.sendMessage(msg);
			}
		}.start();
	}

	private String http_get(String url) throws Exception {

		// String serverurl = "http://192.168.0.2:8080/Moni/test?version=1.0";

		HttpGet httpRequest = new HttpGet(url);
		HttpClient httpClient = new DefaultHttpClient();
		HttpResponse httpResponse = httpClient.execute(httpRequest);
		String responseBody = "";
		if (httpResponse.getStatusLine().getStatusCode() == org.apache.http.HttpStatus.SC_OK) {
			responseBody = EntityUtils.toString(httpResponse.getEntity());
			return responseBody;
		}
		return responseBody;
	}

	/**
	 * 显示'已经是最新'或者'无法获取版本信息'对话框
	 */
	private void showLatestOrFailDialog(Context context, int dialogType) {
		if (latestOrFailDialog != null) {
			// 关闭并释放之前的对话框
			latestOrFailDialog.dismiss();
			latestOrFailDialog = null;
		}
		AlertDialog.Builder builder = new Builder(mContext);
		builder.setTitle("系统提示");
		if (dialogType == DIALOG_TYPE_LATEST) {
			builder.setMessage("您当前已经是最新版本");
		} else if (dialogType == DIALOG_TYPE_FAIL) {
			builder.setMessage("无法获取版本更新信息");
		}
		builder.setPositiveButton("确定", null);
		latestOrFailDialog = builder.create();
		if (context.getClass().getSimpleName()
				.equals("EnvironmentCurrentWeatherPullActivity")) {
			return;
		}
		latestOrFailDialog.show();
	}

	/**
	 * 获取当前客户端版本信息
	 */
	private void getCurrentVersion() {
		try {
			PackageInfo info = mContext.getPackageManager().getPackageInfo(
					mContext.getPackageName(), 0);
			curVersionName = info.versionName;
			curVersionCode = info.versionCode;
		} catch (NameNotFoundException e) {
			e.printStackTrace(System.err);
		}
	}

	private ICallbackResult callback = new ICallbackResult() {

		@Override
		public void OnBackResult(Object result) {
			// TODO Auto-generated method stub
			if ("finish".equals(result)) {

				return;
			}
			int i = (Integer) result;

		}

	};

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

	/**
	 * YYF 显示版本更新通知对话框
	 * 
	 * @param url
	 */
	private void showNoticeDialogYYF(final String url) {
		AlertDialog.Builder builder = new Builder(mContext);
		
		builder.setTitle("软件版本更新");
		builder.setMessage("软件升级，请立即更新！");
		app = WeiBaoApplication.getInstance();
		final DecimalFormat decimalFormat =new DecimalFormat("0.00");

		builder.setPositiveButton("立即更新", new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				File apkf = new File(saveFileName);
				if(shares.getBoolean("down", false)&&apkf.exists()){
					Toast.makeText(mContext, "正在安装,请稍后", Toast.LENGTH_LONG).show();
					installApk();
				}else{
					showDownloadDialog();
					Toast.makeText(mContext, "正在下载,请稍后", Toast.LENGTH_LONG).show();
					new Thread(new Runnable() {
						
						@Override
						public void run() {
							okUtils.downloadFile(mProgress, url, new FileCallBack(
									savePath, apkName) {
								
							@Override
							public void inProgress(float progress, long total,
									int id) {
								mProgress.setProgress((int) (100 * progress));
								String format = decimalFormat.format(progress*total/1024/1024);
								mProgressText.setText(format +"M"+"/"+total/1024/1024+"M");
							}
								
								@Override
								public void onResponse(File file, int id) {
									downloadDialog.dismiss();
									Editor edit = shares.edit();
									edit.putBoolean("down", true).commit();
									Log.e("YYF", "onResponse :" + file.getAbsolutePath());
									Toast.makeText(mContext, "下载成功", Toast.LENGTH_SHORT).show();
									installApk();
								}
								
								@Override
								public void onError(Call call, Exception e, int id) {
									downloadDialog.dismiss();
									Editor edit = shares.edit();
									edit.putBoolean("down", false).commit();
									Log.e("YYF", "onError :" + e.getMessage());
									Toast.makeText(mContext, "下载异常", Toast.LENGTH_SHORT).show();
								}
							});
						}
					}).start();
				}
			}
		});
		builder.setNeutralButton("重新下载", new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				File apkf = new File(saveFileName);
				if(apkf.exists()){
					apkf.delete();
				}
				showDownloadDialog();
				new Thread(new Runnable() {
					
					@Override
					public void run() {
						okUtils.downloadFile(mProgress, url, new FileCallBack(
								savePath, apkName) {
							
							@Override
							public void inProgress(float progress, long total,
									int id) {
								mProgress.setProgress((int) (100 * progress));
								String format = decimalFormat.format(progress*total/1024/1024);
								mProgressText.setText(format +"M"+"/"+total/1024/1024+"M");
							}
							
							@Override
							public void onResponse(File file, int id) {
								downloadDialog.dismiss();
								Editor edit = shares.edit();
								edit.putBoolean("down", true).commit();
								Log.e("YYF", "onResponse :" + file.getAbsolutePath());
								Toast.makeText(mContext, "下载成功", Toast.LENGTH_SHORT).show();
								installApk();
							}
							
							@Override
							public void onError(Call call, Exception e, int id) {
								downloadDialog.dismiss();
								Editor edit = shares.edit();
								edit.putBoolean("down", false).commit();
								Log.e("YYF", "onError :" + e.getMessage());
								Toast.makeText(mContext, "下载异常", Toast.LENGTH_SHORT).show();
							}
						});
					}
				}).start();
			}
		});
		builder.setNegativeButton("以后再说", new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
		noticeDialog = builder.create();
		if (!isBackground(mContext)) {
			noticeDialog.show();
		}
	}

	/**
	 * 显示版本更新通知对话框
	 */
	private void showNoticeDialog() {
		MyLog.i(">>>>>>>gggaghg" + "2");
		AlertDialog.Builder builder = new Builder(mContext);
		builder.setTitle("软件版本更新");
		// builder.setMessage(updateMsg);
		// builder.setMessage("当前软件版本为" + curVersionName + "，检测到最新版本为"
		// + mUpdate.getVersionCode() + "，是否立即更新？");
		builder.setMessage("软件升级，请立即更新！");
		app = WeiBaoApplication.getInstance();

		builder.setPositiveButton("立即更新", new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				// showDownloadDialog();
				// preferences.getBoolean("down", true)
				// if(preferences.getBoolean("down", true)){
				// installApk();
				// }else{
				File ApkFile = new File(saveFileName);
				// 是否已下载更新文件
				/*
				 * if (ApkFile.exists()) { installApk(); return; } else {
				 */
				Intent it = new Intent(mContext,
						UpdateNotificationActivity.class);
				mContext.startActivity(it);
				/* } */
			}
		});
		builder.setNegativeButton("以后再说", new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
		noticeDialog = builder.create();
		// noticeDialog.getWindow().setType(
		// WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
		if (!isBackground(mContext)) {
			noticeDialog.show();
		}
	}

	public static boolean isBackground(Context context) {

		ActivityManager activityManager = (ActivityManager) context
				.getSystemService(Context.ACTIVITY_SERVICE);
		List<RunningAppProcessInfo> appProcesses = activityManager
				.getRunningAppProcesses();
		for (RunningAppProcessInfo appProcess : appProcesses) {
			if (appProcess.processName.equals(context.getPackageName())) {
				if (appProcess.importance == RunningAppProcessInfo.IMPORTANCE_BACKGROUND) {
					return true;
				} else {
					return false;
				}
			}
		}
		return false;
	}

	/**
	 * 显示下载对话框
	 */
	private void showDownloadDialog() {
		AlertDialog.Builder builder = new Builder(mContext,AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
		builder.setCancelable(false);
		builder.setTitle("正在下载新版本");

		final LayoutInflater inflater = LayoutInflater.from(mContext);
		View v = inflater.inflate(R.layout.updateyy, null);
		mProgress = (ProgressBar) v.findViewById(R.id.progressbarYY);
		mProgressText = (TextView) v.findViewById(R.id.currentPosYY);

		builder.setView(v);
//		builder.setNegativeButton("取消", new OnClickListener() {
//			@Override
//			public void onClick(DialogInterface dialog, int which) {
//				dialog.dismiss();
//				interceptFlag = true;
//			}
//		});
//		builder.setOnCancelListener(new OnCancelListener() {
//			@Override
//			public void onCancel(DialogInterface dialog) {
//				dialog.dismiss();
//				interceptFlag = true;
//			}
//		});
		downloadDialog = builder.create();
		downloadDialog.setCanceledOnTouchOutside(false);
		downloadDialog.show();

//		downloadApkYYF();
	}

	private Runnable mdownApkRunnable = new Runnable() {
		@Override
		public void run() {
			try {
				String apkName = "OSChinaApp_" + mUpdate.getVersionCode()
						+ ".apk";
				String tmpApk = "OSChinaApp_" + mUpdate.getVersionCode()
						+ ".tmp";
				// 判断是否挂载了SD卡
				String storageState = Environment.getExternalStorageState();
				if (storageState.equals(Environment.MEDIA_MOUNTED)) {
					savePath = Environment.getExternalStorageDirectory()
							.getAbsolutePath() + "/OSChina/Update/";
					File file = new File(savePath);
					if (!file.exists()) {
						file.mkdirs();
					}
					apkFilePath = savePath + apkName;
					tmpFilePath = savePath + tmpApk;
				}

				// 没有挂载SD卡，无法下载文件
				if (apkFilePath == null || apkFilePath == "") {
					mHandler.sendEmptyMessage(DOWN_NOSDCARD);
					return;
				}

				File ApkFile = new File(apkFilePath);

				// 是否已下载更新文件
				/*
				 * if (ApkFile.exists()) { downloadDialog.dismiss();
				 * installApk(); return; }
				 */

				// 输出临时下载文件
				File tmpFile = new File(tmpFilePath);
				FileOutputStream fos = new FileOutputStream(tmpFile);

				URL url = new URL(apkUrl);
				HttpURLConnection conn = (HttpURLConnection) url
						.openConnection();
				conn.connect();
				int length = conn.getContentLength();
				InputStream is = conn.getInputStream();

				// 显示文件大小格式：2个小数点显示
				DecimalFormat df = new DecimalFormat("0.00");
				// 进度条下面显示的总文件大小
				apkFileSize = df.format((float) length / 1024 / 1024) + "MB";

				int count = 0;
				byte buf[] = new byte[1024];

				do {
					int numread = is.read(buf);
					count += numread;
					// 进度条下面显示的当前下载文件大小
					tmpFileSize = df.format((float) count / 1024 / 1024) + "MB";
					// 当前进度值
					progress = (int) (((float) count / length) * 100);
					// 更新进度
					mHandler.sendEmptyMessage(DOWN_UPDATE);
					if (numread <= 0) {
						// 下载完成 - 将临时下载文件转成APK文件
						if (tmpFile.renameTo(ApkFile)) {
							// 通知安装
							mHandler.sendEmptyMessage(DOWN_OVER);
						}
						break;
					}
					fos.write(buf, 0, numread);
				} while (!interceptFlag);// 点击取消就停止下载

				fos.close();
				is.close();
			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}

		}
	};

	public Update getmUpdate() {
		return mUpdate;
	}

	public void setmUpdate(Update mUpdate) {
		this.mUpdate = mUpdate;
	}

	/**
	 * 下载apk
	 * TODO YYF
	 * @param url
	 */
	private void downloadApkYYF() {
		downLoadThread = new Thread(mdownApkRunnable);
		downLoadThread.start();
	}
	/**
	 * 下载apk
	 * 
	 * @param url
	 */
	private void downloadApk() {
		downLoadThread = new Thread(mdownApkRunnable);
		downLoadThread.start();
	}

	/**
	 * 安装apk
	 * 
	 * @param url
	 */
	private void installApk() {
		File apkfile = new File(saveFileName);
		if (!apkfile.exists()) {
			return;
		}
		Intent i = new Intent(Intent.ACTION_VIEW);
		i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		i.setDataAndType(Uri.parse("file://" + apkfile.toString()),
				"application/vnd.android.package-archive");
		mContext.startActivity(i);
		callback.OnBackResult("finish");

	}
}
