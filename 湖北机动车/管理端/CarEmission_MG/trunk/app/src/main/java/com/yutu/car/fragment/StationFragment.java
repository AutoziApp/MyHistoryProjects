package com.yutu.car.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;

import com.yutu.car.R;
import com.yutu.car.activity.DetailActivity;
import com.yutu.car.adapter.ExpandaListAdapter;
import com.yutu.car.bean.AllStationBean;
import com.yutu.car.presenter.BaseControl;
import com.yutu.car.presenter.ExpandaListControl;
import com.yutu.car.presenter.StationInfoControl;
import com.yutu.car.utils.JsonUtil;
import com.zhy.http.okhttp.callback.StringCallback;
import okhttp3.Call;


public class StationFragment extends BaseFragment implements ExpandableListView.OnChildClickListener{
    private ExpandableListView exList;
    private ExpandaListAdapter mAdapter;
    private AllStationBean bean;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_station, container, false);
        yutuLoading.showDialog();
        initView(view);
        mControl.requestForAllStation(call);
        return view;
    }

    public static StationFragment newInstance(String s) {
        StationFragment fragment = new StationFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    private void initView(View view){
        setTitle(view,"站点信息");
        exList = (ExpandableListView) view.findViewById(R.id.exList);
        exList.setOnChildClickListener(this);
    }

    private void setAdapter(){

        if(bean == null){
            return;
        }
//        mAdapter = new ExpandaListAdapter(mAct,bean);
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
                        mControl.requestForAllStation(call);
                    }
                },2000);
                requestAgain();
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
            bean = (AllStationBean) JsonUtil.jsonToBean(response,AllStationBean.class);
            yutuLoading.dismissDialog();
            if(bean!=null&&bean.getResult()==1){
                setAdapter();
            }else{
                showFailed();
            }
        }
    } ;

    @Override
    public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
        Intent intent = new Intent(mAct, DetailActivity.class);
        BaseControl model = new StationInfoControl();
        ExpandaListControl.ExpandaListItem item = (ExpandaListControl.ExpandaListItem) v.getTag(R.id.childText);
        model.setId(item.getId());
        model.setTitle(item.getTitle());
        intent.putExtra("class",model);
        startActivity(intent);
        return false;
    }
}
