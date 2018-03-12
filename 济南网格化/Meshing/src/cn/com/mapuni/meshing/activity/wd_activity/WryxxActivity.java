package cn.com.mapuni.meshing.activity.wd_activity;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONObject;

import android.os.Bundle;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.meshing.R;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.mapuni.android.base.BaseActivity;
import com.mapuni.android.base.controls.loading.YutuLoading;
import com.mapuni.android.base.interfaces.PathManager;
import com.mapuni.android.dataprovider.JsonHelper;

public class WryxxActivity extends BaseActivity{
	ListView lv_ls_Ent; 
	MyTaskAdapter myTaskAdapter;
	
	//分页加载的数据
	public ArrayList<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();
	public ArrayList<HashMap<String, Object>> SearchList = new ArrayList<HashMap<String, Object>>();
	ArrayList<HashMap<String, Object>> Data;
	private Handler handler;
	public int currage = 1;
	private boolean isMaxPage = false;
	private int PAGENUM = 10;
	private YutuLoading yutuLoading;
	String url;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ui_mapuni1);
		SetBaseStyle((RelativeLayout) findViewById(R.id.parentLayout),"污染源信息");
		middleLayout = ((LinearLayout) findViewById(R.id.middleLayout1));
		LayoutInflater inflater = LayoutInflater.from(this);
		View mainView = inflater.inflate(R.layout.wdwryxxlbactivity_layout, null);
		middleLayout.addView(mainView);
		lv_ls_Ent = (ListView) mainView.findViewById(R.id.lv_ls_Ent);
		
		 handler = new Handler(new Callback() {
			@Override
			public boolean handleMessage(Message msg) {
				yutuLoading.dismissDialog();
				mProgressLoadLayout.setVisibility(View.GONE);
				mLoadLayout.setVisibility(View.GONE);
				switch (msg.what) {
				case 0:
					list.clear();
					myTaskAdapter.notifyDataSetChanged();
					Toast.makeText(WryxxActivity.this, "暂无数据",
							Toast.LENGTH_SHORT).show();
					break;
				case 1:
					list.clear();
					list.addAll(SearchList);
					myTaskAdapter.notifyDataSetChanged();
					break;
				case 3:
					 Toast.makeText(WryxxActivity.this, "服务器异常",Toast.LENGTH_SHORT).show();
					break;
				default:
					break;
				}
				return false;
			}
		});
		initData(1, "");
		initView();
		
	}
	
	//获得分页数据
	public void initData(int page, String search_title) {
		yutuLoading = new YutuLoading(WryxxActivity.this);
		yutuLoading.setCancelable(true);
		yutuLoading.setLoadMsg("正在加载数据，请稍候", "");
		yutuLoading.showDialog();
		isMaxPage = false;
		loadPagedata(page, search_title);
	}
	private void loadPagedata(final int page, final String search_title) {
		currage = page;
		int page_size = PAGENUM;
		Data = new ArrayList<HashMap<String, Object>>();
		getEnterpriseList(page,page_size,search_title); 
//		new Thread() {
//			public void run() {
//				
//			}
//		}.start();
	}
	
	private void initView() {
		myTaskAdapter = new MyTaskAdapter(WryxxActivity.this,list);
		addListLoadBar(lv_ls_Ent);
		lv_ls_Ent.setAdapter(myTaskAdapter);
		lv_ls_Ent.setOnScrollListener(new MyOnScrollListener());
		lv_ls_Ent.setOnItemClickListener(new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(WryxxActivity.this,WryxxSlideActivity.class);
				intent.putExtra("emergency", list.get(arg2));
				startActivity(intent);
			}
			
		});
	}
	
	/**
	 * FileName: TaskManagerFlowActivity.java Description:各个界面的数据适配器
	 * 
	 * @author Administrator
	 * @Version 1.3.4
	 * @Copyright 中科宇图天下科技有限公司<br>
	 * @Create at: 2012-12-10 下午03:34:21
	 */
	public class MyTaskAdapter extends BaseAdapter {
		private final Context context;
		private LayoutInflater mInflater = null;
		private ArrayList<HashMap<String, Object>> data;
		private final int layoutid = R.layout.wd_wryxx_listitem;
		private final int textSize;

		/**
		 * Description:
		 * 
		 * @param _Context
		 * @param
		 * @param 任务状态
		 * @return
		 * @author Administrator
		 * @Create at: 2013-4-9 下午3:27:39
		 */
		public MyTaskAdapter(Context context,
				ArrayList<HashMap<String, Object>> data) {
			this.mInflater = LayoutInflater.from(context);
			this.context = context;
			this.data = data == null ? new ArrayList<HashMap<String, Object>>()
					: data;

			textSize = 21;/*Integer.parseInt(DisplayUitl.getSettingValue(context,
					DisplayUitl.TEXTSIZE, 22).toString());*/
		}

		public void AddValue(ArrayList<HashMap<String, Object>> data) {
			if (this.data == null) {
				this.data = data;
			} else {
				this.data.addAll(data);
			}
		}

		public void shuaxin() {

			this.notifyDataSetChanged();
		}

		@Override
		public int getCount() {
			if (data == null || data.size() == 0) {
				return 0;
			}
			return data.size();
		}

		@Override
		public Object getItem(int position) {
			return data.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(final int position, View convertView,
				ViewGroup parent) {
//			if (data == null || data.size() == 0) {
//				YutuLoading loading = new YutuLoading(context);
//				// if (!Net.checkNet(TaskFlowSildeActivity.this)) {
//				// Toast.makeText(context, "由于网络问题,暂时无法获取任务信息",
//				// Toast.LENGTH_SHORT).show();
//				// }
//				loading.setLoadMsg("", "数据为空!");
//				loading.setFailed();
//				return null;
//			}
			ViewHolder holder = null;

			if (convertView == null) {
				convertView = mInflater.inflate(layoutid, null);
				holder = new ViewHolder();
				holder.lefticon = (ImageView) convertView
						.findViewById(R.id.lefticon);
				holder.id = new TextView(context);
				holder.title = (TextView) convertView.findViewById(R.id.title);
				holder.date = (TextView) convertView.findViewById(R.id.date);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			// 3.Get Data
			int leftImgId = R.drawable.wd_wryxx_qyicon;
			if(data!=null && data.size()>0){
				holder.id.setText(data.get(position).get("id")+"");
				holder.title.setText(data.get(position).get("entName")+"");
				holder.lefticon.setImageResource(leftImgId);
			}
			
			return convertView;
		}
	}

	protected class ViewHolder {
		/** 绑定数据Id */
		public TextView id = null;
		/** 绑定列表的第一行数据 */
		public TextView title = null;
		/** 绑定列表第二行后边的数据 */
		TextView date = null;
		/** 列表左边的图片 */
		ImageView lefticon = null;

		/** 列表项在列表中的位置 */
		public int position = 0;
	}
	
	/** listview底部加载数据的进度框 */
	protected LinearLayout mProgressLoadLayout;
	/** 定义一个布局参数 */
	protected final android.widget.LinearLayout.LayoutParams mLayoutParams = new LinearLayout.LayoutParams(
			LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
	/** 加载数据等待框的父布局 */
	protected LinearLayout mLoadLayout;

	private void addListLoadBar(ListView listview) {
		/** 获取手机宽度 */
		
		DisplayMetrics dm = new DisplayMetrics();
		WryxxActivity.this.getWindowManager().getDefaultDisplay().getMetrics(dm);

		/** ProgressBar的布局 */
		mLoadLayout = new LinearLayout(WryxxActivity.this);
		mLoadLayout.setMinimumHeight(30);
		mLoadLayout.setMinimumWidth(dm.heightPixels);
		mLoadLayout.setGravity(Gravity.CENTER);
		mLoadLayout.setOrientation(LinearLayout.VERTICAL);
		mLoadLayout.setBackgroundResource(R.drawable.loading);
		mLoadLayout.setVisibility(View.GONE);
		mProgressLoadLayout = new LinearLayout(WryxxActivity.this);
		mProgressLoadLayout.setMinimumHeight(30);
		mProgressLoadLayout.setGravity(Gravity.CENTER);

		ProgressBar mProgressBar = new ProgressBar(WryxxActivity.this);
		mProgressBar.setPadding(0, 0, 15, 0);
		mProgressBar.setScrollBarStyle(WryxxActivity.this.MODE_WORLD_READABLE);
		/** 为布局添加进度条 */
		mProgressLoadLayout.addView(mProgressBar, mLayoutParams);
		TextView mTipContent = new TextView(WryxxActivity.this);
		mTipContent.setText("正在加载数据,请稍后...");
		mTipContent.getPaint().setFakeBoldText(true);
		mTipContent.setGravity(Gravity.CENTER_HORIZONTAL);
		/** 为布局添加文本 */
		mProgressLoadLayout.addView(mTipContent, mLayoutParams);
		/** 默认设为不可见，注意View.GONE和View.INVISIBLE的区别 */
		mProgressLoadLayout.setVisibility(View.GONE);
		mProgressLoadLayout.setGravity(Gravity.CENTER);
		/** 添加mProgressLoadLayout，作为一个View */
		mLoadLayout.addView(mProgressLoadLayout, new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		listview.addFooterView(mLoadLayout, null, false);
	}
	//分页加载
	class MyOnScrollListener implements OnScrollListener {
		private int visibleLastIndex = 0; // 最后的可视项索引
		private int visibleItemCount = 0; // 当前窗口可见项总数

		@Override
		public void onScrollStateChanged(AbsListView view, int scrollState) {
			// TODO Auto-generated method stub
			int itemsLastIndex = myTaskAdapter.getCount() - 1; // 数据集最后一项的索引
			int lastIndex = itemsLastIndex + 1;// 加上底部的loadMoreView项
			int a = OnScrollListener.SCROLL_STATE_IDLE;
			int b = OnScrollListener.SCROLL_STATE_FLING;
			int c = OnScrollListener.SCROLL_STATE_TOUCH_SCROLL;
			if (scrollState == OnScrollListener.SCROLL_STATE_IDLE && visibleLastIndex == lastIndex && !isMaxPage) {
				// 如果是自动加载,可以在这里放置异步加载数据的代码
				mProgressLoadLayout.setVisibility(View.VISIBLE);
				mLoadLayout.setVisibility(View.VISIBLE);
				loadPagedata(++currage, "");

			}
		}

		@Override
		public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
			this.visibleItemCount = visibleItemCount;
			visibleLastIndex = firstVisibleItem + visibleItemCount - 1;
		}

	}  
	
	/**
	 * 请求数据
	 */
	private void getEnterpriseList(final int page, final int pageSize, String entname) {
//		http://192.168.15.64:8080/JiNanhuanbaoms/task/selWuranyuan.do?page=1&pageSize=2
		StringBuilder builder = new StringBuilder("?page=");
		builder.append(page)
		.append("&pageSize=").append(pageSize);
		
		HttpUtils utils = new HttpUtils();
		utils.configCurrentHttpCacheExpiry(1000 * 5);
	    // 设置超时时间  
		utils.configTimeout(5 * 1000);// 连接超时  //指的是连接一个url的连接等待时间。  
		utils.configSoTimeout(5 * 1000);// 获取数据超时  //指的是连接上一个url，获取response的返回等待时间 
		String base=PathManager.WURANYUANXX_URL;
		//String base = "http://171.8.66.103:8473/JiNanhuanbaoms/task/selWuranyuan.do"/*linkIp+"/huanbaoms/OneEnterParse/queryEntInfo"*/;
		url = base + builder.toString();
		
		utils.send(HttpMethod.GET, url,new RequestCallBack<String>() {
			@Override
			public void onFailure(HttpException arg0, String arg1) {
				Log.i("utils", String.valueOf(arg1));
				handler.sendEmptyMessage(3);
			}
			
			@Override
			public void onSuccess(ResponseInfo<String> arg0) {
				Log.i("utils", String.valueOf(arg0.result));
				String call = String.valueOf(arg0.result);
				//获取Data
				if(call!=null && !call.equals("")){
					JSONObject jobject;
					try {
						jobject = new JSONObject(call);
						String rows = jobject.getString("rows")+"";
				        Data = JsonHelper.paseJSON(rows)/*JsonHelper.paseJSONToMap(call)*/;
					}catch(Exception e){
						
					}
				}
				Data = new ArrayList<HashMap<String,Object>>();
				if (Data.size() > 0) {
					if (currage == 1) {
						SearchList.clear();
						SearchList.addAll(Data);
					} else {
						SearchList.addAll(Data);
					}
					if (Data.size() == 0 || Data.size() < pageSize) {// /最后一页
						isMaxPage = true;
//						Toast.makeText(WryxxActivity.this, " isMaxPage = true", 200).show();
						if (handler != null)
							handler.sendEmptyMessage(2);
					}
					if (handler != null)
						handler.sendEmptyMessage(1);
				} else {
					try {
						if (currage == 1) {
							handler.sendEmptyMessage(0);
						} else {
							handler.sendEmptyMessage(2);
						}
						isMaxPage = false;
					} catch (Exception e) {
						// TODO: handle exception
					}
				}
			}

		});
	}
}
