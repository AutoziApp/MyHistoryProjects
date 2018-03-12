package com.jy.environment.util;

import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;

import com.jy.environment.controls.AsyncTask;

public class HttpGetTask  extends AsyncTask<String, Void, String> {
	private Handler mHandler;
	private String murl;
	private int gid;
	
	public HttpGetTask(Handler handler,String url,int id){
		mHandler=handler;
		murl=url;
		gid=id;
	}
	
	@Override
	protected String doInBackground(String... arg0) {
			return ApiClient.connServerForResult(murl);

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
