package com.mapuni.administrator.activity.gridMgAc;


import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.andview.refreshview.XRefreshView;
import com.mapuni.administrator.R;
import com.mapuni.administrator.activity.BaseActivity;
import com.mapuni.administrator.adapter.GridNumberAdapter;
import com.mapuni.administrator.bean.GridNumberBean;
import com.mapuni.administrator.divider.DividerItemDecoration;
import com.mapuni.administrator.fragment.GridNumberFragment;
import com.mapuni.administrator.manager.NetManager;
import com.mapuni.administrator.utils.Logger;
import com.mapuni.administrator.utils.SortListUtil;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import okhttp3.Call;

/**
 * @author Tianfy
 * @time 2017/9/20  15:59
 * @describe 网格排名Activity
 */
public class GridNumberActivity extends BaseActivity implements Toolbar.OnMenuItemClickListener, RadioGroup.OnCheckedChangeListener {


    @BindView(R.id.content_frame)
    LinearLayout mContentFrame;
    @BindView(R.id.nav_view)
    RelativeLayout mNavView;
    @BindView(R.id.drawer_layout)
    DrawerLayout mDrawerLayout;
    @BindView(R.id.rb_haveToDo)
    RadioButton mRbHaveToDo;
    @BindView(R.id.rb_toDo)
    RadioButton mRbToDo;
    @BindView(R.id.rb_totalNumber)
    RadioButton mTbTotalNumber;
    @BindView(R.id.rg_grid)
    RadioGroup mRgGrid;
    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.xrefreshview)
    XRefreshView mXrefreshview;
    @BindView(R.id.tv_noData)
    TextView mTvNoData;
    private GridNumberFragment mGridNumberFragment;

    private List<GridNumberBean.RowsBean> mRows;

    private int type = 0;
    private GridNumberAdapter mGridNumberAdapter;

    @Override
    public int setLayoutResID() {
        return R.layout.activity_grid_number;
    }

    @Override
    public void initView() {
        setToolbarTitle("网格排名");
        //初始化DrawerLayout
        initDreawerLayout();
        mToolbar.setOnMenuItemClickListener(this);
        mRgGrid.setOnCheckedChangeListener(this);
        mRbHaveToDo.setChecked(true);
        
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST));
        mXrefreshview.setPullRefreshEnable(false);
        mXrefreshview.setPullLoadEnable(false);
        mRows=new ArrayList<GridNumberBean.RowsBean>();
        mRecyclerView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,-1));
        mGridNumberAdapter = new GridNumberAdapter(this,mRows,type);
        mRecyclerView.setAdapter(mGridNumberAdapter);
    }
    

    @Override
    public void initData() {
        
    }

    private void initDreawerLayout() {
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager
                .beginTransaction();
        mGridNumberFragment = new GridNumberFragment();
        fragmentTransaction.replace(R.id.nav_view, mGridNumberFragment).commit();

        mDrawerLayout.setDrawerListener(new DrawerLayout.SimpleDrawerListener() {
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }
        });
        //默认打开侧边栏
//        mDrawerLayout.openDrawer(Gravity.RIGHT);
    }


    public void showProgressDialog() {
        mSvProgressHUD.showWithStatus("正在加载...");
    }

    public void dissProgressDialog() {
        mSvProgressHUD.dismiss();
    }

    public void inviliProgressDialog() {
        mSvProgressHUD.dismissImmediately();
    }

    public void query(String startTimeStr, String endTimeStr, String areaId, String taskTypeId) {
        if (mDrawerLayout.isDrawerOpen(Gravity.RIGHT)){
            mDrawerLayout.closeDrawer(Gravity.RIGHT);
        }
        mSvProgressHUD.showWithStatus("正在加载...");
        NetManager.requestGridNumberData(startTimeStr, endTimeStr, areaId, taskTypeId, new StringCallback() {


            @Override
            public void onError(Call call, Exception e, int id) {
                Logger.e(GridNumberActivity.class.getSimpleName(), e.toString());
                ((GridNumberActivity)mContext).finish();
                Toast.makeText(mContext, "请求数据失败", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(String response, int id) {

                mSvProgressHUD.dismissImmediately();
                Logger.i(GridNumberActivity.class.getSimpleName(), response.toString());
                GridNumberBean gridNumberBean = gson.fromJson(response.toString(), GridNumberBean.class);
                List<GridNumberBean.RowsBean> tempRows = gridNumberBean.getRows();

                if (tempRows != null && tempRows.size() > 0) {
                    mTvNoData.setVisibility(View.GONE);

                    switch (type) {
                        case 0:
                            SortListUtil.sort(tempRows, "handledRecordCount", SortListUtil.DESC);
                            break;
                        case 1:
                            SortListUtil.sort(tempRows, "handlingRecordCount", SortListUtil.ASC);
                            break;
                        case 2:
                            SortListUtil.sort(tempRows, "recordTatolCount", SortListUtil.DESC);
                            break;
                    }
                    if (mRows!=null&&mRows.size()>0){
                        mRows.clear();
                    }
                    mRows.addAll(tempRows);
                    mGridNumberAdapter.setItype(type);
                    mGridNumberAdapter.notifyDataSetChanged();
                    
                }else{
                    mTvNoData.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }


    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_query:
                mDrawerLayout.openDrawer(Gravity.RIGHT);
                break;
        }
        return true;

    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
            case R.id.rb_haveToDo://已办
                type = 0;
                sort("handledRecordCount",type,SortListUtil.DESC);
                break;
            case R.id.rb_toDo://待办
                type = 1;
                sort("handlingRecordCount",type,SortListUtil.ASC);
                break;
            case R.id.rb_totalNumber://案件总数
                type = 2;
                sort("recordTatolCount",type,SortListUtil.DESC);
                break;
        }

    }
    /**
     *  @author Tianfy
     *  @time 2017/9/22  11:20
     *  @describe 排序方法
     *  @param field 根据那个字段排序
     *  @param type 类别
     *  @param sort 排序的方式
     */
    private void sort(String field,int type,String sort) {
        if (mRows != null && mRows.size() > 0) {
            mTvNoData.setVisibility(View.GONE);

            SortListUtil.sort(mRows, field, sort);
            mGridNumberAdapter.setItype(type);
            mGridNumberAdapter.notifyDataSetChanged();
        }else{
            mTvNoData.setVisibility(View.VISIBLE);
        }
    }


}
