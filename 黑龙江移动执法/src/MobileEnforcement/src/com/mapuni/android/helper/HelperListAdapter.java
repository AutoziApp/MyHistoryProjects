package com.mapuni.android.helper;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.mapuni.android.MobileEnforcement.R;

/**
 * FileName: QYNameListAdapter
 * 
 * @author 赵瑞青
 * @Version 1.0
 * @Copyright 中科宇图天下科技有限公司 Create at: 2013-8-5 下午15:48:46
 */
public class HelperListAdapter extends BaseAdapter {

	public Context context;
	public ArrayList<HashMap<String, Object>> data;

	public HelperListAdapter(Context context,
			ArrayList<HashMap<String, Object>> data) {
		this.context = context;
		this.data = data;
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

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {

		ViewHolder holder = null;
		if (convertView == null) {
			holder = new ViewHolder();
			/** 用来加载GridView的item布局 */
			convertView = LayoutInflater.from(context).inflate(
					R.layout.helper_gridview_list, null);
			/** 初始化item的组件 */
			holder.helper_main_tv = (TextView) convertView
					.findViewById(R.id.helper_main_textView);
			holder.helper_main_img = (ImageView) convertView
					.findViewById(R.id.helper_main_imageView);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		/** 通过holder来获取xmL里的数据 */
		holder.helper_main_img.setImageBitmap(getRes(data.get(position)
				.get("lefticon").toString()));
		holder.helper_main_tv.setText(data.get(position).get("menuname")
				.toString());
		return convertView;
	}

	/** 定义一个ViewHolder，用来优化View */
	class ViewHolder {
		ImageView helper_main_img;
		TextView helper_main_tv;
	}

	/** Description: 获取列表的图片 */
	public Bitmap getRes(String name) {
		ApplicationInfo appInfo = context.getApplicationInfo();
		int resID = context.getResources().getIdentifier(name, "drawable",
				appInfo.packageName);
		return BitmapFactory.decodeResource(context.getResources(), resID);
	}

}
