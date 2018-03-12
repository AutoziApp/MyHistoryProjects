package com.mapuni.mobileenvironment.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.mapuni.mobileenvironment.R;
import com.mapuni.mobileenvironment.adapter.CheckAdapter;
import com.mapuni.mobileenvironment.adapter.ExecuteListAdapter;
import com.mapuni.mobileenvironment.utils.MyDecoration;

import java.util.ArrayList;

public class CheckActivity extends ActivityBase {
    TextView line;
    RecyclerView mRecycle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check);
        setTitle("巡查台账");
        line = (TextView) findViewById(R.id.line);
        mRecycle = (RecyclerView) findViewById(R.id.recycle);
        initData();
    }
    private void initData(){
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mRecycle.setLayoutManager(layoutManager);
        layoutManager.setOrientation(OrientationHelper. VERTICAL);
        mRecycle.addItemDecoration(new MyDecoration(this,OrientationHelper.VERTICAL),0);
        CheckAdapter adapter = new CheckAdapter(this,listener,false);
        mRecycle.setAdapter(adapter);
    }
    private View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            startActivity(new Intent(CheckActivity.this,CheckDetailActivity.class));
        }
    };
}
