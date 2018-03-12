package com.mapuni.android.enterpriseArchives;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import android.R.string;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
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
import com.mapuni.android.base.business.BaseClass;
import com.mapuni.android.base.util.DisplayUitl;
import com.mapuni.android.dataprovider.SqliteUtil;

public class XZCFFJActivity extends BaseActivity {

	/** ������ҵ���� */
	private String qydm = "";
	/** �������� item λ�� */
	private int totalposition = 0;
	/** �������� item λ�� */
	private int itemposition = 0;
	/** ������ҵ���������ƶ� item �� guid */
	private String guid = "";
	/** ���������� */
	private LinearLayout middleLayout;
	
	/** ����ExpandableListView  */
	private ExpandableListView mExpandableListView;
	
	private View view;
	private MyExpandableListViewAdapter adapter;
	private ArrayList<HashMap<String, Object>> xzcfItemList;
	private ArrayList<String> groupList;
	private ArrayList<ArrayList<HashMap<String, Object>>> childList;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.ui_mapuni);
		qydm = getIntent().getStringExtra("QYDM");
		
		totalposition = getIntent().getIntArrayExtra("totalposition")[0];
		itemposition = getIntent().getIntArrayExtra("itemposition")[0];

		middleLayout = (LinearLayout) findViewById(R.id.middleLayout);
		middleLayout.setPadding(8, 8, 8, 8);

		// ��ȡ���Ĳ���
		LayoutInflater inflater = LayoutInflater.from(XZCFFJActivity.this);
		view = inflater.inflate(R.layout.xzcffj_layout, null);
		
		mExpandableListView = (ExpandableListView) view.findViewById(R.id.mexpandablelistview);
		
		// �Զ��� ������
		adapter = new MyExpandableListViewAdapter(XZCFFJActivity.this);

		// ��ѯ������������¼
		String sql = "select * from T_WRY_JYXZCF where QYDM = '" + qydm + "'";
		xzcfItemList = BaseClass.DBHelper.queryBySqlReturnArrayListHashMap(sql);
		
		guid = xzcfItemList.get(totalposition).get("qydm") + "_" + xzcfItemList.get(totalposition).get("id");
		
		// ��ʼ������
		groupList = new ArrayList<String>();
		childList = new ArrayList<ArrayList<HashMap<String, Object>>>();

		switch (itemposition) {
		case XZCFData.LADJ:
			// ���������Ǽ�����
			groupList.add("����Υ����Ϊ�����ǼǱ�");
			groupList.add("����");
			initData(XZCFData.LADJ_B,XZCFData.LADJ_QT);
			
			// ���������������
			adapter.setData(groupList, childList);
			mExpandableListView.setAdapter(adapter);
			
			// ���� childItem ����
			setChildItemListener("LADJ_B","LADJ_QT");
			
			break;
		case XZCFData.TZGZCL:
			groupList.add("�����������������");
			groupList.add("�ֳ�ѯ�ʱ�¼��֤�ݲ���");
			groupList.add("�����������ڸ�֪��������");
			groupList.add("�����������ڸ�֪��");
			groupList.add("��֤��֪���ؿ��ʼ����鵥");
			groupList.add("����");
			
			initData(XZCFData.TZGZCL_DCQKB,XZCFData.TZGZCL_ZJCL,XZCFData.TZGZCL_SPD,XZCFData.TZGZCL_GZS,XZCFData.TZGZCL_XQD,XZCFData.TZGZCL_QT);
			
			adapter.setData(groupList, childList);
			mExpandableListView.setAdapter(adapter);
			
			setChildItemListener("TZGZCL_DCQKB","TZGZCL_ZJCL","TZGZCL_SPD","TZGZCL_GZS","TZGZCL_XQD","TZGZCL_QT");
			
			break;
		case XZCFData.CFJDCL:
			groupList.add("������������������");
			groupList.add("��������������");
			groupList.add("�����������ؿ��ʼ����鵥");
			groupList.add("����");
			
			initData(XZCFData.CFJDCL_SPD,XZCFData.CFJDCL_JDS,XZCFData.CFJDCL_XQD,XZCFData.CFJDCL_QT);
			
			adapter.setData(groupList, childList);
			mExpandableListView.setAdapter(adapter);
			
			setChildItemListener("CFJDCL_SPD","CFJDCL_JDS","CFJDCL_XQD","CFJDCL_QT");
			
			break;
		case XZCFData.FKJNPJ:
			groupList.add("�������Ʊ��");
			groupList.add("����");
			
			initData(XZCFData.FKJNPJ_FKJNPJ,XZCFData.FKJNPJ_QT);
			
			adapter.setData(groupList, childList);
			mExpandableListView.setAdapter(adapter);
			
			setChildItemListener("FKJNPJ_FKJNPJ","FKJNPJ_QT");
			break;
		case XZCFData.XT:
			groupList.add("�߸�֪ͨ��");
			groupList.add("ǿ��ִ������");
			groupList.add("����");
			
			initData(XZCFData.XT_CGTZS,XZCFData.XT_QZZXWS,XZCFData.XT_QT);
			
			adapter.setData(groupList, childList);
			mExpandableListView.setAdapter(adapter);
			
			setChildItemListener("XT_CGTZS","XT_QZZXWS","XT_QT");
			
			break;
		default:
			break;
		}
		
		// ���ý�����Ⲣ��䲼��
		SetBaseStyle((RelativeLayout) findViewById(R.id.parentLayout), XZCFData.strs[itemposition]);
		middleLayout.addView(view);
	}
	
	/** ���� ChildItem �ļ����¼� */
	private void setChildItemListener(final String... folderName) {
		mExpandableListView.setOnChildClickListener(new OnChildClickListener() {
			@Override
			public boolean onChildClick(ExpandableListView parent, View v,
					int groupPosition, int childPosition, long id) {

				fujian(childList.get(groupPosition).get(childPosition).get("filepath").toString(), folderName[groupPosition]);
				
				return false;
			}
		});
	}
	
	/** ���� guid �����ر�־��ѯ����ӵ� childList */
	HashMap<String, Object> tempMap = new HashMap<String, Object>();
	public void initData( int... loadMark) {
		for (int i = 0; i < loadMark.length; i++) {
			String sql = "select * from T_Attachment where FK_Unit = '" + loadMark[i] + "' and FK_Id = '" + guid + "'";
			ArrayList<HashMap<String, Object>> arrayList = SqliteUtil.getInstance().queryBySqlReturnArrayListHashMap(sql);
			// ���ļ���Ӻ�׺
			for (int j = 0; j < arrayList.size(); j++) {
				tempMap = arrayList.get(j);
				tempMap.put("filename", tempMap.get("filename").toString() + tempMap.get("extension").toString());
			}
			childList.add(arrayList);
		}
	}

	/** �ж������ػ��Ǵ򿪸��� */
	public void fujian(String fjname, String folderName) {
		String url = null;
		try {
			url = Global.getGlobalInstance().getSystemurl()+ "/Attach/" + folderName + "/" + java.net.URLEncoder.encode(fjname, "UTF-8");
			String filePathStr = Global.SDCARD_RASK_DATA_PATH + "/Attach/" + folderName + "/" + fjname;
			File file = new File(filePathStr);
			// ����ļ����ڴ��ļ�
			if (fjname != null && !fjname.equals("") && file.exists()) {

				DisplayUitl.openfile(filePathStr, XZCFFJActivity.this);
			} else {// ������������
				downfujian(url, filePathStr);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/** ���ظ��� */
	private void downfujian(String fjnameurl, String filePathStr) {
		try {
			DownloadFile downloadFile = new DownloadFile(XZCFFJActivity.this);

			String[] params = { fjnameurl, filePathStr };

			downloadFile.execute(params);
		} catch (Exception e) {
			Toast.makeText(XZCFFJActivity.this, "�������޸���", Toast.LENGTH_SHORT).show();
		}
	}
	
	@Override
	public void onBackPressed() {
		this.finish();
		super.onBackPressed();
	}
}