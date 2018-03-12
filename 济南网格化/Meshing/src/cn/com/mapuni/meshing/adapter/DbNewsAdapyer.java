package cn.com.mapuni.meshing.adapter;

import java.lang.annotation.Annotation;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.example.meshing.R;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.mapuni.android.base.util.DisplayUitl;

import cn.com.mapuni.meshing.activity.db_activity.DbscfkActivity;
import cn.com.mapuni.meshing.model.DbNews;
import cn.com.mapuni.meshing.model.TodoTaskModel;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class DbNewsAdapyer extends BaseAdapter {

	private Context context;
	private List<TodoTaskModel> list;

	public DbNewsAdapyer(Context context, List<TodoTaskModel> list) {
		this.context = context;
		this.list = list;
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
		ViewHolder viewHolder = null;
		if (view == null) {
			view = View.inflate(context, R.layout.db_list_item, null);
			viewHolder = new ViewHolder();
			viewHolder.tv_id = (TextView) view.findViewById(R.id.tv_id);
			;
			viewHolder.tv_taskName = (TextView) view.findViewById(R.id.tv_taskName);
			viewHolder.tv_problemDesc = (TextView) view.findViewById(R.id.tv_problemDesc);
			viewHolder.tv_grid_member = (TextView) view.findViewById(R.id.tv_grid_member);
			view.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) view.getTag();
		}
		final TodoTaskModel todoTaskModel = list.get(i);
		viewHolder.tv_id.setText(Integer.toString(i + 1));
		viewHolder.tv_taskName.setText(todoTaskModel.getTaskName());
		viewHolder.tv_problemDesc.setText(todoTaskModel.getProblemDesc());
		if(1==compareTime(todoTaskModel.getCreateTime())){
			viewHolder.tv_problemDesc.setTextColor(Color.RED);
		}else{
			viewHolder.tv_problemDesc.setTextColor(Color.parseColor("#414141"));
		}
		viewHolder.tv_grid_member.setText(todoTaskModel.getCreateUserName());
//		viewHolder.tv_grid_member.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//				if (DisplayUitl.isFastDoubleClick()) {
//					return;
//				}
//				Intent intent = new Intent(context, DbscfkActivity.class);
//
//				intent.putExtra("id", todoTaskModel.getId());//post submit
//				intent.putExtra("patrolId", todoTaskModel.getPatrolId());//get detail
//				context.startActivity(intent);
//			}
//		});
		return view;
	}

	class ViewHolder {
		TextView tv_id;
		TextView tv_taskName;
		TextView tv_problemDesc;
		TextView tv_grid_member;
	}
	  // 将字符串转为时间戳
    public static String getTime(String createTime) {
        String re_time = null;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date d;
        try {
            d = sdf.parse(createTime);
            long l = d.getTime();
            String str = String.valueOf(l);
            re_time = str.substring(0, 10);
        }catch (ParseException e) {
            // TODO Auto-generated catch block e.printStackTrace();
        }
        return re_time;
    }
    //比较当前时间与创建时间，如果大于3天返回1；否则返回0
    public static int compareTime(String createTime){
    	String strTimeStamp=getTime(createTime);
    	long oldTime=Long.parseLong(strTimeStamp)*1000;
    	long curTime=System.currentTimeMillis();
    	if((curTime-oldTime)>72*3600*1000){
    		return 1;
    	}else{
    		return 0;
    	}
    }
}
