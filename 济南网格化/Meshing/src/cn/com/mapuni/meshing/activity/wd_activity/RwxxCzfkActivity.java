package cn.com.mapuni.meshing.activity.wd_activity;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Handler.Callback;
import android.text.TextUtils.TruncateAt;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import cn.com.mapuni.meshing.activity.wd_activity.WryxxActivity.MyOnScrollListener;
import cn.com.mapuni.meshing.activity.wd_activity.WryxxActivity.MyTaskAdapter;

import com.example.meshing.R;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.mapuni.android.base.BaseActivity;
import com.mapuni.android.base.adapter.ListActivityAdapter.ViewHolder;
import com.mapuni.android.base.controls.loading.YutuLoading;
import com.mapuni.android.base.interfaces.PathManager;
import com.mapuni.android.base.util.DisplayUitl;
import com.mapuni.android.dataprovider.JsonHelper;

public class RwxxCzfkActivity extends BaseActivity {

	ImageView wd_tx;
	LinearLayout middle;
	String type = "czfk";
	private SlideView slideView;
	/** Ĭ����ʾ�ڼ���ҳ�� */
	private int positon;
    ListView listView;
    MyTaskAdapter adapter;
  //��ҳ���ص�����
  	public ArrayList<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();
  	public ArrayList<HashMap<String, Object>> SearchList = new ArrayList<HashMap<String, Object>>();
  	ArrayList<HashMap<String, Object>> Data;
  	private Handler handler;
  	public int currage = 1;
  	private boolean isMaxPage = false;
  	private int PAGENUM = 11;
  	private YutuLoading yutuLoading;
  	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ui_mapuni);
		type = (String) getIntent().getSerializableExtra("type");
	    SetBaseStyle((RelativeLayout) findViewById(R.id.parentLayout), "���÷���");
		getData();
	}
	private ArrayList<HashMap<String, Object>> arrs = new ArrayList<HashMap<String, Object>>();
	private void getData() {
		middleLayout = ((LinearLayout) findViewById(R.id.middleLayout));
		LayoutInflater inflater = LayoutInflater.from(this);
		View mainView = inflater.inflate(R.layout.wdxcjlactivity_layout, null);
		middleLayout.addView(mainView);
		listView = (ListView) findViewById(R.id.listView);

		 handler = new Handler(new Callback() {
				@Override
				public boolean handleMessage(Message msg) {
					yutuLoading.dismissDialog();
					mProgressLoadLayout.setVisibility(View.GONE);
					mLoadLayout.setVisibility(View.GONE);
					switch (msg.what) {
					case 0:
						list.clear();
						adapter.notifyDataSetChanged();
						Toast.makeText(RwxxCzfkActivity.this, "��������",
								Toast.LENGTH_SHORT).show();
						break;
					case 1:
						list.clear();
						list.addAll(SearchList);
						adapter.notifyDataSetChanged();
						break;
					case 3:
						 Toast.makeText(RwxxCzfkActivity.this, "�������쳣",Toast.LENGTH_SHORT).show();
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
	//��÷�ҳ����
	public void initData(int page, String search_title) {
		yutuLoading = new YutuLoading(RwxxCzfkActivity.this);
		yutuLoading.setCancelable(true);
		yutuLoading.setLoadMsg("���ڼ������ݣ����Ժ�", "");
		yutuLoading.showDialog();
		isMaxPage = false;
		loadPagedata(page, search_title);
	}
	private void loadPagedata(final int page, final String search_title) {
		currage = page;
		int page_size = PAGENUM;
		Data = new ArrayList<HashMap<String, Object>>();
		getEnterpriseList(page,page_size,search_title); 
	}
	
	private void initView() {
		adapter = new MyTaskAdapter(RwxxCzfkActivity.this, list,type);
		addListLoadBar(listView);
		listView.setAdapter(adapter);
		listView.setOnScrollListener(new MyOnScrollListener());
		listView.setOnItemClickListener(new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(RwxxCzfkActivity.this,
						RwxxLiebiaoXQActivity.class);
				intent.putExtra("statu", type);
				intent.putExtra("arr", list.get(arg2));
				startActivity(intent);
			}
			
		});
		
	}

	/**
	 * FileName: TaskManagerFlowActivity.java Description:�������������������
	 * 
	 * @author Administrator
	 * @Version 1.3.4
	 * @Copyright �п���ͼ���¿Ƽ����޹�˾<br>
	 * @Create at: 2012-12-10 ����03:34:21
	 */
	public class MyTaskAdapter extends BaseAdapter {
		private final Context context;
		private LayoutInflater mInflater = null;
		private ArrayList<HashMap<String, Object>> data;
		private final int layoutid = R.layout.wd_xcjl_listitem;
		private final String rwzt;
		private final int textSize;
		private int textColor = Color.BLACK;
		private int textColorRed = Color.RED;
		private int textColorGreen = Color.GREEN;
		private int textColorYellow = Color.rgb(255, 140, 00);

		/**
		 * Description:
		 * 
		 * @param _Context
		 * @param
		 * @param ����״̬
		 * @return
		 * @author Administrator
		 * @Create at: 2013-4-9 ����3:27:39
		 */
		public MyTaskAdapter(Context context,
				ArrayList<HashMap<String, Object>> data, String rwzt) {
			this.mInflater = LayoutInflater.from(context);
			this.context = context;
			this.data = data == null ? new ArrayList<HashMap<String, Object>>()
					: data;
			this.rwzt = rwzt;

			textSize = 21;/*
						 * Integer.parseInt(DisplayUitl.getSettingValue(context,
						 * DisplayUitl.TEXTSIZE, 22).toString());
						 */
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
//				// Toast.makeText(context, "������������,��ʱ�޷���ȡ������Ϣ",
//				// Toast.LENGTH_SHORT).show();
//				// }
//				loading.setLoadMsg("", "���޴�״̬����");
//				loading.setFailed();
//				return loading;
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
//				holder.task_state = (TextView) convertView
//						.findViewById(R.id.task_state);
				holder.yiwancheng_beizhu = (TextView) convertView
						.findViewById(R.id.yiwancheng_beizhu);
				holder.chuli = (Button) convertView.findViewById(R.id.chuli);
				holder.cuiban = (Button) convertView.findViewById(R.id.cuiban);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			
			final String idStr = (String) data.get(position).get("entname");
			
			holder.lefticon.setVisibility(View.GONE);
			holder.cuiban.setVisibility(View.GONE);
			holder.chuli.setVisibility(View.GONE);
			
			holder.yiwancheng_beizhu.setText(data.get(position).get("wtjl")+"");
			holder.yiwancheng_beizhu.setVisibility(View.VISIBLE);
			holder.id.setText(idStr);
			holder.title.setText(data.get(position).get("entname")+"");
			String time = data.get(position).get("xcwtsj")+"";
			if(time.length()>=10)
			   time = time.substring(0, 10);
			holder.date.setText(time);
			return convertView;
		}
	}

	protected class ViewHolder {
//		public TextView task_state;
		/** ������Id */
		public TextView id = null;
		/** ���б�ĵ�һ������ */
		public TextView title = null;
		/** ���б�ڶ������� */
		// public TextView content = null;
		/** ���б�ڶ��к�ߵ����� */
		// TextView dateMsg = null;
		/** ���б�ڶ��к�ߵ����� */
		TextView date = null;
		TextView yiwancheng_beizhu = null;

		/** �б���ߵ�ͼƬ */
		ImageView lefticon = null;
		/** �б��ұߵ�ͼ�� */
		// ImageView righticon = null;
		Button chuli = null;
		Button cuiban = null;
		/** �б������б��е�λ�� */
		public int position = 0;
	}
	/** listview�ײ��������ݵĽ��ȿ� */
	protected LinearLayout mProgressLoadLayout;
	/** ����һ�����ֲ��� */
	protected final android.widget.LinearLayout.LayoutParams mLayoutParams = new LinearLayout.LayoutParams(
			LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
	/** �������ݵȴ���ĸ����� */
	protected LinearLayout mLoadLayout;

	private void addListLoadBar(ListView listview) {
		/** ��ȡ�ֻ���� */
		
		DisplayMetrics dm = new DisplayMetrics();
		RwxxCzfkActivity.this.getWindowManager().getDefaultDisplay().getMetrics(dm);

		/** ProgressBar�Ĳ��� */
		mLoadLayout = new LinearLayout(RwxxCzfkActivity.this);
		mLoadLayout.setMinimumHeight(30);
		mLoadLayout.setMinimumWidth(dm.heightPixels);
		mLoadLayout.setGravity(Gravity.CENTER);
		mLoadLayout.setOrientation(LinearLayout.VERTICAL);
		mLoadLayout.setBackgroundResource(R.drawable.loading);
		mLoadLayout.setVisibility(View.GONE);
		mProgressLoadLayout = new LinearLayout(RwxxCzfkActivity.this);
		mProgressLoadLayout.setMinimumHeight(30);
		mProgressLoadLayout.setGravity(Gravity.CENTER);

		ProgressBar mProgressBar = new ProgressBar(RwxxCzfkActivity.this);
		mProgressBar.setPadding(0, 0, 15, 0);
		mProgressBar.setScrollBarStyle(RwxxCzfkActivity.this.MODE_WORLD_READABLE);
		/** Ϊ������ӽ����� */
		mProgressLoadLayout.addView(mProgressBar, mLayoutParams);
		TextView mTipContent = new TextView(RwxxCzfkActivity.this);
		mTipContent.setText("���ڼ�������,���Ժ�...");
		mTipContent.getPaint().setFakeBoldText(true);
		mTipContent.setGravity(Gravity.CENTER_HORIZONTAL);
		/** Ϊ��������ı� */
		mProgressLoadLayout.addView(mTipContent, mLayoutParams);
		/** Ĭ����Ϊ���ɼ���ע��View.GONE��View.INVISIBLE������ */
		mProgressLoadLayout.setVisibility(View.GONE);
		mProgressLoadLayout.setGravity(Gravity.CENTER);
		/** ���mProgressLoadLayout����Ϊһ��View */
		mLoadLayout.addView(mProgressLoadLayout, new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		listview.addFooterView(mLoadLayout, null, false);
	}
	//��ҳ����
	class MyOnScrollListener implements OnScrollListener {
		private int visibleLastIndex = 0; // ���Ŀ���������
		private int visibleItemCount = 0; // ��ǰ���ڿɼ�������

		@Override
		public void onScrollStateChanged(AbsListView view, int scrollState) {
			// TODO Auto-generated method stub
			int itemsLastIndex = adapter.getCount() - 1; // ���ݼ����һ�������
			int lastIndex = itemsLastIndex + 1;// ���ϵײ���loadMoreView��
			int a = OnScrollListener.SCROLL_STATE_IDLE;
			int b = OnScrollListener.SCROLL_STATE_FLING;
			int c = OnScrollListener.SCROLL_STATE_TOUCH_SCROLL;
			if (scrollState == OnScrollListener.SCROLL_STATE_IDLE && visibleLastIndex == lastIndex && !isMaxPage) {
				// ������Զ�����,��������������첽�������ݵĴ���
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
	 * ��������
	 */
	private void getEnterpriseList(final int page, final int pageSize, String entname) {
		StringBuilder builder = new StringBuilder("?userid=");
		builder.append("1234")
		.append("&page=").append(page)
		.append("&pageSize=").append(pageSize);
		
		HttpUtils utils = new HttpUtils();
		utils.configCurrentHttpCacheExpiry(1000 * 5);
	    // ���ó�ʱʱ��  
		utils.configTimeout(5 * 1000);// ���ӳ�ʱ  //ָ��������һ��url�����ӵȴ�ʱ�䡣  
		utils.configSoTimeout(5 * 1000);// ��ȡ���ݳ�ʱ  //ָ����������һ��url����ȡresponse�ķ��صȴ�ʱ�� 
		String base =PathManager.CHUZHIFANKUI_URL;
		//String base = /*linkIp+*/"http://192.168.15.66:8080/JiNanhuanbaoms/task/Handlingfeedback.do";
		String url = base + builder.toString();
		
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
				//��ȡData
				if(call!=null && !call.equals("")){
					JSONObject jobject;
					try {
						jobject = new JSONObject(call);
						String rows = jobject.getString("rows")+"";
				        Data = JsonHelper.paseJSON(rows)/*JsonHelper.paseJSONToMap(call)*/;
				        if (Data==null ) {
				        	Data=new ArrayList<HashMap<String,Object>>();
						}else{
							Data=DisplayUitl.parseLowerList(Data);
						}
					}catch(Exception e){
						
					}
				}
				if (Data.size() > 0) {
					if (currage == 1) {
						SearchList.clear();
						SearchList.addAll(Data);
					} else {
						SearchList.addAll(Data);
					}
					if (Data.size() == 0 || Data.size() < pageSize) {// /���һҳ
						isMaxPage = true;
//						Toast.makeText(RwxxXcjlActivity.this, " isMaxPage = true", 200).show();
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
