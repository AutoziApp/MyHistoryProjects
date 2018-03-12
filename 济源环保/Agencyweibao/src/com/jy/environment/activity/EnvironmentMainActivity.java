package com.jy.environment.activity;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.util.EncodingUtils;
import org.json.JSONObject;

import android.app.TabActivity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TabHost;

import com.jy.environment.R;
import com.jy.environment.controls.AsyncTask;
import com.jy.environment.controls.WeiBaoApplication;
import com.jy.environment.model.CollectDeviceInfo;
import com.jy.environment.util.MyLog;
import com.jy.environment.util.NetUtil;
import com.jy.environment.util.NotificationUtils;
import com.jy.environment.util.SharedPreferencesUtil;
import com.jy.environment.webservice.UrlComponent;
import com.umeng.analytics.MobclickAgent;

/**
 * 环境首页
 * 
 * @author baiyuchuan
 * 
 */
public class EnvironmentMainActivity extends TabActivity implements
		OnClickListener {
	private Context mContext;
	private final String TAB_HISTORY = "History";
	private final String TAB_FAVORITE = "Favorite";
	private final String TAB_BARCODE = "Barcode";
	private final String TAB_CREATE = "Create";
	private final String TAB_SETTING = "Setting";
	public boolean flag = false;
	private String curVersionName = "";
	private RadioGroup rgroup_main_tab;
	private TabHost tabHost;
	// private ImageView bgChange_first;
	private Map<String, Object> params = new HashMap<String, Object>();
	SharedPreferencesUtil sh;
	String imei;
	String versionCode;
	public static boolean isWidget;
	private long lastUpdateTime;
	private SharedPreferences shares;
	private final String SHARE_LOGIN_TAG = "MAP_SHARE_LOGIN_TAG";

	protected void onPause() {
		MyLog.i("onPause");
		super.onPause();
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.environment_main_activity);
		shares = getSharedPreferences(SHARE_LOGIN_TAG, 0);
		sh = SharedPreferencesUtil.getInstance(EnvironmentMainActivity.this);
		// bgChange_first = (ImageView)
		// findViewById(R.id.weather_background_normal_first);
		if (sh.IsFirstStart()) {
			// bgChange_first.setVisibility(View.VISIBLE);
			// bgChange_first.setOnClickListener(new OnClickListener() {
			// @Override
			// public void onClick(View v) {
			// // bgChange_first.setVisibility(View.GONE);
			// sh.setIsFirstStart(false);
			// }
			// });

		}
		// startService(new Intent(this, LocationService.class));
		Bundle bundle = getIntent().getExtras();
		if (bundle != null) {
			isWidget = bundle.getBoolean("iswidget");
		}
		CollectDeviceInfo coll = new CollectDeviceInfo();
		coll.updateProperties(EnvironmentMainActivity.this);
		coll.saveDeviceInfo2File();
		params = coll.buildData();
		imei = params.get("imei").toString();
		versionCode = getCurrentVersion(EnvironmentMainActivity.this);

		String path = Environment.getExternalStorageDirectory().toString()
				+ "/weibao/";

		initView();

		initListener();

		if (NetUtil.getNetworkState(WeiBaoApplication.getInstance()) != NetUtil.NETWORN_NONE) {
			if (NotificationUtils.is_update) {
				// UpdateManagerUtil.getUpdateManager()
				// .checkAppUpdate(this, false);
				NotificationUtils.is_update = false;
			} else {
			}
			// NotificationUtils.updateNot(MainActivity.this);
			String path2 = Environment.getExternalStorageDirectory().toString()
					+ "/weibao/";
			String fileName = "crash.log";

			File dir = new File(path2 + fileName);
			if (dir.exists()) {
				exxd name = new exxd();
				name.execute(path, "crash.log");
			}

			/*
			 * new AsyncTask<Void, Void, Void>(){
			 * 
			 * @Override protected Void doInBackground(Void... params) { // TODO
			 * Auto-generated method stub return null; }
			 * 
			 * 
			 * };
			 */

			if (sh.IsFirstUpload()) {

				exxd name2 = new exxd();
				name2.execute(path, "deviceInfo.log");
			}

		}

	}

	/**
	 * 获取当前客户端版本信息
	 */
	private String getCurrentVersion(Context context) {

		PackageInfo info;
		try {
			info = context.getPackageManager().getPackageInfo(
					context.getPackageName(), 0);
			curVersionName = info.versionName;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return curVersionName;
	}

	public boolean fileIsExists(String fileName) {
		File f = new File(fileName);
		if (!f.exists()) {
			return false;
		}
		return true;
	}

	public String readFileSdcard(String fileName) {

		String res = null;

		try {

			FileInputStream fin = new FileInputStream(fileName);

			int length = fin.available();

			byte[] buffer = new byte[length];

			fin.read(buffer);

			res = EncodingUtils.getString(buffer, "UTF-8");

			fin.close();

		}

		catch (Exception e) {

			e.printStackTrace();

		}

		return res;

	}

	class exxd extends AsyncTask<String, Void, Void> {

		@Override
		protected Void doInBackground(String... params) {
			String result = "";
			if (fileIsExists(params[0] + params[1])) {
				result = readFileSdcard(params[0] + params[1]);
				sendPost(result, params[1]);
			}
			return null;
		}

	}

	public static InputStream getInputStreamByPost(String urlPath,
			Map<String, Object> params, String encoding) throws Exception {
		StringBuffer sb = new StringBuffer();
		for (Map.Entry<String, Object> entry : params.entrySet()) {
			sb.append(entry.getKey()).append("=").append(entry.getValue())
					.append("&");
		}
		String data = sb.deleteCharAt(sb.length() - 1).toString();
		URL url = new URL(urlPath);

		HttpURLConnection conn = (HttpURLConnection) url.openConnection();

		conn.setDoOutput(true);
		conn.setDoInput(true);
		conn.setRequestMethod("POST");

		conn.setUseCaches(false);
		conn.setInstanceFollowRedirects(true);

		conn.setConnectTimeout(6 * 1000);

		conn.setRequestProperty("Content-Type",
				"application/x-www-form-urlencoded");

		conn.setRequestProperty("Connection", "Keep-Alive");

		conn.setRequestProperty("Charset", "UTF-8");

		DataOutputStream dos = new DataOutputStream(conn.getOutputStream());

		dos.writeBytes(data);
		dos.flush();
		dos.close();
		if (conn.getResponseCode() == 200) {

			return conn.getInputStream();
		}
		return null;
	}

	public static String convertStreamToString(InputStream is) {

		BufferedReader reader = new BufferedReader(new InputStreamReader(is));

		StringBuilder sb = new StringBuilder();

		String line = null;

		try {

			while ((line = reader.readLine()) != null) {

				sb.append(line + "/n");

			}

		} catch (IOException e) {

			e.printStackTrace();

		} finally {

			try {

				is.close();

			} catch (IOException e) {

				e.printStackTrace();

			}

		}

		return sb.toString();

	}

	public static String convertStreamToString1(InputStream is) {

		BufferedReader reader = new BufferedReader(new InputStreamReader(is));

		StringBuilder sb = new StringBuilder();

		String line = null;

		try {

			while ((line = reader.readLine()) != null) {

				sb.append(line);

			}

		} catch (IOException e) {

			e.printStackTrace();

		} finally {

			try {

				is.close();

			} catch (IOException e) {

				e.printStackTrace();

			}

		}

		return sb.toString();

	}

	public String sendPost(String json, String in) {
		try {
			String urlPath = "";
			Map<String, Object> map = new HashMap<String, Object>();
			if (in.equals("crash.log")) {
				map.put("error", json);
				map.put("imei", imei);
				map.put("versionCode", versionCode);
				urlPath = UrlComponent.errormsg_Get;
			} else if (in.equals("deviceInfo.log")) {
				map = params;
				urlPath = UrlComponent.devicemsg_Get;
			}

			try {
				InputStream is = getInputStreamByPost(urlPath, map, "UTF-8");

				String si = convertStreamToString1(is);
				JSONObject jsonObject = new JSONObject(si);

				String p1 = jsonObject.getString("flag");
				if (p1.equals("true")) {
					String path = Environment.getExternalStorageDirectory()
							.toString() + "/weibao/";

					if (in.equals("crash.log")) {
						String fileName = "crash.log";
						File dir = new File(path + fileName);
						if (dir.exists()) {
							dir.delete();
						}

					}
					if (in.equals("deviceInfo.log")) {
						sh.setIsFirstUpload(false);
						String fileName = "deviceInfo.log";

						File dir = new File(path + fileName);
						if (dir.exists()) {
							dir.delete();
						}

					}

				}
			} catch (Exception e) {

			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	private void initListener() {
		// TODO Auto-generated method stub

		rgroup_main_tab
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(RadioGroup group, int checkedId) {
						switch (checkedId) {
						case R.id.rbtn_tq:
							MobclickAgent.onEvent(
									EnvironmentMainActivity.this,
									"HJClickCity");
							tabHost.setCurrentTabByTag(TAB_SETTING);
							break;
						case R.id.rbtn_hjdt:
							MobclickAgent.onEvent(
									EnvironmentMainActivity.this,
									"HJClickMap");
							tabHost.setCurrentTabByTag(TAB_FAVORITE);
							break;

						case R.id.rbtn_create:
							tabHost.setCurrentTabByTag(TAB_CREATE);
							break;
						case R.id.rbtn_setting:
							MobclickAgent.onEvent(
									EnvironmentMainActivity.this,
									"HJClickWeather");
							tabHost.setCurrentTabByTag(TAB_HISTORY);
							break;
						}

					}
				});


	}

	/*
	 * @Override public boolean onCreateOptionsMenu(Menu menu) { // Inflate the
	 * menu; this adds items to the action bar if it is present.
	 * getMenuInflater().inflate(R.menu.main, menu); return true; }
	 */

	private void initView() {
		try {
			rgroup_main_tab = (RadioGroup) findViewById(R.id.rgroup_main_tab);
			tabHost = (TabHost) findViewById(android.R.id.tabhost);
			tabHost.setup(getLocalActivityManager());
			tabHost.addTab(tabHost.newTabSpec(TAB_SETTING)
					.setIndicator(TAB_SETTING)
//					.setContent(new Intent(this, EnvironmentCurrentWeatherPullActivity.class)));
			.setContent(new Intent(this, EnvironmentWeatherRankkActivity.class)));
			
			Intent intent=new Intent(this, EnvironmentMapActivity.class);
			intent.putExtra("TAG", EnvironmentMainActivity.class.getSimpleName());
			tabHost.addTab(tabHost.newTabSpec(TAB_FAVORITE)
					.setIndicator(TAB_FAVORITE)
//					.setContent(new Intent(this, MapMainNewActivity.class)));
			        .setContent(intent));

			tabHost.addTab(tabHost.newTabSpec(TAB_CREATE)//微站页面
					.setIndicator(TAB_CREATE).setContent(
							new Intent(this,MicroStationActivity.class)));
//							new Intent(this,SettingPersonActivity.class)));
			tabHost.addTab(tabHost
					.newTabSpec(TAB_HISTORY)
					.setIndicator(TAB_HISTORY)
					.setContent(
//							new Intent(this,SettingPersonActivity.class)));
			new Intent(this,StatisticalActivity.class)));
		} catch (Exception e) {
			// TODO: handle exception
			MyLog.e("weibao Exception" + e);
		}

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

	}

	public Animation inFromRightAnimation() {
		Animation inFromRight = new TranslateAnimation(
				Animation.RELATIVE_TO_PARENT, +1.0f,
				Animation.RELATIVE_TO_PARENT, 0.0f,
				Animation.RELATIVE_TO_PARENT, 0.0f,
				Animation.RELATIVE_TO_PARENT, 0.0f);
		inFromRight.setDuration(240);
		inFromRight.setInterpolator(new AccelerateInterpolator());
		return inFromRight;
	}

	public Animation outToRightAnimation() {
		Animation outtoLeft = new TranslateAnimation(
				Animation.RELATIVE_TO_PARENT, -1.0f,
				Animation.RELATIVE_TO_PARENT, 0.0f,
				Animation.RELATIVE_TO_PARENT, 0.0f,
				Animation.RELATIVE_TO_PARENT, 0.0f);
		outtoLeft.setDuration(240);
		outtoLeft.setInterpolator(new AccelerateInterpolator());
		return outtoLeft;
	}
}
