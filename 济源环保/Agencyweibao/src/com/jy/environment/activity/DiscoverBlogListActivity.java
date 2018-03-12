package com.jy.environment.activity;

import java.io.File;
import java.io.FilenameFilter;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONException;
import org.json.JSONObject;
import org.kymjs.aframe.ui.widget.RoundImageView;

import com.handmark.pulltorefresh.library.ILoadingLayout;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.jy.environment.R;
import com.jy.environment.adapter.DiscoverBlogCityAdapter;
import com.jy.environment.adapter.DiscoverBlogPicGridAdapter;
import com.jy.environment.adapter.DiscoverFaceGridViewAdapter;
import com.jy.environment.base.ActivityBase;
import com.jy.environment.business.BusinessSearch;
import com.jy.environment.controls.AsyncTask;
import com.jy.environment.controls.WeiBaoApplication;
import com.jy.environment.database.dal.CityDB;
import com.jy.environment.model.DeleteBlogCommentModel;
import com.jy.environment.model.DeleteBlogModel;
import com.jy.environment.model.DiscoverBlogUpLoadResult;
import com.jy.environment.model.DiscoverDeleteBlogStatueModel;
import com.jy.environment.model.DiscoverFlagModel;
import com.jy.environment.model.MyPostWeiboInfo;
import com.jy.environment.model.NewsCounts;
import com.jy.environment.model.PicPath;
import com.jy.environment.model.Pinglun;
import com.jy.environment.model.ResultBlogList;
import com.jy.environment.model.ResultPostBlogComment;
import com.jy.environment.model.Weib;
import com.jy.environment.util.BimpHelper;
import com.jy.environment.util.CommonUtil;
import com.jy.environment.util.FileUtils;
import com.jy.environment.util.MyLog;
import com.jy.environment.util.NetUtil;
import com.jy.environment.util.StringUtil;
import com.jy.environment.webservice.UrlComponent;
import com.jy.environment.widget.MyListView;
import com.jy.environment.widget.NoScrollGridView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;
import com.nostra13.universalimageloader.core.assist.SimpleImageLoadingListener;
import com.umeng.analytics.MobclickAgent;

import android.app.Activity;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ImageSpan;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 发现环境说列表页
 * 
 * @author baiyuchuan
 * 
 */
public class DiscoverBlogListActivity extends ActivityBase implements
		OnRefreshListener<ListView>, OnItemClickListener, OnClickListener {
	public static final Pattern EMOTION_URL = Pattern.compile("\\[(\\S+?)\\]");
	private ArrayList<MyPostWeiboInfo> weiboInfos;
	private PullToRefreshListView mListView;
	private ArrayList<String> items = new ArrayList<String>();
	protected ImageLoader imageLoader = ImageLoader.getInstance();
	private int start = 0;
	private String userid;
	private List<Weib> weibs = new ArrayList<Weib>();
	List<Pinglun> pingluns;
	List<Pinglun> pingluns1;
	private PopupWindow localPopupWindow;
	private PopupWindow popupWindow;
	private RelativeLayout screen;
	private int liebiaoposition;
	private int liebiaoposition1;
	private InputMethodManager imm;
	private View view;
	private String observer = "正在发生";;
	private String content;
	private String time;
	private String weiboid;
	private ListView lv_group;
	private List<String> groups;
	private TextView shujunulll;
	private MessageReceiver mMessageReceiver;
	public static final String MESSAGE_RECEIVED_ACTION2 = "com.mapuni.weibo";
	// private ProgressDialog prDialog;
	private WeiBaoApplication mApplication;
	private TextView nickname_title, weibo_difang, weibo_life;
	private RoundImageView login_nc;
	private ImageView tansuo_edit_iv;
	private String difang = "";
	private String nick_name = "";
	private DisplayImageOptions options;
	private DisplayImageOptions options2;
	private ImageView tansuo_spinner, nopicture_View1;
	private String username = "";
	private String userNc = "";
	private WeiBaoApplication application;
	private int width, height;
	private boolean shuju = false;
	private CityDB mCityDB;
	private String dianjicity = "";
	private String dingweicity;
	// 聊天
	private RelativeLayout discover_blog_list_bottom;
	private TextView discover_blog_list_send;
	private EditText discover_blog_list_ed;
	private UpLoadTask upLoadTask = new UpLoadTask();
	private String[] envir = new String[] { "好环境", "一般环境", "差环境" };
	/**
	 * 赞与后台的异步操作
	 */
	private PraiseTask praiseTask;
	/**
	 * 判断是刷新or加载更多
	 */
	private String shua = "";
	/**
	 * 生活圈列表adapter
	 */
	private HbdtAdapter name;
	private String city_name = "";
	private int page = 1;
	private String sertime = "";
	private GetBlogListAsyncTask dasd;
	private String province = "";
	private ImageLoadingListener animateFirstListener;
	private ImageView top_bg, exposure_back;
	private ListView listView;
	private SendBlogCommentTask blogCommentTask;
	private long time1, time2;
	Dialog dialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.discove_blog_list);
		animateFirstListener = new AnimateFirstDisplayListener();
		shujunulll = (TextView) findViewById(R.id.shuju);
		discover_blog_list_bottom = (RelativeLayout) findViewById(R.id.discover_blog_list_bottom);
		discover_blog_list_send = (TextView) findViewById(R.id.discover_blog_list_send);
		discover_blog_list_send.setOnClickListener(this);
		discover_blog_list_ed = (EditText) findViewById(R.id.discover_blog_list_ed);
		screen = (RelativeLayout) findViewById(R.id.screen);
		screen.setOnClickListener(this);
		mListView = (PullToRefreshListView) findViewById(R.id.listView);
		praiseTask = new PraiseTask();
		blogCommentTask = new SendBlogCommentTask();
		listView = mListView.getRefreshableView();
		View viewTop = LayoutInflater.from(DiscoverBlogListActivity.this)
				.inflate(R.layout.discover_top, null);
		listView.addHeaderView(viewTop);
		nopicture_View1 = (ImageView) findViewById(R.id.nopicture_View1);
		mListView.setOnRefreshListener(this);
		imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
		DisplayMetrics metric = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metric);
		width = metric.widthPixels; // 屏幕宽度（像素）
		height = metric.heightPixels; // 屏幕高度（像素）
		userid = WeiBaoApplication.getUserId();
		application = WeiBaoApplication.getInstance();
		username = WeiBaoApplication.getUsename();
		userNc = WeiBaoApplication.getUserNc();
		options = new DisplayImageOptions.Builder()
				.showImageOnLoading(R.drawable.logo31)
				.showImageForEmptyUri(R.drawable.logo31)
				.showImageOnFail(R.drawable.logo31).cacheInMemory(true)
				.cacheOnDisc(true).considerExifParams(true).build();

		options2 = new DisplayImageOptions.Builder()
				.showImageOnLoading(R.drawable.discover_top_bg)
				.showImageForEmptyUri(R.drawable.discover_top_bg)
				.showImageOnFail(R.drawable.discover_top_bg)
				.cacheInMemory(true).cacheOnDisc(true).considerExifParams(true)
				.build();
		mApplication = WeiBaoApplication.getInstance();
		mCityDB = mApplication.getCityDB();
		city_name = WeiBaoApplication.selectedCity;
		province = mCityDB.getprovicecity(city_name);
		dingweicity = mApplication.getDingweicity();
		dasd = new GetBlogListAsyncTask();
		exposure_back = (ImageView) findViewById(R.id.exposure_back);
		exposure_back.setOnClickListener(this);
		tansuo_spinner = (ImageView) viewTop.findViewById(R.id.tansuo_spinner);
		tansuo_spinner.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				showdi(v);
			}

		});
		weibo_difang = (TextView) viewTop.findViewById(R.id.nweibo_difang);
		nickname_title = (TextView) viewTop.findViewById(R.id.nickname_title);
		login_nc = (RoundImageView) viewTop.findViewById(R.id.login_nc);
		top_bg = (ImageView) viewTop.findViewById(R.id.bg);
		login_nc.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				MobclickAgent.onEvent(DiscoverBlogListActivity.this,
						"FXSelfHead");
				if (nickname_title.getText().equals("游客")) {
					Intent loginIntent = new Intent(
							DiscoverBlogListActivity.this,
							UserLoginActivity.class);
					loginIntent.putExtra("from", "bloglist");
					startActivity(loginIntent);
				} else {
					// Intent intentz = new
					// Intent(DiscoverBlogList1Activity.this,
					// UserInfoActivity.class);
					// startActivity(intentz);
					Intent intentz = new Intent(DiscoverBlogListActivity.this,
							DiscoverMySelfBlogListActivity.class);
					startActivity(intentz);

				}

			}
		});
		nickname_title.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if (nickname_title.getText().equals("游客")) {
					Intent loginIntent = new Intent(
							DiscoverBlogListActivity.this,
							UserLoginActivity.class);
					startActivity(loginIntent);
				} else {
					Intent intentz = new Intent(DiscoverBlogListActivity.this,
							UserInfoActivity.class);
					startActivity(intentz);
				}
			}
		});

		weibo_life = (TextView) findViewById(R.id.weibo_life);
		weibo_difang.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				MobclickAgent.onEvent(DiscoverBlogListActivity.this,
						"FXSelectCity");
				mListView
						.setMode(com.handmark.pulltorefresh.library.PullToRefreshBase.Mode.BOTH);
				showdi(v);
			}

		});

		/**
		 * 默认显示天气选中的city
		 */
		difang = mApplication.selectedCity;

		if (!"".equals(difang)) {

			weibo_difang.setText(difang);
		}
		if (!"".equals(nick_name)) {
			nickname_title.setText(nick_name);
		}

		// prDialog = new ProgressDialog(DiscoverBlogListActivity.this);
		// prDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		// prDialog.setMessage("正在努力加载中……");
		//
		// // 进度条是否不明确
		// prDialog.setIndeterminate(true);
		// prDialog.setCancelable(true);
		dialog = CommonUtil.getCustomeDialog(this, R.style.load_dialog,
				R.layout.custom_progress_dialog);
		TextView titleTxtv = (TextView) dialog.findViewById(R.id.dialogText);
		titleTxtv.setText("正在努力加载中……");

		tansuo_edit_iv = (ImageView) findViewById(R.id.tansuo_edit_iv);
		tansuo_edit_iv.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				username = application.getUsename();
				if (username.equals("") || username.equals("游客")) {
					username = "游客";
				}
				/*
				 * if (username.equals("") || username.equals("游客")) {
				 * Toast.makeText(DiscoverBlogListActivity.this,
				 * "只有登录后才能发环境说哦！", 0).show(); new Thread() { public void run()
				 * { try { currentThread().sleep(500); Intent intent = new
				 * Intent( DiscoverBlogListActivity.this,
				 * UserLoginActivity.class); intent.putExtra("from",
				 * "weibolists"); startActivity(intent); } catch
				 * (InterruptedException e) { e.printStackTrace(); } };
				 * }.start();
				 * 
				 * return; }
				 */
				MobclickAgent.onEvent(DiscoverBlogListActivity.this,
						"FXPublish");
				Intent postweibo = new Intent(DiscoverBlogListActivity.this,
						DiscoverPostBlogActivity.class);
				startActivity(postweibo);
			}
		});

		ILoadingLayout startLabels = mListView.getLoadingLayoutProxy(true,
				false);
		startLabels.setPullLabel("");// 刚下拉时，显示的提示
		startLabels.setRefreshingLabel("");// 刷新时
		startLabels.setReleaseLabel("");// 下来达到一定距离时，显示的提示

		// ILoadingLayout endLabels = mListView.getLoadingLayoutProxy(
		// false, true);
		// endLabels.setPullLabel("你可劲拉，拉2...");// 刚下拉时，显示的提示
		// endLabels.setRefreshingLabel("好嘞，正在刷新2...");// 刷新时
		// endLabels.setReleaseLabel("你敢放，我就敢刷新2...");// 下来达到一定距离时，显示的提示

		dialog.show();
		name = new HbdtAdapter();
		name.bindData(weibs, DiscoverBlogListActivity.this);
		mListView.setAdapter(name);
		GetBlogPicAsyncTask asyncTask = new GetBlogPicAsyncTask();
		asyncTask.execute();
		dasd.execute("xx", dianjicity);
		registerMessageReceiver1();
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub
		if (localPopupWindow != null && localPopupWindow.isShowing()) {

			localPopupWindow.dismiss();

			localPopupWindow = null;
		}
		return super.onTouchEvent(event);
	}

	@Override
	protected void onRestart() {
		super.onRestart();
		/**
		 * 当发完微博或者评论结束后重新刷新数据
		 */
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK
				&& event.getAction() == KeyEvent.ACTION_DOWN
				&& discover_blog_list_bottom.getVisibility() == View.VISIBLE) {
			discover_blog_list_bottom.setVisibility(View.GONE);
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {

	}

	@Override
	public void onRefresh(PullToRefreshBase<ListView> refreshView) {
		if (refreshView.isHeaderShown()) {
			loadNextPage("shuxing", dianjicity);
		} else {
			loadNextPage("more", dianjicity);
		}
	}

	private void loadNextPage(String tag, String dianjicity) {
		if (AsyncTask.Status.PENDING == dasd.getStatus()) {
			if (localPopupWindow != null) {
				localPopupWindow.dismiss();
				localPopupWindow = null;
			}
			if (dialog != null) {
				// prDialog.show();
			}
			dasd.execute(tag, dianjicity);
		} else if (AsyncTask.Status.RUNNING == dasd.getStatus()) {
			Toast.makeText(DiscoverBlogListActivity.this,
					this.getResources().getString(R.string.loading_data), 2000)
					.show();
		} else if (AsyncTask.Status.FINISHED == dasd.getStatus()) {
			if (dialog != null) {
				// prDialog.show();
			}
			dasd = new GetBlogListAsyncTask();
			dasd.execute(tag, dianjicity);
		}

	}

	private void ZanPage(String json) {
		if (AsyncTask.Status.PENDING == praiseTask.getStatus()) {
			praiseTask.execute(json);
		} else if (AsyncTask.Status.RUNNING == praiseTask.getStatus()) {
			Toast.makeText(DiscoverBlogListActivity.this, ("数据加载中"), 2000)
					.show();
		} else if (AsyncTask.Status.FINISHED == praiseTask.getStatus()) {
			praiseTask = new PraiseTask();
			praiseTask.execute(json);
		}

	}

	/**
	 * 显示所勾选城市列表
	 * 
	 * @param parent
	 */
	private void showdi(View parent) {
		LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		view = layoutInflater.inflate(R.layout.discove_blog_city_list, null);
		lv_group = (ListView) view.findViewById(R.id.lvGroup);
		// 加载数据
		groups = mCityDB.getall();
		DiscoverBlogCityAdapter groupAdapter = new DiscoverBlogCityAdapter(
				this, groups);
		lv_group.setAdapter(groupAdapter);
		// 创建一个PopuWidow对象
		popupWindow = new PopupWindow(view, width / 4,
				LayoutParams.WRAP_CONTENT);
		// 使其聚集
		popupWindow.setFocusable(true);
		// 设置允许在外点击消失
		popupWindow.setOutsideTouchable(true);
		// 这个是为了点击“返回Back”也能使其消失，并且并不会影响你的背景
		popupWindow.setBackgroundDrawable(new BitmapDrawable());
		WindowManager windowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
		// 显示的位置为:屏幕的宽度的一半-PopupWindow的高度的一半
		int xPos = windowManager.getDefaultDisplay().getWidth() / 2
				- popupWindow.getWidth() / 2;
		popupWindow.showAsDropDown(parent, xPos, 0);
		lv_group.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> adapterView, View view,
					int position, long id) {
				shuju = false;
				dianjicity = groups.get(position);
				weibo_difang.setText(dianjicity);
				city_name = WeiBaoApplication.selectedCity;
				mCityDB = mApplication.getCityDB();
				province = mCityDB.getprovicecity(dianjicity);
				loadNextPage("shuxing", dianjicity);
				if (popupWindow != null) {
					popupWindow.dismiss();
				}

			}
		});

	}

	/**
	 * 
	 * @author baiyuchuan AsyncTask<String, Void, List<Weib>>
	 */
	class GetBlogPicAsyncTask extends AsyncTask<String, Void, String> {

		@Override
		protected String doInBackground(String... params) {
			String _Result = null;
			try {
				BusinessSearch search = new BusinessSearch();
				String url = UrlComponent.getGetBlogPic_Get();
				_Result = search.getBlogPic(url);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return _Result;
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			if (!StringUtil.isEmpty(result)) {
				imageLoader.displayImage(result, top_bg, options2,
						animateFirstListener);
			}
		}

	}

	/**
	 * 加载微博列表数据
	 * 
	 * @author baiyuchuan AsyncTask<String, Void, List<Weib>>
	 */
	class GetBlogListAsyncTask extends AsyncTask<String, Void, List<Weib>> {
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			time1 = System.currentTimeMillis();
		}

		@Override
		protected List<Weib> doInBackground(String... arg0) {
			if (isCancelled()) {
				return null;
			}
			shua = arg0[0];
			String xuancity = arg0[1];
			ResultBlogList _Result = null;
			if (!"".equals(xuancity)) {
				city_name = xuancity;
			}
			if (shua.equals("shuxing")) {
				/**
				 * 如果刷新，那么将所有集合里面的数据清空
				 */
				page = 1;
			} else if (shua.equals("more")) {
				page++;
			}

			BusinessSearch search = new BusinessSearch();
			try {
				String url = "";
				if (city_name.contains("自治州")) {
					url = UrlComponent.getWeiboByInfo_Get(province,
							mCityDB.getSuoSuo(city_name), page);
				} else {
					url = UrlComponent.getWeiboByInfo_Get(province, city_name,
							page);
				}
				MyLog.i("getWeiboByInfo_Get load url:" + url);
				/**
				 * 30秒间隔之内不刷新
				 */
				_Result = search.getBlogList(url, userid);
				MyLog.i("getWeiboByInfo_Get load url getReturn:"
						+ _Result.getWeibs());
				if (shua.equals("shuxing")) {
					weibs.clear();
				}

				/**
				 * weibs 里面增加历史上未提交的数据
				 */
				try {
					if (page == 1) {
						weiboInfos = MyPostWeiboInfo
								.getWeiboInfoHestory(DiscoverBlogListActivity.this);
						if (null != weiboInfos) {
							for (int i = weiboInfos.size(); i > 0; i--) {
								// if (nickname_title.getText().equals("游客")) {
								// break;
								// }
								if (i > 0) {
									MyPostWeiboInfo info = weiboInfos
											.get(i - 1);
									// if
									// (!info.getUser_name().equals(username)) {
									// break;
									// }
									MyLog.i("增加历史 weiboInfos   :"
											+ info.toString());
									Weib weib = new Weib(
											new ArrayList<Pinglun>(), "",
											info.getUser_name(), "",
											info.getUserPic(),
											CommonUtil.getTime(info
													.getSendTime()),
											info.getContent(), info.getIsopen()
													+ "", info.getPaths(),
											info.getPaths(), "0",
											new ArrayList<String>(), 0 + "",
											info.getStar() + "", info.getEnv()
													+ "", info.getUserId(),
											new ArrayList<String>(),
											info.getAddress(), info.getLng(),
											info.getLat(),
											new ArrayList<String>(),
											info.getIsAkey(),
											info.getPollutionType(),
											info.getIsanonymous(), 0, 0, 0);
									weib.setHestory(true);
									weib.setCity(info.getInfo_city());
									MyLog.i("增加历史  weib   :" + weib.toString());
									if (nickname_title.getText().equals("游客")
											&& weib.getUserId().equals("1")) {
										if (city_name.equals(weib.getCity())) {
											weibs.add(weib);
										}
									} else if (mApplication.getUserId().equals(
											weib.getUserId())) {
										if (city_name.equals(weib.getCity())) {
											weibs.add(weib);
										}
									}
								}
							}
						}

					}
				} catch (Exception e) {
					MyLog.e("weibao Exception" + e);
				}
				String city_save = weibo_difang.getText().toString();

				if (null != _Result) {
					if (weibs.size() == 0) {
						weibs.addAll(0, _Result.getWeibs());
					} else {
						weibs.addAll(weibs.size(), _Result.getWeibs());
					}
				}
				return weibs;

			} catch (Exception e) {
				MyLog.e("weibao Exception" + e);
			}
			return weibs;
		}

		@Override
		protected void onPostExecute(List<Weib> result) {
			super.onPostExecute(result);
			try {
				mListView.onRefreshComplete();
				dialog.dismiss();
				weibo_difang.setText(city_name);
				if (null == result || result.size() == 0) {
					result = null;
				}
				if (null == result
						&& NetUtil
								.getNetworkState(DiscoverBlogListActivity.this) == NetUtil.NETWORN_NONE) {
					Toast.makeText(DiscoverBlogListActivity.this, "请检查您的网络", 0)
							.show();
					nopicture_View1.setVisibility(View.GONE);
				} else if (null == result && page > 1) {
					nopicture_View1.setVisibility(View.GONE);
					mListView
							.setMode(com.handmark.pulltorefresh.library.PullToRefreshBase.Mode.DISABLED);
					Toast.makeText(DiscoverBlogListActivity.this, "已经是最后一页", 0)
							.show();
				} else if (null == result && page == 1) {
					nopicture_View1.setVisibility(View.VISIBLE);
					mListView
							.setMode(com.handmark.pulltorefresh.library.PullToRefreshBase.Mode.DISABLED);
					weibs.clear();
					if (null != name) {
						name.notifyDataSetChanged();
					}

					// if (weibs.size() == 0) {
					// weibs.addAll(0, result);
					// } else {
					// weibs.addAll(weibs.size(), result);
					// }
				} else if (null != result) {
					nopicture_View1.setVisibility(View.GONE);
					final String city_save = weibo_difang.getText().toString();
					final List<Weib> results = result;
					for (int i = 0; i < result.size(); i++) {
						// 保存最新消息的时间

						if (i == 0 && page == 1) {
							new Thread(new Runnable() {

								@Override
								public void run() {
									// TODO Auto-generated method stub

									String time_save = results.get(0).getTime();
									List<NewsCounts> counts_all = NewsCounts
											.getNewsCount(DiscoverBlogListActivity.this);
									if (counts_all == null) {
										NewsCounts newfirst = new NewsCounts();
										newfirst.setCity(city_save);
										newfirst.setTime(time_save);
										NewsCounts.saveInfo(
												DiscoverBlogListActivity.this,
												newfirst);
									} else {
										for (int j = 0; j < counts_all.size(); j++) {
											NewsCounts news = counts_all.get(j);
											if (news.getCity()
													.equals(city_save)) {
												NewsCounts
														.deleteInfo(
																DiscoverBlogListActivity.this,
																news);
												NewsCounts newfirst = new NewsCounts();
												newfirst.setTime(time_save);
												newfirst.setCity(city_save);
												NewsCounts
														.saveInfo(
																DiscoverBlogListActivity.this,
																newfirst);
												break;
											}
											if (j == (counts_all.size() - 1)
													&& !news.getCity().equals(
															city_save)) {
												NewsCounts newfirst = new NewsCounts();
												newfirst.setCity(city_save);
												newfirst.setTime(time_save);
												NewsCounts
														.saveInfo(
																DiscoverBlogListActivity.this,
																newfirst);
											}

										}
									}
								}
							}).start();
						}
					}

					// TODO Auto-generated method stub

					if (null == result || (result.size() == 0 && !shuju)) {
						mListView.setVisibility(View.GONE);
						shujunulll.setVisibility(View.VISIBLE);
						return;
					} else {
						mListView.setVisibility(View.VISIBLE);
						shujunulll.setVisibility(View.GONE);
						shuju = true;
					}

					if (shua.equals("more")) {
						if (name != null) {
							name.bindData(weibs, DiscoverBlogListActivity.this);
							name.notifyDataSetChanged();
							// onLoad();
						}
					} else {
						if (name != null) {
							name.bindData(weibs, DiscoverBlogListActivity.this);
							name.notifyDataSetChanged();
						} else {
							name = new HbdtAdapter();
							name.bindData(weibs, DiscoverBlogListActivity.this);
							mListView.setAdapter(name);

						}
						if (shua.equals("shuxing")) {
							// onLoad();
							mListView.getRefreshableView().setSelection(0);
						}
					}
				}
			} catch (Exception e) {
				// TODO: handle exception
				MyLog.e("weibao Exception" + e);
			}
		}
	}

	/**
	 * 生活圈列表adapter
	 * 
	 * @author baiyuchuan
	 * 
	 */
	class HbdtAdapter extends BaseAdapter {
		List<Weib> weibs;
		private Activity activity;

		void bindData(List<Weib> weibs, Activity activity) {
			this.weibs = weibs;
			this.activity = activity;
		}

		@Override
		public int getCount() {
			return weibs.size();
		}

		@Override
		public Object getItem(int arg0) {
			return weibs.get(arg0);
		}

		@Override
		public long getItemId(int arg0) {
			return arg0;
		}

		@Override
		public View getView(final int position, View convertView,
				ViewGroup parent) {

			final ViewHolder holder;
			PingLunAdapter adapter;
			boolean islike = false;
			try {
				MyLog.i("weibao Result:" + weibs);
				if (convertView == null) {
					convertView = LayoutInflater.from(getApplicationContext())
							.inflate(R.layout.discover_blog_list_itemqq, null);
					holder = new ViewHolder();
					initViewHolder(convertView, holder);
					convertView.setTag(holder);
				} else {
					holder = (ViewHolder) convertView.getTag();
				}
				final Weib weib = weibs.get(position);
				if (null != weibs && weibs.size() > 0
						&& weibs.get(position).isHestory()) {
					holder.discover_blog_list_pinglunzan
							.setVisibility(View.GONE);
					holder.upload_history.setVisibility(View.VISIBLE);
					holder.upload_history
							.setOnClickListener(new OnClickListener() {
								@Override
								public void onClick(View v) {
									try {
										MyPostWeiboInfo upLoadWeiBoInfo = null;

										if (null != weiboInfos
												&& weiboInfos.size() > 0) {
											for (int i = 0; i < weiboInfos
													.size(); i++) {
												if (weibs
														.get(position)
														.getTime()
														.equals(CommonUtil
																.getTime(weiboInfos
																		.get(i)
																		.getSendTime()))) {
													upLoadWeiBoInfo = weiboInfos
															.get(i);
												}
												;
											}
											MyLog.i(" (myPostWeiboInfo) set :"
													+ weiboInfos.get(
															weiboInfos.size()
																	- position
																	- 1)
															.toString());
											if (NetUtil
													.getNetworkState(DiscoverBlogListActivity.this) == NetUtil.NETWORN_NONE) {
												Toast.makeText(
														DiscoverBlogListActivity.this,
														"请检查您的网络", 0).show();
												return;
											}
											if (upLoadTask.getStatus() == AsyncTask.Status.PENDING) {
												// upLoadTask.execute(weiboInfos
												// .get(weiboInfos.size() -
												// position
												// - 1));
												upLoadTask
														.execute(upLoadWeiBoInfo);

												holder.discover_blog_list_pinglunzan
														.setVisibility(View.VISIBLE);
												holder.upload_history
														.setVisibility(View.GONE);
											} else if (upLoadTask.getStatus() == AsyncTask.Status.RUNNING) {
											} else if (upLoadTask.getStatus() == AsyncTask.Status.FINISHED) {
												upLoadTask = new UpLoadTask();
												// weiboInfos.get(weiboInfos.size()
												// - position
												// - 1);
												upLoadTask
														.execute(upLoadWeiBoInfo);

											}
										}
									} catch (Exception e) {
										e.printStackTrace();
									}

								}
							});
				} else {
					holder.discover_blog_list_pinglunzan
							.setVisibility(View.VISIBLE);
					holder.upload_history.setVisibility(View.GONE);
				}

				if (weib.getIsAkey().equals("1")) {
					if (weib.getIsanonymous().equals("0")) {
						holder.discover_blog_list_itemqq_nc.setText("匿名曝光");
						holder.baoguangtie.setVisibility(View.VISIBLE);
						holder.discover_blog_list_itemqq_usericon
								.setVisibility(View.GONE);
						holder.baoguangtiee.setVisibility(View.GONE);
					} else {
						holder.baoguangtie.setVisibility(View.GONE);
						holder.baoguangtiee.setVisibility(View.VISIBLE);
						holder.discover_blog_list_itemqq_usericon
								.setVisibility(View.VISIBLE);
						if (null == weib.getNc() || "".equals(weib.getNc())) {
							holder.discover_blog_list_itemqq_nc.setText(weib
									.getUsername());
						} else {
							holder.discover_blog_list_itemqq_nc.setText(weib
									.getNc());
						}
					}

					String pollutionType = weib.getPollutionType();
					if (pollutionType != null && !pollutionType.equals("")) {
						if (pollutionType.contains("气")) {
							holder.pollutionType_qi.setVisibility(View.VISIBLE);
						}
						if (pollutionType.contains("水")) {
							holder.pollutionType_shui
									.setVisibility(View.VISIBLE);
						}
						if (pollutionType.contains("土")) {
							holder.pollutionType_tu.setVisibility(View.VISIBLE);
						}
						if (pollutionType.contains("工")) {
							holder.pollutionType_gong
									.setVisibility(View.VISIBLE);
						}
						if (pollutionType.contains("农")) {
							holder.pollutionType_nong
									.setVisibility(View.VISIBLE);
						}
						if (pollutionType.contains("城")) {
							holder.pollutionType_cheng
									.setVisibility(View.VISIBLE);
						}
						if (pollutionType.contains("海")) {
							holder.pollutionType_hai
									.setVisibility(View.VISIBLE);
						}
						if (pollutionType.contains("化")) {
							holder.pollutionType_hua
									.setVisibility(View.VISIBLE);
						}
						if (pollutionType.contains("生")) {
							holder.pollutionType_sheng
									.setVisibility(View.VISIBLE);
						}
						if (pollutionType.contains("噪")) {
							holder.pollutionType_zao
									.setVisibility(View.VISIBLE);
						}
						if (pollutionType.contains("固")) {
							holder.pollutionType_gu.setVisibility(View.VISIBLE);
						}
						if (pollutionType.contains("能")) {
							holder.pollutionType_neng
									.setVisibility(View.VISIBLE);
						}
					}
					SimpleDateFormat sdf = new SimpleDateFormat(
							"yyyy-MM-dd HH:mm:ss");
					String currentTimeSeconds;
					try {
						currentTimeSeconds = sdf.parse(weib.getTime())
								.getTime() / 1000 + "";
						holder.discover_blog_list_itemqq_tm.setText(weib
								.getTime().substring(5, 16));
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					SpannableString spannableString = convertNormalStringToSpannableString(weib
							.getContent());
					if (weib.getIsanonymous().equals("0")) {
						holder.discover_blog_list_itemqq_nc.setText("匿名曝光");
					} else {
						if (StringUtil.isEmpty(weib.getNc())) {
							holder.discover_blog_list_itemqq_nc.setText(weib
									.getUsername());
						} else {
							holder.discover_blog_list_itemqq_nc.setText(weib
									.getNc());
						}
					}
					holder.discover_blog_list_itemqq_content
							.setText(spannableString);

					if (null == weib.getAddress()
							|| "".equals(weib.getAddress())) {
						holder.discover_blog_list_itemqq_ct
								.setVisibility(View.GONE);
						holder.discover_blog_list_itemqq_wz
								.setVisibility(View.GONE);
					} else {
						holder.discover_blog_list_itemqq_ct
								.setVisibility(View.VISIBLE);
						holder.discover_blog_list_itemqq_wz
								.setVisibility(View.VISIBLE);
						holder.discover_blog_list_itemqq_ct.setText(weib
								.getAddress());
					}
					String rat = weib.getShowlevel().toString();

					String xingji = weib.getEnv_state();
					if ("1".equals(xingji)) {
						xingji = envir[0];
					} else if ("2".equals(xingji)) {
						xingji = envir[1];
					} else {
						xingji = envir[2];
					}
					holder.discover_blog_list_itemqq_en.setText(xingji);
					String dianz = weib.getDianz();
					if ("0".equals(dianz)) {
						holder.discover_blog_list_itemqq_zan
								.setVisibility(View.GONE);
					} else {
						holder.discover_blog_list_itemqq_zan
								.setVisibility(View.VISIBLE);
					}
					int small = weib.getSmall_pics().size();
					String[] smallthumbnail;

					List<PicPath> picPaths = new ArrayList<PicPath>();
					if (0 == small) {
						holder.discover_blog_list_itemqq_gvi
								.setVisibility(View.GONE);
					} else {
						holder.discover_blog_list_itemqq_gvi
								.setVisibility(View.VISIBLE);
						smallthumbnail = new String[small];
						for (int i = 0; i < small; i++) {
							smallthumbnail[i] = weib.getSmall_pics().get(i);
							if (weib.isHestory()) {
								picPaths.add(new PicPath(weib.getSmall_pics()
										.get(i), false));
							} else {
								picPaths.add(new PicPath(weib.getSmall_pics()
										.get(i), true));
							}
						}
						holder.discover_blog_list_itemqq_gvi
								.setAdapter(new DiscoverBlogPicGridAdapter(
										smallthumbnail,
										DiscoverBlogListActivity.this, picPaths));
					}
					holder.discover_blog_list_itemqq_gvi
							.setOnItemClickListener(new OnItemClickListener() {
								@Override
								public void onItemClick(AdapterView<?> arg0,
										View arg1, int arg2, long arg3) {
									int datu_nums = weib.getBig_pics().size();
									if (datu_nums > 0) {

										String[] datu_urls = new String[datu_nums];
										for (int i = 0; i < datu_nums; i++) {
											if (null != weibs
													&& weibs.size() > 0
													&& weibs.get(position)
															.isHestory()) {
												datu_urls[i] = "file://"
														+ weibs.get(position)
																.getBig_pics()
																.get(i)
																.toString();
											} else {
												datu_urls[i] = weib
														.getBig_pics().get(i)
														.toString();
											}

										}
										imageBrower(arg2, datu_urls);
									}

								}
							});
					List<String> zanName = weib.getDianz_users();
					List<String> zanNc = weib.getDianz_usersNc();
					int m = zanName.size();
					String zanText = "";
					if (m > 0) {
						switch (m) {
						default:
							MyLog.i("zanText zanName.size():" + zanName.size());
							MyLog.i("zanText zanNc.size():" + zanNc.size());
							for (int i = 1; (i <= 10 && i <= zanName.size()); i++) {
								if (null != zanNc
										&& zanNc.size() >= zanName.size()
										&& !StringUtil.isEmpty(zanNc.get(zanNc
												.size() - i))) {
									if (i == 1) {
										zanText = zanNc.get(zanNc.size() - i);
										MyLog.i("zanText zanNc.get(zanNc.size()-i):"
												+ zanNc.get(zanNc.size() - i));
									} else {
										zanText = zanText + ","
												+ zanNc.get(zanNc.size() - i);
										MyLog.i("zanText zanNc.get(zanNc.size()-i)22:"
												+ zanText);
									}
								} else {
									if (i == 1) {
										zanText = zanName.get(zanName.size()
												- i);
										MyLog.i("zanText zanName.get(zanName.size()-i):"
												+ zanName.get(zanNc.size() - i));
									} else {
										zanText = zanText
												+ ","
												+ zanName.get(zanName.size()
														- i);
										MyLog.i("zanText zanName.get(zanName.size()-i)222:"
												+ zanName.get(zanNc.size() - i));
									}
								}
							}
							MyLog.i("zanText:" + zanText);
							holder.discover_blog_list_itemqq_zan_name
									.setText(zanText + "等" + zanName.size()
											+ "人顶你");
							break;
						}
					} else {
						holder.discover_blog_list_itemqq_zan
								.setVisibility(View.GONE);
					}
					pingluns = weib.getPinglun();
					if (pingluns.size() == 0) {
						holder.discover_blog_list_itemqq_tm_pinglunlie
								.setVisibility(View.GONE);
					} else {
						holder.discover_blog_list_itemqq_tm_pinglunlie
								.setVisibility(View.VISIBLE);
						adapter = new PingLunAdapter();
						adapter.bindData(pingluns);
						holder.discover_blog_listview.setAdapter(adapter);
						setListViewHeight(holder.discover_blog_listview);
					}

					if (weib.getUserId().equals(userid)) {
						holder.discover_blog_list_itemqq_db
								.setVisibility(View.VISIBLE);
					} else if (weib.isHestory() && weib.getUserId().equals("1")
							&& weib.getUsername().equals("游客")) {
						holder.discover_blog_list_itemqq_db
								.setVisibility(View.VISIBLE);
					} else {
						holder.discover_blog_list_itemqq_db
								.setVisibility(View.GONE);
					}
					imageLoader.displayImage(weib.getIcon(),
							holder.discover_blog_list_itemqq_usericon, options,
							animateFirstListener);
					List<String> dianzuser = weib.getDianz_usersId();
					if (dianzuser.size() > 0) {
						if (dianzuser.contains(userid)) {
							holder.discover_blog_list_itemqq_zaniv1
									.setImageDrawable(getResources()
											.getDrawable(
													R.drawable.skin_feed_icon_review_praise));
							islike = true;
							holder.discover_blog_list_itemqq_zan1.setText("已赞");
						} else {
							holder.discover_blog_list_itemqq_zaniv1
									.setImageDrawable(getResources()
											.getDrawable(
													R.drawable.skin_feed_icon_praise_click));
							islike = false;
							holder.discover_blog_list_itemqq_zan1.setText("赞");
						}
					} else {
						holder.discover_blog_list_itemqq_zaniv1
								.setImageDrawable(getResources().getDrawable(
										R.drawable.skin_feed_icon_praise_click));
						islike = false;
						holder.discover_blog_list_itemqq_zan1.setText("赞");
					}
					final boolean islike1 = islike;
					holder.discover_blog_list_itemqq_zanll
							.setOnClickListener(new View.OnClickListener() {

								@Override
								public void onClick(View v) {
									// TODO Auto-generated method stub
									if ("".equals(userid)) {
										new Thread() {
											public void run() {
												try {
													currentThread().sleep(999);
													Intent intent = new Intent(
															getApplicationContext(),
															UserLoginActivity.class);
													intent.putExtra("from",
															"bloglist");
													startActivity(intent);
												} catch (InterruptedException e) {
													// TODO Auto-generated catch
													// block
													e.printStackTrace();
												}
											};
										}.start();
									} else {
										/**
										 * 设置赞或未赞的状态
										 */
										JSONObject jsonObject = new JSONObject();
										try {
											Date date = new Date();
											SimpleDateFormat sdf = new SimpleDateFormat(
													"yyyy-MM-dd HH:mm:ss");
											String time = sdf.format(date);
											jsonObject.put("userId", userid);
											jsonObject.put("islike", islike1);
											jsonObject.put("weiboid", weibs
													.get(position).getId());
											jsonObject.put("time", time);
											jsonObject
													.put("position", position);
										} catch (Exception e1) {
											e1.printStackTrace();
										}

										if (NetUtil
												.getNetworkState(DiscoverBlogListActivity.this) == NetUtil.NETWORN_NONE) {
											Toast.makeText(
													DiscoverBlogListActivity.this,
													"请检查您的网络", 0).show();
											return;
										}
										ZanPage(jsonObject.toString());
										// praiseTask.execute(jsonObject.toString());
									}
								}
							});
					final DeleteBlogModel deleteBlogModel = new DeleteBlogModel(
							weib.getId(), weib.getUserId(), false, position
									+ "");
					if (holder.discover_blog_list_itemqq_db.getVisibility() == 0) {
						holder.discover_blog_list_itemqq_db
								.setOnClickListener(new OnClickListener() {
									@Override
									public void onClick(View v) {
										if (null != weibs
												&& weibs.size() > 0
												&& weibs.get(position)
														.isHestory()) {
											try {
												if (null != weiboInfos
														&& weiboInfos.size() > 0) {
													MyPostWeiboInfo myPostWeiboInfo = weiboInfos
															.get(weiboInfos
																	.size()
																	- position
																	- 1);
													MyPostWeiboInfo
															.deleteInfo(
																	DiscoverBlogListActivity.this,
																	myPostWeiboInfo);
													weiboInfos
															.remove(myPostWeiboInfo);
												}
											} catch (Exception e) {
												e.printStackTrace();
											}
											weibs.remove(position);
											name.notifyDataSetChanged();
										} else {
											// 执行删除的备用数据
											DeleteBlogInfoById blogInfoById = new DeleteBlogInfoById();
											blogInfoById.execute(
													deleteBlogModel.getBlogId(),
													deleteBlogModel.getUserId(),
													deleteBlogModel.getBeizhu());
										}
									}
								});
					}
					if (weib.getPinglun().size() > 0) {
						holder.discover_blog_list_item_edit
								.setVisibility(View.VISIBLE);
						holder.discover_blog_list_item_edit
								.setOnClickListener(new View.OnClickListener() {

									@Override
									public void onClick(View v) {
										// TODO Auto-generated method stub
										if (login()) {
											return;
										}
										chat();
										weiboid = weib.getId();
										liebiaoposition = position;
										observer = WeiBaoApplication.usename;
										java.util.Date currentTime = new java.util.Date();
										SimpleDateFormat sdf = new SimpleDateFormat(
												"yyyy-MM-dd HH:mm");
										time = sdf.format(currentTime);
									}
								});
					} else {
						holder.discover_blog_list_item_edit
								.setVisibility(View.GONE);
					}
					holder.discover_blog_list_itemqq_ping
							.setOnClickListener(new View.OnClickListener() {

								@Override
								public void onClick(View v) {
									// TODO Auto-generated method stub
									if (login()) {
										return;
									}
									chat();
									weiboid = weib.getId();
									liebiaoposition = position;
									observer = WeiBaoApplication.usename;
									java.util.Date currentTime = new java.util.Date();
									SimpleDateFormat sdf = new SimpleDateFormat(
											"yyyy-MM-dd HH:mm");
									time = sdf.format(currentTime);
								}
							});
					convertView.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
							discover_blog_list_bottom.setVisibility(View.GONE);
						}
					});

				} else {

					if (null != weib.getUserId()
							&& weib.getUserId().equals(userid)) {
						holder.discover_blog_list_itemqq_db
								.setVisibility(View.VISIBLE);
					} else if (null != weib.getUserId() && weib.isHestory()
							&& weib.getUserId().equals("1")
							&& weib.getUsername().equals("游客")) {
						holder.discover_blog_list_itemqq_db
								.setVisibility(View.VISIBLE);
					} else {
						holder.discover_blog_list_itemqq_db
								.setVisibility(View.GONE);
					}

					if (null == weib.getNc() || "".equals(weib.getNc())) {
						holder.discover_blog_list_itemqq_nc.setText(weib
								.getUsername());
					} else {
						holder.discover_blog_list_itemqq_nc.setText(weib
								.getNc());
					}
					holder.discover_blog_list_itemqq_usericon
							.setVisibility(View.VISIBLE);
					holder.baoguangtie.setVisibility(View.GONE);
					holder.baoguangtiee.setVisibility(View.GONE);
					holder.pollutionType_qi.setVisibility(View.GONE);
					holder.pollutionType_shui.setVisibility(View.GONE);
					holder.pollutionType_tu.setVisibility(View.GONE);
					holder.pollutionType_gong.setVisibility(View.GONE);
					holder.pollutionType_nong.setVisibility(View.GONE);
					holder.pollutionType_cheng.setVisibility(View.GONE);
					holder.pollutionType_hai.setVisibility(View.GONE);
					holder.pollutionType_hua.setVisibility(View.GONE);
					holder.pollutionType_sheng.setVisibility(View.GONE);
					holder.pollutionType_zao.setVisibility(View.GONE);
					holder.pollutionType_gu.setVisibility(View.GONE);
					holder.pollutionType_neng.setVisibility(View.GONE);
					SimpleDateFormat sdf = new SimpleDateFormat(
							"yyyy-MM-dd HH:mm:ss");
					String currentTimeSeconds;
					try {
						currentTimeSeconds = sdf.parse(weib.getTime())
								.getTime() / 1000 + "";
						holder.discover_blog_list_itemqq_tm.setText(weib
								.getTime().substring(5, 16));
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					SpannableString spannableString = convertNormalStringToSpannableString(weib
							.getContent());
					if (StringUtil.isEmpty(weib.getNc())) {
						holder.discover_blog_list_itemqq_nc.setText(weib
								.getUsername());
					} else {
						holder.discover_blog_list_itemqq_nc.setText(weib
								.getNc());
					}

					holder.discover_blog_list_itemqq_content
							.setText(spannableString);

					// holder.discover_blog_list_itemqq_ct.setText(city_name);
					if (null == weib.getAddress()
							|| "".equals(weib.getAddress())) {
						holder.discover_blog_list_itemqq_ct
								.setVisibility(View.GONE);
						holder.discover_blog_list_itemqq_wz
								.setVisibility(View.GONE);
					} else {
						holder.discover_blog_list_itemqq_ct
								.setVisibility(View.VISIBLE);
						holder.discover_blog_list_itemqq_wz
								.setVisibility(View.VISIBLE);
						holder.discover_blog_list_itemqq_ct.setText(weib
								.getAddress());
					}
					String rat = weib.getShowlevel().toString();

					String xingji = weib.getEnv_state();
					if ("1".equals(xingji)) {
						xingji = envir[0];
					} else if ("2".equals(xingji)) {
						xingji = envir[1];
					} else {
						xingji = envir[2];
					}
					holder.discover_blog_list_itemqq_en.setText(xingji);
					String dianz = weib.getDianz();
					if ("0".equals(dianz)) {
						holder.discover_blog_list_itemqq_zan
								.setVisibility(View.GONE);
					} else {
						holder.discover_blog_list_itemqq_zan
								.setVisibility(View.VISIBLE);
					}
					int small = weib.getSmall_pics().size();
					String[] smallthumbnail;

					if (0 == small) {
						holder.discover_blog_list_itemqq_gvi
								.setVisibility(View.GONE);
					} else {
						holder.discover_blog_list_itemqq_gvi
								.setVisibility(View.VISIBLE);
						smallthumbnail = new String[small];
						List<PicPath> picPaths = new ArrayList<PicPath>();
						for (int i = 0; i < small; i++) {
							smallthumbnail[i] = weib.getSmall_pics().get(i);
							if (weib.isHestory()) {
								picPaths.add(new PicPath(weib.getSmall_pics()
										.get(i), false));
							} else {
								picPaths.add(new PicPath(weib.getSmall_pics()
										.get(i), true));
							}
						}
						// holder.discover_blog_list_itemqq_gvi
						// .setAdapter(new DiscoverBlogPicGridAdapter(
						// smallthumbnail,
						// DiscoverBlogListActivity.this));
						holder.discover_blog_list_itemqq_gvi
								.setAdapter(new DiscoverBlogPicGridAdapter(
										smallthumbnail,
										DiscoverBlogListActivity.this, picPaths));
						// for (int i = 0; i < small; i++) {
						// smallthumbnail[i] = weib.getSmall_pics().get(i);
						// }
						// holder.discover_blog_list_itemqq_gvi
						// .setAdapter(new DiscoverBlogPicGridAdapter(
						// smallthumbnail,
						// DiscoverBlogListActivity.this));
					}
					holder.discover_blog_list_itemqq_gvi
							.setOnItemClickListener(new OnItemClickListener() {
								@Override
								public void onItemClick(AdapterView<?> arg0,
										View arg1, int arg2, long arg3) {
									int datu_nums = weib.getBig_pics().size();
									if (datu_nums > 0) {

										String[] datu_urls = new String[datu_nums];
										for (int i = 0; i < datu_nums; i++) {
											if (null != weibs
													&& weibs.size() > 0
													&& weibs.get(position)
															.isHestory()) {
												datu_urls[i] = "file://"
														+ weibs.get(position)
																.getBig_pics()
																.get(i)
																.toString();
											} else {
												datu_urls[i] = weib
														.getBig_pics().get(i)
														.toString();
											}

										}
										imageBrower(arg2, datu_urls);
									}

								}
							});
					List<String> zanName = weib.getDianz_users();
					List<String> zanNc = weib.getDianz_usersNc();
					int m = zanName.size();
					String zanText = "";
					if (m > 0) {
						switch (m) {
						default:
							// int size = 0;
							// if(zanName.size() > 10){
							// size = 10;
							// }else{
							// size = zanName.size() ;
							// }
							MyLog.i("zanText zanName.size():" + zanName.size());
							MyLog.i("zanText zanNc.size():" + zanNc.size());
							for (int i = 1; (i <= 10 && i <= zanName.size()); i++) {
								if (null != zanNc
										&& zanNc.size() >= zanName.size()
										&& !StringUtil.isEmpty(zanNc.get(zanNc
												.size() - i))) {
									if (i == 1) {
										zanText = zanNc.get(zanNc.size() - i);
										MyLog.i("zanText zanNc.get(zanNc.size()-i):"
												+ zanNc.get(zanNc.size() - i));
									} else {
										zanText = zanText + ","
												+ zanNc.get(zanNc.size() - i);
										MyLog.i("zanText zanNc.get(zanNc.size()-i)22:"
												+ zanText);
									}
								} else {
									if (i == 1) {
										zanText = zanName.get(zanName.size()
												- i);
										MyLog.i("zanText zanName.get(zanName.size()-i):"
												+ zanName.get(zanNc.size() - i));
									} else {
										zanText = zanText
												+ ","
												+ zanName.get(zanName.size()
														- i);
										MyLog.i("zanText zanName.get(zanName.size()-i)222:"
												+ zanName.get(zanNc.size() - i));
									}
								}
							}
							MyLog.i("zanText:" + zanText);
							holder.discover_blog_list_itemqq_zan_name
									.setText(zanText + "等" + zanName.size()
											+ "人觉得很赞");
							break;
						}
					} else {
						holder.discover_blog_list_itemqq_zan
								.setVisibility(View.GONE);
					}
					pingluns = weib.getPinglun();
					if (pingluns.size() == 0) {
						holder.discover_blog_list_itemqq_tm_pinglunlie
								.setVisibility(View.GONE);
					} else {
						holder.discover_blog_list_itemqq_tm_pinglunlie
								.setVisibility(View.VISIBLE);
						adapter = new PingLunAdapter();
						adapter.bindData(pingluns);
						holder.discover_blog_listview.setAdapter(adapter);
						setListViewHeight(holder.discover_blog_listview);
					}
					imageLoader.displayImage(weib.getIcon(),
							holder.discover_blog_list_itemqq_usericon, options,
							animateFirstListener);
					List<String> dianzuser = weib.getDianz_usersId();
					if (dianzuser.size() > 0) {
						if (dianzuser.contains(userid)) {
							holder.discover_blog_list_itemqq_zaniv1
									.setImageDrawable(getResources()
											.getDrawable(
													R.drawable.skin_feed_icon_review_praise));
							islike = true;
							holder.discover_blog_list_itemqq_zan1.setText("已赞");
						} else {
							holder.discover_blog_list_itemqq_zaniv1
									.setImageDrawable(getResources()
											.getDrawable(
													R.drawable.skin_feed_icon_praise_click));
							islike = false;
							holder.discover_blog_list_itemqq_zan1.setText("赞");
						}
					} else {
						holder.discover_blog_list_itemqq_zaniv1
								.setImageDrawable(getResources().getDrawable(
										R.drawable.skin_feed_icon_praise_click));
						islike = false;
						holder.discover_blog_list_itemqq_zan1.setText("赞");
					}
					final boolean islike1 = islike;
					holder.discover_blog_list_itemqq_zanll
							.setOnClickListener(new View.OnClickListener() {

								@Override
								public void onClick(View v) {
									// TODO Auto-generated method stub
									if ("".equals(userid)) {
										new Thread() {
											public void run() {
												try {
													currentThread().sleep(999);
													startActivity(new Intent(
															getApplicationContext(),
															UserLoginActivity.class)
															.putExtra("from",
																	"bloglist"));
												} catch (InterruptedException e) {
													// TODO Auto-generated catch
													// block
													e.printStackTrace();
												}
											};
										}.start();
									} else {
										/**
										 * 设置赞或未赞的状态
										 */
										JSONObject jsonObject = new JSONObject();
										try {
											Date date = new Date();
											SimpleDateFormat sdf = new SimpleDateFormat(
													"yyyy-MM-dd HH:mm:ss");
											String time = sdf.format(date);
											jsonObject.put("userId", userid);
											jsonObject.put("islike", islike1);
											jsonObject.put("weiboid", weibs
													.get(position).getId());
											jsonObject.put("time", time);
											jsonObject
													.put("position", position);
										} catch (Exception e1) {
											e1.printStackTrace();
										}

										if (NetUtil
												.getNetworkState(DiscoverBlogListActivity.this) == NetUtil.NETWORN_NONE) {
											Toast.makeText(
													DiscoverBlogListActivity.this,
													"请检查您的网络", 0).show();
											return;
										}
										ZanPage(jsonObject.toString());
										// praiseTask.execute(jsonObject.toString());
									}
								}
							});
					final DeleteBlogModel deleteBlogModel = new DeleteBlogModel(
							weib.getId(), weib.getUserId(), false, position
									+ "");
					if (holder.discover_blog_list_itemqq_db.getVisibility() == 0) {
						holder.discover_blog_list_itemqq_db
								.setOnClickListener(new OnClickListener() {
									@Override
									public void onClick(View v) {
										if (null != weibs
												&& weibs.size() > 0
												&& weibs.get(position)
														.isHestory()) {
											try {
												if (null != weiboInfos
														&& weiboInfos.size() > 0) {
													MyPostWeiboInfo myPostWeiboInfo = weiboInfos
															.get(weiboInfos
																	.size()
																	- position
																	- 1);
													MyPostWeiboInfo
															.deleteInfo(
																	DiscoverBlogListActivity.this,
																	myPostWeiboInfo);
													weiboInfos
															.remove(myPostWeiboInfo);
												}
											} catch (Exception e) {
												e.printStackTrace();
											}
											weibs.remove(position);
											name.notifyDataSetChanged();
										} else {
											// 执行删除的备用数据
											DeleteBlogInfoById blogInfoById = new DeleteBlogInfoById();
											blogInfoById.execute(
													deleteBlogModel.getBlogId(),
													deleteBlogModel.getUserId(),
													deleteBlogModel.getBeizhu());
										}
									}
								});
					}
					if (weib.getPinglun().size() > 0) {
						holder.discover_blog_list_item_edit
								.setVisibility(View.VISIBLE);
						holder.discover_blog_list_item_edit
								.setOnClickListener(new View.OnClickListener() {

									@Override
									public void onClick(View v) {
										// TODO Auto-generated method stub
										if (login()) {
											return;
										}
										chat();
										weiboid = weib.getId();
										liebiaoposition = position;
										observer = WeiBaoApplication.usename;
										java.util.Date currentTime = new java.util.Date();
										SimpleDateFormat sdf = new SimpleDateFormat(
												"yyyy-MM-dd HH:mm");
										time = sdf.format(currentTime);
									}
								});
					} else {
						holder.discover_blog_list_item_edit
								.setVisibility(View.GONE);
					}
					holder.discover_blog_list_itemqq_ping
							.setOnClickListener(new View.OnClickListener() {

								@Override
								public void onClick(View v) {
									// TODO Auto-generated method stub
									if (login()) {
										return;
									}
									chat();
									weiboid = weib.getId();
									liebiaoposition = position;
									observer = WeiBaoApplication.usename;
									java.util.Date currentTime = new java.util.Date();
									SimpleDateFormat sdf = new SimpleDateFormat(
											"yyyy-MM-dd HH:mm");
									time = sdf.format(currentTime);
								}
							});
					convertView.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
							discover_blog_list_bottom.setVisibility(View.GONE);
						}
					});

				}

			} catch (Exception e) {
				// TODO: handle exception
				MyLog.e("weibao Exception" + e);
			}
			return convertView;
		}
	}

	// 打开聊天
	private void chat() {
		discover_blog_list_bottom.setVisibility(View.VISIBLE);
		discover_blog_list_ed.requestFocus();
		imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
	}

	// 登录模块
	private boolean login() {
		if ("".equals(userid)) {
			new Thread() {
				public void run() {
					try {
						startActivity(new Intent(getApplicationContext(),
								UserLoginActivity.class).putExtra("from",
								"bloglist"));
					} catch (Exception e) {
						// TODO
						// Auto-generated
						// catch
						// block
						e.printStackTrace();
					}
				};
			}.start();
			return true;
		}
		return false;

	}

	// 评论
	class SendBlogCommentTask extends
			AsyncTask<String, Void, ResultPostBlogComment> {

		@Override
		protected ResultPostBlogComment doInBackground(String... params) {
			String url = UrlComponent.saveCommentsUrl_Post;
			BusinessSearch search = new BusinessSearch();
			ResultPostBlogComment _Result = null;
			try {
				_Result = search.sendBlogComment(url, observer, content, time,
						weiboid);

				return _Result;
			} catch (Exception e) {
				e.printStackTrace();
			}

			return _Result;
		}

		@Override
		protected void onPostExecute(ResultPostBlogComment result) {

			super.onPostExecute(result);
			try {
				if (null != result && result.isFlag()) {
					discover_blog_list_ed.setText("");
					imm.hideSoftInputFromWindow(
							discover_blog_list_bottom.getWindowToken(), 0);
					discover_blog_list_bottom.setVisibility(View.GONE);
					Pinglun pinglun = new Pinglun();
					pinglun.setUser(username);
					pinglun.setNc(userNc);
					pinglun.setContent(content);
					pinglun.setCommentPersonId(userid);
					pinglun.setCommentId(result.getCommentId());
					List<Pinglun> list = weibs.get(liebiaoposition)
							.getPinglun();
					list.add(pinglun);
					name.notifyDataSetChanged();
				} else {
					Toast.makeText(DiscoverBlogListActivity.this, "评论失败", 0)
							.show();
				}
			} catch (Exception e) {
				// TODO: handle exception
				MyLog.e("weibao Exception" + e);
			}

		}
	}

	private class ViewHolder {
		private TextView discover_blog_list_itemqq_nc;
		private TextView discover_blog_list_itemqq_tm;
		private TextView discover_blog_list_itemqq_content;
		private TextView discover_blog_list_itemqq_ct;
		private TextView discover_blog_list_itemqq_zan1;
		private TextView discover_blog_list_itemqq_en;
		private TextView discover_blog_list_itemqq_zan_name;
		private TextView discover_blog_list_itemqq_gv;
		private RoundImageView discover_blog_list_itemqq_usericon;
		private ImageView discover_blog_list_itemqq_db;// 删除微博按钮
		private ImageView discover_blog_list_itemqq_zaniv1;
		private ImageView discover_blog_list_itemqq_wz;
		private NoScrollGridView discover_blog_list_itemqq_gvi;

		private LinearLayout discover_blog_list_itemqq_zanll;
		private LinearLayout discover_blog_list_itemqq_zan;
		private LinearLayout discover_blog_list_itemqq_ping;
		private RelativeLayout discover_blog_list_itemqq_tm_pinglunlie;
		private LinearLayout discover_blog_list_itemqq_tm_liebiao;
		private MyListView discover_blog_listview;
		private Button discover_blog_list_item_edit;
		private ImageView upload_history;
		private RelativeLayout discover_blog_list_pinglunzan;
		private RoundImageView baoguangtie;
		private RoundImageView baoguangtiee;
		private TextView pollutionType_qi;
		private TextView pollutionType_shui;
		private TextView pollutionType_tu;
		private TextView pollutionType_gong;
		private TextView pollutionType_nong;
		private TextView pollutionType_cheng;
		private TextView pollutionType_hai;
		private TextView pollutionType_hua;
		private TextView pollutionType_sheng;
		private TextView pollutionType_zao;
		private TextView pollutionType_gu;
		private TextView pollutionType_neng;
	}

	private void initViewHolder(View convertView, ViewHolder holder) {
		holder.discover_blog_list_itemqq_nc = (TextView) convertView
				.findViewById(R.id.discover_blog_list_itemqq_nc);
		holder.discover_blog_list_itemqq_zan1 = (TextView) convertView
				.findViewById(R.id.discover_blog_list_itemqq_zan1);
		holder.discover_blog_list_itemqq_tm = (TextView) convertView
				.findViewById(R.id.discover_blog_list_itemqq_tm);
		holder.discover_blog_list_itemqq_content = (TextView) convertView
				.findViewById(R.id.discover_blog_list_itemqq_content);
		holder.discover_blog_list_itemqq_ct = (TextView) convertView
				.findViewById(R.id.discover_blog_list_itemqq_ct);
		holder.discover_blog_list_itemqq_en = (TextView) convertView
				.findViewById(R.id.discover_blog_list_itemqq_en);
		holder.discover_blog_list_itemqq_zan_name = (TextView) convertView
				.findViewById(R.id.discover_blog_list_itemqq_zan_name);
		holder.discover_blog_list_itemqq_usericon = (RoundImageView) convertView
				.findViewById(R.id.discover_blog_list_itemqq_usericon);
		holder.baoguangtiee = (RoundImageView) convertView
				.findViewById(R.id.baoguangtiee);
		holder.discover_blog_list_itemqq_db = (ImageView) convertView
				.findViewById(R.id.discover_blog_list_itemqq_db);
		holder.discover_blog_list_itemqq_wz = (ImageView) convertView
				.findViewById(R.id.discover_blog_list_itemqq_wz);
		holder.discover_blog_list_itemqq_zaniv1 = (ImageView) convertView
				.findViewById(R.id.discover_blog_list_itemqq_zaniv1);
		holder.discover_blog_list_itemqq_zanll = (LinearLayout) convertView
				.findViewById(R.id.discover_blog_list_itemqq_zanll);
		holder.discover_blog_list_itemqq_zan = (LinearLayout) convertView
				.findViewById(R.id.discover_blog_list_itemqq_zan);
		holder.discover_blog_list_itemqq_gvi = (NoScrollGridView) convertView
				.findViewById(R.id.discover_blog_list_itemqq_gvi);
		holder.discover_blog_list_itemqq_ping = (LinearLayout) convertView
				.findViewById(R.id.discover_blog_list_itemqq_ping);
		holder.discover_blog_list_itemqq_tm_pinglunlie = (RelativeLayout) convertView
				.findViewById(R.id.discover_blog_list_itemqq_tm_pinglunlie);

		holder.discover_blog_listview = (MyListView) convertView
				.findViewById(R.id.discover_blog_listview);
		holder.discover_blog_list_item_edit = (Button) convertView
				.findViewById(R.id.discover_blog_list_item_edit);
		holder.upload_history = (ImageView) convertView
				.findViewById(R.id.upload_history);
		holder.discover_blog_list_pinglunzan = (RelativeLayout) convertView
				.findViewById(R.id.discover_blog_list_pinglunzan);
		holder.baoguangtie = (RoundImageView) convertView
				.findViewById(R.id.baoguangtie);
		holder.pollutionType_qi = (TextView) convertView
				.findViewById(R.id.pollutionType_qi);
		holder.pollutionType_shui = (TextView) convertView
				.findViewById(R.id.pollutionType_shui);
		holder.pollutionType_tu = (TextView) convertView
				.findViewById(R.id.pollutionType_tu);
		holder.pollutionType_gong = (TextView) convertView
				.findViewById(R.id.pollutionType_gong);
		holder.pollutionType_nong = (TextView) convertView
				.findViewById(R.id.pollutionType_nong);
		holder.pollutionType_cheng = (TextView) convertView
				.findViewById(R.id.pollutionType_cheng);
		holder.pollutionType_hai = (TextView) convertView
				.findViewById(R.id.pollutionType_hai);
		holder.pollutionType_hua = (TextView) convertView
				.findViewById(R.id.pollutionType_hua);
		holder.pollutionType_sheng = (TextView) convertView
				.findViewById(R.id.pollutionType_sheng);
		holder.pollutionType_zao = (TextView) convertView
				.findViewById(R.id.pollutionType_zao);
		holder.pollutionType_gu = (TextView) convertView
				.findViewById(R.id.pollutionType_gu);
		holder.pollutionType_neng = (TextView) convertView
				.findViewById(R.id.pollutionType_neng);
	}

	private static class AnimateFirstDisplayListener extends
			SimpleImageLoadingListener {

		static final List<String> displayedImages = Collections
				.synchronizedList(new LinkedList<String>());

		@Override
		public void onLoadingComplete(String imageUri, View view,
				Bitmap loadedImage) {
			if (loadedImage != null) {
				ImageView imageView = (ImageView) view;

				boolean firstDisplay = !displayedImages.contains(imageUri);
				if (firstDisplay) {
					displayedImages.add(imageUri);
				}
			}
		}
	}

	public String gettime(String ser, String time) {
		try {

			DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date d1 = df.parse(ser);
			Date d2 = df.parse(time);
			long diff = (d1.getTime() - d2.getTime()) / 60000;
			if (diff < 1) {
				return "刚刚发送";
			} else if (diff < 60) {
				return diff + "分钟前";
			} else if (diff < 60 * 24) {
				long x = diff / 60;
				return x + "小时前";
			} else {
				// long x = diff / 60 / 24;
				// return x + "天前";
				return time;
			}
		} catch (Exception e) {
			return time;
		}
	}

	public SpannableString convertNormalStringToSpannableString(String txt) {
		String hackTxt;
		if (txt.startsWith("[") && txt.endsWith("]")) {
			hackTxt = txt + " ";
		} else {
			hackTxt = txt;
		}
		SpannableString value = SpannableString.valueOf(hackTxt);
		try {
			addEmotions(value);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return value;
	}

	public static boolean isInteger(String value) {
		try {
			value = value.substring(1, value.lastIndexOf("]"));
			int xx = Integer.parseInt(value);
			if (xx > 30) {
				return false;
			} else {
				return true;
			}
		} catch (NumberFormatException e) {
			return false;
		}
	}

	private void addEmotions(SpannableString value) {
		Matcher localMatcher = EMOTION_URL.matcher(value);
		while (localMatcher.find()) {

			String str2 = localMatcher.group(0);

			int k = localMatcher.start();
			int m = localMatcher.end();
			if (isInteger(str2)) {
				str2 = (String) str2.subSequence(1, str2.length() - 1);
				int yu = Integer.parseInt(str2);

				int[] xx = DiscoverFaceGridViewAdapter.getImageIds();
				Bitmap bitmap = BitmapFactory.decodeResource(getResources(),
						xx[yu]);

				// if (bitmap == null)
				// bitmap =
				// GlobalContext.getInstance().getHuahuaPics().get(str2);
				if (bitmap != null) {
					ImageSpan localImageSpan = new ImageSpan(this, bitmap,
							ImageSpan.ALIGN_BASELINE);
					value.setSpan(localImageSpan, k, m,
							Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
				}
			} else {

			}

		}

	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		unregisterReceiver(mMessageReceiver);
	}

	// 评论列表的adapter
	class PingLunAdapter extends BaseAdapter {
		private List<Pinglun> list;

		public void bindData(List<Pinglun> list) {
			this.list = list;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return list.size();
		}

		@Override
		public Object getItem(int arg0) {
			// TODO Auto-generated method stub
			return list.get(arg0);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(final int position, View convertView,
				ViewGroup parent) {
			// TODO Auto-generated method stub
			ViewPingHolder holder;
			try {
				if (convertView == null) {
					holder = new ViewPingHolder();
					convertView = LayoutInflater.from(getApplicationContext())
							.inflate(R.layout.discover_blog_pinglun, null);
					holder.discover_blog_user = (TextView) convertView
							.findViewById(R.id.discover_blog_user);
					holder.discover_blog_content = (TextView) convertView
							.findViewById(R.id.discover_blog_content);
					holder.discover_blog_delete = (ImageView) convertView
							.findViewById(R.id.discover_blog_delete);
					convertView.setTag(holder);
				} else {
					holder = (ViewPingHolder) convertView.getTag();
				}
				final Pinglun pinglun = list.get(position);
				if (StringUtil.isEmpty(pinglun.getNc())) {
					holder.discover_blog_user.setText(pinglun.getUser() + ":");
				} else {
					holder.discover_blog_user.setText(pinglun.getNc() + ":");
				}
				SpannableString spannableString = convertNormalStringToSpannableString2(pinglun
						.getContent());
				holder.discover_blog_content.setText(spannableString);
				final int position1 = position;
				if (pinglun.getCommentPersonId().equals(userid)) {
					holder.discover_blog_delete.setVisibility(View.VISIBLE);
					holder.discover_blog_delete
							.setOnClickListener(new View.OnClickListener() {

								@Override
								public void onClick(View v) {
									// TODO Auto-generated method stub

									DeleteCommentByInfo deleteCommentByInfo = new DeleteCommentByInfo();
									deleteCommentByInfo.execute(
											pinglun.getCommentPersonId(),
											pinglun.getCommentId(), position1,
											2);

								}
							});
				} else {
					holder.discover_blog_delete.setVisibility(View.GONE);

				}
			} catch (Exception e) {
				// TODO: handle exception
				MyLog.e("weibao Exception" + e);
			}

			return convertView;
		}

		private class DeleteCommentByInfo extends
				AsyncTask<Object, Void, DeleteBlogCommentModel> {
			int position = 0;

			@Override
			protected DeleteBlogCommentModel doInBackground(Object... params) {
				DiscoverDeleteBlogStatueModel _Result = null;
				DeleteBlogCommentModel resultDeleteBlogModel = null;
				BusinessSearch search = new BusinessSearch();
				String deleteCommenturl = UrlComponent
						.deleteWeiboCommentByInfo_Get(params[1].toString(),
								params[0].toString());
				position = (Integer) params[2];
				try {
					_Result = search.deleteCommentByInfo(deleteCommenturl);
					resultDeleteBlogModel = new DeleteBlogCommentModel(
							_Result.isStatus(), (String) params[0],
							(String) params[1], (Integer) params[2],
							(Integer) params[3]);

				} catch (Exception e) {
					e.printStackTrace();
					return null;
				}
				return resultDeleteBlogModel;
			}

			@Override
			protected void onPostExecute(DeleteBlogCommentModel result) {
				super.onPostExecute(result);
				try {
					if (null != result && result.isDelete()) {
						if (null != list && list.size() > position) {
							list.remove(list.get(position));
						}
						notifyDataSetChanged();
						if (list.size() == 0) {
							name.notifyDataSetChanged();
						}
					} else {
						Toast.makeText(
								DiscoverBlogListActivity.this,
								(getResources()
										.getString(R.string.delete_blog_no_result)),
								2000).show();
					}
				} catch (Exception e) {
					// TODO: handle exception
					MyLog.e("weibao Exception" + e);
				}
			}
		}

		class ViewPingHolder {
			public ImageView upload_history;
			public RelativeLayout discover_blog_list_pinglunzan;
			private TextView discover_blog_user;
			private TextView discover_blog_content;
			private ImageView discover_blog_delete;
		}

	}

	private void addEmotions2(SpannableString value) {
		Matcher localMatcher = EMOTION_URL.matcher(value);
		while (localMatcher.find()) {
			String str2 = localMatcher.group(0);
			int k = localMatcher.start();
			int m = localMatcher.end();
			if (isInteger(str2)) {
				str2 = (String) str2.subSequence(1, str2.length() - 1);
				int yu = Integer.parseInt(str2);

				int[] xx = DiscoverFaceGridViewAdapter.getImageIds();

				BitmapFactory.Options bmpFactoryOptions = new BitmapFactory.Options();
				bmpFactoryOptions.inSampleSize = 2;
				bmpFactoryOptions.inJustDecodeBounds = false;
				Bitmap bitmap = BitmapFactory.decodeResource(getResources(),
						xx[yu], bmpFactoryOptions);
				if (bitmap != null) {
					ImageSpan localImageSpan = new ImageSpan(this, bitmap,
							ImageSpan.ALIGN_BASELINE);
					value.setSpan(localImageSpan, k, m,
							Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
				}
			} else {
			}
		}
	}

	private void imageBrower(int position, String[] urls) {
		Intent intent = new Intent(DiscoverBlogListActivity.this,
				DiscoverBlogShowPhotoPagerActivity.class);
		intent.putExtra(DiscoverBlogShowPhotoPagerActivity.EXTRA_IMAGE_URLS,
				urls);
		intent.putExtra(DiscoverBlogShowPhotoPagerActivity.EXTRA_IMAGE_INDEX,
				position);
		startActivity(intent);
	}

	public SpannableString convertNormalStringToSpannableString2(String txt) {
		String hackTxt;
		if (txt.startsWith("[") && txt.endsWith("]")) {
			hackTxt = txt + " ";
		} else {
			hackTxt = txt;
		}
		SpannableString value = SpannableString.valueOf(hackTxt);

		try {
			addEmotions2(value);
		} catch (Exception e) {
		}

		return value;
	}

	class xx implements FilenameFilter {
		@Override
		public boolean accept(File dir, String filename) {
			if (filename.contains("weibao" + city_name)) {

				String name = filename.substring(filename.indexOf(city_name)
						+ city_name.length(), filename.lastIndexOf("."));
				if (!"".equals(name)) {
					int shu = Integer.parseInt(name);
					if (shu > 1) {
						return true;
					}
				}
			}
			return false;
		}

	}

	private void registerMessageReceiver1() {
		// TODO Auto-generated method stub
		mMessageReceiver = new MessageReceiver();
		IntentFilter filter = new IntentFilter();
		filter.setPriority(IntentFilter.SYSTEM_HIGH_PRIORITY);
		filter.addAction(MESSAGE_RECEIVED_ACTION2);

		registerReceiver(mMessageReceiver, filter);
	}

	public class MessageReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			if (MESSAGE_RECEIVED_ACTION2.equals(intent.getAction())) {
				try {
					new Thread().sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				loadNextPage("shuxing", dianjicity);

			}
		}

	}

	protected void onResume() {
		super.onResume();
		userid = WeiBaoApplication.getUserId();
		username = mApplication.getUsename();
		userNc = mApplication.getUserNc();
		difang = mApplication.selectedCity;
		MyLog.i("username :" + username);
		MyLog.i("nick_name :" + userNc);
		imageLoader.displayImage(mApplication.getUserPic(), login_nc, options,
				animateFirstListener);
		if (!"".equals(userNc)) {
			nickname_title.setText(userNc);
		} else if (!"".equals(username)) {
			nickname_title.setText(username);
		} else {
			nickname_title.setText("游客");
		}
		MobclickAgent.onResume(this);
	};

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		MobclickAgent.onPause(this);
	}

	private class UpLoadTask extends AsyncTask<MyPostWeiboInfo, Void, Boolean> {
		MyPostWeiboInfo myPostWeiboInfo;

		@Override
		protected void onPreExecute() {
			dialog = CommonUtil.getCustomeDialog(DiscoverBlogListActivity.this,
					R.style.load_dialog, R.layout.custom_progress_dialog);
			TextView titleTxtv = (TextView) dialog
					.findViewById(R.id.dialogText);
			titleTxtv.setText("正在发布中……");

			super.onPreExecute();
		}

		@Override
		protected Boolean doInBackground(MyPostWeiboInfo... params) {

			myPostWeiboInfo = (MyPostWeiboInfo) params[0];
			MyLog.i(" (myPostWeiboInfo) :" + myPostWeiboInfo.toString());
			String url = UrlComponent.sharePostcontent_Post;
			if (null == myPostWeiboInfo) {
				return false;
			}
			BusinessSearch search = new BusinessSearch();
			DiscoverBlogUpLoadResult _Result;
			DiscoverFlagModel _ResultFlag;
			try {
				_Result = search.blogUpLoadTask(url, myPostWeiboInfo);
				String postid = _Result.getPostid();
				if (myPostWeiboInfo.getPaths().size() < 1) {
					if (!"".equals(postid)) {
						MyPostWeiboInfo.deleteInfo(
								DiscoverBlogListActivity.this, myPostWeiboInfo);
						return true;
					} else {
						return false;
					}
				}
				List<String> list = new ArrayList<String>();
				for (int i = 0; i < myPostWeiboInfo.getPaths().size(); i++) {
					String Str = myPostWeiboInfo
							.getPaths()
							.get(i)
							.substring(
									myPostWeiboInfo.getPaths().get(i)
											.lastIndexOf("/") + 1,
									myPostWeiboInfo.getPaths().get(i)
											.lastIndexOf("."));
					list.add(FileUtils.SDPATH + Str + ".JPEG");
					Bitmap bitmap = BimpHelper
							.revitionImageSize(myPostWeiboInfo.getPaths()
									.get(i));
					String file_name = CommonUtil.BitmapToHexString(bitmap);
					String ur = UrlComponent.uploadPic_Post;
					_ResultFlag = search.blogpPostPic(ur, postid, file_name);
				}
				MyPostWeiboInfo.deleteInfo(DiscoverBlogListActivity.this,
						myPostWeiboInfo);
				return true;
			} catch (Exception e) {
				e.printStackTrace();
				MyLog.i("提交数据 Exception" + e.toString());
			}
			return false;
		}

		@Override
		protected void onPostExecute(Boolean result) {
			super.onPostExecute(result);
			dialog.dismiss();
			try {
				if (result) {
					loadNextPage("shuxing", dianjicity);
					Toast.makeText(DiscoverBlogListActivity.this, "环境说发表成功", 0)
							.show();
					Intent intent = new Intent();
					intent.setAction(DiscoverBlogListActivity.MESSAGE_RECEIVED_ACTION2);
					getApplicationContext().sendBroadcast(intent);
				} else {
					Toast.makeText(DiscoverBlogListActivity.this, "环境说发表失败", 0)
							.show();
				}
			} catch (Exception e) {
				// TODO: handle exception
				MyLog.e("weibao Exception" + e);
			}
		}

	}

	/**
	 * 根据微博主ID号+评论人+评论信息删除相应的微博评论
	 * 
	 * @author baiyuchuan
	 * 
	 */
	private class DeleteCommentByInfo extends
			AsyncTask<Object, Void, DeleteBlogCommentModel> {

		@Override
		protected DeleteBlogCommentModel doInBackground(Object... params) {
			DiscoverDeleteBlogStatueModel _Result = null;
			DeleteBlogCommentModel resultDeleteBlogModel = null;
			BusinessSearch search = new BusinessSearch();
			String deleteCommenturl = UrlComponent
					.deleteWeiboCommentByInfo_Get(params[1].toString(),
							params[0].toString());
			try {
				_Result = search.deleteCommentByInfo(deleteCommenturl);
				resultDeleteBlogModel = new DeleteBlogCommentModel(
						_Result.isStatus(), (String) params[0],
						(String) params[1], (Integer) params[2],
						(Integer) params[3]);
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
			return resultDeleteBlogModel;
		}

		@Override
		protected void onPostExecute(DeleteBlogCommentModel result) {
			super.onPostExecute(result);
			try {
				if (null != result && result.isDelete()) {
					try {
						weibs.get(result.getBeizhu()).getPinglun()
								.remove((result.getPos()));
						name.notifyDataSetChanged();
						Toast.makeText(
								DiscoverBlogListActivity.this,
								(getResources()
										.getString(R.string.delete_blog_success)),
								2000).show();
					} catch (Exception e) {
						e.printStackTrace();
					}
				} else {
					Toast.makeText(
							DiscoverBlogListActivity.this,
							(getResources()
									.getString(R.string.delete_blog_no_result)),
							2000).show();
				}
			} catch (Exception e) {
				// TODO: handle exception
				MyLog.e("weibao Exception" + e);
			}
		}
	}

	/**
	 * 根据ID删除相应的微博信息
	 * 
	 * @author baiyuchuan
	 * 
	 */
	private class DeleteBlogInfoById extends
			AsyncTask<String, Void, DeleteBlogModel> {

		@Override
		protected DeleteBlogModel doInBackground(String... params) {
			DiscoverDeleteBlogStatueModel _Result = null;
			DeleteBlogModel resultDeleteBlogModel = null;
			BusinessSearch search = new BusinessSearch();
			String deleteBlogurl = UrlComponent.deleteWeiboByInfo_Get(
					params[0], params[1]);
			try {
				_Result = search.deleteBlogInfoById(deleteBlogurl);
				resultDeleteBlogModel = new DeleteBlogModel(params[0],
						params[1], _Result.isStatus(), params[2]);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return resultDeleteBlogModel;
		}

		@Override
		protected void onPostExecute(DeleteBlogModel result) {
			super.onPostExecute(result);
			try {
				if (null != result && result.isDelete()) {
					try {
						weibs.remove(Integer.parseInt(result.getBeizhu()));
						name.notifyDataSetChanged();
						Toast.makeText(
								DiscoverBlogListActivity.this,
								(getResources()
										.getString(R.string.delete_blog_success)),
								2000).show();
					} catch (Exception e) {
						e.printStackTrace();
					}
				} else {
					Toast.makeText(
							DiscoverBlogListActivity.this,
							(getResources()
									.getString(R.string.delete_blog_no_result)),
							2000).show();
				}
			} catch (Exception e) {
				// TODO: handle exception
				MyLog.e("weibao Exception" + e);
			}
		}
	}

	/**
	 * 点赞
	 * 
	 * @author baiyuchuan
	 * 
	 */
	private class PraiseTask extends AsyncTask<String, Void, Boolean> {

		/**
		 * 参数
		 */
		private JSONObject jsonObject = null;
		/**
		 * 判断是否数据互通成功标示
		 */
		private boolean isSuccess = false;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}

		@Override
		protected Boolean doInBackground(String... params) {
			String url = UrlComponent.weiboAgreeUrl_Post;
			BusinessSearch search = new BusinessSearch();
			DiscoverFlagModel _Result = null;
			try {
				MyLog.i("weiboAgreeUrl_Post load url params:" + params[0]);
				jsonObject = new JSONObject(params[0]);
				_Result = search.praise(url, jsonObject.getString("userId"),
						jsonObject.getBoolean("islike"),
						jsonObject.getString("weiboid"),
						jsonObject.getString("time"));
				isSuccess = true;
				return _Result.isFlag();
			} catch (Exception e) {
				e.printStackTrace();
				isSuccess = false;
			}
			return false;
		}

		@Override
		protected void onPostExecute(Boolean result) {
			super.onPostExecute(result);
			try {
				if (result && isSuccess) {
					try {
						String dianZanPersonId = jsonObject.getString("userId");
						int position = jsonObject.getInt("position");
						boolean islike = jsonObject.getBoolean("islike");
						weibs.get(position).setZanGuo(
								!weibs.get(position).isZanGuo());
						if (!islike) {
							weibs.get(position).getDianz_usersId()
									.add(dianZanPersonId);
							if (!WeiBaoApplication.getUserNc().equals("")) {
								weibs.get(position).getDianz_usersNc()
										.add(WeiBaoApplication.getUserNc());
								weibs.get(position).getDianz_users()
										.add(mApplication.getUsename());
							} else {
								weibs.get(position).getDianz_usersNc().add("");
								weibs.get(position).getDianz_users()
										.add(mApplication.getUsename());
							}
							int dianz = Integer.parseInt(weibs.get(position)
									.getDianz());
							weibs.get(position).setDianz((++dianz) + "");
							name.notifyDataSetChanged();
							Toast.makeText(DiscoverBlogListActivity.this,
									"点赞成功", 0).show();
						} else {
							try {
								int dianZanPos = 0;
								for (int i = 0; i < weibs.get(position)
										.getDianz_usersId().size(); i++) {
									if (weibs.get(position).getDianz_usersId()
											.get(i).equals(dianZanPersonId)) {
										dianZanPos = i;
									}
								}
								weibs.get(position).getDianz_usersId()
										.remove(dianZanPos);
								weibs.get(position).getDianz_users()
										.remove(dianZanPos);
								weibs.get(position).getDianz_usersNc()
										.remove(dianZanPos);
							} catch (Exception e) {
								e.printStackTrace();
							}

							int dianz = Integer.parseInt(weibs.get(position)
									.getDianz());
							if (dianz > 0) {
								weibs.get(position).setDianz((dianz - 1) + "");
							} else {
								weibs.get(position).setDianz(0 + "");
							}
							name.notifyDataSetChanged();
							Toast.makeText(DiscoverBlogListActivity.this,
									"您已取消点赞", 0).show();
						}
					} catch (JSONException e) {
						e.printStackTrace();
					}

				} else if (!result && isSuccess) {
					Toast.makeText(DiscoverBlogListActivity.this, "点赞失败", 0)
							.show();
				} else if (!isSuccess) {
					Toast.makeText(DiscoverBlogListActivity.this, "点赞失败", 0)
							.show();
				}
			} catch (Exception e) {
				// TODO: handle exception
				MyLog.e("weibao Exception" + e);
			}
		}

	}

	/**
	 * 重新计算ListView的高度，解决ScrollView和ListView两个View都有滚动的效果，在嵌套使用时起冲突的问题
	 * 
	 * @param listView
	 */
	public void setListViewHeight(ListView listView) {

		// 获取ListView对应的Adapter

		PingLunAdapter listAdapter = (PingLunAdapter) listView.getAdapter();

		if (listAdapter == null) {
			return;
		}
		int totalHeight = 0;
		for (int i = 0, len = listAdapter.getCount(); i < len; i++) { // listAdapter.getCount()返回数据项的数目
			View listItem = listAdapter.getView(i, null, listView);
			listItem.measure(0, 0); // 计算子项View 的宽高
			totalHeight += listItem.getMeasuredHeight(); // 统计所有子项的总高度
		}

		ViewGroup.LayoutParams params = listView.getLayoutParams();
		params.height = totalHeight
				+ (listView.getDividerHeight() * (listAdapter.getCount() - 1));
		listView.setLayoutParams(params);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.screen:
			// discover_blog_list_ed.setCursorVisible(false);// 失去光标
			imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
			discover_blog_list_bottom.setVisibility(View.GONE);
			break;
		case R.id.discover_blog_list_send:
			content = discover_blog_list_ed.getText().toString().trim();
			if (null == content || "".equals(content)) {
				return;
			}
			if (CommonUtil.isFastDoubleClick()) {
				return;
			}
			chatmessage();
			break;
		case R.id.exposure_back:
			finish();
			break;
		default:
			break;
		}
	}

	private void chatmessage() {
		if (AsyncTask.Status.PENDING == blogCommentTask.getStatus()) {
			blogCommentTask.execute();
		} else if (AsyncTask.Status.RUNNING == blogCommentTask.getStatus()) {
		} else if (AsyncTask.Status.FINISHED == blogCommentTask.getStatus()) {
			blogCommentTask = new SendBlogCommentTask();
			blogCommentTask.execute("");
		}
	}

}
