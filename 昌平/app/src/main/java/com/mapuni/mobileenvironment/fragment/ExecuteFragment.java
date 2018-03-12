package com.mapuni.mobileenvironment.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.mapuni.mobileenvironment.R;
import com.mapuni.mobileenvironment.activity.AddCheckActivity;
import com.mapuni.mobileenvironment.activity.CheckDetailActivity;
import com.mapuni.mobileenvironment.activity.ExecuteAddActivity;
import com.mapuni.mobileenvironment.activity.SendTaskActivity;
import com.mapuni.mobileenvironment.adapter.MyAdapter;
import com.mapuni.mobileenvironment.model.ItemDetailModel;
import com.mapuni.mobileenvironment.utils.TestData;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.Callback;

import java.util.ArrayList;
import java.util.HashMap;

import okhttp3.Call;
import okhttp3.Response;

public class ExecuteFragment extends SupportFragment implements View.OnClickListener {
    private OnFragmentInteractionListener mListener;
    private RecyclerView mRecycle,mRRecycle;
    private ArrayList<HashMap<String,Object>> checkList;
    private ArrayList<HashMap<String,Object>> executeList;
    private RecyclerView.Adapter checkAdapter,executeAdapter;
    private LinearLayout creatView;
    private LinearLayout sendView;
    private LinearLayout executeListView;
    private LinearLayout waitView;
    private LinearLayout checkView;
    private LinearLayout addCheckView;
    public ExecuteFragment() {
        // Required empty public constructor
    }

    public static ExecuteFragment newFragment() {
        ExecuteFragment fragment = new ExecuteFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_execute, container, false);
        mRecycle = (RecyclerView) view.findViewById(R.id.messageList);
        mRRecycle = (RecyclerView) view.findViewById(R.id.planList);
        initView(view);
        initList();
        return view;
    }
    private void initView(View view){
        creatView =(LinearLayout) view.findViewById(R.id.creatView);
        sendView = (LinearLayout) view.findViewById(R.id.send);
        executeListView = (LinearLayout) view.findViewById(R.id.executeList);
        waitView = (LinearLayout) view.findViewById(R.id.waitView);
        checkView = (LinearLayout) view.findViewById(R.id.checkLayout);
        addCheckView = (LinearLayout) view.findViewById(R.id.addCheck);
        addCheckView.setOnClickListener(this);
        creatView.setOnClickListener(this);
        sendView.setOnClickListener(this);
        executeListView.setOnClickListener(this);
        waitView.setOnClickListener(this);
        checkView.setOnClickListener(this);
    }
    public void initList(){
        checkList = new ArrayList<HashMap<String,Object>>();
        for(int i=0;i<5;i++){
            HashMap<String,Object> map = new HashMap<String,Object>();
            map.put("title","北京木源纸业加工厂");
            checkList.add(map);
        }

        checkAdapter  = new MyAdapter(getActivity(), (ArrayList<ItemDetailModel>) TestData.getXunChaData());
        executeAdapter = new MyAdapter(getActivity(), (ArrayList<ItemDetailModel>) TestData.getChuZhiData());
        LinearLayoutManager layoutManager = new LinearLayoutManager(_mActivity);
        LinearLayoutManager reManager = new LinearLayoutManager(_mActivity);
        mRecycle.setLayoutManager(layoutManager);
        mRRecycle.setLayoutManager(reManager);
        layoutManager.setOrientation(OrientationHelper. VERTICAL);
        reManager.setOrientation(OrientationHelper.VERTICAL);
        mRecycle.setAdapter(checkAdapter);
        mRRecycle.setAdapter(executeAdapter);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onClick(View v) {
        Intent intent = null;
        switch (v.getId()){
            case R.id.creatView:
                intent = new Intent(_mActivity, ExecuteAddActivity.class);
                break;
            case R.id.send:
                intent = new Intent();
                intent.putExtra("from",SendTaskActivity.ISFROMSEND);
                intent.setClass(_mActivity, SendTaskActivity.class);
                break;
            case R.id.executeList:
                intent = new Intent();
                intent.putExtra("from",SendTaskActivity.ISFROMLIST);
                intent.setClass(_mActivity,SendTaskActivity.class);
                break;
            case R.id.waitView:
                intent = new Intent();
                intent.putExtra("from",SendTaskActivity.ISFROMWAIT);
                intent.setClass(_mActivity,SendTaskActivity.class);
                break;
            case R.id.checkLayout:
                intent = new Intent(_mActivity, CheckDetailActivity.class);
                break;
            case R.id.addCheck:
                intent = new Intent(_mActivity, AddCheckActivity.class);
                break;
        }
        startActivity(intent);
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        if(!hidden){
            mListener.onCloseToolbar(false);
            mListener.onChangeTitle(getResources().getString(R.string.tab_execute));
        }
    }

    private void netData(){
        OkHttpUtils.post()
                .url("")
                .addParams("","")
                .build().execute(new Callback() {
                    @Override
                    public Object parseNetworkResponse(Response response, int id) throws Exception {
                        return null;
                    }

                    @Override
                    public void onError(Call call, Exception e, int id) {

                    }

                    @Override
                    public void onResponse(Object response, int id) {

                    }
        });
    }

}
