package com.mapuni.mobileenvironment.login;

import android.app.Activity;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mapuni.mobileenvironment.R;
import com.mapuni.mobileenvironment.activity.AuthenActivity;
import com.mapuni.mobileenvironment.activity.MainActivity;
import com.mapuni.mobileenvironment.app.DataApplication;
import com.mapuni.mobileenvironment.config.PathManager;
import com.mapuni.mobileenvironment.model.AuthenModel;
import com.mapuni.mobileenvironment.model.LoginModel;
import com.mapuni.mobileenvironment.update.BaseAutoUpdate;
import com.mapuni.mobileenvironment.utils.DisplayUitl;
import com.mapuni.mobileenvironment.utils.JsonUtil;
import com.mapuni.mobileenvironment.utils.Net;
import com.mapuni.mobileenvironment.utils.PositionUtil;
import com.mapuni.mobileenvironment.view.YutuLoading;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Request;


/**
 * 登录activity
 */
public class LoginActivity extends Activity {

    private RelativeLayout ivRecord;
    private TextView ivAuto;
    /**
     * logo
     */
    private ImageView ivRemeber;
    /**
     * 用户名图标
     */
    private ImageView ivUser;
    /**
     * 密码图标
     */
    private ImageView ivPass;
    /**
     * 密码
     */
    private EditText passWord;
    /**
     * 用户名
     */
    private EditText userName;
    /**
     * 登录按钮
     */
    private Button btnLogin;
    /**
     * 登录model
     */
    private LoginModel loginModel;
    /**
     * 系统设置
     */
    private Button btnSetteing;

    private BaseAutoUpdate baseAutoUpdate;
    private HashMap<String, String> verson;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);
        DataApplication.translucentStatusBar(this);
//        initView();
//        initData();
//        addListener();
        PositionUtil.setLocation(this);//刷新定位信息并存入sharedpreference
        Object obj = getIntent().getExtras().get("authen");
       if(obj instanceof AuthenModel){
            String s = ((AuthenModel) obj).getResult();
            if(s.equals("通过")){
                startActivity( MainActivity.class);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                finish();
                return;
            }
        }
        startActivity(AuthenActivity.class);
        //startActivity(MainActivity.class);
        finish();
    }
    /**
     * 添加监听器
     */
    private void addListener() {
        addLoginListener();
        /*用户名焦点处理*/
    userName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
       //             ((View) autoTvUsername.getParent()).setBackgroundResource(R.drawable.editdown);
       //             ivUser.setImageResource(R.drawable.username_down);
                } else {
       //             ((View) autoTvUsername.getParent()).setBackgroundResource(R.drawable.editup);
       //             ivUser.setImageResource(R.drawable.username_up);
                }
            }
        });
        /*密码焦点处理*/
        passWord.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
        //            ((View) etPassword.getParent()).setBackgroundResource(R.drawable.editdown);
        //            ivPass.setImageResource(R.drawable.password_down);
                } else {
        //            ((View) etPassword.getParent()).setBackgroundResource(R.drawable.editup);
        //            ivPass.setImageResource(R.drawable.password_up);
                }
            }
        });

        setRecordListener();
        setAutoListener();
    }

    /**
     * 自动登录
     */
    private void setAutoListener() {
        ((View) ivAuto.getParent()).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (DisplayUitl.isFastDoubleClick())
                    return;
                if (loginModel.isAuto()) {
                    loginModel.setAuto(false);
         //           ivAuto.setImageResource(R.drawable.noselect);
                } else {
                    loginModel.setAuto(true);
       //             ivAuto.setImageResource(R.drawable.select);
                }
            }
        });
    }

    /**
     * 记住密码
     */
    private void setRecordListener() {
        ivAuto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                remeberPassword();
            }
        });
        ivRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                remeberPassword();
            }
        });
    }

    private void remeberPassword(){
        if (DisplayUitl.isFastDoubleClick())
            return;
        if (loginModel.isRecord()) {
            loginModel.setRecord(false);
            ivRemeber.setImageResource(R.mipmap.un_dui);

        } else {
            loginModel.setRecord(true);
            ivRemeber.setImageResource(R.mipmap.dui);

        }
    }

    /**
     * 初始化data
     */
    private void initData() {
        loginModel = new LoginModel(this);
        if (loginModel.isRecord()) {
            ivRemeber.setImageResource(R.mipmap.dui);
            userName.setText(loginModel.getUserName());
            passWord.setText(loginModel.getPassword());
        }else{
            ivRemeber.setImageResource(R.mipmap.un_dui);
        }
    }

    private AlertDialog dialog;

    /**
     * 添加点击登陆时候的监听器
     */
    private void addLoginListener() {
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (DisplayUitl.isFastDoubleClick())
                    return;
//                login();
                startActivity(MainActivity.class);
        //        new AsyncCheckNet().execute();
            }
        });
    }

    /**
     * 初始化view
     */
    private void initView() {
        userName = (EditText) findViewById(R.id.userName);
        passWord = (EditText) findViewById(R.id.userPass);
        btnLogin = (Button) findViewById(R.id.btnLogin);
        ivAuto = (TextView) findViewById(R.id.rightEdit);
        ivRecord = (RelativeLayout) findViewById(R.id.remember_btn);
        ivRemeber = (ImageView)findViewById(R.id.remember_view);
    }

    /**
     * 启动主activity
     */
    private void startActivity(Class<?> cls) {
        Intent _Intent = new Intent(this, cls);
        if(cls == AuthenActivity.class){
            _Intent.putExtra("authen", (Serializable) getIntent().getExtras().get("authen"));
        }
        startActivity(_Intent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    private boolean checkLogin(String url, String key, String value){

        if(key.equals("")){
            DataApplication.showToast(getResources().getString(R.string.alert_name_null));
            return false;
        }
        if (value.equals("")){
            DataApplication.showToast(getResources().getString(R.string.alert_password_null));
            return false;
        }
        return  true;
    }

    private void login(){
        final String key = userName.getText().toString().trim();
        final String value = passWord.getText().toString().trim();
        if(TextUtils.isEmpty(key)||TextUtils.isEmpty(value)){
            Toast.makeText(this,"用户名或密码不能为空",Toast.LENGTH_SHORT).show();
            return;
        }
        if(Net.checkNet(this)){//联网情况下正常访问网络登录验证
            if(!checkLogin(PathManager.LOGIN_PATH,key,value))
                return;
            OkHttpUtils
                    .post()
                    .url(PathManager.LOGIN_PATH)
                    .addParams("userId",key)
                    .addParams("loginPwd",value)
                    .build()
                    .execute(new StringCallback() {
                        @Override
                        public void onBefore(Request request, int id) {
                            super.onBefore(request, id);
                            if (yutuLoading == null) {
                                yutuLoading = new YutuLoading(LoginActivity.this);
                            }
                            yutuLoading.setLoadMsg("请稍后", "");
                            yutuLoading.showDialog();
                        }

                        @Override
                        public void onAfter(int id) {
                            super.onAfter(id);
                            if(yutuLoading!=null){
                                yutuLoading.dismissDialog();
                            }

                        }

                        @Override
                        public void onError(Call call, Exception e, int id) {
                            Toast.makeText(LoginActivity.this, "error:"+e.getMessage(), Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onResponse(String response, int id) {

//                            String json ="{\"ping\": \"Hello World\",\"string\": \"Hello World\"}";
//                            ArrayList<HashMap<String,Object>>  data = new ArrayList<HashMap<String, Object>>();
                            Map<String,String> map = new HashMap<String, String>();
                            map = (Map<String,String>) JsonUtil.jsonToMap(response);
                            if(Integer.parseInt(map.get("flag"))==1){
                                DataApplication.showToast("登录成功");
                                loginModel.putPassWord(key,value);
                                startActivity(MainActivity.class);
                            }
                            else{
                                DataApplication.showToast((String)map.get("err"));
                            }
                        }
                    });
        }else {//离线情况下从本地数据库校验用户名和密码

        }


    }

    YutuLoading yutuLoading;

    class AsyncCheckNet extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            if (yutuLoading == null) {
                yutuLoading = new YutuLoading(LoginActivity.this);
            }
            yutuLoading.setLoadMsg("请稍后", "");
            yutuLoading.showDialog();
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String s) {
            if (yutuLoading != null) {
                yutuLoading.dismissDialog();
            }
            if (s.contains("成功")) {
                if (loginModel.login(userName.getText().toString(), passWord.getText().toString()))
                    startActivity(MainActivity.class);
            } else {
                dialog = new AlertDialog.Builder(LoginActivity.this)
                        .setTitle("检查网络")
                        .setMessage("网络接连接失败，请检查是否连专网")
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (DisplayUitl.isFastDoubleClick())
                                    return;
                                dialog.dismiss();
                            }
                        })
                        .setPositiveButton("检查", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (DisplayUitl.isFastDoubleClick())
                                    return;
                                // TODO Auto-generated method stub
                                Intent intent = null;
                                //判断手机系统的版本  即API大于10 就是3.0或以上版本
                                if (android.os.Build.VERSION.SDK_INT > 10) {
                                    intent = new Intent(android.provider.Settings.ACTION_WIRELESS_SETTINGS);
                                } else {
                                    intent = new Intent();
                                    ComponentName component = new ComponentName("com.android.settings","com.android.settings.WirelessSettings");
                                    intent.setComponent(component);
                                    intent.setAction("android.intent.action.VIEW");
                                }
                                LoginActivity.this.startActivity(intent);
                            }
                        }).create();
                dialog.show();
            }
        }

        @Override
        protected String doInBackground(String... params) {
            // http://192.168.4.69:6111/WebService1.asmx
            if (Net.checkURL(PathManager.WebServiceMethodsUrl)) {
          //  if (Net.checkURL("http://192.168.4.69:6111/WebService1.asmx")) {
                return "服务器连接成功!";
            } else {
                return "服务器连接失败!";
            }
        }
    }

}
