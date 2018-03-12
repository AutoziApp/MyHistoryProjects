package com.mapuni.android.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.mapuni.android.MobileEnforcement.R;
import com.mapuni.android.business.RWXX;
import com.mapuni.android.business.SWDB;
import com.mapuni.android.business.TZGGXX;
import com.mapuni.android.oa.TZGGDetailActivity;
import com.mapuni.android.taskmanager.TaskInformationActivity;
import com.mapuni.android.widget.MobileEnforcementWidget;

/**
 * FileName: WidgetService.java Description:Widget支持服务 
 * <li>获取当前用户待执行任务
 * <li>获取当前三天内通知公告
 * <li>获取当前用户待办公文
 * @author 柳思远
 * @Version 1.3.4
 * @Copyright 中科宇图天下科技有限公司 Create at: 2012-12-3 上午11:03:23
 */
public class WidgetService extends Service {
	/**组件*/
	public RemoteViews remoteViews;// RemoteView对象
	public ComponentName thisWidget; // 组件名
	public AppWidgetManager manager; // AppWidget管理器
	
	/**当前状态常量值*/
	
	private  final int STATE_RWGL = 0;
	private  final int STATE_TZGG = 1;
	private  final int STATE_DBGL = 2;
	private  final String[] titles = new String[] { "任务管理", "通知公告", "在线监测", "待办管理" };
	
	private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
	private int current_state = 0;
	
	// 数据集合
	private ArrayList<HashMap<String, Object>> tzggDataList = new ArrayList<HashMap<String, Object>>();
	private ArrayList<HashMap<String, Object>> rwxxDataList = new ArrayList<HashMap<String, Object>>();
	private ArrayList<HashMap<String, Object>> dbglDataList = new ArrayList<HashMap<String, Object>>();

	private String userID = "";
	/** 分页 */
	private  final int pageSize = 3;
	private int dataSize = 0;// 1,2,3
	private int pageCount = 1;// 1,2,3
	private int currentPage = 0;// 1,2,3
	// private int dataSize = 0;
	private  final String TAG = "WidgetService";

	@Override
	public void onCreate() {
		remoteViews = new RemoteViews(getPackageName(),
				R.layout.mobileenforcement_widget);
		thisWidget = new ComponentName(this, MobileEnforcementWidget.class);
		manager = AppWidgetManager.getInstance(this);
		super.onCreate();
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		String actionStr = intent.getAction();
		Log.i(TAG, "onStartCommand" + actionStr);

		// Update Data
		getData();
		if (MobileEnforcementWidget.CHANGE_ACTION.equals(actionStr)) {
			changeSubject();
		} else if (MobileEnforcementWidget.UP_ACTION.equals(actionStr)) {
			onUpAction(current_state);
		} else if (MobileEnforcementWidget.DOWN_ACTION.equals(actionStr)) {
			onDownAction(current_state);
		} else if (MobileEnforcementWidget.TEXT1_ACTION.equals(actionStr)) {
			onTextAction(0);
		} else if (MobileEnforcementWidget.TEXT2_ACTION.equals(actionStr)) {
			onTextAction(1);
		} else if (MobileEnforcementWidget.TEXT3_ACTION.equals(actionStr)) {
			onTextAction(2);
		}
		return super.onStartCommand(intent, flags, startId);
	}

	/**
	 * 文本选项
	 * 
	 * @param index
	 */
	private void onTextAction(int index) {
		SharedPreferences auto_sp = getSharedPreferences("setting",
				MODE_PRIVATE);
		if (!auto_sp.getBoolean("auto_login", false)) {
			Toast.makeText(this, "请先设置自动登录！", 0).show();
			//Intent intent = new Intent(this, FlashActivity.class);
			//intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			//startActivity(intent);
			return;
		}

		ArrayList<HashMap<String, Object>> data = getStateData(current_state);
		String key = "";
		String currentID = "";
		Intent intent = new Intent();
		Bundle bundle;
		if (current_state == STATE_RWGL) {
			// 【任务】
			key = "guid";
			currentID = (String) data.get(currentPage * 3 + index).get(key);
			Log.i(TAG, "当前编号" + currentID);
			intent = new Intent(this, TaskInformationActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			bundle = new Bundle();
			RWXX rwxx = new RWXX();
			rwxx.setCurrentID(currentID);
			bundle.putSerializable("BusinessObj", rwxx);
			bundle.putString("currentTaskID", currentID);
			bundle.putBoolean("editTask", true);
			intent.putExtra("isFromWidget", true);
			bundle.putString("currentTaskID", currentID);
			intent.putExtras(bundle);
			startActivity(intent);
		} else if (current_state == STATE_TZGG) {
			// data = tzggDataList;
			key = "id";
			currentID = (String) data.get(currentPage * 3 + index).get(key);
			Log.i(TAG, "当前编号" + currentID);
			Bundle nextbundle = new Bundle();
			TZGGXX tzggxx = new TZGGXX();
			tzggxx.setCurrentID(currentID);
			nextbundle.putSerializable("BusinessObj", tzggxx);
			nextbundle.putBoolean("IsShowTitle", true);
			intent.putExtras(nextbundle);
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			intent.putExtra("isFromWidget", true);
			intent.setClass(this, TZGGDetailActivity.class);
			startActivity(intent);
			return;
		} else if (current_state == STATE_DBGL) {
			key = "id";
			currentID = (String) data.get(currentPage * 3 + index).get(key);

		}
		Log.i(TAG, "编号：" + currentID);

	}

	/**
	 * 更新数据
	 */
	private void getData() {
		// 1.获取集合
		userID = getUserID();
		/*rwxxDataList = new RWXX().getTaskByUserIDandStatus(userID,
				RWXX.RWZT_WATE_EXECUTION);*/
		tzggDataList = new TZGGXX().getLastThreeDaysTZGGXX();
		dbglDataList = new SWDB().getDBGW(userID);
		// 2.初始化分页
		// int dataSize = ((current_state == STATE_RWGL) ? rwxxDataList :
		// tzggDataList).size();
		ArrayList<HashMap<String, Object>> data = getStateData(current_state);
		if (data == null) {
			return;
		}
		dataSize = data.size();
		// pageCount = dataSize / pageSize + 1;
		pageCount = (int) Math.ceil((dataSize) / 3.0);
		Log.i(TAG, "用户" + userID + "\t共有：" + dataSize + "条数据, 分页数：" + pageCount);
	}

	/**
	 * 获取任务信息
	 * 
	 * @return
	 */
	private String getUserID() {
		if (userID == null || userID.equals("")) {
			SharedPreferences sp = getSharedPreferences("ServiceInfo",
					MODE_WORLD_WRITEABLE);
			userID = sp.getString("userId", "yutu");
			Log.i(TAG,
					"Get UserID From SharedPreference : "
							+ (userID.equals("yutu") ? "NO DATA" : userID));
		}
		return userID;
	}

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	/**
	 * 向下翻页
	 */
	private void onDownAction(int state) {
		ArrayList<HashMap<String, Object>> data = getStateData(state);
		String key = getStateKey(state);

		int size = data.size();
		int imgUpId = R.drawable.widget_arrow_up;
		int imgDownId = R.drawable.widget_arrow_down;
		if (currentPage + 1 == pageCount) {
			// 不让翻页
			Log.i(TAG, "Not DownAction");
			imgDownId = R.drawable.widget_arrow_down_disable;
		} else {
			currentPage++;
			Log.i(TAG, "on DownAction:" + currentPage);
			int begin = currentPage * pageSize;
			int end = (currentPage + 1) * pageSize - 1;
			if (end >= size)
				end = size - 1;
			String text1 = "暂无数据……";
			String text2 = "";
			String text3 = "";
			Log.i(TAG, "Begin at :" + begin + "\tEnd at : " + end);

			if (end - begin == 0)
				text1 = (String) data.get(begin).get(key);
			imgDownId = R.drawable.widget_arrow_down_disable;
			if (end - begin == 1) {
				text1 = (String) data.get(begin).get(key);
				text2 = (String) data.get(begin + 1).get(key);
				imgDownId = R.drawable.widget_arrow_down_disable;
			}
			if (end - begin == 2) {
				text1 = (String) data.get(begin).get(key);
				text2 = (String) data.get(begin + 1).get(key);
				text3 = (String) data.get(begin + 2).get(key);
				imgDownId = R.drawable.widget_arrow_down;
			}
			if (currentPage + 1 == pageCount) {
				imgDownId = R.drawable.widget_arrow_down_disable;
			}
			remoteViews.setTextViewText(R.id.widget_middle_text1, text1);
			remoteViews.setTextViewText(R.id.widget_middle_text2, text2);
			remoteViews.setTextViewText(R.id.widget_middle_text3, text3);
			remoteViews.setImageViewResource(R.id.widget_bottom_up, imgUpId);
			remoteViews
					.setImageViewResource(R.id.widget_bottom_down, imgDownId);
		}
		manager.updateAppWidget(thisWidget, remoteViews);
	}

	/**
	 * 获取当前Key
	 * 
	 * @param state
	 * @return
	 */
	private String getStateKey(int state) {
		String key = "";
		if (state == STATE_RWGL) {
			key = "rwmc";
		} else if (state == STATE_TZGG) {
			key = "timing";
		} else if (state == STATE_DBGL) {
			key = "topic";
		}
		return key;
	}

	/**
	 * 获取当前的数据
	 * 
	 * @param state
	 * @return
	 */
	private ArrayList<HashMap<String, Object>> getStateData(int state) {
		ArrayList<HashMap<String, Object>> data = null;
		if (state == STATE_RWGL) {
			data = rwxxDataList;
		} else if (state == STATE_TZGG) {
			data = tzggDataList;
		} else if (state == STATE_DBGL) {
			data = dbglDataList;
		}
		return data;
	}

	/**
	 * 向上翻页
	 */
	private void onUpAction(int state) {
		ArrayList<HashMap<String, Object>> data = getStateData(state);
		String key = getStateKey(state);

		int size = data.size();
		int imgUpId = R.drawable.widget_arrow_up;
		int imgDownId = R.drawable.widget_arrow_down;
		if (currentPage == 0) {
			// 不让翻页
			Log.i(TAG, "The first Page, Not UpAction");
			imgUpId = R.drawable.widget_arrow_up_disable;
		} else {
			currentPage--;
			Log.i(TAG, "on UpAction:" + currentPage);
			int begin = currentPage * pageSize;
			int end = (currentPage + 1) * pageSize - 1;
			if (end >= size)
				end = size - 1;
			String text1 = "暂无数据……";
			String text2 = "";
			String text3 = "";
			Log.i(TAG, "Begin at :" + begin + "\tEnd at : " + end + "\tsize:"
					+ size);
			if (end - begin == 0)
				text1 = (String) data.get(begin).get(key);
			imgUpId = R.drawable.widget_arrow_up_disable;
			imgDownId = R.drawable.widget_arrow_down_disable;
			if (end - begin == 1) {
				text1 = (String) data.get(begin).get(key);
				text2 = (String) data.get(begin + 1).get(key);
				imgUpId = R.drawable.widget_arrow_up_disable;
				imgDownId = R.drawable.widget_arrow_down_disable;
			}
			if (end - begin == 2) {
				text1 = (String) data.get(begin).get(key);
				text2 = (String) data.get(begin + 1).get(key);
				text3 = (String) data.get(begin + 2).get(key);
				imgUpId = R.drawable.widget_arrow_up;
				imgDownId = R.drawable.widget_arrow_down;
			}
			if (begin == 0)
				imgUpId = R.drawable.widget_arrow_up_disable;
			if (end == size) {
				imgDownId = R.drawable.widget_arrow_down_disable;
			}
			remoteViews.setTextViewText(R.id.widget_middle_text1, text1);
			remoteViews.setTextViewText(R.id.widget_middle_text2, text2);
			remoteViews.setTextViewText(R.id.widget_middle_text3, text3);
			remoteViews.setImageViewResource(R.id.widget_bottom_up, imgUpId);
			remoteViews
					.setImageViewResource(R.id.widget_bottom_down, imgDownId);
		}
		manager.updateAppWidget(thisWidget, remoteViews);
	}

	/**
	 * 显示条目
	 */
	int subjectIndex = 0;

	private void changeSubject() {
		Log.i(MobileEnforcementWidget.TAG, "changeSubject");
		subjectIndex++;
		current_state = subjectIndex % 3;
		remoteViews.setTextViewText(R.id.widget_head_title,
				titles[current_state]);
		remoteViews.setTextViewText(R.id.widget_head_time,
				dateFormat.format(new Date()));
		currentPage = 1;
		onUpAction(current_state);
		manager.updateAppWidget(thisWidget, remoteViews);
	}

}
