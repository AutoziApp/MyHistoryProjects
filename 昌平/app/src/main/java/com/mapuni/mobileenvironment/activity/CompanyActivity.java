package com.mapuni.mobileenvironment.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.TextView;
import android.widget.Toast;

import com.mapuni.mobileenvironment.R;
import com.mapuni.mobileenvironment.adapter.MyExpandableListViewAdapter;
import com.mapuni.mobileenvironment.config.PathManager;
import com.mapuni.mobileenvironment.model.EntModel;
import com.mapuni.mobileenvironment.utils.JsonUtil;
import com.mapuni.mobileenvironment.view.YutuLoading;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;
import com.zhy.http.okhttp.request.RequestCall;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Request;

public class CompanyActivity extends ActivityBase {
    private ExpandableListView listview;
    ArrayList<EntModel> data;
    private Map<String, List<String>> dataset = new HashMap<>();
    private String[] parentList;
    private MyExpandableListViewAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company);
        initView();
    }
    public void initView(){
        yutuLoading = new YutuLoading(this);
        setTitle(getResources().getString(R.string.tite_pollution));
        listview = (ExpandableListView) findViewById(R.id.expandablelistview);
        search();
        listview.setGroupIndicator(null);
        listview.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView expandableListView, View view,
                                        int parentPos, int childPos, long l) {
                Log.i("Lybin",data.get(parentPos).getEnterprise().get(childPos).getEntCode()+"------code");
                Intent intent = new Intent(CompanyActivity.this,CompanyPollutionDetailActivity.class);
                intent.putExtra("data",data.get(parentPos).getEnterprise().get(childPos));
                startActivity(intent);
                return true;
            }
        });
        listview.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView expandableListView, View view, int i, long l) {
                TextView textView = (TextView) view.findViewById(R.id.parent_title);
                if (textView.isSelected()) {
                    textView.setSelected(false);
                } else {
                    textView.setSelected(true);
                }
                return false;
            }
        });
    }

    private void initList() {
        if(parentList==null)
            parentList = new String[data.size()];
        for(int i=0;i<data.size();i++){
            parentList[i] = data.get(i).getRegionName();
            List<EntModel.EnterpriseBean> list = data.get(i).getEnterprise();
            List<String> childrenList = new ArrayList<>();
            for(int n=0;n<list.size();n++){
                childrenList.add(list.get(n).getEntName());
            }
            dataset.put(parentList[i],childrenList);
        }
        adapter = new MyExpandableListViewAdapter(dataset, parentList, CompanyActivity.this);
        listview.setAdapter(adapter);

    }

    private void search(){
        OkHttpUtils.get().url(PathManager.GetEnterList).build().execute(new StringCallback() {
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
                handle.sendEmptyMessageDelayed(FIRSTBTN,2000);
            }

            @Override
            public void onResponse(String response, int id) {
                yutuLoading.dismissDialog();
                data = JsonUtil.fromJsonList(response, EntModel.class);
                initList();
            }
        });
    }



}
