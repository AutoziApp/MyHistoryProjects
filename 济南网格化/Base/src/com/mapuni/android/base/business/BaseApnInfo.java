package com.mapuni.android.base.business;

/**
 * FileName: ApnInfo.java 
 * Description: APN业务类
 * @author 窦帅
 * @Version 1.3.4
 * @Copyright 中科宇图天下科技有限公司 
 * Create at: 2012-11-30 下午02:04:13
 */
public class BaseApnInfo {

	/** apn的id */
	private String apnId;
	/** apn的名字 */
	private String Name;
	/** apn接入点名称，如：3gnet */
	private String apnName;
	/** SIM卡的信息 */
	private String numeric;
	/** 当前apn名称 */
	private String current;
	/**
	 * 移动国家号码，由3位数字组成，唯一地识别移动用户所属的国家。
	 * 我国为460 MCC和MNC是固定的，也就是在你开始用这张手机卡时就已经是这个序列了。
	 */
	private String mcc;
	/** 移动网号，由两位数字组成，用于识别移动用户所归属的移动网。00/02→移动；01/03→联通 */
	private String mnc;
	/** 接入点名称类型 */
	private String type;
	/** apn代理 */
	private String proxy;
	/** apn端口 */
	private String port;

	/** 以下是各个属性的get和set方法 */
	public String getApnId() {
		return apnId;
	}

	public void setApnId(String apnId) {
		this.apnId = apnId;
	}

	public String getApnName() {
		return apnName;
	}

	public void setApnName(String apnName) {
		this.apnName = apnName;
	}

	public String getNumeric() {
		return numeric;
	}

	public void setNumeric(String numeric) {
		this.numeric = numeric;
	}

	public String getCurrent() {
		return current;
	}

	public void setCurrent(String current) {
		this.current = current;
	}

	public String getMcc() {
		return mcc;
	}

	public void setMcc(String mcc) {
		this.mcc = mcc;
	}

	public String getMnc() {
		return mnc;
	}

	public void setMnc(String mnc) {
		this.mnc = mnc;
	}

	public String getName() {
		return Name;
	}

	public void setName(String name) {
		Name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getProxy() {
		return proxy;
	}

	public void setProxy(String proxy) {
		this.proxy = proxy;
	}

	public String getPort() {
		return port;
	}

	public void setPort(String port) {
		this.port = port;
	}

}
