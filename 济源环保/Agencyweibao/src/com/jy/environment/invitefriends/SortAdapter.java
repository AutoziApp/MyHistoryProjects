package com.jy.environment.invitefriends;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.RelativeLayout;
import android.widget.SectionIndexer;
import android.widget.TextView;

import com.jy.environment.R;
import com.jy.environment.util.FileUtils;
import com.jy.environment.util.MyLog;

@SuppressLint("NewApi")
public class SortAdapter extends BaseAdapter implements SectionIndexer {
	private List<SortModel> list = null;
	private Context mContext;
	boolean is = false;
	SharedPreferences sharedPreference;
	HashSet<String> values;
	private Map<Integer, Boolean> isCheckMap = new HashMap<Integer, Boolean>();
	private HashMap<Integer, View> lmap = new HashMap<Integer, View>();
	public String wenjian;

	public SortAdapter(Context mContext, List<SortModel> list) {
		this.mContext = mContext;
		this.list = list;
		sharedPreference = mContext.getSharedPreferences("phone",
				Context.MODE_PRIVATE);
		// 初始化,默认都没有选中
		wenjian =FileUtils.read(mContext, "phone.txt");
		MyLog.i(">>>>>>wenjian"+wenjian);
		configCheckMap(false);
	}

	/**
	 * 首先,默认情况下,所有项目都是没有选中的.这里进行初始化
	 */
	public void configCheckMap(boolean bool) {

		for (int i = 0; i < list.size(); i++) {
			isCheckMap.put(i, bool);
		}

	}

	/**
	 * 
	 * 
	 * @param list
	 */
	public void updateListView(List<SortModel> list) {
		this.list = list;
		notifyDataSetChanged();
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

	@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
	@SuppressLint("NewApi")
	public View getView(final int position, View view, ViewGroup arg2) {
		ViewHolder viewHolder = null;
		final SortModel mContent = list.get(position);
		if (lmap.get(position) == null) {
			viewHolder = new ViewHolder();
			view = LayoutInflater.from(mContext).inflate(
					R.layout.invitefriends_sort_item, null);
			viewHolder.tvTitle = (TextView) view.findViewById(R.id.title);
			viewHolder.tvLetter = (TextView) view.findViewById(R.id.catalog);
			viewHolder.tvButton = (Button) view.findViewById(R.id.tvButton);
			viewHolder.item_sms = (RelativeLayout) view
					.findViewById(R.id.item_sms);
			lmap.put(position, view);
			viewHolder.checkBox = (CheckBox) view.findViewById(R.id.cbCheckBox);
			view.setTag(viewHolder);
		} else {
			view = lmap.get(position);
			viewHolder = (ViewHolder) view.getTag();
		}
		/*
		 * 设置单选按钮的选中
		 */
		if (isCheckMap.get(position)) {

			viewHolder.checkBox.setChecked(true);
		} else {
			viewHolder.checkBox.setChecked(false);
		}
		viewHolder.checkBox
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {
					@Override
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {

						/*
						 * 将选择项加载到map里面寄存
						 */
						isCheckMap.put(position, isChecked);
						notifyDataSetChanged();
					}
				});
		final CheckBox checkBox = viewHolder.checkBox;
		int section = getSectionForPosition(position);

		if (position == getPositionForSection(section)) {
			viewHolder.tvLetter.setVisibility(View.VISIBLE);
			viewHolder.tvLetter.setText(mContent.getSortLetters());
		} else {
			viewHolder.tvLetter.setVisibility(View.GONE);
		}
		viewHolder.item_sms.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (isCheckMap.get(position)) {
					checkBox.setChecked(false);
					isCheckMap.put(position, false);
					notifyDataSetChanged();
				} else {
					checkBox.setChecked(true);
					isCheckMap.put(position, true);
					notifyDataSetChanged();
				}
			}
		});
		viewHolder.tvTitle.setText(list.get(position).getName());
		// values = (HashSet<String>)
		// sharedPreference.getStringSet("phoneNumble",
		// null);
		// MyLog.i(">>>>>>>>values" + values);
		MyLog.i(">>>>>>>>wenjian"+wenjian);
		if (null==mContent.getStrPhoneNumber()||null==wenjian||"".equals(wenjian)
				|| (!wenjian.contains(mContent.getStrPhoneNumber()))) {
			viewHolder.tvButton.setBackground(mContext.getResources()
					.getDrawable(R.drawable.invite1));

		} else {
			viewHolder.tvButton.setBackground(mContext.getResources()
					.getDrawable(R.drawable.invite2));
			viewHolder.tvButton.setEnabled(false);
		}
		// viewHolder.tvButton.setOnClickListener(new View.OnClickListener() {
		//
		// @Override
		// public void onClick(View v) {
		// // TODO Auto-generated method stub
		// // values = (HashSet<String>) sharedPreference.getStringSet(
		// // "phoneNumble", null);
		// // if (values == null) {
		// // values = new HashSet<String>();
		// // }
		// // values.add(list.get(position).getStrPhoneNumber());
		// //
		// // sharedPreference.edit().putStringSet("phoneNumble", values)
		// // .commit();
		// String content =
		// "微保可以实时查看所在城市空气质量，推荐你用一下。下载地址:www.weibao.net/download";
		// // Intent mIntent = new Intent(Intent.ACTION_VIEW);
		// // mIntent.putExtra("address", list.get(position)
		// // .getStrPhoneNumber());
		// // mIntent.putExtra("sms_body", content);
		// // mIntent.setType("vnd.android-dir/mms-sms");
		// // mContext.startActivity(mIntent);
		// Intent intent = new Intent(mContext, SmsActivity.class);
		// intent.putExtra("phoneNumber", list.get(position).getName()
		// + "<" + list.get(position).getStrPhoneNumber() + ">");
		// intent.putExtra("phoneContent", content);
		// mContext.startActivity(intent);
		// }
		// });

		return view;

	}

	final static class ViewHolder {
		TextView tvLetter;
		TextView tvTitle;
		Button tvButton;
		CheckBox checkBox;
		RelativeLayout item_sms;
	}

	/**
	 * ���ListView�ĵ�ǰλ�û�ȡ���������ĸ��Char asciiֵ
	 */
	public int getSectionForPosition(int position) {
		return list.get(position).getSortLetters().charAt(0);
	}

	public int getPositionForSection(int section) {
		for (int i = 0; i < getCount(); i++) {
			String sortStr = list.get(i).getSortLetters();
			char firstChar = sortStr.toUpperCase().charAt(0);
			if (firstChar == section) {
				return i;
			}
		}

		return -1;
	}

	/**
	 * ��ȡӢ�ĵ�����ĸ����Ӣ����ĸ��#���档
	 * 
	 * @param str
	 * @return
	 */
	private String getAlpha(String str) {
		String sortStr = str.trim().substring(0, 1).toUpperCase();
		// ������ʽ���ж�����ĸ�Ƿ���Ӣ����ĸ
		if (sortStr.matches("[A-Z]")) {
			return sortStr;
		} else {
			return "#";
		}
	}

	@Override
	public Object[] getSections() {
		return null;
	}

	public Map<Integer, Boolean> getCheckMap() {
		return this.isCheckMap;
	}
}