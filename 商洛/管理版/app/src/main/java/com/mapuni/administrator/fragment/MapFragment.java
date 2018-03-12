package com.mapuni.administrator.fragment;


import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.andview.refreshview.XRefreshView;
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
import com.mapuni.administrator.R;
import com.mapuni.administrator.activity.myNewsAc.XcsbActivity;
import com.mapuni.administrator.activity.myNewsAc.XflcActivity;
import com.mapuni.administrator.activity.myNewsAc.XfshListActivity;
import com.mapuni.administrator.app.MyApplication;
import com.mapuni.administrator.bean.PatrolObject;
import com.mapuni.administrator.bean.TaskGeneral;
import com.mapuni.administrator.divider.DividerItemDecoration;
import com.mapuni.administrator.itemFactory.TaskGeneralRecyclerItemFactory;
import com.mapuni.administrator.manager.MessageEvent;
import com.mapuni.administrator.manager.NetManager;
import com.mapuni.administrator.manager.PathManager;
import com.mapuni.administrator.utils.DensityUtils;
import com.mapuni.administrator.utils.Logger;
import com.mapuni.administrator.utils.SPUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import me.xiaopan.assemblyadapter.AssemblyRecyclerAdapter;
import okhttp3.Call;

/**
 * Created by Administrator on 2017/8/11.
 * 巡查
 */

public class MapFragment extends Fragment implements View.OnClickListener{
    private View view;
    private PopupWindow popup;
    private Context context;
    private LinearLayout xfshLayout;

    //arcgis地图
    private com.esri.android.map.MapView mMapView;
    private GraphicsLayer layer;

    //popwindow控件
    XRefreshView xRefreshView;
    private AssemblyRecyclerAdapter adapter;//万能适配器
    private RecyclerView recyclerView;
    private TextView mTvNoData;

    List<Object> dataList = new ArrayList<Object>();//数据源
    String sessionId;
    String rows="10";
    int page=1;
    private boolean isLoadMore = false;

    public static MapFragment newInstance(String s) {
        MapFragment fragment = new MapFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState) {
        this.context=getActivity();
        view = inflater.inflate(R.layout.fragment_map, container, false);
        initMap(view);
        initView(view);
        initPopupWindow();
        requestPatrolObject();
        EventBus.getDefault().register(this);
        showOrHindSH();
        return view;
    }

    private void showOrHindSH() {
        String roleId= (String) SPUtils.getSp(getActivity(),"roleId","");
               if(roleId.contains("3")){
                   xfshLayout.setVisibility(View.VISIBLE);
               }else {
                   xfshLayout.setVisibility(View.GONE);
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
        sessionId = (String) SPUtils.getSp(getActivity(), "sessionId", "");
        LinearLayout rwglLayout= (LinearLayout) view.findViewById(R.id.ll_rwgl);
        rwglLayout.setOnClickListener(this);
        LinearLayout xfglLayout= (LinearLayout) view.findViewById(R.id.ll_xflc);
        xfglLayout.setOnClickListener(this);
        LinearLayout xcsbLayout= (LinearLayout) view.findViewById(R.id.ll_xcsb);
        xcsbLayout.setOnClickListener(this);
        xfshLayout= (LinearLayout) view.findViewById(R.id.ll_xfsh);
        xfshLayout.setOnClickListener(this);


    }
    private void initPopupWindow() {
        View v = getActivity().getLayoutInflater().inflate(
                R.layout.popwindow_layout, null);
        popup = new PopupWindow(v, ViewGroup.LayoutParams.MATCH_PARENT, 600, true);
        popup.setFocusable(true);
        //该属性设置为true则你在点击屏幕的空白位置也会退出
        popup.setTouchable(true);
        popup.setBackgroundDrawable(new BitmapDrawable());
        popup.setOutsideTouchable(true);
        popup.setAnimationStyle(R.style.PopupAnimation);

        xRefreshView = (XRefreshView) v.findViewById(R.id.xrefreshview);
        recyclerView = (RecyclerView) v.findViewById(R.id.recyclerView);
        mTvNoData = (TextView) v.findViewById(R.id.tv_noData);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL_LIST));
        adapter = new AssemblyRecyclerAdapter(dataList);
        adapter.addItemFactory(new TaskGeneralRecyclerItemFactory(getActivity()));
        recyclerView.setAdapter(adapter);
        //设置刷新完成以后，headerview固定的时间
        xRefreshView.setPinnedTime(1000);
        xRefreshView.setMoveForHorizontal(true);//解决横向移动冲突
        xRefreshView.setPullLoadEnable(true);
        xRefreshView.setAutoLoadMore(false);
        xRefreshView.enableReleaseToLoadMore(true);
        xRefreshView.enableRecyclerViewPullUp(true);
        xRefreshView.enablePullUpWhenLoadCompleted(true);
        xRefreshView.setXRefreshViewListener(new XRefreshView.SimpleXRefreshListener(){
            @Override
            public void onRefresh(boolean isPullDown) {//下拉刷新
                super.onRefresh(isPullDown);
                isLoadMore = false;
                page = 1;
                loadData();
                xRefreshView.stopRefresh();
            }


            @Override
            public void onLoadMore(boolean isSilence) {//上拉加载
                super.onLoadMore(isSilence);
                isLoadMore = true;
                page++;
                loadData();
                xRefreshView.stopLoadMore(true);
            }
        });
        loadData();
    }

    List<PatrolObject> mPatrolObjects = new ArrayList<>();

    private Callout callout;            //气泡的展示方法

    private void requestPatrolObject() {
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



    private void loadData() {

        NetManager.requestDbTask(sessionId,rows,page+"", new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                Logger.e(MapFragment.class.getSimpleName(),e.toString());
                Toast.makeText(getActivity(), "请求数据失败", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(String response, int id) {
               Logger.i(MapFragment.class.getSimpleName(),response.toString());
                mTvNoData.setVisibility(View.GONE);
                xRefreshView.setVisibility(View.VISIBLE);
                Gson gson=new Gson();
                Type type = new TypeToken<ArrayList<TaskGeneral>>() {}.getType();
                try {
                    List<TaskGeneral> list=gson.fromJson(response.toString(),type);
                    if (!isLoadMore){
                        if (list!=null&&list.size()>0){
                            adapter.setDataList(list);
                        }else{
                            adapter.setDataList(null);
                            mTvNoData.setVisibility(View.VISIBLE);
//                        xRefreshView.setVisibility(View.GONE);
                        }
                    }else{
                        adapter.addAll(list);
                    }
                }catch (Exception e){
                    Toast.makeText(getActivity(), "请求数据失败", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ll_rwgl:
                popup.showAtLocation(view,Gravity.BOTTOM,0, DensityUtils.dip2px(getActivity(),52)+1+DensityUtils.getVirtualBarHeigh(getActivity()));
                break;
            case R.id.ll_xcsb:
                startActivity(new Intent(getActivity(), XcsbActivity.class));
                getActivity().overridePendingTransition(R.anim.activity_enter_anim, R.anim.activity_exit_anim);

                break;
            case R.id.ll_xflc:
                startActivity(new Intent(getActivity(), XflcActivity.class));
                getActivity().overridePendingTransition(R.anim.activity_enter_anim, R.anim.activity_exit_anim);
                break;
            case R.id.ll_xfsh:
                startActivity(new Intent(getActivity(), XfshListActivity.class));
                getActivity().overridePendingTransition(R.anim.activity_enter_anim, R.anim.activity_exit_anim);
                break;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);
    }
    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(context);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
        public void onEventMainThread(MessageEvent event) {
//        Toast.makeText(context,"",Toast.LENGTH_SHORT).show();
        String message = event.getMessage();
        if (message.equals("success")){
            isLoadMore = false;
            page = 1;
            loadData();
            xRefreshView.stopRefresh();
        }
    }
}
