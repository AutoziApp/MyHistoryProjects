package com.jy.environment.activity;

import com.jy.environment.R;
import com.jy.environment.base.ActivityBase;
import com.jy.environment.util.MD5;
import com.jy.environment.util.SharedPreferencesUtil;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

public class AuthorityOldActivity extends ActivityBase implements OnClickListener{

	private static final String AUTHORITY_PASSWORD = "hnst.zhb";
	private EditText etInputPwd;
	private Button btnGetHighest;
	private Context context;
	private SharedPreferencesUtil preferencesUtil;
	private ImageView back;

	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		setContentView(R.layout.activity_authority);
		
		initView();
		initData();
	}

	private void initView() {
		back = (ImageView) findViewById(R.id.back);
		etInputPwd = (EditText) findViewById(R.id.et_input_password);
		btnGetHighest = (Button) findViewById(R.id.btn_get_highest);
		btnGetHighest.setText("点击获取旧版数据权限");
	}

	private void initData() {
		back.setOnClickListener(this);
		btnGetHighest.setOnClickListener(this);
		context = this;
		preferencesUtil = SharedPreferencesUtil.getInstance(context);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.back:
			finish();
			break;
		case R.id.btn_get_highest:
			String plaintextPwd = AUTHORITY_PASSWORD;
			String ciphertextPwd = MD5.md5(plaintextPwd);
			String inputPwd = etInputPwd.getText().toString().trim();
			String inputPwdMd5 = MD5.md5(inputPwd);
			if(ciphertextPwd==null||inputPwdMd5==null){
				showToastLong("密码校验为NULL");
				return;
			}
			if(ciphertextPwd.equals(inputPwdMd5)){
				preferencesUtil.setoldshow(true);
				showToastLong("您已获取旧版数据权限");
				finish();
			}else{
				etInputPwd.setText("");
				showToastShort(TextUtils.isEmpty(inputPwd)?"密码为空":"密码错误，请重新输入");
			}
			break;
		}
	}
	
}
