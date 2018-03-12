/**
 * 
 */
package com.mapuni.android.taskmanager;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lidroid.xutils.util.OtherUtils;
import com.mapuni.android.base.BaseActivity;
import com.mapuni.android.base.Global;
import com.mapuni.android.base.R;
import com.mapuni.android.base.util.DisplayUitl;
import com.mapuni.android.base.widget.PagingListView;
import com.mapuni.android.business.QYJBXX;
import com.mapuni.android.dataprovider.SqliteUtil;
import com.mapuni.android.enterpriseArchives.AddBusinessActivity;
import com.mapuni.android.enterpriseArchives.EnterpriseArchivesActivitySlide;
import com.mapuni.android.netprovider.Net;
import com.mapuni.android.netprovider.WebServiceProvider;
import com.mapuni.yqydweb.YqydWebActivity;

/**
 * @author SS
 * 
 *         Description
 * 
 *         Incoming parameters
 */
public class SelectAddCompanyActivity extends BaseActivity {

	/** ��ʾ����Ĳ��� */
	private RelativeLayout titleLayout;

	/** ���ҳ���м䲼�ֿɼ���ͼ */
	private LinearLayout middleLayout;

	/** ��ҵ������Ϣҵ���� */
	private QYJBXX qyjbxx;

	/** ��ҵ�б��ѯ���� */
	private HashMap<String, Object> companyCondition;

	/** ��ҵ�б�Ĳ�ѯ���� */
	private View queryView;

	/** ��ҵ���� */
	private ArrayList<HashMap<String, Object>> totalDataList;

	/** ��ҵ�б� ,Ĭ������ */
	private PagingListView companyListview;

	/** ��ҵ�б������� */
	private MyAdapter companyAdapter;

	/** ��䲼�ֵ����� */
	private LayoutInflater layoutInflater;

	/** ��ʾ��ѯ������VIew */
	private View companyView;

	/** ���صײ����� */
	private LinearLayout xczf_bottom_layout;

	/** �ײ�ȷ����Ť */
	private Button confirmBtn;
	/** ��ҳ���ؼ��� */
	private MyOnPageCountChangListener pageCountChangListener;

	/** ��ҵ�б�����ҳ�� */
	private int pagingListCount = 1;
	/** ��ҵ�б������Ƿ�ѡ�� */
	// public Map<Integer, Boolean> isSelected;
	/** ����һҳ�洫������������ */
	private String rwbh;
	HashMap<String, Boolean> record = new HashMap<String, Boolean>();
	/** ��¼��ʼ�����������ҵ�������ı� */
	private final HashMap<String, Boolean> recordList = new HashMap<String, Boolean>();
	private final Handler handler = new Handler() {
		@Override
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 0:
				companyAdapter.notifyDataSetChanged();
				companyListview.setFootViewVisibility(false);
				break;
		
			}
		};
	};

	private String taskId_byk;

	private String rwbh2;
	
	//�������ݿ��ֵ
	private ArrayList<ContentValues> values;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ui_mapuni);
		rwbh = getIntent().getStringExtra("rwbh");
		rwbh2 = getIntent().getStringExtra("rwbh2");
	taskId_byk = getIntent().getStringExtra("taskId");
		initData();
		initEntSearchView();
	}

	private void initData() {
		HashMap<String, Object> conditions = new HashMap<String, Object>();
		conditions.put("TaskId", rwbh);
		ArrayList<HashMap<String, Object>> data = SqliteUtil.getInstance().getList("qyid", conditions, "TaskEnpriLink");
		for (HashMap<String, Object> map : data) {
			record.put(map.get("qyid").toString(), true);
			recordList.put(map.get("qyid").toString(), true);

		}

	}

	/** ��ʼ����ҵ��ѯҳ�� */
	private void initEntSearchView() {
		titleLayout = (RelativeLayout) findViewById(R.id.parentLayout);
		middleLayout = (LinearLayout) findViewById(R.id.middleLayout);
		layoutInflater = LayoutInflater.from(this);
		companyView = layoutInflater.inflate(com.mapuni.android.MobileEnforcement.R.layout.qysitelaw, null);
		xczf_bottom_layout = (LinearLayout) companyView.findViewById(com.mapuni.android.MobileEnforcement.R.id.xczf_bottom_layout);
		confirmBtn = (Button) companyView.findViewById(com.mapuni.android.MobileEnforcement.R.id.confirm_btn);
		xczf_bottom_layout.setVisibility(View.GONE);
		companyListview = (PagingListView) companyView.findViewById(com.mapuni.android.MobileEnforcement.R.id.lv_ls_Ent);
		SetBaseStyle(titleLayout, "��ҵ������Ϣ��ѯ");
		qyjbxx = new QYJBXX();
		companyCondition = new HashMap<String, Object>();
		totalDataList = new ArrayList<HashMap<String, Object>>();
		queryView = qyjbxx.getLiaoNingQueryView(this, companyCondition);
		Button query_imagebutton = (Button) queryView.findViewById(com.mapuni.android.MobileEnforcement.R.id.enforcement_query_imagebutton);
		middleLayout.addView(queryView);
		middleLayout.addView(companyView);
		((View) query_imagebutton.getParent()).setVisibility(View.VISIBLE);// ��ѯ��ť�ɼ�
		query_imagebutton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				totalDataList = qyjbxx.getDataList(companyCondition);
				if (totalDataList.size() > 0) {
					companyListview.setVisibility(View.VISIBLE);
					queryView.setVisibility(View.GONE);
					queryImg.setVisibility(View.GONE);
					xczf_bottom_layout.setVisibility(View.VISIBLE);
				} else {
					Toast.makeText(SelectAddCompanyActivity.this, "û�з�����������ҵ", Toast.LENGTH_SHORT).show();
					// ���������ҵ��ť
					Button addCom = (Button) queryView.findViewById(com.mapuni.android.MobileEnforcement.R.id.addCom);
					((View) addCom.getParent()).setVisibility(View.VISIBLE);// �����ȾԴ��ť�ɼ�
					addCom.setOnClickListener(new View.OnClickListener() {
						public void onClick(View v) {
							// ת�������ҵҳ��
							Intent intAddCom = new Intent(SelectAddCompanyActivity.this, AddBusinessActivity.class);
							startActivity(intAddCom);

						}
					});
					return;
				}
				companyAdapter = new MyAdapter(SelectAddCompanyActivity.this);
				companyListview.setAdapter(companyAdapter);
			}
		});
		companyListview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				String qyidStr = totalDataList.get(arg2).get("guid").toString();
				//Intent intent = new Intent(SelectAddCompanyActivity.this, EnterpriseArchivesActivitySlide.class);
				Intent intent = new Intent(SelectAddCompanyActivity.this, YqydWebActivity.class);
				intent.putExtra("qyid", qyidStr);
				startActivity(intent);
			}
		});
		// ��������ѯ��ť���ɼ�
		queryImg.setVisibility(View.GONE);
		pageCountChangListener = new MyOnPageCountChangListener();
		// ���÷�ҳ����
		companyListview.setOnPageCountChangListener(pageCountChangListener);
		// �ײ�ȷ����ť����¼�
		confirmBtn.setOnClickListener(new OnClickListener() {

		

			@Override
			public void onClick(View arg0) {

				values = new ArrayList<ContentValues>();
				String qyid_str = "";
				Iterator<Entry<String, Boolean>> it = record.entrySet().iterator();
				
				final ArrayList<HashMap<String, String>> list=new ArrayList<HashMap<String,String>>();
				while (it.hasNext()) {
					Map.Entry entry1 = (Map.Entry) it.next();
					String qyid = entry1.getKey().toString();
					qyid_str = qyid_str + qyid + ",";
					if (recordList.containsKey(qyid)) {
						continue;
					}
					ContentValues cv = new ContentValues();
					cv.put("Guid", UUID.randomUUID().toString());
					cv.put("TaskID", rwbh2);
				//		cv.put("TaskID", taskId_byk);
				
					cv.put("QYID", qyid);
				//	cv.put("IsExcute", "0");
					//BYK rwzt
					cv.put("IsExcute", "1");
					values.add(cv);
					//���ѡ�����ҵ
				HashMap<String, String> hash=new HashMap<String, String>();
				hash.put("qyguid", qyid);
				
				list.add(hash);
				}
				if (rwbh == null) {
					Intent intent = new Intent();
					intent.putExtra("qyidStr", qyid_str);
					setResult(100, intent);
				} else {
							//�޸� BYK �ж��Ƿ��ϴ��ɹ�
						if (associatedEnterprises(list, taskId_byk)) {
//
							SqliteUtil.getInstance().insert(values, "TaskEnpriLink");
								
							Toast.makeText(SelectAddCompanyActivity.this, "������ҵ�ɹ�", 0).show();
						}else {
							Toast.makeText(SelectAddCompanyActivity.this, "������ҵʧ��,��������!", 0).show();
					}
				
					
				}
				setResult(100);
				record.clear();
				recordList.clear();
				finish();
			}

		});
	}

	/**
	 * ������ҵ�ӿ�
	 * */
	public Boolean associatedEnterprises( ArrayList<HashMap<String , String>> CompanyAdapterData, String taskid) {
		Boolean resultBoolean = false;
		if (Net.checkURL(Global.getGlobalInstance().getSystemurl())) {
			//String methodName = "AddTaskEntLink";
			String methodName = "AddTaskEntLinkInfo";
			ArrayList<HashMap<String, Object>> params = new ArrayList<HashMap<String, Object>>();
			HashMap<String, Object> param = new HashMap<String, Object>();
			// String[] enterCodes = null;
			StringBuffer enterCodes = new StringBuffer();
			if (CompanyAdapterData != null) {
				// int size = CompanyAdapterData.size();
				// enterCodes = new String[size];

				for (int i = 0; i < CompanyAdapterData.size(); i++) {
					// if (i == 0) {
					// enterCodes.append("[" + "\""
					// + CompanyAdapterData.get(i).get("qyguid")
					// + "\"" + ",");
					// } else if (i!=0&&i == CompanyAdapterData.size() - 1) {
					// enterCodes.append("\""
					// + CompanyAdapterData.get(i).get("qyguid")
					// + "\"" + "]");
					// }else if (i==0&&CompanyAdapterData.size()!=1) {
					// enterCodes.append("[" + "\""
					// + CompanyAdapterData.get(i).get("qyguid")
					// + "\"" + "]");
					// }else {
					// enterCodes.append("\""
					// + CompanyAdapterData.get(i).get("qyguid")
					// + "\"" + ",");
					// }

					if (i == 0 && CompanyAdapterData.size() == 1) {
						enterCodes.append("[" + "\""
								+ CompanyAdapterData.get(i).get("qyguid")
								+ "\"" + "]");
					} else if (i != 0 && i == CompanyAdapterData.size() - 1) {
						enterCodes.append("\""
								+ CompanyAdapterData.get(i).get("qyguid")
								+ "\"" + "]");
					} else if (i == 0) {
						enterCodes.append("[" + "\""
								+ CompanyAdapterData.get(i).get("qyguid")
								+ "\"" + ",");
					} else {
						enterCodes.append("\""
								+ CompanyAdapterData.get(i).get("qyguid")
								+ "\"" + ",");
					}
					// enterCodes[i] =
					// "\""+CompanyAdapterData.get(i).get("qyguid")+"\"";
				}
			}
			
			long tempp1 = Long.valueOf(taskid);
		//	long tempp = Long.valueOf(lawenforcementtask.getTaskId());
			param.put("taskId", tempp1);
			param.put("enterCode", enterCodes.toString());
			param.put("SurveyUnit", Global.getGlobalInstance().getAreaCode());

			params.add(param);

			try {
				Object json = WebServiceProvider.callWebService(
						Global.NAMESPACE, methodName, params, Global
								.getGlobalInstance().getSystemurl()
								+ Global.WEBSERVICE_URL,
						WebServiceProvider.RETURN_BOOLEAN, true);
				if (json != null && !json.equals("")) {

					resultBoolean = Boolean.parseBoolean(json.toString());
//					Message msg=new Message();
//					msg.obj=resultBoolean;
//					msg.what=98;
//					handler.sendMessage(msg);
				} else {
					resultBoolean = false;
					//handler.sendEmptyMessage(99);
				}
			} catch (IOException e) {

				e.printStackTrace();
			}
			// if (resultBoolean) {// ��˳ɹ��������ǰ�û�Ϊֱ�Ӹ����ˣ���ʱ������״̬�Ѿ��ı�Ϊ��ִ����Ҫ����һ��
			// handler.sendEmptyMessage(XIAFA_SUCCESS);
			// } else {
			// handler.sendEmptyMessage(XIAFA_FALI);
			// }

		} /*
		 * else { handler.sendEmptyMessage(NO_NET); return; }
		 */
		return resultBoolean;
	}
	
	private class MyOnPageCountChangListener implements PagingListView.PageCountChangListener {

		@Override
		public void onAddPage(AbsListView view) {
			if (view.getId() == com.mapuni.android.MobileEnforcement.R.id.lv_ls_Ent) {// ��ҵ�б�
				new Thread(new Runnable() {

					@Override
					public void run() {

						qyjbxx.setListScrolltimes(++pagingListCount);
						ArrayList<HashMap<String, Object>> newData = null;
						if (companyCondition != null) {
							newData = qyjbxx.getDataList(companyCondition);
						} else {
							newData = qyjbxx.getDataList();
						}
						if (newData.size() < Global.getGlobalInstance().getListNumber()) {
							companyListview.setIsCompleted(true);
							handler.sendEmptyMessage(2);
							return;
						}
						totalDataList.addAll(newData);
						handler.sendEmptyMessage(0);
					}
				}).start();
			}
		}
	}

	/**
	 * FileName: SiteLawActivity.java Description:��ҵ�б�����������
	 * 
	 * @author Administrator
	 * @Version 1.3.4
	 * @Copyright �п���ͼ���¿Ƽ����޹�˾<br>
	 *            Create at: 2012-12-4 ����10:59:42
	 */
	private class MyAdapter extends BaseAdapter {

		private int textSize = 20;

		private MyAdapter(Context context) {

			textSize = Integer.parseInt(String.valueOf(DisplayUitl.getSettingValue(context, DisplayUitl.TEXTSIZE, 20)));
		}

		@Override
		public int getCount() {
			return totalDataList.size();
		}

		@Override
		public Object getItem(int position) {
			return totalDataList.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			// if (convertView == null) {
			convertView = layoutInflater.inflate(com.mapuni.android.MobileEnforcement.R.layout.qylistitem, null);
			// }

			ImageView img = (ImageView) convertView.findViewById(com.mapuni.android.MobileEnforcement.R.id.qylistitem_left_image);
			TextView textView = (TextView) convertView.findViewById(com.mapuni.android.MobileEnforcement.R.id.qylistitem_text);
			CheckBox qychecked = (CheckBox) convertView.findViewById(com.mapuni.android.MobileEnforcement.R.id.qy_check);

			textView.setSelected(true);
			textView.setText((String) totalDataList.get(position).get("qymc"));
			final String qyguid = totalDataList.get(position).get("guid").toString();
			textView.setTag(qyguid);// ������ҵGUID
			textView.setTextSize(textSize);
			img.setImageResource(com.mapuni.android.MobileEnforcement.R.drawable.xczf_gcqy);
			if (record.containsKey(qyguid)) {
				qychecked.setChecked(true);
			} else {
				qychecked.setChecked(false);
			}
			if (recordList.containsKey(qyguid)) {
				//��ʱȡ������ɾ��������ҵ
			//	Toast.makeText(SelectAddCompanyActivity.this, "ȡ����ҵ�뷵��ǰҳ,����ɾ����", Toast.LENGTH_SHORT).show();
				qychecked.setClickable(false);
			}
			qychecked.setOnCheckedChangeListener(new OnCheckedChangeListener() {

				@Override
				public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
					// TODO Auto-generated method stub
					if (isChecked) {
						record.put(qyguid, true);
					} else {
						record.remove(qyguid);

					}
				}

			});
			return convertView;
		}

	}

}
