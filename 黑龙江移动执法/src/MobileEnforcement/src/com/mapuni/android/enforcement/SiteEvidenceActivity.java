package com.mapuni.android.enforcement;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.text.format.Time;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mapuni.android.MobileEnforcement.R;
import com.mapuni.android.attachment.T_Attachment;
import com.mapuni.android.base.Global;
import com.mapuni.android.base.business.BaseUsers;
import com.mapuni.android.base.util.DisplayUitl;
import com.mapuni.android.business.RWXX;
import com.mapuni.android.dataprovider.FileHelper;
import com.mapuni.android.dataprovider.JsonHelper;
import com.mapuni.android.dataprovider.SqliteUtil;
import com.mapuni.android.netprovider.WebServiceProvider;
import com.mapuni.android.uitl.PanduanDayin;

/**
 * FileName: SiteEvidenceActivity.java Description:现场取证
 * 
 * @author Administrator
 * @Version 1.3.4
 * @Copyright 中科宇图天下科技有限公司 Create at: 2012-12-4 上午10:46:44
 */
public class SiteEvidenceActivity extends AttachmentBaseActivity {

	ListView lv;
	GridView gridView;
	// TextView textview;
	EditText edtext;
	String fk_id;
	String qyname;
	private String currentID;
	String TimeData = null;

	private boolean hasUpdateFileFromServer = false;
	/** 是否是从任务详细信息跳入 */
	private String taskInfoFlag;
	// private String IsUpload;
	// private String qydm = "";
	private String qyid = "";
	ImageView record_law, record_;
	Button take_photo, record_video, record_audio, select_attach;
	Intent photo_intent = null;
	Intent audio_intent = null;
	Intent video_intent = null;
	private final RWXX rwxx = new RWXX();
	ImageView iv;// 按钮中间的竖线
	ArrayList<File> allFiles;
	ArrayList<HashMap<String, Object>> fileList;
	GridViewAdapter adapter;

	/** 照片入库的GUID */
	private String imageGuid;
	private String TableName = "TaskEnpriLink";
	private View parentView;
	/**
	 * 该任务关联的企业执行状态
	 */
	private String taskEnpriLinkState;

	String status = null;
	/* 是否执行过加载缩略图任务 */
	private boolean flag = false;
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {

			String json0 = (String) msg.obj;
			if (json0 != null && !json0.equals("")) {
				String[] node = { "Guid", "FileName", "FilePath", "Extension", "FK_Unit", "FK_Id", "Remark", "LinkUrl", "UpdateTime", "FileType" };
				ArrayList<HashMap<String, Object>> list = JsonHelper.paseJSON(json0, node);
				for (int k = 0; k < list.size(); k++) {
					HashMap<String, Object> hashmap = new HashMap<String, Object>();
					hashmap.put("Guid", list.get(k).get("Guid").toString());
					hashmap.put("FileName", list.get(k).get("FileName").toString());
					hashmap.put("Extension", list.get(k).get("Extension").toString());
					hashmap.put("FK_Unit", list.get(k).get("FK_Unit").toString());
					data.add(hashmap);
					// 根据附件的guid判断本地数据是否存在该在该附件记录，若存在update,若不存在insert
					String rwguid = list.get(k).get("Guid").toString();
					ContentValues updateValues = new ContentValues();
					updateValues.put("FileName", list.get(k).get("FileName").toString());
					updateValues.put("FilePath", list.get(k).get("FilePath").toString());
					updateValues.put("Extension", list.get(k).get("Extension").toString());
					updateValues.put("FK_Unit", list.get(k).get("FK_Unit").toString());
					updateValues.put("FK_Id", list.get(k).get("FK_Id").toString());
					updateValues.put("Remark", list.get(k).get("Remark").toString());
					updateValues.put("LinkUrl", list.get(k).get("LinkUrl").toString());
					updateValues.put("UpdateTime", list.get(k).get("UpdateTime").toString());
					updateValues.put("FileType", list.get(k).get("FileType").toString());

					String guidSelect = SqliteUtil.getInstance().getDepidByUserid("select guid from T_Attachment where  guid='" + rwguid + "'");
					if (guidSelect != null && !guidSelect.equals("")) {
						String[] whereArgs = { rwguid };
						try {
							SqliteUtil.getInstance().update("T_Attachment", updateValues, "guid=?", whereArgs);
						} catch (FileNotFoundException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					} else {
						updateValues.put("guid", rwguid);
						SqliteUtil.getInstance().insert(updateValues, "T_Attachment");
					}

				}

				loadAllFileThumbnails();

			}
		}

	};
	private ArrayList<HashMap<String, Object>> data;
	private ProgressDialog loading;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ui_mapuni);
		Time time = new Time("GMT+8");
		time.setToNow();
		int year = time.year;
		int month = time.month;
		int day = time.monthDay;
		TimeData = year + "" + (month + 1) + "" + day;

		parentView = LayoutInflater.from(this).inflate(R.layout.take_evidence, null);
		SetBaseStyle((RelativeLayout) findViewById(R.id.parentLayout), "摄录取证");
		lv = new ListView(this);
		gridView = (GridView) parentView.findViewById(R.id.siteevidence_gridview);
		gridView.setEmptyView(parentView.findViewById(R.id.emptyTipView));

		LinearLayout bottom = (LinearLayout) parentView.findViewById(R.id.siteevidence_bottom_LinearLayout);
		currentID = getIntent().getStringExtra("currentTaskID");
		taskInfoFlag = getIntent().getStringExtra("taskInfoFlag");
		Log.i("info", "" + taskInfoFlag);
		qyid = getIntent().getStringExtra("qyid");
		
	
		
		initGlobal();
		HashMap<String, Object> conditions = new HashMap<String, Object>();
		conditions.put("TaskID", currentID);
		conditions.put("QYID", qyid);
		ArrayList<HashMap<String, Object>> arryListBak = SqliteUtil.getInstance().getList("IsExcute", conditions, TableName);
		if (arryListBak.size() < 1) {
			
			
			if (getIntent().getBooleanExtra("isSearch", false)) {
				
			
				Toast.makeText(SiteEvidenceActivity.this, "请同步任务信息!", Toast.LENGTH_SHORT).show();
				finish();
				return;
			}else{
				Toast.makeText(SiteEvidenceActivity.this, "当前记录已不存在,请重新执法!", Toast.LENGTH_SHORT).show();
				finish();
				return;
			}
		
		}

		taskEnpriLinkState = SqliteUtil.getInstance().getList("IsExcute", conditions, TableName).get(0).get("isexcute").toString();

		fk_id = currentID + "_" + qyid;
		qyname = SqliteUtil.getInstance().getDepidByDepName("select QYMC from T_WRY_QYJBXX where guid = '" + qyid + "'");
		take_photo = new Button(this);
		record_video = new Button(this);
		record_audio = new Button(this);
		select_attach = new Button(this);
		status = rwxx.getTaskStatusFromTaskEnpriLink(currentID, qyid);
		String selectZWsql = "select zw from PC_Users where UserID = '" + Global.getGlobalInstance().getUserid() + "'";
		String ZW = SqliteUtil.getInstance().getDepidByDepName(selectZWsql);
			//BYK  rwzt
		//if ("1".equals(status)) {
		if (getIntent().getBooleanExtra("isSearch", false)) {
			
			take_photo.setEnabled(false);
			record_video.setEnabled(false);
			record_audio.setEnabled(false);
			select_attach.setEnabled(false);
			Toast.makeText(SiteEvidenceActivity.this, "当前企业只能查看!", Toast.LENGTH_SHORT).show();
		}
		
			if ("3".equals(status)) {
			take_photo.setEnabled(false);
			record_video.setEnabled(false);
			record_audio.setEnabled(false);
			select_attach.setEnabled(false);
			Toast.makeText(this, "当前企业执行情况已提交，只能进行查看", Toast.LENGTH_LONG).show();
		}

		if ("009".equals(new RWXX().queryTaskStatus(currentID))) {
			Toast.makeText(this, "当前企业已归档，不能对笔录进行编辑", Toast.LENGTH_LONG).show();
			take_photo.setEnabled(false);
			record_video.setEnabled(false);
			record_audio.setEnabled(false);
		}
		LinearLayout middleLayout = (LinearLayout) findViewById(R.id.middleLayout);
		middleLayout.setPadding(0, 0, 0, 0);
		middleLayout.addView(parentView);

		int i = 0;
		boolean photobool = false, videobool = false, audiobool = false, selectbool = false;

		if (DisplayUitl.getAuthority(PZ_QX)) {
			take_photo.setBackgroundResource(R.drawable.btn_click);
			take_photo.setText("拍照");
			take_photo.setTextColor(Color.WHITE);
			take_photo.setVisibility(View.VISIBLE);
			photobool = true;
			i++;
		}
		if (DisplayUitl.getAuthority(SX_QX)) {
			record_video.setBackgroundResource(R.drawable.btn_click);
			record_video.setText("摄像");
			record_video.setTextColor(Color.WHITE);
			record_video.setVisibility(View.VISIBLE);
			videobool = true;
			i++;
		}
		if (DisplayUitl.getAuthority(LY_QX)) {
			record_audio.setBackgroundResource(R.drawable.btn_click);
			record_audio.setTextColor(Color.WHITE);
			record_audio.setText("录音");
			record_audio.setVisibility(View.VISIBLE);
			audiobool = true;
			i++;
		}
		if (true) {
			select_attach.setBackgroundResource(R.drawable.btn_click);
			select_attach.setTextColor(Color.WHITE);
			select_attach.setText("选附件");
			select_attach.setVisibility(View.VISIBLE);
			selectbool = true;
			i++;
		}

		// 获取手机分辨率
		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		// 算出按钮的高宽
		int width = (int) (dm.widthPixels / (double) i);

		if (photobool) {
			take_photo.setLayoutParams(new LinearLayout.LayoutParams(width, LinearLayout.LayoutParams.FILL_PARENT, 0));
			iv = new ImageView(this);
			iv.setScaleType(ScaleType.FIT_XY);
			iv.setLayoutParams(new LinearLayout.LayoutParams(2, LinearLayout.LayoutParams.FILL_PARENT));

			iv.setBackgroundResource(R.drawable.bg_bottombutton_splite);
			bottom.addView(take_photo);
			bottom.addView(iv);
		}
		if (videobool) {
			record_video.setLayoutParams(new LinearLayout.LayoutParams(width, LinearLayout.LayoutParams.FILL_PARENT, 0));
			iv = new ImageView(this);
			iv.setScaleType(ScaleType.FIT_XY);
			iv.setLayoutParams(new LinearLayout.LayoutParams(2, LinearLayout.LayoutParams.FILL_PARENT));

			iv.setBackgroundResource(R.drawable.bg_bottombutton_splite);
			bottom.addView(record_video);
			bottom.addView(iv);
		}
		if (audiobool) {
			record_audio.setLayoutParams(new LinearLayout.LayoutParams(width, LinearLayout.LayoutParams.FILL_PARENT, 0));

			bottom.addView(record_audio);
		}
		if (selectbool) {
			select_attach.setLayoutParams(new LinearLayout.LayoutParams(width, LinearLayout.LayoutParams.FILL_PARENT, 0));

			bottom.addView(select_attach);
		}

		File taskdir = new File(TASK_PATH + "RWZX/");
		ROOT_TASK_PATH = ROOT_TASK_PATH_FILE + TimeData + "_" + qyname + "/";
		File roottaskdir = new File(ROOT_TASK_PATH);
		/** task文件夹若不存在则创建 */
		if (!taskdir.exists())
			taskdir.mkdirs();
		if (!roottaskdir.exists()) {
			roottaskdir.mkdirs();
		}

		// gridView的点击事件
		gridView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				String filePath = (String) parent.getAdapter().getItem(position);
			//	String url = TASK_PATH + T_Attachment.transitToChinese(T_Attachment.RWZX) + "/" + filePath;
				String url="";
				if (filePath.contains("/")) {
					String[] split = filePath.split("/");
					 url = TASK_PATH + T_Attachment.transitToChinese(T_Attachment.RWZX)  +"/"+ split[split.length-1];
				}else{
					 url = TASK_PATH + T_Attachment.transitToChinese(T_Attachment.RWZX) +"/"+filePath  ;
				}
			
				
				String name = ((TextView) view.findViewById(R.id.item_text)).getText().toString();
				if (!FileHelper.isFileExist(url)) {
					// 如果SDCard不存在媒体文件
					FileHelper fileHelper = new FileHelper();
					int result = fileHelper.showFile2(filePath);
					if (result == 2) {
						Toast.makeText(SiteEvidenceActivity.this, "网络不通，文件下载失败！", Toast.LENGTH_SHORT).show();
						return;
					} else if (result != 0) {
						Toast.makeText(SiteEvidenceActivity.this, "文件下载失败！", Toast.LENGTH_SHORT).show();
						return;
					} else {
						hasUpdateFileFromServer = true;
						adapter.notifyDataSetChanged();
					}
				}
				File file;
				if (name.toLowerCase().endsWith("jpg") || name.toLowerCase().endsWith("png")) {
					file = new File(url);
					FileHelper.OpenFile(FileHelper.FileType.IMAGE.getType(), "", false, SiteEvidenceActivity.this, file);
				} else if (name.toLowerCase().endsWith("mp3") || name.toLowerCase().endsWith("amr")) {
					file = new File(url);
					FileHelper.OpenFile(FileHelper.FileType.AUDIO.getType(), "", false, SiteEvidenceActivity.this, file);
				} else if (name.toLowerCase().endsWith("mp4")) {
					file = new File(url);
					FileHelper.OpenFile(FileHelper.FileType.VIDEO.getType(), "", false, SiteEvidenceActivity.this, file);
				} else if (name.toLowerCase().endsWith(".doc") || name.toLowerCase().endsWith(".docx")) {
					file = new File(url);
					FileHelper.OpenFile(FileHelper.FileType.WORD.getType(), "", false, SiteEvidenceActivity.this, file);
				} else if (name.toLowerCase().endsWith(".xlsx") || name.toLowerCase().endsWith(".xls")) {
					file = new File(url);
					FileHelper.OpenFile(FileHelper.FileType.EXCEL.getType(), "", false, SiteEvidenceActivity.this, file);
				} else if (name.toLowerCase().endsWith(".pdf")) {
					file = new File(url);
					FileHelper.OpenFile(FileHelper.FileType.PDF.getType(), "", false, SiteEvidenceActivity.this, file);
				} else if (name.toLowerCase().endsWith(".ppt") || name.toLowerCase().endsWith(".dps")|| name.toLowerCase().endsWith(".pptx")) {
					file = new File(url);
					FileHelper.OpenFile(FileHelper.FileType.PPT.getType(), "", false, SiteEvidenceActivity.this, file);
				} else if (name.toLowerCase().endsWith(".txt")) {
					file = new File(url);
					FileHelper.OpenFile(FileHelper.FileType.TXT.getType(), "", false, SiteEvidenceActivity.this, file);
				} else {
					Toast.makeText(SiteEvidenceActivity.this, "无法支持此类型！", Toast.LENGTH_LONG).show();
				}
				flag = false;

			}
		});
		// 长按事件

		gridView.setOnItemLongClickListener(new OnItemLongClickListener() {
			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
				if (RWXX.TaskEnpriLink_isexcute.equals(status) && !new RWXX().JudgeUserName(currentID)) {
					Toast.makeText(SiteEvidenceActivity.this, "企业已上传,不能长按操作！", Toast.LENGTH_SHORT).show();
					return true;
				} else {
					String name = ((TextView) view.findViewById(R.id.item_text)).getText().toString();
					showDialog(name, position, view);
				}

				return true;
			}
		});

		lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> adapterview, View arg1, int position, long arg3) {
				ListView l = (ListView) adapterview;
				l.setFocusable(true);
				l.setFocusableInTouchMode(true);
				l.requestFocus();
				l.requestFocusFromTouch();
				l.setSelection(position);
			}
		});

		take_photo.setOnClickListener(new ActionOnClickListener() {

			@Override
			public void onClick(View v) {
				super.onClick(v);
				takePhoto();
			}
		});
		record_audio.setOnClickListener(new ActionOnClickListener() {

			@Override
			public void onClick(View v) {
				super.onClick(v);
				recordAudio();
			}
		});
		record_video.setOnClickListener(new ActionOnClickListener() {

			@Override
			public void onClick(View v) {
				super.onClick(v);
				recordVideo();
			}
		});
		select_attach.setOnClickListener(new ActionOnClickListener() {

			@Override
			public void onClick(View v) {
				super.onClick(v);
				takefigure();
			}
		});

		resetGridView();
//		// 增加判断 当附件数据为0 的时候查询网络数据
//		if ("taskInfoFlag".equals(taskInfoFlag)) {
//			data = new ArrayList<HashMap<String, Object>>();
//			// 从本地数据库中读取任务相关的附件信息
//			// String sql_attach =
//			// "select * from T_Attachment where fk_id like'%" + RWBH + "%'";
//			// data =
//			// SqliteUtil.getInstance().queryBySqlReturnArrayListHashMap(sql_attach);
//			// 修改读取任务相关的附件信息，调用后台接口，从后台数据库读取，再同步到本地数据库中 zhaorq2014.2.18
//			final ArrayList<HashMap<String, Object>> params0 = new ArrayList<HashMap<String, Object>>();
//			HashMap<String, Object> param0 = new HashMap<String, Object>();
//			param0.put("taskId", currentID);
//			param0.put("entId", qyid);
//			params0.add(param0);
//			HashMap<String, Object> param1 = new HashMap<String, Object>();
//			param1.put("token", "");
//			params0.add(param1);
//
//			new Thread() {
//				@Override
//				public void run() {
//					try {
//						String json0 = (String) WebServiceProvider.callWebService(Global.NAMESPACE, "GetTaskEntpriseAttachmentInfo", params0, Global.getGlobalInstance()
//								.getSystemurl() + Global.WEBSERVICE_URL, WebServiceProvider.RETURN_STRING, true);
//						Message msg = new Message();
//						msg.obj = json0;
//						handler.sendMessage(msg);
//
//					} catch (IOException e) {
//						e.printStackTrace();
//					}
//				}
//			}.start();
//		} else {
			loadAllFileThumbnails();
//		}
	}

	private abstract class ActionOnClickListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			isNeedLoad = false;
			updateFlag = false;
			backgroundRunFlag = false;
			
		}
	}

	/**
	 * 初始化公共父类的变量
	 */
	private void initGlobal() {
		CURRENT_ID = currentID;
		QYID = qyid;
		FK_ID = CURRENT_ID + "_" + QYID;
	}

	@Override
	protected void onResume() {
		super.onResume();
		backgroundRunFlag = false;
		Log.i("info", "flag:" + flag);
		if ((updateFlag && isNeedLoad) || hasUpdateFileFromServer) {
			adapter.clearData();
			loadAllFileThumbnails();
		}
		hasUpdateFileFromServer = false;
	}

	@Override
	protected void onPause() {
		super.onPause();
		backgroundRunFlag = true;
	}

	private void loadAllFileThumbnails() {
		adapter.clearData();
		new LoadThumbnailsTask().execute();
	}

	/**
	 * 刷新adapter
	 */

	private void addThumbFile(ThumbFile thumb) {
		adapter.add(thumb);
	}

	/**
	 * Description:显示长按对话框
	 * 
	 * @param name
	 * @param pos
	 * @author Administrator Create at: 2012-12-4 上午10:49:27
	 */
	protected void showDialog(final String name, final int pos, final View view) {
		AlertDialog.Builder dialog = new AlertDialog.Builder(this);
		String[] selections = { "重命名", "删除", "打印" };
		dialog.setItems(selections, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				switch (which) {
				case 0:
					LinearLayout ly = new LinearLayout(SiteEvidenceActivity.this);
					ly.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.FILL_PARENT));
					edtext = new EditText(SiteEvidenceActivity.this);
					edtext.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
					TextView tv = new TextView(SiteEvidenceActivity.this);
					tv.setText("名称：");
					ly.addView(tv);
					ly.addView(edtext);
					AlertDialog.Builder ab = new AlertDialog.Builder(SiteEvidenceActivity.this);
					ab.setTitle("重命名");
					ab.setIcon(R.drawable.icon_rename);
					ab.setView(ly);
					ab.setPositiveButton("确定", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							String newName = edtext.getText().toString();
							// String type =
							// name.substring(name.indexOf("."),name.length());
							String extension = fileList.get(pos).get("extension").toString();
							for (HashMap<String, Object> map : fileList) {
								if (map.get("filename").toString().equals(newName)) {
									Toast.makeText(SiteEvidenceActivity.this, "指定文件与现有文件重名!请更换名称", Toast.LENGTH_LONG).show();
									return;
								}
							}
							// String path=allFiles.get(pos).toString();

							String id = fileList.get(pos).get("guid").toString();
							if (extension.equals(".jpg")) {
								// File file2=new
								// File(TASK_PATH+"/"+currentID+"/"+qyid+"/RaskImage/"+edtext.getText().toString()+".jpg");
								String sql = "update T_Attachment set FileName = '" + newName + "' where guid = '" + id + "'";
								SqliteUtil.getInstance().execute(sql);
								// file1.renameTo(file2);
							} else if (extension.equals(".mp4")) {
								String sql = "update T_Attachment set FileName = '" + newName + "' where guid = '" + id + "'";
								SqliteUtil.getInstance().execute(sql);
							} else if (extension.equals(".amr")) {
								String sql = "update T_Attachment set FileName = '" + newName + "' where guid = '" + id + "'";
								SqliteUtil.getInstance().execute(sql);
							}
							loadAllFileThumbnails();
							Toast.makeText(SiteEvidenceActivity.this, "重命名成功！", Toast.LENGTH_LONG).show();
						}
					});
					ab.setNegativeButton("取消", null);
					ab.show();
					break;
				case 1:
					AlertDialog.Builder deleteab = new AlertDialog.Builder(SiteEvidenceActivity.this);
					deleteab.setTitle("删除");
					deleteab.setMessage("你确定要删除" + name + "吗？");
					deleteab.setIcon(R.drawable.icon_delete);
					deleteab.setPositiveButton("确定", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface arg0, int arg1) {
							String id = fileList.get(pos).get("guid").toString();
							String filepath = fileList.get(pos).get("filepath").toString();
							String FileName = fileList.get(pos).get("filename").toString();
							String Extension = fileList.get(pos).get("extension").toString();
							String sql = "delete from T_Attachment where guid = '" + id + "'";
							SqliteUtil.getInstance().execute(sql);
							fileList.get(pos).clear();
							fileList.remove(pos);
							File f = new File(TASK_PATH + "RWZX/" + filepath);
							if (f != null) {
								f.delete();
							}
							File file = new File(ROOT_TASK_PATH + FileName + Extension);
							if (file != null) {
								file.delete();
							}
							adapter.removeFileAt(pos);
							Toast.makeText(SiteEvidenceActivity.this, "删除" + name + "成功！", Toast.LENGTH_LONG).show();
						}

					});
					deleteab.setNegativeButton("取消", null);
					AlertDialog ad = deleteab.create();
					ad.show();
					break;

				case 2:
					if (name.endsWith("jpg") || name.endsWith("png")) {
						if (!PanduanDayin.appIsInstalled(SiteEvidenceActivity.this, "com.dynamixsoftware.printershare")) {
							PanduanDayin.insatll(SiteEvidenceActivity.this);
						} else {

							String filepath = fileList.get(pos).get("filepath").toString();

							String path = TASK_PATH + "RWZX/" + filepath;
							WorkAsyncTask task = new WorkAsyncTask();
							task.execute(path, String.valueOf(pos));
						}
					} else {
						Toast.makeText(SiteEvidenceActivity.this, "不是图片类型不能打印", 0).show();
					}

					break;

				}
			}
		}).show();
	}

	private class WorkAsyncTask extends AsyncTask<String, Void, Void> {
		private StringBuffer allHtml = new StringBuffer("");
		String tu;
		String pos;

		@Override
		protected Void doInBackground(String... params) {
			this.tu = params[0];
			this.pos = params[1];
			// 获取所需的信息
			// 模板编号
			// industryID = rwxxList.get(0).get("specialtemplateid").toString();

			allHtml.append(getCssString());// 样式
			allHtml.append(getCharsetString());// 字符集
			allHtml.append(getQYJBXXHeader(qyid, params[0], pos));// 企业信息

			writeFile(allHtml.toString(), tu);
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			String s = tu.substring(0, tu.lastIndexOf("/"));
			String g = tu.substring(tu.lastIndexOf("/") + 1, tu.lastIndexOf("."));
			String path3 = s + "/" + g + ".html";
			PanduanDayin.startprintshare(SiteEvidenceActivity.this, path3);
			super.onPostExecute(result);
		}
	}

	private void writeFile(String htmlInfo, String filePath) {
		String s = filePath.substring(0, filePath.lastIndexOf("/"));
		File current_task_dir = new File(s);
		if (!current_task_dir.exists()) {
			current_task_dir.mkdirs();
		}
		String g = filePath.substring(filePath.lastIndexOf("/") + 1, filePath.lastIndexOf("."));
		try {
			File f = new File(s + "/" + g + ".html");

			if (!f.exists()) {
				f.createNewFile();// 不存在则创建
			}
			BufferedWriter output = new BufferedWriter(new FileWriter(f));
			output.write(htmlInfo);
			output.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		Log.i("info", "resultCode:" + resultCode);
		if (resultCode != 0) {
			isNeedLoad = true;
		}
	}

	private void resetGridView() {
		adapter = new GridViewAdapter(this);
		gridView.setAdapter(adapter);
		//if (taskEnpriLinkState.equals("0")) {
		//BYK rwzt
			if (taskEnpriLinkState.equals("1")) {
			rwxx.changEnpriLinkState(currentID, qyid, "2");
				
		}
	}

	public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
		// 源图片的高度和宽度
		final int height = options.outHeight;
		final int width = options.outWidth;
		int inSampleSize = 1;
		if (height > reqHeight || width > reqWidth) {
			// 计算出实际宽高和目标宽高的比率
			final int heightRatio = Math.round((float) height / (float) reqHeight);
			final int widthRatio = Math.round((float) width / (float) reqWidth);
			// 选择宽和高中最小的比率作为inSampleSize的值，这样可以保证最终图片的宽和高
			// 一定都会大于等于目标的宽和高。
			inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
		}
		return inSampleSize;
	}

	/*
	 * 加载图片的异步任务
	 * 
	 * @author liaoningyutu
	 */

	class LoadThumbnailsTask extends AsyncTask<Object, ThumbFile, List<ThumbFile>> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			if (loading == null) {
				loading = new ProgressDialog(SiteEvidenceActivity.this);
				loading.setCancelable(true);
				loading.setMessage("加载中,请稍后...");
			}
			loading.show();
		}

		@Override
		protected List<ThumbFile> doInBackground(Object... params) {
			String sql = "select * from T_Attachment where fk_id = '" + fk_id + "'";
			fileList = SqliteUtil.getInstance().queryBySqlReturnArrayListHashMap(sql);
			if (fileList.size() == 0) {
				return null;
			}

			List<ThumbFile> lists = new ArrayList<SiteEvidenceActivity.ThumbFile>();

			// TODO:准备bms集合
			for (int i = 0; i < fileList.size() && !backgroundRunFlag; i++) {
				ThumbFile f = new ThumbFile();
				String extension = fileList.get(i).get("extension").toString();
				f.setExtension(extension);
				f.setFileName(fileList.get(i).get("filename").toString());
				String filePath = fileList.get(i).get("filepath").toString();
				f.setFilePath(filePath);

				Bitmap bm = null;

				if (extension.equals(".jpg") || extension.equals(".png")) {
					BitmapFactory.Options options = new BitmapFactory.Options();
					options.inJustDecodeBounds = true;
					String picLocation = TASK_PATH + "RWZX/" + filePath;
					BitmapFactory.decodeFile(picLocation, options);
					options.inSampleSize = calculateInSampleSize(options, 100, 100);
					options.inJustDecodeBounds = false;
					bm = BitmapFactory.decodeFile(picLocation, options);
				} else if (extension.equals(".amr")) {
					bm = BitmapFactory.decodeResource(getResources(), R.drawable.audio);
				} else if (extension.equals(".mp4")) {
					bm = ThumbnailUtils.createVideoThumbnail(TASK_PATH + "RWZX/" + filePath, 3);
				}

				f.setmBitmap(bm);

				lists.add(f);
			}
			Log.i("info", "长度：" + lists.size());
			return lists;
		}

		@Override
		protected void onPostExecute(List<ThumbFile> result) {
			super.onPostExecute(result);
			if (loading != null) {
				loading.dismiss();
			}
			adapter.addAll(result);
		}

		@Override
		protected void onProgressUpdate(ThumbFile... values) {
			addThumbFile(values[0]);
			super.onProgressUpdate(values);
		}

	}

	/**
	 * FileName: SiteEvidenceActivity.java Description:GridView数据适配器
	 * 
	 * @author Administrator
	 * @Version 1.3.4
	 * @Copyright 中科宇图天下科技有限公司 Create at: 2012-12-4 上午10:50:54
	 */
	private final class GridViewAdapter extends BaseAdapter {

		ArrayList<ThumbFile> bms = new ArrayList<ThumbFile>();
		LayoutInflater inflater;

		public void add(ThumbFile thumb) {
			List<String> names=new ArrayList<String>();
			bms.add(thumb);
			notifyDataSetChanged();
		}

		public void addAll(List<ThumbFile> result) {
			List<String> names=new ArrayList<String>();
			if (result != null) {
				bms.addAll(result);
				notifyDataSetChanged();
			}
		}

		public void removeFileAt(int position) {
			bms.remove(position);
			notifyDataSetChanged();
		}

		public void clearData() {
			bms.clear();
		}

		public GridViewAdapter(Context context) {
			inflater = LayoutInflater.from(context);
		}

		@Override
		public int getCount() {
			return bms.size();
		}

		@Override
		public Object getItem(int position) {
			return bms.get(position).getFilePath().toString();
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder viewHolder = null;
			if (convertView == null) {

				viewHolder = new ViewHolder();

				convertView = inflater.inflate(R.layout.grid_item, null);
				viewHolder.image = (ImageView) convertView.findViewById(R.id.item_image);
				viewHolder.name = (TextView) convertView.findViewById(R.id.item_text);
				viewHolder.time = (TextView) convertView.findViewById(R.id.item_time);

				convertView.setTag(viewHolder);
			} else {
				viewHolder = (ViewHolder) convertView.getTag();
				Bitmap bt = viewHolder.image.getDrawingCache();
				if (bt != null) {
					bt.recycle();
					bt = null;
				}
			}
			final int pos = position;
			// 获取文件名
			// final String nm=allFiles.get(pos).getName();
			String extension = bms.get(pos).getExtension().toString();
			final String nm = bms.get(pos).getFileName() + extension;
			

			viewHolder.name.setText(nm);
			// if(FileHelper.getFileType(allFiles.get(pos)).equals("jpg")){
			Bitmap b = bms.get(pos).getmBitmap();
			if (b != null) {
				viewHolder.image.setImageBitmap(b);
			} else {
				viewHolder.image.setImageResource(R.drawable.nopictures);
			}
			return convertView;
		}

		private class ViewHolder {
			ImageView image;
			TextView name, time;
		}
	}

	final class ThumbFile {
		private String fileName;
		private String extension;
		private String filePath;
		private Bitmap mBitmap;

		public String getFileName() {
			return fileName;
		}

		public void setFileName(String fileName) {
			this.fileName = fileName;
		}

		public String getExtension() {
			return extension;
		}

		public void setExtension(String extension) {
			this.extension = extension;
		}

		public String getFilePath() {
			return filePath;
		}

		public void setFilePath(String filePath) {
			this.filePath = filePath;
		}

		public Bitmap getmBitmap() {
			return mBitmap;
		}

		public void setmBitmap(Bitmap mBitmap) {
			this.mBitmap = mBitmap;
		}

	}

	/**
	 * Description:获取视频文件播放时长
	 * 
	 * @param context
	 * @param title
	 * @return
	 * @author Administrator Create at: 2012-12-4 上午10:53:50
	 */
	public String getVideoLength(Context context, String title) {
		ContentResolver resolver = context.getContentResolver();
		Cursor cursor = resolver.query(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, new String[] { MediaStore.Video.VideoColumns.DURATION }, " _display_name = ? ",
				new String[] { title }, null);
		String time = "";
		if (cursor != null && cursor.moveToFirst()) {
			time = cursor.getString(0);
		}
		cursor.close();
		return time;
	}

	public void RecursionDeleteFile(File file) {
		if (file.isFile()) {
			file.delete();
		}
	}

	private String getCharsetString() {
		return "<meta http-equiv=\"Content-Type\"content=\"text/html; charset=utf-8\"/>";
	}

	public static boolean appIsInstalled(Context context, String pageName) {
		try {
			context.getPackageManager().getPackageInfo(pageName, 0);
			return true;
		} catch (NameNotFoundException e) {
			return false;
		}
	}

	public void insatll() {
		Intent intent = new Intent(Intent.ACTION_VIEW);

		File file = getAssetFileToCacheDir(SiteEvidenceActivity.this, "xxx.apk");
		intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
		startActivity(intent);

	}

	public static File getAssetFileToCacheDir(Context context, String fileName) {
		try {
			File cacheDir = getCacheDir(context);
			final String cachePath = cacheDir.getAbsolutePath() + File.separator + fileName;
			InputStream is = context.getAssets().open(fileName);
			File file = new File(cachePath);
			file.createNewFile();
			FileOutputStream fos = new FileOutputStream(file);
			byte[] temp = new byte[1024];

			int i = 0;
			while ((i = is.read(temp)) > 0) {
				fos.write(temp, 0, i);
			}
			fos.close();
			is.close();
			return file;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static File getCacheDir(Context context) {
		String APP_DIR_NAME = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Android/data/";
		File dir = new File(APP_DIR_NAME + context.getPackageName() + "/cache/");
		if (!dir.exists()) {
			dir.mkdirs();
		}
		return dir;
	}

	private String getQYJBXXHeader(String qyid, String path, String pos) {
		HashMap<String, Object> qyjbxxData = getQYJBXXData(qyid);

		if (qyjbxxData.size() == 0) {
			qyjbxxData.put("qymc", "");
			qyjbxxData.put("frdb", "");
			qyjbxxData.put("qydz", "");
			qyjbxxData.put("yzbm", "");
			qyjbxxData.put("hblxr", "");
			qyjbxxData.put("frdbdh", "");

		}

		StringBuffer sb = new StringBuffer();
		sb.append("<body><table class=\"Main_Tab_Style\"  border=\"1\" cellpadding=\"2\" cellspacing=\"0\" style=\" width:95%;\">");
		sb.append("<thead style=\"display: table-header-group;\"><tr style=\"height: 50px;\"><td colspan=\"3\"><hr style=\"width: 800px; border: 1px solid #000; margin-top: 50px;\" /></td></tr></thead>");
		sb.append("<tbody runat=\"server\" id=\"pic\"><tr><td style=\"width: 50px; font-weight: bolder; font-size: large; text-align: center;\">图<br />片<br />"
				+ (Integer.parseInt(pos) + 1) + "</td>" + "<td colspan=\"2\" style=\"width: 600px;\"><img id=\"QZTP1\"   height= \"500px;\" width=\"90%\"  src='" + path
				+ "'  alt=\"\" /></td></tr>" + "</tbody>");
		SimpleDateFormat formatdate = new SimpleDateFormat("yyyy年MM月dd日HH时mm分ss秒");
		String time = formatdate.format(new Date());
		String di = "现场";
		HashMap<String, Object> conditions = new HashMap<String, Object>();
		conditions.put("UserID", Global.getGlobalInstance().getUserid());
		String ren = SqliteUtil.getInstance().getList("U_RealName", conditions, "PC_Users").get(0).get("u_realname").toString();
		Object zfzh = new BaseUsers(SiteEvidenceActivity.this).getDetailed(Global.getGlobalInstance().getUserid()).get("zfzh");
		String zhifaid = "";
		if (zfzh != null) {
			zhifaid = zfzh.toString();
		}
		sb.append(" <tfoot style=\"display: table-footer-group;\"><tr><td colspan=\"2\" style=\"width: 200px;\">证明对象：</td>" + "<td style=\"width: 430px;\">"
				+ "<label id=\"lblZMDX\" runat=\"server\">" + qyjbxxData.get("qymc").toString() + "</label></td></tr>"
				+ "<tr> <td colspan=\"2\">拍摄时间：</td><td> <label id=\"lblPSSJ\" runat=\"server\">" + time + "</label></td></tr>"
				+ "<tr> <td colspan=\"2\">拍摄地点：</td><td> <label id=\"lblPSSJ\" runat=\"server\">" + di + "</label></td></tr>"
				+ "<tr> <td colspan=\"2\">拍摄人：</td><td> <label id=\"lblPSSJ\" runat=\"server\">" + ren + "</label></td></tr>"
				+ "<tr> <td colspan=\"2\">当事人、见证人（签名、盖章或者按手印）：</td><td> <label id=\"lblPSSJ\" runat=\"server\"></label></td></tr>"
				+ "<tr> <td colspan=\"2\">执法人员(签名):</td><td> <label id=\"lblPSSJ\" runat=\"server\"></label></td></tr>"
				+ "<tr> <td colspan=\"2\">执法证号： </td><td> <label id=\"lblPSSJ\" runat=\"server\">" + zhifaid + "</label></td></tr>" +

				"</tfoot></table>");
		sb.append("<table><tr><td>        * 注：照片光盘、底片等应附证物袋</td></tr></table>");

		return sb.toString();
	}

	private HashMap<String, Object> getQYJBXXData(String qyid) {
		HashMap<String, Object> dataMap = new HashMap<String, Object>();
		String qyjbxxSql = "SELECT * FROM T_WRY_QYJBXX WHERE guid = '" + qyid + "';";
		ArrayList<HashMap<String, Object>> dataList = SqliteUtil.getInstance().queryBySqlReturnArrayListHashMap(qyjbxxSql);
		if (dataList != null && dataList.size() > 0) {
			dataMap = dataList.get(0);
		}
		return dataMap;
	}

	private static String getCssString() {

		String scc = "<head> <style type=\"text/css\">" + ".Main_Tab_Style td{" + "border:1px solid #65a3b4;" + " font-family:仿宋;" + "font-size:14pt;" + "padding:5px;}" +

		"</style></head>";
		return scc;
	}

}