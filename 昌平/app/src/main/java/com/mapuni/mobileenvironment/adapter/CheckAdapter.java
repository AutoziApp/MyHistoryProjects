package com.mapuni.mobileenvironment.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mapuni.mobileenvironment.R;

import java.util.List;

public class CheckAdapter extends RecyclerView.Adapter<CheckAdapter.ViewHolder>  {
    private List<String> data;
    private Context con;
    private View.OnClickListener mListener;
    private View.OnLongClickListener mLongClick;
    private boolean isDetail;
    private String[] adressList = new String[]{"昌平东小口","昌平北七家","昌平天通苑","小辛庄村","昌平西小口","单家村"};
    public CheckAdapter(Context context, View.OnClickListener listener,boolean isDetail){
        con = context;
        mListener = listener;
        this.isDetail = isDetail;
    }
    public CheckAdapter(Context context, View.OnClickListener listener, View.OnLongClickListener longClick, boolean isDetail){
        con = context;
        mListener = listener;
        this.isDetail = isDetail;
        mLongClick = longClick;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        if(isDetail){
           view  = LayoutInflater.from(con).inflate(R.layout.check_detail_list_item,null);
        }else{
            view = LayoutInflater.from(con).inflate(R.layout.check_list_item,null);
        }
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Log.i("Lybin","onBindViewHolder");
        if(isDetail){
            holder.detailAdress.setText(adressList[position%adressList.length]);
            holder.detailTime.setText("2016-12-05");
            holder.detailIsExecute.setText("是");
        }else {
            holder.nameView.setText("小明");
            holder.unitView.setText("小汤山镇");
            holder.countView.setText("10");
        }
        holder.mView.setOnClickListener(mListener);
        holder.mView.setOnLongClickListener(mLongClick);

    }

    @Override
    public int getItemCount(){
        Log.i("Lybin","getItemCount");
        int length = 10;
        return length;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public  TextView nameView;
        public  TextView countView;
        public TextView unitView;
        public TextView detailAdress;
        public TextView detailTime;
        public TextView detailIsExecute;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            if(isDetail){
                detailAdress = (TextView) view.findViewById(R.id.adressView);
                detailIsExecute = (TextView) view.findViewById(R.id.isExecuteView);
                detailTime = (TextView) view.findViewById(R.id.timeView);
            }else{
                nameView = (TextView) view.findViewById(R.id.nameView);
                countView = (TextView) view.findViewById(R.id.countView);
                unitView = (TextView) view.findViewById(R.id.unitView);
            }

        }
    }
}
