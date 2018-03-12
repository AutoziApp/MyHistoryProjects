package com.mapuni.administrator.activity.wdAc;

import android.util.Log;
import android.widget.TextView;

import com.google.gson.Gson;
import com.mapuni.administrator.R;
import com.mapuni.administrator.activity.BaseActivity;
import com.mapuni.administrator.bean.ContactDetailBean;
import com.mapuni.administrator.manager.NetManager;
import com.mapuni.administrator.utils.SPUtils;
import com.mapuni.administrator.utils.TxtUtil;
import com.zhy.http.okhttp.callback.StringCallback;

import okhttp3.Call;

public class ContactDetailActivity extends BaseActivity {

    private TextView tvName,tvPhone,tvEmail,tvAddress;

    @Override
    public int setLayoutResID() {
        return R.layout.activity_contact_detail;
    }

    @Override
    public void initView() {
        setToolbarTitle("人员详情");
        tvName= (TextView) findViewById(R.id.tv_name);
        tvPhone= (TextView) findViewById(R.id.tv_phone);
        tvEmail= (TextView) findViewById(R.id.tv_email);
        tvAddress= (TextView) findViewById(R.id.tv_address);
    }

    @Override
    public void initData() {
        String sessionId = (String) SPUtils.getSp(this, "sessionId", "");
        NetManager.queryContactDetail(sessionId, getIntent().getStringExtra("id"), new StringCallback() {

            private String mAddress;
            private String mEmail;
            private String mTelephone;
            private String mUserRealname;

            @Override
            public void onError(Call call, Exception e, int id) {
                Log.i("aaa","详情"+e.getMessage());
            }

            @Override
            public void onResponse(String response, int id) {
                Log.i("aaa","详情"+response);
                ContactDetailBean bean=new Gson().fromJson(response,ContactDetailBean.class);
                if(bean!=null){
                    mUserRealname = bean.getUserRealname();
                    mTelephone = bean.getTelephone();
                    mEmail = bean.getEmail();
                    mAddress = bean.getAddress();
                    tvName.setText(TxtUtil.isEmpty(mUserRealname));
                    tvPhone.setText(TxtUtil.isEmpty(mTelephone));
                    tvEmail.setText(TxtUtil.isEmpty(mEmail));
                    tvAddress.setText(TxtUtil.isEmpty(mAddress));
                }
            }
        });
    }
}
