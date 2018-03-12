package com.mapuni.android.base.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONException;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.util.Xml;
import android.view.WindowManager;
import android.widget.Toast;

import com.mapuni.android.base.Global;
import com.mapuni.android.base.R;
import com.mapuni.android.base.interfaces.PathManager;
import com.mapuni.android.dataprovider.DESSecurity;
import com.mapuni.android.dataprovider.SqliteUtil;
import com.mapuni.android.netprovider.Net;
import com.mapuni.android.netprovider.WebServiceProvider;

/**
 * FileName: AutoUpdate.java 
 * Description: �Զ�����
 * <li> DataSync ��ȡconfig �İ汾�� readVersion
 * <li> Main ����°汾  JudgeNewVerson  UPdateAPK ����  �ò�����
 * <li> LoadGridLayout ����°汾  JudgeNewVerson  UPdateAPK ����  �ò�����
 * @author ����
 * @Version 1.3.4
 * @Copyright �п���ͼ���¿Ƽ����޹�˾ 
 * Create at: 2012-12-6 ����02:25:58
 */
public class BaseAutoUpdate {

	/** �ű�����ʧ�� */
	protected  final int STATE_FAILURE = 0;
	/** ���ӷ�������ʱ */
	protected  final int STATE_NETWORK_ERROR = 1;
	/** �ű������ɹ� */
	protected  final int STATE_SUCCESS = 2;
	/** ���Ӳ��Ϸ����� */
	protected  final int STATE_NETWORK_FALURE = 3;
	/** �������� */
	protected  final int STATE_NODATA = 4;
	
	protected final String  TAG="BaseAutoUpdate";
	
	private HashMap<String, String>  versonHashMap;

	/**
	 * Description: ����APK�ļ�
	 * @param apk_url ��������apk�ļ���·��
	 * @param context �����Ķ���
	 * @param code_url �������˴��apk�汾�ļ���·��
	 * void
	 * @author Administrator
	 * Create at: 2012-12-6 ����02:26:40
	 */
	public void UPdateAPK(final String apk_url, final Context context,
			final String code_url) {
	
		if (versonHashMap != null && versonHashMap.get("isviolence").equals("true")) {
			AlertDialog.Builder builder = new AlertDialog.Builder(context);
			builder.setTitle("�Զ�����");
			builder.setMessage("���°汾���Ը��£�ȷ�ϸ��£�");
			builder.setCancelable(false);
			builder.setIcon(R.drawable.base_icon_mapuni_white);
			builder.setPositiveButton("ȷ��",
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface arg0, int arg1) {
							Progressdialog(context, apk_url, code_url);
						}
					});
			AlertDialog adialog = builder.create();
			adialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
			adialog.show();
		} else {
			UpdataDialog(context, apk_url, code_url);
		}
	}

	/**
	 * Description: �ж��Ƿ����°汾����
	 * @param code_url �������˴��apk�汾���ļ���·��
	 * @param context ������
	 * @return 0�������쳣��2����ǰ���°汾��1�����°汾
	 * int
	 * @author ����
	 * Create at: 2012-12-6 ����02:27:57
	 */
	public int JudgeNewVerson(String code_url, Context context) {
		versonHashMap = readVerson(code_url);
		if (null == versonHashMap) {
			return 0;
		}
		if (getTargetSystemVersionNumber(context).compareTo(versonHashMap.get("verson")) < 0) {
			return 1;
		} else {
			return 2;
		}

	}

	/**
	 * Description: ����apk��Dialog
	 * @param context ������
	 * @param apk_url �������˴��apk���ļ���ַ
	 * @param code_url �������˴��apk�汾���ļ���·��
	 * void
	 * @author �����
	 * Create at: 2012-12-6 ����02:37:26
	 */
	void UpdataDialog(final Context context, final String apk_url,
			final String code_url) {
		/** ��ʾ�����°汾���¶Ի��� */
		AlertDialog.Builder ab = new AlertDialog.Builder(context);
		ab.setCancelable(false);
		ab.setTitle("�Զ�����");
		ab.setMessage("���°汾���Ը��£��Ƿ���£�");
		ab.setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				/** ����APK�ĵ���¼� */
				Progressdialog(context, apk_url, code_url);
			}
		});

		ab.setNegativeButton("ȡ��", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				//((Service)context).stopSelf();
				dialog.cancel();
			}
		});

		ab.setIcon(context.getResources().getDrawable(
				R.drawable.base_icon_mapuni_white));
		AlertDialog alert = ab.create();
		alert.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
		alert.show();
	}

	/**
	 * �°汾apk�ı�ע��Ϣ
	 */
	String text = "";

	/**
	 * Description: �����°汾apk
	 * @param context ������
	 * @param apk_url �������˴��apk���ļ���ַ
	 * @param code_url �������˴��apk�汾���ļ���·��
	 * void
	 * @author �����
	 * Create at: 2012-12-6 ����02:40:07
	 */
	public void Progressdialog(final Context context, final String apk_url,
			final String code_url) {
		/** �Զ��������ʾDialog */

		Toast.makeText(context, "��ʼ�����°汾", 0).show();
		
		final NotificationManager manager = (NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);
		final Notification notify = new Notification();
		
		notify.icon = R.drawable.stat_sys_download_anim0;
		notify.tickerText = "�ƶ�ִ��-��ʼ����";
		Intent intent = new Intent();
		final PendingIntent pi = PendingIntent.getActivity(context, 0, intent, 0);
		notify.setLatestEventInfo(context, "�ƶ�ִ��", "0%", pi);
		notify.flags = Notification.FLAG_ONGOING_EVENT;
		manager.notify(0, notify);
		
		//String discript = versonHashMap.get("discrip").toString();
	//	String version = versonHashMap.get("verson").toString();
/*		if (discript != null || discript.equals("")) {
			Date now = new Date();
			DateFormat dateFormat = new SimpleDateFormat("yyyy��MM��dd��");
			dateFormat.format(now);
			String urlpath = Global.getGlobalInstance().getSystemurl() + discript;
			URL url;
			try {

				url = new URL(urlpath);
				HttpURLConnection con;
				try {
					con = (HttpURLConnection) url.openConnection();
					final InputStream in = con.getInputStream();
					text = getString(in, "utf-8");

				} catch (IOException e) {
					e.printStackTrace();
				}
			} catch (MalformedURLException e) {
				e.printStackTrace();
			}

			String checkSql = "select * from T_APK_Version where APKVersion='"
					+ version + "'";
			ArrayList<HashMap<String, Object>> data = SqliteUtil.getInstance().queryBySqlReturnArrayListHashMap(checkSql);
			if(data != null) {
				if (data.size() != 1) {
					String sql = "insert into T_APK_Version (APKVersion,UpdateTime,bz) values ('"
							+ version
							+ "','"
							+ dateFormat.format(now)
							+ "','"
							+ text + "')";
					SqliteUtil.getInstance().execute(sql);
				}
			}
		}*/
		final Handler h = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				notify.setLatestEventInfo(context, "�ƶ�ִ��", msg.arg1+"%", pi);
				manager.notify(0, notify);
				if(msg.arg1 == 100){
					Toast.makeText(context, "�������", 0).show();
					manager.cancel(0);
					((Service)context).stopSelf();//�رո��·���
				}
//				pd.setProgress(msg.arg1);
			}
		};
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					URL url = new URL(apk_url);
					HttpURLConnection con = (HttpURLConnection) url
							.openConnection();
					long length = con.getContentLength();
					InputStream in = con.getInputStream();
					FileOutputStream fos = null;
					if (in != null) {
						/** ���û��Ŀ¼�Ƚ���Ŀ¼ */
						File f = new File(
								PathManager.SDCARD_AutoUpdate_LOCAL_PATH);
						if (!f.exists())
							f.mkdirs();
						/** ��Ŀ¼֮���ļ� */
						File fil = new File(
								PathManager.SDCARD_APK_LOCAL_PATH);
						fos = new FileOutputStream(fil);
						byte[] bytes = new byte[1024*8];
						int flag = -1;
						/** �ļ����ֽڳ��� */
						int count = 0;
						int lastper = 0;
						/** ��δ�����ļ�ĩβ��һֱ��ȡ */
						while ((flag = in.read(bytes)) != -1) {
							fos.write(bytes, 0, flag);
							count += flag;
							double percent = (double) count / (double) length;
							int per = (int) (percent * 100);
							if(per - lastper >= 1 || per == 1){
								Message msg = new Message();
								msg.arg1 = per;
								h.sendMessage(msg);
								lastper = per;
							}
						}
						fos.flush();
						fos.close();
						/** ������ɺ�ȡ������Dialog */
//						pd.cancel();
						/** ������ɺ���ð�װ���� */
						Intent i = new Intent(Intent.ACTION_VIEW);
						String filePath = PathManager.SDCARD_APK_LOCAL_PATH;
						i.setDataAndType(Uri.parse("file://" + filePath),
								"application/vnd.android.package-archive");
						i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
						context.startActivity(i);
					}
				} catch (ClientProtocolException e) {
					ExceptionManager.WriteCaughtEXP(e, "AutoUpdate");
					e.printStackTrace();
				} catch (IOException e) {
					ExceptionManager.WriteCaughtEXP(e, "AutoUpdate");
					e.printStackTrace();
				}
			}
		}).start();
	}

	/**
	 * Description: ���ļ���ת����ʽ
	 * @param is �ļ���
	 * @param encoding �����ʽ
	 * @return ����ת����ɵ��ַ���
	 * String
	 * @author �����
	 * Create at: 2012-12-6 ����02:43:32
	 */
	private String getString(InputStream is, String encoding) {
		try {
			byte[] b = new byte[1024];
			String res = "";
			if (is == null) {
				return "";
			}

			int bytesRead = 0;
			while (true) {
				bytesRead = is.read(b, 0, 1024); // return final read bytes
													// counts
				if (bytesRead == -1) {// end of InputStream
					return res;
				}
				res += new String(b, 0, bytesRead, encoding); // convert to
																// string using
																// bytes
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.print("Exception: " + e);
			return "";
		}
	}

	/**
	 * Description: ��ȡϵͳ�汾��
	 * @param context ������
	 * @return ��ǰϵͳ�汾��
	 * String
	 * @author �����
	 * Create at: 2012-12-6 ����02:44:48
	 */
	public String getTargetSystemVersionNumber(Context context) {
	
		String targetPackage = "com.example.meshing";
		
		try {
			PackageInfo targetInfo = context.getPackageManager().
				getPackageInfo(targetPackage, PackageManager.GET_UNINSTALLED_PACKAGES);
			return targetInfo.versionName;
		} catch (NameNotFoundException e1) {
			e1.printStackTrace();
		}
		return null;
	}
	
	/**
	 * Description: ��ȡ������apk�汾���ļ�XML�е�Verson
	 * @param Url ����������apk�汾����Ϣ��xml�ļ�·��
	 * @return �汾����Ϣ
	 * HashMap<String,String>
	 * @author �����
	 * Create at: 2012-12-6 ����02:45:20
	 */
	public HashMap<String, String> readVerson(String Url) {
		try {
			URL url = new URL(Url);
			HashMap<String, String> map = new HashMap<String, String>();
			InputStream in = url.openStream();
			XmlPullParser xmlparser = Xml.newPullParser();
			xmlparser.setInput(in, "utf-8");
			int event = xmlparser.getEventType();
			while (event != XmlPullParser.END_DOCUMENT) {

				switch (event) {
				case XmlPullParser.START_DOCUMENT:
					break;
				case XmlPullParser.START_TAG:
					if (xmlparser.getName().equals("verson")) {

					} else if (xmlparser.getName().equals("versonnum")) {
						String verson = xmlparser.nextText();
						map.put("verson", verson);
					} else if (xmlparser.getName().equals("isviolence")) {
						String isviolence = xmlparser.nextText();
						map.put("isviolence", isviolence);
					} else if (xmlparser.getName().equals("discrip")) {
						String discrip = xmlparser.nextText();
						map.put("discrip", discrip);
					}
					break;
				case XmlPullParser.END_TAG:
					break;
				}
				event = xmlparser.next();
			}
			return map;
		} catch (FileNotFoundException e) {
			ExceptionManager.WriteCaughtEXP(e, "AutoUpdate");
			e.printStackTrace();
		} catch (XmlPullParserException e) {
			ExceptionManager.WriteCaughtEXP(e, "AutoUpdate");
			e.printStackTrace();
		} catch (IOException e) {
			ExceptionManager.WriteCaughtEXP(e, "AutoUpdate");
			e.printStackTrace();
		} catch (Exception e) {
			// TODO: handle exception
//			ExceptionManager.WriteCaughtEXP(e, "AutoUpdate");
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Description: �������ݿ�ű�,�������������鿪�߳�ִ�д˲���
	 * @author ����� ---��wanglg 8-19 �޸�
	 * return  0 �ű�����ʧ�� 1 ���ӳ�ʱ 2 �ű����³ɹ� 3 ���Ӳ��Ϸ�����  4 �޽ű�����
	 * Create at: 2012-12-6 ����02:46:26
	 */
	public int UpdateDatebaseScript() {
		int result=STATE_FAILURE;
		List<LinkedHashMap<String, Object>> params = new ArrayList<LinkedHashMap<String, Object>>();
		LinkedHashMap<String, Object> param = new LinkedHashMap<String, Object>();
		String methodName = "";
		ArrayList<HashMap<String, Object>> data = new ArrayList<HashMap<String, Object>>();

		String version = "";
		try {
			data = SqliteUtil
					.getInstance()
					.queryBySqlReturnArrayListHashMap(
							"select version from T_DB_Version order by id desc limit 0,1");
			if (data != null && data.size() > 0) {
				version = (String) data.get(0).get("version");
			} else {
				version = "1.3.0";
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		param.put("versionNum", version);
		try {
			methodName = DESSecurity.encrypt("GetVersionScriptList");
			param.put("token", methodName);
		} catch (Exception e2) {
			e2.printStackTrace();
		}
		params.add(param);
		/** ------���ظ��µķ�������ʱд������------- */
		try {
			String strjson = (String) WebServiceProvider.callWebService(
					Global.NAMESPACE, "GetVersionScriptList", params, Global
							.getGlobalInstance().getSystemurl()
							+ Global.WEBSERVICE_URL,
					WebServiceProvider.RETURN_STRING, true);
			if (strjson == null) {
				result=STATE_NETWORK_FALURE;
			} else if (strjson.equals("[]")) {
				result=STATE_NODATA;
			} else {
				if (!"".equals(strjson)) {
					org.json.JSONArray arr = new org.json.JSONArray(
							new org.json.JSONTokener(strjson));
					for (int i = 0; i < arr.length(); i++) {
						JSONObject jsonObject = arr.getJSONObject(i);
						String script = jsonObject.getString("Scripts");
						for (String s : script.split(";")) {
							try {
								SqliteUtil.getInstance().execute(s);
							} catch (Exception e) {
								LogUtil.e("AutoUpdate", "//"+e.getMessage());
								continue;
							}

						}
					}
					result=STATE_SUCCESS;
				} else {
					result=STATE_NODATA;
				}
			}

		} catch (JSONException e) {
			/** �������ݴ��� */
			LogUtil.e("AutoUpdate", e.getMessage());
		} catch (IOException e) {
			/** ���ӷ�������ʱ */
			LogUtil.e("AutoUpdate", e.getMessage());
			result=STATE_NETWORK_ERROR;
		}
		return result;
	}

	
	/**
	 * Description: ������
	 * @param context ������
	 * @return ���°汾����true
	 * boolean
	 * @author Administrator
	 * Create at: 2012-12-6 ����02:49:43
	 */
	public boolean updateCheck(Context context) {
		/** url����ͨ���������� */
		String urlString = Global.getGlobalInstance().getSystemurl();
		
		if (Net.checkURL(urlString + PathManager.APK_CODE_URL)) {
			
				
				if ( JudgeNewVerson(urlString + PathManager.APK_CODE_URL,
								context) == 1) {/** ���°汾�򵯳����¶Ի���  */
					UPdateAPK(Global.getGlobalInstance().getSystemurl()
							+ PathManager.APK_DOWN_URL, context, Global
							.getGlobalInstance().getSystemurl() + PathManager.APK_CODE_URL);
					return true;
				}
			
		}else{
			LogUtil.i(TAG, "������--->���粻ͨ");
			//Toast.makeText(context, "�����쳣��������������", Toast.LENGTH_SHORT).show();
		}
		return false;
	}


}
