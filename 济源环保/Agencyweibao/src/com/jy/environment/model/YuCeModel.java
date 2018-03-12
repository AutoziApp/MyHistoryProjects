package com.jy.environment.model;

import java.util.List;

public class YuCeModel {
	private String city;
	private List<ThreeDayAqiTrendModel> trendModels;
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public List<ThreeDayAqiTrendModel> getTrendModels() {
		return trendModels;
	}

	public void setTrendModels(List<ThreeDayAqiTrendModel> trendModels) {
		this.trendModels = trendModels;
	}
	
}
