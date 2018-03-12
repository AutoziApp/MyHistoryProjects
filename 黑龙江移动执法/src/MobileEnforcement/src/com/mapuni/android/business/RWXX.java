package com.mapuni.android.business;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.R.integer;
import android.app.AlertDialog;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.NotificationCompat;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.f;
import com.mapuni.android.MobileEnforcement.R;
import com.mapuni.android.attachment.T_Attachment;
import com.mapuni.android.base.Global;
import com.mapuni.android.base.business.BaseClass;
import com.mapuni.android.base.business.DataSyncModel;
import com.mapuni.android.base.interfaces.IDetailed;
import com.mapuni.android.base.interfaces.IInitData;
import com.mapuni.android.base.interfaces.IList;
import com.mapuni.android.base.interfaces.IQuery;
import com.mapuni.android.base.util.DisplayUitl;
import com.mapuni.android.base.util.ExceptionManager;
import com.mapuni.android.base.util.LogUtil;
import com.mapuni.android.base.util.OtherTools;
import com.mapuni.android.dataprovider.DESSecurity;
import com.mapuni.android.dataprovider.FileHelper;
import com.mapuni.android.dataprovider.JsonHelper;
import com.mapuni.android.dataprovider.SQLiteDataProvider;
import com.mapuni.android.dataprovider.SqliteUtil;
import com.mapuni.android.dataprovider.XmlHelper;
import com.mapuni.android.enforcement.SiteEvidenceActivity;
import com.mapuni.android.main.MainTabActivity;
import com.mapuni.android.netprovider.HttpUploader;
import com.mapuni.android.netprovider.Net;
import com.mapuni.android.netprovider.WebServiceProvider;
import com.mapuni.android.service.RydwServices;
import com.mapuni.android.taskmanager.TaskMainActivity;
import com.mapuni.android.taskmanager.TaskRegisterActivity;

/**
 * FileName: RWXX.java
 * 
 * @author 柳思远
 * @Version 1.3.4
 * @Copyright 中科宇图天下科技有限公司 Create at: 2012-12-4 上午10:51:15
 */
// public class RWXX extends BaseClass implements Serializable, IList,
// IDetailed,
// IQuery, IGrid {
public class RWXX extends BaseClass implements Serializable, IList, IDetailed,
		IQuery, IInitData {

	/** 任务序列化的号码 */
	private final long serialVersionUID = 19881121L;
	/** 实体类的名字 */
	public static final String BusinessClassName = "RWXX";
	/** 获取该实体类列表样式的标题 */
	private final String ListStyleName = "RWLB";
	/** 获取该实体类数据详情样式的标题 */
	private final String DetailedStyleName = "RWXX";
	/** 获取该实体类数据详情样式的标题 */
	private final String DetailedStyleNameOffice = "RWXXOffice";
	/** 获取该实体类查询样式的标题 */
	private final String QueryStyleName = "RWCX";
	/** 获取该实体类底部菜单的标题 */
	private final static String BottomMenuName = "RWGL";
	/** 该实体类所在表的主键 */
	private final String primaryKey = "Guid";
	/** 查询该实体类的相关信息所用的表名 */
	private final String TableName = "V_YDZF_RWXX";
	/** 该实体类在显示详情是所用的名字标题 */
	private final String DetailedTitleText = "任务详情";
	/** 该实体类在执行查询操作的时候所用的名字标题 */
	private final String QueryTitleText = "任务查询";
	/** 初始化当前该实体类列表滚动的次数 */
	public int ListScrollTimes;
	/** 当前对象的id值 */
	private String CurrentID = "";
	/** 该实体类的筛选条件集合 */
	private HashMap<String, Object> Filter;
	private static TaskResult tr = null;
	/** 用来对查询数据进行排序，此处是为了和sql语句拼接 */
	private final String order = "FBSJ desc";
	/** 实体类展示列表的时候显示的标题名称 */
	private final String TitleName = "任务列表";
	/** 定义一个用于放置返回ArrayList<HashMap<String, Object>>类型数据的集合 */
	private ArrayList<HashMap<String, Object>> dataList;

	/* 任务状态常量值 */
	/** 已下发 */
	public final static String RWZT_WATE_YIXIAFA = "";
	/** 待下发 */
	public final static String RWZT_WAIT_DISPATCH = "000";
	/** 待执行 */
	public final static String RWZT_WATE_EXECUTION = "003";
	/** 执行中（手机端） */
	public static final String RWZT_ON_EXECUTION = "005";
	/** 执行完成 */
	public static final String RWZT_EXECUTION_FINISH = "006";
	/** 待审核 */
	public static final String RWZT_WAIT_AUDIT = "007";
	/** 审核未通过 */
	public static final String RWZT_AUDIT_NOPASSED = "008";
	/** 任务完成 */
	public static final String RWZT_ON_FINISH = "009";
	/** 退回任务 */
	public static final String RWZT_ON_RETURN = "010";
	/** 待归档 */
	public static final String RWZT_WAIT_FILE = "011";
	/** 已挂起*/
	public static final String RWZT_YGQ = "012";
	
	/** 待上报*/
	public static final String RWZT_DSB = "013";

	/** 调查取证完成 */
	// public final static String TaskEnpriLink_isexcute = "1";
	public final static String TaskEnpriLink_isexcute = "3";
	/** 任务关联企业已上传 */
	public static final String TaskEnpriLink_UpLoad = "009";

	/* 任务类型常量值 */

	// 任务紧急程度，根据不同的紧急程度区分
	/** 一般 */
	public final static String JJCD_YB = "001";
	/** 紧急 */
	public final static String JJCD_JJ = "002";
	/** 非常紧急 */
	public final static String JJCD_FCJJ = "003";

	// 任务来源
	/** 现场执法来源 */
	public final static String XCZF_LY = "010";
	/** 一般来源 */
	public final static String YBRW_LY = "011";

	// 任务上传结果值

	/** 上传网络 不通 */
	private static final int NONET = 0;
	/** 上传成功 */
	private static final int UPLOAD_SUCCESS = 1;
	/** 上传失败 */
	private static final int UPLOAD_FAIL = 2;
	/** 任务已经上传 */
	private static final int ALREADY_UPLOAD = 3;

	private static final int PROGRESS = 5;

	private static final int UPLOAD_FILE_SUCCESS = 6;// 文件上传成功

	private int totalSize = 0;
	// 设置后台上传标识
	public static boolean isInBackgroundUpload = false;

	/** 用来限制任务列表显示，通过递归寻找到当前用户权限下的任务列表，该集合放置部门id */
	ArrayList<String> parentlist = new ArrayList<String>();

	/**
	 * 获取任务列表基本查询语句---》关联rwxxUser表--发布人或执行人必须为当前用户 关联PC——user表，查询执行人信息
	 */

	private String loadDataSql = "select distinct(a.RWBH),a.Guid,a.RWMC,a.FBSJ,a.RWZT,a.JJCD,a.BJQX,a.BZ,c.[U_LoginName] as fbr "
			+ "from T_YDZF_RWXX a "
			+ "left join T_YDZF_RWXX_USER b on b.[RWXXBH]=a.rwbh left join PC_Users c on c.[UserID]=b.UserID where a.RWLY<>'010' "
			+ "and (b.[UserID]= '"
			+ Global.getGlobalInstance().getUserid()
			+ "') ";
	NotificationManager manager;
	NotificationCompat.Builder builder;
	boolean doInBackground;// 后台下载标志
	boolean isActivityAlive = true;// 判断当前的宿主Activity是否存活

	public void setCurrentActivityIsAlive(boolean flag) {
		isActivityAlive = flag;
	}

	/**
	 * Description: 用来实现查询列表的时候进行分页显示
	 * 
	 * @return 返回一个用来拼接分页显示sql语句的字符串 String String
	 * @author 王红娟 Create at: 2012-12-3 上午11:13:20
	 */
	public String getOrder() {
		int count = Global.getGlobalInstance().getListNumber();
		int x = GetListScrolltimes() * count - count;
		int j = count;
		String order = " limit " + x + "," + j;
		return order;
	}

	@Override
	public void setListScrolltimes(int listScrollTimes) {
		ListScrollTimes = listScrollTimes;
	}

	@Override
	public int GetListScrolltimes() {
		return ListScrollTimes;
	}

	@Override
	public String GetKeyField() {
		return null;
	}

	@Override
	public String GetTableName() {
		return TableName;
	}

	/** IList */
	@Override
	public String getListTitleText() {
		return TitleName;
	}

	@Override
	public void setCurrentID(String currentIDValue) {
		CurrentID = currentIDValue;
	}

	/**
	 * @return 底部菜单配置文件节点名称
	 */
	public static String getBottommenuname() {
		return BottomMenuName;
	}

	/**
	 * 获取当前用户任务列表
	 */
	@Override
	public ArrayList<HashMap<String, Object>> getDataList() {

		return SqliteUtil.getInstance().queryBySqlReturnArrayListHashMap(
				loadDataSql + " order by " + order + getOrder());

	}

	/**
	 * 过滤获取当前用户任务列表 fliterHashMap 过滤条件
	 */
	@Override
	public ArrayList<HashMap<String, Object>> getDataList(
			HashMap<String, Object> fliterHashMap) {
		if (DisplayUitl.isOfficePerson()) {
			loadDataSql = "select distinct(a.RWBH),a.Guid,a.RWMC,a.FBSJ,a.RWZT,a.JJCD,a.BJQX,a.BZ,c.[U_LoginName] as fbr "
					+ "from T_YDZF_RWXX a "
					+ "left join T_YDZF_RWXX_USER b on b.[RWXXBH]=a.rwbh left join PC_Users c on c.[UserID]=b.UserID where a.RWLY<>'010' ";
		}
		StringBuilder sb = new StringBuilder(loadDataSql);
		if (fliterHashMap != null) {
			for (String key : fliterHashMap.keySet()) {
				if (fliterHashMap.get(key).equals(RWZT_WATE_EXECUTION)) {
					sb.append(" and ").append(
							"(" + key + " = '" + fliterHashMap.get(key) + "'");
					sb.append(" or ").append(
							"rwzt = '" + RWZT_ON_EXECUTION + "'" + ")");
				} else {
					sb.append(" and ").append(
							"(" + key + " = '" + fliterHashMap.get(key) + "'"
									+ ")");
				}
			}
		}
		sb.append(" order by " + order).append(getOrder());

		return SqliteUtil.getInstance().queryBySqlReturnArrayListHashMap(
				sb.toString());

	}

	/**
	 * 获取列表显示样式
	 */
	@Override
	public HashMap<String, Object> getStyleList(Context context)
			throws IOException {
		HashMap<String, Object> styleList = null;
		try {
			styleList = XmlHelper.getListStyleByName(ListStyleName,
					getStyleListInputStream(context));
		} catch (Exception e) {
			ExceptionManager.WriteCaughtEXP(e, "RWXX");
			e.printStackTrace();
		}
		return styleList;
	}

	@Override
	public HashMap<String, Object> getFilter() {
		return Filter;
	}

	/** Detailed */
	@Override
	public String getDetailedTitleText() {
		return DetailedTitleText;
	}

	@Override
	public String getCurrentID() {
		return CurrentID;
	}

	/**
	 * 获取任务详情
	 */
	@Override
	public HashMap<String, Object> getDetailed(String id) {
		HashMap<String, String> primaryKeyMap = new HashMap<String, String>();
		primaryKeyMap.put("key", primaryKey);
		primaryKeyMap.put("keyValue", id);
		return BaseClass.DBHelper.getDetailed(TableName, primaryKeyMap);
	}

	/**
	 * 获取任务详情显示样式
	 */
	@Override
	public ArrayList<HashMap<String, Object>> getStyleDetailed(Context context) {
		ArrayList<HashMap<String, Object>> styleDetailList = null;
		try {
			if (DisplayUitl.isOfficePerson()) {
				styleDetailList = XmlHelper.getStyleByName(
						DetailedStyleNameOffice,
						getStyleDetailedInputStream(context));
			} else {
				styleDetailList = XmlHelper.getStyleByName(DetailedStyleName,
						getStyleDetailedInputStream(context));
			}

		} catch (Exception e) {
			ExceptionManager.WriteCaughtEXP(e, "RWXX");
			e.printStackTrace();
		}
		return styleDetailList;
	}

	/**
	 * Description: 上传任务附件和任务信息
	 * 
	 * 
	 * @param taskIdFloder
	 *            压缩文件的路径
	 * @param context
	 *            上下文
	 * @return 上传附件的返回结果 1：成功，其他：失败 String
	 * @author 柳思远 Create at: 2012-12-4 上午11:19:20
	 */
	public String synchronizeUploadData(String taskIdFloder, final String RWBH,
			final String QYID, final Context context,
			final int mSingleChoiceID, final boolean isComplete, final int fType) {
		ArrayList<TaskFile> _ListAllFile = getAllUploadFile(RWBH, QYID,
				taskIdFloder);
		Log.i(TAG, "_ListAllFile--->>>" + _ListAllFile);

		final ArrayList<TaskFile> listAllFile = new ArrayList<RWXX.TaskFile>();
		/** 遍历所有文件 取出没有上传过的附件 */
		for (TaskFile _taskFile : _ListAllFile) {

			listAllFile.add(_taskFile);

		}

		if (listAllFile != null && listAllFile.size() > 0) {

			int temp = 0;
			for (int n = 0; n < listAllFile.size(); n++) {

				TaskFile taskFile = listAllFile.get(n);

				File absFile = new File(taskFile.getAbsolutePath());

				temp = (int) absFile.length() / 1024 / 1024;
				totalSize += temp;
			}
		}

		if (totalSize > 30) {
			AlertDialog dialog = new AlertDialog.Builder(context)
					.setTitle("提示")
					.setMessage("附件大小超过30M，建议使用WIFI上传，是否退出上传？")
					.setPositiveButton("是", null)
					.setNegativeButton("否",
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									TaskUploadAsync affixUploadAsync = new TaskUploadAsync(
											RWBH, QYID, listAllFile, context,
											mSingleChoiceID, isComplete, fType);
									affixUploadAsync.execute();
								}
							}).create();
			dialog.getWindow().setType(
					WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
			dialog.show();
		} else {
			TaskUploadAsync affixUploadAsync = new TaskUploadAsync(RWBH, QYID,
					listAllFile, context, mSingleChoiceID, isComplete, fType);
			affixUploadAsync.execute();
		}
		return "";
	}

	class TaskUploadAsync extends AsyncTask<Object, Integer, Object> {
		String rwbh;
		String qyid;
		ArrayList<TaskFile> listAllFile;
		String userZw;
		int mSingleChoiceID;
		ProgressDialog pdialog;
		private int index;
		/** 附件是否已经上传过 */
		Boolean isUpload = false;
		private Context mcontext;
		private boolean isComplete;
		private int fType;

		TaskUploadAsync(String rwbh, String qyid,
				ArrayList<TaskFile> listAllFile, Context mcontext,
				int mSingleChoiceID, boolean isComplete, int fType) {
			this.rwbh = rwbh;
			this.qyid = qyid;
			this.listAllFile = listAllFile;
			this.mcontext = mcontext;
			this.mSingleChoiceID = mSingleChoiceID;
			this.isComplete = isComplete;
			this.fType = fType;
			// 获得当前登录用户的职务
			HashMap<String, Object> conditions_user = new HashMap<String, Object>();
			conditions_user = new HashMap<String, Object>();
			conditions_user.put("UserID", Global.getGlobalInstance()
					.getUserid());
			userZw = SqliteUtil.getInstance()
					.getList("zw", conditions_user, "PC_Users").get(0)
					.get("zw").toString();
			pdialog = new ProgressDialog(mcontext, 0);
			pdialog.setCancelable(false);

			if (Net.checkNet(mcontext)) {
				isInBackgroundUpload = true;
				manager = (NotificationManager) TaskUploadAsync.this.mcontext
						.getSystemService(Context.NOTIFICATION_SERVICE);
				builder = new NotificationCompat.Builder(
						TaskUploadAsync.this.mcontext);
				builder.setContentTitle("任务附件上传")
						.setContentText("正在准备上传任务附件...")
						.setSmallIcon(R.drawable.yutu);
				manager.notify(0, builder.build());
				doInBackground = true;
				OtherTools
						.showToast(TaskUploadAsync.this.mcontext, "任务已转入后台上传");
			}

		}

		@Override
		protected void onCancelled() {
			super.onCancelled();
		}

		@Override
		protected void onPostExecute(Object result) {
			if (result != null) {
				isInBackgroundUpload = false;
				if (isActivityAlive) {

					Toast.makeText(mcontext, (CharSequence) result,
							Toast.LENGTH_SHORT).show();
				}
				sendNotification((String) result, 100);
			}
		}

		@Override
		protected void onPreExecute() {
			if (mSingleChoiceID == 1) {
			} else {
				if (listAllFile != null && listAllFile.size() > 0) {
					String fileName = listAllFile.get(index).getFileName()
							+ listAllFile.get(index).getExtension();
				} else {

				}
			}
		}

		protected void onProgressUpdate(Integer... values) {
			if (mSingleChoiceID == 0) {
				String fileName = listAllFile.get(index).getFileName()
						+ listAllFile.get(index).getExtension();
				if (values[0] == 100) {
					if (isUpload) {
						if (isActivityAlive) {

						}
						sendNotification(fileName + " 已经上传", values[0]);
						isUpload = false;
					} else {
						if (isActivityAlive) {

						}
						sendNotification(fileName + " 上传成功", values[0]);
						if (index == listAllFile.size() - 1) {// 附件已经上传完毕
							if (isActivityAlive) {
							}
							sendNotification("正在上传任务信息...", values[0]);
						}

					}
				} else {
					if (isActivityAlive) {
						pdialog.setProgress(values[0]);
					}
					sendNotification(fileName + " 正在上传...", values[0]);
				}
			}
		}

		public void sendNotification(String contentText, int progress) {
			if (builder != null) {
				builder.setContentText(contentText);
				builder.setProgress(100, progress, false);
				manager.notify(0, builder.build());
			}

		}

		@Override
		protected Object doInBackground(Object... params) {

			if (mSingleChoiceID == 0) {
				String serverTime = DisplayUitl.getServerTime();// 首先更新附件表中的更新时间
				if (serverTime != null && !serverTime.equals("")) {
					for (TaskFile taskFile : listAllFile) {// 更新updatetime
						// 确保其他人能够同步此数据
						String guid = taskFile.getGuid();
						ContentValues updateValues = new ContentValues();
						updateValues.put("UpdateTime", serverTime);
						String[] whereArgs = { guid };
						try {
							SqliteUtil.getInstance().update("T_Attachment",
									updateValues, "guid=?", whereArgs);
						} catch (FileNotFoundException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

					}
				} else {

					return "网络连接异常，请检查网络设置后重试！";
				}

				HttpUploader httpUploader = new HttpUploader();

				httpUploader.setWebServiceUrl(Global.getGlobalInstance()
						.getSystemurl() + Global.WEBSERVICE_URL);
				if (listAllFile.size() != 0) {
					// 提前添加数据BYK
					addBL("现场取证资料", rwbh, qyid);
				}

				for (int n = 0; n < listAllFile.size(); n++) {

					TaskFile taskFile = listAllFile.get(n);

					ArrayList<HashMap<String, Object>> params0 = new ArrayList<HashMap<String, Object>>();
					HashMap<String, Object> param0 = new HashMap<String, Object>();
					String path = T_Attachment.transitToChinese(Integer
							.valueOf(taskFile.getUnitId()))
							+ "/"
							+ taskFile.getFilePath();
					param0.put("fileGuid", taskFile.getGuid());
					param0.put("path", path);
					params0.add(param0);

					int finishblocks = 0;// 断点包数

					try {
						Object resultResponseObj0 = WebServiceProvider
								.callWebService(Global.NAMESPACE,
										"GetProgress", params0, Global
												.getGlobalInstance()
												.getSystemurl()
												+ Global.WEBSERVICE_URL,
										WebServiceProvider.RETURN_INT, true);
						if (null != resultResponseObj0) {
							finishblocks = Integer.parseInt(resultResponseObj0
									.toString());
						} else {
							return "获取附件断点信息异常";
						}
						if (finishblocks == -1) {
							return "获取附件断点信息失败";
						}

						if (finishblocks == 20000) {
							isUpload = true;
							publishProgress(100);
							continue;
						}

					} catch (Exception e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}

					FileInputStream fis = null;

					try {

						File absFile = new File(taskFile.getAbsolutePath());
						fis = new FileInputStream(absFile);

						boolean isEnd = false;

						int totalblocks = (int) absFile.length() / (1024 * 500)
								+ 1;
						for (int i = 0; i < totalblocks; i++) {
							if (i < finishblocks) {
								byte[] buffers = new byte[1024 * 500];
								fis.read(buffers);
								continue;
							}
							String attachmentData = "";
							if (i == totalblocks - 1) {
								isEnd = true;
								byte[] buffers = new byte[(int) absFile
										.length() % (1024 * 500)];

								fis.read(buffers);

								attachmentData = Base64.encodeToString(buffers,
										Base64.DEFAULT);
								LogUtil.i("222", attachmentData);

							} else {
								byte[] buffer = new byte[1024 * 500];

								fis.read(buffer);
								attachmentData = Base64.encodeToString(buffer,
										Base64.DEFAULT);

							}

							String AttachmentJosn = "["
									+ GetFujian(taskFile, i + "", rwbh, fType,
											qyid).toString() + "]";
							/**
							 * 上传附件的操作 BYK
							 * */
							Boolean resultBoolean = httpUploader
									.upLoadAffixMethod(AttachmentJosn,
											attachmentData, isEnd);
							if (resultBoolean) {
								index = n;
								publishProgress((i + 1) * 100 / totalblocks);

							} else {
								return "上传附件失败,请检查网络是否正常！";
							}

						}

					} catch (Exception e) {
						if (e.getMessage() != null) {
							Log.e(TAG, e.getMessage());
						}

						return "上传附件出现异常";
					} finally {
						try {
							if (fis != null) {
								fis.close();
							}
						} catch (IOException e) {
							Log.e(TAG, e.getMessage());
						}
					}
				}
				isInBackgroundUpload = false;
			}

			String result = "";
			if (listAllFile.size() == 0) {
				mSingleChoiceID = 0;
			}
			// 上传任务信息 BYK
			int _Result11 = uploadYDZF_TaskSpecialItem(rwbh, qyid, isComplete);

			int lzjd = uploadLZJD_webservice(rwbh, qyid);
			// BYK
			// Boolean executeFinish_byk = requestTaskExecuteFinish_byk(rwbh);
			// rwGuid = UUID.randomUUID().toString();
			if (_Result11 == 1 || _Result11 == 2) {
				if (lzjd == 1||lzjd==-1) {

					if (isComplete) {
						// BYK rwzt
						String sql = "update TaskEnprilink set isexcute ='3' where TaskID='"
								+ rwbh + "' and QYID='"+qyid+"'";

						SqliteUtil.getInstance().execute(sql);

//						String sql2 = "update T_YDZF_RWXX set RWZT='006' where RWBH='"
//								+ rwbh + "' and QYDM='"+qyid+"'";
						String sql2 = "update T_YDZF_RWXX set RWZT='006' where RWBH='"
								+ rwbh + "'";
						SqliteUtil.getInstance().execute(sql2);
					} else {
						String sql = "update TaskEnprilink set isexcute ='2' where TaskID='"
								+ rwbh + "'";

						SqliteUtil.getInstance().execute(sql);

						String sql2 = "update T_YDZF_RWXX set RWZT='005' where RWBH='"
								+ rwbh + "' and QYDM ='"+qyid+"'";
						SqliteUtil.getInstance().execute(sql2);
					}

					result = "任务上传成功";
				} else {

					result = "任务上传失败";
				}
	

			} else {
				result = "任务上传失败";
			}

			return result;

		}

	}

	private int uploadLZJD_webservice(String rwbh2, String qyid2) {
		int _Result = 0;
		String methodName = "UploadEnt_SupervisionResultJson";
		String lzjd_billid = GetExeLawsTempletItems_lzjdk(rwbh2, qyid2);
		String sql = "select * from Supervision where billcode = '"
				+ lzjd_billid + "'";
		// 查询获取 数据
		ArrayList<HashMap<String, Object>> queryData = SqliteUtil.getInstance()
				.queryBySqlReturnArrayListHashMap(sql);
		
		if (queryData==null||queryData.size()==0) {
			return -1;
		}

		ArrayList<String> tables = new ArrayList<String>();
		tables.add("Supervision");

		String json = JsonHelper.listToJSON(queryData);
		HashMap<String, Object> _HashMap = new HashMap<String, Object>();
		_HashMap.put("jsonStr", json);
		_HashMap.put("userCode", Global.getGlobalInstance().getUserid());

		ArrayList<HashMap<String, Object>> _ArrayListParam = new ArrayList<HashMap<String, Object>>();
		_ArrayListParam.add(_HashMap);

		try {
			_Result = (Integer) WebServiceProvider.callWebService(
					Global.NAMESPACE, methodName, _ArrayListParam, Global
							.getGlobalInstance().getSystemurl()
							+ Global.WEBSERVICE_URL,
					WebServiceProvider.RETURN_INT, true);

		} catch (IOException e) {
			e.printStackTrace();
		}
		if (_Result != 0) {// 设置任务状态有改变，通知刷新任务列表
			Global.getGlobalInstance().setTaskStateChange(true);
			DataSyncModel dm = new DataSyncModel();
			// TODO 明天修改这里 打开 同步廉政表
			// dm.syncServiceData(tables, true);
			OtherTools.showLog("T_YDZF_RWXX,附件信息表更新成功");
		}

		return _Result;
	}

	private class TaskFile {
		private String guid;
		private String fileName;
		private String unitId;
		private String absolutePath;
		private String extension;
		private String filePath;

		public String getFilePath() {
			return filePath;
		}

		public void setFilePath(String filePath) {
			this.filePath = filePath;
		}

		public String getExtension() {
			return extension;
		}

		public void setExtension(String extension) {
			this.extension = extension;
		}

		public String getGuid() {
			return guid;
		}

		public void setGuid(String guid) {
			this.guid = guid;
		}

		public String getFileName() {
			return fileName;
		}

		public void setFileName(String fileName) {
			this.fileName = fileName;
		}

		public String getUnitId() {
			return unitId;
		}

		public void setUnitId(String unitId) {
			this.unitId = unitId;
		}

		public String getAbsolutePath() {
			return absolutePath;
		}

		public void setAbsolutePath(String absolutePath) {
			this.absolutePath = absolutePath;
		}

	}

	/**
	 * 任务执行完成提交
	 */
	/**
	 * 调用Webservice
	 * */
	public Boolean requestTaskExecuteFinish_byk(String rwbh) {
		Boolean resultBoolean = false;
		if (Net.checkURL(Global.getGlobalInstance().getSystemurl())) {
			String methodName = "MobileTaskExecute";

			ArrayList<HashMap<String, Object>> params = new ArrayList<HashMap<String, Object>>();
			HashMap<String, Object> param = new HashMap<String, Object>();

			// 添加数据
			ArrayList<HashMap<String, Object>> data_json = new ArrayList<HashMap<String, Object>>();
			HashMap<String, Object> map = new HashMap<String, Object>();

			map.put("FromChannel", "2");
			map.put("RegionCode", Global.getGlobalInstance().getAreaCode());
			// 任务id
			// map.put("CurrentFlowTaskId",getTaskStatus(rwbh));
			map.put("CurrentFlowTaskId", rwbh);
			map.put("TransactorName", Global.getGlobalInstance().getUserid());
			map.put("TransactionType", "29");
			map.put("Comment", "任务完成");
			data_json.add(map);
			String DataJson = JsonHelper.listToJSONXin(data_json);
			param.put("parms", DataJson);
			param.put("coTransactorArry", "");
			params.add(param);
			try {
				String json = (String) WebServiceProvider.callWebService(
						Global.NAMESPACE, methodName, params, Global
								.getGlobalInstance().getSystemurl()
								+ Global.WEBSERVICE_URL,
						WebServiceProvider.RETURN_STRING, true);
				if (json != null && !json.equals("")) {
					if (json.contains("true")) {
						resultBoolean = true;
					} else {
						resultBoolean = false;
					}
				} else {
					resultBoolean = false;
				}
			} catch (IOException e) {

				e.printStackTrace();
			}
		}
		return resultBoolean;
	}

	/**
	 * 上传任务关联的企业信息，如果任务来源为现场执法会上传任务信息，改变任务状态，上传之后更新本地数据库
	 * 
	 * @param RWBH
	 * @param qyid
	 * @return
	 */
	public int uploadYDZF_TaskSpecialItem(String RWBH, String qyid,
			boolean isComplete) {

		String methodName = "UploadTaskExecutionResultJson";
		JSONArray _JsonTaskSpecialItem = GetSpecialItem(RWBH, qyid);

		int _Result = 0;

		String rwly = queryTaskSource(RWBH);
		ArrayList<String> tables = new ArrayList<String>();
		tables.add("TaskEnpriLink");// TaskEnpriLink表中状态发生 改变，需要同步一下
		tables.add("T_YDZF_RWXX");// T_YDZF_RWXX表中状态发生 改变，需要同步一下
		// 同步一下着两张表
		DataSyncModel dm2 = new DataSyncModel();

	//	dm2.syncServiceData(tables, true);

		JSONArray _JsonTaskEntprilink = GetEntpriLinkItems(RWBH, qyid,
				isComplete);
		JSONArray _JsonKCBL = GetKCBLItems(RWBH, qyid);
		JSONArray _JsonXWBL = GetXWBLItems(RWBH, qyid);
		JSONArray _JsonWD = GetWDItems(RWBH, qyid);
		JSONArray _JsonJSBL = GetJSTZSItems(RWBH, qyid);
		JSONArray _JsonXCBL = GetXCHJItems(RWBH, qyid);
		// addBL("现场取证资料", RWBH, qyid);
		JSONArray _JsonExeLawsTemplet = GetExeLawsTempletItems(RWBH, qyid);
		String _JsonStr = "";
		try {
			/** 获得加密字符串 */
			_JsonStr = DESSecurity.encrypt(methodName);
		} catch (Exception e1) {
			e1.printStackTrace();
		}

		HashMap<String, Object> _HashMap = new HashMap<String, Object>();
		_HashMap.put("TaskSpecialItemJson", _JsonTaskSpecialItem.toString()
				+ "");
		_HashMap.put("KCBLJson", _JsonKCBL.toString() + "");
		_HashMap.put("XWBLJson", _JsonXWBL.toString() + "");
		_HashMap.put("WDJson", _JsonWD.toString() + "");
		if (rwly.equals("010")) {// 现场执法任务,上传成功之后直接更改任务状态为待审核
			if (isComplete) {
				_HashMap.put("TaskJson", GetTask(RWBH, RWZT_ON_FINISH)
						.toString());

			} else {
				_HashMap.put("TaskJson", GetTask(RWBH, RWZT_ON_EXECUTION)
						.toString());

			}

			_HashMap.put("TaskUserJson", GetUser(RWBH).toString() + "");
		} else {
			// _HashMap.put("TaskJson", GetTask(RWBH, RWZT_ON_EXECUTION)
			// .toString());
			if (isComplete) {
				_HashMap.put("TaskJson", GetTask(RWBH, RWZT_ON_FINISH)
						.toString());

			} else {
				_HashMap.put("TaskJson", GetTask(RWBH, RWZT_ON_EXECUTION)
						.toString());

			}
			_HashMap.put("TaskUserJson", "[]");
		}
		_HashMap.put("TaskEntpriLinkJson", _JsonTaskEntprilink.toString() + "");
		_HashMap.put("DCTZSJson", _JsonJSBL.toString() + "");
		_HashMap.put("JCJLJson", _JsonXCBL.toString() + "");
		_HashMap.put("ExeLawsTemplet", _JsonExeLawsTemplet.toString());
		_HashMap.put("token", _JsonStr);
		_HashMap.put("ZPSM", "[]");
		ArrayList<HashMap<String, Object>> _ArrayListParam = new ArrayList<HashMap<String, Object>>();
		_ArrayListParam.add(_HashMap);

		try {
			_Result = (Integer) WebServiceProvider.callWebService(
					Global.NAMESPACE, methodName, _ArrayListParam, Global
							.getGlobalInstance().getSystemurl()
							+ Global.WEBSERVICE_URL,
					WebServiceProvider.RETURN_INT, true);
		} catch (IOException e) {
			e.printStackTrace();
		}
		if (_Result != 0) {// 设置任务状态有改变，通知刷新任务列表
			Global.getGlobalInstance().setTaskStateChange(true);
			DataSyncModel dm = new DataSyncModel();
			dm.syncServiceData(tables, true);
			OtherTools.showLog("T_YDZF_RWXX,附件信息表更新成功");
		}
		return _Result;

	}

	public String getQYMC_form_GUID(String guid) {
		String sql = "select qymc from T_Wry_qyjbxx where guid = '" + guid
				+ "'";
		ArrayList<HashMap<String, Object>> list = SqliteUtil.getInstance()
				.queryBySqlReturnArrayListHashMap(sql);
		if (list.size() > 0) {
			return String.valueOf(list.get(0).get("qymc"));
		}
		return "";
	}

	/**
	 * 添加现场取证名称
	 * 
	 * @param name
	 *            取证名称
	 * */
	private void addBL(String name, String rwbh, String qyid) {
		SqliteUtil sqliteUtil = SqliteUtil.getInstance();
		RWXX rwxx = new RWXX();
		String rwzt = rwxx.getTaskid(rwbh);

		ArrayList<HashMap<String, Object>> kc_dataList = getKCData(rwbh, qyid);
		if (kc_dataList.size() > 0) {
			return;
		}

		if (rwzt.equals("003") || rwzt.equals("005")) {

		} else {
			kc_dataList = new ArrayList<HashMap<String, Object>>();
			return;
		}

		ContentValues contentValues = new ContentValues();
		String gUIDString = UUID.randomUUID().toString();
		contentValues.put("id",gUIDString );
		contentValues.put("taskid", rwbh);// 任务ID
		contentValues.put("enterid", qyid);// 污染源(企业)ID
		contentValues.put("updatetime", new SimpleDateFormat(
				"yyyy-MM-dd HH:mm:ss").format(new java.util.Date()));
		String items2 = GetExeLawsTempletItems2(rwbh, qyid);
		if ("".equals(items2)) {
			contentValues.put("billid", gUIDString);
		} else {
			contentValues.put("billid", items2);
		}
		contentValues.put("templetname", name);
		contentValues.put("templettype", "XCQZ");
		sqliteUtil.insert(contentValues, "ExeLawsTemplet");
	}

	public void addBL2(String name, String rwbh, String qyid, String tid) {
		SqliteUtil sqliteUtil = SqliteUtil.getInstance();
		RWXX rwxx = new RWXX();
		String rwzt = rwxx.getTaskid(rwbh);

		ArrayList<HashMap<String, Object>> kc_dataList = getKCData(rwbh, qyid);
		if (kc_dataList.size() > 0) {
			return;
		}

		if (rwzt.equals("003") || rwzt.equals("005")) {

		} else {
			kc_dataList = new ArrayList<HashMap<String, Object>>();
			return;
		}

		ContentValues contentValues = new ContentValues();
		contentValues.put("id", UUID.randomUUID().toString());
		contentValues.put("taskid", rwbh);// 任务ID
		contentValues.put("enterid", qyid);// 污染源(企业)ID
		contentValues.put("updatetime", new SimpleDateFormat(
				"yyyy-MM-dd HH:mm:ss").format(new java.util.Date()));
		String items2 = GetExeLawsTempletItems2(rwbh, qyid);
		if ("".equals(items2)) {
			contentValues.put("billid", UUID.randomUUID().toString());
		} else {
			contentValues.put("billid", items2);
		}

		contentValues.put("templetname", name);
		contentValues.put("templettype", tid);
		sqliteUtil.insert(contentValues, "ExeLawsTemplet");
	}

	/**
	 * 获取现场取证数据
	 * 
	 * @return
	 */
	private ArrayList<HashMap<String, Object>> getKCData(String rwbh,
			String qyid) {

		ArrayList<HashMap<String, Object>> data = new ArrayList<HashMap<String, Object>>();

		/** 查询出笔录表中的数据 */
		data = SqliteUtil
				.getInstance()
				.queryBySqlReturnArrayListHashMap(
						"select * from ExeLawsTemplet where taskid = '"
								+ rwbh
								+ "' and enterid = '"
								+ qyid
								+ "' and templettype = 'XCQZ' order by updatetime desc");
		return data;
	}

	/**
	 * 获取清单数据
	 * */
	public ArrayList<HashMap<String, Object>> getQDData(String rwbh,
			String qyid, String tid) {

		ArrayList<HashMap<String, Object>> data = new ArrayList<HashMap<String, Object>>();

		data = SqliteUtil.getInstance().queryBySqlReturnArrayListHashMap(
				"select * from ExeLawsTemplet where taskid = '" + rwbh
						+ "' and enterid = '" + qyid + "' and templettype = '"
						+ tid + "' order by updatetime desc");
		return data;
	}

	/**
	 * 上传任务关联的企业信息，如果任务来源为现场执法会上传任务信息，改变任务状态，上传之后更新本地数据库
	 * 
	 * @param RWBH
	 * @param qyid
	 * @return
	 */
	public int uploadSimpleLawXWBL(String RWBH) {
		String methodName = "UploadTaskExecutionResultJson";
		JSONArray jsonarray = new JSONArray();
		JSONArray _JsonTaskSpecialItem = jsonarray;

		int _Result = 0;

		String rwly = queryTaskSource(RWBH);
		ArrayList<String> tables = new ArrayList<String>();
		tables.add("TaskEnpriLink");// TaskEnpriLink表中状态发生 改变，需要同步一下
		tables.add("T_YDZF_RWXX");// T_YDZF_RWXX表中状态发生 改变，需要同步一下
		JSONArray _JsonTaskEntprilink = GetEntpriLinkItems(RWBH);
		;
		JSONArray _JsonKCBL = jsonarray;
		JSONArray _JsonXWBL = GetXWBLItems(RWBH);
		JSONArray _JsonWD = GetWDItems(RWBH);
		JSONArray _JsonJSBL = jsonarray;
		JSONArray _JsonXCBL = jsonarray;
		String _JsonStr = "";
		try {
			/** 获得加密字符串 */
			_JsonStr = DESSecurity.encrypt(methodName);
		} catch (Exception e1) {
			e1.printStackTrace();
		}

		HashMap<String, Object> _HashMap = new HashMap<String, Object>();
		if (rwly.equals("013")) {// 简易执法任务,上传成功之后直接更改任务状态为待审核
			_HashMap.put("TaskJson", GetTask(RWBH, RWZT_ON_FINISH).toString());
			_HashMap.put("TaskUserJson", GetUser(RWBH).toString() + "");
		}

		_HashMap.put("TaskSpecialItemJson", _JsonTaskSpecialItem.toString()
				+ "");

		_HashMap.put("TaskEntpriLinkJson", _JsonTaskEntprilink.toString() + "");
		_HashMap.put("KCBLJson", _JsonKCBL.toString() + "");
		_HashMap.put("XWBLJson", _JsonXWBL.toString() + "");
		_HashMap.put("DCTZSJson", _JsonJSBL.toString() + "");
		_HashMap.put("JCJLJson", _JsonXCBL.toString() + "");
		_HashMap.put("WDJson", _JsonWD.toString() + "");
		_HashMap.put("ExeLawsTemplet", "[]");
		_HashMap.put("token", _JsonStr);

		ArrayList<HashMap<String, Object>> _ArrayListParam = new ArrayList<HashMap<String, Object>>();
		_ArrayListParam.add(_HashMap);

		try {
			_Result = (Integer) WebServiceProvider.callWebService(
					Global.NAMESPACE, methodName, _ArrayListParam, Global
							.getGlobalInstance().getSystemurl()
							+ Global.WEBSERVICE_URL,
					WebServiceProvider.RETURN_INT, true);
		} catch (IOException e) {
			e.printStackTrace();
		}
		if (_Result != 0) {// 设置任务状态有改变，通知刷新任务列表
			Global.getGlobalInstance().setTaskStateChange(true);
			DataSyncModel dm = new DataSyncModel();
			dm.syncServiceData(tables, true);
		}
		return _Result;

	}

	/**
	 * Description: 获取任务执行人
	 * 
	 * @param _TaskId
	 *            任务编号
	 * @return 返回一条任务关联的执行人的信息，以json数组格式返回 JSONArray
	 * @author 柳思远 Create at: 2012-12-4 上午11:36:33
	 */
	private JSONArray GetUser(String RWBH) {
		ArrayList<HashMap<String, Object>> _ArrayList = SqliteUtil
				.getInstance().getList(
						"T_YDZF_RWXX_USER where RWXXBH='" + RWBH + "'");

		JSONArray _JsonArray = new JSONArray();

		try {
			for (int i = 0; i < _ArrayList.size(); i++) {
				HashMap<String, Object> _HashMapTemp = _ArrayList.get(i);
				Set _Iterator = _HashMapTemp.entrySet();
				JSONObject _JsonObjectData = new JSONObject();
				for (Iterator iter = _Iterator.iterator(); iter.hasNext();) {

					Map.Entry entry = (Map.Entry) iter.next();
					String _Key = entry.getKey().toString();

					Object _Object = _HashMapTemp.get(_Key);
					_JsonObjectData.put(_Key, _Object);

				}
				_JsonArray.put(_JsonObjectData);
			}
		} catch (Exception ex) {
			LogUtil.v("Json", ex.getMessage());
		}

		return _JsonArray;
	}

	/**
	 * 获取所有该任务相关执行企业未上传的信息
	 * 
	 * @param rwbh
	 * @return
	 */
	public ArrayList<HashMap<String, Object>> getUploadConditions(String rwbh) {
		ArrayList<HashMap<String, Object>> returndata = new ArrayList<HashMap<String, Object>>();
		SqliteUtil su = SqliteUtil.getInstance();
		// String sql = "select * from TaskEnpriLink where taskid='" + rwbh
		// + "' and isexcute<>'1'";
		// BYK rwzt
		String sql = "select * from TaskEnpriLink where taskid='" + rwbh
				+ "' and isexcute<>'3'";
		ArrayList<HashMap<String, Object>> data = su
				.queryBySqlReturnArrayListHashMap(sql);
		if (data.size() == 0) {
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("flag", "true");
			returndata.add(map);
		} else {
			for (HashMap<String, Object> m : data) {
				// BYK rwzt
				if (!m.get("isexcute").equals("3")) {
					// if (!m.get("isexcute").equals("1")) {
					returndata.add(m);
				}
			}
		}
		return returndata;
	}

	/**
	 * Description: 获取任务信息
	 * 
	 * @param _TaskId
	 *            任务id
	 * @param state
	 *            操作状态：upload上传和download下发
	 * @return 任务信息以jsonObject类型返回 JSONObject
	 * @author 柳思远 Create at: 2012-12-4 上午11:37:56
	 */
	public JSONArray GetTask(String RWBH, String rwzt) {
		JSONArray jsonArray = new JSONArray();
		JSONObject _JsonObject = new JSONObject();
		ArrayList<HashMap<String, Object>> _TaskList = SqliteUtil.getInstance()
				.getList("T_YDZF_RWXX where RWBH='" + RWBH + "'");
		if (_TaskList.size() != 0) {

			try {
				HashMap<String, Object> _HashMapTemp = _TaskList.get(0);
				Set _Iterator = _HashMapTemp.entrySet();

				for (Iterator iter = _Iterator.iterator(); iter.hasNext();) {

					Map.Entry entry = (Map.Entry) iter.next();

					String _Key = entry.getKey().toString();
					// 修改上传任务时候上传的参数 BYK
					if (_Key.equals("rwbh")) {
						_Key = "TaskCode";
						_JsonObject.put(_Key, entry.getValue().toString());
					}
					if (_Key.equals("rwmc")) {
						_Key = "TaskName";
						_JsonObject.put(_Key, entry.getValue().toString());
					}
					if (_Key.equals("syncdataregioncode")) {
						_Key = "RegionCode";
						_JsonObject.put(_Key, entry.getValue().toString());
					}
					if (_Key.equals("fbsj")) {
						_Key = "PublishedTime";
						_JsonObject.put(_Key, entry.getValue().toString());
					}
					if (_Key.equals("bjqx")) {
						_Key = "TransactedTime";
						_JsonObject.put(_Key, entry.getValue().toString());
					}

					if (_Key.equals("rwzt")) {
						_JsonObject.put(_Key, rwzt);// 修改任务状态为待审核
						// _JsonObject.put(_Key, entry.getValue().toString());
					}

				}
				// BYK
				_JsonObject.put("Urgency", "001");
				_JsonObject.put("TaskSource", "010");
				_JsonObject.put("FromChannel", "2");
				_JsonObject.put("TaskType", "001");
				_JsonObject.put("Publisher", Global.getGlobalInstance()
						.getUserid());

			} catch (JSONException ex) {
				LogUtil.v("Json", ex.getMessage());
			}
		}
		jsonArray.put(_JsonObject);
		return jsonArray;
	}

	private JSONObject GetFujian(TaskFile taskFile, String i, String rwbh,
			int fType, String qyid) {
		JSONObject _JsonObject = new JSONObject();
		HashMap<String, Object> conditions = new HashMap<String, Object>();
		conditions.put("guid", taskFile.getGuid());

		ArrayList<HashMap<String, Object>> _TaskList = SqliteUtil.getInstance()
				.getList("T_Attachment", conditions);

		try {
			HashMap<String, Object> _HashMapTemp = _TaskList.get(0);
			Set _Iterator = _HashMapTemp.entrySet();

			for (Iterator iter = _Iterator.iterator(); iter.hasNext();) {

				Map.Entry entry = (Map.Entry) iter.next();

				String _Key = entry.getKey().toString();
				if (_Key.contains("filepath")) {
					String fileNameWithPath = entry.getValue().toString();
					fileNameWithPath = fileNameWithPath + "." + i;
					_JsonObject.put("fileNameWithPath", fileNameWithPath);
				}
				_JsonObject.put(_Key, entry.getValue().toString());
				// Object _Object = _HashMapTemp.get(_Key);

			}

			// BYK 获取billid

			// JSONArray items = GetExeLawsTempletItems(rwbh, qyid);
			String billid = GetExeLawsTempletItems2(rwbh, qyid);

			_JsonObject.put("billid", billid);

			_JsonObject.put("biztype", fType);

		} catch (JSONException ex) {
			LogUtil.v("Json", ex.getMessage());
		}
		return _JsonObject;
	}

	/**
	 * Description: 获取专项数组
	 * 
	 * @param _TaskId
	 *            任务编号
	 * @return 专项信息以json数组形式返回 JSONArray
	 * @author 柳思远 Create at: 2012-12-4 上午11:40:26
	 */
	private JSONArray GetSpecialItem(String RWBH, String qyid) {

		ArrayList<HashMap<String, Object>> _ArrayList = SqliteUtil
				.getInstance().getList(
						"YDZF_TaskSpecialItem where TaskId='" + RWBH
								+ "' and EnterID ='" + qyid + "'");

		JSONArray _JsonArray = new JSONArray();
		try {
			for (int i = 0; i < _ArrayList.size(); i++) {
				HashMap<String, Object> _HashMapTemp = _ArrayList.get(i);
				Set _Iterator = _HashMapTemp.entrySet();

				JSONObject _JsonObjectData = new JSONObject();
				for (@SuppressWarnings("rawtypes")
				Iterator iter = _Iterator.iterator(); iter.hasNext();) {

					Map.Entry entry = (Map.Entry) iter.next();
					String _Key = entry.getKey().toString();
					Object _Object = _HashMapTemp.get(_Key);

					_JsonObjectData.put(entry.getKey().toString(), entry
							.getValue().toString());
				}
				_JsonArray.put(_JsonObjectData);

			}

		} catch (JSONException ex) {
			LogUtil.v("Json", ex.getMessage());
		}

		return _JsonArray;
	}

	/**
	 * Description: 获取简易执法任务连接信息
	 * 
	 * @param _TaskId
	 *            企业代码
	 * @return 返回任务企业连接{JSONArray} JSONArray
	 * @author 兰光 Create at: 2013-08-09下午15:04:55
	 */
	private JSONArray GetEntpriLinkItems(String Taskid) {
		ArrayList<HashMap<String, Object>> _ArrayList = SqliteUtil
				.getInstance().getList(
						"TaskEnpriLink where TaskID='" + Taskid + "'");
		JSONArray _JsonArray = new JSONArray();
		try {
			for (int i = 0; i < _ArrayList.size(); i++) {
				HashMap<String, Object> _HashMapTemp = _ArrayList.get(i);
				Set _Iterator = _HashMapTemp.entrySet();

				JSONObject _JsonObjectData = new JSONObject();
				for (Iterator iter = _Iterator.iterator(); iter.hasNext();) {

					Map.Entry entry = (Map.Entry) iter.next();
					String _Key = entry.getKey().toString();

					if (_Key.equals("isexcute")) {

						_JsonObjectData.put(_Key, "3");// 修改企业状态已上传

						// BYK rwzt
						// _JsonObjectData.put(_Key, "1");// 修改企业状态已上传
					} else {
						_JsonObjectData.put(_Key, entry.getValue().toString());
					}

				}
				_JsonArray.put(_JsonObjectData);
			}
		} catch (JSONException ex) {
			LogUtil.v("Json", ex.getMessage());
		}
		return _JsonArray;
	}

	/**
	 * Description: 获取任务企业连接信息
	 * 
	 * @param _TaskId
	 *            企业代码
	 * @return 返回任务企业连接{JSONArray} JSONArray
	 * @author 兰光 Create at: 2013-08-09下午15:04:55
	 */
	private JSONArray GetEntpriLinkItems(String Taskid, String qyid,
			boolean isComplete) {
		ArrayList<HashMap<String, Object>> _ArrayList = SqliteUtil
				.getInstance().getList(
						"TaskEnpriLink where TaskID='" + Taskid
								+ "' and QYID='" + qyid + "'");
		JSONArray _JsonArray = new JSONArray();
		try {
			for (int i = 0; i < _ArrayList.size(); i++) {
				HashMap<String, Object> _HashMapTemp = _ArrayList.get(i);
				Set _Iterator = _HashMapTemp.entrySet();

				JSONObject _JsonObjectData = new JSONObject();
				for (Iterator iter = _Iterator.iterator(); iter.hasNext();) {

					Map.Entry entry = (Map.Entry) iter.next();
					String _Key = entry.getKey().toString();

					if (_Key.equals("isexcute") || _Key.contains("isexcute")) {
						if (isComplete) {
							// BYK rwzt
							// _JsonObjectData.put(_Key, "1");// 修改企业状态已上传
							_JsonObjectData.put(_Key, "3");// 修改企业状态已上传
						} else {
							_JsonObjectData.put(_Key, "2");// 修改企业状态执行中
						}

					} else {
						_JsonObjectData.put(_Key, entry.getValue().toString());
					}

				}
				_JsonObjectData.put("SurveyUnit", Global.getGlobalInstance().getAreaCode());
				_JsonArray.put(_JsonObjectData);
			}
		} catch (JSONException ex) {
			LogUtil.v("Json", ex.getMessage());
		}
		return _JsonArray;
	}

	/**
	 * Description: 获取与任务相关的所有企业信息
	 * 
	 * @return 返回任务企业连接{JSONArray} JSONArray
	 */
	public JSONArray GetEntpriLinkAllItems(String Taskid) {
		ArrayList<HashMap<String, Object>> _ArrayList = SqliteUtil
				.getInstance().getList(
						"TaskEnpriLink where TaskID='" + Taskid + "'");
		JSONArray _JsonArray = new JSONArray();
		try {
			for (int i = 0; i < _ArrayList.size(); i++) {
				HashMap<String, Object> _HashMapTemp = _ArrayList.get(i);
				Set _Iterator = _HashMapTemp.entrySet();

				JSONObject _JsonObjectData = new JSONObject();
				for (Iterator iter = _Iterator.iterator(); iter.hasNext();) {

					Map.Entry entry = (Map.Entry) iter.next();
					String _Key = entry.getKey().toString();

					if (_Key.equals("isexcute")) {
						_JsonObjectData.put(_Key, "2");// 修改任务状态为执行中

					} else {
						_JsonObjectData.put(_Key, entry.getValue().toString());
					}

				}
				_JsonArray.put(_JsonObjectData);
			}
		} catch (JSONException ex) {
			LogUtil.v("Json", ex.getMessage());
		}
		return _JsonArray;
	}

	/**
	 * Description: 获取勘查笔录信息
	 * 
	 * @param _TaskId
	 * 
	 * @return 返回勘查笔录信息{JSONArray} JSONArray
	 * @author 兰光 Create at: 2013-08-09下午16:17:55
	 */
	private JSONArray GetKCBLItems(String Taskid, String qyid) {
		ArrayList<HashMap<String, Object>> _ArrayList;

		_ArrayList = SqliteUtil.getInstance().getList(
				"T_ZFWS_KCBL where TaskID='" + Taskid + "'and EntCode='" + qyid
						+ /**/"'");
		JSONArray _JsonArray = new JSONArray();
		try {
			for (int i = 0; i < _ArrayList.size(); i++) {
				HashMap<String, Object> _HashMapTemp = _ArrayList.get(i);
				Set _Iterator = _HashMapTemp.entrySet();

				JSONObject _JsonObjectData = new JSONObject();
				for (Iterator iter = _Iterator.iterator(); iter.hasNext();) {

					Map.Entry entry = (Map.Entry) iter.next();
					String _Key = entry.getKey().toString();
					Object _Object = _HashMapTemp.get(_Key);

					_JsonObjectData.put(entry.getKey().toString(), entry
							.getValue().toString());
				}
				_JsonArray.put(_JsonObjectData);
			}
		} catch (JSONException ex) {
			LogUtil.v("Json", ex.getMessage());
		}
		return _JsonArray;
	}

	/**
	 * Description: 获取ExeLawsTemplet多表单信息
	 * 
	 * @param _TaskId
	 * 
	 * @return
	 * @author 兰光 Create at: 2013-08-09下午16:17:55
	 */
	private JSONArray GetExeLawsTempletItems(String Taskid, String qyid) {
		ArrayList<HashMap<String, Object>> _ArrayList;

		_ArrayList = SqliteUtil.getInstance().getList(
				"ExeLawsTemplet where TaskID='" + Taskid + "'and EnterId='"
						+ qyid + /**/"'");
		JSONArray _JsonArray = new JSONArray();
		try {
			for (int i = 0; i < _ArrayList.size(); i++) {
				HashMap<String, Object> _HashMapTemp = _ArrayList.get(i);
				Set _Iterator = _HashMapTemp.entrySet();

				JSONObject _JsonObjectData = new JSONObject();
				for (Iterator iter = _Iterator.iterator(); iter.hasNext();) {

					Map.Entry entry = (Map.Entry) iter.next();
					String _Key = entry.getKey().toString();
					Object _Object = _HashMapTemp.get(_Key);

					_JsonObjectData.put(entry.getKey().toString(), entry
							.getValue().toString());
				}
				_JsonArray.put(_JsonObjectData);
			}
		} catch (JSONException ex) {
			LogUtil.v("Json", ex.getMessage());
		}
		return _JsonArray;
	}

	/**
	 * 获取billid BYK
	 * */
	private String GetExeLawsTempletItems2(String Taskid, String qyid) {
		ArrayList<HashMap<String, Object>> _ArrayList;

		_ArrayList = SqliteUtil.getInstance().getList(
				"ExeLawsTemplet where TaskID='" + Taskid + "'and EnterId='"
						+ qyid + /**/"'");
		JSONArray _JsonArray = new JSONArray();
		String billid = "";
		for (int i = 0; i < _ArrayList.size(); i++) {
			HashMap<String, Object> _HashMapTemp = _ArrayList.get(i);
			Set _Iterator = _HashMapTemp.entrySet();

			JSONObject _JsonObjectData = new JSONObject();
			for (Iterator iter = _Iterator.iterator(); iter.hasNext();) {

				Map.Entry entry = (Map.Entry) iter.next();
				String _Key = entry.getKey().toString();
				Object _Object = _HashMapTemp.get(_Key);
				if (_Key.equals("billid") || _Key.equals("BillId")
						|| _Key.equals("Billid") || _Key.equals("billId")) {
					billid = entry.getValue().toString();
				}
			}
		}
		return billid;
	}

	/**
	 * 获取"接受调查通知书"笔录json
	 * 
	 * @param Taskid
	 * @param qyid
	 * @return
	 */
	private JSONArray GetJSTZSItems(String Taskid, String qyid) {
		ArrayList<HashMap<String, Object>> _ArrayList;

		_ArrayList = SqliteUtil.getInstance().getList(
				"Survey_JSTZS where TaskID='" + Taskid + "'and Entid='" + qyid
						+ "'");
		JSONArray _JsonArray = new JSONArray();
		try {
			for (int i = 0; i < _ArrayList.size(); i++) {
				HashMap<String, Object> _HashMapTemp = _ArrayList.get(i);
				Set _Iterator = _HashMapTemp.entrySet();

				JSONObject _JsonObjectData = new JSONObject();
				for (Iterator iter = _Iterator.iterator(); iter.hasNext();) {

					Map.Entry entry = (Map.Entry) iter.next();
					String _Key = entry.getKey().toString();
					Object _Object = _HashMapTemp.get(_Key);

					_JsonObjectData.put(entry.getKey().toString(), entry
							.getValue().toString());
				}
				_JsonArray.put(_JsonObjectData);
			}
		} catch (JSONException ex) {
			LogUtil.v("Json", ex.getMessage());
		}
		return _JsonArray;
	}

	/**
	 * 获取"现场环境监察记录"笔录json
	 * 
	 * @param Taskid
	 * @param qyid
	 * @return
	 */
	private JSONArray GetXCHJItems(String Taskid, String qyid) {
		ArrayList<HashMap<String, Object>> _ArrayList;

		_ArrayList = SqliteUtil.getInstance().getList(
				"YDZF_SiteEnvironmentMonitorRecord where TaskID='" + Taskid
						+ "'and Entid='" + qyid + "'");
		JSONArray _JsonArray = new JSONArray();
		try {
			for (int i = 0; i < _ArrayList.size(); i++) {
				HashMap<String, Object> _HashMapTemp = _ArrayList.get(i);
				Set _Iterator = _HashMapTemp.entrySet();

				JSONObject _JsonObjectData = new JSONObject();
				for (Iterator iter = _Iterator.iterator(); iter.hasNext();) {

					Map.Entry entry = (Map.Entry) iter.next();
					String _Key = entry.getKey().toString();
					Object _Object = _HashMapTemp.get(_Key);
					// 处理生产状态的问题
					if (entry.getKey().toString().equals("scqk_sczk")
							|| entry.getKey().toString().equals("SCQK_SCZK")) {
						String value = entry.getValue().toString();

						if (value.equals("正常生产")) {
							value = "1";
						} else {
							value = "0";
						}
						_JsonObjectData.put(entry.getKey().toString(), value);
					} else {

						_JsonObjectData.put(entry.getKey().toString(), entry
								.getValue().toString());
					}

				}
				_JsonArray.put(_JsonObjectData);
			}
		} catch (JSONException ex) {
			LogUtil.v("Json", ex.getMessage());
		}
		return _JsonArray;
	}

	/**
	 * 获取询问笔录json
	 * 
	 * @param Taskid
	 * @param qyid
	 * @return
	 */
	private JSONArray GetXWBLItems(String Taskid, String qyid) {
		ArrayList<HashMap<String, Object>> _ArrayList;

		_ArrayList = SqliteUtil.getInstance().getList(
				"T_ZFWS_XWBL where TaskID='" + Taskid + "'and SurveyEntCode='"
						+ qyid + "'");
		JSONArray _JsonArray = new JSONArray();
		try {
			for (int i = 0; i < _ArrayList.size(); i++) {
				HashMap<String, Object> _HashMapTemp = _ArrayList.get(i);
				Set _Iterator = _HashMapTemp.entrySet();

				JSONObject _JsonObjectData = new JSONObject();
				for (Iterator iter = _Iterator.iterator(); iter.hasNext();) {

					Map.Entry entry = (Map.Entry) iter.next();
					String _Key = entry.getKey().toString();
					Object _Object = _HashMapTemp.get(_Key);

					_JsonObjectData.put(entry.getKey().toString(), entry
							.getValue().toString());
				}
				_JsonArray.put(_JsonObjectData);
			}
		} catch (JSONException ex) {
			LogUtil.v("Json", ex.getMessage());
		}
		return _JsonArray;
	}

	/**
	 * 获取简易执法询问笔录json
	 * 
	 * @param Taskid
	 * @param qyid
	 * @return
	 */
	private JSONArray GetXWBLItems(String Taskid) {
		ArrayList<HashMap<String, Object>> _ArrayList;

		_ArrayList = SqliteUtil.getInstance().getList(
				"T_ZFWS_XWBL where TaskID='" + Taskid + "'");
		JSONArray _JsonArray = new JSONArray();
		try {
			for (int i = 0; i < _ArrayList.size(); i++) {
				HashMap<String, Object> _HashMapTemp = _ArrayList.get(i);
				Set _Iterator = _HashMapTemp.entrySet();

				JSONObject _JsonObjectData = new JSONObject();
				for (Iterator iter = _Iterator.iterator(); iter.hasNext();) {

					Map.Entry entry = (Map.Entry) iter.next();
					String _Key = entry.getKey().toString();
					Object _Object = _HashMapTemp.get(_Key);

					_JsonObjectData.put(entry.getKey().toString(), entry
							.getValue().toString());
				}
				_JsonArray.put(_JsonObjectData);
			}
		} catch (JSONException ex) {
			LogUtil.v("Json", ex.getMessage());
		}
		return _JsonArray;
	}

	/**
	 * 
	 * @param Taskid
	 * @param qyid
	 * @return
	 */
	private JSONArray GetWDItems(String Taskid, String qyid) {
		ArrayList<HashMap<String, Object>> _ArrayList = new ArrayList<HashMap<String, Object>>();

		_ArrayList = SqliteUtil
				.getInstance()
				.queryBySqlReturnArrayListHashMap(
						"select distinct taskid,entid,result,wtnr,billid from T_ZFWS_XWJLWD where taskid = '"
								+ Taskid + "' and entid = '" + qyid + "'");

		// .getList(
		// "select di T_ZFWS_XWJLWD where TaskID='" + Taskid + "'and EntID='" +
		// qyid
		// + "'");
		JSONArray _JsonArray = new JSONArray();
		try {
			for (int i = 0; i < _ArrayList.size(); i++) {
				HashMap<String, Object> _HashMapTemp = _ArrayList.get(i);
				Set _Iterator = _HashMapTemp.entrySet();

				JSONObject _JsonObjectData = new JSONObject();
				for (Iterator iter = _Iterator.iterator(); iter.hasNext();) {

					Map.Entry entry = (Map.Entry) iter.next();
					String _Key = entry.getKey().toString();
					Object _Object = _HashMapTemp.get(_Key);

					_JsonObjectData.put(entry.getKey().toString(), entry
							.getValue().toString());
				}
				_JsonArray.put(_JsonObjectData);
			}
		} catch (JSONException ex) {
			LogUtil.v("Json", ex.getMessage());
		}
		return _JsonArray;
	}

	/**
	 * 获得简易执法询问笔录json串儿
	 * 
	 * @param Taskid
	 * @param
	 * @return
	 */
	private JSONArray GetWDItems(String Taskid) {
		ArrayList<HashMap<String, Object>> _ArrayList = new ArrayList<HashMap<String, Object>>();

		_ArrayList = SqliteUtil.getInstance().queryBySqlReturnArrayListHashMap(
				"select distinct taskid,result,wtnr,billid from T_ZFWS_XWJLWD where taskid = '"
						+ Taskid + "'");

		JSONArray _JsonArray = new JSONArray();
		try {
			for (int i = 0; i < _ArrayList.size(); i++) {
				HashMap<String, Object> _HashMapTemp = _ArrayList.get(i);
				Set _Iterator = _HashMapTemp.entrySet();

				JSONObject _JsonObjectData = new JSONObject();
				for (Iterator iter = _Iterator.iterator(); iter.hasNext();) {

					Map.Entry entry = (Map.Entry) iter.next();
					String _Key = entry.getKey().toString();
					Object _Object = _HashMapTemp.get(_Key);

					_JsonObjectData.put(entry.getKey().toString(), entry
							.getValue().toString());
				}
				_JsonArray.put(_JsonObjectData);
			}
		} catch (JSONException ex) {
			LogUtil.v("Json", ex.getMessage());
		}
		return _JsonArray;
	}

	private final String TAG = "NetworkService";

	/**
	 * Description: 获取当前任务所有上传文件
	 * 
	 * @param _Path
	 *            文件路径
	 * @param _TaskId
	 *            任务编号
	 * @param _Context
	 *            上下文
	 * @return 返回该路径下所有文件 ArrayList<File>
	 * @author 柳思远 Create at: 2012-12-4 上午11:46:48
	 */
	public ArrayList<TaskFile> getAllUploadFile(String rwbh, String qyid,
			String filepath) {
		ArrayList<TaskFile> _ListFile = new ArrayList<TaskFile>();
		HashMap<String, Object> condition = new HashMap<String, Object>();
		condition.put("fk_id", rwbh + "_" + qyid);
		ArrayList<HashMap<String, Object>> fileLists = SqliteUtil.getInstance()
				.getList(" * ", condition, "T_Attachment");
		if (fileLists != null || fileLists.size() > 0) {

			for (HashMap<String, Object> map : fileLists) {
				TaskFile taskFile = new TaskFile();
				String absolutePath = filepath + map.get("filepath").toString();
				taskFile.setGuid(map.get("guid").toString());
				taskFile.setFileName(map.get("filename").toString());
				taskFile.setFilePath(map.get("filepath").toString());
				taskFile.setAbsolutePath(absolutePath);
				taskFile.setUnitId(map.get("fk_unit").toString());
				taskFile.setExtension(map.get("extension").toString());
				_ListFile.add(taskFile);
			}
		}

		return _ListFile;
	}

	@Override
	public String getQueryTitleText() {
		return QueryTitleText;
	}

	@Override
	public HashMap<String, Object> getQuery() {
		return null;
	}

	/**
	 * 获取查询样式
	 */
	@Override
	public ArrayList<HashMap<String, Object>> getStyleQuery(Context context) {
		ArrayList<HashMap<String, Object>> styleDetailList = null;
		try {
			styleDetailList = XmlHelper.getStyleByName(QueryStyleName,
					getStyleQueryInputStream(context));
		} catch (Exception e) {
			ExceptionManager.WriteCaughtEXP(e, "RWXX");
			e.printStackTrace();
		}
		return styleDetailList;
	}

	@Override
	public void setFilter(HashMap<String, Object> filter) {
		Filter = filter;
	}

	@Override
	public List<HashMap<String, Object>> getSpinner(String AdapterFileName,
			String querycondition) {
		List<HashMap<String, Object>> spinnerdata = null;
		spinnerdata = BaseClass.DBHelper.getList(AdapterFileName,
				querycondition);
		return spinnerdata;
	}

	/**
	 * Description: 返回任务信息的其他信息
	 * 
	 * @param rwid
	 *            任务的id
	 * @param lm
	 *            要返回的某个任务字段值
	 * @return 返回该条任务的该字段的任务信息 String
	 * @author 柳思远 Create at: 2012-12-4 上午11:48:21 wanglg 修改10月2号
	 */
	public String getlz(String rwid, String lm) {
		String result = "";
		HashMap<String, Object> primaryKey = new HashMap<String, Object>();
		primaryKey.put("key", "guid");
		primaryKey.put("keyValue", rwid);
		ArrayList<HashMap<String, Object>> data = BaseClass.DBHelper.getList(
				lm, primaryKey, "T_YDZF_RWXX");
		if (data != null && data.size() > 0) {
			result = data.get(0).get(lm).toString();
		}
		return result;

	}

	/**
	 * Description: 通过用户编号查询该用户的某一状态任务， 待执行任务条数
	 * 
	 * @param userID
	 *            用户编号
	 * @param status
	 *            任务状态
	 * @return 任务信息结合 ArrayList<HashMap<String,Object>>
	 * @author 柳思远 Create at: 2012-12-4 上午11:49:42
	 */
	public int getTaskNumByUserIDandStatus(String userID, String status) {
		String sql = "select count(*) from T_YDZF_RWXX as a left join T_YDZF_RWXX_USER b on a.[RWBH]=b.RWXXBH  "
				+ "where a.[RWZT]='"
				+ status
				+ "' and b.[UserID]='"
				+ userID
				+ "'";

		int count = -1;
		Cursor c = null;

		try {
			c = SqliteUtil.getInstance().queryBySql(sql);

			if (c.moveToNext()) {
				count = c.getInt(0);

			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (c != null) {
				c.close();
			}
		}
		return count;

	}

	/**
	 * Description: 获取现场执法的任务
	 * 
	 * @param userID
	 *            用户编号
	 * @param status
	 *            任务类型
	 * @return 返回该用户的现场执法的任务信息 ArrayList<HashMap<String,Object>>
	 * @author 柳思远 Create at: 2012-12-4 上午11:50:19
	 */
	public ArrayList<HashMap<String, Object>> getSiteTaskByUserid(
			String userID, String status) {
		ArrayList<HashMap<String, Object>> siteTaskList = null;
		// String sql = "SELECT * FROM T_YDZF_RWXX WHERE rwlx='" + status +
		// "' AND FBR='" + userID + "' ORDER BY FBSJ DESC ";
		String sql = "SELECT Guid,RWBH,RWZT,RWMC,RWLX,FBSJ FROM T_YDZF_RWXX WHERE rwly='"
				+ status + "' AND FBR='" + userID + "' ORDER BY FBSJ DESC ";

		siteTaskList = SqliteUtil.getInstance()
				.queryBySqlReturnArrayListHashMap(sql);

		return siteTaskList;
	}

	/**
	 * Description: 查询当前任务状态
	 * 
	 * @param rwID
	 *            任务编号
	 * @return 该条任务的状态 String
	 * @author 柳思远 Create at: 2012-12-4 上午11:51:49
	 */
	public String queryTaskStatus(String RWBH) {
		String status = "";
		ArrayList<HashMap<String, Object>> data = new ArrayList<HashMap<String, Object>>();
		String sql = "select rwzt from T_YDZF_RWXX where rwbh = '" + RWBH + "'";
		data = SqliteUtil.getInstance().queryBySqlReturnArrayListHashMap(sql);

		if (data != null && data.size() > 0) {
			status = (String) data.get(0).get("rwzt");
		}
		return status;
	}

	/**
	 * 查询当前任务来源
	 * 
	 * @param RWBH
	 * @return
	 */
	public String queryTaskSource(String RWBH) {
		String source = "";
		ArrayList<HashMap<String, Object>> data = new ArrayList<HashMap<String, Object>>();
		String sql = "select rwly from T_YDZF_RWXX where rwbh = '" + RWBH + "'";
		data = SqliteUtil.getInstance().queryBySqlReturnArrayListHashMap(sql);

		if (data != null && data.size() > 0) {
			source = (String) data.get(0).get("rwly");
		}
		return source;
	}

	/**
	 * 根据任务编号和企业id获得该任务下的某企业的执行状态
	 * 
	 * @param RWBH
	 * @param qyid
	 * @return 0，待执行 1，已完成 2，执行中
	 */
	public String queryTask_Qyid_Status(String RWBH, String qyid) {
		String status = "";
		ArrayList<HashMap<String, Object>> data = new ArrayList<HashMap<String, Object>>();
		String sql = "select IsExcute from TaskEnpriLink where " + "taskid = '"
				+ RWBH + "' and qyid='" + qyid + "'";
		try {
			data = SQLiteDataProvider.getInstance()
					.queryBySqlReturnArrayListHashMap(sql);
		} catch (Exception e) {
			Log.v(TAG, sql);
			e.printStackTrace();
		}
		if (data != null && data.size() > 0) {
			status = (String) data.get(0).get("isexcute");
		}
		return status;
	}

	public void changeTask_QyidState(String RWBH, String qyid, String isexcute) {

		String sql = "update " + "[TaskEnpriLink] set [isexcute]='" + isexcute
				+ "' where taskid='" + RWBH + "'" + " and qyid='" + qyid + "'";

		SqliteUtil.getInstance().execute(sql);
	}

	public String queryEntid(String entname) {
		String status = "";
		ArrayList<HashMap<String, Object>> data = new ArrayList<HashMap<String, Object>>();
		String sql = "select Guid from T_WRY_QYJBXX where QYMC = '" + entname
				+ "'";
		data = SqliteUtil.getInstance().queryBySqlReturnArrayListHashMap(sql);

		if (data != null && data.size() > 0) {
			status = (String) data.get(0).get("guid");
		}
		return status;

	}

	public String queryEntidbyqydm(String qydm) {
		String status = "";
		ArrayList<HashMap<String, Object>> data = new ArrayList<HashMap<String, Object>>();
		String sql = "select Guid from T_WRY_QYJBXX where QYDM = '" + qydm
				+ "'";
		data = SqliteUtil.getInstance().queryBySqlReturnArrayListHashMap(sql);

		if (data != null && data.size() > 0) {
			status = (String) data.get(0).get("guid");
		}
		return status;

	}

	public String queryEntdmbyqyid(String qyid) {
		String status = "";
		ArrayList<HashMap<String, Object>> data = new ArrayList<HashMap<String, Object>>();
		String sql = "select qydm from T_WRY_QYJBXX where guid = '" + qyid
				+ "'";
		data = SqliteUtil.getInstance().queryBySqlReturnArrayListHashMap(sql);

		if (data != null && data.size() > 0) {
			status = (String) data.get(0).get("qydm");
		}
		return status;

	}

	/**
	 * Description: 保存新建任务
	 * 
	 * @param rwbh
	 *            任务编号
	 * @param issave
	 *            true：待下发，false：带执行
	 * @param guid
	 *            任务的id
	 * @param cont
	 *            1：有任务 编号，其他：没有任务 编号
	 * @param bz
	 *            备注
	 * @param qymc
	 *            企业名称
	 * @param rwmc
	 *            任务名称
	 * @param ssks
	 *            所属科室
	 * @param useridStr
	 *            和任务关联的的用户id，以逗号分隔
	 * @param tid
	 *            模版id
	 * @param info
	 *            任务状态：save：一般任务，xiafa：待下发,xczf：现场执法任务
	 * @param isreply
	 *            是否回复
	 * @return 返回任务 编号：成功，返回“”：失败 String
	 * @author 柳思远 Create at: 2012-12-4 上午11:52:20
	 */

	public String insertRWXX(String rwbh, String bjqx, boolean issave,
			String guid, int cont, String bz, String qyidStr, String rwmc,
			String ssks, String rwly, String useridStr, String tid,
			String info, int isreply, String dwld, String rwlx) {
		String rwbhStr = "";
		if (cont == 1 && !"".equals(rwbh)) {
			rwbhStr = rwbh;
		} else {
			rwbhStr = returnRWBH();
		}

		boolean i = insertDate(issave, cont, qyidStr, bz, rwmc, bjqx, rwbhStr,
				ssks, rwly, tid, guid, info, isreply, rwlx);// 插入rwxx表
		boolean j = insertUserRwxx(cont, useridStr, bz, rwbhStr);// 插入任务用户关系表
		boolean m = true;
		/*
		 * if (dwld != null && !dwld.trim().equals("")) { m = insertRWLC(cont,
		 * rwbhStr, dwld);// 插入rwlc任务流程表 }
		 */

		// UUID uuid = UUID.randomUUID();
		// String s = UUID.randomUUID().toString();
		// boolean k = insertEnpriLinkRwxx(cont, qyidStr, bz, rwbhStr);//
		// 插入任务企业关系表
		boolean k = true;// 插入任务企业关系表

		if (qyidStr != null && !qyidStr.equals("")) {
			k = insertEnpriLinkRwxx(cont, qyidStr, bz, rwbhStr);// 插入任务企业关系表
			insertTaskSpecialItem(cont, rwbhStr, qyidStr, tid);
		}

		if (i && j && k && m) {
			Global.getGlobalInstance().setTaskStateChange(true);// 新增一条任务，刷新任务列表
			return rwbhStr;

		} else
			return "";
	}

	/**
	 * Description: 生成任务编号
	 * 
	 * @return 返回任务编号 String
	 * @author 柳思远 Create at: 2012-12-4 上午11:58:05
	 */
	public String returnRWBH() {
		SimpleDateFormat formatdate = new SimpleDateFormat("yyyyMMddHHmmssSSSS");
		/** 生成规则 */
		String time = "T" + formatdate.format(new Date());
		return time;
	}

	/**
	 * Description: 添加任务
	 * 
	 * @param issave
	 *            true：待下发，false：带执行
	 * @param cont
	 *            1：代表有该条任务，需要删除再插入
	 * @param qymc
	 *            企业名称
	 * @param bz
	 *            备注
	 * @param rwmc
	 *            任务名称
	 * @param rwbhStr
	 *            任务编号
	 * @param ssks
	 *            所属科室
	 * @param tid
	 *            模版id
	 * @param guid
	 *            任务id
	 * @param info
	 *            任务状态：save：一般任务，xiafa：待下发,xczf：现场执法任务
	 * @param isreply
	 *            是否回复
	 * @return true:插入成功返回,false:插入失败 boolean
	 * @author 柳思远 Create at: 2012-12-4 上午11:58:55
	 */
	public boolean insertDate(boolean issave, int cont, String qyidStr,
			String bz, String rwmc, String bjqx, String rwbhStr, String ssks,
			String rwly, String tid, String guid, String info, int isreply,
			String rwlx) {
		try {
			ArrayList<ContentValues> values = new ArrayList<ContentValues>();
			ContentValues content = returnRWXX(issave, qyidStr, rwmc, bjqx, bz,
					rwbhStr, ssks, rwly, tid, guid, info, isreply, rwlx);
			values.add(content);
			long i;
			if (cont == 1) {
				String sqldel = "delete from T_YDZF_RWXX where guid='" + guid
						+ "'";
				SqliteUtil.getInstance().execute(sqldel);
				i = SqliteUtil.getInstance().insert(values, "T_YDZF_RWXX");
				// i = su.update( "T_YDZF_RWXX",content,"guid=?",new
				// String[]{guid});
			} else
				i = SqliteUtil.getInstance().insert(values, "T_YDZF_RWXX");
			if (i >= 1) {
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * Description: 往T_YDZF_RWXX_USER表插入数据
	 * 
	 * @param cont
	 *            1:代表有数据需要删除再插入
	 * @param useridStr
	 *            用户的id，以逗号分隔
	 * @param bz
	 *            备注
	 * @param rwbhStr
	 *            任务编号
	 * @return true:成功，false：失败 boolean
	 * @author 柳思远 Create at: 2012-12-4 下午12:01:16
	 */
	public boolean insertUserRwxx(int cont, String useridStr, String bz,
			String rwbhStr) {
		ArrayList<ContentValues> values = returnRwxxUser(useridStr, rwbhStr, bz);
		long i = 0;
		try {
			if (cont == 1) {
				// for(ContentValues value:values){
				// su.update( "T_YDZF_RWXX_USER",value,"rwxxbh=?",new
				// String[]{value.get("rwxxbh").toString()});
				// }
				String sqldel = "delete from T_YDZF_RWXX_USER where rwxxbh='"
						+ rwbhStr + "'";
				SqliteUtil.getInstance().execute(sqldel);
				i = SqliteUtil.getInstance().insert(values, "T_YDZF_RWXX_USER");
			} else {
				i = SqliteUtil.getInstance().insert(values, "T_YDZF_RWXX_USER");
			}
			if (i >= 1) {
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * Description: 往TaskEnpriLink表插入数据
	 * 
	 * @param cont
	 *            1:代表有数据需要删除再插入
	 * @param qyidStr
	 *            企业的id，以逗号分隔
	 * @param bz
	 *            备注
	 * @param rwbhStr
	 *            任务编号
	 * @return true:成功，false：失败 boolean
	 * @author 赵瑞青 Create at: 2013-8-10 上午10:01:16
	 */
	public boolean insertEnpriLinkRwxx(int cont, String qyidStr, String bz,
			String rwbhStr) {
		ArrayList<ContentValues> values = returnRwxxEnpri(qyidStr, rwbhStr, bz);
		long i = 0;
		try {
			if (cont == 1) {
				// for(ContentValues value:values){
				// su.update( "Enpri",value,"rwxxbh=?",new
				// String[]{value.get("rwxxbh").toString()});
				// }
				String sqldel = "delete from TaskEnpriLink where taskid='"
						+ rwbhStr + "'";
				SqliteUtil.getInstance().execute(sqldel);
				i = SqliteUtil.getInstance().insert(values, "TaskEnpriLink");
			} else {
				i = SqliteUtil.getInstance().insert(values, "TaskEnpriLink");
			}
			if (i >= 1) {
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * Description: 保存信息专项YDZF_TaskSpecialItem
	 * 
	 * @param cont
	 *            1:代表有数据需要删除再插入
	 * @param rwbhStr
	 *            任务编号
	 * @param qyid
	 *            企业id
	 * @param tid
	 *            模版id
	 * @returntrue:成功，false：失败 boolean
	 * @author 柳思远 Create at: 2012-12-4 下午01:20:35
	 */
	private boolean insertTaskSpecialItem(int cont, String rwbhStr,
			String qyidStr, String tid) {
		try {
			String sqldel = "delete from YDZF_TaskSpecialItem where TaskID='"
					+ rwbhStr + "'";
			SqliteUtil.getInstance().execute(sqldel);
			if (qyidStr.length() > 1) {
				for (String qyid : qyidStr.split(",")) {
					String sql = "insert into YDZF_TaskSpecialItem (TaskID,EnterID,IndustryID,SpecialItemID,SpecialItemResult,Remark,SurveyTime,UpdateTime) select '"
							+ rwbhStr
							+ "','"
							+ qyid
							+ "','"
							+ tid
							+ "',SpecialItemID,NULL,NULL,NULL,'"
							+ Global.getGlobalInstance().getDate()
							+ "' from YDZF_TemplateSpacialItem where TID = '"
							+ tid + "'";
					SqliteUtil.getInstance().execute(sql);
				}
			}
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * Description: 返回任务新建信息
	 * 
	 * @param issave
	 *            true：待下发，false：带执行
	 * @param qymc
	 *            企业名称
	 * @param rwmc
	 *            任务名称
	 * @param bz
	 *            备注
	 * @param rwbhStr
	 *            任务编号
	 * @param ssks
	 *            所属科室
	 * @param tid
	 *            模版id
	 * @param guid
	 *            任务id
	 * @param info
	 *            任务类型：save：一般任务，xiafa：待下发,xczf：现场执法任务
	 * @param isreply
	 *            是否回复
	 * @return 将信息转换为ContentValues格式 ContentValues
	 * @author 柳思远 Create at: 2012-12-4 下午01:22:26
	 */
	private ContentValues returnRWXX(boolean issave, String qyidStr,
			String rwmc, String bjqx, String bz, String rwbhStr, String ssks,
			String rwly, String tid, String guid, String info, int isreply,
			String rwlx) {
		ContentValues content = new ContentValues();
		// conditions.put("qymc", qymc);
		String time = Global.getGlobalInstance().getDate();
		// ArrayList<HashMap<String, Object>> list = SqliteUtil
		// .getInstance().getList("qydm", conditions, "T_WRY_QYJBXX");
		content.put("guid", guid);
		content.put("rwmc", rwmc);
		content.put("bjqx", bjqx);
		content.put("rwbh", rwbhStr);
		content.put("qydm", qyidStr);
		content.put("ssks", ssks);
		content.put("fbr", Global.getGlobalInstance().getUserid());
		content.put("fbsj", time);
		content.put("updatetime", time);
		content.put("rwlY", rwly);
		content.put("bz", bz);
		content.put("isreply", isreply);
		content.put("rwlx", rwlx);

		if (issave) {
			content.put("rwzt", RWZT_WAIT_DISPATCH);
		} else {
			content.put("rwzt", RWZT_WATE_EXECUTION);
		}
		content.put("SpecialTemplateID", tid);
		// if (list.size() >= 1) { qyid = list.get(0).get("qydm").toString();
		// content.put("qydm", qyid); }

		return content;
	}

	/**
	 * Description: 循环遍历返回的userid字符串，以逗号分隔，依次插到T_YDZF_RWXX_USER
	 * 
	 * @param useridStr
	 *            以逗号分隔以逗号分隔的用户id，
	 * @param rwbhStr
	 *            任务编号
	 * @param bz
	 *            备注
	 * @return 返回任务和用户的关系数据：rwxxbh，userid，bz，updatetime ArrayList<ContentValues>
	 * @author 柳思远 Create at: 2012-12-4 下午01:29:43
	 */
	private ArrayList<ContentValues> returnRwxxUser(String useridStr,
			String rwbhStr, String bz) {
		ArrayList<ContentValues> list = new ArrayList<ContentValues>();
		ContentValues content;

		for (String id : useridStr.split(",")) {
			content = new ContentValues();
			content.put("rwxxbh", rwbhStr);
			content.put("userid", id);
			content.put("bz", bz);
			content.put("updatetime", Global.getGlobalInstance().getDate());
			list.add(content);
		}

		return list;
	}

	/**
	 * Description: 循环遍历返回的qyidStr字符串，以逗号分隔，依次插到TaskEnpriLink
	 * 
	 * @param qyidStr
	 *            以逗号分隔企业id，
	 * @param rwbhStr
	 *            任务编号
	 * @param bz
	 *            备注
	 * @return 返回任务和企业的关系数据：rwxxbh，qyidStr，bz，updatetime
	 *         ArrayList<ContentValues>
	 * @author 赵瑞青 Create at: 2013-8-10 上午10:10:43
	 */
	private ArrayList<ContentValues> returnRwxxEnpri(String qyidStr,
			String rwbhStr, String bz) {
		ArrayList<ContentValues> list = new ArrayList<ContentValues>();
		ContentValues content;
		// String guid = UUID.randomUUID().toString();
		if (qyidStr.length() > 1) {
			for (String id : qyidStr.split(",")) {
				content = new ContentValues();
				content.put("guid", UUID.randomUUID().toString());
				content.put("taskid", rwbhStr);
				content.put("qyid", id);
				content.put("updatetime", Global.getGlobalInstance().getDate());
				list.add(content);
			}
		}
		return list;
	}

	/**
	 * 更改任务状态
	 * 
	 * @param rwbh
	 * @param rwzt
	 * @return
	 */
	public boolean changeTaskState(String rwbh, String rwzt) {
		ContentValues cv = new ContentValues();
		cv.put("rwzt", rwzt);
		String[] whereArgs = { rwbh };
		return SqliteUtil.getInstance().updateTable("T_YDZF_RWXX", cv,
				"rwbh=?", whereArgs);
	}

	/**
	 * 更改任务关联表的状态
	 * 
	 * @param rwbh
	 * @param qyid
	 * @param newState
	 * @return
	 */
	public Boolean changEnpriLinkState(String rwbh, String qyid, String newState) {
		Boolean result = false;

		String sql = "UPDATE TaskEnpriLink set isexcute='" + newState
				+ "' where " + "TaskID='" + rwbh + "' and QYID='" + qyid + "' ";
		result = SqliteUtil.getInstance().ExecSQL(sql);
		return result;

	}

	/**
	 * 更改任务关联表的状态
	 * 
	 * @param rwbh
	 * @param qyid
	 * @param newState
	 * @return
	 */
	public Boolean changEnpriLinkAllState(String rwbh, String newState) {
		Boolean result = false;

		String sql = "UPDATE TaskEnpriLink set isexcute='" + newState
				+ "' where " + "TaskID='" + rwbh + "'";
		result = SqliteUtil.getInstance().ExecSQL(sql);
		return result;

	}

	/**
	 * Description: 通过任务编号，查询任务id
	 * 
	 * @param rwbh
	 *            任务编号
	 * @return 任务id字符串 String
	 * @author 柳思远 Create at: 2012-12-4 下午01:32:53
	 */
	public String getTaskStatus(String rwbh) {
		String statusSql = "select guid from T_YDZF_RWXX where RWBH = '" + rwbh
				+ "'";
		ArrayList<HashMap<String, Object>> data = null;
		data = SqliteUtil.getInstance().queryBySqlReturnArrayListHashMap(
				statusSql);

		if (data != null && data.size() > 0) {
			return (String) data.get(0).get("guid");
		} else {
			return "";
		}

	}

	/**
	 * Description: 通过任务编号、企业ID，查询执行情况
	 * 
	 * @param rwbh
	 *            任务编号 qyid 企业ID
	 * @return isExcute字符串 String
	 * 
	 */
	public String getTaskStatusFromTaskEnpriLink(String rwbh, String qyid) {
		String statusSql = "select IsExcute from TaskEnpriLink where TaskID = '"
				+ rwbh + "' and QYID = '" + qyid + "' ";
		ArrayList<HashMap<String, Object>> data = null;
		data = SqliteUtil.getInstance().queryBySqlReturnArrayListHashMap(
				statusSql);

		if (data != null && data.size() > 0) {
			return (String) data.get(0).get("isexcute");
		} else {
			return "";
		}

	}

	/**
	 * Description: 通过任务编号，查询任务状态
	 * 
	 * @param taskID
	 *            任务编号
	 * @return 任务状态字符串 String
	 * @author 柳思远 Create at: 2012-12-4 下午01:32:53
	 */
	public String getTaskid(String rwbh) {
		String statusSql = "select rwzt from T_YDZF_RWXX where RWBH = '" + rwbh
				+ "'";
		ArrayList<HashMap<String, Object>> data = null;
		data = SqliteUtil.getInstance().queryBySqlReturnArrayListHashMap(
				statusSql);

		if (data != null && data.size() > 0) {
			return (String) data.get(0).get("rwzt");
		} else {
			return "";
		}

	}

	/**
	 * Description: 通过企业id，查询该企业任务状态
	 * 
	 * @param rwbh
	 *            任务编号
	 * @param qyid
	 *            企业id
	 * @return 企业任务状态字符串 String
	 * @author 王红娟 Create at: 2013-8-12 下午9:15:53
	 */
	public String getqyStatus(String qyid, String rwbh) {
		String statusSql = "select isexcute,qyid from TaskEnpriLink where RWBH = '"
				+ rwbh + "' and qyid ='" + qyid + "'";
		ArrayList<HashMap<String, Object>> data = null;
		data = SqliteUtil.getInstance().queryBySqlReturnArrayListHashMap(
				statusSql);

		if (data != null && data.size() > 0) {
			return (String) data.get(0).get("isexcute");
		} else {
			return "";
		}

	}

	/**
	 * 通过guid查询任务编号
	 * 
	 * @param guid
	 * @return 任务编号
	 */
	public String getRWBH(String guid) {
		String statusSql = "select rwbh from T_YDZF_RWXX where guid='" + guid
				+ "'";
		ArrayList<HashMap<String, Object>> data = null;
		data = SqliteUtil.getInstance().queryBySqlReturnArrayListHashMap(
				statusSql);

		if (data != null && data.size() > 0) {
			return (String) data.get(0).get("rwbh");
		} else {
			return "";
		}

	}

	/**
	 * Description: 判断任务是否为自己的任务或者下属的任务
	 * 
	 * @param rwbh
	 *            任务编号
	 * @return ture：是，false：不是 boolean
	 * @author 柳思远 Create at: 2012-12-4 下午01:33:59
	 */
	public boolean JudgeUserName(String rwbh) {

		// ArrayList<HashMap<String, Object>> list =
		// SqliteUtil.getInstance().queryBySqlReturnArrayListHashMap(
		// "select a.[LoginUserId],a.[AuditUserId] from YDZF_RWLC a where a.[Tid] = '"
		// + rwbh
		// +
		// "' and a.[TaskAction] like '%执行%' and a.[NodeId] = (select max(NodeId) from YDZF_RWLC where Tid = '"
		// + rwbh + "');");
		// if (list.size() < 1) {
		// return false;
		// }
		// String uid = Global.getGlobalInstance().getUserid();
		//
		// Set<Map.Entry<String, Object>> set = list.get(0).entrySet();
		// Iterator<Map.Entry<String, Object>> it = set.iterator();
		// while (it.hasNext()) {
		// Map.Entry<String, Object> m = it.next();
		// m.getValue().equals(uid);
		// return true;
		// }
		// return false;

		HashMap<String, Object> primaryKey = new HashMap<String, Object>();
		primaryKey.put("rwxxbh", rwbh);
		ArrayList<HashMap<String, Object>> rwuser = BaseClass.DBHelper.getList(
				"userid", primaryKey, "T_YDZF_RWXX_USER");
		String uid = Global.getGlobalInstance().getUserid();
		for (HashMap<String, Object> map : rwuser) {
			String userid = (String) map.get("userid");
			// String fbuserid = (String) map.get("fbr");
			if (uid.equals(userid)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Description: 根据任务编号查询任务执行人
	 */
	public String getUserNameByrwbh(String rwbh) {
		HashMap<String, Object> primaryKey = new HashMap<String, Object>();
		primaryKey.put("rwxxbh", rwbh);
		ArrayList<HashMap<String, Object>> rwuser = BaseClass.DBHelper.getList(
				"userid", primaryKey, "T_YDZF_RWXX_USER");
		String username = "";
		for (HashMap<String, Object> map : rwuser) {
			String userid = (String) map.get("userid");
			HashMap<String, Object> primaryKey1 = new HashMap<String, Object>();
			primaryKey1.put("userid", userid);
			ArrayList<HashMap<String, Object>> users = BaseClass.DBHelper
					.getList("u_realname", primaryKey1, "PC_Users");
			for (HashMap<String, Object> map1 : users) {
				username = (String) map1.get("u_realname");
			}
		}
		return username;
	}

	/**
	 * Description: 模版的Spinner 现场执法、任务添加、任务完善 三处使用
	 * 
	 * @return 返回封装好的spinner数据 List<SpinnerItem>
	 * @author 柳思远 Create at: 2012-12-4 下午01:34:53
	 */
	public List<SpinnerItem> getSpinnerItem(String qymc) {
		dataList = new ArrayList<HashMap<String, Object>>();
		HashMap<String, Object> fliterHashMap = new HashMap<String, Object>();
		fliterHashMap.put("status", "1");

		SqliteUtil su = SqliteUtil.getInstance();
		ArrayList<HashMap<String, Object>> result = su
				.queryBySqlReturnArrayListHashMap("select Guid,QYDM,HYLB from t_wry_qyjbxx where qymc='"
						+ qymc + "'");

		MBXX mbxx = new MBXX();
		String filterValue = mbxx.getFilterValue(qymc);
		fliterHashMap.put("tid", filterValue);
		dataList = mbxx.getDataList(fliterHashMap);

		List<SpinnerItem> mbList = new ArrayList<SpinnerItem>();
		SpinnerItem item = null;
		int i = 1;
		for (Map<String, Object> map : dataList) {
			String code = map.get("tid").toString();
			String name = map.get("tname").toString();
			item = new SpinnerItem(code, name, i);
			mbList.add(item);
			i++;
		}
		return mbList;
	}

	public List<SpinnerItem> getSpinnerItem() {
		dataList = new ArrayList<HashMap<String, Object>>();
		HashMap<String, Object> fliterHashMap = new HashMap<String, Object>();
		fliterHashMap.put("status", "1");
		MBXX mbxx = new MBXX();
		dataList = mbxx.getDataList(fliterHashMap);

		List<SpinnerItem> mbList = new ArrayList<SpinnerItem>();
		SpinnerItem item = null;
		int i = 0;
		for (Map<String, Object> map : dataList) {
			String code = map.get("tid").toString();
			String name = map.get("tname").toString();
			item = new SpinnerItem(code, name, i);
			mbList.add(item);
			i++;
		}
		return mbList;
	}

	public ArrayList<HashMap<String, Object>> getQdjcListViewItem() {
		dataList = new ArrayList<HashMap<String, Object>>();
		HashMap<String, Object> fliterHashMap = new HashMap<String, Object>();
		fliterHashMap.put("status", "1");
		MBXX mbxx = new MBXX();
		dataList = mbxx.getDataList(fliterHashMap);
		return dataList;
	}

	/** 任务类型 **/
	public List<SpinnerItem> gettaskTypeSpinnerItem() {
		List<SpinnerItem> typeList = new ArrayList<SpinnerItem>();

		ArrayList<HashMap<String, Object>> type = SqliteUtil.getInstance()
				.queryBySqlReturnArrayListHashMap("select * from T_YDZF_RWLX");

		SpinnerItem item = null;
		int i = 0;
		for (Map<String, Object> map : type) {
			String code = map.get("code").toString();
			String name = map.get("name").toString();
			item = new SpinnerItem(code, name, i);
			typeList.add(item);
			i++;
		}

		return typeList;
	}

	/** 任务类型EditText **/
	public List<String> gettaskTypeEditText() {
		List<String> typeList = new ArrayList<String>();

		ArrayList<HashMap<String, Object>> type = SqliteUtil.getInstance()
				.queryBySqlReturnArrayListHashMap("select * from T_YDZF_RWLX");

		for (Map<String, Object> map : type) {
			String code = map.get("code").toString();
			String name = map.get("name").toString();

			typeList.add(name);

		}

		return typeList;
	}
	
	/** 任务类型EditText **/
	public ArrayList<HashMap<String, Object>> gettaskTypeEditText2() {

		ArrayList<HashMap<String, Object>> type = SqliteUtil.getInstance()
				.queryBySqlReturnArrayListHashMap("select * from T_YDZF_RWLX");

		

		return type;
	}

	/** 任务来源 **/
	public List<SpinnerItem> gettaskSourceSpinnerItem() {
		List<SpinnerItem> typeList = new ArrayList<SpinnerItem>();

		ArrayList<HashMap<String, Object>> type = SqliteUtil.getInstance()
				.queryBySqlReturnArrayListHashMap("select * from T_YDZF_RWLY");

		SpinnerItem item = null;
		int i = 0;
		for (Map<String, Object> map : type) {
			String code = map.get("code").toString();
			String name = map.get("name").toString();
			item = new SpinnerItem(code, name, i);
			typeList.add(item);
			i++;
		}

		return typeList;
	}

	/** 单位领导 **/
	public List<SpinnerItem> getleaderSpinnerItem(String condition) {
		List<SpinnerItem> leaderList = new ArrayList<SpinnerItem>();
		ArrayList<HashMap<String, Object>> type = SqliteUtil.getInstance()
				.queryBySqlReturnArrayListHashMap(
						"select * from PC_Users where (" + condition
								+ ") and SyncDataRegionCode='"
								+ Global.getGlobalInstance().getAreaCode()
								+ "' order by zw ");
		SpinnerItem item = null;
		int i = 0;
		for (Map<String, Object> map : type) {
			String code = map.get("userid").toString();
			String name = map.get("u_realname").toString();
			item = new SpinnerItem(code, name, i);
			leaderList.add(item);
			i++;
		}

		return leaderList;

	}

	/** 任务紧急程度 **/
	public List<SpinnerItem> gettaskStateSpinnerItem() {
		List<SpinnerItem> typeList = new ArrayList<SpinnerItem>();

		ArrayList<HashMap<String, Object>> type = SqliteUtil.getInstance()
				.queryBySqlReturnArrayListHashMap("select * from T_YDZF_JJCD");

		SpinnerItem item = null;
		int i = 0;
		for (Map<String, Object> map : type) {
			String code = map.get("code").toString();
			String name = map.get("name").toString();
			item = new SpinnerItem(code, name, i);
			typeList.add(item);
			i++;
		}

		return typeList;
	}

	/**
	 * 
	 * @param context
	 * @return 返回同步下来的任务条数 如果-1 同步任务失败
	 */
	public int getNewTask(Context context) {
		/** 判断同步占用状态 */

		int result = 0;
		DataSyncModel dataSync = new DataSyncModel();

		String[] tables = { "T_YDZF_RWXX", "T_YDZF_RWXX_USER", "TaskEnpriLink",
				"YDZF_RWLC" };
		for (int i = 0; i < tables.length; i++) {
			if (tables[i].equals("T_YDZF_RWXX")) {
				result = dataSync.synchronizeFetchServerData(true, tables[i]);
				if (result < 0) {// 同步失败
					return -1;
				}
			}/*
			 * else if (tables[i].equals("YDZF_RWLC"))
			 * {//任务流程信息同步更新时有很多数据同步不到，所以暂时改为同步全部
			 * dataSync.synchronizeFetchServerData(false, tables[i]); }
			 */else {
				dataSync.synchronizeFetchServerData(true, tables[i]);
			}

		}

		return result;

	}

	@Override
	public ArrayList<HashMap<String, Object>> getbottomname(Context context) {
		ArrayList<HashMap<String, Object>> BottomMenuList = null;
		try {
			BottomMenuList = XmlHelper.getMenuFromXml(context, BottomMenuName,
					"item", "bottommenu", getBottomMenuInputStream(context));
		} catch (IOException e) {
			e.printStackTrace();
		}
		checkBottomMenuList(BottomMenuList);
		return BottomMenuList;
	}

	/**
	 * Description:筛选出有权限的底部菜单
	 * 
	 * @author Administrator Create at: 2012-12-24 上午10:28:07
	 */
	public void checkBottomMenuList(
			ArrayList<HashMap<String, Object>> bottomlist) {
		Iterator iter = bottomlist.iterator();
		while (iter.hasNext()) {
			HashMap<String, Object> map = (HashMap<String, Object>) iter.next();
			if ("".equals(String.valueOf(map.get("qxid")))
					|| "null".equals(String.valueOf(map.get("qxid")))) {
				continue;
			}
			if (!DisplayUitl.getAuthority(String.valueOf(map.get("qxid")))) {
				iter.remove();
			}
		}
	}

	@Override
	public String getBottomMenuName() {
		return BottomMenuName;
	}

	/**
	 * 判断任务是否超期
	 * 
	 * @param taskBJQX
	 *            任务办结期限
	 * @return 是否过期
	 */
	public boolean checkOverDate(String taskBJQX) {
		if (taskBJQX == null || taskBJQX.equals("")) {
			return false;
		}
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		String nowDate = format.format(new Date());
		if (taskBJQX.compareTo(nowDate) < 0) {
			return true;
		}
		return false;
	}

	public void CheckTRandUpdate(String rwbh, String qyid) {

	}

	/**
	 * Description: 上传任务封装后方法，只需传入任务的GUID和上下文对象
	 * 
	 * @param rwid
	 *            任务GUID
	 * @param context
	 * @author wanglg
	 * @Create at: 2013-5-3 上午9:11:43
	 */
	public void uploadTask(final String rwbh, final Context context,
			String qyid, int mSingleChoiceID, boolean isComplete, int fType) {
		String extra_Path = SiteEvidenceActivity.TASK_PATH + "RWZX/";// 附件路径
		synchronizeUploadData(extra_Path, rwbh, qyid, context, mSingleChoiceID,
				isComplete, fType);
	}

	@Override
	public Intent InitData(Context context, Intent intent, String ywl) {

		// intent = new Intent(context, TaskMainActivity.class);
		intent = new Intent(context, TaskMainActivity.class);
		return intent;

	}

	public ArrayList<HashMap<String, Object>> getRwxxAttachment() {
		ArrayList<HashMap<String, Object>> rwxxActtachment = new ArrayList<HashMap<String, Object>>();
		String sql = "select a.* from T_YDZF_RWXX_FileAttachment as a left join T_YDZF_RWXX as b"
				+ " on a.rwbh=b.rwbh where b.guid='" + getCurrentID() + "'";
		rwxxActtachment = SqliteUtil.getInstance()
				.queryBySqlReturnArrayListHashMap(sql);
		return rwxxActtachment;
	}

	public String getFilePath() {
		String path = new String();
		String filepath = Global.SDCARD_FJ_LOCAL_PATH + "FileAttachment/"
				+ path;
		return filepath;
	}

	/**
	 * Description: 获取任务的执行人字符串，用逗号分开
	 * 
	 * @param rwbh
	 *            任务编号
	 * @return
	 * @author wanglg
	 * @Create at: 2013-7-16 下午4:48:27
	 */

	public String getTaskExecutor(String rwbh) {
		String taskExecutor = "";
		String sql = "select * from T_YDZF_RWXX_USER a left join PC_Users c on a.userid = c.userid where a.RWXXBH ='"
				+ rwbh + "'";
		ArrayList<HashMap<String, Object>> result = null;
		result = SqliteUtil.getInstance().queryBySqlReturnArrayListHashMap(sql);

		if (result != null && result.size() > 0) {
			StringBuilder sb = new StringBuilder();
			for (HashMap<String, Object> item : result) {
				sb.append(item.get("u_realname"));
				sb.append("，");
			}
			sb.deleteCharAt(sb.length() - 1);
			taskExecutor = sb.toString();
		}
		return taskExecutor;

	}

	/**
	 * 获取任务执行人的id集合
	 * 
	 * @param rwbh
	 * @return
	 */
	public ArrayList<String> getTaskExecutorId(String rwbh) {
		ArrayList<String> list = new ArrayList<String>();
		String sql = "select * from T_YDZF_RWXX_USER where RWXXBH ='" + rwbh
				+ "'";
		ArrayList<HashMap<String, Object>> result = null;
		result = SqliteUtil.getInstance().queryBySqlReturnArrayListHashMap(sql);

		if (result != null && result.size() > 0) {
			for (HashMap<String, Object> map : result) {
				list.add(map.get("userid").toString());
			}
		}
		return list;
	}

	/**
	 * 获取任务和企业的关联关系的id集合
	 * 
	 * @param rwbh
	 * @return
	 */
	public String getTask_qyId(String rwbh) {
		StringBuffer sb = new StringBuffer();
		String qyidStr = "";
		String sql = "select * from TaskEnpriLink where taskid ='" + rwbh + "'";
		ArrayList<HashMap<String, Object>> result = null;
		result = SqliteUtil.getInstance().queryBySqlReturnArrayListHashMap(sql);

		if (result != null && result.size() > 0) {
			for (HashMap<String, Object> map : result) {
				sb.append("'");
				sb.append(map.get("qyid").toString());
				sb.append("'");
				sb.append(",");
			}
			qyidStr = sb.toString()
					.substring(0, sb.toString().lastIndexOf(","));
		}
		return qyidStr;
	}

	/**
	 * 获取任务类型集合
	 * */
	public ArrayList<HashMap<String, Object>> getTask_type() {
		String sql = "select RegionName,RegionCode,ParentCode from Region where ParentCode='230000000'";
		ArrayList<HashMap<String, Object>> groupData = SqliteUtil.getInstance()
				.queryBySqlReturnArrayListHashMap(sql);
		return groupData;
	}

	/**
	 * 获取任务类型集合
	 * */
	public ArrayList<HashMap<String, Object>> getTask_type2() {
		String sql = "select name,code from  T_YDZF_RWLX where pcode='' or pcode=' ' or pcode=null order by code";
		ArrayList<HashMap<String, Object>> groupData = SqliteUtil.getInstance()
				.queryBySqlReturnArrayListHashMap(sql);
		return groupData;
	}
	
	/**
	 * 获取人员的外层 根据当前人员地区id
	 * */
	public ArrayList<HashMap<String, Object>> getTask_type3() {
		ArrayList<HashMap<String, Object>> dates=new ArrayList<HashMap<String, Object>>();
//			String sql1="select RegionName,RegionCode,ParentCode from Region where RegionCode='"+Global.getGlobalInstance().getAreaCode()+"'";
//				ArrayList<HashMap<String, Object>> list = SqliteUtil.getInstance()
//										.queryBySqlReturnArrayListHashMap(sql1);
//					dates.addAll(list);
		String sql = "select RegionName,RegionCode,ParentCode from Region where ParentCode='"+Global.getGlobalInstance().getAreaCode()+"'";
		ArrayList<HashMap<String, Object>> groupData = SqliteUtil.getInstance()
				.queryBySqlReturnArrayListHashMap(sql);
		dates.addAll(groupData);
	
		
		return dates;
	}

	/**
	 * 获取任务类型集合子集
	 * */
	public ArrayList<ArrayList<HashMap<String, Object>>> getTask_type_child(
			ArrayList<HashMap<String, Object>> groupData) {
		final ArrayList<ArrayList<HashMap<String, Object>>> childMapData = new ArrayList<ArrayList<HashMap<String, Object>>>();
		if (groupData != null && groupData.size() > 0)
			for (int i = 0; i < groupData.size(); i++) {
				HashMap<String, Object> hashMap = groupData.get(i);
				String tcodeStr = hashMap.get("regioncode") + "";
				/** 查询二级列表数据集合 */
				ArrayList<HashMap<String, Object>> Alist = SqliteUtil
						.getInstance().queryBySqlReturnArrayListHashMap(
								"select RegionName,RegionCode,ParentCode from Region where ParentCode='"
										+ tcodeStr + "'");
				if ((Alist == null) || (Alist.size() == 0)) {
					Alist = new ArrayList<HashMap<String, Object>>();
					Alist.add(hashMap);
				}
				childMapData.add(Alist);
			}
		return childMapData;

	}
	
	
	public ArrayList<ArrayList<HashMap<String, Object>>> getTask_type_child3(
			ArrayList<HashMap<String, Object>> groupData) {
			final ArrayList<ArrayList<HashMap<String, Object>>> childMapData = new ArrayList<ArrayList<HashMap<String, Object>>>();
			if (groupData != null && groupData.size() > 0)
				for (int i = 0; i < groupData.size(); i++) {
					HashMap<String, Object> hashMap = groupData.get(i);
					String tcodeStr = hashMap.get("regioncode") + "";
					/** 查询二级列表数据集合 */
					ArrayList<HashMap<String, Object>> Alist =new ArrayList<HashMap<String,Object>>();
					if (Global.getGlobalInstance().getAreaCode().equals("230000000")) {
						 Alist = SqliteUtil
								.getInstance().queryBySqlReturnArrayListHashMap(
										"select RegionName,RegionCode,ParentCode from Region where ParentCode='"
												+ tcodeStr + "'");
					}
				
					Alist.add(0,hashMap);
					if ((Alist == null) || (Alist.size() == 0)) {
						Alist = new ArrayList<HashMap<String, Object>>();
						Alist.add(hashMap);
					}
					childMapData.add(Alist);
				}
			return childMapData;

	}

	/**
	 * 获取任务类型集合子集
	 * */
	public ArrayList<ArrayList<HashMap<String, Object>>> getTask_type_child2(
			ArrayList<HashMap<String, Object>> groupData) {
		final ArrayList<ArrayList<HashMap<String, Object>>> childMapData = new ArrayList<ArrayList<HashMap<String, Object>>>();
		if (groupData != null && groupData.size() > 0)
			for (int i = 0; i < groupData.size(); i++) {
				HashMap<String, Object> hashMap = groupData.get(i);
				String tcodeStr = hashMap.get("code").toString();
				/** 查询二级列表数据集合 */
				ArrayList<HashMap<String, Object>> Alist = SqliteUtil
						.getInstance().queryBySqlReturnArrayListHashMap(
								"select name,code from  T_YDZF_RWLX where pcode= '"
										+ tcodeStr + "'");
				if ((Alist == null) || (Alist.size() == 0)) {
					Alist = new ArrayList<HashMap<String, Object>>();
					Alist.add(hashMap);
				}
				childMapData.add(Alist);
			}
		return childMapData;

	}

	/**
	 * 二级树列表的适配器
	 * */
	public static class ExpandableBaseAdapter extends BaseExpandableListAdapter {

		public Context context;
		public LayoutInflater inflater;
		public List<String> groupList;
		public ArrayList<ArrayList<HashMap<String, Object>>> childMapData;

		public ExpandableBaseAdapter(Context context, List<String> groupList,
				ArrayList<ArrayList<HashMap<String, Object>>> childMapData) {
			this.context = context;
			this.inflater = LayoutInflater.from(context);
			this.childMapData = childMapData;
			this.groupList = groupList;

		}

		@Override
		public Object getChild(int groupPosition, int childPosition) {
			return childMapData.get(groupPosition).get(childPosition);
		}

		@Override
		public int getChildrenCount(int groupPosition) {
			return childMapData.get(groupPosition).size();
		}

		@Override
		public long getChildId(int groupPosition, int childPosition) {
			return childPosition;
		}

		@Override
		public View getChildView(final int groupPosition,
				final int childPosition, boolean isLastChild, View convertView,
				ViewGroup parent) {
			if (convertView == null) {
				// convertView = inflater.inflate(R.layout.two_class_interface,
				// null);
				convertView = inflater.inflate(
						R.layout.two_class_interface_radio, null);
			}
			final childViewHolder holder = new childViewHolder();
			holder.two_class_interface_name_tv = (TextView) convertView
					.findViewById(R.id.two_class_interface_name_tv);
			// holder.two_class_rb = (RadioButton) convertView
			// .findViewById(R.id.two_class_rb);
			holder.two_class_interface_name_tv.setText(childMapData
					.get(groupPosition).get(childPosition).get("regionname")
					+ "");
			if(childMapData.get(groupPosition).get(childPosition).get("regionname").toString().equals("市辖区")){
				holder.two_class_interface_name_tv.setText(groupList.get(
						groupPosition).toString());
				convertView.setTag("1");//取父节点code
			}else {
				convertView.setTag("0");//取父节点code
			}
			// holder.two_class_rb.setChecked(childCheckBox.get(groupPosition)
			// .get(childPosition).get(UNCHECK_ALL));

			return convertView;
		}

		@Override
		public Object getGroup(int groupPosition) {
			return groupList.get(groupPosition);
		}

		@Override
		public int getGroupCount() {
			return groupList.size();
		}

		@Override
		public long getGroupId(int groupPosition) {
			return groupPosition;
		}

		@Override
		public View getGroupView(int groupPosition, boolean isExpanded,
				View convertView, ViewGroup parent) {
			GroupViewHolder groupHolder = null;

			if (convertView == null) {
				groupHolder = new GroupViewHolder();
				convertView = inflater.inflate(R.layout.one_class_interface,
						null);

				groupHolder.one_class_interface_title_tv = (TextView) convertView
						.findViewById(R.id.one_class_interface_title_tv);

				convertView.setTag(groupHolder);
			} else {
				groupHolder = (GroupViewHolder) convertView.getTag();
			}
			groupHolder.one_class_interface_title_tv.setText(groupList.get(
					groupPosition).toString());

			return convertView;
		}

		@Override
		public boolean hasStableIds() {
			return false;
		}

		@Override
		public boolean isChildSelectable(int groupPosition, int childPosition) {
			return true;
		}

		private class GroupViewHolder {
			TextView one_class_interface_title_tv;
		}

		private class childViewHolder {
			// RadioButton two_class_rb;
			TextView two_class_interface_name_tv;
		}
	}

	/**
	 * 二级树列表的适配器
	 * */
	public static class ExpandableBaseAdapterr extends
			BaseExpandableListAdapter {

		public Context context;
		public LayoutInflater inflater;
		public List<String> groupList;
		public ArrayList<ArrayList<HashMap<String, Object>>> childMapData;

		public ExpandableBaseAdapterr(Context context, List<String> groupList,
				ArrayList<ArrayList<HashMap<String, Object>>> childMapData) {
			this.context = context;
			this.inflater = LayoutInflater.from(context);
			this.childMapData = childMapData;
			this.groupList = groupList;

		}

		@Override
		public Object getChild(int groupPosition, int childPosition) {
			return childMapData.get(groupPosition).get(childPosition);
		}

		@Override
		public int getChildrenCount(int groupPosition) {
			return childMapData.get(groupPosition).size();
		}

		@Override
		public long getChildId(int groupPosition, int childPosition) {
			return childPosition;
		}

		@Override
		public View getChildView(final int groupPosition,
				final int childPosition, boolean isLastChild, View convertView,
				ViewGroup parent) {
			if (convertView == null) {
				// convertView = inflater.inflate(R.layout.two_class_interface,
				// null);
				convertView = inflater.inflate(
						R.layout.two_class_interface_radio, null);
			}
			final childViewHolder holder = new childViewHolder();
			holder.two_class_interface_name_tv = (TextView) convertView
					.findViewById(R.id.two_class_interface_name_tv);
			// holder.two_class_rb = (RadioButton) convertView
			// .findViewById(R.id.two_class_rb);
			holder.two_class_interface_name_tv.setText(childMapData
					.get(groupPosition).get(childPosition).get("name")
					+ "");
			// holder.two_class_rb.setChecked(childCheckBox.get(groupPosition)
			// .get(childPosition).get(UNCHECK_ALL));

			return convertView;
		}

		@Override
		public Object getGroup(int groupPosition) {
			return groupList.get(groupPosition);
		}

		@Override
		public int getGroupCount() {
			return groupList.size();
		}

		@Override
		public long getGroupId(int groupPosition) {
			return groupPosition;
		}

		@Override
		public View getGroupView(int groupPosition, boolean isExpanded,
				View convertView, ViewGroup parent) {
			GroupViewHolder groupHolder = null;

			if (convertView == null) {
				groupHolder = new GroupViewHolder();
				convertView = inflater.inflate(R.layout.one_class_interface,
						null);

				groupHolder.one_class_interface_title_tv = (TextView) convertView
						.findViewById(R.id.one_class_interface_title_tv);

				convertView.setTag(groupHolder);
			} else {
				groupHolder = (GroupViewHolder) convertView.getTag();
			}
			groupHolder.one_class_interface_title_tv.setText(groupList.get(
					groupPosition).toString());

			return convertView;
		}

		@Override
		public boolean hasStableIds() {
			return false;
		}

		@Override
		public boolean isChildSelectable(int groupPosition, int childPosition) {
			return true;
		}

		private class GroupViewHolder {
			TextView one_class_interface_title_tv;
		}

		private class childViewHolder {
			// RadioButton two_class_rb;
			TextView two_class_interface_name_tv;
		}
	}
	/**
	 * 二级树列表的适配器
	 * */
	public static class ExpandableIncludeParentAdapter extends
			BaseExpandableListAdapter {

		public Context context;
		public LayoutInflater inflater;
		public List<String> groupList;
		public ArrayList<ArrayList<HashMap<String, Object>>> childMapData;

		public ExpandableIncludeParentAdapter(Context context, List<String> groupList,
				ArrayList<ArrayList<HashMap<String, Object>>> childMapData) {
			this.context = context;
			this.inflater = LayoutInflater.from(context);
			this.childMapData = childMapData;
			this.groupList = groupList;

		}

		@Override
		public Object getChild(int groupPosition, int childPosition) {
			if(childPosition == 0) {
				return groupList.get(groupPosition);
			}else {
				return childMapData.get(groupPosition).get(childPosition-1);
			}
			
		}

		@Override
		public int getChildrenCount(int groupPosition) {
			return childMapData.get(groupPosition).size() + 1;
		}

		@Override
		public long getChildId(int groupPosition, int childPosition) {
			return childPosition;
		}

		@Override
		public View getChildView(final int groupPosition,
				final int childPosition, boolean isLastChild, View convertView,
				ViewGroup parent) {
			if (convertView == null) {
				// convertView = inflater.inflate(R.layout.two_class_interface,
				// null);
				convertView = inflater.inflate(
						R.layout.two_class_interface_radio, null);
			}
			final childViewHolder holder = new childViewHolder();
			holder.two_class_interface_name_tv = (TextView) convertView
					.findViewById(R.id.two_class_interface_name_tv);
			// holder.two_class_rb = (RadioButton) convertView
			// .findViewById(R.id.two_class_rb);
			if(childPosition == 0) {
				holder.two_class_interface_name_tv.setText(groupList.get(
						groupPosition).toString());
			}else {
				holder.two_class_interface_name_tv.setText(childMapData
						.get(groupPosition).get(childPosition-1).get("regionname")
						+ "");
			}
			// holder.two_class_rb.setChecked(childCheckBox.get(groupPosition)
			// .get(childPosition).get(UNCHECK_ALL));

			return convertView;
		}

		@Override
		public Object getGroup(int groupPosition) {
			return groupList.get(groupPosition);
		}

		@Override
		public int getGroupCount() {
			return groupList.size();
		}

		@Override
		public long getGroupId(int groupPosition) {
			return groupPosition;
		}

		@Override
		public View getGroupView(int groupPosition, boolean isExpanded,
				View convertView, ViewGroup parent) {
			GroupViewHolder groupHolder = null;

			if (convertView == null) {
				groupHolder = new GroupViewHolder();
				convertView = inflater.inflate(R.layout.one_class_interface,
						null);

				groupHolder.one_class_interface_title_tv = (TextView) convertView
						.findViewById(R.id.one_class_interface_title_tv);

				convertView.setTag(groupHolder);
			} else {
				groupHolder = (GroupViewHolder) convertView.getTag();
			}
			groupHolder.one_class_interface_title_tv.setText(groupList.get(
					groupPosition).toString());

			return convertView;
		}

		@Override
		public boolean hasStableIds() {
			return false;
		}

		@Override
		public boolean isChildSelectable(int groupPosition, int childPosition) {
			return true;
		}

		private class GroupViewHolder {
			TextView one_class_interface_title_tv;
		}

		private class childViewHolder {
			// RadioButton two_class_rb;
			TextView two_class_interface_name_tv;
		}
	}
	/**
	 * 二级树列表的适配器
	 * */
	public static class ExpandableBaseAdapter1 extends
			BaseExpandableListAdapter {

		public Context context;
		public LayoutInflater inflater;
		public List<String> groupList;
		public ArrayList<ArrayList<HashMap<String, Object>>> childMapData;

		private final LinkedList<String> usersb;
		private final LinkedList<String> linkedName;

		public ExpandableBaseAdapter1(Context context, List<String> groupList,
				ArrayList<ArrayList<HashMap<String, Object>>> childMapData,
				LinkedList<String> usersb, LinkedList<String> linkedName) {
			this.context = context;
			this.inflater = LayoutInflater.from(context);
			this.childMapData = childMapData;
			this.groupList = groupList;
			this.usersb = usersb;
			this.linkedName = linkedName;

		}

		@Override
		public Object getChild(int groupPosition, int childPosition) {
			return childMapData.get(groupPosition).get(childPosition);
		}

		@Override
		public int getChildrenCount(int groupPosition) {
			return childMapData.get(groupPosition).size();
		}

		@Override
		public long getChildId(int groupPosition, int childPosition) {
			return childPosition;
		}

		@Override
		public View getChildView(final int groupPosition,
				final int childPosition, boolean isLastChild, View convertView,
				ViewGroup parent) {
			// if (convertView == null) {
			convertView = inflater.inflate(R.layout.two_class_interface, null);
			// }

			final childViewHolder holder = new childViewHolder();
			holder.two_class_interface_name_tv = (TextView) convertView
					.findViewById(R.id.two_class_interface_name_tv);
			holder.two_class_cb = (CheckBox) convertView
					.findViewById(R.id.two_class_cb);

			holder.two_class_interface_name_tv.setText(childMapData
					.get(groupPosition).get(childPosition).get("RegionName")
					.toString());

			final String realName = childMapData.get(groupPosition)
					.get(childPosition).get("RegionName").toString();
			final String userCheckedId = childMapData.get(groupPosition)
					.get(childPosition).get("RegionCode").toString();

			if (usersb.contains(userCheckedId)) {
				holder.two_class_cb.setChecked(true);
			} else {
				holder.two_class_cb.setChecked(false);
			}
			holder.two_class_cb
					.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

						@Override
						public void onCheckedChanged(CompoundButton buttonView,
								boolean isChecked) {
							if (isChecked) {
								if (!usersb.contains(userCheckedId)) {
									usersb.add(userCheckedId);
								}
								if (!linkedName.contains(realName)) {
									linkedName.add(realName);
								}
							} else {
								if (usersb.contains(userCheckedId)) {
									usersb.remove(userCheckedId);
								}
								if (linkedName.contains(realName)) {
									linkedName.remove(realName);
								}
							}
						}
					});

			return convertView;
		}

		@Override
		public Object getGroup(int groupPosition) {
			return groupList.get(groupPosition);
		}

		@Override
		public int getGroupCount() {
			return groupList.size();
		}

		@Override
		public long getGroupId(int groupPosition) {
			return groupPosition;
		}

		@Override
		public View getGroupView(int groupPosition, boolean isExpanded,
				View convertView, ViewGroup parent) {
			GroupViewHolder groupHolder = null;

			if (convertView == null) {
				groupHolder = new GroupViewHolder();
				convertView = inflater.inflate(R.layout.one_class_interface,
						null);

				groupHolder.one_class_interface_title_tv = (TextView) convertView
						.findViewById(R.id.one_class_interface_title_tv);

				convertView.setTag(groupHolder);
			} else {
				groupHolder = (GroupViewHolder) convertView.getTag();
			}

			groupHolder.one_class_interface_title_tv.setText(groupList.get(
					groupPosition).toString());

			return convertView;
		}

		@Override
		public boolean hasStableIds() {
			return false;
		}

		@Override
		public boolean isChildSelectable(int groupPosition, int childPosition) {
			return true;
		}

		private class GroupViewHolder {
			TextView one_class_interface_title_tv;
		}

		private class childViewHolder {
			CheckBox two_class_cb;
			TextView two_class_interface_name_tv;
		}
	}

	/**
	 * 二级树列表的适配器
	 * */
	public static class ExpandableBaseAdapter12 extends
			BaseExpandableListAdapter {

		public Context context;
		public LayoutInflater inflater;
		public List<String> groupList;
		public ArrayList<ArrayList<HashMap<String, Object>>> childMapData;

		private final LinkedList<String> usersb;
		private final LinkedList<String> linkedName;

		public ExpandableBaseAdapter12(Context context, List<String> groupList,
				ArrayList<ArrayList<HashMap<String, Object>>> childMapData,
				LinkedList<String> usersb, LinkedList<String> linkedName) {
			this.context = context;
			this.inflater = LayoutInflater.from(context);
			this.childMapData = childMapData;
			this.groupList = groupList;
			this.usersb = usersb;
			this.linkedName = linkedName;

		}

		@Override
		public Object getChild(int groupPosition, int childPosition) {
			return childMapData.get(groupPosition).get(childPosition);
		}

		@Override
		public int getChildrenCount(int groupPosition) {
			return childMapData.get(groupPosition).size();
		}

		@Override
		public long getChildId(int groupPosition, int childPosition) {
			return childPosition;
		}

		@Override
		public View getChildView(final int groupPosition,
				final int childPosition, boolean isLastChild, View convertView,
				ViewGroup parent) {
			// if (convertView == null) {
			convertView = inflater.inflate(R.layout.two_class_interface, null);
			// }

			final childViewHolder holder = new childViewHolder();
			holder.two_class_interface_name_tv = (TextView) convertView
					.findViewById(R.id.two_class_interface_name_tv);
			holder.two_class_cb = (CheckBox) convertView
					.findViewById(R.id.two_class_cb);

			holder.two_class_interface_name_tv.setText(childMapData
					.get(groupPosition).get(childPosition).get("name")
					.toString());

			final String realName = childMapData.get(groupPosition)
					.get(childPosition).get("name").toString();
			final String userCheckedId = childMapData.get(groupPosition)
					.get(childPosition).get("code").toString();

			if (usersb.contains(userCheckedId)) {
				holder.two_class_cb.setChecked(true);
			} else {
				holder.two_class_cb.setChecked(false);
			}
			holder.two_class_cb
					.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

						@Override
						public void onCheckedChanged(CompoundButton buttonView,
								boolean isChecked) {
							if (isChecked) {
								if (!usersb.contains(userCheckedId)) {
									usersb.add(userCheckedId);
								}
								if (!linkedName.contains(realName)) {
									linkedName.add(realName);
								}
							} else {
								if (usersb.contains(userCheckedId)) {
									usersb.remove(userCheckedId);
								}
								if (linkedName.contains(realName)) {
									linkedName.remove(realName);
								}
							}
						}
					});

			return convertView;
		}

		@Override
		public Object getGroup(int groupPosition) {
			return groupList.get(groupPosition);
		}

		@Override
		public int getGroupCount() {
			return groupList.size();
		}

		@Override
		public long getGroupId(int groupPosition) {
			return groupPosition;
		}

		@Override
		public View getGroupView(int groupPosition, boolean isExpanded,
				View convertView, ViewGroup parent) {
			GroupViewHolder groupHolder = null;

			if (convertView == null) {
				groupHolder = new GroupViewHolder();
				convertView = inflater.inflate(R.layout.one_class_interface,
						null);

				groupHolder.one_class_interface_title_tv = (TextView) convertView
						.findViewById(R.id.one_class_interface_title_tv);

				convertView.setTag(groupHolder);
			} else {
				groupHolder = (GroupViewHolder) convertView.getTag();
			}

			groupHolder.one_class_interface_title_tv.setText(groupList.get(
					groupPosition).toString());

			return convertView;
		}

		@Override
		public boolean hasStableIds() {
			return false;
		}

		@Override
		public boolean isChildSelectable(int groupPosition, int childPosition) {
			return true;
		}

		private class GroupViewHolder {
			TextView one_class_interface_title_tv;
		}

		private class childViewHolder {
			CheckBox two_class_cb;
			TextView two_class_interface_name_tv;
		}
	}

	// 任务下发带附件
	public void tasksIssued(String guid, String rwbh, String fjPath,
			Context context) {

		tr = new TaskResult();

		ArrayList<String> _ListFile = getUploadFile(rwbh, context);
		ArrayList<File> _ListAllFile = getAllUploadFile(fjPath, context);
		tr.filenum = _ListAllFile.size();

		// 遍历所有文件
		for (File _File : _ListAllFile) {

			if (_ListFile.contains(_File.getAbsolutePath())) {
				continue;
			}

			String nodirfilename = _File.getAbsolutePath();
			nodirfilename = nodirfilename.substring(nodirfilename
					.lastIndexOf("/") + 1);

			UpLoadFJ task = new UpLoadFJ(context, nodirfilename,
					messagehandler, rwbh, guid);// this
			task.execute(Global.getGlobalInstance().getSystemurl(),
					_File.getAbsolutePath());
		}
	}

	class TaskResult {
		public TaskResult() {

		}

		int filenum = 0;// 需上传文件数
		int fileuploadnum = 0;// 已上传文件数
		boolean istaskdataupload = false;// 任务数据是否上传
	}

	class UpLoadFJ extends AsyncTask<String, Integer, String> {

		// 可变长的输入参数，与AsyncTask.exucute()对应
		// ProgressDialog mProgressDialog;
		Handler handler;
		Context mContext;
		String mFilename;
		String mRwbh;
		String mGuid;

		public UpLoadFJ(Context context, String filename,
				Handler messagehandler, String rwbh, String guid) {
			this.mContext = context;
			this.handler = messagehandler;
			this.mFilename = filename;
			this.mRwbh = rwbh;
			this.mGuid = guid;

			/*
			 * mProgressDialog = new ProgressDialog(context, 0);
			 * mProgressDialog.setTitle(filename + "正在上传附件...");
			 * mProgressDialog.setCancelable(false);
			 * mProgressDialog.setMax(100);
			 * mProgressDialog.setProgressStyle(ProgressDialog
			 * .STYLE_HORIZONTAL); mProgressDialog.show();
			 */
		}

		@Override
		protected String doInBackground(String... params) {

			String surl = params[0];// "http://192.168.0.113/liaoning";
			String absFileName = params[1];
			String namespace = "http://tempuri.org/";
			String url = surl + "/WebService/MobileEnforcementWebService.asmx";
			try {
				absFileName = new String(absFileName.getBytes(), "UTF-8");
			} catch (UnsupportedEncodingException e1) {
				Log.e(TAG, e1.getMessage());
			}
			// mProgressDialog.setMessage(absFileName + "正在上传附件...");
			ByteArrayOutputStream baos = null;
			FileInputStream fis = null;
			boolean result = false;

			int a = absFileName.lastIndexOf('/');
			int b = absFileName.lastIndexOf('/', a - 1);
			int c = absFileName.lastIndexOf('/', b - 1);
			String filename = absFileName.substring(c + 1);

			ArrayList<HashMap<String, Object>> params0 = new ArrayList<HashMap<String, Object>>();
			HashMap<String, Object> param0 = new HashMap<String, Object>();
			param0.put("Path", filename);
			params0.add(param0);

			int finishblocks = 0;

			try {
				Object resultResponseObj0 = WebServiceProvider.callWebService(
						namespace, "GetProgress", params0, url,
						WebServiceProvider.RETURN_INT, true);
				if (null != resultResponseObj0)
					finishblocks = Integer.parseInt(resultResponseObj0
							.toString());
				if (finishblocks == 20000) {

					tr.fileuploadnum++;
					publishProgress(100);
					return null;
				}

			} catch (Exception e1) {
				e1.printStackTrace();
			}

			try {
				// StringBuffer strbuf=new StringBuffer();
				File absFile = new File(absFileName);
				fis = new FileInputStream(absFileName);
				baos = new ByteArrayOutputStream();

				int count = 0;
				// int i=0;
				boolean end = false;

				int totalblocks = (int) absFile.length() / (1024 * 500) + 1;
				for (int i = 0; i < totalblocks; i++) {
					String attachmentData = "";
					// nm.notify("正在上传"+i+"块", 0, null);
					if (i == (int) absFile.length() / (1024 * 500)) {
						end = true;
						byte[] buffers = new byte[(int) absFile.length()
								% (1024 * 500)];

						count = fis.read(buffers);

						attachmentData = Base64.encodeToString(buffers,
								Base64.DEFAULT);

					} else {
						byte[] buffer = new byte[1024 * 500];

						count = fis.read(buffer);

						// baos.write(buffer,0,count);
						attachmentData = Base64.encodeToString(buffer,
								Base64.DEFAULT);

					}

					if (i >= finishblocks) {

						ArrayList<HashMap<String, Object>> params1 = new ArrayList<HashMap<String, Object>>();

						JSONArray _JsonFJ = GetAnnexTable(mGuid, i);// 获得T_Attachment(附件)表中的数据

						HashMap<String, Object> param = new HashMap<String, Object>();
						param.put("AttachmentJosn", _JsonFJ.toString() + "");
						params1.add(param);

						param = new HashMap<String, Object>();
						param.put("FileValue", attachmentData);
						params1.add(param);

						param = new HashMap<String, Object>();
						param.put("isEnd", end);
						params1.add(param);

						publishProgress((i + 1) * 100 / totalblocks);

						Object resultResponseObj = WebServiceProvider
								.callWebService(namespace, "UploadFile",
										params1, url,
										WebServiceProvider.RETURN_BOOLEAN, true);
						boolean resultResponse = false;
						if (null != resultResponseObj) {
							resultResponse = (Boolean) resultResponseObj;
						}

						if (resultResponse) {
							result = resultResponse;

							if (handler != null && i + 1 == totalblocks) {
								Message msg = handler.obtainMessage();
								msg.what = UPLOAD_FILE_SUCCESS;
								msg.obj = mFilename;
								handler.sendMessage(msg);
								tr.fileuploadnum++;
							}
						} else {
							Message msg = new Message();
							msg.what = NONET;
							msg.obj = "Webservice异常";
							if (handler != null) {
								handler.sendMessage(msg);
							}
							break;
						}
					}
				}

				Log.v(TAG, absFileName + "\t成功转化为Base64 Bytes");
			} catch (Exception e) {
				if (e.getMessage() != null) {
					Log.e(TAG, e.getMessage());
				}
				return null;
			} finally {
				try {
					baos.flush();
					baos.close();
					fis.close();

				} catch (IOException e) {
					Log.e(TAG, e.getMessage());
					return null;
				}
			}
			return null;
		}

		@Override
		protected void onCancelled() {
			super.onCancelled();
		}

		@Override
		protected void onPostExecute(String result) {
			// 返回HTML页面的内容
			// mProgressDialog.dismiss();
			TaskRegisterActivity tea = new TaskRegisterActivity();
			tea.finish();
		}

		@Override
		protected void onPreExecute() {
			// 任务启动，可以在这里显示一个对话框，这里简单处理

		}

		@Override
		protected void onProgressUpdate(Integer... values) {
			// 更新进度
			// mProgressDialog.setProgress(values[0]);
		}

	}

	static Context mcontext;
	static ProgressDialog pd = null;
	final static Handler messagehandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case PROGRESS:
				break;
			case NONET:
				Toast.makeText(mcontext, "网络不通，请检查网络设置！", Toast.LENGTH_LONG)
						.show();
				break;

			case UPLOAD_FILE_SUCCESS:
				/*
				 * Toast.makeText(mcontext, msg.obj.toString() + "附件上传成功",
				 * Toast.LENGTH_LONG).show();
				 */
				break;

			case UPLOAD_SUCCESS:
				// 上传成功，设置状态003 待改 YK
				ContentValues cv = new ContentValues();
				cv.put("rwzt", "003");
				try {
					int state = SqliteUtil.getInstance().update("T_YDZF_RWXX",
							cv, "rwbh=?", new String[] { msg.obj.toString() });
				} catch (Exception e) {
					ExceptionManager.WriteCaughtEXP(e, "TaskListActivity");
					e.printStackTrace();
				}
				Toast.makeText(mcontext, "任务上传成功", Toast.LENGTH_LONG).show();

				break;
			case UPLOAD_FAIL:
				if (msg.obj != null) {
					String reason = msg.obj.toString();
					Toast.makeText(mcontext, reason, Toast.LENGTH_SHORT).show();
				}
				break;
			case ALREADY_UPLOAD:
				Toast.makeText(mcontext, "任务已上传，请勿重复上传！", Toast.LENGTH_SHORT)
						.show();
				break;
			}
		};
	};

	/**
	 * Description: 获取已上传的文件
	 * 
	 * @param RWBH
	 *            任务编号
	 * @param _Context
	 *            上下文
	 * @return 已上传文件的数据ArrayList<String> ArrayList<String>
	 * @author 柳思远 Create at: 2012-12-4 上午11:44:30
	 */
	private ArrayList<String> getUploadFile(String RWBH, Context _Context) {
		/** 创建已上传的数据集合 */
		ArrayList<String> _ListFile = new ArrayList<String>();

		/** 共享数据 */
		SharedPreferences _SharedPreferences = _Context.getSharedPreferences(
				RWBH, _Context.MODE_WORLD_READABLE);

		/** 获取已上传的数据 */
		Map _Map = _SharedPreferences.getAll();
		Iterator<Map.Entry<String, ?>> iterator = _Map.entrySet().iterator();
		while (iterator.hasNext()) {
			Map.Entry<String, ?> entry = iterator.next();
			if (entry.getKey().indexOf(RWBH) != -1) {
				_ListFile.add(entry.getKey());
			}
		}
		return _ListFile;
	}

	/**
	 * Description: 获取当前任务所有上传文件
	 * 
	 * @param _Path
	 *            文件路径
	 * @param _TaskId
	 *            任务编号
	 * @param _Context
	 *            上下文
	 * @return 返回该路径下所有文件 ArrayList<File>
	 * @author 柳思远 Create at: 2012-12-4 上午11:46:48
	 */
	public ArrayList<File> getAllUploadFile(String _Path, Context _Context) {
		ArrayList<File> _ListFile = new ArrayList<File>();

		/** 如果没有文件直接返回 */
		File _File = new File(_Path);
		if (!_File.exists()) {
			return _ListFile;
		}
		FileHelper fileHelper = new FileHelper();
		fileHelper.getAbsFiles(_File, _ListFile);

		return _ListFile;
	}

	/**
	 * Description: 获取T_Attachment附件表信息
	 * 
	 * @param guid
	 *            (T_Attachment 表中Guid)
	 * 
	 * @return 返回勘查笔录信息{JSONArray} JSONArray
	 * @author 兰光 Create at: 2013-08-09下午16:17:55
	 */
	private JSONArray GetAnnexTable(String guid, int k) {

		ArrayList<HashMap<String, Object>> _ArrayList = new ArrayList<HashMap<String, Object>>();
		_ArrayList = SqliteUtil.getInstance().queryBySqlReturnArrayListHashMap(
				"select * from T_Attachment where guid = '" + guid + "'");

		JSONArray _JsonArray = new JSONArray();
		try {
			for (int i = 0; i < _ArrayList.size(); i++) {
				HashMap<String, Object> _HashMapTemp = _ArrayList.get(i);
				Set _Iterator = _HashMapTemp.entrySet();

				JSONObject _JsonObjectData = new JSONObject();
				for (Iterator iter = _Iterator.iterator(); iter.hasNext();) {

					Map.Entry entry = (Map.Entry) iter.next();
					String _Key = entry.getKey().toString();
					if (_Key.contains("filepath")) {
						String fileNameWithPath = entry.getValue().toString();
						fileNameWithPath = fileNameWithPath + "." + k;
						_JsonObjectData.put("fileNameWithPath",
								fileNameWithPath);
					}
					Object _Object = _HashMapTemp.get(_Key);

					_JsonObjectData.put(entry.getKey().toString(), entry
							.getValue().toString());
				}
				_JsonArray.put(_JsonObjectData);
			}
		} catch (JSONException ex) {
			LogUtil.v("Json", ex.getMessage());
		}
		return _JsonArray;
	}

	private String GetExeLawsTempletItems_lzjdk(String Taskid, String qyid) {
		ArrayList<HashMap<String, Object>> _ArrayList;

		_ArrayList = SqliteUtil.getInstance().getList(
				"ExeLawsTemplet where TaskID='" + Taskid + "'and EnterId='"
						+ qyid + /**/"'and templettype = 'LZJDK'");
		JSONArray _JsonArray = new JSONArray();
		String billid = "";
		for (int i = 0; i < _ArrayList.size(); i++) {
			HashMap<String, Object> _HashMapTemp = _ArrayList.get(i);
			Set _Iterator = _HashMapTemp.entrySet();

			JSONObject _JsonObjectData = new JSONObject();
			for (Iterator iter = _Iterator.iterator(); iter.hasNext();) {

				Map.Entry entry = (Map.Entry) iter.next();
				String _Key = entry.getKey().toString();
				Object _Object = _HashMapTemp.get(_Key);
				if (_Key.equals("billid") || _Key.equals("BillId")
						|| _Key.equals("Billid") || _Key.equals("billId")) {
					billid = entry.getValue().toString();
				}
			}
		}
		return billid;
	}

}
