package com.mapuni.mobileenvironment.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mapuni.mobileenvironment.R;
import com.mapuni.mobileenvironment.config.PathManager;
import com.mapuni.mobileenvironment.model.AirSingle;
import com.mapuni.mobileenvironment.model.HistoryRecord;
import com.mapuni.mobileenvironment.utils.ACacheUtil;
import com.mapuni.mobileenvironment.utils.DateUtils;
import com.mapuni.mobileenvironment.utils.JsonUtil;
import com.mapuni.mobileenvironment.utils.SignUtil;
import com.mapuni.mobileenvironment.view.YutuLoading;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lecho.lib.hellocharts.gesture.ZoomType;
import lecho.lib.hellocharts.listener.LineChartOnValueSelectListener;
import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.model.ValueShape;
import lecho.lib.hellocharts.view.LineChartView;
import okhttp3.Call;
import okhttp3.Request;

public class newAirCurActivity extends ActivityBase {
    private LineChartView chart;
    private LineChartData data;
    List<Float> values = new ArrayList<>();//折线图y值
    List<String> kedu = new ArrayList<>();//x轴刻度值
    List<AirSingle.DataBean> list = new ArrayList<>();
    private TextView time, pm25, pm10, o3, so2, no2, co,aqi,device_nane,indexView;
    private RelativeLayout pm25Layout,pm10Layout,o3Layout,
            so2Layout,coLayout,no2Layout,aqiLayout;
    private ImageView ivBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_air_cur);
//        ACacheUtil.saveHistoryRecord(this,getIntent().getStringExtra("Deviceid"));//将所有打开此详情页面的相关条目的deviceID保存到历史记录
        ACacheUtil.saveHistoryRecord(this,new HistoryRecord(getIntent().getStringExtra("Deviceid"),"curData"));//将所有打开此详情页面的相关条目的deviceID保存到历史记录
        initView();

        getAirDetailData();
    }

    private void initView() {
        yutuLoading = new YutuLoading(this);
        chart = (LineChartView) findViewById(R.id.linechart);
        chart.setBackgroundColor(Color.parseColor("#3098D9"));
        chart.setZoomType(ZoomType.HORIZONTAL);
        chart.setOnValueTouchListener(new ValueTouchListener());
        time = (TextView) findViewById(R.id.time);
        pm25 = (TextView) findViewById(R.id.pm25_value);
        pm10 = (TextView) findViewById(R.id.pm10_value);
        so2 = (TextView) findViewById(R.id.so2_value);
        o3 = (TextView) findViewById(R.id.o3_value);
        no2 = (TextView) findViewById(R.id.no2_value);
        co = (TextView) findViewById(R.id.co_value);
        aqi = (TextView) findViewById(R.id.aqi_value);
        aqiLayout = (RelativeLayout) findViewById(R.id.aqiLayout);
        indexView = (TextView) findViewById(R.id.indexView);
        pm25Layout = (RelativeLayout) findViewById(R.id.pm25Layout);
        pm10Layout = (RelativeLayout) findViewById(R.id.pm10Layout);
        so2Layout = (RelativeLayout) findViewById(R.id.so2Layout);
        o3Layout = (RelativeLayout) findViewById(R.id.o3Layout);
        no2Layout = (RelativeLayout) findViewById(R.id.no2Layout);
        coLayout = (RelativeLayout) findViewById(R.id.coLayout);
        device_nane= (TextView) findViewById(R.id.device_name);
        ivBack= (ImageView) findViewById(R.id.iv_back);
        ivBack.setOnClickListener(this);
        pm25Layout.setOnClickListener(this);
        pm10Layout.setOnClickListener(this);
        so2Layout.setOnClickListener(this);
        o3Layout.setOnClickListener(this);
        no2Layout.setOnClickListener(this);
        coLayout.setOnClickListener(this);
        aqiLayout.setOnClickListener(this);
        device_nane.setText(getIntent().getStringExtra("DeviceName"));
    }


    //更新折线图
    private void generateData(List<Float> randomNumbersTab, List<String> weeks) {

        List<Line> lines = new ArrayList<Line>();
        List<PointValue> values = new ArrayList<PointValue>();
        for (int j = 0; j < randomNumbersTab.size(); ++j) {
            values.add(new PointValue(j, randomNumbersTab.get(j)));
        }
        Line line = new Line(values);
        line.setColor(Color.YELLOW);
        line.setShape(ValueShape.CIRCLE);
        line.setPointRadius(3);
        line.setStrokeWidth(1);
        line.setCubic(false);//设置是否平滑
        line.setFilled(false);
        line.setHasLabels(false);
        line.setHasLabelsOnlyForSelected(false);
        line.setHasLines(true);
        line.setHasPoints(true);
        line.setPointColor(Color.WHITE);
//        line.setHasLabels(true);
//        line.setHasLabelsOnlyForSelected(true);//点击数据坐标提示数据（设置了这个line.setHasLabels(true);就无效）
        lines.add(line);
        data = new LineChartData(lines);

        List<AxisValue> mAxisValues = new ArrayList<AxisValue>();
        for (int i = 0; i < weeks.size(); i++) {
            mAxisValues.add(new AxisValue(i).setLabel(weeks.get(i)));
        }
        Axis axisX = new Axis();
        axisX.setValues(mAxisValues);  //填充X轴的坐标名称
        axisX.setMaxLabelChars(2);
        Axis axisY = new Axis().setHasLines(false);
//        data.setValueLabelBackgroundColor(Color.parseColor("#00000000"));
        data.setValueLabelBackgroundAuto(true);
        data.setAxisXBottom(axisX);
        data.setAxisYLeft(axisY);
        data.setBaseValue(Float.NEGATIVE_INFINITY);
        chart.setLineChartData(data);
        /**注：下面的7，10只是代表一个数字去类比而已
         * 当时是为了解决X轴固定数据个数。见（http://forum.xda-developers.com/tools/programming/library-hellocharts-charting-library-t2904456/page2）;
         */
//        Viewport v = new Viewport(chart.getMaximumViewport());
//        v.left = 0;
//        v.right= 2;
//        chart.setCurrentViewport(v);
    }

    //访问网络接口获取24小时数据
    private void getAirDetailData() {
        String url = PathManager.SingleHistory;
        Map<String, String> jo = new HashMap<>();
        String nonce = SignUtil.getNonce();
        String timestamp = new Date().getTime() + "";
        jo.put("timestamp", timestamp);
        jo.put("nonce", nonce);
        jo.put("signature", SignUtil.getSignature2(timestamp, nonce));
//        jo.put("st", "2016-11-31");
//        jo.put("et", "2016-11-31");
        jo.put("st", DateUtils.getToday());
        jo.put("et", DateUtils.getToday());
        jo.put("deviceId", getIntent().getStringExtra("Deviceid"));
        OkHttpUtils
                .post()//
                .url(url)//
                .params(jo)
                .build()//
                .execute(new StringCallback() {
                    @Override
                    public void onBefore(Request request, int id) {
                        yutuLoading.showDialog();
                        super.onBefore(request, id);
                    }

                    @Override
                    public void onAfter(int id) {
                        super.onAfter(id);
                    }

                    @Override
                    public void onError(Call call, Exception e, int id) {
                        yutuLoading.showFailed();
                        handle.sendEmptyMessageDelayed(100, 2000);
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        yutuLoading.dismissDialog();
                        AirSingle airSite = (AirSingle) JsonUtil.jsonToBean(response, AirSingle.class);
                        if (0 == airSite.getRet()&&airSite.getData()!=null&&airSite.getData().size()>0) {
                            list.clear();
                            list = airSite.getData();
                            int i = 1;
                            for (AirSingle.DataBean dataBean : list) {
                                values.add((float) dataBean.getPm25());
                                // kedu.add(DateUtils.long2DateString(dataBean.getTime()));
                                kedu.add(i++ + "时");
                            }
                            generateData(values, kedu);
                            refreshData(list.get(list.size()-1));
                        }
                    }
                });
    }

    private class ValueTouchListener implements LineChartOnValueSelectListener {
        @Override
        public void onValueSelected(int lineIndex, int pointIndex, PointValue value) {
            //在此处更新具体大气数值
            if(list!=null&&list.size()>pointIndex){
                refreshData(list.get(pointIndex));
            }
        }

        @Override
        public void onValueDeselected() {
            // TODO Auto-generated method stub

        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_back:
                finish();
                break;
            case R.id.pm10Layout:
                values.clear();
                for (AirSingle.DataBean dataBean : list) {
                    values.add((float) dataBean.getPm10());
                    // kedu.add(DateUtils.long2DateString(dataBean.getTime()));
                }
                generateData(values, kedu);
                indexView.setText("PM10指数");
                break;
            case R.id.pm25Layout:
                values.clear();
                for (AirSingle.DataBean dataBean : list) {
                    values.add((float) dataBean.getPm25());
                    // kedu.add(DateUtils.long2DateString(dataBean.getTime()));
                }
                generateData(values, kedu);
                indexView.setText("PM25指数");
                break;
            case R.id.so2Layout:
                values.clear();
                for (AirSingle.DataBean dataBean : list) {
                    values.add((float) dataBean.getSo2());
                    // kedu.add(DateUtils.long2DateString(dataBean.getTime()));
                }
                generateData(values, kedu);
                indexView.setText("SO2指数");
                break;
            case R.id.coLayout:
                values.clear();
                for (AirSingle.DataBean dataBean : list) {
                    values.add((float) dataBean.getCo());
                    // kedu.add(DateUtils.long2DateString(dataBean.getTime()));
                }
                generateData(values, kedu);
                indexView.setText("CO指数");
                break;
            case R.id.o3Layout:
                values.clear();
                for (AirSingle.DataBean dataBean : list) {
                    values.add((float) dataBean.getO3());
                    // kedu.add(DateUtils.long2DateString(dataBean.getTime()));
                }
                generateData(values, kedu);
                indexView.setText("O3指数");
                break;
            case R.id.no2Layout:
                values.clear();
                for (AirSingle.DataBean dataBean : list) {
                    values.add((float) dataBean.getNo2());
                    // kedu.add(DateUtils.long2DateString(dataBean.getTime()));
                }
                generateData(values, kedu);
                indexView.setText("NO2指数");
                break;
            case R.id.aqiLayout:
                values.clear();
                for (AirSingle.DataBean dataBean : list) {
                    values.add((float) dataBean.getAqi());
                    // kedu.add(DateUtils.long2DateString(dataBean.getTime()));
                }
                generateData(values, kedu);
                indexView.setText("AQI指数");
                break;
        }

    }

    //刷新数据布局
    public void refreshData(AirSingle.DataBean dataBean) {
//        AirSingle.DataBean dataBean = list.get(index);
        String s = DateUtils.long2DateString(dataBean.getTime());
        time.setText(s);
        pm10.setText(-1==dataBean.getPm10()?"-":(int)dataBean.getPm10() + "");
        pm25.setText(-1==dataBean.getPm25()?"-":(int) dataBean.getPm25() + "");
        so2.setText(-1==dataBean.getSo2()?"-":(int)dataBean.getSo2() + "");
        no2.setText(-1==dataBean.getNo2()?"-":(int)dataBean.getNo2() + "");
        co.setText(-1==dataBean.getCo()?"-":(int)dataBean.getCo() + "");
        o3.setText(-1==dataBean.getO3()?"-":(int)dataBean.getO3() + "");
        aqi.setText(-1==dataBean.getAqi()?"-":(int)dataBean.getAqi() + "");
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        OkHttpUtils.getInstance().cancelTag(this);
    }
}
