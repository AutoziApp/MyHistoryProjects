package cn.com.mapuni.loginauth.meshingtotal;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.util.Set;

import cn.com.mapuni.loginauth.R;
import cn.com.mapuni.meshing.base.controls.loading.YutuLoading;
import cn.com.mapuni.meshing.base.interfaces.PathManager;
import cn.com.mapuni.meshing.base.util.DisplayUitl;
import cn.com.mapuni.meshing.base.util.ExampleUtil;
import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;

public class LoginActivity extends Activity implements OnClickListener {
	private Button btn_login;
	private EditText edt_username, edt_password;
	private Intent intent;
	private YutuLoading yutuLoading;
	/** 最后登录的用户信息SP name */
	private final String LAST_USER_SP_NAME = "lastuser";

	/** 当前登录用户名 */
	private String user;
	/** 当前登录用户名密码 */
	private String pass;
	/** 当前登录用户id */
	private String userid;
	private final Handler handler = new Handler() {
		@Override
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 1:
				if (yutuLoading != null) {
					yutuLoading.dismissDialog();
				}
				Toast.makeText(LoginActivity.this, "登录成功！", Toast.LENGTH_SHORT)
						.show();
				saveUserInfo();
				String alias=userid.replaceAll("-","_");
				setAlias(alias);//极光推送注册别名
				intent = new Intent();
				intent.setClassName(LoginActivity.this.getPackageName(), "cn.com.mapuni.meshingtotal.activity.MainActivity");
				startActivity(intent);
				finish();
				break;
			case 2:
				if (yutuLoading != null) {
					yutuLoading.dismissDialog();
				}
				Toast.makeText(LoginActivity.this, "用户名或密码错误！",
						Toast.LENGTH_SHORT).show();
				break;
			case 3:
				if (yutuLoading != null) {
					yutuLoading.dismissDialog();
				}
				LoginActivity.this.finish();
				Toast.makeText(LoginActivity.this, "网络错误", Toast.LENGTH_SHORT)
						.show();
				break;

			}
		}

		private void saveUserInfo() {
			DisplayUitl.writePreferences(LoginActivity.this, LAST_USER_SP_NAME,
					"user", user);
			DisplayUitl.writePreferences(LoginActivity.this, LAST_USER_SP_NAME,
					"pass", pass);
			DisplayUitl.writePreferences(LoginActivity.this, LAST_USER_SP_NAME,
					"userid", userid);
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);// 去掉标题栏
		setContentView(R.layout.activity_login);
		btn_login = (Button) findViewById(R.id.btn_login);
		btn_login.setOnClickListener(this);
		edt_username = (EditText) findViewById(R.id.edt_username);
		edt_username.setOnClickListener(this);
		edt_password = (EditText) findViewById(R.id.edt_password);
		edt_password.setOnClickListener(this);
		autoLogin();
	}

	/**
	 * 自动登录
	 */
	private void autoLogin() {
		String user = DisplayUitl.readPreferences(LoginActivity.this,
				LAST_USER_SP_NAME, "user");
		String pwd = DisplayUitl.readPreferences(LoginActivity.this,
				LAST_USER_SP_NAME, "pass");
		edt_username.setText(user);
		edt_password.setText(pwd);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v.getId() == R.id.btn_login) {
			user = edt_username.getText().toString();
			pass = edt_password.getText().toString();
			if(user.equals("") || pass.equals("")) {
				Toast.makeText(LoginActivity.this, "用户名或密码不能为空！", Toast.LENGTH_SHORT).show();
			} else {
				syncTaskLogin();
//				handler.sendEmptyMessage(1);
			}
		}
	}

	public void syncTaskLogin() {
		yutuLoading = new YutuLoading(this);
		yutuLoading.setCancelable(true);
		yutuLoading.setLoadMsg("正在登录，请稍候", "");
		yutuLoading.showDialog();
		String url = PathManager.JINAN_URL + PathManager.LOGIN_URL;
		RequestParams params = new RequestParams();// 添加提交参数
		params.addBodyParameter("UserName", user);
		params.addBodyParameter("PassWord", pass);
		HttpUtils utils = new HttpUtils();
		utils.configCurrentHttpCacheExpiry(1000 * 60);
		utils.configTimeout(60 * 1000);//
		utils.configSoTimeout(60 * 1000);//
		utils.send(HttpRequest.HttpMethod.POST, url, params, new RequestCallBack<String>() {
			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
				String result = String.valueOf(responseInfo.result);
				try{
				ByteArrayInputStream inputStream = new ByteArrayInputStream(result.getBytes());
				SAXReader reader = new SAXReader();
				Document document = reader.read(inputStream);
				Element root = document.getRootElement();
				JSONObject jsonObject = new JSONObject(root.getText());
					Boolean isSuccess=jsonObject.optBoolean("Result",false);
					if(isSuccess){
						JSONArray jsonArray=jsonObject.getJSONArray("Data");
						JSONObject jsonObject1= (JSONObject) jsonArray.get(0);
						userid=jsonObject1.optString("UserID","user1A");
						handler.sendEmptyMessage(1);
					}else {
						handler.sendEmptyMessage(2);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			@Override
			public void onFailure(HttpException e, String s) {
				handler.sendEmptyMessage(3);
			}
		});
//		new Thread(new Runnable() {
//			@Override
//			public void run() {
//				ArrayList<HashMap<String, Object>> params = new ArrayList<HashMap<String, Object>>();
//				HashMap<String, Object> param = new HashMap<String, Object>();
//				param.put("UserName", user);
//				param.put("PassWord", pass);
//				params.add(param);
//				String result = "";
//				try {
//					result = (String) WebServiceProvider
//							.callWebService(
//									"http://tempuri.org/",
//									"Login",
//									params,
//									"http://171.8.66.103:8033/WebService/RM_WebServiceForJava.asmx",
//									WebServiceProvider.RETURN_STRING, true);
//					// String[] node = { "GUID", "RWBH", "FileAttachmentPath",
//					// "FileAttachmentName", "UpdateTime",
//					// "SyncDataRegionCode" };
//					if (result == null || result.equals("[]")
//							|| result.equals("")) {
//						handler.sendEmptyMessage(3);
//						return;
//					}
//					// ArrayList<HashMap<String, Object>> wjlist =
//					// JsonHelper.paseJSON(result);
//					// if (wjlist == null || wjlist.size() == 0) {
//					// return;
//					// }
//					JSONObject jsonObject = new JSONObject(result);
//					Boolean isSuccess = jsonObject.getBoolean("isSuccess");
//					if (isSuccess) {
//						userid = jsonObject.getJSONArray("result").getJSONObject(0).getString("USER_ID");
//						handler.sendEmptyMessage(1);
//					} else {
//						handler.sendEmptyMessage(2);
//					}
//
//				} catch (IOException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				} catch (JSONException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//			}
//		}).start();

	}

	private long exitTime = 0;
	/**
	 * 按两次返回键退出程序
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {// && event.getAction() ==
			// KeyEvent.ACTION_DOWN
			if ((System.currentTimeMillis() - exitTime) > 2000) {
				Toast.makeText(LoginActivity.this, "再按一次退出登录", Toast.LENGTH_SHORT)
						.show();
				exitTime = System.currentTimeMillis();
			} else {
				android.os.Process.killProcess(android.os.Process.myPid());
				System.exit(0);
			}
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	// 这是来自 JPush Example 的设置别名的 Activity 里的代码。一般 App 的设置的调用入口，在任何方便的地方调用都可以。
	private void setAlias(String alias) {
		if (TextUtils.isEmpty(alias)) {
			Toast.makeText(this, "用户名为空导致设置别名失败", Toast.LENGTH_SHORT).show();
			return;
		}
		if (!ExampleUtil.isValidTagAndAlias(alias)) {
			Toast.makeText(this, "用户名不规范导致设置别名失败", Toast.LENGTH_SHORT).show();
			return;
		}

		// 调用 Handler 来异步设置别名
		mHandler.sendMessage(mHandler.obtainMessage(MSG_SET_ALIAS, alias));
	}

	private static final int MSG_SET_ALIAS = 1001;
	private final Handler mHandler = new Handler() {
		@Override
		public void handleMessage(android.os.Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
				case MSG_SET_ALIAS:
					Log.d("aaa", "Set alias in handler.");
					// 调用 JPush 接口来设置别名。
					JPushInterface.setAliasAndTags(getApplicationContext(),
							(String) msg.obj, null, mAliasCallback);
					break;
				default:
					Log.i("aaa", "Unhandled msg - " + msg.what);
			}
		}
	};

	private final TagAliasCallback mAliasCallback = new TagAliasCallback() {
		@Override
		public void gotResult(int code, String alias, Set<String> tags) {
			String logs;
			switch (code) {
				case 0:
					logs = "Set tag and alias success";
					Log.i("aaa", logs);
					// 建议这里往 SharePreference 里写一个成功设置的状态。成功设置一次后，以后不必再次设置了。
					break;
				case 6002:
					logs = "Failed to set alias and tags due to timeout. Try again after 60s.";
					Log.i("aaa", logs);
					// 延迟 60 秒来调用 Handler 设置别名
					mHandler.sendMessageDelayed(
							mHandler.obtainMessage(MSG_SET_ALIAS, alias), 1000 * 60);
					break;
				default:
					logs = "Failed with errorCode = " + code;
					Log.e("aaa", logs);
			}
//			ExampleUtil.showToast(logs, getApplicationContext());
		}
	};
}
