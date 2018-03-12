package com.mapuni.android.base.business;

/**
 * FileName: ApnInfo.java 
 * Description: APNҵ����
 * @author �˧
 * @Version 1.3.4
 * @Copyright �п���ͼ���¿Ƽ����޹�˾ 
 * Create at: 2012-11-30 ����02:04:13
 */
public class BaseApnInfo {

	/** apn��id */
	private String apnId;
	/** apn������ */
	private String Name;
	/** apn��������ƣ��磺3gnet */
	private String apnName;
	/** SIM������Ϣ */
	private String numeric;
	/** ��ǰapn���� */
	private String current;
	/**
	 * �ƶ����Һ��룬��3λ������ɣ�Ψһ��ʶ���ƶ��û������Ĺ��ҡ�
	 * �ҹ�Ϊ460 MCC��MNC�ǹ̶��ģ�Ҳ�������㿪ʼ�������ֻ���ʱ���Ѿ�����������ˡ�
	 */
	private String mcc;
	/** �ƶ����ţ�����λ������ɣ�����ʶ���ƶ��û����������ƶ�����00/02���ƶ���01/03����ͨ */
	private String mnc;
	/** ������������� */
	private String type;
	/** apn���� */
	private String proxy;
	/** apn�˿� */
	private String port;

	/** �����Ǹ������Ե�get��set���� */
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
