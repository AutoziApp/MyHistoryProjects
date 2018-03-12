package com.jy.environment.activity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jy.environment.R;
import com.jy.environment.base.ActivityBase;
import com.jy.environment.controls.WeiBaoApplication;
import com.jy.environment.database.dal.DBManager;
import com.jy.environment.database.dal.DbHelper;
import com.jy.environment.model.ChatMsg;
import com.jy.environment.model.ChatMsgEntity;
import com.jy.environment.util.ApiClient;
import com.jy.environment.util.DateUtil;
import com.jy.environment.util.JsonUtils;
import com.jy.environment.util.KjhttpUtils;
import com.jy.environment.util.MyLog;
import com.jy.environment.util.NetUtil;
import com.jy.environment.util.ToastUtil;
import com.jy.environment.util.KjhttpUtils.DownGet;
import com.jy.environment.webservice.UrlComponent;
import com.jy.environment.widget.DropdownListView;
import com.jy.environment.widget.DropdownListView.OnRefreshListenerHeader;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;
import com.nostra13.universalimageloader.core.assist.SimpleImageLoadingListener;

/**
 * 公众服务消息
 * 
 * @author baiyuchuan
 * 
 */
public class DiscoverPubServiceNewsActivity extends ActivityBase implements
		OnClickListener {
	private DropdownListView subscription_news_scroll;
	private MyAdapter adapter;
	private ImageView subscription_news_iv, subscription_news_iv2;
	private ProgressBar subscription_news_bar;
	private RelativeLayout rl_bottom;
	private DbHelper db;
	Intent intent;
	int page = 0;
	int position = 0;
	private String userID, publicID, msg;
	private TextView subscription_news_tv1, subscription_news_nodata;
	private String name, fuction, public_photo, biaozhi;
	List<ChatMsgEntity> list = new ArrayList<ChatMsgEntity>();
	private ImageLoader loader;
	DisplayImageOptions defaultOptions;
	DisplayImageOptions defaultOptionss;
	private boolean scrollFlag = false;// 标记是否滑动

	// 获得这个实例的对象
	public static DiscoverPubServiceNewsActivity sm;
	private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();
	// 发送按钮
	private Button subscription_news_btn_send;
	private EditText mEditTextContent;
	private KjhttpUtils http;
	// 点击个人设置具体要进入哪一个界面
	private boolean guanzhu;
	private ExecutorService executorService = Executors.newFixedThreadPool(50);
	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.arg1) {
			case 1:
				String xiaoxi_id = (String) msg.obj;
				ChatMsgEntity chat = new ChatMsgEntity();
				DBManager manager = DBManager
						.getInstances(DiscoverPubServiceNewsActivity.this);
				Cursor cursor = manager.selectUicXiaoXI(
						DiscoverPubServiceNewsActivity.this, "uic", xiaoxi_id);
				String message = cursor.getString(cursor
						.getColumnIndex("message"));
				String create_time = cursor.getString(cursor
						.getColumnIndex("publish_time"));
				if (null != message) {
					mEditTextContent.setText("");
					chat.setMessage(message);
					chat.setCreate_time(create_time);
					list.add(chat);
					if (list.size() == 1) {
						subscription_news_nodata.setVisibility(View.GONE);
						subscription_news_scroll.setVisibility(View.VISIBLE);
						subscription_news_scroll.setAdapter(adapter);
						adapter.bindData(list);
						adapter.notifyDataSetChanged();
					} else {
						adapter.notifyDataSetChanged();
					}
					subscription_news_scroll.setSelection(list.size() - 1);
				}
				break;
			case 2:
				List<ChatMsgEntity> listnew = (List<ChatMsgEntity>) msg.obj;
				list.addAll(0, listnew);
				adapter.bindData(list);
				adapter.notifyDataSetChanged();
				subscription_news_scroll.setSelection(listnew.size());
				subscription_news_scroll.onRefreshCompleteHeader();
				break;
			case 3:
				switch (msg.what) {
				case 1:
					InputMethodManager inputMethodManager = (InputMethodManager) DiscoverPubServiceNewsActivity.this
							.getApplicationContext().getSystemService(
									Context.INPUT_METHOD_SERVICE);
					inputMethodManager.hideSoftInputFromWindow(
							mEditTextContent.getWindowToken(), 0); // 隐藏
					ChatMsgEntity entity = (ChatMsgEntity) msg.obj;
					list.add(entity);
					if (list.size() == 1) {
						mEditTextContent.setText("");
						subscription_news_nodata.setVisibility(View.GONE);
						subscription_news_scroll.setVisibility(View.VISIBLE);
						subscription_news_scroll.setAdapter(adapter);
						adapter.bindData(list);
						adapter.notifyDataSetChanged();
					} else {
						mEditTextContent.setText("");
						adapter.bindData(list);
						adapter.notifyDataSetChanged();
					}
					subscription_news_scroll.setSelection(list.size() - 1);
					break;
				case 2:
					mEditTextContent.setText("");
					adapter.notifyDataSetChanged();
					subscription_news_scroll.setSelection(list.size() - 1);
					break;
				default:
					break;
				}
				break;
			case 4:
				adapter.bindData(list);
				subscription_news_scroll.setAdapter(adapter);
				adapter.notifyDataSetChanged();
				break;
			default:
				break;
			}
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		setContentView(R.layout.discover_pubservice_news_activity);
		sm = this;
		intent = getIntent();
		userID = WeiBaoApplication.getUserId();
		http = new KjhttpUtils(this, null);
		if (null == userID || userID.equals("")) {
			userID = "0";
		}
		init();
		guanzhu = intent.getBooleanExtra("guanzhu", false);
		defaultOptions = new DisplayImageOptions.Builder()
				.showImageForEmptyUri(R.drawable.newsloading)
				.showImageOnFail(R.drawable.newsloading).cacheInMemory(true)
				.cacheOnDisc(true).build();
		defaultOptionss = new DisplayImageOptions.Builder()
				.showImageForEmptyUri(R.drawable.logo31)
				.showImageOnFail(R.drawable.logo31).cacheInMemory(true)
				.cacheOnDisc(true).build();
		loader = ImageLoader.getInstance();

		publicID = intent.getStringExtra("publicID");
		name = intent.getStringExtra("name");
		biaozhi = intent.getStringExtra("biaozhi");
		fuction = intent.getStringExtra("fuction");
		public_photo = intent.getStringExtra("public_photo");
		String url = UrlComponent.getNews_HistoPath_Get(publicID);
		subscription_news_tv1.setText(name);
		DBManager dbManager = DBManager.getInstances(getApplicationContext());
		if (biaozhi.equals("now")) {
			Cursor cursor = dbManager.queryUICHistory(
					DiscoverPubServiceNewsActivity.this, userID, publicID, "5",
					0);
			MyLog.i("cursor>>>>>" + publicID + ">>>>" + cursor.getCount());
			if (cursor.getCount() > 0) {
				for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor
						.moveToNext()) {
					ChatMsgEntity chat1 = new ChatMsgEntity();
					chat1.setTitle(cursor.getString(cursor
							.getColumnIndex("title")));
					chat1.setCreate_time(cursor.getString(cursor
							.getColumnIndex("publish_time")));
					chat1.setSummary(cursor.getString(cursor
							.getColumnIndex("summary")));
					chat1.setFace_pic_url(cursor.getString(cursor
							.getColumnIndex("face_pic_url")));
					chat1.setXiaoxi_id(cursor.getString(cursor
							.getColumnIndex("xiaoxi_id")));
					chat1.setMessage(cursor.getString(cursor
							.getColumnIndex("message")));
					chat1.setContent(cursor.getString(cursor
							.getColumnIndex("content")));
					list.add(chat1);
					MyLog.i(">>>>>>>subscription" + list);
					if (cursor.getString(cursor.getColumnIndex("isread"))
							.equals("0")) {
						ContentValues values = new ContentValues();
						values.put("isread", 1);
						if (userID.equals("0")) {
							dbManager.update(
									DiscoverPubServiceNewsActivity.this,
									"nouic", userID, publicID, values);
						} else {
							dbManager.update(
									DiscoverPubServiceNewsActivity.this, "uic",
									userID, publicID, values);
						}

					}
				}
				Collections.reverse(list);
			} else {
				Cursor cursor2 = dbManager.queryUICHistory(
						DiscoverPubServiceNewsActivity.this, userID, publicID,
						"5", 0);
				for (cursor2.moveToFirst(); !cursor2.isAfterLast(); cursor2
						.moveToNext()) {
					ChatMsgEntity chat1 = new ChatMsgEntity();
					chat1.setTitle(cursor2.getString(cursor2
							.getColumnIndex("title")));
					chat1.setCreate_time(cursor2.getString(cursor2
							.getColumnIndex("publish_time")));
					chat1.setSummary(cursor2.getString(cursor2
							.getColumnIndex("summary")));
					chat1.setFace_pic_url(cursor2.getString(cursor2
							.getColumnIndex("face_pic_url")));
					chat1.setXiaoxi_id(cursor2.getString(cursor2
							.getColumnIndex("xiaoxi_id")));
					chat1.setMessage(cursor2.getString(cursor2
							.getColumnIndex("message")));
					chat1.setContent(cursor2.getString(cursor2
							.getColumnIndex("content")));
					list.add(chat1);
				}
				Collections.reverse(list);
			}

		}

		if (list.size() > 0) {
			adapter.bindData(list);
			subscription_news_scroll.setAdapter(adapter);
			subscription_news_scroll.setSelection(list.size() - 1);
		} else {
			MyLog.i(">>>>>>>>>histroyagga" + url + ">>>>>" + userID);
			http.getString(url, 0, new DownGet() {
				
				@Override
				public void downget(String arg0) {
					// TODO Auto-generated method stub
					if(!arg0.equals(""))
					{
						ChatMsg msg = JsonUtils.jsonNewsLeast(
								DiscoverPubServiceNewsActivity.this, arg0, userID);
						list.addAll(0, msg.getList());
						Message msMessage = Message.obtain();
						msMessage.arg1 = 4;
						DiscoverPubServiceNewsActivity.this.handler
						.sendMessage(msMessage);
					}
				}
			});
			// subscription_news_scroll.setVisibility(View.GONE);
			// subscription_news_nodata.setVisibility(View.VISIBLE);
		}
		subscription_news_scroll
				.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> arg0, View arg1,
							int arg2, long arg3) {
						// TODO Auto-generated method stub
						MyLog.i(">>>>>arg2ggg" + arg2 + ">>>>>arg3" + arg3);
						ChatMsgEntity chat1 = (ChatMsgEntity) adapter
								.getItem((int) arg3);
						String title = chat1.getTitle();
						String xiaoxi_id = chat1.getXiaoxi_id();
						Intent intent = new Intent(
								DiscoverPubServiceNewsActivity.this,
								DiscoverPubServiceWebViewActivity.class);
						intent.putExtra("title", title);
						intent.putExtra("xiaoxi_id", xiaoxi_id);
						startActivity(intent);
					}
				});
		subscription_news_scroll
				.setOnRefreshListenerHead(new OnRefreshListenerHeader() {

					@Override
					public void onRefresh() {
						// TODO Auto-generated method stub
						++page;
						DBManager manager = DBManager
								.getInstances(DiscoverPubServiceNewsActivity.this);
						Cursor cursor2 = manager.queryUICHistory(
								DiscoverPubServiceNewsActivity.this, userID,
								publicID, "5", page);
						MyLog.i(">>>>>>cursor2gghgj" + cursor2.getCount());
						List<ChatMsgEntity> listnew = new ArrayList<ChatMsgEntity>();
						for (cursor2.moveToFirst(); !cursor2.isAfterLast(); cursor2
								.moveToNext()) {
							ChatMsgEntity chat1 = new ChatMsgEntity();
							chat1.setTitle(cursor2.getString(cursor2
									.getColumnIndex("title")));
							chat1.setCreate_time(cursor2.getString(cursor2
									.getColumnIndex("publish_time")));
							chat1.setSummary(cursor2.getString(cursor2
									.getColumnIndex("summary")));
							chat1.setFace_pic_url(cursor2.getString(cursor2
									.getColumnIndex("face_pic_url")));
							chat1.setXiaoxi_id(cursor2.getString(cursor2
									.getColumnIndex("xiaoxi_id")));
							chat1.setMessage(cursor2.getString(cursor2
									.getColumnIndex("message")));
							chat1.setContent(cursor2.getString(cursor2
									.getColumnIndex("content")));
							listnew.add(chat1);
						}
						Collections.reverse(listnew);
						Message msg = Message.obtain();
						msg.arg1 = 2;
						msg.obj = listnew;
						handler.sendMessage(msg);
					}
				});
	}

	public void init() {
		subscription_news_bar = (ProgressBar) findViewById(R.id.subscription_news_bar);
		subscription_news_bar.setVisibility(View.GONE);
		rl_bottom = (RelativeLayout) findViewById(R.id.rl_bottom);
		if (userID.equals("0")) {
			rl_bottom.setVisibility(View.GONE);
		}
		subscription_news_scroll = (DropdownListView) findViewById(R.id.subscription_news_scroll);
		subscription_news_tv1 = (TextView) findViewById(R.id.subscription_news_tv1);
		subscription_news_btn_send = (Button) findViewById(R.id.subscription_news_btn_send);
		mEditTextContent = (EditText) findViewById(R.id.mEditTextContent);
		subscription_news_iv = (ImageView) findViewById(R.id.subscription_news_iv);
		// 让公众号设置按钮显示出来
		subscription_news_iv.setVisibility(View.VISIBLE);
		subscription_news_iv2 = (ImageView) findViewById(R.id.subscription_news_iv2);
		subscription_news_iv2.setOnClickListener(this);
		subscription_news_nodata = (TextView) findViewById(R.id.subscription_news_nodata);
		subscription_news_iv.setOnClickListener(this);
		subscription_news_btn_send.setOnClickListener(this);
		adapter = new MyAdapter();

	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub
		return super.onTouchEvent(event);
	}

	class MyAdapter extends BaseAdapter {

		private List<ChatMsgEntity> list = new ArrayList<ChatMsgEntity>();
		private LayoutInflater mInflater;
		boolean flagright = false;
		boolean flag = false;
		final int TYPE_1 = 0;
		final int TYPE_2 = 1;
		final int TYPE_3 = 2;

		int m = -1;

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return list.size();
		}

		public void bindData(List<ChatMsgEntity> list) {
			mInflater = LayoutInflater
					.from(DiscoverPubServiceNewsActivity.this);
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
		public int getItemViewType(int position) {
			// TODO Auto-generated method stub
			ChatMsgEntity entity = list.get(position);
			String msg = entity.getMessage();
			String content = entity.getContent();
			if (null != msg) {
				return TYPE_1;
			}
			if (null != content) {
				return TYPE_3;
			}

			return TYPE_2;

		}

		@Override
		public int getViewTypeCount() {
			// TODO Auto-generated method stub
			return 3;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			long time1, time2;
			ChatMsgEntity entity = list.get(position);
			String msg = entity.getMessage();
			String content = entity.getContent();
			ViewHolder viewHolder = null;
			ViewHolder1 viewHolder1 = null;
			ViewHolder2 viewHolder2 = null;
			String currentTime = entity.getCreate_time();
			MyLog.i(">>>>>>>currenttime" + currentTime);
			String previewTime = (position - 1) >= 0 ? list.get(position - 1)
					.getCreate_time() : "0";
			MyLog.i(">>>>>>>currenttime" + currentTime + ">>>>previewtiem"
					+ previewTime);
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
			SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String currentTimeSeconds, previewTimeSeconds;
			try {
				if (currentTime.length() < 17) {
					currentTimeSeconds = sdf.parse(currentTime).getTime()
							/ 1000 + "";
				} else {
					currentTimeSeconds = sdf1.parse(currentTime).getTime()
							/ 1000 + "";
				}
				if (previewTime.equals("0")) {
					previewTimeSeconds = "0";
				} else {
					if (previewTime.length() < 17) {
						previewTimeSeconds = sdf.parse(previewTime).getTime()
								/ 1000 + "";
					} else {
						previewTimeSeconds = sdf1.parse(previewTime).getTime()
								/ 1000 + "";
					}
				}

				time1 = Long.valueOf(currentTimeSeconds);
				time2 = Long.valueOf(previewTimeSeconds);

				int type = getItemViewType(position);
				if (convertView == null) {
					switch (type) {
					case TYPE_1:
						viewHolder1 = new ViewHolder1();
						convertView = mInflater.inflate(
								R.layout.chatting_item_msg_text_right_item,
								null);
						viewHolder1.iv_userhead = (ImageView) convertView
								.findViewById(R.id.iv_userhead);
						viewHolder1.tv_chatcontent = (TextView) convertView
								.findViewById(R.id.tv_chatcontent);
						viewHolder1.tv_sendtime = (TextView) convertView
								.findViewById(R.id.tv_sendtime);
						viewHolder1.right_item_bar = (ProgressBar) convertView
								.findViewById(R.id.right_item_bar);
						convertView.setTag(viewHolder1);
						break;
					case TYPE_2:
						convertView = mInflater.inflate(
								R.layout.discover_pubservice_news_right_item,
								null);
						viewHolder = new ViewHolder();
						viewHolder.chat_tv1 = (TextView) convertView
								.findViewById(R.id.chat_tv2);
						viewHolder.chat_tv2 = (TextView) convertView
								.findViewById(R.id.chat_tv1);
						viewHolder.chat_tv3 = (TextView) convertView
								.findViewById(R.id.chat_tv3);
						viewHolder.chat_iv1 = (ImageView) convertView
								.findViewById(R.id.chat_iv1);
						convertView.setTag(viewHolder);
						break;
					case TYPE_3:
						convertView = mInflater
								.inflate(
										R.layout.chatting_item_msg_text_left_item,
										null);
						viewHolder2 = new ViewHolder2();
						viewHolder2.tv_leftsendtime = (TextView) convertView
								.findViewById(R.id.tv_leftsendtime);
						viewHolder2.tv_leftchatcontent = (TextView) convertView
								.findViewById(R.id.tv_leftchatcontent);
						viewHolder2.iv_leftuserhead = (ImageView) convertView
								.findViewById(R.id.iv_leftuserhead);
						convertView.setTag(viewHolder2);
						break;
					default:
						break;
					}

				} else {
					switch (type) {
					case TYPE_1:
						viewHolder1 = (ViewHolder1) convertView.getTag();
						break;
					case TYPE_2:
						viewHolder = (ViewHolder) convertView.getTag();
						break;
					case TYPE_3:
						viewHolder2 = (ViewHolder2) convertView.getTag();
						break;

					default:
						break;
					}
				}
				switch (type) {
				case TYPE_1:
					if (!entity.isFlag()) {
						viewHolder1.right_item_bar.setVisibility(View.GONE);
					} else {
						viewHolder1.right_item_bar.setVisibility(View.VISIBLE);
					}
					viewHolder1.tv_chatcontent.setText(msg);
					if ((time1 - time2) >= 5 * 60) {
						viewHolder1.tv_sendtime.setVisibility(View.VISIBLE);
						viewHolder1.tv_sendtime.setText(DateUtil
								.wechat_time(currentTimeSeconds));
					} else {
						viewHolder1.tv_sendtime.setVisibility(View.GONE);
					}
					loader.displayImage(WeiBaoApplication.getUserPic(),
							viewHolder1.iv_userhead, defaultOptionss,
							animateFirstListener);
					break;
				case TYPE_2:
					String path = entity.getFace_pic_url();
					if (null != path && path.equals("")) {
						viewHolder.chat_iv1.setVisibility(View.GONE);
					} else {
						viewHolder.chat_iv1.setVisibility(View.VISIBLE);
						loader.displayImage(entity.getFace_pic_url(),
								viewHolder.chat_iv1, defaultOptions,
								animateFirstListener);
					}
					// 显示消息更新的时间
					MyLog.i(">>>>>>>>time" + currentTimeSeconds);
					viewHolder.chat_tv1.setText(DateUtil
							.wechat_time(currentTimeSeconds));
					viewHolder.chat_tv2.setText(entity.getTitle());
					viewHolder.chat_tv3.setText(entity.getSummary());
					break;
				case TYPE_3:
					if ((time1 - time2) >= 5 * 60) {
						viewHolder2.tv_leftsendtime.setVisibility(View.VISIBLE);
						viewHolder2.tv_leftsendtime.setText(DateUtil
								.wechat_time(currentTimeSeconds));
					} else {
						viewHolder2.tv_leftsendtime.setVisibility(View.GONE);
					}
					viewHolder2.tv_leftchatcontent.setText(content);
					loader.displayImage(public_photo,
							viewHolder2.iv_leftuserhead, defaultOptions,
							animateFirstListener);
					break;

				default:
					break;
				}
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			return convertView;

		}
	}

	static final class ViewHolder {
		public TextView chat_tv1;
		public TextView chat_tv2;
		public TextView chat_tv3;
		public ImageView chat_iv1;
	}

	static final class ViewHolder1 {
		// 右边的聊天背景
		public TextView tv_sendtime;
		public TextView tv_chatcontent;
		public ImageView iv_userhead;
		public ProgressBar right_item_bar;

	}

	static final class ViewHolder2 {
		// 左边的聊天背景
		public TextView tv_leftsendtime;
		public TextView tv_leftchatcontent;
		public ImageView iv_leftuserhead;

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.subscription_news_btn_send:
			chat();
			break;
		// 返回键
		case R.id.subscription_news_iv:
			finish();
			break;
		case R.id.subscription_news_iv2:
			// 用户信息界面
			yonghu();
			break;
		default:
			break;
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

	private void yonghu() {
		// TODO Auto-generated method stub
		if (guanzhu) {
			Intent intent = new Intent(DiscoverPubServiceNewsActivity.this,
					DiscoverPubServiceAttentionCancelActivity.class);
			intent.putExtra("publicID", publicID);
			intent.putExtra("name", name);
			intent.putExtra("public_photo", public_photo);
			intent.putExtra("fuction", fuction);
			intent.putExtra("flag_bz", "DiscoverPubServiceNewsActivity");
			startActivity(intent);
		} else {
			Intent intent = new Intent(DiscoverPubServiceNewsActivity.this,
					DiscoverPubServiceAddAttentionActivity.class);
			intent.putExtra("publicID", publicID);
			intent.putExtra("name", name);
			intent.putExtra("public_photo", public_photo);
			intent.putExtra("fuction", fuction);
			intent.putExtra("flag_bz", "DiscoverPubServiceNewsActivity");
			startActivity(intent);
		}

	}

	private String getDate() {
		Calendar c = Calendar.getInstance();

		String year = String.valueOf(c.get(Calendar.YEAR));
		String month = String.valueOf(c.get(Calendar.MONTH) + 1);
		String day = String.valueOf(c.get(Calendar.DAY_OF_MONTH));
		String hour = String.valueOf(c.get(Calendar.HOUR_OF_DAY));
		String mins = String.valueOf(c.get(Calendar.MINUTE));

		StringBuffer sbBuffer = new StringBuffer();
		sbBuffer.append(year + "-" + month + "-" + day + " " + hour + ":"
				+ mins);

		return sbBuffer.toString();
	}

	private void chat() {
		if (NetUtil.getNetworkState(DiscoverPubServiceNewsActivity.this) == NetUtil.NETWORN_NONE) {
			ToastUtil.showLong(DiscoverPubServiceNewsActivity.this, "请检查网络设置");
			return;
		}
		final String message = mEditTextContent.getText().toString().trim();
		final String currenttime = getDate();
		if (message.length() <= 0) {
			ToastUtil.showLong(DiscoverPubServiceNewsActivity.this, "请输入聊天内容");
			return;
		}
		final ChatMsgEntity entity = new ChatMsgEntity();

		entity.setMessage(message);
		entity.setCreate_time(currenttime);
		entity.setFlag(true);
		Message msg = Message.obtain();
		msg.arg1 = 3;
		msg.what = 1;
		msg.obj = entity;
		handler.sendMessage(msg);
		final String news_chat = UrlComponent.getNewsChat_Post();
		final JSONObject jsonObject = new JSONObject();
		try {
			jsonObject.put("userID", userID);
			jsonObject.put("account_id_num", publicID);
			jsonObject.put("message", message);
			// new AsyncTask<String, Void, Boolean>() {
			//
			// @Override
			// protected Boolean doInBackground(String... params) {
			// // TODO Auto-generated method stub
			// JSONObject jsonObject = new JSONObject();
			// try {
			// jsonObject.put("userID", userID);
			// jsonObject.put("account_id_num", publicID);
			// jsonObject.put("message", message);
			// String json = ApiClient.PostToServerForResult(
			// news_chat, jsonObject.toString());
			// } catch (Exception e) {
			// // TODO: handle exception
			// }
			//
			// return null;
			// }
			// protected void onPostExecute(Boolean result) {};
			// }.execute(news_chat);
			new Thread(new Runnable() {
				long preTime, nowTime, now1Time;

				@Override
				public void run() {
					// TODO Auto-generated method stub
					preTime = System.currentTimeMillis();
					if (NetUtil
							.getNetworkState(DiscoverPubServiceNewsActivity.this) == NetUtil.NETWORN_NONE) {
						Looper.prepare();
						ToastUtil.showLong(DiscoverPubServiceNewsActivity.this,
								"请检查网络设置");
						Looper.loop();

					} else {
						try {
							MyLog.i(">>>>>>>>>>news_chat" + news_chat);
							String json = ApiClient.PostToServerForResult(
									news_chat, jsonObject.toString());
							nowTime = System.currentTimeMillis();
							long jiange = nowTime - preTime;
							MyLog.i(">>>>>>>tiem访问网络" + jiange + ""
									+ ">>>>>json" + json);
							if (null != json && !"".equals(json)) {
								JSONObject jsonObject = new JSONObject(json);
								boolean flag = jsonObject.getBoolean("flag");
								if (flag) {
									Message msg = Message.obtain();
									msg.arg1 = 3;
									msg.what = 2;
									entity.setFlag(false);
									handler.sendMessage(msg);
									String xiaoxi_id = jsonObject
											.getString("xiaoxi_id");
									// String publish_time = jsonObject
									// .getString("publish_time");
									entity.setXiaoxi_id(xiaoxi_id);

									DBManager dbManager = DBManager
											.getInstances(DiscoverPubServiceNewsActivity.this);
									ContentValues values = new ContentValues();
									values.put("userID", userID);
									values.put("account_id_num", publicID);
									values.put("xiaoxi_id", xiaoxi_id);
									values.put("account_id", name);
									values.put("isread", "1");
									values.put("message", message);
									values.put("publish_time", currenttime);
									dbManager
											.insertSQLite(
													DiscoverPubServiceNewsActivity.this,
													"uic", null, values);
									now1Time = System.currentTimeMillis();
									long jiange1 = now1Time - preTime;
									MyLog.i(">>>>>>>tiem保存数据库" + jiange1 + "");
								}

							}
							MyLog.i(">>>>>chatjson" + json + ">>>>>>jsonobject"
									+ jsonObject.toString());
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
			}).start();
			;

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			MyLog.i("chat>>>>>" + e.toString());
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

	public void publicmethod(View v) {
		yonghu();
	}

}
