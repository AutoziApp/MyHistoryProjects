package com.mapuni.android.taskmanager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.TableRow;
import android.widget.TextView;

import com.mapuni.android.MobileEnforcement.R;
import com.mapuni.android.base.controls.loading.YutuLoading;
import com.mapuni.android.base.util.DisplayUitl;
import com.mapuni.android.business.RWXX;
import com.mapuni.android.dataprovider.SqliteUtil;

public class TaskManagerModel {

	private ProcedureAdapter procedureAdapter;

	/**
	 * ���ʹ��ProcedureAdapter
	 * @param context
	 * @param data
	 * @return
	 */
	public ProcedureAdapter getProcedureAdapter(ArrayList<HashMap<String, Object>> list, Context context, String TaskFlowDirection) {
		procedureAdapter = new ProcedureAdapter(list, context);
		return procedureAdapter;
	}

	public class ProcedureAdapter extends BaseAdapter {
		ArrayList<HashMap<String, Object>> list;
		LayoutInflater inflater;
		TextView textView1 = null;
		TextView textView2 = null;
		TextView textView3 = null;

		public ProcedureAdapter(ArrayList<HashMap<String, Object>> list,
				Context context) {
			this.list = list;
			inflater = LayoutInflater.from(context);
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return list.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return list.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			if (convertView == null) {
				convertView = inflater.inflate(R.layout.procedure_item, null);
			}
			textView1 = (TextView) convertView.findViewById(R.id.textView1);
			textView2 = (TextView) convertView.findViewById(R.id.textView2);
			textView3 = (TextView) convertView.findViewById(R.id.textView3);

			String noteId = list.get(position).get("NodeId").toString();
			String auditName = list.get(position).get("AuditName").toString();
			String auditTime = list.get(position).get("AuditTime").toString();
			String auditResult = list.get(position).get("AuditResult")
					.toString();
			String flowDirection = list.get(position).get("FlowDirection")
					.toString();
			String taskAction = list.get(position).get("TaskAction").toString();

			if ("���񴴽�".equals(taskAction)) {
				textView1.setText("���񴴽���:" + auditName);
				textView2.setText("���񴴽�ʱ��:" + auditTime);
				textView3.setText("������Ϣ:" + auditResult);
			} else if ("��������".equals(taskAction)) {
				textView1.setText(noteId/*transitToChinese(Integer.parseInt(noteId))*/
						+ ":" + auditName);
				textView2.setText("����ʱ��:" + auditTime);
				textView3.setText("�������:" + auditResult);
			} else if ("����ִ��".equals(taskAction) && noteId.equals("ִ����Ա")) {
				textView1.setText("ִ����:" + auditName);
				textView2.setText("ִ��ʱ��:" + auditTime);
				textView3.setText("ִ�����:" + auditResult);
			} else if ("�������".equals(taskAction)) {
				textView1.setText(noteId/*transitToChinese(Integer.parseInt(noteId))*/
						+ ":" + auditName);
				textView2.setText("���ʱ��:" + auditTime);
				textView3.setText("������:" + auditResult);
			} else if ("������".equals(taskAction)) {
				textView1.setText(noteId/*transitToChinese(Integer.parseInt(noteId))*/
						+ ":" + auditName);
				textView2.setText("����ʱ��:" + auditTime);
				textView3.setText("�������:" + auditResult);
			}else if ("����ִ��".equals(taskAction)) {
				textView1.setText(noteId/*transitToChinese(Integer.parseInt(noteId))*/
						+ ":" + auditName);
				textView2.setText("ִ��ʱ��:" + auditTime);
				textView3.setText("ִ�����:" + auditResult);
			}  
			
			else {
				textView1.setText(noteId/*transitToChinese(Integer.parseInt(noteId))*/
						+ ":" + auditName);
				textView2.setText("���ʱ��:" + auditTime);
				textView3.setText("������:" + auditResult);
			}

			if (position > 0) {
//				String SecondaryAuditUserId = list.get(position).get("Assistant")+""/*list.get(position - 1)
//						.get("SecondaryAuditUserId").toString()*/;
				String SecondaryAuditUserName = list.get(position).get("Assistant")+""/*getNameByid(SecondaryAuditUserId)*/;
				if (!SecondaryAuditUserName.equals("")) {
					
				    if ("��������".equals(taskAction)) {
				    	String string = textView1.getText().toString() + "(������:"
								+ SecondaryAuditUserName + ")";
						textView1.setText(string);
					}else{
						String string = textView1.getText().toString() + "(Э����:"
								+ SecondaryAuditUserName + ")";
						textView1.setText(string);
					}
				
				}
			}

			if (auditResult == null || auditResult.equals("")) {
				textView3.setVisibility(View.GONE);
			} else {
				textView3.setVisibility(View.VISIBLE);
			}
			
			if(noteId.equals("0") && flowDirection.equals("1") && position != 0) {
				textView1.setText("���񴴽���:" + auditName);
				textView2.setText("�����޸�ʱ��:" + auditTime);
				textView3.setText("������Ϣ:" + auditResult);
			}

			return convertView;
		}
	}

	/** �����û�ID�õ��û����� **/
	private String getNameByid(String idStr) {
		if (!idStr.equals("")) {
			String[] ids = idStr.toString().split(",");
			StringBuffer idsStr = new StringBuffer();
			StringBuffer depnames = new StringBuffer();
			for (String nid : ids) {
				idsStr.append("'");
				idsStr.append(nid);
				idsStr.append("'");
				idsStr.append(",");
			}
			String nid = idsStr.toString().substring(0, idsStr.toString().lastIndexOf(","));
			;
			final ArrayList<HashMap<String, Object>> data = SqliteUtil.getInstance().queryBySqlReturnArrayListHashMap(
					"select U_RealName from  PC_Users where UserID in(" + nid + ")");
			if (data != null && data.size() > 0) {
				for (int i = 0; i < data.size(); i++) {
					depnames.append(data.get(i).get("u_realname"));
					depnames.append(",");
				}
			}
			String SName = depnames.toString().substring(0, depnames.toString().lastIndexOf(","));
			return SName;
		} else {
			return "";
		}
	}

	public String transitToChinese(int nodeid) {
		String position = "";
		switch (nodeid) {
		case 0:
			position = "�칫��";
			break;
		case 1:
			position = "�ӳ�";
			break;
		case 2:
			position = "֧�ӳ�";
			break;
		case 3:
			position = "ֱ�Ӹ�����";
			break;
		case 4:
			position = "ִ����";
			break;
		default:
			position = "ִ����";
			break;
		}
		return position;
	}

	/**
	 * ������Ϣ
	 */
	public AttachmentAdapter getattachmentAdapter(ArrayList<HashMap<String, Object>> AttachmentData, Context context) {
		AttachmentAdapter attachmentAdapter = new AttachmentAdapter(AttachmentData, context);
		return attachmentAdapter;
	}

	public class AttachmentAdapter extends BaseAdapter {
		ArrayList<HashMap<String, Object>> AttachmentData;
		Context context;

		public AttachmentAdapter(ArrayList<HashMap<String, Object>> AttachmentData, Context context) {
			this.AttachmentData = AttachmentData;
			this.context = context;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return AttachmentData.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return AttachmentData.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if (convertView == null) {
				convertView = View.inflate(context, R.layout.qyname_list, null);
			}
			String fname = AttachmentData.get(position).get("FileName").toString();
			String fext = AttachmentData.get(position).get("Extension").toString();
			String file_form = AttachmentData.get(position).get("FK_Unit").toString();
			TextView filename_text = (TextView) convertView.findViewById(R.id.qymc_text);
			ImageView iconImg = (ImageView) convertView.findViewById(R.id.img);
			// �жϸ�ͼƬ������ʱ�ϴ��ĸ��������������ϴ��ĸ�����1�����·���2����ִ�У�5������
			if (file_form.equals("1") || file_form.equals("2")) {
				iconImg.setImageResource(R.drawable.icon_down);
			} else if (file_form.equals("5")) {
				iconImg.setImageResource(R.drawable.icon_up);
			}else{
				//BYK �޸ĸ�������ʾͼ��
				iconImg.setImageResource(R.drawable.icon_down);
			}

			filename_text.setText(fname + fext);
			filename_text.setTag(AttachmentData.get(position).get("Guid").toString());
			Button zt_btn = (Button) convertView.findViewById(R.id.rwzt);
			zt_btn.setVisibility(View.GONE);
			return convertView;
		}
	}

	/**
	 * ѡ�������������
	 * 
	 * @author wangliugeng
	 * 
	 */
	public SelectAuditorAdapter getselectAuditorAdapter(List<String> groupList, ArrayList<ArrayList<HashMap<String, Object>>> childMapData, LinkedList<String> usersb,
			LinkedList<String> linkedName, Context context) {
		SelectAuditorAdapter selectAuditorAdapter = new SelectAuditorAdapter(groupList, childMapData, usersb, linkedName, context);
		return selectAuditorAdapter;
	}

	public class SelectAuditorAdapter extends BaseExpandableListAdapter {
		/** ��ȡ��һ�鹲ִͬ���˶����б��и����б���������ݼ��� */
		private List<String> groupList;
		/** ��ȡ��һ�鹲ִͬ���˶����б��в�ѯ�Ӽ��б����ݵļ��� */
		private ArrayList<ArrayList<HashMap<String, Object>>> childMapData;
		private final LinkedList<String> usersb;
		private final LinkedList<String> linkedName;
		LayoutInflater layoutInflater;
		private HashMap<Integer, Boolean> ib = new HashMap<Integer, Boolean>();

		private HashMap<Integer, View> itemViews = new HashMap<Integer, View>();

		private HashMap<Integer, Integer> observerHashMap = new HashMap<Integer, Integer>();

		private HashMap<Integer, CheckBox> groupCheckBoxs = new HashMap<Integer, CheckBox>();

		public SelectAuditorAdapter(List<String> groupList, ArrayList<ArrayList<HashMap<String, Object>>> childMapData, LinkedList<String> usersb, LinkedList<String> linkedName,
				Context context) {
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
			this.linkedName = linkedName;

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
		public View getChildView(final int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {

			convertView = layoutInflater.inflate(R.layout.two_class_interface, null);

			TextView userTv = (TextView) convertView.findViewById(R.id.two_class_interface_name_tv);
			CheckBox isSelect = (CheckBox) convertView.findViewById(R.id.two_class_cb);

			final String realName = childMapData.get(groupPosition).get(childPosition).get("u_realname").toString();
			final String userCheckedId = childMapData.get(groupPosition).get(childPosition).get("userid").toString();

			itemViews.put((groupPosition + 1) * 10 + childPosition, convertView);

			if (usersb.contains(userCheckedId)) {
				isSelect.setChecked(true);
			} else {
				isSelect.setChecked(false);
			}
			isSelect.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

				@Override
				public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
					Integer values = observerHashMap.get(groupPosition);
					if (values == null) {
						values = 0;
					}
					if (isChecked) {
						if (!usersb.contains(userCheckedId)) {
							usersb.add(userCheckedId);
						}
						if (!linkedName.contains(realName)) {
							linkedName.add(realName);
						}
						observerHashMap.put(groupPosition, ++values);
					} else {
						if (usersb.contains(userCheckedId)) {
							usersb.remove(userCheckedId);
						}
						if (linkedName.contains(realName)) {
							linkedName.remove(realName);
						}
						observerHashMap.put(groupPosition, --values);
					}
					if (values == childMapData.get(groupPosition).size()) {
						groupCheckBoxs.get(groupPosition).setChecked(true);
					} else if (values == 0) {
						groupCheckBoxs.get(groupPosition).setChecked(false);
					}
				}
			});
			userTv.setText(realName);

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
		public View getGroupView(final int groupPosition, boolean isExpanded, final View view, ViewGroup parent) {

			final View convertView = layoutInflater.inflate(R.layout.one_class_interface, null);

			TextView one_class_interface_title_tv = (TextView) convertView.findViewById(R.id.one_class_interface_title_tv);
			CheckBox itemAllSelect = (CheckBox) convertView.findViewById(R.id.itemAllSelect);
			groupCheckBoxs.put(groupPosition, itemAllSelect);

			itemAllSelect.setChecked(ib.get(groupPosition) != null ? ib.get(groupPosition) : false);

			itemAllSelect.setOnCheckedChangeListener(new OnCheckedChangeListener() {

				@Override
				public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
					Set<Map.Entry<Integer, View>> set = itemViews.entrySet();
					for (Map.Entry<Integer, View> entry : set) {
						Integer keyInteger = entry.getKey();

						Integer temp = (groupPosition + 1) * 10;
						if ((keyInteger - temp) < 10 && (keyInteger - temp) >= 0) {
							View view = entry.getValue();
							((CheckBox) view.findViewById(R.id.two_class_cb)).setChecked(isChecked);
						}
					}

					ib.put(groupPosition, isChecked);

					ArrayList<HashMap<String, Object>> list = childMapData.get(groupPosition);
					for (HashMap<String, Object> hashMap : list) {
						String userCheckedId = hashMap.get("userid").toString();
						String realName = hashMap.get("u_realname").toString();

						if (isChecked) {
							if (!usersb.contains(userCheckedId)) {
								usersb.add(userCheckedId);
							}
							if (!linkedName.contains(realName)) {
								linkedName.add(realName);
							}

						} else {
							if (usersb.contains(userCheckedId)) {
								usersb.remove(userCheckedId);
							}
							if (linkedName.contains(realName)) {
								linkedName.remove(realName);
							}
						}
					}
					SelectAuditorAdapter.this.notifyDataSetChanged();
				}
			});

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
	//�·������������
	public SelectAuditorAdapter2 getselectAuditorAdapter2(List<String> groupList, ArrayList<ArrayList<HashMap<String, Object>>> childMapData, LinkedList<String> usersb,
			LinkedList<String> linkedName, Context context) {
		SelectAuditorAdapter2 selectAuditorAdapter2 = new SelectAuditorAdapter2(groupList, childMapData, usersb, linkedName, context);
		return selectAuditorAdapter2;
	}
	
	public class SelectAuditorAdapter2 extends BaseExpandableListAdapter {
		/** ��ȡ��һ�鹲ִͬ���˶����б��и����б���������ݼ��� */
		private List<String> groupList;
		/** ��ȡ��һ�鹲ִͬ���˶����б��в�ѯ�Ӽ��б����ݵļ��� */
		private ArrayList<ArrayList<HashMap<String, Object>>> childMapData;
		private final LinkedList<String> usersb;
		private final LinkedList<String> linkedName;
		LayoutInflater layoutInflater;
		private HashMap<Integer, Boolean> ib = new HashMap<Integer, Boolean>();

		private HashMap<Integer, View> itemViews = new HashMap<Integer, View>();

		private HashMap<Integer, Integer> observerHashMap = new HashMap<Integer, Integer>();

		private HashMap<Integer, CheckBox> groupCheckBoxs = new HashMap<Integer, CheckBox>();

		public SelectAuditorAdapter2(List<String> groupList, ArrayList<ArrayList<HashMap<String, Object>>> childMapData, LinkedList<String> usersb, LinkedList<String> linkedName,
				Context context) {
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
			this.linkedName = linkedName;

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
		public View getChildView(final int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {

			convertView = layoutInflater.inflate(R.layout.two_class_interface, null);

			TextView userTv = (TextView) convertView.findViewById(R.id.two_class_interface_name_tv);
			CheckBox isSelect = (CheckBox) convertView.findViewById(R.id.two_class_cb);

			final String realName = childMapData.get(groupPosition).get(childPosition).get("regionname").toString();
			final String userCheckedId = childMapData.get(groupPosition).get(childPosition).get("regioncode").toString();

			itemViews.put((groupPosition + 1) * 10 + childPosition, convertView);

			if (usersb.contains(userCheckedId)) {
				isSelect.setChecked(true);
			} else {
				isSelect.setChecked(false);
			}
			isSelect.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

				@Override
				public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
					Integer values = observerHashMap.get(groupPosition);
					if (values == null) {
						values = 0;
					}
					if (isChecked) {
						if (!usersb.contains(userCheckedId)) {
							usersb.add(userCheckedId);
						}
						if (!linkedName.contains(realName)) {
							linkedName.add(realName);
						}
						observerHashMap.put(groupPosition, ++values);
					} else {
						if (usersb.contains(userCheckedId)) {
							usersb.remove(userCheckedId);
						}
						if (linkedName.contains(realName)) {
							linkedName.remove(realName);
						}
						observerHashMap.put(groupPosition, --values);
					}
					if (values == childMapData.get(groupPosition).size()) {
						groupCheckBoxs.get(groupPosition).setChecked(true);
					} else if (values == 0) {
						groupCheckBoxs.get(groupPosition).setChecked(false);
					}
				}
			});
			userTv.setText(realName);

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
		public View getGroupView(final int groupPosition, boolean isExpanded, final View view, ViewGroup parent) {

			final View convertView = layoutInflater.inflate(R.layout.one_class_interface, null);

			TextView one_class_interface_title_tv = (TextView) convertView.findViewById(R.id.one_class_interface_title_tv);
			CheckBox itemAllSelect = (CheckBox) convertView.findViewById(R.id.itemAllSelect);
			groupCheckBoxs.put(groupPosition, itemAllSelect);

			itemAllSelect.setChecked(ib.get(groupPosition) != null ? ib.get(groupPosition) : false);

			itemAllSelect.setOnCheckedChangeListener(new OnCheckedChangeListener() {

				@Override
				public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
					Set<Map.Entry<Integer, View>> set = itemViews.entrySet();
					for (Map.Entry<Integer, View> entry : set) {
						Integer keyInteger = entry.getKey();

						Integer temp = (groupPosition + 1) * 10;
						if ((keyInteger - temp) < 10 && (keyInteger - temp) >= 0) {
							View view = entry.getValue();
							((CheckBox) view.findViewById(R.id.two_class_cb)).setChecked(isChecked);
						}
					}

					ib.put(groupPosition, isChecked);

					ArrayList<HashMap<String, Object>> list = childMapData.get(groupPosition);
					for (HashMap<String, Object> hashMap : list) {
						String userCheckedId = hashMap.get("regioncode").toString();
						String realName = hashMap.get("regionname").toString();

						if (isChecked) {
							if (!usersb.contains(userCheckedId)) {
								usersb.add(userCheckedId);
							}
							if (!linkedName.contains(realName)) {
								linkedName.add(realName);
							}

						} else {
							if (usersb.contains(userCheckedId)) {
								usersb.remove(userCheckedId);
							}
							if (linkedName.contains(realName)) {
								linkedName.remove(realName);
							}
						}
					}
					SelectAuditorAdapter2.this.notifyDataSetChanged();
				}
			});

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
	 * ѡ�������������
	 * 
	 * @author wangliugeng
	 * 
	 */
	public SelectAuditorAdapter1 getselectAuditorAdapter1(List<String> groupList, ArrayList<ArrayList<HashMap<String, Object>>> childMapData, LinkedList<String> usersb,
			LinkedList<String> linkedName, Context context) {
		SelectAuditorAdapter1 selectAuditorAdapter1 = new SelectAuditorAdapter1(groupList, childMapData, usersb, linkedName, context);
		return selectAuditorAdapter1;
	}

	public class SelectAuditorAdapter1 extends BaseExpandableListAdapter {
		/** ��ȡ��һ�鹲ִͬ���˶����б��и����б���������ݼ��� */
		private List<String> groupList;
		/** ��ȡ��һ�鹲ִͬ���˶����б��в�ѯ�Ӽ��б����ݵļ��� */
		private ArrayList<ArrayList<HashMap<String, Object>>> childMapData;
		private final LinkedList<String> usersb;
		private final LinkedList<String> linkedName;
		LayoutInflater layoutInflater;

		public SelectAuditorAdapter1(List<String> groupList, ArrayList<ArrayList<HashMap<String, Object>>> childMapData, LinkedList<String> usersb, LinkedList<String> linkedName,
				Context context) {
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
			this.linkedName = linkedName;

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

			convertView = layoutInflater.inflate(R.layout.two_class_interface, null);

			TextView userTv = (TextView) convertView.findViewById(R.id.two_class_interface_name_tv);

			CheckBox isSelect = (CheckBox) convertView.findViewById(R.id.two_class_cb);
			isSelect.setVisibility(View.GONE);
			final String realName = childMapData.get(groupPosition).get(childPosition).get("u_realname").toString();
			final String userCheckedId = childMapData.get(groupPosition).get(childPosition).get("userid").toString();
			if (usersb.contains(userCheckedId)) {
				isSelect.setChecked(true);
			} else {
				isSelect.setChecked(false);
			}
			isSelect.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

				@Override
				public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
					if (isChecked) {
						if (!usersb.contains(userCheckedId)) {
							usersb.add(userCheckedId);
						}
						if (!linkedName.contains(realName)) {
							linkedName.add(realName);
						}

					} else {

						if (usersb.contains(userCheckedId)) {
							usersb.remove(userCheckedId);
						}
						if (linkedName.contains(realName)) {
							linkedName.remove(realName);
						}

					}
				}
			});
			userTv.setText(realName);
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
	 * �����б�������
	 * 
	 * @author wanglg
	 * 
	 */
	public AttachAdapter getattachAdapter(ArrayList<HashMap<String, Object>> attachAdapterData, Context context) {
		AttachAdapter attachAdapter = new AttachAdapter(attachAdapterData, context);
		return attachAdapter;
	}

	public class AttachAdapter extends BaseAdapter {
		ArrayList<HashMap<String, Object>> attachAdapterData;
		Context context;

		public AttachAdapter(ArrayList<HashMap<String, Object>> attachAdapterData, Context context) {
			this.attachAdapterData = attachAdapterData;
			this.context = context;
		}

		@Override
		public int getCount() {
			int size = attachAdapterData.size();
			/*
			 * if(size==0){ return 1; }
			 */
			return size;
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return attachAdapterData.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		public void addData(ArrayList<HashMap<String, Object>> CompanyAdapterData) {
			this.attachAdapterData.addAll(CompanyAdapterData);
			notifyDataSetChanged();
		}

		public void updateData(ArrayList<HashMap<String, Object>> CompanyAdapterData) {
			this.attachAdapterData = CompanyAdapterData;
			notifyDataSetChanged();
		}

		public ArrayList<HashMap<String, Object>> getData() {
			return attachAdapterData;
		}

		@SuppressLint("ResourceAsColor")
		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			if (convertView == null) {
				convertView = View.inflate(context, R.layout.listitem, null);

			}
			ImageView rw_icon = (ImageView) convertView.findViewById(R.id.listitem_left_image);
			rw_icon.setImageResource(R.drawable.icon_table);
			TextView rwmc_text = (TextView) convertView.findViewById(R.id.listitem_text);
			rwmc_text.setText(attachAdapterData.get(position).get("filename").toString());
			rwmc_text.setTextSize(20);
			rwmc_text.setTag(attachAdapterData.get(position).get("guid").toString());

			return convertView;
		}
	}

	/**
	 * FileName: TaskManagerFlowActivity.java Description:�������������������
	 * 
	 * @author Administrator
	 * @Version 1.3.4
	 * @Copyright �п���ͼ���¿Ƽ����޹�˾<br>
	 * @Create at: 2012-12-10 ����03:34:21
	 */
	public MyTaskAdapter getmyTaskAdapter(Context context, ArrayList<HashMap<String, Object>> data, String rwzt, RWXX rwxx) {
		MyTaskAdapter myTaskAdapter = new MyTaskAdapter(context, data, rwzt, rwxx);
		return myTaskAdapter;
	}

	public class MyTaskAdapter extends BaseAdapter {
		private final Context context;
		private LayoutInflater mInflater = null;
		private ArrayList<HashMap<String, Object>> data;
		private final int layoutid = R.layout.ui_list_item_cell;
		private final String rwzt;
		private final int textSize;
		private int textColor = Color.BLACK;
		private RWXX rwxx;

		/**
		 * Description:
		 * 
		 * @param _Context
		 * @param
		 * @param ����״̬
		 * @return
		 * @author Administrator
		 * @Create at: 2013-4-9 ����3:27:39
		 */
		public MyTaskAdapter(Context context, ArrayList<HashMap<String, Object>> data, String rwzt, RWXX rwxx) {
			this.mInflater = LayoutInflater.from(context);
			this.context = context;
			this.data = data == null ? new ArrayList<HashMap<String, Object>>() : data;
			this.rwzt = rwzt;
			this.rwxx = rwxx;
			textSize = Integer.parseInt(DisplayUitl.getSettingValue(context, DisplayUitl.TEXTSIZE, 22).toString());
		}

		public void AddValue(ArrayList<HashMap<String, Object>> data) {
			if (this.data == null) {
				this.data = data;
			} else {
				this.data.addAll(data);
			}
		}

		public void shuaxin() {

			this.notifyDataSetChanged();
		}

		@Override
		public int getCount() {
			if (data == null || data.size() == 0) {
				return 1;
			}
			return data.size();
		}

		@Override
		public Object getItem(int position) {
			return data.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			if (data == null || data.size() == 0) {
				YutuLoading loading = new YutuLoading(context);
				loading.setLoadMsg("", "���޴�״̬����");
				loading.setFailed();
				return loading;
			}
			ViewHolder holder = null;

			if (convertView == null) {
				convertView = mInflater.inflate(layoutid, null);
				holder = new ViewHolder();
				holder.lefticon = (ImageView) convertView.findViewById(R.id.lefticon);
				holder.righticon = (ImageView) convertView.findViewById(R.id.rightIcon);
				holder.lefticon.setLayoutParams(new TableRow.LayoutParams(50, 50));
				holder.id = new TextView(context);
				holder.title = (TextView) convertView.findViewById(R.id.title);
				holder.title.setTextSize(textSize);
				holder.title.setTextColor(textColor);
				holder.content = (TextView) convertView.findViewById(R.id.content);
				holder.content.setTextColor(textColor);
				holder.date = (TextView) convertView.findViewById(R.id.date);
				holder.date.setTextColor(textColor);

				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			// 3.Get Data
			int leftImgId = R.drawable.rwgl_icon_task_others;
			String idStr = (String) data.get(position).get("guid");
			String titleStr = (String) data.get(position).get("rwmc");
			String contentStr = "";
			String dateStr = "";
			String RWBH = (String) data.get(position).get("rwbh");

			if (rwzt.equals("��ִ��")) {
				String jjcd = data.get(position).get("jjcd").toString();
				String bjqx = data.get(position).get("bjqx").toString();
				// ���ж��Ƿ���
				if (bjqx != null && rwxx.checkOverDate(bjqx)) {
					// leftImgId = R.drawable.icon_taskwait_overtime;
					leftImgId = R.drawable.rwgl_icon_taskwait_warring;

					// ���ж��Ƿ����
				} else if (jjcd != null && jjcd.equals(RWXX.JJCD_FCJJ)) {
					leftImgId = R.drawable.rwgl_icon_taskwait_jinji;

					// Ȼ����ʾ��ͨ�����ִ��
				} else {
					leftImgId = R.drawable.rwgl_icon_taskwait;

				}
			} else if (rwzt.equals("���ύ")) {
				leftImgId = R.drawable.rwgl_task_list_dxf;
			} else if (rwzt.equals("���鵵")) {
				leftImgId = R.drawable.rwgl_task_list_ysc;
			} else if (rwzt.equals("�����")) {
				leftImgId = R.drawable.rwgl_task_list_dxf;
			} else if (rwzt.equals("ִ����")) {
				leftImgId = R.drawable.rwgl_task_list_zxz;
			} else {
				leftImgId = R.drawable.rwgl_icon_task_others;
			}

			dateStr = (String) data.get(position).get("bjqx");// �������
			contentStr = (String) data.get(position).get("fbrmc");// ������Դ
			holder.id.setText(idStr);
			holder.title.setText(titleStr);
			holder.content.setText(contentStr);
			holder.date.setText(dateStr);
			holder.lefticon.setBackgroundResource(leftImgId);

			return convertView;
		}
	}

	public class ViewHolder {
		/** ������Id */
		public TextView id = null;
		/** ���б�ĵ�һ������ */
		public TextView title = null;
		/** ���б�ڶ������� */
		public TextView content = null;
		/** ���б�ڶ��к�ߵ����� */
		TextView date = null;
		/** �б���ߵ�ͼƬ */
		ImageView lefticon = null;
		/** �б��ұߵ�ͼ�� */
		ImageView righticon = null;
		/** �б������б��е�λ�� */
		public int position = 0;

		TextView textview1;
		TextView textView2;
		TextView textView3;
	}

}
