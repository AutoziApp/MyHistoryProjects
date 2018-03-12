package com.jy.environment.map;

import java.util.HashMap;


public class PollutantStation {
	//基站坐标
	private Point _point;

	//污染物值
	private HashMap<String, Double> _pollutantValue;  
	
	//基站名称
	private String _stationName;
	
	//城市名称
	private String _CityName;
	
	//城市ID
	private int _cityID;
	
	public Point get_point() {
		return _point;
	}

	public void set_point(Point _point) {
		this._point = _point;
	}

	public HashMap<String, Double> get_pollutantValue() {
		return _pollutantValue;
	}

	public void set_pollutantValue(HashMap<String, Double> _pollutantValue) {
		this._pollutantValue = _pollutantValue;
	}

	public String get_stationName() {
		return _stationName;
	}

	public void set_stationName(String _stationName) {
		this._stationName = _stationName;
	}

	public String get_CityName() {
		return _CityName;
	}

	public void set_CityName(String _CityName) {
		this._CityName = _CityName;
	}

	public int get_cityID() {
		return _cityID;
	}

	public void set_cityID(int _cityID) {
		this._cityID = _cityID;
	}
	
}
