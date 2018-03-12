package cn.com.mapuni.meshing.adapter;

import java.util.HashMap;
import java.util.List;

import org.apache.commons.httpclient.util.DateUtil;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import cn.com.mapuni.meshing.model.XCPlanModel;
import cn.com.mapuni.meshing.util.DateUtils;

import com.example.meshing.R;
import com.mapuni.android.base.BaseActivity;

public class XCPlanAdapter extends BaseAdapter {

	private Context context;
	private List<XCPlanModel.RowsBean> list;

	public XCPlanAdapter(Context context,List<XCPlanModel.RowsBean> list) {
		this.context = context;
		this.list=list;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder viewHolder;
		if(convertView==null){
			viewHolder=new ViewHolder();
			convertView=View.inflate(context, R.layout.xcplan_list_item, null);
			viewHolder.title=(TextView) convertView.findViewById(R.id.tv_qiye_name);
			viewHolder.time=(TextView) convertView.findViewById(R.id.tv_time);
			viewHolder.content=(TextView)convertView.findViewById(R.id.tv_content);
			convertView.setTag(viewHolder);
		}else {
			viewHolder=(ViewHolder) convertView.getTag();
		}
		viewHolder.title.setText(list.get(position).getEntname());
		viewHolder.time.setText(DateUtils.long2DateString(list.get(position).getUpdateTime()));
		viewHolder.content.setText(list.get(position).getDescribe());
		return convertView;
	}
	
	static class ViewHolder 
    { 
        public TextView title; 
        public TextView time; 
        public TextView content;
    }  
	

}
