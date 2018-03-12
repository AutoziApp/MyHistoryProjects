package com.mapuni.administrator.activity.wdAc;

import android.widget.TextView;

import com.mapuni.administrator.R;
import com.mapuni.administrator.activity.BaseActivity;
import com.mapuni.administrator.utils.AppUtils;

import butterknife.BindView;

public class AboutActivity extends BaseActivity {


    @BindView(R.id.tv_version)
    TextView mTvVersion;

    @Override
    public int setLayoutResID() {
        return R.layout.activity_about;
    }

    @Override
    public void initView() {
        setToolbarTitle("关于");
        mTvVersion.setText("版本号："+ AppUtils.getVersionName(this));
    }

    @Override
    public void initData() {

    }


}
