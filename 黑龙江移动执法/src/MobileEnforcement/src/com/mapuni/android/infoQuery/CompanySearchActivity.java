/**
 * 
 */
package com.mapuni.android.infoQuery;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mapuni.android.base.BaseActivity;
import com.mapuni.android.base.Global;
import com.mapuni.android.base.R;
import com.mapuni.android.base.util.DisplayUitl;
import com.mapuni.android.base.widget.PagingListView;
import com.mapuni.android.business.QYJBXX;
import com.mapuni.android.enterpriseArchives.AddBusinessActivity;
import com.mapuni.android.enterpriseArchives.EnterpriseArchivesActivitySlide;
import com.mapuni.yqydweb.YqydWebActivity;

/**
 * @author SS
 * 
 *         Description
 * 
 *         Incoming parameters
 */
public class CompanySearchActivity extends BaseActivity {

	/** 显示标题的布局 */
	private RelativeLayout titleLayout;

	/** 填充页面中间布局可见视图 */
	private LinearLayout middleLayout;;

	/** 企业基本信息业务类 */
	private QYJBXX qyjbxx;

	/** 企业列表查询条件 */
	private HashMap<String, Object> companyCondition;

	/** 企业列表的查询布局 */
	private View queryView;

	/** 企业数据 */
	private ArrayList<HashMap<String, Object>> totalDataList;

	/** 企业列表 ,默认隐藏 */
	private PagingListView companyListview;

	/** 企业列表适配器 */
	private MyAdapter companyAdapter;

	/** 填充布局的容器 */
	private LayoutInflater layoutInflater;

	/** 显示查询条件的VIew */
	private View companyView;

	/** 隐藏底部布局 */
	private LinearLayout xczf_bottom_layout;

	/** 分页加载监听 */
	private MyOnPageCountChangListener pageCountChangListener;

	/** 企业列表数据页数 */
	private int pagingListCount = 1;

	/** 添加企业按钮 */
	private Button addCom;

	private final Handler handler = new Handler() {
		@Override
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 0:
				totalDataList.addAll((ArrayList<HashMap<String, Object>>) msg.obj);
				companyAdapter.notifyDataSetChanged();
				break;
			case 2:
				companyListview.setIsCompleted(true);
				break;
			}
		};
	};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ui_mapuni);
		initEntSearchView();
	}

	/** 初始化企业查询页面 */
	private void initEntSearchView() {
		titleLayout = (RelativeLayout) findViewById(R.id.parentLayout);
		middleLayout = (LinearLayout) findViewById(R.id.middleLayout);
		layoutInflater = LayoutInflater.from(this);
		companyView = layoutInflater.inflate(com.mapuni.android.MobileEnforcement.R.layout.sitelaw, null);
		xczf_bottom_layout = (LinearLayout) companyView.findViewById(com.mapuni.android.MobileEnforcement.R.id.xczf_bottom_layout);
		xczf_bottom_layout.setVisibility(View.GONE);
		companyListview = (PagingListView) companyView.findViewById(com.mapuni.android.MobileEnforcement.R.id.lv_ls_Ent);
		SetBaseStyle(titleLayout, "企业基本信息查询");
		qyjbxx = new QYJBXX();
		companyCondition = new HashMap<String, Object>();
		totalDataList = new ArrayList<HashMap<String, Object>>();
		queryView = qyjbxx.getLiaoNingQueryView(this, companyCondition);
		Button query_imagebutton = (Button) queryView.findViewById(com.mapuni.android.MobileEnforcement.R.id.enforcement_query_imagebutton);
		middleLayout.addView(queryView);
		middleLayout.addView(companyView);
		((View) query_imagebutton.getParent()).setVisibility(View.VISIBLE);// 查询按钮可见
		query_imagebutton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				totalDataList = qyjbxx.getDataLists(companyCondition,QYJBXX.xzqhss);
				if (totalDataList.size() > 0) {
					companyListview.setVisibility(View.VISIBLE);
					queryView.setVisibility(View.GONE);
				} else {
					Toast.makeText(CompanySearchActivity.this, "没有符合条件的企业", Toast.LENGTH_SHORT).show();
					// 增加添加企业按钮
					addCom = (Button) queryView.findViewById(com.mapuni.android.MobileEnforcement.R.id.addCom);
					((View) addCom.getParent()).setVisibility(View.VISIBLE);// 添加污染源按钮可见
					addCom.setOnClickListener(new View.OnClickListener() {
						public void onClick(View v) {
							// 转到添加企业页面
							Intent intAddCom = new Intent(CompanySearchActivity.this, AddBusinessActivity.class);
							startActivity(intAddCom);

						}
					});
					return;
				}
				companyAdapter = new MyAdapter(CompanySearchActivity.this);
				companyListview.setAdapter(companyAdapter);
			}
		});
		companyListview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				String qyidStr = totalDataList.get(arg2).get("guid").toString();
				Intent intent = new Intent(CompanySearchActivity.this, YqydWebActivity.class);
				intent.putExtra("qyid", qyidStr);
				startActivity(intent);
			}
		});
		pageCountChangListener = new MyOnPageCountChangListener();
		// 设置分页加载
		companyListview.setOnPageCountChangListener(pageCountChangListener);
	}

	private class MyOnPageCountChangListener implements PagingListView.PageCountChangListener {

		@Override
		public void onAddPage(AbsListView view) {
			if (view.getId() == com.mapuni.android.MobileEnforcement.R.id.lv_ls_Ent) {// 企业列表
				new Thread(new Runnable() {

					@Override
					public void run() {

						qyjbxx.setListScrolltimes(++pagingListCount);
						ArrayList<HashMap<String, Object>> newData = null;
						if (companyCondition != null) {
							newData = qyjbxx.getDataList(companyCondition);
						} else {
							newData = qyjbxx.getDataList();
						}
						if (newData.size() < Global.getGlobalInstance().getListNumber()) {
							handler.sendEmptyMessage(2);
							return;
						}
						Message msg = handler.obtainMessage();
						msg.obj = newData;
						msg.what = 0;
						handler.sendMessage(msg);
					}
				}).start();
			}
		}
	}

	/**
	 * FileName: SiteLawActivity.java Description:企业列表数据适配器
	 * 
	 * @author Administrator
	 * @Version 1.3.4
	 * @Copyright 中科宇图天下科技有限公司<br>
	 *            Create at: 2012-12-4 上午10:59:42
	 */
	private class MyAdapter extends BaseAdapter {

		private int textSize = 18;

		private MyAdapter(Context context) {

			textSize = Integer.parseInt(String.valueOf(DisplayUitl.getSettingValue(context, DisplayUitl.TEXTSIZE, 18)));
		}

		@Override
		public int getCount() {
			return totalDataList.size();
		}

		@Override
		public Object getItem(int position) {
			return totalDataList.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {

			VierHolder vh;
			if (convertView == null) {
				vh = new VierHolder();
				convertView = layoutInflater.inflate(com.mapuni.android.MobileEnforcement.R.layout.listitem, null);
				vh.img = (ImageView) convertView.findViewById(com.mapuni.android.MobileEnforcement.R.id.listitem_left_image);
				vh.tv = (TextView) convertView.findViewById(com.mapuni.android.MobileEnforcement.R.id.listitem_text);
				convertView.setTag(vh);
			} else {
				vh = (VierHolder) convertView.getTag();
			}
			vh.img.setImageResource(com.mapuni.android.MobileEnforcement.R.drawable.xczf_qylb_tb);

			vh.tv.setSelected(true);
			vh.tv.setText((String) totalDataList.get(position).get("qymc"));
			vh.tv.setTag(totalDataList.get(position).get("guid"));// 设置企业GUID
			vh.tv.setTextSize(textSize);

			return convertView;
		}

		class VierHolder {
			ImageView img;
			TextView tv;
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			// 如果不显示的话 退出
			if (!(companyListview.getVisibility() == View.VISIBLE)) {
				return super.onKeyDown(keyCode, event);
			} else {
				// 如果显示的话 就把view 隐藏
				companyListview.setVisibility(View.GONE);
				queryView.setVisibility(View.VISIBLE);
				qyjbxx.setListScrolltimes(1);
				pagingListCount = 1;
				companyListview.setIsCompleted(false);

			}
		}
		return false;

	}
}
