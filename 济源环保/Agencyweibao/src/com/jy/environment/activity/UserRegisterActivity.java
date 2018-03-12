package com.jy.environment.activity;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.jy.environment.R;
import com.jy.environment.base.ActivityBase;
import com.jy.environment.business.BusinessSearch;
import com.jy.environment.controls.AsyncTask;
import com.jy.environment.model.UserRegisterModel;
import com.jy.environment.util.CommonUtil;
import com.jy.environment.util.MyLog;
import com.jy.environment.webservice.UrlComponent;
import com.umeng.analytics.MobclickAgent;

/**
 * 用户注册
 * 
 * @author baiyuchuan
 * 
 */
public class UserRegisterActivity extends ActivityBase {
    private EditText view_userName, view_email;
    private EditText view_password;
    private EditText view_passwordConfirm;
    private Button view_submit;
    ImageView back;
    // private Button view_clearAll;
    // String from = "1";// 其渠道
    Context context;
    public static boolean REGISTER_FLAG = false;
    boolean flag;// 条件成立跳转到登录界面

    @Override
    protected void onCreate(Bundle savedInstanceState) {

	// TODO Auto-generated method stub
	super.onCreate(savedInstanceState);
	requestWindowFeature(Window.FEATURE_NO_TITLE);
	setContentView(R.layout.user_register_activity);
	context = this;
	findViews();
	setListener();
	// 然后执行注册的事情,访问远程服务器注册用户
    }

    /** 1.初始化注册view组件 */
    private void findViews() {
	view_userName = (EditText) findViewById(R.id.registerUserName);
	view_userName
		.setFilters(new InputFilter[] { new InputFilter.LengthFilter(30) });
	view_email = (EditText) findViewById(R.id.email_et);
	view_password = (EditText) findViewById(R.id.registerPassword);
	
	// 限制最大密码长度
	view_password
		.setFilters(new InputFilter[] { new InputFilter.LengthFilter(15) });
	view_passwordConfirm = (EditText) findViewById(R.id.confirm_pwd_et);
	view_passwordConfirm
		.setFilters(new InputFilter[] { new InputFilter.LengthFilter(15) });
	view_password.setInputType(InputType.TYPE_CLASS_TEXT
		| InputType.TYPE_TEXT_VARIATION_PASSWORD);
	view_passwordConfirm.setInputType(InputType.TYPE_CLASS_TEXT
		| InputType.TYPE_TEXT_VARIATION_PASSWORD);
	view_submit = (Button) findViewById(R.id.registerSubmit);
	back = (ImageView) findViewById(R.id.register_return_iv);
	// view_clearAll = (Button) findViewById(R.id.registerClear);
    }

    private void setListener() {
	view_submit.setOnClickListener(submitListener);
	view_password.setOnClickListener(new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			int Len = view_password.getEditableText().toString().length();
			view_password.setSelection(Len);
		}
	});
	view_passwordConfirm.addTextChangedListener(new TextWatcher() {
		
		@Override
		public void onTextChanged(CharSequence s, int start, int before, int count) {
			// TODO Auto-generated method stub
			String editable = view_passwordConfirm.getText().toString();   
	        String str = stringFilter(editable.toString()); 
	        if(!editable.equals(str)){ 
	        	view_passwordConfirm.setText(str); 
	            //设置新的光标所在位置 www.2cto.com    
	        	view_passwordConfirm.setSelection(str.length()); 
	        } 
		}
		
		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public void afterTextChanged(Editable s) {
			// TODO Auto-generated method stub
			
		}
	});
	view_password.addTextChangedListener(new TextWatcher() {
		
	    @Override  
	    public void onTextChanged(CharSequence s, int start, int before, int count) { 
	    	String editable = view_password.getText().toString();   
	        String str = stringFilter(editable.toString()); 
	        if(!editable.equals(str)){ 
	        	view_password.setText(str); 
	            //设置新的光标所在位置 www.2cto.com    
	        	view_password.setSelection(str.length()); 
	        } 
	    }  
	    @Override  
	    public void beforeTextChanged(CharSequence s, int start, int count, int after) {  
	        /*tmp = s.toString();  */
	    }  
	      
	    @Override  
	    public void afterTextChanged(Editable s) {  
	      
	    }  
	   
	});
	// view_clearAll.setOnClickListener(clearListener);
	back.setOnClickListener(new OnClickListener() {

	    @Override
	    public void onClick(View v) {
		// TODO Auto-generated method stub
		finish();
	    }
	});
    }

    public static String stringFilter(String str)throws PatternSyntaxException{      
        // 只允许字母和数字        
        String   regEx  =  "[^a-zA-Z0-9]";                      
        Pattern   p   =   Pattern.compile(regEx);      
        Matcher   m   =   p.matcher(str);      
        return   m.replaceAll("").trim();      
    }   
    /** 监听注册确定按钮 */
    private OnClickListener submitListener = new OnClickListener() {
	@Override
	public void onClick(View v) {
	    String userName = view_userName.getText().toString().trim();
	    if (null != userName && userName.length() < 6 || userName.length() >15) {
		CommonUtil.showToast(context, "用户名长度不符合要求");
		return;
	    }
	    String password = view_password.getText().toString().trim();
	    String passwordConfirm = view_passwordConfirm.getText().toString()
		    .trim();
	   // String email = view_email.getText().toString().trim();
	    String email = "";

	    validateForm(userName, password, passwordConfirm);//
	}
    };
    
    public static boolean isUserName(String strUsername){
    String strPattern = "^[a-zA-Z][a-zA-Z0-9_]*$";
    Pattern p = Pattern.compile(strPattern);
    Matcher m = p.matcher(strUsername);
    return m.matches();
    	
    }
    public static boolean isEmail(String strEmail) {
	String strPattern = "^\\s*\\w+(?:\\.{0,1}[\\w-]+)*@[a-zA-Z0-9]+(?:[-.][a-zA-Z0-9]+)*\\.[a-zA-Z]+\\s*$";
	Pattern p = Pattern.compile(strPattern);
	Matcher m = p.matcher(strEmail);
	return m.matches();
    }

    /** 检查注册表单 */
    private void validateForm(final String userName, final String password, String pwdConfirm) {

		String suggest = "";

		if (userName.length() < 1) {
			suggest = "用户名不能为空";
			CommonUtil.showToast(context, suggest);
			return;
		}
		if(!isUserName(userName)){
			suggest = "用户名格式不正确";
			CommonUtil.showToast(context, suggest);
			return;
		}
/*		if (email.length() < 1) {
			suggest = "邮箱不能为空";
			CommonUtil.showToast(context, suggest);
			return;
		}
		if (!isEmail(email)) {
			suggest = "邮箱格式不正确";
			CommonUtil.showToast(context, suggest);
			return;

		}*/
		if (password.length() < 1) {
			suggest = "密码不能为空";
			CommonUtil.showToast(context, suggest);
			return;
		}
		if (!password.equals(pwdConfirm)) {
			suggest = "两次密码不一致,请重新填写";
			CommonUtil.showToast(context, suggest);
			return;
		}
		/** 请求服务器 */
		if (userName != null || password != null ) {
		    Register register = new Register();
		    String email = "";
		    MobclickAgent.onEvent(UserRegisterActivity.this,"WRegister");
		    register.execute(userName, password,email);
			    

		}

	}

    private class Register extends AsyncTask<String, Void, UserRegisterModel> {
	@Override
	protected UserRegisterModel doInBackground(String... arg0) {
	    String userName = arg0[0];
	    String password = arg0[1];
	    String email = arg0[2];
	    String url = UrlComponent.register_Get(userName, password);
	    BusinessSearch search = new BusinessSearch();
	    UserRegisterModel _Result = null;
	    try {
		_Result = search.register(url);
		MyLog.i("_Result"+_Result);
		return _Result;
	    } catch (Exception e) {
		e.printStackTrace();
	    }
	    return _Result;
	}

	protected void onPostExecute(UserRegisterModel result) {
		try {
			MyLog.i("weibao result" + result);
		
	    if(null != result && result.isFlag()){
		CommonUtil.showToast(context, "注册成功");
		finish();
	    }else{
		CommonUtil.showToast(context, result.getMsg());
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
