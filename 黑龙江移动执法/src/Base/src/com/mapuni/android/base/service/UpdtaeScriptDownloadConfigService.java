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
 * ��½ǰ���������ű����º������ļ��Լ��汾���·���,���²���ִ����֮��رձ�����
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
	/** �����º�����Ϣ */
	private final int APKNEW = 2;
	/** �Ƿ����°汾 */
	private Boolean hasNewAPK = false;

	@Override
	public IBinder onBind(Intent intent) {

		return null;
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {

		handler = new Handler() {
			// ����Ƿ���ɱ�ʶ
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
					if (hasNewAPK) {// ���°汾�������Ի���,���Ҳ������ٷ���
						
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
					UpdtaeScriptDownloadConfigService.this.stopSelf();// ִ�и��²�����ɺ�رշ���
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
	 * �жϱ���config.xml gis_config.xml�İ汾�Ų����ظ���
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
	 * ����������½ű�
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
	 * ����Ƿ����°汾���ȽϺ�ʱ�����Կ��߳�ִ��
	 */
	protected void updateAPK() {
		new Thread() {
			@Override
			public void run() {

				String urlString = Global.getGlobalInstance().getSystemurl();
				if (Net.checkURL(urlString + PathManager.APK_CODE_URL)) {

					if (autopudate.JudgeNewVerson(urlString + PathManager.APK_CODE_URL, UpdtaeScriptDownloadConfigService.this) == 1) {
						/** ���°汾�򵯳����¶Ի��� */
						hasNewAPK = true;
					}

				} else {
					Log.i(TAG, "������--->���粻ͨ");
				}
				handler.sendEmptyMessage(APKNEW);

			}
		}.start();
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		Log.i(TAG, "UpdtaeScriptDownloadConfigService��������--->");
	}
}
