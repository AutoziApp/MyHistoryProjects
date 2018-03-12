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
 * ִ��ר��--����չ��ListView
 * ��ʾר��
 * @author Liusy
 * 2012-7-4
 */
public class JCKHItemActivity extends BaseActivity {
	ExpandableListView listView = null;
	private int expandGroupID = 0;
	
	//��ҳ�沼��
	private RelativeLayout fatherRelativeLayout;
	private LinearLayout middleLayout;
	private Intent intent;	
	private TreeNode frontdata = null;//ǰһҳ������Node
	private List<TreeNode> childNodeList;//��ǰNode������Ҷ�ӽڵ㼯��
	
	//ȫ�ֱ���
	private int LIST_SIZT;
	private String surveyTime = Global.getGlobalInstance().getDate();
	private String resultTypeId = "";//�������ID
	private String remarkResult = "";//�����ע�ı�
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
		
		// ����״̬
		requestWindowFeature(Window.FEATURE_NO_TITLE);
//		getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,
//				WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		
		setContentView(R.layout.ui_mapuni);
		//�޸ı���
		RelativeLayout relativeLayout = (RelativeLayout)findViewById(R.id.parentLayout);
		relativeLayout.setBackgroundColor(Color.rgb(245,245,245));
		
		// ��ȡǰҳ���ݵ���Ϣ
		intent = getIntent();
		frontdata = (TreeNode) intent.getExtras().get("node");// ���ǰһҳ�洫��Node����
		childNodeList = frontdata.getChildren();
		LIST_SIZT = childNodeList.size();
		// ��ʼ�����ֿؼ�
		initViews(this);
	}
    
    
    
    /**
     * ��ʼ������
     * ���ÿؼ�������
	 * @author Liusy
	 */
	private void initViews(Context context) {	
		//���಼��
		LinearLayout linearLayout = (LinearLayout)findViewById(R.id.ui_mapuni_divider);
		linearLayout.setVisibility(View.GONE);
		fatherRelativeLayout = (RelativeLayout) findViewById(R.id.parentLayout);
		middleLayout = (LinearLayout) findViewById(R.id.middleLayout);
		
		
		if (frontdata.getChildren() != null){
			//�趨��������
			Collections.sort(frontdata.getChildren());
			SetBaseStyle(fatherRelativeLayout, frontdata.title);
		}
		setTitleLayoutVisiable(true);//�������Ƿ�ɼ�
		
		//���಼�֣���ӵ����಼����
		LayoutInflater mInflater = LayoutInflater.from(context);
		LinearLayout specialItemLayout = (LinearLayout) mInflater.inflate(R.layout.specialitem, null);
		middleLayout.addView(specialItemLayout, new LayoutParams(
				LinearLayout.LayoutParams.FILL_PARENT,
				LinearLayout.LayoutParams.FILL_PARENT));
		
		 //��ȡExpandableListView
        listView = (ExpandableListView)specialItemLayout.findViewById(android.R.id.list);
        listView.setAdapter(new MyExpandableAdapter(this, childNodeList));
        //���ñ���ͼƬ��ͼƬ�ָ���
        listView.setGroupIndicator(this.getResources().getDrawable(R.layout.expandablelistviewselector));
        listView.setDivider(getResources().getDrawable(R.drawable.list_divider));
        listView.setChildDivider(getResources().getDrawable(R.drawable.list_divider));
        //Ĭ��չ����һ��
        listView.expandGroup(0);
        
        /**
         * ����Group��
         * expandGroupID����ʵʱ�򿪵�GroupID
         */
        listView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
			
			@Override
			public void onGroupExpand(int groupPosition) {
				listView.setSelectionFromTop(groupPosition - 1, 10);
				if(expandGroupID != groupPosition) {
					//����ǰ��Group
					listView.collapseGroup(expandGroupID);
					//����һ��Group				
				}
				expandGroupID = groupPosition;
			}
		});
        
        /**
         * ������Group����
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
         * �������������¼�
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
	 * ExpandableList����������
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
		
			//��ǰר��
			//�����������
			TreeNode currNode = childNodeList.get(groupPosition);
			LastResult result0 = new LastResult(currNode.rwID, currNode.qyID, currNode.TID, currNode.SpecialItemID);
			//�Խ�����м���
			final LastResult lastResult = getLastResult(currNode.rwID, currNode.SpecialItemID, result0);
			
			//��ȡ�������
			//�ǡ���[һ�ࡢ���ࡢ���ࡢ����]ID,RName,ControlType
			final ArrayList<HashMap<String, Object>> resultType = getResultTypeData(currNode.ResultTypeID);			
			
			if(resultType != null) {
				try{
					String contrlType = (String) resultType.get(0).get("controltype");
			

				//==================��ѡ��ť=================================================
				if(contrlType.contains("Check")) {
					LogUtil.i(TAG, "��ʾ��ѡ��ť��");
					
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
						
						//����Ĭ��ѡ��
						if(resultText.equals(lastResult.Text)){ 
							radioButton.setChecked(true);
						}
						group.addView(radioButton);
					}
					resultLayout.addView(group);//��ӵ�������
					
					//�ı���
					TextView noteTextView = new TextView(mContext);
					noteTextView.setPadding(25, 0, 5, 5);
					noteTextView.requestFocus();
					String remarkStr = "��ע��";
					String remark = currNode.RemarkTip;
					if(!"".equals(remark)){
						remarkStr = remark;
					}
					noteTextView.setText(remarkStr);
					noteTextView.setTextColor(Color.BLACK);
					resultLayout.addView(noteTextView);//��ӵ�������
					
					//�༭��
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
					resultLayout.addView(editText);//��ӵ�������
					
					editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
						
						@Override
						public void onFocusChange(View v, boolean hasFocus) {
							/*String s = ((EditText)v).getText().toString();*/
							if(!hasFocus) {
								remarkString = ((EditText)v).getText().toString();
								
							}
						}
					});
					
					//����������
					group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
						
						@Override
						public void onCheckedChanged(RadioGroup group, int checkedId) {
							buttonOnClickBoolean = false;
							
							RadioButton rb = (RadioButton) resultLayout.findViewById(group.getCheckedRadioButtonId());
							String rbtext = rb.getText().toString();// ȡ�ñ�ѡ���RadioButton�е��ı�����
												
							for(HashMap<String, Object> map : resultType){
								if(rbtext.equals((String) map.get("rname"))){
									resultTypeId = (String) map.get("id");
									//Log.i(TAG, "��ȡ���->" + rbtext + "ID->" + resultTypeId);
								}
							}
							
							remarkResult = editText.getText().toString().trim();
							//�����ݿ��б�������
							saveDataToDB(lastResult, resultTypeId, remarkResult, surveyTime);
							
							//ListView�Ķ���:����ǰ�����һ��
							listCollapseAction();
							editText.setText("");
							
							// ���һ�������򷵻���һҳ
							if (groupPosition == LIST_SIZT - 1) {
								JCKHItemActivity.this.finish();
							}
							
						}
						
					});
				//=======================�����ı���==========================================	
				} else if (contrlType.contains("Text")) {
					String lastSpecialItem = lastResult.SpecialItemResult;
					if(lastSpecialItem != null && loadLastBoolean){
						specialItemText = lastResult.SpecialItemResult;
						loadLastBoolean = false;
					}
					//Log.i(TAG, "��ʾ�����ı���");
					//��ʾ�ı���
					TextView noteTextView = new TextView(mContext);
					noteTextView.setPadding(25, 0, 5, 5);
					noteTextView.setText("�����룺");
					resultLayout.addView(noteTextView);//��ӵ�������
					
					//�����ı���
					final EditText editTextView = new EditText(mContext);
					editTextView.setWidth(250);
					editTextView.setInputType(EditorInfo.TYPE_CLASS_NUMBER);
					//ֱ�Ӹ��轹��
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
					resultLayout.addView(editTextView);//��ӵ�������
					
				
					//��ť
					Button putButton = new Button(mContext);
					putButton.setText(android.R.string.ok);
					putButton.setWidth(200);
					resultLayout.addView(putButton);//��ӵ�������
					
					putButton.setOnClickListener(new View.OnClickListener() {
						
						@Override
						public void onClick(View v) {
							String specialItemResult = editTextView.getText().toString().trim();
							
							//ȷ���û�������Ϣ֮��ı�״̬
							if(!"".equals(specialItemResult)){							
								
								String remarkStr = "";
								//�����ݿ��б�������
								saveDataToDB(lastResult, specialItemResult, remarkStr, surveyTime);
								editTextView.setText("");
							}	
							
							//ListView�Ķ���:����ǰ�����һ��
							listCollapseAction();
							
							// ���һ�������򷵻���һҳ
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
			if(currNode.children.size() < 1) {//Ҷ�ӽڵ�
				textResult = queryNodeResult(currNode);
				if(textResult.length() > 1) {
					textResult = FileHelper.formatTextLength(textResult, 8);
				}
			}
			if(!"".equals(textResult)) {
				String textInfo = groupName.getText().toString() + "<sub><font color='#0288e7'>[" + textResult + "]</font></sub>";
				groupName.setText(Html.fromHtml(textInfo));
			}
			//��ѯ���ݿ⣬ʵʱ��ֵ
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
     * @author Liusy
     * 2012-7-4
     */
    private final class CheckGroupListener implements OnCheckedChangeListener {

		@Override
		public void onCheckedChanged(RadioGroup group, int checkedId) {
			System.out.println(checkedId);			
			
			//������ջ�֮ǰ�򿪵�Group
			listView.collapseGroup(expandGroupID);
			//����һ��Group
			int index = expandGroupID + 1;
			if(index == 6) {
				return;
			}
			listView.expandGroup(index);
		}
    	
    }
    
    /**
	 * ListView�Ķ���
	 * @author Liusy
	 */
	private void listCollapseAction() {
		//������ջ�֮ǰ�򿪵�Group
		listView.collapseGroup(expandGroupID);
		//����һ��Group
		int index = expandGroupID + 1;
		if(index == LIST_SIZT) {
			return;
		}
		listView.expandGroup(index);
		
		LogUtil.i("TAG","��ʾ--->" + expandGroupID);
	}
    
    /**
	 * �ϴν���Ķ���
	 * @author Lsy
	 *
	 */
	public final class LastResult {
		public String TaskID;				//������
		public String EnterID;				//��ҵ���
		public String IndustryID;			//��ҵ���
		public String SpecialItemID;		//ר����
		public String SpecialItemResult;	//������ͱ�Ż����ı�
		public String Text;					//����ı�
		public String Remark;				//��ע��Ϣ
		public boolean flag;				//�Ƿ��Ѿ�����
		//����������
		//��ʼ��ֵ�������ţ���ҵ��ţ�ģ���š�ר����
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
	 * ��ѯ���ݿ�
	 * ͨ������ID��ר��ID����ȡ�ϴν��
	 * @param qyID ��ҵID
	 * @return ��ע��Ϣ 
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
			//�ϴ�ѡ����Ϣ���ı�
			lastResult.Text = getLastTaskResultText(depID, specialItemID);
			lastResult.flag = true;
		} else {
			lastResult.flag = false;
		}
		return lastResult;
	}
	
	/**
	 * ��ѯ���ݿ�
	 * ͨ������ID��ר��ID����ȡ���
	 * @param qyID ��ҵID
	 * @return �ϴ�ѡ����Ϣ���ı�
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
	 * ��ѯ���ݿ�
	 * ��ȡ�������ͣ��ı�����ѡ��
	 * @param resultTypeID �������ID
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
	 * ��������
	 * @param lastResult	����������
	 * @param result	          �������
	 * @param remark		��ע��Ϣ�ı�
	 * @param surveyTime	����ʱ��
	 * @param hasValue		������߸���
	 */
	private void saveDataToDB(final LastResult lastResult, final String result, final String remark, final String surveyTime) {		
		String departmentID = lastResult.TaskID;
		String qyID = lastResult.EnterID;
		String industryID = lastResult.IndustryID;
		String specialItemID = lastResult.SpecialItemID;
				
		if(lastResult.flag) {
			//�Ѿ�������Ϣ������
			//�������ݵ����ݿ���
			ContentValues contentValues = new ContentValues();
			contentValues.put("SpecialItemResult", result);
			contentValues.put("Remark", remark);
			contentValues.put("SurveyTime", surveyTime);
			LogUtil.i(TAG, "Update !");
			if(SQLiteDataProvider.getInstance().updateTable(RESULT_TABLE, contentValues, " DepID = ? and SpecialItemID = ?", new String[]{departmentID, specialItemID})){
				LogUtil.i(TAG, "Update Success");
			}
			//note = "������ɣ�";
		} else {
			
			//û�б�����Ϣ������
			LogUtil.i(TAG, "Insert !");
			if(insertTaskResult(departmentID, qyID, industryID, specialItemID, result, remark, surveyTime)){
				LogUtil.i(TAG, "Insert Success");
			}
		}
		LogUtil.i(TAG, "��Ϣ��������->" + departmentID +"��ҵ����->"+ qyID +"��ҵ���->"+ industryID +"ר����->"
				+ specialItemID +"���(�ı�����)->"+ result +"��ע->"+ remark +"����ʱ��->"+ surveyTime);	
		//�ı�����״̬Ϊִ����
		if(!isTaskStart) {
			LogUtil.i(TAG, "�ı�״̬ " + departmentID + " ---->ִ����");
			isTaskStart = true;
			//changeTaskStateOn(departmentID);
		}		
	}
	
	/**
	 * �����������뵽���ݿ�
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
	 * ��ѯר��Ľ��ֵ�ı�
	 * @author Liusy
	 * @param node		��ǰ�ڵ�
	 * @return			��ǰ�ڵ�Ľ��ֵ
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