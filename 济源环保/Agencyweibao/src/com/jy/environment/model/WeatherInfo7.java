package com.jy.environment.model;

public class WeatherInfo7 {

	private String temp="";
	private String weather="";
	private String wind="";
	private String windLevel="";
	private String todayTime="";
	
	
	public String getTemp() {
		return temp;
	}
	public void setTemp(String temp) {
		this.temp = temp;
	}
	public String getWeather() {
		return weather;
	}
	public void setWeather(String weather) {
		this.weather = weather;
	}
	public String getWind() {
		return wind;
	}
	public void setWind(String wind) {
		this.wind = wind;
	}
	public String getWindLevel() {
		return windLevel;
	}
	public void setWindLevel(String windLevel) {
		this.windLevel = windLevel;
	}
	public String getTodayTime() {
		return todayTime;
	}
	public void setTodayTime(String todayTime) {
		this.todayTime = todayTime;
	}
	@Override
	public String toString() {
	    return "WeatherInfo7 [temp=" + temp + ", weather=" + weather
		    + ", wind=" + wind + ", windLevel=" + windLevel
		    + ", todayTime=" + todayTime + "]";
	}
		
}
