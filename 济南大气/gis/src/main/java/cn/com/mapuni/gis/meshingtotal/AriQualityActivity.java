package cn.com.mapuni.gis.meshingtotal;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.tianditu.android.maps.GeoPoint;
import com.tianditu.android.maps.ItemizedOverlay;
import com.tianditu.android.maps.MapController;
import com.tianditu.android.maps.MapView;
import com.tianditu.android.maps.MapView.LayoutParams;
import com.tianditu.android.maps.MyLocationOverlay;
import com.tianditu.android.maps.Overlay;
import com.tianditu.android.maps.OverlayItem;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.json.JSONArray;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.List;

import cn.com.mapuni.gis.meshingtotal.model.AriQualityBean;
import cn.com.mapuni.gis.meshingtotal.tdt.BaseMapActivityTDT;
import cn.com.mapuni.gis.meshingtotal.tdt.BaseMapActivityTDT.ViewCallback;
import cn.com.mapuni.meshing.base.controls.loading.YutuLoading;
import cn.com.mapuni.meshing.base.interfaces.PathManager;

public class AriQualityActivity extends BaseMapActivityTDT implements
        ViewCallback, OnClickListener {
    private FrameLayout mView;
    private MapView mapView;
    private Context mContext;

    private TextView showHind;
    private LinearLayout news;
    private OverItemT mOverlay = null;
    public View mPopView = null;
    private ImageView ivClose;
    private TextView upload;
    private TextView biaoti1;
    private TextView biaoti2;
    private TextView biaoti3;
    private TextView biaoti4;
    private TextView biaoti5;
    private YutuLoading yutuLoading;
    List<AriQualityBean> data;
    private ListView listView;
    private GeoPoint point;
    private AriQualityBean bean;
    private Drawable markers;
    private Button leftButton, rightButton;
    @Override
    public void onCreate(FrameLayout view, MapView mapView) {
        this.mapView = mapView;
        this.mView = view;
        this.mContext = this;
        data = new ArrayList<AriQualityBean>();
        initData();

    }

    private void initData() {
        readRiskSources();
    }

    void initView() {
        mView.removeAllViews();
        // 查询条件
        View mainView = LayoutInflater.from(mContext).inflate(
                R.layout.dbmainactivity_layout, null);
        mView.addView(mainView);
        leftButton = (Button) mainView.findViewById(R.id.left_button);
        rightButton = (Button) mainView.findViewById(R.id.rightbutton);
        leftButton.setVisibility(View.GONE);
        rightButton.setVisibility(View.GONE);
        biaoti1 = (TextView) mainView.findViewById(R.id.biaoti1);
        biaoti2 = (TextView) mainView.findViewById(R.id.biaoti2);
        biaoti3 = (TextView) mainView.findViewById(R.id.biaoti3);
        biaoti4 = (TextView) mainView.findViewById(R.id.biaoti4);
        biaoti5 = (TextView) mainView.findViewById(R.id.biaoti5);
        biaoti1.setText("序号");
        biaoti2.setText("企业名称");
        biaoti3.setText("详情");
        biaoti4.setText("");
        biaoti4.setVisibility(View.GONE);
        biaoti5.setText("");
        biaoti5.setVisibility(View.GONE);
        // listview添加模拟数据
        listView = (ListView) mainView.findViewById(R.id.lv);


        DbNewsAdapyer adapter = new DbNewsAdapyer(mContext, data);
        listView.setAdapter(adapter);


        listView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // TODO Auto-generated method stub
                mOverlay.setLastPoint(position);

            }
        });

        // ///////////////
        showHind = (TextView) mainView.findViewById(R.id.showhind);
        news = (LinearLayout) mainView.findViewById(R.id.news);
        showHind.setOnClickListener(this);
    }

    void initMapView() {
        // 地图视图
        mapView.removeAllOverlay();// 清空所有覆盖物
        mapView.setBuiltInZoomControls(true);// 是否显示地图缩放按钮
        // 得到mMapView的控制权,可以用它控制和驱动平移和缩放
        MapController mMapController = mapView.getController();
        // 用给定的经纬度构造一个GeoPoint，单位是微度 (度 * 1E6)
        GeoPoint point = new GeoPoint((int) (116.915 * 1E6),
                (int) (117.404 * 1E6));
        // 设置地图中心点
        mMapController.setCenter(point);
        // 设置地图zoom级别
        mMapController.setZoom(12);
        // 添加我的位置和覆盖物集合

        List<Overlay> list = mapView.getOverlays();
        MyLocationOverlay myLocation = new MyLocationOverlay(mContext, mapView);
        myLocation.enableMyLocation();
        list.add(myLocation);
        Drawable markers[]=new Drawable[]{mContext.getResources().getDrawable(
                R.drawable.ari1),mContext.getResources().getDrawable(
                R.drawable.ari2),mContext.getResources().getDrawable(
                R.drawable.ari3),mContext.getResources().getDrawable(
                R.drawable.ari4),mContext.getResources().getDrawable(
                R.drawable.ari5),mContext.getResources().getDrawable(
                R.drawable.ari6),mContext.getResources().getDrawable(
                R.drawable.ari7),mContext.getResources().getDrawable(
                R.drawable.shikong1),mContext.getResources().getDrawable(
                R.drawable.shikong2),mContext.getResources().getDrawable(
                R.drawable.shikong3),mContext.getResources().getDrawable(
                R.drawable.shikong4),mContext.getResources().getDrawable(
                R.drawable.shikong5),mContext.getResources().getDrawable(
                R.drawable.shikong6),mContext.getResources().getDrawable(
                R.drawable.shikong7)};

        Drawable marker = mContext.getResources().getDrawable(
                R.drawable.ari4);
        mPopView = LayoutInflater.from(mContext).inflate(
                R.layout.popview_ariq, null);

        mOverlay = new OverItemT(marker, data,markers);


        list.add(mOverlay);
        // 创建弹出框view

        ivClose = (ImageView) mPopView.findViewById(R.id.close);
        ivClose.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mPopView.setVisibility(View.GONE);
            }
        });
        mapView.addView(mPopView, new LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT, null,
                LayoutParams.TOP_LEFT));
        mPopView.setVisibility(View.GONE);
    }

    class OverItemT extends ItemizedOverlay<OverlayItem> implements
            Overlay.Snappable {
        /*GeoPoint point = new GeoPoint((int) (36.915 * 1E6),
                (int) (117.404 * 1E6));*/
        private List<OverlayItem> mGeoList = new ArrayList<OverlayItem>();

        public OverItemT(Drawable marker, List<AriQualityBean> zbdata,Drawable[]markers) {
            super(boundCenterBottom(marker));
            for (int i = 0; i < zbdata.size(); i++) {
                OverlayItem item = new OverlayItem(
                        new GeoPoint((int) (zbdata.get(i).getLatitude() * 1E6),
                                (int) (zbdata.get(i).getLongitude() * 1E6)), "P" + i, "point"
                        + i);
                if(data.get(i).getStationCode()<9){
                    Log.d("StationCode", "StationCode=" + data.get(i).getStationCode());
                    if(data.get(i).getAQI()<=50){
                        marker=markers[0];
                    }else if(data.get(i).getAQI()<=100){
                        marker=markers[1];
                    }else if(data.get(i).getAQI()<=150){
                        marker=markers[2];
                    }else if(data.get(i).getAQI()<=200){
                        marker=markers[4];
                    }else if(data.get(i).getAQI()<=300){
                        marker=markers[5];
                    }else if (data.get(i).getAQI()>300){
                        marker=markers[6];
                    }else{
                        marker=markers[3];
                    }

                }else {
                    Log.d("StationCode", ">>>>StationCode=" + data.get(i).getStationCode());
                    if(data.get(i).getAQI()<=50){
                        marker=markers[7];
                    }else if(data.get(i).getAQI()<=100){
                        marker=markers[8];
                    }else if(data.get(i).getAQI()<=150){
                        marker=markers[9];
                    }else if(data.get(i).getAQI()<=200){
                        marker=markers[10];
                    }else if(data.get(i).getAQI()<=300){
                        marker=markers[11];
                    }else if (data.get(i).getAQI()>300){
                        marker=markers[12];
                    }else{
                        marker=markers[13];
                    }
                }

                item.setMarker(marker);
                mGeoList.add(item);
            }
            populate();


        }

        public void setLastPoint(int i) {
            onTap(i);
        }

        @Override
        protected OverlayItem createItem(int i) {
            return mGeoList.get(i);
        }

        @Override
        public int size() {
            return mGeoList.size();
        }

        @Override
        protected boolean onTap(int i) {
            if (i == -1) {
                mPopView.setVisibility(View.GONE);
                return false;
            }
            if (data != null && data.size() > 0) {
                TextView title = (TextView) mPopView.findViewById(R.id.tv_title);
                TextView tppe1 = (TextView) mPopView.findViewById(R.id.tv_type1);
                TextView tppe2 = (TextView) mPopView.findViewById(R.id.tv_type2);
                title.setText(data.get(i).getPositionName());
                tppe1.setText(data.get(i).getPositionName());
                tppe2.setText(data.get(i).getAQI()+"");
            }

            GeoPoint pt = mGeoList.get(i).getPoint();
            mapView.updateViewLayout(mPopView, new LayoutParams(
                    LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, pt,
                    -3, -6, LayoutParams.BOTTOM_CENTER));
            mPopView.setVisibility(View.VISIBLE);
            mapView.getController().animateTo(pt);
            return true;
        }

        @Override
        public boolean onTap(GeoPoint p, MapView mapView1) {

            return super.onTap(p, mapView);
        }

    }

    @Override
    public void onClick(View arg0) {
        if (arg0.getId() == R.id.showhind) {
            if (news.getVisibility() == View.VISIBLE) {
                news.setVisibility(View.GONE);
            } else {
                news.setVisibility(View.VISIBLE);
            }
        }
    }

    private void readRiskSources() {
        yutuLoading = new YutuLoading(this);
        yutuLoading.setCancelable(true);
        yutuLoading.setLoadMsg("正在获取信息，请稍候", "");
        yutuLoading.showDialog();
        //接口调用方法一
        String url = PathManager.JINAN_URL + PathManager.getSubStationAirDataMH;
        HttpUtils utils = new HttpUtils();
        utils.configCurrentHttpCacheExpiry(1000 * 5);
        utils.configTimeout(60 * 1000);//
        utils.configSoTimeout(60 * 1000);//
        RequestParams params=new RequestParams();
//        params.addBodyParameter("timeStr", DateTimeHelper.getNowDate());
        params.addBodyParameter("timeStr", "");
        utils.send(HttpRequest.HttpMethod.POST, url,params, new RequestCallBack<String>() {
            @Override
            public void onFailure(HttpException arg0, String arg1) {
                Toast.makeText(AriQualityActivity.this, "数据请求失败", Toast.LENGTH_SHORT).show();
                if (yutuLoading != null) {
                    yutuLoading.dismissDialog();
                }
//				updateList();
            }

            @Override
            public void onSuccess(ResponseInfo<String> arg0) {
                String result = String.valueOf(arg0.result);

                try {
                    ByteArrayInputStream inputStream = new ByteArrayInputStream(result.getBytes());
                    SAXReader reader = new SAXReader();
                    Document document = reader.read(inputStream);
                    Element root = document.getRootElement();
                    JSONArray jsonArray = new JSONArray(root.getText());
                    if (jsonArray != null && jsonArray.length() > 0) {
                        for (int i = 0; i < jsonArray.length(); i++) {
                            String PositionName = jsonArray.getJSONObject(i).optString("PositionName");
                            double Longitude = jsonArray.getJSONObject(i).optDouble("Longitude", 0);
                            double Latitude = jsonArray.getJSONObject(i).optDouble("Latitude", 0);
                            int AQI = jsonArray.getJSONObject(i).optInt("AQI");
                            String PrimaryPollutant = jsonArray.getJSONObject(i).optString("PrimaryPollutant");
                            int StationCode=jsonArray.getJSONObject(i).optInt("StationCode",0);
                            bean = new AriQualityBean(PositionName, Longitude, Latitude, AQI, PrimaryPollutant,StationCode);
                            data.add(bean);
                            Log.d("StationCode", "StationCode" + StationCode);


                        }
                    }
                } catch (Exception e) {

                }
                if (yutuLoading != null) {
                    yutuLoading.dismissDialog();
                }
                //updateList();
                initView();
                initMapView();
            }
        });
    }

    public class DbNewsAdapyer extends BaseAdapter {

        private Context context;
        private List<AriQualityBean> list;

        public DbNewsAdapyer(Context context, List<AriQualityBean> list) {
            this.context = context;
            this.list = list;
        }

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return list.size();
        }

        @Override
        public Object getItem(int arg0) {
            // TODO Auto-generated method stub
            return list.get(arg0);
        }

        @Override
        public long getItemId(int arg0) {
            // TODO Auto-generated method stub
            return arg0;
        }

        @Override
        public View getView(int i, View view, ViewGroup arg2) {
            ViewHolder viewHolder = null;
            if (view == null) {
                view = View.inflate(context, R.layout.jcqyc_list_item, null);
                ((LinearLayout) view).setGravity(Gravity.CENTER);
                viewHolder = new ViewHolder();
                viewHolder.grdrecodename = (TextView) view.findViewById(R.id.tv_id);
                viewHolder.gridpeople = (TextView) view
                        .findViewById(R.id.entname);
                viewHolder.details=(TextView) view
                        .findViewById(R.id.details);
                view.setTag(viewHolder);

            } else {
                viewHolder = (ViewHolder) view.getTag();
            }
            viewHolder.grdrecodename.setText(i+1+"");
            viewHolder.gridpeople.setText(list.get(i).getPositionName());
            viewHolder.details.setText("详情");
            return view;
        }

        class ViewHolder {
            TextView grdrecodename;
            TextView gridpeople;
            TextView details;
        }
    }

}
