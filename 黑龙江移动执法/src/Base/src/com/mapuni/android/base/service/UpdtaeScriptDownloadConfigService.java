package com.mapuni.android.base.service;

import java.io.IOException;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;

import com.mapuni.android.base.Global;
import com.mapuni.android.base.business.BaseDataSync;
import com.mapuni.android.base.interfaces.PathManager;
import com.mapuni.android.base.util.BaseAutoUpdate;
import com.mapuni.android.netprovider.Net;

/**
 * 登陆前启动，检查脚本更新和配置文件以及版本更新服务,更新操作执行完之后关闭本服务
 * 
 * @author wanglg
 * 
 */
public class UpdtaeScriptDownloadConfigService extends Service {
	private final String TAG = "UpdtaeScriptDownloadConfigService";
	BaseAutoUpdate autopudate;
	Handler handler;
	private final int CONFIGSUCCESS = 0;
	private final int DBSCRIPT = 1;
	/** 检测更新后发送消息 */
	private final int APKNEW = 2;
	/** 是否有新版本 */
	private Boolean hasNewAPK = false;

	@Override
	public IBinder onBind(Intent intent) {

		return null;
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {

		handler = new Handler() {
			// 检测是否完成标识
			Boolean ISCONFIGSUCCESS = false;
			Boolean ISDBSCRIPT = false;
			Boolean ISAPKNEW = false;

			@Override
			public void handleMessage(Message msg) {
				switch (msg.what) {
				case CONFIGSUCCESS:
					ISCONFIGSUCCESS = true;
					break;
				case DBSCRIPT:
					ISDBSCRIPT = true;
					break;
				case APKNEW:
					if (hasNewAPK) {// 有新版本，弹出对话框,并且不能销毁服务
						
						autopudate.new UpdateAsyncTask(UpdtaeScriptDownloadConfigService.this)
						.execute();
					} else {
						ISAPKNEW = true;
					}

					break;
				default:
					break;
				}

				if (ISCONFIGSUCCESS & ISDBSCRIPT & ISAPKNEW) {
					UpdtaeScriptDownloadConfigService.this.stopSelf();// 执行更新操作完成后关闭服务
				}
			}
		};
		autopudate = new BaseAutoUpdate();
		checkAndUpdateConfig();
		checkDB();
		updateAPK();

		return super.onStartCommand(intent, flags, startId);
	}

	/**
	 * 判断本地config.xml gis_config.xml的版本号并下载更新
	 */
	public void checkAndUpdateConfig() {
		final String[] tables = { "config.xml", "gis_config.xml" };

		new Thread() {
			@Override
			public void run() {

				try {
					if (Net.checkURL(Global.getGlobalInstance().getSystemurl())) {
						BaseDataSync bds = new BaseDataSync();

						for (int i = 0; i < tables.length; i++) {
							bds.synchronizeFetchServerData(true, tables[i]);
						}
					}
				} catch (IOException e) {
					e.printStackTrace();
				} finally {
					handler.sendEmptyMessage(CONFIGSUCCESS);
				}

			}
		}.start();
	}

	/**
	 * 链接网络更新脚本
	 */
	protected void checkDB() {
		new Thread(new Runnable() {

			@Override
			public void run() {
				//autopudate.UpdateDatebaseScript();
				handler.sendEmptyMessage(DBSCRIPT);
			}
		}).start();

	}

	/**
	 * 检测是否有新版本，比较耗时，所以开线程执行
	 */
	protected void updateAPK() {
		new Thread() {
			@Override
			public void run() {

				String urlString = Global.getGlobalInstance().getSystemurl();
				if (Net.checkURL(urlString + PathManager.APK_CODE_URL)) {

					if (autopudate.JudgeNewVerson(urlString + PathManager.APK_CODE_URL, UpdtaeScriptDownloadConfigService.this) == 1) {
						/** 有新版本则弹出更新对话框 */
						hasNewAPK = true;
					}

				} else {
					Log.i(TAG, "检查更新--->网络不通");
				}
				handler.sendEmptyMessage(APKNEW);

			}
		}.start();
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		Log.i(TAG, "UpdtaeScriptDownloadConfigService服务销毁--->");
	}
}
