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

	/** ��¼ģ��Ŀ����� */
	private LoginController loginController = null;
	// private Context context = null;

	private final String TAG = "Login";
	private Global global;
	/** ��¼��ť */
	private Button login_btn;
	/** �����û��� */
	private AutoCompleteTextView actv;
	/** ��ס���� */
	private CheckBox remember_pwd;
	/** �Զ���¼ */
	private CheckBox auto_login;
	/** �������� */
	private EditText pwd_edt;
	/** չʾ��¼������û� */
// private ImageButton down_list_imagebtn;
	/** ��¼������ */
	private YutuLoading yutuLoading;
	/** ��ס������û����� */
	private List<String> list;
	/** ��ס����ı�ʶ */
	private boolean REMEMBER = false;
	/** �Զ���¼�жϱ�־,�����ݿ��ж�ȡ��Ĭ��false */
	private boolean AUTO = false;

	/** ��ǰ�û������� */
	private final int NOUSER = 0;
	/** ������� */
	private final int PASSWORDERR = 1;
	/** ���������� */
	private final int NONET = 2;
	/** ��¼�ɹ� */
	private final int LOGIN_SUCCESS = 4;
	/** �Ƿ��ַ� */
	private final int INPUT_LAWLESS = 5;
	/** �ǲ���yutu�û���¼ */
	private Boolean isYUTU_USER = false;

	private HashMap<String, Object> dataMap = null;
	/** ϵͳ���� */
	private String serverName = "";
	/** ������·�� */
	private String serverAad = "";

	// ϵͳ����
	private TextView login_sz;

	private AlertDialog dialog;

	/** ��ǰ��¼�û��� */
	private String user;
	/** ��ǰ��¼�û������� */
	private String pass;
	private BaseUsers users_instance;
	private BaseDataSync dataSync;

	/** ��ż�ס����������û����������SP name */
	private final String USERS_SP_NAME = "userinfo";
	/** ����¼���û���ϢSP name */
	private final String LAST_USER_SP_NAME = "lastuser";

	/**
	 * ��õ�¼���͹�������Ϣ������Ӧ
	 */
	protected Handler login_handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			if (yutuLoading != null && msg.what != LOGIN_SUCCESS) {
				yutuLoading.dismissDialog();
			}
			switch (msg.what) {
			case NOUSER:
				Toast.makeText(getContext(), "��ǰ�û�������", Toast.LENGTH_LONG).show();
				break;
			case PASSWORDERR:
				Toast.makeText(getContext(), "�û������������", Toast.LENGTH_LONG).show();
				break;
			case NONET:
				Toast.makeText(getContext(), "����������", Toast.LENGTH_LONG).show();
				break;
			case LOGIN_SUCCESS:
				loginController.hideDialog(LoginController.HANDLE_HIDE_LoginDialog);
				break;
			case INPUT_LAWLESS:
				Toast.makeText(getContext(), "�����û�����������ڷǷ��ַ�", Toast.LENGTH_LONG).show();
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

	/**
	 * �����η��ؼ��˳�����
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {// && event.getAction() ==
												// KeyEvent.ACTION_DOWN
			if ((System.currentTimeMillis() - exitTime) > 2000) {
				Toast.makeText(getContext(), "�ٰ�һ���˳���¼", Toast.LENGTH_SHORT).show();
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
		// ��ס����checkbox����
		remember_pwd.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean isChecked) {
				REMEMBER = isChecked;
			}
		});

		// �Զ���¼����
		auto_login.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
				AUTO = arg1;
			}
		});

		setItemClickListener(actv, remember_pwd, pwd_edt);
		autoLogin(actv, pwd_edt, auto_login);
	//	 down_list_imagebtn.setOnClickListener(clickListener);// ��ʾ��¼���û���
		login_btn.setOnClickListener(clickListener);// ��¼����
		
		
	
		

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
						Toast.makeText(getContext(), "���ػ�����Ϣ��ɾ����", Toast.LENGTH_SHORT).show();
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
				ab.setTitle("ϵͳ����");
				ab.setIcon(getContext().getResources().getDrawable(R.drawable.base_icon_mapuni_white));
				ab.setNegativeButton("ȡ��", new DialogInterface.OnClickListener() {

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
		menu.add(0, 0, 0, "ϵͳ����");
		menu.add(0, 1, 0, "�������");
		menu.add(0, 2, 0, "APN����");
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
			Toast.makeText(getContext(), "���ػ�����Ϣ��ɾ����", Toast.LENGTH_SHORT).show();
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
	 * ʵ�����ؼ�
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
		yutuLoading.setLoadMsg("��¼�У����Ե�...", "��¼ʧ�ܣ����Ժ����ԣ�");
		
		setSystemLogo();
	
		
	}

	/**
	 * Description:���÷�������ַ
	 * 
	 * @author ������ Create at: 2012-12-4 ����4:16:18
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
		ab.setTitle("ϵͳ����");
		ab.setIcon(getContext().getResources().getDrawable(R.drawable.base_icon_mapuni_white));
		ab.setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {

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

		ab.setNegativeButton("ȡ��", new DialogInterface.OnClickListener() {

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
	 * �����ֻ�����˵���
	 */
	private long exitTime = 0;

	/** ��ȡ���м�ס������û��� */
	protected List<String> getAllUser() {

		return users_instance
				.getAllUser(getContext().getSharedPreferences(USERS_SP_NAME, getContext().MODE_WORLD_WRITEABLE));
	}

	/** ���������û���������� */
	protected void setItemClickListener(AutoCompleteTextView actv, final CheckBox remember_pwd,
			final EditText pwd_edt) {
		actv.setOnItemClickListener(new OnItemClickListener() {// AutoCompleteText����
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				String nowuser = String.valueOf(arg0.getItemAtPosition(arg2));
				pwd_edt.setText(DisplayUitl.readPreferences(getContext(), USERS_SP_NAME, nowuser));
				remember_pwd.setChecked(true);

			}
		});

	}

	/**
	 * ���SharedPreferences����
	 */
	protected void clearCache() {
		getContext().getSharedPreferences(USERS_SP_NAME, getContext().MODE_WORLD_WRITEABLE).edit().clear().commit();
		getContext().getSharedPreferences(LAST_USER_SP_NAME, getContext().MODE_WORLD_WRITEABLE).edit().clear().commit();

	}

	/** Ĭ����ʾ���һλ��¼��¼�û������룬��������Զ���¼����Ĭ�ϵ�¼��ť����� */
	protected void autoLogin(AutoCompleteTextView actv, EditText pwd_edt, CheckBox auto_login) {

		String user = DisplayUitl.readPreferences(getContext().getApplicationContext(), LAST_USER_SP_NAME, "user");
		String pwd = DisplayUitl.readPreferences(getContext().getApplicationContext(), LAST_USER_SP_NAME, "pass");
		for (String userStr : list) {// �ж����һ�ε�¼�û��Ƿ��ס����
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
	 * ��¼�ɹ������ ��������¼�ˣ������ѡ��ס���룬���뱾��SP�洢
	 * 
	 * @param user
	 *            ��¼��
	 * @param pass
	 *            ����
	 */
	protected void setRemember(String user, String pass) {

		DisplayUitl.writePreferences(getContext(), LAST_USER_SP_NAME, "user", user);
		DisplayUitl.writePreferences(getContext(), LAST_USER_SP_NAME, "pass", pass);

		// ���ü����жϱ�־
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
	 * Description:���ݲ�ͬ��ϵͳ����Logo
	 * 
	 * @author ������ Create at: 2012-12-4 ����4:18:38
	 */
	protected void setSystemLogo() {

		ImageView sysLogoImageView = (ImageView) findViewById(R.id.faceImga);
		/** ����ϵͳʱ���뽫Ĭ������ΪĿ��ϵͳ */
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
	 * ͬ������user���жϵ�¼�û��Ƿ�Ϊ�����û�
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
		if (userlist != null && userlist.size() > 0) {// ��̨�������û���ͬ�����û�Ȩ��
			final String[] tables = { "PC_UserModelPession" };
			// ͬ���û�Ȩ�ޱ��Լ����������Ϣ
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
	 * ������ԭ��
	 */
	private void checkFailReason() {
		// �����û�����ѯ���������Ƿ���ȷ
		ArrayList<HashMap<String, Object>> userl = users_instance.checkPwd(user);
		if (userl != null && userl.size() != 0) {// �����û��������������
			login_handler.sendEmptyMessage(PASSWORDERR);
		} else {// �����ڴ��û�
			login_handler.sendEmptyMessage(NOUSER);
		}
		Looper.loop();
	}

	/**
	 * �������ʱ�����ߵ�¼ ��¼�ɹ�����ʼ����¼�û���Ϣ,���͵�¼�ɹ���Ϣ
	 */
	private void initLoginUserInfo(ArrayList<HashMap<String, Object>> list) {
		global.initGlobalData(list);
		setRemember(user, pass);
		// ��½�ٶ���
		loginBaiDu();
		login_handler.sendEmptyMessage(LOGIN_SUCCESS);
		Looper.loop();
	}

	/**
	 * ����������ȡ��Login�ȴ���
	 */
	public void dismissLoginDialog() {
		login_handler.sendEmptyMessage(-1);
	}

	/**
	 * û�������ʱ�����ߵ�¼ ��¼�ɹ�����ʼ����¼�û���Ϣ,���͵�¼�ɹ���Ϣ
	 */
	private void initLoginUserInfo() {
		global.initGlobalData(user, pass);
		setRemember(user, pass);
		// ��½�ٶ���
		loginBaiDu();
		login_handler.sendEmptyMessage(LOGIN_SUCCESS);
		Looper.loop();
	}

	/**
	 * ��¼�ɹ�����½�ٶ���,��ȡ�ٶ��˺�id���豸id,���ж��Ƿ��ѱ���
	 */
	private void loginBaiDu() {
		Resources resource = getContext().getResources();
		String pkgName = getContext().getPackageName();
		// Push: ��apikey�ķ�ʽ��¼��һ�������Activity��onCreate�С�
		// �����apikey�����manifest�ļ��У�ֻ��һ�ִ�ŷ�ʽ��
		// ���������Զ��峣����������ʽʵ�֣����滻�����е�Utils.getMetaValue(PushDemoActivity.this,
		// "api_key")
		// ͨ��share preferenceʵ�ֵİ󶨱�־���أ�����Ѿ��ɹ��󶨣���ȡ����ΰ�
		if (!Utils.hasBind(getContext().getApplicationContext())) {
			try {
				PushManager.startWork(getContext().getApplicationContext(), PushConstants.LOGIN_TYPE_API_KEY,
						Utils.getMetaValue(getContext(), "api_key"));
			} catch (Exception e) {
				e.printStackTrace();
			}
			// Push: �������ڵ���λ�����ͣ����Դ�֧�ֵ���λ�õ����͵Ŀ���
			// PushManager.enableLbs(getContext().getApplicationContext());
		}

		// Push: �����Զ����֪ͨ��ʽ������API���ܼ��û��ֲᣬ�����ʹ��ϵͳĬ�ϵĿ��Բ�����δ���
		// ����֪ͨ���ͽ����У��߼�����->֪ͨ����ʽ->�Զ�����ʽ��ѡ�в�����дֵ��1��
		// ���·������� PushManager.setNotificationBuilder(this, 1, cBuilder)�еĵڶ���������Ӧ
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
	 * Description:������̨��������
	 * 
	 * @author ������ Create at: 2012-12-4 ����4:12:27
	 */
	private void doMessagePush() {

		Intent intent = new Intent();
		intent.setAction("com.mapuni.action.launch_service");
		intent.putExtra("operation", "start");
		getContext().sendBroadcast(intent);

	}

	/**
	 * ��¼����¼�����
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
					Toast.makeText(getContext(), "�˺Ż����벻��Ϊ��!", Toast.LENGTH_SHORT).show();
					return;
				}
				user = actv.getText().toString();
				user = user.toLowerCase();
				pass = pwd_edt.getText().toString();

				// if (user.equals("yutu") && pass.equals("mapuni")) {
				// isYUTU_USER = true;
				// }

				yutuLoading.showDialog();
			//	login_btn.setText("��¼��...");
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
//									 "��̨��ַ��д�������������룡",
//									 Toast.LENGTH_LONG).show();
//									 Looper.loop();
//									 return;
									ArrayList<HashMap<String, Object>> userlist = users_instance.checkUsers(user, pass);
									if (userlist == null) {
										login_handler.sendEmptyMessage(INPUT_LAWLESS);
										Looper.loop();
										return;
									}
									if (userlist.size() == 0) {// �����ж������˺Ż��������
										if (Net.checkURL(global.getSystemurl())) {// �����жϵ�ǰ�����û��Ƿ����
											getNewUserList(user, pass, userlist);
											if (userlist.size() > 0) {
												initLoginUserInfo();
											} else {
												checkFailReason();
											}
										} else {
											checkFailReason();

										}
									} else {// ���ش��ڴ��û�
										initLoginUserInfo();
									}
									return;
								}
								if ("-1".equals(jsonStr)) {
									if (yutuLoading != null) {
										yutuLoading.dismissDialog();
									}
									Toast.makeText(getContext(), "�û���������������������룡", Toast.LENGTH_LONG).show();
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
										//login_btn.setText("������µ�¼");
										Toast.makeText(getContext(), "��½ʧ�ܣ������µ����½��", Toast.LENGTH_LONG).show();
										Looper.loop();
									}

								}

							} catch (Exception e1) {

								e1.printStackTrace();
							}

						} else {
							// û�����ʱ���ý���ϵͳ
							 if (yutuLoading != null) {
							 yutuLoading.dismissDialog();
							 }
							 Toast.makeText(getContext(), "���粻����,������������!",
							 Toast.LENGTH_LONG).show();
							 Looper.loop();
							// û�����ʱ��Ҳ�ý���ϵͳ�����ش��ڴ��û�
//							ArrayList<HashMap<String, Object>> userlist = users_instance.checkUsers(user, pass);
//							if (userlist == null) {
//								login_handler.sendEmptyMessage(INPUT_LAWLESS);
//								Looper.loop();
//								return;
//							}
//							if (userlist.size() == 0) {// �����ж������˺Ż��������
//								if (Net.checkURL(global.getSystemurl())) {// �����жϵ�ǰ�����û��Ƿ����
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
//							} else {// ���ش��ڴ��û�
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
						// if (userlist.size() == 0) {// �����ж������˺Ż��������
						// if (Net.checkURL(global.getSystemurl())) {//
						// �����жϵ�ǰ�����û��Ƿ����
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
						// } else {// ���ش��ڴ��û�
						// initLoginUserInfo();
						// }

					}

				}).start();

			}

		}

	};

}
