package com.jy.environment.model;


public class Item{

	private String cityName;
	private String rank;
	private String index;
	public String getCityName() {
		return cityName;
	}
	public void setCityName(String cityName) {
		this.cityName = cityName;
	}
	public String getRank() {
		return rank;
	}
	public void setRank(String rank) {
		this.rank = rank;
	}
	public String getIndex() {
		return index;
	}
	public void setIndex(String index) {
		this.index = index;
	}
	@Override
	public String toString() {
		return "Item [cityName=" + cityName + ", rank=" + rank + ", index="
				+ index + "]";
	}
	
	
}
