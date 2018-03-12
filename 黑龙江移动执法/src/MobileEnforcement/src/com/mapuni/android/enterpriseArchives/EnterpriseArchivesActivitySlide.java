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
 * 一企一档界面
 * 
 * @author wangliugeng
 * 
 */
public class EnterpriseArchivesActivitySlide extends BaseActivity {
	/** 企业代码 */
	private String qydm;
	/** 企业查询 */
	private String qyid;
	/** 不可编辑标识 */
	private String noedit;
	/** 标题集合 */
	private final ArrayList<String> titles = new ArrayList<String>();
	/** 业务类集合 */
	private final ArrayList<ArrayList<IList>> businesses = new ArrayList<ArrayList<IList>>();

	/** 样式集合 */
	private final ArrayList<String> styles = new ArrayList<String>();
	/** 企业基本信息业务类 */
	private final QYJBXX qyjbxx = new QYJBXX();
	/** 环保地图权限 */
	private final static String HBDT_QX = "vmob5A";
	/** 在线监测权限 */
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
				"一企一档信息");
		qyid = getIntent().getStringExtra("qyid");
		noedit = getIntent().getStringExtra("noedit");
		// body
		body = (LinearLayout) findViewById(R.id.middleLayout);
		body.setPadding(0, 0, 0, 0);

//		setEditButtonListener(clickListener);  //editImg为null
		setSynchronizeButtonVisiable(true);

		/** 初始化配置信息 */
		initConfigValue();

		/** 初始化组件 */
		initView();
	}

	/**
	 * 编辑按钮监听事件
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
		 *            true 为list显示 false为view显示
		 * @param index
		 *            view在viewpager的位置
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
					//瓶坯企业简介
					// 添加自定义企业简介界面
//					ArrayList<HashMap<String, Object>> mdata2 = (ArrayList<HashMap<String, Object>>) ((ArrayList<Object>) obj).get(0);
//					LinearLayout linearLayout = new LinearLayout(context);
//					linearLayout.setOrientation(LinearLayout.VERTICAL);
//					linearLayout.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT));
//					linearLayout.setPadding(12,12,12,12);
//					linearLayout.setGravity(Gravity.LEFT);
//
//					// 自定义企业简介的 WebView 界面
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
				if (i == 0 ) { //XXX 企业基本信息和企业简介
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

	/** 加载底部菜单 */
	private void loadBotomLayout(RelativeLayout view) {
		RelativeLayout.LayoutParams lp1 = new RelativeLayout.LayoutParams(
				ViewGroup.LayoutParams.FILL_PARENT,
				ViewGroup.LayoutParams.WRAP_CONTENT);

		lp1.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
		LinearLayout bottomLayout = new LinearLayout(this);
		ImageView splite;
		

		if (DisplayUitl.getAuthority(HBDT_QX)) {
			Button zxjc_btn = new Button(this);
			zxjc_btn.setText("地图定位");
			zxjc_btn.setBackgroundColor(Color.parseColor("#404F8B"));
//			zxjc_btn.setBackgroundResource(R.drawable.btn_shap);
			zxjc_btn.setTextColor(android.graphics.Color.WHITE);
			zxjc_btn.setVisibility(View.VISIBLE);
			zxjc_btn.getPaint().setFakeBoldText(true);// 加粗
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
								"无该企业位置信息！", Toast.LENGTH_SHORT).show();
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
								"该企业位置信息不规范无法定位！", Toast.LENGTH_LONG).show();
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

	/** 列表Item点击监听 */
	OnItemClickListener itemClickListener = new OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {

			 // 跳转到行政处罚界面 com.mapuni.android.infoQuery.XZCF@42cc0388
//			if ((parent.getTag().toString().split("@")[0]).equals("com.mapuni.android.infoQuery.XZCF")) {
//				Intent intent = new Intent(EnterpriseArchivesActivitySlide.this, XZCFActivity.class);
//				intent.putExtra("QYDM", qyid);
//				intent.putExtra("position", new int[] {position});
//				startActivity(intent);
//				
//			}else
			if (parent.getId() == 2) {// 判断如果是企业平面图界面则实现以下点击事件
				// 添加异步下载企业平面图附件代码
				String sql = "select FileName,FilePath from T_Attachment where FK_Id = '" + qyid + "' and FK_Unit = '69'";
				ArrayList<HashMap<String, Object>> dateList = SqliteUtil.getInstance().queryBySqlReturnArrayListHashMap(sql);
				String fileName = dateList.get(position).get("filepath").toString();
				fujian(fileName);
				
//			}else if (parent.getId() == 3) {
//				// 添加企业环境管理制度跳转详情界面
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
	
	/** 判断是下载还是打开附件 */
	public void fujian(String fjname) {
		String url = null;
		try {
			url = Global.getGlobalInstance().getSystemurl() + "/Attach/QYPMT/" + java.net.URLEncoder.encode(fjname, "UTF-8");
			String filePathStr = Global.SDCARD_RASK_DATA_PATH+ "/Attach/QYPMT/" + fjname;
			File file = new File(filePathStr);
			// 如果文件存在打开文件
			if (fjname != null && !fjname.equals("") && file.exists()) {
				DisplayUitl.openfile(filePathStr, EnterpriseArchivesActivitySlide.this);
			} else {
				downfujian(url, filePathStr);
			}
		} catch (Exception e) {
			e.printStackTrace();
		};
	
	}

	/** 下载附件 */
	private void downfujian(String fjnameurl, String filePathStr) {
		try {
			DownloadFile downloadFile = new DownloadFile(EnterpriseArchivesActivitySlide.this);

			String[] params = { fjnameurl, filePathStr };

			downloadFile.execute(params);
		} catch (Exception e) {
			Toast.makeText(EnterpriseArchivesActivitySlide.this, "服务器无附件", Toast.LENGTH_SHORT).show();
		}

	}
}
