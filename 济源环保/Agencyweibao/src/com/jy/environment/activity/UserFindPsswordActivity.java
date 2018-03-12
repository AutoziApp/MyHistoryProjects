package com.jy.environment.activity;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.jy.environment.R;
import com.jy.environment.base.ActivityBase;
import com.jy.environment.business.BusinessSearch;
import com.jy.environment.controls.AsyncTask;
import com.jy.environment.model.DiscoverFlagModel;
import com.jy.environment.util.MyLog;
import com.jy.environment.webservice.UrlComponent;
import com.umeng.analytics.MobclickAgent;

/**
 * 密码找回
 * 
 * @author baiyuchuan
 * 
 */
public class UserFindPsswordActivity extends ActivityBase {
    private ImageView back;
    private EditText email_et, find_username_et;
    private Button submit;
    String usename, email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
	// TODO Auto-generated method stub
	super.onCreate(savedInstanceState);
	requestWindowFeature(Window.FEATURE_NO_TITLE);
	setContentView(R.layout.user_find_pssword_activity);
	findViewById();
	initListener();

    }

    private void findViewById() {
	back = (ImageView) findViewById(R.id.findPWD_return_iv);
	email_et = (EditText) findViewById(R.id.findPWD_email_et);
	find_username_et = (EditText) findViewById(R.id.findPWD_userName_et);
	submit = (Button) findViewById(R.id.findPWD_submit);
    }

    private void initListener() {
	back.setOnClickListener(new OnClickListener() {

	    @Override
	    public void onClick(View v) {
		finish();
	    }
	});

	submit.setOnClickListener(new OnClickListener() {

	    @Override
	    public void onClick(View v) {
		// TODO Auto-generated method stub
		email = email_et.getText().toString().trim();
		usename = find_username_et.getText().toString().trim();
		if (!email.equals("") || !usename.equals("")) {
			MobclickAgent.onEvent(UserFindPsswordActivity.this,"WGetPSDSubmit");
		    FindPasswordTask findPasswordTask = new FindPasswordTask();
		    findPasswordTask.execute();
		} else {
		    Toast.makeText(UserFindPsswordActivity.this, "邮箱不能为空", 0)
			    .show();
		}
	    }
	});
    }

    private class FindPasswordTask extends AsyncTask<Void, Void, DiscoverFlagModel> {
	@Override
	protected DiscoverFlagModel doInBackground(Void... arg0) {
	    String url = UrlComponent.passwordReFind_Get(usename, email);
	    BusinessSearch search = new BusinessSearch();
	    DiscoverFlagModel _Result = null;
	    try {
		_Result = search.findPassword(url);
		return _Result;
	    } catch (Exception e) {
		e.printStackTrace();
	    }
	    return _Result;

	}

	protected void onPostExecute(DiscoverFlagModel result) {
		 try {
				MyLog.i("weibao result:" + result);
	    if (null != result && result.isFlag()) {
		Toast.makeText(UserFindPsswordActivity.this, "请登录邮箱获取您的密码", 1)
			.show();
		finish();
	    } else {
		Toast.makeText(UserFindPsswordActivity.this, "请检查用户名或邮箱是否输入正确",
			0).show();
	    }
		 } catch (Exception e) {
				MyLog.e("weibao Exception", e);
			}
	};

    }

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		MobclickAgent.onPause(this);
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		MobclickAgent.onResume(this);
	}
    
    
}
