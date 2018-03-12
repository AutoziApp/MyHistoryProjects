package cn.com.mapuni.meshing.activity;

import cn.com.mapuni.meshing.activity.gis.MapTdtFragment;
import cn.com.mapuni.meshing.activity.wd_activity.WdFragment;
import com.example.meshing.R;
import com.mapuni.android.base.util.DisplayUitl;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

public class NewMainActivity extends Activity implements
		OnCheckedChangeListener {
	private RadioGroup rg_main;// 选项按钮
	private RadioButton rb_xc;// 巡查按钮选项
	private RadioButton rb_db;// 巡查按钮选项
	/**
	 * 用于对Fragment进行管理
	 */
	private FragmentManager fragmentManager;
	/**
	 * 用于展示用户信息的Fragment
	 */
	private WdFragment wdFragment;
	/**
	 * 用于展示地图信息的Fragment
	 */
	private MapTdtFragment mapTdtFragment;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_new_main);
		fragmentManager = getFragmentManager();
		initView();
		initData();
	}

	/*
	 * 初始化控件
	 */
	private void initView() {
		rg_main = (RadioGroup) findViewById(R.id.rg_main);
		rg_main.setOnCheckedChangeListener(this);
		rb_xc = (RadioButton) findViewById(R.id.rb_xc);
		rb_db = (RadioButton) findViewById(R.id.rb_db);
	}

	/*
	 * 初始化数据
	 */
	private void initData() {
		// 第一次启动时选中第0个tab
		//不具有巡检功能的角色屏蔽巡检页面
		String organization_code = DisplayUitl.readPreferences(this, "lastuser", "organization_code");
		String havePatrolRole = DisplayUitl.readPreferences(this, "lastuser", "havePatrolRole");
		String haveAdminRole = DisplayUitl.readPreferences(this, "lastuser", "haveAdminRole");
		String haveLiaisonRole = DisplayUitl.readPreferences(this, "lastuser", "haveLiaisonRole");
		if ((!"1".equals(havePatrolRole) && !"1".equals(haveAdminRole) && !"1".equals(haveLiaisonRole))
				|| (organization_code.length() == 10)) {
			rb_xc.setChecked(true);
		} else {
			rb_xc.setVisibility(View.GONE);
			rb_db.setChecked(true);
		}
	}

	int checkedId=-1;
	@Override
	protected void onRestart() {
		// TODO Auto-generated method stub
		super.onRestart();
		if(-1!=checkedId){
			rg_main.check(checkedId);
		}
	}
	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		this.checkedId=checkedId;
		// TODO Auto-generated method stub
		// 开启一个Fragment事务
		FragmentTransaction transaction = fragmentManager.beginTransaction();
		// 先隐藏掉所有的Fragment，以防止有多个Fragment显示在界面上的情况
		if (wdFragment != null) {
			transaction.hide(wdFragment);
		}
		if (mapTdtFragment != null) {
			transaction.hide(mapTdtFragment);
		}

		if (checkedId == R.id.rb_xc) {
			if (mapTdtFragment == null) {
				// 如果MapTdtFragment为空，则创建一个并添加到界面上
				mapTdtFragment = new MapTdtFragment(0);
				transaction.add(R.id.content, mapTdtFragment);
			} else {
				// 如果MapTdtFragment不为空，则直接将它显示出来
				transaction.show(mapTdtFragment);
				mapTdtFragment.changeView(0);
			}
		} else if (checkedId == R.id.rb_db) {
			if (mapTdtFragment == null) {
				// 如果MapTdtFragment为空，则创建一个并添加到界面上
				mapTdtFragment = new MapTdtFragment(1);
				transaction.add(R.id.content, mapTdtFragment);
			} else {
				// 如果MapTdtFragment不为空，则直接将它显示出来
				transaction.show(mapTdtFragment);
				mapTdtFragment.changeView(1);
			}
		} else if (checkedId == R.id.rb_wd) {
			if (wdFragment == null) {
				// 如果WdFragment为空，则创建一个并添加到界面上
				wdFragment = new WdFragment();
				transaction.add(R.id.content, wdFragment);
			} else {
				// 如果WdFragment不为空，则直接将它显示出来
				transaction.show(wdFragment);
			}
		}
		
		transaction.commit();
	}
	
	/**
	 * 按撤销键弹出推出对话框
	 */
	@Override
	public boolean dispatchKeyEvent(KeyEvent event) {
		if (event.getKeyCode() == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
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
	 * @author xuhuaiguang
	 * @Version 1.3.4
	 * @Copyright 中科宇图天下科技有限公司 Create at: 2012-12-6 上午09:43:32
	 */
	private class btnExitListener implements android.content.DialogInterface.OnClickListener {
		@Override
		public void onClick(DialogInterface arg0, int arg1) {
			finish();
		}
	}
}
