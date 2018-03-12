package com.jy.environment.util;

import java.io.UnsupportedEncodingException;

import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;

import com.jy.environment.controls.AsyncTask;


public class HttpPostTask extends AsyncTask<String, Void, String> {
	protected Handler mHandler;
	protected String murl;
	protected int gid;
	protected String mpostcontent;
	
	public HttpPostTask(Handler handler,String url,String postcontent,int id){
		mHandler=handler;
		murl=url;
		gid=id;
		mpostcontent=postcontent;
	}
	
	@Override
	protected String doInBackground(String... arg0) {
		try {
			return ApiClient.PostToServerForResult(murl, mpostcontent);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 return "";
	}

	@Override
	protected void onPostExecute(String result) {
		
		// TODO Auto-generated method stub
		super.onPostExecute(result);
		if(TextUtils.isEmpty(result)){
			Message m=mHandler.obtainMessage();
			m.what=-1;
			mHandler.sendMessage(m);
		}else{
			Message m=mHandler.obtainMessage();
			m.what=gid;
			m.obj=result;
			mHandler.sendMessage(m);
		}
	}

}
