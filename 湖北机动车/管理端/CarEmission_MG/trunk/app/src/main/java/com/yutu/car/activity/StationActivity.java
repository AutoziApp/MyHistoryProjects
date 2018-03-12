package com.yutu.car.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;

import com.yutu.car.R;
import com.yutu.car.adapter.ExpandaListAdapter;
import com.yutu.car.bean.AllStationBean;
import com.yutu.car.presenter.BaseControl;
import com.yutu.car.presenter.ExpandaListControl;
import com.yutu.car.presenter.NetControl;
import com.yutu.car.presenter.StationInfoControl;
import com.yutu.car.utils.JsonUtil;
import com.yutu.car.views.YutuLoading;
import com.zhy.http.okhttp.callback.StringCallback;

import okhttp3.Call;

import static com.yutu.car.R.layout.item;

public class StationActivity extends BaseActivity implements ExpandableListView.OnChildClickListener {
    private ExpandableListView exList;
    private ExpandaListAdapter mAdapter;
//    private AllStationBean bean;
    private Context context;
    private YutuLoading yutuLoading;
    private ExpandaListControl listControl;
    private NetControl mControl = new NetControl();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_station);
        listControl = (ExpandaListControl) getIntent().getExtras().get("control");
        yutuLoading=new YutuLoading(this);
        yutuLoading.showDialog();
        initView();
        listControl.requestNet(call);
    }
    private void initView(){
        setTitle(listControl.getTitle(),true,false);
        exList = (ExpandableListView) findViewById(R.id.exList);
        exList.setOnChildClickListener(this);
    }
    private void setAdapter(){

        if(listControl == null){
            return;
        }
        mAdapter = new ExpandaListAdapter(this,listControl);
        exList.setAdapter(mAdapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
//            点击重试
            case R.id.showFailed:
                yutuLoading.showDialog();
//                延时发送
                delayedPost(new Runnable() {
                    @Override
                    public void run() {
                        listControl.requestNet(call);
                    }
                },2000);
                requestAgain();
                break;
            case R.id.leftIcon:
                finish();
                break;
        }
    }
    public StringCallback call = new StringCallback() {
        @Override
        public void onError(Call call, Exception e, int id) {
            yutuLoading.dismissDialog();
            showFailed();
        }

        @Override
        public void onResponse(String response, int id) {
            Log.d("lvcheng","站点response="+response);
//            bean = (AllStationBean) JsonUtil.jsonToBean(response,AllStationBean.class);
            int flag = listControl.jsonToBean(response);
            yutuLoading.dismissDialog();
            if(flag==1){
                setAdapter();
            }else{
                showFailed();
            }
        }
    } ;

    @Override
    public boolean onChildClick(ExpandableListView expandableListView, View view, int groupPosition, int childPosition, long l) {
        Intent intent = new Intent(this, DetailActivity.class);
        BaseControl model = listControl.getDetailControl();
        ExpandableListAdapter adapter = expandableListView.getExpandableListAdapter();
        ExpandaListControl.ExpandaListItem item = (ExpandaListControl.ExpandaListItem) adapter.getChild(groupPosition, childPosition);
//        ExpandaListControl.ExpandaListItem item = (ExpandaListControl.ExpandaListItem) view.getTag(R.id.childText);
        model.setId(item.getId());
        model.setTitle(item.getTitle());
        intent.putExtra("class",model);
        startActivity(intent);
        return false;
    }
}
