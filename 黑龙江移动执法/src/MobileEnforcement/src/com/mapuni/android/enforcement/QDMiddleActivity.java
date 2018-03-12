package com.mapuni.android.enforcement;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mapuni.android.MobileEnforcement.R;
import com.mapuni.android.base.BaseActivity;
import com.mapuni.android.base.Global;
import com.mapuni.android.business.RWXX;
import com.mapuni.android.business.TreeNode;
import com.mapuni.android.dataprovider.SQLiteDataProvider;
import com.mapuni.android.dataprovider.SqliteUtil;
import com.mapuni.android.enforcement.QdjcnlActivity.MyAdapter;

/**
 * FileName: QDMiddleActivity.java Description:结点的中间跳转页面
 * 
 * @author liusy
 * @Version 1.3.4
 * @Copyright 中科宇图天下科技有限公司 Create at: 2012-12-4 上午10:14:01
 */
public class QDMiddleActivity extends BaseActivity {
	private Intent intent;
	private TreeNode frontdata;
	private RelativeLayout rl;
	private LinearLayout middleLayout;
	private ListView lv;
	private LayoutInflater li;
	private MyAdapter adapter;

	public final String TAG = "JContentActivity";
	public final String RESULT_TABLE = "YDZF_TaskSpecialItem";

	private boolean isTaskStart = false;
	private String qyid;
	private String rwbh;
	private String tname;
	private String tid;
	private boolean isSearch;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.ui_mapuni);

		// 获取前页传递的信息
		intent = getIntent();
		qyid = intent.getStringExtra("qyid");
		isSearch = intent.getBooleanExtra("isSearch", false);
		rwbh = intent.getStringExtra("rwbh");
	  tname = intent.getStringExtra("tname");
	 tid = intent.getStringExtra("tid");
		frontdata = (TreeNode) intent.getExtras().get("node");// 获得前一页面传的Node对象

		li = LayoutInflater.from(this);
		rl = (RelativeLayout) findViewById(R.id.parentLayout);
		// 非叶子结点
		if (frontdata.getChildren() != null) {
			// 设定基本布局
			Collections.sort(frontdata.getChildren());
			String a = frontdata.title;
			String b = a.substring(0, a.length() > 13 ? 13 : a.length());
			SetBaseStyle(rl, b + "...");
		}

		middleLayout = (LinearLayout) findViewById(R.id.middleLayout);
		setTitleLayoutVisiable(true);

		lv = new ListView(this);
		lv.setCacheColorHint(Color.TRANSPARENT);
		middleLayout.addView(lv, new LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.FILL_PARENT));
		adapter = new QdjcnlActivity.MyAdapter(frontdata.getChildren(), this);
		lv.setAdapter(adapter);
		lv.setDivider(getResources().getDrawable(R.drawable.list_divider));
		lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(final AdapterView<?> parent, View view, final int position, long id) {
				final MyAdapter aa = (MyAdapter) parent.getAdapter();
				final TreeNode currentNode = (TreeNode) aa.getItem(position);

				// 使用新效果
				if (Global.getGlobalInstance().useNewStyleBoolean) {
					if (currentNode.getChildren().get(0).getChildren().size() > 0) {
						// 如果有子节点则跳转
						Intent next = new Intent(QDMiddleActivity.this, QDMiddleActivity.class);
						Bundle bundle = new Bundle();
						bundle.putSerializable("node", (TreeNode) aa.getItem(position));
						next.putExtras(bundle);
						next.putExtra("qyid", qyid);
						startActivityForResult(next, position);
					} else {
						Intent next = new Intent(QDMiddleActivity.this, SpecialItemActivity.class);
						Bundle bundle = new Bundle();
						bundle.putSerializable("node", (TreeNode) aa.getItem(position));
						next.putExtras(bundle);
						next.putExtra("qyid", qyid);
						startActivityForResult(next, position);
					}
				} else {
					// 使用原有效果
					if (currentNode.getChildren().size() > 0) {

						// 如果有子节点则跳转
						Intent next = new Intent(QDMiddleActivity.this, QDMiddleActivity.class);
						Bundle bundle = new Bundle();
						bundle.putSerializable("node", (TreeNode) aa.getItem(position));
						next.putExtras(bundle);
						startActivityForResult(next, position);
						Toast.makeText(QDMiddleActivity.this, "加载数据中，请稍后……", Toast.LENGTH_SHORT).show();

					} else {
						// 叶子节点不跳转，弹出对话框
						// 获取信息
						final String rwID = currentNode.rwID;
						final String qyID = currentNode.qyID;
						final String industryID = currentNode.TID;
						final String specialItemID = currentNode.SpecialItemID;
						final String resultTypeID = currentNode.ResultTypeID;
						final Context context = QDMiddleActivity.this;
						final String remark = currentNode.RemarkTip;
						final String surveyTime = Global.getGlobalInstance().getDate();

						// 获取结果类型
						// 是、否[一类、二类、三类、四类]
						final ArrayList<HashMap<String, Object>> result = getResultTypeData(resultTypeID);
						LastResult result0 = new LastResult(rwID, qyID, industryID, specialItemID);
						// 对结果进行加载
						final LastResult lastResult = getLastResult(rwID, specialItemID, result0);
						Log.i(TAG, "结果类型ID： " + currentNode.ResultTypeID + "-->获取结果数:" + result.size());

						ArrayList<String> resultList = new ArrayList<String>();
						final HashMap<String, String> resultIdMap = new HashMap<String, String>();
						String contrlType = "";
						for (HashMap<String, Object> map : result) {
							contrlType = (String) map.get("controltype");
							Log.i(TAG, "结果类型 : " + contrlType);

							resultList.add((String) map.get("rname"));
							resultIdMap.put((String) map.get("rname"), (String) map.get("id"));
						}

						//
						// 显示
						//
						LayoutInflater mInflater = LayoutInflater.from(context);

						// ==================单选按钮===================================================
						if (contrlType != null && contrlType.contains("Check")) {
							// 单选按钮
							Log.i(TAG, "显示单选按钮");
							// 对话框的布局
							AlertDialog.Builder ab = new AlertDialog.Builder(context);
							LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
							// 通过布局加载器，加载对话框的布局
							final LinearLayout outLayout = (LinearLayout) mInflater.inflate(R.layout.checkgroup, null);
							RadioGroup radioGroup = new RadioGroup(context);
							radioGroup.setLayoutParams(params);
							radioGroup.setGravity(Gravity.LEFT);

							// 获取上次结果的文本信息
							String lastText = lastResult.Text;
							Log.i(TAG, "上次结果文本信息：" + lastText);

							// 遍历集合获取显示信息
							// 初始化控件
							for (String text : resultList) {
								RadioButton radioButton = new RadioButton(context);
								radioButton.setText(text);
								// 加载数据库中信息
								if (text.equals(lastText)) {
									radioButton.setChecked(true);
									// radioButton
								}
								if (lastText == null) {
									// hasValue = false;
								}
								radioButton.setPadding(70, 5, 5, 5);
								radioGroup.addView(radioButton);
							}

							outLayout.addView(radioGroup);

							ab.setView(outLayout);
							ab.setTitle("请选择");
							ab.setIcon(R.drawable.icon_mapuni_white);
							final AlertDialog dialog = ab.create();
							dialog.setOnDismissListener(new MyDialogDismiss());

							// 文本框
							TextView noteTextView = new TextView(context);
							String remarkStr = "备注：";
							if (!"".equals(remark)) {
								remarkStr = remark;
							}
							noteTextView.setText(remarkStr);
							noteTextView.setWidth(200);
							noteTextView.setPadding(5, 5, 5, 5);

							// 编辑框
							final EditText editText = new EditText(context);
							editText.setWidth(200);
							editText.setText(lastResult.Remark);
							editText.setFilters(new InputFilter[] { new InputFilter.LengthFilter(180) });

							// 添加到布局
							outLayout.addView(noteTextView);
							outLayout.addView(editText);

							/*
							 * if(!"".equals(remark)){ Log.i(TAG, "备注：" +
							 * remark); //备注文本 TextView noteTextView = new
							 * TextView(context); noteTextView.setText(remark);
							 * noteTextView.setWidth(200);
							 * noteTextView.setPadding(5, 5, 5, 5);
							 * 
							 * //编辑区域 editText.setWidth(200);
							 * editText.setText(lastResult.Remark);
							 * editText.setFilters(new InputFilter[] { new
							 * InputFilter.LengthFilter(180) });
							 * 
							 * 
							 * //Button putButton = new Button(context);
							 * //putButton.setText(android.R.string.ok);
							 * 
							 * //添加到布局 outLayout.addView(noteTextView);
							 * outLayout.addView(editText);
							 * //outLayout.addView(putButton);
							 * 
							 * }
							 */

							// 保存
							// 单选按钮事件监听
							radioGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {
								@Override
								public void onCheckedChanged(RadioGroup group, int checkedId) {

									/*
									 * mHandler = new Handler(){ public void
									 * handleMessage(android.os.Message msg) {
									 * if(msg.what == 1){
									 * 
									 * } }; };
									 */

									// 记录当前用户选择
									currentNode.state = true;
									aa.notifyDataSetChanged();
									dialog.cancel();

									RadioButton rb = (RadioButton) outLayout.findViewById(group.getCheckedRadioButtonId());
									String rbtext = rb.getText().toString();// 取得被选择的RadioButton中的文本内容

									String resultTypeId = "";

									for (String text : resultIdMap.keySet()) {
										if (text.equals(rbtext)) {
											resultTypeId = resultIdMap.get(text);
										}
									}

									Log.i(TAG, "获取结果ID：" + resultTypeId);

									// 获取结果
									String remarkStr = editText.getText().toString().trim();
									if (!"".equals(remarkStr)) {
										currentNode.state = true;
									}

									// 向数据库中保存数据
									saveDataToDB(lastResult, resultTypeId, remarkStr, surveyTime);

									// 最后一个子项则返回上一页
									if (position == (((MyAdapter) parent.getAdapter()).getCount() - 1)) {
										// (((Node)((Adapter)parent.getAdapter()).getItem(position)).getParent()).setState(true);
										Log.i("TAG", ((TreeNode) ((MyAdapter) parent.getAdapter()).getItem(position)).getParent().title);
										setResult(1);
										QDMiddleActivity.this.finish();
										// ((Adapter)parent.getAdapter()).notifyDataSetChanged();
									}
								}

							});
							dialog.show();

							// =======================输入文本框==========================================
						} else if (contrlType != null && contrlType.contains("Text")) {
							// 输入文本框
							Log.i(TAG, "显示输入文本框！");
							AlertDialog.Builder ab = new AlertDialog.Builder(context);
							LayoutParams params = new LayoutParams(250, LayoutParams.WRAP_CONTENT);
							// 通过布局加载器，加载对话框的布局
							final LinearLayout outLayout = (LinearLayout) mInflater.inflate(R.layout.checkgroup, null);
							outLayout.setLayoutParams(params);

							final EditText editTextView = new EditText(context);
							editTextView.setWidth(250);

							String textContent = lastResult.SpecialItemResult;

							if (textContent == null) {

							}
							if (!"".equals(textContent)) {
								editTextView.setText(textContent);
							}

							Button putButton = new Button(context);
							putButton.setText(android.R.string.ok);
							putButton.setWidth(200);

							// outLayout.addView(noteTextView);
							outLayout.addView(editTextView);
							outLayout.addView(putButton);

							ab.setView(outLayout);
							ab.setTitle("请输入：");
							ab.setIcon(R.drawable.icon_mapuni_white);
							ab.create();
							final AlertDialog dialog = ab.create();
							dialog.setOnDismissListener(new MyDialogDismiss());

							putButton.setOnClickListener(new View.OnClickListener() {

								@Override
								public void onClick(View v) {
									// 用户输入完成，点击保存
									dialog.cancel();

									String specialItemResult = editTextView.getText().toString().trim();

									// 确定用户输入信息之后改变状态
									if (!"".equals(specialItemResult)) {
										currentNode.state = true;
										aa.notifyDataSetChanged();

										String remarkStr = "";
										// 向数据库中保存数据
										saveDataToDB(lastResult, specialItemResult, remarkStr, surveyTime);

									}

									// 最后一个子项则返回上一页
									if (position == (((MyAdapter) parent.getAdapter()).getCount() - 1)) {
										// (((Node)((Adapter)parent.getAdapter()).getItem(position)).getParent()).setState(true);
										Log.i("TAG", ((TreeNode) ((MyAdapter) parent.getAdapter()).getItem(position)).getParent().title);

										QDMiddleActivity.this.finish();
										// ((Adapter)parent.getAdapter()).notifyDataSetChanged();
									}
								}

							});
							dialog.show();
						}

					}
				}
			}
		});
		// Intent intent = new Intent();

		// Bundle bundle = new Bundle();
		// bundle.putSerializable("node", frontdata);
		// intent.putExtras(bundle);

		// setResult(1,intent);
	}

	/**
	 * 通知列表数据改变
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		// if (resultCode == 1) {
		((TreeNode) adapter.getItem(requestCode)).setState(true);

		adapter.notifyDataSetChanged();
		Log.i(TAG, "返回页面");
		// }
	}

	/**
	 * Description: 将任务结果保存到数据库
	 * 
	 * @param rwID
	 *            任务编号
	 * @param qyID
	 *            企业编号
	 * @param industryID
	 *            行业（模板）编号
	 * @param specialItemID
	 *            专项编号
	 * @param specialItemResult
	 *            结果类型编号
	 * @param remark
	 *            备注信息
	 * @param surveyTime
	 *            调查时间
	 * @return 保存是否成功
	 * @author Administrator Create at: 2012-12-4 上午10:15:25
	 */
	private boolean insertTaskResult(String rwID, String qyID, String industryID, String specialItemID, String specialItemResult, String remark, String surveyTime) {

		if (TextUtils.isEmpty(industryID))
			industryID = "1";
		String sql = "insert into YDZF_TaskSpecialItem(TaskID,EnterID,IndustryID,SpecialItemID,SpecialItemResult,Remark,SurveyTime) " + "values('" + rwID + "','" + qyID + "','"
				+ industryID + "','" + specialItemID + "','" + specialItemResult + "','" + remark + "','" + surveyTime + "')";
		System.out.println(sql);
		ArrayList<HashMap<String,Object>> qdData = getQDData();
		//判断是否添加过
		if (qdData.size()>0) {
			
		}else{
			new RWXX().addBL2(tname, rwID, qyID,tid);
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
	 * Description:查询数据库 <li>获取其结果类型：文本、单选框
	 * 
	 * @param resultTypeID
	 * @return 结果类型ID
	 * @author Administrator Create at: 2012-12-4 上午10:16:54
	 */
	private ArrayList<HashMap<String, Object>> getResultTypeData(String resultTypeID) {
		ArrayList<HashMap<String, Object>> data = new ArrayList<HashMap<String, Object>>();
		String sql = "";
		sql = "select ID,RName,ControlType from YDZF_ResultType where PID = '" + resultTypeID + "' order by sortIndex ";

		data = SQLiteDataProvider.getInstance().queryBySqlReturnArrayListHashMap(sql);

		if (data.size() == 0) {
			sql = "select RName,ControlType from YDZF_ResultType where ID = '" + resultTypeID + "' order by sortIndex ";

			data = SQLiteDataProvider.getInstance().queryBySqlReturnArrayListHashMap(sql);

			return data;
		}
		return data;
	}

	/**
	 * Description: 查询数据库 <li>通过任务ID和专项ID获取结果值文本
	 * 
	 * @param rwID
	 *            任务编号
	 * @param specialItemID
	 *            专项编号
	 * @return 上次选择信息的文本
	 * @author Administrator Create at: 2012-12-4 上午10:17:38
	 */
	private String getLastTaskResultText(String rwID, String specialItemID) {
		String text = "";
		ArrayList<HashMap<String, Object>> data = new ArrayList<HashMap<String, Object>>();
		String sql = "SELECT RName FROM YDZF_ResultType WHERE ID = " + "(SELECT SpecialItemResult FROM YDZF_TaskSpecialItem WHERE TaskID ='" + rwID + "' and SpecialItemID ='"
				+ specialItemID + "')";
		Log.i(TAG, sql);

		data = SQLiteDataProvider.getInstance().queryBySqlReturnArrayListHashMap(sql);

		for (HashMap<String, Object> map : data) {
			text = (String) map.get("rname");
		}
		return text;
	}

	/**
	 * Description:查询数据库 <li>通过任务ID，专项ID，获取上次保存的结果对象
	 * 
	 * @param rwID
	 *            任务编号
	 * @param specialItemID
	 *            专项编号
	 * @param lastResult
	 *            结果对象
	 * @return
	 * @author Administrator Create at: 2012-12-4 上午10:19:19
	 */
	private LastResult getLastResult(String rwID, String specialItemID, LastResult lastResult) {
		ArrayList<HashMap<String, Object>> data = new ArrayList<HashMap<String, Object>>();
		String sql = "SELECT * FROM YDZF_TaskSpecialItem WHERE TaskID ='" + rwID + "' and IndustryID ='" + lastResult.IndustryID + "' and SpecialItemID ='" + specialItemID + "'";
		Log.i(TAG, sql);

		data = SQLiteDataProvider.getInstance().queryBySqlReturnArrayListHashMap(sql);

		if (data.size() > 0) {
			for (HashMap<String, Object> map : data) {
				lastResult.SpecialItemResult = (String) map.get("specialitemresult");
				lastResult.Remark = (String) map.get("remark");
			}
			// 上次选择信息的文本
			lastResult.Text = getLastTaskResultText(rwID, specialItemID);
			lastResult.flag = true;
		} else {
			lastResult.flag = false;
		}
		return lastResult;
	}

	/**
	 * FileName: QDMiddleActivity.java Description:对话框消失监听,保存用户信息
	 * 
	 * @author Administrator
	 * @Version 1.3.4
	 * @Copyright 中科宇图天下科技有限公司 Create at: 2012-12-4 上午10:20:28
	 */
	private final class MyDialogDismiss implements DialogInterface.OnDismissListener {

		@Override
		public void onDismiss(DialogInterface dialog) {
			Log.i(TAG, "AlertDialog Dismiss !");
			// 保存用户输入信息
			// 保存成功吐司提示
		}
	}

	/**
	 * Description:保存数据
	 * 
	 * @param lastResult
	 *            保存结果对象
	 * @param result
	 *            结果类型
	 * @param remark
	 *            备注信息文本
	 * @param surveyTime
	 *            调查时间
	 * @author Administrator Create at: 2012-12-4 上午10:20:56
	 */
	private void saveDataToDB(final LastResult lastResult, final String result, final String remark, final String surveyTime) {
		// 异步线程不能做
		/*
		 * final String rwID = lastResult.TaskID; final String qyID =
		 * lastResult.EnterID; final String industryID = lastResult.IndustryID;
		 * final String specialItemID = lastResult.SpecialItemID;
		 * 
		 * //设置全部执行更新 //hasValue = true;
		 * 
		 * new Thread(){ public void run() { //String note = "";
		 * if(lastResult.flag) { //已经保存信息：更新 //保存数据到数据库中 ContentValues
		 * contentValues = new ContentValues();
		 * contentValues.put("SpecialItemResult", result);
		 * contentValues.put("Remark", remark); contentValues.put("SurveyTime",
		 * surveyTime); Log.i(TAG, "Update !");
		 * if(SQLiteHelper.getInstance().updateTable(RESULT_TABLE,
		 * contentValues, " TaskID = ? and SpecialItemID = ?", new
		 * String[]{rwID, specialItemID})){ Log.i(TAG, "Update Success"); }
		 * //note = "更新完成！"; } else {
		 * 
		 * //没有保存信息：插入 Log.i(TAG, "Insert !"); if(insertTaskResult(rwID, qyID,
		 * industryID, specialItemID, result, remark, surveyTime)){ Log.i(TAG,
		 * "Insert Success"); } //note = "保存完成！"; } try { Thread.sleep(500);、 }
		 * catch (InterruptedException e) { e.printStackTrace(); }
		 * mHandler.sendEmptyMessage(1); }; }.start();
		 */

		// Toast.makeText(JContentActivity.this, "保存完成",
		// Toast.LENGTH_SHORT).show();

		String rwID = lastResult.TaskID;
		String qyID = lastResult.EnterID;
		String industryID = lastResult.IndustryID;
		String specialItemID = lastResult.SpecialItemID;

		// String note = "";
		if (lastResult.flag) {
			// 已经保存信息：更新
			// 保存数据到数据库中
			ContentValues contentValues = new ContentValues();
			contentValues.put("SpecialItemResult", result);
			contentValues.put("Remark", remark);
			contentValues.put("SurveyTime", surveyTime);
			Log.i(TAG, "Update !");
			if (SQLiteDataProvider.getInstance().updateTable(RESULT_TABLE, contentValues, " TaskID = ? and SpecialItemID = ?", new String[] { rwID, specialItemID })) {
				Log.i(TAG, "Update Success");
			}
			// note = "更新完成！";
		} else {

			// 没有保存信息：插入
			Log.i(TAG, "Insert !");
			if (insertTaskResult(rwID, qyID, industryID, specialItemID, result, remark, surveyTime)) {
				Log.i(TAG, "Insert Success");
			}
			// note = "保存完成！";
		}
		// Toast.makeText(JContentActivity.this, note,
		// Toast.LENGTH_SHORT).show();
		Log.i(TAG, "信息：" + rwID + "->" + qyID + "->" + industryID + "->" + specialItemID + "->" + result + "->" + remark + "->" + surveyTime);

		// 改变任务状态为执行中
		if (!isTaskStart) {
			Log.i(TAG, "改变状态 " + rwID + " ---->执行中");
			isTaskStart = true;
			// new RWXX().changeTaskState(rwID,
			// RWXX.RWZT_WATE_EXECUTION,qyID,RWXX.RWZT_ON_EXECUTION);
		}
	}

	/**
	 * FileName: QDMiddleActivity.java Description:上次结果的对象的封装
	 * 
	 * @author Administrator
	 * @Version 1.3.4
	 * @Copyright 中科宇图天下科技有限公司 Create at: 2012-12-4 上午10:38:54
	 */
	public final class LastResult {
		public String TaskID; // 任务编号
		public String EnterID; // 企业编号
		public String IndustryID; // 行业编号
		public String SpecialItemID; // 专项编号
		public String SpecialItemResult; // 结果类型编号或者文本
		public String Text; // 结果类型文本
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

}
