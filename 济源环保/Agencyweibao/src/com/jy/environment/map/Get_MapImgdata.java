package com.jy.environment.map;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import org.json.JSONObject;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;

import com.jy.environment.controls.AsyncTask;
import com.jy.environment.controls.WeiBaoApplication;
import com.jy.environment.util.ApiClient;
import com.jy.environment.util.MyLog;
import com.jy.environment.webservice.UrlComponent;

public class Get_MapImgdata extends AsyncTask<String, Void, String> {
	private final String type_air="pic/air/";
	private final String type_temp="pic/temp/";
	private final String type_fog="pic/fog/";
	private final String type_haze="pic/haze/";
	private Handler mHandler;
	private int mtype=0;
	private String mname="";
	
	private Bitmap mbitmap=null;
	
	private byte[] b;
	
	public Get_MapImgdata(Handler handler,int type,String name){
		mHandler=handler;
		mtype=type;
		mname=name;
	}
	@Override
	protected String doInBackground(String... arg0) {
		String url="";
		if(mtype==1){
			url=UrlComponent.baseurl+type_air+mname;
		}else if(mtype==2)
		{
			url=UrlComponent.baseurl+type_temp+mname;
		}else if(mtype==3)
		{
			url=UrlComponent.baseurl+type_fog+mname;
		}else if(mtype==4)
		{
			url=UrlComponent.baseurl+type_haze+mname;
		}
		MyLog.i(">>>>>>>>>>urltype"+url);
		return ApiClient.connServerForResult(url+UrlComponent.token);
	}

	@Override
	protected void onPostExecute(String result) {
		
		
		
		if(TextUtils.isEmpty(result)){
			Log.v("function", "Get_MapImgdata -1");
			Message m=mHandler.obtainMessage();
			m.what=-1;
			mHandler.sendMessage(m);
		}else{
			try{
				Log.v("function", "Get_MapImgdata res"+mtype+"");
				JSONObject jsonobj = new JSONObject(result);
				String bitmapDATACODE = jsonobj.getString("data");
				
				Message m=mHandler.obtainMessage();
				m.what=mtype;
				//Log.v("getmapcode", bitmapDATACODE);
				b=hexStringToBytes(bitmapDATACODE);
				InputStream is=new ByteArrayInputStream(b);
				m.obj=BitmapFactory.decodeStream(is);
				mHandler.sendMessage(m);
				
			}catch(Exception e)
			{
				Message m=mHandler.obtainMessage();
				m.what=-1;
				mHandler.sendMessage(m);
			}
			
		}
		super.onPostExecute(result);
	}
	
	   public static byte[] hexStringToBytes(String hexString) {
	        if (hexString == null || hexString.equals("")) {
	            return null;
	        }
	        hexString = hexString.toUpperCase();
	        int length = hexString.length() / 2;
	        char[] hexChars = hexString.toCharArray();
	        byte[] d = new byte[length];
	        for (int i = 0; i < length; i++) {
	            int pos = i * 2;
	            d[i] = (byte) (charToByte(hexChars[pos]) << 4 | charToByte(hexChars[pos + 1]));
	            
	        }
	        return d;
	    }
	    private static byte charToByte(char c) {
	        return (byte) "0123456789ABCDEF".indexOf(c);
	    }

}
