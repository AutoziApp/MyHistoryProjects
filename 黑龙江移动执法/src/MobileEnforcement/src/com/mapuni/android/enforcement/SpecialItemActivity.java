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
 * FileName: SpecialItemActivity.java Description:ִ���嵥ר��
 * 
 * @author liusy
 * @Version 1.3.4
 * @Copyright �п���ͼ���¿Ƽ����޹�˾ Create at: 2012-12-3 ����3:57:34
 */
public class SpecialItemActivity extends AttachmentBaseActivity implements OnClickListener {
	CustomExpandableListView listView = null;
	private int expandGroupID = 0;

	private PopupWindow pop;
	// ��ҳ�沼��
	private RelativeLayout fatherRelativeLayout;
	// private LinearLayout middleLayout;
	private Intent intent;
	private TreeNode frontdata = null;// ǰһҳ������Node
	private List<TreeNode> childNodeList;// ��ǰNode������Ҷ�ӽڵ㼯��
	// ȫ�ֱ���
	private int LIST_SIZT;
	private final String surveyTime = Global.getGlobalInstance().getDate();
	/** �������ID */
	private String resultTypeId = "";
	/** �����ע�ı� */
	private String remarkResult = "";

	public final String RESULT_TABLE = "YDZF_TaskSpecialItem";
	private final String TAG = "SpecialItemActivity";

	private String specialItemText = "";
	private boolean loadLastBoolean = true;

	private boolean isEditable = true;
	private final RWXX rwxx = new RWXX();
	private InputMethodManager imm;
	private String qyid;
	/** ��ǰ�ֻ�ϵͳ�汾�� */
	private int currentapiVersion = android.os.Build.VERSION.SDK_INT;
	private String rwbh;
	private String     tname;
	private String tid;
	private boolean isSearch;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.ui_mapuni);
		// �޸ı���
		RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.parentLayout);
		relativeLayout.setBackgroundColor(Color.rgb(245, 245, 245));

		// ��ȡǰҳ���ݵ���Ϣ
		intent = getIntent();
		qyid = intent.getStringExtra("qyid");
		
		isSearch = intent.getBooleanExtra("isSearch", false);
		isEditable=!isSearch;
		//BYK
		rwbh = intent.getStringExtra("rwbh");
	    tname = intent.getStringExtra("tname");
	   tid = intent.getStringExtra("tid");
		
		frontdata = (TreeNode) intent.getExtras().get("node");// ���ǰһҳ�洫��Node����

		initGlobal();
		childNodeList = frontdata.getChildren();
		LIST_SIZT = childNodeList.size();
		// ��ʼ�����ֿؼ�
		initViews(this);

		// ��ȡ����״̬
		String status = rwxx.getTaskStatusFromTaskEnpriLink(frontdata.rwID, qyid);
		// �Ƿ�ִ��,0:δִ��,1:����ȡ֤���,2:ִ���� ��״̬��1��ʱ���ܱ༭
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
				Toast.makeText(this, "��ǰ��ҵ����ȡ֤����ɣ��Ѳ����޸�", Toast.LENGTH_LONG).show();
			//}
			if ("009".equals(new RWXX().queryTaskStatus(frontdata.rwID))) {
				Toast.makeText(this, "��ǰ��ҵ�ѹ鵵�����ܶԱ�¼���б༭", Toast.LENGTH_LONG).show();
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
	 * ��ʼ����������ı���
	 */
	private void initGlobal() {
		CURRENT_ID = frontdata.rwID;
		QYID = qyid;
		FK_ID = CURRENT_ID + "_" + QYID;
	}

	/**
	 * Description: ��ʼ������ ���ÿؼ�������
	 * 
	 * @param context
	 * @author Administrator<br>
	 *         Create at: 2012-12-4 ����11:20:56
	 */
	private void initViews(Context context) {

		// ���಼��
		((LinearLayout) findViewById(R.id.ui_mapuni_divider)).setVisibility(View.GONE);

		fatherRelativeLayout = (RelativeLayout) findViewById(R.id.parentLayout);

		if (frontdata.getChildren() != null) {
			// �趨��������
			Collections.sort(frontdata.getChildren());
			String a = frontdata.title;
			String b = a.substring(0, a.length() > 13 ? 13 : a.length());
			SetBaseStyle(fatherRelativeLayout, b + "...");

//			queryImg.setVisibility(View.VISIBLE);
//			queryImg.setImageResource(R.drawable.base_add_task_new);
//			queryImg.setOnClickListener(this);
		}
		setTitleLayoutVisiable(true);// �������Ƿ�ɼ�

		// ���಼�֣���ӵ����಼����
		LayoutInflater mInflater = LayoutInflater.from(context);
		ScrollView scrollView = (ScrollView) mInflater.inflate(R.layout.specialitem, null);
		((LinearLayout) findViewById(R.id.middleLayout)).addView(scrollView, new LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.FILL_PARENT));

		// ��ȡExpandableListView
		listView = (CustomExpandableListView) scrollView.findViewById(android.R.id.list);
		listView.setAdapter(new MyExpandableAdapter(this, childNodeList));
		// ���ñ���ͼƬ��ͼƬ�ָ���
		listView.setGroupIndicator(this.getResources().getDrawable(R.layout.expandablelistviewselector));
		listView.setDivider(getResources().getDrawable(R.drawable.list_divider));
		listView.setChildDivider(getResources().getDrawable(R.drawable.list_divider));
		// viewGroup���������ӿؼ�����ý���
		listView.setDescendantFocusability(ViewGroup.FOCUS_BEFORE_DESCENDANTS);
		// Ĭ��չ����һ��
		listView.expandGroup(0);

		/** ����Group�� expandGroupID����ʵʱ�򿪵�GroupID */
		listView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {

			@Override
			public void onGroupExpand(int groupPosition) {
				listView.setSelectionFromTop(groupPosition - 1, 10);
				if (expandGroupID != groupPosition) {
					// ����ǰ��Group
					listView.collapseGroup(expandGroupID);
				}
				// ����һ��Group
				expandGroupID = groupPosition;
			}
		});

		/** ������ViewGroup���� */
		listView.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {

			@Override
			public void onGroupCollapse(int groupPosition) {
				specialItemText = "";
				loadLastBoolean = true;
			}
		});

		/** ������ViewGroup�������¼� */
		listView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {

			@Override
			public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
				// viewGroupֻ�е����ӿؼ�����Ҫ�����ʱ��Ż�ý���
				listView.setDescendantFocusability(ViewGroup.FOCUS_AFTER_DESCENDANTS);
				return true;
			}

		});
	}

	/**
	 * FileName: SpecialItemActivity.java Description:ExpandableList����������
	 * 
	 * @author Administrator
	 * @Version 1.3.4
	 * @Copyright �п���ͼ���¿Ƽ����޹�˾<br>
	 *            Create at: 2012-12-4 ����11:21:11
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

			// ��ǰר��
			// �����������
			TreeNode currNode = childNodeList.get(groupPosition);
			LastResult result0 = new LastResult(currNode.rwID, currNode.qyID, currNode.TID, currNode.SpecialItemID);
			// �Խ�����м���
			final LastResult lastResult = getLastResult(currNode.rwID, currNode.qyID, currNode.SpecialItemID, result0);

			// ��ȡ�������
			// �ǡ���[һ�ࡢ���ࡢ���ࡢ����]ID,RName,ControlType
			final ArrayList<HashMap<String, Object>> resultType = getResultTypeData(currNode.ResultTypeID);

			if (resultType != null) {
				try {
					String contrlType = (String) resultType.get(0).get("controltype");

					// ==================��ѡ��ť=================================================
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

							// ����Ĭ��ѡ��
							if (resultText.equals(lastResult.Text)) {
								radioButton.setChecked(true);

								// ��δ���û�ã�radiobutton�����ȼ���group��onCheckedChange�¼�
								// radioButton.setOnClickListener(new
								// OnClickListener() {
								// @Override
								// public void onClick(View v) {
								// Log.i("info", "onClick");
								//
								// String rbtext = ((RadioButton)
								// v).getText().toString();//
								// ȡ�ñ�ѡ���RadioButton�е��ı�����
								//
								// for (HashMap<String, Object> map :
								// resultType) {
								// if (rbtext.equals(map.get("rname"))) {
								// resultTypeId = (String) map.get("id");
								//
								// }
								// }
								//
								// // �����ݿ��б�������
								// saveDataToDB(lastResult, resultTypeId,
								// remarkResult, surveyTime);
								// closeInputMethod(editText);
								// // ListView�Ķ���:����ǰ�����һ��
								// // listCollapseAction();
								// // reloadListView();
								// notifyDataSetChanged();
								//
								// }
								// });

							}
							group.addView(radioButton);
						}
						resultLayout.addView(group);// ��ӵ�������

						// �ı���
						TextView noteTextView = new TextView(mContext);
						noteTextView.setPadding(25, 0, 5, 5);
						// noteTextView.requestFocus();

						String remarkStr = "��ע��";
						String remark = currNode.RemarkTip;
						if (!"".equals(remark)) {
							remarkStr = remark;
						}
						noteTextView.setText(remarkStr);
						noteTextView.setTextColor(Color.BLACK);
						resultLayout.addView(noteTextView);// ��ӵ�������

						// ��ť
						Button putButton = (Button) childview.findViewById(R.id.specialitem_edit_button_button);
						putButton.setOnClickListener(new View.OnClickListener() {

							@Override
							public void onClick(View v) {
								remarkResult = editText.getText().toString().trim();
								saveDataToDB(lastResult, lastResult.SpecialItemResult, remarkResult, surveyTime);
								closeInputMethod(editText);
								// ListView�Ķ���:����ǰ�����һ��
								listCollapseAction();
							}
						});
						// �༭��
						// final EditText editText = new EditText(mContext);
						editText.setWidth(200);
						editText.setFocusable(isEditable);// �ж��Ƿ���Ա༭
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

						// resultLayout.addView(editText);// ��ӵ�������
						resultLayout.addView(childview);// ��ӵ�������

						// ����������
						group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

							@Override
							public void onCheckedChanged(RadioGroup group, int checkedId) {

								RadioButton rb = (RadioButton) resultLayout.findViewById(group.getCheckedRadioButtonId());
								String rbtext = rb.getText().toString();// ȡ�ñ�ѡ���RadioButton�е��ı�����

								for (HashMap<String, Object> map : resultType) {
									if (rbtext.equals(map.get("rname"))) {
										resultTypeId = (String) map.get("id");
										// Log.i(TAG, "��ȡ���->" + rbtext + "ID->"
										// + resultTypeId);
									}
								}

								// String info = "���㹫˾�������ڵ��ҵ�λ���д����������л����֣�";
								remarkResult = editText.getText().toString().trim();

								// �����ݿ��б�������
								saveDataToDB(lastResult, resultTypeId, remarkResult, surveyTime);
								closeInputMethod(editText);
								// ListView�Ķ���:����ǰ�����һ��
								// listCollapseAction();
								// reloadListView();
								// notifyDataSetChanged();
								reloadListView();
								// editText.setText("");

								// ���һ�������򷵻���һҳ
								/*
								 * if (groupPosition == LIST_SIZT - 1) {
								 * SpecialItemActivity.this.finish(); }
								 */

							}

						});
						// =======================�����ı���==========================================
					} else if (contrlType.contains("Text")) {
						String lastSpecialItem = lastResult.SpecialItemResult;
						if (lastSpecialItem != null && loadLastBoolean) {
							specialItemText = lastResult.SpecialItemResult;
							loadLastBoolean = false;
						}
						View childview = mInflater.inflate(R.layout.specialitem_edit_button, null);

						// ��ʾ�ı���
						TextView noteTextView = new TextView(mContext);
						noteTextView.setPadding(25, 0, 5, 5);
						noteTextView.setText("�����룺");
						// noteTextView.requestFocus();
						// noteTextView.setEnabled(isEditable);
						resultLayout.addView(noteTextView);// ��ӵ�������

						// �����ı���
						final EditText editTextView = (EditText) childview.findViewById(R.id.specialitem_edit_button_editText);
						editTextView.setWidth(250);
						editTextView.setFocusable(isEditable);
						// editTextView.requestFocus();
						setInputState(editTextView);
						editTextView.setFilters(new InputFilter[] { new InputFilter.LengthFilter(100) });
						if (isEditable) {
							editTextView.setEnabled(true);
						}
						// ֱ�Ӹ��轹��

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

						// resultLayout.addView(editTextView);// ��ӵ�������

						// ��ť
						Button putButton = (Button) childview.findViewById(R.id.specialitem_edit_button_button);
						// putButton.setText(android.R.string.ok);
						// putButton.setWidth(200);
						putButton.setFocusable(false);
						// resultLayout.addView(putButton);// ��ӵ�������
						resultLayout.addView(childview);
						putButton.setOnClickListener(new View.OnClickListener() {

							@Override
							public void onClick(View v) {
								String specialItemResult = editTextView.getText().toString().trim();

								// ȷ���û�������Ϣ֮��ı�״̬
								if (!"".equals(specialItemResult)) {
									String remarkStr = "";
									// �����ݿ��б�������
									saveDataToDB(lastResult, specialItemResult, remarkStr, surveyTime);
									editTextView.setText("");
									closeInputMethod(editTextView);
								}

								// ListView�Ķ���:����ǰ�����һ��
								listCollapseAction();

								// ���һ�������򷵻���һҳ
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

		// �ر��ٴ򿪵�ǰ�ĵ��ѡ��
		protected void reloadListView() {
			// ������ջ�֮ǰ�򿪵�Group
			listView.collapseGroup(expandGroupID);
			// ����һ��Group
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

			if (currNode.children.size() < 1) {// Ҷ�ӽڵ�
				queryNodeResult(currNode, groupName);

			} else {
				groupName.setText(currNode.title);
			}
			/*
			 * String spename = currNode.title; if (!"".equals(textResult)) {
			 * if(textResult.equals("��")){ spename =
			 * "<sub><font color='#FF0000'>" + spename + "</font></sub>"; }
			 * String textInfo = spename + "<sub><font color='#0288e7'>[" +
			 * textResult + "]</font></sub>";
			 * groupName.setText(Html.fromHtml(textInfo)); }
			 */
			// ��ѯ���ݿ⣬ʵʱ��ֵ
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
	 * CheckGroup�ĵ���¼�����
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
	 * //������ջ�֮ǰ�򿪵�Group listView.collapseGroup(expandGroupID); //����һ��Group int
	 * index = expandGroupID + 1; if(index == 6) { return; }
	 * listView.expandGroup(index); }
	 * 
	 * }
	 */

	/**
	 * ListView�Ķ��� չ��������
	 */
	private void listCollapseAction() {
		// ������ջ�֮ǰ�򿪵�Group
		listView.collapseGroup(expandGroupID);
		// ����һ��Group
		int index = expandGroupID + 1;
		if (index == LIST_SIZT) {
			return;
		}
		listView.expandGroup(index);

	}

	/**
	 * FileName: SpecialItemActivity.java Description:�ϴν���Ķ���
	 * 
	 * @author Administrator
	 * @Version 1.3.4
	 * @Copyright �п���ͼ���¿Ƽ����޹�˾<br>
	 *            Create at: 2012-12-4 ����11:22:57
	 */
	private final class LastResult {
		public String TaskID; // ������
		public String EnterID; // ��ҵ���
		public String IndustryID; // ��ҵ���
		public String SpecialItemID; // ר����
		public String SpecialItemResult; // ������ͱ�Ż����ı�
		public String Text; // ����ı�
		public String Remark; // ��ע��Ϣ
		public boolean flag; // �Ƿ��Ѿ�����

		// ����������
		// ��ʼ��ֵ�������ţ���ҵ��ţ�ģ���š�ר����
		public LastResult(String taskID, String enterID, String industryID, String specialItemID) {
			super();
			TaskID = taskID;
			EnterID = enterID;
			IndustryID = industryID;
			SpecialItemID = specialItemID;
		}
	}

	/**
	 * ��ѯ���ݿ� ͨ������ID��ר��ID����ȡ�ϴν��
	 * 
	 * @param qyID
	 *            ��ҵID
	 * @return ��ע��Ϣ
	 * @author Administrator<br>
	 *         Create at: 2012-12-4 ����11:20:56
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
			// �ϴ�ѡ����Ϣ���ı�
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
		// //�ϴ�ѡ����Ϣ���ı�
		// lastResult.Text = getLastTaskResultText(rwID, qyID, specialItemID);
		// }
		// }
		return lastResult;
	}

	/**
	 * ��ѯ���ݿ� ͨ������ID��ר��ID����ȡ���
	 * 
	 * @param qyID
	 *            ��ҵID
	 * @return �ϴ�ѡ����Ϣ���ı�
	 * @author Administrator<br>
	 *         Create at: 2012-12-4 ����11:20:56
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
	 * ��ѯ���ݿ� ��ȡ�������ͣ��ı�����ѡ��
	 * 
	 * @param resultTypeID
	 *            �������ID
	 * @author Administrator<br>
	 *         Create at: 2012-12-4 ����11:20:56
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
	 * ��������
	 * 
	 * @param lastResult
	 *            ����������
	 * @param result
	 *            �������
	 * @param remark
	 *            ��ע��Ϣ�ı�
	 * @param surveyTime
	 *            ����ʱ��
	 * @author Administrator<br>
	 *         Create at: 2012-12-4 ����11:20:56
	 */
	private void saveDataToDB(final LastResult lastResult, String result, final String remark, final String surveyTime) {
		String rwID = lastResult.TaskID;
		String qyID = lastResult.EnterID;
		String industryID = lastResult.IndustryID;
		String specialItemID = lastResult.SpecialItemID;

		if (lastResult.flag) {
			// �Ѿ�������Ϣ������
			// �������ݵ����ݿ���
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
		LogUtil.i(TAG, "��Ϣ��������->" + rwID + "��ҵ����->" + qyID + "��ҵ���->" + industryID + "ר����->" + specialItemID + "���(�ı�����)->" + result + "��ע->" + remark + "����ʱ��->" + surveyTime);
	//BYK
		// �ı�����״̬Ϊִ����
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
	 * �����������뵽���ݿ�
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
	 *         Create at: 2012-12-4 ����11:20:56
	 */
	private boolean insertTaskResult(String rwID, String qyID, String industryID, String specialItemID, String specialItemResult, String remark, String surveyTime) {
			
		if (TextUtils.isEmpty(industryID))
			industryID = "1";
		String sql = "insert into YDZF_TaskSpecialItem(TaskID,EnterID,IndustryID,SpecialItemID,SpecialItemResult,Remark,SurveyTime) " + "values('" + rwID + "','" + qyID + "','"
				+ industryID + "','" + specialItemID + "','" + specialItemResult + "','" + remark + "','" + surveyTime + "')";
		ArrayList<HashMap<String,Object>> qdData = getQDData();
		//�ж��Ƿ���ӹ�
		if (qdData.size()>0) {
			
		}else{
			rwxx.addBL2(tname, rwID, qyID,tid);
		}
		 	
		
		return SQLiteDataProvider.getInstance().ExecSQL(sql);
	}
	/**
	 * ��ȡ�嵥����
	 * */
	private ArrayList<HashMap<String, Object>> getQDData() {

		ArrayList<HashMap<String, Object>> data = new ArrayList<HashMap<String, Object>>();

		/** ��ѯ����¼���е����� */
		data = SqliteUtil.getInstance().queryBySqlReturnArrayListHashMap(
				"select * from ExeLawsTemplet where taskid = '" + rwbh + "' and enterid = '" + qyid
						+ "' and templettype = '"+tid+"' order by updatetime desc");
		return data;
	}

	/**
	 * ��ѯר��Ľ��ֵ�ı�
	 * 
	 * @author Liusy
	 * @param node
	 *            ��ǰ�ڵ�
	 * @return ��ǰ�ڵ�Ľ��ֵ
	 * @author Administrator<br>
	 *         Create at: 2012-12-4 ����11:20:56
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

		if (data.size() == 0) {// ���û��ѡ�����ֻ��ʾ����ǰ�ı���
			tv.setText(resultText);
			return;
		}

		// �ж���type֮����߼�

		String sqlStr = "select WFBS as answer from YDZF_SpecialItem where ID='" + node.SpecialItemID + "'";

		ArrayList<HashMap<String, Object>> result1 = SQLiteDataProvider.getInstance().queryBySqlReturnArrayListHashMap(sqlStr);
		String answer = "";
		for (HashMap<String, Object> hashMap : result1) {
			answer = (String) hashMap.get("answer");
		}

		String typeStr = (String) data.get(0).get("type");
		if (typeStr != null && typeStr.contains("Text")) {// textʱ����ʾ������е���Ϣ
			sqlText = "SELECT SpecialItemResult AS text FROM YDZF_TaskSpecialItem WHERE TaskID ='" + node.rwID + "' and IndustryID = '" + node.TID + "' and EnterID='" + qyid
					+ "'and SpecialItemID ='" + node.SpecialItemID + "' and text is not null";

		} else if (typeStr != null && typeStr.contains("Check")) {// check��ʱ����ʾ��ѡ�����Ϣ
			sqlText = "SELECT RName AS text FROM YDZF_ResultType WHERE ID = " + "(SELECT SpecialItemResult as id FROM YDZF_TaskSpecialItem WHERE TaskID ='" + node.rwID
					+ "' and EnterID='" + qyid + "' and IndustryID = '" + node.TID + "'and SpecialItemID ='" + node.SpecialItemID + "' and id is not null)";
		}

		try {
			data = SQLiteDataProvider.getInstance().queryBySqlReturnArrayListHashMap(sqlText);
			if (data == null || data.size() == 0) {// �����û�鵽��Ϣ������ʾ��ע��ߵĶ���
				if (typeStr != null && typeStr.contains("Check")) {
					sqlText = "select Remark as text from YDZF_TaskSpecialItem  WHERE TaskID = '" + node.rwID + "'and SpecialItemID ='" + node.SpecialItemID + "' and EnterID='"
							+ qyid + "' and Remark is not null";
				}
				data = SQLiteDataProvider.getInstance().queryBySqlReturnArrayListHashMap(sqlText);
			}

		} catch (Exception e) {
			Log.i(TAG, e.getMessage());
		}

		// data�е����ݾ��ǵ�ѡ��ť�����֣������������Ϣ�����ߵ�ѡ��ťʱ��ı�ע��Ϣ

		if (data != null && data.size() > 0) {// �������¼�������ݣ�����ִ����
			for (HashMap<String, Object> map : data) {
				if (typeStr != null && typeStr.contains("Check")) {

					String text = (String) map.get("text");
					if (text != null && !"".equals(text) && ("��".equals(text) || "��".equals(text) || "����".equals(text))) {
						if (((String) map.get("text")).equals(answer) || "����".equals(answer)) {

							String result = "<span><font color='#0288e7'>[" + (String) map.get("text") + "]</font></span>";
							resultText = "<span><font color='#FF0000'>" + resultText + "</font></span>" + result;
							tv.setText(Html.fromHtml(resultText));
						} else {// ѡ������
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
		} else {// ��������ִ������ѯ�ϴμ����
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
					result = "<span><font color='#A9A9A9'>[" + result + "(�ϴμ�¼)" + "]</font></span>";
					tv.setText(Html.fromHtml(resultText + result));

				}
			} else {
				tv.setText(resultText);
			}

		}

	}

	/**
	 * �ر����뷨
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

		Log.i("wang", "��ǰ�ֻ�ϵͳ�汾��Ϊ-->" + currentapiVersion);
		if (currentapiVersion > 10) {
			editText.requestFocus();
		}

	}

	@Override
	public void onClick(View v) {
		// TODO:����popwindow
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

	// ���´���Ϊpopwindow��button�¼���������ʱʹ��ǿ���Control+CV

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		Log.i("info", "ִ�з��ؽ��special");
		super.onActivityResult(requestCode, resultCode, data);
	}

}