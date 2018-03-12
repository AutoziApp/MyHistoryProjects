package com.mapuni.administrator.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mapuni.administrator.R;
import com.mapuni.administrator.activity.gridMgAc.GridNumberActivity;
import com.mapuni.administrator.bean.GridNumberBean;
import com.mapuni.administrator.utils.TxtUtil;

import java.util.List;

/**
 * @name shangluoAdminstor
 * @class name：com.mapuni.administrator.adapter
 * @class describe
 * @anthor Tianfy
 * @time 2017/9/22 11:07
 * @change
 * @chang time
 * @class describe
 */

public class GridNumberAdapter extends RecyclerView.Adapter<GridNumberAdapter.MyViewHolder> {
    private Context context;
    private List<GridNumberBean.RowsBean> rows;
    private int iType;

    public GridNumberAdapter(GridNumberActivity gridNumberActivity, List<GridNumberBean.RowsBean> rows, int iType) {
        this.context = gridNumberActivity;
        this.rows = rows;
        this.iType = iType;
    }

    @Override
    public GridNumberAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        GridNumberAdapter.MyViewHolder viewHolder = new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.list_item_grid_number, parent, false));
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(GridNumberAdapter.MyViewHolder holder, int position) {
        GridNumberBean.RowsBean rowsBean = rows.get(position);
        holder.mTvRanking.setText(TxtUtil.isEmpty((position + 1) + ""));
        holder.mTvAreaName.setText(rowsBean.getGridName());
        switch (iType) {
            case 0:
                holder.mTvNumber.setText(TxtUtil.isEmpty(rowsBean.getHandledRecordCount() + ""));
                break;
            case 1:
                holder.mTvNumber.setText(TxtUtil.isEmpty(rowsBean.getHandlingRecordCount() + ""));
                break;
            case 2:
                holder.mTvNumber.setText(TxtUtil.isEmpty(rowsBean.getRecordTatolCount() + ""));
                break;
        }
    }

    public int setItype(int iType) {
        return this.iType = iType;
    }

    @Override
    public int getItemCount() {
        return rows.size();
    }


    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView mTvRanking;//排名
        TextView mTvAreaName;//地区
        TextView mTvNumber;//个数

        public MyViewHolder(View itemView) {
            super(itemView);
            mTvRanking = (TextView) itemView.findViewById(R.id.tv_ranking);
            mTvAreaName = (TextView) itemView.findViewById(R.id.tv_areaName);
            mTvNumber = (TextView) itemView.findViewById(R.id.tv_number);
        }
    }
}

