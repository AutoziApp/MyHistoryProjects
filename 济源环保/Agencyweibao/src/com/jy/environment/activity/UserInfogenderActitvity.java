package com.jy.environment.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.jy.environment.R;
import com.jy.environment.base.ActivityBase;
import com.jy.environment.business.BusinessSearch;
import com.jy.environment.controls.AsyncTask;
import com.jy.environment.controls.WeiBaoApplication;
import com.jy.environment.model.DiscoverFlagModel;
import com.jy.environment.util.MyLog;
import com.jy.environment.webservice.UrlComponent;

public class UserInfogenderActitvity extends ActivityBase{


	
	private String gender_name;
	private String userid;
	private String type;
	private RadioGroup gender_radio;
	private RadioButton gender_btn1,gender_btn2,gender_btn;
	private ImageView back;
	private Button btn;
	private UpdateNcTask updateNcTask;
	String gender_text;
	private String SHARE_LOGIN_USERGENDER = "MAP_LOGIN_USERGENDER";
	private final String SHARE_LOGIN_TAG = "MAP_SHARE_LOGIN_TAG";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.user_info_sex);
		Intent intent = getIntent();
		gender_name = intent.getStringExtra("content");
		userid = intent.getStringExtra("userid");
		type = intent.getStringExtra("type");
		initView();
		if(gender_name.equals("男"))
		{
			gender_btn1.setChecked(true);
		}else {
			gender_btn2.setChecked(true);
		}
		initListener();
	}


	private void initView() {
		gender_radio = (RadioGroup) findViewById(R.id.userinfo_radiogroup);
		back = (ImageView) findViewById(R.id.afterlogin_return_iv_gender);
		btn = (Button) findViewById(R.id.user_info_gender_btn);
		//gender_btn = (RadioButton) findViewById(gender_radio.getCheckedRadioButtonId());
		gender_btn1 = (RadioButton) findViewById(R.id.userinfo_btn1);
		gender_btn2 = (RadioButton) findViewById(R.id.userinfo_btn2);
	}
	
	 private RadioGroup.OnCheckedChangeListener mChangeRadio = new   
	           RadioGroup.OnCheckedChangeListener()  
	  {   
	    @Override   
	    public void onCheckedChanged(RadioGroup group, int checkedId)  
	    {   
	      if(checkedId==gender_btn1.getId())  
	      {   
	    	gender_btn1.setChecked(true);  
	      }   
	      else if(checkedId==gender_btn2.getId())   
	      { 
	    	gender_btn2.setChecked(true);
	      }         
	    }   
	  };
    void initListener() {
    	gender_radio.setOnCheckedChangeListener(mChangeRadio);
		back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		
		btn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
	    	    gender_btn = (RadioButton) findViewById(gender_radio.getCheckedRadioButtonId());
	    	    gender_text = gender_btn.getText().toString();
	    	    if(gender_text.equals(gender_name))
	    	    {
	    	    	Toast.makeText(UserInfogenderActitvity.this, "未做任何修改", 0).show();
	    	    }else{
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
    	    prDialog = new ProgressDialog(UserInfogenderActitvity.this);
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
    	    DiscoverFlagModel _Result = search.updateUserInfo(url,userid,gender_text,type);
    		return _Result.isFlag();
    	    } catch (Exception e) {
    		e.printStackTrace();
    	    }

    	    return false;
    	}

    	@Override
    	protected void onPostExecute(Boolean result) {

    	    super.onPostExecute(result);
    	    try {
				MyLog.i("weibao result:" + result);
    	    prDialog.cancel();
    	    if (result) {
    	    	Toast.makeText(UserInfogenderActitvity.this, "修改成功", 0).show();
    	    	SharedPreferences share = getSharedPreferences(SHARE_LOGIN_TAG, 0);
    	        share.edit().putString(SHARE_LOGIN_USERGENDER,
    	        		gender_text).commit();
    	 		WeiBaoApplication.setUserGener(gender_text);
    	 		finish();
    	    } else {
    		Toast.makeText(UserInfogenderActitvity.this, "修改失败", 0)
    			.show();
    	    }
    	    } catch (Exception e) {
				MyLog.e("weibao Exception", e);
			}
    	}

        }
	
	
	
	





}
