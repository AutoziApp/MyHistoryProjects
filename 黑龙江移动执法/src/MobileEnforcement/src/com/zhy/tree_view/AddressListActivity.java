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
		//TODO ��ΪЭ���˶�ѡ 
//		select_fshr = (TextView)findViewById(R.id.select_fshr);
		//��ȡ���� ArrayList<HashMap<String, Object>> arr
		arr = (ArrayList<HashMap<String, Object>>) getIntent().getSerializableExtra("arr");
//			boolean extra = getIntent().getBooleanExtra("isFshr", false);
//			if (extra) {
//				select_fshr.setText("��ѡ��Э����Ա");
//			}
		
		// �ɹ����������
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
		//��ȡ1��Ŀ¼
		for(int i=0;i<arr.size();i++){
			if(String.valueOf(arr.get(i).get("ParentId")).equals("p0")){
				list.add(arr.get(i));
			}
		}
		for (int i = 0; i < list.size(); i++) {
			// ------------��һ����Ϊ0 ��������ݿ�ݹ鲻�淶���˴�����
			mDatas.add(new FileBean(tree_id++, 0, String.valueOf(list.get(i)
					.get("Text"))));
			getDep(String.valueOf(list.get(i).get("Id")), tree_id - 1);
		}
	}

	private ArrayList<HashMap<String, Object>> getDep(String id, int tag) {
//		String sql = "select * from PC_DepartmentInfo where DepParentID = '"
//				+ depid + "' order by OrderId";
		ArrayList<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();
		//��ȡ1��Ŀ¼
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
//	 * ��д�˷��������µ�����</br>����Ҫ��:���쵼����ʾ����֧���쵼��֧���·Ƕ��쵼���Ų���ʾ�쵼����֧�Ӳ�����Ա������ʾ��
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
//		// 1/���쵼2.���쵼������ʾ��Ա���ӳ������ӳ������֧�Ӹ��ƿƳ�
//		if ("140900000000departA30B".equals(depparentid)) {// ������ŵ��ڼ��֧��
//			if ("���쵼".equals(depName)) {// ���쵼����ʾ֧�ӵ������쵼
//				sql = "select userid,u_realname,depid,depname, u_photo,u_hometel,zwmc from V_Users where U_Status !='-1' and  zw !='0' and zw!='4' and DepParentID ='140900000000departA30B' and SyncDataRegionCode='"
//						+ syncReginCode + "'order by OrderID";
//			} else {// ֧���¿��Ҳ���ʾ�쵼
//				sql = "select userid,u_realname,depid,depname, u_photo,u_hometel,zwmc from V_Users where U_Status !='-1' and  depname= '"
//						+ depName
//						+ "' and (zw='4' or zw='0') and DepParentID ='140900000000departA30B'  and SyncDataRegionCode='"
//						+ syncReginCode + "'order by OrderID";
//			}
//		} else {// �Ǽ��֧��������ʾ
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
//			// �ӿո�����ָ��
//			String name = String.valueOf(list.get(i).get("u_realname")) + " , "
//					+ String.valueOf(list.get(i).get("u_photo")) + ","
//					+ String.valueOf(list.get(i).get("zwmc") + " ");
//			mDatas.add(new FileBean(tree_id++, tag, name));
//		}
//	}

//	private void getUsers(String depName, String syncReginCode, int tag) {
//		// ���쵼����̨��ɣ�2.���쵼������ʾ��Ա���ӳ������ӳ������֧�Ӹ��ƿƳ�
//		String sql = "select userid,u_realname,depid,depname, u_photo,u_hometel,zwmc from V_Users where U_Status !='-1' and  depname= '"
//				+ depName
//				+ "'and SyncDataRegionCode='"
//				+ syncReginCode
//				+ "'order by OrderID";
//		ArrayList<HashMap<String, Object>> list = SqliteUtil.getInstance()
//				.queryBySqlReturnArrayListHashMap(sql);
//		for (int i = 0; i < list.size(); i++) {
//			// �ӿո�����ָ��
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
			//����name��code
			for(int i=0;i<arr.size();i++){
				if(String.valueOf(arr.get(i).get("Text")).equals(name)){
					code = String.valueOf(arr.get(i).get("Id"));
					break;
				}
			}
			   //�жϿգ��ҾͲ��ж��ˡ�������  
            Intent data=new Intent();  
            data.putExtra("name", name);  
            data.putExtra("code", code);  
            //�����������Լ����ã��������ó�20  
            setResult(20, data);  
            //�رյ����Activity  
            finish(); 
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	YutuLoading yutuLoading;
	private TextView select_fshr;

}
