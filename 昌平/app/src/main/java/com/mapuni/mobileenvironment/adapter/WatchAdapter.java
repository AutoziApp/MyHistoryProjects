package com.mapuni.mobileenvironment.adapter;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.mapuni.mobileenvironment.R;
import com.mapuni.mobileenvironment.model.EntModel;

import java.util.List;

/**
 * Created by Mai on 2016/11/25.
 */

public class WatchAdapter extends BaseAdapter {
    private Context context;
    private List<?> list;

    public WatchAdapter(Context context, List<?> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public int getViewTypeCount() {
        return super.getViewTypeCount();
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        ViewHolder viewHolder=null;
        Log.i("Lybin",i+"");
        if(view==null){
            Log.i("Lybin",i+"---------view==null");
            view=View.inflate(context, R.layout.item_listview_water_ac,null);
            viewHolder=new ViewHolder();
            viewHolder.textView= (TextView) view.findViewById(R.id.tv);
            view.setTag(viewHolder);
        }else {
            viewHolder= (ViewHolder) view.getTag();
        }
        if(i%2==0){
            viewHolder.textView.setBackgroundColor(Color.parseColor("#D9E8FF"));
        }else{
            viewHolder.textView.setBackgroundColor(Color.parseColor("#ffffff"));
        }
        EntModel model = (EntModel) list.get(i);
//        viewHolder.textView.setText(model.getEntName());

        return view;
    }
    class ViewHolder{
        TextView textView;
    }

}
