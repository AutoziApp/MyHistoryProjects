package com.mapuni.android.enterpriseArchives;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.zip.Inflater;

import org.dom4j.DocumentException;

import android.R.string;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.text.Layout;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mapuni.android.MobileEnforcement.R;
import com.mapuni.android.base.BaseActivity;
import com.mapuni.android.base.Global;
import com.mapuni.android.base.LoadDetailLayout;
import com.mapuni.android.base.adapter.ListActivityAdapter;
import com.mapuni.android.base.adapter.ListActivityAdapter.ViewHolder;
import com.mapuni.android.base.business.BaseObjectFactory;
import com.mapuni.android.base.controls.loading.YutuLoading;
import com.mapuni.android.base.interfaces.IList;
import com.mapuni.android.base.interfaces.PathManager;
import com.mapuni.android.base.util.DisplayUitl;
import com.mapuni.android.business.QYJBXX;
import com.mapuni.android.common.DetailedActivity;
import com.mapuni.android.dataprovider.FileHelper;
import com.mapuni.android.dataprovider.SqliteUtil;
import com.mapuni.android.dataprovider.XmlHelper;
import com.mapuni.android.enforcement.QdjcnlActivity;
import com.mapuni.android.gis.MapActivity;

/**
 * һ��һ������
 * 
 * @author wangliugeng
 * 
 */
public class EnterpriseArchivesActivitySlide extends BaseActivity {
	/** ��ҵ���� */
	private String qydm;
	/** ��ҵ��ѯ */
	private String qyid;
	/** ���ɱ༭��ʶ */
	private String noedit;
	/** ���⼯�� */
	private final ArrayList<String> titles = new ArrayList<String>();
	/** ҵ���༯�� */
	private final ArrayList<ArrayList<IList>> businesses = new ArrayList<ArrayList<IList>>();

	/** ��ʽ���� */
	private final ArrayList<String> styles = new ArrayList<String>();
	/** ��ҵ������Ϣҵ���� */
	private final QYJBXX qyjbxx = new QYJBXX();
	/** ������ͼȨ�� */
	private final static String HBDT_QX = "vmob5A";
	/** ���߼��Ȩ�� */
	private final static String ZXJC_QX = "vmob3A";

	private YutuLoading yutuLoading;
	// ///////////////////////////////////////////
	private LinearLayout body;
	private SlideView slideView;
	private Context context = EnterpriseArchivesActivitySlide.this;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.ui_mapuni);
		findViewById(R.id.ui_mapuni_divider).setVisibility(View.GONE);
		this.SetBaseStyle((RelativeLayout) findViewById(R.id.parentLayout),
				"һ��һ����Ϣ");
		qyid = getIntent().getStringExtra("qyid");
		noedit = getIntent().getStringExtra("noedit");
		// body
		body = (LinearLayout) findViewById(R.id.middleLayout);
		body.setPadding(0, 0, 0, 0);

//		setEditButtonListener(clickListener);  //editImgΪnull
		setSynchronizeButtonVisiable(true);

		/** ��ʼ��������Ϣ */
		initConfigValue();

		/** ��ʼ����� */
		initView();
	}

	/**
	 * �༭��ť�����¼�
	 */
	OnClickListener clickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {

			EnterpriseArchivesActivitySlide.this.finish();
			Intent intent = new Intent(EnterpriseArchivesActivitySlide.this,
					AddBusinessActivity.class);
			intent.putExtra("qydm", qyid);
			startActivity(intent);

		}
	};

	private class SyncLoadingData extends AsyncTask<View, Void, Void> {
		private boolean typeFlg;
		private int index;
		private int childIndex;
		private ArrayList<Object> obj;
		private LoadDetailLayout loadDetailLayout;
		private View view;
		private LayoutInflater inflater;

		/**
		 * 
		 * @param typeFlg
		 *            true Ϊlist��ʾ falseΪview��ʾ
		 * @param index
		 *            view��viewpager��λ��
		 */
		public SyncLoadingData(boolean typeFlg, int index) {
			this.typeFlg = typeFlg;
			this.index = index;
		}

		public SyncLoadingData(boolean typeFlg, int index, int childIndex) {
			this.typeFlg = typeFlg;
			this.index = index;
			this.childIndex = childIndex;
		}

		@Override
		protected Void doInBackground(View... params) {
			view = params[0];

			if (typeFlg) {
				IList lst = businesses.get(index).get(0);

				if (lst != null) {
					HashMap<String, Object> condition = new HashMap<String, Object>();
					condition.put("QYDM", qyid);
					HashMap<String, Object> style = new HashMap<String, Object>();

					try {
						style = lst.getStyleList(EnterpriseArchivesActivitySlide.this);
					} catch (IOException e) {
						e.printStackTrace();
					}

					ArrayList<HashMap<String, Object>> mdata = lst.getDataList(condition);
					obj = new ArrayList<Object>();

					if (mdata == null) {
						mdata = new ArrayList<HashMap<String, Object>>();
					}

					obj.add(mdata);
					obj.add(style);
				}
			} else {
				if (index == 0) {
					loadDetailLayout = new LoadDetailLayout(
							EnterpriseArchivesActivitySlide.this, qyjbxx, false);

				} else {
					IList lst = businesses.get(index).get(childIndex);
					
					if (lst != null) {
						HashMap<String, Object> condition = new HashMap<String, Object>();
						condition.put("QYDM", qyid);
						HashMap<String, Object> style = new HashMap<String, Object>();

						try {
							style = lst.getStyleList(EnterpriseArchivesActivitySlide.this);
						} catch (IOException e) {
							e.printStackTrace();
						}

						ArrayList<HashMap<String, Object>> mdata = lst.getDataList(condition);
						obj = new ArrayList<Object>();

						if (mdata == null) {
							mdata = new ArrayList<HashMap<String, Object>>();
						}

						obj.add(mdata);
						obj.add(style);
					}
				}
			}
			
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {		
			if (typeFlg) {
				@SuppressWarnings("unchecked")
				ArrayList<HashMap<String, Object>> mdata2 = (ArrayList<HashMap<String, Object>>) ((ArrayList<Object>) obj).get(0);
				@SuppressWarnings("unchecked")
				HashMap<String, Object> style2 = (HashMap<String, Object>) ((ArrayList<Object>) obj).get(1);

				if (mdata2.size() == 0) {
					((ListView) (view)).setDivider(null);
				}
				if (mdata2.size() >= 1) {
					((ListView) (view)).setDivider(getResources().getDrawable(
							R.drawable.list_divider));
				}
				
				((ListView) (view)).setAdapter(new ListActivityAdapter(EnterpriseArchivesActivitySlide.this, new Bundle(),mdata2, style2));

			} else {
				if (index == 0) {
					((RelativeLayout) view).addView(loadDetailLayout.loadDetailed(qyid));
					loadBotomLayout(((RelativeLayout) view));

				} else if (index == 1) { 
					//ƿ����ҵ���
					// ����Զ�����ҵ������
//					ArrayList<HashMap<String, Object>> mdata2 = (ArrayList<HashMap<String, Object>>) ((ArrayList<Object>) obj).get(0);
//					LinearLayout linearLayout = new LinearLayout(context);
//					linearLayout.setOrientation(LinearLayout.VERTICAL);
//					linearLayout.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT));
//					linearLayout.setPadding(12,12,12,12);
//					linearLayout.setGravity(Gravity.LEFT);
//
//					// �Զ�����ҵ���� WebView ����
//					WebView webView = new WebView(EnterpriseArchivesActivitySlide.this);
//					webView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT));
//					WebSettings webSettings = webView.getSettings();
//					webSettings.setJavaScriptEnabled(true);
//					webSettings.setBuiltInZoomControls(true);
//					webSettings.setLoadWithOverviewMode(true);
//					
//					String str = mdata2.get(0).get("qyjj").toString();
//					if ((null != str) && !"".equals(str)) {
//						webView.loadDataWithBaseURL(null, str, "text/html", "utf-8",null);
//						linearLayout.addView(webView);
//					}else {
//						inflater = LayoutInflater.from(EnterpriseArchivesActivitySlide.this);
//						LinearLayout layout = (LinearLayout) inflater.inflate(R.layout.my_failed, null);
//						linearLayout.addView(layout);
//					}
//
//					((RelativeLayout) view).addView(linearLayout);
				} else {

					ArrayList<HashMap<String, Object>> mdata2 = (ArrayList<HashMap<String, Object>>) ((ArrayList<Object>) obj).get(0);
					HashMap<String, Object> style2 = (HashMap<String, Object>) ((ArrayList<Object>) obj).get(1);

					if (mdata2.size() == 0) {
						((ListView) (view)).setDivider(null);
					}
					if (mdata2.size() >= 1) {
						((ListView) (view)).setDivider(getResources().getDrawable(R.drawable.list_divider));
					}

					((ListView) (view)).setAdapter(new ListActivityAdapter(
							EnterpriseArchivesActivitySlide.this, new Bundle(),mdata2, style2));
				}

			}

			if (yutuLoading != null) {
				yutuLoading.dismissDialog();
			}
		}
	}

	private void initView() {
		slideView = new SlideView(this, 0);

		for (int i = 0; i < titles.size(); i++) {
			SlideOnLoadAdapter adapter = null;

			if (styles.get(i).equals("listview")) {
				final ListView listview = new ListView(this);
				listview.setCacheColorHint(Color.TRANSPARENT);
				listview.setLayoutParams(new LayoutParams(
						LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
				listview.setId( i );
				listview.setTag(businesses.get(i).get(0));
				listview.setOnItemClickListener(itemClickListener);
				final int temp = i;
				adapter = new SlideOnLoadAdapter(listview) {
					@Override
					public void OnLoad() {
						yutuLoading = new YutuLoading(
								EnterpriseArchivesActivitySlide.this);
						yutuLoading.setCancelable(true);
						yutuLoading.showDialog();

						new SyncLoadingData(true, temp).execute(this.view);
					}
				};
			} else {
				if (i == 0 ) { //XXX ��ҵ������Ϣ����ҵ���
					final int index = i;
					RelativeLayout view = new RelativeLayout(this);
					adapter = new SlideOnLoadAdapter(view) {
						@Override
						public void OnLoad() {
							yutuLoading = new YutuLoading(EnterpriseArchivesActivitySlide.this);
							yutuLoading.setCancelable(true);
							yutuLoading.showDialog();

							new SyncLoadingData(false, index).execute(this.view);
						}
					};
				} else {
					final SlideView childSlideView = new SlideView(this, 4);
					childSlideView.setLoseBackColor(R.drawable.btn_click);

					final int temp = i;
					adapter = new SlideOnLoadAdapter(childSlideView) {

						@Override
						public void OnLoad() {
							ArrayList<IList> list = businesses.get(temp);
							int width = DisplayUitl.getMobileResolution(EnterpriseArchivesActivitySlide.this)[0];
							childSlideView.setSlideViewWidth(width/list.size());

							SlideOnLoadAdapter childAdapter;
							for (int n = 0; n < list.size(); n++) {

								final ListView listview = new ListView(EnterpriseArchivesActivitySlide.this);
								listview.setCacheColorHint(Color.TRANSPARENT);
								listview.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,LayoutParams.FILL_PARENT));
								listview.setTag(list.get(n));
								listview.setOnItemClickListener(itemClickListener);

								final int childIndex = n;
								childAdapter = new SlideOnLoadAdapter(listview) {
									@Override
									public void OnLoad() {
										yutuLoading = new YutuLoading(EnterpriseArchivesActivitySlide.this);
										yutuLoading.setCancelable(true);
										yutuLoading.showDialog();

										new SyncLoadingData(false, temp,childIndex).execute(this.view);
									}
								};
								childSlideView.AddPageView(childAdapter, list.get(n).getListTitleText());

							}
							childSlideView.Display();

						}

					};

				}

			}

			slideView.AddPageView(adapter, titles.get(i));
		}

		slideView.Display();
		body.addView(slideView);
		
	}

	private void initConfigValue() {
		ArrayList<HashMap<String, Object>> value = getConfigValue();

		if (value == null) {
			return;
		}

		int index = 0;

		for (HashMap<String, Object> configValue : value) {
			String title = configValue.get("title").toString();
			titles.add(title);
			String business = configValue.get("ywl").toString();

			if (!business.equals("")) {
				ArrayList<IList> businessList = new ArrayList<IList>();

				String[] businessarr = business.split(",");
				for (String businesslName : businessarr) {
					try {
						businessList.add((IList) BaseObjectFactory
								.createYQYDObject(businesslName));
					} catch (ClassNotFoundException e) {

						try {
							businessList.add((IList) BaseObjectFactory.createObject(businesslName));
						} catch (Exception e1) {
							e1.printStackTrace();
						}
					} catch (IllegalAccessException e) {
						e.printStackTrace();
					} catch (InstantiationException e) {
						e.printStackTrace();
					}
				}
    
				businesses.add(index, businessList);

			} else {
				businesses.add(index, (ArrayList<IList>) new Object());
			}
			styles.add(configValue.get("style").toString());
			index++;
		}
	}

	private ArrayList<HashMap<String, Object>> getConfigValue() {
		ArrayList<HashMap<String, Object>> value = null;
		InputStream is = null;
		try {
			is = getAssets().open("yqyd_config.xml");
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			value = XmlHelper.getList(is);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (DocumentException e) {
			e.printStackTrace();
		}
		return value;
	}

	/** ���صײ��˵� */
	private void loadBotomLayout(RelativeLayout view) {
		RelativeLayout.LayoutParams lp1 = new RelativeLayout.LayoutParams(
				ViewGroup.LayoutParams.FILL_PARENT,
				ViewGroup.LayoutParams.WRAP_CONTENT);

		lp1.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
		LinearLayout bottomLayout = new LinearLayout(this);
		ImageView splite;
		

		if (DisplayUitl.getAuthority(HBDT_QX)) {
			Button zxjc_btn = new Button(this);
			zxjc_btn.setText("��ͼ��λ");
			zxjc_btn.setBackgroundColor(Color.parseColor("#404F8B"));
//			zxjc_btn.setBackgroundResource(R.drawable.btn_shap);
			zxjc_btn.setTextColor(android.graphics.Color.WHITE);
			zxjc_btn.setVisibility(View.VISIBLE);
			zxjc_btn.getPaint().setFakeBoldText(true);// �Ӵ�
			zxjc_btn.setLayoutParams(new LinearLayout.LayoutParams(
					LinearLayout.LayoutParams.FILL_PARENT,
					LinearLayout.LayoutParams.FILL_PARENT, 1.0f));
			zxjc_btn.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					HashMap<String, Object> conditions = new HashMap<String, Object>();
					conditions.put("guid", qyid);

					ArrayList<HashMap<String, Object>> result = SqliteUtil
							.getInstance().getList("T_WRY_QYJBXX", conditions);
					if (result.size() < 1) {
						Toast.makeText(EnterpriseArchivesActivitySlide.this,
								"�޸���ҵλ����Ϣ��", Toast.LENGTH_SHORT).show();
					}
					String jd = result.get(0).get("jd").toString();
					String wd = result.get(0).get("wd").toString();
					try {
						double rjd = Double.parseDouble(jd);
						double rwd = Double.parseDouble(wd);

						Intent intent = new Intent(
								EnterpriseArchivesActivitySlide.this,
								MapActivity.class);
						intent.setAction("WRYDY");
						intent.putExtra("number", 1);
						intent.putExtra("jd", rjd);
						intent.putExtra("wd", rwd);
						intent.putExtra("qydm", qydm);
						intent.putExtra("qyid", qyid);
						intent.putExtra("pname", result.get(0).get("qymc")
								.toString());
						startActivity(intent);
					} catch (Exception e) {
						Toast.makeText(EnterpriseArchivesActivitySlide.this,
								"����ҵλ����Ϣ���淶�޷���λ��", Toast.LENGTH_LONG).show();
						return;
					}
				}
			});
			bottomLayout.addView(zxjc_btn);

			splite = new ImageView(this);
			splite.setScaleType(ScaleType.FIT_XY);
			splite.setLayoutParams(new LinearLayout.LayoutParams(2,
					LinearLayout.LayoutParams.FILL_PARENT));
			splite.setBackgroundResource(R.drawable.bg_bottombutton_splite);
			bottomLayout.addView(splite);
		}

		bottomLayout.setId(1);
		View childview = view.getChildAt(0);
		RelativeLayout.LayoutParams lp2 = new RelativeLayout.LayoutParams(
				ViewGroup.LayoutParams.FILL_PARENT,
				ViewGroup.LayoutParams.FILL_PARENT);
		lp2.addRule(RelativeLayout.ABOVE, bottomLayout.getId());
		childview.setLayoutParams(lp2);
		view.addView(bottomLayout, lp1);
	}

	/** �б�Item������� */
	OnItemClickListener itemClickListener = new OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {

			 // ��ת�������������� com.mapuni.android.infoQuery.XZCF@42cc0388
//			if ((parent.getTag().toString().split("@")[0]).equals("com.mapuni.android.infoQuery.XZCF")) {
//				Intent intent = new Intent(EnterpriseArchivesActivitySlide.this, XZCFActivity.class);
//				intent.putExtra("QYDM", qyid);
//				intent.putExtra("position", new int[] {position});
//				startActivity(intent);
//				
//			}else
			if (parent.getId() == 2) {// �ж��������ҵƽ��ͼ������ʵ�����µ���¼�
				// ����첽������ҵƽ��ͼ��������
				String sql = "select FileName,FilePath from T_Attachment where FK_Id = '" + qyid + "' and FK_Unit = '69'";
				ArrayList<HashMap<String, Object>> dateList = SqliteUtil.getInstance().queryBySqlReturnArrayListHashMap(sql);
				String fileName = dateList.get(position).get("filepath").toString();
				fujian(fileName);
				
//			}else if (parent.getId() == 3) {
//				// �����ҵ���������ƶ���ת�������
//				Intent intent = new Intent(EnterpriseArchivesActivitySlide.this, QYHJGLZDinfoActivity.class);
//				intent.putExtra("QYDM", qyid);
//				intent.putExtra("position", new int[] {position});
//				startActivity(intent);
				
			}else{
				ViewHolder holder = (ViewHolder) view.getTag();
				
				if (holder == null || holder.id == null) {
					return;
				}
				String idValue = holder.id == null ? "" : holder.id.getTag()
						.toString();
				
				Bundle bundle = new Bundle();
				
				IList iList = (IList) parent.getTag();
				
				iList.setCurrentID(idValue);
				bundle.putSerializable("BusinessObj", (Serializable) iList);
				Intent intent = null;
				if (idValue != null && idValue.contains("T")) {
					intent = new Intent(EnterpriseArchivesActivitySlide.this,QdjcnlActivity.class);
					intent.putExtra("qyid", qyid);
					intent.putExtra("rwbh", idValue);
					intent.putExtra("flag", "LSZFQK");
				} else {
					intent = new Intent(EnterpriseArchivesActivitySlide.this,DetailedActivity.class);
				}
				intent.putExtras(bundle);
				startActivity(intent);
			}
			
		}
	};
	
	/** �ж������ػ��Ǵ򿪸��� */
	public void fujian(String fjname) {
		String url = null;
		try {
			url = Global.getGlobalInstance().getSystemurl() + "/Attach/QYPMT/" + java.net.URLEncoder.encode(fjname, "UTF-8");
			String filePathStr = Global.SDCARD_RASK_DATA_PATH+ "/Attach/QYPMT/" + fjname;
			File file = new File(filePathStr);
			// ����ļ����ڴ��ļ�
			if (fjname != null && !fjname.equals("") && file.exists()) {
				DisplayUitl.openfile(filePathStr, EnterpriseArchivesActivitySlide.this);
			} else {
				downfujian(url, filePathStr);
			}
		} catch (Exception e) {
			e.printStackTrace();
		};
	
	}

	/** ���ظ��� */
	private void downfujian(String fjnameurl, String filePathStr) {
		try {
			DownloadFile downloadFile = new DownloadFile(EnterpriseArchivesActivitySlide.this);

			String[] params = { fjnameurl, filePathStr };

			downloadFile.execute(params);
		} catch (Exception e) {
			Toast.makeText(EnterpriseArchivesActivitySlide.this, "�������޸���", Toast.LENGTH_SHORT).show();
		}

	}
}
