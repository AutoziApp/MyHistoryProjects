/**
 * 
 */
package com.mapuni.android.enforcement;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import android.R.integer;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.mapuni.android.MobileEnforcement.R;
import com.mapuni.android.base.BaseActivity;
import com.mapuni.android.base.Global;
import com.mapuni.android.base.controls.loading.YutuLoading;
import com.mapuni.android.base.util.DisplayUitl;
import com.mapuni.android.base.util.OtherTools;
import com.mapuni.android.business.RWXX;
import com.mapuni.android.business.SpinnerItem;
import com.mapuni.android.business.TreeNode;
import com.mapuni.android.dataprovider.FileHelper;
import com.mapuni.android.dataprovider.SqliteUtil;
import com.mapuni.android.enterpriseArchives.EnterpriseArchivesActivitySlide;
import com.mapuni.android.main.MainTabActivity;
import com.mapuni.android.service.RydwServices;
import com.mapuni.yqydweb.YqydWebActivity;

/**
 * 
 * @author wanglg�޸��� 2013 10 15
 * 
 */
public class QdjcnlActivity extends BaseActivity implements OnItemClickListener {

	/** ���Ⲽ�� */
	private RelativeLayout two_list_tool_layout;
	/** ����ID */
	protected String RWID;
	/** �������� */
	protected HashMap<String, Object> RWDetail;
	/** bundle���� */
	protected Bundle RWBundle;
	/** ������Ϣ */
	protected RWXX rwxx;
	/** �Ƿ����ϴ� */
	// private String isUpload = "0";
	/** �Ƿ��Ǵ�������ϸ��Ϣ���� */
	private String taskInfoFlag, flag;
	/** ������Ϣ���� */
	protected ArrayList<HashMap<String, Object>> rwxxAttachment;
	/** �Ƿ����������ȡ֤ */
	int mSingleChoiceID = 1;
	/** ����һ���Ի��� */
	private AlertDialog alertDialog;

	/** �嵥��鰴ť */
	// private Button task_site_check_list_btn;

	/** �嵥���ִ��ģ��Spinner */
	private Spinner task_site_enfor_sp;
	/** �����ѡ���ִ��ģ��ID */
	private String task_site_enfor_sp_id;
	/** ��ѡ�е�Spinner�� */
	private String task_site_enfor_sp_str = null;
	/** ��ҵ���� */
	private static String qyid;

	/** �ж�����״̬�ı�־��0�����ִ�С�1��������ɡ�2����ִ���� */
	//�ĳ�   1   2   3
	private String taskEnpriLinkState;

	/** ���ݿ⹤���� */
	private SqliteUtil sqliteUtil;
	TextView tv2;
	
	/**
	 * ȡ֤�������ϴ��ĸ���
	 * */
  private int IS_OBTAIN=14;
	private final Handler handler = new Handler() {
		@Override
		public void handleMessage(android.os.Message msg) {
			switch (msg.arg1) {
			case DAYINGYULAN:
				if (industryID == null || industryID.equals("")) {
					Toast.makeText(QdjcnlActivity.this, "��ѡ��ִ���嵥!", Toast.LENGTH_SHORT).show();
					return;
				}

				try {

					Intent _Intent = new Intent(QdjcnlActivity.this, TaskImageType.class);
					_Intent.putExtra("TaskBH", rwbh);
					_Intent.putExtra("guid", qyid);
					_Intent.putExtra("industryID", industryID);
					startActivity(_Intent);
				} catch (Exception e) {
					e.printStackTrace();
				}

				break;

			case GETDATA_SUCCESS:
				for (TreeNode node : tempRootNodeList) {
					rootNodeList.add(node);
				}
				zhtree.setAdapter(myAdapter);
				zhtree.setOnItemClickListener(QdjcnlActivity.this);
				myAdapter.notifyDataSetChanged();

				break;

			}
		};
	};

	LinearLayout task_site_scoll;
	LayoutInflater inflater;
	ListView zhtree;
	RelativeLayout qyrelativeLayout;
	YutuLoading yutuLoading;
	private final int GETDATA_SUCCESS = 20;
	private final int DAYINGYULAN = 21;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.qdjcnl);
		Intent intent = getIntent();
		sqliteUtil = SqliteUtil.getInstance();
		/** ��ҵ���� */
		qyid = intent.getStringExtra("qyid");

		/** ������ */
		rwbh = intent.getStringExtra("rwbh");
		/** �Ƿ��� �ϴ� */
		// isUpload=intent.getStringExtra("IsUpload");
		taskInfoFlag = intent.getStringExtra("taskInfoFlag");
		
		isSearch = intent.getBooleanExtra("isSearch", false);
		/***/
		flag = intent.getStringExtra("flag");
		/***/

		final String qymc = intent.getStringExtra("companyname");
		RWBundle = intent.getExtras();
		if (!"LSZFQK".equals(flag)) {
			rwxx = (RWXX) RWBundle.get("BusinessObj");
		}
		if (rwxx == null) {
			rwxx = new RWXX();
		}
		// RWID = rwxx.getCurrentID();
		/** ��ʼ��������Ϣ */
		two_list_tool_layout = (RelativeLayout) this.findViewById(R.id.two_list_tool_layout);
		/** ���ñ������� */
		SetBaseStyle(two_list_tool_layout, "�嵥���");

		task_site_scoll = (LinearLayout) findViewById(R.id.task_site_scoll);
		RelativeLayout qymcLayout = (RelativeLayout) findViewById(R.id.node_list_enter);
		TextView tv = (TextView) qymcLayout.findViewById(R.id.qdjc_qymc_tx);
		tv.setSelected(true);
		Log.i("info", "qymc:" + qymc);
		if (!"".equals(qymc) && !"null".equals(qymc) && qymc != null) {

			tv.setText(qymc);
		} else {

			HashMap<String, Object> conditions1 = new HashMap<String, Object>();
			conditions1.put("guid", qyid);
			Log.i("info", "" + qyid);
			ArrayList<HashMap<String, Object>> arryListBack = SqliteUtil.getInstance().getList("qymc", conditions1, "T_WRY_QYJBXX");
			String qymc1 = "";
			if (arryListBack.size() > 0) {
				qymc1 = arryListBack.get(0).get("qymc").toString();
				tv.setText(qymc1);
			} else {
				tv.setText("��ҵ��Ϣȱʧ");
			}
		}

		qymcLayout.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if ("".equals(qymc)) {
					Toast.makeText(QdjcnlActivity.this, "��Ϣȱʧ����ͬ����ȾԴ��Ϣ", Toast.LENGTH_LONG).show();
					return;
				}
				//Intent intent = new Intent(QdjcnlActivity.this, EnterpriseArchivesActivitySlide.class);
				Intent intent = new Intent(QdjcnlActivity.this, YqydWebActivity.class);
				intent.putExtra("qyid", qyid);
				intent.putExtra("noedit", "noedit");
				startActivity(intent);
				

			}
		});

		inflater = LayoutInflater.from(this);
		final View mainview = inflater.inflate(R.layout.node_list, null);
		tv2 = (TextView) mainview.findViewById(R.id.node_list_tv2);
			//�ֳ�ִ�����ִ���嵥 ���ƻ��嵥��ʱ�����ݴ���
//		tv2.setOnClickListener(new View.OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//
//				qdjc_click(mainview);
//			}
//		});

		// �¼ӷ�����ʼ
		final ArrayList<HashMap<String, Object>> qdjcList;
		HashMap<String, Object> condition = new HashMap<String, Object>();
		condition.put("TaskID", rwbh);
		condition.put("QYID", qyid);
		ArrayList<HashMap<String,Object>> list = SqliteUtil.getInstance().getList("IsExcute", condition, "TaskEnpriLink");
		if (list.size()==0) {
			taskEnpriLinkState="1";
		}else{
			taskEnpriLinkState = list.get(0).get("isexcute").toString();// ��ȡ��ǰ��ҵִ��״̬
			
		}
		
		//taskEnpriLinkState = SqliteUtil.getInstance().getList("IsExcute", condition, "TaskEnpriLink").get(0).get("isexcute").toString();// ��ȡ��ǰ��ҵִ��״̬
		// if(!taskEnpriLinkState.equals("1")){
		qdjcList = rwxx.getQdjcListViewItem();
		// }else{
		/*
		 * qdjcList = SqliteUtil.getInstance().queryBySqlReturnArrayListHashMap(
		 * "select distinct * from YDZF_TaskSpecialItem where TaskId='" +
		 * getrwbh + "' and EnterID ='" + getqyid + "'");
		 */
		// qdjcList = SqliteUtil.getInstance().queryBySqlReturnArrayListHashMap(
		// "select tname,tid from YDZF_SpecialTemplate where tid in(select distinct industryid  from YDZF_TaskSpecialItem where TaskId='"
		// + rwbh
		// + "' and EnterID ='" + qyid + "')");
		// }

		QdjcAdapter qdjcAdapter = new QdjcAdapter(qdjcList, QdjcnlActivity.this);

		task_site_scoll.addView(mainview);

		zhtree = (ListView) mainview.findViewById(R.id.zhzd_tree);
		zhtree.setDivider(getResources().getDrawable(R.drawable.list_divider));
		ImageView dividerImageView = (ImageView) mainview.findViewById(R.id.top_list_divider);
		dividerImageView.setVisibility(View.VISIBLE);
	
		zhtree.setAdapter(qdjcAdapter);
		zhtree.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
//				if (isSearch) {
//						Toast.makeText(QdjcnlActivity.this, "��ǰ��ҵֻ�ܲ鿴!", Toast.LENGTH_LONG).show();
//				}else{
					myposition = arg2;
					tname = qdjcList.get(arg2).get("tname").toString();
					tv2.setText(tname);
					task_site_enfor_sp_id = qdjcList.get(arg2).get("tid").toString();
					industryID = task_site_enfor_sp_id;
					WorkAsyncTask task = new WorkAsyncTask(QdjcnlActivity.this);
					task.execute("");
	//			}
				
			}
		});
		myAdapter = new MyAdapter(rootNodeList, this);
	}
	/**
	 * ѡ���ģ������
	 * */
	private String tname;
	/**
	 * ѡ���poition
	 * */
	private int myposition;
	String industryID = "";
	ArrayList<TreeNode> leavesList = new ArrayList<TreeNode>();

	private ArrayList<HashMap<String, Object>> getFatherItemData(String specialItemID) {
		ArrayList<HashMap<String, Object>> data = new ArrayList<HashMap<String, Object>>();
		String sql = getString(R.string.select_SpecialItem_FatherNode) + " '" + specialItemID + "') order by SortIndex ";

		data = SqliteUtil.getInstance().queryBySqlReturnArrayListHashMap(sql);

		return data;
	}

	/**
	 * ��ȡ���ר��ĸ��ڵ� ����tempRootNodeList
	 * 
	 * @param nodeChild
	 */
	private void getRootNode(TreeNode nodeChild) {
		boolean isNew = true;
		TreeNode nodeFather = null;
		ArrayList<HashMap<String, Object>> fatherList = getFatherItemData(nodeChild.SpecialItemID);
		for (HashMap<String, Object> fatherMap : fatherList) {
			String specialItemID = (String) fatherMap.get("id");
			String content = (String) fatherMap.get("content");
			String isContent = (String) fatherMap.get("iscontent");
			String inColumn = (String) fatherMap.get("valueincolumn");
			String typeID = (String) fatherMap.get("resulttypeid");
			String status = (String) fatherMap.get("status");
			String remark = (String) fatherMap.get("remarktip");
			String level = (String) fatherMap.get("level");
			String sortIndex = (String) fatherMap.get("sortindex");

			// ��ֹ������ͬ�ĸ��ڵ�
			for (TreeNode node : tempRootNodeList) {
				if (specialItemID.equals(node.SpecialItemID)) {
					nodeFather = node;
					isNew = false;
					break;
				}
			}
			if (isNew) {
				nodeFather = new TreeNode();
				// ��ֵ
				nodeFather.TID = industryID;
				nodeFather.SpecialItemID = specialItemID;
				nodeFather.sortIndex = sortIndex;
				nodeFather.title = content;
				nodeFather.SpecialItemLevel = level;
				nodeFather.ValueInColumn = inColumn;
				nodeFather.isContent = isContent;
				nodeFather.status = status;
				nodeFather.RemarkTip = remark;
				nodeFather.ResultTypeID = typeID;

			}

			if (Integer.parseInt(level) > 1) {
				// ���Ǹ��ڵ�
				getRootNode(nodeFather);
			} else if (isNew) {
				// ���µĸ��ڵ�
				tempRootNodeList.add(nodeFather);

			}
		}
	}

	List<TreeNode> tempRootNodeList = new ArrayList<TreeNode>();

	public class WorkAsyncTask extends AsyncTask<String, Integer, String> {
		public WorkAsyncTask(Context context) {
			yutuLoading = new YutuLoading(QdjcnlActivity.this);
			yutuLoading.setLoadMsg("���ݼ����У����Ե�...", "");
			yutuLoading.showDialog();
		}

		@Override
		protected String doInBackground(String... params) {
			// �����Ľ����Ϣ��ѯ���ݿ�
			// ��ȡ���е�Ҷ�ӽڵ�

			leavesList = getLeavesNodeList(industryID);

			// ����ÿһ��Ҷ�ӽڵ�,��ȡ����㣬���ұ��浽������
			for (TreeNode node : leavesList) {
				getRootNode(node);
			}

			for (TreeNode nodeRoot : tempRootNodeList) {
				// ��ÿһ�����ڵ�����ӽڵ�
				nodeRoot.rwID = rwbh;
				setChildNode(nodeRoot);
				// ���ø��ڵ��״̬
				// setFatherNodeState(nodeRoot);
			}

			RemoveEmptyTypeNode(tempRootNodeList);

			// ��������
			Collections.sort(tempRootNodeList);

			Message msg = handler.obtainMessage();
			msg.arg1 = GETDATA_SUCCESS;
			handler.sendMessage(msg);

			// handler.sendEmptyMessage(GETDATA_SUCCESS);
			return null;
		}

		@Override
		protected void onPostExecute(String result) {
			if (yutuLoading != null) {
				yutuLoading.dismissDialog();
				yutuLoading = null;
			}
			super.onPostExecute(result);
		}
	}

	private void setChildNode(TreeNode node) {
		if (node != null && node.SpecialItemID != null) {
			// ��ѯ�ӽڵ�
			ArrayList<TreeNode> data = getChildNode(node.SpecialItemID);
			if (data != null) {
				for (TreeNode child : data) {
					setChildNode(child);

					child.setParent(node);
					node.children.add(child);
					// �����ӽڵ���ӽڵ�
				}
			}
		}
	}

	private ArrayList<TreeNode> getChildNode(String id) {
		ArrayList<HashMap<String, Object>> data = null;
		ArrayList<TreeNode> nodeList = new ArrayList<TreeNode>();
		TreeNode node = null;

		String sql = "select b.* from YDZF_TemplateSpacialItem as a left outer join YDZF_SpecialItem as b on a.SpecialItemID = b.ID where b.PID = '" + id + "' and a.tid='"
				+ industryID + "'";
		data = SqliteUtil.getInstance().queryBySqlReturnArrayListHashMap(sql);

		if (data.size() > 0) {
			// �����ӽڵ�
			node = new TreeNode();
			for (HashMap<String, Object> map : data) {

				String specialItemID = (String) map.get("id");
				String specialItemContent = (String) map.get("content");
				String isLoadLastResult = (String) map.get("iscontent");
				String valueInColumn = (String) map.get("valueincolumn");
				String resultTypeID = (String) map.get("resulttypeid");
				String remarkTip = (String) map.get("remarktip");
				String specialItemLevel = (String) map.get("level");
				String sortIndex = (String) map.get("sortindex");

				// ר����Ϣ�ķ�װ
				// �����ӽڵ�
				node = new TreeNode(specialItemContent, "");

				node.qyID = qyid;
				node.rwID = rwbh;
				node.TID = industryID;
				node.SpecialItemID = specialItemID;
				node.SpecialItemLevel = specialItemLevel;
				node.ValueInColumn = valueInColumn;
				node.ResultTypeID = resultTypeID;
				node.isLoadLastResult = isLoadLastResult;
				node.RemarkTip = remarkTip;
				node.sortIndex = sortIndex;

				nodeList.add(node);
			}
		}
		 //	�޸� ����ɸѡ���� where Status = '1'  _byk
		sql = "SELECT * FROM YDZF_SpecialItem WHERE IsContent='0' and PID='" + id + "'";

		data = SqliteUtil.getInstance().queryBySqlReturnArrayListHashMap(sql);

		if (data.size() > 0) {
			// �����ӽڵ�
			node = new TreeNode();
			for (HashMap<String, Object> map : data) {

				String specialItemID = (String) map.get("id");
				String specialItemContent = (String) map.get("content");
				String isLoadLastResult = (String) map.get("iscontent");
				String valueInColumn = (String) map.get("valueincolumn");
				String resultTypeID = (String) map.get("resulttypeid");
				String remarkTip = (String) map.get("remarktip");
				String specialItemLevel = (String) map.get("level");
				String sortIndex = (String) map.get("sortindex");

				// ר����Ϣ�ķ�װ
				// �����ӽڵ�
				node = new TreeNode(specialItemContent, "");

				node.qyID = qyid;
				node.rwID = rwbh;
				node.TID = industryID;
				node.SpecialItemID = specialItemID;
				node.SpecialItemLevel = specialItemLevel;
				node.ValueInColumn = valueInColumn;
				node.ResultTypeID = resultTypeID;
				node.isLoadLastResult = isLoadLastResult;
				node.RemarkTip = remarkTip;
				node.sortIndex = sortIndex;

				nodeList.add(node);
			}
		}
		return nodeList;
	}

	private void RemoveEmptyTypeNode(List<TreeNode> nodeList) {
		List<TreeNode> nodes = new ArrayList<TreeNode>();
		for (int i = 0; i < nodeList.size(); i++) {
			if (nodeList.get(i).children.size() > 0) {
				RemoveEmptyTypeNode(nodeList.get(i).children);
			} else {
				nodes.add(nodeList.get(i));
			}
		}

		for (TreeNode node : nodes) {
			boolean isHas = false;
			for (TreeNode n : leavesList) {
				if (n.SpecialItemID.equals(node.SpecialItemID)) {
					isHas = true;
				}
			}

			if (!isHas) {
				nodeList.remove(node);
			}
		}
	}

	static StringBuffer stringBuffer = new StringBuffer();
	String rwbh;

	MyAdapter myAdapter;
	List<TreeNode> rootNodeList = new ArrayList<TreeNode>();

	/**
	 * ��ȡģ�������е�ר��
	 * 
	 * @param industryID
	 * @return
	 */
	private ArrayList<TreeNode> getLeavesNodeList(String industryID) {
		String sql = "select * from YDZF_TemplateSpacialItem where TID = '" + industryID + "'";
		ArrayList<HashMap<String, Object>> data = null;
		ArrayList<TreeNode> nodeList = new ArrayList<TreeNode>();
		TreeNode node = null;

		data = SqliteUtil.getInstance().queryBySqlReturnArrayListHashMap(sql);

		for (HashMap<String, Object> map : data) {
			String tID = (String) map.get("tid");
			String specialItemID = (String) map.get("specialitemid");// ר��id
			String specialItemLevel = (String) map.get("specialitemlevel");
			String specialItemContent = (String) map.get("specialitemcontent");
			String resultTypeID = (String) map.get("resulttypeid");
			String remarkTip = (String) map.get("remarktip");
			String sortIndex = (String) map.get("specialitemsortindex");
			String isLoadLastResult = (String) map.get("isloadlastresult");
			String valueInColumn = (String) map.get("valueincolumn");

			// ר����Ϣ�ķ�װ
			// �����ӽڵ�
			node = new TreeNode(specialItemContent, "");

			node.qyID = qyid;
			node.rwID = rwbh;
			node.TID = tID;
			node.SpecialItemID = specialItemID;
			node.SpecialItemLevel = specialItemLevel;
			node.ValueInColumn = valueInColumn;
			node.ResultTypeID = resultTypeID;
			node.isLoadLastResult = isLoadLastResult;
			node.RemarkTip = remarkTip;
			node.sortIndex = sortIndex;

			nodeList.add(node);
		}
		return nodeList;
	}

	private static boolean queryNodeState(String str, String rwID, String industryID) {
		ArrayList<HashMap<String, Object>> data = new ArrayList<HashMap<String, Object>>();
		String sql = "SELECT Count(SpecialItemID) AS Count1 FROM YDZF_TaskSpecialItem " + "WHERE SpecialItemResult is not null "
				+ "and SpecialItemResult != '' and SpecialItemResult != 'null' " + "and SpecialItemID in (" + str + ") " + "and TaskID = '" + rwID + "' and EnterID='" + qyid
				+ "' and IndustryID = '" + industryID + "' limit 0,1";

		String result = "";

		data = SqliteUtil.getInstance().queryBySqlReturnArrayListHashMap(sql);

		for (HashMap<String, Object> map : data) {
			result = (String) map.get("count1");
		}

		// String[] _IdArray = str.split(",");

		int _Count = Integer.parseInt(result);

		if (_Count > 0) {
			return true;
		} else {
			return false;
		}
	}

	public static class MyAdapter extends BaseAdapter {
		private final LayoutInflater mInflater;
		private final List<TreeNode> root;

		public MyAdapter(List<TreeNode> root, Context context) {
			this.root = root;
			mInflater = LayoutInflater.from(context);
		}

		@Override
		public int getCount() {
			/*
			 * if(root.size() == 0) { return 1; }
			 */
			return root.size();
		}

		@Override
		public Object getItem(int position) {
			return root.get(position);
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			TreeNode currNode = root.get(position);
			stringBuffer.delete(0, stringBuffer.length());

			getLeaves(currNode);

			String _StrId = stringBuffer.toString();
			if (_StrId != "") {
				_StrId = _StrId.substring(0, _StrId.length() - 1);
			}
			// �жϸýڵ��״̬
			currNode.state = queryNodeState(_StrId, currNode.rwID, currNode.TID);

			View views = mInflater.inflate(R.layout.listitem, null);

			// Image Left
			ImageView imageleft = (ImageView) views.findViewById(R.id.listitem_left_image);
			imageleft.setImageResource(R.drawable.icon_left_not_checked);

			// TextView Center
			TextView tv = (TextView) views.findViewById(R.id.listitem_text);
			tv.setGravity(Gravity.CENTER_VERTICAL);
			tv.setPadding(5, 5, 0, 0);
			tv.setText(currNode.title.toString());

			// ImageView Right
			ImageView imageRight = (ImageView) views.findViewById(R.id.listitem_image);
			imageRight.setPadding(5, 0, 0, 0);

			String textResult = "";
			if (currNode.children.size() < 1) {// Ҷ�ӽڵ�
				textResult = queryNodeResult(currNode);
				if (textResult.length() > 1) {
					textResult = FileHelper.formatTextLength(textResult, 8);
				}
			}

			if (!"".equals(textResult)) {
				String textInfo = tv.getText().toString() + "<sub><font color='blue'>[" + textResult + "]</font></sub>";
				tv.setText(Html.fromHtml(textInfo));
			}

			if (currNode.getChildren().size() > 0 && !currNode.getState()) {
				imageRight.setImageResource(R.drawable.main_more_arrow);
			} else if (currNode.getChildren().size() > 0 && currNode.getState()) {
				imageleft.setImageResource(R.drawable.icon_left_checked);

				imageRight.setImageResource(R.drawable.main_more_arrow);

			} else if (currNode.getChildren().size() < 1 && currNode.getState()) {
				// Ҷ�ӽڵ㲻��ʾ��ͼƬ
				imageleft.setImageResource(R.drawable.icon_left_checked);

			}
			// ���ܣ�����ִ��ʱ�� 50ms����
			// Log.i(TAG, "Build Adapter : ִ��ʱ��---->" +
			// (System.currentTimeMillis() - time));
			return views;
		}

	}

	public static void getLeaves(TreeNode node) {
		List<TreeNode> childList = node.children;
		if (childList.size() == 0) {
			stringBuffer.append("'" + node.SpecialItemID + "',").toString();
		} else {
			// ���ڸ��ڵ�
			for (TreeNode nodeChild : childList) {
				getLeaves(nodeChild);
			}
		}
	}

	private static String queryNodeResult(TreeNode node) {
		String sqlType = "SELECT ControlType as type FROM YDZF_ResultType WHERE ID = '" + node.ResultTypeID + "'";
		String sqlText = "";
		String resultText = "";
		ArrayList<HashMap<String, Object>> data = null;

		data = SqliteUtil.getInstance().queryBySqlReturnArrayListHashMap(sqlType);

		if (data.size() == 0)
			return "";
		String typeStr = (String) data.get(0).get("type");
		if (typeStr != null && typeStr.contains("Text")) {
			sqlText = "SELECT SpecialItemResult AS text FROM YDZF_TaskSpecialItem WHERE TaskID ='" + node.rwID + "' AND EnterID = '" + node.qyID + "' AND IndustryID = '"
					+ node.TID + "'AND SpecialItemID ='" + node.SpecialItemID + "'";
		} else if (typeStr != null && typeStr.contains("Check")) {
			sqlText = "SELECT RName AS text FROM YDZF_ResultType WHERE ID = " + "(SELECT SpecialItemResult FROM YDZF_TaskSpecialItem WHERE TaskID ='" + node.rwID
					+ "' AND EnterID = '" + node.qyID + "' AND IndustryID = '" + node.TID + "'and SpecialItemID ='" + node.SpecialItemID + "')";
		}

		data = SqliteUtil.getInstance().queryBySqlReturnArrayListHashMap(sqlText);

		if (data != null && data.size() > 0) {
			for (HashMap<String, Object> map : data) {
				resultText = (String) map.get("text");
			}
		} else {
			if (typeStr != null && typeStr.contains("Text")) {
				sqlText = "SELECT SpecialItemResult AS text FROM YDZF_TaskSpecialItem WHERE EnterID = '" + node.qyID + "' AND IndustryID = '" + node.TID + "'AND SpecialItemID ='"
						+ node.SpecialItemID + "' order by UpdateTime";
			} else if (typeStr != null && typeStr.contains("Check")) {
				sqlText = "SELECT RName AS text FROM YDZF_ResultType WHERE ID = "
						+ "(SELECT SpecialItemResult FROM YDZF_TaskSpecialItem WHERE SpecialItemResult is not null and EnterID = '" + node.qyID + "' AND IndustryID = '" + node.TID
						+ "'and SpecialItemID ='" + node.SpecialItemID + "' order by UpdateTime desc limit 0,1)";
			}

			data = SqliteUtil.getInstance().queryBySqlReturnArrayListHashMap(sqlText);

			for (HashMap<String, Object> map : data) {
				resultText = (String) map.get("text");
			}
		}

		return resultText;
	}

	/** ��¼ȡ֤��ť�����¼� */
	public void obtain_evid_click(View view) {

		Intent scene_update_intent = new Intent(QdjcnlActivity.this, SiteEvidenceActivity.class);
		scene_update_intent.putExtra("currentTaskID", rwbh);
		scene_update_intent.putExtra("qyid", qyid);
		scene_update_intent.putExtra("taskInfoFlag", taskInfoFlag);
		scene_update_intent.putExtra("isSearch", isSearch);
		startActivity(scene_update_intent);

	}

	/** �嵥���ģ���ʼ����ʾ */
	public void qdjc_init(View view) {
		if (RWBundle != null) {

		}
	}

	private class QdjcAdapter extends BaseAdapter {
		ArrayList<HashMap<String, Object>> list;
		LayoutInflater inflater;
		TextView textView1 = null;
		ArrayList<String> industryIDList;

		public QdjcAdapter(ArrayList<HashMap<String, Object>> list, Context context) {
			this.list = list;
			inflater = LayoutInflater.from(context);
			HashMap<String, Object> conditions = new HashMap<String, Object>();
			conditions.put("TaskID", rwbh);
			conditions.put("EnterID", qyid);
			ArrayList<HashMap<String, Object>> data = SqliteUtil.getInstance().getList("IndustryID", conditions, "YDZF_TaskSpecialItem");
			industryIDList = new ArrayList<String>();
			for (HashMap<String, Object> map : data) {
				industryIDList.add(map.get("industryid").toString());
			}
		}
		@Override
		public int getCount() {
			return list.size();
		}

		@Override
		public Object getItem(int position) {
			return list.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {

			convertView = inflater.inflate(R.layout.qdjcmb_item, null);

			textView1 = (TextView) convertView.findViewById(R.id.textView1);
			String tname = list.get(position).get("tname").toString();
			textView1.setText(tname);
			String tid = list.get(position).get("tid").toString();
			for (String industryID : industryIDList) {
				if (tid.equals(industryID)) {
					textView1.setTextColor(Color.RED);
					break;
				}
			}

			return convertView;
		}
	}

	private boolean isComplete = false;
	private boolean isSearch;

	public void qdjc_complete(View view) {
		isComplete = true;
		upload_click(view);
	}

	/** �嵥����¼����� */
	public void qdjc_click(View view) {
		if (RWBundle != null) {
			final TextView tv = (TextView) view.findViewById(R.id.node_list_tv2);
			LayoutInflater inflater = LayoutInflater.from(QdjcnlActivity.this);
			View v = inflater.inflate(R.layout.task_site_enforcement_template, null);
			task_site_enfor_sp = (Spinner) v.findViewById(R.id.task_site_enfor_sp);

			/** ִ��ģ�������� */
			ArrayAdapter<SpinnerItem> mbAdapter;
			/** Spinner���ݼ��� */
			List<SpinnerItem> mbList = new ArrayList<SpinnerItem>();
			if (rwxx != null) {
				mbList = rwxx.getSpinnerItem();
			}

			mbAdapter = new ArrayAdapter<SpinnerItem>(this, android.R.layout.simple_spinner_item, mbList);
			mbAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			task_site_enfor_sp.setAdapter(mbAdapter);

			task_site_enfor_sp.setOnItemSelectedListener(new OnItemSelectedListener() {

				@Override
				public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
					task_site_enfor_sp_str = task_site_enfor_sp.getSelectedItem().toString();
					ArrayList<HashMap<String, Object>> task_site_enfor_sp_map = new ArrayList<HashMap<String, Object>>();
					task_site_enfor_sp_map = sqliteUtil.queryBySqlReturnArrayListHashMap("select tid from YDZF_SpecialTemplate where tname = '" + task_site_enfor_sp_str + "'");
					if (task_site_enfor_sp_map != null && task_site_enfor_sp_map.size() > 0) {
						task_site_enfor_sp_id = task_site_enfor_sp_map.get(0).get("tid").toString();
					}
				}

				@Override
				public void onNothingSelected(AdapterView<?> arg0) {

				}
			});

			AlertDialog.Builder builder = new AlertDialog.Builder(QdjcnlActivity.this);
			builder.setTitle("��ѡ��ִ��ģ��");
			builder.setIcon(getResources().getDrawable(R.drawable.icon_mapuni_white));
			builder.setView(v);
			builder.setPositiveButton("ȷ��", new OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					tv.setText("(" + task_site_enfor_sp_str + ")");
					rootNodeList.clear();
					tempRootNodeList.clear();
					industryID = task_site_enfor_sp_id;
					WorkAsyncTask task = new WorkAsyncTask(QdjcnlActivity.this);
					task.execute("");
				}
			});

			builder.setNegativeButton("ȡ��", new OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					/** ȡ���Ի��� */
					alertDialog.cancel();
				}
			});
			alertDialog = builder.create();
			alertDialog.show();
		}
	}

	/** ����ѡ��ť�����¼� */
	public void record_click(View view) {

		Intent saveIntent = new Intent(QdjcnlActivity.this, SiteWriteRecordActivity.class);

		saveIntent.putExtra("qyid", qyid);
		saveIntent.putExtra("rwbh", rwbh);
		saveIntent.putExtra("isSearch", isSearch);

		startActivity(saveIntent);
	}

	/** ����ϴ���ť�����¼� */
	public void upload_click(View view) {
		if (RWXX.isInBackgroundUpload) {
			OtherTools.showToast(this, "��ǰ���������ϴ��������ظ��ϴ�");
			return;
		}
		

		HashMap<String, Object> conditions = new HashMap<String, Object>();
		conditions.put("TaskID", rwbh);
		conditions.put("QYID", qyid);
		ArrayList<HashMap<String, Object>> data = SqliteUtil.getInstance().getList("IsExcute", conditions, "TaskEnpriLink");
		conditions.clear();
		conditions.put("RWBH", rwbh);
		ArrayList<HashMap<String, Object>> data2 = SqliteUtil.getInstance().getList("rwzt", conditions, "T_YDZF_RWXX");
		String rwzt = "";
		
		if (data2.size()==0) {
			rwzt="003";
		}else{
			rwzt = data2.get(0).get("rwzt").toString();
		}
		
		 

		String selectZWsql = "select zw from PC_Users where UserID = '" + Global.getGlobalInstance().getUserid() + "'";
		String ZW = SqliteUtil.getInstance().getDepidByDepName(selectZWsql);
		//BYK rwzt
		//if (data.get(0).get("isexcute").toString().equals("1")) {
		
		String is_wan="1";
		if (data.size()==0) {
			 is_wan="1";
		}else {
		is_wan=	data.get(0).get("isexcute").toString();
		}
		
		if (isSearch) {
			Toast.makeText(QdjcnlActivity.this, "��ǰ��ҵֻ�ܲ鿴!", Toast.LENGTH_LONG).show();
			return;
		}
		
			if (is_wan.equals("3")) {
				//�����ֳ�ִ��ִ����� �������ύ��������� 5-24
		//	if (!rwxx.JudgeUserName(rwbh) || !("003".equals(rwzt) || "005".equals(rwzt))) {
				Toast.makeText(QdjcnlActivity.this, "��ǰ��ҵ״���Ѿ��ϴ��������ظ��ϴ���", Toast.LENGTH_LONG).show();
				return;
			//}
		}
		AlertDialog.Builder alertBuilder = new AlertDialog.Builder(QdjcnlActivity.this);
		mSingleChoiceID = 1;
		final String[] singleItems = { "��		(ͬʱ�ϴ��ĵ���ȡ֤����,������������ԭ���ϴ�ʧ�ܣ�) ", "��		(ֻ�ϴ�ִ���嵥��ִ��������ĵ�)" };
		alertBuilder.setTitle("�Ƿ�ͬʱ�ϴ�ͼƬ������Ƶ��ȡ֤����(�����̨�ϴ�)");
		alertBuilder.setSingleChoiceItems(singleItems, 1, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				mSingleChoiceID = whichButton;
			}
		});
		alertBuilder.setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				if (mSingleChoiceID >= 0) {
				
					rwxx.uploadTask(rwbh, QdjcnlActivity.this, qyid, mSingleChoiceID, isComplete,IS_OBTAIN);
					DisplayUitl.saveAppInfoDataToPreference(QdjcnlActivity.this,
							"userid", Global.getGlobalInstance().getUserid());
					//ִ����ʱ���ϴ���Աλ��
					Intent loctionnewIntent = new Intent(QdjcnlActivity.this,
					RydwServices.class);
					QdjcnlActivity.this.startService(loctionnewIntent);
				}
			}
		});
		alertBuilder.setNegativeButton("ȡ��", null);
		alertBuilder.create().show();
	}

	@Override
	public void finish() {
		super.finish();
		if (rwxx != null) {
			rwxx.setCurrentActivityIsAlive(false);
		}
	}

	/** ��ӡԤ����ť�����¼� */
	public void preview_click(View view) {
		Message msg = handler.obtainMessage();
		msg.arg1 = DAYINGYULAN;
		handler.sendMessage(msg);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
		super.onActivityResult(requestCode, resultCode, intent);

		if (myAdapter != null) {

			myAdapter.notifyDataSetChanged();
		}

	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		// TODO Auto-generated method stub

		Intent intent = new Intent();
		MyAdapter mAdapter = (MyAdapter) parent.getAdapter();
		Context context = QdjcnlActivity.this;
		Bundle bundle = new Bundle();
		TreeNode node = (TreeNode) mAdapter.getItem(position);
		bundle.putSerializable("node", node);
		if (node.children.size() > 0) {
			if (node.getChildren().get(0).getChildren().size() > 0) {
				// ��������������
				intent.putExtra("qyid", qyid);
				intent.putExtra("isSearch", isSearch);
				intent.setClass(context, QDMiddleActivity.class);
			} else {
				// ����ֻ��һ��
				intent.putExtra("qyid", qyid);
				intent.putExtra("isSearch", isSearch);
				intent.setClass(context, SpecialItemActivity.class);
			}
			//BYK
			
			intent.putExtra("rwbh", rwbh);
			
			 ArrayList<HashMap<String, Object>> qdjcListViewItem = rwxx.getQdjcListViewItem();
		//	 String tname = qdjcListViewItem.get(position).get("tname").toString();
			 intent.putExtra("tname", tname);
			 String tid = qdjcListViewItem.get(myposition).get("tid").toString();
			 intent.putExtra("tid", tid);
			 
			intent.putExtras(bundle);
			startActivityForResult(intent, 1);
		} else {
			Toast.makeText(context, "��ǰ���ݲ�����!", Toast.LENGTH_SHORT).show();
		}
		context = null;

	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (!tv2.getText().toString().equals("")) {
				tv2.setText("");
				final ArrayList<HashMap<String, Object>> qdjcList;
				HashMap<String, Object> condition = new HashMap<String, Object>();
				condition.put("TaskID", rwbh);
				condition.put("QYID", qyid);
				// wsc 30
				ArrayList<HashMap<String, Object>> arryListBack;
				arryListBack = SqliteUtil.getInstance().getList("IsExcute", condition, "TaskEnpriLink");
				if (arryListBack.size() > 0)
					taskEnpriLinkState = arryListBack.get(0).get("isexcute").toString();// ��ȡ��ǰ��ҵִ��״̬
				else
					taskEnpriLinkState = "";// wsc ����� 30

				// if(!taskEnpriLinkState.equals("1")){
				qdjcList = rwxx.getQdjcListViewItem();
				// }else{
				//
				// qdjcList =
				// SqliteUtil.getInstance().queryBySqlReturnArrayListHashMap(
				// "select tname,tid from YDZF_SpecialTemplate where tid in" +
				// "(select distinct industryid  from YDZF_TaskSpecialItem where TaskId='"
				// + rwbh
				// + "' and EnterID ='" + qyid + "')");
				// }
				QdjcAdapter qdjcAdapter = new QdjcAdapter(qdjcList, QdjcnlActivity.this);

				zhtree.setAdapter(qdjcAdapter);
				zhtree.setOnItemClickListener(new OnItemClickListener() {
				

					@Override
					public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
						tname=qdjcList.get(arg2).get("tname").toString();
						myposition=arg2;
						tv2.setText(tname);
						rootNodeList.clear();
						tempRootNodeList.clear();
						task_site_enfor_sp_id = qdjcList.get(myposition).get("tid").toString();
						industryID = task_site_enfor_sp_id;
						WorkAsyncTask task = new WorkAsyncTask(QdjcnlActivity.this);
						task.execute("");
					}
				});
				return true;
			}
		}
		return super.onKeyDown(keyCode, event);
	}

}
