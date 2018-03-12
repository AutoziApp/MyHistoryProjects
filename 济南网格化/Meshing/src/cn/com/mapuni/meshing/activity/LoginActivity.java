package cn.com.mapuni.meshing.activity;

import android.app.Activity;
import android.app.DownloadManager.Request;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.preference.PreferenceActivity.Header;
import android.text.TextUtils;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import cn.com.mapuni.meshing.activity.gis.LocationService;
import cn.com.mapuni.meshing.activity.gis.MapBinder;
import cn.com.mapuni.meshing.activity.gis.LocationService.CallBackAdrr;
import cn.com.mapuni.meshing.activity.gis.LocationService.CallBackAdrrPoint;
import cn.com.mapuni.meshing.activity.xc_activity.XcrwActivity;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.Response;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.meshing.R;
import com.iflytek.ui.r;
import com.mapuni.android.base.Global;
import com.mapuni.android.base.controls.loading.YutuLoading;
import com.mapuni.android.base.interfaces.PathManager;
import com.mapuni.android.base.util.DisplayUitl;
import com.mapuni.android.dataprovider.JsonHelper;
import com.mapuni.android.netprovider.WebServiceProvider;
import com.tianditu.android.maps.GeoPoint;
import com.tianditu.android.maps.MapView;
import com.tianditu.android.maps.MyLocationOverlay;

import okhttp3.Call;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.Response;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import cn.com.mapuni.meshing.service.GjscService;
import cn.com.mapuni.meshing.util.ExampleUtil;
import cn.com.mapuni.meshing.util.UploadFile;
import cn.com.mapuni.meshing.location.service.BaiduLocationService;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.Poi;

import cn.com.mapuni.meshing.activity.LocationApplication;
import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;

public class LoginActivity extends Activity implements DialogInterface,
		OnClickListener {
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
	private String sessionId;
	private String organization_code;
	private String user_id;
	private String USER_ID;
	private String USER_NAME;
	private String ORGANIZATION_CODE;
	private GeoPoint point;
	private String address;
	private String addressBaidu;
	public LocationService.MyBinder myBinder;
	/**
	 * 捕获手机物理菜单键
	 */
	private long exitTime = 0;
	private String havePatrolRole;// 3701040302  三级管理员	37010403二级管理员      admin01一级管理员
	private String haveAdminRole;// A3701040302 三级负责人	A37010403 二级负责人	A555555一级负责人
	private String haveFreeRole;// 是否是自由巡检员，该角色下的用户上报，需要选择上报单位。
	private String haveLiaisonRole;// 是否是联络员
	private String haveInspectorRole;// 370104030201 四级巡检员
	private String user_Name;// 姓名
	private String user_Account;//用户登陆账户
	private String userGridLevel;	//登陆用户权限
	private String phone;
	private String organization_name;// 组织单位
	private BaiduLocationService baiduLocationService;

	private Double lat;// 纬度
	private Double lon;// 经度
	private Double tdt_log;
	private Double tdu_lat;
	final double x_pi = 3.14159265358979324 * 3000.0 / 180.0;
	private final Handler handler = new Handler() {
		@Override
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 101:
				yutuLoading = new YutuLoading(LoginActivity.this);
				yutuLoading.setCancelable(true);
				yutuLoading.setLoadMsg("正在登录，请稍候", "");
				yutuLoading.showDialog();
				break;
			case 1:
				if (yutuLoading != null) {
					yutuLoading.dismissDialog();
				}
				String result = (String) msg.obj;
				JSONObject sywrwJsonObject;
				try {
					sywrwJsonObject = new JSONObject(result);
					sessionId = sywrwJsonObject.getString("sessionId");
					user_id = sywrwJsonObject.getJSONObject("user")
							.getString("user_Id");
					organization_code = sywrwJsonObject.getJSONObject("user")
							.getJSONObject("org")
							.getString("organization_code");
					organization_name = sywrwJsonObject.getJSONObject("user")
							.getJSONObject("org")
							.getString("organization_name");
					havePatrolRole = sywrwJsonObject.getJSONObject("user")
							.getString("havePatrolRole");
					haveAdminRole = sywrwJsonObject.getJSONObject("user")
							.getString("haveAdminRole");
					haveFreeRole = sywrwJsonObject.getJSONObject("user")
							.getString("haveFreeRole");
					haveLiaisonRole = sywrwJsonObject.getJSONObject("user")
							.getString("haveLiaisonRole");
					haveInspectorRole = sywrwJsonObject.getJSONObject("user")
							.getString("haveInspectorRole");
					user_Name = sywrwJsonObject.getJSONObject("user")
							.getString("user_Name");
					phone = sywrwJsonObject.getJSONObject("user")
							.getString("email");
					userGridLevel = sywrwJsonObject.getJSONObject("user")
							.getString("userGridLevel");			//获取用户权限分级
					user_Account = sywrwJsonObject.getJSONObject("user")
							.getString("user_Account");
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				Toast.makeText(LoginActivity.this, "登录成功！", Toast.LENGTH_SHORT)
						.show();
				startService(new Intent(LoginActivity.this, GjscService.class));
				saveUserInfo();
				
				// try {
				// UploadFile.uploadLocation(USER_ID, point.getLongitudeE6() + "
				// ", point.getLatitudeE6() + "");
				// } catch (Exception e) {
				// // TODO Auto-generated catch block
				// e.printStackTrace();
				// }			
				intent = new Intent(LoginActivity.this, NewMainActivity.class);	
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
				// LoginActivity.this.finish();
				Toast.makeText(LoginActivity.this, "网络错误", Toast.LENGTH_SHORT)
						.show();
//				intent = new Intent(LoginActivity.this, NewMainActivity.class);	
//				startActivity(intent);
				break;

			}
		}

		private void saveUserInfo() {
			DisplayUitl.writePreferences(LoginActivity.this, LAST_USER_SP_NAME,
					"user", user);
			setAlias(user);//极光推送注册别名
			DisplayUitl.writePreferences(LoginActivity.this, LAST_USER_SP_NAME,
					"pass", pass);
			DisplayUitl.writePreferences(LoginActivity.this, LAST_USER_SP_NAME,
					"sessionId", sessionId);
			DisplayUitl.writePreferences(LoginActivity.this, LAST_USER_SP_NAME,
					"organization_code", organization_code);
			DisplayUitl.writePreferences(LoginActivity.this, LAST_USER_SP_NAME,
					"user_id", user_id);
			DisplayUitl.writePreferences(LoginActivity.this, LAST_USER_SP_NAME,
					"userGridLevel", userGridLevel);//网格等级

			DisplayUitl.writePreferences(LoginActivity.this, LAST_USER_SP_NAME,
					"havePatrolRole", havePatrolRole);
			DisplayUitl.writePreferences(LoginActivity.this, LAST_USER_SP_NAME,
					"haveAdminRole", haveAdminRole);
			DisplayUitl.writePreferences(LoginActivity.this, LAST_USER_SP_NAME,
					"haveFreeRole", haveFreeRole);
			DisplayUitl.writePreferences(LoginActivity.this, LAST_USER_SP_NAME,
					"haveLiaisonRole", haveLiaisonRole);
			DisplayUitl.writePreferences(LoginActivity.this, LAST_USER_SP_NAME,
					"haveInspectorRole", haveInspectorRole);

			DisplayUitl.writePreferences(LoginActivity.this, LAST_USER_SP_NAME,
					"user_Name", user_Name);
			DisplayUitl.writePreferences(LoginActivity.this, LAST_USER_SP_NAME,
					"phone", phone);
			DisplayUitl.writePreferences(LoginActivity.this, LAST_USER_SP_NAME,
					"organization_name", organization_name);
			DisplayUitl.writePreferences(LoginActivity.this, LAST_USER_SP_NAME,
					"user_Account", user_Account);
			// Log.i("aaa", USER_ID);
			// Global.getGlobalInstance().setUserid(USER_ID);
			Global.getGlobalInstance().setUserid(user_id);
			Global.getGlobalInstance().setUsername(USER_NAME);
			Global.getGlobalInstance().setAreaCode(ORGANIZATION_CODE);
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
		startMapSerview();
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

/*	*//***
	 * Stop location service
	 *//*
	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		baiduLocationService.unregisterListener(mListener); // 注销掉监听
		baiduLocationService.stop(); // 停止定位服务
		stopMapSerview();
		super.onStop();
	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		// -----------location config ------------
		baiduLocationService = ((LocationApplication) getApplication()).baiduLocationService;
		// 获取locationservice实例，建议应用中只初始化1个location实例，然后使用，可以参考其他示例的activity，都是通过此种方式获取locationservice实例的
		baiduLocationService.registerListener(mListener);
		// 注册监听
		int type = getIntent().getIntExtra("from", 0);
		if (type == 0) {
			baiduLocationService.setLocationOption(baiduLocationService
					.getDefaultLocationClientOption());
		} else if (type == 1) {
			baiduLocationService.setLocationOption(baiduLocationService
					.getOption());
		}
		baiduLocationService.start();

	}*/

	/*****
	 * @see copy funtion to you project
	 *      定位结果回调，重写onReceiveLocation方法，可以直接拷贝如下代码到自己工程中修改
	 * 
	 *//*
	private BDLocationListener mListener = new BDLocationListener() {

		@Override
		public void onReceiveLocation(BDLocation location) {
			// TODO Auto-generated method stub
			if (null != location
					&& location.getLocType() != BDLocation.TypeServerError) {

				lat = location.getLatitude();

				lon = location.getLongitude();
				addressBaidu = location.getAddrStr()
						+ location.getLocationDescribe();
				// bd_decrypt2(lon, lat);
				if (location.getLocType() == BDLocation.TypeGpsLocation) {// GPS定位结果

				} else if (location.getLocType() == BDLocation.TypeNetWorkLocation) {// 网络定位结果
					// 运营商信息
					if (location.hasAltitude()) {// *****如果有海拔高度*****

					}

				} else if (location.getLocType() == BDLocation.TypeOffLineLocation) {// 离线定位结果

				} else if (location.getLocType() == BDLocation.TypeServerError) {
					Toast.makeText(LoginActivity.this, "服务端网络定位失败",
							Toast.LENGTH_SHORT).show();
				} else if (location.getLocType() == BDLocation.TypeNetWorkException) {
					Toast.makeText(LoginActivity.this, "网络不同导致定位失败，请检查网络是否通畅",
							Toast.LENGTH_SHORT).show();
				} else if (location.getLocType() == BDLocation.TypeCriteriaException) {
					Toast.makeText(
							LoginActivity.this,
							"无法获取有效定位依据导致定位失败，一般是由于手机的原因，处于飞行模式下一般会造成这种结果，可以试着重启手机",
							Toast.LENGTH_SHORT).show();
				}
			}

		}

		public void onConnectHotSpotMessage(String s, int i) {
		}
	};*/

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.btn_login:
			if (DisplayUitl.isFastDoubleClick()) {
				break;
			}
			user = edt_username.getText().toString();
			pass = edt_password.getText().toString();

			if (point == null) {
				point = myBinder.getOverlay().getMyLocation();
				address = "无";
			}
			// String versionStr = android.os.Build.VERSION.RELEASE;
			// //float versionFloat = Float.parseFloat(versionStr);
			// Double versionFloat=Double.parseDouble(versionStr)

			try {
				if (null != point) {
					UploadFile.login(handler, user, pass, "app",
							(Double) (point.getLongitudeE6() / 1E6) + "",
							(Double) (point.getLatitudeE6() / 1E6) + "",
							address);
				} else {
					UploadFile.login(handler, user, pass, "app", lon + "", lat
							+ "", addressBaidu);
				}

				// UploadFile.login(handler, user, pass, "app", lon + "", lat +
				// "", addressBaidu);

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			// syncTaskLogin();

			break;

		default:
			break;
		}

	}

	public void syncTaskLogin() {
		yutuLoading = new YutuLoading(this);
		yutuLoading.setCancelable(true);
		yutuLoading.setLoadMsg("正在上传数据，请稍候", "");
		yutuLoading.showDialog();
		new Thread(new Runnable() {
			@Override
			public void run() {
				ArrayList<LinkedHashMap<String, Object>> params = new ArrayList<LinkedHashMap<String, Object>>();
				LinkedHashMap<String, Object> param = new LinkedHashMap<String, Object>();
				user = edt_username.getText().toString();
				pass = edt_password.getText().toString();
				param.put("userName", user);
				param.put("userPwd", pass);
				params.add(param);
				String result = "";
				try {
					result = (String) WebServiceProvider.callWebService(
							PathManager.NAMESPACE, "login", params,
							PathManager.LONGIN_URL,
							WebServiceProvider.RETURN_STRING, true);
					if (result == null || result.equals("[]")
							|| result.equals("")) {
						handler.sendEmptyMessage(3);
						return;
					}

					JSONObject jsonObject = new JSONObject(result);
					Boolean isSuccess = jsonObject.getBoolean("isSuccess");
					if (isSuccess) {
						USER_ID = jsonObject.getJSONArray("result")
								.getJSONObject(0).getString("USER_ID");
						ORGANIZATION_CODE = jsonObject
								.getJSONArray("Organization").getJSONObject(0)
								.getString("ORGANIZATION_CODE");
						USER_NAME = jsonObject.getJSONArray("result")
								.getJSONObject(0).getString("USER_NAME");
						
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

	private void getLocationAdrr() {
		MapBinder.getInstance().getBinder().getLocationAddr(new CallBackAdrr() {
			@Override
			public void callbackadrr(String adrr, GeoPoint mGeoPoint) {
				// mbwz_eit.setText(adrr);
				address = adrr;
			}
		});
	}

	private void getLocationAdrrPoint() {
		MapBinder.getInstance().getBinder()
				.CallBackAdrrPoint(new CallBackAdrrPoint() {
					@Override
					public void CallBackAdrrPoint(GeoPoint mGeoPoint) {
						point = mGeoPoint;
						Log.i("bai", "执行失败1：" + point.getLongitudeE6());
						Log.i("bai", "执行失败1：" + point.getLongitudeE6());
					}
				});
	}

	private ServiceConnection connection = new ServiceConnection() {
		@Override
		public void onServiceDisconnected(ComponentName name) {

		}

		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			myBinder = (LocationService.MyBinder) service;
			MapBinder.getInstance().setBinder(myBinder);
			myBinder.start(LoginActivity.this.getApplicationContext(),
					new MapView(LoginActivity.this, ""));
			getLocationAdrr();
			getLocationAdrrPoint();
		}
	};

	private void startMapSerview() {
		Intent bindIntent = new Intent(this.getApplicationContext(),
				LocationService.class);
		this.getApplicationContext().bindService(bindIntent, connection,
				BIND_AUTO_CREATE);
	
	}
	private void stopMapSerview() {
		
		this.getApplicationContext().unbindService(connection);
	}

	/**
	 * 按两次返回键退出程序
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {// && event.getAction() ==
												// KeyEvent.ACTION_DOWN
			if ((System.currentTimeMillis() - exitTime) > 2000) {
				Toast.makeText(LoginActivity.this, "再按一次退出登录",
						Toast.LENGTH_SHORT).show();
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
	public void cancel() {
		// TODO Auto-generated method stub

	}

	@Override
	public void dismiss() {
		// TODO Auto-generated method stub

	}

	void bd_decrypt(double bd_lon, double bd_lat) {
		double x_pi = 3.14159265358979324 * 3000.0 / 180.0;
		double x = bd_lon - 0.0065, y = bd_lat - 0.006;
		double z = Math.sqrt(x * x + y * y) - 0.00002 * Math.sin(y * x_pi);
		double theta = Math.atan2(y, x) - 0.000003 * Math.cos(x * x_pi);
		String str = z * Math.cos(theta) + "," + z * Math.sin(theta);
		tdt_log = z * Math.cos(theta);
		tdu_lat = z * Math.sin(theta);
		// return z * Math.cos(theta)+","+z * Math.sin(theta);

	}

	void bd_decrypt2(double bd_lat, double bd_lon) {
		double x = bd_lon - 0.0065, y = bd_lat - 0.006;
		double z = Math.sqrt(x * x + y * y) - 0.00002 * Math.sin(y * x_pi);
		double theta = Math.atan2(y, x) - 0.000003 * Math.cos(x * x_pi);
		tdt_log = z * Math.sin(theta);
		tdu_lat = z * Math.cos(theta);
		int i = 0;

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
}
