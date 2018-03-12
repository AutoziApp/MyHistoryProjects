package com.mapuni.android.login;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.Parcelable;
import android.provider.Settings;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AbsoluteLayout;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mapuni.android.base.Global;
import com.mapuni.android.base.R;
import com.mapuni.android.base.business.BaseApnInfo;
import com.mapuni.android.base.interfaces.PathManager;
import com.mapuni.android.base.service.UpdtaeScriptDownloadConfigService;
import com.mapuni.android.base.util.Apn;
import com.mapuni.android.base.util.DisplayUitl;
import com.mapuni.android.base.util.ExceptionManager;
import com.mapuni.android.dataprovider.ConfigManager;

public class LoginFlashDialog extends Dialog implements DialogInterface {
	/** ������Ϣ���� */
	private Handler mHandler;
	private Message message;

	/** ��¼ģ��Ŀ����� */
	private LoginController loginC = null;

	/** ϵͳ���� */
	private String sysType;

	/** ��Ϣ״ֵ̬ */
	private final int STATUS_ANIMATIONDONE = 0;
	private final int STATUS_DATAPREPARED = 1;
	// ���ӵ�ϵͳ��Ϣ����
	private Looper looper;
	private final Context mContext;

	public LoginFlashDialog(Context context) {
		super(context);
		this.mContext = context;
	}

	public LoginFlashDialog(Context context, int theme, LoginController loginController) {
		super(context);
		this.loginC = loginController;
		this.mContext = context;
	}

	public LoginFlashDialog(Context context, int theme) {
		super(context, theme);
		this.mContext = context;
	}

	protected LoginFlashDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
		super(context, cancelable, cancelListener);
		this.mContext = context;
	}

	@Override
	public void addContentView(View view, LayoutParams params) {
		super.addContentView(view, params);
	}

	@Override
	public void cancel() {
		// super.cancel(); �������̽�ֹ�����˳�dialog
		// �����˳�ʹ��dismiss
	}

	@Override
	public void dismiss() {
		super.dismiss();
	}

	@Override
	public boolean dispatchKeyEvent(KeyEvent event) {
		return super.dispatchKeyEvent(event);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.base_flash);
		if (loginC == null) {
			loginC = LoginController.getInstance(getContext());

		}
		preparHeadler();
		// ׼������
		prepareData();
		// ��ʼ������
		initViews();
	}

	@Override
	protected void onStart() {
		super.onStart();
	}

	@Override
	protected void onStop() {
		super.onStop();
		startServiceToUpdate();
	}

	@Override
	public void setContentView(View view) {
		super.setContentView(view);
	}

	@Override
	public void setTitle(CharSequence title) {
		super.setTitle(title);
	}

	@Override
	public void show() {
		super.show();
	}

	public void preparHeadler() {
		looper = Looper.getMainLooper();
		mHandler = new Handler(looper) {
			boolean isAnimationDone = false;
			boolean isDataPrepared = false;

			@Override
			public void handleMessage(Message msg) {
				switch (msg.what) {
				case STATUS_ANIMATIONDONE:
					isAnimationDone = true;
					break;
				case STATUS_DATAPREPARED:
					isDataPrepared = true;
					break;
				default:
					break;
				}
				if (isAnimationDone && isDataPrepared) {
					// ������½ҳ��Login
					launchActivity();

				}
			}
		};
	}

	/**
	 * Description:׼������ <li>�˷�������һ�߳�ִ�У����Ż�������Ӧ
	 * 
	 * @author Administrator Create at: 2012-12-6 ����09:47:45
	 */
	private void prepareData() {
		// ���õ�ǰϵͳ
		Global global = Global.getGlobalInstance();
		sysType = getSystemType();

		global.setSystemtype(sysType);

		// ���������ݷ�ʽ
		setDesktopShortcut();
		/*
		 * // ����apn Apn apn = new Apn(mContext); apn.OpenApnSettings();
		 */

		// setApn();
		message = new Message();
		message.what = STATUS_DATAPREPARED;
		mHandler.sendMessageDelayed(message, 1500);

		// �����ļ����
		File configfil = new File(PathManager.SDCARD_CONFIG_LOCAL_PATH);
		if (configfil.exists()) {
			// ��config.xml�ļ����ȡ��������ַ������
			HashMap<String, Object> dataMap = DisplayUitl.getDataXML("server");
			if (dataMap == null || dataMap.size() == 0) {
				Toast.makeText(getContext(), "ϵͳ�����ļ����𻵣�����������ļ�", 1).show();
				dismiss();
				android.os.Process.killProcess(android.os.Process.myPid());
				System.exit(0);

			}
			String serverName = dataMap.get("servername").toString();
			String serverAad = dataMap.get("serverurl").toString();
			if (serverName != null && !serverName.equals("")) {// ��config.xml�еõ����÷���������
				global.setSystemname(serverName);
				DisplayUitl.saveAppInfoDataToPreference(getContext(), "system_name", serverName);
			}
			if (serverAad != null && !serverAad.equals("")) {// ��config.xml�еõ����÷�������ַ
				global.setSystemurl(serverAad);
				DisplayUitl.saveAppInfoDataToPreference(getContext(), "url", serverAad);

			}
		} else {
			Toast.makeText(getContext(), "ϵͳȱ�������ļ��޷�����", 1).show();
			dismiss();
			android.os.Process.killProcess(android.os.Process.myPid());
			System.exit(0);

		}
	}

	public void setApn() {
		HashMap<String, Object> dataMap = DisplayUitl.getDataXML("apn_ydzf");
		if (!"true".equalsIgnoreCase(dataMap.get("isedited").toString())) {
			message = new Message();
			message.what = STATUS_DATAPREPARED;
			mHandler.sendMessageDelayed(message, 1500);
		} else {
			if (Build.VERSION.SDK_INT > 10) {
				Intent apnIntent = new Intent(android.provider.Settings.ACTION_APN_SETTINGS);
				getContext().startActivity(apnIntent);
				return;
			}
		}

		Apn apn = new Apn(getContext());
		BaseApnInfo currentApn = apn.getCurrentApn();
		if (currentApn == null) {
			Toast.makeText(getContext(), "��û��ѡ��APN,��ѡ��APN", Toast.LENGTH_LONG).show();
			return;
		}
		// ������ǰapn�ӿ�����
		String currentStr = currentApn.getApnId() + "";
		Global.getGlobalInstance().setCurrentApnId(currentStr);

		// ����ָ��apn
		List<BaseApnInfo> apnList = new ArrayList<BaseApnInfo>();
		apnList = apn.getAllApn();
		BaseApnInfo apnInfo = new BaseApnInfo();
		for (BaseApnInfo apninfo : apnList) {
			if (!(currentStr.equals(apninfo.getApnId())))
				apn.deleteApn(apninfo);
		}

		apnInfo.setName(dataMap.get("name").toString());
		apnInfo.setApnName(dataMap.get("apn").toString());
		apnInfo.setNumeric(dataMap.get("numeric").toString());
		apnInfo.setMcc(dataMap.get("mcc").toString());
		apnInfo.setMnc(dataMap.get("mnc").toString());
		apnInfo.setType(dataMap.get("type").toString());
		apnInfo.setApnId(currentStr);
		apn.updateApn(apnInfo);
		Global.getGlobalInstance().setUpdataapn(true);
		message = new Message();
		message.what = STATUS_DATAPREPARED;
		mHandler.sendMessageDelayed(message, 1500);
	}

	/**
	 * Description:��ʼ����
	 * 
	 * @param Context
	 * @author Administrator Create at: 2012-12-6 ����09:47:41
	 */
	@SuppressWarnings("deprecation")
	private void initViews() {
		// ��ȡ��Ļ���

		Window window = getWindow();
		WindowManager windowManager = window.getWindowManager();
		int screenHeight = windowManager.getDefaultDisplay().getHeight();

		LinearLayout layout = (LinearLayout) findViewById(R.id.flash_out);
		LinearLayout upLayout = new LinearLayout(getContext());
		LinearLayout downLayout = new LinearLayout(getContext());
		TextView mSystemTextView = new TextView(getContext());
		mSystemTextView.setGravity(Gravity.CENTER);

		TextView mNameTextView = new TextView(getContext());
		mNameTextView.setGravity(Gravity.CENTER);
		upLayout.addView(mSystemTextView, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));

		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
		downLayout.addView(mNameTextView, params);
		// ��һ���޸ĺ󣬴�ֵ�ɴ����ݿ����
		mSystemTextView.setText(getSystemName1());
		mNameTextView.setText(getSystemName2());

		mSystemTextView.setTextSize(32);
		mNameTextView.setTextSize(26);

		mSystemTextView.setTextColor(Color.WHITE);
		mNameTextView.setTextColor(Color.WHITE);

		upLayout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
		downLayout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));

		layout.addView(upLayout);
		layout.addView(downLayout);

		LinearLayout bottomLayout = new LinearLayout(getContext());
		TextView cnText = new TextView(getContext());
		TextView enText = new TextView(getContext());
		cnText.setTextColor(Color.WHITE);
		enText.setTextColor(Color.WHITE);
		cnText.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 15);
		enText.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 10);
		cnText.getPaint().setFakeBoldText(true);
		enText.getPaint().setFakeBoldText(true);
		cnText.setGravity(Gravity.CENTER);
		enText.setGravity(Gravity.CENTER);
		bottomLayout.setOrientation(LinearLayout.VERTICAL);
		bottomLayout.addView(cnText, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
		bottomLayout.addView(enText, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
		bottomLayout.setLayoutParams(new AbsoluteLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT, 0, (int) (screenHeight * 0.85)));
		layout.addView(bottomLayout);

		Animation animation = AnimationUtils.loadAnimation(getContext(), R.anim.base_logo_fadein);
		layout.setAnimation(animation);
		FrameLayout fl = (FrameLayout) findViewById(R.id.new_falsh);
		fl.setAnimation(animation);
		animation.setAnimationListener(new Animation.AnimationListener() {

			@Override
			public void onAnimationStart(Animation animation) {

			}

			@Override
			public void onAnimationRepeat(Animation animation) {

			}

			@Override
			public void onAnimationEnd(Animation animation) {
				message = new Message();
				message.what = STATUS_ANIMATIONDONE;
				mHandler.sendMessageDelayed(message, 1000);

			}
		});
	}

	@Override
	public Window getWindow() {
		// TODO Auto-generated method stub
		return super.getWindow();
	}

	public String getSystemName1() {
		String SystemName = "�������";
		try {
			SystemName = ConfigManager.getValue("server", "systemname1");
			if (SystemName == null || SystemName.equals("") || SystemName.equals("null"))
				SystemName = "�������";

		} catch (final Exception e) {
			ExceptionManager.WriteCaughtEXP(e, "��ȡϵͳ����ʧ�ܣ�servername is null ?");
			e.printStackTrace();
		}
		return SystemName;
	}

	public String getSystemName2() {
		try {
			String SystemName = ConfigManager.getValue("server", "systemname2");
			if (SystemName == null || SystemName.equals("") || SystemName.equals("null"))
				return "�ƶ�ִ��ϵͳ";
			return SystemName;
		} catch (final Exception e) {
			ExceptionManager.WriteCaughtEXP(e, "��ȡϵͳ����ʧ�ܣ�servername is null ?");
			e.printStackTrace();
		}
		return "�ƶ�ִ��ϵͳ";
	}

	/**
	 * Description:���������ݷ�ʽ
	 * 
	 * @author Liusy Create at: 2012-12-6 ����10:40:44
	 */
	protected void setDesktopShortcut() {
		SharedPreferences preferences = getContext().getSharedPreferences("isFirstInstall", getContext().MODE_WORLD_WRITEABLE);
		boolean isFirst = preferences.getBoolean("isfrist", true);
		// ��ȡ�ϵ� �汾��
		String oldversion = preferences.getString("bbh", "0");
		// ��ȡ��ǰapk�İ汾��
		String apkbbh = DisplayUitl.getVersionName(this.getContext());
		SharedPreferences.Editor editor = preferences.edit();
		// �������İ汾�ź�apk�İ汾�Ų�һ�������¼����µİ汾��
		if (!apkbbh.equals(oldversion)) {
			editor.putString("bbh", apkbbh);
		}
		if (isFirst) {
			Intent shortcutIntent = new Intent("com.android.launcher.action.INSTALL_SHORTCUT");
			shortcutIntent.putExtra("duplicate", false);
			shortcutIntent.putExtra(Intent.EXTRA_SHORTCUT_NAME, getContext().getString(R.string.app_name));
			Parcelable icon = Intent.ShortcutIconResource.fromContext(getContext(), R.drawable.yutu);
			shortcutIntent.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, icon);
			Intent intent = new Intent();
			intent.setClassName("com.mapuni.android.MobileEnforcement", "com.mapuni.android.main.MainTabActivity");
			intent.setAction("android.intent.action.MAIN");
			intent.addCategory("android.intent.category.LAUNCHER");
			// ������ͼƬ�����еĳ��������
			shortcutIntent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, intent);
			getContext().sendBroadcast(shortcutIntent);
			Toast.makeText(getContext(), "�ƶ�ִ���Ѵ��������ݷ�ʽ", 1).show();
			// ��ȡ�汾��
			String sysbbh = DisplayUitl.getVersionName(this.getContext());
			// Boolean bl=new Boolean(sysbbh);
			// boolean bbh=bl.booleanValue();
			editor.putString("bbh", sysbbh);
		}

		editor.putBoolean("isfrist", false);
		editor.commit();
	}

	/**
	 * Description: ���������ļ�Config.xml��ȡϵͳ ����
	 * 
	 * @return ϵͳ���� String
	 * @author ������ Create at: 2012-12-5 ����06:14:44
	 */
	private String getSystemType() {
		String SystemType = null;
		try {
			SystemType = ConfigManager.getValue("server", "servertype");
			if (SystemType == null || SystemType.equals("") || SystemType.equals("null")) {
				SystemType = "YDZF";
			}
		} catch (final Exception e) {
			SystemType = "YDZF";
			ExceptionManager.WriteCaughtEXP(e, "��ȡϵͳ����ʧ�ܣ�servername is null ?");
			e.printStackTrace();
		}
		return SystemType;
	}

	/**
	 * �磺DisplayUitl.getDataXML("apn_ydzf");
	 * 
	 * @param str
	 * @return
	 */
	protected HashMap<String, Object> getDataXML(String str) {
		return DisplayUitl.getDataXML("apn_ydzf");
	}

	/**
	 * Description:����config��gis_config�����ݿ�ű�
	 * 
	 * @author wanglg
	 * @Create at: 2013-6-3 ����3:42:04
	 */
	private void startServiceToUpdate() {
		Intent intent = new Intent(getContext(), UpdtaeScriptDownloadConfigService.class);

		getContext().startService(intent);
	}

	/**
	 * ��תҳ�浽��½ҳ��
	 */
	protected void launchActivity() {
		// overridePendingTransition(R.anim.fade, R.anim.hold);
		/*
		 * Intent intent = new Intent();
		 * intent.setClassName("com.mapuni.android.MobileEnforcement",
		 * "com.mapuni.android.login.LoginActivity");
		 * context.startActivity(intent);
		 */
		loginC.openDialog(LoginController.HANDLE_OPEN_LoginDialog);

	}

}
