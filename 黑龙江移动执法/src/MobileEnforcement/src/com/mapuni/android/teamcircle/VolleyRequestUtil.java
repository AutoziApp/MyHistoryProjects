package com.mapuni.android.teamcircle;

import java.util.Map;

import android.content.Context;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.mapuni.android.base.Global;

public class VolleyRequestUtil {
	public static StringRequest stringRequest;
	public static Context context;

    /*
    * ��ȡGET��������
    * ������
    * context����ǰ�����ģ�
    * url�������url��ַ��
    * tag����ǰ����ı�ǩ��
    * volleyListenerInterface��VolleyListenerInterface�ӿڣ�
    * */
	public static void RequestGet(Context context, String url, String tag,
			VolleyListenerInterface volleyListenerInterface) {
		// �����������е�tag�������
		Global.getRequestQueue().cancelAll(tag);
		// ������ǰ�����󣬻�ȡ�ַ�������
		stringRequest = new StringRequest(Request.Method.GET, url,
				volleyListenerInterface.responseListener(),
				volleyListenerInterface.errorListener());
		// Ϊ��ǰ������ӱ��
		stringRequest.setTag(tag);
		// ����ǰ������ӵ����������
		Global.getRequestQueue().add(stringRequest);
		// ������ǰ�������
		Global.getRequestQueue().start();
	}

    /*
    * ��ȡPOST�������ݣ�����Ĵ���ΪMap��
    * ������
    * context����ǰ�����ģ�
    * url�������url��ַ��
    * tag����ǰ����ı�ǩ��
    * params��POST�������ݣ�
    * volleyListenerInterface��VolleyListenerInterface�ӿڣ�
    * */
	public static void RequestPost(Context context, String url, String tag,
			final Map<String, String> params,
			VolleyListenerInterface volleyListenerInterface) {
		// �����������е�tag�������
		Global.getRequestQueue().cancelAll(tag);
		// ������ǰ��POST���󣬲�����������д��Map��
		stringRequest = new StringRequest(Request.Method.POST, url,
				volleyListenerInterface.responseListener(),
				volleyListenerInterface.errorListener()) {
			@Override
			protected Map<String, String> getParams() throws AuthFailureError {
				return params;
			}
		};
		// Ϊ��ǰ������ӱ��
		stringRequest.setTag(tag);
		// ����ǰ������ӵ����������
		Global.getRequestQueue().add(stringRequest);
		// ������ǰ�������
		Global.getRequestQueue().start();
	}
}
