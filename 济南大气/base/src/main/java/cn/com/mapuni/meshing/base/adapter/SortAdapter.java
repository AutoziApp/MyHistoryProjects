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
 * Description: �������ظ���ҳ�������listView��ʾ
 * 
 * 
 * 
 * @author ��ѧ÷
 * 
 */
public class SortAdapter extends BaseAdapter {
	/** ��ȡ�б������Դ */
	private final ArrayList<HashMap<String, Object>> data;
	/** �����Ĳ��� */
	private final Context context;

	/** ���캯�� */
	public SortAdapter(Context context, ArrayList<HashMap<String, Object>> data) {
		this.context = context;
		this.data = data;
	}

	/**
	 * Description: ��ȡ�б��ͼƬ
	 * 
	 * @param name
	 *            ��Ƭ������
	 * @return ����������Ƭ Bitmap
	 * @author zxm Create at: 2013-07-04 ����11:30:37
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
			/** ��������listview��item���� */
			convertView = LayoutInflater.from(context).inflate(
					R.layout.sort_item, null);
			/** ��ʼ��item����� */
			holder.sortTypeTextView = (TextView) convertView
					.findViewById(R.id.sort_type_text);
			holder.sort_type_image = (ImageView) convertView
					.findViewById(R.id.sort_type_image);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		/** ͨ��holder����ȡxml������� */
		holder.sort_type_image.setImageBitmap(getRes(data.get(position)
				.get("lefticon").toString()));
		holder.sortTypeTextView.setText(data.get(position).get("menuname")
				.toString());
		return convertView;
	}

	/** ����һ��ViewHolder�������Ż�listview */
	class ViewHolder {
		ImageView sort_type_image;
		TextView sortTypeTextView;
	}
}
