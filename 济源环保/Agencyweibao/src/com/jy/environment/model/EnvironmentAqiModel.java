package com.jy.environment.model;

import java.util.List;


public class EnvironmentAqiModel {
	private String level;
	private String updatetime;
	private String pollutant;
	private int aqi;
	private String pm10;
	private String pm25;
	private List<EnvironmentMonitorModel> monitorModels;
	public String getLevel() {
		return level;
	}
	public void setLevel(String level) {
		this.level = level;
	}
	public String getUpdatetime() {
		return updatetime;
	}
	public void setUpdatetime(String updatetime) {
		this.updatetime = updatetime;
	}
	public int getAqi() {
		return aqi;
	}
	public void setAqi(int aqi) {
		this.aqi = aqi;
	}
	public List<EnvironmentMonitorModel> getMonitorModels() {
		return monitorModels;
	}
	public void setMonitorModels(List<EnvironmentMonitorModel> monitorModels) {
		this.monitorModels = monitorModels;
	}
	public String getPollutant() {
		return pollutant;
	}
	public void setPollutant(String pollutant) {
		this.pollutant = pollutant;
	}
	public String getPm10() {
		return pm10;
	}
	public void setPm10(String pm10) {
		this.pm10 = pm10;
	}
	public String getPm25() {
		return pm25;
	}
	public void setPm25(String pm25) {
		this.pm25 = pm25;
	}
}
