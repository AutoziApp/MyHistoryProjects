package com.mapuni.mobileenvironment.view;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import com.mapuni.mobileenvironment.R;
import com.mapuni.mobileenvironment.adapter.TreeAdapter;
import com.mapuni.mobileenvironment.model.Node;
import com.mapuni.mobileenvironment.utils.SqliteUtil;
import java.util.ArrayList;
import java.util.HashMap;

public class AllGridCityDialog extends Activity implements OnItemClickListener {

	ListView code_list;
	AllGridCityDialog oThis = this;
	TreeAdapter ta;
	Node root;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.tree_grid_city_main);

//		toolBar = (LinearLayout) findViewById(R.id.toolBar);
		code_list = (ListView) findViewById(R.id.code_list);
		code_list.setOnItemClickListener(this);
		root =(Node)getIntent().getSerializableExtra("");
//		setToolBar(new String[] { "确定", "", "", "退出" }, new int[] { 0, 3 });
		setPreson();
	}

	/**
	 * 递归遍历节点
	 *
	 * @return
	 */
//	Node chidNode, secNode, thrNode, forNode;
//	Node root;
//	Node parentNode;
	boolean rootNodeFlag = true;

	// String sql="select t.code,t.name from Industry t  where t.pid='" +
	// serchCondition + "'";
	// ArrayList<HashMap<String, Object>>
	// arrayListNode=SqliteUtil.getInstance().queryBySqlReturnArrayListHashMap(sql);
	private void setPreson() {


		// root.setIcon(R.drawable.icon_department);//设置图标
//		parentNode = root;
//		root = parentNode;
////		new SearchData().execute();
//		getAllPeople();

		ta = new TreeAdapter(oThis, root);
		// 设置整个树是否显示复选框
		ta.setCheckBox(false);
		// 设置展开和折叠时图标
		ta.setExpandedCollapsedIcon( R.mipmap.tree_ex,R.mipmap.tree_ec);
		// 设置默认展开级别
		ta.setExpandLevel(1);
		code_list.setAdapter(ta);

	}

//	public void getAllPeople() {
//		// 查找出来所有的支队和总队
//		String serchCondition="130300000000";
//		String sqlDepart = "select t.grid_areacode,t.grid_areaname from T_Base_GridInfo t  where t.grid_parentgid='" + serchCondition + "'";
//		ArrayList<HashMap<String, Object>> arrayListNodeDepart = SqliteUtil
//				.getInstance().queryBySqlReturnArrayListHashMap(sqlDepart);
//
//		if (arrayListNodeDepart.size() > 0) {
//			for (HashMap<String, Object> hashMapNodDepart : arrayListNodeDepart) {
//				chidNode = new Node(hashMapNodDepart.get("grid_areaname").toString(),
//						hashMapNodDepart.get("grid_areacode").toString());
//				chidNode.setExpanded(false);
//				chidNode.setParent(root);
//				chidNode.setIcon(R.mipmap.chazhao);// 设置图标
//				root.add(chidNode);
//
//				// 查找支队或者总队所属科室
//				String sqlChildDepart = "select grid_areacode,grid_areaname from T_Base_GridInfo where grid_parentgid ='"
//						+ hashMapNodDepart.get("grid_areacode").toString() + "'";
//				ArrayList<HashMap<String, Object>> arrayListNodeChildDepat = SqliteUtil
//						.getInstance().queryBySqlReturnArrayListHashMap(
//								sqlChildDepart);
//				for (HashMap<String, Object> hashMapNodChildDepart : arrayListNodeChildDepat) {
//					secNode = new Node(hashMapNodChildDepart.get("grid_areaname")
//							.toString(), hashMapNodChildDepart.get("grid_areacode")
//							.toString());
//					secNode.setExpanded(false);
//					secNode.setParent(chidNode);
//					secNode.setIcon(R.mipmap.chazhao);// 设置图标
//					chidNode.add(secNode);
//					// 查找科室所含人员
//					String sqlChildPeople = "select grid_areaname,grid_areacode from T_Base_GridInfo where grid_parentgid ='"
//							+ hashMapNodChildDepart.get("grid_areacode").toString()
//							+ "'";
//					// String
//					// sqlChildPeople="select t.UserID,t.U_RealName from PC_Users t  where t.depid='"+
//					// hashMapNodChildDepart.get("depid").toString() +"'";
//					ArrayList<HashMap<String, Object>> arrayListNodeChildPeople = SqliteUtil
//							.getInstance().queryBySqlReturnArrayListHashMap(sqlChildPeople);
//					for (HashMap<String, Object> hashMapNodChildPeople : arrayListNodeChildPeople) {
//
//						// 查找科室所含人员
//						String sqlChild2People = "select grid_areaname,grid_areacode from T_Base_GridInfo where grid_parentgid ='"
//								+ hashMapNodChildPeople.get("grid_areacode").toString()
//								+ "'";
//						// String
//						// sqlChildPeople="select t.UserID,t.U_RealName from PC_Users t  where t.depid='"+
//						// hashMapNodChildDepart.get("depid").toString() +"'";
//						ArrayList<HashMap<String, Object>> arrayListNodeChildPeople1 = SqliteUtil.getInstance().queryBySqlReturnArrayListHashMap(sqlChild2People);
//						if (arrayListNodeChildPeople1.size() > 0) {
//							thrNode = new Node(hashMapNodChildPeople
//									.get("grid_areaname").toString(),
//									hashMapNodChildPeople.get("grid_areacode")
//											.toString());
//							thrNode.setExpanded(false);
//							thrNode.setIcon(R.mipmap.chazhao);// 设置图标
//
//						} else {
//							thrNode = new Node(hashMapNodChildPeople
//									.get("grid_areaname").toString(),
//									hashMapNodChildPeople.get("grid_areacode")
//											.toString());
//							thrNode.setExpanded(false);
//							thrNode.setIcon(R.mipmap.ic_launcher);// 设置图标
//
//						}
//						thrNode.setParent(secNode);
//						secNode.add(thrNode);
//
//						for (HashMap<String, Object> hashMapNodChildPeople1 : arrayListNodeChildPeople1) {
//							forNode = new Node(hashMapNodChildPeople1.get(
//									"grid_areaname").toString(), hashMapNodChildPeople1
//									.get("grid_areacode").toString());
//							forNode.setExpanded(false);
//							forNode.setParent(thrNode);
//							thrNode.add(forNode);
//							forNode.setIcon(R.mipmap.ic_launcher);// 设置图标
//						}
//					}
//				}
//			}
//		}
//	}
//

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
							long id) {
		// TODO Auto-generated method stub
		TreeAdapter adapter = (TreeAdapter) parent.getAdapter();
		Node node = (Node)adapter.getItem(position);
		if(node.isLeaf()) {
			Intent intent = getIntent();
			Bundle bundle = new Bundle();
			bundle.putString("hylbname", node.getText());
			bundle.putString("hylbcode", node.getValue());
			intent.putExtras(bundle);
			AllGridCityDialog.this.setResult(0, intent);
			finish();
		} else {
			// 这句话写在最后面
			adapter.ExpandOrCollapse(position);
		}
	}

//	class SearchData extends AsyncTask<String,String,String>{
//
//		@Override
//		protected String doInBackground(String... params) {
//			getAllPeople();
//			return null;
//		}
//
//		@Override
//		protected void onPostExecute(String s) {
//
//			ta.notifyDataSetChanged();
////			TreeAdapter ta = new TreeAdapter(oThis, root);
////			// 设置整个树是否显示复选框
////			ta.setCheckBox(false);
////			// 设置展开和折叠时图标
////			ta.setExpandedCollapsedIcon(R.mipmap.chazhao, R.mipmap.chazhao);
////			// 设置默认展开级别
////			ta.setExpandLevel(1);
////			code_list.setAdapter(ta);
//			super.onPostExecute(s);
//		}
//	}

	// 设置底部工具栏
//	private void setToolBar(String[] name_array, int[] pos_array) {
//		toolBar.removeAllViews();
//
//		GridView toolbarGrid = new GridView(this);
//		toolbarGrid.setNumColumns(4);// 设置每行列数
//		toolbarGrid.setGravity(Gravity.CENTER);// 位置居中
//		ToolbarAdapter adapter = new ToolbarAdapter(this, name_array, null,
//				pos_array);
//		toolbarGrid.setAdapter(adapter);
//		toolbarGrid.setOnItemClickListener(new OnItemClickListener() {
//			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
//					long arg3) {
//				switch (arg2) {
//				case 0:// 显示选中结果
//					List<Node> nodes = ((TreeAdapter) code_list.getAdapter())
//							.getSeletedNodes();
//					String msg = "";
//					String valueMsg = "";
//
//					for (int i = 0; i < nodes.size(); i++) {
//						Node n = nodes.get(i);
//						if (n.isLeaf()) {
//							if (!msg.equals("")) {
//								msg += ",";
//								valueMsg += ",";
//							}
//							msg += n.getText();
//							valueMsg += n.getValue();
//						}
//
//					}
//					if (msg.equals("")) {
//						Toast.makeText(oThis, "没有选中任何项", Toast.LENGTH_SHORT)
//								.show();
//					} else {
//						Toast.makeText(oThis, msg, Toast.LENGTH_SHORT).show();
//					}
//					Intent intent = getIntent();
//					Bundle bundle = new Bundle();
//					bundle.putString("hylb", msg);
//					intent.putExtras(bundle);
//					AllGridCityDialog.this.setResult(0, intent);
//					finish();
//					break;
//				case 3:// 返回
//					oThis.finish();
//					// System.exit(0);
//					break;
//				}
//			}
//		});
//		toolBar.addView(toolbarGrid);
//	}
}