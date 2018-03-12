package com.mapuni.mobileenvironment.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.mapuni.mobileenvironment.R;
import com.mapuni.mobileenvironment.adapter.ExecuteListAdapter;
import com.mapuni.mobileenvironment.view.MyDecoration;

import java.util.ArrayList;
import java.util.List;

public class SendTaskActivity extends ActivityBase {
    private List<String> data;
    private RecyclerView recycle;
    int before;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_task);
        recycle = (RecyclerView) findViewById(R.id.recycle);
        initView();
        initData();
    }
    private void initView(){
        before = (int) getIntent().getExtras().get("from");
        if(before==ISFROMLIST)
            setTitle(getResources().getString(R.string.btn_execute));
        else if(before==ISFROMSEND)
            setTitle(getResources().getString(R.string.send_title));
        else if(before==ISFROMWAIT)
            setTitle(getResources().getString(R.string.ready_title));

    }
    private void initData(){
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recycle.setLayoutManager(layoutManager);
        layoutManager.setOrientation(OrientationHelper. VERTICAL);
//        recycle.addItemDecoration(new MyDecoration(this,OrientationHelper.VERTICAL),0);
        data = new ArrayList<>();
        data.add("北京木源纸业加工厂");
        data.add("关于中秋节、国庆节放假的通知");
        data.add("北京木源纸业加工厂");
        ExecuteListAdapter adapter = new ExecuteListAdapter(this,data,itemListener);
        recycle.setAdapter(adapter);
    }

    public View.OnClickListener itemListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent();
            intent.putExtra("from",before);
            intent.setClass(SendTaskActivity.this,TaskDetailActivity.class);
            startActivity(intent);
        }
    };
}
