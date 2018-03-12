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

	/** 显示标题的布局 */
	private RelativeLayout titleLayout;

	/** 填充页面中间布局可见视图 */
	private LinearLayout middleLayout;

	/** 企业基本信息业务类 */
	private QYJBXX qyjbxx;

	/** 企业列表查询条件 */
	private HashMap<String, Object> companyCondition;

	/** 企业列表的查询布局 */
	private View queryView;

	/** 企业数据 */
	private ArrayList<HashMap<String, Object>> totalDataList;

	/** 企业列表 ,默认隐藏 */
	private PagingListView companyListview;

	/** 企业列表适配器 */
	private MyAdapter companyAdapter;

	/** 填充布局的容器 */
	private LayoutInflater layoutInflater;

	/** 显示查询条件的VIew */
	private View companyView;

	/** 隐藏底部布局 */
	private LinearLayout xczf_bottom_layout;

	/** 底部确定按扭 */
	private Button confirmBtn;
	/** 分页加载监听 */
	private MyOnPageCountChangListener pageCountChangListener;

	/** 企业列表数据页数 */
	private int pagingListCount = 1;
	/** 企业列表数据是否被选中 */
	// public Map<Integer, Boolean> isSelected;
	/** 从上一页面传过来的任务编号 */
	private String rwbh;
	HashMap<String, Boolean> record = new HashMap<String, Boolean>();
	/** 记录初始任务相关联企业，不做改变 */
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
	
	//插入数据库的值
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

	/** 初始化企业查询页面 */
	private void initEntSearchView() {
		titleLayout = (RelativeLayout) findViewById(R.id.parentLayout);
		middleLayout = (LinearLayout) findViewById(R.id.middleLayout);
		layoutInflater = LayoutInflater.from(this);
		companyView = layoutInflater.inflate(com.mapuni.android.MobileEnforcement.R.layout.qysitelaw, null);
		xczf_bottom_layout = (LinearLayout) companyView.findViewById(com.mapuni.android.MobileEnforcement.R.id.xczf_bottom_layout);
		confirmBtn = (Button) companyView.findViewById(com.mapuni.android.MobileEnforcement.R.id.confirm_btn);
		xczf_bottom_layout.setVisibility(View.GONE);
		companyListview = (PagingListView) companyView.findViewById(com.mapuni.android.MobileEnforcement.R.id.lv_ls_Ent);
		SetBaseStyle(titleLayout, "企业基本信息查询");
		qyjbxx = new QYJBXX();
		companyCondition = new HashMap<String, Object>();
		totalDataList = new ArrayList<HashMap<String, Object>>();
		queryView = qyjbxx.getLiaoNingQueryView(this, companyCondition);
		Button query_imagebutton = (Button) queryView.findViewById(com.mapuni.android.MobileEnforcement.R.id.enforcement_query_imagebutton);
		middleLayout.addView(queryView);
		middleLayout.addView(companyView);
		((View) query_imagebutton.getParent()).setVisibility(View.VISIBLE);// 查询按钮可见
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
					Toast.makeText(SelectAddCompanyActivity.this, "没有符合条件的企业", Toast.LENGTH_SHORT).show();
					// 增加添加企业按钮
					Button addCom = (Button) queryView.findViewById(com.mapuni.android.MobileEnforcement.R.id.addCom);
					((View) addCom.getParent()).setVisibility(View.VISIBLE);// 添加污染源按钮可见
					addCom.setOnClickListener(new View.OnClickListener() {
						public void onClick(View v) {
							// 转到添加企业页面
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
		// 标题栏查询按钮不可见
		queryImg.setVisibility(View.GONE);
		pageCountChangListener = new MyOnPageCountChangListener();
		// 设置分页加载
		companyListview.setOnPageCountChangListener(pageCountChangListener);
		// 底部确定按钮点击事件
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
					//添加选择的企业
				HashMap<String, String> hash=new HashMap<String, String>();
				hash.put("qyguid", qyid);
				
				list.add(hash);
				}
				if (rwbh == null) {
					Intent intent = new Intent();
					intent.putExtra("qyidStr", qyid_str);
					setResult(100, intent);
				} else {
							//修改 BYK 判断是否上传成功
						if (associatedEnterprises(list, taskId_byk)) {
//
							SqliteUtil.getInstance().insert(values, "TaskEnpriLink");
								
							Toast.makeText(SelectAddCompanyActivity.this, "关联企业成功", 0).show();
						}else {
							Toast.makeText(SelectAddCompanyActivity.this, "关联企业失败,请检查网络!", 0).show();
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
	 * 关联企业接口
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
			// if (resultBoolean) {// 审核成功后如果当前用户为直接负责人，此时该任务状态已经改变为待执行需要更新一下
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
			if (view.getId() == com.mapuni.android.MobileEnforcement.R.id.lv_ls_Ent) {// 企业列表
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
	 * FileName: SiteLawActivity.java Description:企业列表数据适配器
	 * 
	 * @author Administrator
	 * @Version 1.3.4
	 * @Copyright 中科宇图天下科技有限公司<br>
	 *            Create at: 2012-12-4 上午10:59:42
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
			textView.setTag(qyguid);// 设置企业GUID
			textView.setTextSize(textSize);
			img.setImageResource(com.mapuni.android.MobileEnforcement.R.drawable.xczf_gcqy);
			if (record.containsKey(qyguid)) {
				qychecked.setChecked(true);
			} else {
				qychecked.setChecked(false);
			}
			if (recordList.containsKey(qyguid)) {
				//暂时取消长按删除关联企业
			//	Toast.makeText(SelectAddCompanyActivity.this, "取消企业请返回前页,长按删除！", Toast.LENGTH_SHORT).show();
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
