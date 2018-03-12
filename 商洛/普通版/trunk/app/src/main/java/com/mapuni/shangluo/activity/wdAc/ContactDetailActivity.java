package com.mapuni.shangluo.activity.wdAc;

import android.util.Log;
import android.widget.TextView;

import com.google.gson.Gson;
import com.mapuni.shangluo.R;
import com.mapuni.shangluo.activity.BaseActivity;
import com.mapuni.shangluo.bean.ContactDetailBean;
import com.mapuni.shangluo.manager.NetManager;
import com.mapuni.shangluo.utils.SPUtils;
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
            @Override
            public void onError(Call call, Exception e, int id) {
                Log.i("aaa","详情"+e.getMessage());
            }

            @Override
            public void onResponse(String response, int id) {
                Log.i("aaa","详情"+response);
                ContactDetailBean bean=new Gson().fromJson(response,ContactDetailBean.class);
                if(bean!=null){
                    tvName.setText(bean.getUserRealname());
                    tvPhone.setText(bean.getTelephone());
                    tvEmail.setText(bean.getEmail());
                    tvAddress.setText(bean.getAddress());
                }
            }
        });
    }
}
