package com.mapuni.mobileenvironment.fragment;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.CameraUpdate;
import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.CoordinateConverter;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.UiSettings;
import com.amap.api.maps2d.model.BitmapDescriptorFactory;
import com.amap.api.maps2d.model.CameraPosition;
import com.amap.api.maps2d.model.GroundOverlay;
import com.amap.api.maps2d.model.GroundOverlayOptions;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.LatLngBounds;
import com.amap.api.maps2d.model.Marker;
import com.amap.api.maps2d.model.MarkerOptions;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mapuni.mobileenvironment.R;
import com.mapuni.mobileenvironment.activity.CompanyPollutionDetailActivity;
import com.mapuni.mobileenvironment.activity.YouYanDetailActivity;
import com.mapuni.mobileenvironment.activity.newAirCurActivity;
import com.mapuni.mobileenvironment.app.DataApplication;
import com.mapuni.mobileenvironment.bean.StreetBean;
import com.mapuni.mobileenvironment.config.PathManager;
import com.mapuni.mobileenvironment.model.AirSingle;
import com.mapuni.mobileenvironment.model.EntModel;
import com.mapuni.mobileenvironment.model.GasSite;
import com.mapuni.mobileenvironment.model.MapSource;
import com.mapuni.mobileenvironment.model.MapYouYan;
import com.mapuni.mobileenvironment.utils.DateUtils;
import com.mapuni.mobileenvironment.utils.JsonUtil;
import com.mapuni.mobileenvironment.utils.PollutionLevelCalUtil;
import com.mapuni.mobileenvironment.utils.SignUtil;
import com.mapuni.mobileenvironment.view.YutuLoading;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.BitmapCallback;
import com.zhy.http.okhttp.callback.StringCallback;

import java.lang.reflect.Type;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import cn.qqtheme.framework.entity.City;
import cn.qqtheme.framework.entity.County;
import cn.qqtheme.framework.entity.Province;
import cn.qqtheme.framework.picker.AddressPicker;
import okhttp3.Call;
import okhttp3.Request;
import sbingo.freeradiogroup.FreeRadioGroup;


public class MapFragment extends SupportFragment implements AMap.InfoWindowAdapter, View.OnClickListener, AMap.OnMarkerClickListener, AMap.OnMapClickListener{
    private Marker currentMarker;
    private OnFragmentInteractionListener mListener;
    private MapView mapView;
    private AMap aMap;
    private UiSettings mUiSettings;//定义一个UiSettings对象
    private FreeRadioGroup mGroup;
    Bitmap bitmap;
    Button btnStreet;
    private int currenHour;
    private List<GasSite.DataBeanX.DataBean> gass;
    private RadioButton rb_yan, rb_air, rb_source, rb_location, rb_map;
    private GroundOverlay groundoverlay;

    private boolean isShow = false;
    //    private RadioButton  rb_chazhao,  rb_clear;
    //声明AMapLocationClient类对象
    public AMapLocationClient mLocationClient = null;
    private List<Marker> curMarkers = new ArrayList<>();
    //声明定位回调监听器
    public AMapLocationListener mLocationListener = new AMapLocationListener() {
        @Override
        public void onLocationChanged(AMapLocation amapLocation) {
            if (amapLocation != null) {
                if (amapLocation.getErrorCode() == 0) {
                    lat = amapLocation.getLatitude();
                    lon = amapLocation.getLongitude();
                    aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lat, lon), 11));
                  /*  MarkerOptions markerOptions = new MarkerOptions();
                    markerOptions.position(new LatLng(lat, lon));
                    BitmapDescriptor bitmapDescriptor = BitmapDescriptorFactory.fromBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.location_map));
                    markerOptions.icon(bitmapDescriptor);
                    aMap.addMarker(markerOptions);*/
                    Marker marker = aMap.addMarker(new MarkerOptions().position(new LatLng(lat, lon))
                            .snippet("").icon(BitmapDescriptorFactory.fromResource(R.drawable.location_map)));
                    curMarkers.add(marker);
                } else {
                    //显示错误信息ErrCode是错误码，errInfo是错误信息，详见错误码表。
                    Toast.makeText(getContext(),"AmapError"+","+ "location_mark Error, ErrCode:"
                            + amapLocation.getErrorCode() + ", errInfo:"
                            + amapLocation.getErrorInfo(),Toast.LENGTH_SHORT).show();
                }
            }
        }
    };

    //声明mLocationOption对象
    public AMapLocationClientOption mLocationOption = null;
    private double lat = 0;
    private double lon = 0;
    List<MapSource> sources = new ArrayList<MapSource>();//污染源数据集合
    List<GasSite.DataBeanX.DataBean> airData;//污染源数据集合
    private Gson gson;

    public MapFragment() {
    }

    public static MapFragment newFragment() {
        MapFragment fragment = new MapFragment();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public static boolean requestPermission(Context c) {
        String[] permission = new String[]{Manifest.permission.ACCESS_FINE_LOCATION};
        int permissionLocation = ContextCompat.checkSelfPermission((Activity) c,
                Manifest.permission.ACCESS_FINE_LOCATION);
        if (permissionLocation == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else if (permissionLocation == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions((Activity) c, permission, 1024);
            return false;
        }
        return false;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1024: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    setUpMap();
                } else {
                    Toast.makeText(getActivity(), "请批准权限,否则无法定位", Toast.LENGTH_SHORT).show();
                    DataApplication.requestPermission(getActivity());
                }
                return;
            }
        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        if (!hidden) {
            mListener.onCloseToolbar(false);
            mListener.onChangeTitle(getResources().getString(R.string.tab_map));
        }
//        super.onHiddenChanged(hidden);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = View.inflate(getContext(), R.layout.fragment_map, null);

        initSourceData();//请求污染源数据
        initGasData();//请求gas站点数据
        initYanData();
        initHeatMap(0);
        //获取地图控件引用
        mapView = (MapView) view.findViewById(R.id.map);

        //在activity执行onCreate时执行mMapView.onCreate(savedInstanceState)，实现地图生命周期管理
        mapView.onCreate(savedInstanceState);// 此方法必须重写
        aMap = mapView.getMap();
        mUiSettings = aMap.getUiSettings();//实例化UiSettings类
        mUiSettings.setScaleControlsEnabled(true);//显示比例尺控件
        mUiSettings.setZoomControlsEnabled(false);//是否显示缩放按钮
        mUiSettings.setAllGesturesEnabled(true);//所有手势
       // aMap.setOnCameraChangeListener(this);
        //绑定信息窗点击事件
        aMap.setOnInfoWindowClickListener(listener);
        aMap.setInfoWindowAdapter(this);
        //初始化定位
        mLocationClient = new AMapLocationClient(getActivity());
        //设置定位回调监听
        mLocationClient.setLocationListener(mLocationListener);

        aMap.setOnMapClickListener(this);
        aMap.setOnMarkerClickListener(this);

        mGroup = (FreeRadioGroup) view.findViewById(R.id.group);
        rb_map = (RadioButton) view.findViewById(R.id.rb_map);
        rb_air = (RadioButton) view.findViewById(R.id.rb_air);
        rb_source = (RadioButton) view.findViewById(R.id.rb_source);
        rb_yan = (RadioButton) view.findViewById(R.id.rb_yan);
//        rb_chazhao = (RadioButton) view.findViewById(rb_chazhao);
        rb_location = (RadioButton) view.findViewById(R.id.rb_location);
//        rb_clear = (RadioButton) view.findViewById(rb_clear);
        rb_air.setOnClickListener(this);
        rb_source.setOnClickListener(this);
        rb_yan.setOnClickListener(this);
        rb_map.setOnClickListener(this);
//        rb_chazhao.setOnClickListener(this);
        rb_location.setOnClickListener(this);
//        rb_clear.setOnClickListener(this);
        btnStreet= (Button) view.findViewById(R.id.btn_street);
        return view;
    }

    /**
     * 通过拼接当前时间来获取  网络热力图
     * @param
     */
    private void initHeatMap(int select) {
        Calendar c = Calendar.getInstance(Locale.CHINA);
        int Hour = 0;
        String yue = "";
        String ri = "";
        String nian = c.get(Calendar.YEAR) + "";
        int tempYue = c.get(Calendar.MONTH) + 1;
        int tempRi = c.get(Calendar.DAY_OF_MONTH);
        if (isShow) {
            Hour = c.get(Calendar.HOUR_OF_DAY) - 2;
        } else {
            Hour = c.get(Calendar.HOUR_OF_DAY) - 1;
        }
        if (select==666){
            Hour = Hour - 1;
        }
        String currenHour = null;
        if (Hour < 10) {
            currenHour = "0" + Hour;
        } else {
            currenHour = "" + Hour;
        }
        String NetHotMap = "";
        if (tempYue < 10) {
            yue = "0" + (tempYue + "");
        } else {
            yue = tempYue + "";
        }
        if (tempRi<10){
            ri = "0"+(tempRi + "");
        }else {
            ri = tempRi + "";
        }
        String url = "http://218.246.81.181:8119/file/img/" + nian + "-" + yue + "/" + nian + "-" + yue + "-" + ri + "T" + currenHour + "_00_00.png";

        OkHttpUtils.get()
                .url(url)
                .build()
                .execute(new BitmapCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                    }

                    @Override
                    public void onResponse(Bitmap response, int id) {
                        /*if (response == null) {
                            isShow = true;
                            initHeatMap(0);
                            return;
                        }*/
                        bitmap = response;
                    }
                });
    }

    public LatLng changeLatLng(LatLng sourceLatLng) {
        CoordinateConverter converter = new CoordinateConverter();
        // CoordType.GPS 待转换坐标类型
        converter.from(CoordinateConverter.CoordType.BAIDU);
        // sourceLatLng待转换坐标点 LatLng类型
        converter = converter.coord(sourceLatLng);
        // 执行转换操作
        LatLng desLatLng = converter.convert();
        return desLatLng;
    }
    public LatLng changeLatLngFromGPS(LatLng sourceLatLng) {
        CoordinateConverter converter = new CoordinateConverter();
        // CoordType.GPS 待转换坐标类型
        converter.from(CoordinateConverter.CoordType.GPS);
        // sourceLatLng待转换坐标点 LatLng类型
        converter = converter.coord(sourceLatLng);
        // 执行转换操作
        LatLng desLatLng = converter.convert();
        return desLatLng;
    }

    //初始化污染源数据
    private void initSourceData() {
        OkHttpUtils
                .get()//
                .url(PathManager.MapOverEnterList)//
                .build()//
                .execute(new StringCallback() {
                    @Override
                    public void onBefore(Request request, int id) {
                        super.onBefore(request, id);
                    }

                    @Override
                    public void onAfter(int id) {
                        super.onAfter(id);
                    }

                    @Override
                    public void onError(Call call, Exception e, int id) {
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        gson = new Gson();
                        Type type = new TypeToken<ArrayList<MapSource>>() {
                        }.getType();
                        sources = gson.fromJson(response, type);
                    }
                });
    }

    private void initGasData() {
//        String url = PathManager.GetSites;
        String url = PathManager.GetDataHour;
        Map<String, String> jo = new HashMap<>();
        String nonce = SignUtil.getNonce();
        String timestamp = new Date().getTime() + "";
        jo.put("timestamp", timestamp);
        jo.put("nonce", nonce);
        jo.put("signature", SignUtil.getSignature2(timestamp, nonce));
        OkHttpUtils
                .post()//
                .url(url)//
                .params(jo)
                .build()//
                .execute(new StringCallback() {
                    @Override
                    public void onBefore(Request request, int id) {
                        super.onBefore(request, id);
                    }

                    @Override
                    public void onAfter(int id) {
                        super.onAfter(id);
                    }

                    @Override
                    public void onError(Call call, Exception e, int id) {
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        Log.i("Lybin", "airStation-------" + response);
                        GasSite sites = (GasSite) JsonUtil.jsonToBean(response, GasSite.class);
                        if (0 == sites.getRet()) {
                            gass = sites.getData().getData();
                        }
                    }
                });
    }

    ArrayList yanList;

    private void initYanData() {
        OkHttpUtils
                .get()//
                .url(PathManager.MapYanList)//
                .build()//
                .execute(new StringCallback() {
                    @Override
                    public void onBefore(Request request, int id) {
                        super.onBefore(request, id);
                    }

                    @Override
                    public void onAfter(int id) {
                        super.onAfter(id);
                    }

                    @Override
                    public void onError(Call call, Exception e, int id) {
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        yanList = JsonUtil.fromJsonList(response, MapYouYan.class);
                    }
                });
    }

    /*private void setUpMap() {
        aMap.clear();
        //初始化定位参数
        mLocationOption = new AMapLocationClientOption();
        //设置定位模式为高精度模式，Battery_Saving为低功耗模式，Device_Sensors是仅设备模式
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        //设置是否返回地址信息（默认返回地址信息）
        mLocationOption.setNeedAddress(true);
        //设置是否只定位一次,默认为false
        mLocationOption.setOnceLocation(true);
        //设置是否强制刷新WIFI，默认为强制刷新
        mLocationOption.setWifiActiveScan(true);
        //设置是否允许模拟位置,默认为false，不允许模拟位置
        mLocationOption.setMockEnable(false);
        //设置定位间隔,单位毫秒,默认为2000ms
        mLocationOption.setInterval(2000);
        //给定位客户端对象设置定位参数
        mLocationClient.setLocationOption(mLocationOption);
        //启动定位
        mLocationClient.startLocation();
        // 自定义系统定位小蓝点
        //changeCamera();

    }*/
    private LocationManager locationManager;
    private String locationProvider;

    private void setUpMap() {
        clearMarker();
      /*  //获取地理位置管理器
        locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        //获取所有可用的位置提供器
        List<String> providers = locationManager.getProviders(true);
        if (providers.contains(LocationManager.GPS_PROVIDER)) {
            //如果是GPS
            locationProvider = LocationManager.GPS_PROVIDER;
        } else if (providers.contains(LocationManager.NETWORK_PROVIDER)) {
            //如果是Network
            locationProvider = LocationManager.NETWORK_PROVIDER;
        } else {
            Toast.makeText(getActivity(), "没有可用的位置提供器", Toast.LENGTH_SHORT).show();
            return;
        }

        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        Location location = locationManager.getLastKnownLocation(locationProvider);
        double Longitude=location.getLongitude();
        double Latitude=location.getLatitude();
        LatLng latLng=changeLatLngFromGPS(new LatLng(Latitude,Longitude));
        aMap.moveCamera(CameraUpdateFactory.newCameraPosition(new CameraPosition(
//                    new LatLng(40.220181, 116.267296),//新的中心点坐标
//                    15, //新的缩放级别
//
                new LatLng(latLng.latitude,latLng.longitude),//新的中心点坐标
                13,
                30, //俯仰角0°~45°（垂直与地图时为0）
                0  ////偏航角 0~360° (正北方为0)
        )));
        MarkerOptions option = new MarkerOptions().position(latLng)
                .title("").snippet("").icon(
                        BitmapDescriptorFactory.fromBitmap(
                                BitmapFactory.decodeResource(getResources(), R.drawable.location_map)));
        aMap.addMarker(option);*/
        //初始化定位参数
        mLocationOption = new AMapLocationClientOption();
        //设置定位模式为高精度模式，Battery_Saving为低功耗模式，Device_Sensors是仅设备模式
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        //设置是否返回地址信息（默认返回地址信息）
        mLocationOption.setNeedAddress(true);
        //设置是否只定位一次,默认为false
        mLocationOption.setOnceLocation(true);
        //设置是否强制刷新WIFI，默认为强制刷新
        mLocationOption.setWifiActiveScan(true);
        //设置是否允许模拟位置,默认为false，不允许模拟位置
        mLocationOption.setMockEnable(false);
        //设置定位间隔,单位毫秒,默认为2000ms
        mLocationOption.setInterval(2000);
        //给定位客户端对象设置定位参数
        mLocationClient.setLocationOption(mLocationOption);
        //启动定位
        mLocationClient.startLocation();
    }
    /**
     * 根据动画按钮状态，调用函数animateCamera或moveCamera来改变可视区域
     */
    private void changeCamera(CameraUpdate update) {

        aMap.moveCamera(update);

    }
    private void addOverlayToMap() {
        clearMarker();
        if (bitmap==null){
           Toast.makeText(getContext(),"热力图本地无数据",Toast.LENGTH_SHORT).show();
            return;
        }
        //初始化定位
        aMap.moveCamera(CameraUpdateFactory.newCameraPosition(new CameraPosition(
//                    new LatLng(40.220181, 116.267296),//新的中心点坐标
//                    15, //新的缩放级别
//                    116.242    40.228
                new LatLng(40.228,116.242),//新的中心点坐标
                10,
                30, //俯仰角0°~45°（垂直与地图时为0）
                0  ////偏航角 0~360° (正北方为0)
        )));
            LatLng xiNan = new LatLng(39.955742, 115.703376);
            LatLng dongBei = new LatLng(40.456941, 116.687682);
           // LatLng latLng = changeLatLng(xiNan);
           // LatLng latLng1 = changeLatLng(dongBei);
       /* getDouble(xiNanGD.longitude)
        getDouble(dongBeiGD.longitude)    */
            LatLngBounds bounds = new LatLngBounds.Builder()
                    .include(new LatLng(xiNan.latitude, xiNan.longitude))
                    .include(new LatLng(dongBei.latitude, dongBei.longitude)).build();
        if (groundoverlay!=null){
            groundoverlay.setVisible(true);
            groundoverlay.setImage(BitmapDescriptorFactory.fromBitmap(bitmap));
        }else {
            groundoverlay = aMap.addGroundOverlay(new GroundOverlayOptions()
                    .anchor(0.5f, 0.5f)
                    .transparency(0.5f)
                    .image(BitmapDescriptorFactory.fromBitmap(bitmap))
                    .positionFromBounds(bounds));
        }
        }

    private void initPosition() {
        clearMarker();
        if(yanList==null||yanList.size()<=0){
            Toast.makeText(getActivity(),"未获取油烟数据",Toast.LENGTH_SHORT).show();
            return;
        }
        //MapYan yan = (MapYan) yanList.get(0);
        aMap.moveCamera(CameraUpdateFactory.newCameraPosition(new CameraPosition(
//                new LatLng(yan.getLatitude(),yan.getLongitude()),//新的中心点坐标
                new LatLng(40.228,116.242),//新的中心点坐标
                12, //新的缩放级别
                30, //俯仰角0°~45°（垂直与地图时为0）
                0  ////偏航角 0~360° (正北方为0)
        )));
        //List<LatLng> latlngs = new ArrayList<>();
       //冯家湾
        for(int i=0;i<yanList.size();i++){
            MapYouYan model = (MapYouYan) yanList.get(i);
            LatLng latLng = changeLatLng(new LatLng(model.getLatitude(),model.getLongitude()));
            //通过油烟排口信息进行油烟等级判断 来加载不同类型的图片
            Bitmap bitmap=null;
            if (model.getOnlineStatus() == 1 && model.getGfj()== 1&&model.getJhq()==1) {
                //UrlImage = "../Images/Gis/Hood_green.png";
                bitmap = BitmapFactory.decodeResource(getResources(),R.mipmap.hood_green);
            }
            else if (model.getOnlineStatus() == 1 &&  model.getGfj()== 1 && model.getJhq()==0) {
                // UrlImage = "../Images/Gis/Hood_greenAndor.png";
                bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.hood_greenandor);
            }
            else if (model.getOnlineStatus() == 1 &&  model.getGfj()== 0 && model.getJhq()==1) {
                //  UrlImage = "../Images/Gis/Hood_greenAndor.png";
                bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.hood_greenandor);
            }
            else if (model.getOnlineStatus() == 1 &&  model.getGfj()== 0 && model.getJhq()==0) {
                //  UrlImage = "../Images/Gis/Hood_greenAndall.png";
                bitmap = BitmapFactory.decodeResource(getResources(),R.mipmap.hood_greenandall);
            }
            else if (model.getOnlineStatus()==0) {
                //   UrlImage = "../Images/Gis/Hood_gray.png";
                bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.hood_gray);
            }

            Marker marker = aMap.addMarker(new MarkerOptions().position(latLng).
                    title(model.getEntName())
                    .snippet("").icon(BitmapDescriptorFactory.fromBitmap(
                            bitmap )));
            marker.setObject(model);
            curMarkers.add(marker);
        }
    }

    //污染源地图打点
    List<MarkerOptions> sourceOptions;
    private void initSourcePoints() {
        clearMarker();
        if (sources != null && sources.size() > 0) {
            aMap.moveCamera(CameraUpdateFactory.newCameraPosition(new CameraPosition(
//                    new LatLng(sources.get(0).getLatitude(), sources.get(0).getLongitude()),//新的中心点坐标
//                    12, //新的缩放级别
//                   116.242    40.228
                    new LatLng(40.228,116.242),//新的中心点坐标
                    12,
                    30, //俯仰角0°~45°（垂直与地图时为0）
                    0  ////偏航角 0~360° (正北方为0)
            )));
            if(sourceOptions!=null&&sourceOptions.size()>0){
                for (int i=0;i<sources.size();i++) {
                    MapSource source = sources.get(i);
                    MarkerOptions option = sourceOptions.get(i);
                    sourceOptions.add(option);
                    Marker marker = aMap.addMarker(option);
//                Marker marker = aMap.addMarker(new MarkerOptions().position(new LatLng(source.getLatitude(), source.getLongitude())).title(source.getEntName()).snippet("").icon(BitmapDescriptorFactory.fromBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.wuranyuan))));
//                Marker marker = aMap.addMarker(new MarkerOptions().position(new LatLng(source.getLatitude(), source.getLongitude())).title(source.getEntName()).snippet(""));
                    marker.setObject(source);//将该点详情信息绑定到marker
                    curMarkers.add(marker);
                }
                return;
            }
            sourceOptions = new ArrayList<>();
            for (MapSource source : sources) {
                LatLng _LatLng = changeLatLng(new LatLng(source.getLatitude(), source.getLongitude()));
                MarkerOptions option = new MarkerOptions().position(_LatLng)
                        .title(source.getEntName()).snippet("").icon(
                                BitmapDescriptorFactory.fromBitmap(
                                        BitmapFactory.decodeResource(getResources(), R.mipmap.wuranyuana_marker)));

                sourceOptions.add(option);
                Marker marker = aMap.addMarker(option);
//                Marker marker = aMap.addMarker(new MarkerOptions().position(new LatLng(source.getLatitude(), source.getLongitude())).title(source.getEntName()).snippet("").icon(BitmapDescriptorFactory.fromBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.wuranyuan))));
//                Marker marker = aMap.addMarker(new MarkerOptions().position(new LatLng(source.getLatitude(), source.getLongitude())).title(source.getEntName()).snippet(""));
                marker.setObject(source);//将该点详情信息绑定到marker
                curMarkers.add(marker);
            }
        } else {
            Toast.makeText(getActivity(), "暂无请求到相应数据！", Toast.LENGTH_SHORT).show();
        }
    }
    List<MarkerOptions> airMarks;
    Map airMarksMap;
    Map airSpecialMap;
    //gas站点地图打点
    private void initGasPoints() {
        clearMarker();
        if (gass != null && gass.size() > 0) {
            btnStreet.setText("所有街道");
            aMap.moveCamera(CameraUpdateFactory.newCameraPosition(new CameraPosition(
//                    new LatLng(40.220181, 116.267296),//新的中心点坐标
//                    15, //新的缩放级别
//                    116.242    40.228
                    new LatLng(40.228,116.242),//新的中心点坐标
                    12,
                    30, //俯仰角0°~45°（垂直与地图时为0）
                    0  ////偏航角 0~360° (正北方为0)
            )));
            if(airMarksMap!=null&&airMarksMap.size()>0){
                updateAirMap(gass);
//                curMarkers.addAll(airMarks);
                return;
            }
            clearMarker();
            airMarks = new ArrayList();
            if(airMarksMap==null)
                airMarksMap = new HashMap();
            if(airSpecialMap==null)
                airSpecialMap = new HashMap();
            reverseStation(gass);
            for ( GasSite.DataBeanX.DataBean dataBean : gass) {
//                int icon = .PollutionLevelCalUtilgetLevelIconForMap("pm25",dataBean.getPm25());
                LatLng _LatLng = changeLatLng(new LatLng(dataBean.getLat(), dataBean.getLon()));
                int icon = PollutionLevelCalUtil.getLevelIconForMap("pm25",dataBean.getPm25());
                MarkerOptions markerOptions = new MarkerOptions().position(_LatLng).title(dataBean.getName()).snippet("").icon(
                        BitmapDescriptorFactory.fromBitmap(
                                BitmapFactory.decodeResource(getResources(),icon)));
                airMarks.add(markerOptions);
                Marker marker = aMap.addMarker(markerOptions);
//                Marker marker = aMap.addMarker(new MarkerOptions().position(new LatLng(dataBean.getLat(), dataBean.getLon())).title(dataBean.getDname()).snippet("").icon(BitmapDescriptorFactory.fromBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.kongqi))));
                marker.setObject(dataBean);//将该点详情信息绑定到marker
                AirHodleModel model = new AirHodleModel(markerOptions,dataBean);
                airMarksMap.put(dataBean.getId(),model);
                curMarkers.add(marker);
            }

        } else {
            Toast.makeText(getActivity(), "暂无请求到相应数据！", Toast.LENGTH_SHORT).show();
        }

    }
    private void updateAirMap(List list){
        clearMarker();
        for(int i=0;i<list.size();i++){
            GasSite.DataBeanX.DataBean bean = (GasSite.DataBeanX.DataBean) list.get(i);
            AirHodleModel model = (AirHodleModel) airMarksMap.get(bean.getId());
            Marker marker = aMap.addMarker(model.getOptions());
            marker.setObject(model.getBean());
            curMarkers.add(marker);
        }
    }



    private void refreshGasInfo(GasSite.DataBeanX.DataBean data,String deviceId, final TextView content1, final TextView content2) {
        StringBuilder sb1 = new StringBuilder();
        content1.setText(sb1);
        StringBuilder sb2 = new StringBuilder();
        content2.setText(sb2);
        if (!deviceId.substring(0, 1).equals("T")) {
            content1.append("首要污染物：" + data.getMost() + "\n");
            content1.append("pm10：" + (-1 == data.getPm10() ? "无" : data.getPm10()) + "\n");
            content1.append("pm25：" + (-1 == data.getPm25() ? "无" : data.getPm25()) + "\n");
        }else {
            content1.append("aqi：" + (-1 == data.getAqi() ? "无" : data.getAqi()) + "\n");
            content2.append("首要污染物：" + data.getMost() + "\n");
            content1.append("co：" + (-1 == data.getCo() ? "无" : data.getCo()) + "\n");
            content2.append("no2：" + (-1 == data.getNo2() ? "无" : data.getNo2()) + "\n");
            content1.append("o3：" + (-1 == data.getO3() ? "无" : data.getO3()) + "\n");
            content2.append("pm10：" + (-1 == data.getPm10() ? "无" : data.getPm10()) + "\n");
            content1.append("pm25：" + (-1 == data.getPm25() ? "无" : data.getPm25()) + "\n");
            content2.append("so2：" + (-1 == data.getSo2() ? "无" : data.getSo2()) + "\n");
        }
    }

    //刷新Gas信息窗信息
    private void refreshGasInfo(String deviceId, final TextView content1, final TextView content2) {
        String url = PathManager.SingleHistory;
        Map<String, String> jo = new HashMap<>();
        String nonce = SignUtil.getNonce();
        String timestamp = new Date().getTime() + "";
        jo.put("timestamp", timestamp);
        jo.put("nonce", nonce);
        jo.put("signature", SignUtil.getSignature2(timestamp, nonce));
        jo.put("st", DateUtils.getToday());
        jo.put("et", DateUtils.getToday());
        jo.put("deviceId", deviceId);
        OkHttpUtils
                .post()//
                .url(url)//
                .params(jo)
                .build()//
                .execute(new StringCallback() {
                    @Override
                    public void onBefore(Request request, int id) {
                        super.onBefore(request, id);
                    }

                    @Override
                    public void onAfter(int id) {
                        super.onAfter(id);
                    }

                    @Override
                    public void onError(Call call, Exception e, int id) {
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        AirSingle airSite = (AirSingle) JsonUtil.jsonToBean(response, AirSingle.class);
                        if (0 == airSite.getRet()) {
                            List<AirSingle.DataBean> list = airSite.getData();
                            AirSingle.DataBean bean = list.get(list.size() - 1);
                            StringBuilder sb1 = new StringBuilder();
                            content1.setText(sb1);
                            StringBuilder sb2 = new StringBuilder();
                            content2.setText(sb2);
                            if (!bean.getDeviceid().substring(0, 1).equals("T")) {
                                content1.append("首要污染物：" + bean.getMost() + "\n");
                                content1.append("pm10：" + (-1 == bean.getPm10() ? "无" : bean.getPm10()) + "\n");
                                content1.append("pm25：" + (-1 == bean.getPm25() ? "无" : bean.getPm25()) + "\n");
                            }else {
                                content1.append("aqi：" + (-1 == bean.getAqi() ? "无" : bean.getAqi()) + "\n");
                                content2.append("首要污染物：" + bean.getMost() + "\n");
                                content1.append("co：" + (-1 == bean.getCo() ? "无" : bean.getCo()) + "\n");
                                content2.append("no2：" + (-1 == bean.getNo2() ? "无" : bean.getNo2()) + "\n");
                                content1.append("o3：" + (-1 == bean.getO3() ? "无" : bean.getO3()) + "\n");
                                content2.append("pm10：" + (-1 == bean.getPm10() ? "无" : bean.getPm10()) + "\n");
                                content1.append("pm25：" + (-1 == bean.getPm25() ? "无" : bean.getPm25()) + "\n");
                                content2.append("so2：" + (-1 == bean.getSo2() ? "无" : bean.getSo2()) + "\n");
                            }
                        }
                    }
                });
    }

    @Override
    public View getInfoWindow(Marker marker) {
        View infoWindow = View.inflate(getActivity(), R.layout.custom_infowindow, null);
        Object object = marker.getObject();
        if (object.getClass() == MapSource.class) {//判断是污染源marker
            MapSource mapSource = (MapSource) object;
            TextView entName = (TextView) infoWindow.findViewById(R.id.ent_name);
            entName.setText(mapSource.getEntName());
            StringBuilder sb1 = new StringBuilder();
            TextView tvContent1 = (TextView) infoWindow.findViewById(R.id.tv_content1);
            tvContent1.setText(sb1);
            StringBuilder sb2 = new StringBuilder();
            TextView tvContent2 = (TextView) infoWindow.findViewById(R.id.tv_content2);
            tvContent2.setText(sb2);
            if (mapSource.getWaterOutPut().size() != 0) {
                for (int i = 0; i < mapSource.getWaterOutPut().size(); i++) {
                    MapSource.WaterOutPutBean wateroutOutBean = mapSource.getWaterOutPut().get(i);
                    for (int j = 0; j < wateroutOutBean.getPollutants().size(); j = j + 2) {
                        tvContent1.append(wateroutOutBean.getPollutants().get(j).getPollutantName() + "：" +
                                wateroutOutBean.getPollutants().get(j).getStrength() + " ");
                        tvContent1.append("\n");
                        if (j + 1 < wateroutOutBean.getPollutants().size()) {
                            tvContent2.append(wateroutOutBean.getPollutants().get(j + 1).getPollutantName() + "：" +
                                    wateroutOutBean.getPollutants().get(j + 1).getStrength() + " ");
                            tvContent2.append("\n");
                        }
                    }
                }
            }
            if (mapSource.getGasOutput().size() != 0) {
                for (int i = 0; i < mapSource.getGasOutput().size(); i++) {
                    MapSource.GasOutputBean gasOutBean = mapSource.getGasOutput().get(i);
                    for (int j = 0; j < gasOutBean.getPollutants().size(); j = j + 2) {
                        tvContent1.append(gasOutBean.getPollutants().get(j).getPollutantName() + ": " +
                                gasOutBean.getPollutants().get(j).getStrength() + " ");
                        tvContent1.append("\n");
                        if (j + 1 < gasOutBean.getPollutants().size()) {
                            tvContent2.append(gasOutBean.getPollutants().get(j + 1).getPollutantName() + ": " +
                                    gasOutBean.getPollutants().get(j + 1).getStrength() + " ");
                            tvContent2.append("\n");
                        }
                    }

                }

            }

        }
        if (object.getClass() == GasSite.DataBeanX.DataBean.class) {//判断是gas站点的marker
            GasSite.DataBeanX.DataBean gas = (GasSite.DataBeanX.DataBean) object;
            TextView entName = (TextView) infoWindow.findViewById(R.id.ent_name);
            entName.setText(gas.getName().replaceAll("\\d+", "").trim());
            TextView tvContent1 = (TextView) infoWindow.findViewById(R.id.tv_content1);
            TextView tvContent2 = (TextView) infoWindow.findViewById(R.id.tv_content2);
            refreshGasInfo(gas,gas.getId(), tvContent1, tvContent2);
        }
        if(object.getClass() == MapYouYan.class){
            MapYouYan model = (MapYouYan) object;
            TextView entName = (TextView) infoWindow.findViewById(R.id.ent_name);
            entName.setText(model.getEntName());
            StringBuilder sb1 = new StringBuilder();
            TextView tvContent1 = (TextView) infoWindow.findViewById(R.id.tv_content1);
            tvContent1.setText(sb1);
            MapYouYan.DataBean bean = null;
            try {
                 bean = model.getData().get(0);
            }catch (Exception e){
                Log.i("Lybin",e.toString()+"---"+this.getClass().getName());
            }
            if (bean!=null && bean.getPollutants().size()> 0) {
                List pollutants = bean.getPollutants();
                for (int i = 0; i < pollutants.size(); i++) {
                    MapYouYan.DataBean.PollutantsBean _Bean = ( MapYouYan.DataBean.PollutantsBean) pollutants.get(i);
                        tvContent1.append(_Bean.getPollutantName() + "：" +
                                _Bean.getStrength() + " ");
                        if(i!=pollutants.size()-1)
                            tvContent1.append("\n");
                }
            }else{
                    tvContent1.append("暂无数据");
            }

        }
        return infoWindow;
    }

    //与上面的方法功能相同，暂时没有用到
    @Override
    public View getInfoContents(Marker marker) {
        return null;
    }

    AMap.OnInfoWindowClickListener listener = new AMap.OnInfoWindowClickListener() {

        @Override
        public void onInfoWindowClick(Marker marker) {

            // arg0.setTitle("infowindow clicked");
//            Toast.makeText(getActivity(), marker.getTitle(), Toast.LENGTH_SHORT).show();
            if (marker.getObject().getClass() == GasSite.DataBeanX.DataBean.class) {
                Intent intent = new Intent(getActivity(), newAirCurActivity.class);
                GasSite.DataBeanX.DataBean dataBean = (GasSite.DataBeanX.DataBean) marker.getObject();
                intent.putExtra("Deviceid", dataBean.getId());
                intent.putExtra("DeviceName", dataBean.getName());
                startActivity(intent);
            }
            if (marker.getObject().getClass() == MapYouYan.class) {
                MapYouYan bean = (MapYouYan) marker.getObject();
                Intent intent = new Intent(getActivity(), YouYanDetailActivity.class);
                EntModel.EnterpriseBean model = new EntModel.EnterpriseBean();
                model.setEntCode(Integer.parseInt(bean.getEntCode()));
                model.setEntName(bean.getEntName());
                intent.putExtra("data", model);
                startActivity(intent);
            }
            if (marker.getObject().getClass() == MapSource.class) {
                Intent intent = new Intent(getActivity(), CompanyPollutionDetailActivity.class);
                MapSource dataBean = (MapSource) marker.getObject();
                EntModel.EnterpriseBean enterpriseBean = new EntModel.EnterpriseBean();
                enterpriseBean.setEntCode((int) dataBean.getEntCode());
                enterpriseBean.setEntName(dataBean.getEntName());
                intent.putExtra("data", enterpriseBean);
                startActivity(intent);
            }
        }
    };

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onResume() {
        super.onResume();
        //在activity执行onResume时执行mMapView.onResume ()，实现地图生命周期管理
        mapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView.onPause ()，实现地图生命周期管理
        mapView.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
        mLocationClient.stopLocation();//停止定位
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理
        OkHttpUtils.getInstance().cancelTag(this);
        mapView.onDestroy();
        mLocationClient.onDestroy();//销毁定位客户端。
        OkHttpUtils.getInstance().cancelTag(this);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //在activity执行onSaveInstanceState时执行mMapView.onSaveInstanceState (outState)，实现地图生命周期管理
        mapView.onSaveInstanceState(outState);
    }
    public void cancelDialog(){
        if(yutuLoading!=null&&yutuLoading.isShow())
            yutuLoading.dismissDialog();
    }
    //通过radiogroup给radiobutton设置监听，radiobutton不能重复选取，所以单独给radiobutton设置监听
    YutuLoading yutuLoading;
    @Override
    public void onClick(View view) {
//        if(yutuLoading==null)
//            yutuLoading = new YutuLoading(getActivity());
        if (groundoverlay!=null){
            groundoverlay.setVisible(false);
        }
        switch (view.getId()) {
            case R.id.rb_map:
                if(btnStreet.getVisibility()==View.VISIBLE){
                    btnStreet.setVisibility(View.GONE);
                }
                //加载网络热力图
                addOverlayToMap();
                break;
            case R.id.rb_yan:
//                yutuLoading.showDialog();
                initPosition();
                if(btnStreet.getVisibility()==View.VISIBLE){
                    btnStreet.setVisibility(View.GONE);
                }
                break;
            case R.id.rb_air:
//                yutuLoading.showDialog();
                initGasPoints();
                if(btnStreet.getVisibility()!=View.VISIBLE){
                    btnStreet.setVisibility(View.VISIBLE);
                    btnStreet.setOnClickListener(this);
                }
                break;
            case R.id.rb_source:
                initSourcePoints();
                if(btnStreet.getVisibility()==View.VISIBLE){
                    btnStreet.setVisibility(View.GONE);
                }
                break;
//            case rb_chazhao:
//
//                break;
            case R.id.rb_location:
                if (requestPermission(getContext())){
                    setUpMap();
                }
                if(btnStreet.getVisibility()==View.VISIBLE){
                    btnStreet.setVisibility(View.GONE);
                }
                break;
            case  R.id.btn_street:
                onAddress2Picker();
                break;
//            case rb_clear://清空地图上的标记点位
//                aMap.clear();
//                break;
            default:
        }
    }


    @Override
    public boolean onMarkerClick(Marker marker) {
        currentMarker = marker;
        return false;
    }

    @Override
    public void onMapClick(LatLng latLng) {
        if (currentMarker.isInfoWindowShown()) {
            currentMarker.hideInfoWindow();//这个是隐藏infowindow窗口的方法
        }
    }

    //获取自定义marker布局
    protected View getMyMarkerView(int resId, String pm_val) {
        View view = View.inflate(getActivity(), R.layout.map_marker, null);
        ImageView iv = (ImageView) view.findViewById(R.id.iv);
        TextView tv_val = (TextView) view.findViewById(R.id.marker_tv_val);
        iv.setImageResource(resId);
        tv_val.setText(pm_val);
        return view;
    }



   /* @Override
    public void onCameraChangeFinish(CameraPosition cameraPosition) {
        Log.i("aaa",cameraPosition.zoom+"");
       *//* if (rb_air.isChecked()) {
//            14.9
            if (cameraPosition.zoom < 14) {
                for (int i=0;i<curMarkers.size();i++) {
                    Marker marker  = curMarkers.get(i);
                    int icon = PollutionLevelCalUtil.getLevelIconForMap("pm25",gass.get(i).getPm25());
                    marker.setIcon(BitmapDescriptorFactory.fromBitmap(BitmapFactory
                            .decodeResource(getResources(), icon)));
                }
            } else {
                for (Marker marker : curMarkers) {
                    GasSite.DataBeanX.DataBean dataBean= (GasSite.DataBeanX.DataBean) marker.getObject();
                    marker.setIcon(BitmapDescriptorFactory.fromView(getMyMarkerView(R.mipmap.level_a,
                            dataBean.getName().replaceAll("\\d+", "").trim())));
                }
            }
        }*//*
        if (rb_source.isChecked()) {
//            12.5
            if (cameraPosition.zoom < 13) {
                for (Marker marker : curMarkers) {
                    marker.setIcon(BitmapDescriptorFactory.fromBitmap(BitmapFactory
                            .decodeResource(getResources(), R.mipmap.wuranyuana_marker)));
                }
            } else {
                for (Marker marker : curMarkers) {
                    MapSource source= (MapSource) marker.getObject();
                    marker.setIcon(BitmapDescriptorFactory.fromView(getMyMarkerView(R.mipmap.wuranyuana_marker, source.getEntName())));
                }
            }
        }
    }*/
    private double getDouble(Double d){
        DecimalFormat decimalFormat=new DecimalFormat();
        decimalFormat.setMaximumFractionDigits(6);
        decimalFormat.setRoundingMode(RoundingMode.UP);
        String format = decimalFormat.format(d*0.37);
        double v = Double.parseDouble(format);

        return v;
    }
    //清除Marker
    private void clearMarker(){
        if (curMarkers.size()>0){
            for (int i=0;i<curMarkers.size();i++){
                curMarkers.get(i).remove();
            }
        }
    }
    //新增镇街选择按键 筛选数据
    public void onAddress2Picker() {
        try {
//            ArrayList<Province> data = new ArrayList<>();
//            String json = ConvertUtils.toString(getActivity().getAssets().open("city2.json"));
//            data.addAll(JSON.parseArray(json, Province.class));
            ArrayList data = DataApplication.streetBean.getProvinces();
            AddressPicker picker = new AddressPicker(getActivity(), data);
            picker.setShadowVisible(true);
            //picker.setTextSizeAutoFit(false);
            picker.setHideProvince(true);
//            picker.setSelectedItem("贵州", "贵阳", "花溪");
            picker.setOnAddressPickListener(new AddressPicker.OnAddressPickListener() {
                @Override
                public void onAddressPicked(Province province, City city, County county) {
//                    Toast.makeText(getActivity(), "province : " + province + ", city: " + city + ", county: " + county, Toast.LENGTH_SHORT).show();
                    //showToast("province : " + province + ", city: " + city + ", county: " + county);
                    updateAirMap(getStationByStreet(city.getAreaId(),county.getAreaId()));
                    btnStreet.setText(city.getAreaName());
                }
            });
            picker.show();
        } catch (Exception e) {
            Toast.makeText(getActivity(), e.toString(), Toast.LENGTH_SHORT).show();
        }
    }
    //市区专分类
    public List reverseStation(List data){
        List _Data  = data;
// GasSite.DataBeanX.DataBean
        List city = new ArrayList();
        List area = new ArrayList();
        List special = new ArrayList();
        for(int i=0;i<_Data.size();i++){
            Object obj =null;
            String deviceid = "";
            obj = (GasSite.DataBeanX.DataBean) _Data.get(i);
            deviceid = ((GasSite.DataBeanX.DataBean)_Data.get(i)).getId().substring(0, 1);
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

        airSpecialMap.clear();
        airSpecialMap.put("city",city);
        airSpecialMap.put("area",area);
        airSpecialMap.put("special",special);
        return  _Data;
    }
    //街道下的站点数据
    private List getStationByStreet(String cityId,String deviceId){
        Map map = DataApplication.streetBean.getStreetMap();
        StreetBean.DataBean bean= (StreetBean.DataBean) map.get(cityId);
        List AirBean = new ArrayList();
        if(bean==null){
            if(deviceId.equals("C000000")){
                AirBean.addAll((List) airSpecialMap.get("city"));
                return AirBean;
            }else if(deviceId.equals("A000000")){
                AirBean.addAll((List) airSpecialMap.get("area"));
                return AirBean;
            }else if(deviceId.equals("S000000")){
                AirBean.addAll((List) airSpecialMap.get("special"));
                return AirBean;
            }else{
                for(Object dataBean:gass){
                    AirBean.add(dataBean);
                }
                return AirBean;
            }
        }
        AirHodleModel model = (AirHodleModel) airMarksMap.get(deviceId);
        if(model!=null){
            AirBean.add(model.getBean());
            return AirBean;
        }
        for(StreetBean.DataBean.DevicesBean device:bean.getDevices()){
            String id = device.getDeviceid();
            AirHodleModel _Model = (AirHodleModel) airMarksMap.get(id);
            AirBean.add(_Model.getBean());
        }
        return AirBean;
    }

    private class AirHodleModel{
        private  MarkerOptions options;
        private GasSite.DataBeanX.DataBean  bean;
        public AirHodleModel(MarkerOptions options, GasSite.DataBeanX.DataBean  bean){
            this.options = options;
            this.bean = bean;
        }
        public MarkerOptions getOptions() {
            return options;
        }

        public void setOptions(MarkerOptions options) {
            this.options = options;
        }

        public GasSite.DataBeanX.DataBean getBean() {
            return bean;
        }

        public void setBean(GasSite.DataBeanX.DataBean bean) {
            this.bean = bean;
        }


    }
}
