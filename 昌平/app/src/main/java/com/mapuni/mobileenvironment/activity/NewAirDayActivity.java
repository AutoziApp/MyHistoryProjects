package com.mapuni.mobileenvironment.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.mapuni.mobileenvironment.R;
import com.mapuni.mobileenvironment.config.PathManager;
import com.mapuni.mobileenvironment.model.AirDayHistory;
import com.mapuni.mobileenvironment.model.HistoryRecord;
import com.mapuni.mobileenvironment.utils.ACacheUtil;
import com.mapuni.mobileenvironment.utils.DateUtils;
import com.mapuni.mobileenvironment.utils.JsonUtil;
import com.mapuni.mobileenvironment.utils.SignUtil;
import com.mapuni.mobileenvironment.view.YutuLoading;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lecho.lib.hellocharts.gesture.ZoomType;
import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.model.ValueShape;
import lecho.lib.hellocharts.model.Viewport;
import lecho.lib.hellocharts.view.LineChartView;
import okhttp3.Call;
import okhttp3.Request;

public class NewAirDayActivity extends ActivityBase implements RadioGroup.OnCheckedChangeListener {
    private ImageView back;
    private TextView title, charTitle;
    private RadioGroup radioGroup;
    private RadioButton aqi, pm25, o3, pm10, co, so2, no2;
    private LineChartView chart;
    private LineChartData data;
    List<Float> values = new ArrayList<>();//折线图y值
    List<String> kedu = new ArrayList<>();//x轴刻度值
    List<AirDayHistory.DataBean> list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_air_day);
        ACacheUtil.saveHistoryRecord(this,new HistoryRecord(getIntent().getStringExtra("Deviceid"),"dayData"));//将所有打开此详情页面的相关条目的deviceID保存到历史记录
        initView();
        getGasHistiryDayData();
    }

    private void initView() {
        yutuLoading = new YutuLoading(this);
        back = (ImageView) findViewById(R.id.iv_back);
        title = (TextView) findViewById(R.id.device_name);
        title.setText(getIntent().getStringExtra("DeviceName").replaceAll("\\d+", "").trim());
        charTitle = (TextView) findViewById(R.id.char_title);
        radioGroup = (RadioGroup) findViewById(R.id.rg);
        aqi = (RadioButton) findViewById(R.id.rb_aqi);
        pm25 = (RadioButton) findViewById(R.id.rb_pm25);
        o3 = (RadioButton) findViewById(R.id.rb_o3);
        pm10 = (RadioButton) findViewById(R.id.rb_pm10);
        co = (RadioButton) findViewById(R.id.rb_co);
        so2 = (RadioButton) findViewById(R.id.rb_so2);
        no2 = (RadioButton) findViewById(R.id.rb_no2);
        yutuLoading = new YutuLoading(this);
        chart = (LineChartView) findViewById(R.id.linechart);
        chart.setBackgroundColor(Color.parseColor("#ffffff"));
        chart.setZoomType(ZoomType.HORIZONTAL);
        back.setOnClickListener(this);
        radioGroup.setOnCheckedChangeListener(this);
        aqi.setChecked(true);
    }

    //联网获取历史日数据
    private void getGasHistiryDayData() {
        String url = PathManager.GetDayDataById;
        Map<String, String> jo = new HashMap<>();
        String nonce = SignUtil.getNonce();
        String timestamp = new Date().getTime() + "";
        jo.put("timestamp", timestamp);
        jo.put("nonce", nonce);
        jo.put("signature", SignUtil.getSignature2(timestamp, nonce));
        jo.put("st", DateUtils.getLastXDaysDate(31));
        jo.put("et", DateUtils.getLastXDaysDate(1));
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
                        AirDayHistory airDayHistory = (AirDayHistory) JsonUtil.jsonToBean(response, AirDayHistory.class);
                        try{
                            list = airDayHistory.getData();
                            Collections.reverse(list);
                            refreshData(0);
                        }catch(Exception e){
                            Log.i("Lybin","NewAirDayActivity----line130---------"+e.toString());
                        }
                    }
                });
    }

    //动态组装折线图数据
    private void refreshData(int position) {
        if (list == null)
            return;
        values.clear();
        kedu.clear();
        switch (position) {
            case 0://aqi
                for (AirDayHistory.DataBean dataBean : list) {
                    values.add((float) dataBean.getAqi());
                    kedu.add(DateUtils.long2DateString2(dataBean.getTime()));
                }
                charTitle.setText(getIntent().getStringExtra("DeviceName").replaceAll("\\d+", "").trim() +
                        ":aqi变化趋势" + "(" + DateUtils.getLastXDaysDate(31) + "至" +
                        DateUtils.getLastXDaysDate(1) + ")");
                break;
            case 1://pm25
                for (AirDayHistory.DataBean dataBean : list) {
                    values.add((float) dataBean.getPm25());
                    kedu.add(DateUtils.long2DateString2(dataBean.getTime()));
                }
                charTitle.setText(getIntent().getStringExtra("DeviceName").replaceAll("\\d+", "").trim() +
                        ":pm2.5变化趋势" + "(" + DateUtils.getLastXDaysDate(31) + "至" +
                        DateUtils.getLastXDaysDate(1) + ")");
                break;
            case 2://o3
                for (AirDayHistory.DataBean dataBean : list) {
                    values.add((float) dataBean.getO3());
                    kedu.add(DateUtils.long2DateString2(dataBean.getTime()));
                }
                charTitle.setText(getIntent().getStringExtra("DeviceName").replaceAll("\\d+", "").trim() +
                        ":o3变化趋势" + "(" + DateUtils.getLastXDaysDate(31) + "至" +
                        DateUtils.getLastXDaysDate(1) + ")");
                break;
            case 3://pm10
                for (AirDayHistory.DataBean dataBean : list) {
                    values.add((float) dataBean.getPm10());
                    kedu.add(DateUtils.long2DateString2(dataBean.getTime()));
                }
                charTitle.setText(getIntent().getStringExtra("DeviceName").replaceAll("\\d+", "").trim() +
                        ":pm10变化趋势" + "(" + DateUtils.getLastXDaysDate(31) + "至" +
                        DateUtils.getLastXDaysDate(1) + ")");
                break;
            case 4://co
                for (AirDayHistory.DataBean dataBean : list) {
                    values.add((float) dataBean.getCo());
                    kedu.add(DateUtils.long2DateString2(dataBean.getTime()));
                }
                charTitle.setText(getIntent().getStringExtra("DeviceName").replaceAll("\\d+", "").trim() +
                        ":co变化趋势" + "(" + DateUtils.getLastXDaysDate(31) + "至" +
                        DateUtils.getLastXDaysDate(1) + ")");
                break;
            case 5://so2
                for (AirDayHistory.DataBean dataBean : list) {
                    values.add((float) dataBean.getSo2());
                    kedu.add(DateUtils.long2DateString2(dataBean.getTime()));
                }
                charTitle.setText(getIntent().getStringExtra("DeviceName").replaceAll("\\d+", "").trim() +
                        ":so2变化趋势" + "(" + DateUtils.getLastXDaysDate(31) + "至" +
                        DateUtils.getLastXDaysDate(1) + ")");
                break;
            case 6://no2
                for (AirDayHistory.DataBean dataBean : list) {
                    values.add((float) dataBean.getNo2());
                    kedu.add(DateUtils.long2DateString2(dataBean.getTime()));
                }
                charTitle.setText(getIntent().getStringExtra("DeviceName").replaceAll("\\d+", "").trim() +
                        ":no2变化趋势" + "(" + DateUtils.getLastXDaysDate(31) + "至" +
                        DateUtils.getLastXDaysDate(1) + ")");
                break;
        }
        generateData(values, kedu);
    }


    //更新折线图
    private void generateData(List<Float> randomNumbersTab, List<String> weeks) {

        List<Line> lines = new ArrayList<Line>();
        List<PointValue> values = new ArrayList<PointValue>();
        for (int j = 0; j < randomNumbersTab.size(); ++j) {
            values.add(new PointValue(j, randomNumbersTab.get(j)));
        }
        Line line = new Line(values);
        line.setColor(Color.parseColor("#3967a6"));
        line.setShape(ValueShape.CIRCLE);
        line.setPointRadius(3);
        line.setStrokeWidth(1);
        line.setCubic(false);//设置折线是否平滑
        line.setFilled(false);
        line.setHasLabels(false);
        line.setHasLabelsOnlyForSelected(false);
        line.setHasLines(true);
        line.setHasPoints(true);
        line.setPointColor(Color.parseColor("#3967a6"));
        line.setHasLabels(true);
//        line.setHasLabelsOnlyForSelected(true);//点击数据坐标提示数据（设置了这个line.setHasLabels(true);就无效）
        lines.add(line);
        data = new LineChartData(lines);

        List<AxisValue> mAxisValues = new ArrayList<AxisValue>();
        for (int i = 0; i < weeks.size(); i++) {
            mAxisValues.add(new AxisValue(i).setLabel(weeks.get(i)));
        }
        Axis axisX = new Axis().setTextSize(10).setHasLines(false).setTextColor(Color.parseColor("#666666"));
        axisX.setValues(mAxisValues);  //填充X轴的坐标名称
        axisX.setMaxLabelChars(2);
        Axis axisY = new Axis().setHasLines(false).setTextSize(10).setTextColor(Color.parseColor("#666666"));
//        data.setValueLabelBackgroundColor(Color.parseColor("#00000000"));
        data.setValueLabelBackgroundAuto(true);
        data.setAxisXBottom(axisX);
        data.setAxisYLeft(axisY);
        data.setBaseValue(Float.NEGATIVE_INFINITY);
        chart.setLineChartData(data);
        /**注：下面的7，10只是代表一个数字去类比而已
         * 当时是为了解决X轴固定数据个数。见（http://forum.xda-developers.com/tools/programming/library-hellocharts-charting-library-t2904456/page2）;
         */
        Viewport v = new Viewport(chart.getMaximumViewport());
        v.left = 0;
        v.right = 6;
        chart.setCurrentViewport(v);
    }

    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int i) {
        switch (i) {
            case R.id.rb_aqi:
                refreshData(0);
                break;
            case R.id.rb_pm25:
                refreshData(1);
                break;
            case R.id.rb_o3:
                refreshData(2);
                break;
            case R.id.rb_pm10:
                refreshData(3);
                break;
            case R.id.rb_co:
                refreshData(4);
                break;
            case R.id.rb_so2:
                refreshData(5);
                break;
            case R.id.rb_no2:
                refreshData(6);
                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        OkHttpUtils.getInstance().cancelTag(this);
    }
}
