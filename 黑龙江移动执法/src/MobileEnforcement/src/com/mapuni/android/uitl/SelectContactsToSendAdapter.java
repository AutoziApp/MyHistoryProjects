package com.mapuni.android.uitl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.mapuni.android.base.business.ContactBean;
import com.mapuni.android.base.util.LogUtil;

import com.mapuni.android.MobileEnforcement.R;

public class SelectContactsToSendAdapter extends BaseAdapter {

	private final LayoutInflater inflater;
	private final List<ContactBean> list;
	private final HashMap<String, Integer> alphaIndexer;
	private final String[] sections;
	private final Context ctx;
	public static  Map<Integer, Boolean> isSelected;

	public SelectContactsToSendAdapter(Context context, List<ContactBean> list) {

		this.ctx = context;
		this.inflater = LayoutInflater.from(context);
		this.list = list;
		this.alphaIndexer = new HashMap<String, Integer>();
		this.sections = new String[list.size()];
		isSelected = new HashMap<Integer, Boolean>();

		for (int i = 0; i < list.size(); i++) {

			isSelected.put(i, false);
			if (list.get(i).getSelected() == 1) {
				isSelected.put(i, true);
			}

		}

	}

	@Override
	public int getCount() {
		LogUtil.i("ac", list.size() + "ad");
		return list.size();

	}

	@Override
	public Object getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	public void remove(int position) {
		list.remove(position);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		ViewHolder holder;
		if (convertView == null) {
			convertView = inflater.inflate(
					R.layout.select_contact_to_send_list_item, null);
			holder = new ViewHolder();

			holder.name = (TextView) convertView.findViewById(R.id.name);

			holder.check = (ImageView) convertView.findViewById(R.id.check);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		ContactBean cb = list.get(position);
		LogUtil.i("ac", cb + "");
		String name = cb.getQYMC();

		LogUtil.i("ac", name + "fsf");
		holder.name.setText(name);

		if (isSelected.get(position)) {
			holder.check.setImageResource(R.drawable.ic_checkbox_checked);
		} else {
			holder.check.setImageResource(R.drawable.ic_checkbox_unchecked);
		}

		return convertView;
	}

	private static class ViewHolder {

		TextView name;

		ImageView check;
	}

}
