package com.jy.environment.activity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.jy.environment.R;
import com.jy.environment.base.ActivityBase;
import com.jy.environment.controls.AsyncTask;
import com.jy.environment.controls.WeiBaoApplication;
import com.jy.environment.database.dal.DBInfo;
import com.jy.environment.database.dal.DBManager;
import com.jy.environment.invitefriends.ClearEditText;
import com.jy.environment.model.PublicService;
import com.jy.environment.model.PublicServiceItem;
import com.jy.environment.util.ApiClient;
import com.jy.environment.util.JsonUtils;
import com.jy.environment.util.MyLog;
import com.jy.environment.util.NetUtil;
import com.jy.environment.util.ToastUtil;
import com.jy.environment.webservice.UrlComponent;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;
import com.nostra13.universalimageloader.core.assist.SimpleImageLoadingListener;
import com.umeng.analytics.MobclickAgent;

/**
 * 公众服务搜索公众号
 * 
 * @author baiyuchuan
 * 
 */
public class DiscoverPubServiceSearchActivity extends ActivityBase implements
		OnClickListener {
	private GridView public_services_gridview;
	private ImageView public_services_back;
	List<PublicService> list;
	// 用户没有登录的点击精品推荐跳到登陆界面
	Intent intent;
	String to;
	private MyAdapter adapter;
	private ProgressBar public_services_bar;
	private ClearEditText public_services_filter_edit;
	private TextView public_services_filter_edit_search;
	private ImageLoader loader;
	/** 用来操作sharePreferences标识 */
	private final String SHARE_LOGIN_TAG = "MAP_SHARE_LOGIN_TAG";
	private String SHARE_LOGIN_USERID = "MAP_LOGIN_USERID";
	// File file;
	DisplayImageOptions defaultOptions;

	ImageLoaderConfiguration config;
	private String path = UrlComponent.jingpin_path;
	private String userID, publicID;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.discover_pubservice_search_activity);
		userID = WeiBaoApplication.getUserId();
		MyLog.i(">>>>>userID" + userID);
		defaultOptions = new DisplayImageOptions.Builder()
				.showImageForEmptyUri(R.drawable.empty_photo)
				.showImageOnFail(R.drawable.empty_photo).cacheInMemory(true)
				.cacheOnDisc(true).build();
		config = new ImageLoaderConfiguration.Builder(getApplicationContext())
				.defaultDisplayImageOptions(defaultOptions)
				.discCacheSize(50 * 1024 * 1024)//
				.discCacheFileCount(100)// 缓存一百张图片
				.writeDebugLogs().build();
		loader = ImageLoader.getInstance();
		loader.init(config);
		init();

		public_services_gridview
				.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> arg0, View arg1,
							int arg2, long arg3) {
						// TODO Auto-generated method stub
						final PublicServiceItem p1 = (PublicServiceItem) adapter
								.getItem(arg2);
						publicID = p1.getId();
						if (publicID.equals("27") || publicID.equals("28")
								|| publicID.equals("29")
								|| publicID.equals("30")
								|| publicID.equals("32")) {
							Intent intent = new Intent(
									DiscoverPubServiceSearchActivity.this,
									DiscoverPubServiceAttentionCancelActivity.class);
							intent.putExtra("publicID", p1.getId());
							intent.putExtra("name", p1.getName());
							intent.putExtra("fuction", p1.getFuction());
							intent.putExtra("public_photo",
									p1.getPublic_photo());
							intent.putExtra("flag_bz", "PublicServiceActivity");
							startActivity(intent);
							return;
						}
						if (NetUtil
								.getNetworkState(DiscoverPubServiceSearchActivity.this) == NetUtil.NETWORN_NONE) {
							Toast.makeText(
									DiscoverPubServiceSearchActivity.this,
									"请检查网络设置", 0).show();
							return;
						}
						if (userID.equals("0")) {
							new Thread() {
								public void run() {
									try {
										currentThread().sleep(500);
										Intent intent = new Intent(
												DiscoverPubServiceSearchActivity.this,
												UserLoginActivity.class);
										intent.putExtra("from", "gongzhong");
										startActivity(intent);
									} catch (InterruptedException e) {
										e.printStackTrace();
									}
								};
							}.start();
							return;
						}

						MyLog.i(">>>>>>>huanbaobaike" + publicID);
						String usePath = UrlComponent.getusePathGet(userID,
								publicID);
						new AsyncTask<String, Void, Boolean>() {

							@Override
							protected Boolean doInBackground(String... params) {
								// TODO Auto-generated method stub
								Boolean flag = null;
								if (NetUtil
										.getNetworkState(DiscoverPubServiceSearchActivity.this) == NetUtil.NETWORN_NONE) {
									return null;
								} else {
									String json = ApiClient
											.connServerForResult(params[0]);
									try {
										JSONObject j1 = new JSONObject(json);
										flag = j1.getBoolean("flag");
									} catch (JSONException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
								}
								return flag;
							}

							protected void onPostExecute(Boolean result) {
								if (result == null) {
									return;
								}
								if (result)
								// 跳转到取消关注页面
								{
									Intent intent = new Intent(
											DiscoverPubServiceSearchActivity.this,
											DiscoverPubServiceAttentionCancelActivity.class);
									intent.putExtra("publicID", p1.getId());
									intent.putExtra("name", p1.getName());
									intent.putExtra("fuction", p1.getFuction());
									intent.putExtra("public_photo",
											p1.getPublic_photo());
									intent.putExtra("flag_bz",
											"PublicServiceActivity");
									startActivity(intent);
								} else {
									Intent intent = new Intent(
											DiscoverPubServiceSearchActivity.this,
											DiscoverPubServiceAddAttentionActivity.class);
									intent.putExtra("publicID", p1.getId());
									intent.putExtra("name", p1.getName());
									intent.putExtra("fuction", p1.getFuction());
									intent.putExtra("public_photo",
											p1.getPublic_photo());
									startActivity(intent);
								}
							};

						}.execute(usePath);
					}
				});

	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		userID = WeiBaoApplication.getUserId();
		if (null == userID || userID.equals("")) {
			userID = "0";
		}
		MyLog.i(">>>>>>ghgh" + userID);
		new MyTask().execute(path);
		MobclickAgent.onResume(this);
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		MobclickAgent.onPause(this);
	}

	public void init() {
		public_services_bar = (ProgressBar) findViewById(R.id.public_services_bar);
		public_services_filter_edit = (ClearEditText) findViewById(R.id.public_services_filter_edit);
		public_services_back = (ImageView) findViewById(R.id.public_services_back);
		public_services_filter_edit_search = (TextView) findViewById(R.id.public_services_filter_edit_search);
		public_services_filter_edit_search.setTextColor(getResources()
				.getColor(R.color.green_dark));
		public_services_gridview = (GridView) findViewById(R.id.public_services_gridview);
		public_services_filter_edit_search.setOnClickListener(this);
		public_services_back.setOnClickListener(this);
		adapter = new MyAdapter();

	}

	class MyTask extends AsyncTask<String, Void, PublicService> {

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
		}

		@Override
		protected PublicService doInBackground(String... params) {
			DBManager manager = DBManager
					.getInstances(DiscoverPubServiceSearchActivity.this);
			Cursor cursor = manager.query(
					DiscoverPubServiceSearchActivity.this,
					DBInfo.TABLE_JINGPIN, null);
			List<PublicServiceItem> list = null;
			if (cursor.getCount() > 0) {
				list = new ArrayList<PublicServiceItem>();
				for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor
						.moveToNext()) {
					PublicServiceItem item = new PublicServiceItem();
					item.setId(cursor.getString(cursor
							.getColumnIndex("publicID")));
					item.setName(cursor.getString(cursor.getColumnIndex("name")));
					item.setPublic_photo(cursor.getString(cursor
							.getColumnIndex("public_photo")));
					item.setFuction(cursor.getString(cursor
							.getColumnIndex("fuction")));
					list.add(item);
				}
				PublicService p1 = new PublicService();
				p1.setList(list);
				return p1;
			} else {
				if (NetUtil
						.getNetworkState(DiscoverPubServiceSearchActivity.this) != NetUtil.NETWORN_NONE) {
					String shuju = ApiClient.connServerForResult(params[0]);
					return JsonUtils.jsonService(
							DiscoverPubServiceSearchActivity.this, shuju);
				} else {
					Looper.prepare();
					Toast.makeText(DiscoverPubServiceSearchActivity.this,
							"请检查网络", 0).show();
					Looper.loop();
					return null;
				}
			}
		}

		@Override
		protected void onPostExecute(PublicService result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			public_services_bar.setVisibility(View.GONE);
			if (result == null) {
				ToastUtil.showLong(DiscoverPubServiceSearchActivity.this,
						"网络出问题,请检查网络设置");
			} else {
				adapter.bindData(result.getList());
				public_services_gridview.setAdapter(adapter);
			}
		}

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.public_services_back:
			finish();
			break;
		case R.id.public_services_filter_edit_search:
			search();

			break;
		default:
			break;
		}
	}

	private void search() {
		MobclickAgent.onEvent(DiscoverPubServiceSearchActivity.this,
				"FXServerSearch");
		String searchString = public_services_filter_edit.getText().toString()
				.trim();
		if (NetUtil.getNetworkState(DiscoverPubServiceSearchActivity.this) == NetUtil.NETWORN_NONE) {
			Toast.makeText(DiscoverPubServiceSearchActivity.this, "请检查网络设置", 0)
					.show();
			return;
		}
		if (searchString.length() == 0) {
			Toast t = Toast.makeText(DiscoverPubServiceSearchActivity.this,
					"请输入要查找的公众号", 0);
			t.show();
		} else {
			Intent intent = new Intent(DiscoverPubServiceSearchActivity.this,
					DiscoverPubServiceSearchResultActivity.class);
			intent.putExtra("search", searchString);
			startActivity(intent);
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

	class MyAdapter extends BaseAdapter {
		List<PublicServiceItem> list;
		private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();

		void bindData(List<PublicServiceItem> list) {
			this.list = list;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return list.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return list.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		ViewHolder holder = null;

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub

			if (convertView == null) {
				holder = new ViewHolder();
				convertView = LayoutInflater
						.from(DiscoverPubServiceSearchActivity.this)
						.inflate(
								R.layout.discover_pubservice_search_gridview_item,
								null);

				holder.explorer_item_iv1 = (ImageView) convertView
						.findViewById(R.id.public_services_gridview_item_iv);
				holder.explorer_item_tv1 = (TextView) convertView
						.findViewById(R.id.public_services_gridview_item_iv_tv);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			PublicServiceItem pService = list.get(position);
			holder.explorer_item_tv1.setText(pService.getName());
			holder.explorer_item_iv1.setImageResource(R.drawable.empty_photo);
			if (!loader.isInited()) {
				loader.init(config);
			}
			loader.displayImage(pService.getPublic_photo(),
					holder.explorer_item_iv1, defaultOptions,
					animateFirstListener);
			return convertView;
		}

	}

	static class ViewHolder {
		ImageView explorer_item_iv1;
		TextView explorer_item_tv1;
	}
}
