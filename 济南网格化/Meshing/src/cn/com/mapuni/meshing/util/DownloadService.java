package cn.com.mapuni.meshing.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.widget.RemoteViews;

import cn.com.mapuni.meshing.util.UpdateApkActivity.ICallbackResult;

import com.example.meshing.R;
import com.mapuni.android.base.Global;
import com.mapuni.android.base.interfaces.PathManager;

public class DownloadService extends Service {
	private static final int NOTIFY_ID = 0;
	private int progress;
	private NotificationManager mNotificationManager;
	private boolean canceled;
//	private SharedPreferences preferences;
//	private SharedPreferences.Editor editor;
	// ���صİ�װ��url
//	private String apkUrl = "http://www.shenbianer.com.cn:8080/epservice/api/app/update";
	private String apkUrl = PathManager.APK_DOWN_URL_JINAN;
	// private String apkUrl = MyApp.downloadApkUrl;
	/* ���ذ���װ·�� */
	private static final String savePath = PathManager.SDCARD_AutoUpdate_LOCAL_PATH;

	private static final String saveFileName = PathManager.SDCARD_APK_LOCAL_PATH;
	private ICallbackResult callback;
	private DownloadBinder binder;
	private Global app;
	private boolean serviceIsDestroy = false;
	
	Notification mNotification;

	private Context mContext = this;
	private Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			switch (msg.what) {
			case 0:
				app.setDownload(false);
				// �������
				// ȡ��֪ͨ
				mNotificationManager.cancel(NOTIFY_ID);
//				
//				editor = preferences.edit();
//				editor.putBoolean("down", true);
//				editor.commit();
				
				installApk();
				break;
			case 2:
				app.setDownload(false);
				// �������û������ֶ�ȡ�������Իᾭ��activity��onDestroy();����
				// ȡ��֪ͨ
				mNotificationManager.cancel(NOTIFY_ID);
				break;
			case 1:
				int rate = msg.arg1;
				app.setDownload(true);
				if (rate < 100) {
					RemoteViews contentview = mNotification.contentView;
					contentview.setTextViewText(R.id.tv_progress, rate + "%");
					contentview.setProgressBar(R.id.progressbar, 100, rate, false);
				} else {
				
//					// ������Ϻ�任֪ͨ��ʽ
//					mNotification.flags = Notification.FLAG_AUTO_CANCEL;
//					mNotification.contentView = null;
//					Intent intent = new Intent(mContext, UpdateApkActivity.class);
//					// ��֪�����
//					intent.putExtra("completed", "yes");
//					// ���²���,ע��flagsҪʹ��FLAG_UPDATE_CURRENT
//					PendingIntent contentIntent = PendingIntent.getActivity(mContext, 0, intent,
//							PendingIntent.FLAG_UPDATE_CURRENT);
//					mNotification.setLatestEventInfo(mContext, "�������", "�ļ����������", contentIntent);
					
					RemoteViews contentview = mNotification.contentView;
					contentview.setTextViewText(R.id.tv_progress, rate + "%");
					contentview.setProgressBar(R.id.progressbar, 100, rate, false);
					
					serviceIsDestroy = true;
					stopSelf();// ͣ����������
				}
				mNotificationManager.notify(NOTIFY_ID, mNotification);
				break;
			}
		}
	};

	//
	// @Override
	// public int onStartCommand(Intent intent, int flags, int startId) {
	// // TODO Auto-generated method stub
	// return START_STICKY;
	// }

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		
		return binder;
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	
		// ���类�����ˣ�������ζ�Ĭ��ȡ���ˡ�
		app.setDownload(false);
	}

	@Override
	public boolean onUnbind(Intent intent) {
		// TODO Auto-generated method stub
	
		return super.onUnbind(intent);
	}

	@Override
	public void onRebind(Intent intent) {
		// TODO Auto-generated method stub

		super.onRebind(intent);
	
	}

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		binder = new DownloadBinder();
		mNotificationManager = (NotificationManager) getSystemService(android.content.Context.NOTIFICATION_SERVICE);
//		setForeground(true);// �����ȷ���Ƿ�������
		app = Global.getGlobalInstance();
		app.setDownload(false);
//		preferences = getSharedPreferences("config", MODE_PRIVATE);
	}

	public class DownloadBinder extends Binder {
		public void start() {
			if (downLoadThread == null || !downLoadThread.isAlive()) {
				
				progress = 0;
				setUpNotification();
				new Thread() {
					public void run() {
						// ����
						startDownload();
					};
				}.start();
			}
		}

		public void cancel() {
			canceled = true;
		}

		public int getProgress() {
			return progress;
		}

		public boolean isCanceled() {
			return canceled;
		}

		public boolean serviceIsDestroy() {
			return serviceIsDestroy;
		}

		public void cancelNotification() {
			mHandler.sendEmptyMessage(2);
		}

		public void addCallback(ICallbackResult callback) {
			DownloadService.this.callback = callback;
		}
	}

	private void startDownload() {
		// TODO Auto-generated method stub
		canceled = false;
		downloadApk();
	}


	// ֪ͨ��
	/**
	 * ����֪ͨ
	 */
	private void setUpNotification() {
		int icon = R.drawable.yutu;
		CharSequence tickerText = "��ʼ����";
		long when = System.currentTimeMillis();
		mNotification = new Notification(icon, tickerText, when);
		// ������"��������"��Ŀ��
		mNotification.flags = Notification.FLAG_ONGOING_EVENT;

		RemoteViews contentView = new RemoteViews(getPackageName(), R.layout.download_notification_layout);
		contentView.setTextViewText(R.id.name, "���������񻯻������ϵͳ.apk ��������...");
		// ָ�����Ի���ͼ
		mNotification.contentView = contentView;

		Intent intent = new Intent(this, UpdateApkActivity.class);
		// ���������� �ڰ�home�󣬵��֪ͨ��������֮ǰactivity ״̬;
		// ����������Ļ�������service���ں�̨���أ� �ڵ������ͼƬ���½������ʱ��ֱ�ӵ����ؽ��棬�൱�ڰѳ���MAIN ��ڸ��� - -
		// ����ô���ô������
		// intent.setAction(Intent.ACTION_MAIN);
 		// intent.addCategory(Intent.CATEGORY_LAUNCHER);
		PendingIntent contentIntent = PendingIntent.getActivity(this, 0, intent,
				PendingIntent.FLAG_UPDATE_CURRENT);

		// ָ��������ͼ
		mNotification.contentIntent = contentIntent;
		mNotificationManager.notify(NOTIFY_ID, mNotification);
	}

	//
	/**
	 * ����apk
	 * 
	 * @param url
	 */
	private Thread downLoadThread;

	private void downloadApk() {
		downLoadThread = new Thread(mdownApkRunnable);
		downLoadThread.start();
	}

	/**
	 * ��װapk
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
		i.setDataAndType(Uri.parse("file://" + apkfile.toString()), "application/vnd.android.package-archive");
		mContext.startActivity(i);
		callback.OnBackResult("finish");

	}

	private int lastRate = 0;
	private Runnable mdownApkRunnable = new Runnable() {
		@Override
		public void run() {
			try {
				URL url = new URL(apkUrl);

				HttpURLConnection conn = (HttpURLConnection) url.openConnection();
				conn.connect();
				int length = conn.getContentLength();
				InputStream is = conn.getInputStream();

				
				File file = new File(savePath);
				if (!file.exists()) {
					file.mkdirs();
				}
				String apkFile = saveFileName;
				File ApkFile = new File(apkFile);
				if(ApkFile.exists()){
					ApkFile.delete();
				}
				ApkFile = new File(apkFile);
				FileOutputStream fos = new FileOutputStream(ApkFile);

				int count = 0;
				byte buf[] = new byte[1024 * 8];

				do {
					int numread = is.read(buf);
					count += numread;
					progress = (int) (((float) count / length) * 100);
					// ���½���
					Message msg = mHandler.obtainMessage();
					msg.what = 1;
					msg.arg1 = progress;
					if (progress >= lastRate + 1) {
						mHandler.sendMessage(msg);
						lastRate = progress;
						if (callback != null)
							callback.OnBackResult(progress);
					}
					if (numread <= 0) {
						// �������֪ͨ��װ
						mHandler.sendEmptyMessage(0);
						// �������ˣ�cancelledҲҪ����
						canceled = true;
						break;
					}
					fos.write(buf, 0, numread);
				} while (!canceled);// ���ȡ����ֹͣ����.

				fos.close();
				is.close();
			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}

		}
	};

}
