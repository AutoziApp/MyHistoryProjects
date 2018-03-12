package cn.com.mapuni.meshing.activity.wd_activity;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.meshing.R;


public class EmergencyFujianAdapter extends BaseAdapter {
	ArrayList<HashMap<String, Object>>  list;
	Context content;
	String type;

	public EmergencyFujianAdapter(String type,ArrayList<HashMap<String, Object>>  list,
			Context content) {
		super();
		this.list = list;
		this.content = content;
		this.type = type;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return list.get(arg0);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int index, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (null == convertView) {
			// 装载布局文件 app_item.xml
			convertView = (LinearLayout)LayoutInflater.from(content).inflate(
					R.layout.yqyd_listview_layout_items, null);
			holder = new ViewHolder();
			holder.name = (TextView) convertView.findViewById(R.id.anniuncement_title);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
        holder.name.setText(list.get(index).get("filename")+"");
    	
		return convertView;
	}

	class ViewHolder {
		private TextView name;// 名称 
	}
}
