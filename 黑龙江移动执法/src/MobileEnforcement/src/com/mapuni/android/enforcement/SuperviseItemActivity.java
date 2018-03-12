package com.mapuni.android.enforcement;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Html;
import android.text.InputFilter;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.mapuni.android.MobileEnforcement.R;
import com.mapuni.android.base.Global;
import com.mapuni.android.base.widget.CustomExpandableListView;
import com.mapuni.android.business.RWXX;
import com.mapuni.android.business.TreeNode;
import com.mapuni.android.dataprovider.FileHelper;
import com.mapuni.android.dataprovider.SQLiteDataProvider;
import com.mapuni.android.dataprovider.SqliteUtil;

/**
 * FileName: SpecialItemActivity.java Description:执法清单专项
 * 
 * @author liusy
 * @Version 1.3.4
 * @Copyright 中科宇图天下科技有限公司 Create at: 2012-12-3 下午3:57:34
 */
public class SuperviseItemActivity extends AttachmentBaseActivity implements OnClickListener {
	CustomExpandableListView listView = null;
	private int expandGroupID = 0;
	// private String tempResult;
	private List<String> tempList = new ArrayList<String>();
	private HashMap<String, Boolean> tempHash = new HashMap<String, Boolean>();
	// 父页面布局
	private RelativeLayout fatherRelativeLayout;
	// private LinearLayout middleLayout;
	private Intent intent;
	// private TreeNode frontdata = null;// 前一页面点击的Node
	private List<TreeNode> childNodeList;// 当前Node的所有叶子节点集合
	private List<HashMap<String, Object>> childList;
	// 全局变量
	private int LIST_SIZT;
	private final String surveyTime = Global.getGlobalInstance().getDate();
	/** 结果类型ID */
	private String resultTypeId = "";
	/** 结果备注文本 */
	private String remarkResult = "";
	LastResult lastResult;
	public final String RESULT_TABLE = "YDZF_SiteEnvironmentMonitorRecord";
	private final String TAG = "SpecialItemActivity";

	private String specialItemText = "";
	private boolean onceFlag = true;

	private boolean isEditable = true;
	private final RWXX rwxx = new RWXX();
	private InputMethodManager imm;
	private String entid;
	private String taskid;
	private String code;
	private String name;

	Intent intent1;
	boolean isClick = false;
	// private String insertOrUpdate = "insert";
	/** 当前手机系统版本号 */
	private int currentapiVersion = android.os.Build.VERSION.SDK_INT;
	private PopupWindow pop;

	private String tag = "";
	private boolean isSearch;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.ui_mapuni);
		// 修改背景
		RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.parentLayout);
		relativeLayout.setBackgroundColor(Color.rgb(245, 245, 245));

		// 获取前页传递的信息
		intent = getIntent();
		entid = intent.getStringExtra("entid");
		taskid = intent.getStringExtra("taskid");
		initGlobal();
		code = intent.getStringExtra("code");
		isSearch = intent.getBooleanExtra("isSearch", false);
        isEditable=!isSearch;
		tag = code;
		name = intent.getStringExtra("name");
		// frontdata = (TreeNode) intent.getExtras().get("node");//
		// 获得前一页面传的Node对象
		// childNodeList = frontdata.getChildren();
		// LIST_SIZT = childNodeList.size();
		childList = new ArrayList<HashMap<String, Object>>();
		if ("10".equals(code)) {
			HashMap<String, Object> hash = new HashMap<String, Object>();
			hash.put("code", "100");
			hash.put("name", "目前生产状况");
			childList.add(hash);
			hash = new HashMap<String, Object>();
			hash.put("code", "101");
			hash.put("name", "主要原料及用量");
			childList.add(hash);
			hash = new HashMap<String, Object>();
			hash.put("code", "102");
			hash.put("name", "主要产品及用量");
			childList.add(hash);
		} else if ("11".equals(code)) {
			HashMap<String, Object> hash = new HashMap<String, Object>();
			hash.put("code", "110");
			hash.put("name", "未经审批建设情况");
			childList.add(hash);
			hash = new HashMap<String, Object>();
			hash.put("code", "111");
			hash.put("name", "未按期验收情况");
			childList.add(hash);
		} else if ("12".equals(code)) {
			HashMap<String, Object> hash = new HashMap<String, Object>();
			hash.put("code", "120");
			hash.put("name", "废水_____元/月");
			childList.add(hash);
			hash = new HashMap<String, Object>();
			hash.put("code", "121");
			hash.put("name", "废气_____元/月");
			childList.add(hash);
			hash = new HashMap<String, Object>();
			hash.put("code", "122");
			hash.put("name", "噪声_____元/月");
			childList.add(hash);
			hash = new HashMap<String, Object>();
			hash.put("code", "123");
			hash.put("name", "废渣_____元/月");
			childList.add(hash);
		} else if ("20".equals(code)) {
			HashMap<String, Object> hash = new HashMap<String, Object>();
			hash.put("code", "200");
			hash.put("name", "废水治理设施建设运行情况");
			childList.add(hash);
			hash = new HashMap<String, Object>();
			hash.put("code", "201");
			hash.put("name", "废气治理设施运行情况");
			childList.add(hash);
			hash = new HashMap<String, Object>();
			hash.put("code", "202");
			hash.put("name", "污染物自动监控系统运行情况");
			childList.add(hash);
			hash = new HashMap<String, Object>();
			hash.put("code", "203");
			hash.put("name", "固体废物治理设施运行情况");
			childList.add(hash);
			hash = new HashMap<String, Object>();
			hash.put("code", "204");
			hash.put("name", "噪声治理设施运行情况");
			childList.add(hash);
			hash = new HashMap<String, Object>();
			hash.put("code", "205");
			hash.put("name", "新污水地方排放标准后污水处理设施升级改造");
			childList.add(hash);
		} else if ("30".equals(code)) {
			HashMap<String, Object> hash = new HashMap<String, Object>();
			hash.put("code", "300");
			hash.put("name", "排放去向");
			childList.add(hash);
			hash = new HashMap<String, Object>();
			hash.put("code", "301");
			hash.put("name", "监测超标因子及浓度");
			childList.add(hash);
			hash = new HashMap<String, Object>();
			hash.put("code", "302");
			hash.put("name", "超标因子排放标准");
			childList.add(hash);
			hash = new HashMap<String, Object>();
			hash.put("code", "303");
			hash.put("name", "超标原因");
			childList.add(hash);
			hash = new HashMap<String, Object>();
			hash.put("code", "304");
			hash.put("name", "在线监测超标情况");
			childList.add(hash);
			hash = new HashMap<String, Object>();
			hash.put("code", "305");
			hash.put("name", "超标原因");
			childList.add(hash);
			hash = new HashMap<String, Object>();
			hash.put("code", "306");
			hash.put("name", "上季度监督性监测超标情况");
			childList.add(hash);
			hash = new HashMap<String, Object>();
			hash.put("code", "307");
			hash.put("name", "上季度在线监测超标情况");
			childList.add(hash);
		} else if ("31".equals(code)) {
			HashMap<String, Object> hash = new HashMap<String, Object>();
			hash.put("code", "310");
			hash.put("name", "考核时段");
			childList.add(hash);
			hash = new HashMap<String, Object>();
			hash.put("code", "311");
			hash.put("name", "监测超标因子及浓度");
			childList.add(hash);
			hash = new HashMap<String, Object>();
			hash.put("code", "312");
			hash.put("name", "超标因子排放标准");
			childList.add(hash);
			hash = new HashMap<String, Object>();
			hash.put("code", "313");
			hash.put("name", "超标原因");
			childList.add(hash);
			hash = new HashMap<String, Object>();
			hash.put("code", "314");
			hash.put("name", "在线监测超标情况");
			childList.add(hash);
			hash = new HashMap<String, Object>();
			hash.put("code", "315");
			hash.put("name", "超标原因");
			childList.add(hash);
			hash = new HashMap<String, Object>();
			hash.put("code", "316");
			hash.put("name", "上季度监督性监测超标情况");
			childList.add(hash);
			hash = new HashMap<String, Object>();
			hash.put("code", "317");
			hash.put("name", "上季度在线监测超标情况");
			childList.add(hash);
		} else if ("32".equals(code)) {
			HashMap<String, Object> hash = new HashMap<String, Object>();
			hash.put("code", "320");
			hash.put("name", "一般固体废物处置情况");
			childList.add(hash);
			hash = new HashMap<String, Object>();
			hash.put("code", "321");
			hash.put("name", "危险废物暂存情况");
			childList.add(hash);
			hash = new HashMap<String, Object>();
			hash.put("code", "322");
			hash.put("name", "危险废物转移情况");
			childList.add(hash);
		} else if ("33".equals(code)) {
			HashMap<String, Object> hash = new HashMap<String, Object>();
			hash.put("code", "330");
			hash.put("name", "功能区域");
			childList.add(hash);
			hash = new HashMap<String, Object>();
			hash.put("code", "331");
			hash.put("name", "排放标准");
			childList.add(hash);
			hash = new HashMap<String, Object>();
			hash.put("code", "332");
			hash.put("name", "监测超标情况");
			childList.add(hash);
			hash = new HashMap<String, Object>();
			hash.put("code", "333");
			hash.put("name", "超标原因");
			childList.add(hash);
		}

		else if ("40".equals(code)) {
			HashMap<String, Object> hash = new HashMap<String, Object>();
			hash.put("code", "400");
			hash.put("name", "存在环境问题及意见");
			childList.add(hash);

		}

		LIST_SIZT = childList.size();
		// 初始化布局控件
		initViews(this);

		// 获取任务状态
		String status = rwxx.getTaskStatusFromTaskEnpriLink(taskid, entid);
		// 是否执行,0:未执行,1:调查取证完成,2:执法中 当状态是1的时候不能编辑
		// if (RWXX.TaskEnpriLink_isexcute.equals(status)) {
		String selectZWsql = "select zw from PC_Users where UserID = '" + Global.getGlobalInstance().getUserid() + "'";
		String ZW = SqliteUtil.getInstance().getDepidByDepName(selectZWsql);
		// String taskUserSql = "select fbr from T_YDZF_RWXX where rwbh = '" +
		// taskid + "'";
		// String task_userId =
		// SqliteUtil.getInstance().getDepidByUserid(taskUserSql);
		// String user = Global.getGlobalInstance().getUserid();
		if ("3".equals(status)) {
			//BYK rwzt
//			if ("1".equals(status)) {
		//	if (!rwxx.JudgeUserName(taskid)) {
				isEditable = false;
				Toast.makeText(this, "当前企业调查取证已完成，已不能修改", Toast.LENGTH_LONG).show();
			//}
			if ("009".equals(new RWXX().queryTaskStatus(taskid))) {
				Toast.makeText(this, "当前企业已归档，不能对笔录进行编辑", Toast.LENGTH_LONG).show();
				isEditable = false;
			}
		}
		if (currentapiVersion <= 10) {
			getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
			getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_UNSPECIFIED);
			getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
		} else {
			getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

		}
	}

	/**
	 * 初始化公共父类的变量
	 */
	private void initGlobal() {
		CURRENT_ID = taskid;
		QYID = entid;
		FK_ID = CURRENT_ID + "_" + QYID;
	}

	/**
	 * Description: 初始化布局 设置控件监听器
	 * 
	 * @param context
	 * @author Administrator<br>
	 *         Create at: 2012-12-4 上午11:20:56
	 */
	private void initViews(Context context) {
		// 父类布局
		((LinearLayout) findViewById(R.id.ui_mapuni_divider)).setVisibility(View.GONE);

		fatherRelativeLayout = (RelativeLayout) findViewById(R.id.parentLayout);
		SetBaseStyle(fatherRelativeLayout, name);
//		queryImg.setVisibility(View.VISIBLE);
//		queryImg.setImageResource(R.drawable.base_add_task_new);
//		queryImg.setOnClickListener(this);

		setTitleLayoutVisiable(true);// 标题栏是否可见

		// 子类布局，添加到父类布局中
		LayoutInflater mInflater = LayoutInflater.from(context);
		ScrollView scrollView = (ScrollView) mInflater.inflate(R.layout.specialitem, null);
		((LinearLayout) findViewById(R.id.middleLayout)).addView(scrollView, new LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.FILL_PARENT));

		// 获取ExpandableListView
		listView = (CustomExpandableListView) scrollView.findViewById(android.R.id.list);
		listView.setAdapter(new MyExpandableAdapter(this, childList));
		for (int i = 0; i < childList.size(); i++) {
			tempList.add("");
			tempHash.put(childList.get(i).get("code").toString(), true);
		}
		ArrayList<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();
		String sqlStr = "select * from YDZF_SiteEnvironmentMonitorRecord where taskid = '" + taskid + "' and entid = '" + entid + "'";
		list = SqliteUtil.getInstance().queryBySqlReturnArrayListHashMap(sqlStr);

		// 如果数据库有数据，则更新数据。反之，则插入数据
		if (list.size() == 0) {
			lastResult = new LastResult(UUID.randomUUID().toString(), taskid, entid, "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "",
					"", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "");
			// insertTaskResult();
		} else {
			// 对结果进行加载

			HashMap<String, Object> hashmap = list.get(0);
			lastResult = new LastResult(hashmap.get("guid").toString(), hashmap.get("taskid").toString(), hashmap.get("entid").toString(), hashmap.get("scqk_sczk").toString(),
					hashmap.get("scqk_ylyl").toString(), hashmap.get("scqk_cpcl").toString(), hashmap.get("jfqk_fs").toString(), hashmap.get("jfqk_fq").toString(), hashmap.get(
							"jfqk_zs").toString(), hashmap.get("jfqk_fz").toString(), hashmap.get("hbzd_wspjs").toString(), hashmap.get("hbzd_waqys").toString(), hashmap.get(
							"wrzl_fs").toString(), hashmap.get("wrzl_fq").toString(), hashmap.get("wrzl_wrw").toString(), hashmap.get("wrzl_gtfw").toString(), hashmap.get(
							"wrzl_zs").toString(), hashmap.get("wrzl_xws").toString(), hashmap.get("wrw_fs_pfqx").toString(), hashmap.get("wrw_fs_yznd").toString(), hashmap.get(
							"wrw_fs_pfbz").toString(), hashmap.get("wrw_fs_cbyy").toString(), hashmap.get("wrw_fs_zxjc").toString(), hashmap.get("wrw_fs_cbyy1").toString(),
					hashmap.get("wrw_fs_jdjc").toString(), hashmap.get("wrw_fs_zxjccb").toString(), hashmap.get("wrw_fq_khsd").toString(), hashmap.get("wrw_fq_yznd").toString(),
					hashmap.get("wrw_fq_pfbz").toString(), hashmap.get("wrw_fq_cbyy").toString(), hashmap.get("wrw_fq_zxjc").toString(), hashmap.get("wrw_fq_cbyy1").toString(),
					hashmap.get("wrw_fq_jdjc").toString(), hashmap.get("wrw_fq_zxjccb").toString(), hashmap.get("wrw_gtfw_fwcz").toString(), hashmap.get("wrw_gtfw_fwzc")
							.toString(), hashmap.get("wrw_gtfw_fwzy").toString(), hashmap.get("wrw_zs_gnqy").toString(), hashmap.get("wrw_zs_pfbz").toString(), hashmap.get(
							"wrw_zs_jccb").toString(), hashmap.get("wrw_zs_cbyy").toString(), hashmap.get("czhjwt").toString(), hashmap.get("xcjcjg").toString(), hashmap.get(
							"jcrzfzh").toString(), hashmap.get("bdcdwqz").toString(), hashmap.get("qzrq").toString(), hashmap.get("updatetime").toString());

		}

		// 设置背景图片、图片分割线
		listView.setGroupIndicator(this.getResources().getDrawable(R.layout.expandablelistviewselector));
		listView.setDivider(getResources().getDrawable(R.drawable.list_divider));
		listView.setChildDivider(getResources().getDrawable(R.drawable.list_divider));
		// viewGroup会优先其子控件而获得焦点
		listView.setDescendantFocusability(ViewGroup.FOCUS_BEFORE_DESCENDANTS);
		// 默认展开第一项
		listView.expandGroup(0);

		/** 监听Group打开 expandGroupID保存实时打开的GroupID */
		listView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {

			@Override
			public void onGroupExpand(int groupPosition) {
				listView.setSelectionFromTop(groupPosition - 1, 10);
				if (expandGroupID != groupPosition) {
					// 收起当前的Group
					listView.collapseGroup(expandGroupID);
				}
				// 打开下一个Group
				expandGroupID = groupPosition;
			}
		});

		/** 监听，ViewGroup收起 */
		listView.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {

			@Override
			public void onGroupCollapse(int groupPosition) {
				specialItemText = "";
				// loadLastBoolean = true;
			}
		});

		/** 监听，ViewGroup子类点击事件 */
		listView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {

			@Override
			public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
				// viewGroup只有当其子控件不需要焦点的时候才获得焦点
				listView.setDescendantFocusability(ViewGroup.FOCUS_AFTER_DESCENDANTS);
				return true;
			}

		});
	}

	/**
	 * FileName: SpecialItemActivity.java Description:ExpandableList数据适配器
	 * 
	 * @author Administrator
	 * @Version 1.3.4
	 * @Copyright 中科宇图天下科技有限公司<br>
	 *            Create at: 2012-12-4 上午11:21:11
	 */
	private final class MyExpandableAdapter extends BaseExpandableListAdapter {
		private final Context mContext;
		private final List<HashMap<String, Object>> childNodeList;
		LayoutInflater mInflater;

		public MyExpandableAdapter(Context context, List<HashMap<String, Object>> nodeList) {
			mContext = context;
			childNodeList = nodeList;
			mInflater = LayoutInflater.from(context);
		}

		@Override
		public Object getChild(int groupPosition, int childPosition) {
			return 1;
		}

		@Override
		public long getChildId(int groupPosition, int childPosition) {
			return childPosition;
		}

		@Override
		public View getChildView(final int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {

			AbsListView.LayoutParams lp = new AbsListView.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

			View view = mInflater.inflate(R.layout.specialitem_child, null);
			final LinearLayout resultLayout = (LinearLayout) view.findViewById(R.id.result_layout);
			resultLayout.setLayoutParams(lp);

			// 当前专项
			// 构建结果布局
			HashMap<String, Object> currNode = childNodeList.get(groupPosition);

			final String code = currNode.get("code").toString();

			try {
				// 单选按钮的code
				if ("100".equals(code)) {
					final ArrayList<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();
					HashMap<String, Object> hash = new HashMap<String, Object>();
					hash.put("code", "0");
					hash.put("value", "正常生产");
					list.add(hash);
					hash = new HashMap<String, Object>();
					hash.put("code", "1");
					hash.put("value", "停产");
					list.add(hash);

					ArrayList<HashMap<String, Object>> valueList = new ArrayList<HashMap<String, Object>>();
					String sqlStr = "select * from YDZF_SiteEnvironmentMonitorRecord where taskid = '" + taskid + "' and entid = '" + entid + "'";
					valueList = SqliteUtil.getInstance().queryBySqlReturnArrayListHashMap(sqlStr);
					String value = "";
					if (valueList.size() > 0) {
						value = valueList.get(0).get("scqk_sczk").toString();
					}

					final RadioGroup group = new RadioGroup(mContext);
					group.setPadding(15, 0, 5, 0);
					for (HashMap<String, Object> map : list) {
						String resultText = (String) map.get("value");
						final RadioButton radioButton = new RadioButton(mContext);
						radioButton.setText(resultText);
						radioButton.setTextColor(Color.BLACK);
						radioButton.setClickable(isEditable);
						// 设置默认选中
						if (resultText.equals(value)) {
							radioButton.setChecked(true);
							radioButton.setOnClickListener(new OnClickListener() {
								@Override
								public void onClick(View v) {

								}
							});

						}
						group.addView(radioButton);
					}
					resultLayout.addView(group);// 添加到布局中
					// 监听，保存
					group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

						@Override
						public void onCheckedChanged(RadioGroup group, int checkedId) {
							isClick = true;
							RadioButton rb = (RadioButton) resultLayout.findViewById(group.getCheckedRadioButtonId());
							String rbtext = rb.getText().toString();// 取得被选择的RadioButton中的文本内容

							String j = code.substring(code.length() - 1);
							int n = Integer.parseInt(j);
							tempList.set(n, rbtext);

							intent1 = new Intent();
							intent1.putExtra("isClick", isClick);
							setResult(Integer.valueOf(tag), intent1);

							// 向数据库中保存数据
							saveOrUpdateData(rbtext, code);
							tempHash.put(code, false);

							// ListView的动作:收起当前项、打开下一项
							listCollapseAction();
						}

					});

				}// 只用填文本的code
				else {

					View childview = mInflater.inflate(R.layout.specialitem_edit_button, null);

					// 提示文本框
					TextView noteTextView = new TextView(mContext);
					noteTextView.setPadding(25, 0, 5, 5);
					noteTextView.setText("请输入：");

					resultLayout.addView(noteTextView);// 添加到布局中

					// 输入文本框
					final EditText editTextView = (EditText) childview.findViewById(R.id.specialitem_edit_button_editText);
					final String twoChar = code.substring(0, code.length() - 1);
					if ("12".equals(twoChar)) {
						editTextView.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
					}
					if (tempHash.get(code)) {
						ArrayList<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();
						String sqlStr = "select * from YDZF_SiteEnvironmentMonitorRecord where taskid = '" + taskid + "' and entid = '" + entid + "'";
						list = SqliteUtil.getInstance().queryBySqlReturnArrayListHashMap(sqlStr);
						if (list.size() > 0) {
							String into = list.get(0).get(codeSwitchField(code).toLowerCase()).toString();
							editTextView.setText(into);
						}
					} else {
						String lastChar = code.substring(code.length() - 1);
						for (int i = 0; i < tempList.size(); i++) {
							if (i == Integer.parseInt(lastChar)) {
								String temp = tempList.get(i);
								if (temp != null && !temp.equals("")) {
									editTextView.setText(temp);
								}
							}
						}
					}

					editTextView.setWidth(250);
					editTextView.setFocusable(isEditable);
					// editTextView.requestFocus();
					setInputState(editTextView);
					editTextView.setFilters(new InputFilter[] { new InputFilter.LengthFilter(100) });
					if (isEditable) {
						editTextView.setEnabled(true);
					}
					// 直接给予焦点

					// 按钮
					Button putButton = (Button) childview.findViewById(R.id.specialitem_edit_button_button);

					putButton.setFocusable(false);

					resultLayout.addView(childview);
					putButton.setOnClickListener(new View.OnClickListener() {

						@Override
						public void onClick(View v) {
							isClick = true;
							String specialItemResult = editTextView.getText().toString().trim();
							intent1 = new Intent();
							intent1.putExtra("isClick", isClick);
							setResult(Integer.valueOf(tag), intent1);
							if ("12".equals(twoChar) && "".equals(specialItemResult)) {
							} else {
								String j = code.substring(code.length() - 1);
								int n = Integer.parseInt(j);
								tempList.set(n, specialItemResult);
								// 向数据库中保存数据
								saveOrUpdateData(specialItemResult, code);
								tempHash.put(code, false);
							}
							listCollapseAction();
						}
					});

				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			// }

			return resultLayout;
		}

		@Override
		public int getChildrenCount(int groupPosition) {
			return 1;
		}

		@Override
		public Object getGroup(int groupPosition) {
			return childNodeList.get(groupPosition);
		}

		@Override
		public int getGroupCount() {
			return childNodeList.size();
		}

		@Override
		public long getGroupId(int groupPosition) {
			return groupPosition;
		}

		@Override
		public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
			LayoutInflater mInflater = LayoutInflater.from(mContext);
			convertView = mInflater.inflate(R.layout.specialitem_group, null);

			TextView groupName = (TextView) convertView.findViewById(R.id.specialitem_group_title);
			HashMap<String, Object> currNode = childNodeList.get(groupPosition);

			groupName.setText(currNode.get("name").toString());
			queryNodeResult(currNode, groupName);

			// 查询数据库，实时赋值
			return convertView;
		}

		@Override
		public boolean hasStableIds() {
			return true;
		}

		@Override
		public boolean isChildSelectable(int groupPosition, int childPosition) {
			return true;
		}

	}

	/**
	 * CheckGroup的点击事件监听
	 * 
	 * @author Liusy 2012-7-4
	 */
	/*
	 * private final class CheckGroupListener implements OnCheckedChangeListener
	 * {
	 * 
	 * @Override public void onCheckedChanged(RadioGroup group, int checkedId) {
	 * System.out.println(checkedId);
	 * 
	 * //点击后收回之前打开的Group listView.collapseGroup(expandGroupID); //打开下一个Group int
	 * index = expandGroupID + 1; if(index == 6) { return; }
	 * listView.expandGroup(index); }
	 * 
	 * }
	 */

	/**
	 * ListView的动作 展开与收起
	 */
	private void listCollapseAction() {
		// 点击后收回之前打开的Group
		listView.collapseGroup(expandGroupID);
		// 打开下一个Group
		int index = expandGroupID + 1;
		if (index == LIST_SIZT) {
			return;
		}
		listView.expandGroup(index);

		Log.i("TAG", "显示--->" + expandGroupID);
	}

	/**
	 * FileName: SpecialItemActivity.java Description:上次结果的对象
	 * 
	 * @author Administrator
	 * @Version 1.3.4
	 * @Copyright 中科宇图天下科技有限公司<br>
	 *            Create at: 2012-12-4 上午11:22:57
	 */
	private final class LastResult {
		public String Guid; // 唯一标识
		public String TaskId; // 任务编号
		public String EntId; // 企业编号
		public String SCQK_SCZK; // 目前生产状况
		public String SCQK_YLYL; // 主要原料及用量
		public String SCQK_CPCL; // 主要产品及产量
		public String JFQK_FS; // 废水缴纳情况
		public String JFQK_FQ; // 废气缴纳情况
		public String JFQK_ZS; // 噪声缴纳情况
		public String JFQK_FZ; // 废渣缴纳情况
		public String HBZD_WSPJS; // 未经审批建设情况
		public String HBZD_WAQYS; // 未按期验收情况
		public String WRZL_FS; // 废水治理设施建设运行情况
		public String WRZL_FQ; // 废气治理设施运行情况
		public String WRZL_WRW; // 污染物自动监控系统运行情况
		public String WRZL_GTFW; // 固体废物治理设施运行情况
		public String WRZL_ZS; // 噪声治理设施运行情况
		public String WRZL_XWS; // 新污水地方排放标准后污水处理设施升级改造
		public String WRW_FS_PFQX; // 废水排放去向
		public String WRW_FS_YZND; // 废水监测超标因子及浓度
		public String WRW_FS_PFBZ; // 废水超标因子排放标准
		public String WRW_FS_CBYY; // 废水超标原因
		public String WRW_FS_ZXJC; // 废水在线监测超标情况
		public String WRW_FS_CBYY1; // 废水超标原因1
		public String WRW_FS_JDJC; // 废水上季度监督性监测超标情况
		public String WRW_FS_ZXJCCB; // 废水上季度在线监测超标情况
		public String WRW_FQ_KHSD; // 废气考核时段
		public String WRW_FQ_YZND; // 废气监测超标因子及浓度
		public String WRW_FQ_PFBZ; // 废气超标因子排放标准
		public String WRW_FQ_CBYY; // 废气超标原因
		public String WRW_FQ_ZXJC; // 废气在线监测超标情况
		public String WRW_FQ_CBYY1; // 废气超标原因1
		public String WRW_FQ_JDJC; // 废气上季度监督性监测超标情况
		public String WRW_FQ_ZXJCCB; // 废气上季度在线监测超标情况
		public String WRW_GTFW_FWCZ; // 固体废物一般固体废物处置情况
		public String WRW_GTFW_FWZC; // 固体废物危险废物暂存情况
		public String WRW_GTFW_FWZY; // 固体废物危险废物转移情况
		public String WRW_ZS_GNQY; // 噪声功能区域
		public String WRW_ZS_PFBZ; // 噪声排放标准
		public String WRW_ZS_JCCB; // 噪声监测超标情况
		public String WRW_ZS_CBYY; // 噪声超标原因
		public String CZHJWT; // 存在环境问题
		public String XCJCJG; // 现场检查机构
		public String JCRZFZH; // 现场监察人员及执法证号
		public String BDCDWQZ; // 被调查单位负责人签字
		public String QZRQ; // 签字日期
		public String Updatetime; // 更新日期

		// 含参数构造
		// 初始赋值：任务编号，企业编号，模板编号、专项编号
		public LastResult(String Guid, String TaskId, String EntId, String SCQK_SCZK, String SCQK_YLYL, String SCQK_CPCL, String JFQK_FS, String JFQK_FQ, String JFQK_ZS,
				String JFQK_FZ, String HBZD_WSPJS, String HBZD_WAQYS, String WRZL_FS, String WRZL_FQ, String WRZL_WRW, String WRZL_GTFW, String WRZL_ZS, String WRZL_XWS,
				String WRW_FS_PFQX, String WRW_FS_YZND, String WRW_FS_PFBZ, String WRW_FS_CBYY, String WRW_FS_ZXJC, String WRW_FS_CBYY1, String WRW_FS_JDJC, String WRW_FS_ZXJCCB,
				String WRW_FQ_KHSD, String WRW_FQ_YZND, String WRW_FQ_PFBZ, String WRW_FQ_CBYY, String WRW_FQ_ZXJC, String WRW_FQ_CBYY1, String WRW_FQ_JDJC, String WRW_FQ_ZXJCCB,
				String WRW_GTFW_FWCZ, String WRW_GTFW_FWZC, String WRW_GTFW_FWZY, String WRW_ZS_GNQY, String WRW_ZS_PFBZ, String WRW_ZS_JCCB, String WRW_ZS_CBYY, String CZHJWT,
				String XCJCJG, String JCRZFZH, String BDCDWQZ, String QZRQ, String Updatetime) {
			super();
			this.Guid = Guid;
			this.TaskId = TaskId; // 任务编号
			this.EntId = EntId; // 企业编号
			this.SCQK_SCZK = SCQK_SCZK; // 目前生产状况
			this.SCQK_YLYL = SCQK_YLYL; // 主要原料及用量
			this.SCQK_CPCL = SCQK_CPCL; // 主要产品及产量
			this.JFQK_FS = JFQK_FS; // 废水缴纳情况
			this.JFQK_FQ = JFQK_FQ; // 废气缴纳情况
			this.JFQK_ZS = JFQK_ZS; // 噪声缴纳情况
			this.JFQK_FZ = JFQK_FZ; // 废渣缴纳情况
			this.HBZD_WSPJS = HBZD_WSPJS; // 未经审批建设情况
			this.HBZD_WAQYS = HBZD_WAQYS; // 未按期验收情况
			this.WRZL_FS = WRZL_FS; // 废水治理设施建设运行情况
			this.WRZL_FQ = WRZL_FQ; // 废气治理设施运行情况
			this.WRZL_WRW = WRZL_WRW; // 污染物自动监控系统运行情况
			this.WRZL_GTFW = WRZL_GTFW; // 固体废物治理设施运行情况
			this.WRZL_ZS = WRZL_ZS; // 噪声治理设施运行情况
			this.WRZL_XWS = WRZL_XWS; // 新污水地方排放标准后污水处理设施升级改造
			this.WRW_FS_PFQX = WRW_FS_PFQX; // 废水排放去向
			this.WRW_FS_YZND = WRW_FS_YZND; // 废水监测超标因子及浓度
			this.WRW_FS_PFBZ = WRW_FS_PFBZ; // 废水超标因子排放标准
			this.WRW_FS_CBYY = WRW_FS_CBYY; // 废水超标原因
			this.WRW_FS_ZXJC = WRW_FS_ZXJC; // 废水在线监测超标情况
			this.WRW_FS_CBYY1 = WRW_FS_CBYY1; // 废水超标原因1
			this.WRW_FS_JDJC = WRW_FS_JDJC; // 废水上季度监督性监测超标情况
			this.WRW_FS_ZXJCCB = WRW_FS_ZXJCCB; // 废水上季度在线监测超标情况
			this.WRW_FQ_KHSD = WRW_FQ_KHSD; // 废气考核时段
			this.WRW_FQ_YZND = WRW_FQ_YZND; // 废气监测超标因子及浓度
			this.WRW_FQ_PFBZ = WRW_FQ_PFBZ; // 废气超标因子排放标准
			this.WRW_FQ_CBYY = WRW_FQ_CBYY; // 废气超标原因
			this.WRW_FQ_ZXJC = WRW_FQ_ZXJC; // 废气在线监测超标情况
			this.WRW_FQ_CBYY1 = WRW_FQ_CBYY1; // 废气超标原因1
			this.WRW_FQ_JDJC = WRW_FQ_JDJC; // 废气上季度监督性监测超标情况
			this.WRW_FQ_ZXJCCB = WRW_FQ_ZXJCCB; // 废气上季度在线监测超标情况
			this.WRW_GTFW_FWCZ = WRW_GTFW_FWCZ; // 固体废物一般固体废物处置情况
			this.WRW_GTFW_FWZC = WRW_GTFW_FWZC; // 固体废物危险废物暂存情况
			this.WRW_GTFW_FWZY = WRW_GTFW_FWZY; // 固体废物危险废物转移情况
			this.WRW_ZS_GNQY = WRW_ZS_GNQY; // 噪声功能区域
			this.WRW_ZS_PFBZ = WRW_ZS_PFBZ; // 噪声排放标准
			this.WRW_ZS_JCCB = WRW_ZS_JCCB; // 噪声监测超标情况
			this.WRW_ZS_CBYY = WRW_ZS_CBYY; // 噪声超标原因
			this.CZHJWT = CZHJWT; // 存在环境问题
			this.XCJCJG = XCJCJG; // 现场检查机构
			this.JCRZFZH = JCRZFZH; // 现场监察人员及执法证号
			this.BDCDWQZ = BDCDWQZ; // 被调查单位负责人签字
			this.QZRQ = QZRQ; // 签字日期
			this.Updatetime = Updatetime; // 更新日期
		}
	}

	/**
	 * 查询数据库 通过任务ID，专项ID，获取结果
	 * 
	 * @param qyID
	 *            企业ID
	 * @return 上次选择信息的文本
	 * @author Administrator<br>
	 *         Create at: 2012-12-4 上午11:20:56
	 */
	private String getLastTaskResultText(String rwID, String qyID, String specialItemID) {
		String text = "";
		ArrayList<HashMap<String, Object>> data = new ArrayList<HashMap<String, Object>>();
		String sql = "SELECT RName FROM YDZF_ResultType WHERE ID = " + "(SELECT SpecialItemResult FROM YDZF_TaskSpecialItem WHERE TaskID ='" + rwID + "' and EnterID='" + qyID
				+ "' and SpecialItemID ='" + specialItemID + "')";
		try {
			data = SQLiteDataProvider.getInstance().queryBySqlReturnArrayListHashMap(sql);
		} catch (Exception e) {
			Log.i(TAG, e.getMessage());
		}
		if (data != null && data.size() > 0) {
			for (HashMap<String, Object> map : data) {
				text = (String) map.get("rname");
			}
		}

		return text;
	}

	/**
	 * 查询数据库 获取其结果类型：文本、单选框
	 * 
	 * @param resultTypeID
	 *            结果类型ID
	 * @author Administrator<br>
	 *         Create at: 2012-12-4 上午11:20:56
	 */
	private ArrayList<HashMap<String, Object>> getResultTypeData(String resultTypeID) {
		ArrayList<HashMap<String, Object>> data = new ArrayList<HashMap<String, Object>>();
		String sql = "";
		sql = "select ID,RName,ControlType from YDZF_ResultType where PID = '" + resultTypeID + "' order by sortIndex ";
		try {
			data = SQLiteDataProvider.getInstance().queryBySqlReturnArrayListHashMap(sql);
		} catch (Exception e) {
			Log.i(TAG, e.getMessage());
		}
		if (data.size() == 0) {
			sql = "select RName,ControlType from YDZF_ResultType where ID = '" + resultTypeID + "' order by sortIndex ";
			try {
				data = SQLiteDataProvider.getInstance().queryBySqlReturnArrayListHashMap(sql);
			} catch (Exception e) {
				Log.i(TAG, e.getMessage());
			}
			return data;
		}
		return data;
	}

	private void saveOrUpdateData(String specialItemResult, String code) {
		ArrayList<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();
		String sqlStr = "select * from YDZF_SiteEnvironmentMonitorRecord where taskid = '" + taskid + "' and entid = '" + entid + "'";
		list = SqliteUtil.getInstance().queryBySqlReturnArrayListHashMap(sqlStr);
		// 如果数据库有数据，则更新数据。反之，则插入数据
		if (list.size() > 0) {
			ContentValues contentValues = new ContentValues();
			contentValues.put(codeSwitchField(code), specialItemResult);
			contentValues.put("Updatetime", Global.getGlobalInstance().getDate());
			contentValues.put("QZRQ", Global.getGlobalInstance().getDate());
			Log.i(TAG, "Update !");
			if (SQLiteDataProvider.getInstance().updateTable(RESULT_TABLE, contentValues, " taskid = ?  and entid = ?", new String[] { taskid, entid })) {
				Log.i(TAG, "Update Success");
			}
		} else {
			codeSwitchFieldAndAssign(code, specialItemResult);
			if (insertTaskResult()) {
				Log.i(TAG, "Insert Success");

			}
		}
	}

	private String codeSwitchField(String code) {
		String field = "";
		int codeInt = Integer.parseInt(code);
		switch (codeInt) {
		case 100:
			field = "SCQK_SCZK";
			break;
		case 101:
			field = "SCQK_YLYL";
			break;
		case 102:
			field = "SCQK_CPCL";
			break;

		case 110:
			field = "HBZD_WSPJS";
			break;
		case 111:
			field = "HBZD_WAQYS";
			break;

		case 120:
			field = "JFQK_FS";
			break;
		case 121:
			field = "JFQK_FQ";
			break;
		case 122:
			field = "JFQK_ZS";
			break;
		case 123:
			field = "JFQK_FZ";
			break;

		case 200:
			field = "WRZL_FS";
			break;
		case 201:
			field = "WRZL_FQ";
			break;
		case 202:
			field = "WRZL_WRW";
			break;
		case 203:
			field = "WRZL_GTFW";
			break;
		case 204:
			field = "WRZL_ZS";
			break;
		case 205:
			field = "WRZL_XWS";
			break;

		case 300:
			field = "WRW_FS_PFQX";
			break;
		case 301:
			field = "WRW_FS_YZND";
			break;
		case 302:
			field = "WRW_FS_PFBZ";
			break;
		case 303:
			field = "WRW_FS_CBYY";
			break;
		case 304:
			field = "WRW_FS_ZXJC";
			break;
		case 305:
			field = "WRW_FS_CBYY1";
			break;
		case 306:
			field = "WRW_FS_JDJC";
			break;
		case 307:
			field = "WRW_FS_ZXJCCB";
			break;

		case 310:
			field = "WRW_FQ_KHSD";
			break;
		case 311:
			field = "WRW_FQ_YZND";
			break;
		case 312:
			field = "WRW_FQ_PFBZ";
			break;
		case 313:
			field = "WRW_FQ_CBYY";
			break;
		case 314:
			field = "WRW_FQ_ZXJC";
			break;
		case 315:
			field = "WRW_FQ_CBYY1";
			break;
		case 316:
			field = "WRW_FQ_JDJC";
			break;
		case 317:
			field = "WRW_FQ_ZXJCCB";
			break;

		case 320:
			field = "WRW_GTFW_FWCZ";
			break;
		case 321:
			field = "WRW_GTFW_FWZC";
			break;
		case 322:
			field = "WRW_GTFW_FWZY";
			break;

		case 330:
			field = "WRW_ZS_GNQY";
			break;
		case 331:
			field = "WRW_ZS_PFBZ";
			break;
		case 332:
			field = "WRW_ZS_JCCB";
			break;
		case 333:
			field = "WRW_ZS_CBYY";
			break;

		case 400:
			field = "CZHJWT";
			break;

		default:
			field = "";
			break;
		}
		return field;
	}

	private void codeSwitchFieldAndAssign(String code, String value) {

		int codeInt = Integer.parseInt(code);
		switch (codeInt) {
		case 100:
			lastResult.SCQK_SCZK = value;
			break;
		case 101:
			lastResult.SCQK_YLYL = value;
			break;
		case 102:
			lastResult.SCQK_CPCL = value;
			break;

		case 110:
			lastResult.HBZD_WSPJS = value;
			break;
		case 111:
			lastResult.HBZD_WAQYS = value;
			break;

		case 120:
			lastResult.JFQK_FS = value;
			break;
		case 121:
			lastResult.JFQK_FQ = value;
			break;
		case 122:
			lastResult.JFQK_ZS = value;
			break;
		case 123:
			lastResult.JFQK_FZ = value;
			break;

		case 200:
			lastResult.WRZL_FS = value;
			break;
		case 201:
			lastResult.WRZL_FQ = value;
			break;
		case 202:
			lastResult.WRZL_WRW = value;
			break;
		case 203:
			lastResult.WRZL_GTFW = value;
			break;
		case 204:
			lastResult.WRZL_ZS = value;
			break;
		case 205:
			lastResult.WRZL_XWS = value;
			break;

		case 300:
			lastResult.WRW_FS_PFQX = value;
			break;
		case 301:
			lastResult.WRW_FS_YZND = value;
			break;
		case 302:
			lastResult.WRW_FS_PFBZ = value;
			break;
		case 303:
			lastResult.WRW_FS_CBYY = value;
			break;
		case 304:
			lastResult.WRW_FS_ZXJC = value;
			break;
		case 305:
			lastResult.WRW_FS_CBYY1 = value;
			break;
		case 306:
			lastResult.WRW_FS_JDJC = value;
			break;
		case 307:
			lastResult.WRW_FS_ZXJCCB = value;
			break;

		case 310:
			lastResult.WRW_FQ_KHSD = value;
			break;
		case 311:
			lastResult.WRW_FQ_YZND = value;
			break;
		case 312:
			lastResult.WRW_FQ_PFBZ = value;
			break;
		case 313:
			lastResult.WRW_FQ_CBYY = value;
			break;
		case 314:
			lastResult.WRW_FQ_ZXJC = value;
			break;
		case 315:
			lastResult.WRW_FQ_CBYY1 = value;
			break;
		case 316:
			lastResult.WRW_FQ_JDJC = value;
			break;
		case 317:
			lastResult.WRW_FQ_ZXJCCB = value;
			break;

		case 320:
			lastResult.WRW_GTFW_FWCZ = value;
			break;
		case 321:
			lastResult.WRW_GTFW_FWZC = value;
			break;
		case 322:
			lastResult.WRW_GTFW_FWZY = value;
			break;

		case 330:
			lastResult.WRW_ZS_GNQY = value;
			break;
		case 331:
			lastResult.WRW_ZS_PFBZ = value;
			break;
		case 332:
			lastResult.WRW_ZS_JCCB = value;
			break;
		case 333:
			lastResult.WRW_ZS_CBYY = value;
			break;

		case 400:
			lastResult.CZHJWT = value;
			break;
		}
	}

	/*
	 * private void deleteDataInDB(final LastResult lastResult, String result,
	 * final String remark, final String surveyTime){ String rwID =
	 * lastResult.TaskID; String qyID = lastResult.EnterID; String industryID =
	 * lastResult.IndustryID; String specialItemID = lastResult.SpecialItemID;
	 * String sql="delete from YDZF_TaskSpecialItem where TaskID='"+rwID
	 * +"' and EnterID ='"+qyID+ "' and IndustryID ='"+industryID+
	 * "' and SpecialItemID ='"+specialItemID+"'";
	 * SqliteUtil.getInstance().deleteCompanyBySql(sql); }
	 */

	private boolean insertTaskResult() {

		String sql = "insert into YDZF_SiteEnvironmentMonitorRecord(Guid,TaskId,EntId,SCQK_SCZK,SCQK_YLYL"
				+ ",SCQK_CPCL,JFQK_FS,JFQK_FQ,JFQK_ZS,JFQK_FZ,HBZD_WSPJS,HBZD_WAQYS,WRZL_FS," + "WRZL_FQ,WRZL_WRW,WRZL_GTFW,WRZL_ZS,WRZL_XWS,WRW_FS_PFQX,WRW_FS_YZND,"
				+ "WRW_FS_PFBZ,WRW_FS_CBYY,WRW_FS_ZXJC,WRW_FS_CBYY1,WRW_FS_JDJC," + "WRW_FS_ZXJCCB,WRW_FQ_KHSD,WRW_FQ_YZND,WRW_FQ_PFBZ,WRW_FQ_CBYY,"
				+ "WRW_FQ_ZXJC,WRW_FQ_CBYY1,WRW_FQ_JDJC,WRW_FQ_ZXJCCB,WRW_GTFW_FWCZ," + "WRW_GTFW_FWZC,WRW_GTFW_FWZY,WRW_ZS_GNQY,WRW_ZS_PFBZ,WRW_ZS_JCCB,"
				+ "WRW_ZS_CBYY,CZHJWT,XCJCJG,JCRZFZH,BDCDWQZ,QZRQ,Updatetime) " + "values('"
				+ lastResult.Guid
				+ "','"
				+ lastResult.TaskId
				+ "','"
				+ lastResult.EntId
				+ "','"
				+ lastResult.SCQK_SCZK
				+ "','"
				+ lastResult.SCQK_YLYL
				+ "','"
				+ lastResult.SCQK_CPCL
				+ "','"
				+ lastResult.JFQK_FS
				+ "','"
				+ lastResult.JFQK_FQ
				+ "','"
				+ lastResult.JFQK_ZS
				+ "','"
				+ lastResult.JFQK_FZ
				+ "','"
				+ lastResult.HBZD_WSPJS
				+ "','"
				+ lastResult.HBZD_WAQYS
				+ "','"
				+ lastResult.WRZL_FS
				+ "','"
				+ lastResult.WRZL_FQ
				+ "','"
				+ lastResult.WRZL_WRW
				+ "','"
				+ lastResult.WRZL_GTFW
				+ "','"
				+ lastResult.WRZL_ZS
				+ "','"
				+ lastResult.WRZL_XWS
				+ "','"
				+ lastResult.WRW_FS_PFQX
				+ "','"
				+ lastResult.WRW_FS_YZND
				+ "','"
				+ lastResult.WRW_FS_PFBZ
				+ "','"
				+ lastResult.WRW_FS_CBYY
				+ "','"
				+ lastResult.WRW_FS_ZXJC
				+ "','"
				+ lastResult.WRW_FS_CBYY1
				+ "','"
				+ lastResult.WRW_FS_JDJC
				+ "','"
				+ lastResult.WRW_FS_ZXJCCB
				+ "','"
				+ lastResult.WRW_FQ_KHSD
				+ "','"
				+ lastResult.WRW_FQ_YZND
				+ "','"
				+ lastResult.WRW_FQ_PFBZ
				+ "','"
				+ lastResult.WRW_FQ_CBYY
				+ "','"
				+ lastResult.WRW_FQ_ZXJC
				+ "','"
				+ lastResult.WRW_FQ_CBYY1
				+ "','"
				+ lastResult.WRW_FQ_JDJC
				+ "','"
				+ lastResult.WRW_FQ_ZXJCCB
				+ "','"
				+ lastResult.WRW_GTFW_FWCZ
				+ "','"
				+ lastResult.WRW_GTFW_FWZC
				+ "','"
				+ lastResult.WRW_GTFW_FWZY
				+ "','"
				+ lastResult.WRW_ZS_GNQY
				+ "','"
				+ lastResult.WRW_ZS_PFBZ
				+ "','"
				+ lastResult.WRW_ZS_JCCB
				+ "','"
				+ lastResult.WRW_ZS_CBYY
				+ "','"
				+ lastResult.CZHJWT
				+ "','"
				+ lastResult.XCJCJG
				+ "','"
				+ lastResult.JCRZFZH + "','" + lastResult.BDCDWQZ + "','" + Global.getGlobalInstance().getDate() + "','" + Global.getGlobalInstance().getDate() + "')";
		Log.i(TAG, sql);
		return SQLiteDataProvider.getInstance().ExecSQL(sql);
	}

	private void queryNodeResult(HashMap<String, Object> node, TextView tv) {
		String resultText = node.get("name").toString();
		String code = node.get("code").toString();
		String lastChar = code.substring(code.length() - 1);
		if (tempHash.get(code)) {
			ArrayList<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();
			String sqlStr = "select * from YDZF_SiteEnvironmentMonitorRecord where taskid = '" + taskid + "' and entid = '" + entid + "'";
			list = SqliteUtil.getInstance().queryBySqlReturnArrayListHashMap(sqlStr);
			if (list.size() > 0) {
				String into = list.get(0).get(codeSwitchField(code).toLowerCase()).toString();
				if (into != null && !into.equals("")) {
					into = FileHelper.formatTextLength(into, 6);
					String result = "<span><font color='#0288e7'>[" + into + "]</font></span>";
					if("120".equals(code)) {
						tv.setText(Html.fromHtml("废水" + "<font color='#0288e7'>[" + into + "]</font>" + "元/月"));
					} else if("121".equals(code)) {
						tv.setText(Html.fromHtml("废气" + "<font color='#0288e7'>[" + into + "]</font>" + "元/月"));
					} else if("122".equals(code)) {
						tv.setText(Html.fromHtml("噪声" + "<font color='#0288e7'>[" + into + "]</font>" + "元/月"));
					} else if("123".equals(code)) {
						tv.setText(Html.fromHtml("废渣" + "<font color='#0288e7'>[" + into + "]</font>" + "元/月"));
					} else {
						tv.setText(Html.fromHtml(resultText + result));
					}
				}
			}
		} else {
			for (int i = 0; i < tempList.size(); i++) {
				if (i == Integer.parseInt(lastChar)) {
					String temp = tempList.get(i);
					if (temp != null && !temp.equals("")) {
						temp = FileHelper.formatTextLength(temp, 6);
						String result = "<span><font color='#0288e7'>[" + temp + "]</font></span>";
						if("120".equals(code)) {
							tv.setText(Html.fromHtml("废水" + "<font color='#0288e7'>[" + temp + "]</font>" + "元/月"));
						} else if("121".equals(code)) {
							tv.setText(Html.fromHtml("废气" + "<font color='#0288e7'>[" + temp + "]</font>" + "元/月"));
						} else if("122".equals(code)) {
							tv.setText(Html.fromHtml("噪声" + "<font color='#0288e7'>[" + temp + "]</font>" + "元/月"));
						} else if("123".equals(code)) {
							tv.setText(Html.fromHtml("废渣" + "<font color='#0288e7'>[" + temp + "]</font>" + "元/月"));
						} else {
							tv.setText(Html.fromHtml(resultText + result));
						}
					}

				}
			}

		}
	}

	/**
	 * 关闭输入法
	 * 
	 * @param editText
	 */
	public void closeInputMethod(EditText editText) {
		if (imm == null) {
			imm = (InputMethodManager) getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);

		}
		imm.hideSoftInputFromWindow(editText.getApplicationWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

	}

	public void setInputState(EditText editText) {

		Log.i("wang", "当前手机系统版本号为-->" + currentapiVersion);
		if (currentapiVersion > 10) {
			editText.requestFocus();
		}

	}

	@Override
	public void onClick(View v) {
		// TODO:弹出popwindow
		if (pop != null) {
			if (pop.isShowing()) {
				pop.dismiss();
				return;
			}
		}
		LayoutInflater inflater = LayoutInflater.from(this);
		View popWindow = inflater.inflate(R.layout.special_item_record_pop, null);
		init(popWindow);
		pop = new PopupWindow(popWindow, 300, 242);
		pop.showAsDropDown(v, 13, 0);

	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		dismisPop();
		return super.dispatchTouchEvent(ev);
	}

	private void dismisPop() {
		if (pop != null && pop.isShowing()) {
			pop.dismiss();
		}
	}

	private void init(View p) {
		Button b1 = (Button) p.findViewById(R.id.button_pop_takePhoto);
		Button b2 = (Button) p.findViewById(R.id.button_pop_record);
		Button b3 = (Button) p.findViewById(R.id.button_pop_takeVedio);
		Button b4 = (Button) p.findViewById(R.id.button_pop_attachment);
		b1.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				dismisPop();
				takePhoto();
			}
		});
		b2.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				dismisPop();
				recordAudio();
			}
		});
		b3.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				dismisPop();
				recordVideo();
			}
		});
		b4.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				dismisPop();
				takefigure();
			}
		});
	}

	// 以下代码为popwindow的button事件监听，暂时使用强大的Control+CV

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		Log.i("info", "执行返回结果special");

	}

}