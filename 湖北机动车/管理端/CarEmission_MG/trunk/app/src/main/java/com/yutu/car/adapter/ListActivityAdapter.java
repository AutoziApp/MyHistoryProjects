package com.yutu.car.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.yutu.car.R;
import com.yutu.car.bean.CareManageBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lenovo on 2017/4/8.
 */

public class ListActivityAdapter extends BaseAdapter {
    private Context mContext;
    private List groupList;
    private ArrayList<CareManageBean> list;

    public ListActivityAdapter(Context context,ArrayList<CareManageBean> list ){
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
        String text = list.get(i).getCARCARDNUMBER();
        Log.d("lvcheng","text="+text);
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
