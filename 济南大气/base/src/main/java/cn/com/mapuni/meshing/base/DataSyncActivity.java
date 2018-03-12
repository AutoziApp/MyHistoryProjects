package cn.com.mapuni.meshing.base;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.AlertDialog;
import android.app.NotificationManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import cn.com.mapuni.meshing.base.R;
import cn.com.mapuni.meshing.base.business.BaseDataSync;
import cn.com.mapuni.meshing.base.util.DisplayUitl;
import cn.com.mapuni.meshing.base.util.ExceptionManager;
import cn.com.mapuni.meshing.base.util.LogUtil;
import cn.com.mapuni.meshing.netprovider.Net;


/**
 * FileName: DataSyncActivity.java Description:数据同步列表展示
 * 
 * @author Administrator
 * @Version 1.3.4
 * @Copyright 中科宇图天下科技有限公司<br>
 *            Create at: 2012-12-4 下午04:11:03
 */
public class DataSyncActivity extends ListActivity {

	public static CheckBox chkChoiceAll;
	/** 日志记录标志 */
	private final String TAG = "DataSyncActivity";

	/** 同步全部的权限 */
	private final String TBQBQX = "vmob4A";

	/** 数据同步业务类 */
	private BaseDataSync dataSync = null; // 业务层

	/** 是否同步任务信息 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 获取同步数据存储对象
		dataSync = (BaseDataSync) businessObj;

		// 获取顶部父容器
		LinearLayout topLayout = (LinearLayout) this.findViewById(R.id.ui_mapuni_divider);
		topLayout.setVisibility(View.VISIBLE);

		// 添加表头布局
		topLayout.addView(addTableHead());

		// 如果有消息则取消提示
		notificationCancel();

		try {
			// 重新填充数据源
			// Bundle bundle = Global.getGlobalInstance().getBundle();
			// ArrayList<HashMap<String, Object>> dataList =
			// dataSync.getDataList(this);
			ArrayList<HashMap<String, Object>> dataList = dataSync.getTablenameCN(DataSyncActivity.this);
			HashMap<String, Object> style = dataSync.getStyleList(this);

			LoadList(bundle, dataList, style);
		} catch (IOException e) {
			ExceptionManager.WriteCaughtEXP(e, "DataSyncActivity");
			LogUtil.e(TAG,"//"+ e.getMessage());
		}
	}

	@Override
	protected void onStart() {
		super.onStart();
		// 如果有消息则取消提示
		notificationCancel();
	}

	/**
	 * Description:取消状态栏通知
	 * 
	 * @author Administrator<br>
	 *         Create at: 2012-12-4 下午04:28:58
	 */
	private void notificationCancel() {
		NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
		manager.cancel(0);
	}

	/**
	 * Description: 添加表头布局 RelativeLayout Button(syncAll) Button(syncLastest)
	 * CheckBox(chkChoiceAll) RelativeLayout
	 * 
	 * @return
	 * @author Administrator<br>
	 *         Create at: 2012-12-4 下午04:11:24
	 */
	private RelativeLayout addTableHead() {
		int syncAllId = 224522;
		int chkChoiceAllId = 224523;

		RelativeLayout tableHead = new RelativeLayout(this);
		tableHead.setBackgroundDrawable(getResources().getDrawable(R.drawable.base_bg_title_datasync));

		RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		layoutParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
		layoutParams.addRule(RelativeLayout.CENTER_VERTICAL);
		layoutParams.setMargins(20, 10, 10, 10);

		Button syncAll = new Button(this);
		syncAll.setText("同步全部");
		syncAll.setTextColor(Color.BLACK);
		syncAll.setTextSize(20.0f);
		syncAll.setId(syncAllId);
		Drawable dSyncAll = getResources().getDrawable(R.drawable.base_bg_button_datasync);
		dSyncAll.setBounds(5, 0, 5, 0);
		syncAll.setBackgroundDrawable(dSyncAll);
		syncAll.setPadding(15, 15, 15, 15);
		syncAll.setGravity(Gravity.CENTER);
		syncAll.setOnClickListener(syncAllBtnListener);
		if (DisplayUitl.getAuthority(TBQBQX))
			tableHead.addView(syncAll, layoutParams);

		layoutParams = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		layoutParams.addRule(RelativeLayout.CENTER_VERTICAL);
		layoutParams.addRule(RelativeLayout.RIGHT_OF, syncAllId);
		layoutParams.setMargins(10, 10, 10, 10);

		Button syncLastest = new Button(this);
		syncLastest.setTextSize(20.0f);
		Drawable dSyncLastest = getResources().getDrawable(R.drawable.base_bg_button_datasync);
		dSyncLastest.setBounds(5, 0, 5, 0);
		syncLastest.setBackgroundDrawable(dSyncAll);
		syncLastest.setText("同步最新");
		syncLastest.setTextColor(Color.BLACK);
		syncLastest.setPadding(15, 15, 15, 15);
		syncLastest.setGravity(Gravity.CENTER);
		syncLastest.setOnClickListener(syncLastestBtnListener);

		tableHead.addView(syncLastest, layoutParams);

		layoutParams = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		layoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
		layoutParams.addRule(RelativeLayout.CENTER_VERTICAL);
		layoutParams.setMargins(0, 0, 8, 0);

		chkChoiceAll = new CheckBox(this);
		chkChoiceAll.setId(chkChoiceAllId);
		// chkChoiceAll.setOnCheckedChangeListener(chkChoiceAllListener);
		chkChoiceAll.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				boolean isChecked = chkChoiceAll.isChecked();
				ListActivity.IS_CHECKED = isChecked;
				adapter.notifyDataSetChanged();

				chkChoice.clear();

				for (int i = 0; i < adapter.getCount(); i++) {
					@SuppressWarnings("unchecked")
					HashMap<String, Object> dataRow = (HashMap<String, Object>) adapter.getItem(i);

					String id = dataRow.get(dataSync.GetKeyField()).toString();
					if (isChecked && !chkChoice.contains(id)) {
						chkChoice.add(id);
					} else {
						break;
					}
				}
			}
		});

		tableHead.addView(chkChoiceAll, layoutParams);

		layoutParams = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		layoutParams.addRule(RelativeLayout.CENTER_VERTICAL);
		layoutParams.addRule(RelativeLayout.LEFT_OF, chkChoiceAllId);
		layoutParams.setMargins(0, 0, 2, 0);

		TextView choiceAllTxt = new TextView(this);
		choiceAllTxt.setText("全选:");
		choiceAllTxt.setTextSize(11.0f);
		choiceAllTxt.setTextColor(Color.BLACK);

		tableHead.addView(choiceAllTxt, layoutParams);

		return tableHead;
	}

	/**
	 * 同步全部数据按钮点击事件
	 */
	private OnClickListener syncAllBtnListener = new OnClickListener() {
		@Override
		public void onClick(View v) {

			if (!Net.checkNet(DataSyncActivity.this)) {
				// Toast.makeText(DataSyncActivity.this, "对不起，网络不通无法同步！",
				// Toast.LENGTH_LONG).show();
				Net.OpenWirelessSettings(DataSyncActivity.this);

				return;
			}
			if (Global.IsDataSync) {
				Toast.makeText(DataSyncActivity.this, "当前同步尚未完成，请稍候", 1).show();
				return;
			}

			doSync(false);
		}
	};

	/**
	 * 同步最新数据按钮点击事件
	 */
	private OnClickListener syncLastestBtnListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			if (!Net.checkNet(DataSyncActivity.this)) {
				// Toast.makeText(DataSyncActivity.this, "对不起，网络不通无法同步！",
				// Toast.LENGTH_LONG).show();
				Net.OpenWirelessSettings(DataSyncActivity.this);

				return;
			}
			if (Global.IsDataSync) {
				Toast.makeText(DataSyncActivity.this, "当前同步尚未完成，请稍候", 1).show();
				return;
			}

			doSync(true);
		}
	};

	/**
	 * 全选功能
	 */
	/*
	 * private OnCheckedChangeListener chkChoiceAllListener = new
	 * OnCheckedChangeListener() {
	 * 
	 * @Override public void onCheckedChanged(CompoundButton buttonView, boolean
	 * isChecked) {
	 * 
	 * ListActivity.IS_CHECKED = isChecked; adapter.notifyDataSetChanged();
	 * 
	 * chkChoice.clear();
	 * 
	 * for (int i = 0; i < adapter.getCount(); i++) {
	 * 
	 * @SuppressWarnings("unchecked") HashMap<String, Object> dataRow =
	 * (HashMap<String, Object>) adapter.getItem(i);
	 * 
	 * String id = dataRow.get(dataSync.GetKeyField()).toString(); if (isChecked
	 * && !chkChoice.contains(id)) { chkChoice.add(id); } else { break; } } } };
	 */

	/**
	 * Description:执行同步全部数据或同步最新数据操作
	 * 
	 * @param updateOrFetchAllData
	 * @author Administrator<br>
	 *         Create at: 2012-12-4 下午04:12:17
	 */
	private void doSync(final boolean updateOrFetchAllData) {

		final Object[] objs = chkChoice.toArray();
		String choice = chkChoice.toString();
		if (objs.length < 1) {
			Toast.makeText(this, "请选择要操作的表", Toast.LENGTH_SHORT).show();
		} // "0"是 污染源基本信息       "4" 是 执法文书   	"5" 是模版信息|| choice.contains("4") || choice.contains("5")
		else if(choice.contains("[0") ){

			new AlertDialog.Builder(DataSyncActivity.this).setTitle("信息提示").setMessage("同步任务信息，系统会舍弃未上传的任务和执法记录，如还有未上传的记录，请选择 \"取消\"")
					.setPositiveButton("确定", new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int whichButton) {
							syncDate(objs, updateOrFetchAllData);
							return;
						}
					})

					.setNegativeButton("取消", new DialogInterface.OnClickListener() {

						public void onClick(DialogInterface dialog, int which) {
							return;

						}
					}).show();

		}else{
			syncDate(objs, updateOrFetchAllData);
		}
	}

	private void syncDate(Object[] objs, boolean updateOrFetchAllData) {
		Global.IsDataSync = true;
		StringBuilder tablesSB = new StringBuilder();

		ArrayList<HashMap<String, Object>> data = dataSync.getTablenameCN(DataSyncActivity.this);
		for (Object obj : objs) {
			String id = obj.toString();
			for (int i = 0; i < data.size(); i++) {
				HashMap<String, Object> dataRow = (HashMap<String, Object>) data.get(i);
				String tablename = dataRow.get("tablename").toString();
				if (id.trim().equals(dataRow.get(dataSync.GetKeyField()).toString().trim())) {
					Object objTable = dataRow.get(BaseDataSync.SYNC_TABLE_REFLECT);
					if (objTable != null && !"".equals(objTable)) {
						tablesSB.append(objTable.toString() + ",");
					}
				}
			}

		}
		if (tablesSB.indexOf(",") != -1) {
			tablesSB = new StringBuilder(tablesSB.substring(0, tablesSB.length() - 1));

			//过滤掉重复的表 避免重复同步同一张表
			String string[] = tablesSB.toString().split(",");
			List<String> temp = new ArrayList<String>();
			tablesSB = new StringBuilder();
			for (int i = 0; i < string.length; i++) {
				if(!temp.contains(string[i])) {
					temp.add(string[i]);
					tablesSB.append(string[i] + ",");
				}
			}
		}
		
		LogUtil.v(TAG, (updateOrFetchAllData ? "更新数据" : "全部数据") + "同步表： --> " + tablesSB.toString());

		// 调用服务下载
		Intent downloadIntent = new Intent(DataSyncActivity.this, DownloadActivity.class);
		Bundle bundle = new Bundle();
		bundle.putString("tables", tablesSB.toString());
		bundle.putSerializable("BusinessObj", dataSync);
		bundle.putString("BusinessType", "DataSync");
		bundle.putString("notificationTile", "数据同步");
		bundle.putBoolean("updateOrFetchAllData", updateOrFetchAllData);
		downloadIntent.putExtras(bundle);

		startActivity(downloadIntent);
	}
}
