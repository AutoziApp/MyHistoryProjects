package com.mapuni.android.helper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.mapuni.android.MobileEnforcement.R;
import com.mapuni.android.base.DownloadActivity;
import com.mapuni.android.base.Global;
import com.mapuni.android.base.business.BaseClass;
import com.mapuni.android.base.business.BaseDataSync;
import com.mapuni.android.base.util.DisplayUitl;
import com.mapuni.android.dataprovider.XmlHelper;
import com.mapuni.android.netprovider.Net;

public class DataSynDialog extends Dialog implements DialogInterface {
	/** ��־��¼��־ */
	private final String TAG = "DataSynDialog";
	private View datasyn_view;
	/** ���湴ѡ�ĸ�ѡ�� */
	protected LinkedList<String> chkChoice = null;
	private ListView datasyn_listview;
	private CheckBox datasyn_checkall;
	private ArrayList<HashMap<String, Object>> dataList;
	/** ѡ��ť��ʶ */
	private final boolean select_btn_on = false;
	/** ListView������ */
	private CheckAdapter mCheckAdapter;
	private int checkNum; // ��¼ѡ�е���Ŀ����
	private boolean checkedboxStatus[], allFlag = false; // ��¼ѡ�е�״̬
	private Button syncLastestBtn, syncAllBtn;// ͬ�����¡�ͬ��ȫ����ť
	/** ����ͬ��ҵ���� */
	private BaseDataSync dataSync = null; // ҵ���
	private Context mContext;
	private String Sel_loc_id;

	public DataSynDialog(Context context) {
		this(context, 0);
		mContext = context;
		/** Dialog���Ի�������ȡ������ */
		this.setCanceledOnTouchOutside(true);
	}

	public DataSynDialog(Context context, int theme) {
		super(context, theme);

	}

	protected DataSynDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
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

	@Override
	public void show() {
		if (datasyn_view == null) {
			/** ���ÿ��岻��ʾ������ */
			super.requestWindowFeature(Window.FEATURE_NO_TITLE);
			LayoutInflater factory = LayoutInflater.from(this.getContext());
			datasyn_view = factory.inflate(R.layout.datasyn_main, null);
			// ͬ��ȫ����ť
			syncAllBtn = (Button) datasyn_view.findViewById(R.id.datasyn_all);
			syncAllBtn.setOnClickListener(syncAllBtnListener);
			// ͬ�����°�ť
			syncLastestBtn = (Button) datasyn_view.findViewById(R.id.datasyn_lastest);
			syncLastestBtn.setOnClickListener(syncLastestBtnListener);
			// ͬ����Ϣ�б�
			datasyn_listview = (ListView) datasyn_view.findViewById(R.id.datasyn_listview);
			// ȫѡ��ť
			datasyn_checkall = (CheckBox) datasyn_view.findViewById(R.id.datasyn_all_check);

			// ��ȡͬ�����ݴ洢����
			dataSync = new BaseDataSync();
			chkChoice = new LinkedList<String>();
			dataList = new ArrayList<HashMap<String, Object>>();
			/** ��XML�ļ��ж�ȡ���� */
			dataList = getMoreMenu("datasync_dialog_config.xml", "item");
			mCheckAdapter = new CheckAdapter(getContext(), dataList);
			datasyn_listview.setAdapter(mCheckAdapter);
			datasyn_checkall.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View arg0) {
					chkChoice.clear();
					for (int i = 0; i < dataList.size(); i++) {
						mCheckAdapter.bChecked.put(i, datasyn_checkall.isChecked());
						if (datasyn_checkall.isChecked()) {
							Sel_loc_id = dataList.get(i).get("id").toString();
							chkChoice.add(Sel_loc_id);
						} else {
							chkChoice.clear();
						}
					}
					RefreshAdapter();
				}
			});
			super.setContentView(datasyn_view);
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

	/** ��ȡXML�ļ� */
	protected ArrayList<HashMap<String, Object>> getMoreMenu(String xml, String nodename) {
		try {
			ArrayList<HashMap<String, Object>> tablename = BaseClass.xmlHelper.getDataFromXmlStream(this.getContext().getResources().getAssets().open(xml), XmlHelper.NODE_LEVEL1);
			ArrayList<HashMap<String, Object>> tablenameCN = new ArrayList<HashMap<String, Object>>();
			for (HashMap<String, Object> tableMap : tablename) {
				if (DisplayUitl.getAuthority(tableMap.get("qxid").toString())) {// �жϵ�ǰ�û��Ƿ���Ȩ��
					tablenameCN.add(tableMap);
				}
			}
			return tablenameCN;
		} catch (IOException e) {
			Log.e(TAG, e.getMessage());
		}
		return new ArrayList<HashMap<String, Object>>();
		/*
		 * ArrayList<HashMap<String, Object>> MoreMenu = null; InputStream
		 * stream = null; try { stream =
		 * this.getContext().getResources().getAssets().open(xml); MoreMenu =
		 * XmlHelper.getList(stream, nodename); } catch (IOException e) {
		 * e.printStackTrace(); } catch (DocumentException e) {
		 * e.printStackTrace(); } return MoreMenu;
		 */
	}

	public class CheckAdapter extends BaseAdapter {

		private LayoutInflater inflater = null;

		private final ArrayList<HashMap<String, Object>> mData;

		/** ����checkBoxѡ��״̬ */
		private final Map<Integer, Boolean> bChecked;

		public CheckAdapter(Context context, ArrayList<HashMap<String, Object>> data) {
			this.mData = data;
			inflater = LayoutInflater.from(context);
			bChecked = new HashMap<Integer, Boolean>();
			for (int i = 0; i < mData.size(); i++) {
				bChecked.put(i, false);
			}
		}

		@Override
		public int getCount() {
			return mData.size();
		}

		@Override
		public Object getItem(int position) {
			return mData.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			ViewHolder holder = null;
			if (convertView == null) {
				holder = new ViewHolder();
				/** ��������GridView��item���� */
				convertView = inflater.inflate(R.layout.datasyn_list, null);
				/** ��ʼ��item����� */
				holder.leftIcon = (ImageView) convertView.findViewById(R.id.lefticon);
				holder.datasyn_tname = (TextView) convertView.findViewById(R.id.datasyn_tablename);
				holder.datasyn_check = (CheckBox) convertView.findViewById(R.id.datasyn_check);
				holder.datasyn_check.setOnCheckedChangeListener(new OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
						allFlag = false;
						bChecked.put(position, arg1);
						Set<Map.Entry<Integer, Boolean>> set = bChecked.entrySet();
						boolean flag = true;
						Sel_loc_id = dataList.get(position).get("id").toString();
						if (arg1) {
							chkChoice.add(Sel_loc_id);
						} else {
							chkChoice.remove(Sel_loc_id);
						}
						for (Map.Entry<Integer, Boolean> entry : set) {
							flag &= entry.getValue();
						}
						Log.e("hello", "-------------" + flag);
						datasyn_checkall.setChecked(flag);
					}
				});
				convertView.setTag(holder);

			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			/** ͨ��holder����ȡ���� */
			holder.datasyn_tname.setText(mData.get(position).get("tablename").toString());
			holder.datasyn_check.setChecked(bChecked.get(position));
			return convertView;
		}
	}

	/** ����һ��ViewHolder�������Ż�View */
	class ViewHolder {
		ImageView leftIcon;
		TextView datasyn_tname;
		CheckBox datasyn_check;
	}

	/** ˢ�������� */
	public void RefreshAdapter() {
		mCheckAdapter.notifyDataSetChanged();
	}

	/** ͬ��ȫ�����ݰ�ť����¼� */
	private final android.view.View.OnClickListener syncAllBtnListener = new android.view.View.OnClickListener() {
		@Override
		public void onClick(View v) {
			if (!Net.checkNet(mContext)) {
				// Net.OpenWirelessSettings(mContext);
				Toast.makeText(mContext, "�޷�ͬ��,��������״���Ƿ�����", Toast.LENGTH_SHORT).show();
				return;
			}
			if (Global.IsDataSync) {
				Toast.makeText(mContext, "��ǰͬ����δ��ɣ����Ժ�", 1).show();
				return;
			}
			doSync(false);
		}
	};

	/** ͬ���������ݰ�ť����¼� */
	private final android.view.View.OnClickListener syncLastestBtnListener = new android.view.View.OnClickListener() {
		@Override
		public void onClick(View v) {
			if (!Net.checkNet(mContext)) {
				// Net.OpenWirelessSettings(mContext);
				Toast.makeText(mContext, "�޷�ͬ��,��������״���Ƿ�����", Toast.LENGTH_SHORT).show();
				return;
			}
			if (Global.IsDataSync) {
				Toast.makeText(mContext, "��ǰͬ����δ��ɣ����Ժ�", 1).show();
				return;
			}
			doSync(true);
		}
	};

	/**
	 * Description:ִ��ͬ��ȫ�����ݻ�ͬ���������ݲ���
	 * 
	 * @param updateOrFetchAllData
	 * @author Administrator<br>
	 *         Create at: 2012-12-4 ����04:12:17
	 */
	private void doSync(boolean updateOrFetchAllData) {

		Object[] objs = chkChoice.toArray();

		if (objs.length < 1) {
			Toast.makeText(getContext(), "��ѡ��Ҫ�����ı�", Toast.LENGTH_SHORT).show();
		} else {
			Global.IsDataSync = true;
			StringBuilder tablesSB = new StringBuilder();

			ArrayList<HashMap<String, Object>> data = dataSync.getTablenameCN(mContext);
			for (Object obj : objs) {
				String id = obj.toString();
				for (int i = 0; i < data.size(); i++) {
					HashMap<String, Object> dataRow = data.get(i);
					if (id.trim().equals(dataRow.get(dataSync.GetKeyField()).toString().trim())) {
						Object objTable = dataRow.get(BaseDataSync.SYNC_TABLE_REFLECT);
						if (objTable != null && !"".equals(objTable)) {
							tablesSB.append(objTable.toString() + ",");
						}
					}
				}
			}
			if (tablesSB.indexOf(",") != -1) {
				tablesSB = new StringBuilder(tablesSB.substring(0, tablesSB.length() - 1));
			}
			Log.v(TAG, (updateOrFetchAllData ? "��������" : "ȫ������") + "ͬ���� --> " + tablesSB.toString());

			/** ���÷������� */
			Intent downloadIntent = new Intent(mContext, DownloadActivity.class);
			Bundle bundle = new Bundle();
			bundle.putString("tables", tablesSB.toString());
			bundle.putSerializable("BusinessObj", dataSync);
			bundle.putString("BusinessType", "DataSync");
			bundle.putString("notificationTile", "����ͬ��");
			bundle.putBoolean("updateOrFetchAllData", updateOrFetchAllData);
			downloadIntent.putExtras(bundle);

			downloadIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			mContext.startActivity(downloadIntent);
			DataSynDialog.this.dismiss();

		}
	}
}
