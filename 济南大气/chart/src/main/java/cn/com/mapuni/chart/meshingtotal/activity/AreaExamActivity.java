package cn.com.mapuni.chart.meshingtotal.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cn.com.mapuni.chart.meshingtotal.R;
import cn.com.mapuni.meshing.base.BaseActivity;
import cn.com.mapuni.meshing.base.controls.loading.YutuLoading;
import cn.com.mapuni.meshing.base.util.DisplayUitl;
import cn.com.mapuni.meshing.base.util.PieChartUtils;
import lecho.lib.hellocharts.listener.PieChartOnValueSelectListener;
import lecho.lib.hellocharts.model.SliceValue;
import lecho.lib.hellocharts.view.PieChartView;
import okhttp3.Call;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class AreaExamActivity extends BaseActivity implements OnClickListener {
	private TextView text_gridName,text_gridMan,text_checkNum,text_msnNum,text_doneNum;
	private PieChartView chart_pie;
	private List<Integer> listData;
	private List<String> listLable;

	private String bodyStr = "";
	private YutuLoading yutuLoading;
	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case 101:
				yutuLoading = new YutuLoading(AreaExamActivity.this);
				yutuLoading.setCancelable(true);
				yutuLoading.setLoadMsg("正在获取数据，请稍候", "");
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
					Toast.makeText(AreaExamActivity.this, "解析数据失败", Toast.LENGTH_SHORT).show();
				}
				break;
			case 103:
				if (null != yutuLoading) {
					yutuLoading.dismissDialog();
				}
				Toast.makeText(AreaExamActivity.this, "查询失败", Toast.LENGTH_SHORT).show();
				break;

			default:
				break;
			}
		}
	};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ui_mapuni);
		setBACK_ISSHOW(true);
		SetBaseStyle((RelativeLayout) findViewById(R.id.parentLayout), "区域考核");
		initView();
	}

	private void initView() {
		middleLayout = ((LinearLayout) findViewById(R.id.middleLayout));
		LayoutInflater inflater = LayoutInflater.from(this);
		View mainView = inflater.inflate(R.layout.areaexamactivity_layout, null);
		middleLayout.addView(mainView);

		text_gridName = (TextView) findViewById(R.id.text_gridName);
		text_gridMan = (TextView) findViewById(R.id.text_gridMan);
		text_checkNum = (TextView) findViewById(R.id.text_checkNum);
		text_msnNum = (TextView) findViewById(R.id.text_msnNum);
		text_doneNum = (TextView) findViewById(R.id.text_doneNum);

		chart_pie = (PieChartView) findViewById(R.id.chart_pie);
		chart_pie.setOnValueTouchListener(new PieChartOnValueSelectListener() {
			@Override
			public void onValueSelected(int arcIndex, SliceValue value) {
				text_gridName.setText(listLable.get(arcIndex) + "");
				text_gridMan.setText("王处");
				text_checkNum.setText("123");
				text_msnNum.setText("53");
				text_doneNum.setText("30");
			}

			@Override
			public void onValueDeselected() {

			}
		});

		addDataToChar();
		callWeb();
	}
	
	void callWeb() {
		String url = "http://192.168.15.66:8080/JiNanhuanbaoms/task/Patrolrecord.do";
		handler.sendEmptyMessage(101);
		MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM)
				.addFormDataPart("page", "1").addFormDataPart("pageSize", "1000")
				.addFormDataPart("Userid", DisplayUitl.readPreferences(AreaExamActivity.this, "lastuser", "userid"))
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
		listData = new ArrayList<Integer>();
		listData.add(320);
		listData.add(210);
		listData.add(453);
		listData.add(475);
		listData.add(345);
		listData.add(543);
		listData.add(340);
		listData.add(230);
		listData.add(453);
		listData.add(475);
		listData.add(345);

		listLable = new ArrayList<String>();
		listLable.add("高新区");
		listLable.add("槐荫区");
		listLable.add("济阳县");
		listLable.add("历城区");
		listLable.add("平阴县");
		listLable.add("章丘市");
		listLable.add("长清区");
		listLable.add("市中区");
		listLable.add("历下区");
		listLable.add("天桥区");
		listLable.add("商河县");

		PieChartUtils pieChartUtils = new PieChartUtils(listData, listLable);
		pieChartUtils.setChartView_qy(chart_pie);
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

}
