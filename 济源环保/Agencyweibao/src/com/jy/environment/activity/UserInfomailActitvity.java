package com.jy.environment.activity;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
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

public class UserInfomailActitvity extends ActivityBase {

	private String mail_name;
	private String mail_name_xg;
	private String userid;
	private String type;
	private EditText mail_text;
	private ImageView back;
	private Button btnCancle, btnBound;
	private UpdateNcTask updateNcTask;
	private String SHARE_LOGIN_USERMAIL = "MAP_LOGIN_USERMAIL";
	private final String SHARE_LOGIN_TAG = "MAP_SHARE_LOGIN_TAG";
	private Drawable drawable2;
	ProgressDialog prDialog;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.user_info_mail);
		Intent intent = getIntent();
		mail_name = intent.getStringExtra("content");
		MyLog.i("<<<<<<<>>>>>mail"+" "+mail_name);
		userid = intent.getStringExtra("userid");
		MyLog.i("<<<<<<<>>>userid"+userid);
		type = intent.getStringExtra("type");
		initView();
		mail_text.setText(mail_name);
		MyLog.i("==========="+mail_text.getText().toString());
		if(mail_text.getText().toString().equals("")){
			btnBound.setEnabled(false);
			Drawable drawable = this.getResources().getDrawable(R.drawable.bound_normal_un);
			btnBound.setBackgroundDrawable(drawable);
		}
		drawable2 = this.getResources().getDrawable(R.drawable.after_drawable_bound_bg);
		initListener();
	}

	private void initView() {
		mail_text = (EditText) findViewById(R.id.userinfo_mail);
		back = (ImageView) findViewById(R.id.afterlogin_return_iv_mail);
		btnBound = (Button) findViewById(R.id.user_info_mail_btnBound);
		
	}

	void initListener() {
		mail_text.setOnFocusChangeListener(new OnFocusChangeListener() {
			
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				// TODO Auto-generated method stub
				if(hasFocus){
					btnBound.setEnabled(true);
					btnBound.setBackgroundDrawable(drawable2);
				}
			}
		});
		back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});
		
		btnBound.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if(isEmail(mail_text.getText().toString())){
					WeiBaoApplication.setUserMail(mail_text.getText().toString());
					new MyTask().execute(userid,mail_text.getText().toString());
				}else if(mail_text.getText().toString().equals("")){
					Toast.makeText(UserInfomailActitvity.this, "请填写您的邮箱！", 0).show();
				}else {
					Toast.makeText(UserInfomailActitvity.this, "邮箱格式不正确！", 0).show();;
				}
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
			prDialog = new ProgressDialog(UserInfomailActitvity.this);
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
					MyLog.i("weibao result:" + result);
			super.onPostExecute(result);
			prDialog.cancel();
			if (result) {
				Toast.makeText(UserInfomailActitvity.this, "修改成功", 0).show();
				SharedPreferences share = getSharedPreferences(SHARE_LOGIN_TAG,
						0);
				share.edit().putString(SHARE_LOGIN_USERMAIL, mail_name_xg)
						.commit();
				WeiBaoApplication.setUserMail(mail_name_xg);
				finish();
			} else {
				Toast.makeText(UserInfomailActitvity.this, "修改失败", 0).show();
			}
			
			 } catch (Exception e) {
					MyLog.e("weibao Exception", e);
				}
		}

	}

	class MyTask extends AsyncTask<String, Void, UserMail> {
		
		
		

		@Override
		protected void onPreExecute() {
		    prDialog = new ProgressDialog(UserInfomailActitvity.this);
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
			String url = UrlComponent.getEmail_Get(params[0],params[1]);
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
				Toast.makeText(UserInfomailActitvity.this,
						"网络出问题,请检查网络设置", 0).show();
			} else {
				prDialog.cancel();
				Toast.makeText(UserInfomailActitvity.this, result.getMessage(), 0).show();
			}
		} catch (Exception e) {
			MyLog.e("weibao Exception", e);
		}

		}
	}

}
