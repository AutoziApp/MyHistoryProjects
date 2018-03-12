package com.jy.environment.model;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.SimpleTimeZone;
import java.util.logging.SimpleFormatter;

import android.app.Activity;
import android.content.Context;
import android.os.Environment;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.util.Log;

/**
 * 终端设备信息类
 * 
 */
public class CollectDeviceInfo {
	private final String TAG = CollectDeviceInfo.class.getSimpleName();
	// /////////////////////////////////////////////// 设备规格
	private final String clientType = "mobile";
	private String manufacturer = "";// 厂商
	private String deviceId;// 设备id
	private String deviceBrand = "";// 品牌
	private String deviceModel = "";// 型号
	private String deviceBoard = "";// 板卡
	

	private String IMEI = "";// 设备串号
	// ////////////////////////////////////////////// 设备软件
	private String deviceKernelName;// 系统内核名称
	private String deviceKernelVersion;// 系统内核版本
	/** 系统名称 */
	private final String osName = "android";
	/** 系统版本 */
	private String osVersion = "";
	/** 系统SDK版本 */
	private int sdkVersion = 0;

	// ////////////////////////////////////////////// SIM卡信息
	private String IMSI = "";// 移动用户唯一标示
	private String telNumber = "";// 手机号码
	private String simSN = "";// SIM卡序列号

	// ////////////////////////////////////////////// 网络信息
	private String networkOperator = "";// 网络运营商代码
	private String networkOperatorName = "";// 网络运营商名称
	private String networkCountryIso = "";// 网络所属国家
	private int networkType = 0;// 网络类型
	private String networkTypeName = "";// 网络类型名称
	private String connectionType = "";// 联网类型
	// ////////////////////////////////////////////// 存储信息
	private long internalMemorySize;// 机身存储大小
	private long externalMemorySize;// 扩展卡存储大小
	
	private String productId;  //渠道号
	Map<String, Object> params = new HashMap<String, Object>();

	public CollectDeviceInfo() {
	}

	/**
	 * @return the clientType
	 */
	public String getClientType() {
		return clientType;
	}

	/**
	 * @return the manufacturer
	 */
	public String getManufacturer() {
		return manufacturer;
	}

	/**
	 * @param manufacturer
	 *            the manufacturer to set
	 */
	public void setManufacturer(String manufacturer) {
		this.manufacturer = manufacturer;
	}

	/**
	 * @return the deviceId
	 */
	public String getDeviceId() {
		return deviceId;
	}

	/**
	 * @param deviceId
	 *            the deviceId to set
	 */
	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	/**
	 * @return the deviceName
	 */
	public String getDeviceBrand() {
		return deviceBrand;
	}

	/**
	 * @param deviceName
	 *            the deviceName to set
	 */
	public void setDeviceBrand(String deviceName) {
		this.deviceBrand = deviceName;
	}

	/**
	 * @return the deviceModel
	 */
	public String getDeviceModel() {
		return deviceModel;
	}

	/**
	 * @param deviceModel
	 *            the deviceModel to set
	 */
	public void setDeviceModel(String deviceModel) {
		this.deviceModel = deviceModel;
	}

	/**
	 * @return the deviceSystemName
	 */
	public String getDeviceKernelName() {
		return deviceKernelName;
	}

	/**
	 * @param deviceSystemName
	 *            the deviceSystemName to set
	 */
	public void setDeviceKernelName(String deviceSystemName) {
		this.deviceKernelName = deviceSystemName;
	}

	/**
	 * @return the deviceSystemVersion
	 */
	public String getDeviceKernelVersion() {
		return deviceKernelVersion;
	}

	/**
	 * @param deviceSystemVersion
	 *            the deviceSystemVersion to set
	 */
	public void setDeviceKernelVersion(String deviceSystemVersion) {
		this.deviceKernelVersion = deviceSystemVersion;
	}

	/**
	 * @return the bOARD
	 */
	public String getDeviceBoard() {
		return deviceBoard;
	}

	/**
	 * @param bOARD
	 *            the bOARD to set
	 */
	public void setDeviceBoard(String bOARD) {
		deviceBoard = bOARD;
	}

	/**
	 * @return the iMEI
	 */
	public String getIMEI() {
		return IMEI;
	}

	/**
	 * @param iMEI
	 *            the iMEI to set
	 */
	public void setIMEI(String iMEI) {
		IMEI = iMEI;
	}

	/**
	 * @return the osName
	 */
	public String getOsName() {
		return osName;
	}

	/**
	 * @return the osVersion
	 */
	public String getOsVersion() {
		return osVersion;
	}

	/**
	 * @param osVersion
	 *            the osVersion to set
	 */
	public void setOsVersion(String osVersion) {
		this.osVersion = osVersion;
	}

	/**
	 * @return the sdkVersion
	 */
	public int getSdkVersion() {
		return sdkVersion;
	}

	/**
	 * @param sdkVersion
	 *            the sdkVersion to set
	 */
	public void setSdkVersion(int sdkVersion) {
		this.sdkVersion = sdkVersion;
	}

	/**
	 * @return the iMSI
	 */
	public String getIMSI() {
		return IMSI;
	}

	/**
	 * @param iMSI
	 *            the iMSI to set
	 */
	public void setIMSI(String iMSI) {
		IMSI = iMSI;
	}

	/**
	 * @return the telNumber
	 */
	public String getTelNumber() {
		return telNumber;
	}

	/**
	 * @param telNumber
	 *            the telNumber to set
	 */
	public void setTelNumber(String telNumber) {
		this.telNumber = telNumber;
	}

	/**
	 * @return the simSN
	 */
	public String getSimSN() {
		return simSN;
	}

	/**
	 * @param simSN
	 *            the simSN to set
	 */
	public void setSimSN(String simSN) {
		this.simSN = simSN;
	}

	/**
	 * @return the networkOperator
	 */
	public String getNetworkOperator() {
		return networkOperator;
	}

	/**
	 * @param networkOperator
	 *            the networkOperator to set
	 */
	public void setNetworkOperator(String networkOperator) {
		this.networkOperator = networkOperator;
	}

	/**
	 * @return the networkOperatorName
	 */
	public String getNetworkOperatorName() {
		return networkOperatorName;
	}

	/**
	 * @param networkOperatorName
	 *            the networkOperatorName to set
	 */
	public void setNetworkOperatorName(String networkOperatorName) {
		this.networkOperatorName = networkOperatorName;
	}

	/**
	 * @return the networkCountryIso
	 */
	public String getNetworkCountryIso() {
		return networkCountryIso;
	}

	/**
	 * @param networkCountryIso
	 *            the networkCountryIso to set
	 */
	public void setNetworkCountryIso(String networkCountryIso) {
		this.networkCountryIso = networkCountryIso;
	}

	/**
	 * @return the networkType
	 */
	public int getNetworkType() {
		return networkType;
	}

	/**
	 * @param networkType
	 *            the networkType to set
	 */
	public void setNetworkType(int networkType) {
		this.networkType = networkType;
	}

	/**
	 * @return the networkTypeName
	 */
	public String getNetworkTypeName() {
		return networkTypeName;
	}

	/**
	 * @param networkTypeName
	 *            the networkTypeName to set
	 */
	public void setNetworkTypeName(String networkTypeName) {
		this.networkTypeName = networkTypeName;
	}

	/**
	 * @return the internalMemorySize
	 */
	public long getInternalMemorySize() {
		return internalMemorySize;
	}

	/**
	 * @param internalMemorySize
	 *            the internalMemorySize to set
	 */
	public void setInternalMemorySize(long internalMemorySize) {
		this.internalMemorySize = internalMemorySize;
	}

	/**
	 * @return the externalMemorySize
	 */
	public long getExternalMemorySize() {
		return externalMemorySize;
	}

	/**
	 * @param externalMemorySize
	 *            the externalMemorySize to set
	 */
	public void setExternalMemorySize(long externalMemorySize) {
		this.externalMemorySize = externalMemorySize;
	}

	/**
	 * @return the connectionType
	 */
	public String getConnectionType() {
		return connectionType;
	}

	/**
	 * @param connectionType
	 *            the connectionType to set
	 */
	public void setConnectionType(String connectionType) {
		this.connectionType = connectionType;
	}

	
	
	
	public String getProductId() {
		return productId;
	}

	public void setProductId(String productId) {
		this.productId = productId;
	}

	/**
	 * 初始化设备信息
	 * 
	 * @param a
	 *            Activity对象(获取屏幕参数所需)
	 */
	public void updateProperties(final Activity a) {
		manufacturer = android.os.Build.MANUFACTURER;
		deviceModel = android.os.Build.MODEL;
		deviceBrand = android.os.Build.BRAND;
		deviceBoard = android.os.Build.BOARD;

		deviceKernelName = (String) System.getProperties().get("os.name");
		deviceKernelVersion = (String) System.getProperties().get("os.version");
		osVersion = android.os.Build.VERSION.RELEASE;
		sdkVersion = android.os.Build.VERSION.SDK_INT;

		// 获取设备信息
		TelephonyManager tm = null;
		try {
			tm = (TelephonyManager) a.getApplicationContext().getSystemService(
					Context.TELEPHONY_SERVICE);
			if (tm != null) {
				telNumber = tm.getLine1Number() == null ? "" : tm
						.getLine1Number();
				deviceId = tm.getDeviceId() == null ? "" : tm.getDeviceId();
				IMEI = deviceId;
				IMSI = tm.getSubscriberId() == null ? "" : tm.getSubscriberId();
				simSN = tm.getSimSerialNumber() == null ? "" : tm
						.getSimSerialNumber();
				networkOperator = tm.getNetworkOperator();
				networkCountryIso = tm.getNetworkCountryIso();
				networkType = tm.getNetworkType();
				networkOperatorName = tm.getNetworkOperatorName();

				switch (networkType) {
				case TelephonyManager.NETWORK_TYPE_EDGE: {
					networkTypeName = "EDGE";
					break;
				}
				case TelephonyManager.NETWORK_TYPE_CDMA: {
					networkTypeName = "CDMA";
					break;
				}
				case TelephonyManager.NETWORK_TYPE_GPRS: {
					networkTypeName = "GPRS";
					break;
				}
				case TelephonyManager.NETWORK_TYPE_1xRTT: {
					networkTypeName = "1xRTT";
					break;
				}
				case TelephonyManager.NETWORK_TYPE_EVDO_0: {
					networkTypeName = "EVDO_0";
					break;
				}
				case TelephonyManager.NETWORK_TYPE_EVDO_A: {
					networkTypeName = "EVDO_A";
					break;
				}
				case TelephonyManager.NETWORK_TYPE_UMTS: {
					networkTypeName = "UMTS";
					break;
				}
				case TelephonyManager.NETWORK_TYPE_UNKNOWN: {
					networkTypeName = "UNKNOWN";
					break;
				}
				default: {
					
					networkTypeName = "UNKNOWN";
					break;
				}
				}
			}
		} catch (SecurityException e) {
			e.getMessage();
			return;
		}
		
	}

	/**
	 * 收集设备参数信息 
	 * 
	 * @return
	 */
	public Map<String, Object> buildData() {
	
		params.put("mobile", telNumber);

		params.put("networkType", networkTypeName);
		params.put("os_version", osVersion);
		params.put("networkOperator", networkOperator);
		params.put("imei", IMEI);
		params.put("model_number", deviceModel);
		params.put("deviceBoard", deviceBoard);
		params.put("networkOperator", networkOperator);
		
		params.put("sdkVersion", sdkVersion);
		params.put("kernelName", deviceKernelName);
	
		params.put("manufacturer", manufacturer);
		params.put("simSN", simSN);

		params.put("imsi", IMSI);
		params.put("kernelVersion", deviceKernelVersion);
	
		params.put("osName", "android");
		return params;
	}
	
    /** 
     * 保存设备信息到文件中 
     *  
     * @param ex 
     * @return  返回文件名称,便于将文件传送到服务器 
     */  
    public  String saveDeviceInfo2File() {  
          
        StringBuffer sb = new StringBuffer();  
        buildData();
        for (Map.Entry<String, Object> entry : params.entrySet()) {  
            String key = entry.getKey();
          
            String value =  String.valueOf(entry.getValue()).toString();  
            sb.append(key + "=" + value + "\n");  
            
        }  
//          
//        Writer writer = new StringWriter();  
//        PrintWriter printWriter = new PrintWriter(writer);  
//        ex.printStackTrace(printWriter);  
//        Throwable cause = ex.getCause();  
//        while (cause != null) {  
//            cause.printStackTrace(printWriter);  
//            cause = cause.getCause();  
//        }  
//        printWriter.close();  
//        String result = writer.toString();  
//        Log.d(TAG, result);
//        sb.append(result);  
        try {  
        	SimpleDateFormat format = new SimpleDateFormat("yy/MM/dd HH:mm:ss");
    	    String ss=  format.format(new Date(System.currentTimeMillis()));
//            long timestamp = System.currentTimeMillis();  
//            SimpleDateFormat formatter=new SimpleDateFormat();
//            String time = formatter.format(new Date());  
            String fileName = "deviceInfo.log";  
            if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {  
          
                String path = 	Environment.getExternalStorageDirectory().toString()+"/weibao/";  
                File dir = new File(path);  
                if (!dir.exists()) {  
                    dir.mkdirs();  
                }  
                FileOutputStream fos = new FileOutputStream(path + fileName);  
                fos.write(sb.toString().getBytes());  
                fos.close();  
            }  
            return fileName;  
        } catch (Exception e) {  
              
        }  
        

        return null;  
    } 
}