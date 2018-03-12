package com.jy.environment.model;

import java.util.List;

public class PmDayHourModel {
	private List<PmModel> hourModels;
	private List<PmModel> minuteModels;
	private List<PmModel> dayModels;
	private String pm10;
	private String pm25;
	private String pmday10;
	private String pmday25;
	private String time;
	private String level;
	private String pollutant;
	
	public List<PmModel> getHourModels() {
		return hourModels;
	}
	public void setHourModels(List<PmModel> hourModels) {
		this.hourModels = hourModels;
	}
	public List<PmModel> getMinuteModels() {
		return minuteModels;
	}
	public void setMinuteModels(List<PmModel> minuteModels) {
		this.minuteModels = minuteModels;
	}
	public List<PmModel> getDayModels() {
		return dayModels;
	}
	public void setDayModels(List<PmModel> dayModels) {
		this.dayModels = dayModels;
	}
	@Override
	public String toString() {
		return "PmDayHourModel [hourModels=" + hourModels + ", minuteModels="
				+ minuteModels + ", dayModels=" + dayModels + "]";
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
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public String getLevel() {
		return level;
	}
	public void setLevel(String level) {
		this.level = level;
	}
	public String getPollutant() {
		return pollutant;
	}
	public void setPollutant(String pollutant) {
		this.pollutant = pollutant;
	}
	public String getPmday10() {
		return pmday10;
	}
	public void setPmday10(String pmday10) {
		this.pmday10 = pmday10;
	}
	public String getPmday25() {
		return pmday25;
	}
	public void setPmday25(String pmday25) {
		this.pmday25 = pmday25;
	}
	
}
