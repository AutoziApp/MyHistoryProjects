package com.mapuni.android.enforcement;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mapuni.android.MobileEnforcement.R;
import com.mapuni.android.base.BaseActivity;
import com.mapuni.android.base.Global;
import com.mapuni.android.business.RWXX;
import com.mapuni.android.business.TreeNode;
import com.mapuni.android.dataprovider.SQLiteDataProvider;
import com.mapuni.android.dataprovider.SqliteUtil;
import com.mapuni.android.enforcement.QdjcnlActivity.MyAdapter;

/**
 * FileName: QDMiddleActivity.java Description:�����м���תҳ��
 * 
 * @author liusy
 * @Version 1.3.4
 * @Copyright �п���ͼ���¿Ƽ����޹�˾ Create at: 2012-12-4 ����10:14:01
 */
public class QDMiddleActivity extends BaseActivity {
	private Intent intent;
	private TreeNode frontdata;
	private RelativeLayout rl;
	private LinearLayout middleLayout;
	private ListView lv;
	private LayoutInflater li;
	private MyAdapter adapter;

	public final String TAG = "JContentActivity";
	public final String RESULT_TABLE = "YDZF_TaskSpecialItem";

	private boolean isTaskStart = false;
	private String qyid;
	private String rwbh;
	private String tname;
	private String tid;
	private boolean isSearch;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.ui_mapuni);

		// ��ȡǰҳ���ݵ���Ϣ
		intent = getIntent();
		qyid = intent.getStringExtra("qyid");
		isSearch = intent.getBooleanExtra("isSearch", false);
		rwbh = intent.getStringExtra("rwbh");
	  tname = intent.getStringExtra("tname");
	 tid = intent.getStringExtra("tid");
		frontdata = (TreeNode) intent.getExtras().get("node");// ���ǰһҳ�洫��Node����

		li = LayoutInflater.from(this);
		rl = (RelativeLayout) findViewById(R.id.parentLayout);
		// ��Ҷ�ӽ��
		if (frontdata.getChildren() != null) {
			// �趨��������
			Collections.sort(frontdata.getChildren());
			String a = frontdata.title;
			String b = a.substring(0, a.length() > 13 ? 13 : a.length());
			SetBaseStyle(rl, b + "...");
		}

		middleLayout = (LinearLayout) findViewById(R.id.middleLayout);
		setTitleLayoutVisiable(true);

		lv = new ListView(this);
		lv.setCacheColorHint(Color.TRANSPARENT);
		middleLayout.addView(lv, new LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.FILL_PARENT));
		adapter = new QdjcnlActivity.MyAdapter(frontdata.getChildren(), this);
		lv.setAdapter(adapter);
		lv.setDivider(getResources().getDrawable(R.drawable.list_divider));
		lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(final AdapterView<?> parent, View view, final int position, long id) {
				final MyAdapter aa = (MyAdapter) parent.getAdapter();
				final TreeNode currentNode = (TreeNode) aa.getItem(position);

				// ʹ����Ч��
				if (Global.getGlobalInstance().useNewStyleBoolean) {
					if (currentNode.getChildren().get(0).getChildren().size() > 0) {
						// ������ӽڵ�����ת
						Intent next = new Intent(QDMiddleActivity.this, QDMiddleActivity.class);
						Bundle bundle = new Bundle();
						bundle.putSerializable("node", (TreeNode) aa.getItem(position));
						next.putExtras(bundle);
						next.putExtra("qyid", qyid);
						startActivityForResult(next, position);
					} else {
						Intent next = new Intent(QDMiddleActivity.this, SpecialItemActivity.class);
						Bundle bundle = new Bundle();
						bundle.putSerializable("node", (TreeNode) aa.getItem(position));
						next.putExtras(bundle);
						next.putExtra("qyid", qyid);
						startActivityForResult(next, position);
					}
				} else {
					// ʹ��ԭ��Ч��
					if (currentNode.getChildren().size() > 0) {

						// ������ӽڵ�����ת
						Intent next = new Intent(QDMiddleActivity.this, QDMiddleActivity.class);
						Bundle bundle = new Bundle();
						bundle.putSerializable("node", (TreeNode) aa.getItem(position));
						next.putExtras(bundle);
						startActivityForResult(next, position);
						Toast.makeText(QDMiddleActivity.this, "���������У����Ժ󡭡�", Toast.LENGTH_SHORT).show();

					} else {
						// Ҷ�ӽڵ㲻��ת�������Ի���
						// ��ȡ��Ϣ
						final String rwID = currentNode.rwID;
						final String qyID = currentNode.qyID;
						final String industryID = currentNode.TID;
						final String specialItemID = currentNode.SpecialItemID;
						final String resultTypeID = currentNode.ResultTypeID;
						final Context context = QDMiddleActivity.this;
						final String remark = currentNode.RemarkTip;
						final String surveyTime = Global.getGlobalInstance().getDate();

						// ��ȡ�������
						// �ǡ���[һ�ࡢ���ࡢ���ࡢ����]
						final ArrayList<HashMap<String, Object>> result = getResultTypeData(resultTypeID);
						LastResult result0 = new LastResult(rwID, qyID, industryID, specialItemID);
						// �Խ�����м���
						final LastResult lastResult = getLastResult(rwID, specialItemID, result0);
						Log.i(TAG, "�������ID�� " + currentNode.ResultTypeID + "-->��ȡ�����:" + result.size());

						ArrayList<String> resultList = new ArrayList<String>();
						final HashMap<String, String> resultIdMap = new HashMap<String, String>();
						String contrlType = "";
						for (HashMap<String, Object> map : result) {
							contrlType = (String) map.get("controltype");
							Log.i(TAG, "������� : " + contrlType);

							resultList.add((String) map.get("rname"));
							resultIdMap.put((String) map.get("rname"), (String) map.get("id"));
						}

						//
						// ��ʾ
						//
						LayoutInflater mInflater = LayoutInflater.from(context);

						// ==================��ѡ��ť===================================================
						if (contrlType != null && contrlType.contains("Check")) {
							// ��ѡ��ť
							Log.i(TAG, "��ʾ��ѡ��ť");
							// �Ի���Ĳ���
							AlertDialog.Builder ab = new AlertDialog.Builder(context);
							LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
							// ͨ�����ּ����������ضԻ���Ĳ���
							final LinearLayout outLayout = (LinearLayout) mInflater.inflate(R.layout.checkgroup, null);
							RadioGroup radioGroup = new RadioGroup(context);
							radioGroup.setLayoutParams(params);
							radioGroup.setGravity(Gravity.LEFT);

							// ��ȡ�ϴν�����ı���Ϣ
							String lastText = lastResult.Text;
							Log.i(TAG, "�ϴν���ı���Ϣ��" + lastText);

							// �������ϻ�ȡ��ʾ��Ϣ
							// ��ʼ���ؼ�
							for (String text : resultList) {
								RadioButton radioButton = new RadioButton(context);
								radioButton.setText(text);
								// �������ݿ�����Ϣ
								if (text.equals(lastText)) {
									radioButton.setChecked(true);
									// radioButton
								}
								if (lastText == null) {
									// hasValue = false;
								}
								radioButton.setPadding(70, 5, 5, 5);
								radioGroup.addView(radioButton);
							}

							outLayout.addView(radioGroup);

							ab.setView(outLayout);
							ab.setTitle("��ѡ��");
							ab.setIcon(R.drawable.icon_mapuni_white);
							final AlertDialog dialog = ab.create();
							dialog.setOnDismissListener(new MyDialogDismiss());

							// �ı���
							TextView noteTextView = new TextView(context);
							String remarkStr = "��ע��";
							if (!"".equals(remark)) {
								remarkStr = remark;
							}
							noteTextView.setText(remarkStr);
							noteTextView.setWidth(200);
							noteTextView.setPadding(5, 5, 5, 5);

							// �༭��
							final EditText editText = new EditText(context);
							editText.setWidth(200);
							editText.setText(lastResult.Remark);
							editText.setFilters(new InputFilter[] { new InputFilter.LengthFilter(180) });

							// ��ӵ�����
							outLayout.addView(noteTextView);
							outLayout.addView(editText);

							/*
							 * if(!"".equals(remark)){ Log.i(TAG, "��ע��" +
							 * remark); //��ע�ı� TextView noteTextView = new
							 * TextView(context); noteTextView.setText(remark);
							 * noteTextView.setWidth(200);
							 * noteTextView.setPadding(5, 5, 5, 5);
							 * 
							 * //�༭���� editText.setWidth(200);
							 * editText.setText(lastResult.Remark);
							 * editText.setFilters(new InputFilter[] { new
							 * InputFilter.LengthFilter(180) });
							 * 
							 * 
							 * //Button putButton = new Button(context);
							 * //putButton.setText(android.R.string.ok);
							 * 
							 * //��ӵ����� outLayout.addView(noteTextView);
							 * outLayout.addView(editText);
							 * //outLayout.addView(putButton);
							 * 
							 * }
							 */

							// ����
							// ��ѡ��ť�¼�����
							radioGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {
								@Override
								public void onCheckedChanged(RadioGroup group, int checkedId) {

									/*
									 * mHandler = new Handler(){ public void
									 * handleMessage(android.os.Message msg) {
									 * if(msg.what == 1){
									 * 
									 * } }; };
									 */

									// ��¼��ǰ�û�ѡ��
									currentNode.state = true;
									aa.notifyDataSetChanged();
									dialog.cancel();

									RadioButton rb = (RadioButton) outLayout.findViewById(group.getCheckedRadioButtonId());
									String rbtext = rb.getText().toString();// ȡ�ñ�ѡ���RadioButton�е��ı�����

									String resultTypeId = "";

									for (String text : resultIdMap.keySet()) {
										if (text.equals(rbtext)) {
											resultTypeId = resultIdMap.get(text);
										}
									}

									Log.i(TAG, "��ȡ���ID��" + resultTypeId);

									// ��ȡ���
									String remarkStr = editText.getText().toString().trim();
									if (!"".equals(remarkStr)) {
										currentNode.state = true;
									}

									// �����ݿ��б�������
									saveDataToDB(lastResult, resultTypeId, remarkStr, surveyTime);

									// ���һ�������򷵻���һҳ
									if (position == (((MyAdapter) parent.getAdapter()).getCount() - 1)) {
										// (((Node)((Adapter)parent.getAdapter()).getItem(position)).getParent()).setState(true);
										Log.i("TAG", ((TreeNode) ((MyAdapter) parent.getAdapter()).getItem(position)).getParent().title);
										setResult(1);
										QDMiddleActivity.this.finish();
										// ((Adapter)parent.getAdapter()).notifyDataSetChanged();
									}
								}

							});
							dialog.show();

							// =======================�����ı���==========================================
						} else if (contrlType != null && contrlType.contains("Text")) {
							// �����ı���
							Log.i(TAG, "��ʾ�����ı���");
							AlertDialog.Builder ab = new AlertDialog.Builder(context);
							LayoutParams params = new LayoutParams(250, LayoutParams.WRAP_CONTENT);
							// ͨ�����ּ����������ضԻ���Ĳ���
							final LinearLayout outLayout = (LinearLayout) mInflater.inflate(R.layout.checkgroup, null);
							outLayout.setLayoutParams(params);

							final EditText editTextView = new EditText(context);
							editTextView.setWidth(250);

							String textContent = lastResult.SpecialItemResult;

							if (textContent == null) {

							}
							if (!"".equals(textContent)) {
								editTextView.setText(textContent);
							}

							Button putButton = new Button(context);
							putButton.setText(android.R.string.ok);
							putButton.setWidth(200);

							// outLayout.addView(noteTextView);
							outLayout.addView(editTextView);
							outLayout.addView(putButton);

							ab.setView(outLayout);
							ab.setTitle("�����룺");
							ab.setIcon(R.drawable.icon_mapuni_white);
							ab.create();
							final AlertDialog dialog = ab.create();
							dialog.setOnDismissListener(new MyDialogDismiss());

							putButton.setOnClickListener(new View.OnClickListener() {

								@Override
								public void onClick(View v) {
									// �û�������ɣ��������
									dialog.cancel();

									String specialItemResult = editTextView.getText().toString().trim();

									// ȷ���û�������Ϣ֮��ı�״̬
									if (!"".equals(specialItemResult)) {
										currentNode.state = true;
										aa.notifyDataSetChanged();

										String remarkStr = "";
										// �����ݿ��б�������
										saveDataToDB(lastResult, specialItemResult, remarkStr, surveyTime);

									}

									// ���һ�������򷵻���һҳ
									if (position == (((MyAdapter) parent.getAdapter()).getCount() - 1)) {
										// (((Node)((Adapter)parent.getAdapter()).getItem(position)).getParent()).setState(true);
										Log.i("TAG", ((TreeNode) ((MyAdapter) parent.getAdapter()).getItem(position)).getParent().title);

										QDMiddleActivity.this.finish();
										// ((Adapter)parent.getAdapter()).notifyDataSetChanged();
									}
								}

							});
							dialog.show();
						}

					}
				}
			}
		});
		// Intent intent = new Intent();

		// Bundle bundle = new Bundle();
		// bundle.putSerializable("node", frontdata);
		// intent.putExtras(bundle);

		// setResult(1,intent);
	}

	/**
	 * ֪ͨ�б����ݸı�
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		// if (resultCode == 1) {
		((TreeNode) adapter.getItem(requestCode)).setState(true);

		adapter.notifyDataSetChanged();
		Log.i(TAG, "����ҳ��");
		// }
	}

	/**
	 * Description: �����������浽���ݿ�
	 * 
	 * @param rwID
	 *            ������
	 * @param qyID
	 *            ��ҵ���
	 * @param industryID
	 *            ��ҵ��ģ�壩���
	 * @param specialItemID
	 *            ר����
	 * @param specialItemResult
	 *            ������ͱ��
	 * @param remark
	 *            ��ע��Ϣ
	 * @param surveyTime
	 *            ����ʱ��
	 * @return �����Ƿ�ɹ�
	 * @author Administrator Create at: 2012-12-4 ����10:15:25
	 */
	private boolean insertTaskResult(String rwID, String qyID, String industryID, String specialItemID, String specialItemResult, String remark, String surveyTime) {

		if (TextUtils.isEmpty(industryID))
			industryID = "1";
		String sql = "insert into YDZF_TaskSpecialItem(TaskID,EnterID,IndustryID,SpecialItemID,SpecialItemResult,Remark,SurveyTime) " + "values('" + rwID + "','" + qyID + "','"
				+ industryID + "','" + specialItemID + "','" + specialItemResult + "','" + remark + "','" + surveyTime + "')";
		System.out.println(sql);
		ArrayList<HashMap<String,Object>> qdData = getQDData();
		//�ж��Ƿ���ӹ�
		if (qdData.size()>0) {
			
		}else{
			new RWXX().addBL2(tname, rwID, qyID,tid);
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
	 * Description:��ѯ���ݿ� <li>��ȡ�������ͣ��ı�����ѡ��
	 * 
	 * @param resultTypeID
	 * @return �������ID
	 * @author Administrator Create at: 2012-12-4 ����10:16:54
	 */
	private ArrayList<HashMap<String, Object>> getResultTypeData(String resultTypeID) {
		ArrayList<HashMap<String, Object>> data = new ArrayList<HashMap<String, Object>>();
		String sql = "";
		sql = "select ID,RName,ControlType from YDZF_ResultType where PID = '" + resultTypeID + "' order by sortIndex ";

		data = SQLiteDataProvider.getInstance().queryBySqlReturnArrayListHashMap(sql);

		if (data.size() == 0) {
			sql = "select RName,ControlType from YDZF_ResultType where ID = '" + resultTypeID + "' order by sortIndex ";

			data = SQLiteDataProvider.getInstance().queryBySqlReturnArrayListHashMap(sql);

			return data;
		}
		return data;
	}

	/**
	 * Description: ��ѯ���ݿ� <li>ͨ������ID��ר��ID��ȡ���ֵ�ı�
	 * 
	 * @param rwID
	 *            ������
	 * @param specialItemID
	 *            ר����
	 * @return �ϴ�ѡ����Ϣ���ı�
	 * @author Administrator Create at: 2012-12-4 ����10:17:38
	 */
	private String getLastTaskResultText(String rwID, String specialItemID) {
		String text = "";
		ArrayList<HashMap<String, Object>> data = new ArrayList<HashMap<String, Object>>();
		String sql = "SELECT RName FROM YDZF_ResultType WHERE ID = " + "(SELECT SpecialItemResult FROM YDZF_TaskSpecialItem WHERE TaskID ='" + rwID + "' and SpecialItemID ='"
				+ specialItemID + "')";
		Log.i(TAG, sql);

		data = SQLiteDataProvider.getInstance().queryBySqlReturnArrayListHashMap(sql);

		for (HashMap<String, Object> map : data) {
			text = (String) map.get("rname");
		}
		return text;
	}

	/**
	 * Description:��ѯ���ݿ� <li>ͨ������ID��ר��ID����ȡ�ϴα���Ľ������
	 * 
	 * @param rwID
	 *            ������
	 * @param specialItemID
	 *            ר����
	 * @param lastResult
	 *            �������
	 * @return
	 * @author Administrator Create at: 2012-12-4 ����10:19:19
	 */
	private LastResult getLastResult(String rwID, String specialItemID, LastResult lastResult) {
		ArrayList<HashMap<String, Object>> data = new ArrayList<HashMap<String, Object>>();
		String sql = "SELECT * FROM YDZF_TaskSpecialItem WHERE TaskID ='" + rwID + "' and IndustryID ='" + lastResult.IndustryID + "' and SpecialItemID ='" + specialItemID + "'";
		Log.i(TAG, sql);

		data = SQLiteDataProvider.getInstance().queryBySqlReturnArrayListHashMap(sql);

		if (data.size() > 0) {
			for (HashMap<String, Object> map : data) {
				lastResult.SpecialItemResult = (String) map.get("specialitemresult");
				lastResult.Remark = (String) map.get("remark");
			}
			// �ϴ�ѡ����Ϣ���ı�
			lastResult.Text = getLastTaskResultText(rwID, specialItemID);
			lastResult.flag = true;
		} else {
			lastResult.flag = false;
		}
		return lastResult;
	}

	/**
	 * FileName: QDMiddleActivity.java Description:�Ի�����ʧ����,�����û���Ϣ
	 * 
	 * @author Administrator
	 * @Version 1.3.4
	 * @Copyright �п���ͼ���¿Ƽ����޹�˾ Create at: 2012-12-4 ����10:20:28
	 */
	private final class MyDialogDismiss implements DialogInterface.OnDismissListener {

		@Override
		public void onDismiss(DialogInterface dialog) {
			Log.i(TAG, "AlertDialog Dismiss !");
			// �����û�������Ϣ
			// ����ɹ���˾��ʾ
		}
	}

	/**
	 * Description:��������
	 * 
	 * @param lastResult
	 *            ����������
	 * @param result
	 *            �������
	 * @param remark
	 *            ��ע��Ϣ�ı�
	 * @param surveyTime
	 *            ����ʱ��
	 * @author Administrator Create at: 2012-12-4 ����10:20:56
	 */
	private void saveDataToDB(final LastResult lastResult, final String result, final String remark, final String surveyTime) {
		// �첽�̲߳�����
		/*
		 * final String rwID = lastResult.TaskID; final String qyID =
		 * lastResult.EnterID; final String industryID = lastResult.IndustryID;
		 * final String specialItemID = lastResult.SpecialItemID;
		 * 
		 * //����ȫ��ִ�и��� //hasValue = true;
		 * 
		 * new Thread(){ public void run() { //String note = "";
		 * if(lastResult.flag) { //�Ѿ�������Ϣ������ //�������ݵ����ݿ��� ContentValues
		 * contentValues = new ContentValues();
		 * contentValues.put("SpecialItemResult", result);
		 * contentValues.put("Remark", remark); contentValues.put("SurveyTime",
		 * surveyTime); Log.i(TAG, "Update !");
		 * if(SQLiteHelper.getInstance().updateTable(RESULT_TABLE,
		 * contentValues, " TaskID = ? and SpecialItemID = ?", new
		 * String[]{rwID, specialItemID})){ Log.i(TAG, "Update Success"); }
		 * //note = "������ɣ�"; } else {
		 * 
		 * //û�б�����Ϣ������ Log.i(TAG, "Insert !"); if(insertTaskResult(rwID, qyID,
		 * industryID, specialItemID, result, remark, surveyTime)){ Log.i(TAG,
		 * "Insert Success"); } //note = "������ɣ�"; } try { Thread.sleep(500);�� }
		 * catch (InterruptedException e) { e.printStackTrace(); }
		 * mHandler.sendEmptyMessage(1); }; }.start();
		 */

		// Toast.makeText(JContentActivity.this, "�������",
		// Toast.LENGTH_SHORT).show();

		String rwID = lastResult.TaskID;
		String qyID = lastResult.EnterID;
		String industryID = lastResult.IndustryID;
		String specialItemID = lastResult.SpecialItemID;

		// String note = "";
		if (lastResult.flag) {
			// �Ѿ�������Ϣ������
			// �������ݵ����ݿ���
			ContentValues contentValues = new ContentValues();
			contentValues.put("SpecialItemResult", result);
			contentValues.put("Remark", remark);
			contentValues.put("SurveyTime", surveyTime);
			Log.i(TAG, "Update !");
			if (SQLiteDataProvider.getInstance().updateTable(RESULT_TABLE, contentValues, " TaskID = ? and SpecialItemID = ?", new String[] { rwID, specialItemID })) {
				Log.i(TAG, "Update Success");
			}
			// note = "������ɣ�";
		} else {

			// û�б�����Ϣ������
			Log.i(TAG, "Insert !");
			if (insertTaskResult(rwID, qyID, industryID, specialItemID, result, remark, surveyTime)) {
				Log.i(TAG, "Insert Success");
			}
			// note = "������ɣ�";
		}
		// Toast.makeText(JContentActivity.this, note,
		// Toast.LENGTH_SHORT).show();
		Log.i(TAG, "��Ϣ��" + rwID + "->" + qyID + "->" + industryID + "->" + specialItemID + "->" + result + "->" + remark + "->" + surveyTime);

		// �ı�����״̬Ϊִ����
		if (!isTaskStart) {
			Log.i(TAG, "�ı�״̬ " + rwID + " ---->ִ����");
			isTaskStart = true;
			// new RWXX().changeTaskState(rwID,
			// RWXX.RWZT_WATE_EXECUTION,qyID,RWXX.RWZT_ON_EXECUTION);
		}
	}

	/**
	 * FileName: QDMiddleActivity.java Description:�ϴν���Ķ���ķ�װ
	 * 
	 * @author Administrator
	 * @Version 1.3.4
	 * @Copyright �п���ͼ���¿Ƽ����޹�˾ Create at: 2012-12-4 ����10:38:54
	 */
	public final class LastResult {
		public String TaskID; // ������
		public String EnterID; // ��ҵ���
		public String IndustryID; // ��ҵ���
		public String SpecialItemID; // ר����
		public String SpecialItemResult; // ������ͱ�Ż����ı�
		public String Text; // ��������ı�
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

}
