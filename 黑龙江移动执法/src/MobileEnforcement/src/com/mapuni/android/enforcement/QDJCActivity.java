package com.mapuni.android.enforcement;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteException;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.Html;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mapuni.android.MobileEnforcement.R;
import com.mapuni.android.base.Global;
import com.mapuni.android.base.controls.loading.YutuLoading;
import com.mapuni.android.base.util.DisplayUitl;
import com.mapuni.android.business.QYJBXX;
import com.mapuni.android.business.RWXX;
import com.mapuni.android.business.TreeNode;
import com.mapuni.android.dataprovider.FileHelper;
import com.mapuni.android.dataprovider.SqliteUtil;
import com.mapuni.android.enterpriseArchives.EnterpriseArchivesActivitySlide;
import com.mapuni.android.taskmanager.BaseTaskdetailActivity;

/**
 * FileName: QDJCActivity.java Description:�嵥��� <li>�ֳ�ִ�������嵥 <li>�����������嵥
 * 
 * @author Liusy
 * @Version 1.3.4
 * @Copyright �п���ͼ���¿Ƽ����޹�˾ Create at: 2012-11-30 ����10:22:42
 */
public class QDJCActivity extends BaseTaskdetailActivity implements
		OnItemClickListener {
	private static final String TAG = "HDJcdActivity";

	/********************** Ȩ������ ******************************/
	private final String WRYXX_QX = "vmob2A1B";// ��ȾԴ��Ϣ
//	private final String XCQZ_QX = "vmob2A3B";// �ֳ�ȡ֤
	private final String XCBL_QX = "vmob2A2B";// �嵥��ӡ

	private static final int GETDATA_SUCCESS = -1;

	private ProgressDialog pd;
	private String CurrentTaskID = "";
	private boolean rwzt_is_uploaded = false;// ��־���������Ƿ��Ѿ��ϴ�
	private boolean is_oneself = false;// ��־�������Ƿ����Լ�������
	/** ��ҵ������Ϣ */
	RelativeLayout qyrelativeLayout;

	ListView zhtree;
	RelativeLayout childrl;
	LinearLayout middlelayout;

	Button detail;
	LayoutInflater inflater;
	ImageView text_detail_btn;

	private MyAdapter myAdapter;
	private String qydm;
	private String qyid = "";
	/** ��ҵID */
	private String industryID = "";

	/** Ҷ�ӽڵ㼯�� */
	ArrayList<TreeNode> leavesList = new ArrayList<TreeNode>();

	/** ���ڵ㼯�� */
	List<TreeNode> rootNodeList = new ArrayList<TreeNode>();
	List<TreeNode> tempRootNodeList = new ArrayList<TreeNode>();

	private Handler mHandler;

	static StringBuffer stringBuffer = new StringBuffer();
	private final RWXX rwxx = new RWXX();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		CurrentTaskID = RWBH;
		qyid = currentqyid;
		yutuLoading.setLoadMsg("������ת�����Ե�...", "");
		
		qydm = new QYJBXX().getQYDM(qyid);
		
		
		industryID = stid;
		

		inflater = LayoutInflater.from(this);
		View mainview = inflater.inflate(R.layout.node_list, null);
		LinearLayout layout = (LinearLayout) findViewById(R.id.ui_mapuni_divider);
		layout.setVisibility(View.GONE);
		middlelayout = (LinearLayout) findViewById(R.id.middleLayout);
		middlelayout.addView(mainview);
		middlelayout.setPadding(0, 0, 0, 0);

		/** ListView ����ʾִ���嵥�е����� */
		zhtree = (ListView) mainview.findViewById(R.id.zhzd_tree);
		zhtree.setDivider(getResources().getDrawable(R.drawable.list_divider));
		ImageView dividerImageView = (ImageView) mainview
				.findViewById(R.id.top_list_divider);
		dividerImageView.setVisibility(View.VISIBLE);
		setTitleLayoutVisiable(true);

	

		// ������
		myAdapter = new MyAdapter(rootNodeList, this);
		zhtree.setAdapter(myAdapter);
		zhtree.setOnItemClickListener(this);

		// ���ӵ�ϵͳ��Ϣ����
		Looper looper = Looper.getMainLooper();
		mHandler = new Handler(looper) {
			@Override
			public void handleMessage(Message msg) {

				switch (msg.what) {
				case GETDATA_SUCCESS:
					for (TreeNode node : tempRootNodeList) {
						rootNodeList.add(node);
					}
					myAdapter.notifyDataSetChanged();
					zhtree.requestFocusFromTouch();
					break;
				}

			}
		};

		// ִ���첽��������
		WorkAsyncTask task = new WorkAsyncTask(this);
		task.execute("");
		LinearLayout bottom = (LinearLayout) findViewById(R.id.bottomLayout);
		bottom.setVisibility(View.VISIBLE);
		// TaskDetailActivity taskDetail=new TaskDetailActivity();
//		Button evidence;
		Button  record;
//		Button taskupload;
		record = new Button(this);
//		evidence = new Button(this);
		detail = new Button(this);
//		taskupload = new Button(this);
		ImageView splite;
		int i = 0;
		boolean detailbool = false, recordbool = false, evidencebool = false, taskuploadbool = false;
		if (RWDetail!=null && RWDetail.size()>0) {
			if (RWDetail.get("rwzt").toString().equals(RWXX.TaskEnpriLink_UpLoad))
				rwzt_is_uploaded = true; // ����Ѿ��ϴ�����Ϊtrue
		}
		
		if (rwxx.JudgeUserName(RWBH))
			is_oneself = true;// ������Լ�����������Ϊtrue
		// ����ͼƬ
		if (DisplayUitl.getAuthority(WRYXX_QX)) {
			detail.setBackgroundResource(R.drawable.btn_shap);
			detail.setText("һ��һ��");
			detail.setTextColor(android.graphics.Color.WHITE);
			detail.setWidth(1);
			detail.setVisibility(View.VISIBLE);
			detail.getPaint().setFakeBoldText(true);// �Ӵ�
			detailbool = true;
			// i++;
		}
//		if (DisplayUitl.getAuthority(XCQZ_QX) && !rwzt_is_uploaded
//				&& is_oneself) {
//			evidence.setBackgroundResource(R.drawable.btn_shap);
//			evidence.setText("�ֳ�ȡ֤");
//			evidence.setTextColor(android.graphics.Color.WHITE);
//			evidence.setWidth(1);
//			evidence.setVisibility(View.VISIBLE);
//			evidence.getPaint().setFakeBoldText(true);// �Ӵ�
//			evidencebool = true;
//			i++;
//		}

//		Button mZFWSButton = new Button(this);
//		mZFWSButton = new Button(this);
//		mZFWSButton.setBackgroundResource(R.drawable.btn_shap);
//		mZFWSButton.setId(4);
//		mZFWSButton.setText("�ֳ���¼");
//		mZFWSButton.setWidth(1);
//		mZFWSButton.setGravity(Gravity.CENTER);
//		mZFWSButton.setTextColor(android.graphics.Color.WHITE);
//		mZFWSButton.setVisibility(View.VISIBLE);
//		mZFWSButton.getPaint().setFakeBoldText(true);// �Ӵ�
//		i++;

//		if (DisplayUitl.getAuthority(XCQZ_QX) && !rwzt_is_uploaded
//				&& is_oneself) {// ������������Ѿ��ϴ����߲����Լ����������button������ʾ
//			taskupload.setBackgroundResource(R.drawable.btn_shap);
//			taskupload.setText("����ϴ�");
//			taskupload.setTextColor(android.graphics.Color.WHITE);
//			taskupload.setWidth(1);
//			taskupload.setVisibility(View.VISIBLE);
//			taskupload.getPaint().setFakeBoldText(true);// �Ӵ�
//			taskuploadbool = true;
//			i++;
//		}
		/** �嵥��ӡ */
		if (DisplayUitl.getAuthority(XCBL_QX)) {
			record.setBackgroundResource(R.drawable.btn_shap);
			record.setText("�嵥��ӡ");
			record.setTextColor(android.graphics.Color.WHITE);
			record.setWidth(1);
			record.setVisibility(View.VISIBLE);
			record.getPaint().setFakeBoldText(true);// ��
			recordbool = true;
			i++;
		}
		rwzt_is_uploaded = false;
		is_oneself = false;
		// ��ȡ�ֻ��ֱ���
		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		// �����ť�ĸ߿�
		// int high=(int)((dm.widthPixels/(double)i)*((double)60/(double)300));
		int width = (int) (dm.widthPixels / (double) i);
		// ������
		int j = 0;
		/** �ֳ�ȡ֤ */
//		if (evidencebool) {
//			evidence.setLayoutParams(new LinearLayout.LayoutParams(width,
//					LinearLayout.LayoutParams.FILL_PARENT, 0));
//			splite = new ImageView(this);
//			splite.setScaleType(ScaleType.FIT_XY);
//			splite.setLayoutParams(new LinearLayout.LayoutParams(2,
//					LinearLayout.LayoutParams.FILL_PARENT));
//			splite.setBackgroundResource(R.drawable.bg_bottombutton_splite);
//			bottom.addView(evidence);
//			if (j != i) {
//				bottom.addView(splite);
//			}
//		}

//		if (true) {
//			mZFWSButton.setLayoutParams(new LinearLayout.LayoutParams(width,
//					LinearLayout.LayoutParams.FILL_PARENT, 0));
//			splite = new ImageView(this);
//			splite.setScaleType(ScaleType.FIT_XY);
//			splite.setLayoutParams(new LinearLayout.LayoutParams(2,
//					LinearLayout.LayoutParams.FILL_PARENT));
//			splite.setBackgroundResource(R.drawable.bg_bottombutton_splite);
//			bottom.addView(mZFWSButton);
//			if (j != i) {
//				bottom.addView(splite);
//			}
//		}

		/** �����ϴ� */
//		if (taskuploadbool) {
//			taskupload.setLayoutParams(new LinearLayout.LayoutParams(width,
//					LinearLayout.LayoutParams.FILL_PARENT, 0));
//			splite = new ImageView(this);
//			splite.setScaleType(ScaleType.FIT_XY);
//			splite.setLayoutParams(new LinearLayout.LayoutParams(2,
//					LinearLayout.LayoutParams.FILL_PARENT));
//			splite.setBackgroundResource(R.drawable.bg_bottombutton_splite);
//			bottom.addView(taskupload);
//			if (j != i) {
//				bottom.addView(splite);
//			}
//		}
		/** �嵥��ӡ */
		if (recordbool) {
			record.setLayoutParams(new LinearLayout.LayoutParams(width,
					LinearLayout.LayoutParams.FILL_PARENT, 0));
			splite = new ImageView(this);
			splite.setScaleType(ScaleType.FIT_XY);
			splite.setLayoutParams(new LinearLayout.LayoutParams(2,
					LinearLayout.LayoutParams.FILL_PARENT));

			splite.setBackgroundResource(R.drawable.bg_bottombutton_splite);
			bottom.addView(record);
			if (j != i) {
				bottom.addView(splite);
			}

		}
		if (i != 0) {
			bottom.setVisibility(View.VISIBLE);
		} else {
			bottom.setVisibility(View.GONE);
		}

//		mZFWSButton.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				Intent intent2 = new Intent(QDJCActivity.this,
//						SiteWriteRecordActivity.class);
//				intent2.putExtra("taskid", RWBH);
//				startActivity(intent2);
//			}
//		});

//		evidence.setOnClickListener(new OnClickListener() {// �ֳ�ȡ֤����¼�
//			@Override
//			public void onClick(View arg0) {
//				// String taskID=intent.getStringExtra("rwGUID");
//				Intent intent = new Intent(QDJCActivity.this,
//						SiteEvidenceActivity.class);
//				intent.putExtra("currentTaskID", CurrentTaskID);
//				startActivity(intent);
//
//			}
//		});

		/** �嵥��ӡ ��ť�ļ����¼� */
		record.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Context context = QDJCActivity.this;

				try {
					String rwzt = rwxx.getTaskStatus(RWBH);
					String filePath = Global.SDCARD_RASK_DATA_PATH + "ImgDoc/"
							+ RWBH;
					Intent _Intent = new Intent(context, TaskImageType.class);
					_Intent.putExtra("TaskBH", CurrentTaskID);
					_Intent.putExtra("guid", qyid);
					startActivity(_Intent);
				} catch (SQLiteException e) {
					e.printStackTrace();
				}
			}
		});

		/** ��ȾԴ�������¼� */
		detail.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {

				Bundle nextBundle = new Bundle();
				nextBundle.putBoolean("IsShowTitle", false);

				if (null == qyid || qyid.equals("")) {
					Toast.makeText(QDJCActivity.this, "��ǰ��ҵ������!",
							Toast.LENGTH_LONG).show();
				} else {
					if (yutuLoading != null)
						yutuLoading.showDialog();
					arg0.setEnabled(false);
					Intent intent = new Intent(QDJCActivity.this,
							EnterpriseArchivesActivitySlide.class);
					intent.putExtra("qyid", qyid);
					startActivity(intent);
				}
			}
		});
//		taskupload.setOnClickListener(new OnClickListener() {// �����ϴ�
//					@Override
//					public void onClick(View arg0) {
////						rwxx.isforOneEntpri = false;
////						String qyid = rwxx.queryEntidbyqydm(qydm);
//						rwxx.uploadTask(RWID, QDJCActivity.this,qyid);
//					}
//				});

	}

	/**
	 * Description: ���ڴ��ݽ�����һ����㣬��ѯ���ӽڵ㣬�����ø��ӽ���ϵ
	 * 
	 * @param node
	 * @author Administrator Create at: 2012-12-4 ����10:11:13
	 */
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

	/**
	 * Description: ����ר��ID��ȡ�������ӽڵ�
	 * 
	 * @param id
	 *            ר��ID
	 * @return �ӽڵ㼯��
	 * @author Administrator Create at: 2012-12-4 ����10:10:46
	 */
	private ArrayList<TreeNode> getChildNode(String id) {
		ArrayList<HashMap<String, Object>> data = null;
		ArrayList<TreeNode> nodeList = new ArrayList<TreeNode>();
		TreeNode node = null;

		String sql = "select b.* from YDZF_TemplateSpacialItem as a left outer join YDZF_SpecialItem as b on a.SpecialItemID = b.ID where b.PID = '"
				+ id + "' and a.tid='" + industryID + "'";
		data = SqliteUtil.getInstance()
				.queryBySqlReturnArrayListHashMap(sql);

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

				node.qyID = qydm;
				node.rwID = RWBH;
				node.TID = industryID;
				node.SpecialItemID = specialItemID;
				node.SpecialItemLevel = specialItemLevel;
				node.ValueInColumn = valueInColumn;
				node.ResultTypeID = resultTypeID;
				node.isLoadLastResult = isLoadLastResult;
				node.RemarkTip = remarkTip;
				node.sortIndex = sortIndex;

				Log.i(TAG, "����ӽڵ㣺" + specialItemContent);
				nodeList.add(node);
			}
		}
  //	�޸� ����ɸѡ���� where Status = '1'  _byk
		sql = "SELECT * FROM YDZF_SpecialItem WHERE IsContent='0' and PID='"
				+ id + "'";

		Log.i(TAG, sql);
		data = SqliteUtil.getInstance()
				.queryBySqlReturnArrayListHashMap(sql);

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

				node.qyID = qydm;
				node.rwID = RWBH;
				node.TID = industryID;
				node.SpecialItemID = specialItemID;
				node.SpecialItemLevel = specialItemLevel;
				node.ValueInColumn = valueInColumn;
				node.ResultTypeID = resultTypeID;
				node.isLoadLastResult = isLoadLastResult;
				node.RemarkTip = remarkTip;
				node.sortIndex = sortIndex;

				Log.i(TAG, "����ӽڵ㣺" + specialItemContent);
				nodeList.add(node);
			}
		}
		return nodeList;
	}

	/**
	 * Description:��ȡ���е�Ҷ�ӽ��
	 * 
	 * @param industryID
	 * @return Ҷ�ӽ�㼯��
	 * @author Administrator Create at: 2012-11-30 ����10:26:41
	 */
	private ArrayList<TreeNode> getLeavesNodeList(String industryID) {
		String sql = "select * from YDZF_TemplateSpacialItem where TID = '"
				+ industryID + "'";
		ArrayList<HashMap<String, Object>> data = null;
		ArrayList<TreeNode> nodeList = new ArrayList<TreeNode>();
		TreeNode node = null;

		data = SqliteUtil.getInstance()
				.queryBySqlReturnArrayListHashMap(sql);
		Log.i(TAG, RWBH + "��ȡר�����" + data.size());

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

			node.qyID = qydm;
			node.rwID = RWBH;
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

	/**
	 * Description: ��ѯ���ݿ�,��ȡ������Ϣ
	 * 
	 * @param rwGUID
	 *            �����GUID
	 * @return ������Ϣ����
	 * @author Administrator Create at: 2012-12-4 ����10:10:00
	 */
	private ArrayList<HashMap<String, Object>> getTaskInfo(String rwGUID) {
		ArrayList<HashMap<String, Object>> data = null;
		String sql = "select * from T_YDZF_RWXX where Guid = '" + rwGUID + "'";

		data = SqliteUtil.getInstance()
				.queryBySqlReturnArrayListHashMap(sql);

		return data;
	}

	/**
	 * ������ҳ�淵�أ�ˢ��ҳ���б�
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		Log.i(TAG, "onActivityResult");

		// ֪ͨAdapterˢ�����ݣ����򷵻�ʱ״̬����ı�
		if (myAdapter != null) {
			Log.i(TAG, "notifyDataSetChanged");
			myAdapter.notifyDataSetChanged();
		}
	}

	/**
	 * Description:�ݹ��㷨,��ȡ���ڵ� <li>����һ��Ҷ�ӽڵ㣬��ȡ�����и��ڵ㣬��������ӵ�������
	 * 
	 * @param nodeChild
	 * @author Administrator Create at: 2012-12-4 ����10:08:57
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
			Log.i(TAG, "��ǰ��  Content : " + content);

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
				Log.i(TAG, "��Ӹ��ڵ㣺" + nodeFather.title);
			}
		}
	}

	/**
	 * Description: ��ѯ���ݿ� <li>����ר��ID����ȡ�丸�ڵ�
	 * 
	 * @param specialItemID
	 *            ר��ID
	 * @return ���ڵ�ļ���
	 * @author Administrator Create at: 2012-12-4 ����10:08:09
	 */
	private ArrayList<HashMap<String, Object>> getFatherItemData(
			String specialItemID) {
		ArrayList<HashMap<String, Object>> data = new ArrayList<HashMap<String, Object>>();
		String sql = getString(R.string.select_SpecialItem_FatherNode) + " '"
				+ specialItemID + "') order by SortIndex ";

		data = SqliteUtil.getInstance()
				.queryBySqlReturnArrayListHashMap(sql);

		return data;
	}

	/**
	 * ִ���嵥ListView�ĵ���¼�
	 */
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		Intent intent = new Intent();
		MyAdapter mAdapter = (MyAdapter) parent.getAdapter();
		Context context = QDJCActivity.this;
		Bundle bundle = new Bundle();
		TreeNode node = (TreeNode) mAdapter.getItem(position);
		bundle.putSerializable("node", node);
		if (node.children.size() > 0) {
			if (node.getChildren().get(0).getChildren().size() > 0) {
				// ��������������
				intent.setClass(context, QDMiddleActivity.class);
			} else {
				// ����ֻ��һ��
				intent.putExtra("qyid", qyid);
				intent.setClass(context, SpecialItemActivity.class);
			}
			intent.putExtras(bundle);
			startActivityForResult(intent, 1);
		} else {
			Toast.makeText(context, "��ǰ���ݲ�����!", Toast.LENGTH_SHORT).show();
		}
		context = null;
	}

	/**
	 * FileName: QDJCActivity.java Description:�����ݵ�Adapter
	 * 
	 * @author Administrator
	 * @Version 1.3.4
	 * @Copyright �п���ͼ���¿Ƽ����޹�˾ Create at: 2012-12-4 ����10:07:41
	 */
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
			ImageView imageleft = (ImageView) views
					.findViewById(R.id.listitem_left_image);
			imageleft.setImageResource(R.drawable.icon_left_not_checked);

			// TextView Center
			TextView tv = (TextView) views.findViewById(R.id.listitem_text);
			tv.setGravity(Gravity.CENTER_VERTICAL);
			tv.setPadding(5, 5, 0, 0);
			tv.setText(currNode.title.toString());

			// ImageView Right
			ImageView imageRight = (ImageView) views
					.findViewById(R.id.listitem_image);
			imageRight.setPadding(5, 0, 0, 0);

			String textResult = "";
			if (currNode.children.size() < 1) {// Ҷ�ӽڵ�
				textResult = queryNodeResult(currNode);
				if (textResult.length() > 1) {
					textResult = FileHelper.formatTextLength(textResult, 8);
				}
			}

			if (!"".equals(textResult)) {
				String textInfo = tv.getText().toString()
						+ "<sub><font color='blue'>[" + textResult
						+ "]</font></sub>";
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

	/**
	 * Description: ��ѯ�ڵ��״̬
	 * 
	 * @param str
	 *            �����ַ���
	 * @param rwID
	 *            ������
	 * @param industryID
	 *            ��ҵ���
	 * @return �Ƿ��޸Ĺ�
	 * @author Administrator Create at: 2012-12-4 ����10:06:43
	 */
	private static boolean queryNodeState(String str, String rwID,
			String industryID) {
		ArrayList<HashMap<String, Object>> data = new ArrayList<HashMap<String, Object>>();
		String sql = "SELECT Count(SpecialItemID) AS Count1 FROM YDZF_TaskSpecialItem "
				+ "WHERE SpecialItemResult is not null "
				+ "and SpecialItemResult != '' and SpecialItemResult != 'null' "
				+ "and SpecialItemID in ("
				+ str
				+ ") "
				+ "and TaskID = '"
				+ rwID + "' and IndustryID = '" + industryID + "' limit 0,1";

		String result = "";

		data = SqliteUtil.getInstance()
				.queryBySqlReturnArrayListHashMap(sql);

		for (HashMap<String, Object> map : data) {
			result = (String) map.get("count1");
		}

		String[] _IdArray = str.split(",");

		int _Count = Integer.parseInt(result);

		if (_Count == _IdArray.length) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Description: ��ѯר��Ľ��ֵ�ı�
	 * 
	 * @param node
	 *            ��ǰ�ڵ�
	 * @return ��ǰ�ڵ�Ľ��ֵ
	 * @author Administrator Create at: 2012-12-4 ����10:06:14
	 */
	private static String queryNodeResult(TreeNode node) {
		String sqlType = "SELECT ControlType as type FROM YDZF_ResultType WHERE ID = '"
				+ node.ResultTypeID + "'";
		String sqlText = "";
		String resultText = "";
		ArrayList<HashMap<String, Object>> data = null;

		data = SqliteUtil.getInstance()
				.queryBySqlReturnArrayListHashMap(sqlType);

		Log.i(TAG, sqlType);
		if (data.size() == 0)
			return "";
		String typeStr = (String) data.get(0).get("type");
		if (typeStr != null && typeStr.contains("Text")) {
			sqlText = "SELECT SpecialItemResult AS text FROM YDZF_TaskSpecialItem WHERE TaskID ='"
					+ node.rwID
					+ "' AND EnterID = '"
					+ node.qyID
					+ "' AND IndustryID = '"
					+ node.TID
					+ "'AND SpecialItemID ='" + node.SpecialItemID + "'";
		} else if (typeStr != null && typeStr.contains("Check")) {
			sqlText = "SELECT RName AS text FROM YDZF_ResultType WHERE ID = "
					+ "(SELECT SpecialItemResult FROM YDZF_TaskSpecialItem WHERE TaskID ='"
					+ node.rwID + "' AND EnterID = '" + node.qyID
					+ "' AND IndustryID = '" + node.TID
					+ "'and SpecialItemID ='" + node.SpecialItemID + "')";
		}

		data = SqliteUtil.getInstance()
				.queryBySqlReturnArrayListHashMap(sqlText);

		if (data != null && data.size() > 0) {
			for (HashMap<String, Object> map : data) {
				resultText = (String) map.get("text");
			}
		} else {
			if (typeStr != null && typeStr.contains("Text")) {
				sqlText = "SELECT SpecialItemResult AS text FROM YDZF_TaskSpecialItem WHERE EnterID = '"
						+ node.qyID
						+ "' AND IndustryID = '"
						+ node.TID
						+ "'AND SpecialItemID ='"
						+ node.SpecialItemID
						+ "' order by UpdateTime";
			} else if (typeStr != null && typeStr.contains("Check")) {
				sqlText = "SELECT RName AS text FROM YDZF_ResultType WHERE ID = "
						+ "(SELECT SpecialItemResult FROM YDZF_TaskSpecialItem WHERE SpecialItemResult is not null and EnterID = '"
						+ node.qyID
						+ "' AND IndustryID = '"
						+ node.TID
						+ "'and SpecialItemID ='"
						+ node.SpecialItemID
						+ "' order by UpdateTime desc limit 0,1)";
			}

			data = SqliteUtil.getInstance()
					.queryBySqlReturnArrayListHashMap(sqlText);

			for (HashMap<String, Object> map : data) {
				resultText = (String) map.get("text");
			}
		}

		return resultText;
	}

	/**
	 * Description:�ݹ��ȡһ��������е�Ҷ��
	 * 
	 * @param node
	 *            ��ǰNode
	 * @author Administrator Create at: 2012-12-4 ����10:05:41
	 */
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

	/**
	 * FileName: QDJCActivity.java Description:�첽�����嵥�������
	 * 
	 * @author Administrator
	 * @Version 1.3.4
	 * @Copyright �п���ͼ���¿Ƽ����޹�˾ Create at: 2012-12-4 ����10:00:12
	 */
	public class WorkAsyncTask extends AsyncTask<String, Integer, String> {
		public WorkAsyncTask(Context context) {
			yutuLoading = new YutuLoading(QDJCActivity.this);
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
				node.rwID = RWBH;
				getRootNode(node);
			}

			for (TreeNode nodeRoot : tempRootNodeList) {
				// ��ÿһ�����ڵ�����ӽڵ�
				nodeRoot.rwID = RWBH;
				setChildNode(nodeRoot);
				// ���ø��ڵ��״̬
				// setFatherNodeState(nodeRoot);
			}

			RemoveEmptyTypeNode(tempRootNodeList);

			// ��������
			Collections.sort(tempRootNodeList);

			Log.i(TAG, "�첽�������");
			mHandler.sendEmptyMessage(GETDATA_SUCCESS);
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

	/**
	 * Description:�Ƴ��սڵ�
	 * 
	 * @param nodeList
	 * @author Administrator Create at: 2012-12-4 ����09:59:52
	 */
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

//	@Override
//	public boolean dispatchKeyEvent(KeyEvent event) {
//		Context context = QDJCActivity.this;
//		if (event.getKeyCode() == KeyEvent.KEYCODE_BACK
//				&& event.getAction() == KeyEvent.ACTION_DOWN) {
//			// �жϵ�ǰ����״̬,��������Ѿ��ϴ���������ʾ����������������Լ���������ʾ
//			if (!RWXX.RWZT_UPLOAD.equals(rwxx.queryTaskStatus(RWBH))
//					&& rwxx.JudgeUserName(RWBH)) {
//				AlertDialog.Builder ab = new AlertDialog.Builder(context);
//				ab.setTitle("��ʾ");
//				TextView view = new TextView(context);
//				view.setText("[�ֳ�ִ��]û���ϴ�������Ƿ��˳�?");
//				ab.setView(view);
//				ab.setIcon(getResources().getDrawable(
//						R.drawable.icon_mapuni_white));
//				ab.setNegativeButton("ȡ��",
//						new DialogInterface.OnClickListener() {
//							@Override
//							public void onClick(DialogInterface dialog,
//									int which) {
//
//							}
//						});
//				ab.setPositiveButton("ȷ��",
//						new DialogInterface.OnClickListener() {
//
//							@Override
//							public void onClick(DialogInterface dialog,
//									int which) {
//								finish();
//							}
//						});
//				ab.show();
//			}
//		}
//		return super.dispatchKeyEvent(event);
//	}

	@Override
	protected void onStop() {
		if (yutuLoading != null)
			yutuLoading.dismissDialog();
		super.onStop();
	}

	@Override
	protected void onResume() {
		if (detail != null && !detail.isEnabled())
			detail.setEnabled(true);
		super.onResume();
	}

	@Override
	public void addTitle() {
		SetBaseStyle(relativeLayout, "�嵥���");

	}

	@Override
	public void addBottom() {
		// TODO Auto-generated method stub

	}

}
