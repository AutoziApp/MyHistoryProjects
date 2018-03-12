package cn.com.mapuni.meshing.base.adapter;

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

import cn.com.mapuni.meshing.base.R;

/**
 * Description: 用来加载更多页面的数据listView显示
 * 
 * 
 * 
 * @author 钟学梅
 * 
 */
public class SortAdapter extends BaseAdapter {
	/** 获取列表的数据源 */
	private final ArrayList<HashMap<String, Object>> data;
	/** 上下文参数 */
	private final Context context;

	/** 构造函数 */
	public SortAdapter(Context context, ArrayList<HashMap<String, Object>> data) {
		this.context = context;
		this.data = data;
	}

	/**
	 * Description: 获取列表的图片
	 * 
	 * @param name
	 *            照片的名字
	 * @return 返回所需照片 Bitmap
	 * @author zxm Create at: 2013-07-04 上午11:30:37
	 */
	public Bitmap getRes(String name) {
		ApplicationInfo appInfo = context.getApplicationInfo();
		int resID = context.getResources().getIdentifier(name, "drawable",
				appInfo.packageName);
		return BitmapFactory.decodeResource(context.getResources(), resID);
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
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			holder = new ViewHolder();
			/** 用来加载listview的item布局 */
			convertView = LayoutInflater.from(context).inflate(
					R.layout.sort_item, null);
			/** 初始化item的组件 */
			holder.sortTypeTextView = (TextView) convertView
					.findViewById(R.id.sort_type_text);
			holder.sort_type_image = (ImageView) convertView
					.findViewById(R.id.sort_type_image);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		/** 通过holder来获取xml里的数据 */
		holder.sort_type_image.setImageBitmap(getRes(data.get(position)
				.get("lefticon").toString()));
		holder.sortTypeTextView.setText(data.get(position).get("menuname")
				.toString());
		return convertView;
	}

	/** 定义一个ViewHolder，用来优化listview */
	class ViewHolder {
		ImageView sort_type_image;
		TextView sortTypeTextView;
	}
}
