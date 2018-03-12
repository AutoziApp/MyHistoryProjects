package com.jy.environment.activity;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.jy.environment.R;
import com.jy.environment.base.ActivityBase;
import com.jy.environment.business.BusinessSearch;
import com.jy.environment.controls.AsyncTask;
import com.jy.environment.model.UserMail;
import com.jy.environment.util.MyLog;
import com.jy.environment.webservice.UrlComponent;

public class UserUnbindPhoneActivity extends ActivityBase {
	private TimeCount timeCount;
	// 倒计时
	private int recLen = 20;
	private String phone;
	private String userid;
	private String type;
	private EditText phoneEditText;
	private EditText identifyingCodeEditText;
	private ImageView back;
	private Button btnGetIdetifyingCode;
	private Button btnUnbind;
	private Drawable drawable1;
	private ProgressDialog prDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.user_info_phone_unbind);
		Intent intent = getIntent();
		phone = intent.getStringExtra("content");
		MyLog.i("<<<<<>>>>><<<<<<phone" + phone);
		userid = intent.getStringExtra("userid");
		type = intent.getStringExtra("type");
		initView();
		phoneEditText.setText(phone);
		phoneEditText.setEnabled(false);
		Drawable drawable = this.getResources().getDrawable(
				R.drawable.bound_unnormal_un);
		btnUnbind.setBackgroundDrawable(drawable);
		drawable1 = this.getResources().getDrawable(R.drawable.btn_unbind);
		initListener();
	}

	private void initView() {
		phoneEditText = (EditText) findViewById(R.id.user_info_edit_phone);
		identifyingCodeEditText = (EditText) findViewById(R.id.user_info_edit_identifyingcode);
		back = (ImageView) findViewById(R.id.afterlogin_return_iv_phone);
		btnGetIdetifyingCode = (Button) findViewById(R.id.btn_code);
		btnUnbind = (Button) findViewById(R.id.btn_unbundle);
		timeCount = new TimeCount(60000, 1000);
	}

	void initListener() {
		// identifyingCodeEditText.setOnClickListener(new OnClickListener() {
		// @Override
		// public void onClick(View v) {
		// btnUnbind.setBackgroundDrawable(drawable1);
		// }
		// });

		identifyingCodeEditText
				.setOnFocusChangeListener(new OnFocusChangeListener() {

					@Override
					public void onFocusChange(View v, boolean hasFocus) {
						// TODO Auto-generated method stub
						if (hasFocus) {
							btnUnbind.setBackgroundDrawable(drawable1);
							btnUnbind.setEnabled(true);
						}
					}
				});

		back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		btnGetIdetifyingCode.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				// phoneEditText.setText(phone);

				timeCount.start();
				new MyTask()
						.execute(userid, phoneEditText.getText().toString());
			}
		});
		btnUnbind.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				if (identifyingCodeEditText.getText().toString().isEmpty()) {
					Toast.makeText(UserUnbindPhoneActivity.this, "验证码不能为空",
							3000).show();
				}
				new MyTaskUnbind().execute(userid, identifyingCodeEditText
						.getText().toString());
			}
		});
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

	class TimeCount extends CountDownTimer {

		public TimeCount(long millisInFuture, long countDownInterval) {
			super(millisInFuture, countDownInterval);
			// TODO Auto-generated constructor stub
		}

		@Override
		public void onTick(long millisUntilFinished) {
			// TODO Auto-generated method stub
			btnGetIdetifyingCode.setClickable(false);
			btnGetIdetifyingCode.setText("重新发送(" + millisUntilFinished / 1000
					+ ")秒");
			btnGetIdetifyingCode.setBackgroundColor(Color.GRAY);
		}

		@Override
		public void onFinish() {
			// TODO Auto-generated method stub
			// btnGetIdetifyingCode.setText("获取验证码");
			btnGetIdetifyingCode.setText("");
			btnGetIdetifyingCode.setClickable(true);
			btnGetIdetifyingCode
					.setBackgroundResource(R.drawable.verification_code_obtain);
		}

	}

	class MyTask extends AsyncTask<String, Void, UserMail> {
		@Override
		protected UserMail doInBackground(String... params) {
			String url = UrlComponent.getUnbindPhone_Get(params[0], params[1]);
			MyLog.i(">>>>>>>>history" + url);
			BusinessSearch search = new BusinessSearch();
			UserMail _Result = null;
			try {
				_Result = search.getEmail(url);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return _Result;

		}

		@Override
		protected void onPostExecute(UserMail result) {
			super.onPostExecute(result);
			try {
				MyLog.i("weibao result" + result);
			if (null == result) {
				Toast.makeText(UserUnbindPhoneActivity.this, "网络出问题,请检查网络设置", 0)
						.show();
			} else {
				Toast.makeText(UserUnbindPhoneActivity.this,
						result.getMessage(), 0).show();
			}
			} catch (Exception e) {
				MyLog.e("weibao Exception", e);
			}
		}
	}

	class MyTaskUnbind extends AsyncTask<String, Void, UserMail> {
		@Override
		protected void onPreExecute() {
			prDialog = new ProgressDialog(UserUnbindPhoneActivity.this);
			prDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			prDialog.setMessage("请稍等……");
			// 进度条是否不明确
			prDialog.setIndeterminate(true);
			prDialog.setCancelable(true);
			prDialog.show();
			super.onPreExecute();
		}

		@Override
		protected UserMail doInBackground(String... params) {
			String url = UrlComponent.getUnbind_Get(params[0], params[1]);
			MyLog.i(">>>>>>>>history" + url);
			BusinessSearch search = new BusinessSearch();
			UserMail _Result = null;
			try {
				_Result = search.getEmail(url);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return _Result;

		}

		@Override
		protected void onPostExecute(UserMail result) {
			super.onPostExecute(result);
			try {
				MyLog.i("weibao result" + result);
				if (null == result) {
					Toast.makeText(UserUnbindPhoneActivity.this,
							"网络出问题,请检查网络设置", 0).show();
				} else {
					prDialog.cancel();
					Toast.makeText(UserUnbindPhoneActivity.this,
							result.getMessage(), 0).show();
					finish();
				}

			} catch (Exception e) {
				MyLog.e("weibao Exception", e);
			}
		}
	}

}
