package com.jy.environment.activity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jy.environment.R;
import com.jy.environment.base.ActivityBase;
import com.jy.environment.business.BusinessSearch;
import com.jy.environment.controls.AsyncTask;
import com.jy.environment.controls.WeiBaoApplication;
import com.jy.environment.database.dal.DbHelper;
import com.jy.environment.model.ChatMsg;
import com.jy.environment.model.ChatMsgEntity;
import com.jy.environment.util.MyLog;
import com.jy.environment.webservice.UrlComponent;
import com.jy.environment.widget.DropdownListView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;
import com.nostra13.universalimageloader.core.assist.SimpleImageLoadingListener;

/**
 * 公众服务查看历史消息
 * 
 * @author baiyuchuan
 * 
 */
public class DiscoverPubServiceHistoryNewsActivity extends ActivityBase
		implements OnClickListener {
	private ListView subscription_news_scrolll;
	private DropdownListView subscription_news_scroll;
	private MyAdapter adapter;
	private ImageView subscription_news_iv, subscription_news_iv2;
	private ProgressBar subscription_news_bar;
	private DbHelper db;
	Intent intent;
	// 聊天相对布局隐藏
	private RelativeLayout rl_bottom;
	private String userID, publicID;
	private TextView subscription_news_tv1, subscription_news_nodata;
	private String name, fuction, public_photo, biaozhi;
	List<ChatMsgEntity> list = new ArrayList<ChatMsgEntity>();
	private ImageLoader loader;
	DisplayImageOptions defaultOptions;
	private String path, usidhistory;
	// 保存历史消息名字
	private String publichistory;
	// 获得这个实例的对象
	private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();
	// 发送按钮
	private Button subscription_news_btn_send;
	// 点击个人设置具体要进入哪一个界面
	private boolean guanzhu;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		setContentView(R.layout.discover_pubservice_news_activity);
		intent = getIntent();
		init();
		publicID = intent.getStringExtra("publicID");
		path = UrlComponent.getNews_HistoPath_Get(publicID);
		userID = WeiBaoApplication.getUserId();
		usidhistory = userID + "*" + publicID + "history.json";
		guanzhu = intent.getBooleanExtra("guanzhu", false);
		defaultOptions = new DisplayImageOptions.Builder()
				.showImageForEmptyUri(R.drawable.newsloading)
				.showImageOnFail(R.drawable.newsloading).cacheInMemory(true)
				.cacheOnDisc(true).build();
		loader = ImageLoader.getInstance();
		publichistory = publicID + "history.json";
		name = intent.getStringExtra("name");
		fuction = intent.getStringExtra("fuction");
		public_photo = intent.getStringExtra("public_photo");
		subscription_news_tv1.setText(name);
		new MyTask().execute(path);
		subscription_news_scrolll
				.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> arg0, View arg1,
							int arg2, long arg3) {
						// TODO Auto-generated method stub
						Intent intent = new Intent(
								DiscoverPubServiceHistoryNewsActivity.this,
								DiscoverPubServiceWebViewActivity.class);
						ChatMsgEntity chat1 = (ChatMsgEntity) adapter
								.getItem(arg2);
						String xiaoxi_id = chat1.getXiaoxi_id();
						String title = chat1.getTitle();
						intent.putExtra("xiaoxi_id", xiaoxi_id);
						intent.putExtra("title", title);
						startActivity(intent);
					}
				});

	}

	public void init() {
		subscription_news_bar = (ProgressBar) findViewById(R.id.subscription_news_bar);
		subscription_news_scrolll = (ListView) findViewById(R.id.subscription_news_scrolll);
		subscription_news_scroll = (DropdownListView) findViewById(R.id.subscription_news_scroll);
		subscription_news_scroll.setVisibility(View.GONE);
		subscription_news_scrolll.setVisibility(View.VISIBLE);
		subscription_news_tv1 = (TextView) findViewById(R.id.subscription_news_tv1);
		subscription_news_btn_send = (Button) findViewById(R.id.subscription_news_btn_send);
		rl_bottom = (RelativeLayout) findViewById(R.id.rl_bottom);
		rl_bottom.setVisibility(View.GONE);
		subscription_news_iv = (ImageView) findViewById(R.id.subscription_news_iv);
		subscription_news_iv2 = (ImageView) findViewById(R.id.subscription_news_iv2);
		subscription_news_iv2.setVisibility(View.INVISIBLE);
		subscription_news_iv2.setOnClickListener(this);
		subscription_news_nodata = (TextView) findViewById(R.id.subscription_news_nodata);
		subscription_news_iv.setOnClickListener(this);
		subscription_news_btn_send.setOnClickListener(this);
		adapter = new MyAdapter();

	}

	class MyAdapter extends BaseAdapter {

		private List<ChatMsgEntity> list;
		private LayoutInflater mInflater;

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return list.size();
		}

		public void bindData(List<ChatMsgEntity> list) {
			mInflater = LayoutInflater
					.from(DiscoverPubServiceHistoryNewsActivity.this);
			this.list = list;
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

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			try {
				ChatMsgEntity entity = list.get(position);
				ViewHolder viewHolder = null;
				if (convertView == null) {
					convertView = mInflater.inflate(
							R.layout.discover_pubservice_news_right_item, null);

					viewHolder = new ViewHolder();
					viewHolder.chat_tv1 = (TextView) convertView
							.findViewById(R.id.chat_tv1);
					viewHolder.chat_tv2 = (TextView) convertView
							.findViewById(R.id.chat_tv2);
					viewHolder.chat_tv3 = (TextView) convertView
							.findViewById(R.id.chat_tv3);
					viewHolder.chat_iv1 = (ImageView) convertView
							.findViewById(R.id.chat_iv1);
					convertView.setTag(viewHolder);
				} else {
					viewHolder = (ViewHolder) convertView.getTag();
				}
				String currentTime = entity.getCreate_time();
				SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
				String currentTimeSeconds, previewTimeSeconds;
					currentTimeSeconds = sdf1.parse(currentTime).getTime() / 1000
							+ "";
					String path = entity.getFace_pic_url();
					viewHolder.chat_iv1.setImageResource(R.drawable.newsloading);
					if (path.equals("")) {
						viewHolder.chat_iv1.setVisibility(View.GONE);
					} else {
						viewHolder.chat_iv1.setVisibility(View.VISIBLE);
						loader.displayImage(entity.getFace_pic_url(),
								viewHolder.chat_iv1, defaultOptions,
								animateFirstListener);
					}
					String time = entity.getCreate_time();
					// 显示消息更新的时间
					viewHolder.chat_tv2.setText(getTime(time));
					viewHolder.chat_tv1.setText(entity.getTitle());
					viewHolder.chat_tv3.setText(entity.getSummary());
				
			} catch (Exception e) {
				// TODO: handle exception
				MyLog.e("weibao Exception" + e);
			}
			
			return convertView;
		}

		class ViewHolder {
			public TextView chat_tv1;
			public TextView chat_tv2;
			public TextView chat_tv3;
			public ImageView chat_iv1;
		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.subscription_news_btn_send:
			break;
		// 返回键
		case R.id.subscription_news_iv:
			finish();
			break;

		default:
			break;
		}
	}

	

	class MyTask extends AsyncTask<String, Void, ChatMsg> {
		@Override
		protected ChatMsg doInBackground(String... params) {
			String url = UrlComponent.getNews_HistoPath_Get(publicID);
			BusinessSearch search = new BusinessSearch();
			ChatMsg _Result = null;
			try {
				_Result = search.getHistoryNews(url);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return _Result;

		}

		@Override
		protected void onPostExecute(ChatMsg result) {
			super.onPostExecute(result);
			subscription_news_bar.setVisibility(View.GONE);
			if (null == result) {
				Toast.makeText(DiscoverPubServiceHistoryNewsActivity.this,
						"网络出问题,请检查网络设置", 0).show();
			} else {
				if (result.getList().size() == 0) {
					subscription_news_nodata.setVisibility(View.VISIBLE);
					subscription_news_scrolll.setVisibility(View.GONE);
				} else {
					subscription_news_nodata.setVisibility(View.GONE);
					subscription_news_scrolll.setVisibility(View.VISIBLE);
					List<ChatMsgEntity> listhistory = result.getList();
					Collections.reverse(listhistory);
					adapter.bindData(listhistory);
					subscription_news_scrolll.setAdapter(adapter);
				}
			}

		}
	}
	private String getTime(String time) {
		String data = null;
		String yue = time.substring(5, 7);
		yue = (yue.substring(0, 1).equals("0")) ? yue.substring(1) : yue;
		String ri = time.substring(8, 10);
		ri = (ri.substring(0, 1).equals("0")) ? ri.substring(1) : ri;
		String shi = time.substring(11, 13);
		shi = (shi.substring(0, 1).equals("0")) ? shi.substring(1) : shi;
		String fen = time.substring(14, 16);
		fen = (fen.substring(0, 1).equals("0")) ? fen.substring(1) : fen;
		data = yue + "月" + ri + "日" + "\t\t" + shi + "时" + fen + "分";
		return data;
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
					displayedImages.add(imageUri);
				}
			}
		}

	}
}
