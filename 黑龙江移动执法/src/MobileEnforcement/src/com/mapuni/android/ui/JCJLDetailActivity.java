package com.mapuni.android.ui;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.http.client.ClientProtocolException;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableLayout.LayoutParams;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.mapuni.android.MobileEnforcement.R;
import com.mapuni.android.base.Global;
import com.mapuni.android.base.adapter.TreeViewAdapter;
import com.mapuni.android.base.business.BaseClass;
import com.mapuni.android.base.interfaces.IDetailed;
import com.mapuni.android.base.util.DisplayUitl;
import com.mapuni.android.base.util.ExceptionManager;
import com.mapuni.android.common.DetailedActivity;
import com.mapuni.android.dataprovider.FileHelper;
import com.mapuni.android.dataprovider.XmlHelper;
import com.mapuni.android.enforcement.TaskImageType;
import com.mapuni.android.netprovider.Net;

/**
 * FileName: JCJLDetailActivity.java
 * Description:����¼����
 * @author ������
 * @Version 1.3.4
 * @Copyright �п���ͼ���¿Ƽ����޹�˾<br>
 * Create at: 2012-12-4 ����04:47:56
 */
public class JCJLDetailActivity extends DetailedActivity {
	Bundle bundle;
	protected RelativeLayout parentLayout;
	protected IDetailed businessObj;
	
	/**��������״̬*/
	private  final int CHANGE_STATE = 0; // ���سɹ�
	private  final int DOWNLOAD_FAIL = 1; //����ʧ��
	private  final int COMPLETE_ALL = 2; // ���ݱ�ȫ��ͬ�����
	ProgressDialog pd;
	Handler handler;
	Message message;
	String titleText;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ui_mapuni);
		// ��ȡbundle��Ϣ
		Intent uponIntent = getIntent();
		bundle = uponIntent.getExtras();
		businessObj = (IDetailed) bundle.getSerializable("BusinessObj");
		titleText = uponIntent.getStringExtra("title");
		parentLayout = (RelativeLayout) this.findViewById(R.id.parentLayout);
		super.SetBaseStyle(parentLayout, "����¼��������");
		String itemId = uponIntent.getStringExtra("id");
		loadjcjlDetailed(itemId);
	}

	// �ж��Ƿ��Ǽ���¼��ѯ
	public String JCJLCX = "����¼��Ϣ����";
	TreeViewAdapter adapter;
	public String[] groups = { "ִ���ĵ�", "ȡ֤����" };

	/**
	 * ������ϸ��Ϣ
	 * 
	 * @param itemId
	 */
	private void loadjcjlDetailed(final String itemId) {
		TableLayout queryTable = new TableLayout(this);// �������鲼��

		final ScrollView scrollView = new ScrollView(this);// ������
		scrollView.setScrollContainer(true);
		scrollView.setFocusable(true);

		final LinearLayout allLayout = new LinearLayout(this);// �������ϼӵ��ܲ���
		final LinearLayout btnLayout = new LinearLayout(this);// ���ظ����İ�ť�Ĳ���
		final Button downloadfj = new Button(this);// ���ظ����İ�ť
		final Button downloadqd = new Button(this);// �����嵥�İ�ť
		allLayout.setOrientation(LinearLayout.VERTICAL);// ���ܲ��ּ��ش�ֱ����
		LinearLayout tableLayout = new LinearLayout(this);// �����Ĳ���
		final LinearLayout treeLayout = new LinearLayout(this);// �����Ĳ���
		btnLayout.setLayoutParams(new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.FILL_PARENT,
				LinearLayout.LayoutParams.WRAP_CONTENT)); // ���ظ�����ť�Ĳ���
		downloadfj.setLayoutParams(new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.WRAP_CONTENT,
				LinearLayout.LayoutParams.WRAP_CONTENT,1)); // ���ظ�����ť�Ĳ���
		downloadfj.setText("��   ��   ��   ��"); 
		downloadqd.setLayoutParams(new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.WRAP_CONTENT,
				LinearLayout.LayoutParams.WRAP_CONTENT,1)); // ���ظ�����ť�Ĳ���
		downloadqd.setText("��   ��   ��   ��"); 
		treeLayout.setGravity(Gravity.TOP);
		allLayout.setGravity(Gravity.CENTER);
		tableLayout.setGravity(Gravity.CENTER);

		LinearLayout detailedLayout = (LinearLayout) this.findViewById(R.id.middleLayout);

		detailedLayout.addView(scrollView);
		scrollView.addView(allLayout);
		tableLayout.addView(queryTable);
		btnLayout.addView(downloadfj);
		btnLayout.addView(downloadqd);
		allLayout.addView(tableLayout);
		allLayout.addView(btnLayout);
		// allLayout.addView(treeLayout);

		queryTable.setColumnStretchable(1, true);
		// ������ TableLayout �����ؼ�
		TableRow.LayoutParams rightLayoutParams = new TableRow.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		rightLayoutParams.setMargins(0, 0, 15, 0);

		// �õ���ѯ��Ŀ������

		ArrayList<HashMap<String, Object>> styleDetailed = businessObj
				.getStyleDetailed(this);
		HashMap<String, Object> querytj = new HashMap<String, Object>();
		querytj.put("id", itemId);
		HashMap<String, Object> detaild = businessObj.getDetailed(itemId);// ͨ��id��ѯ������

		int itemCount = styleDetailed.size();
		int counter = 0;
		for (int index = 0; index < itemCount; index++) {
			Map<String, Object> map = styleDetailed.get(index);

			if (styleDetailed.get(index).get(XmlHelper.QUERY_EDIT_TEXT)
					.equals("name") && counter == 0) {
				counter++;
				continue;// name���Բ���Ҫ��ӵ��б�
			}

			TableRow row = new TableRow(this);
			TextView textView = new TextView(this);
			// textView.setMaxEms(5);

			// ���
			String hint = styleDetailed.get(index).get(XmlHelper.QUERY_HINT).toString();
			textView.setTextColor(android.graphics.Color.BLACK);
			textView.setText(hint);
			textView.setGravity(Gravity.RIGHT);
			textView.setPadding(15, 0, 0, 0);
			row.addView(textView);
			EditText editText = null;
			String value = null;

			editText = new EditText(this);
			editText.setMaxEms(editText.getWidth());
			value = styleDetailed.get(index).get(XmlHelper.QUERY_EDIT_TEXT).toString();
			editText.setId(index); // �������ø��� EditText �� Id, ��ȡ���ݹ��� XPath
			// editText.setm(0, 0, 15, 0);
			// ���ʽʱ����
			editText.setFocusableInTouchMode(false);
			String textvalue = String.valueOf(detaild.get(value));
			String unit = "";
			if (null != map.get("unit") && textvalue.length() > 0) {
				unit = map.get("unit").toString();
			}

			if ("time".equals(map.get("style")) && textvalue.contains("T")) {
				String values = (String) textvalue.subSequence(0, textvalue.length() - 6);
				textvalue = values.replace("T", " ");
			}
			editText.setText(textvalue + " " + unit);// ����EditText����
			row.addView(editText, rightLayoutParams);

			// 211.99.132.164/

			queryTable.addView(row);
		}
		handler = new Handler() {

			@Override
			public void handleMessage(Message msg) {

				switch (msg.what) {
				case CHANGE_STATE:
					if (pd != null)
						pd.cancel();
					Tree(scrollView,allLayout,treeLayout,btnLayout);
//					btnLayout.setVisibility(View.GONE);
					break;

				case DOWNLOAD_FAIL:
					Toast.makeText(JCJLDetailActivity.this,
							"��������ʧ��", Toast.LENGTH_SHORT)
							.show();
					File f = new File("/sdcard/mapuni/MobileEnforcement/jcjl/"
							+ bundle.getString("rwbh"));
					f.delete();
					if (pd != null)
						pd.cancel();
					break;
				case COMPLETE_ALL:
					Toast.makeText(JCJLDetailActivity.this,
							"���ļ��Բ�����", Toast.LENGTH_SHORT)
							.show();
					if (pd != null)
						pd.cancel();
					break;

				}
			}
		};
		// ����ѯ��ť��Ӽ����¼�
		downloadfj.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				File f = new File("/sdcard/mapuni/MobileEnforcement/jcjl/"
						+ bundle.getString("rwbh") + "/");
				if (f.exists()&& f.listFiles().length>0) {
					Tree(scrollView,allLayout,treeLayout,btnLayout);
					btnLayout.setVisibility(View.GONE);
				} else {
					if (Net.checkNet(JCJLDetailActivity.this)) {// ������ŵ���

						pd = new ProgressDialog(JCJLDetailActivity.this);
						pd.setMessage("�����������ݣ����Ե�...");
						pd.setCancelable(false);
						pd.show();
						HashMap<String, Object> fliterHashMap = new HashMap<String, Object>();
						fliterHashMap.put("TaskID",titleText);
						fliterHashMap.put("TypeId !", "05");
						ArrayList<HashMap<String, Object>> fjName = BaseClass.DBHelper
								.getOrderList("T_WRY_JCJLRWZX", fliterHashMap,"UpdateTime	asc");
						if(fjName.size()==0){
							pd.cancel();
							Toast.makeText(JCJLDetailActivity.this,"�������޸�����", Toast.LENGTH_SHORT).show();
							return;
						}
						Progressdialog(fjName, bundle.getString("rwbh"));
					} else {
						if(pd != null)
							pd.cancel();
						Toast.makeText(JCJLDetailActivity.this,"��������ʧ�ܣ������������ã�", Toast.LENGTH_SHORT).show();
						return;
					}
				}
				
			}
		});
		downloadqd.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				String rwbh =titleText;
				String filePath = Global.SDCARD_RASK_DATA_PATH + "ImgDoc/" + rwbh;
				if(!FileHelper.isFileExist(filePath) && !Net.checkNet(JCJLDetailActivity.this)) {
					Toast.makeText(JCJLDetailActivity.this, "���粻ͨ���޷��鿴�嵥!", Toast.LENGTH_SHORT).show();
					return;
				}
				Intent _Intent=new Intent(JCJLDetailActivity.this, TaskImageType.class);
				_Intent.putExtra("TaskId", rwbh);
				startActivity(_Intent);	
			}
		});

	}

	// ѭ������ÿ������
	void Progressdialog(
			final ArrayList<HashMap<String, Object>> fjName,
			final String content) {

		new Thread(new Runnable() {
			@Override
			public void run() {
				// ����list�õ��ļ���	
				int successcount = 0;
						try {							
							boolean issuccess;
							for(HashMap<String, Object> jcjlmap:fjName){
								String filename =jcjlmap.get("docpath").toString();
								String path = (String) filename.substring(filename.indexOf(".") + 1,filename.length());
								// �ж��ļ������ָ�ʽ								
								if (path.equals("docx")||path.equals("doc")) {
									String Downloadurl = Global.getGlobalInstance().getSystemurl()
											+ Global.JCJLRWFJ_DOWN_URL+ content+ "/RaskFile"+ "/"
											+ java.net.URLEncoder.encode(filename,"UTF-8");
									issuccess = download(Downloadurl, content,filename);
									if(!issuccess){
										message = handler.obtainMessage();
										message.what = DOWNLOAD_FAIL;
										handler.sendMessage(message);
										break;
									}
									successcount++;
								}
								if (path.equals("xls")||path.equals("xlsx")) {
									String Downloadurl = Global.getGlobalInstance().getSystemurl()
											+ Global.JCJLRWFJ_DOWN_URL+ content+ "/RaskFile"+ "/"
											+ java.net.URLEncoder.encode(filename,"UTF-8");
									issuccess = download(Downloadurl, content,filename);
									if(!issuccess){
										message = handler.obtainMessage();
										message.what = DOWNLOAD_FAIL;
										handler.sendMessage(message);
										break;
									}
									successcount++;
								}

								if (path.equals("mp3")) {
									String Downloadurl = Global.getGlobalInstance().getSystemurl()
											+ Global.JCJLRWFJ_DOWN_URL+ content+ "/RaskAudio"+ "/"
											+ java.net.URLEncoder.encode(filename,"UTF-8");
									issuccess = download(Downloadurl, content,filename);
									if(!issuccess){
										message = handler.obtainMessage();
										message.what = DOWNLOAD_FAIL;
										handler.sendMessage(message);
										break;
									}
									successcount++;
								}
								if (path.equals("amr")) {
									String Downloadurl = Global.getGlobalInstance().getSystemurl()
											+ Global.JCJLRWFJ_DOWN_URL+ content+ "/RaskAudio"+ "/"
											+ java.net.URLEncoder.encode(filename,"UTF-8");
									issuccess = download(Downloadurl, content,filename);
									if(!issuccess){
										message = handler.obtainMessage();
										message.what = DOWNLOAD_FAIL;
										handler.sendMessage(message);
										break;
									}
									successcount++;
								}
								if (path.equals("mp4")) {
									String Downloadurl = Global.getGlobalInstance().getSystemurl()
											+ Global.JCJLRWFJ_DOWN_URL+ content+ "/RaskVideo"+ "/"
											+ java.net.URLEncoder.encode(filename,"UTF-8");
									issuccess = download(Downloadurl, content,filename);
									if(!issuccess){
										message = handler.obtainMessage();
										message.what = DOWNLOAD_FAIL;
										handler.sendMessage(message);
										break;
									}
									successcount++;
								}
								if (path.equals("jpg")) {
									String Downloadurl = Global.getGlobalInstance().getSystemurl()
											+ Global.JCJLRWFJ_DOWN_URL+ content+ "/RaskImage"+ "/"
											+ java.net.URLEncoder.encode(filename,"UTF-8");
									issuccess = download(Downloadurl, content,filename);
									if(!issuccess){
										message = handler.obtainMessage();
										message.what = DOWNLOAD_FAIL;
										handler.sendMessage(message);
										break;
									}
									successcount++;
								}
							}

						} catch (UnsupportedEncodingException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
							ExceptionManager.WriteCaughtEXP(e1, "JCJLListActivity");
							message =handler.obtainMessage();
							message.what = DOWNLOAD_FAIL;
							handler.sendMessage(message);
						}				
				if(successcount==fjName.size()){
					message = handler.obtainMessage();
					message.what = CHANGE_STATE;
					handler.sendMessage(message);
				}
				
			}
		}).start();
	}

	// װ�����ķ���
	void Tree(ScrollView scrollView,LinearLayout allLayout,LinearLayout treeLayout,LinearLayout btnLayout) {
		scrollView.removeAllViews();
		ExpandableListView.LayoutParams ELayoutParams = new ExpandableListView.LayoutParams(
				LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
		ExpandableListView jcjlExpandable = new ExpandableListView(
				JCJLDetailActivity.this);

		jcjlExpandable.setLayoutParams(ELayoutParams);
		adapter = new TreeViewAdapter(JCJLDetailActivity.this,
				TreeViewAdapter.PaddingLeft >> 1);
		HashMap<String, Object> filter = new HashMap<String, Object>();
		final String rwbh = businessObj.getCurrentID();
		filter.put("Guid", rwbh);
		List<TreeViewAdapter.TreeNode> treeNode = adapter.GetTreeNode();
		Iterator<HashMap<String, Object>> it;
		HashMap<String, Object> jcjltreemap;
		String key;
		Object value;
		Iterator<String> set;
		int rowcount = 2;
		for (int i = 0; i < groups.length; i++) {
			final String jcjlTreetablename = "V_JCJL_TREE_ZXWD";
			TreeViewAdapter.TreeNode node = new TreeViewAdapter.TreeNode();
			node.parent = groups[i];
			if (i == 0) {
				filter.put("TypeId", "01"); // ִ���ĵ�
			}
			if (i == 1) {
				filter.remove("TypeId");
				filter.put("TypeId !", "01");
				filter.put("TypeId !", "05");
			}
			ArrayList<HashMap<String, Object>> treezxwd = BaseClass.DBHelper
					.getOrderList(jcjlTreetablename, filter, "FBSJ asc");
			// ���������������ݱ������ص�����
			it = treezxwd.iterator();
			while (it.hasNext()) {
				jcjltreemap = it.next();
				set = jcjltreemap.keySet().iterator();
				while (set.hasNext()) {
					/**2012��10��16���޸ı�������
					 * key = set.next();
					value = jcjltreemap.get(key);
					if (key.equals("docpath")) {
						node.childs.add(value);
						rowcount++;
					}*/
					key = set.next();
					value = jcjltreemap.get(key);
					if (key.equals("docpath")) {
						HashMap<String, Object> child = new HashMap<String, Object>();
						child.put("title", value);
						node.childs.add(child);
						rowcount++;
					}
				}
			}
			treeNode.add(node);

		}

		adapter.UpdateTreeNode(treeNode);
		jcjlExpandable.setAdapter(adapter);
		jcjlExpandable.expandGroup(0);
		jcjlExpandable.expandGroup(1);
		treeLayout.addView(jcjlExpandable);
		treeLayout.setLayoutParams(new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.FILL_PARENT,
				TreeViewAdapter.ItemHeight * rowcount));// ���Ĳ��ֲ���

		allLayout.addView(treeLayout);
		scrollView.addView(allLayout);
		btnLayout.setVisibility(View.GONE);
		// ������ӵ����¼�
		jcjlExpandable
				.setOnChildClickListener(new OnChildClickListener() {

					@Override
					public boolean onChildClick(
							ExpandableListView expandablelistview,
							View view, int pid, int cid, long l) {
						TextView text = (TextView) view;
						CharSequence path = text.getText();
						String filepash = "/sdcard/mapuni/MobileEnforcement/jcjl/"
								+ bundle.getString("rwbh") + "/" + path;
						DisplayUitl.openfile(filepash,
								JCJLDetailActivity.this);
						return false;
					}
				});

	}

	// ���ظ����ķ���
	public boolean download(String Downloadurl, String content,
			String wjName) {
		boolean ifsuccess = true;
		try {
			URL url = new URL(Downloadurl);
			HttpURLConnection con = (HttpURLConnection) url
					.openConnection();
			InputStream in = con.getInputStream();
			FileOutputStream fos = null;

			if (in != null) {
				File f = new File(
						"/sdcard/mapuni/MobileEnforcement/jcjl/"
								+ content + "/");// ���û��Ŀ¼�Ƚ���Ŀ¼
				if (!f.exists())
					f.mkdirs();
				File fil = new File(
						"/sdcard/mapuni/MobileEnforcement/jcjl/"
								+ content + "/" + wjName);// ��Ŀ¼֮���ļ�
				fos = new FileOutputStream(fil);
				byte[] bytes = new byte[1024];
				int flag = -1;

				while ((flag = in.read(bytes)) != -1) {// ��δ�����ļ�ĩβ��һֱ��ȡ
					fos.write(bytes, 0, flag);

				}
				fos.flush();
				fos.close();
			} else {
				ifsuccess= false;
			}
		} catch (ClientProtocolException e) {	
			ifsuccess= false;
			ExceptionManager.WriteCaughtEXP(e, "JCJLListActivity");
			e.printStackTrace();
		} catch (IOException e) {
			ifsuccess= false;
			e.printStackTrace();

		}
		return ifsuccess;
	}
}
