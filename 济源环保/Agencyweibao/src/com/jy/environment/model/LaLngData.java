package com.jy.environment.model;

public class LaLngData {
	/**
	 * 不含“市”字
	 */
	private String cityname;
	private double weidu;
	private double jingdu;

	public LaLngData() {
		super();
	}

	public LaLngData(String cityname, double weidu, double jingdu) {
		super();
		this.cityname = cityname;
		this.weidu = weidu;
		this.jingdu = jingdu;
	}

	public String getCityname() {
		return cityname;
	}

	public void setCityname(String cityname) {
		this.cityname = cityname;
	}

	public double getWeidu() {
		return weidu;
	}

	public void setWeidu(double weidu) {
		this.weidu = weidu;
	}

	public double getJingdu() {
		return jingdu;
	}

	public void setJingdu(double jingdu) {
		this.jingdu = jingdu;
	}

	@Override
	public String toString() {
		return "LaLngData [cityname=" + cityname + ", weidu=" + weidu + ", jingdu=" + jingdu + "]";
	}

}
