package com.mapuni.mobileenvironment.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.mapuni.mobileenvironment.R;
import com.mapuni.mobileenvironment.widget.NiceSpinner;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class TaskTurnActivity extends ActivityBase {
    private NiceSpinner mSpinearA;
    private NiceSpinner mSpinearB;
    private TextView endTime;
    private TextView mExecute;
    private EditText mEdit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.popwindow_task_turn);
        initView();
        initCalendar();
        initSpinner();
    }
    private void initView(){
        mSpinearA = (NiceSpinner) findViewById(R.id.spinnerA);
        mSpinearB = (NiceSpinner) findViewById(R.id.spinnerB);
        endTime = (TextView) findViewById(R.id.end_time);
        mExecute = (TextView) findViewById(R.id.execute);
        mEdit = (EditText) findViewById(R.id.editText);
        endTime.setOnClickListener(this);
        mExecute.setOnClickListener(this);
    }
    private void initSpinner(){
        NiceSpinner.IconChange  ichange = new NiceSpinner.IconChange() {
            @Override
            public void spinner() {}
            @Override
            public void spinnerPress() {}
        };
        List<String> teamData = new LinkedList<>(Arrays.asList("局领导",
                "监察一队","监察二队"));
        List<String> executorData = new LinkedList<>(Arrays.asList("王会",
                "邹振坤","张振国"));
        mSpinearB.attachDataSource(executorData);
        mSpinearA.attachDataSource(teamData);
        mSpinearB.setIconChange(ichange);
        mSpinearA.setIconChange(ichange);
    }
    @Override
    public void onClick(View view){
        switch (view.getId()){
            case R.id.end_time:
                showPopwindow(getDataPick(R.id.end_time));
                break;
            case R.id.execute:
                finish();
                break;
            case R.id.set:
                if(timeView.findViewById(R.id.date).getVisibility()==View.VISIBLE){
                    endTime.setText(getDate(view));
                }else{
                    endTime.append(" "+getTime());
                }
                break;
        }
    }

}
