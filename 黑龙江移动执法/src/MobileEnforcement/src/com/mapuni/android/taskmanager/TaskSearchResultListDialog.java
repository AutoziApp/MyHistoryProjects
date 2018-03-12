package com.mapuni.android.taskmanager;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteException;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.mapuni.android.MobileEnforcement.R;
import com.mapuni.android.base.BaseDialog;
import com.mapuni.android.base.Global;
import com.mapuni.android.base.business.DataSyncModel;
import com.mapuni.android.base.controls.loading.YutuLoading;
import com.mapuni.android.base.util.LogUtil;
import com.mapuni.android.base.widget.PagingListView;
import com.mapuni.android.business.RWXX;
import com.mapuni.android.dataprovider.DESSecurity;
import com.mapuni.android.dataprovider.JsonHelper;
import com.mapuni.android.dataprovider.SqliteUtil;
import com.mapuni.android.netprovider.Net;
import com.mapuni.android.netprovider.WebServiceProvider;

public class TaskSearchResultListDialog extends BaseDialog implements DialogInterface {
	private Context mContext;
	private YutuLoading yutuLoading;
	private PagingListView listview;
	/** ����״̬ */
	String rwValue = "";
	private CompanyAdapter companyAdapter;
	/** �Ӳ�ѯҳ�洫������������ */
	ArrayList<HashMap<String, Object>> searchConditionsList;
	/** ��ҳ�������ݵ�ҳ�� */
	private int pageIndex = 1;
	/** ��ҳ����һҳ������ */
	private final int pageSize = 30;
	/** �û��������� **/
	private final String UserAreaCode = Global.getGlobalInstance().getAreaCode();

	public TaskSearchResultListDialog(Context context) {
		super(context);
		this.mContext = context;
	}

	public TaskSearchResultListDialog(Context context, int theme) {
		super(context, theme);
		this.mContext = context;
	}

	public void show() {
		super.show();
		yutuLoading = new YutuLoading(mContext);
		yutuLoading.setLoadMsg("�����������ݣ����Ժ�", "");
		yutuLoading.setCancelable(true);
		yutuLoading.showDialog();
	}

	public void showWithPara(ArrayList<HashMap<String, Object>> getSearchConditionsList) {
		setContentView(R.layout.ui_mapuni);
		findViewById(R.id.ui_mapuni_divider).setVisibility(View.GONE);
		SetBaseStyle((RelativeLayout) findViewById(R.id.parentLayout), "�����ѯ");
		initView();
		initData(getSearchConditionsList);
	}

	private void initData(ArrayList<HashMap<String, Object>> getSearchConditionsList) {
		searchConditionsList = getSearchConditionsList;
		new Thread(new Runnable() {

			@Override
			public void run() {
				if (!Net.checkURL(Global.getGlobalInstance().getSystemurl())) {
					handler.sendEmptyMessage(1);
					return;
				}
				//ArrayList<HashMap<String, Object>> data = requestSearchTask(searchConditionsList);
				ArrayList<HashMap<String, Object>> data = requestSearchTask2(searchConditionsList);
				if (data.size() == 0) {
					handler.sendEmptyMessage(3);
					return;
				}
				Message ms = new Message();
				ms.what = 2;
				ms.obj = data;
				handler.sendMessage(ms);

			}
		}).start();

	}

	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			yutuLoading.dismissDialog();
			switch (msg.what) {
			case 0:
				ArrayList<HashMap<String, Object>> data = (ArrayList<HashMap<String, Object>>) msg.obj;
				if (data.size() < pageSize) {
					if (pageIndex > 1) {
						listview.setIsCompleted(true);
						Toast.makeText(mContext, "ȫ�����ݼ��������", Toast.LENGTH_SHORT).show();
					}
				}
				companyAdapter.addData(data);
				break;
			case 1:
				replaceView("��������ԭ���޷���ѯ����");
				break;
			case 2:
				ArrayList<HashMap<String, Object>> firstData = (ArrayList<HashMap<String, Object>>) msg.obj;
				companyAdapter = new CompanyAdapter(firstData);
				listview.setAdapter(companyAdapter);
				break;
			case 3:
				replaceView("û�в�ѯ����������������");
				break;
			}

		};
	};

	/**
	 * û������ʱ�滻����
	 */
	public void replaceView(String tip) {
		/** û�����ݣ��滻���� */
		YutuLoading loading = new YutuLoading(mContext);
		loading.setLoadMsg("������", tip, Color.BLACK);
		loading.setFocusable(false);
		loading.setClickable(false);
		loading.setEnabled(false);
		loading.setFailed();
		loading.setLayoutParams(new android.widget.AbsListView.LayoutParams(android.widget.AbsListView.LayoutParams.FILL_PARENT,
				android.widget.AbsListView.LayoutParams.FILL_PARENT));
		listview.setVisibility(View.GONE);

		linearlayout.addView(loading);

	}

	LinearLayout linearlayout;

	private void initView() {
		// �ж�����ǲ�ѯ�����Ľ��ֱ���б���ʾ
		linearlayout = new LinearLayout(mContext);
		linearlayout.setOrientation(1);
		linearlayout.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
		listview = new PagingListView(mContext);
		listview.setCacheColorHint(Color.TRANSPARENT);
		listview.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.FILL_PARENT));

		linearlayout.addView(listview);
		((LinearLayout) findViewById(R.id.middleLayout)).addView(linearlayout);
		listview.setOnPageCountChangListener(new PagingListView.PageCountChangListener() {

			@Override
			public void onAddPage(AbsListView view) {
				new Thread(new Runnable() {

					@Override
					public void run() {
						if (!Net.checkURL(Global.getGlobalInstance().getSystemurl())) {
							handler.sendEmptyMessage(1);
							return;
						}
						ArrayList<HashMap<String, Object>> data = requestSearchTask2(searchConditionsList);
						Message ms = new Message();
						ms.what = 0;
						ms.obj = data;
						handler.sendMessage(ms);
					}
				}).start();

			}
		});
		listview.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				//String guid = companyAdapter.getData().get(arg2).get("guid").toString();
				
				//�޸Ļ�ȡguid  
				String rwbh_byk =  companyAdapter.getData().get(arg2).get("rwbh").toString();
				String guid_byk = getGUID(rwbh_byk);
				ArrayList<HashMap<String, Object>> data = companyAdapter.getData();
				String letaskid = data.get(arg2).get("letaskid").toString();
				RWXX RWXX = new RWXX();
				RWXX.setCurrentID(guid_byk);
				String rwzt = companyAdapter.getData().get(arg2).get("rwzt").toString();
				String rwlx = companyAdapter.getData().get(arg2).get("rwlx").toString();
			
				TaskManagerController contro = TaskManagerController.getInstance(mContext);
				contro.openObjectParaView(contro.TASK_SEARCH_RESULT_DETAIL, RWXX,rwbh_byk,letaskid,rwzt,rwlx);
				
				ArrayList<String> tables = new ArrayList<String>();
				tables.add("TaskEnpriLink");// TaskEnpriLink����״̬���� �ı䣬��Ҫͬ��һ��
				// ͬ��һ�������ű�
			//	DataSyncModel dm2 = new DataSyncModel();

		//		dm2.syncServiceData(tables, true);
				return;
			}
		});

	}

	/**
	 * ��ѯ������������Ϣ�б�
	 * 
	 */
	private class CompanyAdapter extends BaseAdapter {
		ArrayList<HashMap<String, Object>> CompanyAdapterData;

		public CompanyAdapter(ArrayList<HashMap<String, Object>> CompanyAdapterData) {
			this.CompanyAdapterData = CompanyAdapterData;
		}

		@Override
		public int getCount() {
			return CompanyAdapterData.size();
		}

		@Override
		public Object getItem(int position) {
			return CompanyAdapterData.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		public void addData(ArrayList<HashMap<String, Object>> CompanyAdapterData) {
			this.CompanyAdapterData.addAll(CompanyAdapterData);
			notifyDataSetChanged();
		}

		public ArrayList<HashMap<String, Object>> getData() {
			return CompanyAdapterData;
		}

		@SuppressLint("ResourceAsColor")
		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			ViewHolder viewHolder;
			if (convertView == null) {
				convertView = View.inflate(mContext, R.layout.qyname_list, null);

				viewHolder = new ViewHolder();
				viewHolder.rw_icon = (ImageView) convertView.findViewById(R.id.img);
				viewHolder.rwmc_text = (TextView) convertView.findViewById(R.id.qymc_text);
				viewHolder.zt_btn = (Button) convertView.findViewById(R.id.rwzt);
				convertView.setTag(viewHolder);
			}

			viewHolder = ((ViewHolder) convertView.getTag());
			viewHolder.rwmc_text.setText(CompanyAdapterData.get(position).get("rwmc").toString());
			//�޸�guid
			String rwbh_byk = CompanyAdapterData.get(position).get("rwbh").toString();
			String guid_byk = getGUID(rwbh_byk);
			viewHolder.rwmc_text.setTag(guid_byk);
			viewHolder.zt_btn.setBackgroundColor(Color.TRANSPARENT);

			String rwzt = CompanyAdapterData.get(position).get("rwzt").toString();
			// �������������״̬������ͼ��Ķ�Ӧ��ϵ
			if (rwzt.equals(RWXX.RWZT_WAIT_DISPATCH)) {
				rwValue = "���ύ";
				viewHolder.rw_icon.setImageResource(R.drawable.rwgl_task_list_dsc);
			} else if (rwzt.equals(RWXX.RWZT_WATE_EXECUTION)) {
				rwValue = "��ִ��";
				viewHolder.rw_icon.setImageResource(R.drawable.rwgl_icon_taskwait);
			} else if (rwzt.equals(RWXX.RWZT_ON_EXECUTION)) {
				rwValue = "ִ����";
				viewHolder.rw_icon.setImageResource(R.drawable.rwgl_task_list_zxz);
			} else if (rwzt.equals(RWXX.RWZT_EXECUTION_FINISH)) {
				rwValue = "ִ�����";
				viewHolder.rw_icon.setImageResource(R.drawable.rwgl_task_list_zwwc);
			} else if (rwzt.equals(RWXX.RWZT_WAIT_AUDIT)) {
				rwValue = "�����";
				viewHolder.rw_icon.setImageResource(R.drawable.rwgl_task_list_dxf);
			} else if (rwzt.equals(RWXX.RWZT_ON_FINISH)) {
				rwValue = "�������";
				viewHolder.rw_icon.setImageResource(R.drawable.rwgl_task_list_yxf);
			} else if (rwzt.equals(RWXX.RWZT_AUDIT_NOPASSED)) {
				rwValue = "���δͨ��";
				viewHolder.rw_icon.setImageResource(R.drawable.rwgl_task_list_dxg);
			} else if (rwzt.equals(RWXX.RWZT_ON_RETURN)) {
				rwValue = "�˻�����";
				viewHolder.rw_icon.setImageResource(R.drawable.rwgl_task_list_rwht);
			} else if (rwzt.equals(RWXX.RWZT_WAIT_FILE)) {
				rwValue = "���鵵";
				viewHolder.rw_icon.setImageResource(R.drawable.rwgl_task_list_zwwc);
				//tfy:�޸��ѹ���״̬
			}else if (rwzt.equals(RWXX.RWZT_YGQ)) {
				rwValue = "�ѹ���";
				viewHolder.rw_icon.setImageResource(R.drawable.rwgl_ygq);
			}
			else if (rwzt.equals(RWXX.RWZT_DSB)) {
				rwValue = "���ϱ�";
				viewHolder.rw_icon.setImageResource(R.drawable.rwgl_dsb);
			}
			else {
				rwValue = "״̬����ȷ";
				viewHolder.zt_btn.setTextColor(R.color.red);
				viewHolder.rw_icon.setImageResource(R.drawable.rwgl_icon_task_others);
			}
			viewHolder.zt_btn.setText(rwValue);
			return convertView;
		}
	}

	private class ViewHolder {
		ImageView rw_icon;
		TextView rwmc_text;
		Button zt_btn;
	}

	/**
	 * ������������webservice��ȡ������Ϣ
	 */
	public ArrayList<HashMap<String, Object>> requestSearchTask(ArrayList<HashMap<String, Object>> searchConditionsList) {

		String methodName = "GetAllTaskByUserId";
		ArrayList<HashMap<String, Object>> searchdata = new ArrayList<HashMap<String, Object>>();
		//�Ӳ�ѯҳ�洫������������
		HashMap<String, Object> map = searchConditionsList.get(0);
		map.put("pageIndex", pageIndex++);
		map.put("pageSize", pageSize);
		map.put("syncDataRegionCode", UserAreaCode);
		ArrayList<HashMap<String, Object>> data = new ArrayList<HashMap<String, Object>>();
		data.add(map);
		String taskInfoJson = JsonHelper.listToJSON(data);
		ArrayList<HashMap<String, Object>> params = new ArrayList<HashMap<String, Object>>();
		HashMap<String, Object> param = new HashMap<String, Object>();
		param.put("taskInfoJson", taskInfoJson);
		String token = "";
		try {
			token = DESSecurity.encrypt(methodName);
		} catch (Exception e1) {

			e1.printStackTrace();
		}
		param.put("token", token);
		params.add(param);
		try {
			String jsonStr = (String) WebServiceProvider.callWebService(Global.NAMESPACE, methodName, params, Global.getGlobalInstance().getSystemurl() + Global.WEBSERVICE_URL,
					WebServiceProvider.RETURN_STRING, true);
			if (jsonStr != null&&!"".equals(jsonStr)) {
				// String node[]
				// ={"RowNumber","RWBH","FBSJ","BJQX","RWMC","KeyWord","RWLYMC","RWZT","RWZTMC","FBRMC"};
				searchdata = JsonHelper.paseJSON(jsonStr);
				if (searchdata == null) {
					searchdata = new ArrayList<HashMap<String, Object>>();
				} else {
					searchdata = parseLowerList(searchdata);
				}

			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return searchdata;
	}
	
	
	/**
	 * ������������webservice��ȡ������Ϣ
	 */
	public ArrayList<HashMap<String, Object>> requestSearchTask2(ArrayList<HashMap<String, Object>> searchConditionsList) {

		String methodName = "QueryList";
		ArrayList<HashMap<String, Object>> searchdata = new ArrayList<HashMap<String, Object>>();
		//�Ӳ�ѯҳ�洫������������
		HashMap<String, Object> map = searchConditionsList.get(0);
//		map.put("pageIndex", pageIndex++);
//		map.put("pageSize", pageSize);
		map.put("pageIndex",pageIndex++ );
		map.put("pageSize", "15");
	
	String hashMapToJson = hashMapToJson(map);
		
		ArrayList<HashMap<String, Object>> params = new ArrayList<HashMap<String, Object>>();
		HashMap<String, Object> param = new HashMap<String, Object>();
		param.put("flowQueryConditionModel", hashMapToJson);
		
		params.add(param);
		try {
			String jsonStr = (String) WebServiceProvider.callWebService(Global.NAMESPACE, methodName, params, Global.getGlobalInstance().getSystemurl() + Global.WEBSERVICE_URL,
					WebServiceProvider.RETURN_STRING, true);
			if (jsonStr != null&&!"".equals(jsonStr)) {
				// String node[]
				// ={"RowNumber","RWBH","FBSJ","BJQX","RWMC","KeyWord","RWLYMC","RWZT","RWZTMC","FBRMC"};
			
				//searchdata = JsonHelper.paseJSON(jsonStr);
				//TODO �����ֶβ�ͬ BYK  
				TaskListModel paseJSON = JsonHelper.paseJSON(jsonStr, TaskListModel.class);
				
				List<TaskListDetail> taskListLine = paseJSON.TaskListLine;
				ArrayList<HashMap<String, Object>> datas=new ArrayList<HashMap<String,Object>>();
				String rwzt_byk="";
				for (int i = 0; i < taskListLine.size(); i++) {
					HashMap<String, Object>  maps=new HashMap<String, Object>();
					
					TaskListDetail taskListDetail = taskListLine.get(i);
					maps.put("fbsj", taskListDetail.LeTaskCreatedTime);
					maps.put("bjqx", taskListDetail.LeTaskTransactedTime);
					//����
					maps.put("rwmc", taskListDetail.LeTaskName);
					//״̬ 
					//���·�			
					if (taskListDetail.LeTaskProcessStatus.equals("10")) {
						maps.put("rwzt","000");
						rwzt_byk="000";
					}else if (taskListDetail.LeTaskProcessStatus.equals("15")) {
						//��ִ��
						maps.put("rwzt","003");
						rwzt_byk="003";
					}else if (taskListDetail.LeTaskProcessStatus.equals("16")) {
						//ִ����
						maps.put("rwzt","005");
						rwzt_byk="005";
					}
					else if (taskListDetail.LeTaskProcessStatus.equals("23")) {
						//�԰��
						maps.put("rwzt","009");
						rwzt_byk="009";
					}
					else if (taskListDetail.LeTaskProcessStatus.equals("12")) {
						//�����
						maps.put("rwzt","007");
						rwzt_byk="007";
					}
					else if (taskListDetail.LeTaskProcessStatus.equals("13")) {
						//�����
						maps.put("rwzt","007");
						rwzt_byk="007";
					}
					else if (taskListDetail.LeTaskProcessStatus.equals("14")) {
						//���δͨ��
						maps.put("rwzt","008");
						rwzt_byk="008";
					}
					else if (taskListDetail.LeTaskProcessStatus.equals("19")) {
						//�˻�����
						maps.put("rwzt","010");
						rwzt_byk="010";
					}
					else if (taskListDetail.LeTaskProcessStatus.equals("20")) {
						//���鵵
						maps.put("rwzt","011");
						rwzt_byk="011";
					}
					else if (taskListDetail.LeTaskProcessStatus.equals("17")) {
						//ִ�����*�����
						maps.put("rwzt","007");
						rwzt_byk="007";
					}
					else if (taskListDetail.LeTaskProcessStatus.equals("18")) {
						//ִ�����*�����
						maps.put("rwzt","007");
						rwzt_byk="007";
					}
					else if (taskListDetail.LeTaskProcessStatus.equals("21")) {
						//�ѹ���
						maps.put("rwzt","012");
						rwzt_byk="012";
					}
					else if (taskListDetail.LeTaskProcessStatus.equals("22")) {
						//�ѹ���
						maps.put("rwzt","013");
						rwzt_byk="013";
					}
					else {
						//����
						maps.put("rwzt","-1");
					}
					//��������
					
					maps.put("rwlx",taskListDetail.LeTaskType);
					//  ������Դ
					maps.put("rwly",taskListDetail.LeTaskSource);
					//�����̶�
					maps.put("jjcd",taskListDetail.LeTaskUrgency);
					//������
					maps.put("fbr",taskListDetail.LeTaskPublisher);
					//�����
					maps.put("LeTaskSubjectTerm",taskListDetail.LeTaskSubjectTerm);
					
					//���̷�����Դ(1����λ�ڲ���2���ϼ���λ��3���¼���λ)

					maps.put("OriginatingSource",taskListDetail.OriginatingSource);
					
					//��ǰ��������Id

					maps.put("CurrentFlowTaskId",taskListDetail.CurrentFlowTaskId);
					
					//�Ͻ���������Id

					maps.put("PrevFlowTaskId",taskListDetail.PrevFlowTaskId);
					//�Ͻڰ�����

					maps.put("PrevTransactor",taskListDetail.PrevTransactor);
					
					//�Ͻڰ������

					maps.put("PrevTransactedComment",taskListDetail.PrevTransactedComment);
					//�½ڰ�����

					maps.put("NextTransactor",taskListDetail.NextTransactor);
					//����code
					maps.put("rwbh", taskListDetail.LeTaskCode);
					
					//����id
					maps.put("LeTaskId", taskListDetail.LeTaskId);
					
					datas.add(maps);
					
					//���뱾�����ݿ�
					ContentValues cv = new ContentValues();
					if ("011".equals(taskListDetail.LeTaskSource)) {
						cv.put("rwly", RWXX.YBRW_LY);
					}else if ("010".equals(taskListDetail.LeTaskSource)) {
						cv.put("rwly", RWXX.XCZF_LY);
					}
					String guid_byk = UUID.randomUUID().toString();
					cv.put("guid", guid_byk);
					cv.put("bz", taskListDetail.PrevTransactedComment);
					cv.put("rwmc", taskListDetail.LeTaskName);
					cv.put("rwbh", taskListDetail.LeTaskCode);
					cv.put("jjcd", taskListDetail.LeTaskUrgency);
		
					
					String sql = "select * from PC_USERS where U_RealName='"+taskListDetail.LeTaskPublisher.toString().trim()+"'";

					HashMap<String, Object> userInfo = SqliteUtil.getInstance().getDataMapBySqlForDetailed(sql);
					if (userInfo.size()!=0) {
						cv.put("fbr",userInfo.get("userid").toString());
					}else{
						cv.put("fbr","");
					}
					
					// �������������͵�¼�û�����������ͬ
					cv.put("syncdataregioncode", UserAreaCode);
					cv.put("fbsj", taskListDetail.LeTaskCreatedTime);
					cv.put("bjqx", taskListDetail.LeTaskTransactedTime);
					cv.put("rwzt", rwzt_byk);
					cv.put("rwlx",taskListDetail.LeTaskType);
					HashMap<String, Object> conditions = new HashMap<String, Object>();
					conditions.put("rwbh", taskListDetail.LeTaskCode);
					
				ArrayList<HashMap<String, Object>> rwdata = getList2("rwzt", conditions, "T_YDZF_RWXX");
					
					if (rwdata == null || rwdata.size() == 0) {
						
						SqliteUtil.getInstance().insert(cv, "T_YDZF_RWXX");
					}else {
						String[] whereArgs = { taskListDetail.LeTaskCode };
						SqliteUtil.getInstance().update("T_YDZF_RWXX", cv, "guid=?", whereArgs);
					}
				}
				
				
				if (datas == null) {
					searchdata = new ArrayList<HashMap<String, Object>>();
				} else {
					searchdata = parseLowerList(datas);
				}

			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return searchdata;
	}
	/**
	 * ת��Сд��key
	 * 
	 * @param data
	 * @return
	 */
	public ArrayList<HashMap<String, Object>> parseLowerList(ArrayList<HashMap<String, Object>> data) {
		ArrayList<HashMap<String, Object>> parseData = new ArrayList<HashMap<String, Object>>();
		for (int i = 0; i < data.size(); i++) {
			HashMap<String, Object> map = new HashMap<String, Object>();
			Iterator<?> iterator = data.get(i).entrySet().iterator();

			while (iterator.hasNext()) {
				@SuppressWarnings("rawtypes")
				Map.Entry entry = (Map.Entry) iterator.next();
				map.put(entry.getKey().toString().toLowerCase(), entry.getValue());
			}
			parseData.add(map);

		}
		return parseData;
	}
	

    /**������ԴHashMapת����json 
     * @param map  
     */  
    public static String hashMapToJson(HashMap map) {  
        String string = "{";  
        for (Iterator it = map.entrySet().iterator(); it.hasNext();) {  
            Entry e = (Entry) it.next();  
            string +=  e.getKey() + ":";  
            string += "\"" + e.getValue() + "\",";  
        }  
        string = string.substring(0, string.lastIndexOf(","));  
        string += "}";  
        return string;  
    }  
    
	public String getGUID(String rwbh) {
		String statusSql = "select guid from T_YDZF_RWXX where rwbh='" + rwbh
				+ "'";
		ArrayList<HashMap<String, Object>> data = null;
		data = SqliteUtil.getInstance().queryBySqlReturnArrayListHashMap(
				statusSql);

		if (data != null && data.size() > 0) {
			return (String) data.get(0).get("guid");
		} else {
			return "";
		}

	}
	
	
	/**
	 * �����ṩ���������ڱ��в�ѯ��������
	 * 
	 * @param colum
	 *            ��Ҫ��ѯ����
	 * @param table
	 *            ����
	 * @param condition
	 *            ɸѡ����������֮����and���ӣ������Ĳ������� = ������ƥ������ֵ��
	 * @return default : new ArrayList<HashMap<String, Object>>()
	 */
	public ArrayList<HashMap<String, Object>> getList2(String colum,
			HashMap<String, Object> conditions, String table) {
		StringBuilder sql = new StringBuilder("select " + colum + " from "
				+ table + " where ");

		if (conditions != null && conditions.size() > 0) {
			Iterator<Entry<String, Object>> iterator = conditions.entrySet()
					.iterator();
			while (iterator.hasNext()) {
				Entry<String, Object> condition = iterator.next();

				sql.append( condition.getKey() + " = '"
						+ condition.getValue() + "'");
			}
		}
		try {
			SqliteUtil instance = SqliteUtil.getInstance();
			ArrayList<HashMap<String, Object>> data = instance.queryBySqlReturnArrayListHashMap(sql
					.toString());
			return data;
		} catch (SQLiteException e) {
		} catch (Exception e) {
		}
		return new ArrayList<HashMap<String, Object>>();
	}
	
	
	
}
