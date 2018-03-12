package com.jy.environment.activity;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.view.ViewGroup.LayoutParams;
import android.widget.TextView;
import cn.jpush.android.api.JPushInterface;

import com.jy.environment.base.ActivityBase;
/**
 * 展示jpush界面（不知是否可用）
 * @author baiyuchuan
 *
 */
public class ShowPushMsgActivity extends ActivityBase {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TextView tv = new TextView(this);
        tv.setText("用户自定义打开的Activity");
        Intent intent = getIntent();
        if (null != intent) {
	        try {
	        	Bundle bundle = getIntent().getExtras();
	        	String json=bundle.getString("cn.jpush.android.EXTRA");
				JSONObject obj=new JSONObject(json);
				String msg=obj.getString("msg");
				
				String title = bundle.getString(JPushInterface.EXTRA_NOTIFICATION_TITLE);
		        String content = bundle.getString(JPushInterface.EXTRA_ALERT);
		        tv.setText("Title : " + title + "  " + "Content : " + content+" "+"msg :"+ msg);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	        
        }
        addContentView(tv, new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
    }

}
