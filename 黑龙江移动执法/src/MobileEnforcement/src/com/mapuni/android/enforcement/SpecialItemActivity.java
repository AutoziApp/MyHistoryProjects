package com.mapuni.android.enforcement;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Html;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextUtils;
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
import com.mapuni.android.base.util.LogUtil;
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
public class SpecialItemActivity extends AttachmentBaseActivity implements OnClickListener {
	CustomExpandableListView listView = null;
	private int expandGroupID = 0;

	private PopupWindow pop;
	// 父页面布局
	private RelativeLayout fatherRelativeLayout;
	// private LinearLayout middleLayout;
	private Intent intent;
	private TreeNode frontdata = null;// 前一页面点击的Node
	private List<TreeNode> childNodeList;// 当前Node的所有叶子节点集合
	// 全局变量
	private int LIST_SIZT;
	private final String surveyTime = Global.getGlobalInstance().getDate();
	/** 结果类型ID */
	private String resultTypeId = "";
	/** 结果备注文本 */
	private String remarkResult = "";

	public final String RESULT_TABLE = "YDZF_TaskSpecialItem";
	private final String TAG = "SpecialItemActivity";

	private String specialItemText = "";
	private boolean loadLastBoolean = true;

	private boolean isEditable = true;
	private final RWXX rwxx = new RWXX();
	private InputMethodManager imm;
	private String qyid;
	/** 当前手机系统版本号 */
	private int currentapiVersion = android.os.Build.VERSION.SDK_INT;
	private String rwbh;
	private String     tname;
	private String tid;
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
		qyid = intent.getStringExtra("qyid");
		
		isSearch = intent.getBooleanExtra("isSearch", false);
		isEditable=!isSearch;
		//BYK
		rwbh = intent.getStringExtra("rwbh");
	    tname = intent.getStringExtra("tname");
	   tid = intent.getStringExtra("tid");
		
		frontdata = (TreeNode) intent.getExtras().get("node");// 获得前一页面传的Node对象

		initGlobal();
		childNodeList = frontdata.getChildren();
		LIST_SIZT = childNodeList.size();
		// 初始化布局控件
		initViews(this);

		// 获取任务状态
		String status = rwxx.getTaskStatusFromTaskEnpriLink(frontdata.rwID, qyid);
		// 是否执行,0:未执行,1:调查取证完成,2:执法中 当状态是1的时候不能编辑
		// if (RWXX.TaskEnpriLink_isexcute.equals(status)) {
		String selectZWsql = "select zw from PC_Users where UserID = '" + Global.getGlobalInstance().getUserid() + "'";
		String ZW = SqliteUtil.getInstance().getDepidByDepName(selectZWsql);
		// String taskUserSql = "select fbr from T_YDZF_RWXX where rwbh = '" +
		// frontdata.rwID + "'";
		// String task_userId =
		// SqliteUtil.getInstance().getDepidByUserid(taskUserSql);
		// String user = Global.getGlobalInstance().getUserid();
	//	if ("1".equals(status)) {
		//BYK rwzt
			if ("3".equals(status)) {
			//if (!rwxx.JudgeUserName(frontdata.rwID)) {
				isEditable = false;
				Toast.makeText(this, "当前企业调查取证已完成，已不能修改", Toast.LENGTH_LONG).show();
			//}
			if ("009".equals(new RWXX().queryTaskStatus(frontdata.rwID))) {
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
		CURRENT_ID = frontdata.rwID;
		QYID = qyid;
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

		if (frontdata.getChildren() != null) {
			// 设定基本布局
			Collections.sort(frontdata.getChildren());
			String a = frontdata.title;
			String b = a.substring(0, a.length() > 13 ? 13 : a.length());
			SetBaseStyle(fatherRelativeLayout, b + "...");

//			queryImg.setVisibility(View.VISIBLE);
//			queryImg.setImageResource(R.drawable.base_add_task_new);
//			queryImg.setOnClickListener(this);
		}
		setTitleLayoutVisiable(true);// 标题栏是否可见

		// 子类布局，添加到父类布局中
		LayoutInflater mInflater = LayoutInflater.from(context);
		ScrollView scrollView = (ScrollView) mInflater.inflate(R.layout.specialitem, null);
		((LinearLayout) findViewById(R.id.middleLayout)).addView(scrollView, new LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.FILL_PARENT));

		// 获取ExpandableListView
		listView = (CustomExpandableListView) scrollView.findViewById(android.R.id.list);
		listView.setAdapter(new MyExpandableAdapter(this, childNodeList));
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
				loadLastBoolean = true;
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
		private final List<TreeNode> childNodeList;
		LayoutInflater mInflater;

		public MyExpandableAdapter(Context context, List<TreeNode> nodeList) {
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
			TreeNode currNode = childNodeList.get(groupPosition);
			LastResult result0 = new LastResult(currNode.rwID, currNode.qyID, currNode.TID, currNode.SpecialItemID);
			// 对结果进行加载
			final LastResult lastResult = getLastResult(currNode.rwID, currNode.qyID, currNode.SpecialItemID, result0);

			// 获取结果类型
			// 是、否[一类、二类、三类、四类]ID,RName,ControlType
			final ArrayList<HashMap<String, Object>> resultType = getResultTypeData(currNode.ResultTypeID);

			if (resultType != null) {
				try {
					String contrlType = (String) resultType.get(0).get("controltype");

					// ==================单选按钮=================================================
					if (contrlType.contains("Check")) {

						String lastRemark = lastResult.Remark;
						if (lastRemark != null && !lastRemark.equals("null") && loadLastBoolean) {
							loadLastBoolean = false;
						}

						final RadioGroup group = new RadioGroup(mContext);
						View childview = mInflater.inflate(R.layout.specialitem_edit_button, null);
						final EditText editText = (EditText) childview.findViewById(R.id.specialitem_edit_button_editText);

						group.setPadding(15, 0, 5, 0);
						for (HashMap<String, Object> map : resultType) {
							String resultText = (String) map.get("rname");
							final RadioButton radioButton = new RadioButton(mContext);
							radioButton.setText(resultText);
							radioButton.setTextColor(Color.BLACK);
							radioButton.setClickable(isEditable);

							// 设置默认选中
							if (resultText.equals(lastResult.Text)) {
								radioButton.setChecked(true);

								// 这段代码没用，radiobutton会优先监听group的onCheckedChange事件
								// radioButton.setOnClickListener(new
								// OnClickListener() {
								// @Override
								// public void onClick(View v) {
								// Log.i("info", "onClick");
								//
								// String rbtext = ((RadioButton)
								// v).getText().toString();//
								// 取得被选择的RadioButton中的文本内容
								//
								// for (HashMap<String, Object> map :
								// resultType) {
								// if (rbtext.equals(map.get("rname"))) {
								// resultTypeId = (String) map.get("id");
								//
								// }
								// }
								//
								// // 向数据库中保存数据
								// saveDataToDB(lastResult, resultTypeId,
								// remarkResult, surveyTime);
								// closeInputMethod(editText);
								// // ListView的动作:收起当前项、打开下一项
								// // listCollapseAction();
								// // reloadListView();
								// notifyDataSetChanged();
								//
								// }
								// });

							}
							group.addView(radioButton);
						}
						resultLayout.addView(group);// 添加到布局中

						// 文本框
						TextView noteTextView = new TextView(mContext);
						noteTextView.setPadding(25, 0, 5, 5);
						// noteTextView.requestFocus();

						String remarkStr = "备注：";
						String remark = currNode.RemarkTip;
						if (!"".equals(remark)) {
							remarkStr = remark;
						}
						noteTextView.setText(remarkStr);
						noteTextView.setTextColor(Color.BLACK);
						resultLayout.addView(noteTextView);// 添加到布局中

						// 按钮
						Button putButton = (Button) childview.findViewById(R.id.specialitem_edit_button_button);
						putButton.setOnClickListener(new View.OnClickListener() {

							@Override
							public void onClick(View v) {
								remarkResult = editText.getText().toString().trim();
								saveDataToDB(lastResult, lastResult.SpecialItemResult, remarkResult, surveyTime);
								closeInputMethod(editText);
								// ListView的动作:收起当前项、打开下一项
								listCollapseAction();
							}
						});
						// 编辑框
						// final EditText editText = new EditText(mContext);
						editText.setWidth(200);
						editText.setFocusable(isEditable);// 判断是否可以编辑
						editText.setInputType(InputType.TYPE_CLASS_TEXT);
						// editText.requestFocus();
						setInputState(editText);
						if (lastResult.Remark != null && lastResult.Remark.length() != 0 && !"null".equals(lastResult.Remark)) {
							editText.setText(lastResult.Remark);
						} else {
							// editText.setText(remarkString);
							// remarkString = "";
							editText.setText("");
						}

						editText.setFilters(new InputFilter[] { new InputFilter.LengthFilter(100) });

						// resultLayout.addView(editText);// 添加到布局中
						resultLayout.addView(childview);// 添加到布局中

						// 监听，保存
						group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

							@Override
							public void onCheckedChanged(RadioGroup group, int checkedId) {

								RadioButton rb = (RadioButton) resultLayout.findViewById(group.getCheckedRadioButtonId());
								String rbtext = rb.getText().toString();// 取得被选择的RadioButton中的文本内容

								for (HashMap<String, Object> map : resultType) {
									if (rbtext.equals(map.get("rname"))) {
										resultTypeId = (String) map.get("id");
										// Log.i(TAG, "获取结果->" + rbtext + "ID->"
										// + resultTypeId);
									}
								}

								// String info = "请你公司于三日内到我单位进行处理。（长春市环保局）";
								remarkResult = editText.getText().toString().trim();

								// 向数据库中保存数据
								saveDataToDB(lastResult, resultTypeId, remarkResult, surveyTime);
								closeInputMethod(editText);
								// ListView的动作:收起当前项、打开下一项
								// listCollapseAction();
								// reloadListView();
								// notifyDataSetChanged();
								reloadListView();
								// editText.setText("");

								// 最后一个子项则返回上一页
								/*
								 * if (groupPosition == LIST_SIZT - 1) {
								 * SpecialItemActivity.this.finish(); }
								 */

							}

						});
						// =======================输入文本框==========================================
					} else if (contrlType.contains("Text")) {
						String lastSpecialItem = lastResult.SpecialItemResult;
						if (lastSpecialItem != null && loadLastBoolean) {
							specialItemText = lastResult.SpecialItemResult;
							loadLastBoolean = false;
						}
						View childview = mInflater.inflate(R.layout.specialitem_edit_button, null);

						// 提示文本框
						TextView noteTextView = new TextView(mContext);
						noteTextView.setPadding(25, 0, 5, 5);
						noteTextView.setText("请输入：");
						// noteTextView.requestFocus();
						// noteTextView.setEnabled(isEditable);
						resultLayout.addView(noteTextView);// 添加到布局中

						// 输入文本框
						final EditText editTextView = (EditText) childview.findViewById(R.id.specialitem_edit_button_editText);
						editTextView.setWidth(250);
						editTextView.setFocusable(isEditable);
						// editTextView.requestFocus();
						setInputState(editTextView);
						editTextView.setFilters(new InputFilter[] { new InputFilter.LengthFilter(100) });
						if (isEditable) {
							editTextView.setEnabled(true);
						}
						// 直接给予焦点

						if (!"".equals(specialItemText) && !"null".equals(specialItemText)) {
							editTextView.setText(specialItemText);
						}
						/*
						 * editTextView .setOnFocusChangeListener(new
						 * View.OnFocusChangeListener() {
						 * 
						 * @Override public void onFocusChange(View v, boolean
						 * hasFocus) { if (!hasFocus) { specialItemText =
						 * ((EditText) v) .getText().toString();
						 * 
						 * } } });
						 */

						// resultLayout.addView(editTextView);// 添加到布局中

						// 按钮
						Button putButton = (Button) childview.findViewById(R.id.specialitem_edit_button_button);
						// putButton.setText(android.R.string.ok);
						// putButton.setWidth(200);
						putButton.setFocusable(false);
						// resultLayout.addView(putButton);// 添加到布局中
						resultLayout.addView(childview);
						putButton.setOnClickListener(new View.OnClickListener() {

							@Override
							public void onClick(View v) {
								String specialItemResult = editTextView.getText().toString().trim();

								// 确定用户输入信息之后改变状态
								if (!"".equals(specialItemResult)) {
									String remarkStr = "";
									// 向数据库中保存数据
									saveDataToDB(lastResult, specialItemResult, remarkStr, surveyTime);
									editTextView.setText("");
									closeInputMethod(editTextView);
								}

								// ListView的动作:收起当前项、打开下一项
								listCollapseAction();

								// 最后一个子项则返回上一页
								/*
								 * if (groupPosition == LIST_SIZT - 1) {
								 * SpecialItemActivity.this.finish(); }
								 */
							}
						});

						
						
						
						
						
						
						
						
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			return resultLayout;
		}

		// 关闭再打开当前的点击选项
		protected void reloadListView() {
			// 点击后收回之前打开的Group
			listView.collapseGroup(expandGroupID);
			// 打开下一个Group
			int index = expandGroupID;
			if (index == LIST_SIZT) {
				return;
			}
			listView.expandGroup(index);

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
			return LIST_SIZT;
		}

		@Override
		public long getGroupId(int groupPosition) {
			return groupPosition;
		}

		@Override
		public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
			LayoutInflater mInflater = LayoutInflater.from(mContext);
			View view = mInflater.inflate(R.layout.specialitem_group, null);

			TextView groupName = (TextView) view.findViewById(R.id.specialitem_group_title);
			TreeNode currNode = childNodeList.get(groupPosition);

			if (currNode.children.size() < 1) {// 叶子节点
				queryNodeResult(currNode, groupName);

			} else {
				groupName.setText(currNode.title);
			}
			/*
			 * String spename = currNode.title; if (!"".equals(textResult)) {
			 * if(textResult.equals("否")){ spename =
			 * "<sub><font color='#FF0000'>" + spename + "</font></sub>"; }
			 * String textInfo = spename + "<sub><font color='#0288e7'>[" +
			 * textResult + "]</font></sub>";
			 * groupName.setText(Html.fromHtml(textInfo)); }
			 */
			// 查询数据库，实时赋值
			return view;
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
		public String TaskID; // 任务编号
		public String EnterID; // 企业编号
		public String IndustryID; // 行业编号
		public String SpecialItemID; // 专项编号
		public String SpecialItemResult; // 结果类型编号或者文本
		public String Text; // 结果文本
		public String Remark; // 备注信息
		public boolean flag; // 是否已经保存

		// 含参数构造
		// 初始赋值：任务编号，企业编号，模板编号、专项编号
		public LastResult(String taskID, String enterID, String industryID, String specialItemID) {
			super();
			TaskID = taskID;
			EnterID = enterID;
			IndustryID = industryID;
			SpecialItemID = specialItemID;
		}
	}

	/**
	 * 查询数据库 通过任务ID，专项ID，获取上次结果
	 * 
	 * @param qyID
	 *            企业ID
	 * @return 备注信息
	 * @author Administrator<br>
	 *         Create at: 2012-12-4 上午11:20:56
	 */
	private LastResult getLastResult(String rwID, String qyID, String specialItemID, LastResult lastResult) {
		ArrayList<HashMap<String, Object>> data = new ArrayList<HashMap<String, Object>>();
		String sql = "SELECT * FROM YDZF_TaskSpecialItem WHERE TaskID ='" + rwID + "' and EnterID='" + qyID + "' and IndustryID ='" + lastResult.IndustryID
				+ "' and SpecialItemID ='" + specialItemID + "'";

		try {
			data = SQLiteDataProvider.getInstance().queryBySqlReturnArrayListHashMap(sql);
		} catch (Exception e) {
			Log.i(TAG, e.getMessage());
		}
		if (data.size() > 0) {
			for (HashMap<String, Object> map : data) {
				lastResult.SpecialItemResult = (String) map.get("specialitemresult");
				lastResult.Remark = (String) map.get("remark");
			}
			// 上次选择信息的文本
			lastResult.Text = getLastTaskResultText(rwID, qyID, specialItemID);
			lastResult.flag = true;
		}
		// if(data == null || data.size() == 0 || lastResult.SpecialItemResult
		// == null || "".equals(lastResult.SpecialItemResult)){
		// if(data == null || data.size() == 0)
		// lastResult.flag = false;
		//
		// String sql1 =
		// "SELECT * FROM YDZF_TaskSpecialItem WHERE SpecialItemResult is not null and EnterID ='"+qyID+"' and IndustryID ='"+lastResult.IndustryID+"' and SpecialItemID ='"+specialItemID+"' order by UpdateTime desc limit 0,1";
		// try {
		// data =
		// SQLiteDataProvider.getInstance().queryBySqlReturnArrayListHashMap(sql1);
		// } catch (Exception e) {
		// Log.i(TAG, e.getMessage());
		// }
		// if(data.size() > 0){
		// for (HashMap<String, Object> map : data) {
		// lastResult.SpecialItemResult = (String)map.get("specialitemresult");
		// lastResult.Remark = (String)map.get("remark");
		// }
		// //上次选择信息的文本
		// lastResult.Text = getLastTaskResultText(rwID, qyID, specialItemID);
		// }
		// }
		return lastResult;
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

	/**
	 * 保存数据
	 * 
	 * @param lastResult
	 *            保存结果对象
	 * @param result
	 *            结果类型
	 * @param remark
	 *            备注信息文本
	 * @param surveyTime
	 *            调查时间
	 * @author Administrator<br>
	 *         Create at: 2012-12-4 上午11:20:56
	 */
	private void saveDataToDB(final LastResult lastResult, String result, final String remark, final String surveyTime) {
		String rwID = lastResult.TaskID;
		String qyID = lastResult.EnterID;
		String industryID = lastResult.IndustryID;
		String specialItemID = lastResult.SpecialItemID;

		if (lastResult.flag) {
			// 已经保存信息：更新
			// 保存数据到数据库中
			ContentValues contentValues = new ContentValues();
			contentValues.put("SpecialItemResult", result);
			contentValues.put("Remark", remark);
			contentValues.put("SurveyTime", surveyTime);
			Log.i(TAG, "Update !");
			if (SQLiteDataProvider.getInstance().updateTable(RESULT_TABLE, contentValues, " TaskID = ? and SpecialItemID = ? and EnterID = ?",
					new String[] { rwID, specialItemID, qyid })) {
				Log.i(TAG, "Update Success");
			}
		} else {

			if (insertTaskResult(rwID, qyID, industryID, specialItemID, result, remark, surveyTime)) {
				Log.i(TAG, "Insert Success");
			}
		}
		LogUtil.i(TAG, "信息：任务编号->" + rwID + "企业代码->" + qyID + "行业编号->" + industryID + "专项编号->" + specialItemID + "结果(文本或编号)->" + result + "备注->" + remark + "调查时间->" + surveyTime);
	//BYK
		// 改变任务状态为执行中
//		if (rwxx.queryTask_Qyid_Status(rwID, qyID).equals("0")) {
//		
//			rwxx.changeTask_QyidState(rwID, qyID, "2");
//		}
		if (rwxx.queryTask_Qyid_Status(rwID, qyID).equals("1")) {
			
			rwxx.changeTask_QyidState(rwID, qyID, "2");
		}
		String rwzt = rwxx.queryTaskStatus(rwID);

		if (rwzt.equals(RWXX.RWZT_WATE_EXECUTION)) {
			rwxx.changeTaskState(rwID, RWXX.RWZT_ON_EXECUTION);

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

	/**
	 * 将任务结果插入到数据库
	 * 
	 * @param rwID
	 * @param qyID
	 * @param industryID
	 * @param specialItemID
	 * @param specialItemResult
	 * @param remark
	 * @param surveyTime
	 * @return
	 * @author Administrator<br>
	 *         Create at: 2012-12-4 上午11:20:56
	 */
	private boolean insertTaskResult(String rwID, String qyID, String industryID, String specialItemID, String specialItemResult, String remark, String surveyTime) {
			
		if (TextUtils.isEmpty(industryID))
			industryID = "1";
		String sql = "insert into YDZF_TaskSpecialItem(TaskID,EnterID,IndustryID,SpecialItemID,SpecialItemResult,Remark,SurveyTime) " + "values('" + rwID + "','" + qyID + "','"
				+ industryID + "','" + specialItemID + "','" + specialItemResult + "','" + remark + "','" + surveyTime + "')";
		ArrayList<HashMap<String,Object>> qdData = getQDData();
		//判断是否添加过
		if (qdData.size()>0) {
			
		}else{
			rwxx.addBL2(tname, rwID, qyID,tid);
		}
		 	
		
		return SQLiteDataProvider.getInstance().ExecSQL(sql);
	}
	/**
	 * 获取清单数据
	 * */
	private ArrayList<HashMap<String, Object>> getQDData() {

		ArrayList<HashMap<String, Object>> data = new ArrayList<HashMap<String, Object>>();

		/** 查询出笔录表中的数据 */
		data = SqliteUtil.getInstance().queryBySqlReturnArrayListHashMap(
				"select * from ExeLawsTemplet where taskid = '" + rwbh + "' and enterid = '" + qyid
						+ "' and templettype = '"+tid+"' order by updatetime desc");
		return data;
	}

	/**
	 * 查询专项的结果值文本
	 * 
	 * @author Liusy
	 * @param node
	 *            当前节点
	 * @return 当前节点的结果值
	 * @author Administrator<br>
	 *         Create at: 2012-12-4 上午11:20:56
	 */
	private void queryNodeResult(TreeNode node, TextView tv) {
		// String headerStr=node.title;
		String sqlType = "SELECT ControlType as type FROM YDZF_ResultType WHERE ID = '" + node.ResultTypeID + "'";
		String sqlText = "";
		String resultText = node.title;

		ArrayList<HashMap<String, Object>> data = null;
		try {
			data = SQLiteDataProvider.getInstance().queryBySqlReturnArrayListHashMap(sqlType);
		} catch (Exception e) {
			Log.i(TAG, e.getMessage());
		}

		if (data.size() == 0) {// 如果没有选择过就只显示，当前的标题
			tv.setText(resultText);
			return;
		}

		// 判断有type之后的逻辑

		String sqlStr = "select WFBS as answer from YDZF_SpecialItem where ID='" + node.SpecialItemID + "'";

		ArrayList<HashMap<String, Object>> result1 = SQLiteDataProvider.getInstance().queryBySqlReturnArrayListHashMap(sqlStr);
		String answer = "";
		for (HashMap<String, Object> hashMap : result1) {
			answer = (String) hashMap.get("answer");
		}

		String typeStr = (String) data.get(0).get("type");
		if (typeStr != null && typeStr.contains("Text")) {// text时候显示输入框中的信息
			sqlText = "SELECT SpecialItemResult AS text FROM YDZF_TaskSpecialItem WHERE TaskID ='" + node.rwID + "' and IndustryID = '" + node.TID + "' and EnterID='" + qyid
					+ "'and SpecialItemID ='" + node.SpecialItemID + "' and text is not null";

		} else if (typeStr != null && typeStr.contains("Check")) {// check的时候显示被选择的信息
			sqlText = "SELECT RName AS text FROM YDZF_ResultType WHERE ID = " + "(SELECT SpecialItemResult as id FROM YDZF_TaskSpecialItem WHERE TaskID ='" + node.rwID
					+ "' and EnterID='" + qyid + "' and IndustryID = '" + node.TID + "'and SpecialItemID ='" + node.SpecialItemID + "' and id is not null)";
		}

		try {
			data = SQLiteDataProvider.getInstance().queryBySqlReturnArrayListHashMap(sqlText);
			if (data == null || data.size() == 0) {// 如果都没查到消息就是显示备注里边的东西
				if (typeStr != null && typeStr.contains("Check")) {
					sqlText = "select Remark as text from YDZF_TaskSpecialItem  WHERE TaskID = '" + node.rwID + "'and SpecialItemID ='" + node.SpecialItemID + "' and EnterID='"
							+ qyid + "' and Remark is not null";
				}
				data = SQLiteDataProvider.getInstance().queryBySqlReturnArrayListHashMap(sqlText);
			}

		} catch (Exception e) {
			Log.i(TAG, e.getMessage());
		}

		// data中的内容就是单选按钮的文字，或者输入框信息，或者单选按钮时候的备注信息

		if (data != null && data.size() > 0) {// 表明是新加入的数据，正在执法？
			for (HashMap<String, Object> map : data) {
				if (typeStr != null && typeStr.contains("Check")) {

					String text = (String) map.get("text");
					if (text != null && !"".equals(text) && ("是".equals(text) || "否".equals(text) || "其他".equals(text))) {
						if (((String) map.get("text")).equals(answer) || "其他".equals(answer)) {

							String result = "<span><font color='#0288e7'>[" + (String) map.get("text") + "]</font></span>";
							resultText = "<span><font color='#FF0000'>" + resultText + "</font></span>" + result;
							tv.setText(Html.fromHtml(resultText));
						} else {// 选择否后标红
							String result = "<span><font color='#0288e7'>[" + (String) map.get("text") + "]</font></span>";
							tv.setText(Html.fromHtml(resultText + result));
						}
					} else {

						String result = "<span><font color='#0288e7'>" + "" + "</font></span>";
						tv.setText(Html.fromHtml(resultText + result));
					}

				} else {
					String result = FileHelper.formatTextLength((String) map.get("text"), 6);//
					result = "<span><font color='#0288e7'>[" + result + "]</font></span>";
					resultText = resultText + result;
					tv.setText(Html.fromHtml(resultText));
				}

			}
		} else {// 不是正在执法，查询上次检查结果
			if (typeStr != null && typeStr.contains("Text")) {
				sqlText = "SELECT SpecialItemResult AS text FROM YDZF_TaskSpecialItem WHERE text is not null and EnterID = '" + node.qyID + "' AND IndustryID = '" + node.TID
						+ "'AND SpecialItemID ='" + node.SpecialItemID + "' order by UpdateTime desc limit 0,1";
			} else if (typeStr != null && typeStr.contains("Check")) {
				sqlText = "SELECT RName AS text FROM YDZF_ResultType WHERE ID = "
						+ "(SELECT SpecialItemResult FROM YDZF_TaskSpecialItem WHERE SpecialItemResult is not null and EnterID = '" + node.qyID + "' AND IndustryID = '" + node.TID
						+ "'and SpecialItemID ='" + node.SpecialItemID + "' order by UpdateTime desc limit 0,1)";

			}
			try {
				data = SQLiteDataProvider.getInstance().queryBySqlReturnArrayListHashMap(sqlText);
			} catch (Exception e) {
				Log.i(TAG, e.getMessage());
			}

			if (data != null && data.size() > 0) {
				for (HashMap<String, Object> map : data) {
					String result = FileHelper.formatTextLength((String) map.get("text"), 6);
					result = "<span><font color='#A9A9A9'>[" + result + "(上次记录)" + "]</font></span>";
					tv.setText(Html.fromHtml(resultText + result));

				}
			} else {
				tv.setText(resultText);
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
		pop = new PopupWindow(popWindow, 300, 300);
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
		Log.i("info", "执行返回结果special");
		super.onActivityResult(requestCode, resultCode, data);
	}

}