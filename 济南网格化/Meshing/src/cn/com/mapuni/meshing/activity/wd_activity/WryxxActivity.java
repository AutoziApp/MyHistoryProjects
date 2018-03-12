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
	
	//��ҳ���ص�����
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
		SetBaseStyle((RelativeLayout) findViewById(R.id.parentLayout),"��ȾԴ��Ϣ");
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
					Toast.makeText(WryxxActivity.this, "��������",
							Toast.LENGTH_SHORT).show();
					break;
				case 1:
					list.clear();
					list.addAll(SearchList);
					myTaskAdapter.notifyDataSetChanged();
					break;
				case 3:
					 Toast.makeText(WryxxActivity.this, "�������쳣",Toast.LENGTH_SHORT).show();
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
		yutuLoading = new YutuLoading(WryxxActivity.this);
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
		private final int layoutid = R.layout.wd_wryxx_listitem;
		private final int textSize;

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
//				// Toast.makeText(context, "������������,��ʱ�޷���ȡ������Ϣ",
//				// Toast.LENGTH_SHORT).show();
//				// }
//				loading.setLoadMsg("", "����Ϊ��!");
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
		/** ������Id */
		public TextView id = null;
		/** ���б�ĵ�һ������ */
		public TextView title = null;
		/** ���б�ڶ��к�ߵ����� */
		TextView date = null;
		/** �б���ߵ�ͼƬ */
		ImageView lefticon = null;

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
		WryxxActivity.this.getWindowManager().getDefaultDisplay().getMetrics(dm);

		/** ProgressBar�Ĳ��� */
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
		/** Ϊ������ӽ����� */
		mProgressLoadLayout.addView(mProgressBar, mLayoutParams);
		TextView mTipContent = new TextView(WryxxActivity.this);
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
			int itemsLastIndex = myTaskAdapter.getCount() - 1; // ���ݼ����һ�������
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
//		http://192.168.15.64:8080/JiNanhuanbaoms/task/selWuranyuan.do?page=1&pageSize=2
		StringBuilder builder = new StringBuilder("?page=");
		builder.append(page)
		.append("&pageSize=").append(pageSize);
		
		HttpUtils utils = new HttpUtils();
		utils.configCurrentHttpCacheExpiry(1000 * 5);
	    // ���ó�ʱʱ��  
		utils.configTimeout(5 * 1000);// ���ӳ�ʱ  //ָ��������һ��url�����ӵȴ�ʱ�䡣  
		utils.configSoTimeout(5 * 1000);// ��ȡ���ݳ�ʱ  //ָ����������һ��url����ȡresponse�ķ��صȴ�ʱ�� 
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
				//��ȡData
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
					if (Data.size() == 0 || Data.size() < pageSize) {// /���һҳ
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
