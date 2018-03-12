package com.mapuni.android.dataprovider;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.mapuni.android.base.Global;
import com.mapuni.android.netprovider.WebServiceProvider;

public class NetProvider {

	/**
	 * 对WebService进行了一个简单的封装，不必每次都需要填写wsdl和namespace
	 * 
	 * @param methodName
	 *            方法名
	 * @param params
	 *            参数集合
	 * @param returnType
	 *            返回值类型
	 * @return 调用后台返回的值
	 */
	public static Object callWebService(String methodName, List<HashMap<String, Object>> params, int returnType) {
		try {
			return WebServiceProvider.callWebService(Global.NAMESPACE, methodName, params, Global.getGlobalInstance().getSystemurl() + Global.WEBSERVICE_URL, returnType, true);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 对WebService进行了一个简单的封装，不必每次都需要填写wsdl和namespace
	 * 
	 * @param methodName
	 *            方法名
	 * @param params
	 *            参数集合
	 * @param returnType
	 *            返回值类型
	 * @return 调用后台返回的值
	 */
	public static Object callWebService(String methodName, HashMap<String, Object> param, int returnType) {
		List<HashMap<String, Object>> params = new ArrayList<HashMap<String, Object>>();
		params.add(param);
		return callWebService(methodName, params, returnType);
	}
}
