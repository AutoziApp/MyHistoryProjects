/**
 * 
 */
package com.mapuni.android.service;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;

import com.mapuni.android.netprovider.WebServiceProvider;



import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.Looper;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

/**
 * @author yutu
 *
 */
public class NetworkService extends Service {
	private  final String TAG = "NetworkService" ;
	private NotificationManager NotificationManager;  
	private Notification notification;
	private PendingIntent pendingIntent = null; 
	
	/**
	 * 
	 */
	public NetworkService() {
		// TODO Auto-generated constructor stub
	}
	/**
	 * 有断点续传的上传函数
	 * @param serveraddr 服务器地址
	 * @param absFileName 文件在手机上的绝对路径
	 * @return 是否成功
	 */
	public boolean UploadFilePerfect(String serveraddr,String absFileName)
	{
		String surl =serveraddr;//"http://192.168.5.112";
		String namespace = "http://tempuri.org/";
		String url = surl +"/WebService/MobileEnforcementWebService.asmx";
		try {
			absFileName = new String(absFileName.getBytes(), "UTF-8");
		} catch (UnsupportedEncodingException e1) {
			Log.e(TAG, e1.getMessage());
		}
		
		ByteArrayOutputStream baos = null;
		FileInputStream fis        = null;
		boolean result=false;
		
		int a=absFileName.lastIndexOf('/');
		int b=absFileName.lastIndexOf('/', a-1);
		int c=absFileName.lastIndexOf('/', b-1);
		int d=absFileName.lastIndexOf('/', c-1);
		String filename=absFileName.substring(d+1);
		
		
		
		ArrayList<HashMap<String, Object>> params0 = new ArrayList<HashMap<String,Object>>();
		HashMap<String, Object> param0 = new HashMap<String, Object>();
		param0.put("Path", filename);
		params0.add(param0);
		
		int finishblocks=0;
		
		try {
			Object resultResponseObj0=WebServiceProvider.callWebService(namespace, 
					"GetProgress", params0, url, WebServiceProvider.RETURN_INT, true);
			if(null!=resultResponseObj0)
				finishblocks = Integer.parseInt(resultResponseObj0.toString());
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		try {
			//StringBuffer strbuf=new StringBuffer();
			File absFile = new File(absFileName);
			fis = new FileInputStream(absFileName);	
			baos = new ByteArrayOutputStream();

			int count = 0;
			//int i=0;
			boolean end=false;
			for(int i=0;i<(int)absFile.length()/(1024*500)+1;i++){
				String attachmentData="";
				//nm.notify("正在上传"+i+"块", 0, null);
				if(i==(int)absFile.length()/(1024*500)){
					end=true;
					byte[] buffers = new byte[(int)absFile.length()%(1024*500)];
					
					count =fis.read(buffers);
					
					//baos.write(buffers, 0, count);
					attachmentData=Base64.encodeToString(buffers, Base64.DEFAULT);
//					baos.flush();
//					baos.close();
//					fis.close();
//					if(!attachmentData.endsWith("==")){
//						attachmentData=attachmentData.substring(0,attachmentData.length()-3);
//						attachmentData=attachmentData.concat("==");
//					}
				}else{
					byte[] buffer = new byte[1024*500];
					
					count =fis.read(buffer);
					
					//baos.write(buffer,0,count);
					attachmentData=Base64.encodeToString(buffer, Base64.DEFAULT);
					
				}
				//String rwname=absFileName.substring(absFileName.lastIndexOf("/") + 1)+"."+i;
				//count+=1000*1024;
				//strbuf.append(str);
				if(i>=finishblocks)
				{
				//String filename = absFileName;//.substring( 
						//absFileName.indexOf(RWBH));
				//this.convertStringToLocalFile1("pash"+i,attachmentData);
				/*调用webserice的参数设置*/
				ArrayList<HashMap<String, Object>> params = new ArrayList<HashMap<String,Object>>();
			
				
				HashMap<String, Object> param = new HashMap<String, Object>();
				param.put("fileNameWithPath", filename+"."+i);
				params.add(param);
				
				param = new HashMap<String, Object>();
				param.put("strValue", attachmentData);
				params.add(param);
				
				param = new HashMap<String, Object>();
				param.put("isEnd", end);
				params.add(param);
				
				
//				Log.i("s", ""+WSUtils.callWebService(namespace, 
//						"UploadPartFile", params, url, WSUtils.RETURN_BOOLEAN, true));
				
			//	notification.icon = R.drawable.stat_sys_download_anim0;
				notification.tickerText = "正在上传第"+i+"块";
				notification.flags = Notification.FLAG_ONGOING_EVENT;//在通知栏上点击此通知后自动清除此通知 
				notification.flags |= Notification.FLAG_NO_CLEAR;
				notification.setLatestEventInfo(this, "正在上传第"+i+"块", "正在上传第"+i+"块", pendingIntent);

				// 发出通知
				NotificationManager.notify(0, notification);
				/*Toast.makeText(NetworkService.this, "正在上传第"+i+"块", Toast.LENGTH_LONG).show();  */
				
				
				Object resultResponseObj=WebServiceProvider.callWebService(namespace, 
						"UploadPartFile", params, url, WebServiceProvider.RETURN_BOOLEAN, true);
				boolean resultResponse=false;
				if(null!=resultResponseObj){
					resultResponse = (Boolean)resultResponseObj;
				}
				
				if(resultResponse){
					result=resultResponse;
				}else{
					result=false;
					break;
				}
				}
			}
			//this.convertBase64StringToLocalFile(strbuf.t, "/sdcard/mapuni/MobileEnforcement/rw/", "rwmc");
			//while ((count = fis.read(buffer,1,1000)) >= 0) {
				
			//}
			Log.v(TAG, absFileName + "\t成功转化为Base64 Bytes");
		} catch (FileNotFoundException e) {
			Log.e(TAG, e.getMessage());
			return false;
		} catch (IOException e) {
			Log.e(TAG, e.getMessage());
			return false;
		} finally {
			try {
				baos.flush();
				baos.close();
				fis.close();
				
			} catch (IOException e) {
				Log.e(TAG, e.getMessage());
				return false;
			}
		}
		//return Base64.encodeToString(baos.toByteArray(), Base64.DEFAULT);
		return result;
		
	}

	/* (non-Javadoc)
	 * @see android.app.Service#onBind(android.content.Intent)
	 */
	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}
	
	 @Override  
	     public void onCreate() {   
	         Log.v(TAG, "NetworkService onCreate");   
	         super.onCreate();   
	     }   
	        
	     @Override  
	     public void onStart(Intent intent, int startId) {   
	         Log.v(TAG, "NetworkService onStart");   
	         super.onStart(intent, startId);   
	     }   
	        
	     @Override  
	     public int onStartCommand(final Intent intent, int flags, int startId) {
	    	 this.NotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
				this.notification = new Notification();
				pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);/**/
				
	    	new Thread(new Runnable() 
	    	{
	    		@Override
				public void run() {
	    			Looper.prepare();
	    	 String mname= intent.getStringExtra("method");
	    	if(mname.indexOf("uploadfile")!=-1)
	    	{
	    		int filenum=0;
	    		filenum=intent.getIntExtra("filenum", 0);
	    		String url=intent.getStringExtra("url");
	    		for(int k=0;k<filenum;k++)
	    		{
	    		String filename=intent.getStringExtra("file"+k);
	    		UploadFilePerfect(url,filename);
	    		
	    		}
	    		}
	    	Looper.loop();
	    		}
	    	}).start();
	         Log.v(TAG, "NetworkService onStartCommand");   
	         return super.onStartCommand(intent, flags, startId);   
	     }   

}
