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
 * Description: 自动更新
 * <li> DataSync 读取config 的版本号 readVersion
 * <li> Main 监测新版本  JudgeNewVerson  UPdateAPK 更新  用不到了
 * <li> LoadGridLayout 监测新版本  JudgeNewVerson  UPdateAPK 更新  用不到了
 * @author 包坤
 * @Version 1.3.4
 * @Copyright 中科宇图天下科技有限公司 
 * Create at: 2012-12-6 下午02:25:58
 */
public class BaseAutoUpdate {

	/** 脚本升级失败 */
	protected  final int STATE_FAILURE = 0;
	/** 连接服务器超时 */
	protected  final int STATE_NETWORK_ERROR = 1;
	/** 脚本升级成功 */
	protected  final int STATE_SUCCESS = 2;
	/** 连接不上服务器 */
	protected  final int STATE_NETWORK_FALURE = 3;
	/** 已是最新 */
	protected  final int STATE_NODATA = 4;
	
	protected final String  TAG="BaseAutoUpdate";
	
	private HashMap<String, String>  versonHashMap;

	/**
	 * Description: 更新APK文件
	 * @param apk_url 服务器端apk文件的路径
	 * @param context 上下文对象
	 * @param code_url 服务器端存放apk版本文件的路径
	 * void
	 * @author Administrator
	 * Create at: 2012-12-6 下午02:26:40
	 */
	public void UPdateAPK(final String apk_url, final Context context,
			final String code_url) {
	
		if (versonHashMap != null && versonHashMap.get("isviolence").equals("true")) {
			AlertDialog.Builder builder = new AlertDialog.Builder(context);
			builder.setTitle("自动更新");
			builder.setMessage("有新版本可以更新，确认更新？");
			builder.setCancelable(false);
			builder.setIcon(R.drawable.base_icon_mapuni_white);
			builder.setPositiveButton("确定",
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
	 * Description: 判断是否有新版本程序
	 * @param code_url 服务器端存放apk版本号文件的路径
	 * @param context 上下文
	 * @return 0：网络异常，2：当前最新版本，1：有新版本
	 * int
	 * @author 包坤
	 * Create at: 2012-12-6 下午02:27:57
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
	 * Description: 更新apk的Dialog
	 * @param context 上下文
	 * @param apk_url 服务器端存放apk的文件地址
	 * @param code_url 服务器端存放apk版本号文件的路径
	 * void
	 * @author 王红娟
	 * Create at: 2012-12-6 下午02:37:26
	 */
	void UpdataDialog(final Context context, final String apk_url,
			final String code_url) {
		/** 显示已有新版本更新对话框 */
		AlertDialog.Builder ab = new AlertDialog.Builder(context);
		ab.setCancelable(false);
		ab.setTitle("自动更新");
		ab.setMessage("有新版本可以更新，是否更新？");
		ab.setPositiveButton("确定", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				/** 下载APK的点击事件 */
				Progressdialog(context, apk_url, code_url);
			}
		});

		ab.setNegativeButton("取消", new DialogInterface.OnClickListener() {
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
	 * 新版本apk的备注信息
	 */
	String text = "";

	/**
	 * Description: 下载新版本apk
	 * @param context 上下文
	 * @param apk_url 服务器端存放apk的文件地址
	 * @param code_url 服务器端存放apk版本号文件的路径
	 * void
	 * @author 王红娟
	 * Create at: 2012-12-6 下午02:40:07
	 */
	public void Progressdialog(final Context context, final String apk_url,
			final String code_url) {
		/** 自定义进度显示Dialog */

		Toast.makeText(context, "开始下载新版本", 0).show();
		
		final NotificationManager manager = (NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);
		final Notification notify = new Notification();
		
		notify.icon = R.drawable.stat_sys_download_anim0;
		notify.tickerText = "移动执法-开始下载";
		Intent intent = new Intent();
		final PendingIntent pi = PendingIntent.getActivity(context, 0, intent, 0);
		notify.setLatestEventInfo(context, "移动执法", "0%", pi);
		notify.flags = Notification.FLAG_ONGOING_EVENT;
		manager.notify(0, notify);
		
		//String discript = versonHashMap.get("discrip").toString();
	//	String version = versonHashMap.get("verson").toString();
/*		if (discript != null || discript.equals("")) {
			Date now = new Date();
			DateFormat dateFormat = new SimpleDateFormat("yyyy年MM月dd日");
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
				notify.setLatestEventInfo(context, "移动执法", msg.arg1+"%", pi);
				manager.notify(0, notify);
				if(msg.arg1 == 100){
					Toast.makeText(context, "下载完成", 0).show();
					manager.cancel(0);
					((Service)context).stopSelf();//关闭更新服务
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
						/** 如果没有目录先建立目录 */
						File f = new File(
								PathManager.SDCARD_AutoUpdate_LOCAL_PATH);
						if (!f.exists())
							f.mkdirs();
						/** 有目录之后建文件 */
						File fil = new File(
								PathManager.SDCARD_APK_LOCAL_PATH);
						fos = new FileOutputStream(fil);
						byte[] bytes = new byte[1024*8];
						int flag = -1;
						/** 文件总字节长度 */
						int count = 0;
						int lastper = 0;
						/** 若未读到文件末尾则一直读取 */
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
						/** 下载完成后取消进度Dialog */
//						pd.cancel();
						/** 下载完成后调用安装程序 */
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
	 * Description: 将文件流转换格式
	 * @param is 文件流
	 * @param encoding 编码格式
	 * @return 返回转换完成的字符串
	 * String
	 * @author 王红娟
	 * Create at: 2012-12-6 下午02:43:32
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
	 * Description: 获取系统版本号
	 * @param context 上下文
	 * @return 当前系统版本号
	 * String
	 * @author 王红娟
	 * Create at: 2012-12-6 下午02:44:48
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
	 * Description: 读取服务器apk版本号文件XML中的Verson
	 * @param Url 服务器放置apk版本号信息的xml文件路径
	 * @return 版本号信息
	 * HashMap<String,String>
	 * @author 王红娟
	 * Create at: 2012-12-6 下午02:45:20
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
	 * Description: 更新数据库脚本,连网操作，建议开线程执行此操作
	 * @author 王红娟 ---》wanglg 8-19 修改
	 * return  0 脚本更新失败 1 连接超时 2 脚本更新成功 3 连接不上服务器  4 无脚本更新
	 * Create at: 2012-12-6 下午02:46:26
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
		/** ------下载更新的方法中暂时写在这里------- */
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
			/** 解析数据错误 */
			LogUtil.e("AutoUpdate", e.getMessage());
		} catch (IOException e) {
			/** 连接服务器超时 */
			LogUtil.e("AutoUpdate", e.getMessage());
			result=STATE_NETWORK_ERROR;
		}
		return result;
	}

	
	/**
	 * Description: 检查更新
	 * @param context 上下文
	 * @return 有新版本返回true
	 * boolean
	 * @author Administrator
	 * Create at: 2012-12-6 下午02:49:43
	 */
	public boolean updateCheck(Context context) {
		/** url若不通则跳过更新 */
		String urlString = Global.getGlobalInstance().getSystemurl();
		
		if (Net.checkURL(urlString + PathManager.APK_CODE_URL)) {
			
				
				if ( JudgeNewVerson(urlString + PathManager.APK_CODE_URL,
								context) == 1) {/** 有新版本则弹出更新对话框  */
					UPdateAPK(Global.getGlobalInstance().getSystemurl()
							+ PathManager.APK_DOWN_URL, context, Global
							.getGlobalInstance().getSystemurl() + PathManager.APK_CODE_URL);
					return true;
				}
			
		}else{
			LogUtil.i(TAG, "检查更新--->网络不通");
			//Toast.makeText(context, "网络异常，请检查网络设置", Toast.LENGTH_SHORT).show();
		}
		return false;
	}


}
