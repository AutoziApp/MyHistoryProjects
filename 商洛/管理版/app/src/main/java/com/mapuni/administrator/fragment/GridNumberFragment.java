package com.mapuni.administrator.fragment;

import android.app.DownloadManager;
import android.view.View;

import com.mapuni.administrator.activity.gridMgAc.GridNameActivity;
import com.mapuni.administrator.activity.gridMgAc.GridNumberActivity;

/**
 * @name shangluoAdminstor
 * @class name：com.mapuni.administrator.fragment
 * @class describe
 * @anthor Tianfy
 * @time 2017/9/20 17:17
 * @change
 * @chang time
 * @class describe
 */

public class GridNumberFragment extends TababstractFragment{
    

    @Override
    public void initData() {
        super.initData();
        mContainerHellorcharts.setVisibility(View.GONE);
        QUERY_TYPE=2;
        initTaskTypeData();
        mContainerTaskStatistic.setVisibility(View.GONE);
        mContainerPic.setVisibility(View.GONE);
    }

    @Override
    protected void dissDialog() {
        ((GridNumberActivity)mActivity).inviliProgressDialog();
    }

    @Override
    protected void aShowDialog() {
        ((GridNumberActivity)mActivity).showProgressDialog();
    }

    @Override
    protected void dataQuery(String startTimeStr, String endTimeStr, String areaId, String taskTypeId, int QUERY_TYPE) {
        if (taskTypeId!=null){
            ((GridNumberActivity)mActivity).query(startTimeStr,endTimeStr,areaId,taskTypeId);
        }else{
            throw new NullPointerException(taskTypeId+"is null");
        }
    }

    @Override
    protected void getQueryType(int QUERY_TYPE) {
        
    }

    @Override
    public void goneOrVisibility(int HELLO_CHARTS_TYPE) {

    }
}
