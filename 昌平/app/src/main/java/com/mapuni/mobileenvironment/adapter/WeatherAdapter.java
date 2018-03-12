package com.mapuni.mobileenvironment.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.echo.holographlibrary.Line;
import com.echo.holographlibrary.LineGraph;
import com.echo.holographlibrary.LinePoint;
import com.mapuni.mobileenvironment.R;
import com.mapuni.mobileenvironment.activity.newAirCurActivity;
import com.mapuni.mobileenvironment.config.PathManager;
import com.mapuni.mobileenvironment.fragment.HomeFragment;
import com.mapuni.mobileenvironment.model.GasAndJuli;
import com.mapuni.mobileenvironment.model.GasSite;
import com.mapuni.mobileenvironment.model.WeatherModel;
import com.mapuni.mobileenvironment.utils.CommonUtil;
import com.mapuni.mobileenvironment.utils.JsonUtil;
import com.mapuni.mobileenvironment.utils.PositionUtil;
import com.mapuni.mobileenvironment.utils.ScreenUtils;
import com.mapuni.mobileenvironment.utils.SharepreferenceUtil;
import com.mapuni.mobileenvironment.utils.SignUtil;
import com.mapuni.mobileenvironment.view.ListViewForScrollView;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Request;


public class WeatherAdapter extends BaseAdapter {
    private WeatherModel model;
    private Context context;
    private LayoutInflater mInflater;
    private List<Integer> mTags;
    private static final int TYPE_1 = 0;
    private static final int TYPE_2 = 1;
    private static final int TYPE_3 = 2;
    private int screenWidth;
    private HomeFragment.ItemListener listener;

    public WeatherAdapter(Context con, WeatherModel model, HomeFragment.ItemListener listener) {
        screenWidth = ScreenUtils.getScreenW(con);
        context = con;
        this.model = model;
        this.listener = listener;
        mInflater = LayoutInflater.from(con);
        mTags = new ArrayList<Integer>();
        mTags.add(TYPE_1);
        mTags.add(TYPE_2);
        /**
         * 这里屏蔽了最新超标污染源模块
         */
        //mTags.add(TYPE_3);
    }

    @Override
    public int getCount() {
        return mTags.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {


        int itemType = getItemViewType(position);
//		List<Trend> trends = result.getTrends();
//		LifeItem item = result.getLife();
        switch (itemType) {
            case TYPE_1:
            /*
			 * convertView = mLayoutInflater.inflate(R.watch_dialog_layout.activity_main,
			 * null); activity_main(convertView, trends);
			 */
                convertView = mInflater.inflate(R.layout.linegraph, parent, false);
                linegraph(convertView);
                break;
            case TYPE_2:
			/*
			 * convertView = mLayoutInflater.inflate(R.watch_dialog_layout.activity_main,
			 * null); activity_main(convertView, trends);
			 */
                convertView = mInflater.inflate(R.layout.item_view_two, parent, false);
                initGasData(convertView);
//			linegraph(convertView);
                break;
            case TYPE_3:
			/*
			 * convertView = mLayoutInflater.inflate(R.watch_dialog_layout.activity_main,
			 * null); activity_main(convertView, trends);
			 */
                convertView = mInflater.inflate(R.layout.item_view_three, parent, false);
                initPollution(convertView);
                listener.setView(convertView);
                break;
        }

//		convertView = mInflater.inflate(R.watch_dialog_layout.pollution_item, null);


        return convertView;
    }

    private void initGasData(final View view) {
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
                        GasSite airSite = (GasSite) JsonUtil.jsonToBean(response, GasSite.class);
                        if (0 == airSite.getRet()) {

                            GasSite.DataBeanX outerData=  airSite.getData();//获取被筛选的数据
                            List<GasSite.DataBeanX.DataBean> data=outerData.getData();
                            List<GasAndJuli> fixDatas=new ArrayList<>();
                            double[] curPosition= SharepreferenceUtil.getLatlog(context);//获取当前点位的经纬度
//                            double[] curPosition= new double[]{40.200,116.234};//环保局的经纬度坐标
                            for(GasSite.DataBeanX.DataBean dataBean:data){//遍历数据源，计算距离
                                double[] latlog=new double[2];//组装点位信息
                                latlog[0]=dataBean.getLat();
                                latlog[1]=dataBean.getLon();
                                float juli=PositionUtil.calculateLineDistance(curPosition,latlog);
                                fixDatas.add(new GasAndJuli(juli,dataBean));
                            }
                            Collections.sort(fixDatas, new Comparator<GasAndJuli>() {//按照从小到大的顺序进行排序
                                @Override
                                public int compare(GasAndJuli t0, GasAndJuli t1) {
                                    if (t0.getJuli() < t1.getJuli()) {
                                        return -1;
                                    } else if(t0.getJuli() > t1.getJuli()){
                                        return 1;
                                    }else{
                                        return 0;
                                    }
                                }
                            });
                            ArrayList<GasAndJuli> tempList = new ArrayList<>();
                            for (int i = 0; i < fixDatas.size(); i++) {
                                if (i < 3) {
                                    tempList.add(fixDatas.get(i));
                                }
                            }
                            fixDatas=tempList;//截取集合长度为3
                            ListViewForScrollView listview = (ListViewForScrollView) view.findViewById(R.id.lv);//初始化控件
                            NearGasAdapter adapter=new NearGasAdapter(context,fixDatas);
                            listview.setAdapter(adapter);
                            final List<GasAndJuli> finalFixDatas = fixDatas;
                            listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                    Intent intent = new Intent(context, newAirCurActivity.class);
                                    intent.putExtra("Deviceid", finalFixDatas.get(i).getDataBean().getId());
                                    intent.putExtra("DeviceName", finalFixDatas.get(i).getDataBean().getName());
                                    context.startActivity(intent);
                                }
                            });
                        }
                    }
                });
    }

    private void initPollution(View view) {
        RelativeLayout iOne = (RelativeLayout) view.findViewById(R.id.item_one);
        RelativeLayout iTwo = (RelativeLayout) view.findViewById(R.id.item_two);
        RelativeLayout iThree = (RelativeLayout) view.findViewById(R.id.item_three);

        iOne.setOnClickListener(listener);
        iTwo.setOnClickListener(listener);
        iThree.setOnClickListener(listener);
        if (model.getOverList()==null){
            return;
        }
        if (model.getOverList().size()==0){
            iOne.setVisibility(View.GONE);
            iTwo.setVisibility(View.GONE);
            iThree.setVisibility(View.GONE);
        }else if (model.getOverList().size()==1){
            iOne.setVisibility(View.VISIBLE);
            iTwo.setVisibility(View.GONE);
            iThree.setVisibility(View.GONE);
        }else if (model.getOverList().size()==2){
            iOne.setVisibility(View.VISIBLE);
            iTwo.setVisibility(View.VISIBLE);
            iThree.setVisibility(View.GONE);
        }else{
            iOne.setVisibility(View.VISIBLE);
            iTwo.setVisibility(View.VISIBLE);
            iThree.setVisibility(View.VISIBLE);
        }
    }

//	private void linegraph(View view){
//		LinearLayout layout = (LinearLayout) view.findViewById(R.id.itemWater);
//		layout.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
////				Intent intent = new Intent(context, WaterInfoActivity.class);
////				WaterModel _model = new WaterModel();
////				_model.setLATITUDE(30.5404400000);
////				_model.setLONGITUDE(114.2347010000);
////				_model.setArea("武汉市");
////				_model.setMuther("长江");
////				_model.setStationName("杨泗港");
////				_model.setLevel(3);
////				_model.setmDrawable(context.getResources().getDrawable(R.drawable.water_img3));
////				intent.putExtra("index", 2);
////				context.startActivity(intent);
//			}
//		});
//	}

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return TYPE_1;
        }
        if (position == 1) {
            return TYPE_2;
        }

        return TYPE_3;

    }

    public int getWeatherIcon(String climate) {
        int weatherIcon = R.mipmap.weather_icon_qingtian;
        String climateString = CommonUtil.getWeatherIconString(climate, 0);
        if (CommonUtil.initWeatherIconMap().containsKey(climateString)) {
            weatherIcon = CommonUtil.initWeatherIconMap().get(climateString);
        }
        return weatherIcon;
    }

    private void linegraph(View view) {
        TextView linegraph_tv1 = (TextView) view.findViewById(R.id.linegraph_tv1);
        TextView linegraph_tv2 = (TextView) view.findViewById(R.id.linegraph_tv2);
        TextView linegraph_tv3 = (TextView) view.findViewById(R.id.linegraph_tv3);
        TextView linegraph_tv4 = (TextView) view.findViewById(R.id.linegraph_tv4);
        TextView linegraph_tv5 = (TextView) view.findViewById(R.id.linegraph_tv5);
        TextView linegraph_tv6 = (TextView) view.findViewById(R.id.linegraph_tv6);
        TextView linegraph_tv7 = (TextView) view.findViewById(R.id.linegraph_tv7);
        TextView linegraph_tv8 = (TextView) view.findViewById(R.id.linegraph_tv8);
        TextView linegraph_tv9 = (TextView) view.findViewById(R.id.linegraph_tv9);
        TextView linegraph_tv10 = (TextView) view.findViewById(R.id.linegraph_tv10);
        TextView linegraph_tv11 = (TextView) view.findViewById(R.id.linegraph_tv11);
        TextView linegraph_tv12 = (TextView) view.findViewById(R.id.linegraph_tv12);
        ImageView activity_main_iv0 = (ImageView) view.findViewById(R.id.activity_main_iv0);
        ImageView activity_main_iv1 = (ImageView) view.findViewById(R.id.activity_main_iv1);
        ImageView activity_main_iv2 = (ImageView) view.findViewById(R.id.activity_main_iv2);
        ImageView activity_main_iv3 = (ImageView) view.findViewById(R.id.activity_main_iv3);
        ImageView activity_main_iv4 = (ImageView) view.findViewById(R.id.activity_main_iv4);
        ImageView activity_main_iv5 = (ImageView) view.findViewById(R.id.activity_main_iv5);
        if (model==null){
            return;
        }
        linegraph_tv1.setText(model.getWeather().get(0).getWeek());
        linegraph_tv2.setText(model.getWeather().get(1).getWeek());
        linegraph_tv3.setText(model.getWeather().get(2).getWeek());
        linegraph_tv4.setText(model.getWeather().get(3).getWeek());
        linegraph_tv5.setText(model.getWeather().get(4).getWeek());
        linegraph_tv6.setText(model.getWeather().get(0).getDate());
        linegraph_tv7.setText(model.getWeather().get(1).getDate());
        linegraph_tv8.setText(model.getWeather().get(2).getDate());
        linegraph_tv9.setText(model.getWeather().get(3).getDate());
        linegraph_tv10.setText(model.getWeather().get(4).getDate());
        linegraph_tv11.setText(model.getWeather().get(5).getWeek());
        linegraph_tv12.setText(model.getWeather().get(5).getDate());
        activity_main_iv0
                .setImageDrawable(context.getResources().getDrawable(getWeatherIcon(model.getWeather().get(0).getWeather())));
        activity_main_iv1
                .setImageDrawable(context.getResources().getDrawable(getWeatherIcon(model.getWeather().get(1).getWeather())));
        activity_main_iv2
                .setImageDrawable(context.getResources().getDrawable(getWeatherIcon(model.getWeather().get(2).getWeather())));
        activity_main_iv3
                .setImageDrawable(context.getResources().getDrawable(getWeatherIcon(model.getWeather().get(3).getWeather())));
        activity_main_iv4
                .setImageDrawable(context.getResources().getDrawable(getWeatherIcon(model.getWeather().get(4).getWeather())));
        activity_main_iv5
                .setImageDrawable(context.getResources().getDrawable(getWeatherIcon(model.getWeather().get(5).getWeather())));

        drawBitmap(view);
    }


    private void drawBitmap(View forcastView) {
        String tempArray[] = model.getWeather().get(0).getTemp().replace("℃", "").split("~");
        int high = 0;
        int low = 0;
        Line l1 = new Line();
        Line l2 = new Line();
        int x_index = screenWidth / 6;
        int x_index_half = x_index / 2;
        LinePoint p1 = new LinePoint(); // 高温曲线
        LinePoint p2 = new LinePoint(); // 低温曲线
        p1.setX(x_index_half);
        p1.setY(high = Math
                .max(Math.max(Math.max(CommonUtil.StringToInt(tempArray[0]), CommonUtil.StringToInt(tempArray[1])),
                        CommonUtil.StringToInt(tempArray[1])), CommonUtil.StringToInt(tempArray[1])));
        l1.addPoint(p1);

        p2.setX(x_index_half);
        p2.setY(low = Math.min(CommonUtil.StringToInt(tempArray[1]), CommonUtil.StringToInt(tempArray[0])));
        l2.addPoint(p2);

        String tempArray2[] = model.getWeather().get(1).getTemp().replace("℃", "").split("~");
        p1 = new LinePoint();
        p1.setX(x_index_half + x_index);

        p1.setY(Math.max(CommonUtil.StringToInt(tempArray2[0]), CommonUtil.StringToInt(tempArray2[1])));
        high = high > Math.max(Math.max(CommonUtil.StringToInt(tempArray2[0]), CommonUtil.StringToInt(tempArray2[1])),
                CommonUtil.StringToInt(tempArray2[1]))
                ? high
                : Math.max(
                Math.max(CommonUtil.StringToInt(tempArray2[0]), CommonUtil.StringToInt(tempArray2[1])),
                CommonUtil.StringToInt(tempArray2[1]));
        l1.addPoint(p1);
        p2 = new LinePoint();
        p2.setX(x_index_half + x_index);

        p2.setY(Math.min(CommonUtil.StringToInt(tempArray2[1]), CommonUtil.StringToInt(tempArray2[0])));
        l2.addPoint(p2);
        low = low < Math.min(CommonUtil.StringToInt(tempArray2[1]), CommonUtil.StringToInt(tempArray2[0])) ? low
                : Math.min(CommonUtil.StringToInt(tempArray2[1]), CommonUtil.StringToInt(tempArray2[0]));

        String tempArray3[] = model.getWeather().get(2).getTemp().replace("℃", "").split("~");
        p1 = new LinePoint();
        p1.setX(x_index_half + x_index * 2);
        p1.setY(Math.max(Math.max(CommonUtil.StringToInt(tempArray3[0]), CommonUtil.StringToInt(tempArray3[1])),
                CommonUtil.StringToInt(tempArray3[1])));
        l1.addPoint(p1);
        high = high > Math.max(Math.max(CommonUtil.StringToInt(tempArray3[0]), CommonUtil.StringToInt(tempArray3[1])),
                CommonUtil.StringToInt(tempArray3[1]))
                ? high
                : Math.max(
                Math.max(CommonUtil.StringToInt(tempArray3[0]), CommonUtil.StringToInt(tempArray3[1])),
                CommonUtil.StringToInt(tempArray3[1]));
        p2 = new LinePoint();
        p2.setX(x_index_half + x_index * 2);

        p2.setY(Math.min(CommonUtil.StringToInt(tempArray3[1]), CommonUtil.StringToInt(tempArray3[0])));
        l2.addPoint(p2);
        low = low < Math.min(CommonUtil.StringToInt(tempArray3[1]), CommonUtil.StringToInt(tempArray3[0])) ? low
                : Math.min(CommonUtil.StringToInt(tempArray3[1]), CommonUtil.StringToInt(tempArray3[0]));

        String tempArray4[] = model.getWeather().get(3).getTemp().replace("℃", "").split("~");
        p1 = new LinePoint();
        p1.setX(x_index_half + x_index * 3);

        p1.setY(Math.max(Math.max(CommonUtil.StringToInt(tempArray4[0]), CommonUtil.StringToInt(tempArray4[1])),
                CommonUtil.StringToInt(tempArray4[1])));
        l1.addPoint(p1);
        high = high > Math.max(Math.max(CommonUtil.StringToInt(tempArray4[0]), CommonUtil.StringToInt(tempArray4[1])),
                CommonUtil.StringToInt(tempArray4[1]))
                ? high
                : Math.max(
                Math.max(CommonUtil.StringToInt(tempArray4[0]), CommonUtil.StringToInt(tempArray4[1])),
                CommonUtil.StringToInt(tempArray4[1]));
        p2 = new LinePoint();
        p2.setX(x_index_half + x_index * 3);

        p2.setY(Math.min(CommonUtil.StringToInt(tempArray4[1]), CommonUtil.StringToInt(tempArray4[0])));
        l2.addPoint(p2);
        low = low < Math.min(CommonUtil.StringToInt(tempArray4[1]), CommonUtil.StringToInt(tempArray4[0])) ? low
                : Math.min(CommonUtil.StringToInt(tempArray4[1]), CommonUtil.StringToInt(tempArray4[0]));

        String tempArray5[] = model.getWeather().get(4).getTemp().replace("℃", "").split("~");
        p1 = new LinePoint();
        p1.setX(x_index_half + x_index * 4);

        p1.setY(Math.max(Math.max(CommonUtil.StringToInt(tempArray5[0]), CommonUtil.StringToInt(tempArray5[1])),
                CommonUtil.StringToInt(tempArray5[1])));
        l1.addPoint(p1);
        high = high > Math.max(Math.max(CommonUtil.StringToInt(tempArray5[0]), CommonUtil.StringToInt(tempArray5[1])),
                CommonUtil.StringToInt(tempArray5[1]))
                ? high
                : Math.max(
                Math.max(CommonUtil.StringToInt(tempArray5[0]), CommonUtil.StringToInt(tempArray5[1])),
                CommonUtil.StringToInt(tempArray5[1]));
        p2 = new LinePoint();
        p2.setX(x_index_half + x_index * 4);

        p2.setY(Math.min(CommonUtil.StringToInt(tempArray5[1]), CommonUtil.StringToInt(tempArray5[0])));
        l2.addPoint(p2);
        low = low < Math.min(CommonUtil.StringToInt(tempArray5[1]), CommonUtil.StringToInt(tempArray5[0])) ? low
                : Math.min(CommonUtil.StringToInt(tempArray5[1]), CommonUtil.StringToInt(tempArray5[0]));

        String tempArray6[] = model.getWeather().get(5).getTemp().replace("℃", "").split("~");
        p1 = new LinePoint();
        p1.setX(x_index_half + x_index * 5);

        p1.setY(Math.max(Math.max(CommonUtil.StringToInt(tempArray6[0]), CommonUtil.StringToInt(tempArray6[1])),
                CommonUtil.StringToInt(tempArray6[1])));
        l1.addPoint(p1);
        high = high > Math.max(Math.max(CommonUtil.StringToInt(tempArray6[0]), CommonUtil.StringToInt(tempArray6[1])),
                CommonUtil.StringToInt(tempArray6[1]))
                ? high
                : Math.max(
                Math.max(CommonUtil.StringToInt(tempArray6[0]), CommonUtil.StringToInt(tempArray6[1])),
                CommonUtil.StringToInt(tempArray6[1]));
        p2 = new LinePoint();
        p2.setX(x_index_half + x_index * 5);

        p2.setY(Math.min(CommonUtil.StringToInt(tempArray6[1]), CommonUtil.StringToInt(tempArray6[0])));
        l2.addPoint(p2);
        low = low < Math.min(CommonUtil.StringToInt(tempArray6[1]), CommonUtil.StringToInt(tempArray6[0])) ? low
                : Math.min(CommonUtil.StringToInt(tempArray6[1]), CommonUtil.StringToInt(tempArray6[0]));

        // l1.setColor(Color.parseColor("#FF9900"));
        l1.setColor(Color.parseColor("#FFFFFF"));

        LineGraph li1 = (LineGraph) forcastView.findViewById(R.id.linegraph_graph);
        // li1.setPointYellow(true);
        l1.setHigh(true);
        li1.addLine(l1);
        li1.setRangeY(low - 10, high + 5);
        li1.setOnPointClickedListener(new LineGraph.OnPointClickedListener() {
            @Override
            public void onClick(int lineIndex, int pointIndex) {
            }
        });

        // l2.setColor(Color.parseColor("#0099CC"));
        l2.setColor(Color.parseColor("#FFFFFF"));

        LineGraph li2 = (LineGraph) forcastView.findViewById(R.id.linegraph_graph);
        // li2.setPointYellow(false);
        l2.setHigh(false);
        li2.addLine(l2);
        li2.setRangeY(low - 10, high + 5);

        li2.setOnPointClickedListener(new LineGraph.OnPointClickedListener() {
            @Override
            public void onClick(int lineIndex, int pointIndex) {
            }
        });

    }
}