package com.yutu.car.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

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

public class CareManageActivity extends BaseActivity implements ListView.OnItemClickListener {

    private YutuLoading yutuLoading;
    private CareManageBean bean;
    private NetControl mControl;
    private ListView listView;
    private ListActivityAdapter madapter;
    private Handler handler;
    private boolean is_divPage;
    private ProgressDialog dialog;
    private static int pageNo = 1;
    private double totle;
    private ArrayList<CareManageBean> list;
    private String mCarNum;
    private String mVinNum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_care_manage);
        ButterKnife.bind(this);
        mControl = new NetControl();
        yutuLoading = new YutuLoading(this);
        yutuLoading.showDialog();
        pageNo = 1;
        mControl.requestForCarManage(pageNo, call);
        initView();
    }

    private void initView() {
        setTitle("车辆管理信息", true, true);
        ImageView rightIcon = (ImageView) findViewById(R.id.rightIconA);
        rightIcon.setImageResource(R.mipmap.search);
        listView = (ListView) findViewById(R.id.caremanageList);
        listView.setOnItemClickListener(this);
        rightIcon.setOnClickListener(this);
        ListScroll();

    }

    private void ListScroll() {
        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int scrollState) {
                Log.d("lvcheng", "取证=" + totle / 20);
                if (is_divPage && scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE && pageNo <= totle / 20 + 1) {
                    Toast.makeText(CareManageActivity.this, "正在获取更多数据...", Toast.LENGTH_SHORT).show();

                    handler.sendEmptyMessage(0);

                } else if (pageNo > totle / 20 + 1) {
                    Toast.makeText(CareManageActivity.this, "没有更多数据啦....", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onScroll(AbsListView absListView, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                is_divPage = (firstVisibleItem + visibleItemCount == totalItemCount);

            }
        });

        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if (msg.what == 0) {

                    pageNo++;
                    mControl.requestForCarManage(pageNo, call);
                    Log.d("lvcheng", "pageNo=" + pageNo);
                }
            }
        };

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
                        if (ERROR_FLAGS == 1) {
                            mControl.requestForCarManage(pageNo, call);
                            ;
                        } else {
                            mControl.requestSearchForCarManage(mCarNum, mVinNum, searchCall);
                        }
                    }
                }, 2000);
                requestAgain();
                break;
            case R.id.leftIcon:
                finish();
                break;
            case R.id.rightIconA:
                showSearchDialog();
                break;
        }
    }

    private void showSearchDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = View.inflate(this, R.layout.dialog_car_search,
                null);
        builder.setView(view);
        final AlertDialog dialog = builder.create();
        dialog.show();
        final EditText carNumEdit = (EditText) view.findViewById(R.id.carNumEdit);
        final EditText vinEdit = (EditText) view.findViewById(R.id.vinEdit);
        // 取消按钮
        Button searchBtn = (Button) view.findViewById(R.id.searchBtn);
        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCarNum = carNumEdit.getText().toString().trim();
                mVinNum = vinEdit.getText().toString().trim();
                if (TextUtils.isEmpty(mCarNum)) {
                    Toast.makeText(CareManageActivity.this, "车牌号码不能为空", Toast.LENGTH_SHORT).show();
                }
                if (TextUtils.isEmpty(mVinNum)) {
                    Toast.makeText(CareManageActivity.this, "VIN不能为空", Toast.LENGTH_SHORT).show();
                }
                Intent intent=new Intent(CareManageActivity.this,CareSearchActivity.class);
                intent.putExtra("mCarNum",mCarNum);
                intent.putExtra("mVinNum",mVinNum);
                startActivity(intent);
//                yutuLoading.showDialog();
//                mControl.requestSearchForCarManage(mCarNum, mVinNum, searchCall);
                dialog.dismiss();
            }
        });
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
