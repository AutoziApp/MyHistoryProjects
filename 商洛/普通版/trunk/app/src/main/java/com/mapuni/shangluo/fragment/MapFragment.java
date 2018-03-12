package com.mapuni.shangluo.fragment;


import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.esri.android.map.Callout;
import com.esri.android.map.GraphicsLayer;
import com.esri.android.map.ags.ArcGISTiledMapServiceLayer;
import com.esri.android.map.event.OnSingleTapListener;
import com.esri.core.geometry.GeometryEngine;
import com.esri.core.geometry.Point;
import com.esri.core.geometry.SpatialReference;
import com.esri.core.map.Graphic;
import com.esri.core.symbol.PictureMarkerSymbol;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mapuni.shangluo.R;
import com.mapuni.shangluo.activity.xcAc.QianDaoActivity;
import com.mapuni.shangluo.activity.xcAc.XcsbActivity;
import com.mapuni.shangluo.app.MyApplication;
import com.mapuni.shangluo.bean.PatrolObject;
import com.mapuni.shangluo.manager.NetManager;
import com.mapuni.shangluo.manager.PathManager;
import com.mapuni.shangluo.utils.SPUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.Call;

/**
 * Created by Administrator on 2017/8/11.
 */

public class MapFragment extends Fragment implements View.OnClickListener{

    private ImageView ivXcsb,ivQiandao;
    private com.esri.android.map.MapView mMapView;
    private GraphicsLayer layer;
    private Callout callout;            //气泡的展示方法

    List<PatrolObject> mPatrolObjects = new ArrayList<>();

    public static MapFragment newInstance(String s) {
        MapFragment fragment = new MapFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState) {
        
        View view = inflater.inflate(R.layout.fragment_map, container, false);
        initMap(view);
        initView(view);
        requestPatrolObject();
        return view;
    }

    private void requestPatrolObject() {
        String sessionId=(String) SPUtils.getSp(getActivity(), "sessionId", "");
        NetManager.requestPotrolObject(sessionId, new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
//                Log.i("aaa", "获取监管对象失败" + e.getMessage());
                Toast.makeText(getActivity(), "获取监管对象失败" + e.getMessage(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(String response, int id) {
                Log.i("aaa", "获取监管对象成功" + response.toString());
                Gson gson = new Gson();
                Type type = new TypeToken<ArrayList<PatrolObject>>() {}.getType();
                try {
                    mPatrolObjects = gson.fromJson(response.toString(), type);
                    showPatrolObjectmarkers(mPatrolObjects);
                    mMapView.setOnSingleTapListener(new OnSingleTapListener() {
                        public void onSingleTap(float v, float v1) {
                            SelectOneGraphic(v,v1);

                        }
                    });
                }catch (Exception e){
                    Toast.makeText(getActivity(), "获取监管对象失败" + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    //设置标记点击事件
    private void SelectOneGraphic(float x, float y) {
        if (layer != null && layer.isInitialized() && layer.isVisible()) {
            Graphic result = null;
            // 检索当前 光标点（手指按压位置）的附近的 graphic对象
            result = GetGraphicsFromLayer(x, y, layer);
            if (result != null) {
                // 获得附加特别的属性
                final String name = (String) result
                        .getAttributeValue("name");
                // 显示提示
                callout(mMapView.toMapPoint(x, y), result);
            }else if (callout!=null){
                callout.animatedHide();
            }
        }
    }

    /**
     * 添加标注信息
     *点击弹出详情信息
     * @param pt
     *            标注的点
     *  Graphic
     *            标注的内容
     */
    public void callout(final Point pt,Graphic result) {
        LayoutInflater inflater;
        callout = mMapView.getCallout();
        callout.setCoordinates(pt);
        inflater = LayoutInflater.from(getActivity());
        View inflate = inflater.inflate(R.layout.map_callout, null);
        TextView tv = (TextView) inflate.findViewById(R.id.tv);
            String content= (String) result.getAttributeValue("name");
            tv.setText(content);
        callout.setMaxWidth(1200);
        callout.setMaxHeight(300);
        callout.setContent(inflate);
        callout.show();
    }
    /*
 * 从一个图层里里 查找获得 Graphics对象. x,y是屏幕坐标,layer
 * 是GraphicsLayer目标图层（要查找的）。相差的距离是50像素。
 */
    private Graphic GetGraphicsFromLayer(double xScreen, double yScreen,
                                         GraphicsLayer layer) {
        Graphic result = null;
        try {
            int[] idsArr = layer.getGraphicIDs();
            double x = xScreen;
            double y = yScreen;
            for (int i = 0; i < idsArr.length; i++) {
                Graphic gpVar = layer.getGraphic(idsArr[i]);
                if (gpVar != null) {
                    Point pointVar = (Point) gpVar.getGeometry();
                    pointVar = mMapView.toScreenPoint(pointVar);
                    double x1 = pointVar.getX();
                    double y1 = pointVar.getY();
                    if (Math.sqrt((x - x1) * (x - x1) + (y - y1) * (y - y1)) < 20) {
                        result = gpVar;
                        break;
                    }
                }
            }
        } catch (Exception e) {
            return null;
        }
        return result;
    }
    //展示空气质量marker
    private void showPatrolObjectmarkers(List<PatrolObject> patrolObjects) {
        List<Point> objPoints=new ArrayList<>();
        List<HashMap<String,Object>> objMsgs=new ArrayList<>();
        if (patrolObjects!=null){
            for (PatrolObject object:patrolObjects){
                if(TextUtils.isEmpty(object.getLongitude())||TextUtils.isEmpty(object.getLatitude())){
                    break;
                }
                Point point=new Point(Double.parseDouble(object.getLongitude()),Double.parseDouble(object.getLatitude()));
                        objPoints.add(point);
                HashMap<String,Object> map=new HashMap<>();
//                map.put("uuid",msg.getAQILEVELNAME());      //AQI等级  一 二
                map.put("name",object.getName());  //监管对象名称
//                map.put("supervisionTypeName",msg.getAQIPRIMPOLLUTE());  //首要污染物
//                map.put("supervisionTypeUuid",msg.getPOINTNAME());            //测点名称
//                map.put("address",msg.getCODE_AQILEVEL());
//                map.put("createTime",msg.getAQI());                        //AQI数值
                objMsgs.add(map);
            }
        }
        addObjMakers(objPoints,objMsgs);
    }

    //添加空气质量marker
    public void addObjMakers(List<Point> myPoint, List<HashMap<String, Object>> objMsgs) {
        //双重循环 获取两个集合中有相同POINTCODE的值
        for (int i=0;i<myPoint.size();i++){
            Point point = myPoint.get(i);
            if (point==null){
                Toast.makeText(getActivity(),"经纬度数据为空",Toast.LENGTH_SHORT).show();
                return;
            }
            Graphic graphic=null;
                    Drawable drawable = MyApplication.getContextObject().getResources().getDrawable(R.drawable.marker);
                    PictureMarkerSymbol marker = new PictureMarkerSymbol(drawable);
                    graphic=new Graphic(point,marker,objMsgs.get(i));
            layer.addGraphic(graphic);
        }
    }

    private void initMap(View view) {
        mMapView= (com.esri.android.map.MapView) view.findViewById(R.id.map);
        layer = new GraphicsLayer();
//        mMapView.addLayer(new ArcGISTiledMapServiceLayer("http://113.200.60.90:6080/arcgis/rest/services/ShangluoImage/MapServer"));//影像图可用
        mMapView.addLayer(new ArcGISTiledMapServiceLayer(PathManager.mapPath));//
        mMapView.addLayer(layer);
        mMapView.enableWrapAround(true);
        mMapView.setEsriLogoVisible(false);
        mMapView.setScale(1000000);
        mMapView.setMapBackground(Color.WHITE,Color.WHITE,0,0);
        Point point = new Point(109.9404100000,33.8703600000);
        Point mapPoint = (Point) GeometryEngine.project(point , SpatialReference.create(4326),mMapView.getSpatialReference());
        mMapView.centerAt(mapPoint, true);

    }

    private void initView(View view) {
        ivXcsb= (ImageView) view.findViewById(R.id.iv_xcsb);
        ivQiandao= (ImageView) view.findViewById(R.id.iv_qiandao);
        ivQiandao.setOnClickListener(this);
        ivXcsb.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_xcsb:
                startActivity(new Intent(getActivity(), XcsbActivity.class));
                getActivity().overridePendingTransition(R.anim.activity_enter_anim, R.anim.activity_exit_anim);

                break;
            case R.id.iv_qiandao:
                startActivity(new Intent(getActivity(), QianDaoActivity.class));
                getActivity().overridePendingTransition(R.anim.activity_enter_anim, R.anim.activity_exit_anim);

                break;
        }
    }
}
