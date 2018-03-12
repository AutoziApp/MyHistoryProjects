package com.yutu.car.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.yutu.car.R;
import com.yutu.car.presenter.NetControl;
import com.yutu.car.utils.JsonUtil;
import com.yutu.car.views.YutuLoading;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.Map;

import okhttp3.Call;

public class CheckStationActivity extends BaseActivity {
    private NetControl control;
    private YutuLoading yutuLoading;
    private ImageView imageView;
    private String jgbh,nr,jg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_station2);
        imageView=(ImageView) findViewById(R.id.show_draw);
        jgbh=getIntent().getStringExtra("jgbh");
        nr=getIntent().getStringExtra("nr");
        jg=getIntent().getStringExtra("jg");
            yutuLoading=new YutuLoading(this);
            yutuLoading = new YutuLoading(this);
            yutuLoading.showDialog();
            control=new NetControl();
            control.requestForCheckoutStation(jgbh,nr,jg,call);
//        }else {
//            userId=getIntent().getStringExtra("userId");
//            pkid=getIntent().getStringExtra("pkid");
//            control.requestForCheckoutStation("1","",userId,pkid,call);
//        }

        initView();
    }
    private void initView(){
        setTitle("报告详情",true,false);
//        if(checkstation.equals("1")){
//            imageView.setImageResource(R.mipmap.shiliang);
//        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.leftIcon:
                finish();
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
            String result = map.get("result").toString().substring(0,1);
            yutuLoading.dismissDialog();
            if(result.equals("1")){
//                if(map.get("info").toString().equals("审核成功")){
//                    imageView.setImageResource(R.mipmap.shiliang);
//                }else {
//                    imageView.setImageResource(R.mipmap.shenheweitongguo);
//                }
                if(jg.equals("1")){
                    imageView.setImageResource(R.mipmap.shiliang);
                }else{
                    imageView.setImageResource(R.mipmap.shenheweitongguo);
                }
            }else{
                showFailed();
            }
        }
    } ;
}
