package cn.com.mapuni.meshing.activity;


import cn.com.mapuni.meshing.location.service.BaiduLocationService;
import cn.jpush.android.api.JPushInterface;

import com.baidu.mapapi.SDKInitializer;

import android.app.Application;
import android.app.Service;
import android.os.Vibrator;

/**
 * 涓籄pplication锛屾墍鏈夌櫨搴﹀畾浣峉DK鐨勬帴鍙ｈ鏄庤鍙傝�冪嚎涓婃枃妗ｏ細http://developer.baidu.com/map/loc_refer/index.html
 *
 * 鐧惧害瀹氫綅SDK瀹樻柟缃戠珯锛歨ttp://developer.baidu.com/map/index.php?title=android-locsdk
 * 
 * 鐩存帴鎷疯礉com.baidu.location.service鍖呭埌鑷繁鐨勫伐绋嬩笅锛岀畝鍗曢厤缃嵆鍙幏鍙栧畾浣嶇粨鏋滐紝涔熷彲浠ユ牴鎹甦emo鍐呭鑷灏佽
 */
public class LocationApplication extends Application {
	public BaiduLocationService baiduLocationService;
    public Vibrator mVibrator;
    @Override
    public void onCreate() {
    	
        super.onCreate();
        /***
         * 鍒濆鍖栧畾浣峴dk锛屽缓璁湪Application涓垱寤�
         */
        baiduLocationService = new BaiduLocationService(getApplicationContext());
        mVibrator =(Vibrator)getApplicationContext().getSystemService(Service.VIBRATOR_SERVICE);
        SDKInitializer.initialize(getApplicationContext());  
        JPushInterface.setDebugMode(false); 	// 璁剧疆寮�鍚棩蹇�,鍙戝竷鏃惰鍏抽棴鏃ュ織
        JPushInterface.init(this);     		// 鍒濆鍖� JPush
       
    }
}
