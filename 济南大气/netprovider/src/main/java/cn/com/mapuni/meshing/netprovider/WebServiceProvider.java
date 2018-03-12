package cn.com.mapuni.meshing.netprovider;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.SoapFault;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
//import org.ksoap2.transport.AndroidHttpTransport;

import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;

/**
 * WebService工具类
 * 
 * @author xugf@mapuni.com
 * 
 */
@SuppressWarnings("deprecation")
public class WebServiceProvider {

	private static final String TAG = "WSUtils";
	// public static Context activitycontext;

	/* 返回值的类型 */
	public static final int RETURN_OBJECT = -1;
	public static final int RETURN_BOOLEAN = 0;
	public static final int RETURN_INT = 1;
	public static final int RETURN_STRING = 2;
	public static final int RETURN_HASHMAP = 3;
	public static final int RETURN_LIST = 4;
	public static final int RETURN_LIST_HASHMAP = 5;
	public static final int RETURN_VOID = 6;
	public static final int RETURN_ARRAY = 7;

//	private static AndroidHttpTransport androidHT = null;

	public static Object callWebServiceWithProgressDlg(Context activitycontext, String nameSpace, String methodName,
			List<HashMap<String, Object>> params, String wsdl, int returnType, boolean isDotNet, int... timeout) throws IOException {
		ProgressDialog dialog = new ProgressDialog(activitycontext);
		dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		// 设置进度条风格，风格为圆形，旋转的
		dialog.show();
		Object ob = callWebService(nameSpace, methodName, params, wsdl, returnType, isDotNet, timeout);
		dialog.dismiss();
		return ob;
	}

	/**
	 * 调用webservice
	 * 
	 * @param nameSpace
	 *            发布的wsdl命名空间
	 * @param methodName
	 *            调用webservice的方法名
	 * @param params
	 *            要传入的参数
	 * @param wsdl
	 *            webservice的wsdl
	 * @param returnType
	 *            调用webservice的方法返回值类型
	 * @param isDotNet
	 *            调用的webservice是否为dot net链接
	 * @param timeout
	 *            设置webservice连接超时时间
	 * @return
	 * @throws IOException
	 */
	public static Object callWebService(String nameSpace, String methodName, List<HashMap<String, Object>> params, String wsdl,
			int returnType, boolean isDotNet, int... timeout) throws IOException {

		String SOAP_ACTION = null;

		if (isDotNet) {
			SOAP_ACTION = nameSpace + methodName;
		}
		SoapObject request = new SoapObject(nameSpace, methodName);

		/* 装入参数 */
		if ((params != null) && (!(params.isEmpty()))) {
			for (int i = 0; i < params.size(); ++i) {

				HashMap<String, Object> _HashMapTemp = params.get(i);
				Set _Iterator = _HashMapTemp.entrySet();

				for (Iterator iter = _Iterator.iterator(); iter.hasNext();) {

					Map.Entry entry = (Map.Entry) iter.next();
					String _Key = entry.getKey().toString();
					request.addProperty(_Key, entry.getValue());
				}
			}
		}

		/*
		 * SoapSerializationEnvelope类的构造方法设置SOAP协议的版本号，该版本号需要根据服务端Webservice的版本号设置
		 */
		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);

		envelope.dotNet = isDotNet;
		envelope.bodyOut = request;
		envelope.setOutputSoapObject(request);

		/* 设置默认webserice连接超时时间为30秒 */
		if (timeout.length < 1) {
			timeout = new int[] { 30000 };
		}
		boolean IsTrue = Net.checkURL(wsdl);
		Object result = null;
		if (IsTrue) {
//			androidHT = new AndroidHttpTransport(wsdl, timeout[0]);
//			androidHT.debug = true;
//			boolean isConnected = true;
//			try {
//				androidHT.call(SOAP_ACTION, envelope); // 调用webservice
//			} catch (Exception e1) {
//				isConnected = false;
//			}
//			try {
//				if (isConnected) {
//					result = envelope.getResponse();
//				}
//			} catch (SoapFault e) {
//				Log.e(TAG, e.getMessage());
//			}
		}

		if (result != null) {
			switch (returnType) {
			case RETURN_BOOLEAN:
				result = Boolean.valueOf(result.toString());
				break;
			case RETURN_INT:
				result = Integer.valueOf(Integer.parseInt(result.toString()));
				break;
			case RETURN_STRING:
				result = String.valueOf(result);
				break;
			case RETURN_HASHMAP:
				SoapObject soapObject = (SoapObject) result;

				HashMap<String, Object> map = null;
				if ((result != null) && (soapObject.getPropertyCount() > 0)) {
					map = new HashMap<String, Object>();
				}

				for (int i = 0; i < soapObject.getPropertyCount(); ++i) {
					String key = ((SoapObject) soapObject.getProperty(i)).getProperty("key").toString();
					Object value = ((SoapObject) soapObject.getProperty(i)).getProperty("value");
					map.put(key, value);
				}

				result = map;
				break;
			case RETURN_LIST:
				SoapObject soapObjectList = (SoapObject) result;

				ArrayList<Object> list = null;
				if ((soapObjectList != null) && (soapObjectList.getPropertyCount() > 0)) {
					list = new ArrayList<Object>();
				}

				for (int i = 0; i < soapObjectList.getPropertyCount(); ++i) {
					Object value = soapObjectList.getProperty(i);
					list.add(value);
				}

				result = list;
				break;
			case RETURN_LIST_HASHMAP:
				SoapObject soapObjectListHashMap = (SoapObject) result;
				ArrayList<Object> listHashMap = null;

				if ((soapObjectListHashMap != null) && (soapObjectListHashMap.getPropertyCount() > 0)) {

					listHashMap = new ArrayList<Object>();

					if (isDotNet) {
						SoapObject soapObjectKey = (SoapObject) soapObjectListHashMap.getProperty(0);
						ArrayList<Object> listKey = null;

						if ((soapObjectKey != null) && (soapObjectKey.getPropertyCount() > 0)) {
							listKey = new ArrayList<Object>();
						}

						for (int i = 0; i < soapObjectKey.getPropertyCount(); ++i) {
							String value = soapObjectKey.getProperty(i).toString();
							listKey.add(value);
						}

						int i = 1;
						for (; i < soapObjectListHashMap.getPropertyCount(); ++i) {
							SoapObject soapObject2 = (SoapObject) soapObjectListHashMap.getProperty(i);

							HashMap<String, Object> map2 = null;

							if ((soapObject2 != null) && (soapObject2.getPropertyCount() > 0)) {
								map2 = new HashMap<String, Object>();
							}

							for (int j = 0; j < soapObject2.getPropertyCount(); ++j) {
								String key = (String) listKey.get(j);
								Object value = soapObject2.getProperty(j);
								map2.put(key, value);
							}
							listHashMap.add(map2);
						}
					} else {
						int i = 0;
						for (; i < soapObjectListHashMap.getPropertyCount(); ++i) {
							SoapObject soapObject2 = (SoapObject) soapObjectListHashMap.getProperty(i);

							HashMap<String, Object> map2 = null;
							if ((soapObject2 != null) && (soapObject2.getPropertyCount() > 0)) {
								map2 = new HashMap<String, Object>();
							}
							for (int j = 0; j < soapObject2.getPropertyCount(); ++j) {
								String key = ((SoapObject) soapObject2.getProperty(j)).getProperty("key").toString();
								Object value = ((SoapObject) soapObject2.getProperty(j)).getProperty("value");
								map2.put(key, value);
							}
							listHashMap.add(map2);
						}
					}
				}
				result = listHashMap;
				break;
			case RETURN_VOID:
				result = null;
				break;
			case RETURN_ARRAY:
				SoapObject soapObjectArray = (SoapObject) result;

				String[] array = null;
				if ((soapObjectArray != null) && (soapObjectArray.getPropertyCount() > 0)) {
					array = new String[soapObjectArray.getPropertyCount()];
				}

				for (int i = 0; i < soapObjectArray.getPropertyCount(); ++i) {
					String value = soapObjectArray.getProperty(i).toString();
					array[i] = value;
				}

				result = array;
				break;
			}

		} else {
			switch (returnType) {
			case RETURN_BOOLEAN:
				result = false;
				break;
			case RETURN_INT:
				result = 0;
				break;
			case RETURN_STRING:
				result = "";
				break;
			case RETURN_HASHMAP:
				HashMap<String, Object> map = new HashMap<String, Object>();
				result = map;
				break;
			case RETURN_LIST:
				ArrayList<Object> list = new ArrayList<Object>();
				result = list;
				break;
			case RETURN_LIST_HASHMAP:
				ArrayList<Object> listHashMap = new ArrayList<Object>();
				result = listHashMap;
				break;
			case RETURN_VOID:
				result = null;
				break;
			case RETURN_ARRAY:
				String[] temp = { "" };
				result = temp;
				break;
			}
		}

		return result;
	}

	/**
	 * 断掉连接
	 */
	public static void disconnect() {
//		try {
//			androidHT.getConnection().disconnect();
//		} catch (IOException e) {
//			Log.e(TAG, e.getMessage());
//		}
	}

}
