package com.yutu.car.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.yutu.car.R;
import com.yutu.car.adapter.NewEnProListActivityAdapter;
import com.yutu.car.bean.EnvironmentaProtectionBean;
import com.yutu.car.bean.NewEnvironmentaProtectionBean;
import com.yutu.car.presenter.BaseControl;
import com.yutu.car.presenter.NetControl;
import com.yutu.car.presenter.NewEnProControl;
import com.yutu.car.utils.JsonUtil;
import com.yutu.car.views.YutuLoading;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.ButterKnife;
import okhttp3.Call;

public class EnvironmentaProtectionActivity extends BaseActivity implements AdapterView.OnItemClickListener {
    private YutuLoading yutuLoading;
    private NetControl mControl;
    private ListView listView;
    private EnvironmentaProtectionBean bean;
//    private EnProListActivityAdapter madapter;
    private NewEnProListActivityAdapter madapter;
    private String startTime, endTime;
    private Handler handler;
    private boolean is_divPage;
    private ProgressDialog dialog;
    private static int pageNo = 1;
    private double totle;
//    private ArrayList<EnvironmentaProtectionBean> list;
    private ArrayList<NewEnvironmentaProtectionBean> list;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_environmenta_protection);
        ButterKnife.bind(this);
        Intent _intent = getIntent();
        startTime = _intent.getStringExtra("startTime");
        endTime = _intent.getStringExtra("endTime");
        mControl = new NetControl();
        mControl.requestForEnPrList(pageNo, startTime, endTime, call);
        yutuLoading = new YutuLoading(this);
        yutuLoading.showDialog();
        initView();

    }

    private void initView() {
        setTitle("环保检测核发信息", true, false);
        listView = (ListView) findViewById(R.id.caremanageList);
        listView.setOnItemClickListener(this);
        ListScroll();
    }

    private void ListScroll() {
        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int scrollState) {
                Log.d("lvcheng", "取证=" + totle / 20);
                if (is_divPage && scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE && pageNo <= totle / 20 + 1) {
                    Toast.makeText(EnvironmentaProtectionActivity.this, "正在获取更多数据...", Toast.LENGTH_SHORT).show();

                    handler.sendEmptyMessage(0);

                } else if (pageNo > totle / 20 + 1) {
                    Toast.makeText(EnvironmentaProtectionActivity.this, "没有更多数据啦....", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onScroll(AbsListView absListView, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                is_divPage = (firstVisibleItem + visibleItemCount == totalItemCount);

            }
        });
       /* handler=new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if(msg.what==0){
                    madapter.notifyDataSetChanged();
                }else if(listView.getFooterViewsCount()!=0)
                {
                    listView.removeFooterView(v);
                    Toast.makeText(getActivity(), "数据全部加载完!", Toast.LENGTH_LONG).show();

                }
            }
        };*/
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if (msg.what == 0) {

                    pageNo++;
                    mControl.requestForEnPrList(pageNo, startTime, endTime, call);
                    Log.d("lvcheng", "pageNo=" + pageNo);
                }
            }
        };

    }

    private void setAdapter(ArrayList list) {
        this.list = new ArrayList<>(list);
        madapter = new NewEnProListActivityAdapter(this, this.list);
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
                        mControl.requestForEnPrList(pageNo, startTime, endTime, call);
                    }
                }, 2000);
                requestAgain();
                break;
            case R.id.leftIcon:
                finish();
                break;
        }
    }


    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Intent intent = new Intent(this, DetailActivity.class);
//        BaseControl model = new EnProControl();
        BaseControl model = new NewEnProControl();
        model.setTitle("环保检测详细信息");
        model.setId(list.get(i).getJCBGDBH());
        // model.setPkid(list.get(i).getPKID());
        intent.putExtra("class", model);
        startActivity(intent);
    }

    private ArrayList addList(ArrayList list) {
        this.list.addAll(list);
        madapter.notifyDataSetChanged();
        return this.list;
    }

    public StringCallback call = new StringCallback() {
        @Override
        public void onError(Call call, Exception e, int id) {
            yutuLoading.dismissDialog();
            showFailed();
        }

        @Override
        public void onResponse(String response, int id) {
            yutuLoading.dismissDialog();
            try {
                JSONObject jsonObject=new JSONObject(response);
                double i = jsonObject.optDouble("flag",0);
                if (i == 1) {
                    String result = jsonObject.getString("data");
                    ArrayList list = JsonUtil.fromJsonList(result, NewEnvironmentaProtectionBean.class);
                    Log.d("lvcheng", "list=" + list);
                    if (madapter == null) {
                        setAdapter(list);
                    } else {
                        addList(list);
                    }
                }else{
                    showFailed();
                }
                totle = jsonObject.optDouble("total",0);
            } catch (JSONException e) {
                e.printStackTrace();
                showFailed();
            }

        }
//        @Override
//        public void onResponse(String response, int id) {
//            yutuLoading.dismissDialog();
//            Map map = JsonUtil.jsonToMap(response);
//            if (map != null) {
//                double i = (double) map.get("flag");
//                if (map != null && i == 1) {
//                    Object obj = map.get("data");
//                    String result = JsonUtil.objectToJson(obj);
//                    ArrayList list = JsonUtil.fromJsonList(result, NewEnvironmentaProtectionBean.class);
//                    Log.d("lvcheng", "list=" + list);
//                    if (madapter == null) {
//                        setAdapter(list);
//                    } else {
//                        addList(list);
//                    }
//                }else{
//                    showFailed();
//                }
//            }else {
//                showFailed();
//            }
//
//
//            totle = (double) map.get("total");
//
//        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        pageNo=1;
    }
}
