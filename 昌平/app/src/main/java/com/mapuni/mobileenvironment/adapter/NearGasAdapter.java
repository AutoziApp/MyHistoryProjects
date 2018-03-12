package com.mapuni.mobileenvironment.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.mapuni.mobileenvironment.R;
import com.mapuni.mobileenvironment.model.GasAndJuli;

import java.util.List;

/**
 * Created by Mai on 2016/11/25.
 */

public class NearGasAdapter extends BaseAdapter {
    private Context context;
    private List<GasAndJuli> list;

    public NearGasAdapter(Context context, List<GasAndJuli> list) {
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
            view=View.inflate(context, R.layout.item_neargas_list,null);
            viewHolder=new ViewHolder();
            viewHolder.textView= (TextView) view.findViewById(R.id.tv);
            viewHolder.pm25 = (TextView) view.findViewById(R.id.pm25);
            viewHolder.tvMost= (TextView) view.findViewById(R.id.most);
            view.setTag(viewHolder);
        }else {
            viewHolder= (ViewHolder) view.getTag();
        }
        viewHolder.textView.setText(list.get(i).getDataBean().getName().replaceAll("\\d+", "").trim());
        viewHolder.pm25.setText(-1==list.get(i).getDataBean().getPm25()?"-":list.get(i).getDataBean().getPm25()+"");
        viewHolder.tvMost.setText(list.get(i).getDataBean().getMost());
        return view;
    }
    class ViewHolder{
        TextView textView;
        TextView pm25;
        TextView tvMost;
    }

}
