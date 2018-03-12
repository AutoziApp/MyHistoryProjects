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
 * FileName: DownloadService.java Description:ͬ������
 * 
 * @author ������
 * @Version 1.3.4
 * @Copyright �п���ͼ���¿Ƽ����޹�˾ Create at: 2012-12-3 ����09:36:31
 */
public class DownloadService extends Service {
	/** ��־��¼��ǩ */
	private final String TAG = "DownloadService";

	/** ��Ϣ״̬����ֵ */
	private final int CHANGE_STATE = 0; // ״̬�����ı�
	private final int DOWNLOAD_FAIL = 1; // ״̬�����ı�
	private final int COMPLETE_ALL = 2; // ���ݱ�ȫ��ͬ�����

	private String notificationTile = ""; // ״̬���ı���
	private String currentState = ""; // ����״̬

	private boolean isComplete = false; // �Ƿ��������

	private Intent intent = null;
	private ConditionVariable mCondition = null;
	protected IList businessObj = null;
	private Bundle bundle = null;
	private int res;

	private TextProgressBar bar;
	private AlertDialog dialog;

	boolean updateOrFetchAllData;
	/** ����ͬ��ʧ����־ */
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

		// ��ȡ��ֵ
		if (intent == null) {
			return super.onStartCommand(intent, flags, startId);

		}
		this.intent = intent;

		bundle = intent.getExtras();
		businessObj = (IList) bundle.getSerializable("BusinessObj");

		notificationTile = bundle.getString("notificationTile");

		boolean isLaunch = bundle.getBoolean("islaunch");// �ս������ʱ��������ı�־

		if (!isLaunch) {

			// ͬ������ʱ���������������ݽ���ͬ��
			// ��ʼ��״̬��

			// �������ع����У����֪ͨ�����ص�������

			// ��������ͼ��ı仯
			Thread notifyingThread = new Thread(null, mTask, "DownloadService");
			mCondition = new ConditionVariable(false);
			notifyingThread.start();

			// ����һ���µ��߳����أ����ʹ��Serviceͬ�����أ��ᵼ��ANR���⣬Service����Ҳ������
			new Thread(new DownloadRunnable()).start();

		}

		return super.onStartCommand(intent, flags, startId);
	}

	/** ������Ϣ��Handler */
	private Handler downloadHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {

			switch (msg.what) {
			case CHANGE_STATE:
				// ��Ϣ��ʾ��ǰ״̬
				// notification.defaults = Notification.DEFAULT_SOUND;//��������
				break;
			case DOWNLOAD_FAIL:
				isComplete = true;
				Global.IsDataSync = false;
				// ��Ϣ��ʾ��ǰ״̬
				Toast.makeText(getApplicationContext(), "ͬ�������쳣������ͣͬ��,��鿴����״̬�Ƿ�������", Toast.LENGTH_LONG).show();
				if (dialog != null) {
					dialog.dismiss();
				}
				showSyncLog();
				stopService(intent);
				break;
			case COMPLETE_ALL:
				// �޸�״̬��ͼ��Ϊ��ͬ�����״̬
				isComplete = true;
				Global.IsDataSync = false;
				Toast.makeText(getApplicationContext(), "ͬ���ɹ�", Toast.LENGTH_LONG).show();
				// �����Ѿ�Ϊ���£�û��Ҫͬ��������
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
	 * ֪ͨ������ͼ�꣬�����߳̿���
	 */
	private Runnable mTask = new Runnable() {
		public void run() {
			long blockTime = 150;
			long beginend = 120;
			while (!isComplete) {// δ���������ˢ��֪ͨ
				/* ����ͼ��Ķ�̬���� */
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
	 * FileName: DownloadService.java Description:�����߳�
	 * 
	 * @author Administrator
	 * @Version 1.3.4
	 * @Copyright �п���ͼ���¿Ƽ����޹�˾ Create at: 2012-12-3 ����09:58:01
	 */
	class DownloadRunnable implements Runnable {

		/* ��ȡ��Ϣ */
		Message message = downloadHandler.obtainMessage();

		@Override
		public void run() {

			// ͬ�������ݲ���
			updateOrFetchAllData = bundle.getBoolean("updateOrFetchAllData");
			String tables = bundle.getString("tables");
			// BaseDataSync dataSync = (BaseDataSync) businessObj;
			DataSyncModel dataSync = new DataSyncModel();
			String[] tablesArray = tables.split(",");

			for (int count = 0; count < tablesArray.length; count++) {
				// String table=tablesArray[count];
				String tablename = tablesArray[count];
				try {
					// ��֪ͨ����ʾͬ������������

					// ��ʾ��ǰͬ���ı�
					message = downloadHandler.obtainMessage();
					message.what = CHANGE_STATE;
					notificationTile = "[" + tablename + "] (" + (count + 1) + "/" + tablesArray.length + ")";
					// notificationTile ="(" + (count + 1)
					// + "/" + tablesArray.length + ")";
					downloadHandler.sendMessage(message);

					res = dataSync.synchronizeFetchServerData(updateOrFetchAllData, tablename);

					// ͬ����ɣ�������ʾ��Ϣ
					switch (res) {
					case DataSyncModel.SYNC_FAIL:
						if (Net.checkURL(Global.getGlobalInstance().getSystemurl())) {// ������������ͬ��ʣ�µı�
							message = downloadHandler.obtainMessage();
							message.what = CHANGE_STATE;
							notificationTile = "���ݱ�[" + tablename + "](" + (count + 1) + "/" + tablesArray.length + ")--ͬ��ʧ��";
							syncLog.append(notificationTile + "\n");
						} else {
							message = downloadHandler.obtainMessage();
							message.what = DOWNLOAD_FAIL;
							notificationTile = "���ݱ�[" + tablename + "](" + (count + 1) + "/" + tablesArray.length + ")--ͬ��ʧ��";
							syncLog.append(notificationTile + "\n");
							return;
						}

						break;
					case DataSyncModel.SYNC_SERVICE_ERR:
						if (Net.checkURL(Global.getGlobalInstance().getSystemurl())) {// ������������ͬ��ʣ�µı�
							message = downloadHandler.obtainMessage();
							message.what = CHANGE_STATE;
							notificationTile = "���ݱ�[" + tablename + "](" + (count + 1) + "/" + tablesArray.length + ")--��ȡ����������쳣";
							syncLog.append(notificationTile + "\n");
							downloadHandler.sendMessage(message);
						} else {
							message = downloadHandler.obtainMessage();
							message.what = DOWNLOAD_FAIL;
							notificationTile = "���ݱ�[" + tablename + "](" + (count + 1) + "/" + tablesArray.length + ")--��ȡ����������쳣";
							syncLog.append(notificationTile + "\n");
							downloadHandler.sendMessage(message);
							return;
						}

						break;
					case DataSyncModel.SYNC_TIMEOUT:// ��ʱ
						message = downloadHandler.obtainMessage();
						message.what = DOWNLOAD_FAIL;
						notificationTile = "���ݱ�[" + tablename + "](" + (count + 1) + "/" + tablesArray.length + ")--���ӷ�������ʱ";
						syncLog.append(notificationTile + "\n");
						downloadHandler.sendMessage(message);
						return;

					case 0:
						// û��Ҫͬ��������
						message = downloadHandler.obtainMessage();
						message.what = CHANGE_STATE;
						notificationTile = "���ݱ�[" + tablename + "] (" + (count + 1) + "/" + tablesArray.length + ")--����������";
						syncLog.append(notificationTile + "\n");
						downloadHandler.sendMessage(message);
						break;

					default:
						// ���سɹ�
						message = downloadHandler.obtainMessage();
						message.what = CHANGE_STATE;

						notificationTile = "���ݱ�[" + tablename + "](" + (count + 1) + "/" + tablesArray.length + ")--ͬ���ɹ�";
						syncLog.append(notificationTile + " ��Ӱ������" + res + "\n");
						downloadHandler.sendMessage(message);

						break;
					}

				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					LogUtil.i(TAG, tablename + "ͬ���쳣");
					message = downloadHandler.obtainMessage();
					message.what = CHANGE_STATE;
					notificationTile = "���ݱ�[" + tablename + "](" + (count + 1) + "/" + tablesArray.length + ")--ͬ��ʧ��";
					syncLog.append(notificationTile + "\n");
				}

				bar.setProgress((int) (((count + 0.0) / tablesArray.length) * 100));
			}
			// ��Ϣ��ʾͬ�������еı�

			message = downloadHandler.obtainMessage();
			message.what = COMPLETE_ALL;
			notificationTile = "����ͬ�����";
			downloadHandler.sendMessage(message);

		}

	}

	/**
	 * չʾͬ����־
	 */
	private void showSyncLog() {
		if (syncLog.length() == 0) {
			return;
		}
		AlertDialog.Builder ab = new AlertDialog.Builder(DownloadService.this);
		ab.setTitle("ͬ����־");
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

		ab.setNegativeButton("ȷ��", new DialogInterface.OnClickListener() {

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
		LogUtil.i(TAG, "DownloadService��������");
		super.onDestroy();
		this.stopSelf();
	}

}
