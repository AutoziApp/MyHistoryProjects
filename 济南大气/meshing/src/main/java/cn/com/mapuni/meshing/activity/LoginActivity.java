package cn.com.mapuni.meshing.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import cn.com.mapuni.meshing.base.controls.loading.YutuLoading;
import cn.com.mapuni.meshing.base.util.DisplayUitl;
import cn.com.mapuni.meshing.netprovider.WebServiceProvider;
import cn.com.mapuni.meshingtotal.R;

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
				intent = new Intent(LoginActivity.this, MainActivity.class);
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
		switch (v.getId()) {
		case R.id.btn_login:
//			syncTaskLogin();
			handler.sendEmptyMessage(1);
			break;

		default:
			break;
		}

	}

	public void syncTaskLogin() {
		yutuLoading = new YutuLoading(this);
		yutuLoading.setCancelable(true);
		yutuLoading.setLoadMsg("正在登录，请稍候", "");
		yutuLoading.showDialog();
		new Thread(new Runnable() {
			@Override
			public void run() {
				ArrayList<HashMap<String, Object>> params = new ArrayList<HashMap<String, Object>>();
				HashMap<String, Object> param = new HashMap<String, Object>();
				user = edt_username.getText().toString();
				pass = edt_password.getText().toString();
				param.put("userName", user);
				param.put("userPwd", pass);
				params.add(param);
				String result = "";
				try {
					result = (String) WebServiceProvider
							.callWebService(
									"http://tempuri.org/",
									"login",
									params,
									"http://171.8.66.103:8033/WebService/RM_WebServiceForJava.asmx",
									WebServiceProvider.RETURN_STRING, true);
					// String[] node = { "GUID", "RWBH", "FileAttachmentPath",
					// "FileAttachmentName", "UpdateTime",
					// "SyncDataRegionCode" };
					if (result == null || result.equals("[]")
							|| result.equals("")) {
						handler.sendEmptyMessage(3);
						return;
					}
					// ArrayList<HashMap<String, Object>> wjlist =
					// JsonHelper.paseJSON(result);
					// if (wjlist == null || wjlist.size() == 0) {
					// return;
					// }
					JSONObject jsonObject = new JSONObject(result);
					Boolean isSuccess = jsonObject.getBoolean("isSuccess");
					if (isSuccess) {
						userid = jsonObject.getJSONArray("result").getJSONObject(0).getString("USER_ID");
						handler.sendEmptyMessage(1);
					} else {
						handler.sendEmptyMessage(2);
					}

				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}).start();

	}
}
