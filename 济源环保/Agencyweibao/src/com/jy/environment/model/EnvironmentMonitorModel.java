package com.jy.environment.model;

import java.util.List;

public class EnvironmentMonitorModel {
	private String station;
	private String stationtype;
	private List<WeatherInfoMonth> infoMonths;

	public String getStation() {
		return station;
	}

	public void setStation(String station) {
		this.station = station;
	}

	public String getStationtype() {
		return stationtype;
	}

	public void setStationtype(String stationtype) {
		this.stationtype = stationtype;
	}

	public List<WeatherInfoMonth> getInfoMonths() {
		return infoMonths;
	}

	public void setInfoMonths(List<WeatherInfoMonth> infoMonths) {
		this.infoMonths = infoMonths;
	}

	@Override
	public String toString() {
		return "EnvironmentMonitorModel [station=" + station + ", stationtype="
				+ stationtype + ", infoMonths=" + infoMonths + "]";
	}

}
