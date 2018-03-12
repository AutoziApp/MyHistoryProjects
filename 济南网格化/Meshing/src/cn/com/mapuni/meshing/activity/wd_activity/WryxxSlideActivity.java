package cn.com.mapuni.meshing.activity.wd_activity;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONObject;

import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
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
import com.mapuni.android.base.LoadDetailLayout;
import com.mapuni.android.base.controls.loading.YutuLoading;
import com.mapuni.android.base.interfaces.IList;
import com.mapuni.android.base.interfaces.PathManager;
import com.mapuni.android.base.util.DisplayUitl;
import com.mapuni.android.dataprovider.JsonHelper;

public class WryxxSlideActivity extends BaseActivity {

	ImageView wd_tx;
	LinearLayout middle;
    int index_scss = 1;
	private SlideView slideView;
	YutuLoading yutuLoading;
	/** 默认显示第几个页面 */
	private int positon;
	String title[] = { "企业基本信息", "生产设施运行情况", "污染物治理设施", "排污口情况" };
	/** 定义 LayoutInflater */
	private LayoutInflater inflater;
	View viewQYJBXX, viewSCYL;
	String PolSorCode;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ui_mapuni);
		SetBaseStyle((RelativeLayout) findViewById(R.id.parentLayout), "一企一档信息");
		initData();
	}
	HashMap<String, Object> arr = new HashMap<String, Object>();
	private void initData() {
		middleLayout = ((LinearLayout) findViewById(R.id.middleLayout));
		LayoutInflater inflater = LayoutInflater.from(this);
		View mainView = inflater.inflate(R.layout.wdwryxxactivity_layout, null);
		middleLayout.addView(mainView);

		middle = (LinearLayout) findViewById(R.id.middle);
		arr = (HashMap<String, Object>) getIntent().getSerializableExtra(
				"emergency");
		PolSorCode = arr.get("polSorCode")+"";
		initView();

	}

	private void initView() {
		slideView = new SlideView(this, 7);
		int size = 4;
		SlideOnLoadAdapter adapter = null;
		// slideView.setSlideViewWidth(DisplayUitl.getMobileResolution(this)[0]
		// / size);
		// slideView.setLoseBackColor(R.color.rwgl_fl);
		for (int i = 0; i < size; i++) {
			final int temp = i;
//			if (i <= 0) {// 企业基本信息
				RelativeLayout view = new RelativeLayout(this);
				adapter = new SlideOnLoadAdapter(view) {
					@Override
					public void OnLoad() {
						yutuLoading = new YutuLoading(
								WryxxSlideActivity.this);
						yutuLoading.setCancelable(true);
						yutuLoading.showDialog();

						new SyncLoadingData(false, temp).execute(this.view);
					}
				};
//			} else {
//				final ListView listview = new ListView(this);
//				listview.setCacheColorHint(Color.TRANSPARENT);
//				listview.setDivider(null);
//				listview.setLayoutParams(new LayoutParams(
//						LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
//				
//				final int temp = i;
//				adapter = new SlideOnLoadAdapter(listview) {
//					@Override
//					public void OnLoad() {
//						yutuLoading = new YutuLoading(WryxxSlideActivity.this);
//						yutuLoading.setCancelable(true);
//						yutuLoading.showDialog();
//						new SyncLoadingData(true, temp).execute(this.view);
//					}
//				};
//			}
			slideView.AddPageView(adapter, title[i]);
		}
		// slideView.setFirstPosition(positon);
		slideView.Display();

		middle.addView(slideView);
	}
	SetData setData = new SetData();
	private class SyncLoadingData extends AsyncTask<View, Void, Void> {
		private boolean typeFlg;
		private int index;
		private int childIndex;
		private ArrayList<Object> obj;
		private LoadDetailLayout loadDetailLayout;
		private View view;

		/**
		 * 
		 * @param typeFlg
		 *            true 为list显示 false为view显示
		 * @param index
		 *            view在viewpager的位置
		 */
		public SyncLoadingData(boolean typeFlg, int index) {
			this.typeFlg = typeFlg;
			this.index = index;
		}

		public SyncLoadingData(boolean typeFlg, int index, int childIndex) {
			this.typeFlg = typeFlg;
			this.index = index;
			this.childIndex = childIndex;
		}

		@Override
		protected Void doInBackground(View... params) {
			view = params[0];

			if (typeFlg) {
				
			} else {
				if (index == 0) {
					
				}
			}

			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			
//			if (typeFlg) {
//				ArrayList<HashMap<String, Object>> mdata2 = (ArrayList<HashMap<String, Object>>) ((ArrayList<Object>) obj).get(0);
//				HashMap<String, Object> style2 = (HashMap<String, Object>) ((ArrayList<Object>) obj).get(1);
//
//				if (mdata2.size() == 0) {
//					((ListView) (view)).setDivider(null);
//				}
//				if (mdata2.size() >= 1) {
//					((ListView) (view)).setDivider(getResources().getDrawable(R.drawable.list_divider));
//				}
//
////				((ListView) (view)).setAdapter(new ListActivityAdapter(EnterpriseArchivesActivitySlide.this, new Bundle(), mdata2, style2));
//
//			} else {
				if (index == 0) {
					inflater = LayoutInflater.from(WryxxSlideActivity.this);
					// 查询条件
					viewQYJBXX = inflater.inflate(R.layout.yqyd_qyjbxx_layout, null);
					((RelativeLayout) view).addView(viewQYJBXX);
					setData.InfoFindViewQYJBXX(WryxxSlideActivity.this,arr,viewQYJBXX);
					if (yutuLoading != null) {
						yutuLoading.dismissDialog();
					}
				}else{
					viewSCYL = inflater.inflate(R.layout.yqyd_listview_layout, null);
					((RelativeLayout) view).addView(viewSCYL);
					//根据id找出对应的数据
					String sql;
					index_scss = index;
					if(index == 1){//生产设施
						getSCSSList(PolSorCode);
//						arrs = searchdata_scss;
					}else if(index == 2){//污染物治理
						getWrwzlList(PolSorCode);
//						arrs = searchdata_wrwzl;
					}else if(index == 3){//排污口
						getPWKList(PolSorCode);
//						arrs = searchdata_pwk;
					}
//					ArrayList<HashMap<String, Object>> arr = SqliteUtil.getInstance().queryBySqlReturnArrayListHashMap(sql.toString());
//					for(int i=0;i<3;i++){
//						HashMap<String, Object> arr = new HashMap<String, Object>();
//						if(index == 1){
//						   arr.put("name", "生产设施"+(i+1));
//						}else if(index == 2){
//						   arr.put("name", "污染物"+(i+1));
//						}else if(index == 3){
//							   arr.put("name", "排污口"+(i+1));
//							}
//						arrs.add(arr);
//					}
					
				}

//			}

			
		}
	}
	ArrayList<HashMap<String, Object>> arrs = new ArrayList<HashMap<String, Object>>();
	ArrayList<HashMap<String, Object>> searchdata_pwk = new ArrayList<HashMap<String, Object>>();
	ArrayList<HashMap<String, Object>> searchdata_scss = new ArrayList<HashMap<String, Object>>();
	ArrayList<HashMap<String, Object>> searchdata_wrwzl = new ArrayList<HashMap<String, Object>>();

	// 获取排污口信息
/*	http://192.168.15.66:8080/JiNanhuanbaoms/task/Selinformation.do?PolSorCode=123 
*/	private void getPWKList(final String PolSorCode) {
		StringBuilder builder = new StringBuilder("?PolSorCode=");
		builder.append(PolSorCode);

		HttpUtils utils = new HttpUtils();
		utils.configCurrentHttpCacheExpiry(1000 * 5);
		// 设置超时时间
		utils.configTimeout(5 * 1000);// 连接超时 //指的是连接一个url的连接等待时间。
		utils.configSoTimeout(5 * 1000);// 获取数据超时
										// //指的是连接上一个url，获取response的返回等待时间

		//String base = "http://192.168.15.66:8080/JiNanhuanbaoms/task/Selinformation.do";
		String base=PathManager.PAIWU_URL;
		String url = base + builder.toString();

		utils.send(HttpMethod.GET, url, new RequestCallBack<String>() {
			@Override
			public void onFailure(HttpException arg0, String arg1) {
				Toast.makeText(WryxxSlideActivity.this, "服务器异常", 200).show();
				if (yutuLoading != null && yutuLoading.isShown()) {
					yutuLoading.dismissDialog();
				}
			}

			@Override
			public void onSuccess(ResponseInfo<String> arg0) {
				Log.i("utils", String.valueOf(arg0.result));
				String json = String.valueOf(arg0.result);
//				if (yutuLoading != null && yutuLoading.isShown()) {
//					yutuLoading.dismissDialog();
//				}
				// 获取Data
				if (json != null && !json.equals("")) {
					JSONObject jobject;
					try {
						jobject = new JSONObject(json);
						String rows = jobject.getString("rows") + "";
						searchdata_pwk = JsonHelper.paseJSON(rows);
						if (searchdata_pwk == null) {
							searchdata_pwk = new ArrayList<HashMap<String, Object>>();
						} else {
							searchdata_pwk = DisplayUitl
									.parseLowerList(searchdata_pwk);
							setData.InfoFindViewSCYL(WryxxSlideActivity.this,index_scss,searchdata_pwk,viewSCYL);
						}
					} catch (Exception e) {
						// TODO Auto-generated catch block
						searchdata_pwk = new ArrayList<HashMap<String, Object>>();
					}
				}
				if (yutuLoading != null && yutuLoading.isShown()) {
					yutuLoading.dismissDialog();
				}	
			}

		});
	}
    //获取污染物治理信息
	private void getWrwzlList(final String PolSorCode) {
		StringBuilder builder = new StringBuilder("?PolSorCode=");
		builder.append(PolSorCode);
	
		HttpUtils utils = new HttpUtils();
		utils.configCurrentHttpCacheExpiry(1000 * 5);
		// 设置超时时间
		utils.configTimeout(5 * 1000);// 连接超时 //指的是连接一个url的连接等待时间。
		utils.configSoTimeout(5 * 1000);// 获取数据超时
										// //指的是连接上一个url，获取response的返回等待时间
	
		//String base = "http://192.168.15.66:8080/JiNanhuanbaoms/task/Pollutanttreatment.do";
		String base=PathManager.PAIWUZHILI_URL;
		String url = base + builder.toString();
	
		utils.send(HttpMethod.GET, url, new RequestCallBack<String>() {
			@Override
			public void onFailure(HttpException arg0, String arg1) {
				Toast.makeText(WryxxSlideActivity.this, "服务器异常", 200).show();
				if (yutuLoading != null && yutuLoading.isShown()) {
					yutuLoading.dismissDialog();
				}
			}
	
			@Override
			public void onSuccess(ResponseInfo<String> arg0) {
				Log.i("utils", String.valueOf(arg0.result));
				String json = String.valueOf(arg0.result);
	//			if (yutuLoading != null && yutuLoading.isShown()) {
	//				yutuLoading.dismissDialog();
	//			}
				// 获取Data
				if (json != null && !json.equals("")) {
					JSONObject jobject;
					try {
						jobject = new JSONObject(json);
						String rows = jobject.getString("rows") + "";
						searchdata_wrwzl = JsonHelper.paseJSON(rows);
						if (searchdata_wrwzl == null) {
							searchdata_wrwzl = new ArrayList<HashMap<String, Object>>();
						} else {
							searchdata_wrwzl = DisplayUitl
									.parseLowerList(searchdata_wrwzl);
							setData.InfoFindViewSCYL(WryxxSlideActivity.this,index_scss,searchdata_wrwzl,viewSCYL);
						}
					} catch (Exception e) {
						// TODO Auto-generated catch block
						searchdata_wrwzl = new ArrayList<HashMap<String, Object>>();
					}
				}
				if (yutuLoading != null && yutuLoading.isShown()) {
					yutuLoading.dismissDialog();
				}
			}
	
		});
	}
	 //获取生产设施运行情况信息
		private void getSCSSList(final String PolSorCode) {
			StringBuilder builder = new StringBuilder("?PolSorCode=");
			builder.append(PolSorCode);
		
			HttpUtils utils = new HttpUtils();
			utils.configCurrentHttpCacheExpiry(1000 * 5);
			// 设置超时时间
			utils.configTimeout(5 * 1000);// 连接超时 //指的是连接一个url的连接等待时间。
			utils.configSoTimeout(5 * 1000);// 获取数据超时
											// //指的是连接上一个url，获取response的返回等待时间
			String base =PathManager.SHENGCHANSHESHI_URL;
			//String base = "http://192.168.15.66:8080/JiNanhuanbaoms/task/Operationofproduction.do";
			String url = base + builder.toString();
		
			utils.send(HttpMethod.GET, url, new RequestCallBack<String>() {
				@Override
				public void onFailure(HttpException arg0, String arg1) {
					Toast.makeText(WryxxSlideActivity.this, "服务器异常", 200).show();
					if (yutuLoading != null && yutuLoading.isShown()) {
						yutuLoading.dismissDialog();
					}
				}
		
				@Override
				public void onSuccess(ResponseInfo<String> arg0) {
					Log.i("utils", String.valueOf(arg0.result));
					String json = String.valueOf(arg0.result);
		//			if (yutuLoading != null && yutuLoading.isShown()) {
		//				yutuLoading.dismissDialog();
		//			}
					// 获取Data
					if (json != null && !json.equals("")) {
						JSONObject jobject;
						try {
							jobject = new JSONObject(json);
							String rows = jobject.getString("rows") + "";
							searchdata_scss = JsonHelper.paseJSON(rows);
							if (searchdata_scss == null) {
								searchdata_scss = new ArrayList<HashMap<String, Object>>();
							} else {
								searchdata_scss = DisplayUitl
										.parseLowerList(searchdata_scss);
								setData.InfoFindViewSCYL(WryxxSlideActivity.this,index_scss,searchdata_scss,viewSCYL);
							}
						} catch (Exception e) {
							// TODO Auto-generated catch block
							searchdata_scss = new ArrayList<HashMap<String, Object>>();
						}
					}
					if (yutuLoading != null && yutuLoading.isShown()) {
						yutuLoading.dismissDialog();
					}
					
					
				}
		
			});
		}
}
