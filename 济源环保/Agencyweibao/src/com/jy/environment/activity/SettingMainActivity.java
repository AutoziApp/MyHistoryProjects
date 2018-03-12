package com.jy.environment.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import cn.jpush.android.api.JPushInterface;

import com.jy.environment.R;
import com.jy.environment.base.ActivityBase;
import com.jy.environment.controls.WeiBaoApplication;
import com.jy.environment.util.MyLog;
import com.jy.environment.util.SharedPreferencesUtil;
import com.jy.environment.util.UpdateManagerUtil;
import com.jy.environment.widget.SwitchButton;
import com.umeng.analytics.MobclickAgent;

/**
 * 更多
 * 
 * @author baiyuchuan
 * 
 */
public class SettingMainActivity extends ActivityBase implements
	OnChangedListener, OnCheckedChangeListener {
    // private MySlipSwitch slipswitch_MSL;
    // 访问通讯录
    private SwitchButton mSlideButton, mSlideButtondw, mSlideButtonNotice,
	    mSlideButtonbeij, mSlideButtondwSms;
    final int NOTIFICATION_ID = 0x1234;
    private RelativeLayout rel_advice;
    private RelativeLayout rel_version;
    private RelativeLayout rel_about, bangzhu, login, yuyinbaobre, offlinemap;
    SharedPreferencesUtil mSpUtil;
    TextView yubao, login_tv, afterLogin;
    WeiBaoApplication mApplication;
    NotificationManager notificationManager;
    private final String SHARE_LOGIN_TAG = "MAP_SHARE_LOGIN_TAG";

    protected void onCreate(Bundle savedInstanceState) {
	// TODO Auto-generated method stub

	super.onCreate(savedInstanceState);
	setContentView(R.layout.setting_main_activity);
	notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
	mSpUtil = SharedPreferencesUtil.getInstance(SettingMainActivity.this);

	mApplication = WeiBaoApplication.getInstance();

	mSlideButton = (SwitchButton) this.findViewById(R.id.mSlideButton);
	mSlideButtondw = (SwitchButton) this.findViewById(R.id.mSlideButtondw);
	mSlideButtonbeij = (SwitchButton) this
		.findViewById(R.id.mSlideButtondbeij);
	mSlideButtonNotice = (SwitchButton) this
		.findViewById(R.id.mSlideButtonNotice);
	mSlideButtondwSms = (SwitchButton) findViewById(R.id.mSlideButtondwSms);
	mSlideButtondwSms.setChecked(mSpUtil.getSmsNotify());
	mSlideButtondwSms.setOnCheckedChangeListener(this);
	mSlideButton.setChecked(mSpUtil.getMsgNotify());
	mSlideButton.setOnCheckedChangeListener(this);

	mSlideButtondw.setChecked(mSpUtil.getisDingwei());
	mSlideButtondw.setOnCheckedChangeListener(this);

	mSlideButtonNotice.setChecked(mSpUtil.getnotificationShow());
	mSlideButtonNotice.setOnCheckedChangeListener(this);

	mSlideButtonbeij.setChecked(mSpUtil.getBeijing());
	mSlideButtonbeij.setOnCheckedChangeListener(this);

	yubao = (TextView) findViewById(R.id.yushengy);
	login_tv = (TextView) findViewById(R.id.login_tv);
	login = (RelativeLayout) findViewById(R.id.login);

	String yu = mSpUtil.getYuy();
	if (yu.equals("xiaoyan")) {
	    yubao.setText("天气播报(普通话(女))");
	} else if (yu.equals("xiaoyu")) {
	    yubao.setText("天气播报(普通话(男))");
	} else if (yu.equals("vixm")) {
	    yubao.setText("天气播报(粤语(女))");
	} else if (yu.equals("vixl")) {
	    yubao.setText("天气播报(台湾(女))");
	} else if (yu.equals("vixr")) {
	    yubao.setText("天气播报(四川(女))");
	} else if (yu.equals("vixyun")) {
	    yubao.setText("天气播报(东北(女))");
	} else if (yu.equals("vixk")) {
	    yubao.setText("天气播报(河南(男))");
	} else if (yu.equals("vixqa")) {
	    yubao.setText("天气播报(湖南(男))");
	} else {
	    yubao.setText("天气播报(普通话(女))");
	}

	offlinemap = (RelativeLayout) findViewById(R.id.offLineMapSet);
	offlinemap.setOnClickListener(new OnClickListener() {

	    @Override
	    public void onClick(View arg0) {
		Intent intent_offline = null;
		intent_offline = new Intent(SettingMainActivity.this,
			SettingOfflineMapActivity.class);
		startActivity(intent_offline);
	    }
	});

	yuyinbaobre = (RelativeLayout) findViewById(R.id.yuyinbaobre);
	yuyinbaobre.setOnClickListener(new OnClickListener() {

	    @Override
	    public void onClick(View arg0) {
		// TODO Auto-generated method stub

		AlertDialog.Builder builder = new AlertDialog.Builder(
			SettingMainActivity.this)
			.setTitle("选择语言播报人")
			.setItems(
				new String[] { "普通话(女)", "普通话(男)", "粤语(女)",
					"台湾(女)", "四川(女)", "东北(女)", "河南(男)",
					"湖南(男)" },
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

	    }
	});

	login.setOnClickListener(new OnClickListener() {

	    @Override
	    public void onClick(View v) {
		// TODO Auto-generated method stub
		Intent loginIntent = new Intent(SettingMainActivity.this,
			UserLoginActivity.class);
		startActivity(loginIntent);
	    }
	});

	bangzhu = (RelativeLayout) findViewById(R.id.bangzhu);
	bangzhu.setOnClickListener(new OnClickListener() {

	    @Override
	    public void onClick(View v) {
		// TODO Auto-generated method stub

		Intent intent = new Intent(SettingMainActivity.this,
			WelcomePagerActivity.class);
		intent.putExtra("bangzhu", "bangzhu");
		startActivity(intent);
	    }
	});
	rel_advice = (RelativeLayout) findViewById(R.id.advice);
	rel_advice.setOnClickListener(new OnClickListener() {
	    public void onClick(View v) {
		// TODO Auto-generated method stub
		Intent intent = new Intent(SettingMainActivity.this,
			SettingFeedbackActivity.class);
		startActivity(intent);
	    }
	});
	// app=(MyApp) getApplication();
	rel_version = (RelativeLayout) findViewById(R.id.rel_version);
	rel_version.setOnClickListener(new OnClickListener() {

	    @Override
	    public void onClick(View v) {
		UpdateManagerUtil.getUpdateManager().checkAppUpdate(
			SettingMainActivity.this, true);
	    }
	});
	rel_about = (RelativeLayout) findViewById(R.id.rel_about);
	rel_about.setOnClickListener(new OnClickListener() {

	    @Override
	    public void onClick(View v) {
		Intent intent2 = new Intent(SettingMainActivity.this,
			SettingAboutActivity.class);
		startActivity(intent2);
	    }
	});
    }

    protected void dialogUpdate() {
	AlertDialog dialog = new AlertDialog.Builder(SettingMainActivity.this)
		.setTitle("提示信息").setMessage("已是最新版本，不需要更新")
		.setPositiveButton("确定", new DialogInterface.OnClickListener() {
		    @Override
		    public void onClick(DialogInterface dialog, int which) {
			// TODO Auto-generated method stub
			dialog.dismiss();
		    }
		}).create();
	dialog.show();
    }

    public void OnChanged(boolean CheckState) {
	String push = null;
	if (CheckState) {
	    push = "开启";
	} else {
	    push = "关闭";
	}

	Toast.makeText(this, "推送已经" + push, Toast.LENGTH_SHORT).show();

	// 获取系统的NotificationManager服务
	NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
	if (CheckState) {
	    PendingIntent pi = PendingIntent.getActivity(
		    SettingMainActivity.this, 0, null, 0);
	    // 创建一个Notification
	    Notification notify = new Notification();
	    // 为Notification设置图标，该图标显示在状态栏
	    notify.icon = R.drawable.notify;
	    // 为Notification设置文本内容，该文本会显示在状态栏
	    notify.tickerText = "启动推送的通知";
	    notify.setLatestEventInfo(SettingMainActivity.this, "普通通知", "点击查看",
		    pi);
	    // 发送通知
	    notificationManager.notify(NOTIFICATION_ID, notify);
	} else {

	    notificationManager.cancel(NOTIFICATION_ID);
	}
    }

    @Override
    protected void onResume() {
	// TODO Auto-generated method stub
	super.onResume();
	String usename = mApplication.getUsename().toString();
	String usenaNc = mApplication.getUserNc();
	MyLog.i("usenaNc" + usenaNc);
	if (!usename.equals("")) {
	    login_tv.setText(usename);
	    login.setOnClickListener(new OnClickListener() {
		@Override
		public void onClick(View v) {
		    Intent intentz = new Intent(SettingMainActivity.this,
			    UserInfoActivity.class);
		    startActivity(intentz);
		}
	    });

	    if (!usenaNc.equals("")) {
		login_tv.setText(usenaNc);
		login.setOnClickListener(new OnClickListener() {
		    @Override
		    public void onClick(View v) {
			Intent intentz = new Intent(SettingMainActivity.this,
				UserInfoActivity.class);
			startActivity(intentz);
		    }
		});

	    }

	} else {
	    login_tv.setText("登  录");
	    login.setOnClickListener(new OnClickListener() {

		@Override
		public void onClick(View v) {
		    Intent loginIntent = new Intent(SettingMainActivity.this,
			    UserLoginActivity.class);
		    startActivity(loginIntent);
		}
	    });
	}

	MobclickAgent.onResume(this);
    }
    
    

    @Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		MobclickAgent.onPause(this);
	}



	long waitTime = 2000;
    long touchTime = 0;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

	if (event.getAction() == KeyEvent.ACTION_DOWN
		&& keyCode == KeyEvent.KEYCODE_BACK) {// 当keyCode等于退出事件值时
	// ToQuitTheApp();
	    long currentTime = System.currentTimeMillis();
	    if ((currentTime - touchTime) >= waitTime) {
		Toast.makeText(this, "再按一次退出", Toast.LENGTH_SHORT).show();
		touchTime = currentTime;
	    } else {
		finish();
		Intent intent = new Intent(
			WeiBaoApplication.LOCATION_SERVICEINTENT);
		SettingMainActivity.this.stopService(intent);
		System.exit(0);
	    }
	    return true;
	} else {
	    return super.onKeyDown(keyCode, event);
	}
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
	// TODO Auto-generated method stub
	switch (buttonView.getId()) {
	case R.id.mSlideButton:
	    mSpUtil.setMsgNotify(isChecked);
	    if (isChecked) {
		Toast.makeText(getApplicationContext(), "开启", 0).show();
		JPushInterface.resumePush(SettingMainActivity.this);
	    } else {
		Toast.makeText(getApplicationContext(), "关闭", 0).show();
		JPushInterface.stopPush(SettingMainActivity.this);
	    }

	    break;
	case R.id.mSlideButtondbeij:

	    mSpUtil.SetBeijing(isChecked);
	    if (isChecked) {
		Toast.makeText(getApplicationContext(), "开启", 0).show();
		JPushInterface.resumePush(SettingMainActivity.this);
	    } else {
		Toast.makeText(getApplicationContext(), "关闭", 0).show();
		JPushInterface.stopPush(SettingMainActivity.this);
	    }

	    break;
	case R.id.mSlideButtonNotice:
	    mSpUtil.setnotificationShow(isChecked);
	    if (isChecked) {
		Toast.makeText(getApplicationContext(), "开启", 0).show();

	    } else {
		Toast.makeText(getApplicationContext(), "关闭", 0).show();
		notificationManager.cancel(WeiBaoApplication.NOTIFICATION_ID1);

	    }

	    break;
	case R.id.mSlideButtondw:
	    mSpUtil.setIsDingwei(isChecked);
	    if (isChecked) {
		Toast.makeText(getApplicationContext(), "开启", 0).show();
		// JPushInterface.resumePush(MoreActivity.this);
	    } else {
		Toast.makeText(getApplicationContext(), "关闭", 0).show();
		// JPushInterface.stopPush(MoreActivity.this);
	    }

	    break;
	case R.id.mSlideButtondwSms:
	    mSpUtil.setSmsNotify(isChecked);
	    if (isChecked) {
		Toast.makeText(SettingMainActivity.this, "开启", 0).show();
	    } else {
		Toast.makeText(SettingMainActivity.this, "关闭", 0).show();
	    }
	}
    }
}