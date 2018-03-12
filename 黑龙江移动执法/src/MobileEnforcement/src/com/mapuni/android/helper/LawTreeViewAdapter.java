package com.mapuni.android.helper;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.mapuni.android.base.R;
import com.mapuni.android.helper.LawRegulationDetail;

/**
 * FileName: TreeViewAdapter.java Description: 两级树的适配器
 * 
 * @author 王红娟
 * @Version 1.3.4
 * @Copyright 中科宇图天下科技有限公司 Create at: 2012-12-3 下午05:57:50
 */
public class LawTreeViewAdapter extends BaseExpandableListAdapter {

	/** 每一个节点的高度 */
	public static final int ItemHeight = 75;
	/** 每一项距左边的距离 */
	public static final int PaddingLeft = 36;
	/** 初始化据左边的的距离 */
	private int myPaddingLeft = 0;
	//private LawRegulationDetail LawRegulationDetail=null;
	/**
	 * FileName: TreeViewAdapter.java Description: 内部类
	 * 
	 * @author 王红娟
	 * @Version 1.3.4
	 * @Copyright 中科宇图天下科技有限公司 Create at: 2012-12-3 下午06:00:44
	 */
	static public class TreeNode {
		public Object parent;
		public List<HashMap<String, Object>> childs = new ArrayList<HashMap<String, Object>>();
	}

	List<TreeNode> treeNodes = new ArrayList<TreeNode>();
	Context parentContext;

	public LawTreeViewAdapter(Context view, int myPaddingLeft) {
		parentContext = view;
		this.myPaddingLeft = myPaddingLeft;
	}

	@Override
	public int getGroupCount() {
		return treeNodes.size();

	}

	@Override
	public int getChildrenCount(int groupPosition) {
		if (treeNodes == null || treeNodes.size() == 0) {
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
		HashMap<String, String> map = (HashMap<String, String>) getGroup(groupPosition);
		TextView textView = getGroupTextView(this.parentContext);
		textView.setText(map.get("title"));
		textView.setTag(map.get("id"));
		textView.setGravity(Gravity.CENTER_VERTICAL);
		textView.setPadding(myPaddingLeft + PaddingLeft, 0, 0, 0);
		textView.setTextColor(Color.BLACK);
		return textView;
	}

	@Override
	public View getChildView(int groupPosition, int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {
		// View treeView = getTextView(this.parentContext);
		View treeView = LayoutInflater.from(this.parentContext).inflate(
				R.layout.flfgtreeview, null);

		TextView textView = (TextView) treeView.findViewById(R.id.flfgtitle);
		ImageView image = (ImageView) treeView.findViewById(R.id.flfglefticon);
		final Button fuhan = (Button) treeView.findViewById(R.id.fuhan);
		final Button xxfg = (Button) treeView.findViewById(R.id.xxfg);
		String filepath = treeNodes.get(groupPosition).childs
				.get(childPosition).get("id").toString();
		String name = treeNodes.get(groupPosition).childs.get(childPosition)
				.get("title").toString();
		textView.setText(name);
		textView.setGravity(Gravity.CENTER_VERTICAL);
		textView.setTextSize(16);
		fuhan.setText("复      函");
		xxfg.setText("现行法律");
		xxfg.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				String xxfgfilepath = xxfg.getTag().toString();
				LawRegulationDetail LawRegulationDetail = new LawRegulationDetail(parentContext);
				LawRegulationDetail.getWindow().setType(
						WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
				LawRegulationDetail.showView(xxfgfilepath, "");
				/*Intent intent = new Intent();
				intent.setClassName("com.mapuni.android.MobileEnforcement",
						"com.mapuni.android.uitl.LNFLFGShow");
				intent.putExtra("path", xxfgfilepath);
				parentContext.startActivity(intent);*/
				
			}
		});
		textView.setTag(filepath);
		Drawable d = this.parentContext.getResources().getDrawable(
				R.drawable.specialitem_right);
		image.setBackgroundDrawable(d);
		fuhan.setTag(filepath + "/复函");
		xxfg.setTag(filepath + "/现行法律");
		fuhan.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				String fuhanfilepath = fuhan.getTag().toString();
				/*Intent intent = new Intent();
				intent.setClassName("com.mapuni.android.MobileEnforcement",
						"com.mapuni.android.uitl.LNFLFGShow");
				intent.putExtra("path", fuhanfilepath);
				intent.putExtra("filename", "");
				parentContext.startActivity(intent);*/
				LawRegulationDetail LawRegulationDetail = new LawRegulationDetail(parentContext);
				LawRegulationDetail.getWindow().setType(
						WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
				LawRegulationDetail.showView(fuhanfilepath, "");			
					
				
				
			}
		});
		textView.setTextColor(Color.BLACK);
		fuhan.setTextColor(Color.BLACK);
		xxfg.setTextColor(Color.BLACK);

		textView.setHeight(ItemHeight);
		image.setPadding(0, 0, 0, 0);
		File file = new File(filepath);
		if (!ifHaveDirectory(file)) {
			fuhan.setVisibility(View.GONE);
			xxfg.setVisibility(View.GONE);
		}
		return treeView;
	}

	@Override
	public boolean isChildSelectable(int i, int j) {

		return true;
	}

	static public View getTextView(Context context) {
		View itemview = LayoutInflater.from(context).inflate(
				R.layout.flfgtreeview, null);
		// AbsListView.LayoutParams lp = new AbsListView.LayoutParams(
		// ViewGroup.LayoutParams.FILL_PARENT, ItemHeight);
		// itemview.setLayoutParams(lp);
		// textView.setPadding(10, 0, 0, 0);
		// textView.setGravity(Gravity.CENTER_VERTICAL);

		return itemview;
	}

	static public TextView getGroupTextView(Context context) {
		AbsListView.LayoutParams lp = new AbsListView.LayoutParams(
				ViewGroup.LayoutParams.FILL_PARENT, ItemHeight);

		TextView textView = new TextView(context);
		textView.setTextSize(22);
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

	public boolean ifHaveDirectory(File file) {
		File[] files = file.listFiles();
		for (File f : files) {
			if (f.isDirectory()) {
				return true;
			}

		}
		return false;
	}

}
