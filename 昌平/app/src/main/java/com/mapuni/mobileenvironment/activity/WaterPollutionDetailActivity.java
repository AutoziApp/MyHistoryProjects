package com.mapuni.mobileenvironment.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.ContextThemeWrapper;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mapuni.mobileenvironment.R;
import com.mapuni.mobileenvironment.adapter.PollutionInfoAdapter;
import com.mapuni.mobileenvironment.bean.PollutionMode;
import com.mapuni.mobileenvironment.utils.TestData;
import com.mapuni.mobileenvironment.utils.timepicker.NumericWheelAdapter;
import com.mapuni.mobileenvironment.utils.timepicker.OnWheelScrollListener;
import com.mapuni.mobileenvironment.utils.timepicker.WheelView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

public class WaterPollutionDetailActivity extends ActivityBase{
    private LinearLayout timeLayout;
    private TextView timeView;
    private TextView selectView;
    private LinearLayout selectLayout;
    private TextView searchView;
    private TextView mTitleView;
    private PopupWindow menuWindow;
    private WheelView year;
    private WheelView month;
    private WheelView day;
    private String date = "";
    private ListView mList;
    private ImageView vBack;
    private ImageView vLine;
    PollutionInfoAdapter  mAdapter;
    private List<PollutionMode> mData;
    final String selectWaterData[]={"COD","氨氮"};
    final String selectAirData[]={"S02","NOx"};
    String[] selectStrings;
    Boolean isWaterControll ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
//        @drawable/pic_layer
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_water_pollution_detail);
        initCalendar();
        timeLayout = (LinearLayout) findViewById(R.id.time);
        selectLayout = (LinearLayout) findViewById(R.id.select);
        timeView = (TextView) findViewById(R.id.timeView);
        searchView = (TextView) findViewById(R.id.search);
        selectView = (TextView) findViewById(R.id.selectView);
        mTitleView = (TextView) findViewById(R.id.title);
        mList = (ListView) findViewById(R.id.list);
        vBack = (ImageView) findViewById(R.id.back);
        vLine = (ImageView) findViewById(R.id.line);
        vLine.setOnClickListener(this);
        timeLayout.setOnClickListener(this);
        selectLayout.setOnClickListener(this);
        searchView.setOnClickListener(this);
        vBack.setOnClickListener(this);
        mData =  TestData.getPollutionInfo();
        mAdapter = new PollutionInfoAdapter(this,mData);
        mList.setAdapter(mAdapter);
        initData();
    }
    private void initData(){
        isWaterControll = (Boolean) getIntent().getExtras().get("isWater");
        String string  = (String) getIntent().getExtras().get("name");
//        mTitleView.setText(string);
        setTitle(string,1,1);
        if(!isWaterControll){
            selectView.setText(selectAirData[0]);
        }
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch(v.getId()){
            case R.id.time:
                showPopwindow(getDataPick(0));
                break;
            case R.id.select:
                getDialog(selectStrings);
                break;
            case R.id.search:
                mData.clear();
                mData.addAll(TestData.getPollutionItem(timeView.getText().toString()));
                mAdapter.notifyDataSetChanged();
                break;
            case R.id.leftIcon:
                finish();
                break;
            case R.id.rightIconB:
//                AqiModel model = new AqiModel();
                Intent intent = new Intent(WaterPollutionDetailActivity.this, WaterPollutionChartActivity.class);
//			intent.putExtra("type", 2);
//			intent.putExtra("from", "PollutionActivity");
//			intent.putExtra(CommonUtil.DETAILMODEL, model);
                startActivity(intent);
                break;
            case R.id.set:
                    if(timeView.findViewById(R.id.date).getVisibility()==View.VISIBLE){
                        timeView.setText(getDate(v));
                    }else{
                        timeView.append(" "+getTime());
                    }
                break;
        }
    }


    private void getDialog(final String[] data){
        //dialog参数设置
        AlertDialog.Builder builder=new AlertDialog.Builder( new ContextThemeWrapper(this,R.style.Dialog));
        DialogInterface.OnClickListener interface1 = new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                // TODO Auto-generated method stub
                if(isWaterControll){
                    selectView.setText(selectWaterData[which]);
                }else{
                    selectView.setText(selectAirData[which]);
                }
                dialog.dismiss();
            }
        };

        //先得到构造器
//	        builder.setTitle("监测因子"); //设置标题
//	        builder.setIcon(R.mipmap.ic_launcher);//设置图标，图片id即可
        //设置列表显示，注意设置了列表显示就不要设置builder.setMessage()了，否则列表不起作用。
        if(isWaterControll){
//				selectStrings = selectWaterData;
            builder.setItems(selectWaterData,(android.content.DialogInterface.OnClickListener) interface1);
//			            @Override
////			            public void onClick(DialogInterface dialog, int which) {
////			            	selectView.setText(selectWaterData[which]);
////			                dialog.dismiss();
//			            }
//			        });
        }else{
//				selectStrings = selectAirData;
            builder.setItems(selectAirData,(android.content.DialogInterface.OnClickListener) interface1);
//			            @Override
////			            public void onClick(DialogInterface dialog, int which) {
////			            	selectView.setText(selectAirData[which]);
////			                dialog.dismiss();
        }


        builder.create().show();

    }
}
