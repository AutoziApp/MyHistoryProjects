package com.mapuni.android.enforcement;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.mapuni.android.MobileEnforcement.R;
import com.mapuni.android.base.BaseActivity;

/**
 * 环境监察通知书界面 NOEM = Notice of environmental monitoring 环境监察通知书
 * 
 * @author Sahadev Create at 2014-5-26 17:04:05
 */
public class NOEMActivity extends BaseActivity {
	private LinearLayout layout;// 中间填充的主要布局
	private ListView mainListView, childListView;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		SetBaseStyle("环境监察通知书");

		LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		layout = (LinearLayout) inflater.inflate(R.layout.layout_noem, null);
		middleLayout.addView(layout);

		Resources data = getResources();
		String temp[] = data.getStringArray(R.array.noem_main);

		mainListView = new ListView(this);
		mainListView.setCacheColorHint(Color.TRANSPARENT);
		mainListView.setDivider(null);
		mainListView.setDescendantFocusability(ViewGroup.FOCUS_AFTER_DESCENDANTS);

		layout.addView(mainListView);

		mainListView.setAdapter(new MainAdapter(temp, this) {

			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				ViewHolder viewHolder = null;
				if (convertView == null) {
					convertView = this.inflater.inflate(R.layout.noem_main, null);
					viewHolder = new ViewHolder();
					viewHolder.input = (EditText) convertView.findViewById(R.id.input);

					convertView.setTag(viewHolder);
				}
				viewHolder = (ViewHolder) convertView.getTag();

				viewHolder.input.setHint(data[position]);

				viewHolder.input.setOnFocusChangeListener(new OnFocusChangeListener() {

					@Override
					public void onFocusChange(View v, boolean hasFocus) {
						if (!hasFocus) {
							String value = ((TextView) v).getText().toString();
						}
					}
				});
				return convertView;
			}

			@Override
			public Object getItem(int position) {
				return this.data[position];
			}

			class ViewHolder {
				EditText input;
			}
		});

	}

	private abstract class MainAdapter extends BaseAdapter {
		protected String[] data;
		protected LayoutInflater inflater;
		protected Context context;

		public MainAdapter(String[] data, Context context) {
			super();
			this.data = data;
			inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			this.context = context;
		}

		@Override
		public int getCount() {
			return data.length;
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

	}
}