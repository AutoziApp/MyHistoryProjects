package cn.com.mapuni.meshing.location.service;

import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.LocationClientOption.LocationMode;
import android.content.Context;

/**
 * 
 * @author baidu
 *
 */
public class BaiduLocationService {
	private LocationClient client = null;
	private LocationClientOption mOption,DIYoption;
	private Object  objLock = new Object();

	/***
	 * 
	 * @param locationContext
	 */
	public BaiduLocationService(Context locationContext){
		synchronized (objLock) {
			if(client == null){
				client = new LocationClient(locationContext);
				client.setLocOption(getDefaultLocationClientOption());
			}
		}
	}
	
	/***
	 * 
	 * @param listener
	 * @return
	 */
	
	public boolean registerListener(BDLocationListener listener){
		boolean isSuccess = false;
		if(listener != null){
			client.registerLocationListener(listener);
			isSuccess = true;
		}
		return  isSuccess;
	}
	
	public void unregisterListener(BDLocationListener listener){
		if(listener != null){
			client.unRegisterLocationListener(listener);
		}
	}
	
	/***
	 * 
	 * @param option
	 * @return isSuccessSetOption
	 */
	public boolean setLocationOption(LocationClientOption option){
		boolean isSuccess = false;
		if(option != null){
			if(client.isStarted())
				client.stop();
			DIYoption = option;
			client.setLocOption(option);
		}
		return isSuccess;
	}
	
	public LocationClientOption getOption(){
		return DIYoption;
	}
	/***
	 * 
	 * @return DefaultLocationClientOption
	 */
	public LocationClientOption getDefaultLocationClientOption(){
		if(mOption == null){
			mOption = new LocationClientOption();
			mOption.setLocationMode(LocationMode.Hight_Accuracy);//��ѡ��Ĭ�ϸ߾��ȣ����ö�λģʽ���߾��ȣ��͹��ģ����豸
			mOption.setCoorType("bd09ll");//��ѡ��Ĭ��gcj02�����÷��صĶ�λ�������ϵ�������ϰٶȵ�ͼʹ�ã���������Ϊbd09ll;
			mOption.setScanSpan(0);//��ѡ��Ĭ��0��������λһ�Σ����÷���λ����ļ����Ҫ���ڵ���1000ms������Ч��
		    mOption.setIsNeedAddress(true);//��ѡ�������Ƿ���Ҫ��ַ��Ϣ��Ĭ�ϲ���Ҫ
		    mOption.setIsNeedLocationDescribe(true);//��ѡ�������Ƿ���Ҫ��ַ����
		    mOption.setNeedDeviceDirect(false);//��ѡ�������Ƿ���Ҫ�豸������
		    mOption.setLocationNotify(false);//��ѡ��Ĭ��false�������Ƿ�gps��Чʱ����1S1��Ƶ�����GPS���
		    mOption.setIgnoreKillProcess(true);//��ѡ��Ĭ��true����λSDK�ڲ���һ��SERVICE�����ŵ��˶������̣������Ƿ���stop��ʱ��ɱ��������̣�Ĭ�ϲ�ɱ��   
		    mOption.setIsNeedLocationDescribe(true);//��ѡ��Ĭ��false�������Ƿ���Ҫλ�����廯�����������BDLocation.getLocationDescribe��õ�����������ڡ��ڱ����찲�Ÿ�����
		    mOption.setIsNeedLocationPoiList(true);//��ѡ��Ĭ��false�������Ƿ���ҪPOI�����������BDLocation.getPoiList��õ�
		    mOption.SetIgnoreCacheException(false);//��ѡ��Ĭ��false�������Ƿ��ռ�CRASH��Ϣ��Ĭ���ռ�
		    
		    mOption.setIsNeedAltitude(false);//��ѡ��Ĭ��false�����ö�λʱ�Ƿ���Ҫ������Ϣ��Ĭ�ϲ���Ҫ����������λ�汾������
		 
		}
		return mOption;
	}
	
	public void start(){
		synchronized (objLock) {
			if(client != null && !client.isStarted()){
				client.start();
			}
		}
	}
	public void stop(){
		synchronized (objLock) {
			if(client != null && client.isStarted()){
				client.stop();
			}
		}
	}
	
	public boolean requestHotSpotState(){
		
		return client.requestHotSpotState();
		
	}
	
}
