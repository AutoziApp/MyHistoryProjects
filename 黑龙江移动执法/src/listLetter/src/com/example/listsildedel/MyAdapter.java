package com.example.listsildedel;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.SectionIndexer;
import android.widget.TextView;

public class MyAdapter extends BaseAdapter implements SectionIndexer {

	private List<HashMap<String, Object>> list = null;
	private Context mContext;

	public MyAdapter(Context mContext, List<HashMap<String, Object>> list) {
		this.mContext = mContext;
		this.list = list;
	}

	public int getCount() {
		return this.list.size();
	}

	public Object getItem(int position) {
		return list.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(final int position, View view, ViewGroup arg2) {
		ViewHolder viewHolder = null;
		if (view == null) {
			viewHolder = new ViewHolder();
			view = LayoutInflater.from(mContext).inflate(R.layout.item, null);
			viewHolder.tvTitle = (TextView) view.findViewById(R.id.title);
			viewHolder.tvLetter = (TextView) view.findViewById(R.id.catalog);
			viewHolder.tvCode = (TextView) view.findViewById(R.id.code);
			view.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) view.getTag();
		}
		final Map<String, Object> mContent = list.get(position);
		if (position == 0) {
			viewHolder.tvLetter.setVisibility(View.VISIBLE);
			viewHolder.tvLetter.setText(mContent.get("pid").toString());
		} else {
			String lastCatalog = list.get(position - 1).get("pid").toString();
			if (mContent.get("pid").toString().equals(lastCatalog)) {
				viewHolder.tvLetter.setVisibility(View.GONE);
			} else {
				viewHolder.tvLetter.setVisibility(View.VISIBLE);
				viewHolder.tvLetter.setText(mContent.get("pid").toString());
			}
		}

		viewHolder.tvTitle.setText(mContent.get("name").toString());
		viewHolder.tvCode.setText(mContent.get("code").toString());

		return view;

	}

	final static class ViewHolder {
		TextView tvTitle;
		TextView tvLetter;
		TextView tvCode;
	}

	public Object[] getSections() {
		// TODO Auto-generated method stub
		return null;
	}

	public int getSectionForPosition(int position) {

		return 0;
	}

	public int getPositionForSection(int section) {
		HashMap<String, Object> mContent;
		String l;
		if (section == '!') {
			return 0;
		} else {
			for (int i = 0; i < getCount(); i++) {
				mContent = list.get(i);
				l = mContent.get("pid").toString();
				if (null == l || "".equals(l)) {
					continue;
				}
				char firstChar = l.toUpperCase().charAt(0);
				if (((Character) firstChar).hashCode() == section) {
					return i + 1;
				}
			}
		}
		mContent = null;
		l = null;
		return -1;
	}
}