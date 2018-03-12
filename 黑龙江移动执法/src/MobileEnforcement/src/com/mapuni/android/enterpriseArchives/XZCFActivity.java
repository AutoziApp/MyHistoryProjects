package com.mapuni.android.enterpriseArchives;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import android.R.string;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.hp.hpl.sparta.Text;
import com.mapuni.android.MobileEnforcement.R;
import com.mapuni.android.base.BaseActivity;
import com.mapuni.android.base.Global;
import com.mapuni.android.base.util.DisplayUitl;
import com.mapuni.android.dataprovider.SqliteUtil;

public class XZCFActivity extends BaseActivity {

	private String qydm = "";
	/** ���� item �� guid */
	private String guid = "";
	private int position = 0;
	private LinearLayout middleLayout;
	/** ���� EditText �ؼ� */
	private EditText et_ajmc, et_labh, et_lasj, et_cfje, et_ajblzt, et_ay,
			et_cfnr;
	private ListView lv_fj;
	private View view;
	/** �����б� */
	private ArrayList<HashMap<String, Object>> dataList;
	/** �������� */
	private String filepathName;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.ui_mapuni);
		qydm = getIntent().getStringExtra("QYDM");
		position = getIntent().getIntArrayExtra("position")[0];

		middleLayout = (LinearLayout) findViewById(R.id.middleLayout);
		middleLayout.setPadding(8, 8, 8, 8);

		// ��ȡ���Ĳ���
		LayoutInflater inflater = LayoutInflater.from(XZCFActivity.this);
		view = inflater.inflate(R.layout.xzcf_layout, null);
		
		et_ajmc = (EditText) view.findViewById(R.id.et_ajmc);
		et_labh = (EditText) view.findViewById(R.id.et_labh);
		et_lasj = (EditText) view.findViewById(R.id.et_lasj);
		et_cfje = (EditText) view.findViewById(R.id.et_cfje);
		et_ajblzt = (EditText) view.findViewById(R.id.et_ajblzt);
		et_ay = (EditText) view.findViewById(R.id.et_ay);
		et_cfnr = (EditText) view.findViewById(R.id.et_cfnr);

		lv_fj = (ListView) view.findViewById(R.id.lv_fj);

		// ������ҵ����͵�� item �� position ��ȡÿ�� item ������
		String sql = "select * from T_WRY_JYXZCF where QYDM = '" + qydm + "'";
		HashMap<String, Object> dataMap = SqliteUtil.getInstance().queryBySqlReturnArrayListHashMap(sql).get(position);

		// ���� EditText ����
		et_ajmc.setText(isNull(dataMap.get("ajmc")));
		et_labh.setText(isNull(dataMap.get("labh")));
		et_lasj.setText(dateFormat(isNull(dataMap.get("lasj"))));
		et_cfje.setText(isNull(dataMap.get("cfje")));
		et_ajblzt.setText(isNull(dataMap.get("ajblzt")));
		et_ay.setText(isNull(dataMap.get("ay")));
		et_cfnr.setText(dateFormat(isNull(dataMap.get("cfnr"))));

		dataList = new ArrayList<HashMap<String,Object>>();
		String[] strs = XZCFData.strs;

		for (int i = 0; i < strs.length; i++) {
			HashMap<String, Object> dataDetailMap = new HashMap<String, Object>();
			dataDetailMap.put("itmeName", strs[i]);
			dataList.add(dataDetailMap);
		}
		
		SimpleAdapter adapter = new SimpleAdapter(XZCFActivity.this, dataList,R.layout.item_list, new String[] { "itmeName" },new int[] { R.id.tv_item });
		lv_fj.setAdapter(adapter);

		lv_fj.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				Intent intent = new Intent(XZCFActivity.this, XZCFFJActivity.class);
				intent.putExtra("QYDM", qydm);
				intent.putExtra("totalposition", new int[] {position});
				intent.putExtra("itemposition", new int[] {arg2});
				startActivity(intent);
			}
		});

		// ���ý�����Ⲣ��䲼��
		SetBaseStyle((RelativeLayout) findViewById(R.id.parentLayout), "��������");
		middleLayout.addView(view);
	}

	
	/** �ж��Ƿ�Ϊ�� */
	private String isNull(Object obj) {
		String str = obj.toString();
		if (null != str && !"".equals(str)) {
			return str;
		}
		return "";
	}

	/** ��ʽ��ʱ�� */
	private String dateFormat(String str) {
		return str.split("T")[0].toString();
	}
}