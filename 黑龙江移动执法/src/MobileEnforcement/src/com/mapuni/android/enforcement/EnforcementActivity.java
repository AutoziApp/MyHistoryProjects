package com.mapuni.android.enforcement;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mapuni.android.base.BaseActivity;
import com.mapuni.android.base.Global;
import com.mapuni.android.base.R;
import com.mapuni.android.base.business.DataSyncModel;
import com.mapuni.android.base.controls.loading.YutuLoading;
import com.mapuni.android.base.util.DisplayUitl;
import com.mapuni.android.base.widget.PagingListView;
import com.mapuni.android.business.QYJBXX;
import com.mapuni.android.business.RWXX;
import com.mapuni.android.dataprovider.SqliteUtil;
import com.mapuni.android.enforcement.EnforcementModel.EnforcementRecordListAdapter;
import com.mapuni.android.enterpriseArchives.AddBusinessActivity;
import com.mapuni.android.netprovider.Net;

/**
 * �°��ֳ�ִ��ҳ��
 * 
 * @author wanglg
 * 
 */
public class EnforcementActivity extends BaseActivity {

	private final String TAG = "EnforcementActivity";
	private String companyNameSelected;
	RelativeLayout titleLayout;
	LinearLayout middleLayout;
	LayoutInflater layoutInflater;
	/* ����ѡ�� */
	RadioGroup radioGroup;
	/** page ��һ��view */
	View companyView;
	// View enforcementRecord;
	LinearLayout viewPagerLayout;
	ViewPager viewPager;
	/** ����ͼƬ��� */
	private int bmpW;
	/** ����ͼƬƫ���� */
	private int offset = 0;
	/** ��ǰҳ����� */
	private int currIndex = 0;
	private YutuLoading yutuLoading;
	/** ����Ҫ������ҳ����ӵ����list�� */
	private List<View> viewList;
	/** ��ҵ�б� ,Ĭ������ */
	PagingListView companyListview;
	/** ��ҵ�б�ĸ�view */
	LinearLayout companyParentView;
	/** ��ҵ�б�Ĳ�ѯ���� */
	View queryView;

	/** ��ҵ�б�����ҳ�� */
	private int pagingListCount = 1;
	/** ִ����¼�б� */
	PagingListView enforcementRecordListView;
//	LinearLayout xczf_bottom_layout;
	/** ִ����¼����ҳ�� */
	private int enforcementRecordCount = 1;
	/** ��ִͬ���� */
	CheckBox enforcementOfficer;
	/** ִ�� */
//	Button enforcementButton;
//	/** ���ذ�ť */
//	Button backButton;
	/** ��ѡ��ҵ���� */
	EditText companyName;

	QYJBXX qyjbxx;
	/** �ֳ�ִ��ģ�� */
	EnforcementModel enforcementModel;

	/**
	 * ��ҵ�б�������
	 */
	private MyAdapter companyAdapter;
	/**
	 * ִ����¼������
	 */
	private EnforcementRecordListAdapter aecordAdapter;
	/**
	 * ��ҵ�б��ѯ����
	 */
	private HashMap<String, Object> companyCondition;
	/**
	 * ִ����¼��ѯ����
	 */
	private HashMap<String, Object> aecordCondition;
	/** ִ����ԱID���ݼ� */
	private StringBuffer sbLawManUsersId = new StringBuffer(Global.getGlobalInstance().getUserid());
	/** ѡ����ҵ����ҵGUID */
	private String QYGUID = "";
	/** �Ƿ������ֳ�ִ�������¼ */
	private Boolean ISADDTASK = false;
	/**
	 * ��ҳ���سɹ�ʱ������Ϣ���صײ��ȴ���
	 */
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 0:
				companyAdapter.notifyDataSetChanged();
				break;
			case 1:
				aecordAdapter.AddValue((List<HashMap<String, Object>>) msg.obj);
				break;
			case 2:
				if (msg.obj != null) {
					aecordAdapter.AddValue((List<HashMap<String, Object>>) msg.obj);
				} else {
					companyAdapter.notifyDataSetChanged();
				}
				Toast.makeText(EnforcementActivity.this, "ȫ�����ݼ������", Toast.LENGTH_SHORT).show();
				break;
			default:
				break;
			}

		};
	};

	/** ��ҵ���� */
	private ArrayList<HashMap<String, Object>> totalDataList = new ArrayList<HashMap<String, Object>>();
	private ArrayList<HashMap<String, Object>> enforcementList;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ui_mapuni);
		findViewById(R.id.ui_mapuni_divider).setVisibility(View.GONE);
		titleLayout = (RelativeLayout) findViewById(R.id.parentLayout);
		qyjbxx = new QYJBXX();
		SetBaseStyle(titleLayout, "�ֳ�ִ��");
		initView();
		initQueryView();
		initviewBindData();
	}

	@Override
	protected void onRestart() {
		sbLawManUsersId = new StringBuffer(Global.getGlobalInstance().getUserid());// ����ִ����id
		super.onRestart();
	}

	@Override
	protected void onResume() {
		super.onResume();
		reflush();
	}

	/**
	 * ���ؼ������ݲ����ü���
	 */
	public void initviewBindData() {
		MyOnPageCountChangListener pageCountChangListener = new MyOnPageCountChangListener();
		companyListview.setOnPageCountChangListener(pageCountChangListener);// ���÷�ҳ����
		MyOnItemClickListener myOnItemClickListener = new MyOnItemClickListener();
		enforcementOfficer.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

				if (isChecked) {
					if (QYGUID.equals("")) {
						enforcementOfficer.setChecked(false);
						Toast.makeText(EnforcementActivity.this, "����ѡ����ҵ", Toast.LENGTH_SHORT).show();
						return;
					}
					final StringBuffer reserveSb = sbLawManUsersId;// ���ã�Ϊ��������ݣ�ȷ��ʱΪsb��ֵ
					AlertDialog.Builder ab = enforcementModel.showCommonLawPeople(EnforcementActivity.this, layoutInflater, reserveSb);

					ab.setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							if (DisplayUitl.isFastDoubleClick()) {
								return;
							}
							sbLawManUsersId = reserveSb;
							if (QYGUID.equals("")) {
								Toast.makeText(EnforcementActivity.this, "����ѡ����ҵ", Toast.LENGTH_SHORT).show();
								return;
							}
							String RWBH = enforcementModel.createOneEnforcementTask(QYGUID, companyName.getText().toString(), sbLawManUsersId.toString());
							if (!RWBH.equals("")) {
								ISADDTASK = true;// ���һ�����񷵻�ˢ��ִ����¼
								Intent intent = new Intent(EnforcementActivity.this, QdjcnlActivity.class);

								intent.putExtra("qyid", QYGUID);
								intent.putExtra("rwbh", RWBH);
								RWXX rwxx = new RWXX();
								HashMap<String, Object> conditions = new HashMap<String, Object>();
								conditions.put("rwbh", RWBH);
								String guid = SqliteUtil.getInstance().getList("guid", conditions, "T_YDZF_RWXX").get(0).get("guid").toString();
								rwxx.setCurrentID(guid);
								Bundle bundle = new Bundle();
								bundle.putSerializable("BusinessObj", rwxx);
								intent.putExtras(bundle);
								startActivity(intent);
							}

						}

					});
					ab.setNegativeButton("ȡ��", new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							if (enforcementOfficer != null) {
								enforcementOfficer.setChecked(false);
							}
						}
					});

					AlertDialog ad = ab.create();
					ad.setOnKeyListener(new DialogInterface.OnKeyListener() {

						@Override
						public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
							if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
								enforcementOfficer.setChecked(false);
							}
							return false;
						}

					});
					ad.show();
				}

			}
		});
		companyListview.setOnItemClickListener(myOnItemClickListener);
//		/** ���ص���ѯ���� */
//		backButton.setOnClickListener(new View.OnClickListener() {
//			@Override
//			public void onClick(View v) {
//
//				companyListview.setVisibility(View.GONE);
//				queryView.setVisibility(View.VISIBLE);
//				xczf_bottom_layout.setVisibility(View.GONE);
//				pagingListCount = 1;
//				companyListview.setIsCompleted(false);
//				qyjbxx.setListScrolltimes(1);
//			}
//		});

//		enforcementButton.setOnClickListener(new View.OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				if (DisplayUitl.isFastDoubleClick()) {
//					return;
//				}
//				if (QYGUID.equals("")) {
//					Toast.makeText(EnforcementActivity.this, "����ѡ����ҵ", Toast.LENGTH_SHORT).show();
//					return;
//				}
//				String RWBH = enforcementModel.createOneEnforcementTask(QYGUID, companyName.getText().toString(), sbLawManUsersId.toString());
//				if (!RWBH.equals("")) {
//					ISADDTASK = true;// ���һ�����񷵻�ˢ��ִ����¼
//					Intent intent = new Intent(EnforcementActivity.this, QdjcnlActivity.class);
//
//					intent.putExtra("qyid", QYGUID);
//					intent.putExtra("rwbh", RWBH);
//					intent.putExtra("companyname", companyNameSelected);
//					intent.putExtra("IsUpload", "0");
//					LogUtil.i(TAG, "��ҵ����Ϊ---" + QYGUID + ",������Ϊ--->" + RWBH);
//					RWXX rwxx = new RWXX();
//					HashMap<String, Object> conditions = new HashMap<String, Object>();
//					conditions.put("rwbh", RWBH);
//					String guid = SqliteUtil.getInstance().getList("guid", conditions, "T_YDZF_RWXX").get(0).get("guid").toString();
//					rwxx.setCurrentID(guid);
//					Bundle bundle = new Bundle();
//					bundle.putSerializable("BusinessObj", rwxx);
//					intent.putExtras(bundle);
//					startActivity(intent);
//
//				}
//
//			}
//		});
		enforcementModel = new EnforcementModel();
		setData();
		// ArrayList<HashMap<String,Object>> simplelaw = getSimpleLawRecord();
		aecordAdapter = enforcementModel.getRecordAdapter(this, enforcementList);
		enforcementRecordListView.setAdapter(aecordAdapter);
		enforcementRecordListView.setOnItemClickListener(myOnItemClickListener);
		// ����ɾ��
		enforcementRecordListView.setOnItemLongClickListener(new OnItemLongClickListener() {
			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View view, int position, long arg3) {
				showDelDialog(position, view);
				return true;
			}
		});
		enforcementRecordListView.setOnPageCountChangListener(pageCountChangListener);// ���÷�ҳ����
	}

	private void setData() {
		enforcementList = enforcementModel.getUserEnforcementRecord(Global.getGlobalInstance().getUserid(), null);
	}

	private void initView() {
		middleLayout = (LinearLayout) findViewById(R.id.middleLayout);
		layoutInflater = LayoutInflater.from(this);
		viewPagerLayout = (LinearLayout) layoutInflater.inflate(com.mapuni.android.MobileEnforcement.R.layout.enforcement_main_layout, null);
		// InitImageView();
		InitViewPager();
		InitTextView();

		enforcementOfficer = (CheckBox) companyView.findViewById(com.mapuni.android.MobileEnforcement.R.id.gtzf);
//		enforcementButton = (Button) companyView.findViewById(com.mapuni.android.MobileEnforcement.R.id.xczf_btn);
//		backButton = (Button) companyView.findViewById(com.mapuni.android.MobileEnforcement.R.id.back_btn);
		backImg.setVisibility(View.VISIBLE);
		backImg.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				companyListview.setVisibility(View.GONE);
				queryView.setVisibility(View.VISIBLE);
				setBackButtonVisiable(false);
//				xczf_bottom_layout.setVisibility(View.GONE);
				pagingListCount = 1;
				companyListview.setIsCompleted(false);
				qyjbxx.setListScrolltimes(1);
			}
		});
		
		
		companyName = (EditText) companyView.findViewById(com.mapuni.android.MobileEnforcement.R.id.act_sl_EntName);
		setBackButtonVisiable(false);
//		xczf_bottom_layout = (LinearLayout) companyView.findViewById(com.mapuni.android.MobileEnforcement.R.id.xczf_bottom_layout);
//		xczf_bottom_layout.setVisibility(View.GONE);
		middleLayout.addView(viewPagerLayout);
	}

	private void InitViewPager() {
		viewPager = (ViewPager) viewPagerLayout.findViewById(com.mapuni.android.MobileEnforcement.R.id.enforcement_main_viewpager);
		companyView = layoutInflater.inflate(com.mapuni.android.MobileEnforcement.R.layout.sitelaw, null);
		companyParentView = (LinearLayout) companyView.findViewById(com.mapuni.android.MobileEnforcement.R.id.LinearLayout1);
		companyListview = (PagingListView) companyView.findViewById(com.mapuni.android.MobileEnforcement.R.id.lv_ls_Ent);
		companyListview.setDivider(null);
		enforcementRecordListView = new PagingListView(this);
		enforcementRecordListView.setCacheColorHint(Color.TRANSPARENT);
		enforcementRecordListView.setDivider(null);
		enforcementRecordListView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.FILL_PARENT));
		
		viewList = new ArrayList<View>();
		viewList.add(companyView);
		viewList.add(enforcementRecordListView);

		viewPager.setAdapter(new MyViewPagerAdapter());
		viewPager.setCurrentItem(0);
		viewPager.setOnPageChangeListener(new MyOnPageChangeListener());

	}

	/**
	 * ��ʼ��ͷ��
	 */

	private void InitTextView() {
		radioGroup = (RadioGroup) viewPagerLayout.findViewById(com.mapuni.android.MobileEnforcement.R.id.enforcement_main_title);
		RadioButton radioButton1 = (RadioButton) viewPagerLayout.findViewById(com.mapuni.android.MobileEnforcement.R.id.enforcement_main_company);
		RadioButton radioButton2 = (RadioButton) viewPagerLayout.findViewById(com.mapuni.android.MobileEnforcement.R.id.enforcement_main_record);

		radioButton1.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				viewPager.setCurrentItem(0, true);
				if(queryView!=null && queryView.isShown()){
					setBackButtonVisiable(false);
				}else{
				    setBackButtonVisiable(true);
				}
			}
		});

		radioButton2.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				viewPager.setCurrentItem(1, true);
				setBackButtonVisiable(false);
			}
		});

	}

	private YutuLoading load;
	private Handler handler0 = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 111:
				if (load.isShown()) {
					load.dismissDialog();
					Toast.makeText(EnforcementActivity.this, "ͬ��ʧ�ܣ���������", Toast.LENGTH_LONG).show();
				}
				break;
			case -1:
				if (load.isShown()) {
					load.dismissDialog();
					Toast.makeText(EnforcementActivity.this, "ͬ��ʧ��", Toast.LENGTH_LONG).show();
				}
				break;
			case -2:
				if (load.isShown()) {
					load.dismissDialog();
					Toast.makeText(EnforcementActivity.this, "ͬ����ʱ", Toast.LENGTH_LONG).show();
				}
				break;
			case -3:
				if (load.isShown()) {
					load.dismissDialog();
					Toast.makeText(EnforcementActivity.this, "ͬ���������쳣", Toast.LENGTH_LONG).show();
				}
				break;
			default:
				if (load.isShown()) {
					load.dismissDialog();
					Toast.makeText(EnforcementActivity.this, "ͬ���ɹ�", Toast.LENGTH_LONG).show();
				}
				break;

			}
		};
	};

	/**
	 * ��ʼ��page1�Ĳ�ѯ����
	 */
	private void initQueryView() {
		if (companyCondition == null) {
			companyCondition = new HashMap<String, Object>();
		}
		setBackButtonVisiable(false);
		queryView = qyjbxx.getLiaoNingQueryView(this, companyCondition);
		Button query_imagebutton = (Button) queryView.findViewById(com.mapuni.android.MobileEnforcement.R.id.enforcement_query_imagebutton);
		Button simple_law = (Button) queryView.findViewById(com.mapuni.android.MobileEnforcement.R.id.enforcement_Simple_law);
		//BYK ���ü���ִ����ť����
		simple_law.setVisibility(View.GONE);
		Button recordSync = (Button) queryView.findViewById(com.mapuni.android.MobileEnforcement.R.id.enforcement_Simple_sync);
		((View) query_imagebutton.getParent()).setVisibility(View.VISIBLE);// ��ѯ��ť�ɼ�
		((View) simple_law.getParent()).setVisibility(View.VISIBLE);// ��ѯ��ť�ɼ�
		((View) recordSync.getParent()).setVisibility(View.VISIBLE);// ͬ����ť�ɼ�
		recordSync.setVisibility(View.GONE);
		recordSync.setOnClickListener(new OnClickListener() {// ͬ��ִ����¼��ť

					@Override
					public void onClick(View v) {
						load = new YutuLoading(EnforcementActivity.this);
						load.setLoadMsg("���ڸ���ִ����Ϣ�����Ժ�", "");
						load.showDialog();
						new Thread() {
							public void run() {
								String[] tables = { "T_YDZF_RWXX", "T_YDZF_RWXX_USER", "YDZF_SiteEnvironmentMonitorRecord", "TaskEnpriLink", "YDZF_RWLC", "Survey_JSTZS",
										"T_ZFWS_WFXW", "T_ZFWS_WTZD", "T_ZFWS_KCBL", "T_ZFWS_XWBL", "T_ZFWS_XWJLWD", "YDZF_TaskSpecialitem" };
								DataSyncModel dataSync = new DataSyncModel();

								if (!Net.checkURL(Global.getGlobalInstance().getSystemurl())) {
									Log.i("info", "���粻����");
									handler0.sendEmptyMessage(111);
									return;
								}

								int result = 1;
								for (String string : tables) {
									result = dataSync.synchronizeFetchServerData(/*
																				 * "T_ZFWS_XWJLWD"
																				 * .
																				 * equals
																				 * (
																				 * string
																				 * )
																				 * ?
																				 * false
																				 * :
																				 */true, string);
								}
								Log.i("info", "result:" + result);
								handler0.sendEmptyMessage(result);
							};
						}.start();
					}
				});
		query_imagebutton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				totalDataList = qyjbxx.getDataList(companyCondition);
				if (totalDataList.size() > 0) {
					companyListview.setVisibility(View.VISIBLE);
					queryView.setVisibility(View.GONE);
					setBackButtonVisiable(true);
//					xczf_bottom_layout.setVisibility(View.VISIBLE);
				} else {
					Toast.makeText(EnforcementActivity.this, "û�з�����������ҵ�������������ҵ��", Toast.LENGTH_SHORT).show();
					// ���������ҵ��ť
					Button addCom = (Button) queryView.findViewById(com.mapuni.android.MobileEnforcement.R.id.addCom);
					((View) addCom.getParent()).setVisibility(View.VISIBLE);// �����ȾԴ��ť�ɼ�
					addCom.setOnClickListener(new View.OnClickListener() {
						public void onClick(View v) {
							// ת�������ҵҳ��
							Intent intAddCom = new Intent(EnforcementActivity.this, AddBusinessActivity.class);
							startActivity(intAddCom);

						}
					});
					return;
				}
				companyAdapter = new MyAdapter(EnforcementActivity.this);
				companyListview.setAdapter(companyAdapter);

			}
		});

		simple_law.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(EnforcementActivity.this, TaskSimplelawEnforcementActivity.class);
				startActivity(intent);
			}
		});
		companyParentView.addView(queryView);

	}

	public class MyViewPagerAdapter extends PagerAdapter {

		@Override
		public int getCount() {
			return viewList.size();
		}

		@Override
		public int getItemPosition(Object object) {
			return super.getItemPosition(object);
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView(viewList.get(position));
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			container.addView(viewList.get(position), 0);
			return viewList.get(position);
		}

	}

	public class MyOnPageChangeListener implements OnPageChangeListener {

		int one = offset * 2 + bmpW;// ҳ��1 -> ҳ��2 ƫ����

		// int two = one * 2;// ҳ��1 -> ҳ��3 ƫ����
		public void onPageScrollStateChanged(int arg0) {

		}

		public void onPageScrolled(int arg0, float arg1, int arg2) {

		}

		public void onPageSelected(int arg0) {
			if (arg0 == 0) {
				Log.e("hello", "radioGroupѡ����0");
				radioGroup.check(com.mapuni.android.MobileEnforcement.R.id.enforcement_main_company);
			} else {
				Log.e("hello", "radioGroupѡ����1");
				radioGroup.check(com.mapuni.android.MobileEnforcement.R.id.enforcement_main_record);
				if (enforcementModel == null) {
					enforcementModel = new EnforcementModel();
				}
				reflush();
			}

		}
	}

	private void reflush() {
		enforcementModel.setListScrollTimes(1);
		enforcementRecordListView.setIsCompleted(false);
		enforcementRecordCount = 1;
		ArrayList<HashMap<String, Object>> enforcementList = enforcementModel.getUserEnforcementRecord(Global.getGlobalInstance().getUserid(), null);
		aecordAdapter = enforcementModel.getRecordAdapter(EnforcementActivity.this, enforcementList);
		enforcementRecordListView.setAdapter(aecordAdapter);
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

		private int textSize = 26;
		private int selectItem = -1;

		private MyAdapter(Context context) {

			textSize = Integer.parseInt(String.valueOf(DisplayUitl.getSettingValue(context, DisplayUitl.TEXTSIZE, 26)));
		}

//		public void setSelectItem(int selectItem) {
//			this.selectItem = selectItem;
//		}

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
		public View getView(int position, View convertView, ViewGroup parent) {
			if (convertView == null) {
				convertView = layoutInflater.inflate(com.mapuni.android.MobileEnforcement.R.layout.listitem, null);
			}

			ImageView img = (ImageView) convertView.findViewById(com.mapuni.android.MobileEnforcement.R.id.listitem_left_image);
			TextView textView = (TextView) convertView.findViewById(com.mapuni.android.MobileEnforcement.R.id.listitem_text);
			textView.setSelected(true);
			textView.setText((String) totalDataList.get(position).get("qymc"));
			textView.setTag((String) totalDataList.get(position).get("guid"));// ������ҵGUID
			textView.setTextSize(textSize);
			img.setImageResource(com.mapuni.android.MobileEnforcement.R.drawable.xczf_qylb_tb);
//			if (position == selectItem) {
//				convertView.setBackgroundColor(getResources().getColor(com.mapuni.android.MobileEnforcement.R.color.bisque));
//			} else {
//				convertView.setBackgroundColor(Color.WHITE);
//			}
			return convertView;
		}
	}

	/**
	 * �б����¼�
	 * 
	 * @author wanglg
	 * 
	 */
	private class MyOnItemClickListener implements AdapterView.OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
			if (arg0.getId() == com.mapuni.android.MobileEnforcement.R.id.lv_ls_Ent) {// ��ҵ�б���
//				companyAdapter.setSelectItem(arg2);
//				companyAdapter.notifyDataSetInvalidated();
				TextView qymcTv = (TextView) arg1.findViewById(com.mapuni.android.MobileEnforcement.R.id.listitem_text);
				companyName.setText(qymcTv.getText().toString());
				companyNameSelected = qymcTv.getText().toString();
				QYGUID = qymcTv.getTag().toString();
				
				if (DisplayUitl.isFastDoubleClick()) {
					return;
				}
				if (QYGUID.equals("")) {
					Toast.makeText(EnforcementActivity.this, "����ѡ����ҵ", Toast.LENGTH_SHORT).show();
					return;
				}
				String RWBH = enforcementModel.createOneEnforcementTask(QYGUID, companyName.getText().toString(), sbLawManUsersId.toString());
				if (!RWBH.equals("")) {
					ISADDTASK = true;// ���һ�����񷵻�ˢ��ִ����¼
					Intent intent = new Intent(EnforcementActivity.this, QdjcnlActivity.class);

					intent.putExtra("qyid", QYGUID);
					intent.putExtra("rwbh", RWBH);
					intent.putExtra("companyname", companyNameSelected);
					intent.putExtra("IsUpload", "0");
					RWXX rwxx = new RWXX();
					HashMap<String, Object> conditions = new HashMap<String, Object>();
					conditions.put("rwbh", RWBH);
					String guid = SqliteUtil.getInstance().getList("guid", conditions, "T_YDZF_RWXX").get(0).get("guid").toString();
					rwxx.setCurrentID(guid);
					Bundle bundle = new Bundle();
					bundle.putSerializable("BusinessObj", rwxx);
					intent.putExtras(bundle);
					startActivity(intent);

				}
			} else {// ִ����¼���
				TextView rwmcTv = (TextView) arg1.findViewById(com.mapuni.android.MobileEnforcement.R.id.title);
				final String rwbh = rwmcTv.getTag().toString();
				TextView qymcTv = (TextView) arg1.findViewById(com.mapuni.android.MobileEnforcement.R.id.content);
				final String qyid = qymcTv.getTag().toString();
				if (qyid != null && !qyid.equals("")) {
					Intent intent = new Intent(EnforcementActivity.this, QdjcnlActivity.class);

					intent.putExtra("taskInfoFlag", "taskInfoFlag");

					intent.putExtra("qyid", qyid);
					HashMap<String, Object> conditions = new HashMap<String, Object>();
					conditions.put("guid", qyid);
					ArrayList<HashMap<String, Object>> arryListBack = SqliteUtil.getInstance().getList("qymc", conditions, "T_WRY_QYJBXX");
					String qymc = "";
					if (arryListBack.size() > 0) {
						qymc = arryListBack.get(0).get("qymc").toString();
					}
					Log.i("info", "��ҵ���ƣ�" + qymc);
					intent.putExtra("companyname", qymc);
					intent.putExtra("rwbh", rwbh);
					RWXX rwxx = new RWXX();
					conditions = new HashMap<String, Object>();
					conditions.put("rwbh", rwbh);
					String guid = SqliteUtil.getInstance().getList("guid", conditions, "T_YDZF_RWXX").get(0).get("guid").toString();
					rwxx.setCurrentID(guid);
					Bundle bundle = new Bundle();
					bundle.putSerializable("BusinessObj", rwxx);
					intent.putExtras(bundle);
					startActivity(intent);
				} else {// ����ִ��û��qyid
					Intent intent = new Intent(EnforcementActivity.this, TaskSimplelawEnforcementActivity.class);
					intent.putExtra("rwbh", rwbh);
					startActivity(intent);
				}

			}

		}

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		if (requestCode == 520 && resultCode == -1) {
			String qyid = data.getStringExtra("qyid");
			String rwbh = data.getStringExtra("rwbh");
			if (qyid != null && !qyid.equals("")) {
				Intent intent = new Intent(EnforcementActivity.this, QdjcnlActivity.class);
				intent.putExtra("qyid", qyid);
				intent.putExtra("rwbh", rwbh);
				intent.putExtra("taskInfoFlag", "taskInfoFlag");
				RWXX rwxx = new RWXX();
				HashMap<String, Object> conditions = new HashMap<String, Object>();
				conditions.put("rwbh", rwbh);
				String guid = SqliteUtil.getInstance().getList("guid", conditions, "T_YDZF_RWXX").get(0).get("guid").toString();
				rwxx.setCurrentID(guid);
				Bundle bundle = new Bundle();
				bundle.putSerializable("BusinessObj", rwxx);
				intent.putExtras(bundle);
				startActivity(intent);
			} else {
				Intent intent = new Intent(EnforcementActivity.this, TaskSimplelawEnforcementActivity.class);
				intent.putExtra("rwbh", rwbh);
				startActivity(intent);
			}

			if (yutuLoading != null) {
				yutuLoading.dismissDialog();
			}

			return;
		}

		if (yutuLoading != null) {
			yutuLoading.dismissDialog();
		}
	}

	/**
	 * ��ҳ���ؼ����������̲߳�ѯ��ҳ����Ȼ������Ϣ֪ͨ����ˢ��
	 * 
	 * @author wanglg
	 * 
	 */
	private class MyOnPageCountChangListener implements PagingListView.PageCountChangListener {

		@Override
		public void onAddPage(AbsListView view) {
			if (view.getId() == com.mapuni.android.MobileEnforcement.R.id.lv_ls_Ent) {// ��ҵ�б�
				Log.i("info", "��������");
				new Thread(new Runnable() {

					@Override
					public void run() {

						qyjbxx.setListScrolltimes(++pagingListCount);
						ArrayList<HashMap<String, Object>> newData = null;
						if (companyCondition != null) {
							Log.i("hello", "��ҳ���ֲ�ѯ����" + companyCondition.toString());
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
			} else {// ִ����¼
				new Thread(new Runnable() {

					@Override
					public void run() {
						Message msg = new Message();
						msg.what = 1;
						enforcementModel.setListScrollTimes(++enforcementRecordCount);
						ArrayList<HashMap<String, Object>> newData = enforcementModel.getUserEnforcementRecord(Global.getGlobalInstance().getUserid(), aecordCondition);// ��ѯ��ҳ����
						msg.obj = newData;
						if (newData.size() < Global.getGlobalInstance().getListNumber()) {
							enforcementRecordListView.setIsCompleted(true);
							msg.what = 2;
							handler.sendMessage(msg);
							return;
						}

						handler.sendMessage(msg);
					}
				}).start();
			}
		}

	}

	/**
	 * Description:��ʾ����ɾ���Ի���
	 */
	protected void showDelDialog(final int position, final View view) {
		AlertDialog.Builder deleteab = new AlertDialog.Builder(EnforcementActivity.this);
		deleteab.setTitle("ɾ��");
		deleteab.setMessage("��ȷ��Ҫɾ����");
		deleteab.setIcon(com.mapuni.android.MobileEnforcement.R.drawable.icon_delete);
		deleteab.setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				TextView rwmcTv = (TextView) view.findViewById(com.mapuni.android.MobileEnforcement.R.id.title);
				String rwbh = rwmcTv.getTag().toString();
				TextView qymcTv = (TextView) view.findViewById(com.mapuni.android.MobileEnforcement.R.id.content);
				String del_qyguid = qymcTv.getTag().toString();
				// ���������Ų�ѯ��������ִ��״̬��������ϴ�
				RWXX rwxx = new RWXX();
			//	String rwzt = rwxx.getTaskid(rwbh);
			//	if (rwzt.equals("003") || rwzt.equals("005")) {
				//�޸��ݴ�����Ҳ��ɾ��������
				String rwzt_String = aecordAdapter.getRWZT_String(position);
				//	if (rwzt.equals("003")) {
						if (rwzt_String.equals("δ�ϴ�")) {
					// ɾ��������Ϣ���м�¼
					String sql = "delete from T_YDZF_RWXX where RWBH='" + rwbh + "'";
					SqliteUtil.getInstance().execute(sql);
					// ɾ�����������ҵ���м�¼
					String sql0 = "delete from TaskEnpriLink where TaskID='" + rwbh + "' and QYID='" + del_qyguid + "'";
					SqliteUtil.getInstance().execute(sql0);
					// ɾ����ҵ��ص��嵥���¼
					String sql1 = "delete from YDZF_TaskSpecialItem where TaskID='" + rwbh + "' and EnterID='" + del_qyguid + "'";
					SqliteUtil.getInstance().execute(sql1);
					// ɾ����ҵ��ؿ����¼���¼
					String sql2 = "delete from T_ZFWS_KCBL where TaskId='" + rwbh + "' and EntCode='" + del_qyguid + "'";
					SqliteUtil.getInstance().execute(sql2);
					// ɾ ����ҵ��ص�ѯ�ʱ�¼���¼
					String sql3 = "delete from T_ZFWS_XWBL where TaskId='" + rwbh + "' and SurveyEntCode='" + del_qyguid + "'";
					SqliteUtil.getInstance().execute(sql3);
					// ɾ����ҵ��صĸ�����Ϣ���¼
					String sql4 = "delete from T_Attachment where FK_Unit='2' and FK_Id='" + rwbh + "_" + del_qyguid + "'";
					SqliteUtil.getInstance().execute(sql4);
					// ɾ����ҵ��ص�һ��һ����¼
					String sql5 = "delete from T_ZFWS_XWJLWD where TaskID='" + rwbh + "' and EntID='" + del_qyguid + "'";
					SqliteUtil.getInstance().execute(sql5);
					// ɾ����֮�����²�ѯ����
					ArrayList<HashMap<String, Object>> enforcementList = enforcementModel.getUserEnforcementRecord(Global.getGlobalInstance().getUserid(), null);
					if (aecordAdapter != null) {
						aecordAdapter.updateValue(enforcementList);
						aecordAdapter.notifyDataSetChanged();
						ISADDTASK = false;// ���ñ��
						pagingListCount = 1;
						
					}
				} else {
					Toast.makeText(EnforcementActivity.this, "����ɾ�����ϴ���ִ����¼��", Toast.LENGTH_SHORT).show();
				}
			}
		});
		deleteab.setNegativeButton("ȡ��", null);
		AlertDialog ad = deleteab.create();
		ad.show();
	}

}
