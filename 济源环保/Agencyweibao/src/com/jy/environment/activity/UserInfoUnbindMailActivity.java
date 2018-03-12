package com.jy.environment.activity;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.jy.environment.R;
import com.jy.environment.base.ActivityBase;
import com.jy.environment.business.BusinessSearch;
import com.jy.environment.controls.AsyncTask;
import com.jy.environment.controls.WeiBaoApplication;
import com.jy.environment.model.DiscoverFlagModel;
import com.jy.environment.model.UserMail;
import com.jy.environment.util.MyLog;
import com.jy.environment.webservice.UrlComponent;

public class UserInfoUnbindMailActivity extends ActivityBase {

	private String mail_name;
	private String mail_name_xg;
	private String userid;
	private String type;
	private EditText mail_text;
	private ImageView back;
	private Button btnCancle, btnUnBound;
	private UpdateNcTask updateNcTask;
	private ProgressDialog prDialog;
	private String SHARE_LOGIN_USERMAIL = "MAP_LOGIN_USERMAIL";
	private final String SHARE_LOGIN_TAG = "MAP_SHARE_LOGIN_TAG";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.user_info_mail_unbind);
		Intent intent = getIntent();
		mail_name = intent.getStringExtra("content");
		userid = intent.getStringExtra("userid");
		type = intent.getStringExtra("type");
		initView();
		mail_text.setText(mail_name);
		mail_text.setEnabled(false);
		initListener();
	}

	private void initView() {
		mail_text = (EditText) findViewById(R.id.userinfo_mail);
		back = (ImageView) findViewById(R.id.afterlogin_return_iv_mail);
		btnUnBound = (Button) findViewById(R.id.user_info_mail_btnUnBound);
		
	}

	void initListener() {
		back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});
		
		btnUnBound.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
	
				/*userid="8954";
				mail_name="xuhuaiguang@yeah.net";*/
				new MyTaskUnBind().execute(userid,mail_name);
				
			}
		});

		
	}

	public static boolean isEmail(String strEmail) {
		String strPattern = "^\\s*\\w+(?:\\.{0,1}[\\w-]+)*@[a-zA-Z0-9]+(?:[-.][a-zA-Z0-9]+)*\\.[a-zA-Z]+\\s*$";
		Pattern p = Pattern.compile(strPattern);
		Matcher m = p.matcher(strEmail);
		return m.matches();
	}

	public class UpdateNcTask extends AsyncTask<String, Void, Boolean> {

		private ProgressDialog prDialog;

		@Override
		protected void onPreExecute() {
			prDialog = new ProgressDialog(UserInfoUnbindMailActivity.this);
			prDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			prDialog.setMessage("正在努力上传中……");

			// 进度条是否不明确
			prDialog.setIndeterminate(true);
			prDialog.setCancelable(true);
			prDialog.show();
			super.onPreExecute();
			View view = getWindow().peekDecorView();
			if (view != null) {
				InputMethodManager inputmanger = (InputMethodManager) getSystemService(DiscoverBlogPostCommentsActivity.INPUT_METHOD_SERVICE);
				inputmanger.hideSoftInputFromWindow(view.getWindowToken(), 0);
			}

		}

		@Override
		protected Boolean doInBackground(String... params) {
			String url = UrlComponent.updateUserInfo();
			BusinessSearch search = new BusinessSearch();
			try {
				DiscoverFlagModel _Result = search.updateUserInfo(url, userid,
						mail_name_xg, type);
				return _Result.isFlag();
			} catch (Exception e) {
				e.printStackTrace();
			}

			return false;
		}

		@Override
		protected void onPostExecute(Boolean result) {
			try {
				MyLog.i("weibao result :" + result);
			super.onPostExecute(result);
			prDialog.cancel();
			if (result) {
				Toast.makeText(UserInfoUnbindMailActivity.this, "修改成功", 0).show();
				SharedPreferences share = getSharedPreferences(SHARE_LOGIN_TAG,
						0);
				share.edit().putString(SHARE_LOGIN_USERMAIL, mail_name_xg)
						.commit();
				WeiBaoApplication.setUserMail(mail_name_xg);
				finish();
			} else {
				Toast.makeText(UserInfoUnbindMailActivity.this, "修改失败", 0).show();
			}
			
		} catch (Exception e) {
			MyLog.e("weibao Exception", e);
		}
		}

	}

	class MyTaskUnBind extends AsyncTask<String, Void, UserMail> {
		@Override
		protected void onPreExecute() {
		    prDialog = new ProgressDialog(UserInfoUnbindMailActivity.this);
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
			String url = UrlComponent.getEmail_UnBind(params[0],params[1]);
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
				MyLog.i("weibao result:" + result);
			if (null == result) {
				Toast.makeText(UserInfoUnbindMailActivity.this,
						"网络出问题,请检查网络设置", 0).show();
			} else {
				prDialog.cancel();
				Toast.makeText(UserInfoUnbindMailActivity.this, result.getMessage(), 0).show();
				MyLog.i("xu1123:" + "0");
				WeiBaoApplication.setIsEmailBind("0");
				UserInfoActivity.saveInfoSharePreferences(UserInfoUnbindMailActivity.this,
						WeiBaoApplication.getUserNc(), "", WeiBaoApplication.getPhone(), "0",
						WeiBaoApplication.getIsPhoneBind());
			}
			
			} catch (Exception e) {
				MyLog.e("weibao Exception", e);
			}
		}
	}

}
