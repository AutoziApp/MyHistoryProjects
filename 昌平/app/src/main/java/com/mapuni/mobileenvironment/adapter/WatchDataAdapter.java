package com.mapuni.mobileenvironment.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.mapuni.mobileenvironment.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mai on 2016/11/25.
 */

public class WatchDataAdapter extends RecyclerView.Adapter<ViewHolder> {
    private Context context;
    private List<String> mTime;
    private List<String> mValue;
    private static final int TYPE_ITEM = 0;
    private static final int TYPE_FOOTER = 1;
    private int SHOW = 1;
    private int GONG = 0;
    private int CURRENTSHOW = 1;

    public WatchDataAdapter(Context context, List<String> time,List<String> value) {
        this.context = context;
        if(time==null||value==null){
            mTime = new ArrayList<>();
            mValue=new ArrayList<>();
            return;
        }
        this.mValue = value;
        this.mTime = time;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(viewType == TYPE_ITEM){
            View view = LayoutInflater.from(context).inflate(R.layout.watch_data_item,null,false);
            return new ItemViewHolder(view);
        }else if(viewType == TYPE_FOOTER){
            View view = LayoutInflater.from(context).inflate(R.layout.item_foot, parent, false);
            return new FootViewHolder(view);
        }
        return null;

    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if(holder instanceof ItemViewHolder&&mValue.size()>0){
            ((ItemViewHolder)holder).time.setText(mTime.get(position));
            ((ItemViewHolder) holder).value.setText(mValue.get(position));
        }else if(holder instanceof FootViewHolder){
            if(CURRENTSHOW == SHOW){
                ((FootViewHolder) holder).view.setVisibility(View.VISIBLE);
            }else{
                ((FootViewHolder) holder).view.setVisibility(View.GONE);
                return;
            }
            if(mValue.size()==0){
                ((FootViewHolder) holder).bar.setVisibility(View.GONE);
                ((FootViewHolder) holder).text.setText("--无数据--");
            }else{
                ((FootViewHolder) holder).bar.setVisibility(View.VISIBLE);
                ((FootViewHolder) holder).text.setText(context.getResources().getString(R.string.loading));
            }
        }
    }


    @Override
    public int getItemCount() {
//        return mValue.size() == 0 ? 0 : mValue.size() + 1;
        return  mValue.size() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        if (position + 1 == getItemCount()) {
            return TYPE_FOOTER;
        } else {
            return TYPE_ITEM;
        }
    }


    public class ItemViewHolder extends ViewHolder {
        public  View mView;
        public  TextView value;
        public  TextView time;

        public ItemViewHolder(View view) {
            super(view);
            mView = view;
            time = (TextView) view.findViewById(R.id.time);
            value = (TextView) view.findViewById(R.id.value);
        }
    }
    public void footRemoved(){
        if(mValue.size()==0){
            return;
        }
        CURRENTSHOW = GONG;
        notifyDataSetChanged();
    }
    public void footShow(){
        if(mValue.size()>0&&mValue.size()<15){
            footRemoved();
            return;
        }
        CURRENTSHOW = SHOW;
        notifyDataSetChanged();
    }
    static class FootViewHolder extends ViewHolder {
        ProgressBar bar;
        TextView text;
        View view;
        public FootViewHolder(View view) {
            super(view);
            this.view=view;
            text = (TextView) view.findViewById(R.id.text);
            bar = (ProgressBar) view.findViewById(R.id.progressBar);
        }
    }


}
