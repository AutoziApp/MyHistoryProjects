package com.mapuni.mobileenvironment.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.mapuni.mobileenvironment.R;
import com.mapuni.mobileenvironment.adapter.CheckAdapter;
import com.mapuni.mobileenvironment.utils.MyDecoration;
import com.mapuni.mobileenvironment.widget.NiceSpinner;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class AddCheckActivity extends ActivityBase implements NiceSpinner.IconChange{
//    所属大队
    private NiceSpinner spinnerA;
//    案件来源
    private NiceSpinner spinnerB;
//    是否需要处置
    private NiceSpinner spinnerC;
//    监督员
    private NiceSpinner spinnerD;
//    分类
    private NiceSpinner spinnerE;
    private TextView timeView;
    private TextView cancelView;
    private TextView commitView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_add);
        initView();
        initData();
        initCalendar();
        initSpinner();
    }
    private void initView(){
        setTitle("添加台账");
        spinnerA = (NiceSpinner) findViewById(R.id.spinnerA);
        spinnerB = (NiceSpinner) findViewById(R.id.spinnerB);
        spinnerC = (NiceSpinner) findViewById(R.id.spinnerC);
        spinnerD = (NiceSpinner) findViewById(R.id.spinnerD);
        spinnerE = (NiceSpinner) findViewById(R.id.spinnerE);
        timeView = (TextView) findViewById(R.id.timeView);
        cancelView = (TextView) findViewById(R.id.cancel);
        commitView = (TextView) findViewById(R.id.commit);
        cancelView.setOnClickListener(this);
        commitView.setOnClickListener(this);
        timeView.setOnClickListener(this);
    }
    private void initData(){

    }
    public void initSpinner(){
        List<String> dataA = new LinkedList<>(Arrays.asList("监察一队", "监察二队"));
        List<String> dataB = new LinkedList<>(Arrays.asList("群众举报", "领导交办","巡查发现"));
        List<String> dataC = new LinkedList<>(Arrays.asList("是", "否"));
        List<String> dataD = new LinkedList<>(Arrays.asList("姚飞", "张振国"));
        List<String> dataE = new LinkedList<>(Arrays.asList("水", "气","声","污染源"));

        spinnerA.attachDataSource(dataA);
        spinnerB.attachDataSource(dataB);
        spinnerC.attachDataSource(dataC);
        spinnerD.attachDataSource(dataD);
        spinnerE.attachDataSource(dataE);
        spinnerA.setIconChange(this);
        spinnerB.setIconChange(this);
        spinnerC.setIconChange(this);
        spinnerD.setIconChange(this);
        spinnerE.setIconChange(this);
    }
    @Override
    public void onClick(View view){
        switch (view.getId()){
            case R.id.timeView:
                showPopwindow(getDataPick(R.id.end_time));
                break;
            case R.id.set:
                if(super.timeView.findViewById(R.id.date).getVisibility()==View.VISIBLE){
                    timeView.setText(getDate(view));
                }else{
                    timeView.append(" "+getTime());
                }
                break;
            case R.id.cancel:
                finish();
                break;
            case R.id.commit:
                finish();
                break;
        }
    }

    @Override
    public void spinner() {
        Log.i("Lybin","spinner");
    }

    @Override
    public void spinnerPress() {
        Log.i("Lybin","spinnerPress");
    }
}
