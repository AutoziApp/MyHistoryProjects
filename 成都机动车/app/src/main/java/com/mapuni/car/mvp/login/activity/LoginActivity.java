package com.mapuni.car.mvp.login.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.mapuni.car.R;
import com.mapuni.car.app.ConsTants;
import com.mapuni.car.mvp.login.contract.LoginContract;
import com.mapuni.car.mvp.login.model.LoginCarTypeBean;
import com.mapuni.car.mvp.login.model.LoginModel;
import com.mapuni.car.mvp.login.presenter.LoginPresenter;
import com.mapuni.car.mvp.main.activity.MainActivity;
import com.mapuni.core.base.CoreBaseActivity;
import com.mapuni.core.utils.DialogManager;
import com.mapuni.core.utils.ToastUtils;
import com.mapuni.core.utils.circularanim.CircularAnim;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;

public class LoginActivity extends CoreBaseActivity<LoginPresenter, LoginModel> implements LoginContract.LoginActivity, View.OnClickListener {
    @BindView(R.id.nameEt)
    EditText nameEt;
    @BindView(R.id.keyEt)
    EditText keyEt;
    @BindView(R.id.rememberIcon)
    ImageView rememberIcon;
    @BindView(R.id.autoIcon)
    ImageView autoIcon;
    @BindView(R.id.rememberLayout)
    LinearLayout rememberLayout;
    @BindView(R.id.autoLayout)
    LinearLayout autoLayout;
    @BindView(R.id.change_btn2)
    TextView loginBtn;
    @BindView(R.id.ipSet)
    TextView ipSet;
    @BindView(R.id.progressBar2)
    ProgressBar progressBar;

    @Override
    public int getLayoutId() {
        return R.layout.activity_login;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        loginBtn.setOnClickListener(this);
        rememberLayout.setOnClickListener(this);
        autoLayout.setOnClickListener(this);
        ipSet.setOnClickListener(this);
    }

    @Override
    public void initLoginStation(Map map, boolean isRemember, boolean isAuto) {
        if (mPresenter.isRemember) {
            rememberIcon.setImageResource(R.mipmap.xuanzhong);
            if (map != null) {
                nameEt.setText(map.get("UserName") + "");
                keyEt.setText(map.get("Key") + "");
            }
            if (mPresenter.isAuto) {
                autoIcon.setImageResource(R.mipmap.xuanzhong);
            } else {
                autoIcon.setImageResource(R.mipmap.moren);
            }
        } else {
            rememberIcon.setImageResource(R.mipmap.moren);
        }
    }

    @Override
    public void startLogin() {
        CircularAnim.hide(loginBtn)
                .endRadius(progressBar.getHeight() / 2)
                .go(new CircularAnim.OnAnimationEndListener() {
                    @Override
                    public void onAnimationEnd() {
                        progressBar.setVisibility(View.VISIBLE);
                        TelephonyManager mTm = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
                        String imei = mTm.getDeviceId();
                     imei="863454039170654";
                        mPresenter.login(nameEt.getText().toString(), keyEt.getText().toString(), imei);
                    }
                });
    }

    @Override
    public void startActivity() {
//        progressBar.postDelayed(new Runnable() {
//                @Override
//                public void run() {
//                    CircularAnim.fullActivity(LoginActivity.this, progressBar)
//                            .colorOrImageRes(R.color.btnColor)
//                            .go(new CircularAnim.OnAnimationEndListener() {
//                                @Override
//                                public void onAnimationEnd() {
//                                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
//                                    finish();
//                                }
//                            });
//                }
//            }, 1000);
        mPresenter.requstCarType();
    }


    @Override
    public void jumpActivity() {
        HashMap<String, List<LoginCarTypeBean>> carMap = ConsTants.carMap;
        if (carMap != null && carMap.size() > 0) {
            progressBar.postDelayed(new Runnable() {
                @Override
                public void run() {
                    CircularAnim.fullActivity(LoginActivity.this, progressBar)
                            .colorOrImageRes(R.color.btnColor)
                            .go(new CircularAnim.OnAnimationEndListener() {
                                @Override
                                public void onAnimationEnd() {
                                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                                    finish();
                                }
                            });
                }
            }, 1000);
        } else {
            CircularAnim.show(loginBtn).triggerView(progressBar).go();
            ToastUtils.showToast(LoginActivity.this, "网络状态不佳");
        }


    }

    public void stopLogin() {
        CircularAnim.show(loginBtn).triggerView(progressBar).go();
    }

    public boolean checkInput(EditText editText) {
        Animation shakeAnimation = AnimationUtils.loadAnimation(this,
                R.anim.shake);
        if (editText.getText() == null || editText.getText().length() < 1) {
            editText.startAnimation(shakeAnimation);
            if (editText.getHint() != null) {
                Toast.makeText(this,
                        "请输入" + editText.getHint().toString(), Toast.LENGTH_SHORT).show();
            }
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.change_btn2:
//             startActivity(new Intent(this, MainActivity.class));
//               lgc.login(nameEt,keyEt);
                if (checkInput(nameEt) && checkInput(keyEt)) {
                    startLogin();
                }
                break;
            case R.id.rememberLayout:
                if (mPresenter.changeRememberType()) {
                    rememberIcon.setImageResource(R.mipmap.xuanzhong);
                } else {
                    rememberIcon.setImageResource(R.mipmap.moren);
                    if (mPresenter.isAuto) {
                        autoIcon.setImageResource(R.mipmap.moren);
                        mPresenter.changeAutoType();
                    }
                }
                break;
            case R.id.autoLayout:
                boolean auto = mPresenter.changeAutoType();
                if (auto) {
                    autoIcon.setImageResource(R.mipmap.xuanzhong);
                    if (!mPresenter.isRemember) {
                        rememberIcon.setImageResource(R.mipmap.xuanzhong);
                        mPresenter.changeRememberType();
                    }
                } else {
                    autoIcon.setImageResource(R.mipmap.moren);
                }
                break;
            case R.id.ipSet:
                DialogManager.showDialog(R.layout.set_adress_dialog, "设置后台地址", ConsTants.Base_Url,
                        R.mipmap.set_adress, this, new DialogClick());
                break;
        }
    }

    private class DialogClick implements DialogManager.DialogListaner {
        @Override
        public void onClick(String s) {
            if (s.equals("-1") || s.equals("")) {
                ToastUtils.showToast(mContext, "IP未重新设置");
            } else {
                ConsTants.setIp(LoginActivity.this, s);
            }
        }
    }

    public void startMainActivity() {
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }


    @Override
    public Context getContext() {
        return this;
    }

    @Override
    public void showError(String msg) {
        CircularAnim.show(loginBtn).triggerView(progressBar).go();
        if (msg.contains("Failed")) {
            ToastUtils.showToast(this, "连接失败");
            return;
        }
        ToastUtils.showToast(this, msg);

    }
}
