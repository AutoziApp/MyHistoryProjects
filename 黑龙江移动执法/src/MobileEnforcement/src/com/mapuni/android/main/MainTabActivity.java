package com.mapuni.android.main;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;

import org.dom4j.DocumentException;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.TabActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.text.TextUtils.TruncateAt;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TabHost;
import android.widget.Toast;

import com.mapuni.android.base.Global;
import com.mapuni.android.base.R;
import com.mapuni.android.base.business.BaseObjectFactory;
import com.mapuni.android.base.controls.loading.YutuLoading;
import com.mapuni.android.base.interfaces.IInitData;
import com.mapuni.android.base.util.Apn;
import com.mapuni.android.base.util.DisplayUitl;
import com.mapuni.android.base.util.LogUtil;
import com.mapuni.android.business.RWXX;
import com.mapuni.android.dataprovider.ConfigManager;
import com.mapuni.android.dataprovider.XmlHelper;
import com.mapuni.android.login.InstallResource;
import com.mapuni.android.login.InstallResource.Loadlisenter;
import com.mapuni.android.login.LoginController;
import com.mapuni.android.netprovider.WebServiceProvider;
import com.mapuni.android.service.RydwServices;
import com.mapuni.android.taskmanager.TaskInformationActivity;
import com.mapuni.android.thirdpart.SpeakUtil;

public class MainTabActivity extends TabActivity implements OnCheckedChangeListener, Loadlisenter {

	private static final String TAG = "MainTabActivity";

	private int ifstar = 0;
	private RadioGroup mainTab;
	private TabHost mTabHost;
	Intent intent, tempIntent;
	private YutuLoading loading;
	public final int YUTU_LOADING = 103;
	public final int YUTU_HBT_STAR = 104;
	public final int YUTU_HBT_LOGIN = 105;

	@SuppressWarnings("unused")
	private final String MAIN = "MainTabActivity";

	protected Handler main_handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case YUTU_LOADING:
				Log.e("hello", "---YUTU_LOADING---");
				if (loading != null) {
					loading.dismissDialog();
				}
				break;
			case YUTU_HBT_STAR:
				ifstar = 1;
				Global.getGlobalInstance().setAppStatus(Global.APP_LOGIN_DURING);
				loginC.openDialog(loginC.HANDLE_OPEN_LoginFlashDialog);
				break;
			case YUTU_HBT_LOGIN:
				if (loading != null) {
					loading.dismissDialog();
				}
	
             //wsc	2014.11.13			
				// ���ö�λ����
				DisplayUitl.saveAppInfoDataToPreference(MainTabActivity.this,
						"userid", Global.getGlobalInstance().getUserid());
//				Intent loctionnewIntent = new Intent(MainTabActivity.this,
//						RydwServices.class);
//				MainTabActivity.this.startService(loctionnewIntent);
				// ���õ�½�ӿ�
				callLogin();
	           //wsc	2014.11.13				
				InitView();
				break;
			}
		}
	};
	/** ��½ģ��Ŀ����� */
	LoginController loginC = LoginController.getInstance(MainTabActivity.this, main_handler);
	
	private void callLogin() {
		new Thread(new Runnable() {

			@Override
			public void run() {
				String methodName = "SaveLoginInfo";
				ArrayList<HashMap<String, Object>> params = new ArrayList<HashMap<String, Object>>();
				HashMap<String, Object> param = new HashMap<String, Object>();
				param.put("LoginUserId", Global.getGlobalInstance().getUserid());
				LogUtil.i("LoginUserId", "LoginUserId:"+Global.getGlobalInstance().getUserid());
				params.add(param);

//				try {
//					String callWebService = (String)WebServiceProvider.callWebService(Global.NAMESPACE,
//							methodName, params, Global.getGlobalInstance()
//									.getSystemurl() + Global.WEBSERVICE_URL,
//							WebServiceProvider.RETURN_STRING, true);
//				} catch (IOException e) {
//					e.printStackTrace();
//				}
			}
		}).start();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		LogUtil.i(TAG, "onCreate  !");
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.maintab);
		Unzip_mapuni();
	
	}

	@Override
	protected void onDestroy() {
		LogUtil.i(TAG, "onDestroy   !");
		super.onDestroy();

	}

	@Override
	protected void onStart() {
		LogUtil.i(TAG, "onStart  !");
		super.onStart();

	}

	@Override
	protected void onStop() {
		super.onStart();
	}

	@Override
	protected void onResume() {
		LogUtil.i(TAG, "onStart  !");
		super.onResume();
		if (Global.getGlobalInstance().getAppStatus() == Global.APP_RUN_NORMAL) {
			LogUtil.i(TAG, "onResume  FLOATING_WINDOW_SHOW !");
		}
	}

	@Override
	protected void onPause() {
		super.onPause();
		LogUtil.i(TAG, "onPause  !");
	}

	public void InitView() {
		mainTab = (RadioGroup) findViewById(R.id.main_tab);
//		mainTab.setBackgroundResource(R.drawable.maintab_toolbar_bg);
		mainTab.setBackgroundResource(com.mapuni.android.MobileEnforcement.R.drawable.bg_radiogroup_normal);
		this.mTabHost = getTabHost();
		loading = new YutuLoading(this);
		loading.setLoadMsg("�����У����Ժ󡤡���", "");
		loading.setCancelable(true);
		loading.showDialog();
		loadLayout();
		mainTab.setOnCheckedChangeListener(this);
		loginC.openDialog(LoginController.HANDLE_HIDE_LoginDismiss);
	}

	@Override
	public void onCheckedChanged(final RadioGroup group, final int checkedId) {
		RadioButton radioBtn = (RadioButton) group.findViewById(checkedId);

		@SuppressWarnings("unchecked")
		final HashMap<String, Object> menu = (HashMap<String, Object>) radioBtn.getTag();
		final String tabname = menu.get("tabname").toString();
		String btnName = menu.get("menuname").toString();
		String imagename = menu.get("imagename").toString();
		String ywl = menu.get("ywl").toString();
		Intent intent = (Intent) menu.get("intent");
		tempIntent = initdata(intent, ywl);
		setTabUpIntent(tabname, btnName, tempIntent, imagename);
		this.mTabHost.setCurrentTabByTag(tabname);
	}

	public int getWindowManagerWidth(int btnCount) {
		WindowManager wm = (WindowManager) this.getSystemService(Context.WINDOW_SERVICE);
		int width = wm.getDefaultDisplay().getWidth();// ��Ļ���
		if (width != 0 && btnCount != 0) {
			return width / btnCount;
		}
		return 0;
	}

	/** ����tabҳ�Ĳ��� */
	public void loadLayout() {
		ArrayList<HashMap<String, Object>> tabMenu;
		tabMenu = getMoreMenu("main_tabmenu.xml", "item");

		ArrayList<HashMap<String, Object>> tabMenus = new ArrayList<HashMap<String, Object>>();
		for (HashMap<String, Object> map : tabMenu) {
			String menuqxid = map.get("qxid").toString();
			if (DisplayUitl.getAuthority(menuqxid)) {
				tabMenus.add(map);
			}
		}
		int width = getWindowManagerWidth(tabMenus.size());
		for (HashMap<String, Object> menu : tabMenus) {
			int order = Integer.parseInt(menu.get("order").toString());
			String btnName = menu.get("menuname").toString();
			String menuqxid = menu.get("qxid").toString();
			String imagename = menu.get("imagename").toString();
			String contextname = menu.get("contextname").toString();
			String classname = menu.get("classname").toString();
			String ywl = menu.get("ywl").toString();
			boolean ifchecked = Boolean.parseBoolean(menu.get("ifchecked").toString());
			String tabname = menu.get("tabname").toString();
			if (DisplayUitl.getAuthority(menuqxid)) {
				LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(width, LayoutParams.WRAP_CONTENT);
				RadioButton Tab = new RadioButton(MainTabActivity.this);
				Tab.setChecked(ifchecked);
				Tab.setId(order);
				intent = prepareIntent(contextname, classname);
				tempIntent = intent;
				menu.put("intent", tempIntent);
				Tab.setTag(menu);
				setStyle(Tab, imagename);
				mainTab.addView(Tab, order, params);
				setImg(Tab, btnName);
				tempIntent = initdata(intent, ywl);
				setTabUpIntent(tabname, btnName, tempIntent, imagename);
			} else {
				continue;
			}

		}   
		if (loading != null) {
			loading.dismissDialog();
		}
	}

	public void setTabUpIntent(String tabname, String btnName, Intent intent, String imagename) {
		setupIntent(tabname, btnName, tempIntent);
	}

	/**
	 * ��radionButton������ʽ
	 * 
	 * @param radioBtn
	 */
	public void setStyle(RadioButton radioBtn, String imagename) {
		radioBtn.setTextSize(0);
		radioBtn.setTextColor(Color.parseColor("#FFFFFF"));
		radioBtn.setEllipsize(TruncateAt.MARQUEE);
		radioBtn.setGravity(Gravity.CENTER_HORIZONTAL);
		radioBtn.isChecked();
		radioBtn.setPadding(0, 2, 0, 0);
		radioBtn.setButtonDrawable(android.R.color.transparent);// ȥ��ǰ�ߵ�Բ��
		radioBtn.setSingleLine(true);
		radioBtn.setBackgroundDrawable(getResources().getDrawable(geBitmaptRes(imagename)));

	}

	/**
	 * Description: ��ȡ����˵������ļ�����
	 * 
	 * @param xml
	 *            xml�ļ�������
	 * @param nodeFather
	 *            ���ڵ�
	 * @return ArrayList<HashMap<String, Object>>
	 * @author ����� Create at: 2013-4-15 ����11:20:34
	 */
	protected ArrayList<HashMap<String, Object>> getMoreMenu(String xml, String nodename) {
		ArrayList<HashMap<String, Object>> MoreMenu = null;
		InputStream stream = null;
		try {
			stream = MainTabActivity.this.getResources().getAssets().open(xml);
			MoreMenu = XmlHelper.getList(stream, nodename);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (DocumentException e) {
			e.printStackTrace();
		}
		return MoreMenu;

	}

	/**
	 * ��ʼ����ť������
	 * 
	 * @param rwglIntent
	 * @param ywl
	 *            �����ҵ���������
	 * @return
	 */
	public Intent initdata(Intent ywlintent, String ywl) {
		IInitData business = null;
		Intent intent = null;
		try {
			if (ywl.equals("ZHCXXX")) {
				business = (IInitData) BaseObjectFactory.createinfoQueryObject(ywl);
			} else {
				business = (IInitData) BaseObjectFactory.createObject(ywl);
			}
			intent = business.InitData(this, ywlintent, ywl);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		}

		return intent;
	}

	/**
	 * �������������Ƴ��Ի���
	 */
	@Override
	public boolean dispatchKeyEvent(KeyEvent event) {
		if (event.getKeyCode() == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
			if (RWXX.isInBackgroundUpload) {
				Builder dialog = new AlertDialog.Builder(this);
				dialog.setTitle("ϵͳ�˳�");
				dialog.setMessage("�������ں�̨�ϴ��������ĵȴ�����");
				dialog.setNegativeButton("��", null);
				dialog.show();
			} else {
				Builder dialog = new AlertDialog.Builder(this);
				dialog.setTitle("ϵͳ�˳�");
				dialog.setMessage("ȷ��Ҫ�˳�ϵͳ��");
				dialog.setPositiveButton("��", new btnExitListener());
				dialog.setNegativeButton("��", null);
				dialog.show();
			}

		}
		return super.dispatchKeyEvent(event);
	}

	/**
	 * �˳�ϵͳ
	 * 
	 * @author Administrator
	 * 
	 */
	private class btnExitListener implements android.content.DialogInterface.OnClickListener {
		@Override
		public void onClick(DialogInterface arg0, int arg1) {
			if (Build.VERSION.SDK_INT <= 10 && Global.getGlobalInstance().isUpdataapn()) {
				Apn apn = new Apn(MainTabActivity.this);
				apn.setReturnAPN();
			}
			appExit();
		}
	}

	/**
	 * Description: �������˳��������� void
	 * 
	 * @author ����� Create at: 2012-12-5 ����02:25:21
	 */
	public void appExit() {
		Intent loctionnewIntent = new Intent(this,
				RydwServices.class);
		stopService(loctionnewIntent);
		finish();
		android.os.Process.killProcess(android.os.Process.myPid());
		System.exit(0);
	}

	/**
	 * ׼��tab������Intent
	 */
	private Intent prepareIntent(String pakegeName, String classname) {// ʵ������spec��intent
		Class<?> obj = null;
		try {
			obj = Class.forName(pakegeName + "." + classname);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		Intent intent = new Intent(this, obj);
		return intent;
	}

	/**
	 * 
	 */
	private void setupIntent(String tabname, String menuname, Intent intent) {
		TabHost localTabHost = this.mTabHost;
		localTabHost.addTab(buildTabSpec(tabname, menuname, tempIntent));
	}

	/**
	 * ����TabHost��Tabҳ
	 * 
	 * @param tag
	 *            ���
	 * @param resLabel
	 *            ��ǩ
	 * @param resIcon
	 *            ͼ��
	 * @param content
	 *            ��tabչʾ������
	 * @return һ��tab
	 */
	private TabHost.TabSpec buildTabSpec(String tag, String resLabel, final Intent content) {
		return this.mTabHost.newTabSpec(tag).setIndicator(resLabel, null).setContent(tempIntent);
	}

	/**
	 * ���ͼƬ
	 * 
	 */
	private void setImg(RadioButton radioBtn, String btnName) {
		radioBtn.setText(btnName);
		radioBtn.setVisibility(View.VISIBLE);
	}

	/**
	 * Description: ��ȡ�б��ͼƬid
	 * 
	 * @param name
	 *            ��Ƭ������
	 * @return ����������Ƭid Bitmap
	 * @author xgf Create at: 2012-11-30 ����11:30:37
	 */
	public int geBitmaptRes(String name) {
		ApplicationInfo appInfo = this.getApplicationInfo();
		int resID = this.getResources().getIdentifier(name, "drawable", appInfo.packageName);
		return resID;
	}

	public void LoadResult(int status) {
		if (status < 0) {
			// ��Դ����ʧ�ܣ���ʾ���˳�
			Toast.makeText(this, "ϵͳ��ʼ��ʧ��", 1).show();
			Log.e("hello", "ϵͳ��ʼ��ʧ��");
			android.os.Process.killProcess(android.os.Process.myPid());
			System.exit(0);
		} else {
			// ��ʼ��ʼ�����Լ����߼�
			main_handler.sendEmptyMessage(YUTU_HBT_STAR);
			Log.d("lkzsjkdlj", "suc");
			Log.e("hello", "��ʼ��ʼ�����Լ����߼�");
		}

	}

	/**
	 * ��ѹ�����ļ�
	 */
	private void Unzip_mapuni() {

		String mapuniPath = Environment.getExternalStorageDirectory() + File.separator + "mapuni";

		String configPath = Environment.getExternalStorageDirectory() + File.separator + "mapuni/MobileEnforcement/data/config.xml";

		File configFile = new File(configPath);

		String currVersion = DisplayUitl.getVersionName(this.getApplicationContext());

		String configVersion = ConfigManager.getValue("config", "versonnum");

		if (!configFile.exists() || !configVersion.equals(currVersion)) {
			Log.e("hello", "��ѹ�����ļ���ѹ�����ļ�if");
			InstallResource installResource = new InstallResource(this);
			installResource.SetLisenter(this);
			installResource.Start(mapuniPath);
		} else {
			if (ifstar == 0) {
				main_handler.sendEmptyMessage(YUTU_HBT_STAR);
				Log.e("hello", "��ѹ�����ļ���ѹ�����ļ�else");
			}

		}
	}
					
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			appExit();
			return true;
		}
		return false;
	}

}
