package com.mapuni.mobileenvironment.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.mapuni.mobileenvironment.R;
import com.mapuni.mobileenvironment.adapter.ExecuteListAdapter;
import com.mapuni.mobileenvironment.adapter.TaskDetailAdapter;
import com.mapuni.mobileenvironment.utils.DisplayUitl;
import com.mapuni.mobileenvironment.utils.MyDecoration;

import java.util.ArrayList;
import java.util.Map;

public class TaskDetailActivity extends ActivityBase {
    private TextView btnInfo;
    private TextView btnFlow;
    private RecyclerView mRecycle;
    private ArrayList data;
    private boolean isBtnInfo = true;
    TextView tCancel,tSure;
    private TaskDetailAdapter infoAdapter;
    private TaskDetailAdapter flowAdapter;
    private LinearLayout topLayout;
    private PopupWindow mPopwin;
    int before;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_detail);
        initView();
        initData();
        changeSelect();
    }
    private  void initView(){
        before = (int) getIntent().getExtras().get("from");
        if(before==ISFROMLIST)
            setTitle(getResources().getString(R.string.execute_details));
        else if(before==ISFROMSEND)
            setTitle(getResources().getString(R.string.send_title),"督办");
        else if(before==ISFROMWAIT)
            setTitle(getResources().getString(R.string.ready_details),"处理");
        btnFlow = (TextView) findViewById(R.id.flowView);
        btnInfo = (TextView) findViewById(R.id.infoView);
        mRecycle = (RecyclerView) findViewById(R.id.recycle);
        topLayout = (LinearLayout) findViewById(R.id.title_tab);
        btnInfo.setOnClickListener(this);
        btnFlow.setOnClickListener(this);
    }
    private void initData(){
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mRecycle.setLayoutManager(layoutManager);
        layoutManager.setOrientation(OrientationHelper. VERTICAL);
//        mRecycle.addItemDecoration(new MyDecoration(this,OrientationHelper.VERTICAL),0);
        data = new ArrayList<>();
        data.add("北京木源纸业加工厂");
        data.add("昌平白皂化纤厂污染超标");
        data.add("北京木源纸业加工厂");
        infoAdapter = new TaskDetailAdapter(this,data,isBtnInfo);
        mRecycle.setAdapter(infoAdapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.infoView:
                isBtnInfo = true;
                changeSelect();
                changeData();
                break;
            case R.id.flowView:
                isBtnInfo = false;
                changeSelect();
                changeData();
                break;
            case R.id.rightIcon:
                if( before==ISFROMWAIT){
                    startActivity(new Intent(TaskDetailActivity.this,TaskExecuteActivity.class));
                    return;
                }
                View view = LayoutInflater.from(this).inflate(R.layout.watch_dialog_layout,null);
                tCancel = (TextView) view.findViewById(R.id.cancelView);
                tSure = (TextView) view.findViewById(R.id.sureView);
                tCancel.setOnClickListener(this);
                tSure.setOnClickListener(this);
                mPopwin = showView(view,320,280);
                break;
            case R.id.cancelView:
                if (mPopwin!=null)
                    mPopwin.dismiss();
                break;
            case R.id.sureView:
                if (mPopwin!=null)
                    mPopwin.dismiss();
                break;
        }
    }

    public void changeSelect(){
        if(isBtnInfo){
            topLayout.setVisibility(View.GONE);
            btnFlow.setBackgroundResource(R.color.PagerBg);
            btnInfo.setBackgroundResource(R.color.pager_select);

        }else{
            topLayout.setVisibility(View.VISIBLE);
            btnFlow.setBackgroundResource(R.color.pager_select);
            btnInfo.setBackgroundResource(R.color.PagerBg);
        }
    }
    public void changeData(){
        mRecycle.removeAllViews();
        if(isBtnInfo){
            mRecycle.setAdapter(infoAdapter);
        }
        else{
            if(flowAdapter==null){
                flowAdapter = new  TaskDetailAdapter(this,data,isBtnInfo);
            }
            mRecycle.setAdapter(flowAdapter);
        }

    }
}
