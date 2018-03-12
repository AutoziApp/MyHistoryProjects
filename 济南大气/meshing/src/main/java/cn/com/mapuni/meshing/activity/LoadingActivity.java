package cn.com.mapuni.meshing.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.widget.Toast;

import cn.com.mapuni.meshing.base.controls.loading.YutuLoading;
import cn.com.mapuni.meshing.base.interfaces.PathManager;
import cn.com.mapuni.meshing.base.util.BaseAutoUpdate;
import cn.com.mapuni.meshing.netprovider.Net;
import cn.com.mapuni.meshingtotal.R;

public class LoadingActivity extends Activity {
	private final int SPLASH_DISPLAY_LENGHT = 1000; // �ӳ�1��

	private YutuLoading yutuLoading;
	BaseAutoUpdate autoUpdate;
	private final Handler handler = new Handler() {
		@Override
		public void handleMessage(android.os.Message msg) {
			final Intent intent = new Intent(LoadingActivity.this, LoginActivity.class);
			switch (msg.what) {
				case 0:
					if(yutuLoading != null) {
						yutuLoading.dismissDialog();
					}
					Toast.makeText(LoadingActivity.this, "�޷���ȡ�����ļ�,������������!", Toast.LENGTH_SHORT).show();
					intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
					startActivity(intent);
					finish();
					break;
				case 1:
					if(yutuLoading != null) {
						yutuLoading.dismissDialog();
					}
					AlertDialog.Builder builder = new Builder(LoadingActivity.this);
					builder.setTitle("�Զ�����").setMessage("���°汾���Ը��£��Ƿ���£�").setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							Intent intent = new Intent();
							intent.setClassName("cn.com.mapuni.meshingtotal", "cn.com.mapuni.meshing.update.UpdateApkActivity");
							LoadingActivity.this.startActivity(intent);
						}
					}).setNegativeButton("ȡ��", new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss();
							intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
							startActivity(intent);
							finish();
						}
					}).show();
					break;
				case 2:
					if(yutuLoading != null) {
						yutuLoading.dismissDialog();
					}
					intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
					startActivity(intent);
					finish();
//				Toast.makeText(mContext, "���޸�����Ϣ!", Toast.LENGTH_SHORT).show();
					break;
			}
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);// ȥ��������
		setContentView(R.layout.activity_loading);
//		new Handler().postDelayed(new Runnable() {
//			public void run() {
//				Intent intent;
//
//				intent = new Intent(LoadingActivity.this, LoginActivity.class);
//				intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
//				startActivity(intent);
//				finish();
//
//			}
//
//		}, SPLASH_DISPLAY_LENGHT);

		yutuLoading = new YutuLoading(LoadingActivity.this);
		yutuLoading.setCancelable(true);
		yutuLoading.setLoadMsg("���ڼ����£����Ժ�", "");
		yutuLoading.showDialog();

		new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				try {
					Thread.sleep(SPLASH_DISPLAY_LENGHT);
				} catch (Exception e) {
					// TODO: handle exception
				}

				if(!Net.checkURL(PathManager.APK_CODE_URL_JINAN)){
					handler.sendEmptyMessage(0);
					return;
				}

				BaseAutoUpdate autoUpdate = new BaseAutoUpdate();
				int result = autoUpdate.JudgeNewVerson(PathManager.APK_CODE_URL_JINAN, LoadingActivity.this);
				if(result == 1) {
					handler.sendEmptyMessage(1);
				} else {
					handler.sendEmptyMessage(2);
				}
			}
		}).start();

	}

	boolean isFirst = true;
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		if (!isFirst) {
			Intent newIntent = new Intent(LoadingActivity.this, LoginActivity.class);
			newIntent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			startActivity(newIntent);
			finish();
		}
		isFirst = false;
	}
}