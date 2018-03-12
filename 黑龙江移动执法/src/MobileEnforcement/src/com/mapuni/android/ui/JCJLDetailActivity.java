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
 * Description:检查记录详情
 * @author 王留庚
 * @Version 1.3.4
 * @Copyright 中科宇图天下科技有限公司<br>
 * Create at: 2012-12-4 下午04:47:56
 */
public class JCJLDetailActivity extends DetailedActivity {
	Bundle bundle;
	protected RelativeLayout parentLayout;
	protected IDetailed businessObj;
	
	/**更新数据状态*/
	private  final int CHANGE_STATE = 0; // 下载成功
	private  final int DOWNLOAD_FAIL = 1; //下载失败
	private  final int COMPLETE_ALL = 2; // 数据表全部同步完成
	ProgressDialog pd;
	Handler handler;
	Message message;
	String titleText;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ui_mapuni);
		// 获取bundle信息
		Intent uponIntent = getIntent();
		bundle = uponIntent.getExtras();
		businessObj = (IDetailed) bundle.getSerializable("BusinessObj");
		titleText = uponIntent.getStringExtra("title");
		parentLayout = (RelativeLayout) this.findViewById(R.id.parentLayout);
		super.SetBaseStyle(parentLayout, "检查记录任务详情");
		String itemId = uponIntent.getStringExtra("id");
		loadjcjlDetailed(itemId);
	}

	// 判断是否是检查记录查询
	public String JCJLCX = "检查记录信息详情";
	TreeViewAdapter adapter;
	public String[] groups = { "执行文档", "取证材料" };

	/**
	 * 加载详细信息
	 * 
	 * @param itemId
	 */
	private void loadjcjlDetailed(final String itemId) {
		TableLayout queryTable = new TableLayout(this);// 任务详情布局

		final ScrollView scrollView = new ScrollView(this);// 滚动条
		scrollView.setScrollContainer(true);
		scrollView.setFocusable(true);

		final LinearLayout allLayout = new LinearLayout(this);// 滚动条上加的总布局
		final LinearLayout btnLayout = new LinearLayout(this);// 下载附件的按钮的布局
		final Button downloadfj = new Button(this);// 下载附件的按钮
		final Button downloadqd = new Button(this);// 下载清单的按钮
		allLayout.setOrientation(LinearLayout.VERTICAL);// 给总布局加载垂直布局
		LinearLayout tableLayout = new LinearLayout(this);// 方表格的布局
		final LinearLayout treeLayout = new LinearLayout(this);// 放树的布局
		btnLayout.setLayoutParams(new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.FILL_PARENT,
				LinearLayout.LayoutParams.WRAP_CONTENT)); // 下载附件按钮的参数
		downloadfj.setLayoutParams(new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.WRAP_CONTENT,
				LinearLayout.LayoutParams.WRAP_CONTENT,1)); // 下载附件按钮的参数
		downloadfj.setText("查   看   附   件"); 
		downloadqd.setLayoutParams(new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.WRAP_CONTENT,
				LinearLayout.LayoutParams.WRAP_CONTENT,1)); // 下载附件按钮的参数
		downloadqd.setText("查   看   清   单"); 
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
		// 逐行往 TableLayout 里填充控件
		TableRow.LayoutParams rightLayoutParams = new TableRow.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		rightLayoutParams.setMargins(0, 0, 15, 0);

		// 得到查询项目的数量

		ArrayList<HashMap<String, Object>> styleDetailed = businessObj
				.getStyleDetailed(this);
		HashMap<String, Object> querytj = new HashMap<String, Object>();
		querytj.put("id", itemId);
		HashMap<String, Object> detaild = businessObj.getDetailed(itemId);// 通过id查询出数据

		int itemCount = styleDetailed.size();
		int counter = 0;
		for (int index = 0; index < itemCount; index++) {
			Map<String, Object> map = styleDetailed.get(index);

			if (styleDetailed.get(index).get(XmlHelper.QUERY_EDIT_TEXT)
					.equals("name") && counter == 0) {
				counter++;
				continue;// name属性不需要添加到列表
			}

			TableRow row = new TableRow(this);
			TextView textView = new TextView(this);
			// textView.setMaxEms(5);

			// 左侧
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
			editText.setId(index); // 这里设置各个 EditText 的 Id, 读取数据构造 XPath
			// editText.setm(0, 0, 15, 0);
			// 表达式时调用
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
			editText.setText(textvalue + " " + unit);// 设置EditText内容
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
							"数据下载失败", Toast.LENGTH_SHORT)
							.show();
					File f = new File("/sdcard/mapuni/MobileEnforcement/jcjl/"
							+ bundle.getString("rwbh"));
					f.delete();
					if (pd != null)
						pd.cancel();
					break;
				case COMPLETE_ALL:
					Toast.makeText(JCJLDetailActivity.this,
							"该文件以不存在", Toast.LENGTH_SHORT)
							.show();
					if (pd != null)
						pd.cancel();
					break;

				}
			}
		};
		// 给查询按钮添加监听事件
		downloadfj.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				File f = new File("/sdcard/mapuni/MobileEnforcement/jcjl/"
						+ bundle.getString("rwbh") + "/");
				if (f.exists()&& f.listFiles().length>0) {
					Tree(scrollView,allLayout,treeLayout,btnLayout);
					btnLayout.setVisibility(View.GONE);
				} else {
					if (Net.checkNet(JCJLDetailActivity.this)) {// 有网络才弹出

						pd = new ProgressDialog(JCJLDetailActivity.this);
						pd.setMessage("正在下载数据，请稍等...");
						pd.setCancelable(false);
						pd.show();
						HashMap<String, Object> fliterHashMap = new HashMap<String, Object>();
						fliterHashMap.put("TaskID",titleText);
						fliterHashMap.put("TypeId !", "05");
						ArrayList<HashMap<String, Object>> fjName = BaseClass.DBHelper
								.getOrderList("T_WRY_JCJLRWZX", fliterHashMap,"UpdateTime	asc");
						if(fjName.size()==0){
							pd.cancel();
							Toast.makeText(JCJLDetailActivity.this,"该任务无附件！", Toast.LENGTH_SHORT).show();
							return;
						}
						Progressdialog(fjName, bundle.getString("rwbh"));
					} else {
						if(pd != null)
							pd.cancel();
						Toast.makeText(JCJLDetailActivity.this,"网络连接失败，请检查网络设置！", Toast.LENGTH_SHORT).show();
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
					Toast.makeText(JCJLDetailActivity.this, "网络不通，无法查看清单!", Toast.LENGTH_SHORT).show();
					return;
				}
				Intent _Intent=new Intent(JCJLDetailActivity.this, TaskImageType.class);
				_Intent.putExtra("TaskId", rwbh);
				startActivity(_Intent);	
			}
		});

	}

	// 循环下载每个附件
	void Progressdialog(
			final ArrayList<HashMap<String, Object>> fjName,
			final String content) {

		new Thread(new Runnable() {
			@Override
			public void run() {
				// 遍历list得到文件名	
				int successcount = 0;
						try {							
							boolean issuccess;
							for(HashMap<String, Object> jcjlmap:fjName){
								String filename =jcjlmap.get("docpath").toString();
								String path = (String) filename.substring(filename.indexOf(".") + 1,filename.length());
								// 判断文件是那种格式								
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

	// 装载树的方法
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
				filter.put("TypeId", "01"); // 执行文档
			}
			if (i == 1) {
				filter.remove("TypeId");
				filter.put("TypeId !", "01");
				filter.put("TypeId !", "05");
			}
			ArrayList<HashMap<String, Object>> treezxwd = BaseClass.DBHelper
					.getOrderList(jcjlTreetablename, filter, "FBSJ asc");
			// 将解析出来的数据遍历加载到树上
			it = treezxwd.iterator();
			while (it.hasNext()) {
				jcjltreemap = it.next();
				set = jcjltreemap.keySet().iterator();
				while (set.hasNext()) {
					/**2012年10月16日修改备份如下
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
				TreeViewAdapter.ItemHeight * rowcount));// 树的布局参数

		allLayout.addView(treeLayout);
		scrollView.addView(allLayout);
		btnLayout.setVisibility(View.GONE);
		// 给树添加单击事件
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

	// 下载附件的方法
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
								+ content + "/");// 如果没有目录先建立目录
				if (!f.exists())
					f.mkdirs();
				File fil = new File(
						"/sdcard/mapuni/MobileEnforcement/jcjl/"
								+ content + "/" + wjName);// 有目录之后建文件
				fos = new FileOutputStream(fil);
				byte[] bytes = new byte[1024];
				int flag = -1;

				while ((flag = in.read(bytes)) != -1) {// 若未读到文件末尾则一直读取
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
