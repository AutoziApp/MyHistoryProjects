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

	/** 定义企业代码 */
	private String qydm = "";
	/** 定义点击的 item 位置 */
	private int totalposition = 0;
	/** 定义点击的 item 位置 */
	private int itemposition = 0;
	/** 定义企业环境管理制度 item 的 guid */
	private String guid = "";
	/** 定义主布局 */
	private LinearLayout middleLayout;
	
	/** 定义ExpandableListView  */
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

		// 获取填充的布局
		LayoutInflater inflater = LayoutInflater.from(XZCFFJActivity.this);
		view = inflater.inflate(R.layout.xzcffj_layout, null);
		
		mExpandableListView = (ExpandableListView) view.findViewById(R.id.mexpandablelistview);
		
		// 自定义 适配器
		adapter = new MyExpandableListViewAdapter(XZCFFJActivity.this);

		// 查询的行政处罚记录
		String sql = "select * from T_WRY_JYXZCF where QYDM = '" + qydm + "'";
		xzcfItemList = BaseClass.DBHelper.queryBySqlReturnArrayListHashMap(sql);
		
		guid = xzcfItemList.get(totalposition).get("qydm") + "_" + xzcfItemList.get(totalposition).get("id");
		
		// 初始化数据
		groupList = new ArrayList<String>();
		childList = new ArrayList<ArrayList<HashMap<String, Object>>>();

		switch (itemposition) {
		case XZCFData.LADJ:
			// 加载立案登记数据
			groupList.add("环境违法行为立案登记表");
			groupList.add("其他");
			initData(XZCFData.LADJ_B,XZCFData.LADJ_QT);
			
			// 给适配器添加数据
			adapter.setData(groupList, childList);
			mExpandableListView.setAdapter(adapter);
			
			// 设置 childItem 监听
			setChildItemListener("LADJ_B","LADJ_QT");
			
			break;
		case XZCFData.TZGZCL:
			groupList.add("环境案件调查情况表");
			groupList.add("现场询问笔录等证据材料");
			groupList.add("行政处罚听众告知书审批单");
			groupList.add("行政处罚听众告知书");
			groupList.add("听证告知书特快邮件详情单");
			groupList.add("其他");
			
			initData(XZCFData.TZGZCL_DCQKB,XZCFData.TZGZCL_ZJCL,XZCFData.TZGZCL_SPD,XZCFData.TZGZCL_GZS,XZCFData.TZGZCL_XQD,XZCFData.TZGZCL_QT);
			
			adapter.setData(groupList, childList);
			mExpandableListView.setAdapter(adapter);
			
			setChildItemListener("TZGZCL_DCQKB","TZGZCL_ZJCL","TZGZCL_SPD","TZGZCL_GZS","TZGZCL_XQD","TZGZCL_QT");
			
			break;
		case XZCFData.CFJDCL:
			groupList.add("行政处罚决定审批单");
			groupList.add("行政处罚决定书");
			groupList.add("处罚决定书特快邮件详情单");
			groupList.add("其他");
			
			initData(XZCFData.CFJDCL_SPD,XZCFData.CFJDCL_JDS,XZCFData.CFJDCL_XQD,XZCFData.CFJDCL_QT);
			
			adapter.setData(groupList, childList);
			mExpandableListView.setAdapter(adapter);
			
			setChildItemListener("CFJDCL_SPD","CFJDCL_JDS","CFJDCL_XQD","CFJDCL_QT");
			
			break;
		case XZCFData.FKJNPJ:
			groupList.add("罚款缴纳票据");
			groupList.add("其他");
			
			initData(XZCFData.FKJNPJ_FKJNPJ,XZCFData.FKJNPJ_QT);
			
			adapter.setData(groupList, childList);
			mExpandableListView.setAdapter(adapter);
			
			setChildItemListener("FKJNPJ_FKJNPJ","FKJNPJ_QT");
			break;
		case XZCFData.XT:
			groupList.add("催告通知书");
			groupList.add("强制执行文书");
			groupList.add("其他");
			
			initData(XZCFData.XT_CGTZS,XZCFData.XT_QZZXWS,XZCFData.XT_QT);
			
			adapter.setData(groupList, childList);
			mExpandableListView.setAdapter(adapter);
			
			setChildItemListener("XT_CGTZS","XT_QZZXWS","XT_QT");
			
			break;
		default:
			break;
		}
		
		// 设置界面标题并填充布局
		SetBaseStyle((RelativeLayout) findViewById(R.id.parentLayout), XZCFData.strs[itemposition]);
		middleLayout.addView(view);
	}
	
	/** 设置 ChildItem 的监听事件 */
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
	
	/** 根据 guid 和下载标志查询并添加到 childList */
	HashMap<String, Object> tempMap = new HashMap<String, Object>();
	public void initData( int... loadMark) {
		for (int i = 0; i < loadMark.length; i++) {
			String sql = "select * from T_Attachment where FK_Unit = '" + loadMark[i] + "' and FK_Id = '" + guid + "'";
			ArrayList<HashMap<String, Object>> arrayList = SqliteUtil.getInstance().queryBySqlReturnArrayListHashMap(sql);
			// 给文件添加后缀
			for (int j = 0; j < arrayList.size(); j++) {
				tempMap = arrayList.get(j);
				tempMap.put("filename", tempMap.get("filename").toString() + tempMap.get("extension").toString());
			}
			childList.add(arrayList);
		}
	}

	/** 判断是下载还是打开附件 */
	public void fujian(String fjname, String folderName) {
		String url = null;
		try {
			url = Global.getGlobalInstance().getSystemurl()+ "/Attach/" + folderName + "/" + java.net.URLEncoder.encode(fjname, "UTF-8");
			String filePathStr = Global.SDCARD_RASK_DATA_PATH + "/Attach/" + folderName + "/" + fjname;
			File file = new File(filePathStr);
			// 如果文件存在打开文件
			if (fjname != null && !fjname.equals("") && file.exists()) {

				DisplayUitl.openfile(filePathStr, XZCFFJActivity.this);
			} else {// 不存在则下载
				downfujian(url, filePathStr);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/** 下载附件 */
	private void downfujian(String fjnameurl, String filePathStr) {
		try {
			DownloadFile downloadFile = new DownloadFile(XZCFFJActivity.this);

			String[] params = { fjnameurl, filePathStr };

			downloadFile.execute(params);
		} catch (Exception e) {
			Toast.makeText(XZCFFJActivity.this, "服务器无附件", Toast.LENGTH_SHORT).show();
		}
	}
	
	@Override
	public void onBackPressed() {
		this.finish();
		super.onBackPressed();
	}
}