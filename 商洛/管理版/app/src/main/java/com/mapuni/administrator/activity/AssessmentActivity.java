package com.mapuni.administrator.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.mapuni.administrator.R;
/**
 *  @author Tianfy
 *  @time 2017/9/4  9:53
 *  @describe 考核评价Activity
 */
public class AssessmentActivity extends BaseActivity {

    @Override
    public int setLayoutResID() {
        return R.layout.activity_assessment;
    }

    @Override
    public void initView() {
        setToolbarTitle("考核评价");
    }

    @Override
    public void initData() {

    }
}
