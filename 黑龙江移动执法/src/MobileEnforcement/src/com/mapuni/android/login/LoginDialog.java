package com.mapuni.android.login;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Notification;
import android.app.AlertDialog.Builder;
import android.content.ComponentName;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.provider.Settings;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.android.pushservice.CustomPushNotificationBuilder;
import com.baidu.android.pushservice.PushConstants;
import com.baidu.android.pushservice.PushManager;
import com.mapuni.android.MobileEnforcement.R;
import com.mapuni.android.baidupush.Utils;
import com.mapuni.android.base.Global;
import com.mapuni.android.base.business.BaseDataSync;
import com.mapuni.android.base.business.BaseUsers;
import com.mapuni.android.base.controls.loading.YutuLoading;
import com.mapuni.android.base.interfaces.PathManager;
import com.mapuni.android.base.util.Apn;
import com.mapuni.android.base.util.DisplayUitl;
import com.mapuni.android.base.util.LogUtil;
import com.mapuni.android.dataprovider.ConfigManager;
import com.mapuni.android.dataprovider.DESSecurity;
import com.mapuni.android.dataprovider.JsonHelper;
import com.mapuni.android.dataprovider.SqliteUtil;
import com.mapuni.android.main.MainTabActivity;
import com.mapuni.android.netprovider.Net;
import com.mapuni.android.netprovider.WebServiceProvider;
import com.mapuni.android.service.MessageService;
import com.mapuni.android.thirdpart.SpeakUtil;

public class LoginDialog extends Dialog {

	/** 登录模块的控制器 */
	private LoginController loginController = null;
	// private Context context = null;

	private final String TAG = "Login";
	private Global global;
	/** 登录按钮 */
	private Button login_btn;
	/** 输入用户名 */
	private AutoCompleteTextView actv;
	/** 记住密码 */
	private CheckBox remember_pwd;
	/** 自动登录 */
	private CheckBox auto_login;
	/** 输入密码 */
	private EditText pwd_edt;
	/** 展示记录密码的用户 */
// private ImageButton down_list_imagebtn;
	/** 登录弹出框 */
	private YutuLoading yutuLoading;
	/** 记住密码的用户集合 */
	private List<String> list;
	/** 记住密码的标识 */
	private boolean REMEMBER = false;
	/** 自动登录判断标志,从数据库中读取，默认false */
	private boolean AUTO = false;

	/** 当前用户不存在 */
	private final int NOUSER = 0;
	/** 密码错误 */
	private final int PASSWORDERR = 1;
	/** 无网络连接 */
	private final int NONET = 2;
	/** 登录成功 */
	private final int LOGIN_SUCCESS = 4;
	/** 非法字符 */
	private final int INPUT_LAWLESS = 5;
	/** 是不是yutu用户登录 */
	private Boolean isYUTU_USER = false;

	private HashMap<String, Object> dataMap = null;
	/** 系统名称 */
	private String serverName = "";
	/** 服务器路径 */
	private String serverAad = "";

	// 系统设置
	private TextView login_sz;

	private AlertDialog dialog;

	/** 当前登录用户名 */
	private String user;
	/** 当前登录用户名密码 */
	private String pass;
	private BaseUsers users_instance;
	private BaseDataSync dataSync;

	/** 存放记住密码的所有用户名和密码的SP name */
	private final String USERS_SP_NAME = "userinfo";
	/** 最后登录的用户信息SP name */
	private final String LAST_USER_SP_NAME = "lastuser";

	/**
	 * 获得登录发送过来的消息做出反应
	 */
	protected Handler login_handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			if (yutuLoading != null && msg.what != LOGIN_SUCCESS) {
				yutuLoading.dismissDialog();
			}
			switch (msg.what) {
			case NOUSER:
				Toast.makeText(getContext(), "当前用户不存在", Toast.LENGTH_LONG).show();
				break;
			case PASSWORDERR:
				Toast.makeText(getContext(), "用户密码输入错误", Toast.LENGTH_LONG).show();
				break;
			case NONET:
				Toast.makeText(getContext(), "无网络连接", Toast.LENGTH_LONG).show();
				break;
			case LOGIN_SUCCESS:
				loginController.hideDialog(LoginController.HANDLE_HIDE_LoginDialog);
				break;
			case INPUT_LAWLESS:
				Toast.makeText(getContext(), "输入用户名或密码存在非法字符", Toast.LENGTH_LONG).show();
				break;
			}
		}
	};
	private Context context;

	public LoginDialog(Context context) {
		super(context);
		 this.context = context;
	}

	public LoginDialog(Context context, int theme, LoginController loginController) {
		super(context);
		this.loginController = loginController;
		 this.context = context;
	}

	public LoginDialog(Context context, int theme) {
		super(context, theme);
		 this.context = context;
	}

	protected LoginDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
		super(context, cancelable, cancelListener);
		 this.context = context;
	}

	@Override
	public void addContentView(View view, LayoutParams params) {
		super.addContentView(view, params);
	}

	@Override
	public void cancel() {
		// super.cancel(); 开机过程禁止按键退出dialog
		// 代码退出使用dismiss
	}

	@Override
	public void dismiss() {
		super.dismiss();
	}

	@Override
	public boolean dispatchKeyEvent(KeyEvent event) {
		return super.dispatchKeyEvent(event);
	}

	/**
	 * 按两次返回键退出程序
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {// && event.getAction() ==
												// KeyEvent.ACTION_DOWN
			if ((System.currentTimeMillis() - exitTime) > 2000) {
				Toast.makeText(getContext(), "再按一次退出登录", Toast.LENGTH_SHORT).show();
				exitTime = System.currentTimeMillis();
			} else {
				dismiss();
				android.os.Process.killProcess(android.os.Process.myPid());
				System.exit(0);
			}
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.base_login);
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		if (loginController == null) {
			loginController = LoginController.getInstance(getContext());
			// loginController = new LoginController(context);
		}

	
		global = Global.getGlobalInstance();
		initView();
		users_instance = new BaseUsers();
		dataSync = new BaseDataSync();
		list = getAllUser();

		ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(),
				android.R.layout.simple_dropdown_item_1line, list);

		actv.setAdapter(adapter);
		// 记住密码checkbox监听
		remember_pwd.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean isChecked) {
				REMEMBER = isChecked;
			}
		});

		// 自动登录功能
		auto_login.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
				AUTO = arg1;
			}
		});

		setItemClickListener(actv, remember_pwd, pwd_edt);
		autoLogin(actv, pwd_edt, auto_login);
	//	 down_list_imagebtn.setOnClickListener(clickListener);// 显示记录的用户名
		login_btn.setOnClickListener(clickListener);// 登录监听
		
		
	
		

		login_sz.setOnClickListener(new android.view.View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				LayoutInflater li = LayoutInflater.from(getContext());
				View v = li.inflate(R.layout.setsystem, null);
				Button system = (Button) v.findViewById(R.id.system);
				Button clean = (Button) v.findViewById(R.id.clean);
				Button apn = (Button) v.findViewById(R.id.apn);
				system.setOnClickListener(new android.view.View.OnClickListener() {

					@Override
					public void onClick(View arg0) {
						dialog.dismiss();
						setServerHost();
					}
				});
				clean.setOnClickListener(new android.view.View.OnClickListener() {

					@Override
					public void onClick(View arg0) {
						dialog.dismiss();
						clearCache();
						list.clear();
						ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(),
								android.R.layout.simple_dropdown_item_1line, list);
						actv.setAdapter(adapter);
						Toast.makeText(getContext(), "本地缓存信息已删除！", Toast.LENGTH_SHORT).show();
					}
				});
				apn.setOnClickListener(new android.view.View.OnClickListener() {

					@Override
					public void onClick(View arg0) {
						dialog.dismiss();
						Intent apnSetIntent = new Intent(Settings.ACTION_APN_SETTINGS);
						getContext().startActivity(apnSetIntent);
					}
				});

				AlertDialog.Builder ab = new AlertDialog.Builder(getContext());
				ab.setTitle("系统设置");
				ab.setIcon(getContext().getResources().getDrawable(R.drawable.base_icon_mapuni_white));
				ab.setNegativeButton("取消", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {

					}

				});
				ab.setView(v);
				dialog = ab.create();

				dialog.show();

			}
		});


	
	
	}

	@Override
	protected void onStart() {
		super.onStart();
	}

	@Override
	protected void onStop() {
		super.onStop();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add(0, 0, 0, "系统设置");
		menu.add(0, 1, 0, "清除缓存");
		menu.add(0, 2, 0, "APN设置");
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		switch (item.getItemId()) {
		case 0:
			setServerHost();
			break;
		case 1:
			clearCache();
			list.clear();
			ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(),
					android.R.layout.simple_dropdown_item_1line, list);
			actv.setAdapter(adapter);
			Toast.makeText(getContext(), "本地缓存信息已删除！", Toast.LENGTH_SHORT).show();
			break;
		case 2:
			Intent apnSetIntent = new Intent(Settings.ACTION_APN_SETTINGS);
			this.getContext().startActivity(apnSetIntent);
			break;
		}
		return super.onOptionsItemSelected(item);
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

	/**
	 * 实例化控件
	 */
	private void initView() {
		login_btn = (Button) findViewById(R.id.login_btn_login);
		actv = (AutoCompleteTextView) findViewById(R.id.login_edit_account);
		actv.setTextColor(Color.BLACK);
		pwd_edt = (EditText) findViewById(R.id.login_edit_pwd);
		pwd_edt.setTextColor(Color.BLACK);
		remember_pwd = (CheckBox) findViewById(R.id.login_cb_visible);
		login_sz = (TextView) findViewById(R.id.login_sz);

		auto_login = (CheckBox) findViewById(R.id.login_cb_openvibra);
			// down_list_imagebtn = (ImageButton)findViewById(R.id.down_list_ImageButton);
		
		yutuLoading = new YutuLoading(getContext());
		yutuLoading.setLoadMsg("登录中，请稍等...", "登录失败，请稍候重试！");
		
		setSystemLogo();
	
		
	}

	/**
	 * Description:设置服务器地址
	 * 
	 * @author 王振洋 Create at: 2012-12-4 下午4:16:18
	 */
	public void setServerHost() {
		File configfil = new File(PathManager.SDCARD_CONFIG_LOCAL_PATH);
		LayoutInflater li = LayoutInflater.from(getContext());
		View v = li.inflate(R.layout.setserverhost, null);

		final EditText serveradd = (EditText) v.findViewById(R.id.setserverhost);
		final EditText servername = (EditText) v.findViewById(R.id.setservername);
		servername.setFocusableInTouchMode(false);

		if (configfil.exists()) {
			dataMap = DisplayUitl.getDataXML("server");
			serverName = dataMap.get("servername").toString();
			serverAad = dataMap.get("serverurl").toString();
			servername.setText(serverName);
			serveradd.setText(serverAad);
		} else {
			servername.setText("");
			serveradd.setText("");
		}

		AlertDialog.Builder ab = new AlertDialog.Builder(getContext());
		ab.setTitle("系统设置");
		ab.setIcon(getContext().getResources().getDrawable(R.drawable.base_icon_mapuni_white));
		ab.setPositiveButton("确定", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				String serverurl = "";
				String surl;
				String serveraddress = serveradd.getText().toString().trim();
				String servicername = servername.getText().toString().trim().toUpperCase();

				if (serveraddress.endsWith("/")) {
					surl = serveraddress.substring(0, serveraddress.length() - 1);
				} else {
					surl = serveraddress;
				}

				if (!surl.contains("http://")) {
					serverurl = "http://" + surl;
				} else {
					serverurl = surl;
				}

				Global.getGlobalInstance().setSystemurl(serverurl);

				Global.getGlobalInstance().setSystemname(servicername);

				if (!servicername.equalsIgnoreCase(serverName) || !serveraddress.equalsIgnoreCase(serverAad)) {
					HashMap<String, String> map = new HashMap<String, String>();
					map.put("servername", servicername);
					map.put("serverurl", serverurl);
					ConfigManager.setConfigValues(map);
				}
			}
		});

		ab.setNegativeButton("取消", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {

			}

		});
		ab.setView(v);
		final AlertDialog ad = ab.create();
		// ad.getWindow().setType(
		// WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
		ad.show();
	}

	/**
	 * 捕获手机物理菜单键
	 */
	private long exitTime = 0;

	/** 获取所有记住密码的用户名 */
	protected List<String> getAllUser() {

		return users_instance
				.getAllUser(getContext().getSharedPreferences(USERS_SP_NAME, getContext().MODE_WORLD_WRITEABLE));
	}

	/** 设置输入用户名点击监听 */
	protected void setItemClickListener(AutoCompleteTextView actv, final CheckBox remember_pwd,
			final EditText pwd_edt) {
		actv.setOnItemClickListener(new OnItemClickListener() {// AutoCompleteText监听
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				String nowuser = String.valueOf(arg0.getItemAtPosition(arg2));
				pwd_edt.setText(DisplayUitl.readPreferences(getContext(), USERS_SP_NAME, nowuser));
				remember_pwd.setChecked(true);

			}
		});

	}

	/**
	 * 清除SharedPreferences内容
	 */
	protected void clearCache() {
		getContext().getSharedPreferences(USERS_SP_NAME, getContext().MODE_WORLD_WRITEABLE).edit().clear().commit();
		getContext().getSharedPreferences(LAST_USER_SP_NAME, getContext().MODE_WORLD_WRITEABLE).edit().clear().commit();

	}

	/** 默认显示最后一位登录登录用户和密码，如果设置自动登录，则默认登录按钮被点击 */
	protected void autoLogin(AutoCompleteTextView actv, EditText pwd_edt, CheckBox auto_login) {

		String user = DisplayUitl.readPreferences(getContext().getApplicationContext(), LAST_USER_SP_NAME, "user");
		String pwd = DisplayUitl.readPreferences(getContext().getApplicationContext(), LAST_USER_SP_NAME, "pass");
		for (String userStr : list) {// 判断最后一次登录用户是否记住密码
			if (userStr.equals(user)) {
				actv.setText(user);
				pwd_edt.setText(pwd);
				if (!user.equals("")) {
					remember_pwd.setChecked(true);
				}
			}
		}

	}

	/**
	 * 登录成功后调用 重新最后登录人，如果勾选记住密码，记入本地SP存储
	 * 
	 * @param user
	 *            登录名
	 * @param pass
	 *            密码
	 */
	protected void setRemember(String user, String pass) {

		DisplayUitl.writePreferences(getContext(), LAST_USER_SP_NAME, "user", user);
		DisplayUitl.writePreferences(getContext(), LAST_USER_SP_NAME, "pass", pass);

		// 设置记忆判断标志
		if (REMEMBER) {
			DisplayUitl.writePreferences(getContext(), USERS_SP_NAME, user, pass);

		} else {
			if (getContext().getSharedPreferences(USERS_SP_NAME, getContext().MODE_PRIVATE).getString(user, "")
					.equals(pass)) {
				getContext().getSharedPreferences(USERS_SP_NAME, getContext().MODE_PRIVATE).edit().remove(user)
						.commit();

			}
		}
	}

	/**
	 * Description:根据不同的系统设置Logo
	 * 
	 * @author 王振洋 Create at: 2012-12-4 下午4:18:38
	 */
	protected void setSystemLogo() {

		ImageView sysLogoImageView = (ImageView) findViewById(R.id.faceImga);
		/** 发布系统时，请将默认设置为目标系统 */
		 int imgResId = R.drawable.base_logo_hlj;
		//int imgResId = R.drawable.login_logo;
		sysLogoImageView.setImageResource(imgResId);
	}

	public void speakWelcome() {
		Intent intent = new Intent();
		intent.setAction("com.mapuni.android.loginwelcome.speak");
		getContext().sendBroadcast(intent);
	}

	/**
	 * 同步最新user表，判断登录用户是否为新增用户
	 * 
	 * @param user
	 * @param pass
	 * @param userlist
	 */
	private void getNewUserList(String user, String pass, ArrayList<HashMap<String, Object>> userlist) {

		BaseDataSync bds = new BaseDataSync();
		try {
			bds.synchronizeFetchServerData(true, "PC_Users");
		} catch (IOException e) {

			e.printStackTrace();
		}
		userlist = users_instance.checkUsers(user, pass);
		if (userlist != null && userlist.size() > 0) {// 后台新增此用户，同步此用户权限
			final String[] tables = { "PC_UserModelPession" };
			// 同步用户权限表以及任务相关信息
			for (int i = 0; i < tables.length; i++) {
				try {
					bds.synchronizeFetchServerData(true, tables[i]);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

	}

	/**
	 * 检测错误原因
	 */
	private void checkFailReason() {
		// 根据用户名查询输入密码是否正确
		ArrayList<HashMap<String, Object>> userl = users_instance.checkPwd(user);
		if (userl != null && userl.size() != 0) {// 存在用户，密码输入错误
			login_handler.sendEmptyMessage(PASSWORDERR);
		} else {// 不存在此用户
			login_handler.sendEmptyMessage(NOUSER);
		}
		Looper.loop();
	}

	/**
	 * 有网络的时候，在线登录 登录成功，初始化登录用户信息,发送登录成功消息
	 */
	private void initLoginUserInfo(ArrayList<HashMap<String, Object>> list) {
		global.initGlobalData(list);
		setRemember(user, pass);
		// 登陆百度云
		loginBaiDu();
		login_handler.sendEmptyMessage(LOGIN_SUCCESS);
		Looper.loop();
	}

	/**
	 * 用于主界面取消Login等待框
	 */
	public void dismissLoginDialog() {
		login_handler.sendEmptyMessage(-1);
	}

	/**
	 * 没有网络的时候，离线登录 登录成功，初始化登录用户信息,发送登录成功消息
	 */
	private void initLoginUserInfo() {
		global.initGlobalData(user, pass);
		setRemember(user, pass);
		// 登陆百度云
		loginBaiDu();
		login_handler.sendEmptyMessage(LOGIN_SUCCESS);
		Looper.loop();
	}

	/**
	 * 登录成功，登陆百度云,获取百度账号id和设备id,并判断是否已保存
	 */
	private void loginBaiDu() {
		Resources resource = getContext().getResources();
		String pkgName = getContext().getPackageName();
		// Push: 以apikey的方式登录，一般放在主Activity的onCreate中。
		// 这里把apikey存放于manifest文件中，只是一种存放方式，
		// 您可以用自定义常量等其它方式实现，来替换参数中的Utils.getMetaValue(PushDemoActivity.this,
		// "api_key")
		// 通过share preference实现的绑定标志开关，如果已经成功绑定，就取消这次绑定
		if (!Utils.hasBind(getContext().getApplicationContext())) {
			try {
				PushManager.startWork(getContext().getApplicationContext(), PushConstants.LOGIN_TYPE_API_KEY,
						Utils.getMetaValue(getContext(), "api_key"));
			} catch (Exception e) {
				e.printStackTrace();
			}
			// Push: 如果想基于地理位置推送，可以打开支持地理位置的推送的开关
			// PushManager.enableLbs(getContext().getApplicationContext());
		}

		// Push: 设置自定义的通知样式，具体API介绍见用户手册，如果想使用系统默认的可以不加这段代码
		// 请在通知推送界面中，高级设置->通知栏样式->自定义样式，选中并且填写值：1，
		// 与下方代码中 PushManager.setNotificationBuilder(this, 1, cBuilder)中的第二个参数对应
		CustomPushNotificationBuilder cBuilder = new CustomPushNotificationBuilder(getContext().getApplicationContext(),
				R.layout.activity_baidu, R.id.image_icon, R.id.TextView_title, R.id.TextView_contents);
		cBuilder.setNotificationFlags(Notification.FLAG_AUTO_CANCEL);
		cBuilder.setNotificationDefaults(Notification.DEFAULT_ALL);// .DEFAULT_SOUND
																	// |
																	// Notification.DEFAULT_VIBRATE);
		cBuilder.setStatusbarIcon(getContext().getApplicationInfo().icon);
		cBuilder.setLayoutDrawable(resource.getIdentifier("simple_notification_icon", "drawable", pkgName));
		PushManager.setDefaultNotificationBuilder(getContext().getApplicationContext(), cBuilder);
	}

	/**
	 * Description:开启后台工作服务
	 * 
	 * @author 王振洋 Create at: 2012-12-4 下午4:12:27
	 */
	private void doMessagePush() {

		Intent intent = new Intent();
		intent.setAction("com.mapuni.action.launch_service");
		intent.putExtra("operation", "start");
		getContext().sendBroadcast(intent);

	}

	/**
	 * 登录点击事件监听
	 */
	android.view.View.OnClickListener clickListener = new android.view.View.OnClickListener() {

		@Override
		public void onClick(View v) {
			if (DisplayUitl.isFastDoubleClick()) {
				return;
			}
			int id = v.getId();
			

			
//			 if (id == R.id.down_list_ImageButton) { if (actv == null)
//			  System.out.println("!!!!!!!!!!actv is Null!!!!!!!!!!!!");
//			  actv.showDropDown(); } else 
				  if (id == R.id.login_btn_login) {

				if (TextUtils.isEmpty(actv.getText()) || TextUtils.isEmpty(pwd_edt.getText())) {
					Toast.makeText(getContext(), "账号或密码不能为空!", Toast.LENGTH_SHORT).show();
					return;
				}
				user = actv.getText().toString();
				user = user.toLowerCase();
				pass = pwd_edt.getText().toString();

				// if (user.equals("yutu") && pass.equals("mapuni")) {
				// isYUTU_USER = true;
				// }

				yutuLoading.showDialog();
			//	login_btn.setText("登录中...");
				new Thread(new Runnable() {

					@Override
					public void run() {
						Looper.prepare();
						// if (isYUTU_USER) {
						// initLoginUserInfo();
						// return;
						// }
						if (Net.checkURL(global.getSystemurl())) {
							String methodName = "MobileLoginVerify";
							ArrayList<HashMap<String, Object>> params = new ArrayList<HashMap<String, Object>>();
							HashMap<String, Object> map = new HashMap<String, Object>();
							String token = "";
							try {
								token = DESSecurity.encrypt(methodName);

								map.put("loginName", user);
								map.put("password", pass);
								map.put("token", token);
								params.add(map);
								LogUtil.i(TAG, "loginName:"+user+"loginName:"+pass+"token:"+token);

								String jsonStr = (String) WebServiceProvider.callWebService(Global.NAMESPACE,
										methodName, params,
										Global.getGlobalInstance().getSystemurl() + Global.WEBSERVICE_URL,
										WebServiceProvider.RETURN_STRING, true);
								if (jsonStr == null) {
									 if (yutuLoading != null) {
									 yutuLoading.dismissDialog();
									 }
//									 Toast.makeText(getContext(),
//									 "后台地址填写错误，请重新输入！",
//									 Toast.LENGTH_LONG).show();
//									 Looper.loop();
//									 return;
									ArrayList<HashMap<String, Object>> userlist = users_instance.checkUsers(user, pass);
									if (userlist == null) {
										login_handler.sendEmptyMessage(INPUT_LAWLESS);
										Looper.loop();
										return;
									}
									if (userlist.size() == 0) {// 本地判断输入账号或密码错误
										if (Net.checkURL(global.getSystemurl())) {// 联网判断当前输入用户是否存在
											getNewUserList(user, pass, userlist);
											if (userlist.size() > 0) {
												initLoginUserInfo();
											} else {
												checkFailReason();
											}
										} else {
											checkFailReason();

										}
									} else {// 本地存在此用户
										initLoginUserInfo();
									}
									return;
								}
								if ("-1".equals(jsonStr)) {
									if (yutuLoading != null) {
										yutuLoading.dismissDialog();
									}
									Toast.makeText(getContext(), "用户名或密码错误，请重新输入！", Toast.LENGTH_LONG).show();
									Looper.loop();

								} else {

									ArrayList<HashMap<String, Object>> list = JsonHelper.paseJSON(jsonStr);
									if (list != null && list.size() > 0) {
										HashMap<String, Object> hash = list.get(0);
										String userid = hash.get("UserID").toString();
										String useridSelect = SqliteUtil.getInstance().getDepidByUserid(
												"select U_LoginName from PC_Users where  UserID='" + userid + "'");
										if (useridSelect != null && !useridSelect.equals("")) {
											ContentValues updateValues = new ContentValues();
											updateValues.put("U_LoginName", hash.get("U_LoginName").toString());
											updateValues.put("U_Password", hash.get("U_Password").toString());
											updateValues.put("U_Email", hash.get("U_Email").toString());
											updateValues.put("U_Role", hash.get("U_Role").toString());
											updateValues.put("U_Status", hash.get("U_Status").toString());
											updateValues.put("U_Sex", hash.get("U_Sex").toString());
											updateValues.put("U_HomeTel", hash.get("U_HomeTel").toString());
											updateValues.put("U_Photo", hash.get("U_Photo").toString());
											updateValues.put("U_RealName", hash.get("U_RealName").toString());
											updateValues.put("DepID", hash.get("DepID").toString());
											updateValues.put("RegionCode", hash.get("RegionCode").toString());
											updateValues.put("U_ExtendField3", hash.get("U_ExtendField3").toString());
											updateValues.put("JCDD_Code", hash.get("JCDD_Code").toString());
											updateValues.put("U_SessionId", hash.get("U_SessionId").toString());
											updateValues.put("UpdateTime", hash.get("UpdateTime").toString());
											updateValues.put("SyncDataRegionCode",
													hash.get("SyncDataRegionCode").toString());
											updateValues.put("ZFZH", hash.get("ZFZH").toString());
											updateValues.put("ZW", hash.get("ZW").toString());

											String[] whereArgs = { userid };

											SqliteUtil.getInstance().update("PC_Users", updateValues, "userid=?",
													whereArgs);
										} else {
											ContentValues updateValues = new ContentValues();
											updateValues.put("UserID", userid);
											updateValues.put("U_LoginName", hash.get("U_LoginName").toString());
											updateValues.put("U_Password", hash.get("U_Password").toString());
											updateValues.put("U_Email", hash.get("U_Email").toString());
											updateValues.put("U_Role", hash.get("U_Role").toString());
											updateValues.put("U_Status", hash.get("U_Status").toString());
											updateValues.put("U_Sex", hash.get("U_Sex").toString());
											updateValues.put("U_HomeTel", hash.get("U_HomeTel").toString());
											updateValues.put("U_Photo", hash.get("U_Photo").toString());
											updateValues.put("U_RealName", hash.get("U_RealName").toString());
											updateValues.put("DepID", hash.get("DepID").toString());
											updateValues.put("RegionCode", hash.get("RegionCode").toString());
											updateValues.put("U_ExtendField3", hash.get("U_ExtendField3").toString());
											updateValues.put("JCDD_Code", hash.get("JCDD_Code").toString());
											updateValues.put("U_SessionId", hash.get("U_SessionId").toString());
											updateValues.put("UpdateTime", hash.get("UpdateTime").toString());
											updateValues.put("SyncDataRegionCode",
													hash.get("SyncDataRegionCode").toString());
											updateValues.put("ZFZH", hash.get("ZFZH").toString());
											updateValues.put("ZW", hash.get("ZW").toString());

											SqliteUtil.getInstance().insert(updateValues, "PC_Users");
										}
										initLoginUserInfo(list);
									} else {
										if (yutuLoading != null) {
											yutuLoading.dismissDialog();
										}
										//login_btn.setText("点击重新登录");
										Toast.makeText(getContext(), "登陆失败，请重新点击登陆！", Toast.LENGTH_LONG).show();
										Looper.loop();
									}

								}

							} catch (Exception e1) {

								e1.printStackTrace();
							}

						} else {
							// 没网络的时候不让进入系统
							 if (yutuLoading != null) {
							 yutuLoading.dismissDialog();
							 }
							 Toast.makeText(getContext(), "网络不可用,请检查网络设置!",
							 Toast.LENGTH_LONG).show();
							 Looper.loop();
							// 没网络的时候也让进入系统，本地存在此用户
//							ArrayList<HashMap<String, Object>> userlist = users_instance.checkUsers(user, pass);
//							if (userlist == null) {
//								login_handler.sendEmptyMessage(INPUT_LAWLESS);
//								Looper.loop();
//								return;
//							}
//							if (userlist.size() == 0) {// 本地判断输入账号或密码错误
//								if (Net.checkURL(global.getSystemurl())) {// 联网判断当前输入用户是否存在
//									getNewUserList(user, pass, userlist);
//									if (userlist.size() > 0) {
//										initLoginUserInfo();
//									} else {
//										checkFailReason();
//									}
//								} else {
//									checkFailReason();
//
//								}
//							} else {// 本地存在此用户
//								initLoginUserInfo();
//							}

						}

						//
						// ArrayList<HashMap<String, Object>> userlist =
						// users_instance.checkUsers(user, pass);
						// if (userlist == null) {
						// login_handler.sendEmptyMessage(INPUT_LAWLESS);
						// Looper.loop();
						// return;
						// }
						// if (userlist.size() == 0) {// 本地判断输入账号或密码错误
						// if (Net.checkURL(global.getSystemurl())) {//
						// 联网判断当前输入用户是否存在
						// getNewUserList(user, pass, userlist);
						// if (userlist.size() > 0) {
						// initLoginUserInfo();
						// } else {
						// checkFailReason();
						// }
						// } else {
						// checkFailReason();
						//
						// }
						// } else {// 本地存在此用户
						// initLoginUserInfo();
						// }

					}

				}).start();

			}

		}

	};

}
