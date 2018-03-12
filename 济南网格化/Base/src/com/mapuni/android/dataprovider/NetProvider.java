package com.mapuni.android.dataprovider;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import com.mapuni.android.base.Global;
import com.mapuni.android.netprovider.WebServiceProvider;

public class NetProvider {

	/**
	 * ��WebService������һ���򵥵ķ�װ������ÿ�ζ���Ҫ��дwsdl��namespace
	 * 
	 * @param methodName
	 *            ������
	 * @param params
	 *            ��������
	 * @param returnType
	 *            ����ֵ����
	 * @return ���ú�̨���ص�ֵ
	 */
	public static Object callWebService(String methodName, List<LinkedHashMap<String, Object>> params, int returnType) {
		try {
			return WebServiceProvider.callWebService(Global.NAMESPACE, methodName, params, Global.getGlobalInstance().getSystemurl() + Global.WEBSERVICE_URL, returnType, true);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * ��WebService������һ���򵥵ķ�װ������ÿ�ζ���Ҫ��дwsdl��namespace
	 * 
	 * @param methodName
	 *            ������
	 * @param params
	 *            ��������
	 * @param returnType
	 *            ����ֵ����
	 * @return ���ú�̨���ص�ֵ
	 */
	public static Object callWebService(String methodName, LinkedHashMap<String, Object> param, int returnType) {
		List<LinkedHashMap<String, Object>> params = new ArrayList<LinkedHashMap<String, Object>>();
		params.add(param);
		return callWebService(methodName, params, returnType);
	}
}
