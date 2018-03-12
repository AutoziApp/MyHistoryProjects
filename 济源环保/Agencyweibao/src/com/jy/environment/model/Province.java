package com.jy.environment.model;

import java.util.Date;

import com.baidu.mapapi.model.LatLng;
//import com.baidu.platform.comapi.basestruct.GeoPoint;
//
/**
 * @author yutu
 * 省份描述 
 *
 */
public class Province { 
	private String name;
	private String city;
	private LatLng location;
	private int weather;
	private String weather_name;
	private String weather_chname;
	/**
	 * 天气点更新时间
	 */
	private Date weatherUpdateTime;
	private double temp;
	/**
	 * 温度描述点数据的更新时间，目前还没有该类数据
	 */
	private Date tempUpdateTime;
	private double aqi;
	private double pm25;
	private double pm10;
	private double no2;
	private double co;
	private double so2;
	private double o3;
	
	/**
	 * aqi,pm25,pm10,co,,....等空气质量数据的更新时间
	 */
	private Date airUpdateTime;
	
	
	
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public LatLng getLocation() {
		return location;
	}
	public void setLocation(LatLng location) {
		this.location = location;
	}
	public int getWeather() {
		return weather;
	}
	public void setWeather(int weather) {
		this.weather = weather;
	}
	public double getTemp() {
		return temp;
	}
	public void setTemp(double temp) {
		this.temp = temp;
	}
	public double getPm25() {
		return pm25;
	}
	public void setPm25(double pm25) {
		this.pm25 = pm25;
	}
	public Date getWeatherUpdateTime() {
		return weatherUpdateTime;
	}
	public void setWeatherUpdateTime(Date weatherUpdateTime) {
		this.weatherUpdateTime = weatherUpdateTime;
	}
	public Date getTempUpdateTime() {
		return tempUpdateTime;
	}
	public void setTempUpdateTime(Date tempUpdateTime) {
		this.tempUpdateTime = tempUpdateTime;
	}
	public Date getAirUpdateTime() {
		return airUpdateTime;
	}
	public void setAirUpdateTime(Date airUpdateTime) {
		this.airUpdateTime = airUpdateTime;
	}
	public double getPm10() {
		return pm10;
	}
	public void setPm10(double pm10) {
		this.pm10 = pm10;
	}
	public double getAqi() {
		return aqi;
	}
	public void setAqi(double aqi) {
		this.aqi = aqi;
	}
	public double getNo2() {
		return no2;
	}
	public void setNo2(double no2) {
		this.no2 = no2;
	}
	public double getSo2() {
		return so2;
	}
	public void setSo2(double so2) {
		this.so2 = so2;
	}
	public double getCo() {
		return co;
	}
	public void setCo(double co) {
		this.co = co;
	}
	public double getO3() {
		return o3;
	}
	public void setO3(double o3) {
		this.o3 = o3;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getWeather_name() {
		return weather_name;
	}
	public void setWeather_name(String weather_name) {
		this.weather_name = weather_name;
	}
	public String getWeather_chname() {
		return weather_chname;
	}
	public void setWeather_chname(String weather_chname) {
		this.weather_chname = weather_chname;
	}
	
	

}
