package com.mapuni.android.taskmanager;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.UUID;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;

import com.mapuni.android.base.BaseActivity;
import com.mapuni.android.base.Global;
import com.mapuni.android.base.R;
import com.mapuni.android.base.util.DisplayUitl;
import com.mapuni.android.business.RWXX;
import com.mapuni.android.dataprovider.JsonHelper;
import com.mapuni.android.dataprovider.SqliteUtil;
import com.mapuni.android.enforcement.SiteEvidenceActivity;

public class TaskSiteLawStartActivity extends BaseActivity {
	LayoutInflater inflater;
	/** 底部布局 */
	LinearLayout bottom;
	/** 窗体view */
	LinearLayout parentLayout;
	/** 中间view */
	LinearLayout middleLayout;
	RelativeLayout parentview;
	protected String CurrentTaskID;
	
	 protected RWXX rwxx;
	/** 打印按钮 */
	private Button button;
	/** 选择文书 */
	private Spinner spinner;
	private final Handler mhandler = new Handler();

	private String RWBH;

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		inflater = LayoutInflater.from(this);
		parentLayout = (LinearLayout) inflater.inflate(R.layout.ui_mapuni, null);
		parentview = (RelativeLayout) parentLayout.findViewById(R.id.parentLayout);
		middleLayout = (LinearLayout) parentLayout.findViewById(R.id.middleLayout);
		setContentView(parentLayout);
		SetBaseStyle(parentview, "现场执法");
		rwxx = (RWXX) getIntent().getExtras().get("BusinessObj");
		CurrentTaskID = rwxx.getCurrentID();
		RWBH = rwxx.getRWBH(CurrentTaskID);
		loadBottomView();
		loadMiddleView();
	}

	/**
	 * 加载底部布局
	 */
	private void loadBottomView() {
		bottom = (LinearLayout) parentLayout.findViewById(R.id.bottomLayout);
		bottom.setVisibility(View.VISIBLE);
		int[] arr = DisplayUitl.getMobileResolution(this);
		int width = arr[0] / 2;
		Button taskupload = new Button(this);
		taskupload.setBackgroundResource(com.mapuni.android.MobileEnforcement.R.drawable.btn_shap);
		taskupload.setText("结果上传");
		taskupload.setTextColor(android.graphics.Color.WHITE);

		taskupload.setVisibility(View.VISIBLE);
		taskupload.getPaint().setFakeBoldText(true);// 加粗
		taskupload.setLayoutParams(new LinearLayout.LayoutParams(width, LinearLayout.LayoutParams.FILL_PARENT, 0));

		Button evidence = new Button(this);
		evidence.setBackgroundResource(com.mapuni.android.MobileEnforcement.R.drawable.btn_shap);
		evidence.setText("现场取证");
		evidence.setTextColor(android.graphics.Color.WHITE);
		evidence.setWidth(width);
		evidence.setVisibility(View.VISIBLE);
		evidence.getPaint().setFakeBoldText(true);// 加粗
		evidence.setLayoutParams(new LinearLayout.LayoutParams(width, LinearLayout.LayoutParams.FILL_PARENT, 0));
		ImageView splite = new ImageView(this);
		splite.setScaleType(ScaleType.FIT_XY);
		splite.setLayoutParams(new LinearLayout.LayoutParams(2, LinearLayout.LayoutParams.FILL_PARENT));
		splite.setBackgroundResource(com.mapuni.android.MobileEnforcement.R.drawable.bg_bottombutton_splite);
		bottom.addView(evidence);
		bottom.addView(splite);
		bottom.addView(taskupload);

		evidence.setOnClickListener(new OnClickListener() {// 取证点击事件
			@Override
			public void onClick(View arg0) {
				// String taskID=intent.getStringExtra("rwGUID");
				Intent intent = new Intent(TaskSiteLawStartActivity.this, SiteEvidenceActivity.class);
				intent.putExtra("currentTaskID", rwxx.getRWBH(CurrentTaskID));
				startActivity(intent);

			}
		});

		taskupload.setOnClickListener(new OnClickListener() {// 任务上传
					@Override
					public void onClick(View arg0) {
						// rwxx.isforOneEntpri=false;
						// rwxx.uploadTask(CurrentTaskID,
						// TaskSiteLawStartActivity.this,"");
					}
				});

	}

	/**
	 * 加载中间布局
	 */
	private void loadMiddleView() {
		LinearLayout middle_layout = (LinearLayout) inflater.inflate(com.mapuni.android.MobileEnforcement.R.layout.tasksitelawstart_middlelayout, null);
		WebView webView = (WebView) middle_layout.findViewById(com.mapuni.android.MobileEnforcement.R.id.tasksitelawstart_webview);
		webView.loadUrl("file:///android_asset/kcbl.html");

		init(webView);
		spinner = (Spinner) middle_layout.findViewById(com.mapuni.android.MobileEnforcement.R.id.tasksitelawstart_sppinner);
		button = (Button) middle_layout.findViewById(com.mapuni.android.MobileEnforcement.R.id.tasksitelawstart_button);
		ArrayList<String> spinnerlist = new ArrayList<String>();
		spinnerlist.add("天津市环境保护局现场检查勘察笔录");
		ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(TaskSiteLawStartActivity.this, android.R.layout.simple_spinner_item, spinnerlist);
		arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner.setAdapter(arrayAdapter);
		middleLayout.addView(middle_layout);
	}

	private void init(WebView webView) {
		WebSettings ws = webView.getSettings();
		ws.setBuiltInZoomControls(true);
		ws.setJavaScriptEnabled(true);
		ws.setUseWideViewPort(true);
		ws.setLoadWithOverviewMode(true);
		webView.setWebViewClient(new WebViewClient());
		webView.addJavascriptInterface(new Object() {
			public void clickOnAndroid(final String str) {
				mhandler.post(new Runnable() {
					@Override
					public void run() {
					}
				});
			}

			// js调java的方法 查询数据库,把内容返回给web界面
			public String mydata(String t) {

				String result = "";
				String sql = "select * from V_ZFWS_KCBL where TaskId='" + RWBH + "'";
				ArrayList<HashMap<String, Object>> data = SqliteUtil.getInstance().queryBySqlReturnArrayListHashMap(sql);
				if (data != null && data.size() > 0) { // 数据库中有数据--》有填写过笔录

					result = getJsonString(data);
				} else {// 第一次打开勘查笔录
					HashMap<String, Object> conditions = new HashMap<String, Object>();
					conditions.put("RWBH", RWBH);
					String qydm = "";
					ArrayList<HashMap<String, Object>> qyList = SqliteUtil.getInstance().getList("T_YDZF_RWXX", conditions);
					if (qyList != null && qyList.size() > 0) {
						qydm = qyList.get(0).get("qydm").toString();
					}
					ArrayList<String> TaskExecutorId = rwxx.getTaskExecutorId(RWBH);
					String SurveyPeopleID = "";
					for (String str : TaskExecutorId) {
						if (!str.equals(Global.getGlobalInstance().getUserid())) {
							SurveyPeopleID = str;
							break;
						}
					}
					String guid = UUID.randomUUID().toString();
					String querysql = "INSERT INTO T_ZFWS_KCBL (Guid,TaskId,EntCode,RecordPeopleID,SurveyPeopleID,SurveyStartDate)VALUES " + "(" + "'" + guid + "'" + "," + "'"
							+ RWBH + "','" + qydm + "','" + Global.getGlobalInstance().getUserid() + "','" + SurveyPeopleID + "','" + Global.getGlobalInstance().getDate() + "'"
							+ ")";

					SqliteUtil.getInstance().execute(querysql);
					data = SqliteUtil.getInstance().queryBySqlReturnArrayListHashMap(sql);
					if (data != null && data.size() > 0) {
						result = getJsonString(data);
					}

				}

				return result;
			}
		}, "demo");

	}

	public String getJsonString(ArrayList<HashMap<String, Object>> data) {
		// 数据库中有数据--》有填写过笔录
		String result = "";
		String startdate = "surveystartdate";
		String enddate = "surveyenddate";
		HashMap<String, Object> map = data.get(0);
		String startdatevalue = map.get(startdate).toString();// 得到笔录开始时间
		Date date = DisplayUitl.ParseDate(startdatevalue);
		map.put("surveystartdate_year", date.getYear());
		map.put("surveystartdate_month", date.getMonth());
		map.put("surveystartdate_day", date.getDay());
		map.put("surveystartdate_hours", date.getHours());
		map.put("surveystartdate_minutes", date.getMinutes());
		String enddatevalue = map.get(enddate).toString();// 得到笔录结束时间

		if (!enddatevalue.equals("")) {
			date = DisplayUitl.ParseDate(enddatevalue);
			map.put("surveyenddate_year", date.getYear());
			map.put("surveyenddate_month", date.getMonth());
			map.put("surveyenddate_day", date.getDay());
			map.put("surveyenddate_hours", date.getHours());
			map.put("surveyenddate_minutes", date.getMinutes());
		} else {
			map.put("surveyenddate_year", "");
			map.put("surveyenddate_month", "");
			map.put("surveyenddate_day", "");
			map.put("surveyenddate_hours", "");
			map.put("surveyenddate_minutes", "");
		}

		/*
		 * map.remove(startdate); map.remove(enddate);
		 */
		ArrayList<HashMap<String, Object>> newData = new ArrayList<HashMap<String, Object>>();
		newData.add(map);
		result = JsonHelper.listToJSON(newData);
		return result;

	}

}
