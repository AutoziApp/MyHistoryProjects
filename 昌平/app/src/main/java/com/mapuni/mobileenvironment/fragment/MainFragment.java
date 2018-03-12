package com.mapuni.mobileenvironment.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mapuni.mobileenvironment.R;
import com.mapuni.mobileenvironment.view.BottomBar;
import com.mapuni.mobileenvironment.view.BottomBarTab;


public class MainFragment extends SupportFragment {

    private static final String ARG_COLUMN_COUNT = "column-count";
    private int mColumnCount = 1;
    private SupportFragment[] mFragments = new SupportFragment[3];


    // TODO: Customize parameter initialization
    public static MainFragment newInstance(int columnCount) {
        MainFragment fragment = new MainFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        if (savedInstanceState == null) {
            mFragments[0] =HomeFragment.newFragment();
//            mFragments[1] = ExecuteFragment.newFragment();
//            mFragments[2] = OAFragment.newFragment();
            mFragments[1] = WatchFragment.newFragment();
            mFragments[2] = MapFragment.newFragment();
            loadMultipleRootTransaction(R.id.fl_tab_container, 0,mFragments);
        } else {
            // 这里库已经做了Fragment恢复,所有不需要额外的处理了, 不会出现重叠问题
            // 这里我们需要拿到mFragments的引用,也可以通过getChildFragmentManager.getFragments()自行进行判断查找(效率更高些),用下面的方法查找更方便些
            mFragments[0] = findStackFragment(getChildFragmentManager(),HomeFragment.class,true);
//            mFragments[1] = findStackFragment(getChildFragmentManager(),ExecuteFragment.class,true);
//            mFragments[2] = findStackFragment(getChildFragmentManager(),OAFragment.class,true);
            mFragments[1] = findStackFragment(getChildFragmentManager(),WatchFragment.class,true);
            mFragments[2] = findStackFragment(getChildFragmentManager(),MapFragment.class,true);
        }
        initView(view);
        return view;
    }
    BottomBar mBottomBar;
    private void initView(View view) {
//        EventBus.getDefault().register(this);
        int selectColor = R.color.PagerBg;
        int cancelColor = R.color.gray;
        mBottomBar = (BottomBar) view.findViewById(R.id.bottomBar);
        mBottomBar.addItem(new BottomBarTab(_mActivity,selectColor,cancelColor, R.drawable.select_envir_home,getResources().getString(R.string.tab_home)));
//        mBottomBar.addItem(new BottomBarTab(_mActivity,selectColor,cancelColor, R.drawable.select_envir_chuzhi,getResources().getString(R.string.tab_execute)));
//        mBottomBar.addItem(new BottomBarTab(_mActivity,selectColor,cancelColor, R.drawable.select_envir_bangong,getResources().getString(R.string.tab_oa)));
        mBottomBar.addItem(new BottomBarTab(_mActivity,selectColor,cancelColor, R.drawable.select_envir_jiance,getResources().getString(R.string.tab_watch)));
        mBottomBar.addItem(new BottomBarTab(_mActivity,selectColor,cancelColor, R.drawable.select_envir_gis,getResources().getString(R.string.tab_map)));
        mBottomBar.setOnTabSelectedListener(new BottomBar.OnTabSelectedListener() {
            @Override
            public void onTabSelected(int position, int prePosition) {
                showHideFragment(getFragmentManager(),mFragments[position], mFragments[prePosition],position,prePosition);
//                replaceTransaction(R.id.fl_tab_container,getFragmentManager(),mFragments[position],false);
            }

            @Override
            public void onTabUnselected(int position) {

            }

            @Override
            public void onTabReselected(int position) {
                // 这里推荐使用EventBus来实现 -> 解耦
                // 在FirstPagerFragment,FirstHomeFragment中接收, 因为是嵌套的Fragment
                // 主要为了交互: 重选tab 如果列表不在顶部则移动到顶部,如果已经在顶部,则刷新
                // EventBus.getDefault().post(new TabSelectedEvent(position));
            }
        });
    }

}
