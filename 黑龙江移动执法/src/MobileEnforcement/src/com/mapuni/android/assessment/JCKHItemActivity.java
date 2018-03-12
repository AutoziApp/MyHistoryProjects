package com.mapuni.android.assessment;

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
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.AbsListView;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mapuni.android.MobileEnforcement.R;
import com.mapuni.android.base.BaseActivity;
import com.mapuni.android.base.Global;
import com.mapuni.android.base.util.LogUtil;
import com.mapuni.android.business.TreeNode;
import com.mapuni.android.dataprovider.FileHelper;
import com.mapuni.android.dataprovider.SQLiteDataProvider;
/**
 * 执法专项--可扩展的ListView
 * 显示专项
 * @author Liusy
 * 2012-7-4
 */
public class JCKHItemActivity extends BaseActivity {
	ExpandableListView listView = null;
	private int expandGroupID = 0;
	
	//父页面布局
	private RelativeLayout fatherRelativeLayout;
	private LinearLayout middleLayout;
	private Intent intent;	
	private TreeNode frontdata = null;//前一页面点击的Node
	private List<TreeNode> childNodeList;//当前Node的所有叶子节点集合
	
	//全局变量
	private int LIST_SIZT;
	private String surveyTime = Global.getGlobalInstance().getDate();
	private String resultTypeId = "";//结果类型ID
	private String remarkResult = "";//结果备注文本
	boolean isTaskStart = false;
	
	public  final String RESULT_TABLE = "YDZF_JCKH_DepartmentItem";
	private  final String TAG = "JCKHItemActivity";
	
	private String remarkString = "";
	private String specialItemText = "";
	private boolean loadLastBoolean = true;
	private boolean buttonOnClickBoolean = true;
	
    @Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		// 窗口状态
		requestWindowFeature(Window.FEATURE_NO_TITLE);
//		getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,
//				WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		
		setContentView(R.layout.ui_mapuni);
		//修改背景
		RelativeLayout relativeLayout = (RelativeLayout)findViewById(R.id.parentLayout);
		relativeLayout.setBackgroundColor(Color.rgb(245,245,245));
		
		// 获取前页传递的信息
		intent = getIntent();
		frontdata = (TreeNode) intent.getExtras().get("node");// 获得前一页面传的Node对象
		childNodeList = frontdata.getChildren();
		LIST_SIZT = childNodeList.size();
		// 初始化布局控件
		initViews(this);
	}
    
    
    
    /**
     * 初始化布局
     * 设置控件监听器
	 * @author Liusy
	 */
	private void initViews(Context context) {	
		//父类布局
		LinearLayout linearLayout = (LinearLayout)findViewById(R.id.ui_mapuni_divider);
		linearLayout.setVisibility(View.GONE);
		fatherRelativeLayout = (RelativeLayout) findViewById(R.id.parentLayout);
		middleLayout = (LinearLayout) findViewById(R.id.middleLayout);
		
		
		if (frontdata.getChildren() != null){
			//设定基本布局
			Collections.sort(frontdata.getChildren());
			SetBaseStyle(fatherRelativeLayout, frontdata.title);
		}
		setTitleLayoutVisiable(true);//标题栏是否可见
		
		//子类布局，添加到父类布局中
		LayoutInflater mInflater = LayoutInflater.from(context);
		LinearLayout specialItemLayout = (LinearLayout) mInflater.inflate(R.layout.specialitem, null);
		middleLayout.addView(specialItemLayout, new LayoutParams(
				LinearLayout.LayoutParams.FILL_PARENT,
				LinearLayout.LayoutParams.FILL_PARENT));
		
		 //获取ExpandableListView
        listView = (ExpandableListView)specialItemLayout.findViewById(android.R.id.list);
        listView.setAdapter(new MyExpandableAdapter(this, childNodeList));
        //设置背景图片、图片分割线
        listView.setGroupIndicator(this.getResources().getDrawable(R.layout.expandablelistviewselector));
        listView.setDivider(getResources().getDrawable(R.drawable.list_divider));
        listView.setChildDivider(getResources().getDrawable(R.drawable.list_divider));
        //默认展开第一项
        listView.expandGroup(0);
        
        /**
         * 监听Group打开
         * expandGroupID保存实时打开的GroupID
         */
        listView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
			
			@Override
			public void onGroupExpand(int groupPosition) {
				listView.setSelectionFromTop(groupPosition - 1, 10);
				if(expandGroupID != groupPosition) {
					//收起当前的Group
					listView.collapseGroup(expandGroupID);
					//打开下一个Group				
				}
				expandGroupID = groupPosition;
			}
		});
        
        /**
         * 监听，Group收起
         */
        listView.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {
			
			@Override
			public void onGroupCollapse(int groupPosition) {
				//Toast.makeText(MainActivity.this, "OnGroupCollapse", Toast.LENGTH_SHORT).show();
				remarkString = "";
				specialItemText = "";
				loadLastBoolean = true;
				buttonOnClickBoolean = true;
				
			}
		});        
        
        /**
         * 监听，子类点击事件
         */
        listView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
			
			@Override
			public boolean onChildClick(ExpandableListView parent, View v,
					int groupPosition, int childPosition, long id) {
				
				return false;
			}
		});
	}


	/**
	 * ExpandableList数据适配器
	 * @author Liusy
	 * 2012-7-5
	 */
	private final class MyExpandableAdapter extends BaseExpandableListAdapter { 	   	
    	private Context mContext;
    	private List<TreeNode> childNodeList;
    	
		public MyExpandableAdapter(Context context, List<TreeNode> nodeList) {
			mContext = context;
			childNodeList = nodeList;
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
		public View getChildView(final int groupPosition, int childPosition,
				boolean isLastChild, View convertView, ViewGroup parent) {
			LayoutInflater mInflater = LayoutInflater.from(mContext);
			AbsListView.LayoutParams lp = new AbsListView.LayoutParams(
	                ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
			
			View view = mInflater.inflate(R.layout.specialitem_child, null);
			final LinearLayout resultLayout = (LinearLayout) view.findViewById(R.id.result_layout);	
			resultLayout.setLayoutParams(lp);
		
			//当前专项
			//构建结果布局
			TreeNode currNode = childNodeList.get(groupPosition);
			LastResult result0 = new LastResult(currNode.rwID, currNode.qyID, currNode.TID, currNode.SpecialItemID);
			//对结果进行加载
			final LastResult lastResult = getLastResult(currNode.rwID, currNode.SpecialItemID, result0);
			
			//获取结果类型
			//是、否[一类、二类、三类、四类]ID,RName,ControlType
			final ArrayList<HashMap<String, Object>> resultType = getResultTypeData(currNode.ResultTypeID);			
			
			if(resultType != null) {
				try{
					String contrlType = (String) resultType.get(0).get("controltype");
			

				//==================单选按钮=================================================
				if(contrlType.contains("Check")) {
					LogUtil.i(TAG, "显示单选按钮！");
					
					String lastRemark = lastResult.Remark;
					if(lastRemark != null && loadLastBoolean){
						remarkString = lastResult.Remark;
						loadLastBoolean = false;
					}
					
					final RadioGroup group = new RadioGroup(mContext);
					group.setPadding(15, 0, 5, 0);
					for(HashMap<String, Object> map : resultType) {
						String resultText = (String) map.get("rname");
						RadioButton radioButton = new RadioButton(mContext);
						radioButton.setText(resultText);
						radioButton.setTextColor(Color.BLACK);
						
						//设置默认选中
						if(resultText.equals(lastResult.Text)){ 
							radioButton.setChecked(true);
						}
						group.addView(radioButton);
					}
					resultLayout.addView(group);//添加到布局中
					
					//文本框
					TextView noteTextView = new TextView(mContext);
					noteTextView.setPadding(25, 0, 5, 5);
					noteTextView.requestFocus();
					String remarkStr = "备注：";
					String remark = currNode.RemarkTip;
					if(!"".equals(remark)){
						remarkStr = remark;
					}
					noteTextView.setText(remarkStr);
					noteTextView.setTextColor(Color.BLACK);
					resultLayout.addView(noteTextView);//添加到布局中
					
					//编辑框
					final EditText editText = new EditText(mContext);
					editText.setWidth(200);
					editText.setInputType(EditorInfo.TYPE_CLASS_NUMBER);
					/*if(lastResult.Remark != null || lastResult.Remark.length() != 0) {
						editText.setText(lastResult.Remark);
					} else {
						editText.setText(remarkString);
					}*/
					editText.setText(remarkString);
					
					editText.setFilters(new InputFilter[] { new InputFilter.LengthFilter(100) });
					resultLayout.addView(editText);//添加到布局中
					
					editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
						
						@Override
						public void onFocusChange(View v, boolean hasFocus) {
							/*String s = ((EditText)v).getText().toString();*/
							if(!hasFocus) {
								remarkString = ((EditText)v).getText().toString();
								
							}
						}
					});
					
					//监听，保存
					group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
						
						@Override
						public void onCheckedChanged(RadioGroup group, int checkedId) {
							buttonOnClickBoolean = false;
							
							RadioButton rb = (RadioButton) resultLayout.findViewById(group.getCheckedRadioButtonId());
							String rbtext = rb.getText().toString();// 取得被选择的RadioButton中的文本内容
												
							for(HashMap<String, Object> map : resultType){
								if(rbtext.equals((String) map.get("rname"))){
									resultTypeId = (String) map.get("id");
									//Log.i(TAG, "获取结果->" + rbtext + "ID->" + resultTypeId);
								}
							}
							
							remarkResult = editText.getText().toString().trim();
							//向数据库中保存数据
							saveDataToDB(lastResult, resultTypeId, remarkResult, surveyTime);
							
							//ListView的动作:收起当前项、打开下一项
							listCollapseAction();
							editText.setText("");
							
							// 最后一个子项则返回上一页
							if (groupPosition == LIST_SIZT - 1) {
								JCKHItemActivity.this.finish();
							}
							
						}
						
					});
				//=======================输入文本框==========================================	
				} else if (contrlType.contains("Text")) {
					String lastSpecialItem = lastResult.SpecialItemResult;
					if(lastSpecialItem != null && loadLastBoolean){
						specialItemText = lastResult.SpecialItemResult;
						loadLastBoolean = false;
					}
					//Log.i(TAG, "显示输入文本框！");
					//提示文本框
					TextView noteTextView = new TextView(mContext);
					noteTextView.setPadding(25, 0, 5, 5);
					noteTextView.setText("请输入：");
					resultLayout.addView(noteTextView);//添加到布局中
					
					//输入文本框
					final EditText editTextView = new EditText(mContext);
					editTextView.setWidth(250);
					editTextView.setInputType(EditorInfo.TYPE_CLASS_NUMBER);
					//直接给予焦点
					editTextView.requestFocus();
					//String textContent = lastResult.SpecialItemResult;
					if(!"".equals(specialItemText)){
						editTextView.setText(specialItemText);
					}
					editTextView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
						
						@Override
						public void onFocusChange(View v, boolean hasFocus) {
							/*String s = ((EditText)v).getText().toString();*/
							if(!hasFocus) {
								specialItemText = ((EditText)v).getText().toString();
								
							}
						}
					});
					resultLayout.addView(editTextView);//添加到布局中
					
				
					//按钮
					Button putButton = new Button(mContext);
					putButton.setText(android.R.string.ok);
					putButton.setWidth(200);
					resultLayout.addView(putButton);//添加到布局中
					
					putButton.setOnClickListener(new View.OnClickListener() {
						
						@Override
						public void onClick(View v) {
							String specialItemResult = editTextView.getText().toString().trim();
							
							//确定用户输入信息之后改变状态
							if(!"".equals(specialItemResult)){							
								
								String remarkStr = "";
								//向数据库中保存数据
								saveDataToDB(lastResult, specialItemResult, remarkStr, surveyTime);
								editTextView.setText("");
							}	
							
							//ListView的动作:收起当前项、打开下一项
							listCollapseAction();
							
							// 最后一个子项则返回上一页
							if (groupPosition == LIST_SIZT - 1) {
								JCKHItemActivity.this.finish();
							}
						}
					});
					
				}				
				}catch (Exception e) {
				e.printStackTrace();
				}
			}
			
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
			return LIST_SIZT;
		}
		
		@Override
		public long getGroupId(int groupPosition) {	
			return groupPosition;
		}
		
		@Override
		public View getGroupView(int groupPosition, boolean isExpanded,
				View convertView, ViewGroup parent) {
			LayoutInflater mInflater = LayoutInflater.from(mContext);
			View view = mInflater.inflate(R.layout.specialitem_group, null);
			
			TextView groupName=(TextView)view.findViewById(R.id.specialitem_group_title); 
			String groupStr = childNodeList.get(groupPosition).title;
			groupName.setText(groupStr);
			
			TreeNode currNode = childNodeList.get(groupPosition);
			String textResult = "";
			if(currNode.children.size() < 1) {//叶子节点
				textResult = queryNodeResult(currNode);
				if(textResult.length() > 1) {
					textResult = FileHelper.formatTextLength(textResult, 8);
				}
			}
			if(!"".equals(textResult)) {
				String textInfo = groupName.getText().toString() + "<sub><font color='#0288e7'>[" + textResult + "]</font></sub>";
				groupName.setText(Html.fromHtml(textInfo));
			}
			//查询数据库，实时赋值
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
     * @author Liusy
     * 2012-7-4
     */
    private final class CheckGroupListener implements OnCheckedChangeListener {

		@Override
		public void onCheckedChanged(RadioGroup group, int checkedId) {
			System.out.println(checkedId);			
			
			//点击后收回之前打开的Group
			listView.collapseGroup(expandGroupID);
			//打开下一个Group
			int index = expandGroupID + 1;
			if(index == 6) {
				return;
			}
			listView.expandGroup(index);
		}
    	
    }
    
    /**
	 * ListView的动作
	 * @author Liusy
	 */
	private void listCollapseAction() {
		//点击后收回之前打开的Group
		listView.collapseGroup(expandGroupID);
		//打开下一个Group
		int index = expandGroupID + 1;
		if(index == LIST_SIZT) {
			return;
		}
		listView.expandGroup(index);
		
		LogUtil.i("TAG","显示--->" + expandGroupID);
	}
    
    /**
	 * 上次结果的对象
	 * @author Lsy
	 *
	 */
	public final class LastResult {
		public String TaskID;				//任务编号
		public String EnterID;				//企业编号
		public String IndustryID;			//行业编号
		public String SpecialItemID;		//专项编号
		public String SpecialItemResult;	//结果类型编号或者文本
		public String Text;					//结果文本
		public String Remark;				//备注信息
		public boolean flag;				//是否已经保存
		//含参数构造
		//初始赋值：任务编号，企业编号，模板编号、专项编号
		public LastResult(String taskID, String enterID, String industryID,
				String specialItemID) {
			super();
			TaskID = taskID;
			EnterID = enterID;
			IndustryID = industryID;
			SpecialItemID = specialItemID;
		}
	}
	
	/**
	 * 查询数据库
	 * 通过任务ID，专项ID，获取上次结果
	 * @param qyID 企业ID
	 * @return 备注信息 
	 */
	private LastResult getLastResult(String depID, String specialItemID, LastResult lastResult) {
		ArrayList<HashMap<String, Object>> data = new ArrayList<HashMap<String,Object>>();
		String sql = "SELECT * FROM YDZF_JCKH_DepartmentItem WHERE DepID ='"+depID+"' and IndustryID ='"+lastResult.IndustryID+"' and SpecialItemID ='"+specialItemID+"'";
		
		data = SQLiteDataProvider.getInstance().queryBySqlReturnArrayListHashMap(sql);
		
		if(data.size() > 0){
			for (HashMap<String, Object> map : data) {			
				lastResult.SpecialItemResult = (String)map.get("specialitemresult");
				lastResult.Remark = (String)map.get("remark");
			}
			//上次选择信息的文本
			lastResult.Text = getLastTaskResultText(depID, specialItemID);
			lastResult.flag = true;
		} else {
			lastResult.flag = false;
		}
		return lastResult;
	}
	
	/**
	 * 查询数据库
	 * 通过任务ID，专项ID，获取结果
	 * @param qyID 企业ID
	 * @return 上次选择信息的文本
	 */
	private String getLastTaskResultText(String departmentID, String specialItemID) {
		String text = "";
		ArrayList<HashMap<String, Object>> data = new ArrayList<HashMap<String,Object>>();
		String sql = "SELECT RName FROM YDZF_JCKH_ResultType WHERE ID = " +
				"(SELECT SpecialItemResult FROM YDZF_JCKH_DepartmentItem WHERE DepID ='"+departmentID+"' and SpecialItemID ='"+specialItemID+"')";
		LogUtil.i(TAG, sql);
	
		data = SQLiteDataProvider.getInstance().queryBySqlReturnArrayListHashMap(sql);
		
		for(HashMap<String, Object> map : data){
			text = (String) map.get("rname");
		}
		return text;
	}
	
	
	/**
	 * 查询数据库
	 * 获取其结果类型：文本、单选框
	 * @param resultTypeID 结果类型ID
	 */
	private ArrayList<HashMap<String, Object>> getResultTypeData(String resultTypeID) {
		ArrayList<HashMap<String, Object>> data = new ArrayList<HashMap<String,Object>>();
		String sql = "";
		sql = "select ID,RName,ControlType from YDZF_JCKH_ResultType where PID = '" + resultTypeID + "' order by sortIndex ";
		
			data = SQLiteDataProvider.getInstance().queryBySqlReturnArrayListHashMap(sql);
		
		if(data.size() == 0) {
			sql = "select RName,ControlType from YDZF_JCKH_ResultType where ID = '" + resultTypeID + "' order by sortIndex ";
			
				data = SQLiteDataProvider.getInstance().queryBySqlReturnArrayListHashMap(sql);
			
			return data;
		}
		return data;
	}
	
	/**
	 * 保存数据
	 * @param lastResult	保存结果对象
	 * @param result	          结果类型
	 * @param remark		备注信息文本
	 * @param surveyTime	调查时间
	 * @param hasValue		保存或者更新
	 */
	private void saveDataToDB(final LastResult lastResult, final String result, final String remark, final String surveyTime) {		
		String departmentID = lastResult.TaskID;
		String qyID = lastResult.EnterID;
		String industryID = lastResult.IndustryID;
		String specialItemID = lastResult.SpecialItemID;
				
		if(lastResult.flag) {
			//已经保存信息：更新
			//保存数据到数据库中
			ContentValues contentValues = new ContentValues();
			contentValues.put("SpecialItemResult", result);
			contentValues.put("Remark", remark);
			contentValues.put("SurveyTime", surveyTime);
			LogUtil.i(TAG, "Update !");
			if(SQLiteDataProvider.getInstance().updateTable(RESULT_TABLE, contentValues, " DepID = ? and SpecialItemID = ?", new String[]{departmentID, specialItemID})){
				LogUtil.i(TAG, "Update Success");
			}
			//note = "更新完成！";
		} else {
			
			//没有保存信息：插入
			LogUtil.i(TAG, "Insert !");
			if(insertTaskResult(departmentID, qyID, industryID, specialItemID, result, remark, surveyTime)){
				LogUtil.i(TAG, "Insert Success");
			}
		}
		LogUtil.i(TAG, "信息：任务编号->" + departmentID +"企业代码->"+ qyID +"行业编号->"+ industryID +"专项编号->"
				+ specialItemID +"结果(文本或编号)->"+ result +"备注->"+ remark +"调查时间->"+ surveyTime);	
		//改变任务状态为执行中
		if(!isTaskStart) {
			LogUtil.i(TAG, "改变状态 " + departmentID + " ---->执行中");
			isTaskStart = true;
			//changeTaskStateOn(departmentID);
		}		
	}
	
	/**
	 * 将任务结果插入到数据库
	 * @param departmentID
	 * @param industryID
	 * @param specialItemID
	 * @param specialItemResult
	 * @param remark
	 * @param surveyTime
	 * @return
	 */
	private boolean insertTaskResult(String departmentID,
			String qyID, String industryID,
			String specialItemID,
			String specialItemResult, String remark,
			String surveyTime) {
		
		if(TextUtils.isEmpty(industryID))
			industryID = "1";
		String sql = "insert into YDZF_JCKH_DepartmentItem(depID,IndustryID,SpecialItemID,SpecialItemResult,Remark,SurveyTime,Scroe) " +
				"values('"+departmentID+"','"+industryID+"','"+specialItemID+"','"+specialItemResult+"','"+remark+"','"+surveyTime+"','"+specialItemResult+"')";
		LogUtil.i(TAG, sql);
		return SQLiteDataProvider.getInstance().ExecSQL(sql);
	}	
	
	/**
	 * 查询专项的结果值文本
	 * @author Liusy
	 * @param node		当前节点
	 * @return			当前节点的结果值
	 */	
	private  String queryNodeResult(TreeNode node) {
		String sqlText = "SELECT SpecialItemResult as text FROM YDZF_JCKH_DepartmentItem WHERE depID = '"+node.rwID+"' and IndustryID = '"+node.TID+"' and SpecialItemID = '"+node.SpecialItemID+"'";
		ArrayList<HashMap<String, Object>> data = null;
		
		data = SQLiteDataProvider.getInstance().queryBySqlReturnArrayListHashMap(sqlText);
		
		if(data != null && data.size() > 0) {
			return (String)data.get(0).get("text");
		}
		return "";
	}
	
}