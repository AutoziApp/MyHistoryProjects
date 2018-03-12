package com.mapuni.mobileenvironment.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mapuni.mobileenvironment.R;
import com.mapuni.mobileenvironment.adapter.CheckAdapter;
import com.mapuni.mobileenvironment.utils.MyDecoration;
import com.mapuni.mobileenvironment.widget.NiceSpinner;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class CheckDetailActivity extends ActivityBase {
    RecyclerView mRecycle;
    private LinearLayout searchLayout;
    private LinearLayout detailSearchLayout;
    private LinearLayout titleLayout;
    private LinearLayout detailTitleLayout;
    private TextView tOrder;
    private TextView tAdd;
    private NiceSpinner mSpinner;
    private View currentView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check);
        initView();
        initSpinner();
        initData();
    }
    private void initView(){
        setTitle("台账详情");
        mRecycle = (RecyclerView) findViewById(R.id.recycle);
        searchLayout = (LinearLayout) findViewById(R.id.searchLayout);
        detailSearchLayout = (LinearLayout) findViewById(R.id.detailSearchLayout);
        titleLayout = (LinearLayout) findViewById(R.id.titleLayout);
        detailTitleLayout = (LinearLayout) findViewById(R.id.detail_title);
        tOrder = (TextView) findViewById(R.id.orderView);
        mSpinner = (NiceSpinner) findViewById(R.id.spinner);
        tAdd = (TextView) findViewById(R.id.addIcon);
        tAdd.setOnClickListener(this);
        tOrder.setOnClickListener(this);
        tOrder.setVisibility(View.VISIBLE);
        titleLayout.setVisibility(View.GONE);
        detailTitleLayout.setVisibility(View.VISIBLE);
        searchLayout.setVisibility(View.GONE);
        detailSearchLayout.setVisibility(View.VISIBLE);

    }
    private void initData(){
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mRecycle.setLayoutManager(layoutManager);
        layoutManager.setOrientation(OrientationHelper. VERTICAL);
        mRecycle.addItemDecoration(new MyDecoration(this,OrientationHelper.VERTICAL),0);
        CheckAdapter adapter = new CheckAdapter(this,clickListener,longClick,true);
        mRecycle.setAdapter(adapter);

    }
    public void initSpinner(){
            List<String> data = new LinkedList<>(Arrays.asList("是",
                    "否"));
            mSpinner.attachDataSource(data);
            mSpinner.setIconChange(new NiceSpinner.IconChange() {
                @Override
                public void spinner() {
                }
                @Override
                public void spinnerPress() {
                }
            });
    }
    @Override
    public void onClick(View view){
        switch (view.getId()){
            case R.id.addIcon:

                startActivity(new Intent(CheckDetailActivity.this,AddCheckActivity.class));
                break;
            case R.id.orderView:
                if(currentView!=null){
                    boolean isselect = (boolean) currentView.getTag(R.id.tag_first);
                    if(isselect==true){
                        isselect=false;
                        currentView.setTag(R.id.tag_first,isselect);
                        currentView.setBackgroundColor(getResources().getColor(R.color.item_bg));
                        startActivity(new Intent(CheckDetailActivity.this,ExecuteAddActivity.class));
                        break;
                    }
                }
                Toast.makeText(CheckDetailActivity.this,"请长按选择台账",Toast.LENGTH_SHORT).show();
                break;
        }
    }

    View.OnClickListener clickListener  = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            startActivity(new Intent(CheckDetailActivity.this,AddCheckActivity.class));
        }
    };
    View.OnLongClickListener longClick = new View.OnLongClickListener(){
        @Override
        public boolean onLongClick(View v) {
            currentView = v;
            currentView.setTag(R.id.tag_first,true);
            v.setBackground(getResources().getDrawable(R.drawable.air1));
            return true;
        }
    };
}
