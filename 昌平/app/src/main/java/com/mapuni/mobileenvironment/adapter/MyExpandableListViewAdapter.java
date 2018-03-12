package com.mapuni.mobileenvironment.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.mapuni.mobileenvironment.R;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Mai on 2016/11/28.
 */

public class MyExpandableListViewAdapter extends BaseExpandableListAdapter {

    private Map<String, List<String>> dataset = new HashMap<>();
    private String[] parentList;
    private Context context;

    public MyExpandableListViewAdapter(Map<String, List<String>> dataset, String[] parentList, Context context) {
        this.dataset = dataset;
        this.parentList = parentList;
        this.context = context;
    }



    //  获得某个父项的某个子项
    @Override
    public Object getChild(int parentPos, int childPos) {
        return dataset.get(parentList[parentPos]).get(childPos);
    }

    //  获得父项的数量
    @Override
    public int getGroupCount() {
        return dataset.size();
    }

    //  获得某个父项的子项数目
    @Override
    public int getChildrenCount(int parentPos) {
        return dataset.get(parentList[parentPos]).size();
    }

    //  获得某个父项
    @Override
    public Object getGroup(int parentPos) {
        return dataset.get(parentList[parentPos]);
    }

    //  获得某个父项的id
    @Override
    public long getGroupId(int parentPos) {
        return parentPos;
    }

    //  获得某个父项的某个子项的id
    @Override
    public long getChildId(int parentPos, int childPos) {
        return childPos;
    }

    //  按函数的名字来理解应该是是否具有稳定的id，这个方法目前一直都是返回false，没有去改动过
    @Override
    public boolean hasStableIds() {
        return false;
    }


    //  获得子项显示的view
    @Override
    public View getChildView(int parentPos, int childPos, boolean b, View view, ViewGroup viewGroup) {
        if (view == null) {

            view = View.inflate(context, R.layout.child_item, null);

        }
        view.setTag(R.layout.parent_item, parentPos);
        view.setTag(R.layout.child_item, childPos);
        TextView text = (TextView) view.findViewById(R.id.child_title);
        text.setText(dataset.get(parentList[parentPos]).get(childPos));
        return view;
    }

    //  子项是否可选中，如果需要设置子项的点击事件，需要返回true
    @Override
    public boolean isChildSelectable(int i, int i1) {
        return true;
    }

    //  获得父项显示的view
    @Override
    public View getGroupView(int parentPos, boolean isExpanded, View view, ViewGroup viewGroup) {
        if (view == null) {
            view = View.inflate(context,R.layout.parent_item, null);
        }
        if(parentPos%2==0){
            view.setBackgroundResource(R.color.blue_item);
        }else{
            view.setBackgroundResource(R.color.white);
        }
        view.setTag(R.layout.parent_item, parentPos);
        view.setTag(R.layout.child_item, -1);
        TextView text = (TextView) view.findViewById(R.id.parent_title);
        text.setText(parentList[parentPos]+"("+dataset.get(parentList[parentPos]).size()+")");
        //判断isExpanded就可以控制是按下还是关闭，同时更换图片
        if(isExpanded){
            text.setSelected(false);
        }else{
            text.setSelected(true);
        }
        return view;
    }


}
