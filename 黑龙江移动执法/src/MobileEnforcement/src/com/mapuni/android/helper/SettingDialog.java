package com.mapuni.android.helper;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.mapuni.android.MobileEnforcement.R;
import com.mapuni.android.base.Global;
import com.mapuni.android.base.service.UpdtaeScriptDownloadConfigService;
import com.mapuni.android.base.util.DisplayUitl;
import com.mapuni.android.dataprovider.SqliteUtil;
import com.mapuni.android.netprovider.Net;

public class SettingDialog extends Dialog implements DialogInterface {

	private final String TAG = "SettingDialog";

	// private HelperController HelperC;
	private View settingview;
	private final String SYNCDATA = "sync_data"; // ͬ������
	private final String STATUS_BAR_TIPS = "status_bar_tips"; // ״̬��������ʾ
	private final String LISTLOADNUM = "listloadnum"; // �б�һ�μ�����������
	private final String SYNCDATANUM = "syncdatanum"; // ͬ������һ����������
	private final String AUTOASYNCDURATION = "autosyncduration"; // �Զ�ͬ��ʱ����
	private final String AUTOCLEANSYSGRABAGE = "autosysgrabage";// �Զ�����ϵͳ����
	// �����������Ӧ��ѡ��

	private final String[] listLoadSelections = { "30", "50", "100" };// �б�һ�μ�����������
	private final String[] requestNums = { "1000", "2000", "3000", "5000" };// ͬ������һ����������
	private final String[] requestDurations = { "5", "10", "30", "60" };// ����ʱ����
	private final String[] cleanDurations = { "7", "15", "21", "30" };// ����ϵͳ������ʱ������

	String[] names1 = { "�Ƿ��Զ���������", "�Ƿ��˳���״̬��ʾ" };// ��һ��ListView��ʾ������
	String[] keys1 = { SYNCDATA, STATUS_BAR_TIPS };// ��һ��ListView�������key����

	String[] names2 = { "�б�һ�μ�����������", "ͬ������һ����������", "�Զ�ͬ��ʱ����", "����ϵͳ������ʱ������" };// �ڶ���ListView��ʾ������
	String[] keys2 = { LISTLOADNUM, SYNCDATANUM, AUTOASYNCDURATION, AUTOCLEANSYSGRABAGE };// �ڶ���ListView�������key����
	boolean[] defValues1 = { false, true };// ��һ��ListView��Ĭ��ֵ����

	int[] defValues2 = { 30, 5000, 60, 15 };// �ڶ���ListView��Ĭ��ֵ����
	String[][] selections2 = { listLoadSelections, requestNums, requestDurations, cleanDurations };// listview3��ѡ��

	private TextView setSystem, About, EnvName, Version, CompanyName, Ownership;
	private ListView listView1, listView2;
	private ArrayList<HashMap<String, Object>> data1, data2;
	private SharedPreferences sp;
	private MyAdapter adapter1, adapter2;
	private ArrayList<HashMap<String, Object>> oldValues;
	private SqliteUtil su;
	private TextView tvCheckUpdate;

	public SettingDialog(Context context) {
		this(context, 0);
	}

	public SettingDialog(Context context, int theme) {
		super(context, theme);
		// Dialog���Ի�������ȡ������
		this.setCanceledOnTouchOutside(true);
	}

	protected SettingDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
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
		if (settingview == null) {
			/** ���ÿ��岻��ʾ������ */
			super.requestWindowFeature(Window.FEATURE_NO_TITLE);

			LayoutInflater factory = LayoutInflater.from(this.getContext());
			settingview = factory.inflate(R.layout.setting_main, null);

			setSystem = (TextView) settingview.findViewById(R.id.set_system);
			About = (TextView) settingview.findViewById(R.id.set_about);
			EnvName = (TextView) settingview.findViewById(R.id.set_envname);
			Version = (TextView) settingview.findViewById(R.id.set_version);
			CompanyName = (TextView) settingview.findViewById(R.id.set_companyname);

			tvCheckUpdate = (TextView) settingview.findViewById(R.id.check_update);

			tvCheckUpdate.setClickable(true);
			tvCheckUpdate.setFocusable(true);
			tvCheckUpdate.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					if (!Net.checkURL(Global.getGlobalInstance().getSystemurl())) {
						Toast.makeText(getContext(), "��ȡ����˰汾��Ϣʧ�ܣ�������������", 1).show();
					} else {
						Intent intent = new Intent(getContext(), UpdtaeScriptDownloadConfigService.class);
						getContext().startService(intent);
						Toast.makeText(getContext(), "���޸�����Ϣ", 1).show();
						// getContext().stopService(intent);
					}

				}
			});

			Ownership = (TextView) settingview.findViewById(R.id.set_ownership);
			listView1 = (ListView) settingview.findViewById(R.id.set_listView1);
			listView2 = (ListView) settingview.findViewById(R.id.set_listView2);

			String bbh = DisplayUitl.getVersionName(getContext());
			Version.setText("  " + bbh);
			getOldValues();
			// ��ʼ��ҳ����ʾ
			initData();
			adapter1 = new MyAdapter(data1);
			adapter2 = new MyAdapter(data2);

			listView1.setAdapter(adapter1);
			listView2.setAdapter(adapter2);

			listView1.setOnItemClickListener(listener);
			listView2.setOnItemClickListener(listener);

			super.setContentView(settingview);
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

	@Override
	public void cancel() {
		super.cancel();
	}

	/**
	 * ������ݿ�T_USER_SETTING�������е�������
	 */
	private void getOldValues() {
		su = SqliteUtil.getInstance();
		oldValues = su.queryBySqlReturnArrayListHashMap("select * from T_USER_SETTING where SYSCODE = '" + Global.getGlobalInstance().getSystemtype() + "'");
	}

	/**
	 * ��ȡԭ����ֵ
	 * 
	 * @param key
	 * @return
	 */
	private Object getValue(String key) {
		if (key == null || "".equals(key)) {
			return null;
		}
		Object values = null;
		if (oldValues != null && oldValues.size() > 0) {
			for (HashMap<String, Object> map : oldValues) {
				if (key.equalsIgnoreCase(String.valueOf(map.get("setkey")))) {
					values = map.get("setvalue");
					break;
				}
			}
		}
		return values;
	}

	/**
	 * Description:��ʼ��ҳ����Ҫ��ʾ������
	 * 
	 */
	private void initData() {
		data1 = new ArrayList<HashMap<String, Object>>();
		data2 = new ArrayList<HashMap<String, Object>>();

		// ��ʼ��data1���������ͣ�key��value
		for (int i = 0; i < names1.length; i++) {
			Object value = getValue(keys1[i]);
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("type", 1);
			map.put("name", names1[i]);
			map.put("value", value == null ? defValues1[i] : value);
			data1.add(map);
		}

		// ��ʼ��data2���������ͣ�key��value
		for (int k = 0; k < names2.length; k++) {
			Object value = getValue(keys2[k]);
			HashMap<String, Object> map2 = new HashMap<String, Object>();
			map2.put("type", 3);
			map2.put("name", names2[k]);
			map2.put("value", value == null ? defValues2[k] : value);
			data2.add(map2);
		}
	}

	/**
	 * ����ListView��onItemClick����¼�
	 */
	OnItemClickListener listener = new OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			HashMap<String, Object> map = null;
			int parentID = parent.getId();
			if (parentID == R.id.set_listView1) {
				map = data1.get(position);
				boolean result = Boolean.parseBoolean(String.valueOf(map.get("value")));
				// �л����ص���ʾͼƬ�����������ý��
				if (result) {
					view.findViewById(R.id.switchLayout).setBackgroundResource(R.drawable.switch_off);
					view.findViewById(R.id.onText).setVisibility(View.INVISIBLE);
					view.findViewById(R.id.offText).setVisibility(View.VISIBLE);
					map.put("value", false);
					saveSettings(R.id.set_listView1, keys1[position], false);
				} else {
					view.findViewById(R.id.switchLayout).setBackgroundResource(R.drawable.switch_on);
					view.findViewById(R.id.onText).setVisibility(View.VISIBLE);
					view.findViewById(R.id.offText).setVisibility(View.INVISIBLE);
					map.put("value", true);
					saveSettings(R.id.set_listView1, keys1[position], true);

				}
			} else if (parentID == R.id.set_listView2) {
				map = data2.get(position);
				showSelectDialog(R.id.set_listView2, position);
			}

		}

	};

	/**
	 * Description:����ѡ��Ի���
	 */
	private void showSelectDialog(final int viewId, final int position) {
		Dialog dialog;
		dialog = new Dialog(this.getContext());
		AlertDialog.Builder builder = new AlertDialog.Builder(this.getContext());
		builder.setIcon(R.drawable.icon_mapuni_white);
		HashMap<String, Object> map = null;
		String[] items = null; // Ҫ��ʾ��ѡ��
		int defIndex = 0; // Ĭ��ѡ����
		if (viewId == R.id.set_listView2) {
			map = data2.get(position);
			builder.setTitle("ѡ��" + names2[position]);

			items = selections2[position];
		}
		final String[] items1 = items;
		String value = String.valueOf(map.get("value"));
		boolean flag = false;// ����Ƿ�������Ҫ��ʾ��ֵ��
		for (int i = 0; i < items.length; i++) {
			if (value.equals(items[i])) {
				defIndex = i;
				flag = true;
				break;
			}
		}

		builder.setSingleChoiceItems(items, defIndex, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				if (viewId == R.id.set_listView2) {
					data2.get(position).put("value", items1[which]);
					adapter2.notifyDataSetChanged();
					saveSettings(viewId, keys2[position], items1[which]);
				}
			}
		});

		dialog = builder.create();
		dialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
		dialog.show();
	}

	/**
	 * Description:�������ý��
	 */
	private void saveSettings(int viewId, String key, Object value) {

		String sql;
		Object oldValue = getValue(key);
		if (oldValue == null) {
			sql = "insert into T_USER_SETTING('SYSCODE','SETKEY','SETVALUE') VALUES('" + Global.getGlobalInstance().getSystemtype() + "','" + key + "','" + value + "')";
		} else {
			sql = "update T_USER_SETTING set SETVALUE = '" + value + "' where SETKEY = '" + key + "' and SYSCODE = '" + Global.getGlobalInstance().getSystemtype() + "'";
		}
		su.execute(sql);
		getOldValues();
	}

	/**
	 * FileName: Setting��ListView������
	 */
	class MyAdapter extends BaseAdapter {

		private final ArrayList<HashMap<String, Object>> data;
		private final LayoutInflater inflater;

		public MyAdapter(ArrayList<HashMap<String, Object>> data) {
			this.data = data;
			inflater = getLayoutInflater();
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
		public View getView(int position, View convertView, ViewGroup parent) {
			if (convertView == null) {
				convertView = inflater.inflate(R.layout.setting_item, null);
			}
			HashMap<String, Object> map = data.get(position);
			int type = (Integer) map.get("type");
			TextView textView = (TextView) convertView.findViewById(R.id.textView1);
			TextView valueText = (TextView) convertView.findViewById(R.id.valueText1);
			LinearLayout switchLayout = (LinearLayout) convertView.findViewById(R.id.switchLayout);
			TextView onText = (TextView) convertView.findViewById(R.id.onText);
			TextView offText = (TextView) convertView.findViewById(R.id.offText);
			textView.setText(String.valueOf(map.get("name")));
			if (type == 1) {
				valueText.setVisibility(View.GONE);
				switchLayout.setVisibility(View.VISIBLE);
				if (Boolean.parseBoolean(String.valueOf(map.get("value")))) {
					switchLayout.setBackgroundResource(R.drawable.switch_on);
					onText.setVisibility(View.VISIBLE);
					offText.setVisibility(View.INVISIBLE);
				} else {
					switchLayout.setBackgroundResource(R.drawable.switch_off);
					onText.setVisibility(View.INVISIBLE);
					offText.setVisibility(View.VISIBLE);
				}
			} else {
				valueText.setVisibility(View.VISIBLE);
				switchLayout.setVisibility(View.GONE);
				if (type == 2) {
					// if(position == 0)//û��������˵���ʽѡ��֮ǰif(position == 0 ||
					// position == 1)
					// valueText.setText(listTextSizeName[getElementIndex(listTextSize,
					// String.valueOf(map.get("value")))]);
					// valueText.setText(Integer.parseInt(String.valueOf(map.get("value")))==1?"�Ź���":"�����л�");
					// else
					// valueText.setText(listTextSizeName[getElementIndex(listTextSize,
					// String.valueOf(map.get("value")))]);
				} else {
					if (position == 3) {
						valueText.setText(String.valueOf(map.get("value")) + "��֮ǰ");
					} else
						valueText.setText(String.valueOf(map.get("value")) + (position == 2 ? "����" : "��"));
				}
			}
			return convertView;
		}
	}
}
