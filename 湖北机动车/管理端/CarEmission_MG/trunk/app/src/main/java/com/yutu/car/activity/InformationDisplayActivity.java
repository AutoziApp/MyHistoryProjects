package com.yutu.car.activity;


import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.yutu.car.R;
import com.yutu.car.presenter.BaseControl;
import com.yutu.car.presenter.NetControl;
import com.yutu.car.utils.JsonUtil;
import com.yutu.car.views.YutuLoading;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.List;
import java.util.Map;

import okhttp3.Call;

public class InformationDisplayActivity extends BaseActivity {
    private YutuLoading yutuLoading;
    private BaseControl control;
    private NetControl netControl;
    List data;
    private TextView show_info;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_information_display);
        show_info=(TextView)findViewById(R.id.show_info) ;
        control=(BaseControl)getIntent().getExtras().get("class");
        setTitle(control.getTitle(),true,false);
        yutuLoading=new YutuLoading(this);
        yutuLoading.showDialog();
        control.requestData(call);


    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()){
            case R.id.leftIcon:
                finish();
                break;
            case  R.id.showFailed:
            yutuLoading.showDialog();
            delayedPost(new Runnable() {
                @Override
                public void run() {
                    control.requestData(call);
                }
            }, 2000);
            requestAgain();
            break;
        }
    }

    public StringCallback call = new StringCallback() {
        @Override
        public void onError(Call call, Exception e, int id) {
            yutuLoading.dismissDialog();
           showFailed();
//            String s = e.toString();
        }

        @Override
        public void onResponse(String response, int id) {
            Log.d("lvcheng","response>>>>>>>>>>"+response);
            Map map = JsonUtil.jsonToMap(response);
            String flag = map.get("flag").toString().substring(0,1);
            yutuLoading.dismissDialog();

            if(flag.equals("1")){
                show_info.setText("    "+map.get("content").toString());
            }
        }
    } ;
}
