package com.jy.environment.model;

import java.util.List;

public class TrendModel {
	private List<AQI> listAQI7day;
	private List<PM10Info24H> pm10Info24Hs;
	private List<AQI> listAQI24h;
	private List<AQI> listAQI30d;
	private List<PM25> listPM25Of24h;
	private List<SO2> listSO2Of24h;
	private List<NO2> listNO2Of24h;
	public TrendModel(List<AQI> listAQI7day, List<PM10Info24H> pm10Info24Hs, List<AQI> listAQI24h, List<AQI> listAQI30d,
			List<PM25> listPM25Of24h,List<SO2> listSO2Of24h,List<NO2> listNO2Of24h) {
		super();
		this.listAQI7day = listAQI7day;
		this.pm10Info24Hs = pm10Info24Hs;
		this.listAQI24h = listAQI24h;
		this.listAQI30d = listAQI30d;
		this.listPM25Of24h = listPM25Of24h;
		this.listSO2Of24h=listSO2Of24h;
		this.listNO2Of24h=listNO2Of24h;
	}
	public TrendModel() {
		super();
	}
	public List<AQI> getListAQI7day() {
		return listAQI7day;
	}
	public void setListAQI7day(List<AQI> listAQI7day) {
		this.listAQI7day = listAQI7day;
	}
	public List<PM10Info24H> getPm10Info24Hs() {
		return pm10Info24Hs;
	}
	public void setPm10Info24Hs(List<PM10Info24H> pm10Info24Hs) {
		this.pm10Info24Hs = pm10Info24Hs;
	}
	public List<AQI> getListAQI24h() {
		return listAQI24h;
	}
	public void setListAQI24h(List<AQI> listAQI24h) {
		this.listAQI24h = listAQI24h;
	}
	public List<AQI> getListAQI30d() {
		return listAQI30d;
	}
	public void setListAQI30d(List<AQI> listAQI30d) {
		this.listAQI30d = listAQI30d;
	}
	public List<PM25> getListPM25Of24h() {
		return listPM25Of24h;
	}
	public void setListPM25Of24h(List<PM25> listPM25Of24h) {
		this.listPM25Of24h = listPM25Of24h;
	}
	public List<SO2> getListSO2Of24h() {
		return listSO2Of24h;
	}
	public void setListSO2Of24h(List<SO2> listSO2Of24h) {
		this.listSO2Of24h = listSO2Of24h;
	}
	public List<NO2> getListNO2Of24h() {
		return listNO2Of24h;
	}
	public void setListNO2Of24h(List<NO2> listNO2Of24h) {
		this.listNO2Of24h = listNO2Of24h;
	}
	@Override
	public String toString() {
		return "TrendModel [listAQI7day=" + listAQI7day + ", pm10Info24Hs=" + pm10Info24Hs + ", listAQI24h="
				+ listAQI24h + ", listAQI30d=" + listAQI30d + ", listPM25Of24h=" + listPM25Of24h + ", listSO2Of24h=" + listSO2Of24h
				+ ", listNO2Of24h=" + listNO2Of24h+ "]";
	}
	
}
