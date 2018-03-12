package com.jy.environment.activity;

import android.app.ProgressDialog;
import android.content.Intent;
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
import com.jy.environment.util.MyLog;
import com.jy.environment.webservice.UrlComponent;

public class UserInfoncActivity extends ActivityBase {

	private String nc_name;
	private String nc_name_xg;
	private String userid;
	private String type;
	private EditText nc_text;
	private ImageView back;
	private Button btn;
	private UpdateNcTask updateNcTask;
	private View userinfoLayout;
	private String SHARE_LOGIN_NC = "MAP_LOGIN_NC";
	/** 用来操作sharePreferences标识 */
	private final String SHARE_LOGIN_TAG = "MAP_SHARE_LOGIN_TAG";
	private String SHARE_LOGIN_USERNC = "MAP_LOGIN_USERNC";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.user_info_nc);
		Intent intent = getIntent();
		nc_name = intent.getStringExtra("content");
		userid = intent.getStringExtra("userid");
		type = intent.getStringExtra("type");
		initView();
		nc_text.setText(nc_name);
		initListener();
	}

	private void initView() {
		nc_text = (EditText) findViewById(R.id.user_info_edit_nc);
		back = (ImageView) findViewById(R.id.afterlogin_return_iv_nc);
		btn = (Button) findViewById(R.id.user_info_nc_btn);
		userinfoLayout = findViewById(R.layout.user_info);
	}

	void initListener() {
		back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});

		btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				nc_name_xg = nc_text.getText().toString().trim();
				if(nc_name_xg.equals(nc_name))
				{
					Toast.makeText(UserInfoncActivity.this, "未做任何修改！", 0).show();
				}else if (nc_name_xg.equals("")) {
					Toast.makeText(UserInfoncActivity.this, "未填写内容！", 0).show();
				}else if (nc_name_xg.length()>7){
					Toast.makeText(UserInfoncActivity.this, "请填写七个字以内的昵称！", 0).show();
				}
				else{
				updateNcTask = new UpdateNcTask();
				updateNcTask.execute();
				}
			}
		});

	}

	public class UpdateNcTask extends AsyncTask<String, Void, Boolean> {

		private ProgressDialog prDialog;

		@Override
		protected void onPreExecute() {
			prDialog = new ProgressDialog(UserInfoncActivity.this);
			prDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			prDialog.setMessage("修改中……");

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
						nc_name_xg, type);
				MyLog.i("+++++++++++" + _Result);
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
				Toast.makeText(UserInfoncActivity.this, "修改成功", 0).show();
/*				SharedPreferences share = getSharedPreferences(SHARE_LOGIN_TAG, 0);
		        share.edit().putString(SHARE_LOGIN_USERNC,
		        		nc_name_xg).commit();*/
		        WeiBaoApplication.setUserNc(nc_name_xg);
				finish();
			} else {
				Toast.makeText(UserInfoncActivity.this, "修改失败", 0).show();
			}
			
		} catch (Exception e) {
			MyLog.e("weibao Exception", e);
		}
		}

	}
	
}
