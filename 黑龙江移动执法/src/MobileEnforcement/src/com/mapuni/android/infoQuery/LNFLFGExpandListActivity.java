/**
 * 
 */
package com.mapuni.android.infoQuery;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ExpandableListView.OnGroupExpandListener;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mapuni.android.MobileEnforcement.R;
import com.mapuni.android.base.BaseActivity;
import com.mapuni.android.base.util.DisplayUitl;
import com.mapuni.android.dataprovider.SqliteUtil;

/**
 * 
 * Description: 辽宁法律法规类
 * 
 * @author 钟学梅
 * @Version 1.4.7
 * @Copyright 中科宇图天下科技有限公司 Create at: 2013-08-29
 */
public class LNFLFGExpandListActivity extends BaseActivity {

	private ExpandableListView expandListLiew;
	private RelativeLayout two_list_tool_layout;// 标题布局
	private SuperTreeViewAdapter superAdapter;
	private List<SuperTreeViewAdapter.SuperTreeNode> superNodeTree;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ln_two_list_tool);
		two_list_tool_layout = (RelativeLayout)findViewById(R.id.ln_two_list_tool_layout);
		SetBaseStyle(two_list_tool_layout, "环保手册");
		super.setSearchButtonVisiable(true);
		queryImg.setVisibility(View.GONE);
		
		expandListLiew = (ExpandableListView) this
				.findViewById(R.id.expandList);

		expandListLiew.setGroupIndicator(getResources().getDrawable(
				R.layout.expandablelistviewselector));
		superAdapter = new SuperTreeViewAdapter(this, stvClickEvent);
		superAdapter.RemoveAll();
		superAdapter.notifyDataSetChanged();
		initData();
		superAdapter.UpdateTreeNode(superNodeTree);
		expandListLiew.setAdapter(superAdapter);
		expandListLiew.expandGroup(0);
		
		expandListLiew.setOnChildClickListener(stvClickEvent);
		
		expandListLiew.setOnGroupExpandListener(new OnGroupExpandListener(){
            @Override
            public void onGroupExpand(int groupPosition){

                    for(int i = 0; i < superAdapter.getGroupCount(); i++){
                            if(i != groupPosition && expandListLiew.isGroupExpanded(i)){
                            	expandListLiew.collapseGroup(i);
                            }
                    }
            }
    });
	}
	
	/**
	 * 递归遍历文件夹
	 * 
	 * @param filepath
	 * @return
	 */
	int i = 0;
	int j = 0;

	public void initData() {
		ArrayList<HashMap<String, Object>> map;
		try {
			superNodeTree = superAdapter.GetTreeNode();
			map = SqliteUtil.getInstance().queryBySqlReturnArrayListHashMap(
					"select * from T_HandBookCatalog where Pid ='0';");
			for (HashMap<String, Object> hashMap : map) {
				SuperTreeViewAdapter.SuperTreeNode superNode = new SuperTreeViewAdapter.SuperTreeNode();
				superNode.Title = hashMap.get("name");
				superNode.Id = hashMap.get("id");
				
				ArrayList<HashMap<String, Object>> map2 = SqliteUtil.getInstance().queryBySqlReturnArrayListHashMap(
						"select * from T_HandBookCatalog where Pid = '"
									+ superNode.Id.toString() + "';");
				for(HashMap<String, Object> hashMap2 : map2){
					TreeViewAdapter.TreeNode node = new TreeViewAdapter.TreeNode();
					node.Title = hashMap2.get("name");
					node.Id = hashMap2.get("id");
					
					ArrayList<HashMap<String, Object>> map3 = SqliteUtil.getInstance().queryBySqlReturnArrayListHashMap(
							"select * from T_HandBookCatalog where Pid = '"
										+ node.Id.toString() + "';");
					for(HashMap<String, Object> hashMap3 : map3){
						node.childs.add(hashMap3.get("id").toString()+"|"+hashMap3.get("name").toString());
					}
					superNode.childs.add(node);
				}
				superNodeTree.add(superNode);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	OnChildClickListener stvClickEvent = new OnChildClickListener() {

		@Override
		public boolean onChildClick(ExpandableListView parent, View v,
				int groupPosition, int childPosition, long id) {
			TextView textView = (TextView) v.findViewById(R.id.flfgtitle);
			Intent intent = new Intent(LNFLFGExpandListActivity.this, LNFLFGShow.class);
			intent.putExtra("pid",
					textView.getTag().toString());
			intent.putExtra("title",
					textView.getText().toString());
			startActivity(intent);
			return false;
		}
	};
}
