package com.mapuni.android.ui;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

import org.dom4j.DocumentException;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.mapuni.android.MobileEnforcement.R;
import com.mapuni.android.base.BaseActivity;
import com.mapuni.android.base.Global;
import com.mapuni.android.base.business.BaseObjectFactory;
import com.mapuni.android.base.controls.listview.HorizontialListView;
import com.mapuni.android.base.util.DisplayUitl;
import com.mapuni.android.business.QYJBXX;
import com.mapuni.android.business.RWXX;
import com.mapuni.android.common.DetailedActivity;
import com.mapuni.android.dataprovider.SqliteUtil;
import com.mapuni.android.dataprovider.XmlHelper;
import com.mapuni.android.enterpriseArchives.EnterpriseArchivesActivitySlide;
import com.mapuni.android.taskmanager.TaskInformationActivity;

/**
 * FileName: GlobalSearchActivity.java Description:全局搜索 <li>搜索所有附件，并可以打开 <li>
 * 权限过滤
 * 
 * @author Liusy
 * @Version 1.3.4
 * @Copyright 中科宇图天下科技有限公司<br>
 *            Create at: 2012-12-19 下午02:28:09
 */
public class GlobalSearchActivity extends BaseActivity {
	// 父页面布局
	private RelativeLayout fatherRelativeLayout;
	private LinearLayout middleLayout;
	// 本界面布局
	private EditText mEditText;
	private HorizontialListView mTypeListView;
	private ListView mResultListView;
	protected LinearLayout mLoadLayout;
	protected LinearLayout mProgressLoadLayout;
	/** 查询项 */
	private ArrayList<HashMap<String, Object>> resultDataList;
	private ArrayList<HashMap<String, Object>> typeDataList;
	private ArrayList<HashMap<String, Object>> queryData;
	private HashMap<String, String> conditionMap;
	private MyResultAdapter mResultAdapter;
	private MyTypeAdapter mTypeAdapter;

	/** 当前选择类型，空为全部类型 */
	private int selectedType = 0;
	// 配置查询项
	private final String STYLE_GLOBALSEARCH = "style_globalsearch.xml";
	private final String FILE_PATH = Global.SDCARD_FJ_LOCAL_PATH;
	private final String TAG = "GlobalSearch";

	/** 加载数据--没有数据 */
	private final int MESSAGE_LOADING_DONE = 0;
	private final int MESSAGE_LOADING_DATA = 1;
	private final int MESSAGE_LOADING_NODATA = 2;

	private String result_id = "";
	private String result_column = "";
	// private String select_type = "";
	private String show_type = "";

	private Handler handler;

	/** 判断列表是否滚到最后一行 */
	private final boolean flag = false;
	private final int startItem = 0;
	private final int endItem = 0;
	private int scrollTimes = 1;
	private final int listCount = 20;
	private boolean isToast = true;

	/** 列表字号大小设置 */
	/** 列表第一次加载的条数 */
	// private static final int listcount =
	// Global.getGlobalInstance().getListNumber();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.ui_mapuni);
		// 父类布局样式
		fatherRelativeLayout = (RelativeLayout) findViewById(R.id.parentLayout);
		middleLayout = (LinearLayout) findViewById(R.id.middleLayout);
		findViewById(R.id.ui_mapuni_divider).setVisibility(View.GONE);
		SetBaseStyle(fatherRelativeLayout, "全局搜索");
		setTitleLayoutVisiable(true);

		typeDataList = initTypeData(this);
		initViews(this);

		handler = new Handler() {
			@Override
			public void handleMessage(android.os.Message msg) {
				switch (msg.what) {
				case MESSAGE_LOADING_DONE:
					mLoadLayout.setVisibility(View.GONE);
					// mResultListView.removeFooterView(mLoadLayout);
					mResultAdapter.notifyDataSetChanged();
					break;
				case MESSAGE_LOADING_DATA:
					// Refresh
					mResultListView.addFooterView(mLoadLayout);
					mProgressLoadLayout.setVisibility(View.VISIBLE);
					mLoadLayout.setVisibility(View.VISIBLE);
					break;
				case MESSAGE_LOADING_NODATA:
					// Refresh
					Toast.makeText(GlobalSearchActivity.this, "数据加载完成！", 0).show();
					break;
				default:
					break;
				}
			};
		};
	}

	/**
	 * Description:初始化界面组件
	 * 
	 * @param context
	 * @author Administrator<br>
	 *         Create at: 2012-12-19 下午04:51:05
	 */
	private void initViews(final Context context) {
		// 美化图片
		// TextView
		// type
		// result
		LinearLayout outLayout = (LinearLayout) LayoutInflater.from(context).inflate(R.layout.globalsearch, null);
		mEditText = (EditText) outLayout.findViewById(R.id.globalsearch_et);
		mTypeListView = (HorizontialListView) outLayout.findViewById(R.id.globalsearch_type);
		mResultListView = (ListView) outLayout.findViewById(R.id.globalsearch_result);

		// 文本框
		mEditText.addTextChangedListener(new MySearchTextWatcher());

		// 类型
		mTypeAdapter = new MyTypeAdapter(context, typeDataList);
		mTypeListView.setAdapter(mTypeAdapter);
		mTypeListView.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long arg3) {
				selectedType = position;
				scrollTimes = 1;
				// 选中，刷新
				mTypeAdapter.notifyDataSetChanged();
				// 重新查询
				String input_info = mEditText.getText().toString();
				if (input_info.equals("")) {
					return;
				}
				doQuery(selectedType, input_info);
				/*
				 * String table =
				 * (String)typeDataList.get(position).get("table");
				 * //result_type =
				 * (String)typeDataList.get(position).get("name");
				 * 
				 * queryData = new ArrayList<HashMap<String,Object>>();
				 * //HashMap<String, String> condition = new HashMap<String,
				 * String>(); conditionMap = new HashMap<String, String>();
				 * conditionMap.put("id",
				 * (String)typeDataList.get(selectedType).get("id"));
				 * conditionMap.put("column",
				 * (String)typeDataList.get(selectedType ).get("column"));
				 * conditionMap.put("name",
				 * (String)typeDataList.get(selectedType).get("name"));
				 * conditionMap.put("ywl",
				 * (String)typeDataList.get(selectedType).get("ywl"));
				 * conditionMap.put("table",
				 * (String)typeDataList.get(selectedType).get("table"));
				 * conditionMap.put("type",
				 * (String)typeDataList.get(selectedType).get("type"));
				 * conditionMap.put("path",
				 * (String)typeDataList.get(selectedType).get("path"));
				 * conditionMap.put("value", input_info.toString()); queryData =
				 * searchForData(conditionMap); if(queryData == null) { return;
				 * } resultDataList.clear(); resultDataList.addAll(queryData);
				 * mResultAdapter.notifyDataSetChanged();
				 */
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {

			}

		});
		// 结果
		/** 获取手机宽度 */
		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		resultDataList = new ArrayList<HashMap<String, Object>>();
		mResultAdapter = new MyResultAdapter(context, resultDataList);
		mResultListView.setAdapter(mResultAdapter);

		/** ProgressBar的布局 */
		mProgressLoadLayout = new LinearLayout(this);
		mProgressLoadLayout.setMinimumHeight(30);
		mProgressLoadLayout.setGravity(Gravity.CENTER);

		mLoadLayout = new LinearLayout(this);
		mLoadLayout.setMinimumHeight(30);
		mLoadLayout.setMinimumWidth(dm.heightPixels);
		mLoadLayout.setGravity(Gravity.CENTER);
		mLoadLayout.setOrientation(LinearLayout.VERTICAL);
		mLoadLayout.setBackgroundResource(R.drawable.loading);
		mLoadLayout.setVisibility(View.GONE);
		mLoadLayout.addView(mProgressLoadLayout, new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));

		mResultListView.addFooterView(mLoadLayout);
		mResultListView.setOnScrollListener(new ResultListViewOnScrollListener());
		mResultListView.setOnItemClickListener(new ResultListViewOnClickListener());
		middleLayout.addView(outLayout);
	}

	/**
	 * Description:初始化类别数据
	 * 
	 * @param context
	 * @return 数据集合
	 * @author Administrator<br>
	 *         Create at: 2012-12-19 下午03:54:41
	 */
	private ArrayList<HashMap<String, Object>> initTypeData(Context context) {
		InputStream inputStream = null;
		ArrayList<HashMap<String, Object>> dataXMLList = null;
		ArrayList<HashMap<String, Object>> powerList = null;
		try {
			inputStream = context.getResources().getAssets().open(STYLE_GLOBALSEARCH);
			dataXMLList = XmlHelper.getList(inputStream, "item");
		} catch (IOException e) {
			e.printStackTrace();
		} catch (DocumentException e) {
			e.printStackTrace();
		}
		// 通过权限过滤
		// 为空或者匹配上权限，即可显示
		powerList = new ArrayList<HashMap<String, Object>>();
		for (HashMap<String, Object> dataMap : dataXMLList) {
			if ("".equals(dataMap.get("qxid")) || DisplayUitl.getAuthority((String) dataMap.get("qxid"))) {
				powerList.add(dataMap);
			}
		}
		return powerList;
	}

	/**
	 * Description:根据条件查询信息
	 * 
	 * @param input_info
	 * @return
	 * @author Administrator<br>
	 *         Create at: 2012-12-19 下午04:58:07
	 */
	private ArrayList<HashMap<String, Object>> searchForData(HashMap<String, String> condition) {
		result_id = condition.get("id");
		result_column = condition.get("column");
		show_type = condition.get("type");
		String name = condition.get("name");
		String table = condition.get("table");
		String ywl = condition.get("ywl");
		String value = condition.get("value");
		String path = condition.get("path");
		StringBuffer sb = new StringBuffer("");
		if (!table.equals("")) {
			// 正常查询
			sb = buildQuerySql(result_id, result_column, name, show_type, ywl, path, table, value);
		} else {
			// 连接查询
			for (HashMap<String, Object> map : typeDataList) {
				String id = (String) map.get("id");
				String column = (String) map.get("column");
				String type = (String) map.get("name");
				String ywlStr = (String) map.get("ywl");
				String pathStr = (String) map.get("path");
				String tabbleStr = (String) map.get("table");
				String showtype = (String) map.get("type");
				if (id.equals("") || tabbleStr.equals("") || type.equals("")) {
					continue;
				}
				sb.append(buildQuerySql(id, column, type, showtype, ywlStr, pathStr, tabbleStr, value));
				sb.append(" union ");
			}
			sb.delete(sb.length() - 6, sb.length());
		}
		sb.append(getOrder());
		Log.i(TAG, "[Build SQL]: sql = " + sb.toString());
		return SqliteUtil.getInstance().queryBySqlReturnArrayListHashMap(sb.toString());
	}

	private StringBuffer buildQuerySql(String id, String column, String name, String showtype, String ywl, String path, String table, String value) {
		StringBuffer sb = new StringBuffer("");
		sb.append("select " + id + " as id," + column + " as name,'" + name + "' as type, '" + showtype + "'as showtype,'" + ywl + "' as ywl, '" + path + "' as path from ");
		sb.append(table);
		sb.append(" where " + column + " like '%");
		sb.append(value);
		sb.append("%'");
		return sb;
	}

	private void doQuery(int selectType, String text) {
		ArrayList<HashMap<String, Object>> data = new ArrayList<HashMap<String, Object>>();
		conditionMap = new HashMap<String, String>();
		conditionMap.put("id", (String) typeDataList.get(selectedType).get("id"));
		conditionMap.put("column", (String) typeDataList.get(selectedType).get("column"));
		conditionMap.put("name", (String) typeDataList.get(selectedType).get("name"));
		conditionMap.put("table", (String) typeDataList.get(selectedType).get("table"));
		conditionMap.put("type", (String) typeDataList.get(selectedType).get("type"));
		conditionMap.put("ywl", (String) typeDataList.get(selectedType).get("ywl"));
		conditionMap.put("value", text.toString());
		Log.i(TAG, "[Search For]: text = " + text);
		data = searchForData(conditionMap);
		if (data == null) {
			return;
		}
		resultDataList.clear();
		resultDataList.addAll(data);
		mResultAdapter.notifyDataSetChanged();
	}

	/**
	 * FileName: MySearchTextWatcher Description:TextWatcher接口
	 * 
	 * @author Administrator
	 * @Version 1.3.4
	 * @Copyright 中科宇图天下科技有限公司<br>
	 *            Create at: 2012-12-20 下午01:13:04
	 */
	private class MySearchTextWatcher implements TextWatcher {

		@Override
		public void afterTextChanged(Editable s) {

		}

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count, int after) {

		}

		@Override
		public void onTextChanged(CharSequence s, int start, int before, int count) {
			/*
			 * resultDataList.clear(); if (mEditText.getText() != null) { String
			 * input_info = mEditText.getText().toString(); resultDataList =
			 * searchForData(input_info); mResultAdapter.notifyDataSetChanged();
			 * }
			 */
			if (s != null && s.length() > 0) {
				doQuery(selectedType, s.toString());
			}
		}
	}

	/**
	 * FileName: GlobalSearchActivity.java Description:匹配结果显示列表
	 * 
	 * @author Administrator
	 * @Version 1.3.4
	 * @Copyright 中科宇图天下科技有限公司<br>
	 *            Create at: 2012-12-19 下午05:42:49
	 */
	private class MyResultAdapter extends BaseAdapter {
		private final ArrayList<HashMap<String, Object>> data;
		private final Context context;
		private LayoutInflater mInflater = null;
		private final int layoutid = R.layout.ui_list_item_cell;

		public MyResultAdapter(Context context, ArrayList<HashMap<String, Object>> data) {
			this.data = data;
			this.context = context;
			this.mInflater = LayoutInflater.from(context);
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
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder = null;
			// 1.Get Setting
			int textSize = (Integer) DisplayUitl.getSettingValue(context, DisplayUitl.TEXTSIZE, 22);
			int textColor = Color.BLACK;

			// 2.Get View
			if (convertView == null) {
				convertView = mInflater.inflate(layoutid, null);
				holder = new ViewHolder();

				holder.lefticon = (ImageView) convertView.findViewById(R.id.lefticon);
				holder.lefticon.setLayoutParams(new TableRow.LayoutParams(30, 30));

				holder.title = (TextView) convertView.findViewById(R.id.title);
				holder.title.setTextSize(textSize);
				holder.title.setTextColor(textColor);
				holder.title.setPadding(0, 0, 10, 0);

				// holder.content = (TextView)
				// convertView.findViewById(R.id.content);
				// holder.content.setTextColor(textColor);

				holder.date = (TextView) convertView.findViewById(R.id.date);
				holder.date.setTextColor(textColor);
				holder.date.setLayoutParams(new TableRow.LayoutParams(80, LayoutParams.WRAP_CONTENT));

				holder.righticon = (ImageView) convertView.findViewById(R.id.rightIcon);
				holder.righticon.setLayoutParams(new TableRow.LayoutParams(30, 30));
				holder.righticon.setPadding(0, 0, 10, 0);

				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			// 3.Get Data
			// Image
			int leftIconId = R.drawable.icon_table;
			int rightIconId = R.drawable.righticon_default;

			// show
			String name = (String) data.get(position).get("name");
			String type = (String) data.get(position).get("type");

			// content
			String id = (String) data.get(position).get("id");
			String ywl = (String) data.get(position).get("ywl");
			String path = (String) data.get(position).get("path");
			String showtype = (String) data.get(position).get("showtype");
			HashMap<String, String> values = new HashMap<String, String>();
			values.put("name", name);
			values.put("type", type);
			values.put("id", id);
			values.put("ywl", ywl);
			values.put("path", path);
			values.put("showtype", showtype);

			// 4.Bind data to view
			holder.title.setText(name);
			holder.title.setTag(values);
			holder.date.setText(type);
			holder.lefticon.setBackgroundResource(leftIconId);
			holder.righticon.setBackgroundResource(rightIconId);

			return convertView;
		}
	}

	public final class ViewHolder {
		public TextView title = null;
		public TextView content = null;
		public TextView date = null;
		public ImageView lefticon = null;
		public ImageView righticon = null;
	}

	/**
	 * FileName: GlobalSearchActivity.java Description:类型显示列表
	 * 
	 * @author Administrator
	 * @Version 1.3.4
	 * @Copyright 中科宇图天下科技有限公司<br>
	 *            Create at: 2012-12-19 下午05:43:14
	 */
	private class MyTypeAdapter extends BaseAdapter {
		ArrayList<HashMap<String, Object>> data;
		Context context;

		public MyTypeAdapter(Context context, ArrayList<HashMap<String, Object>> data) {
			this.data = data;
			this.context = context;
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
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			/*
			 * LinearLayout layout = new LinearLayout(context);
			 * layout.setOrientation(LinearLayout.VERTICAL);
			 * 
			 * ImageView upImageView = new ImageView(context);
			 * upImageView.setImageResource(R.drawable.icon_left_not_checked);
			 * upImageView.setImageResource(R.drawable.icon_left_not_checked);
			 * 
			 * TextView mTextView = new TextView(context); String name =
			 * (String)data.get(position).get("type"); mTextView.setText(name);
			 * 
			 * layout.addView(upImageView); layout.addView(mTextView);
			 */

			TextView mTextView = new TextView(context);
			String type = (String) data.get(position).get("name");
			mTextView.setText(type);
			mTextView.setTextColor(Color.BLACK);
			mTextView.setTextSize(20);
			mTextView.setPadding(5, 2, 5, 2);
			// mTextView.setWidth(200);
			// mTextView.setBackgroundResource(R.drawable.btn_shap);

			if (position == selectedType) {
				String textInfo = "<b><font color='#0288e7'>[" + type + "]</font></b>";
				mTextView.setText(Html.fromHtml(textInfo));
			}
			return mTextView;
		}
	}

	/**
	 * FileName: GlobalSearchActivity.java Description:滑动事件监听
	 * 
	 * @author Administrator
	 * @Version 1.3.4
	 * @Copyright 中科宇图天下科技有限公司<br>
	 *            Create at: 2013-1-14 下午02:32:29
	 */
	private final class ResultListViewOnScrollListener implements AbsListView.OnScrollListener {
		int visibleLastIndex = 0;
		boolean isLastPage = false;
		boolean isScrollToEnd = false;

		@Override
		public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
			if (firstVisibleItem + visibleItemCount == totalItemCount && totalItemCount >= listCount) {
				isScrollToEnd = true;
				handler.sendEmptyMessage(MESSAGE_LOADING_DATA);
			}
		}

		@Override
		public void onScrollStateChanged(AbsListView view, int scrollState) {
			if (isScrollToEnd && scrollState == OnScrollListener.SCROLL_STATE_IDLE) {
				// 满足条件，加载数据
				if (view.getLastVisiblePosition() == view.getCount() - 1) {
					new Thread(new Runnable() {
						@Override
						public void run() {
							AppendData();
							handler.sendEmptyMessage(MESSAGE_LOADING_DONE);
						};
					}).start();
				}
			}
		}
	}

	private void AppendData() {
		Log.i(TAG, "On Scroll : AppendData");
		if (isToast) {
			ArrayList<HashMap<String, Object>> Datalist = new ArrayList<HashMap<String, Object>>();
			scrollTimes++;
			Datalist = searchForData(conditionMap);
			if (Datalist != null) {
				if (Datalist.size() < listCount) {
					handler.sendEmptyMessage(MESSAGE_LOADING_NODATA);
					isToast = false;
				}
				resultDataList.addAll(Datalist);
			}
		}
	}

	public String getOrder() {
		// int count = Global.getGlobalInstance().getListNumber();
		int count = listCount;
		int x = scrollTimes * count - count;
		int j = count;
		String order = " limit " + x + "," + j;
		return order;
	}

	/**
	 * FileName: GlobalSearchActivity.java Description:结果列表点击事件监听
	 * 
	 * @author Administrator
	 * @Version 1.3.4
	 * @Copyright 中科宇图天下科技有限公司<br>
	 *            Create at: 2013-1-14 下午02:32:46
	 */
	private final class ResultListViewOnClickListener implements OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> arg0, View view, int position, long arg3) {
			// click listener for the result list
			// different type for different action
			HashMap<String, String> values = (HashMap<String, String>) view.findViewById(R.id.title).getTag();
			if (values == null || values.size() == 0) {
				return;
			}
			String showtype = values.get("showtype");
			String name = values.get("name");
			String ywl = values.get("ywl");
			String id = values.get("id");
			String path = values.get("path");
			Context context = GlobalSearchActivity.this;
			Intent intent = new Intent();
			Bundle nextBundle = null;
			// 附件
			if ("detail".equals(showtype)) {
				// some special detail activity
				if (ywl.equals("RWXX")) {
					intent.setClass(context, TaskInformationActivity.class);
					RWXX rwxx = new RWXX();
					rwxx.setCurrentID(id);
					nextBundle = new Bundle();
					nextBundle.putSerializable("BusinessObj", rwxx);
					intent.putExtras(nextBundle);
				} else if (ywl.equals("QYJBXX")) {
					intent.setClass(context, EnterpriseArchivesActivitySlide.class);
					intent.putExtra("qydm", getQYBM(id));
				} else {
					// open detail activity
					try {
						intent.setClass(context, DetailedActivity.class);
						intent.putExtra("itemId", id);
						nextBundle = new Bundle();
						nextBundle.putSerializable("BusinessObj", (Serializable) BaseObjectFactory.createObject(ywl));
						intent.putExtras(nextBundle);
					} catch (ClassNotFoundException e) {
						e.printStackTrace();
					} catch (IllegalAccessException e) {
						e.printStackTrace();
					} catch (InstantiationException e) {
						e.printStackTrace();
					}
				}
				Log.i(TAG, "[Start Detail]: ID = " + id);
				startActivity(intent);
			} else if ("fj".equals(showtype)) {
				// open attach files
				String filePath = FILE_PATH + path + "/" + name;
				if (path.equals("")) {
					for (HashMap<String, Object> map : typeDataList) {
						File tempfile = new File(FILE_PATH + map.get("path") + "/" + name);
						if (tempfile.exists()) {
							filePath = tempfile.getAbsolutePath();
						}
					}
				}
				Log.i(TAG, "[Open File]:" + filePath);
				// 打开文件
				if (!new File(filePath).exists()) {
					Toast.makeText(GlobalSearchActivity.this, "对不起，文件不存在！", 0).show();
					return;
				}
				DisplayUitl.openfile(filePath, GlobalSearchActivity.this);
			} else {
				Toast.makeText(context, "数据错误", 0).show();
				return;
			}
			// Detail
			// Others
		}
	}

	/**
	 * Description: 获取企业代码
	 * 
	 * @param id
	 *            企业ID
	 * @return 返回企业代码 String
	 * @author 王红娟 Create at: 2012-12-6 上午11:25:38
	 */
	public String getQYBM(String id) {
		String qydm = "";
		QYJBXX qyjbxx = new QYJBXX();
		HashMap<String, Object> hashMap = qyjbxx.getDetailed(id);
		if (hashMap != null) {
			qydm = hashMap.get("qydm").toString();
			if (qydm == null) {
				qydm = "";
			}
		}
		return qydm;
	}
}