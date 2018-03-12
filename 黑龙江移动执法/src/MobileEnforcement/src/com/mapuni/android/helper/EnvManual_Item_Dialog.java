package com.mapuni.android.helper;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
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
import android.widget.Toast;

import com.mapuni.android.MobileEnforcement.R;
import com.mapuni.android.base.controls.loading.YutuLoading;
import com.mapuni.android.base.util.LogUtil;
import com.mapuni.android.dataprovider.SqliteUtil;

public class EnvManual_Item_Dialog extends Dialog implements DialogInterface {

	private final String TAG = "EnvManual_Item_Dialog";
	private int Itemcode;
	protected YutuLoading yutuLoading;
	private String sql;
	private View view;
	private TextView EnvManual_title;
	private ListView EnvManual_listview;
	// ��ѯ�õ������б�
	private ArrayList<HashMap<String, Object>> listData = null;
	// ÿһ��������ϸ��Ϣ
	private HashMap<String, Object> detailInfo = null;
	// ����
	private String tablename = "";
	// ����
	private final HashMap<String, String> primaryKey = new HashMap<String, String>();
	private EnvManualController EnvManualC;
	private int ItemCode;

	public EnvManual_Item_Dialog(Context context) {
		this(context, 0);
		// Dialog���Ի�������ȡ������
		this.setCanceledOnTouchOutside(true);
	}

	public EnvManual_Item_Dialog(Context context, int theme) {
		super(context, theme);

	}

	protected EnvManual_Item_Dialog(Context context, boolean cancelable,
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

	public void show(int ItemCode) {
		if (view == null) {
			/** ���ÿ��岻��ʾ������ */
			super.requestWindowFeature(Window.FEATURE_NO_TITLE);
			LayoutInflater factory = LayoutInflater.from(this.getContext());
			view = factory.inflate(R.layout.envmanual_main, null);
			EnvManual_title = (TextView) view.findViewById(R.id.env_title);
			EnvManual_listview = (ListView) view
					.findViewById(R.id.env_listView);
		}
		this.Itemcode = ItemCode;
		listData = new ArrayList<HashMap<String, Object>>();
		switch (this.Itemcode) {
		case EnvManualController.HBFL:
			EnvManual_title.setText("��������");
			tablename = "T_ZSK_FLFGBZ";
			sql = "select id,ymbt as ltitle,sjlb as stitle from " + tablename;
			listData = getDataList(sql);
			break;
		case EnvManualController.HBBZ:
			EnvManual_title.setText("������׼");
			tablename = "T_ZSK_FLFGBZ";
			sql = "select id, ymbt as ltitle,sjlb as stitle from " + tablename;
			listData = getDataList(sql);
			break;
		case EnvManualController.ZDWJ:
			EnvManual_title.setText("�ƶ��ļ�");
			tablename = "T_ZSK_FLFGBZ";
			sql = "select id,ymbt as ltitle,sjlb as stitle from " + tablename;
			listData = getDataList(sql);
			break;
		case EnvManualController.WHP:
			EnvManual_title.setText("Σ��Ʒ");
			tablename = "V_WHY_WHP";
			sql = "select id, cname as ltitle,itemalias as stitle from "
					+ tablename;
			listData = getDataList(sql);
			break;
		case EnvManualController.YJYA:
			EnvManual_title.setText("Ӧ��Ԥ��");
			tablename = "V_ZSK_YAJAL";
			sql = "select id, bt as ltitle,fj as stitle from " + tablename;
			listData = getDataList(sql);
			break;
		case EnvManualController.ZJK:
			EnvManual_title.setText("ר�ҿ�");
			tablename = "V_ZSK_Expert_New";
			sql = "select id, name as ltitle,administration_business as stitle from "
					+ tablename;
			listData = getDataList(sql);
			break;

		}

		if (listData != null && listData.size() > 0) {
			EnvManual_listview.setAdapter(new EnvManualListAdapter(this
					.getContext(), listData));
			EnvManual_listview
					.setOnItemClickListener(new OnItemClickListener() {

						@Override
						public void onItemClick(AdapterView<?> arg0, View arg1,
								int arg2, long arg3) {
							String guid = listData.get(arg2).get("id")
									.toString();
							if (guid == null || guid.equals("")) {
								Toast.makeText(getContext(), "������ϸ��Ϣ",
										Toast.LENGTH_SHORT).show();
								return;
							}
							HashMap<String, String> primaryKey = new HashMap<String, String>();
							primaryKey.put("key", "id");
							primaryKey.put("keyValue", guid);
							detailInfo = getDetailed(tablename, primaryKey);

							EnvManualController.getInstance().openOtherDialog(
									EnvManualController.DETAILINFO, detailInfo,
									Itemcode);

						}
					});
			super.setContentView(view);
		} else {
			yutuLoading = new YutuLoading(this.getContext());
			yutuLoading.setLoadMsg("", "�Բ����������ݣ�", Color.BLACK);
			yutuLoading.setFocusable(false);
			yutuLoading.setClickable(false);
			yutuLoading.setEnabled(false);
			yutuLoading.setFailed();
			yutuLoading.setBackgroundColor(Color.WHITE);
			yutuLoading
					.setLayoutParams(new android.widget.AbsListView.LayoutParams(
							android.widget.AbsListView.LayoutParams.FILL_PARENT,
							android.widget.AbsListView.LayoutParams.FILL_PARENT));
			super.setContentView(yutuLoading);
		}
		super.show();
	}

	@Override
	public void hide() {
		super.hide();
	}

	@Override
	public void cancel() {
		super.cancel();
	}

	@Override
	public void dismiss() {
		super.dismiss();
	}

	/**
	 * ������
	 */
	public class EnvManualListAdapter extends BaseAdapter {

		public Context context;
		public ArrayList<HashMap<String, Object>> data;

		public EnvManualListAdapter(Context context,
				ArrayList<HashMap<String, Object>> data) {
			this.context = context;
			this.data = data;
		}

		@Override
		public int getCount() {
			return data.size();
		}

		@Override
		public Object getItem(int position) {
			return data.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(final int position, View convertView,
				ViewGroup parent) {

			ViewHolder holder = null;
			if (convertView == null) {
				holder = new ViewHolder();
				/** ��������GridView��item���� */
				convertView = LayoutInflater.from(context).inflate(
						R.layout.envmanual_item_list, null);
				/** ��ʼ��item����� */
				holder.env_ltitle = (TextView) convertView
						.findViewById(R.id.env_ltitle);
				holder.env_stitle = (TextView) convertView
						.findViewById(R.id.env_stitle);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			holder.env_ltitle.setText(data.get(position).get("ltitle")
					.toString());
			holder.env_stitle.setText(data.get(position).get("stitle")
					.toString());
			return convertView;
		}

		/** ����һ��ViewHolder�������Ż�View */
		class ViewHolder {
			TextView env_ltitle;
			TextView env_stitle;
		}

	}

	/**
	 * ����SQL����ѯ������(�˷����ڵ�����ɺ����ر��α�)
	 */
	public ArrayList<HashMap<String, Object>> getDataList(String sql) {
		SqliteUtil su = SqliteUtil.getInstance();
		ArrayList<HashMap<String, Object>> data = su
				.queryBySqlReturnArrayListHashMap(sql);
		return data;
	}

	/**
	 * ���ݱ�����������ȡ��ϸ��Ϣ
	 * 
	 * @param table
	 * @param primaryKey
	 *            ������ֵ��1�����������ƣ�key 2��������ֵ�� keyValue
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
