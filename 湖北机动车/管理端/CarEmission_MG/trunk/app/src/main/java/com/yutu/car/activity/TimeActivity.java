package com.yutu.car.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.InputType;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.yutu.car.R;
import com.yutu.car.presenter.BaseControl;
import com.yutu.car.presenter.CheckCountControl;
import com.yutu.car.presenter.CheckPassPerControl;
import com.yutu.car.presenter.FirstCheckPassPerControl;
import com.yutu.car.presenter.NetControl;
import com.yutu.car.presenter.SecondCheckPassPerControl;
import com.yutu.car.utils.DataPickDialogUtil;
import com.yutu.car.utils.JsonUtil;
import com.yutu.car.views.YutuLoading;
import com.zhy.http.okhttp.callback.StringCallback;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import okhttp3.Call;

public class TimeActivity extends BaseActivity implements View.OnClickListener{
    private EditText startTime,endTime,con_type,check_stationname;
    private DataPickDialogUtil dataPickDialogUtil;
    private String title;
    private Button search;
    private double dou;
    private ArrayList<HashMap<String, Object>> departCodeDataList;
    private NetControl netControl;
    private YutuLoading yutuLoading;
    private ArrayList<HashMap<String, Object>> departCodeDataList2;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time);
        netControl=new NetControl();

        dataPickDialogUtil=new DataPickDialogUtil(this);
        initView();
    }

    private void initView() {
        setTitle("检测信息统计",true,false);
        startTime=(EditText)findViewById(R.id.start_time);
        endTime=(EditText)findViewById(R.id.end_time);
        con_type=(EditText)findViewById(R.id.con_type);
        con_type.setTag("");
        search=(Button) findViewById(R.id.search_btn);
        check_stationname=(EditText) findViewById(R.id.check_stationname);
        check_stationname.setTag("");
        startTime.setOnClickListener(this);
        endTime.setOnClickListener(this);
        con_type.setOnClickListener(new apartListener());
        search.setOnClickListener(this);
        check_stationname.setOnClickListener(this);
        startTime.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                startTime.setInputType(InputType.TYPE_NULL);
                return false;
            }
        });
        endTime.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                endTime.setInputType(InputType.TYPE_NULL);
                return false;
            }
        });
        con_type.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                con_type.setInputType(InputType.TYPE_NULL);
                return false;
            }
        });

        check_stationname.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                check_stationname.setInputType(InputType.TYPE_NULL);
                return false;
            }
        });

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.leftIcon:
                finish();
                break;
            case R.id.start_time:
                dataPickDialogUtil.dateTimePicKDialog(startTime);
                break;
            case R.id.end_time:
                dataPickDialogUtil.dateTimePicKDialog(endTime);
                break;
            case R.id.check_stationname:
                //yutuLoading = new YutuLoading(this);
               // yutuLoading.showDialog();
                netControl.requestCheckStationName(call);
                break;
            case R.id.search_btn:
                SimpleDateFormat CurrentTime= new SimpleDateFormat("yyyy-MM-dd");
                try{
                    Date beginTime=CurrentTime.parse(startTime.getText().toString());
                    Date endTime1=CurrentTime.parse(endTime.getText().toString());
                    if(endTime1.getTime()<=beginTime.getTime()){
                        Toast.makeText(this,"时间输入有问题，请从新输入",Toast.LENGTH_SHORT).show();
                        return;
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
               if(startTime.getText().toString().equals("")||
                       endTime.getText().toString().equals("")||
                       con_type.getText().toString().equals("")||
                       check_stationname.getText().toString().equals("")){
                   Toast.makeText(this,"所选项不能为空",Toast.LENGTH_LONG).show();
               }else{
                   if(con_type.getTag().equals("1")){
                       intent=new Intent(TimeActivity.this,DetailActivity.class);
                       BaseControl control=new CheckCountControl();
                       control.setTitle("检测数量");
                       control.setId("1");
                       control.setStartTime(startTime.getText().toString());
                       control.setEndTime(endTime.getText().toString());
                       control.setPkid(check_stationname.getTag().toString());
                      intent.putExtra("class",control);
                       startActivity(intent);
                   }else if(con_type.getTag().equals("2")){
                       intent=new Intent(TimeActivity.this,DetailActivity.class);
                       BaseControl control=new CheckPassPerControl();
                       control.setTitle("检测合格率");
                       control.setId("3");
                       control.setStartTime(startTime.getText().toString());
                       control.setEndTime(endTime.getText().toString());
                       control.setPkid(check_stationname.getTag().toString());
                       intent.putExtra("class",control);
                       startActivity(intent);
                   }else if (con_type.getTag().equals("3")){
                       intent=new Intent(TimeActivity.this,DetailActivity.class);
                       BaseControl control=new FirstCheckPassPerControl();
                       control.setTitle("首检合格率");
                       control.setId("4");
                       control.setStartTime(startTime.getText().toString());
                       control.setEndTime(endTime.getText().toString());
                       control.setPkid(check_stationname.getTag().toString());
                       intent.putExtra("class",control);
                       startActivity(intent);
                   }else if(con_type.getTag().equals("4")){
                       intent=new Intent(TimeActivity.this,DetailActivity.class);
                       BaseControl control=new SecondCheckPassPerControl();
                       control.setTitle("复检合格率");
                       control.setId("2");
                       control.setStartTime(startTime.getText().toString());
                       control.setEndTime(endTime.getText().toString());
                       control.setPkid(check_stationname.getTag().toString());
                       intent.putExtra("class",control);
                       startActivity(intent);
                   }



               }
                break;
        }
    }
    private class apartListener implements  View.OnClickListener{

        @Override
        public void onClick(View view) {
            departCodeDataList = new ArrayList<HashMap<String, Object>>();
            HashMap<String, Object> hashMap = new HashMap<String, Object>();
            hashMap.put("depid", "1");
            hashMap.put("depname", "检测数量");
            departCodeDataList.add(hashMap);
            hashMap = new HashMap<String, Object>();
            hashMap.put("depid","2" );
            hashMap.put("depname", "检测合格率");
            departCodeDataList.add(hashMap);
            hashMap = new HashMap<String, Object>();
            hashMap.put("depid", "3");
            hashMap.put("depname", "首检合格率");
            departCodeDataList.add(hashMap);
            hashMap = new HashMap<String, Object>();
            hashMap.put("depid", "4");
            hashMap.put("depname", "复检检合格率");
            departCodeDataList.add(hashMap);
            if (departCodeDataList != null && departCodeDataList.size() > 0) {
                String[] depart = new String[departCodeDataList.size()];
                for (int i = 0; i < departCodeDataList.size(); i++) {
                    depart[i] = departCodeDataList.get(i).get("depname")
                            .toString();
                }
                AlertDialog.Builder malertdialog = new AlertDialog.Builder(
                        TimeActivity.this);
                TextView textView_title = new TextView(TimeActivity.this);
                textView_title
                        .setBackgroundResource(R.drawable.task_kind_title_bg);
                textView_title.setPadding(15, 35, 15, 35);
                textView_title.setTextColor(Color.WHITE);
                textView_title.setTextSize(20);
                textView_title.setText("选择检测类型");
                malertdialog.setCustomTitle(textView_title);
                malertdialog.setItems(depart, malertdialoglistener2);
                malertdialog.show();

            } else {
                Toast.makeText(TimeActivity.this, "不存在检测类型!",Toast.LENGTH_LONG ).show();
            }


        }
    }

    private DialogInterface.OnClickListener malertdialoglistener2 = new DialogInterface.OnClickListener() {
        public void onClick(DialogInterface dialog, int whichButton) {
            if (departCodeDataList != null && departCodeDataList.size() > 0) {
                con_type.setText(departCodeDataList.get(whichButton)
                        .get("depname").toString());
                con_type.setTag(departCodeDataList.get(whichButton)
                        .get("depid").toString());
            }
        }
    };
    public StringCallback call = new StringCallback(){

        @Override
        public void onError(Call call, Exception e, int id) {

        }

        @Override
        public void onResponse(String response, int id) {
            ArrayList<HashMap<String, Object>> objList  =JsonUtil.jsonToListMap(response);
            departCodeDataList2 = new ArrayList<HashMap<String, Object>>();
            HashMap<String, Object> hashMap;
            for (int i = 0; i < objList.size(); i++) {
                hashMap = new HashMap<String, Object>();
                hashMap.put("depid", objList.get(i).get("PKID"));
                hashMap.put("depname", objList.get(i).get("STATIONSHORTNAME"));
                departCodeDataList2.add(hashMap);
            }

            if (departCodeDataList2 != null
                    && departCodeDataList2.size() > 0) {
                String[] depart = new String[departCodeDataList2.size()];
                for (int i = 0; i < departCodeDataList2.size(); i++) {
                    depart[i] = departCodeDataList2.get(i).get("depname")
                            .toString();
                }
                AlertDialog.Builder malertdialog = new AlertDialog.Builder(
                        TimeActivity.this);
                // malertdialog.setTitle("选择监测类型");
                TextView textView_title = new TextView(
                        TimeActivity.this);
                textView_title
                        .setBackgroundResource(R.drawable.task_kind_title_bg);
                textView_title.setPadding(15, 35, 15, 35);
                textView_title.setTextColor(Color.WHITE);
                textView_title.setTextSize(20);
                textView_title.setText("请选择检测站");
                malertdialog.setCustomTitle(textView_title);
                malertdialog.setItems(depart, malertdialoglistener4);
                malertdialog.show();
            } else {
                Toast.makeText(TimeActivity.this, "暂无数据!", Toast.LENGTH_LONG).show();
            }

        }
    };

    private DialogInterface.OnClickListener malertdialoglistener4 = new DialogInterface.OnClickListener() {
        public void onClick(DialogInterface dialog, int whichButton) {
            if (departCodeDataList2 != null && departCodeDataList2.size() > 0) {
                check_stationname.setText(departCodeDataList2.get(whichButton)
                        .get("depname").toString());
                check_stationname.setTag(departCodeDataList2.get(whichButton)
                        .get("depid").toString());
            }
        }
    };
}
