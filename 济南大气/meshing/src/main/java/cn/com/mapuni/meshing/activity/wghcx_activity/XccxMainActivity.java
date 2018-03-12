package cn.com.mapuni.meshing.activity.wghcx_activity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.Call;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import org.json.JSONArray;
import org.json.JSONObject;

import lecho.lib.hellocharts.view.ColumnChartView;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import cn.com.mapuni.meshing.base.BaseActivity;
import cn.com.mapuni.meshing.base.controls.loading.YutuLoading;
import cn.com.mapuni.meshing.base.util.DisplayUitl;
import cn.com.mapuni.meshing.util.ColumnChartUtils;

import cn.com.mapuni.meshingtotal.R;

public class XccxMainActivity extends BaseActivity implements OnClickListener, OnCheckedChangeListener {

	private RadioButton radio_ri, radio_zhou, radio_yue;
	private ColumnChartView chart;

	private String bodyStr = "";
	private YutuLoading yutuLoading;
	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case 101:
				yutuLoading = new YutuLoading(XccxMainActivity.this);
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
						arr1.put("ID", jsonArray.getJSONObject(i).getString("id"));
						obj.add(arr1);
					}
					
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					Toast.makeText(XccxMainActivity.this, "解析数据失败", Toast.LENGTH_SHORT).show();
				}
				break;
			case 103:
				if (null != yutuLoading) {
					yutuLoading.dismissDialog();
				}
				Toast.makeText(XccxMainActivity.this, "查询失败", Toast.LENGTH_SHORT).show();
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
		SetBaseStyle((RelativeLayout) findViewById(R.id.parentLayout), "统计信息");
		initView();
	}

	private void initView() {
		middleLayout = ((LinearLayout) findViewById(R.id.middleLayout1));
		LayoutInflater inflater = LayoutInflater.from(this);
		View mainView = inflater.inflate(R.layout.xccxmainactivity_layout, null);
		middleLayout.addView(mainView);

		chart = (ColumnChartView) findViewById(R.id.chart);

		radio_ri = (RadioButton) mainView.findViewById(R.id.radio_ri);
		radio_zhou = (RadioButton) mainView.findViewById(R.id.radio_zhou);
		radio_yue = (RadioButton) mainView.findViewById(R.id.radio_yue);
		radio_ri.setOnCheckedChangeListener(this);
		radio_zhou.setOnCheckedChangeListener(this);
		radio_yue.setOnCheckedChangeListener(this);
		radio_yue.setChecked(true);

		addDataToChar();
		callWeb();
	}
	
	void callWeb() {
		String url = "http://192.168.15.66:8080/JiNanhuanbaoms/task/Patrolrecord.do";
		handler.sendEmptyMessage(101);
		MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM)
				.addFormDataPart("page", "1").addFormDataPart("pageSize", "1000")
				.addFormDataPart("Userid", DisplayUitl.readPreferences(XccxMainActivity.this, "lastuser", "userid"))
				.addFormDataPart("Status", "1000");
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


	void addDataToChar() {
		List<Integer> list_hg = new ArrayList<Integer>();
		list_hg.add(320);
		list_hg.add(210);
		list_hg.add(453);
		list_hg.add(475);
		list_hg.add(345);
		list_hg.add(543);
		list_hg.add(340);
		list_hg.add(230);

		ColumnChartUtils columnChartUtils = new ColumnChartUtils(list_hg, null,
				new String[] { "七月", "八月", "九月", "十月", "十一月", "十二月", "一月", "二月" });
		columnChartUtils.setChartView_qy(chart);
		
	}

	void addDataToChar2() {
		List<Integer> list_hg = new ArrayList<Integer>();
		list_hg.add(12);
		list_hg.add(16);
		list_hg.add(35);
		list_hg.add(40);
		list_hg.add(24);
		list_hg.add(32);
		list_hg.add(14);

		ColumnChartUtils columnChartUtils = new ColumnChartUtils(list_hg, null,
				new String[] { "17日", "18日", "19日", "20日", "21日", "22日", "23日" });
		columnChartUtils.setChartView_qy(chart);
		
	}

	void addDataToChar3() {
		List<Integer> list_hg = new ArrayList<Integer>();
		list_hg.add(12);
		list_hg.add(16);
		list_hg.add(35);
		list_hg.add(40);
		list_hg.add(24);
		list_hg.add(32);
		list_hg.add(15);
		list_hg.add(12);
		list_hg.add(36);
		list_hg.add(36);
		list_hg.add(40);
		list_hg.add(24);
		list_hg.add(32);
		list_hg.add(34);
		list_hg.add(4);
		list_hg.add(17);
		list_hg.add(2);
		list_hg.add(30);
		list_hg.add(24);
		list_hg.add(22);
		list_hg.add(14);
		list_hg.add(13);
		list_hg.add(20);

		ColumnChartUtils columnChartUtils = new ColumnChartUtils(list_hg, null,
				new String[] { "01日", "02日", "03日", "04日", "05日", "06日", "07日", "08日", "09日", "10日", "11日", "12日",
						"13日", "14日", "15日", "16日", "17日", "18日", "19日", "20日", "21日", "22日", "23日" });
		columnChartUtils.setChartView_qy(chart);
		
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

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		// TODO Auto-generated method stub private RadioButton
		// radio_ri,radio_zhou,radio_yue;
		if (isChecked) {
			switch (buttonView.getId()) {
			case R.id.radio_ri:
				addDataToChar3();
				break;
			case R.id.radio_zhou:
				addDataToChar2();
				break;
			case R.id.radio_yue:
				addDataToChar();
				break;
			default:
				break;
			}
		}

	}

}
