package com.jy.environment.model;

import java.util.ArrayList;
import java.util.List;

public class MainAqiData {

	List<ThreeDayAqiTrendModel> forecast = new ArrayList<ThreeDayAqiTrendModel>();
	int aqi;
	private String primary;
	private String updatetime;
	public List<ThreeDayAqiTrendModel> getForecast() {
		return forecast;
	}
	public void setForecast(List<ThreeDayAqiTrendModel> forecast) {
		this.forecast = forecast;
	}
	public int getAqi() {
		return aqi;
	}
	public void setAqi(int aqi) {
		this.aqi = aqi;
	}
	public String getPrimary() {
		return primary;
	}
	public void setPrimary(String primary) {
		this.primary = primary;
	}
	public String getUpdatetime() {
		return updatetime;
	}
	public void setUpdatetime(String updatetime) {
		this.updatetime = updatetime;
	}
	@Override
	public String toString() {
		return "MainAqiData [forecast=" + forecast + ", aqi=" + aqi + ", primary=" + primary + ", updatetime="
				+ updatetime + "]";
	}
	
	
}
