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
	/** 初始化当前该实体类列表滚动的次数 */
	public int ListScrollTimes = 1;
	/** 现场执法任务来源值 */
	public final String rwly = "010";
	/** 现场执法任务类型值：监时性监察 */
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
	 * Description: 用来实现查询列表的时候进行分页显示
	 * 
	 * @return 返回一个用来拼接分页显示sql语句的字符串 String String
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
	 * 根据userid获取现场执法记录，返回数据并非全部数据，而是分批返回数据
	 * 
	 * @param userID
	 * @param conditions
	 *            条件 如果用到like查询 hashmap key --> '1' value-->
	 *            2+"' or"+字段名+" like '%"+likeName+"%"
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
			sql.append(" order by fbsj desc ");// 按发布时间排序
			sql.append(" limit " + getOrder());
			data = SqliteUtil.getInstance().queryBySqlReturnArrayListHashMap(sql.toString());

		}

		return data;

	}

	/**
	 * 因为外部使用EnforcementRecordListAdapter
	 * 必须设置EnforcementRecordListAdapter为静态，所以写了此方法
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
			//暴力处理出现多个相同条目
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
			
			
			//暴力处理出现多个相同条目
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
			//暴力处理出现多个相同条目
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
			//暴力处理出现多个相同条目
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
			//暴力处理出现多个相同条目
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
			//暴力处理出现多个相同条目
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
			return "未上传";
		} else if (rwzt.equals("3")) {
			return "已上传";
		} else if (rwzt.equals("2")) {
			return "执行中";
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
				holder.text4.setText("未上传");
			} else if (rwzt.equals("3")) {
				leftImageID = R.drawable.rwgl_task_list_ysc;
				holder.text4.setText("已上传");
			} else if (rwzt.equals("2")) {
				leftImageID = R.drawable.rwgl_task_list_zxz;
				holder.text4.setText("执行中");
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
	 * 展示执法记录查询dialog 产生效果必须使用本模型的adapter
	 * 
	 * @param context
	 * @param layoutInflater
	 * @param aecordCondition
	 *            查询条件 不可为null
	 */
	public void showQueryDialog(final Context context, LayoutInflater layoutInflater, final HashMap<String, Object> aecordCondition) {
		View dialogView = layoutInflater.inflate(R.layout.enforcement_enforcementmodel_query, null);
		AlertDialog.Builder ab = new AlertDialog.Builder(context);
		ab.setTitle("执法记录查询");
		ab.setIcon(context.getResources().getDrawable(R.drawable.yutu));
		ab.setView(dialogView);
		final EditText companyname = (EditText) dialogView.findViewById(R.id.enforcement_enforcementmodel_query_companyname);
		final EditText taskname = (EditText) dialogView.findViewById(R.id.enforcement_enforcementmodel_query_taskname);
		// companyname.setText(temporaryCompany);
		// taskname.setText(temporaryTask);

		ab.setPositiveButton("查询", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				ListScrollTimes = 1;// 重置limit 开始位置--》可能limit已经变化
				String company = companyname.getText().toString();
				String task = taskname.getText().toString();
				// temporaryCompany = company;
				// temporaryTask = task;
				aecordCondition.put("'1'", "2' or rwmc like '%" + task + "%");
				aecordCondition.put("'2'", "1' or qymc like '%" + company + "%");// 拼出like查询语句
				ArrayList<HashMap<String, Object>> newData = getUserEnforcementRecord(Global.getGlobalInstance().getUserid(), aecordCondition);
				enforcementRecordListAdapter.updateValue(newData);
				// 关闭输入法
				InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);

				imm.hideSoftInputFromWindow(taskname.getApplicationWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
			}

		});
		ab.setNegativeButton("取消", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// 关闭输入法
				InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);

				imm.hideSoftInputFromWindow(taskname.getApplicationWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

			}
		});
		AlertDialog ad = ab.create();
		((Dialog) ad).setCanceledOnTouchOutside(true);
		ad.show();
	}

	/**
	 * 展示执法记录查询dialog
	 * 
	 * @param context
	 * @param aecordCondition
	 */
	public void showQueryDialog(Context context, HashMap<String, Object> aecordCondition) {
		LayoutInflater layoutInflater = LayoutInflater.from(context);
		showQueryDialog(context, layoutInflater, aecordCondition);
	}

	/**
	 * 展示共同执法人
	 * 
	 * @param context
	 * @param layoutInflater
	 * @param sb
	 *            执法人id字符集
	 */
	public Builder showCommonLawPeople(final Context context, LayoutInflater layoutInflater, final StringBuffer sb) {
		View dialogView = layoutInflater.inflate(R.layout.enforcementmodel_select_commonpeople, null);

		AlertDialog.Builder ab = new AlertDialog.Builder(context);
		ab.setTitle("请选择共同执法人");
		ab.setIcon(context.getResources().getDrawable(R.drawable.yutu));
		ab.setView(dialogView);

		final ExpandableListView expandableListView = (ExpandableListView) dialogView.findViewById(R.id.enforcementmodel_select_commonpeople_explistview);
		expandableListView.setGroupIndicator(context.getResources().getDrawable(R.layout.expandablelistviewselector));

		TextView tv = (TextView) dialogView.findViewById(R.id.enforcementmodel_select_commonpeople_tv);
		/** 根据用户登录用户的id查询 */
		ArrayList<HashMap<String, Object>> login_user_data = new ArrayList<HashMap<String, Object>>();
		login_user_data = SqliteUtil.getInstance().queryBySqlReturnArrayListHashMap(
				"select * from PC_DepartmentInfo where depid =(select depparentid from PC_Users LEFT JOIN PC_DepartmentInfo on PC_Users.depId = PC_DepartmentInfo.depId where userid = '"
						+ Global.getGlobalInstance().getUserid() + "')");
		/** 获得登录用户父部门的名称 */
		String depParentName = login_user_data.get(0).get("depname").toString();
		/** 获得登录用户父部门的id */
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
				if (userCheckedId.equals(Global.getGlobalInstance().getUserid())) {// 如果选择本人给以提示
					Toast.makeText(context, "不能取消掉本人", Toast.LENGTH_SHORT).show();
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
		 * ab.setPositiveButton("确定", new DialogInterface.OnClickListener() {
		 * 
		 * @Override public void onClick(DialogInterface dialog, int which) {
		 * 
		 * }
		 * 
		 * }); ab.setNegativeButton("取消", new DialogInterface.OnClickListener()
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
	 * 共同执法人员适配器
	 */
	public class CommonLawPeopleAdapter extends BaseExpandableListAdapter {
		/** 获取第一组共同执法人二级列表中父级列表适配的数据集合 */
		private List<String> groupList;
		/** 获取第一组共同执法人二级列表中查询子级列表数据的集合 */
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
	 * 生成一条现场执法任务
	 * 
	 * @param qyguid
	 *            企业id
	 * @param rwmc
	 *            任务名称
	 * @param executorID
	 *            执行人id字符串 用逗号分隔
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
		long bjqx = System.currentTimeMillis() + (1000 * 60 * 60 * 24 * 3);// 默认当前日期延后三天为办结期限
		cv.put("bjqx", Global.getGlobalInstance().getDate(new Date(bjqx), "yyyy-MM-dd"));
		cv.put("bz", "现场执法");

		long result = SqliteUtil.getInstance().insert(cv, "T_YDZF_RWXX");
		if (result > 0) {// 向任务信息user表插入数据
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

			ArrayList<ContentValues> taskEnpriLinkValues = new ArrayList<ContentValues>();// 向任务和企业关联表插入数据
			String[] qyidArr = qyguid.split(",");
			for (int i = 0; i < qyidArr.length; i++) {
				String taskEnpriLinkGUID = UUID.randomUUID().toString();
				ContentValues qyRwUsers = new ContentValues();
				qyRwUsers.put("TaskID", rwbh);
				//BYK rwzt
				//qyRwUsers.put("IsExcute", "0");// 待执行
				qyRwUsers.put("IsExcute", "1");// 待执行
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
	 * Description: 生成任务编号
	 * 
	 * @return 返回任务编号 String
	 * @author 柳思远 Create at: 2012-12-4 上午11:58:05
	 */
	private String returnRWBH() {
		SimpleDateFormat formatdate = new SimpleDateFormat("yyyyMMddHHmmssSSSS");
		/** 生成规则 */
		String time = "T" + formatdate.format(new Date());
		return time;
	}

}
