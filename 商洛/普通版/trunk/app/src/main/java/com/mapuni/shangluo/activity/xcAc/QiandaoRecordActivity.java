package com.mapuni.shangluo.activity.xcAc;

import android.graphics.Color;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bigkoo.pickerview.TimePickerView;
import com.google.gson.Gson;
import com.lin.timeline.TimeLineDecoration;
import com.mapuni.shangluo.R;
import com.mapuni.shangluo.activity.BaseActivity;
import com.mapuni.shangluo.activity.xcAc.timeline.Analog;
import com.mapuni.shangluo.activity.xcAc.timeline.AnalogAdapter;
import com.mapuni.shangluo.bean.QianDaoRecordBean;
import com.mapuni.shangluo.manager.NetManager;
import com.mapuni.shangluo.utils.SPUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import okhttp3.Call;

import static com.lin.timeline.TimeLineDecoration.BEGIN;
import static com.lin.timeline.TimeLineDecoration.END_FULL;
import static com.lin.timeline.TimeLineDecoration.NORMAL;

public class QiandaoRecordActivity extends BaseActivity implements View.OnClickListener {

    private String  sessionId;
    private Calendar originalCalendar,preCalendar;

    //控件
    private ImageView ivLeft,ivRight;
    private TextView dateTitle,tvNoData;
    private RecyclerView recyclerView;
    AnalogAdapter adapter;


    @Override
    public int setLayoutResID() {
        return R.layout.activity_qiandao_record;
    }

    @Override
    public void initView() {
        setToolbarTitle("签到记录");
        ivLeft= (ImageView) findViewById(R.id.left_button);
        ivRight= (ImageView) findViewById(R.id.right_button);
        dateTitle= (TextView) findViewById(R.id.date_title);
        tvNoData= (TextView) findViewById(R.id.tv_noData);
        recyclerView= (RecyclerView) findViewById(R.id.recyclerView);
        ivLeft.setOnClickListener(this);
        ivRight.setOnClickListener(this);
        dateTitle.setOnClickListener(this);
        initCarlender();
        initTimeLine();
    }

    private void initTimeLineData(List<QianDaoRecordBean.RowsBean> list) {
        final List<Analog> analogs = new ArrayList<>();

        for (int i=0;i<list.size();i++){
            Analog analog=new Analog();
            analog.isHead=(i==0)?true:false;
            analog.text=list.get(i).getAddress();
            analog.time=list.get(i).getCreateTime();
            analogs.add(analog);
        }

        adapter.setItems(analogs);
    }

    private void initTimeLine() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        final TimeLineDecoration decoration = new TimeLineDecoration(this)
                .setLineColor(R.color.Grey_500)
                .setLineWidth(1)
                .setLeftDistance(16)
                .setTopDistance(16)
                .setBeginMarker(R.drawable.begin_marker)
                .setMarkerRadius(4)
                .setMarkerColor(R.color.colorAccent)
                .setCallback(new TimeLineDecoration.TimeLineAdapter() {

                    @Nullable
                    @Override
                    public Rect getRect(int position) {
                        return new Rect(0, 16, 0, 16);
                    }

                    @Override
                    public int getTimeLineType(int position) {
                        if (position == 0) return BEGIN;
                        else if (position == adapter.getItemCount() - 1) return END_FULL;
                        else return NORMAL;
                    }
                });
        recyclerView.addItemDecoration(decoration);

        adapter = new AnalogAdapter(this);
        recyclerView.setAdapter(adapter);
    }

    private void initCarlender() {
        Calendar now = Calendar.getInstance();
        originalCalendar=Calendar.getInstance();
        preCalendar=now;
        dateTitle.setText(now.get(Calendar.YEAR)+"/"+formatNum(now.get(Calendar.MONTH)+1));

    }

    @Override
    public void initData() {
        sessionId = (String) SPUtils.getSp(this, "sessionId", "");
        getData("");
    }

    private void getData(String date) {
        NetManager.getQiandaoRecord(sessionId, date,new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                Toast.makeText(QiandaoRecordActivity.this,"获取数据失败",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(String response, int id) {
                QianDaoRecordBean bean=new Gson().fromJson(response,QianDaoRecordBean.class);
                List<QianDaoRecordBean.RowsBean> list=bean.getRows();
                if(list!=null && list.size()>0){
                    recyclerView.setVisibility(View.VISIBLE);
                    tvNoData.setVisibility(View.GONE);
                    initTimeLineData(list);
                }else {//无数据
                    recyclerView.setVisibility(View.GONE);
                    tvNoData.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.left_button:
                preCalendar.add(Calendar.MONTH,-1);
                dateTitle.setText(preCalendar.get(Calendar.YEAR)+"/"+formatNum(preCalendar.get(Calendar.MONTH)+1));
                getData(preCalendar.get(Calendar.YEAR)+""+formatNum(preCalendar.get(Calendar.MONTH)+1));
                break;
            case R.id.right_button:
                preCalendar.add(Calendar.MONTH,1);
                dateTitle.setText(preCalendar.get(Calendar.YEAR)+"/"+formatNum(preCalendar.get(Calendar.MONTH)+1));
                getData(preCalendar.get(Calendar.YEAR)+""+formatNum(preCalendar.get(Calendar.MONTH)+1));
                break;
            case R.id.date_title:
                openTimePicker();
                break;
        }
        compareTime();
    }

    private void openTimePicker() {
        Calendar selectedDate = Calendar.getInstance();
        Calendar startDate = Calendar.getInstance();
        Calendar endDate = Calendar.getInstance();
        //正确设置方式 原因：注意事项有说明
        startDate.set(2010,0,1);

       TimePickerView pvTime = new TimePickerView.Builder(this, new TimePickerView.OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {//选中事件回调
                DateFormat f = new SimpleDateFormat("yyyy/MM");
                String strDate=f.format(date);
                dateTitle.setText(strDate);
                Calendar tempCal=Calendar.getInstance();
                tempCal.setTime(date);
                preCalendar.set(Calendar.YEAR,tempCal.get(Calendar.YEAR));
                preCalendar.set(Calendar.MONTH,tempCal.get(Calendar.MONTH));

                getData(strDate.replace("/",""));
                compareTime();
            }
        })
                .setType(new boolean[]{true, true, false, false, false, false})// 默认全部显示
                .setCancelText("取消")//取消按钮文字
                .setSubmitText("确定")//确认按钮文字
                .setContentSize(18)//滚轮文字大小
                .setTitleSize(20)//标题文字大小
//                .setTitleText("Title")//标题文字
                .setOutSideCancelable(true)//点击屏幕，点在控件外部范围时，是否取消显示
                .isCyclic(false)//是否循环滚动
                .setTitleColor(Color.BLACK)//标题文字颜色
                .setSubmitColor(Color.BLACK)//确定按钮文字颜色
                .setCancelColor(Color.BLACK)//取消按钮文字颜色
                .setTitleBgColor(Color.WHITE)//标题背景颜色 Night mode
                .setBgColor(Color.WHITE)//滚轮背景颜色 Night mode
                .setDate(selectedDate)// 如果不设置的话，默认是系统时间*/
                .setRangDate(startDate,endDate)//起始终止年月日设定
                .setLabel("年","月","日","时","分","秒")//默认设置为年月日时分秒
                .isCenterLabel(true) //是否只显示中间选中项的label文字，false则每项item全部都带有label。
                .isDialog(true)//是否显示为对话框样式
                .build();
        pvTime.show();
    }

    private void compareTime(){
        int res=originalCalendar.compareTo(preCalendar);
        switch (res){
            case -1://o比p早
                ivRight.setVisibility(View.GONE);
                break;
            case 0://一样
                ivRight.setVisibility(View.GONE);
                break;
            case 1://o比p晚
                ivRight.setVisibility(View.VISIBLE);
                break;
        }
    }

    private String formatNum(int n){

        if(n>=0 && n<10){
            return "0"+n;
        }else {
            return ""+n;
        }

    }
}
