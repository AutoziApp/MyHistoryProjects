package com.mapuni.shangluo.activity.xcAc;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.LocationSource;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.UiSettings;
import com.amap.api.maps2d.model.BitmapDescriptorFactory;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.MarkerOptions;
import com.amap.api.maps2d.model.MyLocationStyle;
import com.bigkoo.svprogresshud.SVProgressHUD;
import com.mapuni.shangluo.R;
import com.mapuni.shangluo.activity.BaseActivity;
import com.mapuni.shangluo.manager.NetManager;
import com.mapuni.shangluo.utils.SPUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

import okhttp3.Call;

public class QianDaoActivity extends BaseActivity implements LocationSource, AMapLocationListener,View.OnClickListener {
    private MapView mapView;
    private AMap aMap;
    private TextView tvAddress;
    private TextView ivQianDao;

    //定位需要的声明
    private AMapLocationClient mLocationClient = null;//定位发起端
    private AMapLocationClientOption mLocationOption = null;//定位参数
    private LocationSource.OnLocationChangedListener mListener = null;//定位监听器

    //标识，用于判断是否只显示一次定位信息和用户重新定位
    private boolean isFirstLoc = true;

    String  sessionId;
    String longitude="";
    String latitude="";
    String address="";

    @Override
    public int setLayoutResID() {
        return R.layout.activity_qian_dao;
    }

    @Override
    public void initView() {
        setToolbarTitle("签到页面");
        mapView = (MapView) findViewById(R.id.mapview);
        tvAddress= (TextView) findViewById(R.id.tv_address);
        ivQianDao= (TextView) findViewById(R.id.iv_qiandao);
        ivQianDao.setOnClickListener(this);
        mapView.onCreate(savedInstanceState);

        //获取地图对象
        aMap = mapView.getMap();
        // 设置显示定位按钮 并且可以点击
        UiSettings settings = aMap.getUiSettings();
        // 设置定位监听
        aMap.setLocationSource(this);
        // 是否显示定位按钮
        settings.setMyLocationButtonEnabled(true);
        settings.setZoomControlsEnabled(false);
        // 是否可触发定位并显示定位层
        aMap.setMyLocationEnabled(true);
        // 定位的小图标 默认是蓝点 这里自定义一团火，其实就是一张图片
        MyLocationStyle myLocationStyle = new MyLocationStyle();
//        myLocationStyle.myLocationIcon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_launcher));
        myLocationStyle.radiusFillColor(android.R.color.transparent);
        myLocationStyle.strokeColor(android.R.color.transparent);
        aMap.setMyLocationStyle(myLocationStyle);
        // 开始定位
        initLoc();

        mToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item)  {
                //在这里执行我们的逻辑代码
                switch (item.getItemId()){
                    case R.id.action_record:
                        startActivity(new Intent(QianDaoActivity.this,QiandaoRecordActivity.class));
                        break;
                }
                return false;
            }
        });

    }

    //定位
    private void initLoc() {
        // 初始化定位
        mLocationClient = new AMapLocationClient(getApplicationContext());
        // 设置定位回调监听
        mLocationClient.setLocationListener(this);
        // 初始化定位参数
        mLocationOption = new AMapLocationClientOption();
        // 设置定位模式为高精度模式，Battery_Saving为低功耗模式，Device_Sensors是仅设备模式
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        // 设置是否返回地址信息（默认返回地址信息）
        mLocationOption.setNeedAddress(true);
        // 设置是否只定位一次,默认为false
        mLocationOption.setOnceLocation(false);
        // 设置是否强制刷新WIFI，默认为强制刷新
        mLocationOption.setWifiActiveScan(true);
        // 设置是否允许模拟位置,默认为false，不允许模拟位置
        mLocationOption.setMockEnable(false);
        // 设置定位间隔,单位毫秒,默认为2000ms
        mLocationOption.setInterval(2000);
        // 给定位客户端对象设置定位参数
        mLocationClient.setLocationOption(mLocationOption);
        // 启动定位
        mLocationClient.startLocation();
    }


    @Override
    public void initData() {
          sessionId = (String) SPUtils.getSp(this, "sessionId", "");
        isRegisted();
    }

    public void isRegisted() {

        NetManager.isRegisted(sessionId, new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                Toast.makeText(QianDaoActivity.this,"状态异常，请稍后重试！",Toast.LENGTH_SHORT).show();
                finish();
            }

            @Override
            public void onResponse(String response, int id) {
                try {
                    JSONObject jsonObject=new JSONObject(response);
                    int status=jsonObject.optInt("status",-1);
                    switch (status){
                        case 0://未签
                            ivQianDao.setText("签到");
                            break;
                        case 1://已签
                            ivQianDao.setText("已签到");
                            ivQianDao.setClickable(false);
                            break;
                        case -1://报错
                            Toast.makeText(QianDaoActivity.this,"状态异常，请稍后重试！",Toast.LENGTH_SHORT).show();
                            finish();
                            break;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });

    }


    @Override
    public void onLocationChanged(AMapLocation amapLocation) {
        if (amapLocation != null) {
            if (amapLocation.getErrorCode() == 0) {
                //定位成功回调信息，设置相关消息
                amapLocation.getLocationType();
                // 获取当前定位结果来源，如网络定位结果，详见官方定位类型表
                latitude=""+amapLocation.getLatitude();
                // /获取纬度
                longitude=""+amapLocation.getLongitude();
                // 获取经度
                amapLocation.getAccuracy();
                // 获取精度信息
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date date = new Date(amapLocation.getTime());
                df.format(date);
                //定位时间
                address=""+amapLocation.getAddress();
                // 地址，如果option中设置isNeedAddress为false，则没有此结果，网络定位结果中会有地址信息，GPS定位不返回地址信息。
                amapLocation.getCountry();
                // 国家信息
                amapLocation.getProvince();
                // 省信息
                amapLocation.getCity();
                // 城市信息
                amapLocation.getDistrict();
                // 城区信息
                amapLocation.getStreet();
                // 街道信息
                amapLocation.getStreetNum();
                // 街道门牌号信息
                amapLocation.getCityCode();
                // 城市编码
                amapLocation.getAdCode();
                // 地区编码 // 如果不设置标志位，此时再拖动地图时，它会不断将地图移动到当前的位置
                if (isFirstLoc) {
                    // 设置缩放级别
                    aMap.moveCamera(CameraUpdateFactory.zoomTo(17));
                    // 将地图移动到定位点
                    aMap.moveCamera(CameraUpdateFactory.changeLatLng(new LatLng(amapLocation.getLatitude(), amapLocation.getLongitude())));
                    // 点击定位按钮 能够将地图的中心移动到定位点
                    mListener.onLocationChanged(amapLocation);
                    // 添加图钉
//                    aMap.addMarker(getMarkerOptions(amapLocation));
                    // 获取定位信息
//                    StringBuffer buffer = new StringBuffer();
//                    buffer.append(amapLocation.getCountry() + "" + amapLocation.getProvince() + "" + amapLocation.getCity() + "" + amapLocation.getProvince() + "" + amapLocation.getDistrict() + "" + amapLocation.getStreet() + "" + amapLocation.getStreetNum());
//                    Toast.makeText(getApplicationContext(), buffer.toString(), Toast.LENGTH_LONG).show();
                    tvAddress.setText(address);
                    isFirstLoc = false;
                }
            } else {
                // 显示错误信息ErrCode是错误码，errInfo是错误信息，详见错误码表。
                Log.e("AmapError", "location Error, ErrCode:" + amapLocation.getErrorCode() + ", errInfo:" + amapLocation.getErrorInfo());
                Toast.makeText(getApplicationContext(), "定位失败", Toast.LENGTH_LONG).show();
            }
        }
    }


    //自定义一个图钉，并且设置图标，当我们点击图钉时，显示设置的信息
    private MarkerOptions getMarkerOptions(AMapLocation amapLocation) {
        // 设置图钉选项
        MarkerOptions options = new MarkerOptions();
        // 图标
        options.icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_launcher));
        // 位置
        options.position(new LatLng(amapLocation.getLatitude(), amapLocation.getLongitude()));
        StringBuffer buffer = new StringBuffer();
        buffer.append(amapLocation.getCountry() + "" + amapLocation.getProvince() + "" + amapLocation.getCity() + "" + amapLocation.getDistrict() + "" + amapLocation.getStreet() + "" + amapLocation.getStreetNum());
        // 标题
        options.title(buffer.toString());
        // 子标题
        options.snippet("这里好火");
        // 设置多少帧刷新一次图片资源
        options.period(60);
        return options;
    }


    public void regester(String  longitude,String latitude,String address){
         final SVProgressHUD mSVProgressHUD = new SVProgressHUD(this);
        NetManager.regester(sessionId, longitude, latitude, address, new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                mSVProgressHUD.showErrorWithStatus("签到失败", SVProgressHUD.SVProgressHUDMaskType.GradientCancel);
            }

            @Override
            public void onResponse(String response, int id) {
                //{"msg":"200"}
                try {
                    JSONObject jsonObject=new JSONObject(response);
                    if("200".equals(jsonObject.optString("msg"))){
                        ivQianDao.setText("已签到");
                        ivQianDao.setClickable(false);
                        mSVProgressHUD.showSuccessWithStatus("签到成功！");
                    }else {
                        mSVProgressHUD.showErrorWithStatus("签到失败", SVProgressHUD.SVProgressHUDMaskType.GradientCancel);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    mSVProgressHUD.showErrorWithStatus("签到失败", SVProgressHUD.SVProgressHUDMaskType.GradientCancel);
                }
            }
        });

    }


    @Override
    public void activate(OnLocationChangedListener onLocationChangedListener) {
        mListener = onLocationChangedListener;
    }

    @Override
    public void deactivate() {
        mListener = null;
    }

    /**
     * 方法必须重写
     * map的生命周期方法
     */
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_qiandao:
                regester(longitude,latitude,address);
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // 为toolbar创建Menu
        getMenuInflater().inflate(R.menu.menu_main2, menu);
        return true;
    }

}
