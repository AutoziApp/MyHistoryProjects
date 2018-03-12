package cn.com.mapuni.meshing.adapter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.example.meshing.R;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.TextView;

public class MyExpandableListViewAdapter extends BaseExpandableListAdapter {

    private Map<String, List<String>> dataset = new HashMap<String, List<String>>();
    private List<String> parentList;
    
    private Context context;
    
    public MyExpandableListViewAdapter(Map<String, List<String>> dataset, List<String> parentList, Context context) {
        this.dataset = dataset;
        this.parentList = parentList;
        this.context = context;
    }



    //  获得某个父项的某个子项
    @Override
    public Object getChild(int parentPos, int childPos) {
        return dataset.get(parentList.get(parentPos)).get(childPos);
    }

    //  获得父项的数量
    @Override
    public int getGroupCount() {
        return dataset.size();
    }

    //  获得某个父项的子项数目
    @Override
    public int getChildrenCount(int parentPos) {
        return dataset.get(parentList.get(parentPos)).size();
    }

    //  获得某个父项
    @Override
    public Object getGroup(int parentPos) {
        return dataset.get(parentList.get(parentPos));
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

		TextView text = (TextView) view.findViewById(R.id.two_class_interface_name_tv);
		RadioButton isSelect = (RadioButton) view.findViewById(R.id.two_class_cb);
        text.setText(dataset.get(parentList.get(parentPos)).get(childPos));
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
        TextView text = (TextView) view.findViewById(R.id.tv_parent_title);
        text.setText(parentList.get(parentPos));
//        //判断isExpanded就可以控制是按下还是关闭，同时更换图片
//        if(isExpanded){
//            text.setSelected(false);
//        }else{
//            text.setSelected(true);
//        }
        return view;
    }


}
