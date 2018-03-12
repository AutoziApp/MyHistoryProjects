package com.yutu.car.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.yutu.car.R;
import com.yutu.car.bean.MmsBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lenovo on 2017/4/8.
 */

public class MmsListActivityAdapter extends BaseAdapter {
    private Context mContext;
    private ArrayList<MmsBean> list;

    public MmsListActivityAdapter(Context context, ArrayList<MmsBean> list ){
        this.mContext=context;
        this.list=list;

    }
    @Override
    public int getCount() {
        return list.size();  }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        GroupHold hold;
        String mtext = list.get(i).getTITLE();
        String ttext=list.get(i).getSENDPEOPLE();
        String sendTime=list.get(i).getSENDTIME();
        if(view==null){
            view= LayoutInflater.from(mContext).inflate(R.layout.mms_item_group_list,viewGroup,false);
            hold = new GroupHold();
            hold.mText = (TextView) view.findViewById(R.id.groupText);
            hold.tText=(TextView) view.findViewById(R.id.send_people);
            hold.mImage = (TextView) view.findViewById(R.id.group_arrow);
            view.setTag(hold);
        }else{
            hold = (GroupHold) view.getTag();
        }
        hold.mText.setText(mtext);
        hold.tText.setText("发件人："+ttext);
        hold.mImage.setText(sendTime);

        return view;
    }

    class GroupHold{
        TextView mText,tText,mImage;
    }
}
