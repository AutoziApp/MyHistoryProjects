package cn.com.mapuni.meshingtotal.service;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.gson.Gson;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.ByteArrayInputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import cn.com.mapuni.meshingtotal.R;
import cn.com.mapuni.meshingtotal.activity.notice.AlarmReportListActivity;
import cn.com.mapuni.meshingtotal.model.NoticeBean;


public class NoticeService extends Service {

	private Date startDate ;
	private String page_size;
	private boolean isShowNotice = false;
	private List<NoticeBean.DataBean>  list=new ArrayList<>();
	private final static long timediff = 60*60*1000;
	private boolean isStartTime = true;

	private Handler handler = new Handler(){
		public void handleMessage(Message msg) {
			try {
				if (endWork()) {
					whileStartTime();
					return;
				}
				if (!getTimeDifference() && !isShowNotice) {
					handler.sendEmptyMessage(0);
					return;
				}
				switch (msg.what) {
				case 1:
					List<NoticeBean.DataBean>  noticelist= (List<NoticeBean.DataBean>) msg.obj;
					NotificationManager nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
					//鑾峰彇PendingIntent
					Intent mainIntent = new Intent(NoticeService.this, AlarmReportListActivity.class);
//					mainIntent.putExtra("noticeListTran", new NoticeListTranstanceBean(noticelist));
					PendingIntent mainPendingIntent = PendingIntent.getActivity(NoticeService.this, 0, mainIntent, PendingIntent.FLAG_UPDATE_CURRENT);
					//鍒涘缓 Notification.Builder 瀵硅薄
					NotificationCompat.Builder builder = new NotificationCompat.Builder(NoticeService.this)
							.setSmallIcon(R.mipmap.yutu)
							//鐐瑰嚮閫氱煡鍚庤嚜鍔ㄦ竻闄?
							.setAutoCancel(true)
							.setContentTitle("济南市大气污染防治监管总平台")
							.setContentText("你有"+noticelist.size()+"条超标信息暂未查看")
							.setContentIntent(mainPendingIntent);
					//鍙戦?侀?氱煡
					nm.notify(1, builder.build());
					isShowNotice = false;
					handler.sendEmptyMessage(0);
					break;

				case 0:
					startCountTime();
					break;

				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		};
	};
	private SharedPreferences spf;

	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * 鑾峰彇鏈?鏂扮殑閫氱煡
	 */
	protected void startCountTime() {
			new Thread(new Runnable() {
				@Override
				public void run() {
					list.clear();
					final Date time_now = Calendar.getInstance().getTime();
					//鎺ュ彛璋冪敤鏂规硶涓?
					String url = "http://119.164.253.237:8033/service/appservice.asmx/GetAlarmReport";
					HttpUtils utils = new HttpUtils();
					utils.configCurrentHttpCacheExpiry(1000 * 5);
					utils.configTimeout(5 * 1000);//
					utils.configSoTimeout(5 * 1000);//
					RequestParams params = new RequestParams();// 娣诲姞鎻愪氦鍙傛暟
					params.addBodyParameter("userid", "63d10d11-a3e8-4188-b91c-0f22e0e15e65");
					utils.send(HttpRequest.HttpMethod.POST, url,params, new RequestCallBack<String>() {
						@Override
						public void onFailure(HttpException arg0, String arg1) {

						}

						@Override
						public void onSuccess(ResponseInfo<String> arg0) {
							String result = String.valueOf(arg0.result);
							Log.i("aaa",result);
							try {
								ByteArrayInputStream inputStream = new ByteArrayInputStream(result.getBytes());
								SAXReader reader = new SAXReader();
								Document document = reader.read(inputStream);
								Element root = document.getRootElement();
								String str=root.getText();
								Log.i("aaa",str);
								Gson gson=new Gson();
								NoticeBean bean=gson.fromJson(str,NoticeBean.class);
								List<NoticeBean.DataBean> dataBeens=bean.getData();
								if(dataBeens!=null&&dataBeens.size()>0) {
									for (NoticeBean.DataBean db : dataBeens) {
//										String time = db.getMonitorTime();
//										Date d = DateTimeHelper.parseStringToDate(time);
//										long oldTime = startDate.getTime();
//										if (oldTime < d.getTime()) {
											list.add(db);
//											setSettingLong(NOTICE_NEW, d.getTime());
//											setSettingInt("超标数据", 10 + i);
//										}
									}
								}
								if (list.size()>0) {
									isShowNotice = true;
									startDate = time_now;
									Message msg = Message.obtain();
									msg.what = 1;
									msg.obj = list;
									handler.sendMessage(msg);
								} else {
									startDate = Calendar.getInstance().getTime();
									handler.sendEmptyMessage(0);
								}

							} catch (Exception e) {

							}
						}
					});
				}
			}).start();


	}
	@Override
	public void onCreate() {
		super.onCreate();
		page_size = "5";
		spf = getApplicationContext().getSharedPreferences("setting",
				Context.MODE_PRIVATE);
	}
	public void setSettingLong(String tyle, long value) {
		Editor editor = spf.edit();
		editor.putLong(tyle, value);
		editor.commit();
	}
	public long getSettingLong(String tyle, long defValue) {
		if (spf.contains(tyle)){
			return spf.getLong(tyle, defValue);
		}
		return defValue;
	}
	public int getSettingInt(String tyle, int defValue) {
		if (spf.contains(tyle)){
			return spf.getInt(tyle, defValue);
		}
		return defValue;
	}
	public void setSettingInt(String tyle, int value) {
		Editor editor = spf.edit();
		editor.putInt(tyle, value);
		editor.commit();
	}
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
	     startDate = Calendar.getInstance().getTime();
	     whileStartTime();
		return super.onStartCommand(intent, flags, startId);
	}





	/**
	 * 鍒ゆ柇鏃堕棿宸槸鍚︿负鍗婁釜灏忔椂
	 * @return
	 */
	public boolean getTimeDifference() {
		Date endDate = Calendar.getInstance().getTime();
		if(startDate == null ||endDate == null){
            return false;
        }

        long timeLong = endDate.getTime() - startDate.getTime();
        if (timeLong >= timediff){
//        	startDate = endDate;
        	return true;
        }
        return false;
	}
	public void whileStartTime(){
		isStartTime = true;
		new Thread(new Runnable() {
			@Override
			public void run() {
				while (isStartTime) {
					try {
						Thread.sleep(5*1000);
						if (startWork()) {
							startDate = Calendar.getInstance().getTime();
							handler.sendEmptyMessage(0);
							isStartTime =false;
						}
					} catch (InterruptedException e) {
						e.printStackTrace();;
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}).start();
	}
	public int getHour()throws Exception{
		Date date = Calendar.getInstance().getTime();
		SimpleDateFormat sdf = new SimpleDateFormat(
				"HH");
		return Integer.valueOf(sdf.format(date));
	}
	public boolean startWork()throws Exception{
//		return (getHour() >= 7 && getHour() < 22);
		return true;
	}
	public boolean endWork()throws Exception{
//		return (getHour() >= 22 && getHour() < 24) || (getHour() >= 0 && getHour() < 7);
		return false;
	}
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		Intent intent = new Intent(this,NoticeService2.class);
		startService(intent);
	}
}
