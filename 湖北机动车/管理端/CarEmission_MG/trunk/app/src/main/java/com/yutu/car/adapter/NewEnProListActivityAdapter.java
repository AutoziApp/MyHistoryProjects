package com.yutu.car.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.yutu.car.R;
import com.yutu.car.bean.NewEnvironmentaProtectionBean;

import java.util.ArrayList;

/**
 * Created by lenovo on 2017/4/8.
 */

public class NewEnProListActivityAdapter extends BaseAdapter {
    private Context mContext;
    private ArrayList<NewEnvironmentaProtectionBean> list;

    public NewEnProListActivityAdapter(Context context, ArrayList<NewEnvironmentaProtectionBean> list ){
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
        String text = list.get(i).getHPHM();
        if(view==null){
            view= LayoutInflater.from(mContext).inflate(R.layout.item_group_list,viewGroup,false);
            hold = new GroupHold();
            hold.mText = (TextView) view.findViewById(R.id.groupText);
            hold.mImage = (ImageView) view.findViewById(R.id.group_arrow);
            view.setTag(hold);
        }else{
            hold = (GroupHold) view.getTag();
        }
        hold.mText.setText(text);

        return view;
    }

    class GroupHold{
        TextView mText;
        ImageView mImage;
    }
}
