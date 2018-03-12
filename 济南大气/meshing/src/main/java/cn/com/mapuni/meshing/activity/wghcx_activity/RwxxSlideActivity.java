package cn.com.mapuni.meshing.activity.wghcx_activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import cn.com.mapuni.meshing.adapter.SlideOnLoadAdapter;
import cn.com.mapuni.meshing.base.BaseActivity;
import cn.com.mapuni.meshing.base.controls.loading.YutuLoading;
import cn.com.mapuni.meshing.base.dataprovider.JsonHelper;
import cn.com.mapuni.meshing.base.interfaces.PathManager;
import cn.com.mapuni.meshing.base.util.DisplayUitl;
import cn.com.mapuni.meshing.view.SlideView;
import cn.com.mapuni.meshingtotal.R;

public class RwxxSlideActivity extends BaseActivity {

	ImageView wd_tx;
	LinearLayout middle;
	private SlideView slideView;
	YutuLoading yutuLoading;
	/** 默认显示第几个页面 */
	private int positon;
	String title[] = { "待处理", "已完成" };

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ui_mapuni);
		SetBaseStyle((RelativeLayout) findViewById(R.id.parentLayout), "案件查询");
		initData();
		getDCLRWList("0", "1", "50", "");// 待处理
	}

	@Override
	protected void onResume() {
		super.onResume();
		getDCLRWList("0", "1", "50", "");// 待处理
	}
	
	private void initData() {
		middleLayout = ((LinearLayout) findViewById(R.id.middleLayout));
		LayoutInflater inflater = LayoutInflater.from(this);
		View mainView = inflater.inflate(R.layout.wdrwxxactivity_layout, null);
		middleLayout.addView(mainView);
		middle = (LinearLayout) findViewById(R.id.middle);

		yutuLoading = new YutuLoading(RwxxSlideActivity.this);
		yutuLoading.setCancelable(true);
		yutuLoading.setLoadMsg("正在加载数据，请稍候", "");
		yutuLoading.showDialog();
	}

	private void initView() {
		slideView = new SlideView(this, 0);
		int size = 2;
		slideView.setSlideViewWidth(DisplayUitl.getMobileResolution(this)[0]
				/ size);
//		yutuLoading = new YutuLoading(RwxxSlideActivity.this);
//		yutuLoading.setCancelable(true);
//		yutuLoading.setLoadMsg("正在加载数据，请稍候", "");
//		yutuLoading.showDialog();
		
		for (int i = 0; i < size; i++) {
			final ListView listview = new ListView(this);
			listview.setCacheColorHint(Color.TRANSPARENT);
			listview.setDivider(null);
			listview.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,
					LayoutParams.FILL_PARENT));
			SlideOnLoadAdapter adapter = null;
			final int temp = i;
			adapter = new SlideOnLoadAdapter(listview) {
				@Override
				public void OnLoad() {
					new SyncLoadingData(temp).execute(this.view);
				}
			};
			slideView.AddPageView(adapter, title[i]);
		}
		slideView.setFirstPosition(positon);
		slideView.Display();
		middle.addView(slideView);
	}

	private class SyncLoadingData extends AsyncTask<View, Void, Void> {
		private View view;
		private ArrayList<HashMap<String, Object>> obj;
		private int position;
		private String statu;

		public SyncLoadingData(int temp) {
			this.position = temp;
			obj = new ArrayList<HashMap<String, Object>>();
		}

		@Override
		protected Void doInBackground(View... params) {
			view = params[0];
			statu = title[position];
			if (statu.equals("待处理")) {
				obj = searchdata_dcl;
			} else if (statu.equals("已完成")) {
				obj = searchdata_ywc;
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			if (yutuLoading != null && yutuLoading.isShown()) {
				yutuLoading.dismissDialog();
			}
			if (obj != null && obj.size() > 0) {
				((ListView) view).setAdapter(new MyTaskAdapter(
						RwxxSlideActivity.this,
						(ArrayList<HashMap<String, Object>>) obj, statu));
				((ListView) view)
						.setOnItemClickListener(new OnItemClickListener() {

							@Override
							public void onItemClick(AdapterView<?> arg0,
									View arg1, int arg2, long arg3) {
								// TODO Auto-generated method stub
								Intent intent = new Intent(
										RwxxSlideActivity.this,
										RwxxLiebiaoXQActivity.class);
								intent.putExtra("statu", statu);
								intent.putExtra("arr", obj.get(arg2));
								startActivity(intent);
							}

						});
			} else {
				// ((ListView) view).setAdapter(new
				// MyTaskAdapter(TaskFlowSildeActivity.this, new
				// ArrayList<HashMap<String, Object>>(), statu));
			}

		}
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
		private final int layoutid = R.layout.wd_rwxx_listitem;
		private final String rwzt;
		private final int textSize;
		private int textColor = Color.BLACK;
		private int textColorRed = Color.RED;
		private int textColorGreen = Color.GREEN;
		private int textColorYellow = Color.rgb(255, 140, 00);

		/**
		 * Description:
		 * 
		 * @param
		 * @return
		 * @author Administrator
		 * @Create at: 2013-4-9 下午3:27:39
		 */
		public MyTaskAdapter(Context context,
				ArrayList<HashMap<String, Object>> data, String rwzt) {
			this.mInflater = LayoutInflater.from(context);
			this.context = context;
			this.data = data == null ? new ArrayList<HashMap<String, Object>>()
					: data;
			this.rwzt = rwzt;
			textSize = 21;
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
				return 1;
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
			if (data == null || data.size() == 0) {
				YutuLoading loading = new YutuLoading(context);
				loading.setLoadMsg("", "暂无此状态任务");
				loading.setFailed();
				return loading;
			}
			ViewHolder holder = null;

			if (convertView == null) {
				convertView = mInflater.inflate(layoutid, null);
				holder = new ViewHolder();
				holder.lefticon = (ImageView) convertView
						.findViewById(R.id.lefticon);
				holder.id = new TextView(context);
				holder.title = (TextView) convertView.findViewById(R.id.title);
				holder.date = (TextView) convertView.findViewById(R.id.date);
				holder.task_state = (TextView) convertView
						.findViewById(R.id.task_state);
				holder.yiwancheng_beizhu = (TextView) convertView
						.findViewById(R.id.yiwancheng_beizhu);
				holder.chuli = (Button) convertView.findViewById(R.id.chuli);
				holder.cuiban = (Button) convertView.findViewById(R.id.cuiban);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			// 3.Get Data
			int leftImgId = R.drawable.wd_rwxx_dclimage;
			final String idStr = (String) data.get(position).get("name");
			String titleStr = "去焦化厂查看煤渣";
			String contentStr = "";
			String dateStr = "";
			// String RWBH = (String) data.get(position).get("rwbh");
			if (rwzt.equals("待处理")) {
				leftImgId = R.drawable.wd_rwxx_dclimage;
				holder.task_state
						.setBackgroundResource(R.drawable.wd_rwxx_wclbj);
				holder.task_state.setText("未处理");
				holder.yiwancheng_beizhu.setVisibility(View.GONE);

				holder.id.setText(idStr);

			} else if (rwzt.equals("已完成")) {
				leftImgId = R.drawable.wd_rwxx_ywcimage;
				holder.task_state
						.setBackgroundResource(R.drawable.wd_rwxx_ywcbj);
				holder.task_state.setText("已完成");
				holder.cuiban.setVisibility(View.GONE);
				holder.chuli.setVisibility(View.GONE);
				holder.yiwancheng_beizhu.setText(data.get(position).get("wtjl")
						+ "");
				holder.yiwancheng_beizhu.setVisibility(View.VISIBLE);
				holder.id.setText(idStr);

			}
			holder.title.setText(data.get(position).get("entname") + "");
			String time = data.get(position).get("xcwtsj") + "";
			if (time.length() >= 10)
				time = time.substring(0, 10);
			holder.date.setText(time);
			// holder.lefticon.setBackgroundResource(leftImgId);
			holder.lefticon.setImageResource(leftImgId);

			holder.chuli.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
//					Intent intent = new Intent(RwxxSlideActivity.this,
//							RwxxChuliActivity.class);
//					intent.putExtra("arr", data.get(position));
//					startActivity(intent);
				}
			});

			holder.cuiban.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					getCBResult(data.get(position).get("id")+"");
					
				}
			});

			return convertView;
		}
	}

	protected class ViewHolder {
		public TextView task_state;
		/** 绑定数据Id */
		public TextView id = null;
		/** 绑定列表的第一行数据 */
		public TextView title = null;
		/** 绑定列表第二行数据 */
		// public TextView content = null;
		/** 绑定列表第二行后边的数据 */
		// TextView dateMsg = null;
		/** 绑定列表第二行后边的数据 */
		TextView date = null;
		TextView yiwancheng_beizhu = null;

		/** 列表左边的图片 */
		ImageView lefticon = null;
		/** 列表右边的图标 */
		// ImageView righticon = null;
		Button chuli = null;
		Button cuiban = null;
		/** 列表项在列表中的位置 */
		public int position = 0;
	}

	ArrayList<HashMap<String, Object>> searchdata_dcl = new ArrayList<HashMap<String, Object>>();
	ArrayList<HashMap<String, Object>> searchdata_ywc = new ArrayList<HashMap<String, Object>>();

	// 获取待处理接口
	/**
	 * http://171.8.66.103:8473/JiNanhuanbaoms/task/selectAppTaskRecord.do?
	 * tasktaskState=0&page=1&pageSize=10
	 */
	private void getDCLRWList(final String tasktaskState, final String page,
			final String pageSize, final String type) {

		StringBuilder builder = new StringBuilder("?userid=");
		builder.append("123").append("&taskState=").append(tasktaskState)
				.append("&page=").append(page).append("&pageSize=")
				.append(pageSize);

		HttpUtils utils = new HttpUtils();
		utils.configCurrentHttpCacheExpiry(1000 * 5);
		// 设置超时时间
		utils.configTimeout(5 * 1000);// 连接超时 //指的是连接一个url的连接等待时间。
		utils.configSoTimeout(5 * 1000);// 获取数据超时
										// //指的是连接上一个url，获取response的返回等待时间
		String base=PathManager.DAICHILI_URL;
	//	String base = /* linkIp+ */"http://171.8.66.103:8473/JiNanhuanbaoms/task/selBeProcessed.do";
		String url = base + builder.toString();

		utils.send(HttpMethod.GET, url, new RequestCallBack<String>() {
			@Override
			public void onFailure(HttpException arg0, String arg1) {
				Toast.makeText(RwxxSlideActivity.this, "服务器异常", Toast.LENGTH_SHORT).show();
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
						searchdata_dcl = JsonHelper.paseJSON(rows);
						if (searchdata_dcl == null) {
							searchdata_dcl = new ArrayList<HashMap<String, Object>>();
						} else {
							searchdata_dcl = DisplayUitl
									.parseLowerList(searchdata_dcl);
						}
					} catch (Exception e) {
						searchdata_dcl = new ArrayList<HashMap<String, Object>>();
					}
					getYWCRWList("1", "1", "50", "");// 已完成
				}else{
					if (yutuLoading != null && yutuLoading.isShown()) {
						yutuLoading.dismissDialog();
					}
				}
			}

		});
	}

	// 获取已完成接口
	/**
	 * http://171.8.66.103:8473/JiNanhuanbaoms/task/selectAppTaskRecord.do?
	 * tasktaskState=0&page=1&pageSize=10
	 */
	private void getYWCRWList(final String tasktaskState, final String page,
			final String pageSize, final String type) {
		  String userid= DisplayUitl.readPreferences(this,
				 "lastuser", "userid");
		StringBuilder builder = new StringBuilder("?userid=");
		builder.append("123").append("&taskState=").append(tasktaskState)
				.append("&page=").append(page).append("&pageSize=")
				.append(pageSize);

		HttpUtils utils = new HttpUtils();
		utils.configCurrentHttpCacheExpiry(1000 * 5);
		// 设置超时时间
		utils.configTimeout(5 * 1000);// 连接超时 //指的是连接一个url的连接等待时间。
		utils.configSoTimeout(5 * 1000);// 获取数据超时
										// //指的是连接上一个url，获取response的返回等待时间
		String base=PathManager.YIWANCHENG_URL;
		//String base = /* linkIp+ */"http://171.8.66.103:8473/JiNanhuanbaoms/task/selBeProcessed.do";
		String url = base + builder.toString();

		utils.send(HttpMethod.GET, url, new RequestCallBack<String>() {
			@Override
			public void onFailure(HttpException arg0, String arg1) {
				Toast.makeText(RwxxSlideActivity.this, "服务器异常", 200).show();
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
						searchdata_ywc = JsonHelper.paseJSON(rows);
						if (searchdata_ywc == null) {
							searchdata_ywc = new ArrayList<HashMap<String, Object>>();
						} else {
							searchdata_ywc = DisplayUitl
									.parseLowerList(searchdata_ywc);
						}
					} catch (Exception e) {
						// TODO Auto-generated catch block
						searchdata_ywc = new ArrayList<HashMap<String, Object>>();
					}
					initView();
				}else{
					if (yutuLoading != null && yutuLoading.isShown()) {
						yutuLoading.dismissDialog();
					}
				}
				
			}

		});
	}
	// 获取催办接口
/*	http://192.168.15.66:8080/JiNanhuanbaoms/task/Presssth.do?id=2B2646A7-F946-47A7-9FF2-94694A663C45
*/	private void getCBResult(final String id) {
		StringBuilder builder = new StringBuilder("?id=");
		builder.append(id);

		HttpUtils utils = new HttpUtils();
		utils.configCurrentHttpCacheExpiry(1000 * 5);
		// 设置超时时间
		utils.configTimeout(5 * 1000);// 连接超时 //指的是连接一个url的连接等待时间。
		utils.configSoTimeout(5 * 1000);// 获取数据超时
										// //指的是连接上一个url，获取response的返回等待时间
		String base=PathManager.CUIBAN_URL;
		//String base = /* linkIp+ */"http://192.168.15.66:8080/JiNanhuanbaoms/task/Presssth.do";
		String url = base + builder.toString();

		utils.send(HttpMethod.GET, url, new RequestCallBack<String>() {
			@Override
			public void onFailure(HttpException arg0, String arg1) {
				Toast.makeText(RwxxSlideActivity.this, "服务器异常", 200).show();
				if (yutuLoading != null && yutuLoading.isShown()) {
					yutuLoading.dismissDialog();
				}
			}

			@Override
			public void onSuccess(ResponseInfo<String> arg0) {
				String json = String.valueOf(arg0.result);
				// 获取Data
				if (json != null && !json.equals("")) {
					JSONObject jobject;
					try {
						jobject = new JSONObject(json);
						String result_str = jobject.getString("result") + "";
			        	if (result_str != null && result_str.equals("succer")) {
						    Toast.makeText(getApplicationContext(), "已催办", Toast.LENGTH_SHORT).show();
						}else{
							Toast.makeText(getApplicationContext(), "催办失败,请重新操作!", Toast.LENGTH_SHORT).show();
						}
					}catch(Exception e){
						Toast.makeText(getApplicationContext(), "催办失败,请重新操作!", Toast.LENGTH_SHORT).show();
					}
				}else{
					Toast.makeText(getApplicationContext(), "催办失败,请重新操作!", Toast.LENGTH_SHORT).show();
				}
				if (yutuLoading != null && yutuLoading.isShown()) {
					yutuLoading.dismissDialog();
				}
			}

		});
	}

}
