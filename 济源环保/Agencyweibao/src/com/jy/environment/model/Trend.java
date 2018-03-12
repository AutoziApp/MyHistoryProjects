package com.jy.environment.model;

public class Trend {
	private String temp;
	private String weather;
	private String week;
	private String date;

	public String getTemp() {
		return temp;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
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

	public String getWeek() {
		return week;
	}

	public void setWeek(String week) {
		this.week = week;
	}

	@Override
	public String toString() {
		return "Trend [temp=" + temp + ", weather=" + weather + ", week="
				+ week + "]";
	}

}
