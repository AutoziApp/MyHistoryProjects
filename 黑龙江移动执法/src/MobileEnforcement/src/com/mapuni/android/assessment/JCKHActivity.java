package com.mapuni.android.assessment;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteException;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mapuni.android.MobileEnforcement.R;
import com.mapuni.android.base.BaseActivity;
import com.mapuni.android.base.Global;
import com.mapuni.android.base.business.BaseClass;
import com.mapuni.android.base.util.DisplayUitl;
import com.mapuni.android.base.util.LogUtil;
import com.mapuni.android.business.JCKHXX;
import com.mapuni.android.dataprovider.DESSecurity;
import com.mapuni.android.dataprovider.SqliteUtil;
import com.mapuni.android.netprovider.Net;
import com.mapuni.android.netprovider.WebServiceProvider;
import com.mapuni.android.uitl.NotificationOperator;

/**
 * FileName: JCKHActivity.java Description:���鿼��
 * 
 * @author ������
 * @Version 1.3.4
 * @Copyright �п���ͼ���¿Ƽ����޹�˾ Create at: 2012-12-4 ����4:27:44
 */
public class JCKHActivity extends BaseActivity {
	AutoCompleteTextView autotextview1, autotextview2;
	ExpandableListView expandview;
	RadioGroup radioGroup;
	RadioButton radioButton1;
	RadioButton radioButton2;
	LinearLayout linyout1;// ѡ���ϼ���λ����
	ImageView imageView;// ��listView���ͼ��
	ImageButton jckh_search_duiwu;

	private final int HAVEDATA = 0;// ������
	private final int NODATA = 1;// ������
	private final int NOCONNECTION = 2;// ���粻ͨ
	private final int DOWLOADSUCCESS = 3;// ���سɹ�
	private final int FJURLERR = 4;// �����������ӳ���
	private final int UPSUCCESS = 5;// �ϴ��ɹ�
	private final int UPFAILED = 6;// �ϴ�ʧ��

	private ProgressDialog pd;

	private final String TABNAME = "PC_DepartmentInfo";
	private final String PARENTCODE = "dept1A";// ʡ������ID
	private final String TableName = "CheckTable";
	private final String PATH = Global.getGlobalInstance().getSystemurl()
			+ "/MobileEnforcement/Attach/JCKH/";
	private String NOWYEAR;

	private ArrayList<HashMap<String, Object>> mlist;
	private ArrayList<HashMap<String, Object>> citymlist;// �������������
	private ArrayList<HashMap<String, Object>> countrymlist;// �������������

	private final List<String> groupArray = new ArrayList<String>(); // ��ŵ��е�λExpandableListView�ĸ��ڵ�����
	private List<String> adapterArray = new ArrayList<String>();// ��adapter������

	private final List<String> countygroupArray = new ArrayList<String>(); // ����ؼ���λExpandableListView�ĸ��ڵ�����
	private final List<List<String>> childArray = new ArrayList<List<String>>(); // ���ExpandableListView���ӽڵ�����
	private final Calendar calendar = Calendar.getInstance();
	private final SimpleDateFormat fmtDate = new SimpleDateFormat("yyyy-MM-dd");

	private Message msg;
	File f, fil;
	private final String NOWTIME = fmtDate.format(calendar.getTime())
			.substring(0, 4);
	ExpandableAdapter adapter;
	private final Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case UPSUCCESS: // �ϴ��ɹ�
				pd.cancel();
				Toast.makeText(getApplicationContext(), "�ϴ��ɹ�",
						Toast.LENGTH_LONG).show();
				break;
			case UPFAILED: // �ϴ�ʧ��
				pd.cancel();
				Toast.makeText(getApplicationContext(), "�ϴ�ʧ��",
						Toast.LENGTH_LONG).show();
				break;
			case FJURLERR: // ������ַ����
				pd.cancel();
				Toast.makeText(getApplicationContext(), "������ַ����",
						Toast.LENGTH_LONG).show();
				break;
			case HAVEDATA: // ���ݿ�������
				pd.cancel();
				// ���ݿ�������radioButton1�����
				radioButton1.setChecked(true);

				break;
			case NODATA: // ���ݿ�������
				if (pd != null) {
					pd.cancel();
				}
				Toast.makeText(getApplicationContext(), "���ݿ�������",
						Toast.LENGTH_LONG).show();
				break;
			case NOCONNECTION: // ��������ʧ��
				Toast.makeText(getApplicationContext(), "��������ʧ��",
						Toast.LENGTH_LONG).show();
				break;

			case DOWLOADSUCCESS:// ���سɹ�
				if (pd != null) {
					pd.cancel();
				}
				String path = (String) msg.obj;
				DisplayUitl.openfile(path, JCKHActivity.this);
				break;
			/*
			 * case NOTFIY: adapterArray=(List<String>) msg.obj;
			 * adapter.notifyDataSetChanged();
			 */
			}

		}
	};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.jckh);
		RelativeLayout relyout = (RelativeLayout) findViewById(R.id.jckh_relyout);
		SetBaseStyle(relyout, "���鿼��");
		setTitleLayoutVisiable(true);
		// ʱ��ѡ��
		autotextview1 = (AutoCompleteTextView) this
				.findViewById(R.id.jckh_edit_account);
		autotextview2 = (AutoCompleteTextView) this
				.findViewById(R.id.jckh_edit_sjdanwei);
		// String nowtime=fmtDate.format(calendar.getTime()).substring(0, 4);
		autotextview1.setText(NOWTIME);

		List<String> mdata = new ArrayList<String>();
		int i = Integer.valueOf(NOWTIME) - 2012;
		if (i > 0) {
			for (int a = 0; a <= i; a++) {
				mdata.add(String.valueOf(2012 + a));
			}
			autotextview1.setAdapter(new ArrayAdapter<String>(this,
					android.R.layout.simple_dropdown_item_1line, mdata));
			autotextview1.setFocusable(false);

		} else {
			mdata.add("2012");
			autotextview1.setAdapter(new ArrayAdapter<String>(this,
					android.R.layout.simple_dropdown_item_1line, mdata));
			autotextview1.setFocusable(false);
		}
		expandview = (ExpandableListView) this
				.findViewById(R.id.jckh_expandview);
		expandview.setGroupIndicator(getResources().getDrawable(
				R.layout.expandablelistviewselector));
		linyout1 = (LinearLayout) findViewById(R.id.jckh_search_linyout);
		radioGroup = (RadioGroup) this.findViewById(R.id.jckh_radiogroup);
		radioButton1 = (RadioButton) this.findViewById(R.id.jckh_city);
		radioButton2 = (RadioButton) this.findViewById(R.id.jckh_county);

		radioGroup
				.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(RadioGroup group, int checkedId) {
						if (checkedId == radioButton1.getId()) {// �����󶨵��е�λ������
							linyout1.setVisibility(View.GONE);
							adapterArray = groupArray;
							mlist = citymlist;
							adapter = new ExpandableAdapter(JCKHActivity.this,
									adapterArray, childArray);
							expandview.setAdapter(adapter);
							expandview
									.setOnChildClickListener(new OnChildClickListener() {

										@Override
										public boolean onChildClick(
												ExpandableListView parent,
												View v,
												final int groupPosition,
												final int childPosition, long id) {

											ArrayList<HashMap<String, Object>> mdata = new ArrayList<HashMap<String, Object>>();
											HashMap<String, Object> conditions = new HashMap<String, Object>();
											conditions.put("Guid",
													childPosition + "");
											mdata = BaseClass.DBHelper.getList(
													TableName, conditions);
											if (mdata.size() == 0) {
												Toast.makeText(
														JCKHActivity.this,
														"���ݿ���������",
														Toast.LENGTH_LONG)
														.show();
												return false;
											}
											// �����ݿ�õ��ļ�����
											final String uriStr = mdata.get(0)
													.get("checktablename")
													.toString()
													.replace("\n", "");

											String groupID = getParentID(adapterArray
													.get(groupPosition));
											String filepath = "/sdcard/mapuni/MobileEnforcement/fj/"
													+ groupID
													+ autotextview1.getText()
															.toString()
													+ "/"
													+ uriStr;
											File file = new File(filepath);
											if (file.exists()) {// ����������ھʹ�û�о�����
												DisplayUitl.openfile(filepath,
														JCKHActivity.this);
												return true;
											}
											/*
											 * Intent intent=new Intent();
											 * intent
											 * .setClass(getApplicationContext
											 * (), JCKHchildActivity.class);
											 * intent.putExtra("id",
											 * childPosition+1);
											 * startActivity(intent);
											 */
											if (!Net.checkNet(JCKHActivity.this)) {
												Toast.makeText(
														JCKHActivity.this,
														"���粻ͨ������������ã�", 1)
														.show();
												return false;
											}
											pd = new ProgressDialog(
													JCKHActivity.this);
											pd.setMessage("�������ظ������Ժ򡭡�");
											pd.show();

											new Thread(new Runnable() {// �ӷ��������ظ���

														@Override
														public void run() {

															try {
																String urlstr = PATH
																		+ java.net.URLEncoder
																				.encode(uriStr,
																						"UTF-8");
																URL url = new URL(
																		urlstr);
																HttpURLConnection con = (HttpURLConnection) url
																		.openConnection();
																/*
																 * if(!(con.
																 * getResponseCode
																 * ()==200)){//
																 * �ж����ӷ��ش���
																 * msg=handler
																 * .obtainMessage
																 * (); msg.what=
																 * NOCONNECTION;
																 * handler
																 * .sendMessage
																 * (msg); }
																 */
																InputStream in = con
																		.getInputStream();
																FileOutputStream fos = null;
																if (in != null) {

																	String str = adapterArray
																			.get(groupPosition);
																	String groupID = getParentID(str);
																	f = new File(
																			"/sdcard/mapuni/MobileEnforcement/fj/"
																					+ groupID
																					+ autotextview1
																							.getText()
																							.toString());// ���û��Ŀ¼�Ƚ���Ŀ¼
																	if (!f.exists())
																		f.mkdirs();
																	String abosPath = "/sdcard/mapuni/MobileEnforcement/fj/"
																			+ groupID
																			+ autotextview1
																					.getText()
																					.toString()
																			+ "/"
																			+ uriStr;
																	fil = new File(
																			abosPath);// ��Ŀ¼֮���ļ�

																	fos = new FileOutputStream(
																			fil);

																	byte[] bytes = new byte[1024];
																	int flag = -1;
																	int count = 0;// �ļ����ֽڳ���
																	while ((flag = in
																			.read(bytes)) != -1) {// ��δ�����ļ�ĩβ��һֱ��ȡ
																		fos.write(
																				bytes,
																				0,
																				flag);
																		count += flag;

																	}
																	fos.flush();
																	fos.close();

																	LogUtil.v("wang",
																			"���ظ���   "
																					+ uriStr
																					+ " �ɹ�");
																	msg = handler
																			.obtainMessage();
																	msg.what = DOWLOADSUCCESS;
																	msg.obj = abosPath;
																	handler.sendMessage(msg);

																}

															} catch (MalformedURLException e) {
																msg = handler
																		.obtainMessage();
																msg.what = FJURLERR;
																handler.sendMessage(msg);
																e.printStackTrace();
															} catch (IOException e) {
																msg = handler
																		.obtainMessage();
																msg.what = FJURLERR;
																handler.sendMessage(msg);
																e.printStackTrace();
															}

														}
													}).start();
											return false;
										}

									});

						} else if (checkedId == radioButton2.getId()) {

							linyout1.setVisibility(View.VISIBLE);

							adapter = new ExpandableAdapter(JCKHActivity.this,
									new ArrayList<String>(), childArray);
							expandview.setAdapter(adapter);
							autotextview2
									.setAdapter(new ArrayAdapter<String>(
											JCKHActivity.this,
											android.R.layout.simple_dropdown_item_1line,
											groupArray));
							jckh_search_duiwu = (ImageButton) findViewById(R.id.jckh_search_duiwu);
							jckh_search_duiwu
									.setOnClickListener(new OnClickListener() {

										@Override
										public void onClick(View v) {
											autotextview2.showDropDown();
										}
									});
							autotextview2
									.setOnItemClickListener(new AdapterView.OnItemClickListener() {

										@Override
										public void onItemClick(
												AdapterView<?> parent,
												View view, int position, long id) {

											String content = autotextview2
													.getText().toString();
											String sql = "select * from PC_DepartmentInfo a inner join PC_DepartmentInfo "
													+ "b on a.[DepID]=b.DepParentID where a.[DepName]='"
													+ content
													+ "' order by DepName ASC";
											ArrayList<HashMap<String, Object>> list;
											try {
												mlist = SqliteUtil
														.getInstance()
														.queryBySqlReturnArrayListHashMap(
																sql);

												if (mlist != null
														&& mlist.size() > 0) {
													for (HashMap<String, Object> map : mlist) {
														countygroupArray
																.add(map.get(
																		"depname")
																		.toString());
													}
													adapterArray = countygroupArray;
													adapter = new ExpandableAdapter(
															JCKHActivity.this,
															adapterArray,
															childArray);
													expandview
															.setAdapter(adapter);
												} else {
													adapter = new ExpandableAdapter(
															JCKHActivity.this,
															new ArrayList<String>(),
															new ArrayList<List<String>>());
													expandview
															.setAdapter(adapter);
												}

											} catch (SQLiteException e) {
												e.printStackTrace();
											}

										}
									});

						}
					}
				});

		ImageButton imagebutton = (ImageButton) this
				.findViewById(R.id.jckh_search_time);
		imagebutton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				autotextview1.showDropDown();
			}
		});
		pd = new ProgressDialog(this);
		pd.setMessage("�����С���");
		pd.show();
		// ��ѯ���ݿ�
		new Thread(new Runnable() {

			@Override
			public void run() {

				HashMap<String, Object> condition = new HashMap<String, Object>();
				condition.put("DepParentID", PARENTCODE);

				mlist = BaseClass.DBHelper.getList(TABNAME, condition);
				citymlist = mlist;
				if (mlist != null && mlist.size() > 0) {
					for (HashMap<String, Object> map : mlist) {
						groupArray.add(map.get("depname").toString());

					}
					List<String> tempArray = new ArrayList<String>();
					tempArray.add("�������������������������ȡ֤��������Ҫ������¼��");
					tempArray.add("�������������������������ȡ֤����������Ҫ������¼��");
					tempArray.add("���������鹤��������");
					tempArray.add("�����������ȾԴ�ֳ���칤��");
					for (int i = 0; i < groupArray.size(); i++) {
						childArray.add(tempArray);

					}
					Message msg = handler.obtainMessage();
					msg.what = HAVEDATA;
					handler.sendMessage(msg);

				} else {
					Message msg = handler.obtainMessage();
					msg.what = NODATA;
					handler.sendMessage(msg);
				}

			}
		}).start();

	}

	/**
	 * FileName: JCKHActivity.java Description:�Զ���ExpandableListView������
	 * 
	 * @author ������
	 * @Version 1.3.4
	 * @Copyright �п���ͼ���¿Ƽ����޹�˾ Create at: 2012-12-5 ����9:00:12
	 */
	class ExpandableAdapter extends BaseExpandableListAdapter {
		Context context;
		List<String> groupArray;
		List<List<String>> childArray;
		LayoutInflater inflater;

		public ExpandableAdapter(Context context, List<String> groupArray,
				List<List<String>> childArray) {
			this.context = context;
			this.groupArray = groupArray;
			this.childArray = childArray;
			inflater = LayoutInflater.from(context);
		}

		@Override
		public Object getChild(int groupPosition, int childPosition) {
			return childArray.get(groupPosition).get(childPosition);
		}

		@Override
		public long getChildId(int groupPosition, int childPosition) {
			return childPosition;
		}

		@Override
		public View getChildView(final int groupPosition,
				final int childPosition, boolean isLastChild, View convertView,
				ViewGroup parent) {
			String string = childArray.get(groupPosition).get(childPosition);
			LinearLayout linyout = (LinearLayout) inflater.inflate(
					R.layout.jckh_listview_child, null);
			imageView = (ImageView) linyout
					.findViewById(R.id.jckh_listview_child_image);
			imageView.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {

					HashMap<String, Object> conditions = new HashMap<String, Object>();
					conditions.put("Guid", childPosition + "");
					final ArrayList<HashMap<String, Object>> mdata = BaseClass.DBHelper
							.getList(TableName, conditions);
					if (mdata == null || mdata.size() == 0) {
						Toast.makeText(getApplicationContext(), "���ݿ�������",
								Toast.LENGTH_LONG).show();
						return;
					}
					// �����ݿ�õ��ļ�����
					final String uriStr = mdata.get(0).get("checktablename")
							.toString().replace("\n", "");
					String str = adapterArray.get(groupPosition);// �õ���������
					final String groupID = getParentID(str);
					final String filepath = "/sdcard/mapuni/MobileEnforcement/fj/"
							+ groupID
							+ autotextview1.getText().toString()
							+ "/" + uriStr;
					File file = new File(filepath);
					if (file.exists()) {// �����ļ���ʾ�Ƿ��ϴ��������ڵ���Toast
						Dialog dialog = new AlertDialog.Builder(
								JCKHActivity.this)
								.setIcon(
										getResources().getDrawable(
												R.drawable.icon_mapuni_white))
								.setTitle("��ȷ���ϴ��ļ�")
								.setPositiveButton("ȷ��",
										new DialogInterface.OnClickListener() {

											@Override
											public void onClick(
													DialogInterface dialog,
													int which) {
												dialog.cancel();
												if (!Net.checkNet(JCKHActivity.this)) {
													Toast.makeText(
															JCKHActivity.this,
															"���粻ͨ�������������ã�", 1)
															.show();
													return;
												}
												pd = new ProgressDialog(context);
												pd.setMessage("�����ϴ����������Ժ򡭡�");

												new Thread(new Runnable() {

													@Override
													public void run() {

														// ֪ͨ��
														Intent _Intent = new Intent();
														NotificationOperator _NotificationOperator = new NotificationOperator(
																context,
																_Intent);
														_NotificationOperator._TickerText = "�ϴ��ļ���"
																+ uriStr;

														_NotificationOperator
																.InitNotification(
																		R.drawable.stat_sys_upload_anim1,
																		"");
														/*
														 * for(HashMap<String,
														 * Object> map:mlist){
														 * if
														 * (map.get("depid")==
														 * bmId ){
														 * bmId=(String)map.get
														 * ("depid"); break; } }
														 */

														String bmidTime = groupID
																+ "/"
																+ autotextview1
																		.getText()
																		.toString()
																+ "/" + uriStr;

														Boolean Result = new JCKHXX()
																.upLoadFile(
																		filepath,
																		bmidTime);
														if (Result) {
															_NotificationOperator._TickerText = "�ļ���"
																	+ uriStr
																	+ "�ϴ��ɹ�";
															_NotificationOperator._IsComplete = true;
															_NotificationOperator
																	.StopNotification();

															msg = handler
																	.obtainMessage();
															msg.what = UPSUCCESS;
															handler.sendMessage(msg);
															ArrayList<HashMap<String, Object>> param = new ArrayList<HashMap<String, Object>>();
															HashMap<String, Object> pmap1 = new HashMap<String, Object>();
															HashMap<String, Object> pmap2 = new HashMap<String, Object>();
															HashMap<String, Object> pmap3 = new HashMap<String, Object>();
															HashMap<String, Object> pmap4 = new HashMap<String, Object>();
															HashMap<String, Object> pmap5 = new HashMap<String, Object>();
															String token;
															try {
																token = DESSecurity
																		.encrypt("InserCheckTableResult");
																pmap1.put(
																		"token",
																		token);
															} catch (Exception e) {
																e.printStackTrace();
															}

															String Tableid = mdata
																	.get(0)
																	.get("checktableid")
																	.toString();
															pmap2.put(
																	"Tableid",
																	Tableid);
															pmap3.put(
																	"Userid",
																	Global.getGlobalInstance()
																			.getUserid());
															pmap4.put(
																	"Year",
																	autotextview1
																			.getText()
																			.toString());
															pmap5.put("DepId",
																	groupID);
															param.add(pmap1);
															param.add(pmap2);
															param.add(pmap3);
															param.add(pmap4);
															param.add(pmap5);

															try {
																try {
																	int i = (Integer) WebServiceProvider
																			.callWebService(
																					Global.NAMESPACE,
																					"InserCheckTableResult",
																					param,
																					Global.getGlobalInstance()
																							.getSystemurl()
																							+ Global.WEBSERVICE_URL,
																					WebServiceProvider.RETURN_INT,
																					true,
																					15000);
																	if (i == 1) {
																		LogUtil.i("wang",
																				"�������洢�ɹ�");
																	} else {
																		LogUtil.i("wang",
																				"�������洢ʧ��");
																	}
																} catch (IOException e) {
																	e.printStackTrace();
																}
															} catch (Exception e) {
																e.printStackTrace();
															}

														} else {
															_NotificationOperator._TickerText = "�ļ���"
																	+ uriStr
																	+ "�ϴ�ʧ��";
															_NotificationOperator._IsComplete = true;
															_NotificationOperator
																	.StopNotification();
															msg = handler
																	.obtainMessage();
															msg.what = UPFAILED;
															handler.sendMessage(msg);
														}
													}

												}).start();
											}
										})
								.setNegativeButton("ȡ��",
										new DialogInterface.OnClickListener() {

											@Override
											public void onClick(
													DialogInterface dialog,
													int which) {
												dialog.cancel();
											}
										}).create();

						dialog.show();

					} else {
						Toast.makeText(JCKHActivity.this, "������д���˱�",
								Toast.LENGTH_SHORT).show();
					}

				}
			});

			TextView textView = (TextView) linyout
					.findViewById(R.id.jckh_listview_child_text);
			textView.setText(string);
			return linyout;
		}

		@Override
		public int getChildrenCount(int groupPosition) {
			return childArray.get(groupPosition).size();
		}

		@Override
		public Object getGroup(int groupPosition) {
			return groupArray.get(groupPosition);
		}

		@Override
		public int getGroupCount() {
			return groupArray.size();
		}

		@Override
		public long getGroupId(int groupPosition) {
			return groupPosition;
		}

		@Override
		public View getGroupView(int groupPosition, boolean isExpanded,
				View convertView, ViewGroup parent) {
			String string = groupArray.get(groupPosition);
			TextView textview = getGenericView(string);
			textview.setTextColor(Color.BLACK);
			textview.setTextSize(18);
			// LayoutParams mLayoutParams = new
			// LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT,40);
			// textview.setLayoutParams(mLayoutParams);
			return textview;
		}

		public TextView getGenericView(String string) {
			// Layout parameters for the ExpandableListView
			AbsListView.LayoutParams layoutParams = new AbsListView.LayoutParams(
					ViewGroup.LayoutParams.FILL_PARENT, 64);
			TextView text = new TextView(context);
			text.setLayoutParams(layoutParams);
			// Center the text vertically
			text.setGravity(Gravity.CENTER_VERTICAL | Gravity.LEFT);
			// Set the text starting position
			text.setPadding(50, 0, 0, 0);
			text.setTextColor(Color.BLACK);
			text.setTextSize(18);
			text.setText(string);
			return text;
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
	 * Description:�����ַ����õ�����ID�����ļ�·��
	 * 
	 * @param str
	 *            ��������
	 * @return
	 * @author ������ Create at: 2012-12-4 ����5:00:52
	 */
	public String getParentID(String str) {

		for (HashMap<String, Object> map : mlist) {
			if (map.get("depname").equals(str)) {
				return map.get("depid").toString();
			}
		}
		return "";

	}

}
