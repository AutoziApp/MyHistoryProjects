package com.yutu.car.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.yutu.car.R;
import com.yutu.car.bean.AllStationBean;
import com.yutu.car.presenter.ExpandaListControl;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yawei on 2017/3/28.
 */

public class ExpandaListAdapter extends BaseExpandableListAdapter {
    private List groupList;
    private List<ArrayList> childList;
//    private AllStationBean bean;
    private ExpandaListControl listControl;
    private Context mContext;
    public ExpandaListAdapter(Context context,ExpandaListControl control){
        mContext = context;
        listControl = control;
    }
    @Override
    public int getGroupCount() {
        return listControl.getGroupCount();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return listControl.getChildrenCount(groupPosition);
    }

    @Override
    public Object getGroup(int groupPosition) {
        return listControl.getGroup(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return listControl.getItemBean(groupPosition,childPosition);         
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        GroupHold hold;
        String text = listControl.getGroupName(groupPosition);
        int count = listControl.getChildCount(groupPosition);
        if(convertView==null){
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_group_list,null);
            hold = new GroupHold();
            hold.mText = (TextView) convertView.findViewById(R.id.groupText);
            hold.mImage = (ImageView) convertView.findViewById(R.id.group_arrow);
            convertView.setTag(hold);
        }else{
            hold = (GroupHold) convertView.getTag();
        }
        hold.mText.setText(text+" ("+count+")");
        if(isExpanded){
            hold.mText.setTextColor(mContext.getResources().getColor(R.color.item_group_text_select));
            hold.mImage.setImageResource(R.mipmap.arrow_down);
        }else{
            hold.mText.setTextColor(mContext.getResources().getColor(R.color.item_group_text));
            hold.mImage.setImageResource(R.mipmap.arrow_right);
        }
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        ChildHold hold;
        ExpandaListControl.ExpandaListItem item = listControl.getItemBean(groupPosition,childPosition);
        String text = listControl.getChildName(groupPosition,childPosition);
        if(convertView==null){
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_child_list,null);
            hold = new ChildHold();
            hold.mText = (TextView) convertView.findViewById(R.id.childText);
            convertView.setTag(hold);
            convertView.setTag(R.id.childText,item);
        }else{
            hold = (ChildHold) convertView.getTag();
        }
        hold.mText.setText(text);
        return convertView;
    }

    public class ChildHold{
        public TextView mText;
    }
    class GroupHold{
        TextView mText;
        ImageView mImage;
    }
}
