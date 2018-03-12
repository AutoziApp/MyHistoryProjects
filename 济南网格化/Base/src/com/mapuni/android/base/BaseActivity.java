package com.mapuni.android.base;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.impl.client.TunnelRefusedException;
import org.dom4j.DocumentException;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils.TruncateAt;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.mapuni.android.base.adapter.SortAdapter;
import com.mapuni.android.base.business.BaseDataSync;
import com.mapuni.android.base.business.BaseObjectFactory;
import com.mapuni.android.base.business.BaseUsers;
import com.mapuni.android.base.interfaces.IList;
import com.mapuni.android.base.util.DisplayUitl;
import com.mapuni.android.base.util.ExceptionManager;
import com.mapuni.android.base.util.OtherTools;
import com.mapuni.android.dataprovider.XmlHelper;

/**
 * FileName: BaseActivity.java Description: 最基础的Activity
 * 
 * @author 王红娟
 * @Version 1.3.4
 * @Copyright 中科宇图天下科技有限公司 Create at: 2012-12-5 上午11:35:37
 */

@SuppressLint("ResourceAsColor")
public class BaseActivity extends FragmentActivity {
	/** 创建一个Tag名称 */
	private final String TAG = "BaseActivity";

	/** 定义该Activity所需布局 */
	public RelativeLayout mapuniLinearLayout = null;
	private RelativeLayout relativeLayoutTitle = null;

	/** 全屏Dialog，点击按钮出现等待框 */
	protected Dialog dl = null;

	/** 左上角的快捷按钮 */
	public ImageView backImg = null;
	/** 右上角的快捷按钮 */
	public ImageView syncImg = null;
	/** 标题栏上的查询图片按钮 */
	public ImageView queryImg = null;

	/** 标题栏编辑图标 */
	public ImageView editImg = null;
	/** 标题栏保存图标 */
	public ImageView saveImg = null;
	/** 标题栏流程图图标 */
	// public ImageView processImg = null;

	/** 放置快捷键的TableRow，包括（刷新，计算器，查找······）。这三个快捷按钮需要权限控制 */
	public TableRow refurbish_tb, counterTableRow, queryTableRow;
	/** 定义一个布局参数 */
	public RelativeLayout.LayoutParams paramsRelativeLayout;

	/** 是否显示刷新按钮，默认为false */
	private boolean refurbishbool = false;
	/** 是否显示计算器，默认值为false */
	private boolean showCounter = false;

	/** 是否显示快捷菜单查询，默认值为false */
	private boolean showQuery = false;
	/** 是否显示编辑按钮，默认值为false */
	private boolean showEdit = false;

	/** 是否具有添加任务的功能，默认为false，如果为true则查询功能变为任务添加功能 */
	public boolean isAddTask = false;
	/** 是否是任务编辑 */
	public boolean showedittask = false;
	/** 是否是添加企业 */
	public boolean isAddCompany = false;
	/** 是否显示添加企业 */
	private final boolean isShowAddCompany = false;

	/** 全局查找的权限 */
	private final String QJCZ_QX = "vmob15A";

	/** 法律法规，应急手册，全局搜索 */
	TableRow fagui_tb, yingji_tb;

	/** 滑动的Layout */
	private SlideLayout slideLayout;
	/** 排序的ListView */
	private ListView sortListView;
	/** dialog中listView绑定数据 */
	public ArrayList<HashMap<String, Object>> listData;
	private TextView username;
	private String userName;

	/**
	 * 基础中间布局，以供子类填充
	 */
	protected LinearLayout middleLayout;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//		requestWindowFeature(Window.FEATURE_NO_TITLE);
		OtherTools.showLog("---------------"
				+ getClass().getPackage().getName() + "."
				+ this.getClass().getSimpleName() + "---------------");
	}
	
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		// TODO Auto-generated method stub
		super.onConfigurationChanged(newConfig);
	}

	/**
	 * Description: 设置标题布局是否可见
	 * 
	 * @param isShow
	 *            是否可见参数，true：可见，false：不可见 void
	 * @author 王红娟 Create at: 2012-12-5 下午01:53:41
	 */
	public void setTitleLayoutVisiable(boolean isShow) {
		if (!isShow) {
			relativeLayoutTitle.setVisibility(View.GONE);
		} else {
			relativeLayoutTitle.setVisibility(View.VISIBLE);
		}
	}

	/**
	 * Description: 设置快捷菜单是否可见
	 * 
	 * @param isShow
	 *            ture：可见，false：不可见 void
	 * @author 王红娟 Create at: 2012-12-5 下午01:54:29
	 */
	public void setSynchronizeButtonVisiable(boolean isShow) {
		if (!isShow) {
			syncImg.setVisibility(View.INVISIBLE);
			// syncImg.setVisibility(View.VISIBLE);
			Log.i("SSSS", "meiyou");
		} else {
			syncImg.setVisibility(View.VISIBLE);
		}
	}

	/**
	 * Description: 设置标题栏查询按钮是否可见--Search
	 * 
	 * @param isShow
	 *            ture：可见，false：不可见 void
	 * @author 王红娟 Create at: 2012-12-5 下午01:56:02
	 */
	public void setSearchButtonVisiable(boolean isShow) {
		if (!isShow) {
			queryImg.setVisibility(View.GONE);
		} else {
			queryImg.setVisibility(View.VISIBLE);
		}
	}

	/**
	 * Description: 设置计算器菜单是否可见
	 * 
	 * @param isShow
	 *            ture：可见，false：不可见 void
	 * @author 王红娟 Create at: 2012-12-5 下午01:56:47
	 */
	public void setCounterButtonVisiable(boolean isShow) {
		showCounter = isShow;
	}

	/**
	 * Description: 设置刷新按钮是否可见
	 * 
	 * @param isShow
	 *            ture：可见，false：不可见 void
	 * @author 王红娟 Create at: 2012-12-5 下午01:57:20
	 */
	public void setrefurbishButtonVisiable(boolean isShow) {
		refurbishbool = isShow;
	}

	/*
	 * 知识库，目前没用了 public void setknowledgeButtonVisiable(boolean isShow) {
	 * showKnowledge = isShow; }
	 */

	/**
	 * Description: 设置快捷菜单查询--Query
	 * 
	 * @param isShow
	 *            ture：可见，false：不可见 void
	 * @author 王红娟 Create at: 2012-12-5 下午01:57:20
	 */
	public void setQueryButtonVisiable(boolean isShow) {
		if (DisplayUitl.getAuthority(QJCZ_QX)) {
			showQuery = isShow;
		}

	}

	/**
	 * Description:设置是否显示编辑按钮
	 * 
	 * @param isShow
	 * @author 王红娟 Create at: 2012-12-6 上午11:21:42
	 */
	public void setEditButtonVisiable(boolean isShow) {
		showEdit = isShow;
		editImg.setVisibility(isShow ? View.VISIBLE : View.GONE);
	}

	/**
	 * Description:编辑按钮添加监听事件
	 * 
	 * @param clickListener
	 * @author 王红娟 Create at: 2012-12-6 下午1:05:29
	 */
	public void setEditButtonListener(OnClickListener clickListener) {
		editImg.setOnClickListener(clickListener);
	}

	/**
	 * 设置标题的简易方法
	 * 
	 * @param title
	 */
	public void SetBaseStyle(String title) {
		setContentView(R.layout.ui_mapuni);
		SetBaseStyle((RelativeLayout) findViewById(R.id.parentLayout), title);
		middleLayout = (LinearLayout) findViewById(R.id.middleLayout);
	}

	/**
	 * Description: 创建顶部标题基础布局
	 * 
	 * LinearLayout(top) RelativeLayout TextView(title) Button(syncBtn)
	 * RelativeLayout LinearLayout(top)
	 * 
	 * @param mapuniLayout
	 *            布局
	 * @param title
	 *            要显示的标题名称 void
	 * @author 王红娟 Create at: 2012-12-5 下午01:58:53
	 */
	public void SetBaseStyle(RelativeLayout mapuniLayout, String title) {
		mapuniLinearLayout = mapuniLayout;

		/** 顶部父容器 */
		LinearLayout topLayout = (LinearLayout) mapuniLinearLayout
				.findViewById(R.id.topLayout);
		topLayout.setGravity(Gravity.CENTER_VERTICAL);
		topLayout.setBackgroundResource(R.color.topbar_background);
		;
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		params.gravity = Gravity.CENTER_VERTICAL;
		int queryImgId = 156189146;
		int syncImgId = 156189147;
		int titleId = 156189148;

		/** 最顶部子布局容器 */
		relativeLayoutTitle = new RelativeLayout(this);
		relativeLayoutTitle.setPadding(15, 0, 0, 0);

		relativeLayoutTitle.setGravity(RelativeLayout.CENTER_IN_PARENT);
		topLayout.addView(relativeLayoutTitle, params);

		/** 标题文本 */
		paramsRelativeLayout = new RelativeLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		paramsRelativeLayout.addRule(RelativeLayout.CENTER_IN_PARENT);
		// paramsRelativeLayout.addRule(RelativeLayout.CENTER_VERTICAL);
		// paramsRelativeLayout.addRule(RelativeLayout.LEFT_OF, queryImgId);

		TextView titleTextView = new TextView(this);
		titleTextView.setText(title);
		titleTextView.setId(titleId);
		titleTextView.setTextColor(Color.WHITE);
		titleTextView.setTextSize(20.0f);
		titleTextView.setMaxLines(1);
		titleTextView.setEllipsize(TruncateAt.END);
		/** 加粗 */
		// titleTextView.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
		/** 加粗--方法同上 */
		// titleTextView.getPaint().setFakeBoldText(true);
		titleTextView.setGravity(Gravity.CENTER_VERTICAL);
		LinearLayout.LayoutParams ll = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.WRAP_CONTENT,
				LinearLayout.LayoutParams.FILL_PARENT);
		ll.gravity = Gravity.CLIP_VERTICAL;
		relativeLayoutTitle.addView(titleTextView, paramsRelativeLayout);

		/** 操作按钮 */
		paramsRelativeLayout = new RelativeLayout.LayoutParams(80, 80);
		paramsRelativeLayout.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
		paramsRelativeLayout.setMargins(0, 0, 80, 0);
		paramsRelativeLayout.addRule(RelativeLayout.CENTER_VERTICAL);

		// paramsRelativeLayout.addRule(RelativeLayout.CENTER_VERTICAL);
		// paramsRelativeLayout.addRule(RelativeLayout.LEFT_OF, syncImgId);

		queryImg = new ImageView(this);
		queryImg.setId(queryImgId);
		/** 添加任务图标 */
		if (isAddTask) {
			queryImg.setImageDrawable(getResources().getDrawable(
					R.drawable.base_add_task_new));
		} else {
			/** 否则，查询图标 */
			queryImg.setImageDrawable(getResources().getDrawable(
					R.drawable.base_icon_menu_query_new));
		}
		queryImg.setVisibility(View.INVISIBLE);
		queryImg.setPadding(10, 7, 10, 7);
		relativeLayoutTitle.addView(queryImg, paramsRelativeLayout);

		// editImg = new ImageView(this);
		// editImg.setId(queryImgId);
		// editImg.setImageResource(R.drawable.base_icon_edit);
		// editImg.setVisibility(View.GONE);
		// relativeLayoutTitle.addView(editImg, paramsRelativeLayout);

		paramsRelativeLayout = new RelativeLayout.LayoutParams(80, 80);
		paramsRelativeLayout.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
		paramsRelativeLayout.addRule(RelativeLayout.CENTER_VERTICAL);

		syncImg = new ImageView(this);
		syncImg.setId(syncImgId);
		if (SYNCIAM) {
			syncImg.setVisibility(View.VISIBLE);
		} else {
			syncImg.setVisibility(View.INVISIBLE);
		}
		syncImg.setImageDrawable(getResources().getDrawable(R.drawable.more));
		syncImg.setPadding(10, 5, 10, 5);
		/** 给快捷按钮添加监听事件 */
		syncImg.setOnClickListener(syncBtnListener);
		relativeLayoutTitle.addView(syncImg, paramsRelativeLayout);

		paramsRelativeLayout = new RelativeLayout.LayoutParams(70, 80);
		paramsRelativeLayout.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
		paramsRelativeLayout.addRule(RelativeLayout.CENTER_VERTICAL);
		// paramsRelativeLayout.addRule(RelativeLayout.LEFT_OF, titleId);

		backImg = new ImageView(this);
		if (BACK_ISSHOW) {
			backImg.setVisibility(View.VISIBLE);
		} else {
			backImg.setVisibility(View.GONE);
		}
		backImg.setImageDrawable(getResources().getDrawable(R.drawable.back));
		backImg.setPadding(0, 5, 10, 5);
		/** 给快捷按钮添加监听事件 */
		backImg.setOnClickListener(backBtnListener);
		relativeLayoutTitle.addView(backImg, paramsRelativeLayout);
	}

	private boolean SYNCIAM = false;

	public void SetSYNCIAM(boolean isshow) {
		this.SYNCIAM = isshow;
	}

	private boolean BACK_ISSHOW = true;

	public void setBACK_ISSHOW(boolean isShow) {
		this.BACK_ISSHOW = isShow;
	}

	/**
	 * 快捷菜单功能点击事件返回按钮
	 */
	private final OnClickListener backBtnListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			finish();
		}
	};

	/**
	 * Description: 加载底部功能菜单
	 * 
	 * @param dataBottomMenuList
	 *            底部菜单数据源
	 * @throws ClassNotFoundException
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws IOException
	 *             void
	 * @author 王红娟 Create at: 2012-12-5 下午02:05:20
	 */
	public void loadBottomMenu(
			ArrayList<HashMap<String, Object>> dataBottomMenuList)
			throws ClassNotFoundException, InstantiationException,
			IllegalAccessException, IOException {
		TableLayout bottomMenuTable = new TableLayout(this);

		LinearLayout bottomLayout = (LinearLayout) mapuniLinearLayout
				.findViewById(R.id.bottomLayout);
		bottomLayout.setVisibility(0);
		bottomLayout.addView(bottomMenuTable);

		bottomMenuTable.setGravity(Gravity.BOTTOM);
		bottomMenuTable.setColumnStretchable(1, true);
		TableRow row = new TableRow(this);
		row.setGravity(Gravity.CENTER_HORIZONTAL);
		bottomMenuTable.addView(row);
		ImageView imageView;
		for (int i = 0; i < dataBottomMenuList.size(); i++) {
			Map<String, Object> menuItemHashMap = dataBottomMenuList.get(i);
			String img = (String) menuItemHashMap.get("img");
			final String aimType = (String) menuItemHashMap.get("aimType");
			final String entityclass = (String) menuItemHashMap
					.get("entityclass");
			imageView = new ImageView(this);
			imageView.setId(i);
			int imageID = this.getResources().getIdentifier(img, "drawable",
					this.getPackageName());
			imageView.setImageResource(imageID);

			imageView.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					Class<?> aimClassDefinition = null;
					try {
						aimClassDefinition = Class.forName(aimType);
					} catch (ClassNotFoundException e) {
						ExceptionManager.WriteCaughtEXP(e, "BaseActivity");
						e.printStackTrace();
					}
					Intent intent = new Intent();

					Class<?> entityClassDefinition = null;
					try {
						entityClassDefinition = Class.forName(entityclass);
					} catch (ClassNotFoundException e) {
						ExceptionManager.WriteCaughtEXP(e, "BaseActivity");
						e.printStackTrace();
					}
					IList iList = null;
					try {
						iList = (IList) entityClassDefinition.newInstance();
					} catch (InstantiationException e) {
						ExceptionManager.WriteCaughtEXP(e, "BaseActivity");
						e.printStackTrace();
					} catch (IllegalAccessException e) {
						ExceptionManager.WriteCaughtEXP(e, "BaseActivity");
						e.printStackTrace();
					}

					intent.setClass(BaseActivity.this, aimClassDefinition);
					startActivity(intent);

				}
			});
			row.addView(imageView);
		}
	}

	/**
	 * 快捷计算器的监听事件
	 */
	class counterBtnListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			/*
			 * Intent intent = new Intent(BaseActivity.this,
			 * CalculatorActivity.class); startActivity(intent);
			 */
			dl.cancel();
		}
	};

	/**
	 * 刷新按钮的监听事件
	 */
	class refurbishBtnListener implements OnClickListener {
		@Override
		public void onClick(View v) {
			refurbish();
			dl.cancel();
		}
	};

	/**
	 * 查找快捷键的监听事件
	 */
	class queryBtnListener implements OnClickListener {
		@Override
		public void onClick(View v) {
			queryDate();
			dl.cancel();
		}
	};

	/**
	 * 通讯录快捷键的监听事件
	 */
	class communicateBtnListener implements OnClickListener {
		@Override
		public void onClick(View v) {
			dl.dismiss();
			/*
			 * Intent intent=new Intent();
			 * intent.setClassName("com.mapuni.android.MobileEnforcement",
			 * "com.mapuni.android.MobileEnforcement.MainActivity");
			 * 
			 * SharedPreferences mPreferences =
			 * BaseActivity.this.getSharedPreferences("app_info",
			 * Context.MODE_WORLD_WRITEABLE); HashMap<String, String> appInfoMap
			 * = (HashMap<String, String>) mPreferences.getAll();
			 * intent.putExtra("appinfo", appInfoMap);
			 * 
			 * intent.putExtra("ywl", "通讯录"); intent.putExtra("isbase", true);
			 * startActivity(intent);
			 */

			Intent intent;
			BaseUsers users = new BaseUsers(BaseActivity.this);
			;
			HashMap<String, Object> filterMap = new HashMap<String, Object>();
			filterMap.put("cmy", "");
			users.setFilter(filterMap);
			Bundle bundle = new Bundle();
			bundle.putBoolean("IsShowTitle", true);
			bundle.putSerializable("BusinessObj", users);
			intent = new Intent(BaseActivity.this, QueryListActivity.class);
			intent.putExtras(bundle);
			startActivity(intent);
			dl.cancel();
		}
	};

	/**
	 * 返回主页快捷键的监听事件
	 */
	class backmainBtnListener implements OnClickListener {
		@Override
		public void onClick(View v) {
			dl.cancel();
			Intent intent = new Intent("com.mapuni.android.workservice");
			/** 发送广播 */
			BaseActivity.this.sendBroadcast(intent);
		}
	};

	/**
	 * 法规标准快捷键的监听事件
	 */
	class LawBtnListener implements OnClickListener {
		@Override
		public void onClick(View v) {
			dl.dismiss();
			/** 法规标准 */
			Intent intent = new Intent(BaseActivity.this, GridActivity.class);
			intent.putExtra("isShortcut", true);
			intent.putExtra("ywl", "FLFGXX");
			intent.putExtra("packageName", "helper");
			Bundle nextbundle = new Bundle();
			nextbundle.putBoolean("IsShowTitle", true);
			nextbundle.putBoolean("IsMain", false);
			nextbundle.putBoolean(ListActivity.IS_CONTENT_VISIBLE, true);
			nextbundle.putBoolean(ListActivity.IS_DATE_VISIBLE, false);
			intent.putExtras(nextbundle);
			intent.setAction("FGBZ");
			startActivity(intent);
		}
	};

	/**
	 * 应急手册快捷键的监听事件
	 */
	class YinjiBtnListener implements OnClickListener {
		@Override
		public void onClick(View v) {
			/** 应急手册 */
			dl.dismiss();
			Intent intent = new Intent(BaseActivity.this, GridActivity.class);
			intent.putExtra("isShortcut", true);
			intent.putExtra("ywl", "YJSCXX");
			intent.putExtra("packageName", "helper");
			Bundle nextbundle = new Bundle();
			nextbundle.putBoolean("IsShowTitle", true);
			nextbundle.putBoolean("IsMain", false);
			nextbundle.putBoolean(ListActivity.IS_CONTENT_VISIBLE, true);
			nextbundle.putBoolean(ListActivity.IS_DATE_VISIBLE, false);
			intent.putExtras(nextbundle);
			intent.setAction("FGBZ");
			startActivity(intent);
		}
	};

	/**
	 * 添加手册快捷键的监听事件
	 */
	class addCompanyBtnListener implements OnClickListener {
		@Override
		public void onClick(View v) {
			// Intent intent = new Intent();
			// intent.setClass(BaseActivity.this, AddCompanyActivity.class);
			// startActivity(intent);
		}
	};

	/**
	 * 全局搜索快捷键的监听事件
	 */
	class queryAllBtnListener implements OnClickListener {
		@Override
		public void onClick(View v) {
			if (dl != null) {
				dl.dismiss();
			}
			// Intent intent = new Intent();
			// intent.setClassName("com.mapuni.android.MobileEnforcement",
			// "com.mapuni.android.ui.GlobalSearchActivity");
			// startActivity(intent);
		}
	};

	/**
	 * 执法百事通快捷键的监听事件
	 */
	class LawKnowBtnListener implements OnClickListener {
		@Override
		public void onClick(View v) {
			dl.dismiss();
			Intent intent = new Intent();
			intent.setClassName("com.mapuni.android.MobileEnforcement",
					"com.mapuni.android.helper.LawKnowAllActivity");
			startActivity(intent);
		}
	};

	/**
	 * 系统管理快捷键的监听事件
	 */
	class settingBtnListener implements OnClickListener {
		@Override
		public void onClick(View v) {
			dl.dismiss();
			Intent intent = new Intent(BaseActivity.this, GridActivity.class);
			Bundle bundle = new Bundle();
			intent.putExtra("isShortcut", true);
			intent.putExtra("ywl", "QTXX");
			intent.putExtra("packageName", "setting");
			bundle.putBoolean("IsShowTitle", true);
			bundle.putBoolean(ListActivity.IS_CONTENT_VISIBLE, true);
			bundle.putBoolean(ListActivity.IS_DATE_VISIBLE, true);
			bundle.putBoolean(ListActivity.IS_RIGHTICON_VISIBLE, true);
			bundle.putBoolean(ListActivity.IS_LEFTICON_VISIBLE, true);
			intent.putExtras(bundle);
			startActivity(intent);
		}
	};

	/**
	 * 查找
	 */
	public void queryDate() {

	}

	/**
	 * 刷新
	 */
	public void refurbish() {

	}

	/**
	 * 快捷菜单功能点击事件
	 */
	private final OnClickListener syncBtnListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			initListData();
			slideLayout.snapToScreen(1, false);
		}
	};

	/**
	 * Descreption:更多_登录信息界面
	 * 
	 * @1.用来初始化自定义滑动的数据及绑定
	 * @2.绑定用户登录，将用户名传值到滑动的listview的头部
	 * @author 钟学梅
	 * */
	public void initListData() {
		/** 初始化自定义滑动组件 */
		slideLayout = (SlideLayout) findViewById(R.id.slidelayout);
		sortListView = (ListView) findViewById(R.id.sort_listview);
		/** 获取用户登录的名称，传给更多_登录里面的username */
		username = (TextView) findViewById(R.id.username);
		userName = Global.getGlobalInstance().getUsername();
		username.setText(userName);

		/** 绑定listview里的数据 */
		listData = new ArrayList<HashMap<String, Object>>();
		listData = getMoreMenu("style_MoreMenu.xml", "item");

		ArrayList<HashMap<String, Object>> moreMenu = new ArrayList<HashMap<String, Object>>();

		sortListView.setAdapter(new SortAdapter(BaseActivity.this, listData));

		sortListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				switch (position) {
				case 0:
					try {

						// 数据同步
						BaseDataSync dataSync = (BaseDataSync) BaseObjectFactory
								.createBaseObject(BaseDataSync.BusinessClassName);
						/** 初始化数据bundle，将bundle装入Intent传入跳转页面 */
						Bundle bundle = new Bundle();
						bundle.putBoolean("IsShowTitle", true);
						bundle.putBoolean("IsShowSyncBtn", false);
						bundle.putString("TitleText", "同步数据");
						bundle.putSerializable("BusinessObj", dataSync);
						bundle.putBoolean("isShowCheckBox", true);
						bundle.putBoolean("isShowDate", false);
						bundle.putBoolean("isShowRighticon", false);

						Intent intent = new Intent(BaseActivity.this,
								DataSyncActivity.class);
						intent.putExtras(bundle);
						startActivity(intent);

					} catch (ClassNotFoundException e) {
						ExceptionManager.WriteCaughtEXP(e, "BaseActivity");
					} catch (IllegalAccessException e) {
						ExceptionManager.WriteCaughtEXP(e, "BaseActivity");
					} catch (InstantiationException e) {
						ExceptionManager.WriteCaughtEXP(e, "BaseActivity");
					}

					break;
				case 1:
					// 通讯录
					/*
					 * Intent intent; BaseUsers users = new
					 * BaseUsers(BaseActivity.this); HashMap<String, Object>
					 * filterMap = new HashMap<String, Object>();
					 * filterMap.put("cmy", ""); users.setFilter(filterMap);
					 * Bundle bundle = new Bundle();
					 * bundle.putBoolean("IsShowTitle", true);
					 * bundle.putSerializable("BusinessObj", users); intent =
					 * new Intent(BaseActivity.this, QueryListActivity.class);
					 * intent.putExtras(bundle); startActivity(intent); break;
					 */
					Intent intent5 = new Intent(BaseActivity.this,
							GridActivity.class);
					Bundle bundle5 = new Bundle();
					intent5.putExtra("isShortcut", true);
					intent5.putExtra("ywl", "QTXX");
					intent5.putExtra("packageName", "setting");
					bundle5.putBoolean("IsShowTitle", true);
					bundle5.putBoolean(ListActivity.IS_CONTENT_VISIBLE, true);
					bundle5.putBoolean(ListActivity.IS_DATE_VISIBLE, true);
					bundle5.putBoolean(ListActivity.IS_RIGHTICON_VISIBLE, true);
					bundle5.putBoolean(ListActivity.IS_LEFTICON_VISIBLE, true);
					intent5.putExtras(bundle5);
					startActivity(intent5);
					break;

				case 2:
					Intent intent2 = new Intent();
					intent2.setClassName(
							"com.mapuni.android.MobileEnforcement",
							"com.mapuni.android.enterpriseArchives.AddBusinessActivity");
					startActivity(intent2);
					break;
				//
				// Intent intent2 = new Intent(BaseActivity.this,
				// GridActivity.class);
				// intent2.putExtra("isShortcut", true);
				// intent2.putExtra("ywl", "FLFGXX");
				// intent2.putExtra("packageName", "helper");
				// Bundle nextbundle = new Bundle();
				// nextbundle.putBoolean("IsShowTitle", true);
				// nextbundle.putBoolean("IsMain", false);
				// nextbundle
				// .putBoolean(ListActivity.IS_CONTENT_VISIBLE, true);
				// nextbundle.putBoolean(ListActivity.IS_DATE_VISIBLE,
				// false);
				// intent2.putExtras(nextbundle);
				// intent2.setAction("FGBZ");
				// startActivity(intent2);
				// break;
				/** 环境监察执法手册文件目录 */
				/*
				 * String fjpath = Global.HJJCZFSC_PATH; File files = new
				 * File(fjpath); String fjs[] = files.list(); if (fjs == null) {
				 * Toast.makeText(BaseActivity.this, "法律法规标准文件夹为空！", 0) .show();
				 * break; } else { Intent intent2 = new Intent();
				 * intent2.setClassName( "com.mapuni.android.MobileEnforcement",
				 * "com.mapuni.android.uitl.LNFLFGExpandListActivity");
				 * startActivity(intent2); break; }
				 */

				// case 3:
				// // 应急手册
				// Intent intent3 = new Intent(BaseActivity.this,
				// GridActivity.class);
				// intent3.putExtra("isShortcut", true);
				// intent3.putExtra("ywl", "YJSCXX");
				// intent3.putExtra("packageName", "helper");
				// Bundle nextbundle1 = new Bundle();
				// nextbundle1.putBoolean("IsShowTitle", true);
				// nextbundle1.putBoolean("IsMain", false);
				// nextbundle1.putBoolean(ListActivity.IS_CONTENT_VISIBLE,
				// true);
				// nextbundle1.putBoolean(ListActivity.IS_DATE_VISIBLE,
				// false);
				// intent3.putExtras(nextbundle1);
				// intent3.setAction("FGBZ");
				// startActivity(intent3);
				// break;

				case 3:
					/*
					 * // 执法百事通 Intent intent6 = new Intent();
					 * intent6.setClassName(
					 * "com.mapuni.android.MobileEnforcement",
					 * "com.mapuni.android.helper.LawKnowAllActivity");
					 * startActivity(intent6);
					 * 
					 * break;
					 */
					// 系统设置
					/*
					 * Intent intent5 = new Intent(BaseActivity.this,
					 * GridActivity.class); Bundle bundle5 = new Bundle();
					 * intent5.putExtra("isShortcut", true);
					 * intent5.putExtra("ywl", "QTXX");
					 * intent5.putExtra("packageName", "setting");
					 * bundle5.putBoolean("IsShowTitle", true);
					 * bundle5.putBoolean(ListActivity.IS_CONTENT_VISIBLE,
					 * true); bundle5.putBoolean(ListActivity.IS_DATE_VISIBLE,
					 * true);
					 * bundle5.putBoolean(ListActivity.IS_RIGHTICON_VISIBLE,
					 * true);
					 * bundle5.putBoolean(ListActivity.IS_LEFTICON_VISIBLE,
					 * true); intent5.putExtras(bundle5);
					 * startActivity(intent5); break;
					 */
					break;
				case 4:
					// 系统设置
					/*
					 * Intent intent5 = new Intent(BaseActivity.this,
					 * GridActivity.class); Bundle bundle5 = new Bundle();
					 * intent5.putExtra("isShortcut", true);
					 * intent5.putExtra("ywl", "QTXX");
					 * intent5.putExtra("packageName", "setting");
					 * bundle5.putBoolean("IsShowTitle", true);
					 * bundle5.putBoolean(ListActivity.IS_CONTENT_VISIBLE,
					 * true); bundle5.putBoolean(ListActivity.IS_DATE_VISIBLE,
					 * true);
					 * bundle5.putBoolean(ListActivity.IS_RIGHTICON_VISIBLE,
					 * true);
					 * bundle5.putBoolean(ListActivity.IS_LEFTICON_VISIBLE,
					 * true); intent5.putExtras(bundle5);
					 * startActivity(intent5);
					 */
					/*
					 * Intent intent5 = new Intent(); intent5.setClassName(
					 * "com.mapuni.android.MobileEnforcement",
					 * "com.mapuni.android.setting.QTXX");
					 * startActivity(intent5);
					 */
					break;

				default:
					finish();
					break;
				}
				slideLayout.snapToScreen(0, false);
			}

		});

	}

	/**
	 * Description: 快捷菜单弹出来的Dialog
	 * 
	 * void
	 * 
	 * @author 王红娟 Create at: 2012-12-5 下午02:08:19 修改时间：2013-04-17
	 */
	public void dialog() {

		LayoutInflater li = LayoutInflater.from(BaseActivity.this);
		View va = li.inflate(R.layout.base_dialog, null);
		dl = new Dialog(BaseActivity.this, R.style.FullScreenDialog);
		/** 读取更多菜单的配置文件 */
		ArrayList<HashMap<String, Object>> moreMenu = getMoreMenu(
				"style_MoreMenu.xml", "item");
		/** 计数器 */
		int count = 0;
		/** dialog布局 */
		LinearLayout gd_menu = (LinearLayout) va.findViewById(R.id.icon_gd);
		/** 得到更多菜单的信息进行循环添加到dialog的布局中，并且添加监听事件 */
		for (HashMap<String, Object> menu : moreMenu) {
			if (menu.get("menuname").toString().equals("添加企业")
					&& !isShowAddCompany
					|| menu.get("menuname").toString().equals("计算器")
					&& !showCounter
					|| menu.get("menuname").toString().equals("刷新")
					&& !refurbishbool) {
				continue;
			}
			/** 如果是第一个按钮则不添加分割线视图 */
			if (count != 0) {
				View view = new View(this);
				view.setBackgroundResource(R.drawable.base_pop_line);
				view.setLayoutParams(new LinearLayout.LayoutParams(
						LinearLayout.LayoutParams.WRAP_CONTENT, 1,
						(float) 1 / 48));
				gd_menu.addView(view);
			}

			TableRow tableRow = new TableRow(this);
			tableRow.setOrientation(LinearLayout.HORIZONTAL);
			tableRow.setLayoutParams(new LinearLayout.LayoutParams(
					LinearLayout.LayoutParams.FILL_PARENT, 12, (float) 1.0));
			tableRow.setGravity(Gravity.CENTER_VERTICAL);
			LinearLayout linearLayout = new LinearLayout(this);
			linearLayout.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
			linearLayout.setPadding(15, 15, 0, 0);
			ImageView leftimg = new ImageView(this);
			leftimg.setImageBitmap(geBitmaptRes(menu.get("lefticon").toString()));
			linearLayout.addView(leftimg);
			try {
				/** 通过得到监听器的名字，得到相对应的监听内部类对象 */
				try {
					linearLayout.setOnClickListener((OnClickListener) Class
							.forName(
									"com.mapuni.android.base.BaseActivity$"
											+ menu.get("listenername")
													.toString())
							.getDeclaredConstructors()[0].newInstance(this));
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				} catch (SecurityException e) {
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					e.printStackTrace();
				}
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (InstantiationException e) {
				e.printStackTrace();
			}

			TextView textView = new TextView(this);
			textView.setText(menu.get("menuname").toString());
			textView.setTextColor(getColorRes(menu.get("textcolor").toString()));
			textView.setTextSize(Float.parseFloat(menu.get("textsize")
					.toString()));
			/** 加粗--方法同上 */
			textView.getPaint().setFakeBoldText(
					Boolean.parseBoolean(menu.get("fakeboldtext").toString()));
			linearLayout.addView(textView);

			tableRow.addView(linearLayout);
			gd_menu.setPadding(0, 15, 0, 15);
			gd_menu.addView(tableRow);
			count++;
		}
		gd_menu.setLayoutParams(new LinearLayout.LayoutParams(250, count * 80,
				(float) 0.0));

		/** 设置dialog的位置 */
		Window win = dl.getWindow();
		android.view.WindowManager.LayoutParams params = new android.view.WindowManager.LayoutParams();
		/** 获取手机分辨率 */
		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		/** 算出手机的高宽 */
		int width = dm.widthPixels;
		int height = dm.heightPixels;

		LinearLayout toplayout = (LinearLayout) findViewById(R.id.topLayout);
		int topHeight = toplayout.getHeight();

		params.x = width / 2 - 125 - 10 + 300;
		params.y = (int) -((double) height / 2 - topHeight - count * 80 / 2 - (topHeight - 58) * 1);
		// params.y=-470;
		win.setAttributes(params);
		dl.setCanceledOnTouchOutside(true);
		dl.setContentView(va);
		dl.show();

		if (!Global.getGlobalInstance().isShowGd) {
			System.out.println(Global.getGlobalInstance().isShowGd
					+ "是否显示更多里面的子功能");
			fagui_tb.setVisibility(View.GONE);
			yingji_tb.setVisibility(View.GONE);

		}
	}

	/**
	 * Description: 由子类退出方法调用 void
	 * 
	 * @author 王红娟 Create at: 2012-12-5 下午02:25:21
	 */
	public void myExit() {
		Intent intent = new Intent();
		intent.setAction("EXIT");
		this.sendBroadcast(intent);
		finish();
	}

	/**
	 * Description: 获取更多菜单配置文件数据
	 * 
	 * @param xml
	 *            xml文件的名称
	 * @param nodeFather
	 *            根节点
	 * @return ArrayList<HashMap<String, Object>>
	 * @author 王红娟 Create at: 2013-4-15 上午11:20:34
	 */
	protected ArrayList<HashMap<String, Object>> getMoreMenu(String xml,
			String nodename) {
		ArrayList<HashMap<String, Object>> MoreMenu = null;
		InputStream stream = null;
		try {
			stream = BaseActivity.this.getResources().getAssets().open(xml);
			MoreMenu = XmlHelper.getList(stream, nodename);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (DocumentException e) {
			e.printStackTrace();
		}
		return MoreMenu;

	}

	/**
	 * Description: 获取列表的图片
	 * 
	 * @param name
	 *            照片的名字
	 * @return 返回所需照片 Bitmap
	 * @author xgf Create at: 2012-11-30 上午11:30:37
	 */
	public Bitmap geBitmaptRes(String name) {
		ApplicationInfo appInfo = this.getApplicationInfo();
		int resID = this.getResources().getIdentifier(name, "drawable",
				appInfo.packageName);
		return BitmapFactory.decodeResource(this.getResources(), resID);
	}

	/**
	 * Description: 获取颜色的资源id
	 * 
	 * @param name
	 *            颜色的名字
	 * @return 返回所需颜色资源id int
	 * @author 王红娟 Create at: 2012-11-30 上午11:30:37
	 */
	public int getColorRes(String name) {
		ApplicationInfo appInfo = this.getApplicationInfo();
		int resID = this.getResources().getIdentifier(name, "color",
				appInfo.packageName);
		return resID;
	}

	@Override
	protected void onStart() {
		super.onStart();
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	protected void onStop() {
		super.onStop();
	}

	@Override
	protected void onPause() {
		super.onPause();
	}

	@Override
	protected void onRestart() {
		super.onRestart();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public void finish() {
		super.finish();
		overridePendingTransition(R.anim.left_in, R.anim.left_out);
	}

	@Override
	public void startActivity(Intent intent) {
		super.startActivity(intent);
		overridePendingTransition(R.anim.right_in, R.anim.right_out);
	}

}
