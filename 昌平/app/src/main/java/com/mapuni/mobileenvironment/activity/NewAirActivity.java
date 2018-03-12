package com.mapuni.mobileenvironment.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.mapuni.mobileenvironment.R;
import com.mapuni.mobileenvironment.adapter.AirCurAdapter;
import com.mapuni.mobileenvironment.adapter.AirDayAdapter;
import com.mapuni.mobileenvironment.app.DataApplication;
import com.mapuni.mobileenvironment.bean.StreetBean;
import com.mapuni.mobileenvironment.config.PathManager;
import com.mapuni.mobileenvironment.model.AirCurData;
import com.mapuni.mobileenvironment.model.AirDayData;
import com.mapuni.mobileenvironment.net.WebServiceProvider;
import com.mapuni.mobileenvironment.utils.DateUtils;
import com.mapuni.mobileenvironment.utils.JsonUtil;
import com.mapuni.mobileenvironment.utils.SignUtil;
import com.mapuni.mobileenvironment.view.YutuLoading;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.qqtheme.framework.entity.City;
import cn.qqtheme.framework.entity.County;
import cn.qqtheme.framework.entity.Province;
import cn.qqtheme.framework.picker.AddressPicker;
import cn.qqtheme.framework.util.ConvertUtils;
import cn.qqtheme.framework.util.LogUtils;
import okhttp3.Call;
import okhttp3.Request;

import static com.mapuni.mobileenvironment.app.DataApplication.showToast;

public class NewAirActivity extends ActivityBase implements RadioGroup.OnCheckedChangeListener,SwipeRefreshLayout.OnRefreshListener {

    private RadioGroup radioGroup1, radioGroup2;
    private RadioButton hourData, dayData, aqi, pm25, o3, pm10, co, so2, no2;
    private TextView time, selected_time;
    private TextView curSelectTxt,daySelectTxt;
    private LinearLayout timeLayout;
    private LinearLayout ll1, ll2;
    private LinearLayout curStreet,dayStreet;
    private ListView listView, listView2;
    private List<AirCurData.DataBean> curlist;//实时数据
    private List<Double> curValueList;//实时数据value
    private AirCurAdapter airCurAdapter;//实时数据adapter
    private Map curValueMap;//实时数据集合
    private String curTag;
    private List<AirDayData.DataBean> daylist;//日数据
    private Map dayValueMap;//日数据集合
    private List<Double> dayValueList;//实时数据value
    private String dayTag;
    private Map curSortMap;
    private Map daySortMap;
    private AirDayAdapter airDayAdapter;//实时数据adapter
    private boolean isCur = true;//判断是否是实时数据的标志位
    private SwipeRefreshLayout mRefresh;
    private Map curSessionMap;//当前街道标记

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_new_air);
        initView();

    }

    //初始化控件
    private void initView() {
        initCalendar();
        radioGroup1 = (RadioGroup) findViewById(R.id.rg1);
        radioGroup2 = (RadioGroup) findViewById(R.id.rg2);
        hourData = (RadioButton) findViewById(R.id.hourDataBtn);
        dayData = (RadioButton) findViewById(R.id.dayDataBtn);
        aqi = (RadioButton) findViewById(R.id.rb_aqi);
        pm25 = (RadioButton) findViewById(R.id.rb_pm25);
        o3 = (RadioButton) findViewById(R.id.rb_o3);
        pm10 = (RadioButton) findViewById(R.id.rb_pm10);
        co = (RadioButton) findViewById(R.id.rb_co);
        so2 = (RadioButton) findViewById(R.id.rb_so2);
        no2 = (RadioButton) findViewById(R.id.rb_no2);
        time = (TextView) findViewById(R.id.time);
        curSelectTxt = (TextView) findViewById(R.id.curSelectedTxt);
        daySelectTxt = (TextView) findViewById(R.id.daySelectedTxt);
        selected_time = (TextView) findViewById(R.id.selected_time);
        timeLayout = (LinearLayout) findViewById(R.id.time_layout);
        listView = (ListView) findViewById(R.id.lv);
        listView2 = (ListView) findViewById(R.id.lv2);
        curStreet = (LinearLayout) findViewById(R.id.curStreetLayout);
        dayStreet = (LinearLayout) findViewById(R.id.streetLayout);
        mRefresh = (SwipeRefreshLayout) findViewById(R.id.refresh);
        ll1 = (LinearLayout) findViewById(R.id.ll1);
        ll2 = (LinearLayout) findViewById(R.id.ll2);
        curlist = new ArrayList<>();
        curValueList = new ArrayList<>();
        daylist = new ArrayList<>();
        dayValueMap = new HashMap();
        curValueMap = new HashMap();
        daySortMap = new HashMap();
        curSortMap = new HashMap();
        curSessionMap = new HashMap<>();
        curSessionMap.put("cityId","000000");
        curSessionMap.put("deviceId","0000000");
        dayValueList = new ArrayList<>();
        mRefresh.setOnRefreshListener(this);
        radioGroup1.setOnCheckedChangeListener(this);
        radioGroup2.setOnCheckedChangeListener(this);
        timeLayout.setOnClickListener(this);
        curStreet.setOnClickListener(this);
        dayStreet.setOnClickListener(this);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(NewAirActivity.this, newAirCurActivity.class);
                intent.putExtra("Deviceid", curlist.get(i).getDeviceid());
                intent.putExtra("DeviceName", curlist.get(i).getDeviceName());
                startActivity(intent);
            }
        });
        listView2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(NewAirActivity.this, NewAirDayActivity.class);
                intent.putExtra("Deviceid",daylist.get(i).getDeviceid());
                intent.putExtra("DeviceName", daylist.get(i).getDeviceName());
                startActivity(intent);
            }
        });
        initTime();
        curTag = "pm25";
        dayTag = "pm25";
        airCurAdapter = new AirCurAdapter(curlist, curValueList, NewAirActivity.this, curTag);
        listView.setAdapter(airCurAdapter);
        airDayAdapter = new AirDayAdapter(daylist, dayValueList, NewAirActivity.this, dayTag);
        listView2.setAdapter(airDayAdapter);
        hourData.setChecked(true);
        pm25.setChecked(true);
        getAirDayData();
        getAirCurData();
        requestStreetPM();
//        getStreets();
    }

    //初始化实时时间
    public void initTime() {
        initCalendar();
        yutuLoading = new YutuLoading(this);
        String date = DateUtils.getYesterdayDate();
        date = date.substring(0,10);
        selected_time.setText(date);
        time.setText(DateUtils.getSystemNowTime() + " 更新");
//        time.setText(DateUtils.getSystemNowTime() + " 更新");
//        date = date.substring(0, 14);
//        time.setText(date + "00:00更新");
    }



    //网络获取实时数据
    private void getAirCurData() {
        String url = PathManager.GetSiteDataByTime;
        Map<String, String> jo = new HashMap<>();
        String nonce = SignUtil.getNonce();
        String timestamp = new Date().getTime() + "";
        jo.put("timestamp", timestamp);
        jo.put("nonce", nonce);
        jo.put("signature", SignUtil.getSignature2(timestamp, nonce));
//        jo.put("time", time.getText().toString().replace("-", "/"));
        jo.put("time", DateUtils.getNowDate());
        jo.put("type", "1");
        OkHttpUtils
                .post()//
                .url(url)//
                .params(jo)
                .build()//
                .execute(new StringCallback() {
                    @Override
                    public void onBefore(Request request, int id) {
//                        yutuLoading.showDialog();
                        super.onBefore(request, id);
                    }

                    @Override
                    public void onAfter(int id) {
                        super.onAfter(id);
                    }

                    @Override
                    public void onError(Call call, Exception e, int id) {
                        if(mRefresh!=null&&mRefresh.isRefreshing()){
                            mRefresh.setRefreshing(false);
                        }
                        yutuLoading.showFailed();
                        handle.sendEmptyMessageDelayed(SEARCHBTN, 2000);
                    }

                    @Override
                    public void onResponse(String response, int id) {
//                        yutuLoading.dismissDialog();
                       AirCurData airCurData = (AirCurData) JsonUtil.jsonToBean(response, AirCurData.class);
                        if(airCurData==null||airCurData.getData()==null)
                            return;
                        if (0 == airCurData.getRet()) {
                            reverseStation(airCurData.getData(),true);
//                            List _DayList = reverseStation(airCurData.getData());
//                            curlist.clear();
//                            curlist.addAll(_DayList);
////                            Collections.reverse(curlist);
//                            curValueList.clear();
                            curValueMap.clear();
                            for (AirCurData.DataBean dataBean : airCurData.getData()) {
//                                curValueList.add(dataBean.getPm25());
                                curValueMap.put(dataBean.getDeviceid(),dataBean);
                            }

                        String streetId = DataApplication.streetBean.getData().get(0).getStreetId()+"";
                        String name = DataApplication.streetBean.getData().get(0).getStreetName();
                        curSelectTxt.setText("所有站点");
                        if(curValueMap.size()>0){
                            String cityId = (String) curSessionMap.get("cityId");
                            String deviceId = (String) curSessionMap.get("deviceId");
                            StreetBean.DataBean Bean= (StreetBean.DataBean) DataApplication.streetBean.getStreetMap().get(cityId);
                            if(Bean!=null){
                                curSelectTxt.setText(Bean.getStreetName());
                            }
                            updateCurData(getCurStationByStreet(cityId,deviceId));
                            time.setText(DateUtils.getSystemNowTime() + " 更新");
                        }

//                            curTag = "pm25";
//                            airCurAdapter.notifyDataSetChanged();
//                            onCheckedChanged(null,airCurAdapter.getCurrentId());
//                            if(mRefresh!=null&&mRefresh.isRefreshing()){
//                                mRefresh.setRefreshing(false);
//                            }
                        }
                    }
                });
    }

    //网络获取日数据
    private void getAirDayData() {
        String url = PathManager.GetSiteDataByTime;
        Map<String, String> jo = new HashMap<>();
        String nonce = SignUtil.getNonce();
        String timestamp = new Date().getTime() + "";
        jo.put("timestamp", timestamp);
        jo.put("nonce", nonce);
        jo.put("signature", SignUtil.getSignature2(timestamp, nonce));
        jo.put("time", selected_time.getText().toString().replace("-", "/")+" 00:00:00");
        jo.put("type", "0");
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
                        handle.sendEmptyMessageDelayed(SEARCHBTN, 2000);
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        yutuLoading.dismissDialog();
                        AirDayData airDayData = (AirDayData) JsonUtil.jsonToBean(response, AirDayData.class);
                        if (0 == airDayData.getRet()) {
                            reverseStation(airDayData.getData(),true);
//                            List _DayList = reverseStation(airDayData);
//                            if(daylist!=null&&daylist.size()>0)
//                                daylist.clear();
//                            daylist.addAll(_DayList);
////                          Collections.reverse(daylist);
//                            dayValueList.clear();
                            dayValueMap.clear();
                            for (AirDayData.DataBean dataBean : airDayData.getData()) {
//                                dayValueList.add(dataBean.getPm25());
                                dayValueMap.put(dataBean.getDeviceid(),dataBean);
                            }
                            String streetId = DataApplication.streetBean.getData().get(0).getStreetId()+"";
                            String name = DataApplication.streetBean.getData().get(0).getStreetName();
                            daySelectTxt.setText("所有站点");
                            if(dayValueMap.size()>0)
                                updateDayData(getDayStationByStreet("000000","0000000"));
//                            dayTag = "pm25";
//                            airDayAdapter.notifyDataSetChanged();
                        }
                    }
                });
    }

    private void updateDayData(List data){
        List _DayList = reverseStation(data,false);

        if(daylist!=null&&daylist.size()>0)
            daylist.clear();
        daylist.addAll(_DayList);
        dayValueList.clear();
        for (AirDayData.DataBean dataBean : daylist) {
            dayValueList.add(dataBean.getPm25());
        }
        dayTag = "pm25";
        airDayAdapter.notifyDataSetChanged();
    }
    private void updateCurData(List data){
        List _DayList = reverseStation(data,false);
        curlist.clear();
        curlist.addAll(_DayList);
//                           Collections.reverse(curlist);
        curValueList.clear();
        for (AirCurData.DataBean dataBean : curlist) {
            curValueList.add(dataBean.getPm25());
        }
        curTag = "pm25";
        airCurAdapter.notifyDataSetChanged();
        onCheckedChanged(null,airCurAdapter.getCurrentId());
//        time.setText(DateUtils.getSystemNowTime() + " 更新");
        if(mRefresh!=null&&mRefresh.isRefreshing()){
            mRefresh.setRefreshing(false);
        }
    }

//    AirDayData.DataBean dataBean = list.get(i);
////        区
//    if (dataBean.getDeviceid().substring(0, 1).equals("T")) {
//        holder.imageView.setImageResource(imgs[0]);//设置类型图标
//    }
////        专
//    else if(dataBean.getDeviceid().substring(0, 1).equals("A")) {
//        holder.imageView.setImageResource(imgs[2]);//设置类型图标
//    }
////        市
//    else {
//        holder.imageView.setImageResource(imgs[1]);//设置类型图标
//    }
    //市区专分类
    public List reverseStation(List data,boolean isReverMap){
        List _Data  = data;
//        if(data.get(0) instanceof AirDayData.DataBean){
//             _Data = ((AirDayData) data).getData();
//        }else if(data.get(0) instanceof AirCurData.DataBean){
//            _Data = data;
//        }

        List city = new ArrayList();
        List area = new ArrayList();
        List special = new ArrayList();
        for(int i=0;i<_Data.size();i++){
            Object obj =null;
            String deviceid = "";
            if(data.get(i) instanceof AirDayData.DataBean){
                obj = (AirDayData.DataBean) _Data.get(i);
                deviceid = ((AirDayData.DataBean)_Data.get(i)).getDeviceid().substring(0, 1);
            }else if(data.get(i) instanceof AirCurData.DataBean){
                obj = (AirCurData.DataBean) _Data.get(i);
                deviceid = ((AirCurData.DataBean)_Data.get(i)).getDeviceid().substring(0, 1);
            }
        //        区
            if (deviceid.equals("T")) {
                area.add(obj);
            }
        //        专
            else if(deviceid.equals("A")) {
                special.add(obj);
            }
        //        市
            else {
                city.add(obj);
            }
        }
        _Data.clear();
        _Data.addAll(city);
        _Data.addAll(area);
        _Data.addAll(special);
        if(isReverMap){
            if(_Data.get(0) instanceof AirDayData.DataBean){
                daySortMap.clear();
                daySortMap.put("city",city);
                daySortMap.put("area",area);
                daySortMap.put("special",special);
            }else if(_Data.get(0) instanceof AirCurData.DataBean){
                curSortMap.clear();
                curSortMap.put("city",city);
                curSortMap.put("area",area);
                curSortMap.put("special",special);
            }
        }
        return  _Data;
    }

    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int i) {
        switch (i) {
            case R.id.hourDataBtn:
                ll2.setVisibility(View.GONE);
                ll1.setVisibility(View.VISIBLE);
                pm25.setChecked(true);
                isCur = true;
                break;
            case R.id.dayDataBtn:
                ll1.setVisibility(View.GONE);
                ll2.setVisibility(View.VISIBLE);
                pm25.setChecked(true);
                isCur = false;
                break;
            case R.id.rb_aqi:
                if (isCur) {//实时数据的情况
                    curValueList.clear();
                    for (AirCurData.DataBean dataBean : curlist) {
//                        此处作了aqi的六项对比，取最大值
                          curValueList.add(mostAqi(dataBean));
//                        curValueList.add(dataBean.getAQI());
                    }
                    curTag = "aqi";
                    airCurAdapter.change(curTag, R.id.rb_aqi);
                } else {//日数据的情况
                    dayValueList.clear();
                    for (AirDayData.DataBean dataBean : daylist) {
                        dayValueList.add(dataBean.getAQI());
                    }
                    dayTag = "aqi";
                    airDayAdapter.change(dayTag);
                }

                break;
            case R.id.rb_co:
                if (isCur) {//实时数据的情况
                    curValueList.clear();
                    for (AirCurData.DataBean dataBean : curlist) {
                        curValueList.add(dataBean.getCo());
                    }
                    curTag = "co";
                    airCurAdapter.change(curTag,R.id.rb_co);
                } else {//日数据的情况
                    dayValueList.clear();
                    for (AirDayData.DataBean dataBean : daylist) {
                        dayValueList.add(dataBean.getCo());
                    }
                    dayTag = "co";
                    airDayAdapter.change(dayTag);
                }

                break;
            case R.id.rb_no2:
                if (isCur) {//实时数据的情况
                    curValueList.clear();
                    for (AirCurData.DataBean dataBean : curlist) {
                        curValueList.add(dataBean.getNo2());
                    }
                    curTag = "no2";
                    airCurAdapter.change(curTag, R.id.rb_no2);
                } else {//日数据的情况
                    dayValueList.clear();
                    for (AirDayData.DataBean dataBean : daylist) {
                        dayValueList.add(dataBean.getNo2());
                    }
                    dayTag = "no2";
                    airDayAdapter.change(dayTag);
                }

                break;
            case R.id.rb_o3:
                if (isCur) {//实时数据的情况
                    curValueList.clear();
                    for (AirCurData.DataBean dataBean : curlist) {
                        curValueList.add(dataBean.getO3());
                    }
                    curTag = "o3";
                    airCurAdapter.change(curTag,R.id.rb_o3);
                } else {//日数据的情况
                    dayValueList.clear();
                    for (AirDayData.DataBean dataBean : daylist) {
                        dayValueList.add(dataBean.getO3());
                    }
                    dayTag = "o3";
                    airDayAdapter.change(dayTag);
                }

                break;
            case R.id.rb_pm10:
                if (isCur) {//实时数据的情况
                    curValueList.clear();
                    for (AirCurData.DataBean dataBean : curlist) {
                        curValueList.add(dataBean.getPm10());
                    }
                    curTag = "pm10";
                    airCurAdapter.change(curTag,R.id.rb_pm10);
                } else {//日数据的情况
                    dayValueList.clear();
                    for (AirDayData.DataBean dataBean : daylist) {
                        dayValueList.add(dataBean.getPm10());
                    }
                    dayTag = "pm10";
                    airDayAdapter.change(dayTag);
                }

                break;
            case R.id.rb_pm25:
                if (isCur) {//实时数据的情况
                    curValueList.clear();
                    for (AirCurData.DataBean dataBean : curlist) {
                        curValueList.add(dataBean.getPm25());
                    }
                    curTag = "pm25";
                    airCurAdapter.change(curTag,R.id.rb_pm25);
                } else {//日数据的情况
                    dayValueList.clear();
                    for (AirDayData.DataBean dataBean : daylist) {
                        dayValueList.add(dataBean.getPm25());
                    }
                    dayTag = "pm25";
                    airDayAdapter.change(dayTag);
                }

                break;
            case R.id.rb_so2:
                if (isCur) {//实时数据的情况
                    curValueList.clear();
                    for (AirCurData.DataBean dataBean : curlist) {
                        curValueList.add(dataBean.getSo2());
                    }
                    curTag = "so2";
                    airCurAdapter.change(curTag,R.id.rb_so2);
                } else {//日数据的情况
                    dayValueList.clear();
                    for (AirDayData.DataBean dataBean : daylist) {
                        dayValueList.add(dataBean.getSo2());
                    }
                    dayTag = "so2";
                    airDayAdapter.change(dayTag);
                }

                break;
        }
    }
    public  double  mostAqi(AirCurData.DataBean bean){
        double[] data = new double[]{ bean.getCoIAQI(), bean.getNo2IAQI(), bean.getO3IAQI(),
                bean.getPm10IAQI(),bean.getPm25IAQI(),bean.getSo2IAQI()};
        double mostDouble = 0.0;
        for(int i=0;i<data.length;i++){
            if(i==0){
                mostDouble = data[i];
            }else if( data[i]>mostDouble){
               mostDouble = data[i];
            }

        }
        return  mostDouble;
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.time_layout:
                showPopwindow(getDataPick(0));
                break;
            case R.id.set:
                if (timeView.findViewById(R.id.date).getVisibility() == View.VISIBLE) {
                    selected_time.setText(getDateForDay(v));
                    aqi.setChecked(true);
                    getAirDayData();
                    // dismissPopwindow();
                }

                break;
            case R.id.streetLayout:
                onPicker();
                break;
            case R.id.curStreetLayout:
                onPicker();
                break;
        }
    }
    //弹出街道选择
    private void onPicker(){
        try {
//            ArrayList<Province> data = new ArrayList<>();
//            String json = ConvertUtils.toString(getAssets().open("city2.json"));
//            data.addAll(JSON.parseArray(json, Province.class));
            AddressPicker picker = new AddressPicker(this, DataApplication.streetBean.getProvinces());
            picker.setShadowVisible(true);
            picker.setHideProvince(true);
//            picker.setSelectedItem("北京", "阳坊", "004四家庄小学");
            picker.setOnAddressPickListener(new AddressPicker.OnAddressPickListener() {
                @Override
                public void onAddressPicked(Province province, City city, County county) {
//                    showToast("province : " + province + ", city: " + city + ", county: " + county);
                    if(isCur){
                        updateCurData(getCurStationByStreet(city.getAreaId(),county.getAreaId()));
                        curSelectTxt.setText(city.getName());
                    }else{
                        updateDayData(getDayStationByStreet(city.getAreaId(),county.getAreaId()));
                        daySelectTxt.setText(city.getName());
                    }

                }
            });
            picker.show();
        } catch (Exception e) {
            showToast(LogUtils.toStackTraceString(e));
        }
    }
    //街道下的站点实时数据
    private List getCurStationByStreet(String cityId,String deviceId){
        Map map = DataApplication.streetBean.getStreetMap();
        StreetBean.DataBean bean= (StreetBean.DataBean) map.get(cityId);
        curSessionMap.put("cityId",cityId);
        curSessionMap.put("deviceId",deviceId);
        List AirBean = new ArrayList();
        if(bean==null){
            if(deviceId.equals("C000000")){
                AirBean.addAll((List) curSortMap.get("city"));
                return AirBean;
            }else if(deviceId.equals("A000000")){
                AirBean.addAll((List) curSortMap.get("area"));
                return AirBean;
            }else if(deviceId.equals("S000000")){
                AirBean.addAll((List) curSortMap.get("special"));
                return AirBean;
            }else{
                for(Object dataBean:curValueMap.values()){
                    AirBean.add(dataBean);
                }
                return AirBean;
            }
        }

        AirCurData.DataBean Bean = (AirCurData.DataBean) curValueMap.get(deviceId);
        if(Bean!=null){
            AirBean.add(Bean);
            return AirBean;
        }
        for(StreetBean.DataBean.DevicesBean device:bean.getDevices()){
            String id = device.getDeviceid();
            AirCurData.DataBean dataBean = (AirCurData.DataBean) curValueMap.get(id);
            AirBean.add(dataBean);
        }
        return AirBean;
    }
    //街道下的站点日数据
    private List getDayStationByStreet(String cityId,String deviceId){
        Map map = DataApplication.streetBean.getStreetMap();
        StreetBean.DataBean bean= (StreetBean.DataBean) map.get(cityId);
        List AirBean = new ArrayList();
        if(bean==null){
           if(deviceId.equals("C000000")){
               AirBean.addAll((List) daySortMap.get("city"));
               return AirBean;
           }else if(deviceId.equals("A000000")){
               AirBean.addAll((List) daySortMap.get("area"));
               return AirBean;
           }else if(deviceId.equals("S000000")){
               AirBean.addAll((List) daySortMap.get("special"));
               return AirBean;
           }else{
               for(Object dataBean:dayValueMap.values()){
                   AirBean.add(dataBean);
               }
               return AirBean;
           }
        }
        AirDayData.DataBean Bean = (AirDayData.DataBean) dayValueMap.get(deviceId);
        if(Bean!=null){
            AirBean.add(Bean);
            return AirBean;
        }
        for(StreetBean.DataBean.DevicesBean device:bean.getDevices()){
            String id = device.getDeviceid();
            AirDayData.DataBean dataBean = (AirDayData.DataBean) dayValueMap.get(id);
            AirBean.add(dataBean);
        }
        return AirBean;
    }
    //请求街道综合均值
    private void requestStreetPM(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                ArrayList<HashMap<String, Object>> params = new ArrayList<HashMap<String, Object>>();
                HashMap<String, Object> param = new HashMap<String, Object>();
                param.put("ReturnDataType", "1");
                param.put("DateTime", "");
                param.put("CityCode", "");
                param.put("PolluteFiled", "");
                params.add(param);
                String result = "";
                try {
                    result = (String) WebServiceProvider
                            .callWebService(
                                    "http://tempuri.org/",
                                    "JL_HJJC_GetAirQualityCityDayData",
                                    params,
                                    "http://192.168.120.131:5555/CPGISWebservice/JLWebServiceService.asmx",
                                    WebServiceProvider.RETURN_STRING, true);
                    Log.i("Lybin","requestStreetPM-----"+result);
//                    JSONObject jsonObject = new JSONObject(result);
//                    Boolean isSuccess = jsonObject.getBoolean("isSuccess");
//                    if (isSuccess) {
////                        String userid = jsonObject.getJSONArray("result").getJSONObject(0).getString("USER_ID");
////                        handler.sendEmptyMessage(1);
//                    } else {
////                        handler.sendEmptyMessage(2);
//                    }
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }).start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        OkHttpUtils.getInstance().cancelTag(this);
    }

    @Override
    public void onRefresh() {
        getAirCurData();
    }
}
