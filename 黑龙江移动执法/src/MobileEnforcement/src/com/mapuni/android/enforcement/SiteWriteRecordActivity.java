package com.mapuni.android.enforcement;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.UUID;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mapuni.android.MobileEnforcement.R;
import com.mapuni.android.base.BaseActivity;
import com.mapuni.android.base.Global;
import com.mapuni.android.business.RWXX;
import com.mapuni.android.dataprovider.SqliteUtil;

/**
 * FileName: SiteRecordActivity.java Description:�ֳ���¼ ��Ҫ����������
 * 
 * @author
 * @Version 1.3.4
 * @Copyright �п���ͼ���¿Ƽ����޹�˾ Create at: 2012-12-3 ����3:56:55
 */
public class SiteWriteRecordActivity extends BaseActivity {

	/** ��ҳ�沼�� */
	private RelativeLayout fatherRelativeLayout;
	private LinearLayout middleLayout;
	/** ������ */
	private String rwbh = "";
	private String qyid;
	private ListView mListView;
	private ArrayList<HashMap<String, Object>> dataList,kc_dataList,xc_dataList,lz_dataList;
	private MyListViewAdapter adapter;
	private final String BaseWord = Global.SDCARD_RASK_DATA_PATH + "data/task/";
	String billid_byk="";
	private boolean isSearch;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		/** ��Ļ״̬ */
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.ui_mapuni);

		fatherRelativeLayout = (RelativeLayout) findViewById(R.id.parentLayout);
		middleLayout = (LinearLayout) findViewById(R.id.middleLayout);
		SetBaseStyle(fatherRelativeLayout, "�ֳ���¼");
		setTitleLayoutVisiable(true);

		rwbh = getIntent().getStringExtra("rwbh");
		qyid = getIntent().getStringExtra("qyid");
		isSearch = getIntent().getBooleanExtra("isSearch", false);
		mListView = new ListView(this);
		mListView.setCacheColorHint(Color.TRANSPARENT);

		dataList = getData();
		adapter = new MyListViewAdapter(this, dataList);
		mListView.setAdapter(adapter);
		mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
				String fileName = (String) dataList.get(position).get("name");
				String codeStr = dataList.get(position).get("code").toString();
				Intent intent;
				if ("�ֳ�����¼��".equals(fileName)) {
					intent = new Intent(SiteWriteRecordActivity.this,TaskSiteEnforcementActivity.class);
					if (isSearch) {
						intent.putExtra("isSearch",isSearch);
					}else{
						addBL_New(fileName);
					}
						
			
					if (qyid != "" && qyid != null && !qyid.equals("")) {
						intent.putExtra("qyid", qyid);
						intent.putExtra("rwbh", rwbh);
						
						
						if(xc_dataList!=null&&xc_dataList.size() > 0) {
							intent.putExtra("blname",xc_dataList.get(0).get("templetname").toString());
						} else {
							intent.putExtra("blname","");
						}
						Bundle bundle = new Bundle();
						if(xc_dataList!=null&&xc_dataList.size() > 0) {
							bundle.putString("guid", xc_dataList.get(0).get("billid").toString());
							bundle.putString("id", xc_dataList.get(0).get("id").toString());//id,���Ե�billid�쳣Ϊ�յ�ʱ�����exelawstemplet��
						} else {
							bundle.putString("guid", "");
							bundle.putString("id", "");//id,���Ե�billid�쳣Ϊ�յ�ʱ�����exelawstemplet��
						}
						bundle.putString("fn", fileName);
						bundle.putString("code", codeStr);
						intent.putExtras(bundle);
						startActivity(intent);
						return;
					}
				} else if ("�ֳ���飨���죩��¼".equals(fileName)) {
					intent = new Intent(SiteWriteRecordActivity.this,TaskSiteEnforcementActivity.class);
//					addBL(fileName + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
					
					
					if (isSearch) {
						intent.putExtra("isSearch",isSearch);
					}else{
						addBL(fileName);
					}
					if (qyid != "" && qyid != null && !qyid.equals("")) {
						intent.putExtra("qyid", qyid);
						intent.putExtra("rwbh", rwbh);
						if(kc_dataList!=null&&kc_dataList.size() > 0) {
							intent.putExtra("blname",kc_dataList.get(0).get("templetname").toString());
						} else {
							intent.putExtra("blname","");
						}
						Bundle bundle = new Bundle();
						if(kc_dataList!=null&&kc_dataList.size() > 0) {
							bundle.putString("guid", kc_dataList.get(0).get("billid").toString());
							bundle.putString("id", kc_dataList.get(0).get("id").toString());//id,���Ե�billid�쳣Ϊ�յ�ʱ�����exelawstemplet��
						} else {
							bundle.putString("guid", "");
							bundle.putString("id", "");//id,���Ե�billid�쳣Ϊ�յ�ʱ�����exelawstemplet��
						}
						bundle.putString("fn", fileName);
						bundle.putString("code", codeStr);
						intent.putExtras(bundle);
						startActivity(intent);
						return;
					}
				}else if ("�����ල��".equals(fileName)) {
					//TODO  ���������ල���ĵ���¼�
					if (TextUtils.isEmpty(billid_byk)) {
						billid_byk = UUID.randomUUID().toString();
					}
					
					intent = new Intent(SiteWriteRecordActivity.this,SuperviseCardActivity.class);
					if (isSearch) {
						intent.putExtra("isSearch",isSearch);
					}else{
						addLZ(fileName,billid_byk);
					}
					if (qyid != "" && qyid != null && !qyid.equals("")) {
						
 
						intent.putExtra("qyid", qyid);
						intent.putExtra("rwbh", rwbh);
						intent.putExtra("billid", billid_byk);
						Bundle bundle = new Bundle();
						if(lz_dataList!=null&&lz_dataList.size() > 0) {
							bundle.putString("guid", lz_dataList.get(0).get("billid").toString());
							bundle.putString("id", lz_dataList.get(0).get("id").toString());//id,���Ե�billid�쳣Ϊ�յ�ʱ�����exelawstemplet��
						} else {
							bundle.putString("guid", "");
							bundle.putString("id", "");//id,���Ե�billid�쳣Ϊ�յ�ʱ�����exelawstemplet��
						}
						
						bundle.putString("fn", fileName);
						bundle.putString("code", codeStr);
						intent.putExtras(bundle);
						startActivity(intent);
						
						return;
					}
					
					
				
				} else {
					intent= new Intent(SiteWriteRecordActivity.this, SiteWriteRecordAddMoreActivity.class);					
				}
				intent.putExtra("qyid", qyid);
				intent.putExtra("isSearch", isSearch);
				intent.putExtra("rwbh", rwbh);
				Bundle bundle = new Bundle();
				bundle.putString("fn", fileName);
				bundle.putString("code", codeStr);
				intent.putExtras(bundle);
				// SiteWriteRecordActivity.this.setResult(0, intent);
				// SiteWriteRecordActivity.this.finish();
				startActivity(intent);
			}
		});

		middleLayout.addView(mListView);
	}

	@Override
	protected void onResume() {
		adapter.notifyDataSetChanged();
		super.onResume();

	}

	/** �������̷��ؼ��¼� */
	// @Override
	// public boolean onKeyDown(int keyCode, KeyEvent event) {
	// if (keyCode == KeyEvent.KEYCODE_BACK) {
	// Intent intent = getIntent();
	// Bundle bundle = new Bundle();
	// bundle.putString("fn", "");
	// bundle.putString("code", "");
	// intent.putExtras(bundle);
	// SiteWriteRecordActivity.this.setResult(0, intent);
	// SiteWriteRecordActivity.this.finish();
	// }
	// return false;
	// }

	/**
	 * FileName: SiteWriteRecordActivity.java Description:�ֳ���¼�б�������
	 * 
	 * @author Administrator
	 * @Version 1.3.4
	 * @Copyright �п���ͼ���¿Ƽ����޹�˾<br>
	 *            Create at: 2012-12-4 ����11:16:12
	 */
	private class MyListViewAdapter extends BaseAdapter {
		ArrayList<HashMap<String, Object>> data;
		Context context;

		public MyListViewAdapter(Context context, ArrayList<HashMap<String, Object>> data) {
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
			LayoutInflater mInflater = LayoutInflater.from(context);
			LinearLayout layout = (LinearLayout) mInflater.inflate(R.layout.listitem, null);

			ImageView headImageView = (ImageView) layout.findViewById(R.id.listitem_left_image);
			headImageView.setImageResource(R.drawable.icon_left_not_checked);

			TextView mTextView = (TextView) layout.findViewById(R.id.listitem_text);
			String fileName = (String) data.get(position).get("name");
			mTextView.setText(fileName);
			if ("�ֳ�����¼��".equals(fileName)) {
				String xcSql = "select CZHJWT,SCQK_SCZK,SCQK_YLYL,SCQK_CPCL,HBZD_WAQYS,HBZD_WSPJS,JFQK_FS,JFQK_FQ,JFQK_FZ,JFQK_ZS,WRZL_FS,WRZL_FQ,WRZL_WRW,WRZL_GTFW,WRZL_ZS,WRZL_XWS,WRW_FS_PFQX,WRW_FS_YZND,WRW_FS_PFBZ,WRW_FS_CBYY,WRW_FS_ZXJC,WRW_FS_CBYY1,WRW_FS_JDJC,WRW_FS_ZXJCCB,WRW_FQ_KHSD,WRW_FQ_YZND,WRW_FQ_PFBZ,WRW_FQ_CBYY,WRW_FQ_ZXJC,WRW_FQ_CBYY1,WRW_FQ_JDJC,WRW_FQ_ZXJCCB,WRW_GTFW_FWCZ,WRW_GTFW_FWZC,WRW_GTFW_FWZY,WRW_ZS_GNQY,WRW_ZS_PFBZ,WRW_ZS_JCCB,WRW_ZS_CBYY from YDZF_SiteEnvironmentMonitorRecord where taskid = '"
						+ rwbh + "' and entid = '" + qyid + "'";
				ArrayList<HashMap<String, Object>> jbqk2 = new ArrayList<HashMap<String, Object>>();
				jbqk2 = SqliteUtil.getInstance().queryBySqlReturnArrayListHashMap(xcSql);
				if (jbqk2.size() > 0) {
					headImageView.setImageResource(R.drawable.icon_left_checked);
				}

			} else if ("����ѯ�ʱ�¼".equals(fileName)) {
				String xwSql = "select * from ExeLawsTemplet where taskid = '" + rwbh + "' and enterid = '" + qyid + "' and templettype = 'XWBL'";
				String survey = SqliteUtil.getInstance().getDepidByDepName(xwSql);
				if (survey != null && !survey.equals("")) {
					headImageView.setImageResource(R.drawable.icon_left_checked);
				}
			} else if ("�ֳ���飨���죩��¼".equals(fileName)) {
				String kcSql = "select * from ExeLawsTemplet where taskid = '" + rwbh + "' and enterid = '" + qyid + "' and templettype = 'KCBL'";
				String survey = SqliteUtil.getInstance().getDepidByDepName(kcSql);
				if (survey != null && !survey.equals("")) {
					headImageView.setImageResource(R.drawable.icon_left_checked);
				}
			} else if ("�������֪ͨ��".equals(fileName)) {
				String jsSql = "select BreakBehavior from Survey_JSTZS where taskid = '" + rwbh + "' and entid = '" + qyid + "'";
				String Behavior = SqliteUtil.getInstance().getDepidByDepName(jsSql);
				if (Behavior != null && !Behavior.equals("")) {
					headImageView.setImageResource(R.drawable.icon_left_checked);
				}
			}else if("�����ල��".equals(fileName)){
				//TODO �����ѯ
				String kcSql = "select * from ExeLawsTemplet where taskid = '" + rwbh + "' and enterid = '" + qyid + "' and templettype = 'LZJDK'";
				String survey = SqliteUtil.getInstance().getDepidByDepName(kcSql);
				if (survey != null && !survey.equals("")) {
					headImageView.setImageResource(R.drawable.icon_left_checked);
				}
			}

				
			
			/** �ж��ļ��Ƿ���ڣ��������� */
			// String taskPath = BaseWord + rwbh;
			// String targetFilePath = taskPath + "/RaskFile/" + fileName +
			// ".doc";
			// if (new File(targetFilePath).exists()) {
			// headImageView.setImageResource(R.drawable.icon_left_checked);
			// }

			ImageView tailImageView = (ImageView) layout.findViewById(R.id.listitem_image);
			tailImageView.setImageResource(R.drawable.icon_arrow_yellow);

			return layout;
		}
	}
	
	/**����ֳ�����¼
	 * @param name ��¼����
	 * */
	private void addBL_New(String name) {
		String codeStr = "1";
		SqliteUtil sqliteUtil = SqliteUtil.getInstance();
		RWXX rwxx = new RWXX();
		String rwzt = rwxx.getTaskid(rwbh);
		
		if (codeStr.equals("0")) {
//			dataList = getXWData();
		} else if (codeStr.equals("1")) {
			xc_dataList = getXCData();
		} else if (codeStr.equals("3")) {
			
//			dataList = getJCData();
		}
		if(xc_dataList.size() > 0) {
			return;
		}
		
		if (rwzt.equals("003") || rwzt.equals("005")||rwzt.equals("000")||"".equals(rwzt)||rwzt.equals("15")||rwzt.equals("16")||rwzt.equals("007")) {
			
		} else {
			xc_dataList = new ArrayList<HashMap<String,Object>>();
			return;
		}
		
		ContentValues contentValues = new ContentValues();
		String GUID = UUID.randomUUID().toString();
		contentValues.put("guid", GUID);// ID
		contentValues.put("taskid", rwbh);// ����ID
		if (codeStr.equals("0")) {
			contentValues.put("surveyentcode", qyid);// ��ȾԴ(��ҵ)ID
			sqliteUtil.insert(contentValues, "T_ZFWS_XWBL");
		} else if (codeStr.equals("1")) {
			contentValues.put("entcode", qyid);// ��ȾԴ(��ҵ)ID
			sqliteUtil.insert(contentValues, "T_ZFWS_JCJL");
		} else if (codeStr.equals("3")) {
			contentValues.put("entcode", qyid);// ��ȾԴ(��ҵ)ID
			sqliteUtil.insert(contentValues, "T_ZFWS_JCJL");
		}
		contentValues = new ContentValues();
		contentValues.put("id", UUID.randomUUID().toString());
		contentValues.put("taskid", rwbh);// ����ID
		contentValues.put("enterid", qyid);// ��ȾԴ(��ҵ)ID
		contentValues.put("updatetime", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new java.util.Date()));
		contentValues.put("billid", GUID);
		contentValues.put("templetname", name);
		if (codeStr.equals("0")) {
			
		} else if (codeStr.equals("1")) {
			contentValues.put("templettype", "JCJL");
		} else if (codeStr.equals("3")) {
		}
		sqliteUtil.insert(contentValues, "ExeLawsTemplet");
		if (codeStr.equals("0")) {
//			dataList = getXWData();
		} else if (codeStr.equals("1")) {
			xc_dataList = getXCData();
		} else if (codeStr.equals("3")) {
//			dataList = getJCData();
		}
	}
	
	/**
	 * ��ȡ�ֳ�����¼����
	 * 
	 * @return
	 */
	private ArrayList<HashMap<String, Object>> getXCData() {

		ArrayList<HashMap<String, Object>> data = new ArrayList<HashMap<String, Object>>();

		/** ��ѯ����¼���е����� */
		data = SqliteUtil.getInstance().queryBySqlReturnArrayListHashMap(
				"select * from ExeLawsTemplet where taskid = '" + rwbh + "' and enterid = '" + qyid
						+ "' and templettype = 'JCJL' order by updatetime desc");
		return data;
	}
	
	/**��ӿ����¼
	 * @param name ��¼����
	 * */
	private void addBL(String name) {
		String codeStr = "1";
		SqliteUtil sqliteUtil = SqliteUtil.getInstance();
		RWXX rwxx = new RWXX();
		String rwzt = rwxx.getTaskid(rwbh);
		
		if (codeStr.equals("0")) {
		} else if (codeStr.equals("1")) {
			kc_dataList = getKCData();
		} else if (codeStr.equals("3")) {
		}
		if(kc_dataList.size() > 0) {
			return;
		}
		
		if (rwzt.equals("003") || rwzt.equals("005")||"".equals(rwzt)||rwzt.equals("15")||rwzt.equals("16")||rwzt.equals("007")) {
			
		} else {
			kc_dataList = new ArrayList<HashMap<String,Object>>();
			return;
		}
		
		ContentValues contentValues = new ContentValues();
		String GUID = UUID.randomUUID().toString();
		contentValues.put("guid", GUID);// ID
		contentValues.put("taskid", rwbh);// ����ID
		if (codeStr.equals("0")) {
		} else if (codeStr.equals("1")) {
			contentValues.put("entcode", qyid);// ��ȾԴ(��ҵ)ID
			sqliteUtil.insert(contentValues, "T_ZFWS_KCBL");
		} else if (codeStr.equals("3")) {
//			contentValues.put("entcode", qyid);// ��ȾԴ(��ҵ)ID
//			sqliteUtil.insert(contentValues, "T_ZFWS_JCJL");
		}
		contentValues = new ContentValues();
		contentValues.put("id", UUID.randomUUID().toString());
		contentValues.put("taskid", rwbh);// ����ID
		contentValues.put("enterid", qyid);// ��ȾԴ(��ҵ)ID
		contentValues.put("updatetime", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new java.util.Date()));
		contentValues.put("billid", GUID);
		contentValues.put("templetname", name);
		if (codeStr.equals("0")) {
			
		} else if (codeStr.equals("1")) {
			contentValues.put("templettype", "KCBL");
		} else if (codeStr.equals("3")) {
		}
		sqliteUtil.insert(contentValues, "ExeLawsTemplet");
		if (codeStr.equals("0")) {
		} else if (codeStr.equals("1")) {
			kc_dataList = getKCData();
		} else if (codeStr.equals("3")) {
		}
	}
	
	
	
	/**��������ල��
	 * @param name ����
	 * @param gUID 
	 * */
	private void addLZ(String name, String GUID) {
		String codeStr = "1";
		SqliteUtil sqliteUtil = SqliteUtil.getInstance();
		RWXX rwxx = new RWXX();
		String rwzt = rwxx.getTaskid(rwbh);
		
		if (codeStr.equals("0")) {
		} else if (codeStr.equals("1")) {
			lz_dataList = getLZData();
		} else if (codeStr.equals("3")) {
		}
		if(lz_dataList.size() > 0) {
			return;
		}
			
		if (rwzt.equals("003") || rwzt.equals("005")||rwzt.equals("000")||"".equals(rwzt)||rwzt.equals("15")||rwzt.equals("16")||rwzt.equals("007")) {
			
		} else {
			lz_dataList = new ArrayList<HashMap<String,Object>>();
			return;
		}
		
		ContentValues contentValues = new ContentValues();
		//contentValues.put("guid", GUID);// ID
		//contentValues.put("taskid", rwbh);// ����ID
		if (codeStr.equals("0")) {
		} else if (codeStr.equals("1")) {
			contentValues.put("billcode", GUID);// ��ȾԴ(��ҵ)ID
			long insert = sqliteUtil.insert(contentValues, "Supervision");
		} else if (codeStr.equals("3")) {
		}
		ContentValues contentValues2 = new ContentValues();
		contentValues2.put("id", UUID.randomUUID().toString());
		contentValues2.put("taskid", rwbh);// ����ID
		contentValues2.put("enterid", qyid);// ��ȾԴ(��ҵ)ID
		contentValues2.put("updatetime", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new java.util.Date()));
		contentValues2.put("billid", GUID);
		contentValues2.put("templetname", name);
		if (codeStr.equals("0")) {
			
		} else if (codeStr.equals("1")) {
			contentValues2.put("templettype", "LZJDK");
		} else if (codeStr.equals("3")) {
		}
		long insert = sqliteUtil.insert(contentValues2, "ExeLawsTemplet");
		if (codeStr.equals("0")) {
		} else if (codeStr.equals("1")) {
			lz_dataList = getLZData();
		} else if (codeStr.equals("3")) {
		}
	}
	
	/**
	 * ��ȡ�����¼����
	 * 
	 * @return
	 */
	private ArrayList<HashMap<String, Object>> getKCData() {

		ArrayList<HashMap<String, Object>> data = new ArrayList<HashMap<String, Object>>();

		/** ��ѯ����¼���е����� */
		data = SqliteUtil.getInstance().queryBySqlReturnArrayListHashMap(
				"select * from ExeLawsTemplet where taskid = '" + rwbh + "' and enterid = '" + qyid
						+ "' and templettype = 'KCBL' order by updatetime desc");
		return data;
	}

	
	/**
	 * ��ȡ�����ල
	 * 
	 * @return
	 */
	private ArrayList<HashMap<String, Object>> getLZData() {

		ArrayList<HashMap<String, Object>> data = new ArrayList<HashMap<String, Object>>();

		/** ��ѯ����¼���е����� */
		data = SqliteUtil.getInstance().queryBySqlReturnArrayListHashMap(
				"select * from ExeLawsTemplet where taskid = '" + rwbh + "' and enterid = '" + qyid
						+ "' and templettype = 'LZJDK' order by updatetime desc");
		return data;
	}
	/**
	 * ��ȡ����
	 * ��������Ʊ�¼������
	 * @return
	 */
	private ArrayList<HashMap<String, Object>> getData() {

		ArrayList<HashMap<String, Object>> data = new ArrayList<HashMap<String, Object>>();

		/** ��ѯ����¼���е����� */
		data = SqliteUtil.getInstance().queryBySqlReturnArrayListHashMap("select code,name from T_YDZF_PublicCode");
		for(int i = 0;i<data.size();i++){
			if(data.get(i).get("name").equals("�������֪ͨ��")){//||data.get(i).get("name").equals("�ֳ���������¼")
				data.remove(i);
			}
		}
		return data;
	}

}
