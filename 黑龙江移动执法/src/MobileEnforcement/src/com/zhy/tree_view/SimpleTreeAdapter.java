package com.zhy.tree_view;

import java.util.List;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mapuni.android.MobileEnforcement.R;
import com.zhy.tree.bean.Node;
import com.zhy.tree.bean.TreeListViewAdapter;

public class SimpleTreeAdapter<T> extends TreeListViewAdapter<T> {
	public SimpleTreeAdapter(ListView mTree, Context context, List<T> datas,
			int defaultExpandLevel) throws IllegalArgumentException,
			IllegalAccessException {
		super(mTree, context, datas, defaultExpandLevel);

	}

	@Override
	public View getConvertView(Node node, int position, View convertView,
			ViewGroup parent) {

		ViewHolder viewHolder = null;
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.list_item_tree, parent, false);
			viewHolder = new ViewHolder();
			// -------------------父节点布局----------------------
			viewHolder.bumen_info_layout = (RelativeLayout) convertView
					.findViewById(R.id.bumen_info_layout);
			viewHolder.icon = (ImageView) convertView
					.findViewById(R.id.id_treenode_icon);
			viewHolder.id_treenode_label = (TextView) convertView
					.findViewById(R.id.id_treenode_label);
			// -------------------子节点布局--------------------------
			viewHolder.users_info_layout = (RelativeLayout) convertView
					.findViewById(R.id.users_info_layout);
			viewHolder.tv_name = (TextView) convertView
					.findViewById(R.id.tv_name);
			viewHolder.tv_dptName = (TextView) convertView
					.findViewById(R.id.tv_dptName);
			viewHolder.tc_phone = (TextView) convertView
					.findViewById(R.id.tc_phone);
			
			convertView.setTag(viewHolder);

		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		if (node.getIcon() == -1 && node.getName().contains(",")) {
			viewHolder.bumen_info_layout.setVisibility(View.GONE);
			viewHolder.users_info_layout.setVisibility(View.VISIBLE);
			try {
				String[] name = node.getName().split(",");
				viewHolder.tv_name.setText(name[0]);
				viewHolder.tc_phone.setText(name[1]);
				viewHolder.tv_dptName.setText(name[2]);
		
			} catch (Exception e) {
				viewHolder.tv_name.setText(node.getName());
			}
		} else {
			viewHolder.users_info_layout.setVisibility(View.GONE);
			viewHolder.bumen_info_layout.setVisibility(View.VISIBLE);
			viewHolder.icon.setImageResource(node.getIcon());
			viewHolder.id_treenode_label.setText(node.getName());
		}
		return convertView;
	}

	private final class ViewHolder {
		ImageView icon;
		TextView id_treenode_label;

		public RelativeLayout bumen_info_layout, users_info_layout;
		public TextView tv_name;
		public TextView tv_dptName;
		public TextView tc_phone;
	}
}
