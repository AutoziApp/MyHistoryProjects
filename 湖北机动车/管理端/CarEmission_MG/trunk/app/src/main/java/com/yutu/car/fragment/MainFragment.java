package com.yutu.car.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yutu.car.R;
import com.yutu.car.views.BottomBar;
import com.yutu.car.views.BottomBarTab;


public class MainFragment extends BaseFragment {

    private static final String ARG_COLUMN_COUNT = "column-count";
    private int mColumnCount = 1;
    private BaseFragment[] mFragments = new BaseFragment[5];



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
            mFragments[0] = HomePageFragment.newInstance("");
            mFragments[1] = InforqueryStatistics.newInstance("");
            mFragments[2] = CoAccountFragment.newInstance("");
            mFragments[3] = EnvironmentaProtectionFragment.newInstance("");
            mFragments[4] = MonitorprocessFragment.newInstance("");

            loadMultipleRootTransaction(R.id.fl_tab_container, 0,
                    mFragments[0],
                    mFragments[1],
                    mFragments[2],
                    mFragments[3],
                    mFragments[4]
            );
        } else {
            // 这里库已经做了Fragment恢复,所有不需要额外的处理了, 不会出现重叠问题
            // 这里我们需要拿到mFragments的引用,也可以通过getChildFragmentManager.getFragments()自行进行判断查找(效率更高些),用下面的方法查找更方便些
            mFragments[0] = findStackFragment(getChildFragmentManager(),HomePageFragment.class,true);
            mFragments[1] = findStackFragment(getChildFragmentManager(),InforqueryStatistics.class,true);
            mFragments[2] = findStackFragment(getChildFragmentManager(),CoAccountFragment.class,true);
            mFragments[3] = findStackFragment(getChildFragmentManager(),EnvironmentaProtectionFragment.class,true);
            mFragments[4] = findStackFragment(getChildFragmentManager(),MonitorprocessFragment.class,true);
        }

        initView(view);
        return view;
    }
    BottomBar mBottomBar;
    private void initView(View view) {
//        EventBus.getDefault().register(this);
        int selectColor = R.color.tab_color_select;
        int cancelColor = R.color.tab_color;
        mBottomBar = (BottomBar) view.findViewById(R.id.bottomBar);

        mBottomBar
                .addItem(new BottomBarTab(_mActivity,selectColor,cancelColor, R.mipmap.shouye, "首页"))
                .addItem(new BottomBarTab(_mActivity,selectColor,cancelColor, R.mipmap.xinxi, "信息查询"))
                .addItem(new BottomBarTab(_mActivity,selectColor,cancelColor,  R.mipmap.luyou, "碳氧化物核算"))
                .addItem(new BottomBarTab(_mActivity,selectColor,cancelColor,  R.mipmap.jiance, "检测数据验证"))
                .addItem(new BottomBarTab(_mActivity, selectColor,cancelColor, R.mipmap.guanli, "检测过程监控"));
        mBottomBar.setBackgroundColor(getResources().getColor(R.color.white));
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
