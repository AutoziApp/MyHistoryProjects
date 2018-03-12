package cn.com.mapuni.gis.meshingtotal;

import android.content.Context;
import android.graphics.drawable.Drawable;
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

import cn.com.mapuni.gis.meshingtotal.model.ZgdycBean;
import cn.com.mapuni.gis.meshingtotal.tdt.BaseMapActivityTDT;
import cn.com.mapuni.gis.meshingtotal.tdt.BaseMapActivityTDT.ViewCallback;
import cn.com.mapuni.meshing.base.controls.loading.YutuLoading;
import cn.com.mapuni.meshing.base.interfaces.PathManager;

public class ZgdycActivity extends BaseMapActivityTDT implements
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
    List<ZgdycBean> data;
    private ZgdycBean bean;
    private ListView listView;
    private GeoPoint point;
    private Button leftButton, rightButton;
    @Override
    public void onCreate(FrameLayout view, MapView mapView) {
        this.mapView = mapView;
        this.mView = view;
        this.mContext = this;
        data = new ArrayList<ZgdycBean>();
        initData();

    }

    private void initData() {
        readRiskSources();
    }

    void initView() {
        mView.removeAllViews();
        // ��ѯ����
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
        biaoti1.setText("���");
        biaoti2.setText("����");
        biaoti3.setText("����");
        biaoti4.setText("");
        biaoti4.setVisibility(View.GONE);
        biaoti5.setText("");
        biaoti5.setVisibility(View.GONE);

        // listview���ģ������
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
        // ��ͼ��ͼ
        mapView.removeAllOverlay();// ������и�����
        mapView.setBuiltInZoomControls(true);// �Ƿ���ʾ��ͼ���Ű�ť
        // �õ�mMapView�Ŀ���Ȩ,�����������ƺ�����ƽ�ƺ�����
        MapController mMapController = mapView.getController();
        // �ø����ľ�γ�ȹ���һ��GeoPoint����λ��΢�� (�� * 1E6)
        GeoPoint point = new GeoPoint((int) (116.915 * 1E6),
                (int) (117.404 * 1E6));
        // ���õ�ͼ���ĵ�
        mMapController.setCenter(point);
        // ���õ�ͼzoom����
        mMapController.setZoom(12);
        // ����ҵ�λ�ú͸����Ｏ��

        List<Overlay> list = mapView.getOverlays();
        MyLocationOverlay myLocation = new MyLocationOverlay(mContext, mapView);
        myLocation.enableMyLocation();
        list.add(myLocation);
        Drawable markers[] = new Drawable[]{mContext.getResources().getDrawable(
                R.drawable.zhugandao1), mContext.getResources().getDrawable(
                R.drawable.zhugandao2), mContext.getResources().getDrawable(
                R.drawable.zhugandao3), mContext.getResources().getDrawable(
                R.drawable.zhugandao4), mContext.getResources().getDrawable(
                R.drawable.zhugandao5), mContext.getResources().getDrawable(
                R.drawable.zhugandao6), mContext.getResources().getDrawable(
                R.drawable.zhugandao7)};
        Drawable marker = mContext.getResources().getDrawable(
                R.drawable.lltd_icon);
        mPopView = LayoutInflater.from(mContext).inflate(
                R.layout.popview_zgdyc, null);

        mOverlay = new OverItemT(marker, data, markers);


        list.add(mOverlay);
        // ����������view

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

        public OverItemT(Drawable marker, List<ZgdycBean> zbdata, Drawable markers[]) {
            super(boundCenterBottom(marker));
            for (int i = 0; i < zbdata.size(); i++) {
                OverlayItem item = new OverlayItem(
                        new GeoPoint((int) (zbdata.get(i).getLatitude() * 1E6),
                                (int) (zbdata.get(i).getLongitude() * 1E6)), "P" + i, "point"
                        + i);
                if (zbdata.get(i).getPM10() != null) {
                    Double pm10Num = Double.parseDouble(zbdata.get(i).getPM10());
                    if (pm10Num > 0.0 && pm10Num < 50.0) {
                        marker = markers[0];
                    } else if (pm10Num >= 50.0 && pm10Num < 100.0) {
                        marker = markers[2];
                    } else if (pm10Num >= 100.0 && pm10Num < 200.0) {
                        marker = markers[3];
                    } else if (pm10Num >= 200.0 && pm10Num < 300.0) {
                        marker = markers[4];
                    } else if (pm10Num >= 300.0) {
                        marker = markers[5];
                    }
                } else {
                    marker = markers[6];
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
               /* TextView tppe3 = (TextView) mPopView.findViewById(R.id.tv_type3);
                TextView tppe4 = (TextView) mPopView.findViewById(R.id.tv_type4);*/
                title.setText(data.get(i).getEntName());
                tppe1.setText(data.get(i).getNoise());
                tppe2.setText(data.get(i).getPM10());
               /* tppe1.setText(data.get(i).getPM2P5()+"");
                tppe1.setText(data.get(i).getHumidity()+"");*/
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
            // mPopView.setVisibility(View.GONE);

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
        yutuLoading.setLoadMsg("���ڻ�ȡ��Ϣ�����Ժ�", "");
        yutuLoading.showDialog();
        //�ӿڵ��÷���һ
        String url = PathManager.JINAN_URL + PathManager.getAllArtRoadMonitorDataInfo;
        HttpUtils utils = new HttpUtils();
        utils.configCurrentHttpCacheExpiry(1000 * 5);
        utils.configTimeout(60 * 1000);//
        utils.configSoTimeout(60 * 1000);//
        utils.send(HttpRequest.HttpMethod.GET, url, new RequestCallBack<String>() {
            @Override
            public void onFailure(HttpException arg0, String arg1) {
                Toast.makeText(ZgdycActivity.this, "��������ʧ��", Toast.LENGTH_SHORT).show();
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
                            String EntName = jsonArray.getJSONObject(i).optString("EntName");
                            double Latitude = jsonArray.getJSONObject(i).optDouble("Latitude");
                            double Longitude = jsonArray.getJSONObject(i).optDouble("Longitude");
                            String noise = jsonArray.getJSONObject(i).optString("noise");
                            String PM10 = jsonArray.getJSONObject(i).optString("PM10");
                            bean = new ZgdycBean(EntName, Latitude, Longitude, noise, PM10);
                            data.add(bean);
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
        private List<ZgdycBean> list;

        public DbNewsAdapyer(Context context, List<ZgdycBean> list) {
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
                viewHolder.details = (TextView) view
                        .findViewById(R.id.details);
                view.setTag(viewHolder);

            } else {
                viewHolder = (ViewHolder) view.getTag();
            }
            ZgdycBean zgdycBean = list.get(i);
            viewHolder.grdrecodename.setText(i + 1 + "");
            viewHolder.gridpeople.setText(zgdycBean.getEntName());
            viewHolder.details.setText("����");
            return view;
        }

        class ViewHolder {
            TextView grdrecodename;
            TextView gridpeople;
            TextView details;
        }
    }

}
