package com.jy.environment.invitefriends;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.telephony.gsm.SmsManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.jy.environment.R;
import com.jy.environment.base.ActivityBase;
import com.jy.environment.controls.AsyncTask;
import com.jy.environment.controls.WeiBaoApplication;
import com.jy.environment.util.ApiClient;
import com.jy.environment.util.FileUtils;
import com.jy.environment.util.KjhttpUtils;
import com.jy.environment.util.MyLog;
import com.jy.environment.util.ToastUtil;
import com.jy.environment.util.KjhttpUtils.DownGet;
import com.jy.environment.webservice.UrlComponent;

@SuppressLint("NewApi")
public class SmsActivity extends ActivityBase implements OnClickListener {
	private TextView phoneNumble, sms_edit_textview;
	private EditText phoneContent;
	private Button send;
	private ImageView back;
	private mServiceReceiver mReceiver01, mReceiver02;
	String strDestAddress, strMessage;
	private String SMS_SEND_ACTIOIN = "SMS_SEND_ACTIOIN";
	private String SMS_DELIVERED_ACTION = "SMS_DELIVERED_ACTION";
	HashSet<String> values;
	private Intent intent;
	private ArrayList<SortModel> modelsList;
	SharedPreferences sharedPreference;
	private String sb1 = "", sb2 = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.sms_activity);
		intent = getIntent();
		modelsList = intent.getParcelableArrayListExtra("lianxi");
		sharedPreference = getSharedPreferences("phone", Context.MODE_PRIVATE);
		init();

		// strDestAddress = getIntent().getStringExtra("phoneNumber").substring(
		// getIntent().getStringExtra("phoneNumber").indexOf("<") + 1,
		// getIntent().getStringExtra("phoneNumber").indexOf(">"));
		// strMessage = getIntent().getStringExtra("phoneContent");
		// phoneNumble
		// .setText(getIntent().getStringExtra("phoneNumber").substring(0,
		// getIntent().getStringExtra("phoneNumber").indexOf("<")));
		// phoneContent.setText(strMessage);
		// Editable b = phoneContent.getText();
		// phoneContent.setSelection(b.length());

	}

	void init() {
		sb1 = tran(modelsList, "name");
		sb2 = tran(modelsList, "strPhoneNumber");
		phoneNumble = (TextView) findViewById(R.id.sms_tv1);
		phoneContent = (EditText) findViewById(R.id.sms_edit_text1);
		sms_edit_textview = (TextView) findViewById(R.id.sms_edit_textview);
		sms_edit_textview.setText(sb1);
		back = (ImageView) findViewById(R.id.sms_suggest);
		send = (Button) findViewById(R.id.sms_send);
		back.setOnClickListener(this);
		send.setOnClickListener(this);
	}

	@SuppressLint("NewApi")
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.sms_suggest:
			// values = (HashSet<String>) sharedPreference.getStringSet(
			// "phoneNumble", null);
			// if (values == null) {
			// values = new HashSet<String>();
			// }
			// values.add(strDestAddress);
			//
			// sharedPreference.edit().putStringSet("phoneNumble", values)
			// .commit();
			// // new Thread(new Runnable() {
			// //
			// // @Override
			// // public void run() {
			// // runOnUiThread(new Runnable() {
			// //
			// // @Override
			// // public void run() {
			// // Toast.makeText(SmsActivity.this, "发送成功", 1).show();
			// // }
			// // });
			// // }
			// //
			// // }).start();
			// Timer timer = new Timer();
			// TimerTask tast = new TimerTask() {
			// @Override
			// public void run() {
			// finish();
			// }
			// };
			// timer.schedule(tast, 1000);
			finish();
			break;
		case R.id.sms_send:
			// {
			// SmsManager smsManager = SmsManager.getDefault();
			//
			// try {
			// Intent itSend = new Intent(SMS_SEND_ACTIOIN);
			// Intent itDeliver = new Intent(SMS_DELIVERED_ACTION);
			//
			// PendingIntent mSendPI = PendingIntent.getBroadcast(
			// getApplicationContext(), 0, itSend, 0);
			//
			// PendingIntent mDeliverPI = PendingIntent.getBroadcast(
			// getApplicationContext(), 0, itDeliver, 0);
			// MyLog.i(">>>>>>str" + strDestAddress);
			// smsManager.sendTextMessage(strDestAddress, null, strMessage,
			// mSendPI, mDeliverPI);
			//
			// } catch (Exception e) {
			// e.printStackTrace();
			// }
			// }
			// break;
			// values = (HashSet<String>) sharedPreference.getStringSet(
			// "phoneNumble", null);
			// if (values == null) {
			// values = new HashSet<String>();
			// }
			new SmsTask().execute("");
			//
			// Timer timer = new Timer();
			// TimerTask tast = new TimerTask() {
			// @Override
			// public void run() {
			// finish();
			// }
			// };
			// timer.schedule(tast, 1000);
			break;
		default:
			break;
		}
	}

	@SuppressLint("NewApi")
	public class mServiceReceiver extends BroadcastReceiver {
		@SuppressLint("NewApi")
		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub

			try {
				switch (getResultCode()) {
				case Activity.RESULT_OK:
					values = (HashSet<String>) sharedPreference.getStringSet(
							"phoneNumble", null);
					if (values == null) {
						values = new HashSet<String>();
					}
					values.add(strDestAddress);

					sharedPreference.edit().putStringSet("phoneNumble", values)
							.commit();
					mMakeTextToast("邀请成功", true);
					String userId = WeiBaoApplication.getUserId();
					KjhttpUtils http = new KjhttpUtils(SmsActivity.this, null);
					if (!userId.equals("")) {
						http.getString(UrlComponent.getShare(userId, "noise"),
								0, new DownGet() {

									@Override
									public void downget(String arg0) {
										// TODO Auto-generated method stub

									}
								});
					}
					Timer timer = new Timer();
					TimerTask tast = new TimerTask() {
						@Override
						public void run() {
							finish();
						}
					};
					timer.schedule(tast, 1000);
					break;
				case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
					mMakeTextToast("邀请失败", true);
					finish();
					break;
				case SmsManager.RESULT_ERROR_RADIO_OFF:
					mMakeTextToast("邀请失败", true);
					finish();
					break;
				case SmsManager.RESULT_ERROR_NULL_PDU:
					mMakeTextToast("邀请失败", true);
					finish();
					break;
				}
			} catch (Exception e) {
				e.getStackTrace();
			}
		}
	}

	public void mMakeTextToast(String str, boolean isLong) {
		if (isLong == true) {
			Toast.makeText(SmsActivity.this, str, Toast.LENGTH_LONG).show();
		} else {
			Toast.makeText(SmsActivity.this, str, Toast.LENGTH_SHORT).show();
		}
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub

		/* �Զ���IntentFilterΪSENT_SMS_ACTIOIN Receiver */
		IntentFilter mFilter01;
		mFilter01 = new IntentFilter(SMS_SEND_ACTIOIN);
		mReceiver01 = new mServiceReceiver();
		registerReceiver(mReceiver01, mFilter01);

		/* �Զ���IntentFilterΪDELIVERED_SMS_ACTION Receiver */
		mFilter01 = new IntentFilter(SMS_DELIVERED_ACTION);
		mReceiver02 = new mServiceReceiver();
		registerReceiver(mReceiver02, mFilter01);

		super.onResume();
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub

		/* ȡ��ע���Զ���Receiver */
		unregisterReceiver(mReceiver01);
		unregisterReceiver(mReceiver02);

		super.onPause();
	}

	private String tran(ArrayList<SortModel> modelsList, String key) {
		String sb1 = "";
		for (int i = 0; i < modelsList.size(); i++) {
			SortModel model = modelsList.get(i);
			String value = "";
			if ("name".equals(key)) {
				value = model.getName();
				if (null == value) {
					continue;
				}
				if (i == modelsList.size() - 1) {
					sb1 += value;
				} else {
					sb1 += value + ",";
				}
			} else {
				value = model.getStrPhoneNumber();
				if (null == value) {
					continue;
				}
				if (i == modelsList.size() - 1) {
					sb1 += value;
				} else {
					sb1 += value + ",";
				}
				MyLog.i(">>>>>>sb1" + sb1);
			}

		}
		return sb1;
	}

	class SmsTask extends AsyncTask<String, Void, String> {

		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			String message = "360手机助手、豌豆荚搜索空气质量,下载试用吧。空气质量是中科宇图提供的一款公益性产品，主题为环保、健康、生活。各路朋友多多提意见哦";
			try {
				message = URLEncoder.encode(message, "gb2312");
				MyLog.i(">>>>>>>>sb2试一下" + sb2.replace(" ", ""));
				Map<String, Object> postData = new HashMap<String, Object>();
				// 拼装请求地址的参数
				postData.put("account", "85422782");
				postData.put("pwd", "123123");
				postData.put("product", "9");
				postData.put("mobile", sb2.replace(" ", ""));
				postData.put("message", message);
				String url = "http://dx.it1199.com:83/ApiService.asmx/Send";
				ApiClient.sendPostData(url, postData, "gb2312");
			} catch (Exception e) {
				// e.printStackTrace();
			}

			return "";
		}

		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			String wenjian = FileUtils.read(SmsActivity.this, "phone.txt");

			for (int i = 0; i < modelsList.size(); i++) {
				SortModel model = modelsList.get(i);
				String phoneNumber = model.getStrPhoneNumber();
				if (null == phoneNumber) {
					continue;
				}
				if (null != wenjian && wenjian.contains(phoneNumber)) {
					continue;
				} else {
					FileUtils.writeAppend(SmsActivity.this, "phone.txt",
							phoneNumber);
				}
			}
			String userId = WeiBaoApplication.getUserId();
			KjhttpUtils http = new KjhttpUtils(SmsActivity.this,null);
			if (!userId.equals("")) {
				http.getString(UrlComponent.getShare(userId, "phone"), 0, new DownGet() {
					
					@Override
					public void downget(String arg0) {
						// TODO Auto-generated method stub
						
					}
				});
			}
			ToastUtil.showShort(getApplicationContext(), "邀请成功");
			finish();
		}
	}

}
