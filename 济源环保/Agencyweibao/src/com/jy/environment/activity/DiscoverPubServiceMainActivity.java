package com.jy.environment.activity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.json.JSONObject;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jy.environment.R;
import com.jy.environment.business.BusinessSearch;
import com.jy.environment.controls.AsyncTask;
import com.jy.environment.controls.WeiBaoApplication;
import com.jy.environment.database.dal.DBInfo;
import com.jy.environment.database.dal.DBManager;
import com.jy.environment.model.Explorer;
import com.jy.environment.model.GuanZhu;
import com.jy.environment.model.PublicService;
import com.jy.environment.model.PublicServiceItem;
import com.jy.environment.util.ApiClient;
import com.jy.environment.util.MyLog;
import com.jy.environment.util.NetUtil;
import com.jy.environment.util.SharedPreferencesUtil;
import com.jy.environment.util.ToastUtil;
import com.jy.environment.webservice.UrlComponent;
import com.jy.environment.widget.MyDialog;
import com.jy.environment.widget.MyDialog.IDialogOnclickInterface;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;
import com.nostra13.universalimageloader.core.assist.SimpleImageLoadingListener;
import com.umeng.analytics.MobclickAgent;

//公众服务页面

public class DiscoverPubServiceMainActivity extends Activity implements
		OnClickListener, IDialogOnclickInterface {
	private ListView subscribe_list;
	private RelativeLayout subscribe_more;
	private ImageView suhscribe_back;
	private List<Explorer> list;
	private MyAdapter adapter;
	private MyDialog myDialog;
	private View currentItemView;
	private int longClickPosition;
	AttentionTask attentionTask;
	private String item_publicID="";
	SharedPreferencesUtil mSpUtil;
	/** 用来操作sharePreferences标识 */
	private final String SHARE_LOGIN_TAG = "MAP_SHARE_LOGIN_TAG";
	private String SHARE_LOGIN_USERID = "MAP_LOGIN_USERID";
	private String userID, userpublicID;
	private ImageLoader loader;
	DisplayImageOptions defaultOptions;
	private ProgressBar subscribe_bar;
	private TextView subscribe_nodata;
	ImageLoaderConfiguration config;
	private ExecutorService executorService = Executors.newFixedThreadPool(50);
	public static final String TAG = "DiscoverPubServiceMainActivity";
	Handler updateHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			subscribe_bar.setVisibility(View.INVISIBLE);
			if (msg.obj == null) {
				Toast.makeText(DiscoverPubServiceMainActivity.this, "请检查网络", 0)
						.show();
				subscribe_bar.setVisibility(View.GONE);
			} else {
				PublicService result = (PublicService) msg.obj;
				listitem = result.getList();
				MyLog.i("listitemsize" + listitem.size());
				if (listitem.size() == 0) {
					subscribe_nodata.setVisibility(View.VISIBLE);
					subscribe_list.setVisibility(View.GONE);
				} else {
					subscribe_nodata.setVisibility(View.GONE);
					subscribe_list.setVisibility(View.VISIBLE);
					if (adapter == null) {
						adapter = new MyAdapter();
						adapter.bindData(result.getList());
						subscribe_list.setAdapter(adapter);
					} else {
						adapter.bindData(result.getList());
						adapter.notifyDataSetChanged();
					}

				}
			}
		};
	};

	class AttentionTask extends AsyncTask<String, Void, Void> {

		@Override
		protected Void doInBackground(String... params) {
			// TODO Auto-generated method stub
			if (NetUtil.getNetworkState(DiscoverPubServiceMainActivity.this) != NetUtil.NETWORN_NONE) {
				BusinessSearch search = new BusinessSearch();
				try {
					search.attention(DiscoverPubServiceMainActivity.this,
							params[0], params[1]);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			int m = seclect();
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.discover_pubservice_main_activity);
		mSpUtil = SharedPreferencesUtil
				.getInstance(DiscoverPubServiceMainActivity.this);
		myDialog = new MyDialog(this, R.style.MyDialogStyle);
		subscribe_list = (ListView) findViewById(R.id.subscribe_list);
		subscribe_more = (RelativeLayout) findViewById(R.id.subscribe_more);
		suhscribe_back = (ImageView) findViewById(R.id.suhscribe_back);
		subscribe_bar = (ProgressBar) findViewById(R.id.subscribe_bar);
		subscribe_nodata = (TextView) findViewById(R.id.subscribe_nodata);
		subscribe_more.setOnClickListener(this);
		suhscribe_back.setOnClickListener(this);
		userID = WeiBaoApplication.getUserId();
		if (null == userID || userID.equals("")) {
			userID = "0";
		}
	
		defaultOptions = new DisplayImageOptions.Builder()
				.showImageForEmptyUri(R.drawable.empty_photo)
				.showImageOnFail(R.drawable.empty_photo).cacheInMemory(true)
				.considerExifParams(true).cacheOnDisc(true).build();
		config = new ImageLoaderConfiguration.Builder(getApplicationContext())
				.threadPriority(Thread.NORM_PRIORITY - 2)
				.denyCacheImageMultipleSizesInMemory()
				.defaultDisplayImageOptions(defaultOptions)
				.discCacheSize(50 * 1024 * 1024)//
				.discCacheFileCount(100)// 缓存一百张图片
				.writeDebugLogs().build();
		loader = ImageLoader.getInstance();
		if (!loader.isInited()) {
			loader.init(config);
		}
		subscribe_list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(DiscoverPubServiceMainActivity.this,
						DiscoverPubServiceNewsActivity.class);
				PublicServiceItem p1 = (PublicServiceItem) adapter
						.getItem(position);
				intent.putExtra("guanzhu", true);
				intent.putExtra("publicID", p1.getId());
				intent.putExtra("name", p1.getName());
				intent.putExtra("public_photo", p1.getPublic_photo());
				intent.putExtra("fuction", p1.getFuction());
				intent.putExtra("biaozhi", "now");
				startActivity(intent);
				TextView t1 = (TextView) view
						.findViewById(R.id.main_tab_unread_tv);
				t1.setVisibility(View.GONE);
			}
		});
		subscribe_list
				.setOnItemLongClickListener(new OnItemLongClickListener() {

					@Override
					public boolean onItemLongClick(AdapterView<?> arg0,
							View arg1, int arg2, long arg3) {
						// TODO Auto-generated method stub
						myDialog.show();
						PublicServiceItem p1 = (PublicServiceItem) adapter
								.getItem(arg2);
						item_publicID = p1.getId();
						userpublicID = userID + "*" + item_publicID;
						int[] location = new int[2];
						// 获取当前view在屏幕中的绝对位置
						// ,location[0]表示view的x坐标值,location[1]表示view的坐标值
						arg1.getLocationOnScreen(location);
						arg1.setBackgroundColor(getResources().getColor(
								R.color.blue));
						currentItemView = arg1;
						longClickPosition = arg2;
						DisplayMetrics displayMetrics = new DisplayMetrics();
						Display display = DiscoverPubServiceMainActivity.this
								.getWindowManager().getDefaultDisplay();
						display.getMetrics(displayMetrics);
						WindowManager.LayoutParams params = myDialog
								.getWindow().getAttributes();
						params.gravity = Gravity.BOTTOM;
						params.y = display.getHeight() - location[1];
						myDialog.getWindow().setAttributes(params);
						myDialog.setCanceledOnTouchOutside(true);
						myDialog.show();
						return true;
					}
				});
		myDialog.setOnCancelListener(new OnCancelListener() {

			@Override
			public void onCancel(DialogInterface dialog) {
				// TODO Auto-generated method stub
				currentItemView.setBackgroundColor(getResources().getColor(
						android.R.color.white));
			}
		});

	}

	List<PublicServiceItem> listitem;

	@Override
	protected void onRestart() {
		// TODO Auto-generated method stub
		super.onRestart();

	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		userID = WeiBaoApplication.getUserId();
		if (null == userID || userID.equals("")) {
			userID = "0";
		}
		executorService.submit(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				int m = seclect();
				if (m > 0) {
					return;
				} else {
					if (NetUtil
							.getNetworkState(DiscoverPubServiceMainActivity.this) == NetUtil.NETWORN_NONE) {
						Message msg = Message.obtain();
						msg.obj = null;
						updateHandler.sendMessage(msg);

						return;
					}
					attentionTask = new AttentionTask();
					String url = UrlComponent.getSubscribeActivityPath(userID);
					attentionTask.execute(url, userID);
				}

			}
		});
		MobclickAgent.onResume(this);
	}

	
	@Override
	protected void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);
	}

	private int seclect() {
		DBManager manager = DBManager
				.getInstances(DiscoverPubServiceMainActivity.this);
		Cursor cursor;
		if (userID.equals("0")) {
			cursor = manager.query(DiscoverPubServiceMainActivity.this,
					DBInfo.TABLE_NOUSERPUBLIC, userID);
		} else {
			cursor = manager.query(DiscoverPubServiceMainActivity.this,
					DBInfo.TABLE_USERPUBLIC, userID);
		}

		List<PublicServiceItem> list = new ArrayList<PublicServiceItem>();
		PublicService result = new PublicService();
		if (cursor.getCount() > 0) {
			for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor
					.moveToNext()) {
				PublicServiceItem p1 = new PublicServiceItem();
				p1.setId(cursor.getString(cursor.getColumnIndex("publicID")));
				p1.setName(cursor.getString(cursor.getColumnIndex("name")));
				p1.setFuction(cursor.getString(cursor.getColumnIndex("fuction")));
				p1.setPublic_photo(cursor.getString(cursor
						.getColumnIndex("public_photo")));
				p1.setUser_type(cursor.getString(cursor
						.getColumnIndex("user_type")));
				list.add(p1);
			}
			result.setList(list);
			Message msg = Message.obtain();
			msg.obj = result;
			updateHandler.sendMessage(msg);

		} else {
			return 0;
		}
		return cursor.getCount();
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.subscribe_more:
			Intent intent = new Intent(DiscoverPubServiceMainActivity.this,
					DiscoverPubServiceSearchActivity.class);
			startActivity(intent);
			break;
		case R.id.suhscribe_back:
			finish();
			break;
		default:
			break;
		}
	}

	class MyAdapter extends BaseAdapter {
		List<PublicServiceItem> list;
		private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();

		public void bindData(List<PublicServiceItem> list) {
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
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			ViewHolder holder = null;
			if (convertView == null) {
				holder = new ViewHolder();
				convertView = LayoutInflater.from(
						DiscoverPubServiceMainActivity.this).inflate(
						R.layout.discover_pubservice_main_list_item, null);
				holder.explorer_item2_iv2 = (ImageView) convertView
						.findViewById(R.id.explorer_item2_iv2);
				holder.explorer_item2_tv1 = (TextView) convertView
						.findViewById(R.id.explorer_item2_tv1);
				holder.main_tab_unread_tv = (TextView) convertView
						.findViewById(R.id.main_tab_unread_tv);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			DBManager manager = DBManager
					.getInstances(DiscoverPubServiceMainActivity.this);
			PublicServiceItem p1 = list.get(position);
			String publicID = p1.getId();
			// 看设置里有没有要接收和不接受信息
			String accept_news = userID + "*" + publicID;
			SharedPreferencesUtil util = SharedPreferencesUtil
					.getInstance(DiscoverPubServiceMainActivity.this);
			boolean flag = util.get(accept_news, true);
			if (flag) {
				// 从数据库里查找数据库中的未读信息
				int m = 0;
				if (userID.equals("0")) {
					m = manager.selectUic(DiscoverPubServiceMainActivity.this,
							"nouic", userID, p1.getId());
				} else {
					m = manager.selectUic(DiscoverPubServiceMainActivity.this,
							"uic", userID, p1.getId());
				}

				if (m == 0) {
					holder.main_tab_unread_tv.setVisibility(View.GONE);

				} else {
					holder.main_tab_unread_tv.setText(m + "");

				}
			} else {
				holder.main_tab_unread_tv.setVisibility(View.GONE);
			}

			holder.explorer_item2_tv1.setText(p1.getName());
			if (!loader.isInited()) {
				loader.init(config);
			}

			loader.displayImage(p1.getPublic_photo(),
					holder.explorer_item2_iv2, defaultOptions,
					animateFirstListener);
			return convertView;
		}

		class ViewHolder {
			ImageView explorer_item2_iv2;
			TextView explorer_item2_tv1;
			TextView main_tab_unread_tv;
		}

	}

	private class AnimateFirstDisplayListener extends
			SimpleImageLoadingListener {

		final List<String> displayedImages = Collections
				.synchronizedList(new LinkedList<String>());

		@Override
		public void onLoadingComplete(String imageUri, View view,
				Bitmap loadedImage) {
			if (loadedImage != null) {
				ImageView imageView = (ImageView) view;

				boolean firstDisplay = !displayedImages.contains(imageUri);
				if (firstDisplay) {
					// FadeInBitmapDisplayer.animate(imageView, 500);
					displayedImages.add(imageUri);
				}
			}
		}
	}

	@Override
	public void leftOnclick() {
		// TODO Auto-generated method stub
		
		currentItemView.setBackgroundColor(getResources().getColor(
				android.R.color.white));
		DBManager manager = DBManager
				.getInstances(DiscoverPubServiceMainActivity.this);
		boolean flag = manager.deleteSQLiteQuanZhu(
				DiscoverPubServiceMainActivity.this, "uic", userID,
				item_publicID);
		if (flag) {
			ToastUtil.showLong(DiscoverPubServiceMainActivity.this, "缓存清理成功");
		}
		myDialog.dismiss();
	}

	@Override
	public void rightOnclick() {

		// TODO Auto-generated method stub
	
		if (item_publicID.equals("27") || item_publicID.equals("28")
				|| item_publicID.equals("29")) {
			ToastUtil.showLong(DiscoverPubServiceMainActivity.this, "不能取消公众号");
			return;
		}
		currentItemView.setBackgroundColor(getResources().getColor(
				android.R.color.white));
		final String guanzhuPathCancel = UrlComponent.getGuanZhuPathCancel_Get(
				userID, item_publicID);
		MyLog.i(">>>>>guanzhupathCancel" + guanzhuPathCancel);
		// TODO Auto-generated method stub
		if (NetUtil.getNetworkState(DiscoverPubServiceMainActivity.this) == NetUtil.NETWORN_NONE) {
			Toast.makeText(DiscoverPubServiceMainActivity.this, "网络不给力", 0)
					.show();
			return;
		}
		// 取消关注
		new AsyncTask<String, Void, GuanZhu>() {

			@Override
			protected GuanZhu doInBackground(String... params) {
				// TODO Auto-generated method stub
				GuanZhu g1 = new GuanZhu();
				if (NetUtil
						.getNetworkState(DiscoverPubServiceMainActivity.this) == NetUtil.NETWORN_NONE) {
					return null;
				} else {
					String json = ApiClient.connServerForResult(params[0]);
					try {
						JSONObject j1 = new JSONObject(json);
						g1.setStatus(j1.getBoolean("status"));
						g1.setMsg(j1.getString("msg"));
					} catch (Exception e) {
						// TODO: handle exception
						e.printStackTrace();
					}
					return g1;
				}
			}

			protected void onPostExecute(GuanZhu result) {
				if (null == result) {
					myDialog.dismiss();
					return;
				}
				if (result.isStatus()) {
					myDialog.dismiss();
					mSpUtil.Setbutton(userpublicID, true);
					ToastUtil.showLong(DiscoverPubServiceMainActivity.this,
							"取消成功");
					DBManager manager = DBManager
							.getInstances(DiscoverPubServiceMainActivity.this);
					manager.deleteSQLiteQuanZhu(
							DiscoverPubServiceMainActivity.this, "uic", userID,
							item_publicID);
					manager.deleteSQLiteQuanZhu(
							DiscoverPubServiceMainActivity.this,
							DBInfo.TABLE_USERPUBLIC, userID, item_publicID);
					final String path = UrlComponent
							.getSubscribeActivityPath(userID);
					Cursor cursor = manager.query(
							DiscoverPubServiceMainActivity.this,
							DBInfo.TABLE_USERPUBLIC, userID);
					PublicService service = new PublicService();
					List<PublicServiceItem> list = new ArrayList<PublicServiceItem>();

					if (cursor.getCount() > 0) {
						for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor
								.moveToNext()) {
							PublicServiceItem p1 = new PublicServiceItem();
							p1.setId(cursor.getString(cursor
									.getColumnIndex("publicID")));
							p1.setName(cursor.getString(cursor
									.getColumnIndex("name")));
							p1.setFuction(cursor.getString(cursor
									.getColumnIndex("fuction")));
							p1.setPublic_photo(cursor.getString(cursor
									.getColumnIndex("public_photo")));
							p1.setUser_type(cursor.getString(cursor
									.getColumnIndex("user_type")));
							list.add(p1);
						}
						service.setList(list);
						Message msg = Message.obtain();
						msg.obj = service;
						updateHandler.sendMessage(msg);
					}

				} else {
					ToastUtil.showLong(DiscoverPubServiceMainActivity.this,
							result.getMsg());
				}
			}
		}.execute(guanzhuPathCancel);

	}
}
