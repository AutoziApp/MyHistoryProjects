package com.mapuni.android.enterpriseArchives;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

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

public class QYHJGLZDinfoActivity extends BaseActivity {

	private String qydm = "";
	/** ������ҵ���������ƶ� item �� guid */
	private String guid = "";
	private int position = 0;
	private LinearLayout middleLayout;
	/** ���� EditText �ؼ� */
	private EditText et_zdmc,et_wjh,et_fbrq,et_ssbm,et_pzdw,et_sfba,et_basj,et_babm,et_ms,et_zwfj;
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
		LayoutInflater inflater = LayoutInflater.from(QYHJGLZDinfoActivity.this);
		view = inflater.inflate(R.layout.qyhjglzd_layout, null);
		
		et_zdmc = (EditText) view.findViewById(R.id.et_zdmc);
		et_wjh = (EditText) view.findViewById(R.id.et_wjh);
		et_fbrq = (EditText) view.findViewById(R.id.et_fbrq);
		et_ssbm = (EditText) view.findViewById(R.id.et_ssbm);
		et_pzdw = (EditText) view.findViewById(R.id.et_pzdw);
		et_sfba = (EditText) view.findViewById(R.id.et_sfba);
		et_basj = (EditText) view.findViewById(R.id.et_basj);
		et_babm = (EditText) view.findViewById(R.id.et_babm);
		et_ms = (EditText) view.findViewById(R.id.et_ms);
		
		lv_fj = (ListView) view.findViewById(R.id.lv_fj);
		et_zwfj = (EditText) view.findViewById(R.id.et_zwfj);
		
		// ������ҵ����͵�� item �� position ��ȡÿ�� item ������
		String sql = "select * from T_WRY_QYHJGLZD where QYDM = '" + qydm + "'";
		HashMap<String, Object> dataMap = SqliteUtil.getInstance().queryBySqlReturnArrayListHashMap(sql).get(position);
		
		// ���� EditText ����
		et_zdmc.setText(isNull(dataMap.get("zdmc")));
		et_wjh.setText(isNull(dataMap.get("wjh")));
		et_fbrq.setText(dateFormat(isNull(dataMap.get("fbrq"))));
		et_ssbm.setText(isNull(dataMap.get("ssbm")));
		et_pzdw.setText(isNull(dataMap.get("pzdw")));
		et_sfba.setText(isNull(dataMap.get("sfba")));
		et_basj.setText(dateFormat(isNull(dataMap.get("basj"))));
		et_babm.setText(isNull(dataMap.get("babm")));
		et_ms.setText(isNull(dataMap.get("ms")));
		
		// ��ȡ��ҵ���������ƶ� item �� guid
		guid = dataMap.get("guid").toString();
		
		// ���� guid ��ѯ������ FilePath ��FK_Unit = 70��
		String sql_str = "select * from T_Attachment where FK_Id = '" + guid + "' and FK_Unit = '70'";
		dataList = SqliteUtil.getInstance().queryBySqlReturnArrayListHashMap(sql_str);
		
		// ������������Ӻ�׺��
		HashMap<String, Object> tempMap = new HashMap<String, Object>();
		for (int i = 0; i < dataList.size(); i++) {
			tempMap = dataList.get(i);
			tempMap.put("filename", tempMap.get("filename").toString() + tempMap.get("extension").toString());
		}
		
		if (dataList.size() > 0) {
			SimpleAdapter adapter = new SimpleAdapter(QYHJGLZDinfoActivity.this, dataList, R.layout.item_list, new String[]{"filename"},new int[]{R.id.tv_item});
			lv_fj.setAdapter(adapter);
			
			lv_fj.setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
						long arg3) {
					filepathName = dataList.get(arg2).get("filepath").toString();
					fujian(filepathName);
				}
			});
		}else {
			lv_fj.setVisibility(View.GONE);
			et_zwfj.setVisibility(View.VISIBLE);
		}
		
		// ���ý�����Ⲣ��䲼��
		SetBaseStyle((RelativeLayout) findViewById(R.id.parentLayout),"��ҵ���������ƶ�");
		middleLayout.addView(view);
	}
	
	/** �ж������ػ��Ǵ򿪸��� */
	public void fujian(String fjname) {
		String url = null;
		try {
			url = Global.getGlobalInstance().getSystemurl() + "/Attach/QYHJGLZD_FJ/" + java.net.URLEncoder.encode(fjname, "UTF-8");
			String filePathStr = Global.SDCARD_RASK_DATA_PATH+ "/Attach/QYHJGLZD_FJ/" + fjname;
			File file = new File(filePathStr);
			// ����ļ����ڴ��ļ�
			if (fjname != null && !fjname.equals("") && file.exists()) {
				
				DisplayUitl.openfile(filePathStr, QYHJGLZDinfoActivity.this);
			} else {// ������������
				downfujian(url, filePathStr);
			}
		} catch (Exception e) {
			e.printStackTrace();
		};
	}

	/** ���ظ��� */
	private void downfujian(String fjnameurl, String filePathStr) {
		try {
			DownloadFile downloadFile = new DownloadFile(QYHJGLZDinfoActivity.this);

			String[] params = { fjnameurl, filePathStr };

			downloadFile.execute(params);
		} catch (Exception e) {
			Toast.makeText(QYHJGLZDinfoActivity.this, "�������޸���", Toast.LENGTH_SHORT).show();
		}
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