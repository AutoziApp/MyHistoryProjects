package com.mapuni.mobileenvironment.adapter;

/**
 * Created by Mai on 2016/12/5.
 */

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by cg on 2015/9/26.
 */
public class ViewpagerAdapter extends PagerAdapter {

    private List<Fragment> list_fragment;                         //fragment列表
    private List<String> list_Title;                              //tab名的列表


    public ViewpagerAdapter(FragmentManager fm, List<Fragment> list_fragment, List<String> list_Title) {
        this.list_fragment = list_fragment;
        this.list_Title = list_Title;
    }

    @Override
    public int getCount() {
        return list_fragment.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view==object;
    }
}
