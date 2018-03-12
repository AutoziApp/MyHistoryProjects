package com.zhy.tree_view;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Window;
import android.widget.ListView;
import android.widget.TextView;

import com.mapuni.android.MobileEnforcement.R;
import com.mapuni.android.base.controls.loading.YutuLoading;
import com.mapuni.android.dataprovider.SqliteUtil;
import com.zhy.bean.FileBean;
import com.zhy.tree.bean.Node;
import com.zhy.tree.bean.TreeListViewAdapter;
import com.zhy.tree.bean.TreeListViewAdapter.OnTreeNodeClickListener;

public class AddressListActivity extends Activity {
	private List<FileBean> mDatas = new ArrayList<FileBean>();
	private ListView mTree;
	private TreeListViewAdapter mAdapter;
	ArrayList<HashMap<String, Object>> arr = new ArrayList<HashMap<String, Object>>();
	@SuppressWarnings("unchecked")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main_tree);
		mTree = (ListView) findViewById(R.id.id_tree);
		mTree.setCacheColorHint(0);
		//TODO 改为协办人多选 
//		select_fshr = (TextView)findViewById(R.id.select_fshr);
		//获取数据 ArrayList<HashMap<String, Object>> arr
		arr = (ArrayList<HashMap<String, Object>>) getIntent().getSerializableExtra("arr");
//			boolean extra = getIntent().getBooleanExtra("isFshr", false);
//			if (extra) {
//				select_fshr.setText("请选择协办人员");
//			}
		
		// 成功后加载数据
		initDatas();
		try {
			mAdapter = new SimpleTreeAdapter<FileBean>(mTree,
					AddressListActivity.this, mDatas, 2);
			mAdapter.setOnTreeNodeClickListener(new OnTreeNodeClickListener() {
				@Override
				public void onClick(Node node, int position) {
					if (node.isLeaf()) {
						onclick_userInfo(node);
					}
				}
			});

		} catch (Exception e) {
			e.printStackTrace();
		}
		mTree.setAdapter(mAdapter);
	}

	int tree_id = 1;

	private void initDatas() {
		ArrayList<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();
		//获取1级目录
		for(int i=0;i<arr.size();i++){
			if(String.valueOf(arr.get(i).get("ParentId")).equals("p0")){
				list.add(arr.get(i));
			}
		}
		for (int i = 0; i < list.size(); i++) {
			// ------------第一级都为0 服务端数据库递归不规范，此处处理
			mDatas.add(new FileBean(tree_id++, 0, String.valueOf(list.get(i)
					.get("Text"))));
			getDep(String.valueOf(list.get(i).get("Id")), tree_id - 1);
		}
	}

	private ArrayList<HashMap<String, Object>> getDep(String id, int tag) {
//		String sql = "select * from PC_DepartmentInfo where DepParentID = '"
//				+ depid + "' order by OrderId";
		ArrayList<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();
		//获取1级目录
		for(int i=0;i<arr.size();i++){
			if(String.valueOf(arr.get(i).get("ParentId")).equals(id)){
				list.add(arr.get(i));
			}
		}
		for (int i = 0; i < list.size(); i++) {
			mDatas.add(new FileBean(tree_id++, tag, String.valueOf(list.get(i)
					.get("Text"))));
//			getUsers(String.valueOf(list.get(i).get("Text")),
//					String.valueOf(list.get(i).get("syncdataregioncode")),
//					tree_id - 1, id);
			getDep(String.valueOf(list.get(i).get("Id")), tree_id - 1);
		}
		return list;
	}
    
//	/**
//	 * 重写此方法满足新的需求</br>需求要求:队领导下显示所有支队领导，支队下非队领导部门不显示领导，非支队部门人员正常显示。
//	 * @param depName
//	 * @param syncReginCode
//	 * @param tag
//	 * @param depparentid
//	 */
//	private void getUsers(String depName, String syncReginCode, int tag,
//			String depparentid) {
//		String sql = "select userid,u_realname,depid,depname, u_photo,u_hometel,zwmc from V_Users where U_Status !='-1' and  depname= '"
//				+ depName
//				+ "'and SyncDataRegionCode='"
//				+ syncReginCode
//				+ "'order by OrderID";
//		// 1/队领导2.队领导下面显示人员：队长，副队长，监察支队各科科长
//		if ("140900000000departA30B".equals(depparentid)) {// 如果部门等于监测支队
//			if ("队领导".equals(depName)) {// 队领导下显示支队的所有领导
//				sql = "select userid,u_realname,depid,depname, u_photo,u_hometel,zwmc from V_Users where U_Status !='-1' and  zw !='0' and zw!='4' and DepParentID ='140900000000departA30B' and SyncDataRegionCode='"
//						+ syncReginCode + "'order by OrderID";
//			} else {// 支队下科室不显示领导
//				sql = "select userid,u_realname,depid,depname, u_photo,u_hometel,zwmc from V_Users where U_Status !='-1' and  depname= '"
//						+ depName
//						+ "' and (zw='4' or zw='0') and DepParentID ='140900000000departA30B'  and SyncDataRegionCode='"
//						+ syncReginCode + "'order by OrderID";
//			}
//		} else {// 非监测支队正常显示
//			sql = "select userid,u_realname,depid,depname, u_photo,u_hometel,zwmc from V_Users where U_Status !='-1' and  depname= '"
//					+ depName
//					+ "'and SyncDataRegionCode='"
//					+ syncReginCode
//					+ "'order by OrderID";
//		}
//
//		ArrayList<HashMap<String, Object>> list = SqliteUtil.getInstance()
//				.queryBySqlReturnArrayListHashMap(sql);
//		for (int i = 0; i < list.size(); i++) {
//			// 加空格避免空指针
//			String name = String.valueOf(list.get(i).get("u_realname")) + " , "
//					+ String.valueOf(list.get(i).get("u_photo")) + ","
//					+ String.valueOf(list.get(i).get("zwmc") + " ");
//			mDatas.add(new FileBean(tree_id++, tag, name));
//		}
//	}

//	private void getUsers(String depName, String syncReginCode, int tag) {
//		// 队领导（后台完成）2.队领导下面显示人员：队长，副队长，监察支队各科科长
//		String sql = "select userid,u_realname,depid,depname, u_photo,u_hometel,zwmc from V_Users where U_Status !='-1' and  depname= '"
//				+ depName
//				+ "'and SyncDataRegionCode='"
//				+ syncReginCode
//				+ "'order by OrderID";
//		ArrayList<HashMap<String, Object>> list = SqliteUtil.getInstance()
//				.queryBySqlReturnArrayListHashMap(sql);
//		for (int i = 0; i < list.size(); i++) {
//			// 加空格避免空指针
//			String name = String.valueOf(list.get(i).get("u_realname")) + " , "
//					+ String.valueOf(list.get(i).get("u_photo")) + ","
//					+ String.valueOf(list.get(i).get("zwmc") + " ");
//			mDatas.add(new FileBean(tree_id++, tag, name));
//		}
//	}

	private void onclick_userInfo(Node node) {
		try {
			String[] names = node.getName().split(",");
			String code = "";
			String name = "";
			if(names.length>0)
				name = names[0];
			//根据name找code
			for(int i=0;i<arr.size();i++){
				if(String.valueOf(arr.get(i).get("Text")).equals(name)){
					code = String.valueOf(arr.get(i).get("Id"));
					break;
				}
			}
			   //判断空，我就不判断了。。。。  
            Intent data=new Intent();  
            data.putExtra("name", name);  
            data.putExtra("code", code);  
            //请求代码可以自己设置，这里设置成20  
            setResult(20, data);  
            //关闭掉这个Activity  
            finish(); 
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	YutuLoading yutuLoading;
	private TextView select_fshr;

}
