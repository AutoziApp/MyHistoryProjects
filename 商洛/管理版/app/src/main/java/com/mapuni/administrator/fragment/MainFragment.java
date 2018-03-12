package com.mapuni.administrator.fragment;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.mapuni.administrator.R;
import com.mapuni.administrator.utils.SPUtils;
import com.mapuni.administrator.view.BottomBar;
import com.mapuni.administrator.view.BottomBarTab;


public class MainFragment extends Fragment {
    private static final int REQ_MSG = 10;

    public static final int FIRST = 0;
    public static final int SECOND = 1;
    public static final int THIRD = 2;


    private Fragment[] mFragments = new Fragment[4];

    private BottomBar mBottomBar;
    private Context _mActivity;


    public static MainFragment newInstance() {
        Bundle args = new Bundle();

        MainFragment fragment = new MainFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        _mActivity = getActivity();

        mFragments[FIRST] = MapFragment.newInstance("");
        mFragments[SECOND] = AgencyFragment.newInstance("");
        mFragments[THIRD] = MyFragment.newInstance("");


        loadMultipleRootTransaction(getFragmentManager(), R.id.fl_tab_container, FIRST,
                mFragments[FIRST],
                mFragments[SECOND],
                mFragments[THIRD]);
        
        initView(view);

        return view;
    }

    private void initView(View view) {
        mBottomBar = (BottomBar) view.findViewById(R.id.bottomBar);

        mBottomBar
                .addItem(new BottomBarTab(_mActivity, R.drawable.msg_click, "我的消息"))
                .addItem(new BottomBarTab(_mActivity, R.drawable.grid_click,"网格管理"))
                .addItem(new BottomBarTab(_mActivity, R.drawable.wd_click, "我的"));
        // 妯℃嫙鏈娑堟伅
//        mBottomBar.getItem(FIRST).setUnreadCount(9);

        mBottomBar.setOnTabSelectedListener(new BottomBar.OnTabSelectedListener() {
            @Override
            public void onTabSelected(int position, int prePosition) {
                showHideFragment(mFragments[position], mFragments[prePosition]);

            }
            
            @Override
            public void onTabUnselected(int position) {

            }

            @Override
            public void onTabReselected(int position) {

            }
        });
    }

    void loadMultipleRootTransaction(FragmentManager fragmentManager, int containerId,
                                     int showPosition, Fragment... tos) {
//        fragmentManager = checkFragmentManager(fragmentManager, null);
//        if (fragmentManager == null) return;
        FragmentTransaction ft = fragmentManager.beginTransaction();
        for (int i = 0; i < tos.length; i++) {
            Fragment to = tos[i];
//            bindContainerId(containerId, tos[i]);
            String toName = to.getClass().getName();
            ft.add(containerId, to, toName);
            if (i != showPosition) {
                ft.hide(to);
            }
        }
        ft.commit();
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        // TODO Auto-generated method stub
        super.onSaveInstanceState(null);
    }

    public void showHideFragment(Fragment showFragment, Fragment hideFragment) {

        FragmentTransaction ft = getFragmentManager().beginTransaction().show(showFragment);
        ft.hide(hideFragment);
        ft.commit();
    }


}
