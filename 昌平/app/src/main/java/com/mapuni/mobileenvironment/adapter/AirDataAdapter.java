package com.mapuni.mobileenvironment.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mapuni.mobileenvironment.R;
import com.mapuni.mobileenvironment.utils.DateUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mai on 2016/11/25.
 */

public class AirDataAdapter extends RecyclerView.Adapter<ViewHolder> {
    private Context context;
    private List<String> mTime;
    private List<String> mValue;
    private List<String> mStandard;
    private static final int TYPE_ITEM = 0;
    private static final int TYPE_FOOTER = 1;
    private int SHOW = 1;
    private int GONG = 0;
    private int CURRENTSHOW = 1;

    public AirDataAdapter(Context context, List<String> time, List<String> value,List<String> standard) {
        this.context = context;
        if (time == null || value == null||standard==null) {
            mTime = new ArrayList<>();
            mValue = new ArrayList<>();
            mStandard = new ArrayList<>();
            return;
        }
        this.mValue = value;
        this.mTime = time;
        this.mStandard=standard;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        if (viewType == TYPE_ITEM) {
            View view = LayoutInflater.from(context).inflate(R.layout.air_data_item, null, false);
            return new ItemViewHolder(view);
//        }
//        else if (viewType == TYPE_FOOTER) {
//            View view = LayoutInflater.from(context).inflate(R.layout.item_foot, parent, false);
//            return new FootViewHolder(view);
//        }
//        return null;

    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (holder instanceof ItemViewHolder && mValue.size() > 0) {
//            ((ItemViewHolder) holder).time.setText(mTime.get(position));
            ((ItemViewHolder) holder).time.setText(DateUtils.long2DateString(Long.parseLong(mTime.get(position))));
            ((ItemViewHolder) holder).standard.setText(mStandard.get(position));
            ((ItemViewHolder) holder).value.setText("-1".equals(mValue.get(position))?"-":mValue.get(position));
        }
//        else if (holder instanceof FootViewHolder) {
//            if (CURRENTSHOW == SHOW) {
//                ((FootViewHolder) holder).view.setVisibility(View.VISIBLE);
//            } else {
//                ((FootViewHolder) holder).view.setVisibility(View.GONE);
//                return;
//            }
//            if (mValue.size() == 0) {
//                ((FootViewHolder) holder).bar.setVisibility(View.GONE);
//                ((FootViewHolder) holder).text.setText("--无数据--");
//            } else {
//                ((FootViewHolder) holder).bar.setVisibility(View.VISIBLE);
//                ((FootViewHolder) holder).text.setText(context.getResources().getString(R.string.loading));
//            }
//        }
    }


    @Override
    public int getItemCount() {
//        return mValue.size() == 0 ? 0 : mValue.size() + 1;
        return mValue.size() ;
    }
//
//    @Override
//    public int getItemViewType(int position) {
//        if (position + 1 == getItemCount()) {
//            return TYPE_FOOTER;
//        } else {
//            return TYPE_ITEM;
//        }
//    }


    public class ItemViewHolder extends ViewHolder {
        public View mView;
        public TextView value;
        public TextView standard;
        public TextView time;

        public ItemViewHolder(View view) {
            super(view);
            mView = view;
            standard= (TextView) view.findViewById(R.id.standard);
            time = (TextView) view.findViewById(R.id.time);
            value = (TextView) view.findViewById(R.id.value);
        }
    }

    public void footRemoved() {
        if (mValue.size() == 0) {
            return;
        }
        CURRENTSHOW = GONG;
        notifyDataSetChanged();
    }

    public void footShow() {
        CURRENTSHOW = SHOW;
        notifyDataSetChanged();
    }

//    static class FootViewHolder extends ViewHolder {
//        ProgressBar bar;
//        TextView text;
//        View view;
//
//        public FootViewHolder(View view) {
//            super(view);
//            this.view = view;
//            text = (TextView) view.findViewById(R.id.text);
//            bar = (ProgressBar) view.findViewById(R.id.progressBar);
//        }
//    }


}
