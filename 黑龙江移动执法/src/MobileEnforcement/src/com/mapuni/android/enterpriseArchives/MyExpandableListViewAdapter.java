package com.mapuni.android.enterpriseArchives;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mapuni.android.MobileEnforcement.R;

public class MyExpandableListViewAdapter extends BaseExpandableListAdapter {

	 private ArrayList<String> groupArray;
     private ArrayList<ArrayList<HashMap<String, Object>>> childArray;
     private Context context;
     private LayoutInflater inflater;
    
    
     public MyExpandableListViewAdapter(Context context){
    	 inflater = LayoutInflater.from(context);
    	 this.context = context;
     }
     
     public MyExpandableListViewAdapter(Context context,ArrayList<String> courseGroupList,
    		 ArrayList<ArrayList<HashMap<String, Object>>> childArray){
         inflater = LayoutInflater.from(context);
         this.context = context;
         
         this.groupArray = courseGroupList;
         this.childArray = childArray;
     }
    
     /** 加载数据方法 */
     public void setData(ArrayList<String> courseGroupList,ArrayList<ArrayList<HashMap<String, Object>>> childArray){
    	 this.groupArray = courseGroupList;
         this.childArray = childArray;
     }
    
    
    public int getGroupCount() {
        return groupArray.size();
    }

    public int getChildrenCount(int groupPosition) {
        return childArray.get(groupPosition).size();
    }

    public Object getGroup(int groupPosition) {
        return groupArray.get(groupPosition);
    }

    public Object getChild(int groupPosition, int childPosition) {
        return childArray.get(groupPosition).get(childPosition);
    }

    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    public boolean hasStableIds() {
        return false;
    }

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {
		LinearLayout ll = new LinearLayout(context);
		
		ll.setGravity(Gravity.CENTER_VERTICAL);
//		ImageView logo = new ImageView(context);
//		
//		logo.setPadding(65, 8, 8,8);
//		ll.addView(logo);
		TextView textView = new TextView(context);
		textView.setTextColor(Color.BLACK);
		textView.setTextSize(20);
		textView.setPadding(120, 20, 20, 8);
		textView.setText(getGroup(groupPosition).toString());
		ll.addView(textView);

		return ll;
	}

    public View getChildView(int groupPosition, int childPosition,
            boolean isLastChild, View convertView, ViewGroup parent) {

    	ViewHolder holder = new ViewHolder();
    	
        if(convertView == null){
        	convertView = inflater.inflate(R.layout.expandable_list_item, parent, false);
        }
        
        holder.tv_item = (TextView) convertView.findViewById(R.id.expandlist_info);
        
        String name = childArray.get(groupPosition).get(childPosition).get("filename").toString();
        holder.tv_item.setText(name);
        
        return convertView;
    }

    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
    
    
    class ViewHolder{
    	TextView tv_item;
    }
    

}
