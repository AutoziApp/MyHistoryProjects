package com.yutu.car.presenter;

import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.amap.api.maps.AMap;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.PolylineOptions;
import com.yutu.car.R;
import com.yutu.car.bean.MapBean;

import java.util.List;

/**
 * Created by yawei on 2017/3/29.
 */

public class MapControl implements AMap.InfoWindowAdapter {
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

    @Override
    public View getInfoContents(Marker marker) {
        return null;
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            List<LatLng> latLngs = (List<LatLng>) msg.obj;
            
            aMap.addPolyline(new PolylineOptions().
                    addAll(latLngs).width(30).color(Color.argb(255, 190, 32, 32)));
        }
    };

    public void lineForStreet(final List<List<LatLng>> list) {

        new Thread() {
            @Override
            public void run() {
                super.run();
                for (List<LatLng> latLngs : list) {
                    
                    Message msg = Message.obtain();
                    msg.obj = latLngs;
                    mHandler.sendMessage(msg);
                }
            }
        }.start();

    }
}
