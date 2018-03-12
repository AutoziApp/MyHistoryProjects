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
	/** ����¼���û���ϢSP name */
	private final String LAST_USER_SP_NAME = "lastuser";

	/** ��ǰ��¼�û��� */
	private String user;
	/** ��ǰ��¼�û������� */
	private String pass;
	/** ��ǰ��¼�û�id */
	private String userid;
	private final Handler handler = new Handler() {
		@Override
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 1:
				if (yutuLoading != null) {
					yutuLoading.dismissDialog();
				}
				Toast.makeText(LoginActivity.this, "��¼�ɹ���", Toast.LENGTH_SHORT)
						.show();
				saveUserInfo();
				String alias=userid.replaceAll("-","_");
				setAlias(alias);//��������ע�����
				intent = new Intent();
				intent.setClassName(LoginActivity.this.getPackageName(), "cn.com.mapuni.meshingtotal.activity.MainActivity");
				startActivity(intent);
				finish();
				break;
			case 2:
				if (yutuLoading != null) {
					yutuLoading.dismissDialog();
				}
				Toast.makeText(LoginActivity.this, "�û������������",
						Toast.LENGTH_SHORT).show();
				break;
			case 3:
				if (yutuLoading != null) {
					yutuLoading.dismissDialog();
				}
				LoginActivity.this.finish();
				Toast.makeText(LoginActivity.this, "�������", Toast.LENGTH_SHORT)
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
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);// ȥ��������
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
	 * �Զ���¼
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
				Toast.makeText(LoginActivity.this, "�û��������벻��Ϊ�գ�", Toast.LENGTH_SHORT).show();
			} else {
				syncTaskLogin();
//				handler.sendEmptyMessage(1);
			}
		}
	}

	public void syncTaskLogin() {
		yutuLoading = new YutuLoading(this);
		yutuLoading.setCancelable(true);
		yutuLoading.setLoadMsg("���ڵ�¼�����Ժ�", "");
		yutuLoading.showDialog();
		String url = PathManager.JINAN_URL + PathManager.LOGIN_URL;
		RequestParams params = new RequestParams();// ����ύ����
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
	 * �����η��ؼ��˳�����
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {// && event.getAction() ==
			// KeyEvent.ACTION_DOWN
			if ((System.currentTimeMillis() - exitTime) > 2000) {
				Toast.makeText(LoginActivity.this, "�ٰ�һ���˳���¼", Toast.LENGTH_SHORT)
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

	// �������� JPush Example �����ñ����� Activity ��Ĵ��롣һ�� App �����õĵ�����ڣ����κη���ĵط����ö����ԡ�
	private void setAlias(String alias) {
		if (TextUtils.isEmpty(alias)) {
			Toast.makeText(this, "�û���Ϊ�յ������ñ���ʧ��", Toast.LENGTH_SHORT).show();
			return;
		}
		if (!ExampleUtil.isValidTagAndAlias(alias)) {
			Toast.makeText(this, "�û������淶�������ñ���ʧ��", Toast.LENGTH_SHORT).show();
			return;
		}

		// ���� Handler ���첽���ñ���
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
					// ���� JPush �ӿ������ñ�����
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
					// ���������� SharePreference ��дһ���ɹ����õ�״̬���ɹ�����һ�κ��Ժ󲻱��ٴ������ˡ�
					break;
				case 6002:
					logs = "Failed to set alias and tags due to timeout. Try again after 60s.";
					Log.i("aaa", logs);
					// �ӳ� 60 �������� Handler ���ñ���
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
