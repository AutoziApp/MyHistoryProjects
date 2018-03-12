package com.yutu.car.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.xw.repo.XEditText;
import com.yutu.car.R;
import com.yutu.car.bean.LoginBean;
import com.yutu.car.config.PathManager;
import com.yutu.car.presenter.NetControl;
import com.yutu.car.utils.JsonUtil;
import com.yutu.car.utils.SPUtils;
import com.yutu.car.views.YutuLoading;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.Map;

import okhttp3.Call;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    TextView tvIP;
    /*服务器的IP设置按钮*/
    private TextView loggin_ip;
    /*登录按钮*/
    private Button login_btn;
    /*输入用户名*/
    private AutoCompleteTextView login_username;
    /*输入密码*/
    private EditText login_pwd;
    /*记住密码*/
    private CheckBox remember_pwd;
    /*当前用户不存在*/
    private final int NOUSER = 0;
    /*密码错误*/
    private final int PASSWORDERR = 1;
    /*无网络连接*/
    private final int NONTE = 2;
    /*登录成功*/
    private final int LOGIN_SUCCESS = 4;
    /*用户密码不能为空*/
    private final int UESE_PASS_NULL = 5;
    private NetControl netControl;
    Context mContext;
    private String userName, password;
    private YutuLoading yutuLoading;
    private LoginBean bean;
    private Boolean turnon;
    private Map map;
    public static String UserName;
    SharedPreferences sp = null;
    protected Handler login_handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case NOUSER:
                    Toast.makeText(mContext, "当前用户不存在", Toast.LENGTH_LONG).show();
                    break;
                case PASSWORDERR:
                    Toast.makeText(mContext, "用户密码输入错误", Toast.LENGTH_LONG).show();
                    break;
                case NONTE:
                    Toast.makeText(mContext, "无网络连接", Toast.LENGTH_LONG).show();
                    break;
                case LOGIN_SUCCESS:
                    //loginController.hideDialog(LoginController.HANDLE_HIDE_LoginDialog);
                    break;
                case UESE_PASS_NULL:
                    Toast.makeText(mContext, "用户名密码不能为空", Toast.LENGTH_LONG).show();
                    break;
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.base_login);
        sp = this.getSharedPreferences("userinfo", Context.MODE_PRIVATE);
        //获取baseUrl
        String baseUrl= (String) SPUtils.getSp(this, "newIP", PathManager.newIp);
        //设置baseUrl
        PathManager.setPathUrl(baseUrl);
        init();
        netControl = new NetControl();
        yutuLoading = new YutuLoading(this);
    }



    private void init() {
        tvIP= (TextView) findViewById(R.id.tv_IP);
        loggin_ip = (TextView) findViewById(R.id.login_sz);
        login_btn = (Button) findViewById(R.id.login_btn_login);
        login_username = (AutoCompleteTextView) findViewById(R.id.login_edit_account);
        login_pwd = (EditText) findViewById(R.id.login_edit_pwd);
        remember_pwd = (CheckBox) findViewById(R.id.login_cb_visible);
        if (sp.getBoolean("CheckBoxLogin", false)) {
            login_username.setText(sp.getString("uname", null));
            login_pwd.setText(sp.getString("upswd", null));
            remember_pwd.setChecked(true);
        }

        login_btn.setOnClickListener(this);
        tvIP.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_IP:
                //展示设置IP的对话框
                showCustomDialog();
                break;

            case R.id.login_btn_login:
                if (TextUtils.isEmpty(login_username.getText()) || TextUtils.isEmpty(login_pwd.getText())) {
                    Toast.makeText(this, "账号或密码不能为空!", Toast.LENGTH_SHORT).show();
                    return;
                }else if(!isNetworkAvailable(this)){
                    Toast.makeText(this, "当前网络不可用!", Toast.LENGTH_SHORT).show();
                    return;
                }

                userName = login_username.getText().toString();
                password = login_pwd.getText().toString();
                Log.d("zqq", "response=====" +  userName + password);
                boolean CheckBoxLogin = remember_pwd.isChecked();
                if (CheckBoxLogin) {
                    Editor editor = sp.edit();
                    editor.putString("uname", userName);
                    editor.putString("upswd", password);
                    editor.putBoolean("CheckBoxLogin", true);
                    editor.commit();

                } else {
                    Editor editor = sp.edit();
                    editor.putString("uname", null);
                    editor.putString("upswd", null);
                    editor.putBoolean("CheckBoxLogin", false);
                    editor.commit();
                }
//                map = new HashMap();
//                map.put("userid", userName);
//                map.put("password", password);
//                map.put("type", "1");
//                map.put("bbh","1.0");
                yutuLoading.showDialog();
//                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
//                startActivity(intent);

                try {
                    netControl.requestForUsernameAndPassword(userName,password,"1","1.0", call);
                }catch (Exception e){
                    yutuLoading.dismissDialog();
                    Toast.makeText(LoginActivity.this,"请输入正确的ip地址",Toast.LENGTH_SHORT).show();
                }

                
        }

    }


    /**
     * 展示设置IP的对话框
     * @author tianfy
     */
    private void showCustomDialog() {
        AlertDialog.Builder builder=new AlertDialog.Builder(LoginActivity.this);
        builder.setMessage("设置IP");
        final XEditText xEtIP=new XEditText(LoginActivity.this);
        builder.setView(xEtIP);
        xEtIP.setText(PathManager.newIp);
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String newIP = xEtIP.getText().toString().trim();
                if (newIP!=null&&newIP.length()>0){
                    PathManager.setPathUrl(newIP);
                    SPUtils.setSP(LoginActivity.this,"newIP",newIP);
                }else{
                    Toast.makeText(LoginActivity.this, "IP为空，请重新输入", Toast.LENGTH_SHORT).show();
                }
            }
        });
        builder.setNegativeButton("取消", null);
        builder.create().show();
    }

    /**
     * 检测当的网络（WLAN、3G/2G）状态
     * @param context Context
     * @return true 表示网络可用
     */
    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivity = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo info = connectivity.getActiveNetworkInfo();
            if (info != null && info.isConnected())
            {
                // 当前网络是连接的
                if (info.getState() == NetworkInfo.State.CONNECTED)
                {
                    // 当前所连接的网络可用
                    return true;
                }
            }
        }
        return false;
    }


    public StringCallback call = new StringCallback() {
        @Override
        public void onError(Call call, Exception e, int id) {
            //yutuLoading.dismissDialog();
            // showFailed();
            Toast.makeText(LoginActivity.this, "请更新网络!", Toast.LENGTH_SHORT).show();
            yutuLoading.dismissDialog();

        }

        @Override
        public void onResponse(String response, int id) {
            bean = (LoginBean) JsonUtil.jsonToBean(response, LoginBean.class);
            Log.d("lvcheng", "response=====" + response + bean.getFlag());
            yutuLoading.dismissDialog();
            if (bean != null ) {
                if(bean.getFlag().equals("1")){
                    UserName = userName;

                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
                }else{
                    Toast.makeText(LoginActivity.this, "账号或密码不正确!", Toast.LENGTH_SHORT).show();
                }
            }
        }
    };
}
