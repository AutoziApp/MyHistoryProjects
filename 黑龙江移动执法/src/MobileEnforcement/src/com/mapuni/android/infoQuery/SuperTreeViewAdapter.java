package com.mapuni.android.infoQuery;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;

import com.mapuni.android.MobileEnforcement.R;
import com.mapuni.android.dataprovider.SqliteUtil;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AbsListView;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ExpandableListView.OnGroupClickListener;
import android.widget.ExpandableListView.OnGroupCollapseListener;
import android.widget.ExpandableListView.OnGroupExpandListener;
import android.widget.ImageView;
import android.widget.TextView;

public class SuperTreeViewAdapter extends BaseExpandableListAdapter {

	static public class SuperTreeNode {
		Object Title;
		Object Id;
		// �������β˵��Ľṹ��
		List<TreeViewAdapter.TreeNode> childs = new ArrayList<TreeViewAdapter.TreeNode>();
	}

	private List<SuperTreeNode> superTreeNodes = new ArrayList<SuperTreeNode>();
	private Context parentContext;
	private OnChildClickListener stvClickEvent;// �ⲿ�ص�����

	public SuperTreeViewAdapter(Context view, OnChildClickListener stvClickEvent) {
		parentContext = view;
		this.stvClickEvent = stvClickEvent;
	}

	public List<SuperTreeNode> GetTreeNode() {
		return superTreeNodes;
	}

	public void UpdateTreeNode(List<SuperTreeNode> node) {
		superTreeNodes = node;
	}

	public void RemoveAll() {
		superTreeNodes.clear();
	}

	public Object getChild(int groupPosition, int childPosition) {
		return superTreeNodes.get(groupPosition).childs.get(childPosition);
	}

	public int getChildrenCount(int groupPosition) {
		return superTreeNodes.get(groupPosition).childs.size()+1;
	}

	public ExpandableListView getExpandableListView() {
		//TreeViewAdapter.ItemHeight
		AbsListView.LayoutParams lp = new AbsListView.LayoutParams(
				ViewGroup.LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
		ReceiversExpandableListView superTreeView = new ReceiversExpandableListView(
				parentContext, null);
		superTreeView.setGroupIndicator(null);
		superTreeView.setLayoutParams(lp);
		superTreeView.setCacheColorHint(0x00000000);
		superTreeView.setDrawSelectorOnTop(false);
		return superTreeView;
	}

	/**
	 * �������ṹ�еĵڶ�����һ��ExpandableListView
	 */
	
	public View getChildView(int groupPosition, int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {
//		if (this.superTreeNodes != null && this.superTreeNodes.size() > 0) {
//			if (this.superTreeNodes.get(groupPosition).childs
//					.get(childPosition).childs == null
//					|| this.superTreeNodes.get(groupPosition).childs
//							.get(childPosition).childs.size() == 0) {
				convertView = LayoutInflater.from(parentContext).inflate(
						R.layout.flfgtreeview, null);
				
				if (childPosition==0 ) {
					
					String sqlChild="select * from t_handbookcatalog where pid = '0'";
					ArrayList<HashMap<String, Object>> list2 = SqliteUtil.getInstance().queryBySqlReturnArrayListHashMap(sqlChild);
					if (list2.size()!=0) {
						
						
						final TextView textView = (TextView) convertView
								.findViewById(R.id.flfgtitle);
						ImageView imageView = (ImageView) convertView.findViewById(R.id.flfglefticon);
						imageView.setImageResource(R.drawable.base_icon_bst);
						textView.setText(list2.get(groupPosition).get("name").toString());
						textView.setTag(list2.get(groupPosition).get("id").toString());
					}
					
				}else{
					
					String title = ((TreeViewAdapter.TreeNode) getChild(
							groupPosition, childPosition-1)).Title.toString();
					 String id = ((TreeViewAdapter.TreeNode) getChild(groupPosition,
							childPosition-1)).Id.toString();
					final TextView textView = (TextView) convertView
							.findViewById(R.id.flfgtitle);
					ImageView imageView = (ImageView) convertView.findViewById(R.id.flfglefticon);
					imageView.setImageResource(R.drawable.base_icon_bst);
					textView.setText(title);
					textView.setTag(id);	
				}
				
			
				
//				textView.setOnClickListener(new OnClickListener() {
//					
//					@Override
//					public void onClick(View arg0) {
//						Intent intent = new Intent();
//						intent.setClassName(
//								"com.mapuni.android.MobileEnforcement",
//								"com.mapuni.android.infoQuery.LNFLFGShow");
//						
//						intent.putExtra("pid",textView.getTag().toString());
//						intent.putExtra("title",textView.getText().toString());
//						intent.putExtra("remark", "NULL");
//						parentContext.startActivity(intent);
//					}
//				});
//				Button fuhan = (Button) convertView.findViewById(R.id.fuhan);
//				Button xxfg = (Button) convertView.findViewById(R.id.xxfg);
//				// ��ѯ����������
//				ArrayList<HashMap<String, Object>> listFH = SqliteUtil
//						.getInstance().queryBySqlReturnArrayListHashMap(
//								"select count(*) from T_Attachment where FK_Id = '"
//										+ id + "' and remark = '����'");

//				// wsc ������ʾ�ļ������ļ��ĸ�������
//				if (listFH != null) {
//					String sfh = "����" + "(" + listFH.get(0).get("count(*)")
//							+ ")";
//					fuhan.setText(sfh);
//				}
//				// ��ѯ�����з�������
//				ArrayList<HashMap<String, Object>> listXXFL = SqliteUtil
//						.getInstance().queryBySqlReturnArrayListHashMap(
//								"select count(*) from T_Attachment where FK_Id = '"
//										+ id + "' and remark = '���з���'");
//				
//			
//				if (listXXFL != null) {
//					String sfhxx = "���з���" + "("
//							+ listXXFL.get(0).get("count(*)") + ")";
//					xxfg.setText(sfhxx);
//				}
//				xxfg.setTag(id + "|" + title);
//				fuhan.setTag(id + "|" + title);
//				xxfg.setOnClickListener(new OnClickListener() {
//
//					@Override
//					public void onClick(View v) {
//						Intent intent = new Intent();
//						intent.setClassName(
//								"com.mapuni.android.MobileEnforcement",
//								"com.mapuni.android.infoQuery.LNFLFGShow");
//						String str = v.getTag().toString();
//						intent.putExtra("pid",
//								str.substring(0, str.indexOf("|")));
//						intent.putExtra("title",
//								str.substring(str.indexOf("|") + 1));
//						intent.putExtra("remark", "���з���");
//						parentContext.startActivity(intent);
//					}
//				});
//				fuhan.setOnClickListener(new View.OnClickListener() {
//
//					@Override
//					public void onClick(View v) {
//						Intent intent = new Intent();
//						intent.setClassName(
//								"com.mapuni.android.MobileEnforcement",
//								"com.mapuni.android.infoQuery.LNFLFGShow");
//						String str = v.getTag().toString();
//						intent.putExtra("pid",
//								str.substring(0, str.indexOf("|")));
//						intent.putExtra("title",
//								str.substring(str.indexOf("|") + 1));
//						intent.putExtra("remark", "����");
//						parentContext.startActivity(intent);
//					}
//				});
				
				return convertView;
//			}
//		}

		// ��
//		final ReceiversExpandableListView treeView = (ReceiversExpandableListView) getExpandableListView();
//		final TreeViewAdapter treeViewAdapter = new TreeViewAdapter(
//				this.parentContext, 0);
//		List<com.mapuni.android.infoQuery.TreeViewAdapter.TreeNode> tmp = treeViewAdapter
//				.getTreeNode();// ��ʱ����ȡ��TreeViewAdapter��TreeNode���ϣ���Ϊ��
//		final com.mapuni.android.infoQuery.TreeViewAdapter.TreeNode treeNode = (com.mapuni.android.infoQuery.TreeViewAdapter.TreeNode) getChild(
//				groupPosition, childPosition);
//		tmp.add(treeNode);
//		treeViewAdapter.updateTreeNode(tmp);
//		treeView.setAdapter(treeViewAdapter);
//
//		// �ؼ��㣺ȡ��ѡ�еĶ������β˵��ĸ��ӽڵ�,������ظ��ⲿ�ص�����
//		treeView.setOnChildClickListener(this.stvClickEvent);
//
//		/**
//		 * �ؼ��㣺�ڶ����˵�չ��ʱͨ��ȡ�ýڵ��������õ������˵��Ĵ�С
//		 */
//		treeView.setOnGroupExpandListener(new OnGroupExpandListener() {
//			@Override
//			public void onGroupExpand(int groupPosition) {
//
//				AbsListView.LayoutParams lp = new AbsListView.LayoutParams(
//						ViewGroup.LayoutParams.FILL_PARENT, (treeNode.childs
//								.size() + 1) * TreeViewAdapter.ItemHeight + 10);
//				treeView.setLayoutParams(lp);
//			}
//		});
//
//		/**
//		 * �ڶ����˵�����ʱ����Ϊ��׼Item��С
//		 */
//		treeView.setOnGroupCollapseListener(new OnGroupCollapseListener() {
//			@Override
//			public void onGroupCollapse(int groupPosition) {
//
//				AbsListView.LayoutParams lp = new AbsListView.LayoutParams(
//						ViewGroup.LayoutParams.FILL_PARENT,
//						TreeViewAdapter.ItemHeight);
//				treeView.setLayoutParams(lp);
//			}
//		});
//		treeView.setPadding(TreeViewAdapter.PaddingLeft * 2, 0, 0, 0);
//		treeViewAdapter.setListView(treeView);
//		return treeView;
	}

	/**
	 * �������ṹ�е��ײ���TextView,������Ϊtitle
	 */
	public View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {
		// ��ѯ������������Ŀ
		ArrayList<HashMap<String, Object>> list = SqliteUtil.getInstance()
				.queryBySqlReturnArrayListHashMap(
						"select count(*) from t_handbookcatalog where pid = '"
								+ ((SuperTreeNode) getGroup(groupPosition)).Id
										.toString() + "'");
		String numStr = "0";
		int parseInt=0;
		if (list != null) {
			numStr = list.get(0).get("count(*)").toString();
			 parseInt = Integer.parseInt(numStr)+1;
		}
		TextView textView = TreeViewAdapter.getTextView(this.parentContext);
		textView.setText(((SuperTreeNode) getGroup(groupPosition)).Title
				.toString() + "(" + parseInt + ")");
		textView.setGravity(Gravity.CENTER_VERTICAL);
		textView.setPadding(30 + TreeViewAdapter.PaddingLeft, 0, 0, 0);
		textView.setTextColor(Color.BLACK);
		return textView;
	}

	public long getChildId(int groupPosition, int childPosition) {
		return childPosition;
	}

	public Object getGroup(int groupPosition) {
		return superTreeNodes.get(groupPosition);
	}

	public int getGroupCount() {
		return superTreeNodes.size();
	}

	public long getGroupId(int groupPosition) {
		return groupPosition;
	}

	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return true;
	}

	public boolean hasStableIds() {
		return true;
	}

}
