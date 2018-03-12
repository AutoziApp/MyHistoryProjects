package com.mapuni.android.taskmanager;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mapuni.android.MobileEnforcement.R;
import com.mapuni.android.base.BaseActivity;
import com.mapuni.android.base.Global;
import com.mapuni.android.base.controls.loading.YutuLoading;
import com.mapuni.android.base.util.DisplayUitl;
import com.mapuni.android.base.util.LogUtil;
import com.mapuni.android.business.RWXX;
import com.mapuni.android.dataprovider.DESSecurity;
import com.mapuni.android.dataprovider.JsonHelper;
import com.mapuni.android.dataprovider.SqliteUtil;
import com.mapuni.android.enterpriseArchives.SlideOnLoadAdapter;
import com.mapuni.android.enterpriseArchives.SlideView;
import com.mapuni.android.model.FlowStepListxx;
import com.mapuni.android.model.FlowTaskListxx;
import com.mapuni.android.model.FlowTransaction;
import com.mapuni.android.model.Json_list;
import com.mapuni.android.model.LawEnforcementTask;
import com.mapuni.android.netprovider.Net;
import com.mapuni.android.netprovider.WebServiceProvider;

public class TaskFlowSildeActivity extends BaseActivity {
	private static final String TAG = "TaskFlowSildeActivity";
	private RWXX rwxx;
	private SlideView slideView;
	private final String TableName = "T_YDZF_RWXX";
	/** �û��������� **/
	private final String UserAreaCode = Global.getGlobalInstance()
			.getAreaCode();
	ArrayList<HashMap<String, Object>> taskStateData;
	YutuLoading yutuLoading;
	String RWBH;
	/** Ĭ����ʾ�ڼ���ҳ�� */
	private int positon;
	/** �����б��ѯ��� */
	private final String taskListSql = "select distinct a.guid,a.[RWMC],a.[BJQX],a.jjcd,a.[FBSJ],c.[U_RealName] as fbrmc from T_YDZF_RWXX a left join "
			+ " T_YDZF_RWXX_USER as b on a.rwbh=b.RWXXBH left join PC_Users as c on a.fbr=c.UserID  ";
	String transactedTaskId = "0";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		// setContentView(R.layout.ui_mapuni);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.ui_mapuni);
		findViewById(R.id.ui_mapuni_divider).setVisibility(View.GONE);
		SetBaseStyle((RelativeLayout) findViewById(R.id.parentLayout), "�������");
		initData();

	}

	private void initData() {
		rwxx = new RWXX();
		Bundle bundle = getIntent().getExtras();
		// ��ȡ�����������
		taskStateData = (ArrayList<HashMap<String, Object>>) bundle.get("obj");
		LogUtil.i(TAG, "�������Activity��ȡ�����ݣ�" + taskStateData.toString());
		positon = bundle.getInt("Position");
		
		daiXieBan = taskStateData.get(positon).get("statu").toString();
		initView();
	}

	@Override
	protected void onRestart() {

		super.onRestart();
		refreshView();
	}

	@Override
	protected void onPause() {
		super.onPause();
		positon = slideView.getCurrentPosition();
	}

	private final int GUIDANG_SUCCESS = 1;
	private final int GUIDANG_FALI = 0;
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case GUIDANG_SUCCESS:
				if (yutuLoading != null) {
					yutuLoading.dismissDialog();
				}
				Toast.makeText(TaskFlowSildeActivity.this, "����鵵�ɹ�",
						Toast.LENGTH_SHORT).show();
				TaskFlowSildeActivity.this.finish();
				break;
			case GUIDANG_FALI:
				if (yutuLoading != null) {
					yutuLoading.dismissDialog();
				}
				Toast.makeText(TaskFlowSildeActivity.this, "����鵵ʧ��",
						Toast.LENGTH_SHORT).show();
				break;
			default:
				break;
			}
		};
	};
	private String daiXieBan;

	private void initView() {
		slideView = new SlideView(this, 0);
		int size = taskStateData.size();
		slideView.setSlideViewWidth(170/*
										 * DisplayUitl.getMobileResolution(this)[
										 * 0] / size
										 */);
		for (int i = 0; i < size; i++) {
			final ListView listview = new ListView(this);
			listview.setCacheColorHint(Color.TRANSPARENT);
			listview.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,
					LayoutParams.FILL_PARENT));
			SlideOnLoadAdapter adapter = null;
			final int temp = i;
			adapter = new SlideOnLoadAdapter(listview) {
				@Override
				public void OnLoad() {
					yutuLoading = new YutuLoading(TaskFlowSildeActivity.this);
					yutuLoading.setCancelable(true);
					yutuLoading.showDialog();

					new SyncLoadingData(temp).execute(this.view);
				}
			};
			slideView.AddPageView(adapter, taskStateData.get(i).get("statu")
					.toString());
		}
		slideView.setFirstPosition(positon);
		slideView.Display();

		((LinearLayout) findViewById(R.id.middleLayout)).addView(slideView);
	}

	/**
	 * ����ˢ��view
	 */
	public void refreshView() {

		((LinearLayout) findViewById(R.id.middleLayout)).removeAllViews();
		initView();
	}

	private class SyncLoadingData extends AsyncTask<View, Void, Void> {
		private View view;
		private ArrayList<HashMap<String, Object>> obj;
		private int position;
		private String statu;
		private LawEnforcementTask lawenforcementtask;

		public SyncLoadingData(int temp) {
			this.position = temp;
			obj = new ArrayList<HashMap<String, Object>>();
		}

		@Override
		protected Void doInBackground(View... params) {
			view = params[0];
			statu = taskStateData.get(position).get("statu").toString();
			// ���ش洢
			if (statu.equals("���ύ")) {
				String sql = taskListSql + " where a.fbr='"
						+ Global.getGlobalInstance().getUserid()
						+ "' and (rwly='011' or rwly='012') and rwzt='"
						+ RWXX.RWZT_WAIT_DISPATCH
						+ "' and a.syncdataregioncode='" + UserAreaCode
						+ "' order by fbsj desc";
				obj.addAll(SqliteUtil.getInstance()
						.queryBySqlReturnArrayListHashMap(sql));
			} else {// ��̨��ȡ
				String statuvalue = taskStateData.get(position)
						.get("statuvalue").toString();
				obj.addAll(requestAuditTask(statuvalue));
			}
	
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			if (yutuLoading != null) {
				yutuLoading.dismissDialog();
			}
			if (obj != null && obj.size() > 0) {
				((ListView) view).setAdapter(new MyTaskAdapter(TaskFlowSildeActivity.this,
						(ArrayList<HashMap<String, Object>>) obj, statu));
				((ListView) view)
						.setOnItemClickListener(new AdapterView.OnItemClickListener() {

							@Override
							public void onItemClick(AdapterView<?> arg0,
									View arg1, int arg2, long arg3) {
								if (arg1.getTag() == null) {
									return;
								}
								transactedTaskId = ((ViewHolder) arg1.getTag()).transactedFlowInstanceId
										.getText().toString();
								
								
								String guid = ((ViewHolder) arg1.getTag()).id
										.getText().toString();
								if ( statu.equals("���޸�")) {
									//���޸ĵ�ʱ�򴫵ݹ�ȥ������id
									String requestData = requestData(transactedTaskId);
									
									Intent intent = new Intent(
											TaskFlowSildeActivity.this,
											TaskRegisterActivity.class);
									Bundle bundle = new Bundle();
									rwxx.setCurrentID(guid);
									bundle.putSerializable("BusinessObj", rwxx);
									if (statu.equals("���޸�"))
										intent.putExtra("modify", "1");
									intent.putExtra("myTaskId", requestData);
									intent.putExtra("myTransactedTaskId", transactedTaskId);
									intent.putExtras(bundle);
									startActivity(intent);
									return;
								}else if(statu.equals("���ύ") ){
									
								String  sql="select * from T_YDZF_RWXX where Guid ='"+guid+"'";
								ArrayList<HashMap<String, Object>> map = SqliteUtil.getInstance().queryBySqlReturnArrayListHashMap(sql);
								String rwbh = map.get(0).get("rwbh").toString();
								
									Intent intent = new Intent(
											TaskFlowSildeActivity.this,
											TaskRegisterActivity.class);
									Bundle bundle = new Bundle();
									rwxx.setCurrentID(guid);
									bundle.putSerializable("BusinessObj", rwxx);
									intent.putExtra("modify", "0");
									intent.putExtra("myTaskId", rwbh);
									intent.putExtra("myTransactedTaskId", transactedTaskId);
									intent.putExtras(bundle);
									startActivity(intent);
									return;
								}

								rwxx.setCurrentID(guid);// ��ȡ����������
								String rwbh2 = rwxx.getRWBH(guid);
								Intent intent = new Intent(
										TaskFlowSildeActivity.this,
										TaskInformationActivity.class);
								
								intent.putExtra("daiXieBan", daiXieBan);
								
								if (statu.equals("�����") || statu.equals("���޸�")
										|| statu.equals("���鵵")) {
									ArrayList<HashMap<String, Object>> data = (ArrayList<HashMap<String, Object>>) obj;
									intent.putExtra("ProcessStatus", data
											.get(arg2).get("ProcessStatus")
											.toString());
								}
								Bundle bundle = new Bundle();
								bundle.putSerializable("BusinessObj", rwxx);
								bundle.putString("rwzt", statu);
								bundle.putString("transactedTaskId",
										transactedTaskId);
								bundle.putString("ykrwbh",
										rwbh2);
								intent.putExtras(bundle);
								startActivity(intent);
							}
						});
			} else {
				((ListView) view).setAdapter(new MyTaskAdapter(
						TaskFlowSildeActivity.this,
						new ArrayList<HashMap<String, Object>>(), statu));
			}

		}
		
		
		/**
		 * //��ȡ����
		 */
		public String requestData(
				String transactedFlowInstanceId) {
			ArrayList<HashMap<String, Object>> result = new ArrayList<HashMap<String, Object>>();
			//String methodName = "QueryDetail";
			String methodName = "GetTaskQueryDetail";
			ArrayList<HashMap<String, Object>> params = new ArrayList<HashMap<String, Object>>();
			HashMap<String, Object> param = new HashMap<String, Object>();
			param.put("parms", transactedFlowInstanceId);
			LogUtil.i("parms", "����parms��" + transactedFlowInstanceId);
			params.add(param);
			try {
				String json = (String) WebServiceProvider.callWebService(
						Global.NAMESPACE, methodName, params, Global
								.getGlobalInstance().getSystemurl()
								+ Global.WEBSERVICE_URL,
						WebServiceProvider.RETURN_STRING, true);
				if (json != null && !json.equals("")) {
					Json_list json_list = JsonHelper.paseJSON(json, Json_list.class);
					
					lawenforcementtask = json_list.getLawEnforcementTask();
					
					return lawenforcementtask.getTaskId();
				} else {

				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			return "";
		}

		/**
		 * FileName: TaskManagerFlowActivity.java Description:�������������������
		 * 
		 * @author Administrator
		 * @Version 1.3.4
		 * @Copyright �п���ͼ���¿Ƽ����޹�˾<br>
		 * @Create at: 2012-12-10 ����03:34:21
		 */
		public class MyTaskAdapter extends BaseAdapter {
			private final Context context;
			private LayoutInflater mInflater = null;
			private ArrayList<HashMap<String, Object>> data;
			private final int layoutid = R.layout.ui_list_item_cell;
			private final String rwzt;
			private final int textSize;
			private int textColor = Color.BLACK;
			private int textColorRed = Color.RED;
			private int textColorGreen = Color.GREEN;
			private int textColorYellow = Color.rgb(255, 140, 00);

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
			public MyTaskAdapter(Context context,
					ArrayList<HashMap<String, Object>> data, String rwzt) {
				this.mInflater = LayoutInflater.from(context);
				this.context = context;
				this.data = data == null ? new ArrayList<HashMap<String, Object>>()
						: data;
				this.rwzt = rwzt;

				textSize = Integer.parseInt(DisplayUitl.getSettingValue(
						context, DisplayUitl.TEXTSIZE, 22).toString());
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
			public View getView(final int position, View convertView,
					ViewGroup parent) {
				if (data == null || data.size() == 0) {
					YutuLoading loading = new YutuLoading(context);
					if (!Net.checkNet(TaskFlowSildeActivity.this)) {
						Toast.makeText(context, "������������,��ʱ�޷���ȡ������Ϣ",
								Toast.LENGTH_SHORT).show();
					}
					loading.setLoadMsg("", "���޴�״̬����");
					loading.setFailed();
					return loading;
				}
				ViewHolder holder = null;

				if (convertView == null) {
					convertView = mInflater.inflate(layoutid, null);
					holder = new ViewHolder();
					holder.lefticon = (ImageView) convertView
							.findViewById(R.id.lefticon);
					holder.righticon = (ImageView) convertView
							.findViewById(R.id.rightIcon);
					holder.id = new TextView(context);
					holder.transactedFlowInstanceId = new TextView(context);
					holder.title = (TextView) convertView
							.findViewById(R.id.title);
					holder.content = (TextView) convertView
							.findViewById(R.id.content);
					holder.date = (TextView) convertView
							.findViewById(R.id.date);
					holder.dateMsg = (TextView) convertView
							.findViewById(R.id.dateMsg);

					convertView.setTag(holder);
				} else {
					holder = (ViewHolder) convertView.getTag();
				}

				// 3.Get Data
				int leftImgId = R.drawable.rwgl_icon_task_others;
				String idStr = "";
				String transactedFlowInstanceId = "";
				String titleStr = "";
				String contentStr = "";
				String dateStr = "";
				String RWBH = "";
				if (rwzt.equals("���ύ")) {
					idStr = (String) data.get(position).get("guid");
					transactedFlowInstanceId = "";
					titleStr = (String) data.get(position).get("rwmc");
					contentStr = "";
					dateStr = "";
					RWBH = (String) data.get(position).get("rwbh");
				} else {// ��̨��ȡ������
					idStr = (String) data.get(position).get("TaskCode");
					transactedFlowInstanceId = (String) data.get(position).get(
							"TransactedTaskId");
					titleStr = (String) data.get(position).get("TaskName");
					contentStr = "";
					dateStr = "";
					RWBH = (String) data.get(position).get("TaskCode");
				}

				if (rwzt.equals("��ִ��")) {
					String jjcd = data.get(position).get("Urgency").toString();
					String bjqx = data.get(position).get("LeTaskTransactedTime")
							.toString();
					
					String TaskCode = data.get(position).get("TaskCode").toString();
					String sql2="select * from T_YDZF_RWXX where rwbh='"+TaskCode+"'";
					
//					list = SqliteUtil.getInstance().queryBySqlReturnArrayListHashMap(sql2);
//					
//					if (list.size()!=0) {
//						bjqx = list.get(0).get("bjqx").toString();
//					}
					
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
					leftImgId = R.drawable.rwgl_task_list_dsc;
				} else if (rwzt.equals("���鵵")) {
					leftImgId = R.drawable.rwgl_task_list_zwwc;
				} else if (rwzt.equals("�����")) {
					leftImgId = R.drawable.rwgl_task_list_dxf;
					//TODO:ִ���е�������Ȼ��ʾ�ڴ�ִ����
				} else if (rwzt.equals("ִ����")) {
					leftImgId = R.drawable.rwgl_task_list_zxz;
				} else if (rwzt.equals("��Э��")) {
					leftImgId = R.drawable.rwgl_task_list_dxz;
				} else if (rwzt.equals("�Ѱ��")) {
					leftImgId = R.drawable.rwgl_ybj;
					holder.dateMsg.setVisibility(View.GONE);
				} else {
					leftImgId = R.drawable.rwgl_task_list_dxg;
				}
				if (rwzt.equals("���ύ")) {
					dateStr = (String) data.get(position).get("bjqx");// �������
				} else {
//					if (list.size()!=0) {
//						dateStr =list.get(0).get("bjqx").toString();// �������
//					}else {
						dateStr = (String) data.get(position).get("LeTaskTransactedTime");
				//	}
				
				}
				// �����������ỹ�ж�����
				Date nowDate = new Date();
				Date getDate = new Date();
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				try {
					getDate = sdf.parse(dateStr);

				} catch (ParseException e) {
					e.printStackTrace();
				}
				long timeNow = nowDate.getTime();
				long timeGet = getDate.getTime();
				long dd = (timeGet - timeNow) / (1000 * 60 * 60 * 24);// ��Ϊ��

				holder.id.setText(idStr);
				holder.transactedFlowInstanceId
						.setText(transactedFlowInstanceId);
				holder.title.setText(titleStr);
				holder.content.setText(contentStr);

				if (dd > 0) {
					holder.dateMsg.setText("���ỹ��" + dd + "��");
					holder.dateMsg.setTextColor(Color.GRAY);
				} else if (dd < 0) {
					holder.dateMsg.setText("�����ѹ���" + Math.abs(dd) + "��");
					holder.dateMsg.setTextColor(Color.RED);
				} else {
					holder.dateMsg.setText("������������");
					holder.dateMsg.setTextColor(Color.CYAN);
				}
				holder.date.setText(dateStr);
				holder.lefticon.setBackgroundResource(leftImgId);

				return convertView;
			}
		}
	}

	/**
	 * ����״̬��������
	 */
	public ArrayList<HashMap<String, Object>> requestAuditTask(String statuvalue) {
		ArrayList<HashMap<String, Object>> result = new ArrayList<HashMap<String, Object>>();
		String methodName = "GetLeTaskProcessList";
		ArrayList<HashMap<String, Object>> data_json = new ArrayList<HashMap<String, Object>>();
		HashMap<String, Object> conditions = new HashMap<String, Object>();
		conditions.put("TransactorCode", Global.getGlobalInstance().getUserid());
		conditions.put("QueryType", statuvalue);
		data_json.add(conditions);
		String TransactorCode = JsonHelper.listToJSONXin(data_json);

		ArrayList<HashMap<String, Object>> params = new ArrayList<HashMap<String, Object>>();
		HashMap<String, Object> param = new HashMap<String, Object>();
		param.put("parms", TransactorCode);
		params.add(param);
		try {
			String json = (String) WebServiceProvider.callWebService(
					Global.NAMESPACE, methodName, params, Global
							.getGlobalInstance().getSystemurl()
							+ Global.WEBSERVICE_URL,
					WebServiceProvider.RETURN_STRING, true);
			if (json != null && !json.equals("")) {
				result = JsonHelper.paseJSON(json);
				
			} else {

			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * �����Э��������
	 */
	// public ArrayList<HashMap<String, Object>> requestAssistTask() {
	// ArrayList<HashMap<String, Object>> data = new ArrayList<HashMap<String,
	// Object>>();
	// String methodName = "GetAssistTaskInfo";
	// ArrayList<HashMap<String, Object>> params = new ArrayList<HashMap<String,
	// Object>>();
	// HashMap<String, Object> conditions = new HashMap<String, Object>();
	// conditions.put("userId", Global.getGlobalInstance().getUserid());
	// conditions.put("pageIndex", "1");
	// conditions.put("pageSize", "100");
	// conditions.put("syncDataRegionCode", UserAreaCode);
	// String token = "";
	// try {
	// token = DESSecurity.encrypt(methodName);
	// } catch (Exception e1) {
	//
	// e1.printStackTrace();
	// }
	// conditions.put("token", token);
	// params.add(conditions);
	// try {
	// String jsonStr = (String)
	// WebServiceProvider.callWebService(Global.NAMESPACE, methodName, params,
	// Global.getGlobalInstance().getSystemurl() + Global.WEBSERVICE_URL,
	// WebServiceProvider.RETURN_STRING, true);
	// if (jsonStr != null) {
	// String node[] = { "Guid", "RWBH", "BJQX", "RWMC", "FBRMC", "FBSJ",
	// "TaskFlowDirection" };
	// data = JsonHelper.paseJSON(jsonStr, node);
	// data = parseLowerList(data);
	// }
	// } catch (IOException e) {
	// e.printStackTrace();
	// }
	// return data;
	//
	// }

	/**
	 * ������鵵������
	 */
	// public ArrayList<HashMap<String, Object>> requestArchiveTask() {
	// ArrayList<HashMap<String, Object>> data = new ArrayList<HashMap<String,
	// Object>>();
	// String methodName = "GetAwaitFileTaskInfo";
	// ArrayList<HashMap<String, Object>> params = new ArrayList<HashMap<String,
	// Object>>();
	// HashMap<String, Object> conditions = new HashMap<String, Object>();
	// conditions.put("userId", Global.getGlobalInstance().getUserid());
	// conditions.put("pageIndex", "1");
	// conditions.put("pageSize", "100");
	// conditions.put("syncDataRegionCode", UserAreaCode);
	// String token = "";
	// try {
	// token = DESSecurity.encrypt(methodName);
	// } catch (Exception e1) {
	//
	// e1.printStackTrace();
	// }
	// conditions.put("token", token);
	// params.add(conditions);
	// try {
	// String jsonStr = (String)
	// WebServiceProvider.callWebService(Global.NAMESPACE, methodName, params,
	// Global.getGlobalInstance().getSystemurl() + Global.WEBSERVICE_URL,
	// WebServiceProvider.RETURN_STRING, true);
	// if (jsonStr != null) {
	// String node[] = { "Guid", "RWBH", "BJQX", "RWMC", "FBRMC", "FBSJ",
	// "TaskFlowDirection" };
	// data = JsonHelper.paseJSON(jsonStr, node);
	// data = parseLowerList(data);
	// }
	// } catch (IOException e) {
	// e.printStackTrace();
	// }
	// return data;
	//
	// }

	protected class ViewHolder {
		/** ������Id */
		public TextView id = null;
		/** ������Id */
		public TextView transactedFlowInstanceId = null;
		/** ���б�ĵ�һ������ */
		public TextView title = null;
		/** ���б�ڶ������� */
		public TextView content = null;
		/** ���б�ڶ��к�ߵ����� */
		TextView dateMsg = null;
		/** ���б�ڶ��к�ߵ����� */
		TextView date = null;
		/** �б���ߵ�ͼƬ */
		ImageView lefticon = null;
		/** �б��ұߵ�ͼ�� */
		ImageView righticon = null;
		/** �б������б��е�λ�� */
		public int position = 0;
	}

	/**
	 * ת��Сд��key
	 * 
	 * @param data
	 * @return
	 */
	public ArrayList<HashMap<String, Object>> parseLowerList(
			ArrayList<HashMap<String, Object>> data) {
		ArrayList<HashMap<String, Object>> parseData = new ArrayList<HashMap<String, Object>>();
		;
		for (int i = 0; i < data.size(); i++) {
			HashMap<String, Object> map = new HashMap<String, Object>();
			Iterator<?> iterator = data.get(i).entrySet().iterator();

			while (iterator.hasNext()) {
				@SuppressWarnings("rawtypes")
				Map.Entry entry = (Map.Entry) iterator.next();
				map.put(entry.getKey().toString().toLowerCase(),
						entry.getValue());
			}
			parseData.add(map);

		}
		return parseData;
	}

}
