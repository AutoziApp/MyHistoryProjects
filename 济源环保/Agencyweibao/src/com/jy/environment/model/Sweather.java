package com.jy.environment.model;

public class Sweather {

	private String windDirection;
//	private String sidu;
	private String PM2Dot5Data;
	private String weather;
	private String city;
	private String temp;
	private String weekday;
	private String feelTemp;
	private String realTime;
	private String date;
	private String level;
	private String windSpeed;
	private String lunar;
	private String pm25;
	private String SD;

	private String PM2Dot5Data_near;
	private String position_name;
	private String pm25_near;
	private String level_near;
	private String position_name_near;

	public String getPM2Dot5Data_near() {
		return PM2Dot5Data_near;
	}

	public void setPM2Dot5Data_near(String pM2Dot5Data_near) {
		PM2Dot5Data_near = pM2Dot5Data_near;
	}

	public String getPosition_name() {
		return position_name;
	}

	public void setPosition_name(String position_name) {
		this.position_name = position_name;
	}

	public String getPm25_near() {
		return pm25_near;
	}

	public void setPm25_near(String pm25_near) {
		this.pm25_near = pm25_near;
	}

	public String getLevel_near() {
		return level_near;
	}

	public void setLevel_near(String level_near) {
		this.level_near = level_near;
	}

	public String getPosition_name_near() {
		return position_name_near;
	}

	public void setPosition_name_near(String position_name_near) {
		this.position_name_near = position_name_near;
	}

	public String getSD() {
		return SD;
	}

	public void setSD(String sD) {
		SD = sD;
	}

	public String getPm25() {
		return pm25;
	}

	public void setPm25(String pm25) {
		this.pm25 = pm25;
	}

	public String getLunar() {
		return lunar;
	}

	public void setLunar(String lunar) {
		this.lunar = lunar;
	}

	public String getWindSpeed() {
		return windSpeed;
	}

	public void setWindSpeed(String windSpeed) {
		this.windSpeed = windSpeed;
	}

	public String getWindDirection() {
		return windDirection;
	}

	public void setWindDirection(String windDirection) {
		this.windDirection = windDirection;
	}

//	public String getSidu() {
//		return sidu;
//	}
//
//	public void setSidu(String sidu) {
//		this.sidu = sidu;
//	}

	public String getPM2Dot5Data() {
		return PM2Dot5Data;
	}

	public void setPM2Dot5Data(String pM2Dot5Data) {
		PM2Dot5Data = pM2Dot5Data;
	}

	public String getWeather() {
		return weather;
	}

	public void setWeather(String weather) {
		this.weather = weather;
	}

	public String getTemp() {
		return temp;
	}

	public void setTemp(String temp) {
		this.temp = temp;
	}

	public String getWeekday() {
		return weekday;
	}

	public void setWeekday(String weekday) {
		this.weekday = weekday;
	}

	public String getFeelTemp() {
		return feelTemp;
	}

	public void setFeelTemp(String feelTemp) {
		this.feelTemp = feelTemp;
	}

	public String getRealTime() {
		return realTime;
	}

	public void setRealTime(String realTime) {
		this.realTime = realTime;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getLevel() {
		return level;
	}

	public void setLevel(String level) {
		this.level = level;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public Sweather(String weather, String city, String temp, String weekday,
			String feelTemp, String realTime, String date, String level,
			String windSpeed, String windDirection,
			String PM2Dot5Data, String lunar, String pm25, String  SD
			) {
		super();
		this.weather = weather;
		this.city = city;
		this.temp = temp;
		this.weekday = weekday;
		this.feelTemp = feelTemp;
		this.realTime = realTime;
		this.date = date;
		this.level = level;
		this.windSpeed = windSpeed;
		this.windDirection = windDirection;
//		this.sidu = sidu;
		this.lunar = lunar;
		this.PM2Dot5Data = PM2Dot5Data;
		this.pm25 = pm25;
		this.SD = SD;
	}
	public Sweather(String weather, String city, String temp, String weekday,
			String feelTemp, String realTime, String date, String level,
			String windSpeed, String windDirection,
			String PM2Dot5Data, String lunar, String pm25, String  SD,
			String PM2Dot5Data_near, String position_name, String pm25_near,
			String level_near, String position_name_near) {
		super();
		this.weather = weather;
		this.city = city;
		this.temp = temp;
		this.weekday = weekday;
		this.feelTemp = feelTemp;
		this.realTime = realTime;
		this.date = date;
		this.level = level;
		this.windSpeed = windSpeed;
		this.windDirection = windDirection;
//		this.sidu = sidu;
		this.lunar = lunar;
		this.PM2Dot5Data = PM2Dot5Data;
		this.pm25 = pm25;
		this.SD = SD;
		this.PM2Dot5Data_near = PM2Dot5Data_near;
		this.position_name = position_name;
		this.pm25_near = pm25_near;
		this.level_near = level_near;
		this.position_name_near = position_name_near;
	}

	public Sweather() {
		super();
	}

	@Override
	public String toString() {
		return "Sweather [weather=" + weather + ", city=" + city + ", temp="
				+ temp + ", weekday=" + weekday + ", feelTemp=" + feelTemp
				+ ", realTime=" + realTime + ", date=" + date + ", level="
				+ level + ", windSpeed=" + windSpeed + ", lunar=" + lunar
				+ ", pm25=" + pm25 + ", SD=" + SD + ", PM2Dot5Data_near="
				+ PM2Dot5Data_near + ", position_name=" + position_name
				+ ", pm25_near=" + pm25_near + ", level_near=" + level_near
				+ ", position_name_near=" + position_name_near
				+ ", windDirection=" + windDirection 
				+ ", PM2Dot5Data=" + PM2Dot5Data + "]";
	}

}
