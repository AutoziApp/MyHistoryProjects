package com.jy.environment.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import cn.jpush.android.api.JPushInterface;

import com.jy.environment.R;
import com.jy.environment.base.ActivityBase;
import com.jy.environment.util.SharedPreferencesUtil;
import com.jy.environment.util.ToastUtil;
import com.jy.environment.widget.SwitchButton;

public class SettingUpActivity extends ActivityBase implements
		OnCheckedChangeListener, OnClickListener {
	private SwitchButton mSlideButton1, mSlideButton2, mSlideButton3,
			mSlideButton4;
	private SharedPreferencesUtil mSpUtil;
	private RelativeLayout yuyinbaobre;
	private TextView yubao;
	private ImageView afterlogin_return_iv;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.setting_set_activity);
		mSpUtil = SharedPreferencesUtil.getInstance(SettingUpActivity.this);
		init();
	}

	private void init() {
		afterlogin_return_iv = (ImageView) findViewById(R.id.afterlogin_return_iv);
		afterlogin_return_iv.setOnClickListener(this);
		yubao = (TextView) findViewById(R.id.yubao);
		yuyinbaobre = (RelativeLayout) findViewById(R.id.yuyinbaobre);
		yuyinbaobre.setOnClickListener(this);
		mSlideButton1 = (SwitchButton) findViewById(R.id.mSlideButton1);
		mSlideButton1.setChecked(mSpUtil.getMsgNotify());
		mSlideButton1.setOnCheckedChangeListener(this);
		mSlideButton2 = (SwitchButton) findViewById(R.id.mSlideButton2);
		mSlideButton2.setChecked(mSpUtil.getBeijing());
		mSlideButton2.setOnCheckedChangeListener(this);
		mSlideButton3 = (SwitchButton) findViewById(R.id.mSlideButton3);
		mSlideButton3.setChecked(mSpUtil.getnotificationShow());
		mSlideButton3.setOnCheckedChangeListener(this);
		mSlideButton4 = (SwitchButton) findViewById(R.id.mSlideButton4);
		mSlideButton4.setChecked(mSpUtil.getSmsNotify());
		mSlideButton4.setOnCheckedChangeListener(this);
	}

	private void msb(int i, boolean isChecked) {
		// TODO Auto-generated method stub
		if (i == 0) {
			mSpUtil.setMsgNotify(isChecked);
			if (isChecked) {
				ToastUtil.showShort(this, "开启");
				JPushInterface.resumePush(this);
			} else {
				ToastUtil.showShort(this, "关闭");
				JPushInterface.stopPush(this);
			}
		} else if (i == 1) {
			mSpUtil.SetBeijing(isChecked);
			String msg = isChecked ? "开启" : "关闭";
			ToastUtil.showShort(this, msg);
		} else if (i == 2) {
			mSpUtil.setnotificationShow(isChecked);
			String msg = isChecked ? "开启" : "关闭";
			ToastUtil.showShort(this, msg);
		} else {
			mSpUtil.setSmsNotify(isChecked);
			String msg = isChecked ? "开启" : "关闭";
			ToastUtil.showShort(this, msg);
		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.yuyinbaobre:
			AlertDialog.Builder builder = new AlertDialog.Builder(
					SettingUpActivity.this)
					.setTitle("选择语言播报人")
					.setItems(
							new String[] { "普通话(女)", "普通话(男)", "粤语(女)",
									"台湾(女)", "四川(女)", "东北(女)", "河南(男)", "湖南(男)" },
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									// TODO Auto-generated method stub
									switch (which) {
									case 0:
										mSpUtil.SetYuy("xiaoyan");
										yubao.setText("天气播报(普通话(女))");
										break;
									case 1:
										mSpUtil.SetYuy("xiaoyu");
										yubao.setText("天气播报(普通话(男))");
										break;

									case 2:
										mSpUtil.SetYuy("vixm");
										yubao.setText("天气播报(粤语(女))");
										break;
									case 3:
										mSpUtil.SetYuy("vixl");
										yubao.setText("天气播报(台湾(女))");
										break;
									case 4:
										mSpUtil.SetYuy("vixr");
										yubao.setText("天气播报(四川(女))");
										break;
									case 5:
										mSpUtil.SetYuy("vixyun");
										yubao.setText("天气播报(东北(女))");
										break;
									case 6:
										mSpUtil.SetYuy("vixk");
										yubao.setText("天气播报(河南(男))");
										break;
									case 7:
										mSpUtil.SetYuy("vixqa");
										yubao.setText("天气播报(湖南(男))");
										break;

									default:
										break;
									}
								}
							})

					.setNegativeButton("取消", new Dialog.OnClickListener() {

						@Override
						public void onClick(DialogInterface arg0, int arg1) {
							// TODO Auto-generated method stub
							arg0.dismiss();

						}

					});
			builder.create().show();
			break;
		case R.id.afterlogin_return_iv:
			finish();
			break;

		default:
			break;
		}
	}

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		// TODO Auto-generated method stub
		switch (buttonView.getId()) {
		case R.id.mSlideButton1:
			msb(0, isChecked);
			break;
		case R.id.mSlideButton2:
			msb(1, isChecked);
			break;
		case R.id.mSlideButton3:
			msb(2, isChecked);
			break;
		case R.id.mSlideButton4:
			msb(3, isChecked);
			break;
		default:
			break;
		}
	}

}
