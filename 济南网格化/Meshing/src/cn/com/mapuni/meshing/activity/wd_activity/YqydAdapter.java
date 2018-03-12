package cn.com.mapuni.meshing.activity.wd_activity;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.meshing.R;


public class YqydAdapter extends BaseAdapter {
	ArrayList<HashMap<String, Object>>  list;
	Context content;
	int type;

	public YqydAdapter(int type,ArrayList<HashMap<String, Object>>  list,
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
			convertView = (RelativeLayout)LayoutInflater.from(content).inflate(
					R.layout.yqyd_layout_items, null);
			holder = new ViewHolder();
			holder.name = (TextView) convertView.findViewById(R.id.anniuncement_title);
//			holder.num = (TextView) convertView.findViewById(R.id.anniuncement_title1);
			
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		String name = "";
        if(type == 1){
        	name = list.get(index).get("productionname")+"";
        }else if(type == 2){
        	name = list.get(index).get("processingname")+"";
        }else if(type == 3){
        	name = list.get(index).get("equipmentname")+"";
        }
        holder.name.setText(name);
		return convertView;
	}

	class ViewHolder {
		private TextView name;// 名称  事件编号
	}
}
