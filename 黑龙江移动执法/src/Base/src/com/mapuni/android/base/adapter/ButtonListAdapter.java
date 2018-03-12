package com.mapuni.android.base.adapter;

import java.util.ArrayList;

import com.mapuni.android.attachment.T_BAS_Attachment;




import android.R;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class ButtonListAdapter extends BaseAdapter {
	private Context mContext;
	private ArrayList<T_BAS_Attachment> mAttachment;
	
	public ButtonListAdapter(Context context,ArrayList<T_BAS_Attachment> attachment){
		this.mContext = context;
		this.mAttachment = attachment;
	}
	@Override
	public int getCount() {
		return mAttachment.size();
	}

	@Override
	public Object getItem(int position) {
		return mAttachment.get(position);
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		convertView = LayoutInflater.from(mContext).inflate(android.R.layout.simple_list_item_1, null);
		TextView tv = (TextView) convertView.findViewById(R.id.text1);
		tv.setText(mAttachment.get(position).getFileName()+mAttachment.get(position).getExtension());
		
		return convertView;
	}

}
