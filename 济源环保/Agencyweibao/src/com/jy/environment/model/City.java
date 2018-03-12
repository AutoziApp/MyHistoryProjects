package com.jy.environment.model;

import java.io.Serializable;
import java.util.Date;

import com.baidu.mapapi.model.LatLng;

//import com.baidu.platform.comapi.basestruct.GeoPoint;

public class City implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String province;
	private String name;
	private String number;
	private String pinyin;
	private String py;
	private int weather;
	private String weathername;
	private String weatherchname;
	private Date weatherUpdateDate;
	private LatLng location;
	private Date surfaceWaterUpdateDate;
	private Date pollutionUpdateDate;
//	private String isLocaton;

	public City() {
	}

	public City(String province, String city, String number, String allPY,
			String allFristPY) {
		super();
		this.province = province;
		this.name = city;
		this.number = number;
		this.pinyin = allPY;
		this.py = allFristPY;
//		this.location = location;
		
	}

//	public String getIsLocaton() {
//	    return isLocaton;
//	}
//
//	public void setIsLocaton(String isLocaton) {
//	    this.isLocaton = isLocaton;
//	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public String getPinyin() {
		return pinyin;
	}

	public void setPinyin(String pinyin) {
		this.pinyin = pinyin;
	}

	public String getPy() {
		return py;
	}

	public void setPy(String py) {
		this.py = py;
	}


	public int getWeather() {
		return weather;
	}

	public void setWeather(int weather) {
		this.weather = weather;
	}

	public LatLng getLocation() {
		return location;
	}

	public void setLocation(LatLng location) {
		this.location = location;
	}

	public String getWeathername() {
		return weathername;
	}

	public void setWeathername(String weathername) {
		this.weathername = weathername;
	}

	public String getWeatherchname() {
		return weatherchname;
	}

	public void setWeatherchname(String weatherchname) {
		this.weatherchname = weatherchname;
	}

	public Date getWeatherUpdateDate() {
		return weatherUpdateDate;
	}

	public void setWeatherUpdateDate(Date weatherUpdateDate) {
		this.weatherUpdateDate = weatherUpdateDate;
	}

	public Date getSurfaceWaterUpdateDate() {
		return surfaceWaterUpdateDate;
	}

	public void setSurfaceWaterUpdateDate(Date surfaceWaterUpdateDate) {
		this.surfaceWaterUpdateDate = surfaceWaterUpdateDate;
	}

	public Date getPollutionUpdateDate() {
		return pollutionUpdateDate;
	}

	public void setPollutionUpdateDate(Date pollutionUpdateDate) {
		this.pollutionUpdateDate = pollutionUpdateDate;
	}

	@Override
	public String toString() {
		return "City [province=" + province + ", name=" + name + ", number="
				+ number + ", pinyin=" + pinyin + ", py=" + py + ", weather="
				+ weather + ", weathername=" + weathername + ", weatherchname="
				+ weatherchname + ", weatherUpdateDate=" + weatherUpdateDate
				+ ", location=" + location + ", surfaceWaterUpdateDate="
				+ surfaceWaterUpdateDate + ", pollutionUpdateDate="
				+ pollutionUpdateDate + "]";
	}

	
}
