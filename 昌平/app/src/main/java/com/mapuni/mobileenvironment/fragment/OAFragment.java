package com.mapuni.mobileenvironment.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mapuni.mobileenvironment.R;

import java.util.ArrayList;
import java.util.HashMap;

public class OAFragment extends SupportFragment {

    private OnFragmentInteractionListener mListener;
    private RecyclerView mRecycle,mRRecycle;
    private ArrayList<HashMap<String,Object>> messageList;
    private ArrayList<HashMap<String,Object>> planList;
    private RecyclerView.Adapter messageAdapter,planAdapter;
    public OAFragment() {
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        if(!hidden){
            mListener.onCloseToolbar(false);
            mListener.onChangeTitle(getResources().getString(R.string.tab_oa));
        }
//        super.onHiddenChanged(hidden);
    }


    public static OAFragment newFragment() {
        OAFragment fragment = new OAFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_oa, container, false);

        mRecycle = (RecyclerView) view.findViewById(R.id.checkList);
        mRRecycle= (RecyclerView) view.findViewById(R.id.executeList);
        initList();
        return view;

    }

    public void initList(){
        planList = new ArrayList<HashMap<String,Object>>();
        for(int i=0;i<5;i++){
            HashMap<String,Object> map = new HashMap<String,Object>();
            map.put("title","关于中秋节、国庆节放假的通知");
            planList.add(map);
        }
//        planAdapter  = new MyAdapter(_mActivity,planList);
//        messageAdapter = new MyAdapter(_mActivity,planList);
        LinearLayoutManager layoutManager = new LinearLayoutManager(_mActivity);
        LinearLayoutManager reManager = new LinearLayoutManager(_mActivity);
        mRecycle.setLayoutManager(layoutManager);
        mRRecycle.setLayoutManager(reManager);
        layoutManager.setOrientation(OrientationHelper. VERTICAL);
        reManager.setOrientation(OrientationHelper.VERTICAL);
        mRecycle.setAdapter(planAdapter);
        mRRecycle.setAdapter(messageAdapter);
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
    }

}
