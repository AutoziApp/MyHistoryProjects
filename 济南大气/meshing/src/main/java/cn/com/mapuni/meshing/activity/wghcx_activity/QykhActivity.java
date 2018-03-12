package cn.com.mapuni.meshing.activity.wghcx_activity;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import okhttp3.Call;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
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

import cn.com.mapuni.meshing.base.BaseActivity;
import cn.com.mapuni.meshing.base.controls.loading.YutuLoading;
import cn.com.mapuni.meshing.base.interfaces.PathManager;

import cn.com.mapuni.meshingtotal.R;

public class QykhActivity extends BaseActivity implements OnClickListener {
	private LinearLayout middle;
	private ListView listview;
	
	private String bodyStr = "";
	private YutuLoading yutuLoading;
	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case 101:
				yutuLoading = new YutuLoading(QykhActivity.this);
				yutuLoading.setCancelable(true);
				yutuLoading.setLoadMsg("正在查询数据，请稍候", "");
				yutuLoading.showDialog();
				break;
			case 102:
				if (null != yutuLoading) {
					yutuLoading.dismissDialog();
				}
				try {
					JSONObject jsonObject = new JSONObject(bodyStr);
					JSONArray jsonArray = jsonObject.getJSONArray("rows");
					
					ArrayList<HashMap<String, Object>> obj = new ArrayList<HashMap<String, Object>>();
					HashMap<String, Object> arr1;
					for (int i = 0; i < jsonArray.length(); i++) {
						arr1 = new HashMap<String, Object>();
						arr1.put("total", jsonArray.getJSONObject(i).getString("total"));
						arr1.put("num", jsonArray.getJSONObject(i).getString("num"));
						arr1.put("userId", jsonArray.getJSONObject(i).getString("userId"));
						arr1.put("gridCode", jsonArray.getJSONObject(i).getString("gridCode"));
						arr1.put("checkNum", jsonArray.getJSONObject(i).getString("checkNum"));
						arr1.put("msnNum", jsonArray.getJSONObject(i).getString("msnNum"));
						arr1.put("doneNum", jsonArray.getJSONObject(i).getString("doneNum"));
						obj.add(arr1);
					}
					listview.setAdapter(new MyTaskAdapter(QykhActivity.this,
							(ArrayList<HashMap<String, Object>>) obj, "未处理"));
					if(obj.size() == 0) {
						Toast.makeText(QykhActivity.this, "暂无数据", Toast.LENGTH_SHORT).show();
					} else {
						Toast.makeText(QykhActivity.this, "查询成功", Toast.LENGTH_SHORT).show();
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					Toast.makeText(QykhActivity.this, "解析数据失败", Toast.LENGTH_SHORT).show();
				}
				break;
			case 103:
				if (null != yutuLoading) {
					yutuLoading.dismissDialog();
				}
				Toast.makeText(QykhActivity.this, "查询失败", Toast.LENGTH_SHORT).show();
				break;

			default:
				break;
			}
		}
	};
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ui_mapuni1);
		setBACK_ISSHOW(true);
		SetBaseStyle((RelativeLayout) findViewById(R.id.parentLayout), "区域考核查询");
		initView();
	}

	private void initView() {
		middleLayout = ((LinearLayout) findViewById(R.id.middleLayout1));
		LayoutInflater inflater = LayoutInflater.from(this);
		View mainView = inflater.inflate(R.layout.xccxwclajactivity_layout,
				null);
		middleLayout.addView(mainView);

		middle = (LinearLayout) findViewById(R.id.middle);

		listview = new ListView(this);
		listview.setCacheColorHint(Color.TRANSPARENT);
		listview.setDivider(null);
		listview.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT));
		listview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(QykhActivity.this, QykhXqActivity.class);
				SerializableMap map = new SerializableMap();
				HashMap<String, Object> sss = (HashMap<String, Object>)(parent.getAdapter().getItem(position));
				map.setMap(sss);
				Bundle bundle = new Bundle();
				bundle.putSerializable("item", map);
				intent.putExtras(bundle);
//				intent.putExtra("item", map);
				startActivity(intent);
			}
		});
		
		middle.addView(listview);
		
		callWeb();
	}
	
	void callWeb() {
		String url = PathManager.QYKH_URL;
		handler.sendEmptyMessage(101);
		MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM)
				.addFormDataPart("page", "1").addFormDataPart("pageSize", "1000");
		OkHttpClient client = new OkHttpClient();
		RequestBody requestBody = builder.build();
		Request request = new Request.Builder().url(url).post(requestBody).build();

		client.newCall(request).enqueue(new okhttp3.Callback() {

			@Override
			public void onResponse(Call call, Response response) throws IOException {
				bodyStr = response.body().string();
				boolean ok = response.isSuccessful();

				if (ok) {
					handler.sendEmptyMessage(102);
				} else {
					handler.sendEmptyMessage(103);
				}
			}

			@Override
			public void onFailure(Call arg0, IOException e) {
				// TODO Auto-generated method stub
				handler.sendEmptyMessage(103);
			}
		});
	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		Intent intent = null;
		switch (arg0.getId()) {
//		case R.id.ll_wclaj:
//			break;
		default:
			break;
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
				// if (!Net.checkNet(TaskFlowSildeActivity.this)) {
				// Toast.makeText(context, "由于网络问题,暂时无法获取任务信息",
				// Toast.LENGTH_SHORT).show();
				// }
				loading.setLoadMsg("", "暂无数据");
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
			final String idStr = (String) data.get(position).get("gridCode");
			String titleStr = (String) data.get(position).get("gridCode");
			// String RWBH = (String) data.get(position).get("rwbh");
			if (rwzt.equals("待处理")) {
				leftImgId = R.drawable.wd_rwxx_dclimage;
				holder.task_state
						.setBackgroundResource(R.drawable.wd_rwxx_wclbj);
				holder.task_state.setText("未处理");
				holder.yiwancheng_beizhu.setVisibility(View.GONE);
			} else if (rwzt.equals("已完成")) {
				leftImgId = R.drawable.wd_rwxx_dclimage;
				holder.task_state
						.setBackgroundResource(R.drawable.wd_rwxx_ywcbj);
				holder.task_state.setText("已完成");
				holder.cuiban.setVisibility(View.GONE);
				holder.chuli.setVisibility(View.GONE);
				holder.yiwancheng_beizhu.setText("已完成去焦化厂查看煤渣的备注信息");
				holder.yiwancheng_beizhu.setVisibility(View.VISIBLE);
			}

			holder.id.setText(idStr);
			holder.title.setText(titleStr);
			holder.date.setText("2016-12-12");
			// holder.lefticon.setBackgroundResource(leftImgId);
			holder.lefticon.setImageResource(leftImgId);

			holder.date.setVisibility(View.GONE);
			holder.cuiban.setVisibility(View.GONE);
			holder.chuli.setVisibility(View.GONE);
			holder.task_state.setVisibility(View.GONE);

			// holder.banli.setOnClickListener(new OnClickListener() {
			//
			// @Override
			// public void onClick(View v) {
			// rwxx.setCurrentID(idStr);
			// Intent intent = new Intent(TaskFlowSildeActivity.this,
			// FreedomTaskInformationActivity.class);
			//
			//
			// Bundle bundle = new Bundle();
			// bundle.putSerializable("BusinessObj", rwxx);
			// bundle.putString("rwzt", "办理");
			// intent.putExtras(bundle);
			// startActivity(intent);
			// }
			// });

			// holder.zhixin.setOnClickListener(new OnClickListener() {
			//
			// @Override
			// public void onClick(View v) {
			// rwxx.setCurrentID(idStr);
			// Intent intent = new Intent(TaskFlowSildeActivity.this,
			// FreedomTaskInformationActivity.class);
			//
			//
			// Bundle bundle = new Bundle();
			// bundle.putSerializable("BusinessObj", rwxx);
			// bundle.putString("rwzt", "执行");
			// intent.putExtras(bundle);
			// startActivity(intent);
			// }
			// });

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

	/**
	 * 序列化map供Bundle传递map使用 Created on 13-12-9.
	 */
	private class SerializableMap implements Serializable {

		private Map<String, Object> map;

		public Map<String, Object> getMap() {
			return map;
		}

		public void setMap(Map<String, Object> map) {
			this.map = map;
		}
	}

}
