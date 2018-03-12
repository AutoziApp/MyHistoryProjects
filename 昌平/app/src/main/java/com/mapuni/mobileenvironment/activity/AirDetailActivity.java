package com.mapuni.mobileenvironment.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mapuni.mobileenvironment.R;
import com.mapuni.mobileenvironment.adapter.AirDataAdapter;
import com.mapuni.mobileenvironment.model.AirSingle;
import com.mapuni.mobileenvironment.utils.JsonUtil;
import com.mapuni.mobileenvironment.utils.SignUtil;
import com.mapuni.mobileenvironment.view.MyDecoration;
import com.mapuni.mobileenvironment.view.YutuLoading;
import com.mapuni.mobileenvironment.widget.NiceSpinner;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Request;

public class AirDetailActivity extends ActivityBase {
    private LinearLayout startTimeLayout, endTimeLayout;
    private TextView startTime, endTime;
    private NiceSpinner portSpinner;
    private TextView mSearch;
    private RecyclerView mRecycle;
    LinearLayoutManager layoutManager;
    List<AirSingle.DataBean> list;
    List<String> dates;
    List<String> values;
    List<String> standards;
    AirDataAdapter adapter;
    private boolean isStartTime = true;
    private List<String> portData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_air_detail);
        initView();
    }

    private void initView() {
        initCalendar();
        yutuLoading = new YutuLoading(this);
        startTimeLayout = (LinearLayout) findViewById(R.id.starttime_layout);
        endTimeLayout = (LinearLayout) findViewById(R.id.endtime_layout);
        startTime = (TextView) findViewById(R.id.start_time);
        endTime = (TextView) findViewById(R.id.end_time);
        portSpinner = (NiceSpinner) findViewById(R.id.portSpinner);
        mSearch = (TextView) findViewById(R.id.searchBtn);
        mSearch.setOnClickListener(this);
        mRecycle = (RecyclerView) findViewById(R.id.recycle);
        layoutManager = new LinearLayoutManager(this);
        mRecycle.setLayoutManager(layoutManager);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        list = new ArrayList<>();
        dates = new ArrayList<>();
        values = new ArrayList<>();
        standards = new ArrayList<>();
        mRecycle.addItemDecoration(new MyDecoration(this, OrientationHelper.VERTICAL), 0);
        adapter = new AirDataAdapter(AirDetailActivity.this, dates, values, standards);
        mRecycle.setAdapter(adapter);
        setSpinner();
        startTimeLayout.setOnClickListener(this);
        endTimeLayout.setOnClickListener(this);
        getAirDetailData();
    }

    //初始化下拉框数据
    private void setSpinner() {
        portData = new ArrayList<>();
        portData.add("aqi");
        portData.add("co");
        portData.add("no2");
        portData.add("o3");
        portData.add("pm10");
        portData.add("pm25");
        portData.add("so2");
        NiceSpinner.IconChange iChange = new NiceSpinner.IconChange() {
            @Override
            public void spinner() {
                refreshData();
            }

            @Override
            public void spinnerPress() {
            }
        };
        portSpinner.setIconChange(iChange);
        portSpinner.attachDataSource(portData);
    }

    private void getAirDetailData() {
        String url = "http://test.coilabs.com:8891/BJCPAir/yutu/singleHistory";
        Map<String, String> jo = new HashMap<>();
        String nonce = SignUtil.getNonce();
        String timestamp = new Date().getTime() + "";
        jo.put("timestamp", timestamp);
//        随机数
        jo.put("nonce", nonce);
//        用户加密签名，signature结合了开发者填写的token(一串字符串+加密后的密码)参数和请求中的timestamp参数、userName(登录账号)参数。
        jo.put("signature", SignUtil.getSignature2(timestamp, nonce));
        jo.put("st",startTime.getText().toString());
        jo.put("et",endTime.getText().toString());
        jo.put("deviceId",getIntent().getStringExtra("deviceId"));
//        jo.put("st", "2010-01-01");
//        jo.put("et", "2016-12-30");
//        jo.put("deviceId", "A1YQ5200042D0518");
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
                        handle.sendEmptyMessageDelayed(100, 2000);
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        yutuLoading.dismissDialog();
                        AirSingle airSite = (AirSingle) JsonUtil.jsonToBean(response, AirSingle.class);
                        list.clear();
                        list = airSite.getData();
                        refreshData();
                    }
                });
    }

    public void refreshData() {
        dates.clear();
        values.clear();
        standards.clear();
        switch (portSpinner.getText().toString()) {
            case "aqi":
                for (AirSingle.DataBean dataBean : list) {
                    dates.add(dataBean.getTime() + "");
                    values.add(dataBean.getAqi() + "");
                    standards.add("30");
                }
                break;
            case "co":
                for (AirSingle.DataBean dataBean : list) {
                    dates.add(dataBean.getTime() + "");
                    values.add(dataBean.getCo() + "");
                    standards.add("30");
                }
                break;
            case "no2":
                for (AirSingle.DataBean dataBean : list) {
                    dates.add(dataBean.getTime() + "");
                    values.add(dataBean.getNo2() + "");
                    standards.add("30");
                }
                break;
            case "o3":
                for (AirSingle.DataBean dataBean : list) {
                    dates.add(dataBean.getTime() + "");
                    values.add(dataBean.getO3() + "");
                    standards.add("30");
                }
                break;
            case "pm10":
                for (AirSingle.DataBean dataBean : list) {
                    dates.add(dataBean.getTime() + "");
                    values.add(dataBean.getPm10() + "");
                    standards.add("30");
                }
                break;
            case "pm25":
                for (AirSingle.DataBean dataBean : list) {
                    dates.add(dataBean.getTime() + "");
                    values.add(dataBean.getPm25() + "");
                    standards.add("30");
                }
                break;
            case "so2":
                for (AirSingle.DataBean dataBean : list) {
                    dates.add(dataBean.getTime() + "");
                    values.add(dataBean.getSo2() + "");
                    standards.add("30");
                }
                break;
        }
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.starttime_layout:
                isStartTime = true;
                showPopwindow(getDataPick(0));
                break;
            case R.id.endtime_layout:
                isStartTime = false;
                showPopwindow(getDataPick(0));
                break;
            case R.id.set:
                if (true == isStartTime) {//判断是点击的开始时间
                    if (timeView.findViewById(R.id.date).getVisibility() == View.VISIBLE) {
                        startTime.setText(getDate(v));
                        dismissPopwindow();//注销掉可以得到小时
                    } else {
                        startTime.append(" " + getTime());
                        dismissPopwindow();
                    }
                } else {//判断不是点击的开始时间
                    if (timeView.findViewById(R.id.date).getVisibility() == View.VISIBLE) {
                        endTime.setText(getDate(v));
                        dismissPopwindow();
                    } else {
                        endTime.append(" " + getTime());
                        dismissPopwindow();
                    }
                }
                break;
            case R.id.searchBtn:
                getAirDetailData();
                break;
        }
    }
}
