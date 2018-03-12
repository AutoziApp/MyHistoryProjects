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
 * Description: ��ȡ��ǰλ�ù����࣬ʹ�õ���ģʽ
 * @author 
 * @Version 1.3.4
 * @Copyright �п���ͼ���¿Ƽ����޹�˾
 * Create at: 2013-1-15 ����04:19:40
 */
public class GISLocation {

	/** ����λ�ö��� */
	private Location location;
	/** ����λ�ù����� */
	private LocationManager Manager;
	private static GISLocation gisLocation =new GISLocation();
	private String provider;

   private GISLocation(){
	   
   }
   public static GISLocation getGISLocationInstance(){
	
	   return gisLocation;
   }
	
	/**
	 * Description: ��þ�γ�� �����λ�豸�����÷���null�����һ�ε�λ��
	 * 
	 * @param context
	 *            ������
	 * @return �õ�����λ�ö��� Location
	 * @author ����� 
	 * Create at: 2012-12-5 ����03:36:08
	 */
	public Location getLocation(Context context) {
	
	if (provider!=null) {
	Location mlocation = Manager.getLastKnownLocation(provider);
	if(mlocation!=null){
		location=mlocation;
	}else{
		if(provider.equals(LocationManager.GPS_PROVIDER)){
			Location mylocation = null;
			for(int i=0;i<100;i++){//�ظ�����gps��λ��ȡ��ǰλ��
				mylocation=Manager.getLastKnownLocation(provider);
				if(mylocation!=null){
					location=mylocation;
					return location;
				}
			}
			
			if(Manager.isProviderEnabled(Manager.NETWORK_PROVIDER)){//�������޷���ȡ��ǰλ�ò���NET��λ
				Location myLocation=Manager.getLastKnownLocation(Manager.NETWORK_PROVIDER);
				location=myLocation;
			}
			
			
		}
	}
		
	} else {//�ն˽���GPS������
		/*Toast.makeText(context.getApplicationContext(), "GPS�����粻����",
				Toast.LENGTH_SHORT).show();*/
		location=null;
	}

	return location;
}
	


	/**
	 * Description: ���µ�ǰ�ĵ���λ����Ϣ
	 * 
	 * @param location
	 *            ��ȡ�����µĵ���λ����Ϣ���� void
	 * @author ����� 
	 * Create at: 2012-12-5 ����03:36:33
	 */
	public void updateLocation(Location location) {
		this.location = location;
	}

	/**
	 * ��ȡ������Ϣ�ļ���
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
	 * ��ȡ��õ�provider ���GPS��ͨ��ȫ��������null
	 */
	protected String getBestProvider(){

	       // ����λ�ò�ѯ����  
     Criteria criteria = new Criteria();  
     // ��ѯ���ȣ���  
     criteria.setAccuracy(Criteria.ACCURACY_FINE);  
     // �Ƿ��ѯ��������  
     criteria.setAltitudeRequired(false);  
     // �Ƿ��ѯ��λ�� : ��  
     criteria.setBearingRequired(false);  
     // �Ƿ������ѣ���  
     criteria.setCostAllowed(true);  
     // ����Ҫ�󣺵�  
     criteria.setPowerRequirement(Criteria.POWER_LOW);  
     // ��������ʵķ��������� provider ���� 2 ������Ϊ true ˵�� , ���ֻ��һ�� provider ����Ч�� , �򷵻ص�ǰ  
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
	 * Description:��λǰ׼�� ��ʼ��LocationManager ����LocationListener������������oncreate�е���
	 * @param context
	 * @author wanglg
	 * @Create at: 2013-5-22 ����1:04:00
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
