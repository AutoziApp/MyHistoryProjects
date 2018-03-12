package com.mapuni.android.enforcement;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.UUID;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.BaseAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.mapuni.android.MobileEnforcement.R;
import com.mapuni.android.base.Global;
import com.mapuni.android.base.util.DisplayUitl;
import com.mapuni.android.business.RWXX;
import com.mapuni.android.dataprovider.SqliteUtil;

public class EnforcementModel {
	private final String TABLENAME = "T_YDZF_RWXX";
	/** ��ʼ����ǰ��ʵ�����б�����Ĵ��� */
	public int ListScrollTimes = 1;
	/** �ֳ�ִ��������Դֵ */
	public final String rwly = "010";
	/** �ֳ�ִ����������ֵ����ʱ�Լ�� */
	public final String rwlx = "006";
	EnforcementRecordListAdapter enforcementRecordListAdapter;

	// private String temporaryCompany = "";
	// private String temporaryTask = "";

	public int getListScrollTimes() {
		return ListScrollTimes;
	}

	public void setListScrollTimes(int listScrollTimes) {
		ListScrollTimes = listScrollTimes;
	}

	/**
	 * Description: ����ʵ�ֲ�ѯ�б��ʱ����з�ҳ��ʾ
	 * 
	 * @return ����һ������ƴ�ӷ�ҳ��ʾsql�����ַ��� String String
	 * @author wanbglg
	 */
	public String getOrder() {
		int count = Global.getGlobalInstance().getListNumber();
		int x = getListScrollTimes() * count - count;
		int j = count;
		// String order = "UpdateTime desc limit " + x + "," + j;
		String order = x + "," + j;
		return order;
	}

	/**
	 * ����userid��ȡ�ֳ�ִ����¼���������ݲ���ȫ�����ݣ����Ƿ�����������
	 * 
	 * @param userID
	 * @param conditions
	 *            ���� ����õ�like��ѯ hashmap key --> '1' value-->
	 *            2+"' or"+�ֶ���+" like '%"+likeName+"%"
	 * @return
	 */
	public ArrayList<HashMap<String, Object>> getUserEnforcementRecord(String userID, HashMap<String, Object> conditions) {
		String ZW = Global.getGlobalInstance().getUserZW();
		String area = Global.getGlobalInstance().getAreaCode();
		String sqlstr = "select  a.guid,fbsj,rwbh,rwmc,rwmc as qymc, e.[QYID],e.[IsExcute] as  rwzt from T_YDZF_RWXX " + "as a left join T_YDZF_RWXX_USER b on a.RWBH "
				+ "=b.RWXXBH left join TaskEnpriLink e on a.rwbh=e.TaskID  where 1=1 and (a.rwly='" + rwly + "'  or a.rwly='013') and a.SyncDataRegionCode='" + area +"' and b.UserID='" + userID + "' ";
		if (ZW != null && !ZW.equals("")) {
			if ("1".equals(ZW) || "2".equals(ZW)) {
				sqlstr = "select  a.guid,fbsj,rwbh,rwmc,rwmc as qymc, e.[QYID],e.[IsExcute] as  rwzt from T_YDZF_RWXX " + "as a   left join T_YDZF_RWXX_USER b on a.RWBH"
						+ "=b.RWXXBH left join TaskEnpriLink e on a.rwbh=e.TaskID  where 1=1 and (a.rwly='" + rwly + "'  or a.rwly='013') and a.SyncDataRegionCode='" + area +"' ";
			}
		}
		ArrayList<HashMap<String, Object>> data = new ArrayList<HashMap<String, Object>>();
		if (sqlstr != null) {
			StringBuffer sql = new StringBuffer(sqlstr);

			if (conditions != null && conditions.size() > 0) {
				Iterator<Entry<String, Object>> iterator = conditions.entrySet().iterator();
				while (iterator.hasNext()) {
					Entry<String, Object> condition = iterator.next();

					if (!condition.getValue().equals("")) {
						sql.append(" and (" + condition.getKey().trim() + "= '" + condition.getValue() + "')");
					}

				}
			}
			sql.append(" order by fbsj desc ");// ������ʱ������
			sql.append(" limit " + getOrder());
			data = SqliteUtil.getInstance().queryBySqlReturnArrayListHashMap(sql.toString());

		}

		return data;

	}

	/**
	 * ��Ϊ�ⲿʹ��EnforcementRecordListAdapter
	 * ��������EnforcementRecordListAdapterΪ��̬������д�˴˷���
	 * 
	 * @param context
	 * @param data
	 * @return
	 */
	public EnforcementRecordListAdapter getRecordAdapter(Context context, List<HashMap<String, Object>> data) {
		enforcementRecordListAdapter = new EnforcementRecordListAdapter(context, data);
		return enforcementRecordListAdapter;
	}

	public class EnforcementRecordListAdapter extends BaseAdapter {
		LayoutInflater layoutInflater;
		List<HashMap<String, Object>> data;
		int textSize = 22;

		public EnforcementRecordListAdapter(Context context, List<HashMap<String, Object>> newData) {
			layoutInflater = LayoutInflater.from(context);
			this.data = newData;
			//����������ֶ����ͬ��Ŀ
			for (int i = 0; i < data.size(); i++) {
				for (int j = 0; j < data.size(); j++) {
					if (i != j) {
						if (data.get(i).get("rwbh")
								.equals(data.get(j).get("rwbh"))) {
							data.remove(j);
							j--;
						}
					}
				}
			}
			
			textSize = Integer.parseInt(DisplayUitl.getSettingValue(context, DisplayUitl.TEXTSIZE, 22).toString());
		}

		@Override
		public int getCount() {
			
			
			//����������ֶ����ͬ��Ŀ
			for (int i = 0; i < data.size(); i++) {
				for (int j = 0; j < data.size(); j++) {
					if (i != j) {
						if (data.get(i).get("rwbh")
								.equals(data.get(j).get("rwbh"))) {
							data.remove(j);
							j--;
						}
					}
				}
			}
			return data.size();
		}

		public void setDataChanged(List<HashMap<String, Object>> newData) {
			
	
			this.data = newData;
			//����������ֶ����ͬ��Ŀ
			for (int i = 0; i < data.size(); i++) {
				for (int j = 0; j < data.size(); j++) {
					if (i != j) {
						if (data.get(i).get("rwbh")
								.equals(data.get(j).get("rwbh"))) {
							data.remove(j);
							j--;
						}
					}
				}
			}
			notifyDataSetChanged();
		}

		@Override
		public Object getItem(int position) {
			//����������ֶ����ͬ��Ŀ
			for (int i = 0; i < data.size(); i++) {
				for (int j = 0; j < data.size(); j++) {
					if (i != j) {
						if (data.get(i).get("rwbh")
								.equals(data.get(j).get("rwbh"))) {
							data.remove(j);
							j--;
						}
					}
				}
			}
			return data.get(position);
		}

		@Override
		public long getItemId(int position) {

			return position;
		}

		public void AddValue(List<HashMap<String, Object>> newdata) {
			if (this.data == null) {
				this.data = newdata;
			} else {
				this.data.addAll(newdata);
			}
			//����������ֶ����ͬ��Ŀ
			for (int i = 0; i < data.size(); i++) {
				for (int j = 0; j < data.size(); j++) {
					if (i != j) {
						if (data.get(i).get("rwbh")
								.equals(data.get(j).get("rwbh"))) {
							data.remove(j);
							j--;
						}
					}
				}
			}
		
			notifyDataSetChanged();
		}

		public void updateValue(List<HashMap<String, Object>> newdata) {
			if (newdata != null) {
				this.data = newdata;
			} else {
				this.data = new ArrayList<HashMap<String, Object>>();
			}
			//����������ֶ����ͬ��Ŀ
			for (int i = 0; i < data.size(); i++) {
				for (int j = 0; j < data.size(); j++) {
					if (i != j) {
						if (data.get(i).get("rwbh")
								.equals(data.get(j).get("rwbh"))) {
							data.remove(j);
							j--;
						}
					}
				}
			}
	
			notifyDataSetChanged();
		}
	public String getRWZT_String(int pos){
		
		String rwzt = data.get(pos).get("rwzt").toString();
		
		//BYK rwzt
		if (rwzt.equals("1")) {
			return "δ�ϴ�";
		} else if (rwzt.equals("3")) {
			return "���ϴ�";
		} else if (rwzt.equals("2")) {
			return "ִ����";
		}
		
		return "";
	}
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder = null;
			int leftImageID = 0;
			if (convertView == null) {
				convertView = layoutInflater.inflate(R.layout.ui_list_item_cell, null);
				holder = new ViewHolder();
				holder.text1 = (TextView) convertView.findViewById(R.id.title);
				holder.text2 = (TextView) convertView.findViewById(R.id.content);
				holder.text3 = (TextView) convertView.findViewById(R.id.date);
				holder.text4 = (TextView) convertView.findViewById(R.id.text_state);
				holder.text4.setVisibility(View.VISIBLE);
				holder.lefticon = (ImageView) convertView.findViewById(R.id.lefticon);

				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			holder.text1.setText(data.get(position).get("rwmc").toString());
			holder.text1.setTag(data.get(position).get("rwbh").toString());
			holder.text1.setTextSize(textSize);
			holder.text2.setText("");
			holder.text2.setTag(data.get(position).get("qyid").toString());
			String fbsj = data.get(position).get("fbsj").toString();
			
			if (fbsj.contains("T")) {
				String[] split = fbsj.split("T");
				String string = split[1].subSequence(0,8).toString();
				holder.text3.setText(split[0]+" "+string);
			}else{
				holder.text3.setText(data.get(position).get("fbsj").toString());
			}

			String rwzt = data.get(position).get("rwzt").toString();
			holder.lefticon.setTag(rwzt);
			//BYK rwzt
			if (rwzt.equals("1")) {
				leftImageID = R.drawable.rwgl_task_list_dzx;
				holder.text4.setText("δ�ϴ�");
			} else if (rwzt.equals("3")) {
				leftImageID = R.drawable.rwgl_task_list_ysc;
				holder.text4.setText("���ϴ�");
			} else if (rwzt.equals("2")) {
				leftImageID = R.drawable.rwgl_task_list_zxz;
				holder.text4.setText("ִ����");
			}
			holder.lefticon.setImageResource(leftImageID);
			//BYK
			//notifyDataSetChanged();
			return convertView;
		}
	}

	static class ViewHolder {
		TextView text1;
		ImageView lefticon;
		TextView text2;
		TextView text3;
		TextView text4;
	}

	/**
	 * չʾִ����¼��ѯdialog ����Ч������ʹ�ñ�ģ�͵�adapter
	 * 
	 * @param context
	 * @param layoutInflater
	 * @param aecordCondition
	 *            ��ѯ���� ����Ϊnull
	 */
	public void showQueryDialog(final Context context, LayoutInflater layoutInflater, final HashMap<String, Object> aecordCondition) {
		View dialogView = layoutInflater.inflate(R.layout.enforcement_enforcementmodel_query, null);
		AlertDialog.Builder ab = new AlertDialog.Builder(context);
		ab.setTitle("ִ����¼��ѯ");
		ab.setIcon(context.getResources().getDrawable(R.drawable.yutu));
		ab.setView(dialogView);
		final EditText companyname = (EditText) dialogView.findViewById(R.id.enforcement_enforcementmodel_query_companyname);
		final EditText taskname = (EditText) dialogView.findViewById(R.id.enforcement_enforcementmodel_query_taskname);
		// companyname.setText(temporaryCompany);
		// taskname.setText(temporaryTask);

		ab.setPositiveButton("��ѯ", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				ListScrollTimes = 1;// ����limit ��ʼλ��--������limit�Ѿ��仯
				String company = companyname.getText().toString();
				String task = taskname.getText().toString();
				// temporaryCompany = company;
				// temporaryTask = task;
				aecordCondition.put("'1'", "2' or rwmc like '%" + task + "%");
				aecordCondition.put("'2'", "1' or qymc like '%" + company + "%");// ƴ��like��ѯ���
				ArrayList<HashMap<String, Object>> newData = getUserEnforcementRecord(Global.getGlobalInstance().getUserid(), aecordCondition);
				enforcementRecordListAdapter.updateValue(newData);
				// �ر����뷨
				InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);

				imm.hideSoftInputFromWindow(taskname.getApplicationWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
			}

		});
		ab.setNegativeButton("ȡ��", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// �ر����뷨
				InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);

				imm.hideSoftInputFromWindow(taskname.getApplicationWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

			}
		});
		AlertDialog ad = ab.create();
		((Dialog) ad).setCanceledOnTouchOutside(true);
		ad.show();
	}

	/**
	 * չʾִ����¼��ѯdialog
	 * 
	 * @param context
	 * @param aecordCondition
	 */
	public void showQueryDialog(Context context, HashMap<String, Object> aecordCondition) {
		LayoutInflater layoutInflater = LayoutInflater.from(context);
		showQueryDialog(context, layoutInflater, aecordCondition);
	}

	/**
	 * չʾ��ִͬ����
	 * 
	 * @param context
	 * @param layoutInflater
	 * @param sb
	 *            ִ����id�ַ���
	 */
	public Builder showCommonLawPeople(final Context context, LayoutInflater layoutInflater, final StringBuffer sb) {
		View dialogView = layoutInflater.inflate(R.layout.enforcementmodel_select_commonpeople, null);

		AlertDialog.Builder ab = new AlertDialog.Builder(context);
		ab.setTitle("��ѡ��ִͬ����");
		ab.setIcon(context.getResources().getDrawable(R.drawable.yutu));
		ab.setView(dialogView);

		final ExpandableListView expandableListView = (ExpandableListView) dialogView.findViewById(R.id.enforcementmodel_select_commonpeople_explistview);
		expandableListView.setGroupIndicator(context.getResources().getDrawable(R.layout.expandablelistviewselector));

		TextView tv = (TextView) dialogView.findViewById(R.id.enforcementmodel_select_commonpeople_tv);
		/** �����û���¼�û���id��ѯ */
		ArrayList<HashMap<String, Object>> login_user_data = new ArrayList<HashMap<String, Object>>();
		login_user_data = SqliteUtil.getInstance().queryBySqlReturnArrayListHashMap(
				"select * from PC_DepartmentInfo where depid =(select depparentid from PC_Users LEFT JOIN PC_DepartmentInfo on PC_Users.depId = PC_DepartmentInfo.depId where userid = '"
						+ Global.getGlobalInstance().getUserid() + "')");
		/** ��õ�¼�û������ŵ����� */
		String depParentName = login_user_data.get(0).get("depname").toString();
		/** ��õ�¼�û������ŵ�id */
		String depID = login_user_data.get(0).get("depid").toString();
		tv.setText(depParentName);
		ArrayList<HashMap<String, Object>> depData = new ArrayList<HashMap<String, Object>>();
		HashMap<String, Object> conditions = new HashMap<String, Object>();
		conditions.put("DepParentID", depID);
		SqliteUtil.getInstance().recursiveQuery("PC_DepartmentInfo", depData, conditions, "depid,depname", false);
		final List<String> groupList = new ArrayList<String>();
		final ArrayList<ArrayList<HashMap<String, Object>>> childMapData = new ArrayList<ArrayList<HashMap<String, Object>>>();
		for (HashMap<String, Object> map : depData) {
			groupList.add(map.get("depname").toString());
			HashMap<String, Object> condition = new HashMap<String, Object>();
			condition.put("DepID", map.get("depid").toString());
			ArrayList<HashMap<String, Object>> usersData = SqliteUtil.getInstance().getList("UserID,U_RealName", condition, "PC_Users");
			childMapData.add(usersData);
		}
		expandableListView.setAdapter(new CommonLawPeopleAdapter(groupList, childMapData, sb, context));

		expandableListView.setOnChildClickListener(new OnChildClickListener() {

			@Override
			public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
				CheckBox two_class_cb = (CheckBox) v.findViewById(R.id.two_class_cb);

				String userCheckedId = childMapData.get(groupPosition).get(childPosition).get("userid").toString();
				if (userCheckedId.equals(Global.getGlobalInstance().getUserid())) {// ���ѡ���˸�����ʾ
					Toast.makeText(context, "����ȡ��������", Toast.LENGTH_SHORT).show();
					return false;
				}
				two_class_cb.toggle();
				if (sb.toString().contains(userCheckedId)) {
					sb.delete(sb.indexOf(userCheckedId) - 1, sb.lastIndexOf(userCheckedId) + userCheckedId.length());
				} else {
					sb.append("," + userCheckedId);
				}

				return false;
			}
		});
		/*
		 * ab.setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {
		 * 
		 * @Override public void onClick(DialogInterface dialog, int which) {
		 * 
		 * }
		 * 
		 * }); ab.setNegativeButton("ȡ��", new DialogInterface.OnClickListener()
		 * {
		 * 
		 * @Override public void onClick(DialogInterface dialog, int which) {
		 * if(checkBox!=null){ checkBox.setChecked(false); } } });
		 */

		/*
		 * AlertDialog ad = ab.create(); ((Dialog)
		 * ad).setCanceledOnTouchOutside(true); ad.show();
		 */
		return ab;

	}

	/**
	 * ��ִͬ����Ա������
	 */
	public class CommonLawPeopleAdapter extends BaseExpandableListAdapter {
		/** ��ȡ��һ�鹲ִͬ���˶����б��и����б���������ݼ��� */
		private List<String> groupList;
		/** ��ȡ��һ�鹲ִͬ���˶����б��в�ѯ�Ӽ��б����ݵļ��� */
		private ArrayList<ArrayList<HashMap<String, Object>>> childMapData;
		private final StringBuffer usersb;
		LayoutInflater layoutInflater;

		CommonLawPeopleAdapter(List<String> groupList, ArrayList<ArrayList<HashMap<String, Object>>> childMapData, StringBuffer usersb, Context context) {
			layoutInflater = LayoutInflater.from(context);
			if (groupList != null) {
				this.groupList = groupList;
			} else {
				this.groupList = new ArrayList<String>();
			}
			if (childMapData != null) {
				this.childMapData = childMapData;
			} else {
				this.childMapData = new ArrayList<ArrayList<HashMap<String, Object>>>();
			}
			this.usersb = usersb;

		}

		@Override
		public Object getChild(int groupPosition, int childPosition) {

			return childMapData.get(groupPosition).get(childPosition);
		}

		@Override
		public long getChildId(int groupPosition, int childPosition) {

			return childPosition;
		}

		@Override
		public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
			if (convertView == null) {
				convertView = layoutInflater.inflate(R.layout.two_class_interface, null);
			}
			TextView userTv = (TextView) convertView.findViewById(R.id.two_class_interface_name_tv);
			CheckBox isSelect = (CheckBox) convertView.findViewById(R.id.two_class_cb);
			String userCheckedId = childMapData.get(groupPosition).get(childPosition).get("userid").toString();
			if (usersb.toString().contains(userCheckedId)) {
				isSelect.setChecked(true);
			} else {
				isSelect.setChecked(false);
			}
			userTv.setText(childMapData.get(groupPosition).get(childPosition).get("u_realname").toString());
			return convertView;
		}

		@Override
		public int getChildrenCount(int groupPosition) {

			return childMapData.get(groupPosition).size();
		}

		@Override
		public Object getGroup(int groupPosition) {

			return groupList.get(groupPosition);
		}

		@Override
		public int getGroupCount() {

			return groupList.size();
		}

		@Override
		public long getGroupId(int groupPosition) {

			return groupPosition;
		}

		@Override
		public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {

			if (convertView == null) {
				convertView = layoutInflater.inflate(R.layout.one_class_interface, null);
			}
			TextView one_class_interface_title_tv = (TextView) convertView.findViewById(R.id.one_class_interface_title_tv);

			one_class_interface_title_tv.setText(groupList.get(groupPosition).toString());

			return convertView;
		}

		@Override
		public boolean hasStableIds() {

			return false;
		}

		@Override
		public boolean isChildSelectable(int groupPosition, int childPosition) {

			return true;
		}

	}

	/**
	 * ����һ���ֳ�ִ������
	 * 
	 * @param qyguid
	 *            ��ҵid
	 * @param rwmc
	 *            ��������
	 * @param executorID
	 *            ִ����id�ַ��� �ö��ŷָ�
	 * @return
	 */
	public String createOneEnforcementTask(String qyguid, String rwmc, String executorID) {

		String rwbh = returnRWBH();
		String rwguid = UUID.randomUUID().toString();

		String updateTime = Global.getGlobalInstance().getDate();
		String fbsj = updateTime;
		String rwzt = RWXX.RWZT_WATE_EXECUTION;

		ContentValues cv = new ContentValues();
		cv.put("rwbh", rwbh);
		cv.put("guid", rwguid);
		cv.put("rwly", rwly);
		cv.put("rwlx", rwlx);
		cv.put("updatetime", updateTime);
		cv.put("fbsj", fbsj);
		cv.put("rwzt", rwzt);
		cv.put("rwmc", rwmc);
		cv.put("fbr", Global.getGlobalInstance().getUserid());
		cv.put("ssks", Global.getGlobalInstance().getDepId());
		cv.put("syncdataregioncode", Global.getGlobalInstance().getAreaCode());
		long bjqx = System.currentTimeMillis() + (1000 * 60 * 60 * 24 * 3);// Ĭ�ϵ�ǰ�����Ӻ�����Ϊ�������
		cv.put("bjqx", Global.getGlobalInstance().getDate(new Date(bjqx), "yyyy-MM-dd"));
		cv.put("bz", "�ֳ�ִ��");

		long result = SqliteUtil.getInstance().insert(cv, "T_YDZF_RWXX");
		if (result > 0) {// ��������Ϣuser���������
			ArrayList<ContentValues> contentValues = new ArrayList<ContentValues>();
			String[] executorIDArr = executorID.split(",");
			for (int i = 0; i < executorIDArr.length; i++) {
				ContentValues cvRwUsers = new ContentValues();
				cvRwUsers.put("RWXXBH", rwbh);
				cvRwUsers.put("UserID", executorIDArr[i]);
				cvRwUsers.put("UpdateTime", updateTime);

				contentValues.add(cvRwUsers);
			}
			SqliteUtil.getInstance().insert(contentValues, "T_YDZF_RWXX_USER");

			ArrayList<ContentValues> taskEnpriLinkValues = new ArrayList<ContentValues>();// ���������ҵ�������������
			String[] qyidArr = qyguid.split(",");
			for (int i = 0; i < qyidArr.length; i++) {
				String taskEnpriLinkGUID = UUID.randomUUID().toString();
				ContentValues qyRwUsers = new ContentValues();
				qyRwUsers.put("TaskID", rwbh);
				//BYK rwzt
				//qyRwUsers.put("IsExcute", "0");// ��ִ��
				qyRwUsers.put("IsExcute", "1");// ��ִ��
				qyRwUsers.put("UpdateTime", updateTime);
				qyRwUsers.put("guid", taskEnpriLinkGUID);
				qyRwUsers.put("qyid", qyidArr[i]);
				taskEnpriLinkValues.add(qyRwUsers);
			}
			SqliteUtil.getInstance().insert(taskEnpriLinkValues, "TaskEnpriLink");

		} else {
			rwbh = "";
		}
		return rwbh;

	}

	/**
	 * Description: ����������
	 * 
	 * @return ���������� String
	 * @author ��˼Զ Create at: 2012-12-4 ����11:58:05
	 */
	private String returnRWBH() {
		SimpleDateFormat formatdate = new SimpleDateFormat("yyyyMMddHHmmssSSSS");
		/** ���ɹ��� */
		String time = "T" + formatdate.format(new Date());
		return time;
	}

}
