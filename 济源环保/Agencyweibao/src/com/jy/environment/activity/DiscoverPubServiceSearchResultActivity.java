package com.jy.environment.activity;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.jy.environment.R;
import com.jy.environment.base.ActivityBase;
import com.jy.environment.business.BusinessSearch;
import com.jy.environment.controls.AsyncTask;
import com.jy.environment.model.SearchService;
import com.jy.environment.model.SearchServiceItem;
import com.jy.environment.util.ApiClient;
import com.jy.environment.webservice.UrlComponent;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;
import com.nostra13.universalimageloader.core.assist.SimpleImageLoadingListener;
import com.umeng.analytics.MobclickAgent;

/**
 * 公众服务搜索公众号结果列表
 * 
 * @author baiyuchuan
 * 
 */
public class DiscoverPubServiceSearchResultActivity extends ActivityBase
		implements OnClickListener {
	private ListView search_lv;
	private ImageView search_back;
	private MyAdapter adapter;
	private ProgressBar search_bar;
	private TextView search_yonghu;
	// 用户userID,公众号id
	private String userID, gongzhongID;
	/** 用来操作sharePreferences标识 */
	private final String SHARE_LOGIN_TAG = "MAP_SHARE_LOGIN_TAG";
	private String SHARE_LOGIN_USERID = "MAP_LOGIN_USERID";
	private ImageLoader loader;
	DisplayImageOptions defaultOptions;
	// 搜索异步
	SearchPubServiceListTask searchPubServiceListTask;
	private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.discover_pubservice_search_result_activity);
		SharedPreferences share = getSharedPreferences(SHARE_LOGIN_TAG, 0);
		userID = share.getString(SHARE_LOGIN_USERID, "");
		if (userID.equals("")) {
			userID = "0";
		}
		search_lv = (ListView) findViewById(R.id.search_lv);
		search_back = (ImageView) findViewById(R.id.search_back);
		search_bar = (ProgressBar) findViewById(R.id.search_bar);
		search_yonghu = (TextView) findViewById(R.id.search_yonghu);
		search_back.setOnClickListener(this);
		defaultOptions = new DisplayImageOptions.Builder()
				.showImageForEmptyUri(R.drawable.empty_photo)
				.showImageOnFail(R.drawable.empty_photo).cacheInMemory(true)
				.cacheOnDisc(true).build();
		loader = ImageLoader.getInstance();
		adapter = new MyAdapter();
		String searchString = getIntent().getStringExtra("search");
		searchPubServiceListTask = new SearchPubServiceListTask();
		searchPubServiceListTask.execute(searchString);
		search_lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int id,
					long m) {
				// TODO Auto-generated method stub
				final SearchServiceItem p1 = (SearchServiceItem) adapter
						.getItem(id);
				gongzhongID = p1.getId();
				String usePath = UrlComponent
						.getusePathGet(userID, gongzhongID);
				System.out.println(">>>>>>ghhgh" + usePath);
				new AsyncTask<String, Void, Boolean>() {

					@Override
					protected Boolean doInBackground(String... params) {
						// TODO Auto-generated method stub
						Boolean flag = null;
						String json = ApiClient.connServerForResult(params[0]);
						try {
							JSONObject j1 = new JSONObject(json);
							flag = j1.getBoolean("flag");
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						return flag;
					}

					protected void onPostExecute(Boolean result) {
						if (result.booleanValue())
						// 跳转到取消关注页面
						{
							Intent intent = new Intent(
									DiscoverPubServiceSearchResultActivity.this,
									DiscoverPubServiceAttentionCancelActivity.class);
							intent.putExtra("publicID", p1.getId());
							intent.putExtra("name", p1.getName());
							intent.putExtra("fuction", p1.getFuction());
							intent.putExtra("public_photo",
									p1.getPublic_photo());
							intent.putExtra("flag_bz", "SearchActivity");
						} else {
							Intent intent = new Intent(
									DiscoverPubServiceSearchResultActivity.this,
									DiscoverPubServiceAddAttentionActivity.class);
							intent.putExtra("publicID", p1.getId());
							intent.putExtra("name", p1.getName());
							intent.putExtra("fuction", p1.getFuction());
							intent.putExtra("public_photo",
									p1.getPublic_photo());
							intent.putExtra("flag_bz", "SearchActivity");
							startActivity(intent);
						}
					};

				}.execute(usePath);

			}
		});

	}

	class SearchPubServiceListTask extends
			AsyncTask<String, Void, SearchService> {
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
		}

		@Override
		protected SearchService doInBackground(String... params) {
			// TODO Auto-generated method stub
			String path = UrlComponent.getSearchPath_Get(params[0]);
			BusinessSearch search = new BusinessSearch();
			SearchService _Result = null;
			try {
				_Result = search.searchPubServiceList(path);
				return _Result;
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(SearchService result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			search_bar.setVisibility(View.GONE);
			if (result != null) {
				if (result.isFlag() && result.getList().size() > 0) {
					adapter.bindData(result.getList());
					search_lv.setAdapter(adapter);
				} else {
					search_yonghu.setVisibility(View.VISIBLE);
					search_lv.setVisibility(View.GONE);
				}

			}
		}
	}

	class MyAdapter extends BaseAdapter {
		List<SearchServiceItem> list;

		public void bindData(List<SearchServiceItem> list) {
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
						DiscoverPubServiceSearchResultActivity.this).inflate(
						R.layout.discover_pubservice_search_result_item, null);
				holder.search_iv1 = (ImageView) convertView
						.findViewById(R.id.search_iv1);
				holder.search_tv1 = (TextView) convertView
						.findViewById(R.id.search_tv1);

				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			SearchServiceItem item = list.get(position);
			loader.displayImage(item.getPublic_photo(), holder.search_iv1,
					defaultOptions, animateFirstListener);
			holder.search_tv1.setText(item.getName());
			return convertView;
		}

		class ViewHolder {
			ImageView search_iv1;
			TextView search_tv1;

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
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.search_back:
			finish();
			break;

		default:
			break;
		}
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		MobclickAgent.onPause(this);
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		MobclickAgent.onResume(this);
	}
	
	
}
