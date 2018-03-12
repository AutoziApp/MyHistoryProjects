package com.mapuni.android.infoQuery;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.mapuni.android.MobileEnforcement.R;
import com.mapuni.android.dataprovider.SqliteUtil;
import com.mapuni.android.infoQuery.SuperTreeViewAdapter.SuperTreeNode;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AbsListView;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class TreeViewAdapter extends BaseExpandableListAdapter {

	/** 每一个节点的高度 */
	public static final int ItemHeight = 75;
	/** 每一项距左边的距离 */
	public static final int PaddingLeft = 36;
	/** 初始化据左边的的距离 */
	private int myPaddingLeft = 0;

	static public class TreeNode {
		Object Title;
		Object Id;
		List<Object> childs = new ArrayList<Object>();
	}

	List<TreeNode> treeNodes = new ArrayList<TreeNode>();
	Context parentContext;

	public TreeViewAdapter(Context context, int myPaddingLeft) {
		parentContext = context;
		this.myPaddingLeft = myPaddingLeft;

	}

	public List<TreeNode> getTreeNode() {
		return treeNodes;
	}

	public void updateTreeNode(List<TreeNode> nodes) {
		treeNodes = nodes;
	}

	public void removeAll() {
		treeNodes.clear();
	}

	@Override
	public Object getChild(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return treeNodes.get(groupPosition).childs.get(childPosition);
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return childPosition;
	}

	static public TextView getTextView(Context context) {
		AbsListView.LayoutParams lp = new AbsListView.LayoutParams(
				ViewGroup.LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);

		TextView textView = new TextView(context);
		textView.setTextSize(22);
		textView.setLayoutParams(lp);
		textView.setPadding(10, 0, 0, 0);
		textView.setGravity(Gravity.CENTER_VERTICAL);

		return textView;
	}

	@Override
	public View getChildView(int groupPosition, int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		convertView = LayoutInflater.from(parentContext).inflate(
				R.layout.flfgtreeview, null);
		TextView textView = (TextView) convertView.findViewById(R.id.flfgtitle);
		String str = getChild(groupPosition, childPosition).toString();
		String id = str.substring(0, str.indexOf("|"));
		String title = str.substring(str.indexOf("|") + 1);
		ImageView imageView = (ImageView) convertView.findViewById(R.id.flfglefticon);
		imageView.setVisibility(View.INVISIBLE);
		textView.setText(title);
		Button fuhan = (Button) convertView.findViewById(R.id.fuhan);
		Button xxfg = (Button) convertView.findViewById(R.id.xxfg);
		// 查询出复函数量
		ArrayList<HashMap<String, Object>> listFH = SqliteUtil.getInstance()
				.queryBySqlReturnArrayListHashMap(
						"select count(*) from t_attachment where FK_Id = '"
								+ id + "' and remark = '复函'");

		// wsc 增加显示文件夹下文件的复函数量
		if (listFH != null) {
			String sfh = "复函" + "(" + listFH.get(0).get("count(*)") + ")";
			fuhan.setText(sfh);
		}
		// 查询出先行法律数量
		ArrayList<HashMap<String, Object>> listXXFL = SqliteUtil.getInstance()
				.queryBySqlReturnArrayListHashMap(
						"select count(*) from t_attachment where FK_Id = '"
								+ id + "' and remark = '现行法律'");
		if (listXXFL != null) {
			String sfhxx = "现行法律" + "(" + listXXFL.get(0).get("count(*)") + ")";
			xxfg.setText(sfhxx);
		}
		xxfg.setTag(id + "|" + title);
		fuhan.setTag(id + "|" + title);
		xxfg.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClassName("com.mapuni.android.MobileEnforcement",
						"com.mapuni.android.infoQuery.LNFLFGShow");
				String str = v.getTag().toString();
				intent.putExtra("pid", str.substring(0, str.indexOf("|")));
				intent.putExtra("title", str.substring(str.indexOf("|") + 1));
				intent.putExtra("remark", "现行法律");
				parentContext.startActivity(intent);
			}
		});
		fuhan.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClassName("com.mapuni.android.MobileEnforcement",
						"com.mapuni.android.infoQuery.LNFLFGShow");
				String str = v.getTag().toString();
				intent.putExtra("pid", str.substring(0, str.indexOf("|")));
				intent.putExtra("title", str.substring(str.indexOf("|") + 1));
				intent.putExtra("remark", "复函");
				parentContext.startActivity(intent);
			}
		});
		textView.setText(title);
		return convertView;
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		// TODO Auto-generated method stub
		return treeNodes.get(groupPosition).childs.size();
	}

	@Override
	public Object getGroup(int groupPosition) {
		// TODO Auto-generated method stub
		return treeNodes.get(groupPosition);
	}

	@Override
	public int getGroupCount() {
		// TODO Auto-generated method stub
		return treeNodes.size();
	}

	@Override
	public long getGroupId(int groupPosition) {
		// TODO Auto-generated method stub
		return groupPosition;
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		convertView = LayoutInflater.from(parentContext).inflate(
				R.layout.threeflfgtreeview, null);
		TextView textView = (TextView) convertView
				.findViewById(R.id.threeflfgtitle);
		ImageView imageView = (ImageView) convertView.findViewById(R.id.threeflfgimage);
		if(expandableListView!=null){
			if(expandableListView.isGroupExpanded(groupPosition))
				imageView.setImageResource(R.drawable.specialitem_down);
			else
				imageView.setImageResource(R.drawable.specialitem_right);
		}
		// 查询出包含子项数目
		ArrayList<HashMap<String, Object>> list = SqliteUtil.getInstance()
				.queryBySqlReturnArrayListHashMap(
						"select count(*) from T_HandBookCatalog where pid = '"
								+ ((TreeNode) getGroup(groupPosition)).Id
										.toString() + "'");
		String numStr = "0";
		if (list != null) {
			numStr = list.get(0).get("count(*)").toString();
		}
		textView.setText(((TreeNode) getGroup(groupPosition)).Title.toString()
				+ "(" + numStr + ")");
		return convertView;
	}

	@Override
	public boolean hasStableIds() {
		return true;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return true;
	}
	ReceiversExpandableListView expandableListView;
	public void setListView(ReceiversExpandableListView treeView) {
		expandableListView = treeView;
	}

}
