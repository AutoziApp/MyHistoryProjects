package com.mapuni.caremission_ens.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.amap.api.maps.AMap;
import com.amap.api.maps.AMapOptions;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.HeatmapTileProvider;
import com.amap.api.maps.model.LatLng;
import com.mapuni.caremission_ens.R;
import com.mapuni.caremission_ens.bean.MapBean;
import com.mapuni.caremission_ens.presenter.MapControl;
import com.mapuni.caremission_ens.utils.JsonUtil;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.Arrays;

import okhttp3.Call;


public class MapFragment extends BaseFragment {
    private AMap aMap;
    private MapView mapView;
    private MapControl mapControl;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        LinearLayout view = (LinearLayout) inflater.inflate(R.layout.fragment_map, container, false);
        setTitle(view,"地图");
        view.addView(initMap(savedInstanceState));
        mControl.requestForGis(call);
        return view;
    }

    private MapView initMap(Bundle savedInstanceState){
//       市政府
        LatLng _LatLng = new LatLng(30.294606,109.481553);
         final CameraPosition ens = new CameraPosition.Builder()
                .target(_LatLng).zoom(15).bearing(0).tilt(30).build();
        AMapOptions aOptions = new AMapOptions();
        aOptions.tiltGesturesEnabled(false);// 禁止通过手势倾斜地图

        aOptions.camera(ens);
        mapView = new MapView(mAct, aOptions);
        mapView.onCreate(savedInstanceState);
        LinearLayout.LayoutParams mParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        mapView.setLayoutParams(mParams);
//        mapView.onCreate(savedInstanceState);
        if(aMap==null)
            aMap = mapView.getMap();
        mapControl = new MapControl(mAct,aMap);
        aMap.setInfoWindowAdapter(mapControl);
//        aMap.getUiSettings().setZoomControlsEnabled(false);//隐藏缩放按钮
        return mapView;
    }
    public static MapFragment newInstance(String s) {
        MapFragment fragment = new MapFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * 方法必须重写
     */
    @Override
    public void onResume() {
        super.onResume();
        if(mapView!=null)
            mapView.onResume();
    }


    /**
     * 方法必须重写
     */
    @Override
    public void onPause() {
        super.onPause();
        if(mapView!=null){
            mapView.onPause();
        }
    }

    /**
     * 方法必须重写
     */
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    /**
     * 方法必须重写
     */
    @Override
    public void onDestroy() {
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
