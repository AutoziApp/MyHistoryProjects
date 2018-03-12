package com.mapuni.car.mvp.main.fragment;

import android.app.Activity;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.mapuni.car.R;
import com.mapuni.car.mvp.lookcar.fragment.LookCarFragment;
import com.mapuni.car.mvp.main.contract.MainContract;
import com.mapuni.car.mvp.main.model.MainModel;
import com.mapuni.car.mvp.main.presenter.MainPresenter;
import com.mapuni.car.mvp.searchcar.fragment.SearchCarFragment;
import com.mapuni.car.mvp.ywrequest.fragment.RequestFragment;
import com.mapuni.car.mvp.ywsearch.fragment.YWSearchFragment;
import com.mapuni.core.base.CoreBaseFragment;
import com.mapuni.core.widget.bottomwidget.BottomBar;
import com.mapuni.core.widget.bottomwidget.BottomBarTab;

import me.yokeyword.fragmentation.SupportFragment;



/**
 * Created by lybin on 2017/6/27.
 */

public class MainFragment extends CoreBaseFragment<MainPresenter,MainModel> implements MainContract.MainView{
    public static final int FIRST = 0;
    public static final int SECOND = 1;
    public static final int THREE = 2;
    public static final int FOUR = 3;
    private BottomBar mBottomBar;

    private SupportFragment[] mFragments = new SupportFragment[4];

    @Override
    public int getLayoutId() {
        return R.layout.fragment_main;
    }

    @Override
    public void initUI(View view, @Nullable Bundle savedInstanceState) {
        if (savedInstanceState == null) {
            mFragments[FIRST] = new SearchCarFragment();
            mFragments[SECOND] = new LookCarFragment();
            mFragments[THREE] = new RequestFragment();
            mFragments[FOUR] = new YWSearchFragment();
            loadMultipleRootFragment(R.id.fl_tab_container, FIRST,
                    mFragments[FIRST],
                    mFragments[SECOND],
                    mFragments[THREE],
                    mFragments[FOUR]);
        } else {
            // 这里库已经做了Fragment恢复,所有不需要额外的处理了, 不会出现重叠问题

            // 这里我们需要拿到mFragments的引用,也可以通过getChildFragmentManager.getFragments()自行进行判断查找(效率更高些),用下面的方法查找更方便些
            mFragments[FIRST] = findChildFragment(SearchCarFragment.class);
            mFragments[SECOND] = findChildFragment(LookCarFragment.class);
            mFragments[THREE] = findChildFragment(RequestFragment.class);
            mFragments[FOUR] = findChildFragment(YWSearchFragment.class);
        }
        mBottomBar = (BottomBar) view.findViewById(R.id.bottomBar);
        boolean b = checkDeviceHasNavigationBar();
//        if(b==true){
//            FrameLayout.LayoutParams layoutParams = ( FrameLayout.LayoutParams) mBottomBar.getLayoutParams();
//            layoutParams.bottomMargin=80;//将默认的距离底部20dp，改为0，这样底部区域全被listview填满。
//            mBottomBar.setLayoutParams(layoutParams);
////            AndroidSetView as=new AndroidSetView(mBottomBar);
//        }

    }
    @Override
    public void initData() {
//        mPresenter.initConfig();
        mPresenter.requestPermission();
    }

    @Override
    public Activity getParentActivity() {
        return mActivity;
    }

    @Override
    public void showTabList(String[] mTabs) {
        BottomBarTab firstTab = new BottomBarTab(_mActivity, R.mipmap.chax_1, mTabs[FIRST],false);
        firstTab.setBackgroundColor(Color.parseColor("#E629282D"));
        BottomBarTab secondTab = new BottomBarTab(_mActivity, R.mipmap.waig_1, mTabs[SECOND],false);
        secondTab.setBackgroundColor(Color.parseColor("#E629282D"));
        BottomBarTab threeTab = new BottomBarTab(_mActivity, R.mipmap.shenqing_1, mTabs[THREE],false);
        threeTab.setBackgroundColor(Color.parseColor("#E629282D"));
        BottomBarTab fourTab = new BottomBarTab(_mActivity, R.mipmap.yewu_1, mTabs[FOUR],false);
        fourTab.setBackgroundColor(Color.parseColor("#E629282D"));
        mBottomBar
                .addItem(firstTab)
                .addItem(secondTab)
                .addItem(threeTab)
                .addItem(fourTab);


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
    @Override
    public boolean onBackPressedSupport() {
        showToast("返回");
        return true;
    }

    @Override
    public void showError(String msg) {

    }
    public boolean checkDeviceHasNavigationBar() {
        boolean hasNavigationBar = false;
        Resources rs = getResources();
        int id = rs.getIdentifier("config_showNavigationBar", "bool", "android");
        if (id > 0) {
            hasNavigationBar = rs.getBoolean(id);
        }
        return hasNavigationBar;
    }

//    private ContentObserver mNavigationStatusObserver = new ContentObserver(new Handler()) {
//        @Override
//        public void onChange(boolean selfChange) {
//            int navigationBarIsMin = Settings.System.getInt(getActivity().getContentResolver(),
//                    "navigationbar_is_min", 0);
//            FrameLayout.LayoutParams layoutParams = ( FrameLayout.LayoutParams) mBottomBar.getLayoutParams();
//            if (navigationBarIsMin == 1) {
//                //导航键隐藏了
//                layoutParams.bottomMargin=0;//将默认的距离底部20dp，改为0，这样底部区域全被listview填满。
//            } else {
//                //导航键显示了
//                layoutParams.bottomMargin=80;//将默认的距离底部20dp，改为0，这样底部区域全被listview填满。
//            }
//            mBottomBar.setLayoutParams(layoutParams);
//        }
//    };
    }
