package com.mapuni.android.attachment;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.util.Base64;
import android.widget.Toast;

import com.mapuni.android.base.Global;
import com.mapuni.android.base.util.DisplayUitl;
import com.mapuni.android.base.util.LogUtil;
import com.mapuni.android.dataprovider.SqliteUtil;
import com.mapuni.android.netprovider.HttpUploader;
import com.mapuni.android.netprovider.WebServiceProvider;

public class UploadFile {
	/** 所有附件的父目录 */
	public static final String ATTACHMENTPATH = Global.SDCARD_RASK_DATA_PATH + "Attach/";
	/** 回调handler 的what值 */
	public static final int callBackCode = -1;
	Handler asyncCallBack;

	public void upLoadFilesMethod(ArrayList<TaskFile> fileList, Handler asyncCallBack, Context context) {
		this.asyncCallBack = asyncCallBack;
		TaskUploadAsync async = new TaskUploadAsync(fileList, context);
		async.execute();

	}

	class TaskUploadAsync extends AsyncTask<Object, Integer, Object> {

		ArrayList<TaskFile> listAllFile;
		/** 上传附件进度条 */
		ProgressDialog pdialog;
		private int index;
		/** 附件是否已经上传过 */
		Boolean isUpload = false;
		private Context mcontext;

		TaskUploadAsync(ArrayList<TaskFile> listAllFile, Context mcontext) {

			this.listAllFile = listAllFile;
			this.mcontext = mcontext;
			pdialog = new ProgressDialog(mcontext, 0);
			pdialog.setCancelable(false);
			pdialog.setMax(100);
			pdialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);

		}

		@Override
		protected void onCancelled() {
			super.onCancelled();
		}

		@Override
		protected void onPostExecute(Object result) {
			pdialog.cancel();
			if (null != result && !"".equals(result))
				Toast.makeText(mcontext, (CharSequence) result, Toast.LENGTH_SHORT).show();
		}

		@Override
		protected void onPreExecute() {
			if (listAllFile == null || listAllFile.size() == 0) {
				Toast.makeText(mcontext, "没有选择附件！", Toast.LENGTH_SHORT).show();
			} else {
				String fileName = listAllFile.get(index).getFileName() + listAllFile.get(index).getExtension();
				pdialog.setMessage(fileName + " 正在上传...");
				pdialog.show();
			}

		}

		protected void onProgressUpdate(Integer... values) {
			String fileName = listAllFile.get(index).getFileName() + listAllFile.get(index).getExtension();
			if (values[0] == 100) {
				if (isUpload) {
					pdialog.setMessage(fileName + " 已经上传");

					isUpload = false;
				} else {
					pdialog.setMessage(fileName + " 执行成功");
					if (index == listAllFile.size() - 1) {// 附件已经上传完毕
						pdialog.cancel();
						/*
						 * pdialog = new ProgressDialog(mcontext);
						 * pdialog.setCancelable(false);
						 * pdialog.setMessage("正在上传任务信息，请稍候！"); pdialog.show();
						 */
					}
				}

			} else {
				pdialog.setMessage(fileName + " 正在上传...");

			}
			pdialog.setProgress(values[0]);
		}

		@Override
		protected Object doInBackground(Object... params) {
			String serverTime = DisplayUitl.getServerTime();// 首先更新附件表中的更新时间
			if (serverTime != null && !serverTime.equals("")) {
				for (TaskFile taskFile : listAllFile) {// 更新updatetime
														// 确保其他人能够同步此数据
					String guid = taskFile.getGuid();
					ContentValues updateValues = new ContentValues();
					updateValues.put("UpdateTime", serverTime);
					String[] whereArgs = { guid };
					try {
						SqliteUtil.getInstance().update("T_Attachment", updateValues, "guid=?", whereArgs);
					} catch (FileNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				}
			} else {
				return "网络连接异常，请检查网络设置后重试！";
			}
			HttpUploader httpUploader = new HttpUploader();

			httpUploader.setWebServiceUrl(Global.getGlobalInstance().getSystemurl() + Global.WEBSERVICE_URL);

			for (int n = 0; n < listAllFile.size(); n++) {

				TaskFile taskFile = listAllFile.get(n);

				ArrayList<LinkedHashMap<String, Object>> params0 = new ArrayList<LinkedHashMap<String, Object>>();
				LinkedHashMap<String, Object> param0 = new LinkedHashMap<String, Object>();
				String path = T_Attachment.transitToChinese(Integer.valueOf(taskFile.getUnitId())) + "/" + taskFile.getFilePath();
				param0.put("path", path);
				param0.put("fileGuid", taskFile.getGuid());
				params0.add(param0);

				int finishblocks = 0;// 断点包数

				try {
					Object resultResponseObj0 = WebServiceProvider.callWebService(Global.NAMESPACE, "GetProgress", params0, Global.getGlobalInstance().getSystemurl()
							+ Global.WEBSERVICE_URL, WebServiceProvider.RETURN_INT, true);
					if (null != resultResponseObj0) {
						finishblocks = Integer.parseInt(resultResponseObj0.toString());
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
					e1.printStackTrace();
				}

				FileInputStream fis = null;

				try {

					File absFile = new File(taskFile.getAbsolutePath());
					fis = new FileInputStream(absFile);

					boolean isEnd = false;

					int totalblocks = (int) absFile.length() / (1024 * 500) + 1;
					for (int i = 0; i < totalblocks; i++) {
						if (i < finishblocks) {
							byte[] buffers = new byte[1024 * 500];
							fis.read(buffers);
							continue;
						}
						String attachmentData = "";
						if (i == totalblocks - 1) {
							isEnd = true;
							byte[] buffers = new byte[(int) absFile.length() % (1024 * 500)];

							fis.read(buffers);
							attachmentData = Base64.encodeToString(buffers, Base64.DEFAULT);

						} else {
							byte[] buffer = new byte[1024 * 500];

							fis.read(buffer);
							attachmentData = Base64.encodeToString(buffer, Base64.DEFAULT);

						}

						String AttachmentJosn = "[" + GetFujian(taskFile, i + "").toString() + "]";

						Boolean resultBoolean = httpUploader.upLoadAffixMethod(AttachmentJosn, attachmentData, isEnd);
						if (resultBoolean) {
							index = n;
							publishProgress((i + 1) * 100 / totalblocks);
						} else {
							return "上传附件失败,请检查网络是否正常！";
						}

					}

				} catch (Exception e) {
					e.printStackTrace();
					return "上传附件出现异常";
				} finally {
					try {
						if (fis != null) {
							fis.close();
						}
					} catch (IOException e) {
					}
				}
			}
			if (asyncCallBack != null) {
				asyncCallBack.sendEmptyMessage(callBackCode);
			}
			return null;

		}

	}

	private JSONObject GetFujian(TaskFile taskFile, String i) {
		JSONObject _JsonObject = new JSONObject();
		HashMap<String, Object> conditions = new HashMap<String, Object>();
		conditions.put("guid", taskFile.getGuid());

		ArrayList<HashMap<String, Object>> _TaskList = SqliteUtil.getInstance().getList("T_Attachment", conditions);

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

		} catch (JSONException ex) {
			LogUtil.v("Json", ex.getMessage());
		}
		return _JsonObject;
	}

}
