package com.mapuni.mobileenvironment.adapter;

/**
 * Created by Mai on 2016/12/5.
 */

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.mapuni.mobileenvironment.activity.CompanyArchivesActivity;
import com.mapuni.mobileenvironment.model.PagerModel;

import java.util.List;

/**
 * Created by cg on 2015/9/26.
 */
public class TabViewpagerAdapter extends FragmentStatePagerAdapter {

    private List<Fragment> list_fragment;                         //fragment列表
    private List<String> list_Title;                              //tab名的列表
    private Context context;

    public TabViewpagerAdapter(FragmentManager fm, List<Fragment> list_fragment, List<String> list_Title,Context context) {
        super(fm);
        this.list_fragment = list_fragment;
        this.list_Title = list_Title;
        this.context = context;
    }

    @Override
    public Fragment getItem(int position) {
        return list_fragment.get(position);
    }

    @Override
    public int getCount() {
        return list_Title.size();
    }

    @Override
    public int getItemPosition(Object object) {
        return PagerAdapter.POSITION_NONE;
    }

    //此方法用来显示tab上的名字
    @Override
    public CharSequence getPageTitle(int position) {

        return list_Title.get(position % list_Title.size());
    }
}
