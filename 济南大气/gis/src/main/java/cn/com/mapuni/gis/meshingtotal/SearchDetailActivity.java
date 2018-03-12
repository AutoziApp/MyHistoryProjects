package cn.com.mapuni.gis.meshingtotal;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.tianditu.android.maps.GeoPoint;
import com.tianditu.android.maps.MapController;
import com.tianditu.android.maps.MapView;
import com.tianditu.android.maps.MapViewRender;
import com.tianditu.android.maps.Overlay;
import com.tianditu.android.maps.renderoption.DrawableOption;

import javax.microedition.khronos.opengles.GL10;

import cn.com.mapuni.gis.meshingtotal.model.PollutionSourceBean1;
import cn.com.mapuni.gis.meshingtotal.tdt.BaseMapActivityTDT;

import static com.iflytek.msc.e.b.i;

public class SearchDetailActivity extends BaseMapActivityTDT implements
        BaseMapActivityTDT.ViewCallback{
    private FrameLayout mView;
    private MapView mapView;
    private Context mContext;
    private View mPopView = null;
    private ImageView ivClose;
    private PollutionSourceBean1 bean;
    @Override
    public void onCreate(FrameLayout view, MapView mapView) {
        this.mapView = mapView;
        this.mView = view;
        this.mContext = this;
        bean=(PollutionSourceBean1) getIntent().getSerializableExtra("bean");
        initView();
    }



    void initView() {
        mView.removeAllViews();
        // 地图视图
        mapView.removeAllOverlay();// 清空所有  覆盖物
        mapView.setBuiltInZoomControls(true);// 是否显示地图缩放按钮
        // 得到mMapView的控制权,可以用它控制和驱动平移和缩放
        MapController mMapController = mapView.getController();
        // 用给定的经纬度构造一个GeoPoint，单位是微度 (度 * 1E6)
        GeoPoint point = new GeoPoint((int) (bean.getLatitude() * 1E6),
                (int) (bean.getLongitude()* 1E6));
        // 设置地图中心点
        mMapController.setCenter(point);
        // 设置地图zoom级别
        mMapController.setZoom(12);
        // 添加我的位置和覆盖物集合
        mPopView = LayoutInflater.from(mContext).inflate(
                R.layout.popview_wry, null);
        ivClose = (ImageView) mPopView.findViewById(R.id.close);
        ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPopView.setVisibility(View.GONE);
            }
        });
        mapView.addView(mPopView, new MapView.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT, null,
                MapView.LayoutParams.TOP_LEFT));
        mPopView.setVisibility(View.GONE);
        MyOverlay myOverlay=new MyOverlay();
        mapView.addOverlay(myOverlay);
        myOverlay.setGeoPoint(point);
        myOverlay.setVisible(true);
        myOverlay.onTap(point,mapView);
    }


    public class MyOverlay extends Overlay {
        private Drawable mDrawable;
        private GeoPoint mGeoPoint;
        private DrawableOption mOption;

        public MyOverlay() {
            mDrawable = mContext.getResources().getDrawable(R.drawable.lltd_icon);
            mOption = new DrawableOption();
//            mOption.setAnchor(0f, 0f);
        }

        public void setGeoPoint(GeoPoint point) {
            mGeoPoint = point;
        }

        @Override
        public boolean onTap(GeoPoint point, MapView mapView) {
            if (i == -1) {
                mPopView.setVisibility(View.GONE);
                return true;
            }
                TextView tppe1 = (TextView) mPopView.findViewById(R.id.tv_type1);
               // tppe1.setText(bean.getEnterprisetypr());
            TextView title = (TextView) mPopView.findViewById(R.id.tv_title);
           // title.setText(bean.getEntName());
            mapView.updateViewLayout(mPopView, new MapView.LayoutParams(
                    MapView.LayoutParams.WRAP_CONTENT, MapView.LayoutParams.WRAP_CONTENT, mGeoPoint,
                    -3, -30, MapView.LayoutParams.BOTTOM_CENTER));
            mPopView.setVisibility(View.VISIBLE);
            mapView.getController().animateTo(mGeoPoint);
            return true;
        }

        @Override
        public boolean onKeyUp(int keyCode, KeyEvent event, MapView mapView) {

            return super.onKeyUp(keyCode, event, mapView);
        }

        @Override
        public boolean onKeyDown(int keyCode, KeyEvent event, MapView mapView) {

            return super.onKeyDown(keyCode, event, mapView);
        }



        @Override
        public boolean onLongPress(GeoPoint p, MapView mapView) {

            return super.onLongPress(p, mapView);
        }

        @Override
        public boolean isVisible() {
            return super.isVisible();
        }

        @Override
        public void setVisible(boolean b) {
            super.setVisible(b);
        }

        @Override
        public void draw(GL10 gl, MapView mapView, boolean shadow) {
            if (shadow)
                return;

            MapViewRender render = mapView.getMapViewRender();
            render.drawDrawable(gl, mOption, mDrawable, mGeoPoint);
        }
    }



}
