/**
 * 
 */
package com.mapuni.android.base;

import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

/**FileName: GISLocation.java
 * Description: 获取当前位置工具类，使用单例模式
 * @author 
 * @Version 1.3.4
 * @Copyright 中科宇图天下科技有限公司
 * Create at: 2013-1-15 下午04:19:40
 */
public class GISLocation {

	/** 地理位置对象 */
	private Location location;
	/** 地理位置管理器 */
	private LocationManager Manager;
	private static GISLocation gisLocation =new GISLocation();
	private String provider;

   private GISLocation(){
	   
   }
   public static GISLocation getGISLocationInstance(){
	
	   return gisLocation;
   }
	
	/**
	 * Description: 获得经纬度 如果定位设备不可用返回null或最近一次的位置
	 * 
	 * @param context
	 *            上下文
	 * @return 得到地理位置对象 Location
	 * @author 王红娟 
	 * Create at: 2012-12-5 下午03:36:08
	 */
	public Location getLocation(Context context) {
	
	if (provider!=null) {
	Location mlocation = Manager.getLastKnownLocation(provider);
	if(mlocation!=null){
		location=mlocation;
	}else{
		if(provider.equals(LocationManager.GPS_PROVIDER)){
			Location mylocation = null;
			for(int i=0;i<100;i++){//重复调用gps定位获取当前位置
				mylocation=Manager.getLastKnownLocation(provider);
				if(mylocation!=null){
					location=mylocation;
					return location;
				}
			}
			
			if(Manager.isProviderEnabled(Manager.NETWORK_PROVIDER)){//在室内无法获取当前位置采用NET定位
				Location myLocation=Manager.getLastKnownLocation(Manager.NETWORK_PROVIDER);
				location=myLocation;
			}
			
			
		}
	}
		
	} else {//终端禁用GPS和网络
		/*Toast.makeText(context.getApplicationContext(), "GPS或网络不可用",
				Toast.LENGTH_SHORT).show();*/
		location=null;
	}

	return location;
}
	


	/**
	 * Description: 更新当前的地理位置信息
	 * 
	 * @param location
	 *            获取到的新的地理位置信息对象 void
	 * @author 王红娟 
	 * Create at: 2012-12-5 下午03:36:33
	 */
	public void updateLocation(Location location) {
		this.location = location;
	}

	/**
	 * 获取地理信息的监听
	 */
	private final LocationListener locationListener = new LocationListener() {

		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onProviderEnabled(String provider) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onProviderDisabled(String provider) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onLocationChanged(Location location) {
			updateLocation(location);
		}
	};
	/**
	 * 获取最好的provider 如果GPS和通信全被禁返回null
	 */
	protected String getBestProvider(){

	       // 构建位置查询条件  
     Criteria criteria = new Criteria();  
     // 查询精度：高  
     criteria.setAccuracy(Criteria.ACCURACY_FINE);  
     // 是否查询海拨：否  
     criteria.setAltitudeRequired(false);  
     // 是否查询方位角 : 否  
     criteria.setBearingRequired(false);  
     // 是否允许付费：是  
     criteria.setCostAllowed(true);  
     // 电量要求：低  
     criteria.setPowerRequirement(Criteria.POWER_LOW);  
     // 返回最合适的符合条件的 provider ，第 2 个参数为 true 说明 , 如果只有一个 provider 是有效的 , 则返回当前  
     // provider  
     String provider  = Manager.getBestProvider(criteria, true); 
     return provider;
	}
	public void removeLocationListener(){
		if(Manager!=null){
			Manager.removeUpdates(locationListener);
		}
	}
	/**
	 * Description:定位前准备 初始化LocationManager 并绑定LocationListener，在生命周期oncreate中调用
	 * @param context
	 * @author wanglg
	 * @Create at: 2013-5-22 下午1:04:00
	 */
	public void locationPrepare(Context context){
		 Manager=(LocationManager)context.getSystemService(Context.LOCATION_SERVICE);  
			//}
	     provider = getBestProvider();
	     if (provider!=null) {
	    		Manager.requestLocationUpdates(provider, 1000, 0,locationListener);
	    		
	     }
	}
}
