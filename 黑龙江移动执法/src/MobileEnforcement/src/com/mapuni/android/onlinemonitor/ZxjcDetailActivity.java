package com.mapuni.android.onlinemonitor;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.mapuni.android.MobileEnforcement.R;
import com.mapuni.android.base.BaseActivity;
import com.mapuni.android.base.controls.loading.YutuLoading;
import com.mapuni.android.base.util.OtherTools;
import com.mapuni.android.dataprovider.JsonHelper;
import com.mapuni.android.dataprovider.NetProvider;
import com.mapuni.android.dataprovider.SqliteUtil;
import com.mapuni.android.netprovider.Net;
import com.mapuni.android.netprovider.WebServiceProvider;

public class ZxjcDetailActivity extends BaseActivity {
	private Spinner zxjc_WasteWG/* 水or气 */, wastesOuts/* 排口名称 */, chatGraphSel/* 图表选择 */, wastesSeries/* 监测因子 */, wastesDate;
	private int wastesOuts_Spinner_Position, chatGraphSel_Spinner_Position, wastesSeries_Spinner_Position;
	private String qyid = ""/* 企业ID */, qymc = ""/* 企业名称 */, isww = ""/* 是不是废水 */, isgas = ""/* 是不是废气 */;
	private ListView listView;// 显示数据列表
	protected HashMap<String, ArrayList<HashMap<String, Object>>> outs;// 存储一天的所有数据，key是排污口，value是这个排污口的数据
	private String[] outsArr;// 排口spinner的数据集合
	private LinearLayout llGraph;// 用来显示图表的区域
	private LinearLayout llSeisCondition;// 查询之后才能显示出来的排口图表选择布局
	private LinkedList<String> wastDate = new LinkedList<String>();// 时间选择
	private RadioGroup radioGroup;
	private ArrayList<HashMap<String, Object>> queryPWkData = null; // 查询排污口集合
	private String pwkmc = null; // 排污口名称
	ArrayList<String> a1 = null;
	private ArrayList<HashMap<String, Object>> queryWRWData = null; // 因子集合
	private String wrumc = null; // 因子污染物名称
	private String wrumz = null; // 污染物名字
	ArrayList<String> a2 = null;
	private ArrayList<HashMap<String, Object>> queryFQFSData = null; // 废气 废水
	ArrayList<String> a3 = null; // 废气 废水
	private String FqFs = null;

	private Button btSelFrom, btSelTo;
	private String[] chemSeries;
	public static boolean isGas = false;
	ArrayList<String> a4 = null;
	ArrayList<String> a5 = null;
	String hours;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ui_mapuni);

		/* 基础视图初始化 */
		RelativeLayout relayout = (RelativeLayout) this.findViewById(com.mapuni.android.base.R.id.parentLayout);
		Intent intent = getIntent();
		qyid = intent.getStringExtra("qyid");
		qymc = intent.getStringExtra("qymc");
		SetBaseStyle(relayout, qymc);
		setTitleLayoutVisiable(true);

		LayoutInflater inflater = LayoutInflater.from(ZxjcDetailActivity.this);
		View view = inflater.inflate(com.mapuni.android.MobileEnforcement.R.layout.activity_zxjc_detail, null);
		llGraph = (LinearLayout) view.findViewById(com.mapuni.android.MobileEnforcement.R.id.ll_graph);
		llSeisCondition = (LinearLayout) view.findViewById(com.mapuni.android.MobileEnforcement.R.id.zxjc_seriesSelection);
		((LinearLayout) findViewById(com.mapuni.android.MobileEnforcement.R.id.middleLayout)).addView(view);

		listView = (ListView) view.findViewById(com.mapuni.android.MobileEnforcement.R.id.zxjc_qy_listView);
		listView.setCacheColorHint(Color.TRANSPARENT);

		hours = "hours";
		radioGroup = (RadioGroup) findViewById(R.id.rg_zxjc);
		radioGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				switch (checkedId) {
				case R.id.radiou_01:
					hours = "hours";
					break;
				case R.id.radiou_02:
					hours = "week";
					break;
				case R.id.radiou_03:
					hours = "month";
					break;

				}
			}
		});

		btSelFrom = (Button) findViewById(R.id.date_from);
		btSelTo = (Button) findViewById(R.id.date_to);

		/* 初始化spinner */
		// 排放口
		zxjc_WasteWG = (Spinner) view.findViewById(com.mapuni.android.MobileEnforcement.R.id.zxjc_WasteWG);
		// 监测因子
		wastesOuts = (Spinner) view.findViewById(com.mapuni.android.MobileEnforcement.R.id.wastes_series);
		// 统计表/图
		chatGraphSel = (Spinner) view.findViewById(com.mapuni.android.MobileEnforcement.R.id.sp_chatgraph);
		wastesDate = (Spinner) view.findViewById(com.mapuni.android.MobileEnforcement.R.id.wastes_date);
		// 污染物
		wastesSeries = (Spinner) view.findViewById(com.mapuni.android.MobileEnforcement.R.id.wastes_series_chem);
		// 准备数据
		initData();
	}

	class MyOnItemSelectListener implements OnItemSelectedListener {

		@Override
		public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
			if (0 == chatGraphSel.getSelectedItemPosition()) {
				initChatGraph();
			} else {
				initListView();
			}
		}

		public void onNothingSelected(AdapterView<?> arg0) {

		}

	}

	private void queryData() {

		if (qyid != null && !qyid.equals("")) {
			String sql = "select PSCode,IOCode,IOName from MonitorOutputInfo where pscode = '" + qyid + "'";
			queryPWkData = SqliteUtil.getInstance().queryBySqlReturnArrayListHashMap(sql);
			a1 = new ArrayList<String>();
			if (queryPWkData.size() == 0) {
				return;
			} else {
				for (HashMap<String, Object> map : queryPWkData) {
					a1.add(map.get("ioname").toString());
				}
			}
			String sql2 = "select PollutantName,EquivalentValue ,PollutantTypeCode from PollutantCode where PollutantTypeCode = '1' or PollutantTypeCode='2'";
			queryWRWData = SqliteUtil.getInstance().queryBySqlReturnArrayListHashMap(sql2);
			a2 = new ArrayList<String>();
			if (queryWRWData.size() == 0) {
				return;
			} else {
				for (HashMap<String, Object> map2 : queryWRWData) {
					a2.add(map2.get("pollutantname").toString());
				}
			}
			String sql3 = "select PollutantTypeName,PollutantTypeCode from PollutantType where PollutantTypeCode = '1' or PollutantTypeCode='2'";
			queryFQFSData = SqliteUtil.getInstance().queryBySqlReturnArrayListHashMap(sql3);
			a3 = new ArrayList<String>();
			if (queryFQFSData.size() == 0) {
				return;
			} else {
				for (HashMap<String, Object> map3 : queryFQFSData) {
					a3.add(map3.get("pollutanttypename").toString());
				}
			}
		}

	}

	/* 显示为列表 */
	public void initListView() {
		// llGraph.removeAllViews();// 清空视图
		ArrayList<HashMap<String, Object>> dataWaste = list;// 准备数据
		llGraph.setVisibility(View.GONE);
		listView.setVisibility(View.VISIBLE);
		listView.setAdapter(new ZXJCAdapter(dataWaste));
	}

	public void queryZxjcData(View view) {
		wastesOuts_Spinner_Position = wastesOuts.getSelectedItemPosition();
		chatGraphSel_Spinner_Position = chatGraphSel.getSelectedItemPosition();
		wastesSeries_Spinner_Position = wastesSeries.getSelectedItemPosition();
		initData();
	}

	private void initChatGraph() {
		llGraph.removeAllViews();
		listView.setVisibility(View.GONE);

		// int tempPosition = wastesOuts.getSelectedItemPosition();
		// tempPosition = tempPosition > outsArr.length || tempPosition == -1 ?
		// 0 : tempPosition;
		// ArrayList<HashMap<String, Object>> dataWaste =
		// outs.get(outsArr[tempPosition]);
		// int tempPosition2 = wastesSeries.getSelectedItemPosition();
		// tempPosition2 = tempPosition2 > outsArr.length || tempPosition2 == -1
		// ? 0 : tempPosition;
		// String key = chemSeries[tempPosition2];

		// ChartCreator c = new ChartCreator(this, list, wrumz, key);
		for (int i = 0; i < list.size(); i++) {
			String timeStr = list.get(i).get("MonitorTime").toString();
			Date date = new Date();
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(date);
			if ("hours".equals(hours)) {
				calendar.add(Calendar.DAY_OF_MONTH, -1);
				calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(timeStr));
			} else if ("week".equals(hours)) {
				calendar.set(Calendar.MONTH, Integer.parseInt(timeStr.substring(0, timeStr.indexOf("-"))) - 1);
				calendar.set(Calendar.DAY_OF_MONTH, Integer.parseInt(timeStr.substring(timeStr.indexOf("-") + 1)));
			} else if ("month".equals(hours)) {
				calendar.set(Calendar.MONTH, Integer.parseInt(timeStr.substring(0, timeStr.indexOf("-"))) - 1);
				calendar.set(Calendar.DAY_OF_MONTH, Integer.parseInt(timeStr.substring(timeStr.indexOf("-") + 1)));
			}
			date = calendar.getTime();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
			list.get(i).put("MonitorTime", sdf.format(date));
		}
		ChartCreator c = new ChartCreator(this, list, wrumz, "AvgData");
		View v = c.create();
		llGraph.setVisibility(View.VISIBLE);
		llGraph.addView(v);

	}

	/**
	 * 初始化数据 访问后台接口得到json并解析
	 */
	private void initData() {
		if (!Net.isConnect(this)) {
			OtherTools.showToast(this, "网络状况不好，请稍后查询");
			actWhenNoData();
			return;
		}

		new AsyncTask<Void, Void, ArrayList<HashMap<String, Object>>>() {
			private YutuLoading loading = null;

			protected void onPreExecute() {
				loading = new YutuLoading(ZxjcDetailActivity.this);
				loading.setLoadMsg("加载中,请稍后...", "");
				loading.showDialog();
			};

			@Override
			protected ArrayList<HashMap<String, Object>> doInBackground(Void... params) {
				queryData();
				HashMap<String, Object> param = new HashMap<String, Object>();
				param.put("psCode", qyid);
				param.put("outputType", queryPWkData.get(0).get("iocode"));
				param.put("pollutantType", FqFs);
				param.put("dateType", hours);
				param.put("pollutantCode", wrumc);

				Object object = NetProvider.callWebService("GetStatisticsData", param, WebServiceProvider.RETURN_STRING);
				if (object != null && !object.equals("") && !object.equals("[]")) {
					return JsonHelper.paseJSON((String) object);
				}

				return null;
			}

			protected void onPostExecute(ArrayList<HashMap<String, Object>> result) {
				initSpinner();
				if (loading != null) {
					loading.dismissDialog();
				}
				if (result != null) {
					list = result;
					// outs = devideByOut(result);
					// if (outs == null || outs.size() == 0) {
					// OtherTools.showToast(ZxjcDetailActivity.this,
					// "由于服务器异常未能获取数据");
					// actWhenNoData();
					// return;
					// }
					//
					// Set<String> set = outs.keySet();
					// outsArr = new String[set.size()];
					// int i = 0;
					// for (Iterator<String> it = set.iterator(); it.hasNext();)
					// {
					// outsArr[i++] = it.next();
					// }

					// ArrayAdapter<String> adapter5 = new
					// ArrayAdapter<String>(ZxjcDetailActivity.this,
					// android.R.layout.simple_spinner_item, chemSeries);
					// adapter5.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
					// wastesSeries.setAdapter(adapter5);
					/* 给排污口和选择显示模式的spinner加监听 */
					// MyOnItemSelectListener listener = new
					// MyOnItemSelectListener();
					// wastesOuts.setOnItemSelectedListener(listener);
					// chatGraphSel.setOnItemSelectedListener(listener);
					// wastesDate.setOnItemSelectedListener(listener);
					// wastesSeries.setOnItemSelectedListener(listener);
					// btSelFrom.setText(wastDate.getFirst());
					// btSelTo.setText(wastDate.getLast());
					recoveryInstanceState();
					initChatGraph();

				} else {
					OtherTools.showToast(ZxjcDetailActivity.this, "由于服务器异常未能获取数据");
					actWhenNoData();
				}

			};

		}.execute();
	}

	private ArrayList<HashMap<String, Object>> list;

	/**
	 * 转化结果为按排污口分
	 * */
	protected HashMap<String, ArrayList<HashMap<String, Object>>> devideByOut(ArrayList<HashMap<String, Object>> result) {
		HashMap<String, ArrayList<HashMap<String, Object>>> res = new HashMap<String, ArrayList<HashMap<String, Object>>>();
		for (int i = 0; i < result.size(); i++) {
			HashMap<String, Object> map = result.get(i);

		}
		return res;
	}

	private void initSpinner() {
		String[] time = new String[] { "分钟", "小 时" };
		String[] chatorGraph = new String[] { "图", "表" };

		ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, a3);
		ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, a1);
		ArrayAdapter<String> adapter3 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, chatorGraph);
		ArrayAdapter<String> adapter4 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, a2);

		adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		adapter4.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

		zxjc_WasteWG.setAdapter(adapter2);
		chatGraphSel.setAdapter(adapter3);
		wastesOuts.setAdapter(adapter4);
		wastesSeries.setAdapter(adapter1);

		wastesSeries.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				if (wastesSeries.getSelectedItem().toString().equals("废水")) {
					String sql1 = "select PollutantName,EquivalentValue,pollutanttypecode from PollutantCode where PollutantTypeCode = '1'";
					queryWRWData = SqliteUtil.getInstance().queryBySqlReturnArrayListHashMap(sql1);
					a4 = new ArrayList<String>();
					for (HashMap<String, Object> m1 : queryWRWData) {
						a4.add(m1.get("pollutantname").toString());
					}

					ArrayAdapter<String> adapter4 = new ArrayAdapter<String>(ZxjcDetailActivity.this, android.R.layout.simple_spinner_item, a4);
					adapter4.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
					wastesOuts.setAdapter(adapter4);
					FqFs = queryFQFSData.get(arg2).get("pollutanttypecode").toString();
				} else {
					if (wastesSeries.getSelectedItem().toString().equals("废气")) {
						String sql2 = "select PollutantName,EquivalentValue,pollutanttypecode from PollutantCode where PollutantTypeCode = '2'";
						queryWRWData = SqliteUtil.getInstance().queryBySqlReturnArrayListHashMap(sql2);
						a5 = new ArrayList<String>();
						for (HashMap<String, Object> m2 : queryWRWData) {
							a5.add(m2.get("pollutantname").toString());
						}

						ArrayAdapter<String> adapter4 = new ArrayAdapter<String>(ZxjcDetailActivity.this, android.R.layout.simple_spinner_item, a5);
						adapter4.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
						wastesOuts.setAdapter(adapter4);
						FqFs = queryFQFSData.get(arg2).get("pollutanttypecode").toString();
					}
				}

			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {

			}
		});
		wastesOuts.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				wrumc = queryWRWData.get(arg2).get("pollutanttypecode").toString();
				wrumz = queryWRWData.get(arg2).get("pollutantname").toString();
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub

			}
		});
		zxjc_WasteWG.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				pwkmc = queryWRWData.get(arg2).get("pollutanttypecode").toString();
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub

			}
		});
		chatGraphSel.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {

				if (chatGraphSel.getSelectedItem().toString().equals("表")) {
					initListView();
				} else {
					llGraph.setVisibility(View.VISIBLE);
					listView.setVisibility(View.GONE);
				}

			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {

			}
		});
	}

	private void actWhenNoData() {
		wastesOuts.setEnabled(true);
		wastesSeries.setEnabled(true);
	}

	private void recoveryInstanceState() {
		wastesOuts.setSelection(wastesOuts_Spinner_Position);
		chatGraphSel.setSelection(chatGraphSel_Spinner_Position);
		wastesSeries.setSelection(wastesSeries_Spinner_Position);
	}

	private void put(HashMap<String, ArrayList<HashMap<String, Object>>> res, String outName1, HashMap<String, Object> map) {
		String outName = outName1.contains(qymc) ? outName1.substring(qymc.length()) : outName1;
		if (res.containsKey(outName)) {
			res.get(outName).add(map);
		} else {
			ArrayList<HashMap<String, Object>> arr = new ArrayList<HashMap<String, Object>>();
			arr.add(map);
			res.put(outName, arr);
		}
	}

	private class ZXJCAdapter extends BaseAdapter {
		LayoutInflater inflater;
		ArrayList<HashMap<String, Object>> data;

		public ZXJCAdapter(ArrayList<HashMap<String, Object>> data) {
			super();
			this.data = data;
			inflater = ZxjcDetailActivity.this.getLayoutInflater();
		}

		public void changeData(ArrayList<HashMap<String, Object>> arrayList) {
			data = arrayList;
			notifyDataSetChanged();
		}

		@Override
		public int getCount() {
			return data.size();
		}

		@Override
		public Object getItem(int position) {
			return null;
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder viewHolder;
			if (convertView == null) {
				convertView = inflater.inflate(com.mapuni.android.MobileEnforcement.R.layout.zxjc_qy_data, null);
				viewHolder = new ViewHolder();
				viewHolder.liulang = (TextView) convertView.findViewById(com.mapuni.android.MobileEnforcement.R.id.liulang_01);
				viewHolder.date = (TextView) convertView.findViewById(com.mapuni.android.MobileEnforcement.R.id.time_01);

				convertView.setTag(viewHolder);
			}
			viewHolder = (ViewHolder) convertView.getTag();
			String str = data.get(position).get("MonitorTime").toString();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
			Date date = null;
			try {
				date = sdf.parse(str);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd");
			String str1 = sdf2.format(date);
			HashMap<String, Object> map = data.get(position);
			viewHolder.liulang.setText(data.get(position).get("AvgData").toString());
			viewHolder.date.setText(str1);
			return convertView;
		}

		private class ViewHolder {
			public TextView show3;
			TextView liulang, date;
		}

	}

	/**
	 * 自定义大的spinnerAdapter
	 * 
	 * @author Shangb
	 * 
	 *         create at:2014-4-30 11:09:16
	 */
	public class MapuniSpinnerAdapter extends ArrayAdapter<String> {
		private int padding = 10;// 默认内边距10dp
		private int textSize = 25;// 默认字体大小25sp
		private int textColor = Color.BLACK;// 默认字体颜色黑色

		public MapuniSpinnerAdapter(Context context, String[] objects) {
			super(context, android.R.layout.simple_spinner_item, objects);
		}

		public MapuniSpinnerAdapter(Context context, List<String> objects) {
			super(context, android.R.layout.simple_spinner_item, objects);
		}

		@Override
		public View getDropDownView(int position, View convertView, ViewGroup parent) {
			TextView text = null;
			if (convertView == null) {
				text = new TextView(getContext());
				text.setTextSize(textSize);
				text.setTextColor(textColor);
				text.setPadding(padding, padding, padding, padding);
				text.setGravity(Gravity.CENTER);
			} else {
				text = (TextView) convertView;
			}
			String item = getItem(position);
			if (item instanceof CharSequence) {
				text.setText((CharSequence) item);
			} else {
				text.setText(item.toString());
			}
			return text;
		}

	}

	public void selectDate(final View v) {

		final Button b = (Button) v;
		String date = b.getText().toString();
		int y, m, d;
		String[] arr = date.split("-");
		y = Integer.parseInt(arr[0]);
		m = Integer.parseInt(arr[1]);
		d = Integer.parseInt(arr[2]);
		DatePickerDialog dpd = new DatePickerDialog(this, new OnDateSetListener() {

			@Override
			public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
				b.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
			}
		}, y, m - 1, d);
		dpd.show();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (null != outs) {
			outs.clear();
			outs = null;
		}
	}

}
