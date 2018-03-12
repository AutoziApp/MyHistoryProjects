package com.jy.environment.activity;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.jy.environment.R;
import com.jy.environment.base.ActivityBase;
import com.umeng.analytics.MobclickAgent;

/**
 * 关于界面
 * 
 * @author baiyuchuan
 * 
 */
public class SettingAboutActivity extends ActivityBase {
	private ImageView img_about;
	private TextView verson;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.setting_about_activity);
		img_about = (ImageView) findViewById(R.id.img_about);
		verson = (TextView) findViewById(R.id.verson);
		verson.setText("空气质量" + getVersion(this));
		img_about.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				finish();
			}
		});
	}

	public static String getVersion(Context context)// 获取版本号
	{
		try {
			PackageInfo pi = context.getPackageManager().getPackageInfo(
					context.getPackageName(), 0);
			return pi.versionName;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
			return "1.0";
		}
	}

	@Override
	protected void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		MobclickAgent.onResume(this);
	}
	
	
}
