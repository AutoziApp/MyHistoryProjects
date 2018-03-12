package com.mapuni.mobileenvironment.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.mapuni.mobileenvironment.R;
import com.mapuni.mobileenvironment.activity.CompanyArchivesActivity;
import com.mapuni.mobileenvironment.model.PagerModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;


public class PagerBaseFragment extends SupportFragment {
    public TabLayout tabLayout;
    public List<String> titleList;
    private PagerModel model;
    private int index;
    private SupportFragment[] viewSet;
    String title;
    Bundle mBundle;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.pager_base_fragment,null);
        mBundle = getArguments();
        model = (PagerModel)mBundle.get("data");
        index = (int) mBundle.get("index");
        title = (String) model.getTitleList(index).get(0);
        initView(view);
        initData();
        return view;
    }
    public void initView(View v){
        tabLayout = (TabLayout) v.findViewById(R.id.tabLayout);
    }
    public void initData(){
        titleList = model.getTitleList(index);
//        change
        if(index==2){
            titleList.clear();
            titleList.addAll(new ArrayList<>(Arrays.asList("污染物排放分析")));
        }
        viewSet =new SupportFragment[titleList.size()];
        for(int i=0;i<titleList.size();i++){
            tabLayout.addTab(tabLayout.newTab().setText(titleList.get(i)));
            PagerDetailFragment fragment = new PagerDetailFragment();
            Bundle bundel = new Bundle();
            bundel.putString("title",titleList.get(i));
            bundel.putSerializable("data",model);
            fragment.setArguments(bundel);
            viewSet[i] = fragment;
        }
        loadFragmentAllHide(getChildFragmentManager(),R.id.flContainer,viewSet);
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                CompanyArchivesActivity _Act = (CompanyArchivesActivity) getActivity();
                showFragment(getChildFragmentManager(),viewSet[tab.getPosition()],_Act.getDetaiCurrentFragment());
                _Act.setDetailCurrentFragment(viewSet[tab.getPosition()]);
            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {}
            @Override
            public void onTabReselected(TabLayout.Tab tab) {}
        });
    }
    public Fragment showFragment(){
        return  viewSet[tabLayout.getSelectedTabPosition()];
    }
}
