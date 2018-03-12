package com.mapuni.android.base;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.mapuni.android.base.business.BaseObjectFactory;
import com.mapuni.android.base.interfaces.IGrid;
import com.mapuni.android.base.util.Apn;

/**
 * FileName: GridActivity.java Description: 九宫格
 * 
 * @author 柳思远
 * @Version 1.3.4
 * @Copyright 中科宇图天下科技有限公司 Create at: 2012-12-5 下午06:08:32
 */
public class GridActivity extends BaseActivity {

	/** Bundle对象 */
	private Bundle bundle;
	/** 业务类继承了IGrid接口，向上转型 */
	private IGrid businessObj;
	/** 定义一个Intent对象 */
	private Intent intent;

	/** 是否是移动办公的九宫格 */
	private Boolean ydbg;
	/** 根据包名判断是那个包内的类 */
	private String packageName = "";
	/** 用户的权限集合（目前没有用到） */
	public ArrayList<HashMap<String, Object>> authoritylist;
	/** 定义两个文件(目前没有用到) */
	File f, fil;
	// 是否添加任务
	private Boolean isAddTask = false;

	/** 添加任务的权限 */
	public String TJRW_QX = "vmob2A4B";

	// /** 滑动的Layout */
	// private SlideLayout slideLayout;
	// /** 排序的ListView */
	// private ListView sortListView;
	/** dialog中listView绑定数据 */
	public ArrayList<HashMap<String, Object>> listData;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ui_mapuni);

		RelativeLayout linearLayout = (RelativeLayout) this.findViewById(R.id.parentLayout);
		/** 获取上一个页面传过来的Intent */
		Intent it = this.getIntent();
		/** 接收数据 */
		bundle = it.getExtras();
		if (it.getBooleanExtra("isShortcut", false)) {
			String ywl = it.getStringExtra("ywl");
			packageName = it.getStringExtra("packageName");
			try {
				if (packageName.equals("")) {
					businessObj = (IGrid) BaseObjectFactory.createObject(ywl);
				} else if (packageName.equals("helper")) {
					businessObj = (IGrid) BaseObjectFactory.createHelperObject(ywl);
				} else if (packageName.equals("setting")) {
					businessObj = (IGrid) BaseObjectFactory.createSettingObject(ywl);
				} else if (packageName.equals("infoQuery")) {
					businessObj = (IGrid) BaseObjectFactory.createinfoQueryObject(ywl);
				}
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (InstantiationException e) {
				e.printStackTrace();
			}
		} else {
			/** 业务类向上转型 */
			businessObj = (IGrid) bundle.getSerializable("BusinessObj");
		}
		/** 判断是否是移动办公的九宫格 */
		ydbg = bundle.containsKey("isydbg") ? true : false;
		/** 获取九宫格的标题 */
		String titleText = businessObj.getGridTitle();

		isAddTask = bundle.getBoolean("isAddTask");
		// 添加任务
		// if(DisplayUitl.getAuthority(TJRW_QX)&&isAddTask){
		// super.isAddTask=true;
		// }
		/** 设置九宫格的标题 */
		super.SetBaseStyle(linearLayout, titleText);
		/** 是否加载标题布局 */
		boolean isShowTitle = bundle.getBoolean("IsShowTitle");
		super.setTitleLayoutVisiable(isShowTitle);
		super.setSearchButtonVisiable(isAddTask);
		// 添加任务的监听时间
		queryImg.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				/** 任务查询框 */
				if (isAddTask) {
					/*
					 * Bundle bundle = new Bundle();
					 * bundle.putBoolean("IsShowTitle", true);
					 * bundle.putString("TitleText", "任务编辑");
					 * 
					 * Intent _Intent = new Intent(GridActivity.this,
					 * TaskEditActivity.class); _Intent.putExtras(bundle);
					 * startActivity(_Intent);
					 */
				}
			}
		});

		/** 综合查询的判断已经过时 */
		if (businessObj.getGridTitle().equals("综合查询"))
			// super.setknowledgeButtonVisiable(false);
			Log.i("drawable-hdpi", "HashCode ：" + this.toString());
		GridView gridview = new LoadGridLayout(GridActivity.this, intent, businessObj, ydbg).loadGridLayout();
		/** 过去总布局的中间布局 */
		LinearLayout queryLayout = (LinearLayout) this.findViewById(R.id.middleLayout);
		/** 将定义的九宫格布局放入中间布局中 */
		queryLayout.addView(gridview);
		// initListData();// 初始化dialog中listView上的数据
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		Log.i("tag", "onSaveInstanceState");
		if (outState == null) {
			Log.i("tag", "outState空");
		}
		super.onSaveInstanceState(outState);
	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		Log.i("tag", "savedInstanceState");
		if (savedInstanceState == null) {
			Log.i("tag", "savedInstanceState空");
		}
		super.onRestoreInstanceState(savedInstanceState);
	}

	/**
	 * 按撤销键弹出推出对话框
	 */
	@Override
	public boolean dispatchKeyEvent(KeyEvent event) {
		if (event.getKeyCode() == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN && bundle.getBoolean("IsMain")) {
			Builder dialog = new AlertDialog.Builder(this);
			dialog.setIcon(R.drawable.base_icon_mapuni_white);
			dialog.setTitle("系统退出");
			dialog.setMessage("确定要退出系统吗？");
			dialog.setPositiveButton("是", new btnExitListener());
			dialog.setNegativeButton("否", null);
			dialog.show();
		}
		return super.dispatchKeyEvent(event);
	}

	/**
	 * FileName: GridActivity.java Description: 退出系统
	 * 
	 * @author 柳思远
	 * @Version 1.3.4
	 * @Copyright 中科宇图天下科技有限公司 Create at: 2012-12-6 上午09:43:32
	 */
	private class btnExitListener implements android.content.DialogInterface.OnClickListener {
		@Override
		public void onClick(DialogInterface arg0, int arg1) {
			if (Build.VERSION.SDK_INT <= 10 && Global.getGlobalInstance().isUpdataapn()) {
				Apn apn = new Apn(GridActivity.this);
				apn.setReturnAPN();
			}
			myExit();
		}
	}

	/*
	 * @Override public void queryDate() { super.queryDate(); //Intent intent =
	 * new Intent(GridActivity.this, GlobalSearchActivity.class);
	 * startActivity(intent); }
	 * 
	 * 
	 * /** Description: 根据图片的名字（不要后缀）来获取Bitpmap
	 * 
	 * @param name 图片的名字
	 * 
	 * @return 返回一个Bitpmap Bitmap
	 * 
	 * @author 柳思远 Create at: 2012-12-6 上午09:4s3:54
	 */
	public Bitmap getRes(String name) {
		ApplicationInfo appInfo = getApplicationInfo();
		int resID = getResources().getIdentifier(name, "drawable", appInfo.packageName);
		return BitmapFactory.decodeResource(getResources(), resID);
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		Log.i("wang", "主界面销毁！！！");
	}

}
