package com.mapuni.car.mvp.main.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.KeyEvent;

import com.mapuni.car.R;
import com.mapuni.car.mvp.login.activity.LoginActivity;
import com.mapuni.car.mvp.main.fragment.MainFragment;
import com.mapuni.core.base.CoreBaseActivity1;
import com.mapuni.core.utils.SpUtil;
import com.mapuni.core.widget.statusbar.StatusBarUtil;
import com.tencent.bugly.crashreport.CrashReport;

import java.util.Date;

public class MainActivity extends CoreBaseActivity1 {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//        CrashReport.initCrashReport(this, "b8549fe655", false);
//        StatusBarUtil.setTranslucentForImageViewInFragment(this, null);
//        if (savedInstanceState == null) {
//            loadRootFragment(R.id.fl_container, new MainFragment());
//        }

    }
    long lastPressTime = 0;
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            if (new Date().getTime() - lastPressTime < 1000) {
                finish();//结束程序
            } else {
                lastPressTime = new Date().getTime();//重置lastPressTime
                showToast("再按一次返回键退出");
            }
            return false;
        }else {
            return super.onKeyDown(keyCode, event);
        }
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }
    Intent intent;

    @Override
    public void initView(Bundle savedInstanceState) {
        CrashReport.initCrashReport(this, "b8549fe655", false);
        StatusBarUtil.setTranslucentForImageViewInFragment(this, null);
        if (savedInstanceState == null) {
            loadRootFragment(R.id.fl_container, new MainFragment());
        }
        swipeBackLayout.setOnSwipeBackListener((float fractionAnchor, float fractionScreen)->{
            ivShadow.setAlpha(1 - fractionScreen);
            if(intent==null){
                intent= new Intent(MainActivity.this, LoginActivity.class);
                SpUtil.putBoolean(MainActivity.this, SpUtil.AutoType, false);
                startActivity(intent);
            }
        });
    }

}
