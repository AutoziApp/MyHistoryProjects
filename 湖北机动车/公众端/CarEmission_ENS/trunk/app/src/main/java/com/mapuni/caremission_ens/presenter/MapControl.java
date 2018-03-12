package com.mapuni.caremission_ens.presenter;

import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.amap.api.maps.AMap;
import com.amap.api.maps.CoordinateConverter;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.PolylineOptions;
import com.mapuni.caremission_ens.R;
import com.mapuni.caremission_ens.bean.MapBean;

import java.util.ArrayList;
import java.util.List;

import static com.zhy.http.okhttp.log.LoggerInterceptor.TAG;


public class MapControl implements AMap.InfoWindowAdapter {
    private static final String TAG = "MapControl";

    AMap aMap;
    Context context;
    View infoWindow;

    public MapControl(Context context, AMap aMap) {
        this.aMap = aMap;
        this.context = context;
    }

    public void signMarker(List<MapBean.StationInfoBean> list) {
        for (MapBean.StationInfoBean bean : list) {
            LatLng a = new LatLng(bean.getLATITUDE(), bean.getLONGITUDE());
            String info = "当前状态: " + bean.getISOPEN() + "\n\n联系电话: " + bean.getFZRPHONE();
            setMarker(bean.getSTATIONNAME(), info, a);
        }
    }

    private void setMarker(String title, String info, LatLng latLng) {
        MarkerOptions markerOption = new MarkerOptions();
        markerOption.position(latLng);
        markerOption.title(info);
        markerOption.draggable(false);//设置Marker可拖动
        markerOption.icon(BitmapDescriptorFactory.fromView(getMarkerView(R.mipmap.swt_icon, title)));
        // 将Marker设置为贴地显示，可以双指下拉地图查看效果
        markerOption.setFlat(false);//设置marker平贴地图效果
        aMap.addMarker(markerOption);
    }

    //获取自定义marker布局
    protected View getMarkerView(int resId, String pm_val) {
        View view = View.inflate(context, R.layout.map_marker, null);
        ImageView iv = (ImageView) view.findViewById(R.id.iv);
        TextView tv_val = (TextView) view.findViewById(R.id.marker_tv_val);
        iv.setImageResource(resId);
        tv_val.setText(pm_val);
        return view;
    }

    @Override
    public View getInfoWindow(Marker marker) {
        if (infoWindow == null) {
            infoWindow = LayoutInflater.from(context).inflate(
                    R.layout.custom_info_window, null);
        }
        TextView title = (TextView) infoWindow.findViewById(R.id.title);
        title.setText(marker.getTitle());
//        render(marker, infoWindow);
        return infoWindow;
    }

    public LatLng changeLatLng(LatLng sourceLatLng) {
        CoordinateConverter converter = new CoordinateConverter(context);
// CoordType.GPS 待转换坐标类型
        converter.from(CoordinateConverter.CoordType.GPS);
// sourceLatLng待转换坐标点 LatLng类型
        converter = converter.coord(sourceLatLng);
// 执行转换操作
        LatLng desLatLng = converter.convert();
        return desLatLng;
    }

    @Override
    public View getInfoContents(Marker marker) {
        return null;
    }


    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            List<LatLng> _Latlngs = (List<LatLng>) msg.obj;
            aMap.addPolyline(new PolylineOptions().
                    addAll(_Latlngs).width(30).color(Color.argb(255, 190, 32, 32)));
        }
    };

    public void lineForStreet(final List<List<LatLng>> list) {
        long startTime = System.currentTimeMillis();
        Log.i(TAG, "startTime: " + startTime);
        new Thread() {
            @Override
            public void run() {
                super.run();
                for (List<LatLng> latLngs : list) {
                    final List<LatLng> _Latlngs = new ArrayList<>();
                    for (LatLng latLng : latLngs) {
                        _Latlngs.add(changeLatLng(latLng));
                    }
                    Message msg = Message.obtain();
                    msg.obj = _Latlngs;
                    mHandler.sendMessage(msg);
                }
            }
        }.start();
        long endTime = System.currentTimeMillis();
        Log.i(TAG, "endTime: " + endTime);
        Log.i(TAG, "d: " + (endTime - startTime));
    }
}
