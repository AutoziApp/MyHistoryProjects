package com.jy.environment.model;

public class ListHumiDityModel {
	private String timne;
	private int humidity;

	public String getTimne() {
		return timne;
	}

	public void setTimne(String timne) {
		this.timne = timne;
	}

	public int getHumidity() {
		return humidity;
	}

	public void setHumidity(int humidity) {
		this.humidity = humidity;
	}

	@Override
	public String toString() {
		return "ListHumiDityModel [timne=" + timne + ", humidity=" + humidity
				+ "]";
	}
}
