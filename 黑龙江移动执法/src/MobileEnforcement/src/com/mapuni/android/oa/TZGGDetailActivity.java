package com.mapuni.android.oa;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebSettings;
import android.webkit.WebSettings.PluginState;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableLayout.LayoutParams;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.mapuni.android.MobileEnforcement.R;
import com.mapuni.android.base.Global;
import com.mapuni.android.base.adapter.TreeViewAdapter;
import com.mapuni.android.base.business.BaseClass;
import com.mapuni.android.base.controls.listview.MyScrollView;
import com.mapuni.android.base.interfaces.IDetailed;
import com.mapuni.android.base.util.DisplayUitl;
import com.mapuni.android.common.DetailedActivity;
import com.mapuni.android.dataprovider.FileHelper;
import com.mapuni.android.dataprovider.XmlHelper;
import com.mapuni.android.netprovider.Net;
import com.mapuni.android.ui.WebViewFormActivity;

public class TZGGDetailActivity extends DetailedActivity {
	Bundle bundle;
	protected RelativeLayout parentLayout;
	protected IDetailed businessObj;
	protected LinearLayout allLayout = null;
	protected LinearLayout treeLayout = null;
	protected MyScrollView scrollView = null;
	protected TableLayout queryTable = null;// ���鲼��
	protected Button downloadfj = null;// ���ظ����İ�ť
	protected LinearLayout btnLayout = null;
	protected LinearLayout detailedLayout = null;

	private final int CHANGE_STATE = 0; // ���سɹ�
	private final int DOWNLOAD_FAIL = 1; // ����ʧ��
	private final int COMPLETE_ALL = 2; // ���ݱ�ȫ��ͬ�����
	private final String GGmobilefilepath = Global.SDCARD_RASK_DATA_PATH + "YDBG/";
	private final String sort = "createdate asc";
	private String downloadurl;
	protected String downfilecloum = "id";
	private final String openfilecloum = "filename";
	private final String filetable = "T_YDZF_ATTACH";
	private final String relcloum = "infoid";
	FileHelper filehelper = new FileHelper();
	HashMap<String, Object> fliterHashMap = null;
	String[] tableadd;
	ProgressDialog pd;
	Handler handler;
	Message message;
	TreeViewAdapter adapter;
	public final String[] groups = { "   ����" };

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,
		// WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		setContentView(R.layout.ui_mapuni);
		// ��ȡbundle��Ϣ
		Intent uponIntent = getIntent();
		bundle = uponIntent.getExtras();
		businessObj = (IDetailed) bundle.getSerializable("BusinessObj");
		String titleText = businessObj.getDetailedTitleText();
		parentLayout = (RelativeLayout) this.findViewById(R.id.parentLayout);
		super.SetBaseStyle(parentLayout, titleText);
		String itemId = businessObj.getCurrentID();
		LoadDetailed(this, itemId);
		Boolean isFromWidget = uponIntent.getBooleanExtra("isFromWidget", false);
		if (isFromWidget != null && isFromWidget) {
			super.setSynchronizeButtonVisiable(false);
		}
	}

	// ���ز���
	protected void loadlayout() {
		queryTable = new TableLayout(this);
		scrollView = new MyScrollView(this);
		scrollView.setScrollContainer(true);
		scrollView.setFocusable(true);
		allLayout = new LinearLayout(this);// �������ϼӵ��ܲ���
		allLayout.setOrientation(LinearLayout.VERTICAL);// ���ܲ��ּ��ش�ֱ����
		LinearLayout tableLayout = new LinearLayout(this);// �����Ĳ���
		btnLayout = new LinearLayout(this);// �Ű�ť�Ĳ���
		downloadfj = new Button(this);
		downloadfj.setText("��   ��   ��   ��");
		treeLayout = new LinearLayout(this);// �����Ĳ���
		treeLayout.setGravity(Gravity.TOP);
		allLayout.setGravity(Gravity.CENTER);
		tableLayout.setGravity(Gravity.CENTER);
		btnLayout.setGravity(Gravity.CENTER);
		downloadfj.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)); // ���ظ�����ť�Ĳ���
		detailedLayout = (LinearLayout) this.findViewById(R.id.middleLayout);
		detailedLayout.addView(scrollView);
		scrollView.addView(allLayout);
		tableLayout.addView(queryTable);
		btnLayout.addView(downloadfj);
		allLayout.addView(tableLayout);
		allLayout.addView(btnLayout);
		queryTable.setColumnStretchable(1, true);
		Tree(scrollView, allLayout, treeLayout, btnLayout, this);
	}

	// ������ϸ��Ϣ�ķ���
	protected void addcontent(String itemId) {
		// ������ TableLayout �����ؼ�

		// �õ���ѯ��Ŀ������

		final ArrayList<HashMap<String, Object>> styleDetailed = businessObj.getStyleDetailed(this);
		HashMap<String, Object> querytj = new HashMap<String, Object>();
		querytj.put("id", itemId);
		final HashMap<String, Object> detaild = businessObj.getDetailed(itemId);// ͨ��id��ѯ������

		int itemCount = styleDetailed.size();
		int counter = 0;
		for (int index = 0; index < itemCount; index++) {
			TableRow.LayoutParams rightLayoutParams = new TableRow.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			rightLayoutParams.setMargins(0, 0, 15, 0);
			final Map<String, Object> map = styleDetailed.get(index);

			if (styleDetailed.get(index).get(XmlHelper.QUERY_EDIT_TEXT).equals("name") && counter == 0) {
				counter++;
				continue;// name���Բ���Ҫ��ӵ��б�
			}
			if (styleDetailed.get(index).get(XmlHelper.QUERY_EDIT_TEXT).equals("mysql")) {
				counter++;
				continue;// mysql���Բ���Ҫ��ӵ��б�
			}

			TableRow row = new TableRow(this);
			TextView textView = new TextView(this);

			// ���
			String hint = styleDetailed.get(index).get(XmlHelper.QUERY_HINT).toString();
			textView.setTextColor(android.graphics.Color.BLACK);
			textView.setText(hint);
			textView.setGravity(Gravity.RIGHT);
			textView.setPadding(15, 0, 0, 0);
			row.addView(textView);
			EditText editText = null;
			String value = null;
			if ("zhengwen".equals(map.get("style"))) {
				int zwindex = index;

				String value1 = styleDetailed.get(zwindex).get(XmlHelper.QUERY_EDIT_TEXT).toString();
				final String otherstr = String.valueOf(detaild.get(value1));
				WebView wv = new WebView(this);
				// TableLayout.LayoutParams params = new
				// TableLayout.LayoutParams();
				// params.width = 600;
				// params.height = 600;
				// wv.setLayoutParams(params);
				value = styleDetailed.get(index).get(XmlHelper.QUERY_EDIT_TEXT).toString();
				wv.setId(index);
				WebSettings mWebSettings = wv.getSettings();
				mWebSettings.setJavaScriptEnabled(true);// ֧�ֽű�
				mWebSettings.setBuiltInZoomControls(false);// ��������
				mWebSettings.setLightTouchEnabled(false);//
				mWebSettings.setSupportZoom(false);
//				mWebSettings.setPluginsEnabled(false);
				mWebSettings.setPluginState(PluginState.ON);

				mWebSettings.setNeedInitialFocus(false);
				wv.setHapticFeedbackEnabled(false);
				wv.setScrollBarStyle(0);
				wv.setHorizontalScrollBarEnabled(false);
				wv.setVerticalScrollBarEnabled(false);
				row.requestFocus();
				LinearLayout topLin = new LinearLayout(this);
				wv.setClickable(false);
				if (otherstr != null && !"".equals(otherstr) && otherstr != "null") {
					row.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							Intent it = new Intent(TZGGDetailActivity.this, WebViewFormActivity.class);
							it.putExtra("otherstr", otherstr);
							it.putExtra("title", "��ϸ��Ϣ");
							startActivity(it);
						}
					});
					rightLayoutParams = new TableRow.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
					topLin.setLayoutParams(rightLayoutParams);
					topLin.setBackgroundColor(android.graphics.Color.TRANSPARENT);
					topLin.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							Intent it = new Intent(TZGGDetailActivity.this, WebViewFormActivity.class);
							it.putExtra("otherstr", otherstr);
							it.putExtra("title", "��ϸ��Ϣ");
							startActivity(it);
						}
					});
					wv.loadDataWithBaseURL("", otherstr, "text/html", "utf-8", "");
					rightLayoutParams = new TableRow.LayoutParams(LayoutParams.MATCH_PARENT, 300);
					rightLayoutParams.setMargins(40, 40, 40, 40);
					wv.setLayoutParams(rightLayoutParams);
				} else {
					// AssetManager am = getAssets();
					// try {
					// InputStream open = am.open("nocontent.html");
					//
					// } catch (IOException e) {
					// e.printStackTrace();
					// }
					continue;
				}
				rightLayoutParams = new TableRow.LayoutParams(LayoutParams.MATCH_PARENT, 400);
				FrameLayout fl = new FrameLayout(this);
				fl.setLayoutParams(rightLayoutParams);

				LinearLayout lin = new LinearLayout(this);
				lin.setHorizontalGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL);
				lin.setLayoutParams(rightLayoutParams);
				lin.setBackgroundColor(android.graphics.Color.BLACK);
				LinearLayout lin1 = new LinearLayout(this);
				rightLayoutParams = new TableRow.LayoutParams(LayoutParams.MATCH_PARENT, 398);
				rightLayoutParams.setMargins(1, 1, 1, 1);
				lin1.setLayoutParams(rightLayoutParams);
				lin1.setHorizontalGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL);
				lin1.setBackgroundColor(android.graphics.Color.WHITE);
				// lin1.setPadding(1,1,1,1);
				lin.addView(lin1);
				lin1.addView(wv);
				fl.addView(lin);
				fl.addView(topLin);
				topLin.requestFocus();
				rightLayoutParams = new TableRow.LayoutParams(LayoutParams.MATCH_PARENT, 400);
				row.addView(fl, rightLayoutParams);
				rightLayoutParams.setMargins(2, 2, 17, 10);

			} else {
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
			}
			queryTable.addView(row);

		}
	}

	/**
	 * ������ϸ��Ϣ
	 * 
	 * @param itemId
	 */

	protected void LoadDetailed(final Context context, String itemId) {
		loadlayout();// ���ز���
		addcontent(itemId);
		BaseClass baseclass = (BaseClass) businessObj;
		tableadd = baseclass.GetTableName().split("_");
		handler = new Handler() {

			@Override
			public void handleMessage(Message msg) {
				String fjname = msg.obj.toString();
				switch (msg.what) {
				case CHANGE_STATE:
					if (pd != null)
						pd.cancel();
					String filepash = GGmobilefilepath + tableadd[tableadd.length - 1] + "/" + fjname;
					DisplayUitl.openfile(filepash, context);
					// Tree(scrollView, allLayout, treeLayout, btnLayout,
					// context);
					break;
				case DOWNLOAD_FAIL:
					Toast.makeText(context, "��������ʧ��", Toast.LENGTH_SHORT).show();
					File f = new File(GGmobilefilepath + tableadd[tableadd.length - 1] + "/" + fjname);
					f.delete();
					if (pd != null)
						pd.cancel();
					break;
				case COMPLETE_ALL:
					Toast.makeText(context, "���ļ��Բ�����", Toast.LENGTH_SHORT).show();
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
				// BaseClass baseclass=(BaseClass) businessObj;
				fliterHashMap = new HashMap<String, Object>();
				fliterHashMap.put(relcloum, businessObj.getCurrentID());
				ArrayList<HashMap<String, Object>> fjName = BaseClass.DBHelper.getOrderList(filetable, fliterHashMap, sort);
				if (fjName.size() == 0) {
					Toast.makeText(context, "�������޸�����", Toast.LENGTH_SHORT).show();
					return;
				} else {
					if (Net.checkNet(context)) {// ������ŵ���
						pd = new ProgressDialog(context);
						pd.setMessage("�����������ݣ����Ե�...");
						pd.setCancelable(false);
						pd.show();
						// Progressdialog(fjName, context);
					} else {
						Tree(scrollView, allLayout, treeLayout, btnLayout, context);
						Toast.makeText(context, "��������ʧ�ܣ������������ã�", Toast.LENGTH_SHORT).show();
						return;
					}
				}
			}
		});

	}

	// ѭ������ÿ������
	public void Progressdialog(final String fjid, final String fjname, final Context context) {
		new Thread(new Runnable() {

			@Override
			public void run() {
				boolean issuccess;
				File f = new File(GGmobilefilepath + tableadd[tableadd.length - 1] + "/" + fjname);
				if (f.exists() && f.length() > 0) {
					message = handler.obtainMessage();
					message.what = CHANGE_STATE;
					message.obj = fjname;
					handler.sendMessage(message);
					return;
				} else {
					try {
						downloadurl = DisplayUitl.getDataXML("sjz_server").get("serverurl").toString();
					} catch (Exception e) {
						Toast.makeText(TZGGDetailActivity.this, "�����ļ��쳣", 0).show();
						return;
					}
					issuccess = Downloadfile(downloadurl + "/", fjid, fjname, context);
					if (issuccess) {
						message = handler.obtainMessage();
						message.what = CHANGE_STATE;
						message.obj = fjname;
						handler.sendMessage(message);
					} else {
						message = handler.obtainMessage();
						message.what = DOWNLOAD_FAIL;
						message.obj = fjname;
						handler.sendMessage(message);
					}
				}
			}
		}).start();
	}

	// ���ظ����ķ���
	public boolean Downloadfile(String downloadurl, String Feleid, String filename, Context context) {
		boolean ifsuccess = true;
		String type = filename.substring(filename.indexOf("."), filename.length());
		String feleid = Feleid;
		if (Feleid.contains(".")) {
			// feleid = (String)
			// filename.substring(filename.indexOf(".")+1,filename.length());
			feleid = filename.substring(0, filename.indexOf("."));
		}

		try {
			String final_filename = feleid + type;
			String Downloadurl;
			if (Global.getGlobalInstance().getSystemname().equalsIgnoreCase("sjz")) {
				Downloadurl = downloadurl + final_filename;
			} else {
				Downloadurl = downloadurl + java.net.URLEncoder.encode(final_filename, "UTF-8");
			}
			if (Net.checkURL(Downloadurl)) {
				URL url = new URL(Downloadurl);
				HttpURLConnection con = (HttpURLConnection) url.openConnection();
				InputStream in = con.getInputStream();
				FileOutputStream fos = null;
				if (in != null) {
					File f = new File(GGmobilefilepath + tableadd[tableadd.length - 1]);// ���û��Ŀ¼�Ƚ���Ŀ¼
					if (!f.exists())
						f.mkdirs();
					File fil = new File(GGmobilefilepath + tableadd[tableadd.length - 1] + "/" + filename);// ��Ŀ¼֮���ļ�
					fos = new FileOutputStream(fil);
					byte[] bytes = new byte[1024];
					int flag = -1;
					while ((flag = in.read(bytes)) != -1) {// ��δ�����ļ�ĩβ��һֱ��ȡ
						fos.write(bytes, 0, flag);

					}
					fos.flush();
					fos.close();
				} else {
					ifsuccess = false;
				}
			} else {
				ifsuccess = false;
			}

		} catch (IOException e) {
			ifsuccess = false;

			e.printStackTrace();

		}
		return ifsuccess;
	}

	// װ�����ķ���
	void Tree(MyScrollView scrollView, LinearLayout allLayout, LinearLayout treeLayout, LinearLayout btnLayout, final Context context) {
		scrollView.removeAllViews();
		ExpandableListView.LayoutParams ELayoutParams = new ExpandableListView.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
		ExpandableListView jcjlExpandable = new ExpandableListView(context);
		jcjlExpandable.setGroupIndicator(this.getResources().getDrawable(R.layout.expandablelistviewselector));
		jcjlExpandable.setLayoutParams(ELayoutParams);
		adapter = new TreeViewAdapter(context, 30);
		List<TreeViewAdapter.TreeNode> treeNode = adapter.GetTreeNode();
		ArrayList<HashMap<String, Object>> tree = null;
		int rowcount = 2;
		for (int i = 0; i < groups.length; i++) {
			TreeViewAdapter.TreeNode node = new TreeViewAdapter.TreeNode();
			node.parent = groups[i];
			fliterHashMap = new HashMap<String, Object>();
			fliterHashMap.put(relcloum, businessObj.getCurrentID());
			tree = BaseClass.DBHelper.getOrderList(filetable, fliterHashMap, sort);
			// ���������������ݱ������ص�����
			for (HashMap<String, Object> treemap : tree) {
				HashMap<String, Object> child = new HashMap<String, Object>();
				child.put("title", treemap.get(openfilecloum).toString());
				child.put("id", treemap.get(downfilecloum).toString());
				node.childs.add(child);
				rowcount++;
			}
			treeNode.add(node);

		}

		adapter.UpdateTreeNode(treeNode);
		jcjlExpandable.setAdapter(adapter);
		jcjlExpandable.expandGroup(0);
		treeLayout.addView(jcjlExpandable);
		treeLayout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, TreeViewAdapter.ItemHeight * rowcount));// ���Ĳ��ֲ���
		if (tree.size() > 0) {
			allLayout.addView(treeLayout);
		}
		scrollView.addView(allLayout);
		btnLayout.setVisibility(View.GONE);
		// ������ӵ����¼�
		jcjlExpandable.setOnChildClickListener(new OnChildClickListener() {

			@Override
			public boolean onChildClick(ExpandableListView expandablelistview, View view, int pid, int cid, long l) {
				if (Net.checkNet(context)) {// ������ŵ���
					TextView text = (TextView) view;
					CharSequence path = text.getText();
					String id = text.getTag().toString();
					pd = new ProgressDialog(context);
					pd.setMessage("�����������ݣ����Ե�...");
					pd.setCancelable(false);
					pd.show();
					Progressdialog(id, (String) path, context);
				} else {
					Toast.makeText(context, "��������ʧ�ܣ������������ã�", Toast.LENGTH_SHORT).show();
				}
				return false;
			}
		});

	}
}
