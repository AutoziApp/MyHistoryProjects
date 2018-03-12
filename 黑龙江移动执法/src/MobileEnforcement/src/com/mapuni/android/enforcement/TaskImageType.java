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
 * FileName: TaskImageType.java Description:任务清单图片打印 <br>
 * 1.JCJLDetailActivity 检查记录->清单打印 <br>
 * 2.TaskListActivity 任务列表->清单打印<br>
 * 3.HDJcdActivity 清单检查->清单打印
 * 
 * @author Liusy
 * @Version 1.3.4
 * @Copyright 中科宇图天下科技有限公司<br>
 *            Create at: 2012-12-4 上午11:44:40
 */
public class TaskImageType extends BaseActivity {
	/** 显示组件 */
	private LinearLayout middlelayout;
	/** 当前任务编号 */
	private String taskBH;
	/** 加载等待 */

	private YutuLoading yutuLoading;
	/** 列表适配器 */
	private ListViewAdapter myAdapter;

	/** 图片文件集合 */
	private ArrayList<File> fileList;
	/** 图片保存路径 */
	private String taskPath = PathManager.SDCARD_RASK_DATA_PATH + "ImgDoc/";
	/** 分页时的标识 */
	private int flag = 1;
	/** 消息处理 */
	private Handler mHandler;
	/** 加载消息状态 */
	private final int LOAD_SUCCESS = 0;
	private final int LOAD_FAILURE = 1;

	private final boolean isMorePage = true;

	private final String TAG = "TaskImageType";
	private int _page = 1;// 页数
	private String guid = "";// 企业guid
	private String industryID = "";// 行业（模板）代码
	private String surveyDate = Global.getGlobalInstance().getDate("yyyy-MM-dd");

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.ui_mapuni);
		// flag = 1;
		// 获取上一页面传递信息
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
					Toast.makeText(TaskImageType.this, "打印资源生成失败！", Toast.LENGTH_LONG).show();
					finish();
					break;
				}

				if (yutuLoading != null) {
					yutuLoading.dismissDialog();
				}

			}
		};
		// 执行异步加载数据
		WorkAsyncTask task = new WorkAsyncTask(this);
		task.execute("");

	}

	/**
	 * Description: 初始化界面
	 * 
	 * @author Administrator<br>
	 *         Create at: 2012-12-4 上午11:56:20
	 */
	private void getViews() {
		RelativeLayout rl = (RelativeLayout) findViewById(R.id.parentLayout);
		SetBaseStyle(rl, "清单打印");
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
	 * FileName: TaskImageType.java Description:绑定数据适配器
	 * 
	 * @author Administrator
	 * @Version 1.3.4
	 * @Copyright 中科宇图天下科技有限公司<br>
	 *            Create at: 2012-12-4 上午11:56:34
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
			// 头标
			mHeadImageView = (ImageView) view.findViewById(R.id.siteevidence_label_imageView);
			mHeadImageView.setImageResource(R.drawable.icon_image);
			// 文本
			mTitleTextView = (TextView) view.findViewById(R.id.siteevidence_medianame_textView);
			String nm = fileList.get(position).getName();
			mTitleTextView.setText(nm);
			// 操作图标
			mPreviewImageView = (ImageButton) view.findViewById(R.id.siteevidence_preview_imageView);
			mPreviewImageView.setVisibility(View.GONE);

			// 删除操作图标
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
			 * //重命名操作图标 ImageView mRenameImageView = (ImageView)
			 * view.findViewById(R.id.siteevidence_rename_imageView);
			 * mRenameImageView.setVisibility(View.GONE);
			 */
			bluetooth_imageView = (ImageButton) view.findViewById(R.id.siteevidence_bluetooth_imageView);
			mPreviewImageView.setOnClickListener(new OnClickListener() {// 预览
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
							// else Toast.makeText(context, "文件命名不正确",
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
	 * FileName: TaskImageType.java Description:异步加载清单打印图片列表
	 * 
	 * @author Administrator
	 * @Version 1.3.4
	 * @Copyright 中科宇图天下科技有限公司<br>
	 *            Create at: 2012-12-4 上午11:55:45
	 */
	private class WorkAsyncTask extends AsyncTask<String, Integer, String> {
		// 叶子节点集合
		ArrayList<TreeNode> leavesList = new ArrayList<TreeNode>();
		// 根节点集合
		private List<TreeNode> rootNodeList = new ArrayList<TreeNode>();
		private List<TreeNode> tempRootNodeList = new ArrayList<TreeNode>();

		private int leavesLevel = 1;// 叶子节点的最大级别值
		private StringBuffer allHtml = new StringBuffer("");

		// ==============================表格规格定值======================================
		// 表格高度
		int _TableHeight = 900;
		int _RowHeight = 30;

		// 列宽度
		// int _TypeWidth = 300;
		// int _JudeWidth = 444;
		// int _ResultWidth = 140;
		// int _RemarkWidth = 110;
		int _TypeWidth = 200;
		int _JudeWidth = 444;
		int _ResultWidth = 250;
		int _RemarkWidth = 100;

		// 计数器
		int _step = 0;

		// 判断内容显示长度字数
		int _RowSize = 60;
		String _TempStr = "";

		String filePath = taskPath + taskBH;

		public WorkAsyncTask(Context context) {
			yutuLoading = new YutuLoading(context);
			yutuLoading.setLoadMsg("加载数据中，请稍等...", "");
			yutuLoading.showDialog();
		}

		@Override
		protected String doInBackground(String... params) {

			// Build HTML Task Result List
			ArrayList<HashMap<String, Object>> rwxxList = getRWXX(taskBH);
			if (rwxxList == null || rwxxList.size() == 0) {
				return "";
			}
			// 获取所需的信息
			// 模板编号
			// guid = rwxxList.get(0).get("guid").toString();
			leavesLevel = getMaxLevel();
			if (leavesLevel <= 0) {
				mHandler.sendEmptyMessage(LOAD_FAILURE);
				return "该任务没有模板！";
			}
			// 模板信息
			// 1.获取所有的叶子结点
			leavesList = getLeavesNodeList(industryID);

			// 2.对于每一个叶子节点,获取根结点 tempRootNodeList
			for (TreeNode node : leavesList) {
				node.rwID = taskBH;
				getRootNode(node);
			}
			for (TreeNode nodeRoot : tempRootNodeList) {
				// 给每一个根节点添加子节点
				nodeRoot.rwID = taskBH;
				setChildNode(nodeRoot);
			}
			// 移除空节点
			removeEmptyTypeNode(tempRootNodeList);
			// 集合排序
			Collections.sort(tempRootNodeList);

			// 头部
			allHtml.append(getCssString());// 样式
			allHtml.append(getCharsetString());// 字符集
			allHtml.append(getQyMbTitle());// 头部
			// 主要清单结果
			getCheckResult();// 结果
			// 底部
			allHtml.append(getBottom());// 底部信息
			// 写文件
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
		 * Description:返回监察情况与备注 <li>递归加载根节点
		 * 
		 * @param resultHtml
		 * @param rootNode
		 * @author Administrator<br>
		 *         Create at: 2013-1-30 上午11:12:13
		 */

		private void getLasttds(TreeNode node) {
			// 判断是否是叶子，1是叶子，0不是叶子
			if (node.isContent == null) {
				return;
			}

			if (node.isContent.equals("1")) {
				// 对结果进行加载
				LastResult result = new LastResult(node.rwID, node.qyID, node.TID, node.SpecialItemID);// 对于叶子，再取结果
				final LastResult lastResult = getLastResult(node.rwID, node.qyID, node.SpecialItemID, result);
				// 文本存在，不为空
			
				if (node.title != null && node.title.length() > 0) {
					// 值在列中
					if (node.ValueInColumn.equals("1")) {
						allHtml.append("<td class='td'>" + node.title + lastResult.SpecialItemResult + "</td>");
					} else {
						allHtml.append("<td class='td'>" + node.title + "</td>");
						// 装载结果值
						allHtml.append(GetCheckBox(node, lastResult));
						// 加载备注
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
						
					
						// 备注
					}
				}
				_step++;
				allHtml.append("</tr>");

				// 分页
				if (isMorePage && _step * _RowHeight >= _TableHeight) {
					_step = 0;
					allHtml.append(getBottom());// 底部信息
					writeFile(allHtml.toString(), filePath);// 生成文件
					// 清空
					allHtml.delete(0, allHtml.length());
					// 头部
					allHtml.append(getCssString());// 样式
					allHtml.append(getCharsetString());// 字符集
					// allHtml.append(getQYJBXXHeader(guid));//企业信息
					allHtml.append(getClomnHtml(leavesLevel));

					// 接上
					// 1.当前的叶子完结了
					// 2.当前叶子没完。
					// 获取当前叶子父节点
					// 获取当前的根
					TreeNode parentNode = node.getParent();
					if (parentNode != null) {

						List<TreeNode> nodeList = parentNode.getChildren();
						/*
						 * for(int i = 0; i < nodeList.size(); i++) {
						 * if(nodeList.get(i).equals(node)) {//找到当前项
						 * allHtml.append("<tr>"); //判断是否是最后的叶子 if(i <
						 * nodeList.size() - 1) {//不是最后一项，先合并类别
						 * allHtml.append("<td class=\"td\" rowspan='" +
						 * (nodeList.size() - i) + "' colspan='" + (leavesLevel
						 * - Integer.parseInt(node.SpecialItemLevel)) + "'>" +
						 * node.title + "</td>"); } } }
						 */

						for (int i = 0; i < nodeList.size(); i++) {
							if (nodeList.get(i).equals(node)) {// 找到当前项

								if (nodeList.size() - 1 != i) {// 判断是不是最后一个叶子，不是则添加parentNode表格
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
											// 暂且这样修改。
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
				// 不是叶子，从根开始画
			} else {
				// 当前结点的子节点数量
				int childCount = node.children.size();

				// 判断是否子项是否是叶子，是叶子则合并深度rowspan
				if (node.SpecialItemLevel.equals("1")) {
					allHtml.append("<tr>");
				}
				if (IsChildHaveContent(node.SpecialItemID)) {
					// Log.i("SS",
					// "Maybe OOM here at the third time -- >"+i_count++);
					try {
						int count = childCount;
						if ((_step + childCount) * _RowHeight >= _TableHeight + 61) {// 将在此节点分页
							int a = _TableHeight - _step * _RowHeight;// 剩余的高度
							Log.i("info", "剩余的高度：" + a);
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
				} else {// 如果不是叶子，即有结点
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
				// 循环子节点，递归
				for (TreeNode childNode : node.children) {
					getLasttds(childNode);
				}
			}
		}

		/**
		 * Description:返回清单结果的HTML
		 * 
		 * @return
		 * @author Administrator<br>
		 *         Create at: 2013-1-28 下午04:58:53
		 */
		private void getCheckResult() {
			// 根节点数量
			int rootNodeCount = tempRootNodeList.size();
			// StringBuffer resultHtml = new StringBuffer("");
			// resultHtml.append(getTop(leavesLevel));//列名的HTML【ok】
			allHtml.append(getClomnHtml(leavesLevel));// 列名的HTML

			// 一级节点的循环
			for (int i = 0; i < rootNodeCount; i++) {
				TreeNode rootNode = tempRootNodeList.get(i);
				// 判断是否是叶子(0是叶子，1不是叶子)
				// StringBuffer sb = new StringBuffer("");
				String nodeName = rootNode.title;
				getLasttds(rootNode);
			}
		}

		/**
		 * Description:判断子项是否有内容
		 * 
		 * @param itemid
		 * @return
		 * @author Administrator<br>
		 *         Create at: 2013-1-30 下午01:25:09
		 */
		private boolean IsChildHaveContent(String itemid) {
			// String sql =	 //	修改 增加筛选条件 where Status = '1'  _byk
			// "SELECT id FROM YDZF_SpecialItem WHERE pid='"+itemid+"' and IsContent='1'";
			String sql = "SELECT * FROM YDZF_SpecialItem a, YDZF_TemplateSpacialItem b WHERE a.pid='" + itemid + "'"
					+ "  and a.IsContent='1'  and a.ID=b.SpecialItemID and b.TID='" + industryID + "' order by a.sortindex";
			ArrayList<HashMap<String, Object>> data = null;
			data = SQLiteDataProvider.getInstance().queryBySqlReturnArrayListHashMap(sql);

			return data.size() >= 1 ? true : false;
		}

		/**
		 * Description:输出结果栏
		 * 
		 * @param node
		 * @param lastResult
		 * @return
		 * @author Administrator<br>
		 *         Create at: 2013-1-30 下午01:05:40
		 */
		private String GetCheckBox(TreeNode node, LastResult lastResult) {
			StringBuilder str = new StringBuilder();
			str.append("<td class=\"td\">");
			// 获取结果类型
			// 是、否[一类、二类、三类、四类]ID,RName,ControlType
			final ArrayList<HashMap<String, Object>> resultType = getResultTypeData(node.ResultTypeID);
			if (resultType == null || resultType.isEmpty()) {
				str.append("</td>");
				return str.toString();
			}
			String contrlType = (String) resultType.get(0).get("controltype");

			if (contrlType.contains("Check")) {
				// 循环出CheckBox
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
				// 填写Text
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
		 * 查询数据库 获取其结果类型：文本、单选框
		 * 
		 * @param resultTypeID
		 *            结果类型ID
		 * @author Administrator<br>
		 *         Create at: 2012-12-4 上午11:20:56
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
		 * 查询数据库 通过任务ID，专项ID，获取上次结果
		 * 
		 * @param qyID
		 *            企业ID
		 * @return 备注信息
		 * @author Administrator<br>
		 *         Create at: 2012-12-4 上午11:20:56
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
				// 上次选择信息的文本
				lastResult.Text = getLastTaskResultText(rwID, qyID, specialItemID, lastResult);
				lastResult.flag = true;
			} else {
				lastResult.flag = false;
			}
			return lastResult;
		}

		/**
		 * 查询数据库 通过任务ID，专项ID，获取结果
		 * 
		 * @param qyID
		 *            企业ID
		 * @return 上次选择信息的文本
		 * @author Administrator<br>
		 *         Create at: 2012-12-4 上午11:20:56
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
		 * Description:列标题栏的HTML:类别、判断内容、监察情况、备注
		 * 
		 * @param level
		 * @return
		 * @author Administrator<br>
		 *         Create at: 2013-1-30 上午10:57:29
		 */
		private String getClomnHtml(int level) {
			StringBuilder sb = new StringBuilder();
			sb.append("<div style='width:100%; height:1050px;'>");
			sb.append("<table width='100%' class='table'>");
			sb.append("<tr> <td class=\"td\" colspan=\"" + (level - 1) + "\" style='width:" + _TypeWidth + "px' >类别</td><td class=\"td\" style='width:" + _JudeWidth
					+ "px' >判断内容</td><td class=\"td\"  style='width:" + _ResultWidth + "px' >监察情况</td><td class=\"td\" style='width:" + _RemarkWidth + "px'>备注</td></tr>");
			return sb.toString();
		}

		/**
		 * Description:列标题栏的HTML:类别、判断内容、监察情况、备注
		 * 
		 * @param level
		 * @return
		 * @author Administrator<br>
		 *         Create at: 2013-1-30 上午10:57:29
		 */
		public String getTop(int level) {
			StringBuffer sb = new StringBuffer();
			sb.append("<table width='100%' border=\"1 black\" cellpadding=\"0\" cellspacing=\"0\" " + "style=\"border-collapse:collapse; border-color:black\">");
			sb.append("<tr> <td class=\"jcxtd\" colspan=\"" + (level - 1) + "\">" + "类别</td><td class=\"jcxtd\">" + "判断内容</td><td class=\"jcxtd\" width='120px'>"
					+ "监察情况</td><td class=\"jcxtd\">" + "备注</td></tr>");
			return sb.toString();
		}

		private String getBottom() {
			StringBuffer sb = new StringBuffer();
			sb.append("</table>");
			sb.append("<table   width='100%'  border=\"1 black\" cellpadding=\"0\" cellspacing=\"0\" "
					+ "style=\"border-collapse:collapse; border-color:black\"><tr height='80px'> <td style=\"width:100px\"  "
					+ "class=\"td\">需要说明的其他事项：</td> <td  colspan=\"3\" class=\"td\"></td></tr>");
			sb.append("<tr align='left' height='50px'><td style=\"width:100px\"  class=\"td\">"
					+ "监察员签字：</td><td width=\"180px\" class=\"td\"></td> <td width=\"120px\" class=\"td\">" + "当事人签字：</td> <td class=\"td\"></td> </tr></table>");
			sb.append(" <span style=\"float:right;margin-right:100px;\">检查时间:" + surveyDate + "</span><br />");

			sb.append("<div style='margin-top:10px;margin-bottom:50px; text-align:center'>" + "第" + _page + "页" + "&nbsp;共 " + "{totalpage}" + " 页" + "</div>");

			// "<span id=\"divTotalPageCount\"></span></div><script>function getQueryString(name){var reg = new RegExp(\"(^|&)\"+ name +\"=([^&]*)(&|$)\"); var r = window.location.search.substr(1).match(reg); if (r!=null) return unescape(r[2]); return 1;} document.getElementById(\"divTotalPageCount\").innerHTML='共'+getQueryString(\"totalPageCount\")+'页';</script>");
			return sb.toString();
		}

		/**
		 * Description:获取当前模板的深度
		 * 
		 * @return
		 * @author Administrator<br>
		 *         Create at: 2013-1-30 上午10:48:08
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
				// Log.i(TAG, "当前的  Content : " + content);

				// 防止产生相同的父节点
				for (TreeNode node : tempRootNodeList) {
					if (specialItemID.equals(node.SpecialItemID)) {
						nodeFather = node;
						isNew = false;
						break;
					}
				}
				if (isNew) {
					nodeFather = new TreeNode();
					// 赋值
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
					// 不是根节点
					getRootNode(nodeFather);
				} else if (isNew) {
					// 是新的根节点
					tempRootNodeList.add(nodeFather);
					// Log.i(TAG, "添加根节点：" + nodeFather.title);
				}
			}
		}

		/**
		 * Description:移除空节点
		 * 
		 * @param nodeList
		 * @author Administrator Create at: 2012-12-4 上午09:59:52
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
		 * Description: 对于传递进来的一个结点，查询其子节点，并设置父子结点关系
		 * 
		 * @param node
		 * @author Administrator Create at: 2012-12-4 上午10:11:13
		 */
		private void setChildNode(TreeNode node) {
			if (node != null && node.SpecialItemID != null) {
				// 查询子节点
				ArrayList<TreeNode> data = getChildNode(node.SpecialItemID);
				if (data != null) {
					for (TreeNode child : data) {
						setChildNode(child);

						child.setParent(node);
						node.children.add(child);
						// 设置子节点的子节点
					}
				}
			}
		}

		/**
		 * Description:获取所有的叶子节点
		 * 
		 * @param industryID
		 * @return
		 * @author Administrator<br>
		 *         Create at: 2013-1-28 下午04:38:06
		 */
		private ArrayList<TreeNode> getLeavesNodeList(String industryID) {
			String sql = "select * from YDZF_TemplateSpacialItem where TID = '" + industryID + "'";
			ArrayList<HashMap<String, Object>> data = null;
			ArrayList<TreeNode> nodeList = new ArrayList<TreeNode>();
			TreeNode node = null;

			data = SQLiteDataProvider.getInstance().queryBySqlReturnArrayListHashMap(sql);

			for (HashMap<String, Object> map : data) {
				String tID = (String) map.get("tid");
				String specialItemID = (String) map.get("specialitemid");// 专项id
				String specialItemLevel = (String) map.get("specialitemlevel");
				String specialItemContent = (String) map.get("specialitemcontent");
				String resultTypeID = (String) map.get("resulttypeid");
				String remarkTip = (String) map.get("remarktip");
				String sortIndex = (String) map.get("specialitemsortindex");
				String isLoadLastResult = (String) map.get("isloadlastresult");
				String valueInColumn = (String) map.get("valueincolumn");

				// 专项信息的封装
				// 创建子节点
				node = new TreeNode(specialItemContent, "");

				node.qyID = guid;
				node.rwID = taskBH;
				node.TID = tID;
				node.isContent = "1";// 叶子结点
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
		 * Description: 查询数据库 <li>根据专项ID，获取其父节点
		 * 
		 * @param specialItemID
		 *            专项ID
		 * @return 父节点的集合
		 * @author Administrator Create at: 2012-12-4 上午10:08:09
		 */
		private ArrayList<HashMap<String, Object>> getFatherItemData(String specialItemID) {
			ArrayList<HashMap<String, Object>> data = new ArrayList<HashMap<String, Object>>();
			String sql = getString(R.string.select_SpecialItem_FatherNode) + " '" + specialItemID + "') order by SortIndex ";

			data = SQLiteDataProvider.getInstance().queryBySqlReturnArrayListHashMap(sql);

			return data;
		}

		/**
		 * Description: 根据专项ID获取其所有子节点
		 * 
		 * @param id
		 *            专项ID
		 * @return 子节点集合
		 * @author Administrator Create at: 2012-12-4 上午10:10:46
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
				// 创建子节点
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

					// 专项信息的封装
					// 创建子节点
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

					// Log.i(TAG, "添加子节点：" + specialItemContent);
					nodeList.add(node);
				}
			}

			// sql = //	修改 增加筛选条件 where Status = '1'  _byk
			// "select * from YDZF_SpecialItem as c where PID = '"+id+"' and (select count(*) from ( select * from YDZF_SpecialItem as b left outer join YDZF_TemplateSpacialItem as a on a.SpecialItemID = b.ID where a.tid='"+industryID+"' and b.PID=c.id ))>0";
			sql = "SELECT * FROM YDZF_SpecialItem WHERE IsContent='0' and PID='" + id + "' order by sortindex";

			// Log.i(TAG, sql);

			data = SQLiteDataProvider.getInstance().queryBySqlReturnArrayListHashMap(sql);

			if (data.size() > 0) {
				// 创建子节点
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

					// 专项信息的封装
					// 创建子节点
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

					// Log.i(TAG, "添加子节点：" + specialItemContent);
					nodeList.add(node);
				}
			}
			return nodeList;
		}

		/**
		 * Description:写HTML文件
		 * 
		 * @author Administrator<br>
		 *         Create at: 2013-1-28 下午03:46:29
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
				File f = new File(filePath + "/" + "第" + _page + "页" + ".html");
				_page++;
				// if (!f.exists()) {
				f.createNewFile();// 不存在则创建
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
	 * Description:获取头部描述企业基本信息描述HTML
	 * 
	 * @return
	 * @author Administrator<br>
	 *         Create at: 2013-1-28 下午03:47:29
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
		String tableName = getTableTitleName() + "环境监察现场检查记录表";
		StringBuffer sb = new StringBuffer();
		sb.append("<table width='100%' border='1px black' cellpadding=\"0\" cellspacing=\"0\" "
				+ "style=\"border-collapse:collapse;border-color:black;border-bottom:0; border:1px\">");
		sb.append("<tr><td class=\"style1\"  colspan=\"8\">" + tableName + "</td><tr>");
		// sb.append("<tr><td  colspan=\"8\" class=\"td\" >企业基本信息 </td> </tr> ");
		sb.append("<tr><td class=\"td\"  colspan=\"2\"> 企业名称</td> <td class=\"td\" colspan=\"2\"> " + qyjbxxData.get("qymc").toString() + "</td>");
		sb.append(" <td class=\"td\"  colspan=\"2\">法定代表人姓名</td><td  class=\"td\" colspan=\"2\">" + qyjbxxData.get("frdb").toString() + "</td> </tr>");
		sb.append("<tr><td class=\"td\"  colspan=\"2\"> 详细地址</td> <td class=\"td\"' colspan=\"2\">" + qyjbxxData.get("qydz").toString() + "</td>");
		sb.append(" <td class=\"td\"  colspan=\"2\">邮编</td><td class=\"td\" colspan=\"2\">" + qyjbxxData.get("yzbm").toString() + "</td> </tr>");
		sb.append("<tr><td class=\"td\" colspan=\"2\"> 联系人</td> <td class=\"td\" colspan=\"2\">" + qyjbxxData.get("hblxr").toString() + "</td>");
		sb.append(" <td class=\"td\"  colspan=\"2\">联系电话</td><td class=\"td\" colspan=\"2\">" + qyjbxxData.get("frdbdh").toString() + "</td> </tr>");
		sb.append("<tr><td class=\"td\"  colspan=\"2\"> 在建项目进度</td> <td class=\"td\" " + "colspan=\"2\"></td>");
		sb.append(" <td class=\"td\"  colspan=\"2\">已建项目投产时间</td><td class=\"td\" " + "colspan=\"2\"></td> </tr>");
		sb.append("<tr><td class=\"td\"  colspan=\"2\"> 监察人员姓名</td> <td class=\"td\" colspan=\"2\">" + getCheckPeople() + "</td>");
		sb.append(" <td class=\"td\"  colspan=\"2\">监察时间</td><td class=\"td\" colspan=\"2\">" + getCheckTime() + "</td></tr>");
		sb.append("</table>");
		return sb.toString();
	}

	/**
	 * 获取当前登录人的所属部门
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
	 * Description:编码格式
	 * 
	 * @return
	 * @author Administrator<br>
	 *         Create at: 2013-2-19 上午09:32:41
	 */
	private String getCharsetString() {
		return "<meta http-equiv=\"Content-Type\"content=\"text/html; charset=utf-8\"/>";
	}

	/**
	 * 获取监察人员
	 * 
	 * @return
	 */
	private String getCheckPeople() {
		return new RWXX().getTaskExecutor(taskBH);
	}

	/**
	 * Description:获取检查时间
	 * 
	 * @return
	 * @author Administrator<br>
	 *         Create at: 2013-1-28 下午03:47:56
	 */
	private String getCheckTime() {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy年MM月dd日  HH时mm分");
		return dateFormat.format(new Date());
	}

	/**
	 * Description:获取企业基本信息数据
	 * 
	 * @return 默认返回空的map
	 * @author Administrator<br>
	 *         Create at: 2013-1-28 下午03:49:18
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
	 * FileName: SpecialItemActivity.java Description:上次结果的对象
	 * 
	 * @author Administrator
	 * @Version 1.3.4
	 * @Copyright 中科宇图天下科技有限公司<br>
	 *            Create at: 2012-12-4 上午11:22:57
	 */
	private final class LastResult {
		public String TaskID; // 任务编号
		public String EnterID; // 企业编号
		public String IndustryID; // 行业编号
		public String SpecialItemID; // 专项编号
		public String SpecialItemResult; // 结果类型编号或者文本
		public String Text; // 结果文本
		public String Remark; // 备注信息
		public boolean flag; // 是否已经保存

		// 含参数构造
		// 初始赋值：任务编号，企业编号，模板编号、专项编号
		public LastResult(String taskID, String enterID, String industryID, String specialItemID) {
			super();
			TaskID = taskID;
			EnterID = enterID;
			IndustryID = industryID;
			SpecialItemID = specialItemID;
		}
	}

	/**
	 * Description:CSS格式脚本
	 * 
	 * @return
	 * @author Administrator<br>
	 *         Create at: 2013-1-28 下午03:49:33
	 */
	private static String getCssString() {
		String scc = " <style type=\"text/css\">" + ".style1{ " + "color: windowtext;" + "font-size: 30px;" + "font-weight: 700;" + "font-style: normal;"
				+ "text-decoration: none;" + "font-family: 宋体;" + "text-align: center;" + "border-left-style: none;" + "border-left-color: inherit;" + "border-left-width: medium;"
				+ "border-right-style: none;" + "border-right-color: inherit;" + "border-right-width: medium;" + "border-top-style: none;" + "border-top-color: inherit;"
				+ "border-top-width: medium;" + "border-bottom: .5pt solid windowtext;" + "padding-left: 1px;" + "padding-right: 1px;" + "padding-top: 1px;}" + ".jctd {"
				+ "color: windowtext;" + "font-size: 20.0pt;" + "font-weight: 400;" + "font-style: normal;" + "text-decoration: none;" + "font-family: 宋体;" + "text-align: left;"
				+ "vertical-align: middle;" + "border: .5pt solid windowtext;" + "padding-left: 1px;" + "padding-right: 1px;" + "padding-top: 1px;}" + ".table {"
				+ "border: 1px solid #000;" + "border-width: 1px 0 0 1px;" + "margin: 0 0 0 0;" + "text-align: center;" + "border-collapse: collapse;}" + ".td {"
				+ "border: 1px solid #000;" + "border-width: 1px 1px 0px 1px;" + "margin: 0 0 0 0;" + "font-size: 14.0pt;" + "text-align: left;" + "word-break: break-all;"
				+ "table-layout: fixed;" + "border-collapse: collapse;}" + ".jcxtd{" + "color: windowtext;" + "font-size: 20.0pt;" + "font-weight: 400;" + "font-style: normal;"
				+ "text-decoration: none;" + "font-family: 宋体;" + "text-align: left;" + "vertical-align: middle;" + "border: .5pt solid windowtext;" + "padding-left: 1px;"
				+ "padding-right: 1px;" + "padding-top: 1px;}" + ".lasttd{" + "color: windowtext;" + "font-size: 20.0pt;" + "font-weight: 400;" + "font-style: normal;"
				+ "text-decoration: none;" + "font-family: 宋体;" + "vertical-align: middle;" + "padding-left: 1px;" + "padding-right: 1px;" + "padding-top: 1px;}" + ".textborder{"
				+ "border-top:none;border-left:none;border-right:none;border-bottom:none;}" + ".jcth{" + "border:1 black;" + "text-align:center;}" +
				// "height:25px;}" +
				"</style>";
		return scc;
	}

	/**
	 * 递归删除文件和文件夹
	 * 
	 * @param file
	 *            要删除的根目录
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
	 * 替换文件中的字符串
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
			// 替换字符
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
