package com.jy.environment.model;

public class Kongqizhishu {

	private String aqi;
	private String level;
	private String pm25;
	private String pm10;
	private String no2;
	private String So2;
	private String co;
	private String O3;
	private String qingkuang;
	private String jianyi;
	private String mingri;
	public String getAqi() {
		return aqi;
	}
	public void setAqi(String aqi) {
		this.aqi = aqi;
	}
	public String getLevel() {
		return level;
	}
	public void setLevel(String level) {
		this.level = level;
	}
	public String getPm25() {
		return pm25;
	}
	public void setPm25(String pm25) {
		this.pm25 = pm25;
	}
	public String getPm10() {
		return pm10;
	}
	public void setPm10(String pm10) {
		this.pm10 = pm10;
	}
	public String getNo2() {
		return no2;
	}
	public void setNo2(String no2) {
		this.no2 = no2;
	}
	public String getSo2() {
		return So2;
	}
	public void setSo2(String so2) {
		So2 = so2;
	}
	public String getCo() {
		return co;
	}
	public void setCo(String co) {
		this.co = co;
	}
	public String getO3() {
		return O3;
	}
	public void setO3(String o3) {
		O3 = o3;
	}
	public String getQingkuang() {
		return qingkuang;
	}
	public void setQingkuang(String qingkuang) {
		this.qingkuang = qingkuang;
	}
	public String getJianyi() {
		return jianyi;
	}
	public void setJianyi(String jianyi) {
		this.jianyi = jianyi;
	}
	
	
	public String getMingri() {
		return mingri;
	}
	public void setMingri(String mingri) {
		this.mingri = mingri;
	}
	public Kongqizhishu(String aqi, String level, String pm25, String pm10,
			String no2, String so2, String co, String o3, String qingkuang,
			String jianyi, String mingri) {
		super();
		this.aqi = aqi;
		this.level = level;
		this.pm25 = pm25;
		this.pm10 = pm10;
		this.no2 = no2;
		So2 = so2;
		this.co = co;
		O3 = o3;
		this.qingkuang = qingkuang;
		this.jianyi = jianyi;
		this.mingri = mingri;
	}
	public Kongqizhishu() {
		super();
		// TODO Auto-generated constructor stub
	}

	
	
	
	
}
