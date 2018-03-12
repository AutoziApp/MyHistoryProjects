package cn.com.mapuni.gis.meshingtotal.util;

import android.os.Handler;
import android.util.Log;

import com.tianditu.android.maps.GeoPoint;

import java.io.File;
import java.io.IOException;
import java.util.List;

import cn.com.mapuni.meshing.base.interfaces.PathManager;
import okhttp3.Call;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class UploadFile {
	public static final MediaType MEDIA_TYPE_MARKDOWN
	  = MediaType.parse("application/octet-stream");
	
	
	 
	private static final OkHttpClient client = new OkHttpClient();



	protected static final String Tag = "UPLOADFILE";
	public static void uploadFile(final Handler uphandler,List<String> list,
			String edt_task_name_value,
			String sp_task_type_value,
			String mbwz_eit_value,
			String edt_patrol_content_value,GeoPoint point,String time) throws Exception{
		String url=PathManager.XCRW_URL;
		//String url="http://171.8.66.103:8473/JiNanhuanbaoms/task/insertaskFeedback.do";
		 uphandler.sendEmptyMessage(101);
		 double lat  = point.getLatitudeE6();
		 double lon  = point.getLongitudeE6();
		 
		 lat = lat/1000000;
		 lon = lon/1000000;
		 Log.i("bai","lat ："+lat);
		 Log.i("bai","lon ："+lon);
		 Log.i("bai","edt_task_name_value ："+edt_task_name_value);
		 Log.i("bai","mbwz_eit_value ："+mbwz_eit_value);
		 Log.i("bai","sp_task_type_value ："+sp_task_type_value);
		 Log.i("bai","edt_patrol_content_value ："+edt_patrol_content_value);
		 MultipartBody.Builder builder = new MultipartBody.Builder()
	         .setType(MultipartBody.FORM)
	            .addFormDataPart("EntName", edt_task_name_value)
				.addFormDataPart("userid", "123")
				.addFormDataPart("EntAddress", mbwz_eit_value)
				.addFormDataPart("PollutionType", sp_task_type_value)
				.addFormDataPart("WTJLSJ", time/*"2017-02-22"*/)
				.addFormDataPart("Latitude", lat+"")
				.addFormDataPart("Longitude",lon+"")
				.addFormDataPart("WTJL", edt_patrol_content_value);
		 for(String path:list){
			 File file=new File(path);
			 builder.addFormDataPart("file", file.getName(),RequestBody.create(MEDIA_TYPE_MARKDOWN, file));
		 }
		
		  
		  RequestBody requestBody=builder.build();
		 
		 Request request = new Request.Builder()
	         .url(url)
	         .post(requestBody)
	         .build();
	    
	    	 client.newCall(request).enqueue(new okhttp3.Callback() {
				
				@Override
				public void onResponse(Call call, Response response) throws IOException {
					String bodyStr = response.body().string();  
		            boolean ok = response.isSuccessful();  
		            Log.i(Tag,"bodyStr==="+bodyStr);
		            if(ok){  
                        Log.i(Tag,"上传成功:"+bodyStr);
                        uphandler.sendEmptyMessage(102);
                    }else{  
                    	Log.i(Tag,"上传失败1："+bodyStr);
                    	uphandler.sendEmptyMessage(103);
                    }  
				}
				
				@Override
				public void onFailure(Call arg0, IOException e) {
					// TODO Auto-generated method stub
					Log.i(Tag,"上传失败2："+e.toString());
					uphandler.sendEmptyMessage(103);
				}
			});
	    	 
	      
		
	}
	
	
	public static void uploadFile(final Handler uphandler,List<String> list,
			String name,
			String start_time,
			String end_time) throws Exception{
		String url=PathManager.XCRW_URL;
		 //String url="http://171.8.66.103:8473/JiNanhuanbaoms/task/insertaskFeedback.do";
		 uphandler.sendEmptyMessage(101);
		 MultipartBody.Builder builder = new MultipartBody.Builder()
	         .setType(MultipartBody.FORM)
	            .addFormDataPart("EntName", name)
				.addFormDataPart("userid", start_time)
				.addFormDataPart("EntAddress", end_time);
		 for(String path:list){
			 File file=new File(path);
			 builder.addFormDataPart("file", file.getName(),RequestBody.create(MEDIA_TYPE_MARKDOWN, file));
		 }
		
		  
		  RequestBody requestBody=builder.build();
		 
		 Request request = new Request.Builder()
	         .url(url)
	         .post(requestBody)
	         .build();
	    
	    	 client.newCall(request).enqueue(new okhttp3.Callback() {
				
				@Override
				public void onResponse(Call call, Response response) throws IOException {
					String bodyStr = response.body().string();  
		            boolean ok = response.isSuccessful();  
		            Log.i(Tag,"bodyStr==="+bodyStr);
		            if(ok){  
                        Log.i(Tag,"上传成功:"+bodyStr);
                        uphandler.sendEmptyMessage(102);
                    }else{  
                    	Log.i(Tag,"上传失败1："+bodyStr);
                    	uphandler.sendEmptyMessage(103);
                    }  
				}
				
				@Override
				public void onFailure(Call arg0, IOException e) {
					// TODO Auto-generated method stub
					Log.i(Tag,"上传失败2："+e.toString());
					uphandler.sendEmptyMessage(103);
				}
			});
	    	 
	      
		
	}
	
}
