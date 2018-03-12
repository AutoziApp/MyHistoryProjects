package com.yutu.car.activity;

import android.os.PersistableBundle;
import android.os.Bundle;

import com.amap.api.maps.AMap;
import com.amap.api.maps.AMapOptions;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.LatLng;
import com.yutu.car.bean.MapBean;
import com.yutu.car.R;
import com.yutu.car.presenter.MapControl;
import com.yutu.car.presenter.NetControl;
import com.yutu.car.utils.JsonUtil;
import com.zhy.http.okhttp.callback.StringCallback;

import android.view.View;
import android.widget.LinearLayout;

import okhttp3.Call;

public class MapActivity extends BaseActivity{
    private AMap aMap;
    private MapView mapView;
    private MapControl mapControl;
    private NetControl mControl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_map);
        LinearLayout linearLayout=(LinearLayout)findViewById(R.id.fragment_map);
        linearLayout.addView(initMap(savedInstanceState));
        setTitle("地图",true,false);
        mControl=new NetControl();
        mControl.requestForGis(call);

    }
    private MapView initMap(Bundle savedInstanceState){
//       市政府
        LatLng _LatLng = new LatLng(30.294606,109.481553);
        final CameraPosition ens = new CameraPosition.Builder()
                .target(_LatLng).zoom(15).bearing(0).tilt(30).build();
        AMapOptions aOptions = new AMapOptions();
        aOptions.tiltGesturesEnabled(false);// 禁止通过手势倾斜地图

        aOptions.camera(ens);
        mapView = new MapView(this, aOptions);
        mapView.onCreate(savedInstanceState);
        LinearLayout.LayoutParams mParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        mapView.setLayoutParams(mParams);
        mapView.onCreate(savedInstanceState);
        if(aMap==null)
            aMap = mapView.getMap();
        mapControl = new MapControl(this,aMap);
        aMap.setInfoWindowAdapter(mapControl);
//        aMap.getUiSettings().setZoomControlsEnabled(false);//隐藏缩放按钮
        return mapView;
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()){
            case R.id.leftIcon:
                finish();
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(mapView!=null)
            mapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(mapView!=null){
            mapView.onPause();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mapView!=null)
            mapView.onDestroy();
    }

    private StringCallback call = new StringCallback() {
        @Override
        public void onError(Call call, Exception e, int id) {

        }

        @Override
        public void onResponse(String response, int id) {
            MapBean bean = (MapBean) JsonUtil.jsonToBean(response, MapBean.class);
            if(bean.getFlag()==1){
                mapControl.signMarker(bean.getStationInfo());
                mapControl.lineForStreet(bean.getLimitPoint());
            }
        }
    };

}
