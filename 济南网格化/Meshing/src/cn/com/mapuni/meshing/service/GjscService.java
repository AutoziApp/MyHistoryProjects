package cn.com.mapuni.meshing.service;

import java.io.IOException;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.Call;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import cn.com.mapuni.meshing.activity.gis.MapBinder;
import cn.com.mapuni.meshing.activity.gis.LocationService.CallBackAdrr;

import com.mapuni.android.base.interfaces.PathManager;
import com.mapuni.android.base.util.DisplayUitl;
import com.tianditu.android.maps.GeoPoint;
import com.tianditu.android.maps.MyLocationOverlay;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

public class GjscService extends Service{
	private String lon="";
	private String lat="";
	private String address="";
	private Handler mHandler;
	private HandlerThread mThread;
	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public void onCreate() {
		// TODO Auto-generated method stubgg
		super.onCreate();
		// TODO Auto-generated method stub
		mThread = new HandlerThread("Thread_gjsc_" + (int) (Math.random() * 10));
		mThread.start();
		mHandler = new Handler(mThread.getLooper());
		// 在 mThread 线程发起定位
				mHandler.post(new Runnable() {
					@Override
					public void run() {
						while (true) {
							try {
								Thread.sleep(1800*1000);
								getPoint();
								
								String url = PathManager.SCJW;
								MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM)
										.addFormDataPart("sessionId",DisplayUitl.readPreferences(GjscService.this, "lastuser", "sessionId"))
										.addFormDataPart("userId", DisplayUitl.readPreferences(GjscService.this,"lastuser","user_id"))
										.addFormDataPart("userAccount", DisplayUitl.readPreferences(GjscService.this,"lastuser","user_Account"))
										.addFormDataPart("userName", DisplayUitl.readPreferences(GjscService.this,"lastuser","user_Name"))
										.addFormDataPart("gridCode", DisplayUitl.readPreferences(GjscService.this,"lastuser","organization_code"))
										.addFormDataPart("gridName", DisplayUitl.readPreferences(GjscService.this,"lastuser","organization_name"))
										.addFormDataPart("x",lon)
										.addFormDataPart("y",lat)
										.addFormDataPart("address", address);
								RequestBody requestBody = builder.build();
								Request request = new Request.Builder().url(url).post(requestBody).build();
								OkHttpClient client = new OkHttpClient();
								client.newCall(request).enqueue(new okhttp3.Callback() {

									@Override
									public void onResponse(Call call, Response response) throws IOException {
										String bodyStr = response.body().string();
										try {
											JSONObject jsonObject = new JSONObject(bodyStr);
											String status = jsonObject.getString("status");
											if (status.contains("200")) {
												Log.i("lfwang", "上传成功:" + bodyStr);
											} else {
												Log.i("lfwang", "上传失败：" + bodyStr);
											}
										} catch (JSONException e) {
											// TODO Auto-generated catch block
											Log.i("lfwang", "接口返回数据异常,上传失败：" + bodyStr);
											e.printStackTrace();
										}
									}

									@Override
									public void onFailure(Call arg0, IOException e) {
										// TODO Auto-generated method stub
										Log.i("lfwang", "无网络连接：" + e.toString());
									}
								});
							} catch (Exception e) {
								// TODO: handle exception
								Log.i("lfwang", "上传异常---------");
							}
						}
					}
				});
	/*	new Thread(new Runnable() {
			
			@Override
			public void run() {
				while(true){
				// TODO Auto-generated method stub
				try {
					Thread.sleep(300*1000);
					getPoint();
					if (lat.isEmpty()||lon.isEmpty()) {
						continue ;
					}
					String url=PathManager.SCJW;
					MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM)
							.addFormDataPart("sessionId",DisplayUitl.readPreferences(GjscService.this, "lastuser", "sessionId"))
							.addFormDataPart("userId", DisplayUitl.readPreferences(GjscService.this,"lastuser","user_id"))
							.addFormDataPart("userAccount", DisplayUitl.readPreferences(GjscService.this,"lastuser","user_Account"))
							.addFormDataPart("userName", DisplayUitl.readPreferences(GjscService.this,"lastuser","user_Name"))
							.addFormDataPart("gridCode", DisplayUitl.readPreferences(GjscService.this,"lastuser","organization_code"))
							.addFormDataPart("gridName", DisplayUitl.readPreferences(GjscService.this,"lastuser","organization_name"))
							.addFormDataPart("x",lon)
							.addFormDataPart("y",lat)
							.addFormDataPart("address", address);
					RequestBody requestBody = builder.build();
					Request request = new Request.Builder().url(url).post(requestBody).build();
					OkHttpClient client = new OkHttpClient();
					client.newCall(request).enqueue(new okhttp3.Callback() {

						@Override
						public void onResponse(Call call, Response response) throws IOException {
							String bodyStr = response.body().string();
							try {
								JSONObject jsonObject = new JSONObject(bodyStr);
								String status = jsonObject.getString("status");
								if (status.contains("200")) {
									Log.i("lfwang", "执行成功:" + bodyStr);
								} else {
									Log.i("lfwang", "执行失败：" + bodyStr);
								}
							} catch (JSONException e) {
								// TODO Auto-generated catch block
								Log.i("lfwang", "接口返回数据异常,执行失败：" + bodyStr);
								e.printStackTrace();
							}
						}

						@Override
						public void onFailure(Call arg0, IOException e) {
							// TODO Auto-generated method stub
							Log.i("lfwang", "无网络连接：" + e.toString());
						}
					}); 
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				}
			}
		}).start();*/
	
		
	}
	
	private void getPoint(){
		MapBinder.getInstance().getBinder().getLocationAddr(new CallBackAdrr() {

			@Override
			public void callbackadrr(String r, GeoPoint mGeoPoint) {
				address=r;
				lon =mGeoPoint.getLongitudeE6()/1E6+"";
				lat =mGeoPoint.getLatitudeE6()/1E6+"";
				
			}
		});
	}
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		// 清空
		mHandler.removeCallbacksAndMessages(null);
		// 停止线程
		mThread.getLooper().quit();
	}
}
