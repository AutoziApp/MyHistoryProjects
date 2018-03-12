package com.mapuni.android.enforcement;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
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
import com.mapuni.android.base.interfaces.PathManager;
import com.mapuni.android.base.util.ExceptionManager;
import com.mapuni.android.business.RWXX;
import com.mapuni.android.business.TreeNode;
import com.mapuni.android.dataprovider.FileHelper;
import com.mapuni.android.dataprovider.SQLiteDataProvider;
import com.mapuni.android.dataprovider.SqliteUtil;
import com.mapuni.android.uitl.PanduanDayin;

/**
 * FileName: TaskImageType.java Description:�����嵥ͼƬ��ӡ <br>
 * 1.JCJLDetailActivity ����¼->�嵥��ӡ <br>
 * 2.TaskListActivity �����б�->�嵥��ӡ<br>
 * 3.HDJcdActivity �嵥���->�嵥��ӡ
 * 
 * @author Liusy
 * @Version 1.3.4
 * @Copyright �п���ͼ���¿Ƽ����޹�˾<br>
 *            Create at: 2012-12-4 ����11:44:40
 */
public class TaskImageType extends BaseActivity {
	/** ��ʾ��� */
	private LinearLayout middlelayout;
	/** ��ǰ������ */
	private String taskBH;
	/** ���صȴ� */

	private YutuLoading yutuLoading;
	/** �б������� */
	private ListViewAdapter myAdapter;

	/** ͼƬ�ļ����� */
	private ArrayList<File> fileList;
	/** ͼƬ����·�� */
	private String taskPath = PathManager.SDCARD_RASK_DATA_PATH + "ImgDoc/";
	/** ��ҳʱ�ı�ʶ */
	private int flag = 1;
	/** ��Ϣ���� */
	private Handler mHandler;
	/** ������Ϣ״̬ */
	private final int LOAD_SUCCESS = 0;
	private final int LOAD_FAILURE = 1;

	private final boolean isMorePage = true;

	private final String TAG = "TaskImageType";
	private int _page = 1;// ҳ��
	private String guid = "";// ��ҵguid
	private String industryID = "";// ��ҵ��ģ�壩����
	private String surveyDate = Global.getGlobalInstance().getDate("yyyy-MM-dd");

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.ui_mapuni);
		// flag = 1;
		// ��ȡ��һҳ�洫����Ϣ
		Intent _Intent = getIntent();
		taskBH = _Intent.getStringExtra("TaskBH");
		// qydm = getQYBH(taskBH);
		guid = _Intent.getStringExtra("guid");
		industryID = _Intent.getStringExtra("industryID");
		fileList = new ArrayList<File>();
		getViews();
		mHandler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				switch (msg.what) {
				case LOAD_SUCCESS:
					/*
					 * if(newQDDY){ getTaskImage(fileList);
					 * 
					 * }
					 */
					myAdapter.notifyDataSetChanged();
					break;
				case LOAD_FAILURE:
					Toast.makeText(TaskImageType.this, "��ӡ��Դ����ʧ�ܣ�", Toast.LENGTH_LONG).show();
					finish();
					break;
				}

				if (yutuLoading != null) {
					yutuLoading.dismissDialog();
				}

			}
		};
		// ִ���첽��������
		WorkAsyncTask task = new WorkAsyncTask(this);
		task.execute("");

	}

	/**
	 * Description: ��ʼ������
	 * 
	 * @author Administrator<br>
	 *         Create at: 2012-12-4 ����11:56:20
	 */
	private void getViews() {
		RelativeLayout rl = (RelativeLayout) findViewById(R.id.parentLayout);
		SetBaseStyle(rl, "�嵥��ӡ");
		setTitleLayoutVisiable(true);
		middlelayout = (LinearLayout) findViewById(R.id.middleLayout);
		ListView imgListView = new ListView(this);
		imgListView.setCacheColorHint(Color.TRANSPARENT);
		imgListView.setItemsCanFocus(true);
		LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.FILL_PARENT);
		imgListView.setLayoutParams(layoutParams);
		imgListView.setDivider(getResources().getDrawable(R.drawable.list_divider));
		// imgListView.setOnItemSelectedListener(onItemSelectedListener);

		myAdapter = new ListViewAdapter(this);
		imgListView.setAdapter(myAdapter);
		middlelayout.addView(imgListView);

	}

	/**
	 * FileName: TaskImageType.java Description:������������
	 * 
	 * @author Administrator
	 * @Version 1.3.4
	 * @Copyright �п���ͼ���¿Ƽ����޹�˾<br>
	 *            Create at: 2012-12-4 ����11:56:34
	 */
	private class ListViewAdapter extends BaseAdapter {
		private ImageView mHeadImageView;
		private TextView mTitleTextView;
		private ImageButton mPreviewImageView;
		ImageButton bluetooth_imageView;
		private Context context;

		public ListViewAdapter(Context context) {
			this.context = context;
		}

		@Override
		public int getCount() {
			return fileList.size();
		}

		@Override
		public Object getItem(int arg0) {
			return null;
		}

		@Override
		public long getItemId(int arg0) {
			return 0;
		}

		@Override
		public View getView(final int position, View arg1, ViewGroup arg2) {
			LayoutInflater lf = LayoutInflater.from(context);
			View view = lf.inflate(R.layout.site_evidence_list_element, null);
			// ͷ��
			mHeadImageView = (ImageView) view.findViewById(R.id.siteevidence_label_imageView);
			mHeadImageView.setImageResource(R.drawable.icon_image);
			// �ı�
			mTitleTextView = (TextView) view.findViewById(R.id.siteevidence_medianame_textView);
			String nm = fileList.get(position).getName();
			mTitleTextView.setText(nm);
			// ����ͼ��
			mPreviewImageView = (ImageButton) view.findViewById(R.id.siteevidence_preview_imageView);
			mPreviewImageView.setVisibility(View.GONE);

			// ɾ������ͼ��
			/*
			 * ImageView mDeleteImageView = (ImageView)
			 * view.findViewById(R.id.siteevidence_delete_imageView);
			 * //mDeleteImageView.setVisibility(View.GONE);
			 * mDeleteImageView.setOnClickListener(new View.OnClickListener() {
			 * 
			 * @Override public void onClick(View v) {
			 * 
			 * if(fileList.get(position).exists()){
			 * fileList.get(position).delete(); } } });
			 */
			/*
			 * //����������ͼ�� ImageView mRenameImageView = (ImageView)
			 * view.findViewById(R.id.siteevidence_rename_imageView);
			 * mRenameImageView.setVisibility(View.GONE);
			 */
			bluetooth_imageView = (ImageButton) view.findViewById(R.id.siteevidence_bluetooth_imageView);
			mPreviewImageView.setOnClickListener(new OnClickListener() {// Ԥ��
						@Override
						public void onClick(View arg0) {
							if (FileHelper.getFileType(fileList.get(position)).equals("png")) {
								FileHelper.OpenFile(FileHelper.FileType.IMAGE.getType(), "", false, context, fileList.get(position));
							} else if (FileHelper.getFileType(fileList.get(position)).equals("html")) {
								/*
								 * Intent intent =new
								 * Intent(TaskImageType.this,WebViewFormActivity
								 * .class); intent.putExtra("url",
								 * fileList.get(position)+"");
								 * intent.putExtra("title",
								 * fileList.get(position)+"");
								 */

								String url = "file://" + fileList.get(position).getAbsolutePath();
								Intent intent = new Intent(TaskImageType.this, MapuniHtmlActivity.class);
								intent.putExtra("url", url + "?totalPageCount=" + (_page - 1));
								intent.putExtra("imageUri", taskPath + taskBH);
								intent.putExtra("key", "0");
								startActivity(intent);
							}
							// else Toast.makeText(context, "�ļ���������ȷ",
							// Toast.LENGTH_LONG).show();
						}
					});

			bluetooth_imageView.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					/*
					 * String
					 * uri="file:///mnt"+fileList.get(position).getAbsolutePath
					 * (); FileHelper.sendToBluetooth(uri, context);
					 */
					String url = fileList.get(position).getAbsolutePath();
					Intent intent = new Intent(TaskImageType.this, MapuniHtmlActivity.class);

					String path3 = url;
					// Log.i("a", path3);
					if (!PanduanDayin.appIsInstalled(TaskImageType.this, "com.dynamixsoftware.printershare")) {
						PanduanDayin.insatll(TaskImageType.this);
					} else {
						PanduanDayin.startprintshare(TaskImageType.this, path3);
					}

					/*
					 * 
					 * 
					 * Intent intent3 = new Intent(); ComponentName comp3 = new
					 * ComponentName("com.dynamixsoftware.printershare",
					 * "com.dynamixsoftware.printershare.ActivityWeb"); intent =
					 * new Intent(); intent.setComponent(comp);
					 * intent.setAction("android.intent.action.VIEW");
					 * intent.setType("application/ms-word"); String
					 * s=spinner.getSelectedItem().toString(); LO
					 * 
					 * String path=Global.SITELAWRECORD_PATH; Log.i("ac",
					 * path+s); File pdf1=new File(path+"/"+s+".doc");
					 * Log.i("ac", path+s); intent.setData(Uri.fromFile(pdf1));
					 * 
					 * intent3 = new Intent(); intent3.setComponent(comp3);
					 * intent3.setAction("android.intent.action.VIEW");
					 * intent3.setType("text/html"); String
					 * s=spinner.getSelectedItem().toString();
					 * 
					 * String path=Global.SITELAWRECORD_PATH; Log.i("ac",
					 * path+s);
					 * 
					 * String path3=url+"?totalPageCount="+(_page-1)+".html";
					 * File pdf3=new File(path3); // Log.i("ac", path+s);
					 * intent3.setData(Uri.fromFile(pdf3));
					 * startActivity(intent3);
					 */

					/*
					 * intent.putExtra("url", url+"?totalPageCount="+(_page-1));
					 * intent.putExtra("imageUri", taskPath + taskBH);
					 * intent.putExtra("key", "1"); startActivity(intent);
					 */
				}
			});
			return view;
		}
	}

	/**
	 * FileName: TaskImageType.java Description:�첽�����嵥��ӡͼƬ�б�
	 * 
	 * @author Administrator
	 * @Version 1.3.4
	 * @Copyright �п���ͼ���¿Ƽ����޹�˾<br>
	 *            Create at: 2012-12-4 ����11:55:45
	 */
	private class WorkAsyncTask extends AsyncTask<String, Integer, String> {
		// Ҷ�ӽڵ㼯��
		ArrayList<TreeNode> leavesList = new ArrayList<TreeNode>();
		// ���ڵ㼯��
		private List<TreeNode> rootNodeList = new ArrayList<TreeNode>();
		private List<TreeNode> tempRootNodeList = new ArrayList<TreeNode>();

		private int leavesLevel = 1;// Ҷ�ӽڵ����󼶱�ֵ
		private StringBuffer allHtml = new StringBuffer("");

		// ==============================�����ֵ======================================
		// ���߶�
		int _TableHeight = 900;
		int _RowHeight = 30;

		// �п��
		// int _TypeWidth = 300;
		// int _JudeWidth = 444;
		// int _ResultWidth = 140;
		// int _RemarkWidth = 110;
		int _TypeWidth = 200;
		int _JudeWidth = 444;
		int _ResultWidth = 250;
		int _RemarkWidth = 100;

		// ������
		int _step = 0;

		// �ж�������ʾ��������
		int _RowSize = 60;
		String _TempStr = "";

		String filePath = taskPath + taskBH;

		public WorkAsyncTask(Context context) {
			yutuLoading = new YutuLoading(context);
			yutuLoading.setLoadMsg("���������У����Ե�...", "");
			yutuLoading.showDialog();
		}

		@Override
		protected String doInBackground(String... params) {

			// Build HTML Task Result List
			ArrayList<HashMap<String, Object>> rwxxList = getRWXX(taskBH);
			if (rwxxList == null || rwxxList.size() == 0) {
				return "";
			}
			// ��ȡ�������Ϣ
			// ģ����
			// guid = rwxxList.get(0).get("guid").toString();
			leavesLevel = getMaxLevel();
			if (leavesLevel <= 0) {
				mHandler.sendEmptyMessage(LOAD_FAILURE);
				return "������û��ģ�壡";
			}
			// ģ����Ϣ
			// 1.��ȡ���е�Ҷ�ӽ��
			leavesList = getLeavesNodeList(industryID);

			// 2.����ÿһ��Ҷ�ӽڵ�,��ȡ����� tempRootNodeList
			for (TreeNode node : leavesList) {
				node.rwID = taskBH;
				getRootNode(node);
			}
			for (TreeNode nodeRoot : tempRootNodeList) {
				// ��ÿһ�����ڵ�����ӽڵ�
				nodeRoot.rwID = taskBH;
				setChildNode(nodeRoot);
			}
			// �Ƴ��սڵ�
			removeEmptyTypeNode(tempRootNodeList);
			// ��������
			Collections.sort(tempRootNodeList);

			// ͷ��
			allHtml.append(getCssString());// ��ʽ
			allHtml.append(getCharsetString());// �ַ���
			allHtml.append(getQyMbTitle());// ͷ��
			// ��Ҫ�嵥���
			getCheckResult();// ���
			// �ײ�
			allHtml.append(getBottom());// �ײ���Ϣ
			// д�ļ�
			writeFile(allHtml.toString(), filePath);

			fileList = FileHelper.getAllHtmlFiles(filePath);

			for (int i = 0; i < fileList.size(); i++) {

				replaceWord(fileList.get(i), "{totalpage}", String.valueOf(fileList.size()));
			}

			fileList = FileHelper.getAllHtmlFiles(filePath);
			if (fileList.size() >= 1) {
				mHandler.sendEmptyMessage(LOAD_SUCCESS);
			} else {
				mHandler.sendEmptyMessage(LOAD_FAILURE);
			}
			allHtml = null;

			mHandler.sendEmptyMessage(LOAD_SUCCESS);
			return "";
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
		}

		/**
		 * Description:���ؼ������뱸ע <li>�ݹ���ظ��ڵ�
		 * 
		 * @param resultHtml
		 * @param rootNode
		 * @author Administrator<br>
		 *         Create at: 2013-1-30 ����11:12:13
		 */

		private void getLasttds(TreeNode node) {
			// �ж��Ƿ���Ҷ�ӣ�1��Ҷ�ӣ�0����Ҷ��
			if (node.isContent == null) {
				return;
			}

			if (node.isContent.equals("1")) {
				// �Խ�����м���
				LastResult result = new LastResult(node.rwID, node.qyID, node.TID, node.SpecialItemID);// ����Ҷ�ӣ���ȡ���
				final LastResult lastResult = getLastResult(node.rwID, node.qyID, node.SpecialItemID, result);
				// �ı����ڣ���Ϊ��
			
				if (node.title != null && node.title.length() > 0) {
					// ֵ������
					if (node.ValueInColumn.equals("1")) {
						allHtml.append("<td class='td'>" + node.title + lastResult.SpecialItemResult + "</td>");
					} else {
						allHtml.append("<td class='td'>" + node.title + "</td>");
						// װ�ؽ��ֵ
						allHtml.append(GetCheckBox(node, lastResult));
						// ���ر�ע
						String remarkStr = lastResult.Remark;
						remarkStr = (remarkStr == null ? "" : remarkStr);
						// allHtml.append("<td class='td'  width='60px'><input class='textborder' width='60px' type='text' id='"
						// + node.SpecialItemID + "' name='Remark' value='" +
						// remarkStr + "'></td>");
						// allHtml.append("<td class='td'  width='60px'>" +
						// "<textarea id='"
						// + node.SpecialItemID +
						// "' readOnly='true' width='60px'  style='overflow-y:visible;border-style:none;'>"
						// +
						// remarkStr+"</textarea></td>");
						allHtml.append("<td class='td'  width='60px'>" + remarkStr + "</td>");
						
					
						// ��ע
					}
				}
				_step++;
				allHtml.append("</tr>");

				// ��ҳ
				if (isMorePage && _step * _RowHeight >= _TableHeight) {
					_step = 0;
					allHtml.append(getBottom());// �ײ���Ϣ
					writeFile(allHtml.toString(), filePath);// �����ļ�
					// ���
					allHtml.delete(0, allHtml.length());
					// ͷ��
					allHtml.append(getCssString());// ��ʽ
					allHtml.append(getCharsetString());// �ַ���
					// allHtml.append(getQYJBXXHeader(guid));//��ҵ��Ϣ
					allHtml.append(getClomnHtml(leavesLevel));

					// ����
					// 1.��ǰ��Ҷ�������
					// 2.��ǰҶ��û�ꡣ
					// ��ȡ��ǰҶ�Ӹ��ڵ�
					// ��ȡ��ǰ�ĸ�
					TreeNode parentNode = node.getParent();
					if (parentNode != null) {

						List<TreeNode> nodeList = parentNode.getChildren();
						/*
						 * for(int i = 0; i < nodeList.size(); i++) {
						 * if(nodeList.get(i).equals(node)) {//�ҵ���ǰ��
						 * allHtml.append("<tr>"); //�ж��Ƿ�������Ҷ�� if(i <
						 * nodeList.size() - 1) {//�������һ��Ⱥϲ����
						 * allHtml.append("<td class=\"td\" rowspan='" +
						 * (nodeList.size() - i) + "' colspan='" + (leavesLevel
						 * - Integer.parseInt(node.SpecialItemLevel)) + "'>" +
						 * node.title + "</td>"); } } }
						 */

						for (int i = 0; i < nodeList.size(); i++) {
							if (nodeList.get(i).equals(node)) {// �ҵ���ǰ��

								if (nodeList.size() - 1 != i) {// �ж��ǲ������һ��Ҷ�ӣ����������parentNode���
									allHtml.append("<tr>");
									int surplusCount = nodeList.size() - 1 - i;
									try {
										if (Integer.parseInt(node.SpecialItemLevel) == 3) {
											int rows = 0;
											int nodeSortIndex = Integer.parseInt(node.getParent().sortIndex);
											TreeNode grandpa = parentNode.getParent();
											List<TreeNode> nList = grandpa.getChildren();
											for (TreeNode tn : nList) {
												if (Integer.parseInt(tn.sortIndex) > nodeSortIndex) {
													rows += tn.getChildren().size();
												}
											}
											rows += surplusCount;

											allHtml.append("<td class=\"td\" rowspan='" + rows + "' colspan='1'>" + parentNode.getParent().title + "</td>");
											allHtml.append("<td class=\"td\" rowspan='" + surplusCount + "' colspan='" + (leavesLevel - Integer.parseInt(node.SpecialItemLevel))
													+ "'>" + parentNode.title + "</td>");
										} else if (Integer.parseInt(node.SpecialItemLevel) == 2) {
											// ���������޸ġ�
											if ("a02b86ef-0e1f-47a8-92c0-db0333".equals(industryID)
											 // ||
											 //"ee1125e4-d1bd-4e78-9da4-0d548f".equals(industryID)||
											 //"47816749-ac58-4375-8ae4-c48e95".equals(industryID)
											) {
												allHtml.append("<td class=\"td\" rowspan='" + surplusCount + "' colspan='"
														+ (leavesLevel - Integer.parseInt(node.SpecialItemLevel)) + "'>" + parentNode.title + "</td>");
											} else {
												allHtml.append("<td class=\"td\" rowspan='" + surplusCount + "' colspan='2'>" + parentNode.title + "</td>");
											}

										}

									} catch (Exception e) {
										mHandler.sendEmptyMessage(LOAD_FAILURE);
										ExceptionManager.WriteCaughtEXP(e, "TaskImageType");
										return;
									}
								} else {
									if ("ee1125e4-d1bd-4e78-9da4-0d548f".equals(industryID) || "47816749-ac58-4375-8ae4-c48e95".equals(industryID)) {
										int surplusCount = nodeList.size() - 1 - i;
										int rows = 0;
										int nodeSortIndex = Integer.parseInt(node.getParent().sortIndex);
										TreeNode grandpa = parentNode.getParent();
										List<TreeNode> nList = grandpa.getChildren();
										for (TreeNode tn : nList) {
											if (Integer.parseInt(tn.sortIndex) > nodeSortIndex) {
												rows += tn.getChildren().size();
											}
										}
										rows += surplusCount;
										allHtml.append("<td class=\"td\" rowspan='" + rows + "' colspan='1'>" + parentNode.getParent().title + "</td>");
										// allHtml.append("<td class=\"td\" rowspan='"
										// + surplusCount
										// + "' colspan='1'>" + parentNode.title
										// + "</td>");
									}
								}

							}
						}
					}
				}
				// ����Ҷ�ӣ��Ӹ���ʼ��
			} else {
				// ��ǰ�����ӽڵ�����
				int childCount = node.children.size();

				// �ж��Ƿ������Ƿ���Ҷ�ӣ���Ҷ����ϲ����rowspan
				if (node.SpecialItemLevel.equals("1")) {
					allHtml.append("<tr>");
				}
				if (IsChildHaveContent(node.SpecialItemID)) {
					// Log.i("SS",
					// "Maybe OOM here at the third time -- >"+i_count++);
					try {
						int count = childCount;
						if ((_step + childCount) * _RowHeight >= _TableHeight + 61) {// ���ڴ˽ڵ��ҳ
							int a = _TableHeight - _step * _RowHeight;// ʣ��ĸ߶�
							Log.i("info", "ʣ��ĸ߶ȣ�" + a);
							if (a % _RowHeight == 0) {
								count = a / _RowHeight + 1;
							} else {
								count = a / _RowHeight + 1;
							}
							flag++;

						}
						allHtml.append("<td  width=\"70px\" class=\"td\" rowspan='" + count + "' colspan='" + (leavesLevel - Integer.parseInt(node.SpecialItemLevel)) + "'>"
								+ node.title + "</td>");
					} catch (Exception e) {
						mHandler.sendEmptyMessage(LOAD_FAILURE);
						ExceptionManager.WriteCaughtEXP(e, "TaskImageType");
						return;
					}
				} else {// �������Ҷ�ӣ����н��
					if (!node.SpecialItemLevel.equals("2")) {
						childCount = 0;
						List<TreeNode> allChildren = node.children;
						for (TreeNode tn : allChildren) {
							childCount += tn.children.size();
						}

					}
					String nodeTitle = node.title;

					allHtml.append("<td width=\"70px\" class=\"td\" rowspan='" + childCount + "' colspan='1'>" + nodeTitle + "</td>");
				
				}
				// ѭ���ӽڵ㣬�ݹ�
				for (TreeNode childNode : node.children) {
					getLasttds(childNode);
				}
			}
		}

		/**
		 * Description:�����嵥�����HTML
		 * 
		 * @return
		 * @author Administrator<br>
		 *         Create at: 2013-1-28 ����04:58:53
		 */
		private void getCheckResult() {
			// ���ڵ�����
			int rootNodeCount = tempRootNodeList.size();
			// StringBuffer resultHtml = new StringBuffer("");
			// resultHtml.append(getTop(leavesLevel));//������HTML��ok��
			allHtml.append(getClomnHtml(leavesLevel));// ������HTML

			// һ���ڵ��ѭ��
			for (int i = 0; i < rootNodeCount; i++) {
				TreeNode rootNode = tempRootNodeList.get(i);
				// �ж��Ƿ���Ҷ��(0��Ҷ�ӣ�1����Ҷ��)
				// StringBuffer sb = new StringBuffer("");
				String nodeName = rootNode.title;
				getLasttds(rootNode);
			}
		}

		/**
		 * Description:�ж������Ƿ�������
		 * 
		 * @param itemid
		 * @return
		 * @author Administrator<br>
		 *         Create at: 2013-1-30 ����01:25:09
		 */
		private boolean IsChildHaveContent(String itemid) {
			// String sql =	 //	�޸� ����ɸѡ���� where Status = '1'  _byk
			// "SELECT id FROM YDZF_SpecialItem WHERE pid='"+itemid+"' and IsContent='1'";
			String sql = "SELECT * FROM YDZF_SpecialItem a, YDZF_TemplateSpacialItem b WHERE a.pid='" + itemid + "'"
					+ "  and a.IsContent='1'  and a.ID=b.SpecialItemID and b.TID='" + industryID + "' order by a.sortindex";
			ArrayList<HashMap<String, Object>> data = null;
			data = SQLiteDataProvider.getInstance().queryBySqlReturnArrayListHashMap(sql);

			return data.size() >= 1 ? true : false;
		}

		/**
		 * Description:��������
		 * 
		 * @param node
		 * @param lastResult
		 * @return
		 * @author Administrator<br>
		 *         Create at: 2013-1-30 ����01:05:40
		 */
		private String GetCheckBox(TreeNode node, LastResult lastResult) {
			StringBuilder str = new StringBuilder();
			str.append("<td class=\"td\">");
			// ��ȡ�������
			// �ǡ���[һ�ࡢ���ࡢ���ࡢ����]ID,RName,ControlType
			final ArrayList<HashMap<String, Object>> resultType = getResultTypeData(node.ResultTypeID);
			if (resultType == null || resultType.isEmpty()) {
				str.append("</td>");
				return str.toString();
			}
			String contrlType = (String) resultType.get(0).get("controltype");

			if (contrlType.contains("Check")) {
				// ѭ����CheckBox
				for (HashMap<String, Object> map : resultType) {
					String resultText = (String) map.get("rname");
					String resultID = (String) map.get("id");
					if (resultText != null && resultText.length() > 0) {
						String lastResultText = lastResult.Text;
						if (lastResultText == null) {
							lastResultText = "";
						}
						if (resultText.equals(lastResultText)) {
							// str.append(resultText
							// + "<input id='"
							// + resultID
							// + "' checked='checked'  name='"
							// + node.SpecialItemID
							// + "' onClick=\"chooseOne(this,'"
							// + node.SpecialItemID
							// +
							// "');\" type='checkbox' style=\"height:18px;  width:18px\" />");

							str.append(lastResultText);
							if (resultText.length() > 4 || resultType.size() > 2) {
								str.append("</br>");
							}
							break;
						} else {
							// str.append(resultText
							// + "<input  id='"
							// + resultID
							// + "' name='"
							// + node.SpecialItemID
							// + "' onClick=\"chooseOne(this,'"
							// + node.SpecialItemID
							// +
							// "');\" type='checkbox' style=\"height:18px;  width:18px\" />");
							str.append(lastResultText);
							if (resultText.length() > 4 || resultType.size() > 2) {
								str.append("</br>");
							}
							break;
						}
					} else {
						str.append(resultText + "<input id='" + resultID + "' name='" + node.SpecialItemID + "' onClick=\"chooseOne(this,'" + node.SpecialItemID
								+ "');\" type='checkbox' style=\"height:18px;  width:18px\" />");
						if (resultText.length() > 4 || resultType.size() > 2) {
							str.append("</br>");
						}
					}
				}
			} else if (contrlType.contains("Text")) {
				// ��дText
				String resultText = lastResult.SpecialItemResult;
				String resultID = (String) resultType.get(0).get("id");
				if (resultText != null && resultText.length() > 0) {
					str.append(resultText);
				} else {
					str.append("");
				}
			}
			str.append("</td>");
			return str.toString();
		}

		/**
		 * ��ѯ���ݿ� ��ȡ�������ͣ��ı�����ѡ��
		 * 
		 * @param resultTypeID
		 *            �������ID
		 * @author Administrator<br>
		 *         Create at: 2012-12-4 ����11:20:56
		 */
		private ArrayList<HashMap<String, Object>> getResultTypeData(String resultTypeID) {
			ArrayList<HashMap<String, Object>> data = new ArrayList<HashMap<String, Object>>();
			String sql = "";
			sql = "select ID,RName,ControlType from YDZF_ResultType where PID = '" + resultTypeID + "' order by sortIndex ";
			data = SQLiteDataProvider.getInstance().queryBySqlReturnArrayListHashMap(sql);

			if (data.size() == 0) {
				sql = "select ID,RName,ControlType from YDZF_ResultType where ID = '" + resultTypeID + "' order by sortIndex ";

				data = SQLiteDataProvider.getInstance().queryBySqlReturnArrayListHashMap(sql);

				return data;
			}
			return data;
		}

		/**
		 * ��ѯ���ݿ� ͨ������ID��ר��ID����ȡ�ϴν��
		 * 
		 * @param qyID
		 *            ��ҵID
		 * @return ��ע��Ϣ
		 * @author Administrator<br>
		 *         Create at: 2012-12-4 ����11:20:56
		 */
		private LastResult getLastResult(String rwID, String qyID, String specialItemID, LastResult lastResult) {
			ArrayList<HashMap<String, Object>> data = new ArrayList<HashMap<String, Object>>();
			String sql = "SELECT * FROM YDZF_TaskSpecialItem WHERE TaskID ='" + rwID + "' and EnterID = '" + qyID + "' and IndustryID ='" + lastResult.IndustryID
					+ "' and SpecialItemID ='" + specialItemID + "'";
			// Log.i(TAG, sql);

			data = SQLiteDataProvider.getInstance().queryBySqlReturnArrayListHashMap(sql);

			if (data.size() > 0) {
				for (HashMap<String, Object> map : data) {
					lastResult.SpecialItemResult = (String) map.get("specialitemresult");
					lastResult.Remark = (String) map.get("remark");
				}
				// �ϴ�ѡ����Ϣ���ı�
				lastResult.Text = getLastTaskResultText(rwID, qyID, specialItemID, lastResult);
				lastResult.flag = true;
			} else {
				lastResult.flag = false;
			}
			return lastResult;
		}

		/**
		 * ��ѯ���ݿ� ͨ������ID��ר��ID����ȡ���
		 * 
		 * @param qyID
		 *            ��ҵID
		 * @return �ϴ�ѡ����Ϣ���ı�
		 * @author Administrator<br>
		 *         Create at: 2012-12-4 ����11:20:56
		 */
		private String getLastTaskResultText(String rwID, String qyID, String specialItemID, LastResult lastResult) {
			String text = "";
			ArrayList<HashMap<String, Object>> data = new ArrayList<HashMap<String, Object>>();
			String sql = "SELECT RName FROM YDZF_ResultType WHERE ID = " + "(SELECT SpecialItemResult FROM YDZF_TaskSpecialItem WHERE TaskID ='" + rwID + "' and IndustryID ='"
					+ lastResult.IndustryID + "' and EnterID = '" + qyID + "'  and SpecialItemID ='" + specialItemID + "')";

			data = SQLiteDataProvider.getInstance().queryBySqlReturnArrayListHashMap(sql);

			for (HashMap<String, Object> map : data) {
				text = (String) map.get("rname");
			}
			return text;
		}

		/**
		 * Description:�б�������HTML:����ж����ݡ�����������ע
		 * 
		 * @param level
		 * @return
		 * @author Administrator<br>
		 *         Create at: 2013-1-30 ����10:57:29
		 */
		private String getClomnHtml(int level) {
			StringBuilder sb = new StringBuilder();
			sb.append("<div style='width:100%; height:1050px;'>");
			sb.append("<table width='100%' class='table'>");
			sb.append("<tr> <td class=\"td\" colspan=\"" + (level - 1) + "\" style='width:" + _TypeWidth + "px' >���</td><td class=\"td\" style='width:" + _JudeWidth
					+ "px' >�ж�����</td><td class=\"td\"  style='width:" + _ResultWidth + "px' >������</td><td class=\"td\" style='width:" + _RemarkWidth + "px'>��ע</td></tr>");
			return sb.toString();
		}

		/**
		 * Description:�б�������HTML:����ж����ݡ�����������ע
		 * 
		 * @param level
		 * @return
		 * @author Administrator<br>
		 *         Create at: 2013-1-30 ����10:57:29
		 */
		public String getTop(int level) {
			StringBuffer sb = new StringBuffer();
			sb.append("<table width='100%' border=\"1 black\" cellpadding=\"0\" cellspacing=\"0\" " + "style=\"border-collapse:collapse; border-color:black\">");
			sb.append("<tr> <td class=\"jcxtd\" colspan=\"" + (level - 1) + "\">" + "���</td><td class=\"jcxtd\">" + "�ж�����</td><td class=\"jcxtd\" width='120px'>"
					+ "������</td><td class=\"jcxtd\">" + "��ע</td></tr>");
			return sb.toString();
		}

		private String getBottom() {
			StringBuffer sb = new StringBuffer();
			sb.append("</table>");
			sb.append("<table   width='100%'  border=\"1 black\" cellpadding=\"0\" cellspacing=\"0\" "
					+ "style=\"border-collapse:collapse; border-color:black\"><tr height='80px'> <td style=\"width:100px\"  "
					+ "class=\"td\">��Ҫ˵�����������</td> <td  colspan=\"3\" class=\"td\"></td></tr>");
			sb.append("<tr align='left' height='50px'><td style=\"width:100px\"  class=\"td\">"
					+ "���Աǩ�֣�</td><td width=\"180px\" class=\"td\"></td> <td width=\"120px\" class=\"td\">" + "������ǩ�֣�</td> <td class=\"td\"></td> </tr></table>");
			sb.append(" <span style=\"float:right;margin-right:100px;\">���ʱ��:" + surveyDate + "</span><br />");

			sb.append("<div style='margin-top:10px;margin-bottom:50px; text-align:center'>" + "��" + _page + "ҳ" + "&nbsp;�� " + "{totalpage}" + " ҳ" + "</div>");

			// "<span id=\"divTotalPageCount\"></span></div><script>function getQueryString(name){var reg = new RegExp(\"(^|&)\"+ name +\"=([^&]*)(&|$)\"); var r = window.location.search.substr(1).match(reg); if (r!=null) return unescape(r[2]); return 1;} document.getElementById(\"divTotalPageCount\").innerHTML='��'+getQueryString(\"totalPageCount\")+'ҳ';</script>");
			return sb.toString();
		}

		/**
		 * Description:��ȡ��ǰģ������
		 * 
		 * @return
		 * @author Administrator<br>
		 *         Create at: 2013-1-30 ����10:48:08
		 */
		private int getMaxLevel() {
			String querySQl = "SELECT MAX(specialItemlevel) AS level FROM YDZF_TemplateSpacialItem WHERE tid = '" + industryID + "' ";
			int level = 0;

			ArrayList<HashMap<String, Object>> data = SQLiteDataProvider.getInstance().queryBySqlReturnArrayListHashMap(querySQl);
			if (data != null && !data.isEmpty()) {
				String levelStr = data.get(0).get("level").toString();
				if (levelStr.equals("")) {
					return 0;
				}
				level = Integer.parseInt(levelStr);
			}

			return level;
		}

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
				// Log.i(TAG, "��ǰ��  Content : " + content);

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
					// Log.i(TAG, "��Ӹ��ڵ㣺" + nodeFather.title);
				}
			}
		}

		/**
		 * Description:�Ƴ��սڵ�
		 * 
		 * @param nodeList
		 * @author Administrator Create at: 2012-12-4 ����09:59:52
		 */
		private void removeEmptyTypeNode(List<TreeNode> nodeList) {
			List<TreeNode> nodes = new ArrayList<TreeNode>();
			for (int i = 0; i < nodeList.size(); i++) {
				if (nodeList.get(i).children.size() > 0) {
					removeEmptyTypeNode(nodeList.get(i).children);
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
		 * Description:��ȡ���е�Ҷ�ӽڵ�
		 * 
		 * @param industryID
		 * @return
		 * @author Administrator<br>
		 *         Create at: 2013-1-28 ����04:38:06
		 */
		private ArrayList<TreeNode> getLeavesNodeList(String industryID) {
			String sql = "select * from YDZF_TemplateSpacialItem where TID = '" + industryID + "'";
			ArrayList<HashMap<String, Object>> data = null;
			ArrayList<TreeNode> nodeList = new ArrayList<TreeNode>();
			TreeNode node = null;

			data = SQLiteDataProvider.getInstance().queryBySqlReturnArrayListHashMap(sql);

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

				node.qyID = guid;
				node.rwID = taskBH;
				node.TID = tID;
				node.isContent = "1";// Ҷ�ӽ��
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
		 * Description: ��ѯ���ݿ� <li>����ר��ID����ȡ�丸�ڵ�
		 * 
		 * @param specialItemID
		 *            ר��ID
		 * @return ���ڵ�ļ���
		 * @author Administrator Create at: 2012-12-4 ����10:08:09
		 */
		private ArrayList<HashMap<String, Object>> getFatherItemData(String specialItemID) {
			ArrayList<HashMap<String, Object>> data = new ArrayList<HashMap<String, Object>>();
			String sql = getString(R.string.select_SpecialItem_FatherNode) + " '" + specialItemID + "') order by SortIndex ";

			data = SQLiteDataProvider.getInstance().queryBySqlReturnArrayListHashMap(sql);

			return data;
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

			String sql = "select b.* from YDZF_TemplateSpacialItem as a left outer join YDZF_SpecialItem as b on a.SpecialItemID = b.ID where b.PID = '" + id + "' and a.tid='"
					+ industryID + "' order by b.sortindex";

			// String sql =
			// "select * from YDZF_SpecialItem as c where PID = '"+id+"' and (select count(*) from ( select * from YDZF_SpecialItem as b left outer join YDZF_TemplateSpacialItem as a on a.SpecialItemID = b.ID where a.tid='"+industryID+"' and b.PID=c.id ))>0";

			// String sql =
			// "select * from YDZF_SpecialItem where PID = '"+id+"'";
			// Log.i(TAG, sql);

			data = SQLiteDataProvider.getInstance().queryBySqlReturnArrayListHashMap(sql);

			if (data.size() > 0) {
				// �����ӽڵ�
				node = new TreeNode();
				for (HashMap<String, Object> map : data) {

					String specialItemID = (String) map.get("id");
					String specialItemContent = (String) map.get("content");
					String isContent = (String) map.get("iscontent");
					String valueInColumn = (String) map.get("valueincolumn");
					String resultTypeID = (String) map.get("resulttypeid");
					String remarkTip = (String) map.get("remarktip");
					String specialItemLevel = (String) map.get("level");
					String sortIndex = (String) map.get("sortindex");

					// ר����Ϣ�ķ�װ
					// �����ӽڵ�
					node = new TreeNode(specialItemContent, "");

					node.qyID = guid;
					node.rwID = taskBH;
					node.TID = industryID;
					node.SpecialItemID = specialItemID;
					node.SpecialItemLevel = specialItemLevel;
					node.ValueInColumn = valueInColumn;
					node.ResultTypeID = resultTypeID;
					node.isContent = isContent;
					node.isLoadLastResult = "0";
					node.RemarkTip = remarkTip;
					node.sortIndex = sortIndex;

					// Log.i(TAG, "����ӽڵ㣺" + specialItemContent);
					nodeList.add(node);
				}
			}

			// sql = //	�޸� ����ɸѡ���� where Status = '1'  _byk
			// "select * from YDZF_SpecialItem as c where PID = '"+id+"' and (select count(*) from ( select * from YDZF_SpecialItem as b left outer join YDZF_TemplateSpacialItem as a on a.SpecialItemID = b.ID where a.tid='"+industryID+"' and b.PID=c.id ))>0";
			sql = "SELECT * FROM YDZF_SpecialItem WHERE IsContent='0' and PID='" + id + "' order by sortindex";

			// Log.i(TAG, sql);

			data = SQLiteDataProvider.getInstance().queryBySqlReturnArrayListHashMap(sql);

			if (data.size() > 0) {
				// �����ӽڵ�
				node = new TreeNode();
				for (HashMap<String, Object> map : data) {

					String specialItemID = (String) map.get("id");
					String specialItemContent = (String) map.get("content");
					String isContent = (String) map.get("iscontent");
					String valueInColumn = (String) map.get("valueincolumn");
					String resultTypeID = (String) map.get("resulttypeid");
					String remarkTip = (String) map.get("remarktip");
					String specialItemLevel = (String) map.get("level");
					String sortIndex = (String) map.get("sortindex");

					// ר����Ϣ�ķ�װ
					// �����ӽڵ�
					node = new TreeNode(specialItemContent, "");

					node.qyID = guid;
					node.rwID = taskBH;
					node.TID = industryID;
					node.SpecialItemID = specialItemID;
					node.SpecialItemLevel = specialItemLevel;
					node.ValueInColumn = valueInColumn;
					node.ResultTypeID = resultTypeID;
					node.isContent = isContent;
					node.isLoadLastResult = "0";
					node.RemarkTip = remarkTip;
					node.sortIndex = sortIndex;

					// Log.i(TAG, "����ӽڵ㣺" + specialItemContent);
					nodeList.add(node);
				}
			}
			return nodeList;
		}

		/**
		 * Description:дHTML�ļ�
		 * 
		 * @author Administrator<br>
		 *         Create at: 2013-1-28 ����03:46:29
		 */
		private void writeFile(String htmlInfo, String filePath) {

			File current_task_dir = new File(filePath);
			if (_page < 2) {
				RecursionDeleteFile(current_task_dir);
			}

			if (!current_task_dir.exists()) {
				current_task_dir.mkdirs();
			}
			try {
				File f = new File(filePath + "/" + "��" + _page + "ҳ" + ".html");
				_page++;
				// if (!f.exists()) {
				f.createNewFile();// �������򴴽�
				// }
				BufferedWriter output = new BufferedWriter(new FileWriter(f));
				output.write(htmlInfo);
				output.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}

	private ArrayList<HashMap<String, Object>> getRWXX(String rwbh) {
		ArrayList<HashMap<String, Object>> data = new ArrayList<HashMap<String, Object>>();
		String sql = "SELECT * FROM V_YDZF_RWXX WHERE rwbh = '" + rwbh + "'";

		data = SQLiteDataProvider.getInstance().queryBySqlReturnArrayListHashMap(sql);

		return data;
	}

	/**
	 * Description:��ȡͷ��������ҵ������Ϣ����HTML
	 * 
	 * @return
	 * @author Administrator<br>
	 *         Create at: 2013-1-28 ����03:47:29
	 */
	private String getQYJBXXHeader(String guid) {
		HashMap<String, Object> qyjbxxData = getQYJBXXData(guid);

		if (qyjbxxData.size() == 0) {
			qyjbxxData.put("qymc", "");
			qyjbxxData.put("frdb", "");
			qyjbxxData.put("qydz", "");
			qyjbxxData.put("yzbm", "");
			qyjbxxData.put("hblxr", "");
			qyjbxxData.put("frdbdh", "");

		}
		String tableName = getTableTitleName() + "��������ֳ�����¼��";
		StringBuffer sb = new StringBuffer();
		sb.append("<table width='100%' border='1px black' cellpadding=\"0\" cellspacing=\"0\" "
				+ "style=\"border-collapse:collapse;border-color:black;border-bottom:0; border:1px\">");
		sb.append("<tr><td class=\"style1\"  colspan=\"8\">" + tableName + "</td><tr>");
		// sb.append("<tr><td  colspan=\"8\" class=\"td\" >��ҵ������Ϣ </td> </tr> ");
		sb.append("<tr><td class=\"td\"  colspan=\"2\"> ��ҵ����</td> <td class=\"td\" colspan=\"2\"> " + qyjbxxData.get("qymc").toString() + "</td>");
		sb.append(" <td class=\"td\"  colspan=\"2\">��������������</td><td  class=\"td\" colspan=\"2\">" + qyjbxxData.get("frdb").toString() + "</td> </tr>");
		sb.append("<tr><td class=\"td\"  colspan=\"2\"> ��ϸ��ַ</td> <td class=\"td\"' colspan=\"2\">" + qyjbxxData.get("qydz").toString() + "</td>");
		sb.append(" <td class=\"td\"  colspan=\"2\">�ʱ�</td><td class=\"td\" colspan=\"2\">" + qyjbxxData.get("yzbm").toString() + "</td> </tr>");
		sb.append("<tr><td class=\"td\" colspan=\"2\"> ��ϵ��</td> <td class=\"td\" colspan=\"2\">" + qyjbxxData.get("hblxr").toString() + "</td>");
		sb.append(" <td class=\"td\"  colspan=\"2\">��ϵ�绰</td><td class=\"td\" colspan=\"2\">" + qyjbxxData.get("frdbdh").toString() + "</td> </tr>");
		sb.append("<tr><td class=\"td\"  colspan=\"2\"> �ڽ���Ŀ����</td> <td class=\"td\" " + "colspan=\"2\"></td>");
		sb.append(" <td class=\"td\"  colspan=\"2\">�ѽ���ĿͶ��ʱ��</td><td class=\"td\" " + "colspan=\"2\"></td> </tr>");
		sb.append("<tr><td class=\"td\"  colspan=\"2\"> �����Ա����</td> <td class=\"td\" colspan=\"2\">" + getCheckPeople() + "</td>");
		sb.append(" <td class=\"td\"  colspan=\"2\">���ʱ��</td><td class=\"td\" colspan=\"2\">" + getCheckTime() + "</td></tr>");
		sb.append("</table>");
		return sb.toString();
	}

	/**
	 * ��ȡ��ǰ��¼�˵���������
	 */
	private String getTableTitleName() {
		String result = "";
		String depid = Global.getGlobalInstance().getDepId();
		String sql = "select DepName from PC_DepartmentInfo where DepID='" + depid + "'";
		ArrayList<HashMap<String, Object>> data = SqliteUtil.getInstance().queryBySqlReturnArrayListHashMap(sql);
		if (data != null && data.size() > 0) {
			result = data.get(0).get("depname").toString();
		}
		return result;
	}

	/**
	 * Description:�����ʽ
	 * 
	 * @return
	 * @author Administrator<br>
	 *         Create at: 2013-2-19 ����09:32:41
	 */
	private String getCharsetString() {
		return "<meta http-equiv=\"Content-Type\"content=\"text/html; charset=utf-8\"/>";
	}

	/**
	 * ��ȡ�����Ա
	 * 
	 * @return
	 */
	private String getCheckPeople() {
		return new RWXX().getTaskExecutor(taskBH);
	}

	/**
	 * Description:��ȡ���ʱ��
	 * 
	 * @return
	 * @author Administrator<br>
	 *         Create at: 2013-1-28 ����03:47:56
	 */
	private String getCheckTime() {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy��MM��dd��  HHʱmm��");
		return dateFormat.format(new Date());
	}

	/**
	 * Description:��ȡ��ҵ������Ϣ����
	 * 
	 * @return Ĭ�Ϸ��ؿյ�map
	 * @author Administrator<br>
	 *         Create at: 2013-1-28 ����03:49:18
	 */
	private HashMap<String, Object> getQYJBXXData(String guid) {
		HashMap<String, Object> dataMap = new HashMap<String, Object>();
		String qyjbxxSql = "SELECT * FROM T_WRY_QYJBXX WHERE guid = '" + guid + "';";
		ArrayList<HashMap<String, Object>> dataList = SqliteUtil.getInstance().queryBySqlReturnArrayListHashMap(qyjbxxSql);
		if (dataList != null && dataList.size() > 0) {
			dataMap = dataList.get(0);
		}
		return dataMap;
	}

	private String getQYBH(String rwbh) {
		String qybh = "";
		String qyjbxxSql = "SELECT qydm FROM T_YDZF_RWXX WHERE rwbh = '" + taskBH + "'";
		ArrayList<HashMap<String, Object>> dataList = SqliteUtil.getInstance().queryBySqlReturnArrayListHashMap(qyjbxxSql);
		if (dataList != null && dataList.size() > 0) {
			qybh = dataList.get(0).get("qybh").toString();
		}
		return qybh;
	}

	/**
	 * FileName: SpecialItemActivity.java Description:�ϴν���Ķ���
	 * 
	 * @author Administrator
	 * @Version 1.3.4
	 * @Copyright �п���ͼ���¿Ƽ����޹�˾<br>
	 *            Create at: 2012-12-4 ����11:22:57
	 */
	private final class LastResult {
		public String TaskID; // ������
		public String EnterID; // ��ҵ���
		public String IndustryID; // ��ҵ���
		public String SpecialItemID; // ר����
		public String SpecialItemResult; // ������ͱ�Ż����ı�
		public String Text; // ����ı�
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

	/**
	 * Description:CSS��ʽ�ű�
	 * 
	 * @return
	 * @author Administrator<br>
	 *         Create at: 2013-1-28 ����03:49:33
	 */
	private static String getCssString() {
		String scc = " <style type=\"text/css\">" + ".style1{ " + "color: windowtext;" + "font-size: 30px;" + "font-weight: 700;" + "font-style: normal;"
				+ "text-decoration: none;" + "font-family: ����;" + "text-align: center;" + "border-left-style: none;" + "border-left-color: inherit;" + "border-left-width: medium;"
				+ "border-right-style: none;" + "border-right-color: inherit;" + "border-right-width: medium;" + "border-top-style: none;" + "border-top-color: inherit;"
				+ "border-top-width: medium;" + "border-bottom: .5pt solid windowtext;" + "padding-left: 1px;" + "padding-right: 1px;" + "padding-top: 1px;}" + ".jctd {"
				+ "color: windowtext;" + "font-size: 20.0pt;" + "font-weight: 400;" + "font-style: normal;" + "text-decoration: none;" + "font-family: ����;" + "text-align: left;"
				+ "vertical-align: middle;" + "border: .5pt solid windowtext;" + "padding-left: 1px;" + "padding-right: 1px;" + "padding-top: 1px;}" + ".table {"
				+ "border: 1px solid #000;" + "border-width: 1px 0 0 1px;" + "margin: 0 0 0 0;" + "text-align: center;" + "border-collapse: collapse;}" + ".td {"
				+ "border: 1px solid #000;" + "border-width: 1px 1px 0px 1px;" + "margin: 0 0 0 0;" + "font-size: 14.0pt;" + "text-align: left;" + "word-break: break-all;"
				+ "table-layout: fixed;" + "border-collapse: collapse;}" + ".jcxtd{" + "color: windowtext;" + "font-size: 20.0pt;" + "font-weight: 400;" + "font-style: normal;"
				+ "text-decoration: none;" + "font-family: ����;" + "text-align: left;" + "vertical-align: middle;" + "border: .5pt solid windowtext;" + "padding-left: 1px;"
				+ "padding-right: 1px;" + "padding-top: 1px;}" + ".lasttd{" + "color: windowtext;" + "font-size: 20.0pt;" + "font-weight: 400;" + "font-style: normal;"
				+ "text-decoration: none;" + "font-family: ����;" + "vertical-align: middle;" + "padding-left: 1px;" + "padding-right: 1px;" + "padding-top: 1px;}" + ".textborder{"
				+ "border-top:none;border-left:none;border-right:none;border-bottom:none;}" + ".jcth{" + "border:1 black;" + "text-align:center;}" +
				// "height:25px;}" +
				"</style>";
		return scc;
	}

	/**
	 * �ݹ�ɾ���ļ����ļ���
	 * 
	 * @param file
	 *            Ҫɾ���ĸ�Ŀ¼
	 */
	public void RecursionDeleteFile(File file) {
		if (file.isFile()) {
			file.delete();
			return;
		}
		if (file.isDirectory()) {
			File[] childFile = file.listFiles();
			if (childFile == null || childFile.length == 0) {
				file.delete();
				return;
			}
			for (File f : childFile) {
				RecursionDeleteFile(f);
			}
			file.delete();
		}
	}

	public String getQyMbTitle() {
		HashMap<String, Object> qyjbxxData = getQYJBXXData(guid);
		String qymc = "";
		if (qyjbxxData != null && qyjbxxData.size() > 0) {
			qymc = qyjbxxData.get("qymc").toString();
		}

		HashMap<String, Object> conditions = new HashMap<String, Object>();

		conditions.put("TID", industryID);
		ArrayList<HashMap<String, Object>> data = SqliteUtil.getInstance().getList("TName", conditions, "YDZF_SpecialTemplate");
		String mbName = "";
		if (data != null & data.size() > 0) {
			mbName = data.get(0).get("tname").toString();
		}
		String htmlstr = "<center><div style=\"font-size:16pt;font-weihgt:bold;\" >" + qymc + "" + "</div> </center><span style=\"float:right;margin-right:100px;\">------"
				+ mbName + "</span>";

		return htmlstr;
	}

	/**
	 * �滻�ļ��е��ַ���
	 * 
	 * @param file
	 * @param oldWord
	 * @param newWord
	 */
	public void replaceWord(File file, String oldWord, String newWord) {
		try {
			String text = "";
			FileInputStream fis = new FileInputStream(file);
			InputStreamReader isr = new InputStreamReader(fis);
			BufferedReader br = new BufferedReader(isr);

			String str = "";
			while ((str = br.readLine()) != null) {
				text += str;
			}

			FileOutputStream fos = new FileOutputStream(file);
			OutputStreamWriter osw = new OutputStreamWriter(fos);
			BufferedWriter bw = new BufferedWriter(osw);
			// �滻�ַ�
			text = text.replace(oldWord, newWord);
			bw.write(text);

			bw.close();
			osw.close();
			fos.close();
			br.close();
			isr.close();
			fis.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
