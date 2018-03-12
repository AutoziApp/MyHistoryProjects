package com.mapuni.administrator.fragment;

import android.app.DownloadManager;
import android.view.View;
import android.widget.AdapterView;

import com.mapuni.administrator.activity.gridMgAc.TaskStatisticsActivity;

/**
 * @name shangluoAdminstor
 * @class name：com.mapuni.administrator.fragment
 * @class describe
 * @anthor Tianfy
 * @time 2017/9/7 18:06
 * @change
 * @chang time
 * @class describe
 */

public class TaskStatisticsFragment extends TababstractFragment implements View.OnClickListener, AdapterView.OnItemSelectedListener, AdapterView.OnItemClickListener {


    @Override
    protected void dissDialog() {
        ((TaskStatisticsActivity)mActivity).inviliProgressDialog();
    }

    @Override
    protected void aShowDialog() {
        ((TaskStatisticsActivity)mActivity).showProgressDialog();
    }

    @Override
    protected void dataQuery(String startTimeStr, String endTimeStr, String areaId, String taskTypeId, int QUERY_TYPE) {
        ((TaskStatisticsActivity)mActivity).query(startTimeStr,endTimeStr,areaId,taskTypeId,QUERY_TYPE);
    }

    @Override
    protected void getQueryType(int QUERY_TYPE) {
        ((TaskStatisticsActivity)mActivity).setQueryType(QUERY_TYPE);
    }

    @Override
    public void goneOrVisibility(int HELLO_CHARTS_TYPE) {
        ((TaskStatisticsActivity)mActivity).goneOrVisibility(HELLO_CHARTS_TYPE);
    }
}
