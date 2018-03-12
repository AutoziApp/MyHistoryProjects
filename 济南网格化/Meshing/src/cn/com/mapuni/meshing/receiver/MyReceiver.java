package cn.com.mapuni.meshing.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;



import com.google.gson.Gson;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.mapuni.android.base.controls.loading.YutuLoading;
import com.mapuni.android.base.interfaces.PathManager;
import com.mapuni.android.base.util.DisplayUitl;

import java.util.ArrayList;
import java.util.Iterator;

import org.json.JSONException;
import org.json.JSONObject;

import cn.com.mapuni.meshing.activity.LoginActivity;
import cn.com.mapuni.meshing.activity.MainActivity;
import cn.com.mapuni.meshing.activity.wd_activity.XCPlanListActivity;
import cn.com.mapuni.meshing.model.XCPlanModel;
import cn.com.mapuni.meshing.model.XCPlanModel.RowsBean;
import cn.jpush.android.api.JPushInterface;

/**
 * 鑷畾涔夋帴鏀跺櫒
 * 
 * 濡傛灉涓嶅畾涔夎繖涓� Receiver锛屽垯锛�
 * 1) 榛樿鐢ㄦ埛浼氭墦寮�涓荤晫闈�
 * 2) 鎺ユ敹涓嶅埌鑷畾涔夋秷鎭�
 */
public class MyReceiver extends BroadcastReceiver {
	private static final String TAG = "JPush";

	@Override
	public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getExtras();
		Log.d(TAG, "[MyReceiver] onReceive - " + intent.getAction() + ", extras: " + printBundle(bundle));
        if (JPushInterface.ACTION_REGISTRATION_ID.equals(intent.getAction())) {
            String regId = bundle.getString(JPushInterface.EXTRA_REGISTRATION_ID);
            Log.d(TAG, "[MyReceiver] 鎺ユ敹Registration Id : " + regId);
            //send the Registration Id to your server...
                        
        } else if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent.getAction())) {
        	Log.d(TAG, "[MyReceiver] 鎺ユ敹鍒版帹閫佷笅鏉ョ殑鑷畾涔夋秷鎭�: " + bundle.getString(JPushInterface.EXTRA_MESSAGE));
//        	processCustomMessage(context, bundle);
        
        } else if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent.getAction())) {
            Log.d(TAG, "[MyReceiver] 鎺ユ敹鍒版帹閫佷笅鏉ョ殑閫氱煡");
            int notifactionId = bundle.getInt(JPushInterface.EXTRA_NOTIFICATION_ID);
            Log.d(TAG, "[MyReceiver] 鎺ユ敹鍒版帹閫佷笅鏉ョ殑閫氱煡鐨処D: " + notifactionId);
        	
        } else if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent.getAction())) {
            Log.d(TAG, "[MyReceiver] 鐢ㄦ埛鐐瑰嚮鎵撳紑浜嗛�氱煡");   
            
            judgeSession(context,bundle);
//        	Intent i = new Intent(context, MainActivity.class);
//        	i.putExtras(bundle);
//        	i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP );
//        	context.startActivity(i);
            
        	
        } else if (JPushInterface.ACTION_RICHPUSH_CALLBACK.equals(intent.getAction())) {
            Log.d(TAG, "[MyReceiver] 鐢ㄦ埛鏀跺埌鍒癛ICH PUSH CALLBACK: " + bundle.getString(JPushInterface.EXTRA_EXTRA));
            //鍦ㄨ繖閲屾牴鎹� JPushInterface.EXTRA_EXTRA 鐨勫唴瀹瑰鐞嗕唬鐮侊紝姣斿鎵撳紑鏂扮殑Activity锛� 鎵撳紑涓�涓綉椤电瓑..
        	
        } else if(JPushInterface.ACTION_CONNECTION_CHANGE.equals(intent.getAction())) {
        	boolean connected = intent.getBooleanExtra(JPushInterface.EXTRA_CONNECTION_CHANGE, false);
        	Log.w(TAG, "[MyReceiver]" + intent.getAction() +" connected state change to "+connected);
        } else {
        	Log.d(TAG, "[MyReceiver] Unhandled intent - " + intent.getAction());
        }
	}

	// 鎵撳嵃鎵�鏈夌殑 intent extra 鏁版嵁
	private static String printBundle(Bundle bundle) {
		StringBuilder sb = new StringBuilder();
		for (String key : bundle.keySet()) {
			if (key.equals(JPushInterface.EXTRA_NOTIFICATION_ID)) {
				sb.append("\nkey:" + key + ", value:" + bundle.getInt(key));
			}else if(key.equals(JPushInterface.EXTRA_CONNECTION_CHANGE)){
				sb.append("\nkey:" + key + ", value:" + bundle.getBoolean(key));
			} else if (key.equals(JPushInterface.EXTRA_EXTRA)) {
				if (TextUtils.isEmpty(bundle.getString(JPushInterface.EXTRA_EXTRA))) {
					Log.i(TAG, "This message has no Extra data");
					continue;
				}

				try {
					JSONObject json = new JSONObject(bundle.getString(JPushInterface.EXTRA_EXTRA));
					Iterator<String> it =  json.keys();

					while (it.hasNext()) {
						String myKey = it.next().toString();
						sb.append("\nkey:" + key + ", value: [" +
								myKey + " - " +json.optString(myKey) + "]");
					}
				} catch (JSONException e) {
					Log.e(TAG, "Get message extra JSON error!");
				}

			} else {
				sb.append("\nkey:" + key + ", value:" + bundle.getString(key));
			}
		}
		return sb.toString();
	}
	
	private void judgeSession(final Context context,final Bundle bundle) {
		
		StringBuilder builder = new StringBuilder("?sessionId=");
		builder.append(
				DisplayUitl.readPreferences(context,"lastuser", "sessionId"));				
		HttpUtils utils = new HttpUtils();
		utils.configCurrentHttpCacheExpiry(1000 * 5);
		utils.configTimeout(60 * 1000);// 连接超时 //指的是连接一个url的连接等待时间。
		utils.configSoTimeout(60 * 1000);// 获取数据超时
											
		String url = PathManager.JUDGESESSION_URL_JINAN+ builder.toString();
		utils.send(HttpMethod.GET, url, new RequestCallBack<String>() {
			@Override
			public void onFailure(HttpException arg0, String arg1) {				
				Intent i = new Intent(context, LoginActivity.class);
	        	i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP );
	        	context.startActivity(i);
			}

			@Override
			public void onSuccess(ResponseInfo<String> arg0) {				
				Log.i("utils", String.valueOf(arg0.result));
				String resoult = String.valueOf(arg0.result);
				if("-1".equals(resoult)){//session无效
					Intent i = new Intent(context, LoginActivity.class);
		        	i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP );
		        	context.startActivity(i);
				}else if("1".equals(resoult)){//session有效
					String str=bundle.getString(JPushInterface.EXTRA_EXTRA);
//					String jsonstr=str.replaceAll("\\", "");
					try {
						JSONObject jsonObject=new JSONObject(str);						
						String patrolPlan=jsonObject.optString("newsId");						
						if("patrolPlan".equals(patrolPlan)){
							Intent i = new Intent(context, XCPlanListActivity.class);
				        	i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP );
				        	context.startActivity(i);
						}else {
							Intent i = new Intent(context, MainActivity.class);
				        	i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP );
				        	context.startActivity(i);
						}
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				
			}

		});
	}
	
//	//send msg to MainActivity
//	private void processCustomMessage(Context context, Bundle bundle) {
//		if (MainActivity.isForeground) {
//			String message = bundle.getString(JPushInterface.EXTRA_MESSAGE);
//			String extras = bundle.getString(JPushInterface.EXTRA_EXTRA);
//			Intent msgIntent = new Intent(MainActivity.MESSAGE_RECEIVED_ACTION);
//			msgIntent.putExtra(MainActivity.KEY_MESSAGE, message);
//			if (!ExampleUtil.isEmpty(extras)) {
//				try {
//					JSONObject extraJson = new JSONObject(extras);
//					if (extraJson.length() > 0) {
//						msgIntent.putExtra(MainActivity.KEY_EXTRAS, extras);
//					}
//				} catch (JSONException e) {
//
//				}
//
//			}
//			context.sendBroadcast(msgIntent);
//		}
//	}
}
