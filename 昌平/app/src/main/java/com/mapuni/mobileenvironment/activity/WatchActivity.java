package com.mapuni.mobileenvironment.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.mapuni.mobileenvironment.R;
import com.mapuni.mobileenvironment.adapter.WatchAdapter;
import com.mapuni.mobileenvironment.adapter.WatchAdapter_air;
import com.mapuni.mobileenvironment.config.PathManager;
import com.mapuni.mobileenvironment.model.AirSite;
import com.mapuni.mobileenvironment.model.EntModel;
import com.mapuni.mobileenvironment.utils.JsonUtil;
import com.mapuni.mobileenvironment.utils.SignUtil;
import com.mapuni.mobileenvironment.view.YutuLoading;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;
import com.zhy.http.okhttp.request.RequestCall;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Request;

public class WatchActivity extends ActivityBase {
    private ListView listView;
    private TextView noData;
    private List<?> list;
    private WatchAdapter adapter;
    private int BEFORE;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        BEFORE = (int) getIntent().getExtras().get("before");
        setContentView(R.layout.activity_water);
        initView();
        initLister();
    }
    private void initLister() {
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if(BEFORE == ISAIR){
                    Intent intent=new Intent(WatchActivity.this, AirDetailActivity.class);
                    AirSite.DataBean dataBean= (AirSite.DataBean) list.get(i);
                    intent.putExtra("deviceId",dataBean.getDeviceid());
                    startActivity(intent);
                }else if(BEFORE==ISWATER){

                }else if(BEFORE==ISPOLLUTION){

                }
            }
        });
    }
    private void initList(String s) {
        if(BEFORE == ISAIR){
//            getAirData();//添加空气质量监控的列表数据
        }else if(BEFORE==ISWATER){

        }else if(BEFORE==ISPOLLUTION){
            list = JsonUtil.fromJsonList(s,EntModel.class);
        }
        adapter = new WatchAdapter(this, list);
        listView.setAdapter(adapter);
    }

    private void initView() {
        listView = (ListView) findViewById(R.id.list);
        noData = (TextView) findViewById(R.id.nodata);
        yutuLoading = new YutuLoading(this);
        if(BEFORE == ISAIR){
            setTitle(getResources().getString(R.string.btn_airWatch));
            getAirData();//再此处添加网络访问更新页面
        }else if(BEFORE==ISWATER){
            setTitle(getResources().getString(R.string.btn_waterWatch));
        }else if(BEFORE==ISPOLLUTION){
            setTitle(getResources().getString(R.string.tite_pollution));
            search(OkHttpUtils.get().url(PathManager.GetEnterList).build());
        };
    }

    private void search(RequestCall request){
                 request.execute(new StringCallback() {
                    @Override
                    public void onBefore(Request request, int id) {
                        yutuLoading.showDialog();
                        super.onBefore(request, id);
                    }

                    @Override
                    public void onAfter(int id) {
                        super.onAfter(id);
                    }

                    @Override
                    public void onError(Call call, Exception e, int id) {
                        yutuLoading.showFailed();
                        handle.sendEmptyMessageDelayed(FIRSTBTN,2000);
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        yutuLoading.dismissDialog();
                        initList(response);
                        Log.i("Lybin",list.size()+"");
                    }
                });
    }

    private void getAirData(){
        String url = "http://test.coilabs.com:8891/BJCPAir/yutu/getsites";
        Map<String,String> jo=new HashMap<>();
        String nonce= SignUtil.getNonce();
        String timestamp=new Date().getTime()+"";
        jo.put("timestamp",timestamp);
        jo.put("nonce",nonce);
        jo.put("signature",SignUtil.getSignature2(timestamp,nonce));
        OkHttpUtils
                .post()//
                .url(url)//
                .params(jo)
                .build()//
                .execute(new StringCallback() {
                    @Override
                    public void onBefore(Request request, int id) {
                        yutuLoading.showDialog();
                        super.onBefore(request, id);
                    }

                    @Override
                    public void onAfter(int id) {
                        super.onAfter(id);
                    }
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        yutuLoading.showFailed();
                        handle.sendEmptyMessageDelayed(100,2000);
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        yutuLoading.dismissDialog();
                        AirSite airSite= (AirSite) JsonUtil.jsonToBean(response,AirSite.class);
                        list=airSite.getData();
                        WatchAdapter_air adapter = new WatchAdapter_air(WatchActivity.this, list);
                        listView.setAdapter(adapter);
                    }
                });
    }

}
