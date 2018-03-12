package cn.com.mapuni.meshing.base;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;

import org.dom4j.DocumentException;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnCreateContextMenuListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import cn.com.mapuni.meshing.base.R;
import cn.com.mapuni.meshing.base.adapter.SortAdapter;
import cn.com.mapuni.meshing.base.business.BaseDataSync;
import cn.com.mapuni.meshing.base.business.BaseObjectFactory;
import cn.com.mapuni.meshing.base.dataprovider.XmlHelper;
import cn.com.mapuni.meshing.base.util.DisplayUitl;
import cn.com.mapuni.meshing.base.util.ExceptionManager;


public class BaseDialog extends Dialog implements DialogInterface, Window.Callback, KeyEvent.Callback, OnCreateContextMenuListener {

	private final String TAG = "BaseDialog";

	/** 定义该BaseDialog所需布局 */
	public RelativeLayout mapuniLinearLayout = null;
	private RelativeLayout relativeLayoutTitle = null;
	/** 定义一个布局参数 */
	public RelativeLayout.LayoutParams paramsRelativeLayout;

	/** 右上角的快捷按钮 */
	public ImageView syncImg = null;
	/** 标题栏上的查询图片按钮 */
	public ImageView queryImg = null;

	/** 标题栏编辑图标 */
	public ImageView editImg = null;
	/** 标题栏保存图标 */
	public ImageView saveImg = null;

	/** 是否具有添加任务的功能，默认为false，如果为true则查询功能变为任务添加功能 */
	public boolean isAddTask = false;

	private Context mContext;
	/** 添加企业权限 */
	private final String TJQY_QX = "vmob10A1B";
	/** 全局查找的权限 */
	private final String QJCZ_QX = "vmob15A";

	/** 是否显示刷新按钮，默认为false */
	private boolean refurbishbool = false;
	/** 是否显示计算器，默认值为false */
	private boolean showCounter = false;
	/** 是否显示知识库，默认值为false。目前版本没用了 */
	private final boolean showKnowledge = true;
	/** 是否显示快捷菜单查询，默认值为false */
	private boolean showQuery = false;
	/** 是否显示编辑按钮，默认值为false */
	private final boolean showEdit = false;
	/** 排序的ListView */
	private ListView sortListView;
	/** 增加右边滑动栏支持 */
	private SlideLayout slideLayout;
	/** dialog中listView绑定数据 */
	public ArrayList<HashMap<String, Object>> listData;
	private TextView username;
	private String userName;

	public BaseDialog(Context context) {
		this(context, 0);
		this.mContext = context;
	}

	public BaseDialog(Context context, int theme) {
		super(context, theme);
		this.mContext = context;
	}

	protected BaseDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
		super(context, cancelable, cancelListener);
		this.mContext = context;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		/** 初始化自定义滑动组件 */
		slideLayout = (SlideLayout) findViewById(R.id.slidelayout);
		// initListData();
	}

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

		sortListView.setAdapter(new SortAdapter(mContext, listData));

		sortListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
				switch (position) {
				case 0:
					try {

						// 数据同步
						BaseDataSync dataSync = (BaseDataSync) BaseObjectFactory.createBaseObject(BaseDataSync.BusinessClassName);
						/** 初始化数据bundle，将bundle装入Intent传入跳转页面 */
						Bundle bundle = new Bundle();
						bundle.putBoolean("IsShowTitle", true);
						bundle.putBoolean("IsShowSyncBtn", false);
						bundle.putString("TitleText", "同步数据");
						bundle.putSerializable("BusinessObj", dataSync);
						bundle.putBoolean("isShowCheckBox", true);
						bundle.putBoolean("isShowDate", false);
						bundle.putBoolean("isShowRighticon", false);

						Intent intent = new Intent(mContext, DataSyncActivity.class);
						intent.putExtras(bundle);
						mContext.startActivity(intent);

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
					Intent intent5 = new Intent(mContext, GridActivity.class);
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
					mContext.startActivity(intent5);
					break;

				case 2:
					Intent intent2 = new Intent();
					intent2.setClassName("com.mapuni.android.MobileEnforcement", "com.mapuni.android.enterpriseArchives.AddBusinessActivity");

					mContext.startActivity(intent2);
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
					// finish();n
					cancel();
					break;
				}
				slideLayout.snapToScreen(0, false);
			}

		});

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
	protected ArrayList<HashMap<String, Object>> getMoreMenu(String xml, String nodename) {
		ArrayList<HashMap<String, Object>> MoreMenu = null;
		InputStream stream = null;
		try {
			stream = mContext.getResources().getAssets().open(xml);
			MoreMenu = XmlHelper.getList(stream, nodename);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (DocumentException e) {
			e.printStackTrace();
		}
		return MoreMenu;

	}

	@Override
	protected void onStart() {
		super.onStart();
	}

	@Override
	protected void onStop() {
		super.onStop();
	}

	@Override
	public void show() {
		super.show();
	}

	@Override
	public boolean isShowing() {
		return super.isShowing();
	}

	@Override
	public void hide() {
		super.hide();
	}

	@Override
	public void dismiss() {
		super.dismiss();
	}

	@Override
	public void cancel() {
		super.cancel();
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
	 * 快捷菜单功能点击事件
	 */
	/*
	 * private final OnClickListener syncBtnListener = new OnClickListener() {
	 * 
	 * @Override public void onClick(View v) { initListData();
	 * slideLayout.snapToScreen(1, false); } };
	 */
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
		LinearLayout topLayout = (LinearLayout) mapuniLinearLayout.findViewById(R.id.topLayout);
		topLayout.setGravity(Gravity.CENTER_VERTICAL);
		topLayout.setBackgroundDrawable(getContext().getResources().getDrawable(R.drawable.base_bg_title));

		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		params.gravity = Gravity.CENTER_VERTICAL;

		/** 最顶部子布局容器 */
		relativeLayoutTitle = new RelativeLayout(getContext());
		relativeLayoutTitle.setPadding(12, 0, 0, 0);

		relativeLayoutTitle.setGravity(RelativeLayout.CENTER_VERTICAL);
		topLayout.addView(relativeLayoutTitle, params);

		/** 标题文本 */
		paramsRelativeLayout = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		paramsRelativeLayout.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
		paramsRelativeLayout.addRule(RelativeLayout.CENTER_VERTICAL);

		LinearLayout titleLinear = new LinearLayout(getContext());
		titleLinear.setGravity(Gravity.CENTER_VERTICAL);
		TextView TitleTextView = new TextView(getContext());
		TitleTextView.setText(title);
		TitleTextView.setTextColor(Color.WHITE);
		TitleTextView.setTextSize(20.0f);
		/** 加粗 */
		TitleTextView.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
		/** 加粗--方法同上 */
		TitleTextView.getPaint().setFakeBoldText(true);
		TitleTextView.setGravity(Gravity.CENTER_VERTICAL);
		LinearLayout.LayoutParams ll = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.FILL_PARENT);
		ll.gravity = Gravity.CLIP_VERTICAL;
		titleLinear.addView(TitleTextView, ll);
		relativeLayoutTitle.addView(titleLinear, paramsRelativeLayout);

		/** 操作按钮 */
		paramsRelativeLayout = new RelativeLayout.LayoutParams(150, 60);
		paramsRelativeLayout.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
		paramsRelativeLayout.addRule(RelativeLayout.CENTER_VERTICAL);

		queryImg = new ImageView(getContext());
		/** 添加任务图标 */
		if (isAddTask) {
			queryImg.setImageDrawable(getContext().getResources().getDrawable(R.drawable.base_add_task_new));
		} else {
			/** 否则，查询图标 */
			queryImg.setImageDrawable(getContext().getResources().getDrawable(R.drawable.base_icon_menu_query_new));
		}
		queryImg.setPadding(0, 0, 95, 0);
		queryImg.setVisibility(View.INVISIBLE);
		relativeLayoutTitle.addView(queryImg, paramsRelativeLayout);

		editImg = new ImageView(getContext());
		editImg.setImageResource(R.drawable.base_icon_edit);
		editImg.setVisibility(View.GONE);
		relativeLayoutTitle.addView(editImg, paramsRelativeLayout);

		paramsRelativeLayout = new RelativeLayout.LayoutParams(150, 60);
		paramsRelativeLayout.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
		paramsRelativeLayout.addRule(RelativeLayout.CENTER_VERTICAL);

		syncImg = new ImageView(mContext);

		syncImg.setImageDrawable(mContext.getResources().getDrawable(R.drawable.base_icon_menu_more_new));
		// syncImg.setPadding(10, 0, 25, 0);
		syncImg.setPadding(10, 0, 35, 0);
		/** 给快捷按钮添加监听事件 */
		// syncImg.setOnClickListener(syncBtnListener);
		syncImg.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				initListData();
				slideLayout.snapToScreen(1, false);
			}
		});
		relativeLayoutTitle.addView(syncImg, paramsRelativeLayout);

		paramsRelativeLayout = new RelativeLayout.LayoutParams(150, 60);
		paramsRelativeLayout.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
		paramsRelativeLayout.addRule(RelativeLayout.CENTER_VERTICAL);

	}

}
