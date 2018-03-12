package cn.com.mapuni.meshing.adapter;

import java.util.List;

import cn.com.mapuni.meshingtotal.R;

import cn.com.mapuni.meshing.model.DbNews;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class DbNewsAdapyer extends BaseAdapter {

	private Context context;
	private List<DbNews> list;
	public DbNewsAdapyer(Context context,List<DbNews> list){
		this.context=context;
		this.list=list;
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
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@Override
	public View getView(int i, View view, ViewGroup arg2) {
		  ViewHolder viewHolder=null;
	        if(view==null){
	            view=View.inflate(context, R.layout.db_list_item,null);
	            viewHolder=new ViewHolder();
	            viewHolder.tvId= (TextView) view.findViewById(R.id.tv_id);
	            viewHolder.tvImportance= (TextView) view.findViewById(R.id.importance);
	            viewHolder.tvEndDate= (TextView) view.findViewById(R.id.enddate);
	            viewHolder.tvTaskName= (TextView) view.findViewById(R.id.taskname);
	            viewHolder.tvDetails= (TextView) view.findViewById(R.id.details);
	            view.setTag(viewHolder);
	        }else {
	            viewHolder= (ViewHolder) view.getTag();
	        }
	        DbNews dbNews=list.get(i);
	        viewHolder.tvId.setText(dbNews.getId());
	        viewHolder.tvImportance.setText(dbNews.getImportance());
	        viewHolder.tvEndDate.setText(dbNews.getEndDate());
	        viewHolder.tvTaskName.setText(dbNews.getTaskName());
	        viewHolder.tvDetails.setText(dbNews.getDetails());
		return view;
	}
	 class ViewHolder{
	        TextView tvId;
	        TextView tvImportance;
	        TextView tvEndDate;
	        TextView tvTaskName;
	        TextView tvDetails;
	    }
}
