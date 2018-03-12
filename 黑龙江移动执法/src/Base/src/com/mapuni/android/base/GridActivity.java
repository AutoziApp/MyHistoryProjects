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
 * FileName: GridActivity.java Description: �Ź���
 * 
 * @author ��˼Զ
 * @Version 1.3.4
 * @Copyright �п���ͼ���¿Ƽ����޹�˾ Create at: 2012-12-5 ����06:08:32
 */
public class GridActivity extends BaseActivity {

	/** Bundle���� */
	private Bundle bundle;
	/** ҵ����̳���IGrid�ӿڣ�����ת�� */
	private IGrid businessObj;
	/** ����һ��Intent���� */
	private Intent intent;

	/** �Ƿ����ƶ��칫�ľŹ��� */
	private Boolean ydbg;
	/** ���ݰ����ж����Ǹ����ڵ��� */
	private String packageName = "";
	/** �û���Ȩ�޼��ϣ�Ŀǰû���õ��� */
	public ArrayList<HashMap<String, Object>> authoritylist;
	/** ���������ļ�(Ŀǰû���õ�) */
	File f, fil;
	// �Ƿ��������
	private Boolean isAddTask = false;

	/** ��������Ȩ�� */
	public String TJRW_QX = "vmob2A4B";

	// /** ������Layout */
	// private SlideLayout slideLayout;
	// /** �����ListView */
	// private ListView sortListView;
	/** dialog��listView������ */
	public ArrayList<HashMap<String, Object>> listData;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ui_mapuni);

		RelativeLayout linearLayout = (RelativeLayout) this.findViewById(R.id.parentLayout);
		/** ��ȡ��һ��ҳ�洫������Intent */
		Intent it = this.getIntent();
		/** �������� */
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
			/** ҵ��������ת�� */
			businessObj = (IGrid) bundle.getSerializable("BusinessObj");
		}
		/** �ж��Ƿ����ƶ��칫�ľŹ��� */
		ydbg = bundle.containsKey("isydbg") ? true : false;
		/** ��ȡ�Ź���ı��� */
		String titleText = businessObj.getGridTitle();

		isAddTask = bundle.getBoolean("isAddTask");
		// �������
		// if(DisplayUitl.getAuthority(TJRW_QX)&&isAddTask){
		// super.isAddTask=true;
		// }
		/** ���þŹ���ı��� */
		super.SetBaseStyle(linearLayout, titleText);
		/** �Ƿ���ر��Ⲽ�� */
		boolean isShowTitle = bundle.getBoolean("IsShowTitle");
		super.setTitleLayoutVisiable(isShowTitle);
		super.setSearchButtonVisiable(isAddTask);
		// �������ļ���ʱ��
		queryImg.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				/** �����ѯ�� */
				if (isAddTask) {
					/*
					 * Bundle bundle = new Bundle();
					 * bundle.putBoolean("IsShowTitle", true);
					 * bundle.putString("TitleText", "����༭");
					 * 
					 * Intent _Intent = new Intent(GridActivity.this,
					 * TaskEditActivity.class); _Intent.putExtras(bundle);
					 * startActivity(_Intent);
					 */
				}
			}
		});

		/** �ۺϲ�ѯ���ж��Ѿ���ʱ */
		if (businessObj.getGridTitle().equals("�ۺϲ�ѯ"))
			// super.setknowledgeButtonVisiable(false);
			Log.i("drawable-hdpi", "HashCode ��" + this.toString());
		GridView gridview = new LoadGridLayout(GridActivity.this, intent, businessObj, ydbg).loadGridLayout();
		/** ��ȥ�ܲ��ֵ��м䲼�� */
		LinearLayout queryLayout = (LinearLayout) this.findViewById(R.id.middleLayout);
		/** ������ľŹ��񲼾ַ����м䲼���� */
		queryLayout.addView(gridview);
		// initListData();// ��ʼ��dialog��listView�ϵ�����
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		Log.i("tag", "onSaveInstanceState");
		if (outState == null) {
			Log.i("tag", "outState��");
		}
		super.onSaveInstanceState(outState);
	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		Log.i("tag", "savedInstanceState");
		if (savedInstanceState == null) {
			Log.i("tag", "savedInstanceState��");
		}
		super.onRestoreInstanceState(savedInstanceState);
	}

	/**
	 * �������������Ƴ��Ի���
	 */
	@Override
	public boolean dispatchKeyEvent(KeyEvent event) {
		if (event.getKeyCode() == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN && bundle.getBoolean("IsMain")) {
			Builder dialog = new AlertDialog.Builder(this);
			dialog.setIcon(R.drawable.base_icon_mapuni_white);
			dialog.setTitle("ϵͳ�˳�");
			dialog.setMessage("ȷ��Ҫ�˳�ϵͳ��");
			dialog.setPositiveButton("��", new btnExitListener());
			dialog.setNegativeButton("��", null);
			dialog.show();
		}
		return super.dispatchKeyEvent(event);
	}

	/**
	 * FileName: GridActivity.java Description: �˳�ϵͳ
	 * 
	 * @author ��˼Զ
	 * @Version 1.3.4
	 * @Copyright �п���ͼ���¿Ƽ����޹�˾ Create at: 2012-12-6 ����09:43:32
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
	 * /** Description: ����ͼƬ�����֣���Ҫ��׺������ȡBitpmap
	 * 
	 * @param name ͼƬ������
	 * 
	 * @return ����һ��Bitpmap Bitmap
	 * 
	 * @author ��˼Զ Create at: 2012-12-6 ����09:4s3:54
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
		Log.i("wang", "���������٣�����");
	}

}
