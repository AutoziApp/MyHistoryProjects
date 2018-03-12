package com.jy.environment.model;

public class AQIPoint {

	private double jingdu;
	private double weidu;
	private String aqi;
	private String jiancedian;
	private String city;
	private String citycode;
	private String pm2_5;
	private String SO2;
	private String NO2;
	private String O3;
	private String CO;
	private String O32;// 暂时不用
	
	private String PM10;
	private String updateTime;

	
	
	public double getJingdu() {
		return jingdu;
	}

	public void setJingdu(double jingdu) {
		this.jingdu = jingdu;
	}

	public double getWeidu() {
		return weidu;
	}

	public void setWeidu(double weidu) {
		this.weidu = weidu;
	}

	public String getAqi() {
		return aqi;
	}

	public void setAqi(String aqi) {
		this.aqi = aqi;
	}

	public String getJiancedian() {
		return jiancedian;
	}

	public void setJiancedian(String jiancedian) {
		this.jiancedian = jiancedian;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getCitycode() {
		return citycode;
	}

	public void setCitycode(String citycode) {
		this.citycode = citycode;
	}

	public String getPm2_5() {
		return pm2_5;
	}

	public void setPm2_5(String pm2_5) {
		this.pm2_5 = pm2_5;
	}

	public String getSO2() {
		return SO2;
	}

	public void setSO2(String sO2) {
		SO2 = sO2;
	}

	public String getNO2() {
		return NO2;
	}

	public void setNO2(String nO2) {
		NO2 = nO2;
	}

	public String getO3() {
		return O3;
	}

	public void setO3(String o3) {
		O3 = o3;
	}

	public String getCO() {
		return CO;
	}

	public void setCO(String cO) {
		CO = cO;
	}

	public String getO32() {
		return O32;
	}

	public void setO32(String o32) {
		O32 = o32;
	}

	public String getPM10() {
		return PM10;
	}

	public void setPM10(String pM10) {
		PM10 = pM10;
	}

	public String getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}

	public AQIPoint() {
		super();
	}

	/**
	 * TODO YYF应使用该构造方法传递对应参数；
	 * @param jingdu
	 * @param weidu
	 * @param aqi
	 * @param jiancedian
	 * @param pm2_5
	 * @param sO2
	 * @param nO2
	 * @param o3
	 * @param cO
	 * @param o32
	 * @param pM10
	 */
	public AQIPoint(double jingdu, double weidu, String aqi, String jiancedian,
			String pm2_5, String sO2, String nO2, String o3, String cO,
			String o32, String pM10) {
		super();
		this.jingdu = jingdu;
		this.weidu = weidu;
		this.aqi = aqi;
		this.jiancedian = jiancedian;
		this.pm2_5 = pm2_5;
		SO2 = sO2;
		NO2 = nO2;
		O3 = o3;
		CO = cO;
		O32 = o32;
		PM10 = pM10;
	}
	
	/**
	 * 
	 * @param jingdu
	 * @param weidu
	 * @param aqi
	 * @param jiancedian
	 * @param pm2_5
	 * @param sO2
	 * @param nO2
	 * @param o3
	 * @param cO
	 * @param o32
	 * @param pM10
	 * @param updateTime
	 */
	public AQIPoint(double jingdu, double weidu, String aqi, String jiancedian, String pm2_5, String sO2, String nO2,
			String o3, String cO, String o32, String pM10, String updateTime) {
		super();
		this.jingdu = jingdu;
		this.weidu = weidu;
		this.aqi = aqi;
		this.jiancedian = jiancedian;
		this.pm2_5 = pm2_5;
		SO2 = sO2;
		NO2 = nO2;
		O3 = o3;
		CO = cO;
		O32 = o32;
		PM10 = pM10;
		this.updateTime = updateTime;
	}

	@Override
	public String toString() {
		return "AQIPoint [jingdu=" + jingdu + ", weidu=" + weidu + ", aqi="
				+ aqi + ", jiancedian=" + jiancedian + ", city=" + city
				+ ", citycode=" + citycode + ", pm2_5=" + pm2_5 + ", SO2="
				+ SO2 + ", NO2=" + NO2 + ", O3=" + O3 + ", CO=" + CO + ", O32="
				+ O32 + ", PM10=" + PM10 + ", updateTime=" + updateTime + "]";
	}

	public AQIPoint(double jingdu, double weidu, String aqi, String jiancedian) {
		super();
		this.jingdu = jingdu;
		this.weidu = weidu;
		this.aqi = aqi;
		this.jiancedian = jiancedian;
	}

	public AQIPoint(double jingdu, double weidu, String aqi, String jiancedian,
			String updateTime) {
		super();
		this.jingdu = jingdu;
		this.weidu = weidu;
		this.aqi = aqi;
		this.jiancedian = jiancedian;
		this.updateTime = updateTime;
	}




}
