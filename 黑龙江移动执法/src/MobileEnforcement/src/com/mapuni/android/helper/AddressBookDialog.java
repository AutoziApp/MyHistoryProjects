package com.mapuni.android.helper;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.mapuni.android.MobileEnforcement.R;
import com.mapuni.android.base.interfaces.IList;
import com.mapuni.android.base.util.LogUtil;
import com.mapuni.android.base.util.PinyinOperator;
import com.mapuni.android.dataprovider.SqliteUtil;

public class AddressBookDialog extends Dialog implements DialogInterface {

	private final String TAG = "AddressBookDialog";

	/** 获取该实体类列表样式的标题 */
	HashMap<String, Object> styleList = null;
	/** 初始化当前该实体类列表滚动的次数 */
	public int ListScrollTimes = 1;
	/** 查询该实体类的相关信息所用的表名 */
	private ArrayList<HashMap<String, Object>> dataList;
	private ListView addrBook_List;
	private View view;
	// 每一列数据详细信息
	private HashMap<String, Object> detailInfo = null;
	// 表名
	private String tablename = "";
	private int ItemCode;
	private MyListViewAdapter adapter;
	/** 当前业务类向上转型 */
	protected IList businessObj = null;

	public AddressBookDialog(Context context) {
		this(context, 0);
		// Dialog按对话框外面取消操作
		this.setCanceledOnTouchOutside(true);
	}

	public AddressBookDialog(Context context, int theme) {
		super(context, theme);

	}

	protected AddressBookDialog(Context context, boolean cancelable,
			OnCancelListener cancelListener) {
		super(context, cancelable, cancelListener);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	protected void onStart() {
		super.onStart();
	}

	@Override
	protected void onStop() {
		super.onStop();
	}

	public int GetListScrollTimes() {
		return ListScrollTimes;
	}

	public void setListScrollTimes(int listScrollTimes) {
		ListScrollTimes = listScrollTimes;
	}

	@Override
	public void show() {
		if (view == null) {
			/** 设置框体不显示标题栏 */
			super.requestWindowFeature(Window.FEATURE_NO_TITLE);
			LayoutInflater factory = LayoutInflater.from(this.getContext());
			view = factory.inflate(R.layout.addressbook, null);
			addrBook_List = (ListView) view
					.findViewById(R.id.addrbook_listView);
			dataList = getDataList();
			adapter = new MyListViewAdapter(this.getContext(), dataList);
			addrBook_List.setAdapter(adapter);
			/** 并且在这里实现列表项目的单击响应事件 */
			addrBook_List.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						final int position, long id) {
					String guid = dataList.get(position).get("userid")
							.toString();
					tablename = "V_Users";
					HashMap<String, String> primaryKey = new HashMap<String, String>();
					primaryKey.put("key", "userid");
					primaryKey.put("keyValue", guid);
					detailInfo = getDetailed(tablename, primaryKey);

					EnvManualController.getInstance().openOtherDialog(
							EnvManualController.DETAILINFO, detailInfo,
							EnvManualController.ADDRBOOKS);

				}
			});

			// super.setTitle("通讯录");
			super.setContentView(view);
		}
		super.show();

	}

	@Override
	public void hide() {
		super.hide();
	}

	@Override
	public void dismiss() {
		super.dismiss();
	}

	/**
	 * 适配器
	 */
	private class MyListViewAdapter extends BaseAdapter {
		ArrayList<HashMap<String, Object>> data;
		Context context;

		public MyListViewAdapter(Context context,
				ArrayList<HashMap<String, Object>> data) {
			this.data = data;
			this.context = context;
		}

		@Override
		public int getCount() {
			return data.size();
		}

		@Override
		public Object getItem(int position) {
			return null;
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder = null;
			if (convertView == null) {
				holder = new ViewHolder();
				/** 用来加载GridView的item布局 */
				convertView = LayoutInflater.from(context).inflate(
						R.layout.addressbook_list, null);
				/** 初始化item的组件 */
				holder.addr_name_tv = (TextView) convertView
						.findViewById(R.id.addrbook_name);
				holder.addr_dept_tv = (TextView) convertView
						.findViewById(R.id.addrbook_dept);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			/** 通过holder来获取数据 */
			holder.addr_name_tv.setText(data.get(position).get("u_realname")
					.toString());
			holder.addr_dept_tv.setText(data.get(position).get("depname")
					.toString());
			return convertView;
		}

		/** 定义一个ViewHolder，用来优化View */
		class ViewHolder {
			TextView addr_name_tv;
			TextView addr_dept_tv;
		}
	}

	// 根据SQL语句查询用户列表显示
	public ArrayList<HashMap<String, Object>> getDataList() {
		// return BaseClass.DBHelper.getList(tableName);
		SqliteUtil su = SqliteUtil.getInstance();
		String newSql = "select userid,u_realname,depname from V_Users";
		ArrayList<HashMap<String, Object>> data = su
				.queryBySqlReturnArrayListHashMap(newSql);
		data = orderListByFirstPin(data);
		return data;
	}

	/**
	 * Description:排序
	 */
	private ArrayList<HashMap<String, Object>> orderListByFirstPin(
			ArrayList<HashMap<String, Object>> data) {
		int num = data.size();
		HashMap<String, Object>[] datas = new HashMap[num];
		for (int i = 0; i < num; i++) {
			datas[i] = data.get(i);
		}
		for (int i = 0; i < num - 1; i++) {
			for (int j = 0; j < num - i - 1; j++) {
				String namePinyin1 = PinyinOperator
						.getPinYinFirstChar((String) datas[j].get("u_realname"));
				String namePinyin2 = PinyinOperator
						.getPinYinFirstChar((String) datas[j + 1]
								.get("u_realname"));
				if (namePinyin1.compareTo(namePinyin2) > 0) {
					HashMap<String, Object> tempMap = datas[j];
					datas[j] = datas[j + 1];
					datas[j + 1] = tempMap;
				}
			}
		}
		// change back to List
		data.clear();
		for (HashMap<String, Object> map : datas) {
			data.add(map);
		}
		return data;
	}

	/**
	 * 根据表名和主键获取详细信息
	 * 
	 * @param table
	 * @param primaryKey
	 *            放两个值：1，主键的名称：key 2，主键的值： keyValue
	 * @return
	 */
	public HashMap<String, Object> getDetailed(String table,
			HashMap<String, String> primaryKey) {
		SqliteUtil su = SqliteUtil.getInstance();
		try {
			HashMap<String, Object> data = su
					.queryTableDataByTableIdReturnHashMap(table, primaryKey);

			LogUtil.v(TAG, "getDetailed return data.size --> " + data.size());

			return data;
		} catch (Exception e) {
			LogUtil.e(TAG, e.getMessage());
		}
		return new HashMap<String, Object>();
	}

}
