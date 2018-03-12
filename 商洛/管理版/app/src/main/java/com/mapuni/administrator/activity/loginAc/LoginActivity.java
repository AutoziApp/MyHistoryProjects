package com.mapuni.administrator.activity.loginAc;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.bigkoo.svprogresshud.SVProgressHUD;
import com.mapuni.administrator.R;
import com.mapuni.administrator.activity.MainActivity;
import com.mapuni.administrator.bean.LoginResultBean;
import com.mapuni.administrator.iview.ILoginView;
import com.mapuni.administrator.manager.PathManager;
import com.mapuni.administrator.presenter.LoginPresenter;
import com.mapuni.administrator.utils.SPUtils;
import com.xw.repo.XEditText;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends AppCompatActivity implements ILoginView, CompoundButton.OnCheckedChangeListener {
    AMapLocationClient aMapLocationClient;
    AMapLocationClientOption aMapLocationClientOption;
    @BindView(R.id.btn_login)
    TextView mGotomain;
    @BindView(R.id.edt_username)
    XEditText mEdtUsername;
    @BindView(R.id.edt_password)
    XEditText mEdtPassword;
    @BindView(R.id.rememberIcon)
    CheckBox mRememberIcon;
    @BindView(R.id.autoIcon)
    CheckBox mAutoIcon;
    @BindView(R.id.tv_IP)
    TextView tvIP;
    private SVProgressHUD mSvProgressHUD;
    private LoginPresenter mLoginPresenter;
    private String mUserName;
    private String mPassWord;
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        this.mContext = this;
        mSvProgressHUD = new SVProgressHUD(this);

        mLoginPresenter = new LoginPresenter(this);
        initView();
    }

    private void initView() {
        initGDLocation();
        mRememberIcon.setOnCheckedChangeListener(this);
        mAutoIcon.setOnCheckedChangeListener(this);
        boolean isRemember = (boolean) SPUtils.getSp(mContext, "isRemember", false);
        boolean isAutoLogin = (boolean) SPUtils.getSp(mContext, "isAutoLogin", false);
        mUserName = (String) SPUtils.getSp(mContext, "userName", "");
        mPassWord = (String) SPUtils.getSp(mContext, "passWord", "");
        mEdtUsername.setText(mUserName);
        mEdtPassword.setText(mPassWord);
        if (isRemember) {
            mRememberIcon.setChecked(true);
            if (isAutoLogin) {
                mAutoIcon.setChecked(true);
//                mLoginPresenter.login();
                showProgressDialog();
                aMapLocationClient.startLocation();
                return;
            } else {
                mAutoIcon.setChecked(false);
            }
        } else {
            mAutoIcon.setChecked(false);
            mRememberIcon.setChecked(false);
        }
    }

    private void initGDLocation() {
        aMapLocationClient = new AMapLocationClient(this);
        aMapLocationClientOption = new AMapLocationClientOption();
        //设置定位模式为高精度定位
        aMapLocationClientOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
//        aMapLocationClientOption.setGpsFirst(true);//设置gps优先，只在高精度模式下有用
        aMapLocationClientOption.setHttpTimeOut(30000);
        aMapLocationClientOption.setNeedAddress(true);
        aMapLocationClientOption.setOnceLocation(true);
        aMapLocationClientOption.setOnceLocationLatest(true);
        aMapLocationClientOption.setLocationProtocol(AMapLocationClientOption.AMapLocationProtocol.HTTP);
        aMapLocationClient.setLocationOption(aMapLocationClientOption);
        aMapLocationClient.setLocationListener(new AMapLocationListener() {
            @Override
            public void onLocationChanged(AMapLocation aMapLocation) {
                if (aMapLocation != null) {
//                    Toast.makeText(LoginActivity.this,aMapLocation.getLatitude()+""+
//                            aMapLocation.getLongitude()+aMapLocation.getAddress(),Toast.LENGTH_SHORT).show();
                    mLoginPresenter.login(aMapLocation.getLongitude() + "", aMapLocation.getLatitude() + "");
                } else {
                    Log.i("aaa", "errCode:" + aMapLocation.getErrorCode()
                            + ",errInfo:" + aMapLocation.getErrorInfo());
                }
                aMapLocationClient.stopLocation();
            }
        });


    }

    @Override
    protected void onStart() {
        super.onStart();
    }
    
    @OnClick({R.id.btn_login, R.id.tv_IP})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_login:
                showProgressDialog();
                aMapLocationClient.startLocation();
                break;
            case R.id.tv_IP:
                //展示设置IP的对话框
                showCustomDialog();
                break;
        }
    }
    /**
     * 展示设置IP的对话框
     * @author tianfy
     */
    private void showCustomDialog() {
        AlertDialog.Builder builder=new AlertDialog.Builder(mContext);
        builder.setMessage("设置IP");
        final XEditText xEtIP=new XEditText(mContext);
        builder.setView(xEtIP);
        xEtIP.setTextColor(Color.BLACK);
        xEtIP.setText(PathManager.BaseUrl);
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String newIP = xEtIP.getText().toString().trim();
                if (newIP!=null&&newIP.length()>0){
                    PathManager.setBaseUrl(newIP);
                    SPUtils.setSP(mContext,"newIP",newIP);
                }else{
                    Toast.makeText(mContext, "IP为空，请重新输入", Toast.LENGTH_SHORT).show();
                }
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                
            }
        });
        builder.create().show();
    }

    @Override
    public String getUserName() {
        mUserName = mEdtUsername.getText().toString().trim();
        return mUserName;
    }

    @Override
    public String getPassWord() {
        mPassWord = mEdtPassword.getText().toString().trim();
        return mPassWord;
    }

    @Override
    public void showProgressDialog() {
        mSvProgressHUD.showWithStatus("正在登录...", SVProgressHUD.SVProgressHUDMaskType.Black);
    }

    @Override
    public void hideProgressDialog() {
        if (mSvProgressHUD.isShowing()) {
            mSvProgressHUD.dismissImmediately();
        }
    }


    @Override
    public void onSuccess(LoginResultBean bean) {
        mSvProgressHUD.showSuccessWithStatus("登录成功", SVProgressHUD.SVProgressHUDMaskType.Black);
        saveData(bean);
        gotoMainActivity();

    }

    @Override
    public void onError(String errMsg) {
        mSvProgressHUD.showErrorWithStatus("登录失败", SVProgressHUD.SVProgressHUDMaskType.Black);
        Toast.makeText(this, errMsg, Toast.LENGTH_SHORT).show();
        mSvProgressHUD.dismiss();
        clearLoginMsg();
    }


    /**
     * 保存登录信息
     *
     * @param bean
     */
    private void saveData(LoginResultBean bean) {
        boolean isRemember = (boolean) SPUtils.getSp(mContext, "isRemember", false);
        SPUtils.setSP(mContext, "userName", mUserName);
        if (isRemember) {
//            SPUtils.setSP(mContext, "userName", mUserName);
            SPUtils.setSP(mContext, "passWord", mPassWord);
        } else {
//            SPUtils.setSP(mContext, "userName", "");
            SPUtils.setSP(mContext, "passWord", "");
        }
        SPUtils.setSP(LoginActivity.this, "sessionId", bean.getSessionId());
        SPUtils.setSP(LoginActivity.this, "roleId", bean.getRoleId());
        SPUtils.setSP(LoginActivity.this,"gridLevel",bean.getGridLevel());
    }

    /**
     * 跳转到主页面
     */
    private void gotoMainActivity() {
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.activity_enter_anim, R.anim.activity_exit_anim);
        finish();
    }


    /**
     * 重置登录信息
     */
    private void clearLoginMsg() {
        SPUtils.setSP(mContext, "isAutoLogin", false);
        SPUtils.setSP(mContext, "isRemember", false);
        SPUtils.setSP(mContext, "userName", "");
        SPUtils.setSP(mContext, "passWord", "");
    }

    @Override
    protected void onPause() {
        super.onPause();
        mSvProgressHUD.dismiss();
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()) {
            case R.id.rememberIcon:
                if (isChecked) {
                    SPUtils.setSP(mContext, "isRemember", true);
                } else {
                    SPUtils.setSP(mContext, "isRemember", false);
                }
                break;
            case R.id.autoIcon:
                if (isChecked) {
                    SPUtils.setSP(mContext, "isAutoLogin", true);
                } else {
                    SPUtils.setSP(mContext, "isAutoLogin", false);
                }
                break;
            default:
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        aMapLocationClient.onDestroy();
    }


}
