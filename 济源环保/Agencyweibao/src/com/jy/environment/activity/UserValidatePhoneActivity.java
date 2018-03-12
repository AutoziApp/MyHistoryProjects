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
import com.jy.environment.controls.WeiBaoApplication;
import com.jy.environment.model.UserMail;
import com.jy.environment.util.MyLog;
import com.jy.environment.webservice.UrlComponent;

public class UserValidatePhoneActivity extends ActivityBase {
	private TimeCount timeCount;
	// 倒计时
	private int recLen = 20;
	private String phone;
	private String newPhone;
	private String userid;
	private String type;
	private EditText phoneEditText;
	private EditText identifyingCodeEditText;
	private ImageView back;
	private Button btnGetIdetifyingCode;
	private Button btnBundle;
	private Drawable drawable2, drawable3;
	ProgressDialog prDialog;
	private String temp = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.user_info_phone);
		Intent intent = getIntent();
		phone = intent.getStringExtra("content");
		userid = intent.getStringExtra("userid");
		type = intent.getStringExtra("type");
		initView();
		phoneEditText.setText(phone);

		/*
		 * if (!"".equals(phoneEditText.getText().toString())) {
		 * 
		 * new MyTask().execute(userid, phone); }
		 */

		if (phoneEditText.getText().toString().equals("")) {
			btnBundle.setEnabled(false);
			btnGetIdetifyingCode.setEnabled(false);
			Drawable drawable = this.getResources().getDrawable(
					R.drawable.normal_code_un);
			btnGetIdetifyingCode.setBackgroundDrawable(drawable);
			Drawable drawable1 = this.getResources().getDrawable(
					R.drawable.bound_normal_un);
			btnBundle.setBackgroundDrawable(drawable1);
		}
		drawable2 = this.getResources().getDrawable(
				R.drawable.verification_code_obtain);
		drawable3 = this.getResources().getDrawable(
				R.drawable.after_drawable_bound_bg);
		newPhone = phoneEditText.getText().toString();
		initListener();
	}

	private void initView() {
		phoneEditText = (EditText) findViewById(R.id.user_info_edit_phone);
		identifyingCodeEditText = (EditText) findViewById(R.id.user_info_edit_identifyingcode);
		back = (ImageView) findViewById(R.id.afterlogin_return_iv_phone);
		btnGetIdetifyingCode = (Button) findViewById(R.id.btn_code);
		btnBundle = (Button) findViewById(R.id.btn_bundle);
		timeCount = new TimeCount(60000, 1000);
	}

	void initListener() {
		phoneEditText.setOnFocusChangeListener(new OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				// TODO Auto-generated method stub
				if (hasFocus) {
					btnGetIdetifyingCode.setBackgroundDrawable(drawable2);
					btnGetIdetifyingCode.setEnabled(true);
				}
			}
		});

		identifyingCodeEditText
				.setOnFocusChangeListener(new OnFocusChangeListener() {

					@Override
					public void onFocusChange(View v, boolean hasFocus) {
						// TODO Auto-generated method stub
						if (hasFocus) {
							btnBundle.setBackgroundDrawable(drawable3);
							btnBundle.setEnabled(true);
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
				/*
				 * phone = phoneEditText.getText().toString(); if
				 * (phone.equals("")) {
				 * Toast.makeText(UserValidatePhoneActivity.this, "请输入您的手机号",
				 * 0).show(); } else if (!isMobile(phone)) {
				 * Toast.makeText(UserValidatePhoneActivity.this, "请输入13位手机号码",
				 * 0).show(); } else { timeCount.start(); new
				 * MyTask().execute(userid, phone); }
				 */
				newPhone = phoneEditText.getText().toString();
				if (isMobile(newPhone)) {

					MyLog.i("---temp" + temp);
					new MyTask().execute(userid, newPhone);

				} else {
					Toast.makeText(UserValidatePhoneActivity.this,
							"请输入13位手机号码", 0).show();
				}

			}
		});
		btnBundle.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (phoneEditText.getText().toString().isEmpty()) {
					Toast.makeText(UserValidatePhoneActivity.this, "手机号码不能为空",
							3000).show();
				} else {
					if (!isMobile(phoneEditText.getText().toString())) {
						Toast.makeText(UserValidatePhoneActivity.this,
								"请输入13位手机号码", 3000).show();
						phoneEditText.setText("");
					}
					if (identifyingCodeEditText.getText().toString().isEmpty()) {
						Toast.makeText(UserValidatePhoneActivity.this,
								"验证码不能为空", 3000).show();
					}
					WeiBaoApplication.setPhone(phoneEditText.getText()
							.toString());
					new MyTaskBind().execute(userid, identifyingCodeEditText
							.getText().toString());
				}

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
			String url = UrlComponent.getPhone_Get(params[0], params[1]);
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
					Toast.makeText(UserValidatePhoneActivity.this,
							"网络出问题,请检查网络设置", 0).show();
				} else {
					Toast.makeText(UserValidatePhoneActivity.this,
							result.getMessage(), 0).show();
					temp = result.getMessage();
					// finish();
				}
				if (temp.contains("手机号码已经被绑定")) {
					MyLog.i("---temp" + temp);
					return;
				} else {
					timeCount.start();

				}

			} catch (Exception e) {
				MyLog.e("weibao result", e);
			}
		}
	}

	class MyTaskBind extends AsyncTask<String, Void, UserMail> {

		@Override
		protected void onPreExecute() {
			prDialog = new ProgressDialog(UserValidatePhoneActivity.this);
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
			String url = UrlComponent.getBound_Get(params[0], params[1]);
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
					Toast.makeText(UserValidatePhoneActivity.this,
							"网络出问题,请检查网络设置", 0).show();
				} else {
					prDialog.cancel();
					Toast.makeText(UserValidatePhoneActivity.this,
							result.getMessage(), 0).show();
					finish();
				}
			} catch (Exception e) {
				MyLog.e("weibao Exception", e);
			}

		}
	}

}
