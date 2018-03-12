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
 * @author ��˼Զ
 * @Version 1.3.4
 * @Copyright �п���ͼ���¿Ƽ����޹�˾ Create at: 2012-12-4 ����10:51:15
 */
// public class RWXX extends BaseClass implements Serializable, IList,
// IDetailed,
// IQuery, IGrid {
public class RWXX extends BaseClass implements Serializable, IList, IDetailed,
		IQuery, IInitData {

	/** �������л��ĺ��� */
	private final long serialVersionUID = 19881121L;
	/** ʵ��������� */
	public static final String BusinessClassName = "RWXX";
	/** ��ȡ��ʵ�����б���ʽ�ı��� */
	private final String ListStyleName = "RWLB";
	/** ��ȡ��ʵ��������������ʽ�ı��� */
	private final String DetailedStyleName = "RWXX";
	/** ��ȡ��ʵ��������������ʽ�ı��� */
	private final String DetailedStyleNameOffice = "RWXXOffice";
	/** ��ȡ��ʵ�����ѯ��ʽ�ı��� */
	private final String QueryStyleName = "RWCX";
	/** ��ȡ��ʵ����ײ��˵��ı��� */
	private final static String BottomMenuName = "RWGL";
	/** ��ʵ�������ڱ������ */
	private final String primaryKey = "Guid";
	/** ��ѯ��ʵ����������Ϣ���õı��� */
	private final String TableName = "V_YDZF_RWXX";
	/** ��ʵ��������ʾ���������õ����ֱ��� */
	private final String DetailedTitleText = "��������";
	/** ��ʵ������ִ�в�ѯ������ʱ�����õ����ֱ��� */
	private final String QueryTitleText = "�����ѯ";
	/** ��ʼ����ǰ��ʵ�����б�����Ĵ��� */
	public int ListScrollTimes;
	/** ��ǰ�����idֵ */
	private String CurrentID = "";
	/** ��ʵ�����ɸѡ�������� */
	private HashMap<String, Object> Filter;
	private static TaskResult tr = null;
	/** �����Բ�ѯ���ݽ������򣬴˴���Ϊ�˺�sql���ƴ�� */
	private final String order = "FBSJ desc";
	/** ʵ����չʾ�б��ʱ����ʾ�ı������� */
	private final String TitleName = "�����б�";
	/** ����һ�����ڷ��÷���ArrayList<HashMap<String, Object>>�������ݵļ��� */
	private ArrayList<HashMap<String, Object>> dataList;

	/* ����״̬����ֵ */
	/** ���·� */
	public final static String RWZT_WATE_YIXIAFA = "";
	/** ���·� */
	public final static String RWZT_WAIT_DISPATCH = "000";
	/** ��ִ�� */
	public final static String RWZT_WATE_EXECUTION = "003";
	/** ִ���У��ֻ��ˣ� */
	public static final String RWZT_ON_EXECUTION = "005";
	/** ִ����� */
	public static final String RWZT_EXECUTION_FINISH = "006";
	/** ����� */
	public static final String RWZT_WAIT_AUDIT = "007";
	/** ���δͨ�� */
	public static final String RWZT_AUDIT_NOPASSED = "008";
	/** ������� */
	public static final String RWZT_ON_FINISH = "009";
	/** �˻����� */
	public static final String RWZT_ON_RETURN = "010";
	/** ���鵵 */
	public static final String RWZT_WAIT_FILE = "011";
	/** �ѹ���*/
	public static final String RWZT_YGQ = "012";
	
	/** ���ϱ�*/
	public static final String RWZT_DSB = "013";

	/** ����ȡ֤��� */
	// public final static String TaskEnpriLink_isexcute = "1";
	public final static String TaskEnpriLink_isexcute = "3";
	/** ���������ҵ���ϴ� */
	public static final String TaskEnpriLink_UpLoad = "009";

	/* �������ͳ���ֵ */

	// ��������̶ȣ����ݲ�ͬ�Ľ����̶�����
	/** һ�� */
	public final static String JJCD_YB = "001";
	/** ���� */
	public final static String JJCD_JJ = "002";
	/** �ǳ����� */
	public final static String JJCD_FCJJ = "003";

	// ������Դ
	/** �ֳ�ִ����Դ */
	public final static String XCZF_LY = "010";
	/** һ����Դ */
	public final static String YBRW_LY = "011";

	// �����ϴ����ֵ

	/** �ϴ����� ��ͨ */
	private static final int NONET = 0;
	/** �ϴ��ɹ� */
	private static final int UPLOAD_SUCCESS = 1;
	/** �ϴ�ʧ�� */
	private static final int UPLOAD_FAIL = 2;
	/** �����Ѿ��ϴ� */
	private static final int ALREADY_UPLOAD = 3;

	private static final int PROGRESS = 5;

	private static final int UPLOAD_FILE_SUCCESS = 6;// �ļ��ϴ��ɹ�

	private int totalSize = 0;
	// ���ú�̨�ϴ���ʶ
	public static boolean isInBackgroundUpload = false;

	/** �������������б���ʾ��ͨ���ݹ�Ѱ�ҵ���ǰ�û�Ȩ���µ������б��ü��Ϸ��ò���id */
	ArrayList<String> parentlist = new ArrayList<String>();

	/**
	 * ��ȡ�����б������ѯ���---������rwxxUser��--�����˻�ִ���˱���Ϊ��ǰ�û� ����PC����user����ѯִ������Ϣ
	 */

	private String loadDataSql = "select distinct(a.RWBH),a.Guid,a.RWMC,a.FBSJ,a.RWZT,a.JJCD,a.BJQX,a.BZ,c.[U_LoginName] as fbr "
			+ "from T_YDZF_RWXX a "
			+ "left join T_YDZF_RWXX_USER b on b.[RWXXBH]=a.rwbh left join PC_Users c on c.[UserID]=b.UserID where a.RWLY<>'010' "
			+ "and (b.[UserID]= '"
			+ Global.getGlobalInstance().getUserid()
			+ "') ";
	NotificationManager manager;
	NotificationCompat.Builder builder;
	boolean doInBackground;// ��̨���ر�־
	boolean isActivityAlive = true;// �жϵ�ǰ������Activity�Ƿ���

	public void setCurrentActivityIsAlive(boolean flag) {
		isActivityAlive = flag;
	}

	/**
	 * Description: ����ʵ�ֲ�ѯ�б��ʱ����з�ҳ��ʾ
	 * 
	 * @return ����һ������ƴ�ӷ�ҳ��ʾsql�����ַ��� String String
	 * @author ����� Create at: 2012-12-3 ����11:13:20
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
	 * @return �ײ��˵������ļ��ڵ�����
	 */
	public static String getBottommenuname() {
		return BottomMenuName;
	}

	/**
	 * ��ȡ��ǰ�û������б�
	 */
	@Override
	public ArrayList<HashMap<String, Object>> getDataList() {

		return SqliteUtil.getInstance().queryBySqlReturnArrayListHashMap(
				loadDataSql + " order by " + order + getOrder());

	}

	/**
	 * ���˻�ȡ��ǰ�û������б� fliterHashMap ��������
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
	 * ��ȡ�б���ʾ��ʽ
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
	 * ��ȡ��������
	 */
	@Override
	public HashMap<String, Object> getDetailed(String id) {
		HashMap<String, String> primaryKeyMap = new HashMap<String, String>();
		primaryKeyMap.put("key", primaryKey);
		primaryKeyMap.put("keyValue", id);
		return BaseClass.DBHelper.getDetailed(TableName, primaryKeyMap);
	}

	/**
	 * ��ȡ����������ʾ��ʽ
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
	 * Description: �ϴ����񸽼���������Ϣ
	 * 
	 * 
	 * @param taskIdFloder
	 *            ѹ���ļ���·��
	 * @param context
	 *            ������
	 * @return �ϴ������ķ��ؽ�� 1���ɹ���������ʧ�� String
	 * @author ��˼Զ Create at: 2012-12-4 ����11:19:20
	 */
	public String synchronizeUploadData(String taskIdFloder, final String RWBH,
			final String QYID, final Context context,
			final int mSingleChoiceID, final boolean isComplete, final int fType) {
		ArrayList<TaskFile> _ListAllFile = getAllUploadFile(RWBH, QYID,
				taskIdFloder);
		Log.i(TAG, "_ListAllFile--->>>" + _ListAllFile);

		final ArrayList<TaskFile> listAllFile = new ArrayList<RWXX.TaskFile>();
		/** ���������ļ� ȡ��û���ϴ����ĸ��� */
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
					.setTitle("��ʾ")
					.setMessage("������С����30M������ʹ��WIFI�ϴ����Ƿ��˳��ϴ���")
					.setPositiveButton("��", null)
					.setNegativeButton("��",
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
		/** �����Ƿ��Ѿ��ϴ��� */
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
			// ��õ�ǰ��¼�û���ְ��
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
				builder.setContentTitle("���񸽼��ϴ�")
						.setContentText("����׼���ϴ����񸽼�...")
						.setSmallIcon(R.drawable.yutu);
				manager.notify(0, builder.build());
				doInBackground = true;
				OtherTools
						.showToast(TaskUploadAsync.this.mcontext, "������ת���̨�ϴ�");
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
						sendNotification(fileName + " �Ѿ��ϴ�", values[0]);
						isUpload = false;
					} else {
						if (isActivityAlive) {

						}
						sendNotification(fileName + " �ϴ��ɹ�", values[0]);
						if (index == listAllFile.size() - 1) {// �����Ѿ��ϴ����
							if (isActivityAlive) {
							}
							sendNotification("�����ϴ�������Ϣ...", values[0]);
						}

					}
				} else {
					if (isActivityAlive) {
						pdialog.setProgress(values[0]);
					}
					sendNotification(fileName + " �����ϴ�...", values[0]);
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
				String serverTime = DisplayUitl.getServerTime();// ���ȸ��¸������еĸ���ʱ��
				if (serverTime != null && !serverTime.equals("")) {
					for (TaskFile taskFile : listAllFile) {// ����updatetime
						// ȷ���������ܹ�ͬ��������
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

					return "���������쳣�������������ú����ԣ�";
				}

				HttpUploader httpUploader = new HttpUploader();

				httpUploader.setWebServiceUrl(Global.getGlobalInstance()
						.getSystemurl() + Global.WEBSERVICE_URL);
				if (listAllFile.size() != 0) {
					// ��ǰ�������BYK
					addBL("�ֳ�ȡ֤����", rwbh, qyid);
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

					int finishblocks = 0;// �ϵ����

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
							return "��ȡ�����ϵ���Ϣ�쳣";
						}
						if (finishblocks == -1) {
							return "��ȡ�����ϵ���Ϣʧ��";
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
							 * �ϴ������Ĳ��� BYK
							 * */
							Boolean resultBoolean = httpUploader
									.upLoadAffixMethod(AttachmentJosn,
											attachmentData, isEnd);
							if (resultBoolean) {
								index = n;
								publishProgress((i + 1) * 100 / totalblocks);

							} else {
								return "�ϴ�����ʧ��,���������Ƿ�������";
							}

						}

					} catch (Exception e) {
						if (e.getMessage() != null) {
							Log.e(TAG, e.getMessage());
						}

						return "�ϴ����������쳣";
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
			// �ϴ�������Ϣ BYK
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

					result = "�����ϴ��ɹ�";
				} else {

					result = "�����ϴ�ʧ��";
				}
	

			} else {
				result = "�����ϴ�ʧ��";
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
		// ��ѯ��ȡ ����
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
		if (_Result != 0) {// ��������״̬�иı䣬֪ͨˢ�������б�
			Global.getGlobalInstance().setTaskStateChange(true);
			DataSyncModel dm = new DataSyncModel();
			// TODO �����޸����� �� ͬ��������
			// dm.syncServiceData(tables, true);
			OtherTools.showLog("T_YDZF_RWXX,������Ϣ����³ɹ�");
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
	 * ����ִ������ύ
	 */
	/**
	 * ����Webservice
	 * */
	public Boolean requestTaskExecuteFinish_byk(String rwbh) {
		Boolean resultBoolean = false;
		if (Net.checkURL(Global.getGlobalInstance().getSystemurl())) {
			String methodName = "MobileTaskExecute";

			ArrayList<HashMap<String, Object>> params = new ArrayList<HashMap<String, Object>>();
			HashMap<String, Object> param = new HashMap<String, Object>();

			// �������
			ArrayList<HashMap<String, Object>> data_json = new ArrayList<HashMap<String, Object>>();
			HashMap<String, Object> map = new HashMap<String, Object>();

			map.put("FromChannel", "2");
			map.put("RegionCode", Global.getGlobalInstance().getAreaCode());
			// ����id
			// map.put("CurrentFlowTaskId",getTaskStatus(rwbh));
			map.put("CurrentFlowTaskId", rwbh);
			map.put("TransactorName", Global.getGlobalInstance().getUserid());
			map.put("TransactionType", "29");
			map.put("Comment", "�������");
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
	 * �ϴ������������ҵ��Ϣ�����������ԴΪ�ֳ�ִ�����ϴ�������Ϣ���ı�����״̬���ϴ�֮����±������ݿ�
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
		tables.add("TaskEnpriLink");// TaskEnpriLink����״̬���� �ı䣬��Ҫͬ��һ��
		tables.add("T_YDZF_RWXX");// T_YDZF_RWXX����״̬���� �ı䣬��Ҫͬ��һ��
		// ͬ��һ�������ű�
		DataSyncModel dm2 = new DataSyncModel();

	//	dm2.syncServiceData(tables, true);

		JSONArray _JsonTaskEntprilink = GetEntpriLinkItems(RWBH, qyid,
				isComplete);
		JSONArray _JsonKCBL = GetKCBLItems(RWBH, qyid);
		JSONArray _JsonXWBL = GetXWBLItems(RWBH, qyid);
		JSONArray _JsonWD = GetWDItems(RWBH, qyid);
		JSONArray _JsonJSBL = GetJSTZSItems(RWBH, qyid);
		JSONArray _JsonXCBL = GetXCHJItems(RWBH, qyid);
		// addBL("�ֳ�ȡ֤����", RWBH, qyid);
		JSONArray _JsonExeLawsTemplet = GetExeLawsTempletItems(RWBH, qyid);
		String _JsonStr = "";
		try {
			/** ��ü����ַ��� */
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
		if (rwly.equals("010")) {// �ֳ�ִ������,�ϴ��ɹ�֮��ֱ�Ӹ�������״̬Ϊ�����
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
		if (_Result != 0) {// ��������״̬�иı䣬֪ͨˢ�������б�
			Global.getGlobalInstance().setTaskStateChange(true);
			DataSyncModel dm = new DataSyncModel();
			dm.syncServiceData(tables, true);
			OtherTools.showLog("T_YDZF_RWXX,������Ϣ����³ɹ�");
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
	 * ����ֳ�ȡ֤����
	 * 
	 * @param name
	 *            ȡ֤����
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
		contentValues.put("taskid", rwbh);// ����ID
		contentValues.put("enterid", qyid);// ��ȾԴ(��ҵ)ID
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
		contentValues.put("taskid", rwbh);// ����ID
		contentValues.put("enterid", qyid);// ��ȾԴ(��ҵ)ID
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
	 * ��ȡ�ֳ�ȡ֤����
	 * 
	 * @return
	 */
	private ArrayList<HashMap<String, Object>> getKCData(String rwbh,
			String qyid) {

		ArrayList<HashMap<String, Object>> data = new ArrayList<HashMap<String, Object>>();

		/** ��ѯ����¼���е����� */
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
	 * ��ȡ�嵥����
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
	 * �ϴ������������ҵ��Ϣ�����������ԴΪ�ֳ�ִ�����ϴ�������Ϣ���ı�����״̬���ϴ�֮����±������ݿ�
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
		tables.add("TaskEnpriLink");// TaskEnpriLink����״̬���� �ı䣬��Ҫͬ��һ��
		tables.add("T_YDZF_RWXX");// T_YDZF_RWXX����״̬���� �ı䣬��Ҫͬ��һ��
		JSONArray _JsonTaskEntprilink = GetEntpriLinkItems(RWBH);
		;
		JSONArray _JsonKCBL = jsonarray;
		JSONArray _JsonXWBL = GetXWBLItems(RWBH);
		JSONArray _JsonWD = GetWDItems(RWBH);
		JSONArray _JsonJSBL = jsonarray;
		JSONArray _JsonXCBL = jsonarray;
		String _JsonStr = "";
		try {
			/** ��ü����ַ��� */
			_JsonStr = DESSecurity.encrypt(methodName);
		} catch (Exception e1) {
			e1.printStackTrace();
		}

		HashMap<String, Object> _HashMap = new HashMap<String, Object>();
		if (rwly.equals("013")) {// ����ִ������,�ϴ��ɹ�֮��ֱ�Ӹ�������״̬Ϊ�����
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
		if (_Result != 0) {// ��������״̬�иı䣬֪ͨˢ�������б�
			Global.getGlobalInstance().setTaskStateChange(true);
			DataSyncModel dm = new DataSyncModel();
			dm.syncServiceData(tables, true);
		}
		return _Result;

	}

	/**
	 * Description: ��ȡ����ִ����
	 * 
	 * @param _TaskId
	 *            ������
	 * @return ����һ�����������ִ���˵���Ϣ����json�����ʽ���� JSONArray
	 * @author ��˼Զ Create at: 2012-12-4 ����11:36:33
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
	 * ��ȡ���и��������ִ����ҵδ�ϴ�����Ϣ
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
	 * Description: ��ȡ������Ϣ
	 * 
	 * @param _TaskId
	 *            ����id
	 * @param state
	 *            ����״̬��upload�ϴ���download�·�
	 * @return ������Ϣ��jsonObject���ͷ��� JSONObject
	 * @author ��˼Զ Create at: 2012-12-4 ����11:37:56
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
					// �޸��ϴ�����ʱ���ϴ��Ĳ��� BYK
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
						_JsonObject.put(_Key, rwzt);// �޸�����״̬Ϊ�����
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

			// BYK ��ȡbillid

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
	 * Description: ��ȡר������
	 * 
	 * @param _TaskId
	 *            ������
	 * @return ר����Ϣ��json������ʽ���� JSONArray
	 * @author ��˼Զ Create at: 2012-12-4 ����11:40:26
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
	 * Description: ��ȡ����ִ������������Ϣ
	 * 
	 * @param _TaskId
	 *            ��ҵ����
	 * @return ����������ҵ����{JSONArray} JSONArray
	 * @author ���� Create at: 2013-08-09����15:04:55
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

						_JsonObjectData.put(_Key, "3");// �޸���ҵ״̬���ϴ�

						// BYK rwzt
						// _JsonObjectData.put(_Key, "1");// �޸���ҵ״̬���ϴ�
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
	 * Description: ��ȡ������ҵ������Ϣ
	 * 
	 * @param _TaskId
	 *            ��ҵ����
	 * @return ����������ҵ����{JSONArray} JSONArray
	 * @author ���� Create at: 2013-08-09����15:04:55
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
							// _JsonObjectData.put(_Key, "1");// �޸���ҵ״̬���ϴ�
							_JsonObjectData.put(_Key, "3");// �޸���ҵ״̬���ϴ�
						} else {
							_JsonObjectData.put(_Key, "2");// �޸���ҵ״ִ̬����
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
	 * Description: ��ȡ��������ص�������ҵ��Ϣ
	 * 
	 * @return ����������ҵ����{JSONArray} JSONArray
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
						_JsonObjectData.put(_Key, "2");// �޸�����״̬Ϊִ����

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
	 * Description: ��ȡ�����¼��Ϣ
	 * 
	 * @param _TaskId
	 * 
	 * @return ���ؿ����¼��Ϣ{JSONArray} JSONArray
	 * @author ���� Create at: 2013-08-09����16:17:55
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
	 * Description: ��ȡExeLawsTemplet�����Ϣ
	 * 
	 * @param _TaskId
	 * 
	 * @return
	 * @author ���� Create at: 2013-08-09����16:17:55
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
	 * ��ȡbillid BYK
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
	 * ��ȡ"���ܵ���֪ͨ��"��¼json
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
	 * ��ȡ"�ֳ���������¼"��¼json
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
					// ��������״̬������
					if (entry.getKey().toString().equals("scqk_sczk")
							|| entry.getKey().toString().equals("SCQK_SCZK")) {
						String value = entry.getValue().toString();

						if (value.equals("��������")) {
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
	 * ��ȡѯ�ʱ�¼json
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
	 * ��ȡ����ִ��ѯ�ʱ�¼json
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
	 * ��ü���ִ��ѯ�ʱ�¼json����
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
	 * Description: ��ȡ��ǰ���������ϴ��ļ�
	 * 
	 * @param _Path
	 *            �ļ�·��
	 * @param _TaskId
	 *            ������
	 * @param _Context
	 *            ������
	 * @return ���ظ�·���������ļ� ArrayList<File>
	 * @author ��˼Զ Create at: 2012-12-4 ����11:46:48
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
	 * ��ȡ��ѯ��ʽ
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
	 * Description: ����������Ϣ��������Ϣ
	 * 
	 * @param rwid
	 *            �����id
	 * @param lm
	 *            Ҫ���ص�ĳ�������ֶ�ֵ
	 * @return ���ظ�������ĸ��ֶε�������Ϣ String
	 * @author ��˼Զ Create at: 2012-12-4 ����11:48:21 wanglg �޸�10��2��
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
	 * Description: ͨ���û���Ų�ѯ���û���ĳһ״̬���� ��ִ����������
	 * 
	 * @param userID
	 *            �û����
	 * @param status
	 *            ����״̬
	 * @return ������Ϣ��� ArrayList<HashMap<String,Object>>
	 * @author ��˼Զ Create at: 2012-12-4 ����11:49:42
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
	 * Description: ��ȡ�ֳ�ִ��������
	 * 
	 * @param userID
	 *            �û����
	 * @param status
	 *            ��������
	 * @return ���ظ��û����ֳ�ִ����������Ϣ ArrayList<HashMap<String,Object>>
	 * @author ��˼Զ Create at: 2012-12-4 ����11:50:19
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
	 * Description: ��ѯ��ǰ����״̬
	 * 
	 * @param rwID
	 *            ������
	 * @return ���������״̬ String
	 * @author ��˼Զ Create at: 2012-12-4 ����11:51:49
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
	 * ��ѯ��ǰ������Դ
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
	 * ���������ź���ҵid��ø������µ�ĳ��ҵ��ִ��״̬
	 * 
	 * @param RWBH
	 * @param qyid
	 * @return 0����ִ�� 1������� 2��ִ����
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
	 * Description: �����½�����
	 * 
	 * @param rwbh
	 *            ������
	 * @param issave
	 *            true�����·���false����ִ��
	 * @param guid
	 *            �����id
	 * @param cont
	 *            1�������� ��ţ�������û������ ���
	 * @param bz
	 *            ��ע
	 * @param qymc
	 *            ��ҵ����
	 * @param rwmc
	 *            ��������
	 * @param ssks
	 *            ��������
	 * @param useridStr
	 *            ����������ĵ��û�id���Զ��ŷָ�
	 * @param tid
	 *            ģ��id
	 * @param info
	 *            ����״̬��save��һ������xiafa�����·�,xczf���ֳ�ִ������
	 * @param isreply
	 *            �Ƿ�ظ�
	 * @return �������� ��ţ��ɹ������ء�����ʧ�� String
	 * @author ��˼Զ Create at: 2012-12-4 ����11:52:20
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
				ssks, rwly, tid, guid, info, isreply, rwlx);// ����rwxx��
		boolean j = insertUserRwxx(cont, useridStr, bz, rwbhStr);// ���������û���ϵ��
		boolean m = true;
		/*
		 * if (dwld != null && !dwld.trim().equals("")) { m = insertRWLC(cont,
		 * rwbhStr, dwld);// ����rwlc�������̱� }
		 */

		// UUID uuid = UUID.randomUUID();
		// String s = UUID.randomUUID().toString();
		// boolean k = insertEnpriLinkRwxx(cont, qyidStr, bz, rwbhStr);//
		// ����������ҵ��ϵ��
		boolean k = true;// ����������ҵ��ϵ��

		if (qyidStr != null && !qyidStr.equals("")) {
			k = insertEnpriLinkRwxx(cont, qyidStr, bz, rwbhStr);// ����������ҵ��ϵ��
			insertTaskSpecialItem(cont, rwbhStr, qyidStr, tid);
		}

		if (i && j && k && m) {
			Global.getGlobalInstance().setTaskStateChange(true);// ����һ������ˢ�������б�
			return rwbhStr;

		} else
			return "";
	}

	/**
	 * Description: ����������
	 * 
	 * @return ���������� String
	 * @author ��˼Զ Create at: 2012-12-4 ����11:58:05
	 */
	public String returnRWBH() {
		SimpleDateFormat formatdate = new SimpleDateFormat("yyyyMMddHHmmssSSSS");
		/** ���ɹ��� */
		String time = "T" + formatdate.format(new Date());
		return time;
	}

	/**
	 * Description: �������
	 * 
	 * @param issave
	 *            true�����·���false����ִ��
	 * @param cont
	 *            1�������и���������Ҫɾ���ٲ���
	 * @param qymc
	 *            ��ҵ����
	 * @param bz
	 *            ��ע
	 * @param rwmc
	 *            ��������
	 * @param rwbhStr
	 *            ������
	 * @param ssks
	 *            ��������
	 * @param tid
	 *            ģ��id
	 * @param guid
	 *            ����id
	 * @param info
	 *            ����״̬��save��һ������xiafa�����·�,xczf���ֳ�ִ������
	 * @param isreply
	 *            �Ƿ�ظ�
	 * @return true:����ɹ�����,false:����ʧ�� boolean
	 * @author ��˼Զ Create at: 2012-12-4 ����11:58:55
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
	 * Description: ��T_YDZF_RWXX_USER���������
	 * 
	 * @param cont
	 *            1:������������Ҫɾ���ٲ���
	 * @param useridStr
	 *            �û���id���Զ��ŷָ�
	 * @param bz
	 *            ��ע
	 * @param rwbhStr
	 *            ������
	 * @return true:�ɹ���false��ʧ�� boolean
	 * @author ��˼Զ Create at: 2012-12-4 ����12:01:16
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
	 * Description: ��TaskEnpriLink���������
	 * 
	 * @param cont
	 *            1:������������Ҫɾ���ٲ���
	 * @param qyidStr
	 *            ��ҵ��id���Զ��ŷָ�
	 * @param bz
	 *            ��ע
	 * @param rwbhStr
	 *            ������
	 * @return true:�ɹ���false��ʧ�� boolean
	 * @author ������ Create at: 2013-8-10 ����10:01:16
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
	 * Description: ������Ϣר��YDZF_TaskSpecialItem
	 * 
	 * @param cont
	 *            1:������������Ҫɾ���ٲ���
	 * @param rwbhStr
	 *            ������
	 * @param qyid
	 *            ��ҵid
	 * @param tid
	 *            ģ��id
	 * @returntrue:�ɹ���false��ʧ�� boolean
	 * @author ��˼Զ Create at: 2012-12-4 ����01:20:35
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
	 * Description: ���������½���Ϣ
	 * 
	 * @param issave
	 *            true�����·���false����ִ��
	 * @param qymc
	 *            ��ҵ����
	 * @param rwmc
	 *            ��������
	 * @param bz
	 *            ��ע
	 * @param rwbhStr
	 *            ������
	 * @param ssks
	 *            ��������
	 * @param tid
	 *            ģ��id
	 * @param guid
	 *            ����id
	 * @param info
	 *            �������ͣ�save��һ������xiafa�����·�,xczf���ֳ�ִ������
	 * @param isreply
	 *            �Ƿ�ظ�
	 * @return ����Ϣת��ΪContentValues��ʽ ContentValues
	 * @author ��˼Զ Create at: 2012-12-4 ����01:22:26
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
	 * Description: ѭ���������ص�userid�ַ������Զ��ŷָ������β嵽T_YDZF_RWXX_USER
	 * 
	 * @param useridStr
	 *            �Զ��ŷָ��Զ��ŷָ����û�id��
	 * @param rwbhStr
	 *            ������
	 * @param bz
	 *            ��ע
	 * @return ����������û��Ĺ�ϵ���ݣ�rwxxbh��userid��bz��updatetime ArrayList<ContentValues>
	 * @author ��˼Զ Create at: 2012-12-4 ����01:29:43
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
	 * Description: ѭ���������ص�qyidStr�ַ������Զ��ŷָ������β嵽TaskEnpriLink
	 * 
	 * @param qyidStr
	 *            �Զ��ŷָ���ҵid��
	 * @param rwbhStr
	 *            ������
	 * @param bz
	 *            ��ע
	 * @return �����������ҵ�Ĺ�ϵ���ݣ�rwxxbh��qyidStr��bz��updatetime
	 *         ArrayList<ContentValues>
	 * @author ������ Create at: 2013-8-10 ����10:10:43
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
	 * ��������״̬
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
	 * ��������������״̬
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
	 * ��������������״̬
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
	 * Description: ͨ�������ţ���ѯ����id
	 * 
	 * @param rwbh
	 *            ������
	 * @return ����id�ַ��� String
	 * @author ��˼Զ Create at: 2012-12-4 ����01:32:53
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
	 * Description: ͨ�������š���ҵID����ѯִ�����
	 * 
	 * @param rwbh
	 *            ������ qyid ��ҵID
	 * @return isExcute�ַ��� String
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
	 * Description: ͨ�������ţ���ѯ����״̬
	 * 
	 * @param taskID
	 *            ������
	 * @return ����״̬�ַ��� String
	 * @author ��˼Զ Create at: 2012-12-4 ����01:32:53
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
	 * Description: ͨ����ҵid����ѯ����ҵ����״̬
	 * 
	 * @param rwbh
	 *            ������
	 * @param qyid
	 *            ��ҵid
	 * @return ��ҵ����״̬�ַ��� String
	 * @author ����� Create at: 2013-8-12 ����9:15:53
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
	 * ͨ��guid��ѯ������
	 * 
	 * @param guid
	 * @return ������
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
	 * Description: �ж������Ƿ�Ϊ�Լ��������������������
	 * 
	 * @param rwbh
	 *            ������
	 * @return ture���ǣ�false������ boolean
	 * @author ��˼Զ Create at: 2012-12-4 ����01:33:59
	 */
	public boolean JudgeUserName(String rwbh) {

		// ArrayList<HashMap<String, Object>> list =
		// SqliteUtil.getInstance().queryBySqlReturnArrayListHashMap(
		// "select a.[LoginUserId],a.[AuditUserId] from YDZF_RWLC a where a.[Tid] = '"
		// + rwbh
		// +
		// "' and a.[TaskAction] like '%ִ��%' and a.[NodeId] = (select max(NodeId) from YDZF_RWLC where Tid = '"
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
	 * Description: ���������Ų�ѯ����ִ����
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
	 * Description: ģ���Spinner �ֳ�ִ����������ӡ��������� ����ʹ��
	 * 
	 * @return ���ط�װ�õ�spinner���� List<SpinnerItem>
	 * @author ��˼Զ Create at: 2012-12-4 ����01:34:53
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

	/** �������� **/
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

	/** ��������EditText **/
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
	
	/** ��������EditText **/
	public ArrayList<HashMap<String, Object>> gettaskTypeEditText2() {

		ArrayList<HashMap<String, Object>> type = SqliteUtil.getInstance()
				.queryBySqlReturnArrayListHashMap("select * from T_YDZF_RWLX");

		

		return type;
	}

	/** ������Դ **/
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

	/** ��λ�쵼 **/
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

	/** ��������̶� **/
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
	 * @return ����ͬ���������������� ���-1 ͬ������ʧ��
	 */
	public int getNewTask(Context context) {
		/** �ж�ͬ��ռ��״̬ */

		int result = 0;
		DataSyncModel dataSync = new DataSyncModel();

		String[] tables = { "T_YDZF_RWXX", "T_YDZF_RWXX_USER", "TaskEnpriLink",
				"YDZF_RWLC" };
		for (int i = 0; i < tables.length; i++) {
			if (tables[i].equals("T_YDZF_RWXX")) {
				result = dataSync.synchronizeFetchServerData(true, tables[i]);
				if (result < 0) {// ͬ��ʧ��
					return -1;
				}
			}/*
			 * else if (tables[i].equals("YDZF_RWLC"))
			 * {//����������Ϣͬ������ʱ�кܶ�����ͬ��������������ʱ��Ϊͬ��ȫ��
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
	 * Description:ɸѡ����Ȩ�޵ĵײ��˵�
	 * 
	 * @author Administrator Create at: 2012-12-24 ����10:28:07
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
	 * �ж������Ƿ���
	 * 
	 * @param taskBJQX
	 *            ����������
	 * @return �Ƿ����
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
	 * Description: �ϴ������װ�󷽷���ֻ�贫�������GUID�������Ķ���
	 * 
	 * @param rwid
	 *            ����GUID
	 * @param context
	 * @author wanglg
	 * @Create at: 2013-5-3 ����9:11:43
	 */
	public void uploadTask(final String rwbh, final Context context,
			String qyid, int mSingleChoiceID, boolean isComplete, int fType) {
		String extra_Path = SiteEvidenceActivity.TASK_PATH + "RWZX/";// ����·��
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
	 * Description: ��ȡ�����ִ�����ַ������ö��ŷֿ�
	 * 
	 * @param rwbh
	 *            ������
	 * @return
	 * @author wanglg
	 * @Create at: 2013-7-16 ����4:48:27
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
				sb.append("��");
			}
			sb.deleteCharAt(sb.length() - 1);
			taskExecutor = sb.toString();
		}
		return taskExecutor;

	}

	/**
	 * ��ȡ����ִ���˵�id����
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
	 * ��ȡ�������ҵ�Ĺ�����ϵ��id����
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
	 * ��ȡ�������ͼ���
	 * */
	public ArrayList<HashMap<String, Object>> getTask_type() {
		String sql = "select RegionName,RegionCode,ParentCode from Region where ParentCode='230000000'";
		ArrayList<HashMap<String, Object>> groupData = SqliteUtil.getInstance()
				.queryBySqlReturnArrayListHashMap(sql);
		return groupData;
	}

	/**
	 * ��ȡ�������ͼ���
	 * */
	public ArrayList<HashMap<String, Object>> getTask_type2() {
		String sql = "select name,code from  T_YDZF_RWLX where pcode='' or pcode=' ' or pcode=null order by code";
		ArrayList<HashMap<String, Object>> groupData = SqliteUtil.getInstance()
				.queryBySqlReturnArrayListHashMap(sql);
		return groupData;
	}
	
	/**
	 * ��ȡ��Ա����� ���ݵ�ǰ��Ա����id
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
	 * ��ȡ�������ͼ����Ӽ�
	 * */
	public ArrayList<ArrayList<HashMap<String, Object>>> getTask_type_child(
			ArrayList<HashMap<String, Object>> groupData) {
		final ArrayList<ArrayList<HashMap<String, Object>>> childMapData = new ArrayList<ArrayList<HashMap<String, Object>>>();
		if (groupData != null && groupData.size() > 0)
			for (int i = 0; i < groupData.size(); i++) {
				HashMap<String, Object> hashMap = groupData.get(i);
				String tcodeStr = hashMap.get("regioncode") + "";
				/** ��ѯ�����б����ݼ��� */
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
					/** ��ѯ�����б����ݼ��� */
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
	 * ��ȡ�������ͼ����Ӽ�
	 * */
	public ArrayList<ArrayList<HashMap<String, Object>>> getTask_type_child2(
			ArrayList<HashMap<String, Object>> groupData) {
		final ArrayList<ArrayList<HashMap<String, Object>>> childMapData = new ArrayList<ArrayList<HashMap<String, Object>>>();
		if (groupData != null && groupData.size() > 0)
			for (int i = 0; i < groupData.size(); i++) {
				HashMap<String, Object> hashMap = groupData.get(i);
				String tcodeStr = hashMap.get("code").toString();
				/** ��ѯ�����б����ݼ��� */
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
	 * �������б��������
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
			if(childMapData.get(groupPosition).get(childPosition).get("regionname").toString().equals("��Ͻ��")){
				holder.two_class_interface_name_tv.setText(groupList.get(
						groupPosition).toString());
				convertView.setTag("1");//ȡ���ڵ�code
			}else {
				convertView.setTag("0");//ȡ���ڵ�code
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
	 * �������б��������
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
	 * �������б��������
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
	 * �������б��������
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
	 * �������б��������
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

	// �����·�������
	public void tasksIssued(String guid, String rwbh, String fjPath,
			Context context) {

		tr = new TaskResult();

		ArrayList<String> _ListFile = getUploadFile(rwbh, context);
		ArrayList<File> _ListAllFile = getAllUploadFile(fjPath, context);
		tr.filenum = _ListAllFile.size();

		// ���������ļ�
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

		int filenum = 0;// ���ϴ��ļ���
		int fileuploadnum = 0;// ���ϴ��ļ���
		boolean istaskdataupload = false;// ���������Ƿ��ϴ�
	}

	class UpLoadFJ extends AsyncTask<String, Integer, String> {

		// �ɱ䳤�������������AsyncTask.exucute()��Ӧ
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
			 * mProgressDialog.setTitle(filename + "�����ϴ�����...");
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
			// mProgressDialog.setMessage(absFileName + "�����ϴ�����...");
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
					// nm.notify("�����ϴ�"+i+"��", 0, null);
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

						JSONArray _JsonFJ = GetAnnexTable(mGuid, i);// ���T_Attachment(����)���е�����

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
							msg.obj = "Webservice�쳣";
							if (handler != null) {
								handler.sendMessage(msg);
							}
							break;
						}
					}
				}

				Log.v(TAG, absFileName + "\t�ɹ�ת��ΪBase64 Bytes");
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
			// ����HTMLҳ�������
			// mProgressDialog.dismiss();
			TaskRegisterActivity tea = new TaskRegisterActivity();
			tea.finish();
		}

		@Override
		protected void onPreExecute() {
			// ����������������������ʾһ���Ի�������򵥴���

		}

		@Override
		protected void onProgressUpdate(Integer... values) {
			// ���½���
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
				Toast.makeText(mcontext, "���粻ͨ�������������ã�", Toast.LENGTH_LONG)
						.show();
				break;

			case UPLOAD_FILE_SUCCESS:
				/*
				 * Toast.makeText(mcontext, msg.obj.toString() + "�����ϴ��ɹ�",
				 * Toast.LENGTH_LONG).show();
				 */
				break;

			case UPLOAD_SUCCESS:
				// �ϴ��ɹ�������״̬003 ���� YK
				ContentValues cv = new ContentValues();
				cv.put("rwzt", "003");
				try {
					int state = SqliteUtil.getInstance().update("T_YDZF_RWXX",
							cv, "rwbh=?", new String[] { msg.obj.toString() });
				} catch (Exception e) {
					ExceptionManager.WriteCaughtEXP(e, "TaskListActivity");
					e.printStackTrace();
				}
				Toast.makeText(mcontext, "�����ϴ��ɹ�", Toast.LENGTH_LONG).show();

				break;
			case UPLOAD_FAIL:
				if (msg.obj != null) {
					String reason = msg.obj.toString();
					Toast.makeText(mcontext, reason, Toast.LENGTH_SHORT).show();
				}
				break;
			case ALREADY_UPLOAD:
				Toast.makeText(mcontext, "�������ϴ��������ظ��ϴ���", Toast.LENGTH_SHORT)
						.show();
				break;
			}
		};
	};

	/**
	 * Description: ��ȡ���ϴ����ļ�
	 * 
	 * @param RWBH
	 *            ������
	 * @param _Context
	 *            ������
	 * @return ���ϴ��ļ�������ArrayList<String> ArrayList<String>
	 * @author ��˼Զ Create at: 2012-12-4 ����11:44:30
	 */
	private ArrayList<String> getUploadFile(String RWBH, Context _Context) {
		/** �������ϴ������ݼ��� */
		ArrayList<String> _ListFile = new ArrayList<String>();

		/** �������� */
		SharedPreferences _SharedPreferences = _Context.getSharedPreferences(
				RWBH, _Context.MODE_WORLD_READABLE);

		/** ��ȡ���ϴ������� */
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
	 * Description: ��ȡ��ǰ���������ϴ��ļ�
	 * 
	 * @param _Path
	 *            �ļ�·��
	 * @param _TaskId
	 *            ������
	 * @param _Context
	 *            ������
	 * @return ���ظ�·���������ļ� ArrayList<File>
	 * @author ��˼Զ Create at: 2012-12-4 ����11:46:48
	 */
	public ArrayList<File> getAllUploadFile(String _Path, Context _Context) {
		ArrayList<File> _ListFile = new ArrayList<File>();

		/** ���û���ļ�ֱ�ӷ��� */
		File _File = new File(_Path);
		if (!_File.exists()) {
			return _ListFile;
		}
		FileHelper fileHelper = new FileHelper();
		fileHelper.getAbsFiles(_File, _ListFile);

		return _ListFile;
	}

	/**
	 * Description: ��ȡT_Attachment��������Ϣ
	 * 
	 * @param guid
	 *            (T_Attachment ����Guid)
	 * 
	 * @return ���ؿ����¼��Ϣ{JSONArray} JSONArray
	 * @author ���� Create at: 2013-08-09����16:17:55
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
