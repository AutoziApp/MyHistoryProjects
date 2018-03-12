package com.mapuni.android.teamcircle;

import android.content.Context;

import com.android.volley.Response;
import com.android.volley.VolleyError;

public abstract class VolleyListenerInterface {
	public Context mContext;
	public static Response.Listener<String> mListener;
	public static Response.ErrorListener mErrorListener;

	public VolleyListenerInterface(Context context,
			Response.Listener<String> listener,
			Response.ErrorListener errorListener) {
		this.mContext = context;
		this.mErrorListener = errorListener;
		this.mListener = listener;
	}

	// ����ɹ�ʱ�Ļص�����
	public abstract void onMySuccess(String result);

	// ����ʧ��ʱ�Ļص�����
	public abstract void onMyError(VolleyError error);

	// ����������¼�����
	public Response.Listener<String> responseListener() {
		mListener = new Response.Listener<String>() {
			@Override
			public void onResponse(String s) {
				onMySuccess(s);
			}
		};
		return mListener;
	}

	// ��������ʧ�ܵ��¼�����
	public Response.ErrorListener errorListener() {
		mErrorListener = new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError volleyError) {
				onMyError(volleyError);
			}
		};
		return mErrorListener;
	}
}
