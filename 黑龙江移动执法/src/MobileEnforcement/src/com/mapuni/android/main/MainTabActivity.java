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
				// 启用定位服务
				DisplayUitl.saveAppInfoDataToPreference(MainTabActivity.this,
						"userid", Global.getGlobalInstance().getUserid());
//				Intent loctionnewIntent = new Intent(MainTabActivity.this,
//						RydwServices.class);
//				MainTabActivity.this.startService(loctionnewIntent);
				// 调用登陆接口
				callLogin();
	           //wsc	2014.11.13				
				InitView();
				break;
			}
		}
	};
	/** 登陆模块的控制器 */
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
		loading.setLoadMsg("加载中，请稍后···", "");
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
		int width = wm.getDefaultDisplay().getWidth();// 屏幕宽度
		if (width != 0 && btnCount != 0) {
			return width / btnCount;
		}
		return 0;
	}

	/** 加载tab页的布局 */
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
	 * 给radionButton设置样式
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
		radioBtn.setButtonDrawable(android.R.color.transparent);// 去除前边的圆点
		radioBtn.setSingleLine(true);
		radioBtn.setBackgroundDrawable(getResources().getDrawable(geBitmaptRes(imagename)));

	}

	/**
	 * Description: 获取更多菜单配置文件数据
	 * 
	 * @param xml
	 *            xml文件的名称
	 * @param nodeFather
	 *            根节点
	 * @return ArrayList<HashMap<String, Object>>
	 * @author 王红娟 Create at: 2013-4-15 上午11:20:34
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
	 * 初始化按钮的数据
	 * 
	 * @param rwglIntent
	 * @param ywl
	 *            所需的业务类的名字
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
	 * 按撤销键弹出推出对话框
	 */
	@Override
	public boolean dispatchKeyEvent(KeyEvent event) {
		if (event.getKeyCode() == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
			if (RWXX.isInBackgroundUpload) {
				Builder dialog = new AlertDialog.Builder(this);
				dialog.setTitle("系统退出");
				dialog.setMessage("任务正在后台上传，请耐心等待结束");
				dialog.setNegativeButton("是", null);
				dialog.show();
			} else {
				Builder dialog = new AlertDialog.Builder(this);
				dialog.setTitle("系统退出");
				dialog.setMessage("确定要退出系统吗？");
				dialog.setPositiveButton("是", new btnExitListener());
				dialog.setNegativeButton("否", null);
				dialog.show();
			}

		}
		return super.dispatchKeyEvent(event);
	}

	/**
	 * 退出系统
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
	 * Description: 由子类退出方法调用 void
	 * 
	 * @author 王红娟 Create at: 2012-12-5 下午02:25:21
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
	 * 准备tab的内容Intent
	 */
	private Intent prepareIntent(String pakegeName, String classname) {// 实例化子spec的intent
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
	 * 构建TabHost的Tab页
	 * 
	 * @param tag
	 *            标记
	 * @param resLabel
	 *            标签
	 * @param resIcon
	 *            图标
	 * @param content
	 *            该tab展示的内容
	 * @return 一个tab
	 */
	private TabHost.TabSpec buildTabSpec(String tag, String resLabel, final Intent content) {
		return this.mTabHost.newTabSpec(tag).setIndicator(resLabel, null).setContent(tempIntent);
	}

	/**
	 * 添加图片
	 * 
	 */
	private void setImg(RadioButton radioBtn, String btnName) {
		radioBtn.setText(btnName);
		radioBtn.setVisibility(View.VISIBLE);
	}

	/**
	 * Description: 获取列表的图片id
	 * 
	 * @param name
	 *            照片的名字
	 * @return 返回所需照片id Bitmap
	 * @author xgf Create at: 2012-11-30 上午11:30:37
	 */
	public int geBitmaptRes(String name) {
		ApplicationInfo appInfo = this.getApplicationInfo();
		int resID = this.getResources().getIdentifier(name, "drawable", appInfo.packageName);
		return resID;
	}

	public void LoadResult(int status) {
		if (status < 0) {
			// 资源加载失败，提示并退出
			Toast.makeText(this, "系统初始化失败", 1).show();
			Log.e("hello", "系统初始化失败");
			android.os.Process.killProcess(android.os.Process.myPid());
			System.exit(0);
		} else {
			// 开始初始化你自己的逻辑
			main_handler.sendEmptyMessage(YUTU_HBT_STAR);
			Log.d("lkzsjkdlj", "suc");
			Log.e("hello", "开始初始化你自己的逻辑");
		}

	}

	/**
	 * 解压部署文件
	 */
	private void Unzip_mapuni() {

		String mapuniPath = Environment.getExternalStorageDirectory() + File.separator + "mapuni";

		String configPath = Environment.getExternalStorageDirectory() + File.separator + "mapuni/MobileEnforcement/data/config.xml";

		File configFile = new File(configPath);

		String currVersion = DisplayUitl.getVersionName(this.getApplicationContext());

		String configVersion = ConfigManager.getValue("config", "versonnum");

		if (!configFile.exists() || !configVersion.equals(currVersion)) {
			Log.e("hello", "解压部署文件解压部署文件if");
			InstallResource installResource = new InstallResource(this);
			installResource.SetLisenter(this);
			installResource.Start(mapuniPath);
		} else {
			if (ifstar == 0) {
				main_handler.sendEmptyMessage(YUTU_HBT_STAR);
				Log.e("hello", "解压部署文件解压部署文件else");
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
