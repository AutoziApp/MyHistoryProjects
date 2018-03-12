package cn.com.mapuni.meshing.adapter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import cn.com.mapuni.meshing.model.TodoTaskModel;
import cn.com.mapuni.meshing.model.XiaFaTaskModel;

import com.example.meshing.R;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class CommonAdapter extends BaseAdapter{
	private Context context;
	private List<HashMap<String, Object>> list;

	public CommonAdapter(Context context, List<HashMap<String, Object>> list) {
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
			viewHolder.tv_taskName = (TextView) view.findViewById(R.id.tv_taskName);
			viewHolder.tv_problemDesc = (TextView) view.findViewById(R.id.tv_problemDesc);
			viewHolder.tv_grid_member = (TextView) view.findViewById(R.id.tv_grid_member);
			view.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) view.getTag();
		}
		if (list.get(i).get("rwly").equals("巡查任务")) {
			final TodoTaskModel todoTaskModel = (TodoTaskModel)list.get(i).get("content");
			viewHolder.tv_id.setText(Integer.toString(i + 1));
			viewHolder.tv_taskName.setText(todoTaskModel.getTaskName());
			if(1==compareTime(todoTaskModel.getCreateTime())) {
				viewHolder.tv_taskName.setTextColor(Color.RED);
			} else {
				viewHolder.tv_taskName.setTextColor(Color.parseColor("#414141"));
			}
		} else {
			final XiaFaTaskModel xiaFaTaskModel = (XiaFaTaskModel)list.get(i).get("content");
			viewHolder.tv_id.setText(Integer.toString(i + 1));
			viewHolder.tv_taskName.setText(xiaFaTaskModel.getTaskName());
			if(1==compareTime(xiaFaTaskModel.getBeginTime())||"1".equals(xiaFaTaskModel.getReminded())) {
				viewHolder.tv_taskName.setTextColor(Color.RED);
			} else{
				viewHolder.tv_taskName.setTextColor(Color.parseColor("#414141"));
			}
		}
		viewHolder.tv_problemDesc.setText(list.get(i).get("rwly").toString());
		viewHolder.tv_grid_member.setVisibility(View.GONE);
		
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
        }catch (Exception e) {
            // TODO Auto-generated catch block e.printStackTrace();
        }
        return re_time;
    }
    //比较当前时间与创建时间，如果大于3天返回1；否则返回0
    public static int compareTime(String createTime){
    	String strTimeStamp=getTime(createTime);
    	if(strTimeStamp != null) {
    		try {
    			long oldTime=Long.parseLong(strTimeStamp)*1000;
    	    	long curTime=System.currentTimeMillis();
    	    	if((curTime-oldTime)>72*3600*1000){
    	    		return 1;
    	    	}else{
    	    		return 0;
    	    	}
			} catch (Exception e) {
				// TODO: handle exception
				return 1;
			}
	    	
    	}else {
			return 1;
		}
    }

}
