package com.jy.environment.activity;

import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import cn.sharesdk.framework.ShareSDK;

import com.jy.environment.R;
import com.jy.environment.activity.EnvironmentCurrentWeatherPullActivity.MessageReceiver;
import com.jy.environment.base.ActivityBase;
import com.jy.environment.business.BusinessSearch;
import com.jy.environment.controls.AsyncTask;
import com.jy.environment.controls.WeiBaoApplication;
import com.jy.environment.model.LoginModel;
import com.jy.environment.model.UserGetUerInfoModel;
import com.jy.environment.model.UserOtherLoginModel;
import com.jy.environment.util.CommonUtil;
import com.jy.environment.util.KjhttpUtils;
import com.jy.environment.util.MyLog;
import com.jy.environment.util.NetUtil;
import com.jy.environment.util.SharedPreferencesUtil;
import com.jy.environment.util.ToastUtil;
import com.jy.environment.util.UserLoadImage;
import com.jy.environment.webservice.UrlComponent;
import com.jy.environment.widget.CircularImage;
import com.tencent.connect.UserInfo;
import com.tencent.connect.common.Constants;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;
import com.umeng.analytics.MobclickAgent;

/**
 * 用户登录
 * 
 * @author baiyuchuan
 * 
 */
public class UserLoginActivity extends ActivityBase implements OnClickListener {
	private String userName;
	private String password;
	private String userId;
	private String userPic;
	
	String from;
	ImageView back;
	/** 以下是UI */
	private EditText view_userName;
	private EditText view_password;
	private CheckBox view_rememberMe;
	private Button view_loginSubmit;
	private TextView view_loginRegister;
	private TextView find_pwd;
	private InputMethodManager imm;
	// private TextView view_fast;
	/** 第三方登陆 */
	private static final int MSG_USERID_FOUND = 1;
	private static final int MSG_LOGIN = 2;
	private static final int MSG_AUTH_CANCEL = 3;
	private static final int MSG_AUTH_ERROR = 4;
	private static final int MSG_AUTH_COMPLETE = 5;
	private static String userIdQQ;
	private Bitmap picBitmap;
	private String qq_name;
	private String path;
	private Boolean wp;
	private JSONObject resultObject;
	CircularImage cover_user_photo;
	private File protraitFile;
	public static final String KEY_Login = "action_Login";
	String file_name;
	private String userid;
	// private HashMap<String, Object> userinfo;
	private JSONObject userinfo;
	private UserInfo mInfo;

	/** 用来操作sharePreferences标识 */
	private final String SHARE_LOGIN_TAG = "MAP_SHARE_LOGIN_TAG";

	/** 如果登录成功后，保存用户名到sharePreferences 以便下次不在输入了 */
	private String SHARE_LOGIN_USERNAME = "MAP_LOGIN_USERNAME";
	private String SHARE_LOGIN_USERID = "MAP_LOGIN_USERID";
	private String MAP_LOGIN_USERPIC = "MAP_LOGIN_USERPIC";
	private String MAP_LOGIN_USERNC = "MAP_LOGIN_USERNC";
	private String MAP_LOGIN_USERMAIL = "MAP_LOGIN_USERMAIL";

	/** 如果邓丽成功，保存密码到sharePreferences 以便下次不在输入了 */
	private String SHARE_LOGIN_PASSWORD = "MAP_LOGIN_PASSWORD";
	// 保存用户id
	// private String SHARE_LOGIN_ID ="MAP_ID";
	/** 如果登录失败，这个可以给用户确切的消失提示 true 是网络连接失败，false是用户名和密码错误 */
	private boolean isNetError;

	/** 登录loading提示框 */
	private ProgressDialog proDialog;

	/** 条件成立 */
	boolean flag;

	private Context context;

	private static Tencent mTencent;
	private static final String APP_ID = "1101263093";
	private static final String SCOPE = "get_user_info,get_simple_userinfo,add_share";

	public static final String LOGIN_NAME = "LOGINED";
	private KjhttpUtils http;
	private SharedPreferencesUtil util;

	/** 返回码 */

	// String Less = null;

	// boolean StatusCode = false;

	/** 登录后台通知更新UI 主要是用户登录失败 通知UI更新界面 */
	Handler loginHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 10:
				if (proDialog != null) {
					proDialog.dismiss();
				}
				Toast.makeText(UserLoginActivity.this, "账号或密码错误，请重新填写", 0)
						.show();
				break;

			case 11:
				GetUserInfoTask getUserInfoTask = new GetUserInfoTask();
				getUserInfoTask.execute("1", userid);
				if (proDialog != null) {
					proDialog.dismiss();
					// finish();
				}
				// finish();
				break;
			case 12:
				if (proDialog != null) {
					proDialog.dismiss();
				}
				Toast.makeText(UserLoginActivity.this, "登录失败:\n1.请检查您网络连接!", 0)
						.show();
				break;
			}

		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		ShareSDK.initSDK(this);
		setContentView(R.layout.use_login_activity);
		http = new KjhttpUtils(this,null);
		util = SharedPreferencesUtil.getInstance(this);
		mTencent = Tencent.createInstance(APP_ID, this.getApplicationContext());
		imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
		from = getIntent().getStringExtra("from");
		MyLog.i("================from" + from);
		findViewById();
		initView(false);
		// 需要去submitListener里面设置URL
		setListener();
		findViewById(R.id.tvWeibo).setOnClickListener(this);
		findViewById(R.id.tvQq).setOnClickListener(this);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		mTencent.onActivityResult(requestCode, resultCode, data);
	}

	// 腾讯SDK 登陆
	private void login() {
		if (!mTencent.isSessionValid()) {
			mTencent.login(this, SCOPE, loginListener);

		} else {
			mTencent.logout(this);
		}
	}

	// 腾讯SDK 获取用户信息
	private void updateUserInfo() {
		if (mTencent != null && mTencent.isSessionValid()) {
			IUiListener listener = new IUiListener() {

				@Override
				public void onError(UiError e) {

				}

				@Override
				public void onComplete(final Object response) {
					new Thread() {

						@Override
						public void run() {
							userinfo = (JSONObject) response;
							MyLog.i(">>>>>>>response" + response);
							LoginOtherTask loginOtherTask = new LoginOtherTask();
							loginOtherTask.execute();
						}

					}.start();
				}

				@Override
				public void onCancel() {

				}
			};
			mInfo = new UserInfo(this, mTencent.getQQToken());
			mInfo.getUserInfo(listener);

		} else {
			Toast.makeText(UserLoginActivity.this, "获取用户信息失败", 0);
		}
	}

	public static void initOpenidAndToken(JSONObject jsonObject) {
		try {
			String token = jsonObject.getString(Constants.PARAM_ACCESS_TOKEN);
			String expires = jsonObject.getString(Constants.PARAM_EXPIRES_IN);
			String openId = jsonObject.getString(Constants.PARAM_OPEN_ID);
			userIdQQ = openId;
			if (!TextUtils.isEmpty(token) && !TextUtils.isEmpty(expires)
					&& !TextUtils.isEmpty(openId)) {
				mTencent.setAccessToken(token, expires);
				mTencent.setOpenId(openId);
			}
		} catch (Exception e) {
		}
	}

	IUiListener loginListener = new BaseUiListener() {
		@Override
		protected void doComplete(JSONObject values) {
			initOpenidAndToken(values);
			updateUserInfo();
		}
	};

	private class BaseUiListener implements IUiListener {

		@Override
		public void onComplete(Object response) {
			if (null == response) {
				ToastUtil.showLong(UserLoginActivity.this, "登录失败");
				return;
			}
			JSONObject jsonResponse = (JSONObject) response;
			if (null != jsonResponse && jsonResponse.length() == 0) {
				ToastUtil.showLong(UserLoginActivity.this, "登录失败");
				return;
			}
			ToastUtil.showLong(UserLoginActivity.this, "登录成功");
			doComplete((JSONObject) response);
		}

		protected void doComplete(JSONObject values) {

		}

		@Override
		public void onError(UiError e) {
		}

		@Override
		public void onCancel() {
		}
	}

	// 验证邮箱
	public static boolean isEmail(String strEmail) {
		String strPattern = "^\\s*\\w+(?:\\.{0,1}[\\w-]+)*@[a-zA-Z0-9]+(?:[-.][a-zA-Z0-9]+)*\\.[a-zA-Z]+\\s*$";
		Pattern p = Pattern.compile(strPattern);
		Matcher m = p.matcher(strEmail);
		return m.matches();
	}

	// 验证手机号码
	public static boolean isMobile(String str) {
		Pattern p = null;
		Matcher m = null;
		boolean b = false;
		p = Pattern.compile("^[1][3,4,5,8][0-9]{9}$"); // 验证手机号
		m = p.matcher(str);
		b = m.matches();
		return b;
	}

	protected void onDestroy() {
		ShareSDK.stopSDK(this);
		super.onDestroy();
	}

	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tvQq: {
			MobclickAgent.onEvent(UserLoginActivity.this, "WQQLogin");
			login();
		}
			break;
		}
	}

	MessageReceiver mMessageReceiver;

	/** 初始化注册VIEW组件 */
	private void findViewById() {

		view_userName = (EditText) findViewById(R.id.loginUserNameEdit);
		view_password = (EditText) findViewById(R.id.loginPasswordEdit);
		view_password.setInputType(InputType.TYPE_CLASS_TEXT
				| InputType.TYPE_TEXT_VARIATION_PASSWORD);
		view_rememberMe = (CheckBox) findViewById(R.id.loginRememberMeCheckBox);
		boolean flag = util.get("key.autouploadchecked", true);
		view_rememberMe.setChecked(flag);
		view_loginSubmit = (Button) findViewById(R.id.loginSubmit);
		view_loginRegister = (TextView) findViewById(R.id.loginRegister);
		back = (ImageView) findViewById(R.id.login_return_iv);
		find_pwd = (TextView) findViewById(R.id.find_pwd_tv);

		/** 注册成功后传过来用户名和密码,显示在登录界面 */
		if (!flag) {
			Intent intent = getIntent();
			userName = intent.getStringExtra("name");
			password = intent.getStringExtra("pw");

			view_rememberMe.setChecked(false);// 小BUG
			view_userName.setText(userName);
			view_password.setText(password);
		}

	}

	/**
	 * initView:(初始化界面)
	 * 
	 * @param isRememberMe如果当时点击了RememberMe
	 *            ,并且登录成功过一次,则saveSharePreferences(true,ture)后,则直接进入
	 * @return void DOM对象
	 * @throws
	 * @since CodingExample　Ver 1.1
	 */
	private void initView(boolean isRememberMe) {
		SharedPreferences share = getSharedPreferences(SHARE_LOGIN_TAG, 0);
		String userName = share.getString(SHARE_LOGIN_USERNAME, "");
		String password = share.getString(SHARE_LOGIN_PASSWORD, "");
		String userId = share.getString(SHARE_LOGIN_USERID, "");

		if (!"".equals(userName)) {
			view_userName.setText(userName);
		}
		if (!"".equals(password)) {
			view_password.setText(password);
			view_rememberMe.setChecked(true);
		}
		// 如果密码也保存了 直接让登录按钮获取焦点
		if (view_password.getText().toString().length() > 0) {
			// view_loginSubmit.requestFocus();
			// view_password.requestFocus();
		}
		share = null;
	}

	/** 设置一个监听器 */
	private void setListener() {
		view_loginSubmit.setOnClickListener(submitListener);
		view_loginRegister.setOnClickListener(registerLstener);
		view_rememberMe.setOnCheckedChangeListener(rememberMeListener);
		// view_fast.setOnClickListener(fastLstener);
		find_pwd.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				MobclickAgent.onEvent(UserLoginActivity.this,
						"WOpenGetPSDPanel");
				Intent find = new Intent(UserLoginActivity.this,
						UserFindPsswordActivity.class);
				startActivity(find);
			}
		});
		back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
	}

	/** 记住我的选项是否勾选 */
	private boolean isRememberMe() {
		if (view_rememberMe.isChecked()) {
			return true;
		}
		return false;
	}

	/** 登录Button Listener */
	private OnClickListener submitListener = new OnClickListener() {

		@Override
		public void onClick(View v) {

			if (NetUtil.getNetworkState(WeiBaoApplication.getInstance()) == NetUtil.NETWORN_NONE) {
				Toast.makeText(UserLoginActivity.this, "请检查您的网络", 0).show();
				return;
			} else {
				userName = view_userName.getText().toString().trim();
				password = view_password.getText().toString().trim();
				if (userName.equals("") || password.equals("")) {
					Toast.makeText(UserLoginActivity.this, "用户名或密码不能为空", 0)
							.show();
					return;
				}
				proDialog = ProgressDialog.show(UserLoginActivity.this,
						"连接中..", "连接中..请稍后....", true, true);
				// 开启一个线程进行登录验证，主要是用户失败成功可以直接通过startAcitivity(Intent)转向
				// Thread loginThread = new Thread(new LoginFailureHandler());
				// loginThread.start();// 开启
				LoginTask loginTask = new LoginTask();
				MobclickAgent.onEvent(UserLoginActivity.this, "WLogin");
				loginTask.execute();
			}
		}
	};

	/** 注册Listener */
	private OnClickListener registerLstener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			MobclickAgent.onEvent(UserLoginActivity.this, "WOpenRegisterPanel");
			Intent intent = new Intent();
			intent.setClass(UserLoginActivity.this, UserRegisterActivity.class);
			// 转向注册页面
			startActivity(intent);
		}
	};

	/** 记住我checkBoxListener */
	private OnCheckedChangeListener rememberMeListener = new OnCheckedChangeListener() {

		@Override
		public void onCheckedChanged(CompoundButton buttonView,
				boolean isChecked) {
			MobclickAgent.onEvent(UserLoginActivity.this, "WSavePSD");
			util.setChecked(isChecked);
		}
	};

	/**
	 * 如果登录成功过,则将登录用户名和密码记录在SharePreferences
	 * 
	 * @param saveUserName
	 *            是否将用户名保存到SharePreferences
	 * @param savePassword
	 *            是否将密码保存到SharePreferences
	 */
	private void saveSharePreferences(boolean saveUserName, boolean savePassword) {
		SharedPreferences share = getSharedPreferences(SHARE_LOGIN_TAG, 0);
		if (saveUserName) {
			share.edit().putString(SHARE_LOGIN_USERNAME, userName).commit();
			share.edit().putString(SHARE_LOGIN_USERID, userId).commit();
			MyLog.i("userId :" + userId);
			share.edit().putString(MAP_LOGIN_USERPIC, userPic).commit();
			MyLog.i("userPic :" + userPic);
		}
		if (savePassword) {
			share.edit()
					.putString(SHARE_LOGIN_PASSWORD,
							view_password.getText().toString()).commit();
		}
		share = null;
	}

	/**
	 * 如果登录成功过,则将登录用户名和密码记录在SharePreferences
	 * 
	 * @param saveUserName
	 *            是否将用户名保存到SharePreferences
	 * @param savePassword
	 *            是否将密码保存到SharePreferences
	 */
	private void saveInfoSharePreferences(String userNc, String mail) {
		SharedPreferences share = getSharedPreferences(SHARE_LOGIN_TAG, 0);
		/*
		 * share.edit().putString(MAP_LOGIN_USERNC, userNc).commit();
		 * share.edit().putString(MAP_LOGIN_USERMAIL, userNc).commit();
		 */
		share = null;
	}

	/**
	 * 保存第三方登录的用户名
	 * 
	 * @param saveUserName
	 * @param savePassword
	 */
	private void saveSharePreferences_other(boolean saveUserName,
			boolean savePassword) {
		SharedPreferences share_other = getSharedPreferences(SHARE_LOGIN_TAG, 0);
		if (saveUserName) {

			share_other.edit().putString(SHARE_LOGIN_USERNAME, userName)
					.commit();
			if (null != userid && !"".endsWith(userid)) {
				share_other.edit().putString(SHARE_LOGIN_USERID, userid)
						.commit();
				MyLog.i("userId :" + userId);
			}
			share_other.edit().putString(MAP_LOGIN_USERPIC, path).commit();
			MyLog.i("path :" + path);
		}
		share_other = null;
	}

	/** 清除密码 */
	private void clearSharePassword() {
		SharedPreferences share = getSharedPreferences(SHARE_LOGIN_TAG, 0);
		share.edit().putString(SHARE_LOGIN_PASSWORD, "").commit();
		share = null;
	}

	/** 清除用户信息 */
	private void clearShareName() {
		SharedPreferences share = getSharedPreferences(SHARE_LOGIN_TAG, 0);
		share.edit().putString(SHARE_LOGIN_USERNAME, "").commit();
		share.edit().putString(SHARE_LOGIN_USERID, "").commit();
		share.edit().putString(MAP_LOGIN_USERPIC, "").commit();
		share.edit().putString(MAP_LOGIN_USERNC, "").commit();
		share = null;
	}

	public class LoginTask extends AsyncTask<String, Void, LoginModel> {
		@Override
		protected LoginModel doInBackground(String... params) {
			BusinessSearch search = new BusinessSearch();
			LoginModel _Result;
			if (isEmail(userName)) {
			}
			if (!userName.equals("") && !password.equals("")) {
				try {
					_Result = search.login(userName, password);
					return _Result;
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else {
				// 通过调用handler来通知UI主线程更新UI,
				Message message = new Message();
				message.what = 12;
				loginHandler.sendMessage(message);
			}
			return null;
		}

		@Override
		protected void onPostExecute(LoginModel result) {
			try {
				MyLog.i("weibao result" + result);
				
			super.onPostExecute(result);
			// 登录成功
			if (null != result && result.getFlag()) {
				MyLog.i("resulet="+result);
				userId = result.getUserId();
				userName = result.getUserName();
				String url = UrlComponent.getUserInfoById_Get(userId);
				MyLog.i(">>>>>nichen" + url);
				String userNc = result.getNiCheng();
				final SharedPreferences share = getSharedPreferences(
						SHARE_LOGIN_TAG, 0);
				WeiBaoApplication.setUserNc(userNc);
				share.edit().putString("MAP_LOGIN_USERNC", userNc).commit();
				share.edit().putString("MAP_LOGIN_TYPE", "WEIBAO").commit();
				userPic = result.getUserPic();
				Message message = new Message();
				message.what = 11;
				loginHandler.sendMessage(message);
				Intent broadIntent = new Intent(KEY_Login);
				broadIntent.putExtra("userId", userId);
				sendBroadcast(broadIntent);
				WeiBaoApplication.setUsename(userName);
				WeiBaoApplication.getInstance().setUserId(result.getUserId());
				WeiBaoApplication.setUserPwd(password);
				WeiBaoApplication.getInstance().setUserPic(result.getUserPic());
				WeiBaoApplication.getInstance().setIsEmailBind(result.getEmailBind());
				UserInfoActivity.saveInfoSharePreferences(UserLoginActivity.this,
						WeiBaoApplication.getUserNc(), WeiBaoApplication.getUserMail(), WeiBaoApplication.getPhone(),result.getEmailBind(),
						WeiBaoApplication.getIsPhoneBind());
				MyLog.i("xu1123:" + result.getEmailBind());
				MyLog.i("xu112"+"WeiBaoApplication.getIsEmailBind()" + WeiBaoApplication.getIsEmailBind());
				if (isRememberMe()) {
					saveSharePreferences(true, true);
				} else {
					saveSharePreferences(true, false);
				}
				// GetUserInfoTask getUserInfoTask = new GetUserInfoTask();
				// getUserInfoTask.execute("0", userId);
				if (!"".equals(from) && from != null) {
					if (from.equals("weibolists")) {
						Intent postintent = new Intent(UserLoginActivity.this,
								DiscoverPostBlogActivity.class);
						postintent.putExtra("user_name", userName);
						startActivity(postintent);
						finish();
					} else if (from.equals("gongzhong")) {
						Intent intent = new Intent(UserLoginActivity.this,
								DiscoverPubServiceSearchActivity.class);
						intent.putExtra("userId", userId);
						intent.putExtra("to",
								"DiscoverPubServiceSearchActivity");
						startActivity(intent);
					} else if (from.equals("noise")) {
						WeiBaoApplication.setIsnoise_logined(true);
						finish();
					} else if (from.equals("discover")) {
						Intent intent = new Intent(UserLoginActivity.this,
								DiscoverExposureListActivity.class);
						startActivity(intent);
						finish();
					} else if (from.equals("settingPerson")) {
						finish();
					} else if (from.equals("bloglist")) {
						finish();
					} else if (from
							.equals("EnvironmentCurrentWeatherPullActivity")) {
						finish();
					}
				}
			} else {
				Message message = new Message();
				message.what = 10;
				loginHandler.sendMessage(message);
			}
			if (!view_rememberMe.isChecked()) {
				clearSharePassword();
				clearShareName();
			}
			} catch (Exception e) {
				MyLog.e("weibao Exception", e);
			}
		}

	}

	// 另起一个线程，实现第三方登录
	public class LoginOtherTask extends
			AsyncTask<String, Void, UserOtherLoginModel> {
		@Override
		protected UserOtherLoginModel doInBackground(String... params) {
			String qq_url = null;
			try {
				qq_name = (String) userinfo.getString("nickname");
				qq_url = (String) userinfo.get("figureurl_qq_2");
			} catch (JSONException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			try {
				picBitmap = UserLoadImage.loadImageFromUrl(qq_url);
			} catch (Exception e) {
				e.printStackTrace();
			}
			file_name = CommonUtil.BitmapToHexString(picBitmap);
			String url = UrlComponent.postPicUrl_Post_other;
			String picurl = "android";
			BusinessSearch search = new BusinessSearch();
			UserOtherLoginModel _Result;
			try {
				_Result = search.loginOther(url, userIdQQ, qq_name, file_name,
						picurl);
				return _Result;
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(UserOtherLoginModel result) {
			try {
				MyLog.i("weibao result" + result);
			super.onPostExecute(result);
			if (null == result) {
				return;
			}
			MyLog.i(">>>>>>>loginmodel11" + result);
			path = result.getIcon_url();
			userid = result.getUserid();
			String nc = result.getNicheng();
			WeiBaoApplication.setUserNc(nc);
			SharedPreferences share = getSharedPreferences(SHARE_LOGIN_TAG, 0);
			share.edit().putString("MAP_LOGIN_USERNC", nc).commit();
			share.edit().putString("MAP_LOGIN_TYPE", "QQ").commit();
			share.edit().putString("MAP_LOGIN_QQID", userIdQQ).commit();
			String url = UrlComponent.getUserInfoById_Get(userid);
			userName = result.getUsername();
			Message message = new Message();
			message.what = 11;
			loginHandler.sendMessage(message);
			Intent broadIntent = new Intent(KEY_Login);
			broadIntent.putExtra("userId", userid);
			sendBroadcast(broadIntent);
			WeiBaoApplication.getInstance().setUserPic(path);
			WeiBaoApplication.getInstance().setUserId(userid);
			WeiBaoApplication.getInstance().setUsename(userName);
			saveSharePreferences_other(true, true);
			if (!"".equals(from) && from != null) {
				if (from.equals("weibolists")) {
					Intent postintent = new Intent(UserLoginActivity.this,
							DiscoverPostBlogActivity.class);
					startActivity(postintent);
				} else if (from.equals("gongzhong")) {
					Intent intent = new Intent(UserLoginActivity.this,
							DiscoverPubServiceMainActivity.class);
					intent.putExtra("userId", userid);
					startActivity(intent);
				} else if (from.equals("discover")) {
					MyLog.i("================from" + "进1");
					Intent intent = new Intent(UserLoginActivity.this,
							DiscoverExposureListActivity.class);
					startActivity(intent);
				} else if (from.equals("settingPerson")) {
					try {
						Thread.sleep(500);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					finish();
				} else if (from.equals("bloglist")) {
					finish();
				} else if (from.equals("EnvironmentCurrentWeatherPullActivity")) {
					finish();
				}
			} else {
				// ToastUtil.showLong(UserLoginActivity.this, "登录成功");
				// finish();
			}
			} catch (Exception e) {
				MyLog.e("weibao Exception", e);
			}
		}

	}

	// 另起一个线程，实现第三方登录
	public class SinaLoginOtherTask extends
			AsyncTask<String, Void, UserOtherLoginModel> {
		@Override
		protected UserOtherLoginModel doInBackground(String... params) {
			String weibo_url = null;
			try {
				qq_name = (String) userinfo.get("name");
				weibo_url = (String) userinfo.get("avatar_hd");
			} catch (JSONException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			try {
				picBitmap = UserLoadImage.loadImageFromUrl(weibo_url);
			} catch (Exception e) {
				e.printStackTrace();
			}
			file_name = CommonUtil.BitmapToHexString(picBitmap);
			String url = UrlComponent.postPicUrl_Post_other;
			String picurl = "android";
			BusinessSearch search = new BusinessSearch();
			UserOtherLoginModel _Result;
			try {
				_Result = search.loginOther(url, userIdQQ, qq_name, file_name,
						picurl);
				return _Result;
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(UserOtherLoginModel result) {
			try {
				MyLog.i("weibao result" + result);
			super.onPostExecute(result);
			MyLog.i(">>>>>>>loginmodel" + result);
			if (null == result) {
				return;
			}
			path = result.getIcon_url();
			MyLog.i(">>>>>>>>path" + path);
			userid = result.getUserid();
			userName = result.getUsername();
			Message message = new Message();
			message.what = 11;
			loginHandler.sendMessage(message);
			WeiBaoApplication.getInstance().setUserPic(path);
			WeiBaoApplication.getInstance().setUserId(userid);
			WeiBaoApplication.getInstance().setUsename(userName);
			saveSharePreferences_other(true, true);
			if (!"".equals(from) && from != null) {
				if (from.equals("weibolists")) {
					Intent postintent = new Intent(UserLoginActivity.this,
							DiscoverPostBlogActivity.class);
					startActivity(postintent);
				} else if (from.equals("gongzhong")) {
					Intent intent = new Intent(UserLoginActivity.this,
							DiscoverPubServiceMainActivity.class);
					intent.putExtra("userId", userid);
					startActivity(intent);
				} else if (from.equals("discover")) {
					MyLog.i("================from进2");
					Intent intent = new Intent(UserLoginActivity.this,
							DiscoverExposureListActivity.class);
					startActivity(intent);
				}
			} else {
				GetUserInfoTask getUserInfoTask = new GetUserInfoTask();
				getUserInfoTask.execute("0", userid);
			}
		} catch (Exception e) {
			MyLog.e("weibao Exception", e);
		}
		}

	}

	private class GetUserInfoTask extends
			AsyncTask<String, Void, UserGetUerInfoModel> {
		String userPic;
		String nicheng;
		String mail;
		String gender;
		boolean b = false;

		@Override
		protected UserGetUerInfoModel doInBackground(String... params) {
			if ("1".equals(params[0])) {
				b = true;
			} else {
				b = false;
			}
			String url = UrlComponent.getUserInfoById_Get(params[1]);
			MyLog.i(">>>>urlcomponent" + url);
			BusinessSearch search = new BusinessSearch();
			UserGetUerInfoModel _Result = null;
			try {
				_Result = search.getUserInfo(url);
				return _Result;
			} catch (Exception e) {
				e.printStackTrace();
			}
			return _Result;
		}

		@Override
		protected void onPostExecute(UserGetUerInfoModel result) {
			try {
				MyLog.i("weibao result" + result);
			super.onPostExecute(result);
			MyLog.i(">>>>>>>userinfomodel" + result);
			if (null != result) {
				userName = result.getUsername();
				userPic = result.getUserpic();
				nicheng = result.getNc();
				mail = result.getMail();
				gender = result.getGender();
				MyLog.i("user_name" + userName);
				WeiBaoApplication.setUsename(userName);
				WeiBaoApplication.setUserNc(nicheng);
				WeiBaoApplication.setUserMail(mail);
				WeiBaoApplication.setUserGener(gender);
				WeiBaoApplication.setUserPic(userPic);
				// saveInfoSharePreferences(nicheng, mail);
				ToastUtil.showLong(UserLoginActivity.this, "登录成功");

			}
			if (b) {
			} else {
				boolean isOpen = imm.isActive();
				if (isOpen) {
					// 输入法隐藏
					imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
				}
				finish();
			}
			} catch (Exception e) {
				MyLog.e("weibao Exception", e);
			}
		}
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		MobclickAgent.onPause(this);
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		MobclickAgent.onPause(this);
	}

}
