package com.mapuni.mobileenvironment.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.mapuni.mobileenvironment.R;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Mai on 2016/11/29.
 */

public class CompanyListAdapter extends BaseAdapter {
    private Context context;
    private List<HashMap<String,Object>> list;

    public CompanyListAdapter(Context context, List<HashMap<String,Object>> list) {
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
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder=null;
        if(view==null){
            view=View.inflate(context, R.layout.item_company_list,null);
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
        viewHolder.textView.setText((String) list.get(i).get("entname"));
        return view;
    }

    class ViewHolder{
        TextView textView;
    }
}
