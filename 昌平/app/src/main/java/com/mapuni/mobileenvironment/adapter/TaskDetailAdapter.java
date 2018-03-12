package com.mapuni.mobileenvironment.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mapuni.mobileenvironment.R;
import com.mapuni.mobileenvironment.app.DataFactory;

import java.util.List;

public class TaskDetailAdapter extends RecyclerView.Adapter<TaskDetailAdapter.ViewHolder>  {
    private List<String> data;
    private Context con;
    private boolean isInfoView;
    private int[] infoNames;
    private String[] flowsNames;
    private String[] flowsValue;
    public TaskDetailAdapter(Context context, List<String> list,boolean bool){
        data = list;
        con = context;
        isInfoView = bool;
        infoNames = DataFactory.TaskDetailInfoName;
        flowsNames = DataFactory.TaskDetailFlowName;
        flowsValue = new String[]{"2016年11月30日巡查上报","工作上报","2016-11-30 09:32:00","小汤山镇",
        "王明","否","小汤山空气污染","--","2016-11-30 09:33:47","气","2016-11-30 09:33:47","昌平区小汤山镇","待执行"};

    }
    public void changeData(boolean bool){
        isInfoView = bool;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.i("Lybin","onCreateViewHolder");
        View view;
        if(isInfoView){
           view  = LayoutInflater.from(con).inflate(R.layout.info_item,null);
        }else{
            view = LayoutInflater.from(con).inflate(R.layout.flow_item,null);
        }

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Log.i("Lybin","onBindViewHolder");
        if(isInfoView){
            holder.name.setText(con.getString(infoNames[position]));
            holder.value.setText(flowsValue[position%flowsValue.length]);
        }else {
                holder.vCreater.setText("王明");
            holder.vTime.setText("2016-12-15");
            holder.vType.setText("创建");
            if(position%2==0){
                holder.vGrid.setText("东小口镇");
            }else{
                holder.vGrid.setText("陈营村");
            }


        }

    }

    @Override
    public int getItemCount(){
        Log.i("Lybin","getItemCount");
        int length = 0;
        if(isInfoView){
            length = infoNames.length;
        }else{
            length = flowsNames.length;
        }
        return length;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public  TextView name;
        public  TextView value;
        public TextView vCreater;
        public TextView vTime;
        public TextView vType;
        public TextView vGrid;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            if(isInfoView){
                name = (TextView) view.findViewById(R.id.itemName);
                value = (TextView) view.findViewById(R.id.itemValue);
            }else{
                vCreater = (TextView) view.findViewById(R.id.createrView);
                vTime = (TextView) view.findViewById(R.id.timeView);
                vType = (TextView) view.findViewById(R.id.typeView);
                vGrid = (TextView) view.findViewById(R.id.gridView);
            }

        }
    }
}
