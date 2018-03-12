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

	/** ��־��¼��ǩ */
	private final String TAG = "DownloadActivity";

	/** ��Ϣ״̬����ֵ */
	private final int CHANGE_STATE = 0; // ״̬�����ı�
	private final int DOWNLOAD_FAIL = 1; // ״̬�����ı�
	private final int COMPLETE_ALL = 2; // ���ݱ�ȫ��ͬ�����

	private String notificationTile = ""; // ״̬���ı���
	private String currentState = ""; // ����״̬

	private Intent intent = null;
	protected IList businessObj = null;
	private Bundle bundle = null;
	private int res;

	private TextProgressBar bar;

	boolean updateOrFetchAllData;
	Button button;

	Boolean cancel = true;
	/** ����ͬ��ʧ����־ */
	private StringBuffer syncLog = new StringBuffer();

	// ��װһ������
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
				OtherTools.showToast(DownloadActivity.this, "����ȡ��...");
				Global.IsDataSync = false;
			}
		});

		this.intent = getIntent();

		bundle = intent.getExtras();
		businessObj = (IList) bundle.getSerializable("BusinessObj");

		notificationTile = bundle.getString("notificationTile");

		boolean isLaunch = bundle.getBoolean("islaunch");// �ս������ʱ��������ı�־

		if (!isLaunch) {
			// ����һ���µ��߳����أ����ʹ��Serviceͬ�����أ��ᵼ��ANR���⣬Service����Ҳ������
			new Thread(new DownloadRunnable()).start();

		}
	}

	/** ������Ϣ��Handler */
	private Handler downloadHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {

			switch (msg.what) {
			case CHANGE_STATE:
				break;
			case DOWNLOAD_FAIL:
				Global.IsDataSync = false;
				// ��Ϣ��ʾ��ǰ״̬
				Toast.makeText(getApplicationContext(), "ͬ�������쳣������ͣͬ��,��鿴����״̬�Ƿ�������", Toast.LENGTH_LONG).show();
				showSyncLog();
				break;
			case COMPLETE_ALL:
				// �޸�״̬��ͼ��Ϊ��ͬ�����״̬
				Global.IsDataSync = false;
				if (!cancel) {
					OtherTools.showToast(getApplicationContext(), "ͬ����ȡ��");
					finish();
				} else {
					OtherTools.showToast(getApplicationContext(), "ͬ���ɹ�");
				}
				showSyncLog();
				break;
			default:
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

			for (int count = 0; count < tablesArray.length && cancel; count++) {
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

				bar.setProgress((int) (((count + 1.0) / tablesArray.length) * 100));
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
		if (syncLog.length() == 0 || !cancel) {
			return;
		}
		AlertDialog.Builder ab = new AlertDialog.Builder(DownloadActivity.this);
		ab.setTitle("ͬ����־");
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

		ab.setNegativeButton("ȷ��", new OnClickListener() {

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