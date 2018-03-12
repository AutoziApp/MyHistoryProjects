package com.mapuni.administrator.fragment;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mapuni.administrator.R;
import com.mapuni.administrator.activity.BaseActivity;
import com.mapuni.administrator.activity.gridMgAc.ChangeTrendActivity;
import com.mapuni.administrator.activity.gridMgAc.GridNameActivity;
import com.mapuni.administrator.activity.gridMgAc.GridNumberActivity;
import com.mapuni.administrator.activity.gridMgAc.TaskStatisticsActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by Administrator on 2017/8/11.
 */

public class AgencyFragment extends Fragment {

    @BindView(R.id.toolbar_title)
    TextView mToolbarTitle;
    @BindView(R.id.tb_toolbar)
    Toolbar mTbToolbar;
    Unbinder unbinder;
    @BindView(R.id.rl_taskStatistics)
    RelativeLayout mRlTaskStatistics;
    @BindView(R.id.rl_gridNumber)
    RelativeLayout mRlGridNumber;
    @BindView(R.id.rl_changeTrend)
    RelativeLayout mRlChangeTrend;
    @BindView(R.id.rl_gridName)
    RelativeLayout mRlGridName;


    private AppCompatActivity mAppCompatActivity;
    private ActionBar mSupportActionBar;
    private Context mContext;

    public static AgencyFragment newInstance(String s) {
        AgencyFragment fragment = new AgencyFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_grid_manager, container, false);
        unbinder = ButterKnife.bind(this, view);
        mAppCompatActivity = (AppCompatActivity) getActivity();
        mContext = getActivity();
        initToolbar();
        initView();
        initData();
        return view;
    }

    /**
     * 初始化toolbar
     */
    public void initToolbar() {
        mAppCompatActivity.setSupportActionBar(mTbToolbar);
        mSupportActionBar = mAppCompatActivity.getSupportActionBar();
        mSupportActionBar.setDisplayHomeAsUpEnabled(false);
        mSupportActionBar.setDisplayShowTitleEnabled(false);
        mToolbarTitle.setText("网格管理");
    }

    private void initView() {


    }


    /**
     * @author Tianfy
     * @time 2017/9/4  13:54
     * @describe 初始化数据
     */
    private void initData() {

    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.rl_taskStatistics, R.id.rl_gridNumber, R.id.rl_changeTrend, R.id.rl_gridName})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.rl_taskStatistics:
                startActivity(TaskStatisticsActivity.class);
                break;
            case R.id.rl_gridNumber:
                startActivity(GridNumberActivity.class);
                break;
            case R.id.rl_changeTrend:
                startActivity(ChangeTrendActivity.class);
                break;
            case R.id.rl_gridName:
                startActivity(GridNameActivity.class);
                break;
        }
    }

    private void startActivity(Class<? extends BaseActivity> clazz) {
        getActivity().startActivity(new Intent(getActivity(), clazz));
        getActivity().overridePendingTransition(R.anim.activity_enter_anim, R.anim.activity_exit_anim);
    }
}
