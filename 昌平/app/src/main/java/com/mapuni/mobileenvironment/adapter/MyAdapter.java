package com.mapuni.mobileenvironment.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mapuni.mobileenvironment.R;
import com.mapuni.mobileenvironment.model.ItemDetailModel;

import java.util.ArrayList;


public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {

    ArrayList<ItemDetailModel> values;
    Context context;
    int index = 0;
    View.OnClickListener OnItemClick;

    public <T>MyAdapter(Context con, ArrayList<ItemDetailModel> list) {
        con = context;
        values = list;
    }
    public <T>MyAdapter(Context con, ArrayList<ItemDetailModel> list, View.OnClickListener itemClick) {
        con = context;
        values = list;
        OnItemClick = itemClick;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
                ItemDetailModel model = values.get(position);
                holder.titleView.setText(model.getName());
                holder.timeView.setText(model.getTag());
                holder.mView.setTag(model);



//        holder.mView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (null != mListener) {
//
//
//                }
//            }
//        });
    }

    @Override
    public int getItemCount() {
        if(values==null){
            values=new ArrayList<>();
        }
        return  values.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView titleView;
        public final TextView timeView;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            titleView = (TextView) view.findViewById(R.id.title);
            timeView = (TextView) view.findViewById(R.id.time);
            if(OnItemClick!=null)
                mView.setOnClickListener(OnItemClick);
        }
        @Override
        public String toString() {
            return super.toString() + " '" + titleView.getText() + "'";
        }
    }
}
