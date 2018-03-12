package com.mapuni.administrator.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Process;
import android.support.design.widget.AppBarLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.bigkoo.svprogresshud.SVProgressHUD;
import com.google.gson.Gson;
import com.mapuni.administrator.R;
import com.mapuni.administrator.utils.EventManager;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2017/8/10.
 */

public abstract class BaseActivity extends AppCompatActivity {
    public Context mContext;
    private List<Activity> mActivities = new CopyOnWriteArrayList<Activity>();
    @BindView(R.id.app_bar)
    public AppBarLayout mAppBarLayout;
    @BindView(R.id.tb_toolbar)
    public Toolbar mToolbar;
    @BindView(R.id.toolbar_title)
    public TextView toolbarTitle;
    public ActionBar mSupportActionBar;
    public SVProgressHUD mSvProgressHUD;
    public Gson gson;
    public String TAG="";
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //禁止所有Activity横屏
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        this.mContext = this;
        TAG=this.getClass().getSimpleName();
        mActivities.add(this);//管理Activity
        setContentView(setLayoutResID());
        ButterKnife.bind(this);//注解butterknife
        mSvProgressHUD = new SVProgressHUD(this);//全局Loading
        gson = new Gson();
        EventManager.registEventBus(this);//EventBus
        initToolbar();//初始化Toolbar
        initView();
        initData();

    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    /**
     * 初始化toolbar
     */
    public void initToolbar() {
        setSupportActionBar(mToolbar);
        mSupportActionBar = getSupportActionBar();
        setToolbarBack(mSupportActionBar, true);
        mSupportActionBar.setDisplayShowTitleEnabled(false);
        mToolbar.setNavigationIcon(R.drawable.ic_back);
        
    }

    public abstract int setLayoutResID();

    public abstract void initView();

    public abstract void initData();


    public void setToolbarisVisibility(boolean isVisibility) {
        if (isVisibility) {
            mAppBarLayout.setVisibility(View.VISIBLE);
        } else {
            mAppBarLayout.setVisibility(View.GONE);
        }
    }

    /**
     * 设置toolbar的标题
     *
     * @param title
     */
    public void setToolbarTitle(String title) {
        toolbarTitle.setText(title);
    }

    /**
     * 设置toolbar返回键是否显示
     *
     * @param bar
     * @param isVisibility
     */
    public void setToolbarBack(ActionBar bar, boolean isVisibility) {
        bar.setDisplayHomeAsUpEnabled(isVisibility);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            default:
                break;
        }

        return super.onOptionsItemSelected(item);
    }


    /**
     * 开启一个activity
     *
     * @param context
     * @param clazz
     */
    public void baseStartActivity(Context context, Class<? extends BaseActivity> clazz) {
        Intent mIntent = new Intent(context, clazz);
        startActivity(mIntent);
        overridePendingTransition(R.anim.activity_enter_anim, R.anim.activity_exit_anim);
    }

    /**
     * 开启一个activity并关闭自身
     *
     * @param context
     * @param clazz
     */
    public void baseStartActivityAndFinish(Context context, Class<? extends BaseActivity> clazz) {
        baseStartActivity(context, clazz);
//        overridePendingTransition(R.anim.current_activity_exit_anim, R.anim.next_activity_enter_anim);
        finish();
    }


    /**
     * 退出应用的方法
     */
    public void exitApp() {
        for (Activity activity : mActivities) {
            if (!activity.isFinishing()) {
                activity.finish();
            }
            mActivities.remove(activity);
        }
        android.os.Process.killProcess(Process.myPid());
    }

    // spinner初始化简单封装
    public void initSpinner(Spinner spinner, List<String> data) {

        // 建立Adapter并且绑定数据源
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.simple_spinner_item, data);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        ((Activity)mContext).finish();
//        overridePendingTransition(R.anim.current_activity_exit_anim, R.anim.next_activity_enter_anim);
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventManager.unRegistEventBus(this);
    }
}
