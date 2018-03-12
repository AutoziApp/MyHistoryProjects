package com.mapuni.android.base.adapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

/**
 * FileName: TreeViewAdapter.java
 * Description: 两级树的适配器
 * @author 王红娟
 * @Version 1.3.4
 * @Copyright 中科宇图天下科技有限公司
 * Create at: 2012-12-3 下午05:57:50
 */
public class TreeViewAdapter extends BaseExpandableListAdapter {
	
	/**每一个节点的高度*/
	public static final int ItemHeight = 65;
	/**每一项距左边的距离*/
	public static final int PaddingLeft = 36;
	/**初始化据左边的的距离*/
	private int myPaddingLeft = 0;

	/**
	 * FileName: TreeViewAdapter.java
	 * Description: 内部类
	 * @author 王红娟
	 * @Version 1.3.4
	 * @Copyright 中科宇图天下科技有限公司
	 * Create at: 2012-12-3 下午06:00:44
	 */
	static public class TreeNode {
		public Object parent;
		public List<HashMap<String, Object>> childs = new ArrayList<HashMap<String, Object>>();
	}

	List<TreeNode> treeNodes = new ArrayList<TreeNode>();
	Context parentContext;

	public TreeViewAdapter(Context view, int myPaddingLeft) {
		parentContext = view;
		this.myPaddingLeft = myPaddingLeft;
	}

	@Override
	public int getGroupCount() {
		return treeNodes.size();

	}

	@Override
	public int getChildrenCount(int groupPosition) {
		if(treeNodes==null || treeNodes.size()==0){
			return 0;
		}
		return treeNodes.get(groupPosition).childs.size();

	}

	@Override
	public Object getGroup(int groupPosition) {
		return treeNodes.get(groupPosition).parent;
	}

	@Override
	public Object getChild(int groupPosition, int childPosition) {
		return treeNodes.get(groupPosition).childs.size();

	}

	@Override
	public long getGroupId(int groupPosition) {
		return groupPosition;
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		return childPosition;
	}

	@Override
	public boolean hasStableIds() {
		return true;
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {
		TextView textView = getTextView(this.parentContext);
		textView.setText(getGroup(groupPosition).toString());
		textView.setPadding(myPaddingLeft + PaddingLeft, 0, 0, 0);
		textView.setTextColor(Color.BLACK);
		return textView;
	}

	@Override
	public View getChildView(int groupPosition, int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {
		TextView textView = getTextView(this.parentContext);
		textView.setText(treeNodes.get(groupPosition).childs.get(childPosition)
				.get("title").toString());
		textView.setTag(treeNodes.get(groupPosition).childs.get(childPosition)
				.get("id").toString());
		textView.setPadding(myPaddingLeft + PaddingLeft, 0, 0, 0);
		textView.setTextColor(Color.BLACK);
		return textView;
	}


	@Override
	public boolean isChildSelectable(int i, int j) {

		return true;
	}

	static public TextView getTextView(Context context) {
		AbsListView.LayoutParams lp = new AbsListView.LayoutParams(
				ViewGroup.LayoutParams.FILL_PARENT, ItemHeight);

		TextView textView = new TextView(context);
		textView.setLayoutParams(lp);
		textView.setPadding(10, 0, 0, 0);
		textView.setGravity(Gravity.CENTER_VERTICAL);

		return textView;
	}

	public List<TreeNode> GetTreeNode() {
		return treeNodes;
	}

	public void UpdateTreeNode(List<TreeNode> nodes) {
		treeNodes = nodes;
	}

}
