package com.yutu.car.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import com.yutu.car.R;
import com.yutu.car.adapter.ListActivityAdapter;
import com.yutu.car.bean.CareManageBean;
import com.yutu.car.presenter.BaseControl;
import com.yutu.car.presenter.CareManageControl;
import com.yutu.car.presenter.NetControl;
import com.yutu.car.utils.JsonUtil;
import com.yutu.car.views.YutuLoading;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.Map;

import butterknife.ButterKnife;
import okhttp3.Call;

public class CareSearchActivity extends BaseActivity implements ListView.OnItemClickListener {

    private YutuLoading yutuLoading;
    private CareManageBean bean;
    private NetControl mControl;
    private ListView listView;
    private ListActivityAdapter madapter;
    private Handler handler;
    private boolean is_divPage;
    private ProgressDialog dialog;
    private double totle;
    private ArrayList<CareManageBean> list;
    private String mCarNum;
    private String mVinNum;
    private String mCarNum1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_care_manage);
        ButterKnife.bind(this);
        mCarNum= getIntent().getStringExtra("mCarNum");
         mVinNum = getIntent().getStringExtra("mVinNum");
        mControl = new NetControl();
        yutuLoading = new YutuLoading(this);
        yutuLoading.showDialog();
        initView();
    }

    private void initView() {
        setTitle("车辆信息查询结果", true, true);
        ImageView rightIcon = (ImageView) findViewById(R.id.rightIconA);
        rightIcon.setVisibility(View.GONE);
        listView = (ListView) findViewById(R.id.caremanageList);
        listView.setOnItemClickListener(this);
        rightIcon.setOnClickListener(this);
        mControl.requestSearchForCarManage(mCarNum, mVinNum, searchCall);

    }




    private void setAdapter(ArrayList list) {
        this.list = new ArrayList<>(list);
        madapter = new ListActivityAdapter(this, this.list);
        listView.setAdapter(madapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
//            点击重试
            case R.id.showFailed:
                yutuLoading.showDialog();
//                延时发送
                delayedPost(new Runnable() {
                    @Override
                    public void run() {

                        mControl.requestSearchForCarManage(mCarNum, mVinNum, searchCall);

                    }
                }, 2000);
                requestAgain();
                break;
            case R.id.leftIcon:
                finish();
                break;

        }
    }







    private ArrayList addList(ArrayList list) {
        this.list.addAll(list);
        madapter.notifyDataSetChanged();
        return this.list;
    }

    private int ERROR_FLAGS = 0;
    public StringCallback call = new StringCallback() {
        @Override
        public void onError(Call call, Exception e, int id) {
            yutuLoading.dismissDialog();
            ERROR_FLAGS = 1;
            showFailed();
        }

        @Override
        public void onResponse(String response, int id) {
            Log.d("lvcheng", "response=" + response);
            yutuLoading.dismissDialog();
            Map map = JsonUtil.jsonToMap(response);
            if (map != null) {
                double i = (double) map.get("flag");
                if (map != null && i == 1) {
                    Object obj = map.get("data");
                    String result = JsonUtil.objectToJson(obj);
                    ArrayList list = JsonUtil.fromJsonList(result, CareManageBean.class);
                    Log.d("lvcheng", "list=" + list);
                    if (madapter == null) {
                        setAdapter(list);
                    } else {
                        addList(list);
                    }
                }
            }

            totle = (double) map.get("total");

        }
    };


    public StringCallback searchCall = new StringCallback() {
        @Override
        public void onError(Call call, Exception e, int id) {
            yutuLoading.dismissDialog();
            ERROR_FLAGS = 2;
            showFailed();
        }

        @Override
        public void onResponse(String response, int id) {
            Log.d("lvcheng", "response=" + response);
            yutuLoading.dismissDialog();
            Map map = JsonUtil.jsonToMap(response);
            if (map != null) {
               String flag= map.get("flag").toString().substring(0,1);
                if (map != null && flag.equals("1")) {
                    Object obj = map.get("data");
                    String result = JsonUtil.objectToJson(obj);
                    ArrayList list = JsonUtil.fromJsonList(result, CareManageBean.class);
                    Log.d("lvcheng", "list=" + list);
                    setAdapter(list);
                } else {
                    showFailed();
                }
            }
        }
    };

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Intent intent = new Intent(this, DetailActivity.class);
        BaseControl model = new CareManageControl();
        model.setTitle("车辆详细信息");
        model.setId(list.get(i).getPKID());
        intent.putExtra("class", model);
        startActivity(intent);
    }
}
