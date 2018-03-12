package cn.com.mapuni.meshing.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

import com.example.meshing.R;

public class WdWgyAdapter extends BaseAdapter {
	// private List<Map<String, String>> data;
	private LayoutInflater layoutInflater;
	private ArrayList<HashMap<String, String>> data;
	private Context context;

	public WdWgyAdapter(Context context, ArrayList<HashMap<String, String>> data) {
		this.context = context;
		this.data = new ArrayList<HashMap<String, String>>();
		this.data = data;
		this.layoutInflater = LayoutInflater.from(context);
	}

	public void update(ArrayList<HashMap<String, String>> data) {
		this.data = data;
		this.notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		return data.size();
	}

	@Override
	public Object getItem(int position) {
		return data.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	private int clickTemp = -1;

	// 标识选择的Item
	public void setSeclection(int position) {
		clickTemp = position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		List_event_manger list_event_manger = null;
		if (convertView == null) {
			list_event_manger = new List_event_manger();
			// 获得组件，实例化组件
			convertView = layoutInflater.inflate(R.layout.wd_list_item, null);

			list_event_manger.wd_tb = (ImageView) convertView
					.findViewById(R.id.wd_tb);
			list_event_manger.wd_text1 = (TextView) convertView
					.findViewById(R.id.wd_text1);
			list_event_manger.wd_text2 = (TextView) convertView
					.findViewById(R.id.wd_text2);
			list_event_manger.wd_fgx = (TextView) convertView
					.findViewById(R.id.wd_fgx);
			list_event_manger.wd_relativeLayout = (RelativeLayout) convertView
					.findViewById(R.id.wd_relativeLayout);
			convertView.setTag(list_event_manger);
		} else {
			list_event_manger = (List_event_manger) convertView.getTag();
		}
		if (position == 0) {
			// list_event_manger.wd_text1.setText("任务信息");
			// list_event_manger.wd_text2.setText("展现所有上报的任务");
			list_event_manger.wd_tb.setImageResource(R.drawable.wd_rwxx);
			list_event_manger.wd_fgx.setVisibility(View.VISIBLE);
		} else {
			// list_event_manger.wd_text1.setText("污染源信息");
			// list_event_manger.wd_text2.setText("辖区内所有污染源");
			list_event_manger.wd_tb.setImageResource(R.drawable.wd_wryxx);
			list_event_manger.wd_fgx.setVisibility(View.INVISIBLE);
		}
		list_event_manger.wd_text1.setText(data.get(position).get("wd_text1")
				+ "");
		list_event_manger.wd_text2.setText(data.get(position).get("wd_text2")
				+ "");
		if (clickTemp == position) {
			list_event_manger.wd_relativeLayout.setBackgroundColor(Color.parseColor("#e0f5ea"));
		} else {
			list_event_manger.wd_relativeLayout.setBackgroundColor(Color.TRANSPARENT);
		}
		return convertView;
	}

	public final class List_event_manger {
		public ImageView wd_tb;
		public TextView wd_text1, wd_text2;
		public TextView wd_fgx;
		public RelativeLayout wd_relativeLayout;

	}
}
