package com.mapuni.mobileenvironment.adapter;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mapuni.mobileenvironment.R;
import com.mapuni.mobileenvironment.model.AirDayData;
import com.mapuni.mobileenvironment.utils.PollutionLevelCalUtil;

import java.util.List;

/**
 * Created by Mai on 2016/12/22.
 */

public class AirDayAdapter extends BaseAdapter {
    private List<AirDayData.DataBean> list;
    private Context context;
    private List<Double> valueList;
    private String valueTag;

    public AirDayAdapter(List<AirDayData.DataBean> list, List<Double> valueList, Context context, String valueTag) {
        this.list = list;
        this.context = context;
        this.valueList = valueList;
        this.valueTag = valueTag;
    }

    int[] imgs = {R.drawable.qu, R.drawable.shi,R.drawable.zhuan};
    int[] valueBgs = {R.drawable.red, R.drawable.green, R.drawable.orange, R.drawable.grape, R.drawable.brown,R.drawable.yello};
    int nullValue = R.drawable.gray;

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

    public void change(String tag){
        valueTag = tag;
        notifyDataSetChanged();
    }


    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        MyViewHolder holder = null;

        if (view == null) {
            holder = new MyViewHolder();
            view = View.inflate(context, R.layout.item_new_air_cur, null);
            holder.linearLayout = (LinearLayout) view.findViewById(R.id.linearLayout);
            holder.imageView = (ImageView) view.findViewById(R.id.iv);
            holder.city = (TextView) view.findViewById(R.id.city);
            holder.value = (TextView) view.findViewById(R.id.value);
            holder.major = (TextView) view.findViewById(R.id.major);
            holder.tvBg= (ImageView) view.findViewById(R.id.tv_bg);
            view.setTag(holder);
        } else {
            holder = (MyViewHolder) view.getTag();
        }
        if (i % 2 == 0) {
            holder.linearLayout.setBackgroundColor(Color.parseColor("#D9E8FF"));
        } else {
            holder.linearLayout.setBackgroundColor(Color.WHITE);
        }
        AirDayData.DataBean dataBean = list.get(i);
//        区
        if (dataBean.getDeviceid().substring(0, 1).equals("T")) {
            holder.imageView.setImageResource(imgs[0]);//设置类型图标
        }
//        专
        else if(dataBean.getDeviceid().substring(0, 1).equals("A")) {
            holder.imageView.setImageResource(imgs[2]);//设置类型图标
        }
//        市
        else {
            holder.imageView.setImageResource(imgs[1]);//设置类型图标
        }

        holder.city.setText(dataBean.getDeviceName().replaceAll("\\d+", "").trim());//设置city
        holder.major.setText(dataBean.getMost().replaceAll("Average", " ").toUpperCase());//设置主要污染物
        int level = PollutionLevelCalUtil.getDayLevel(valueTag, valueList.get(i));
        holder.value.setTextColor(Color.parseColor("#ffffff"));
        Log.i("Lybin",valueTag);
        Log.i("Lybin","value=========="+valueList.get(i));
        Log.i("Lybin","level=========="+level);

        switch (level) {
            case -1:
                holder.tvBg.setImageResource(nullValue);
                break;
            case 6:
                holder.tvBg.setImageResource(valueBgs[4]);
                break;
            case 5:
                holder.tvBg.setImageResource(valueBgs[3]);
                break;
            case 4:
                holder.tvBg.setImageResource(valueBgs[0]);
                break;
            case 3:
                holder.tvBg.setImageResource(valueBgs[2]);
                break;
            case 2:
                holder.tvBg.setImageResource(valueBgs[5]);
                holder.value.setTextColor(Color.parseColor("#FFE9E0E0"));
                break;
            case 1:
                holder.tvBg.setImageResource(valueBgs[1]);
                break;

        }
        int _Value = valueList.get(i).intValue();
        holder.value.setText(-1 == _Value ? "-" : _Value + "");//设置value值
        return view;
    }

    class MyViewHolder {
        LinearLayout linearLayout;
        ImageView imageView;
        ImageView tvBg;
        TextView city;
        TextView value;
        TextView major;
    }
}
