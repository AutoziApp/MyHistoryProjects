package com.jy.environment.map;

import com.baidu.mapapi.model.LatLng;




/**
 * @author shang
 * 描述渲染区域
 *	左下、右上坐标。
 */
public class RenderBlock {
	
	private double _minLatitude;
	private double _maxLatitude;
	private double _minLongitude;
	private double _maxLongitude;
	//private RelativeTileEnum _relativePos;
	
	public RenderBlock(){
		
	}
	
	/**
	 * @param minLat
	 * @param maxLat
	 * @param minLong
	 * @param maxLong
	 */
	public RenderBlock(int minLat,int maxLat,int minLong,int maxLong){
		_minLatitude = minLat;
		_maxLatitude = maxLat;
		_minLongitude = minLong;
		_maxLongitude = maxLong;
		
	}
	
	public RenderBlock(LatLng leftBottom,LatLng rightTop)
	{
		_minLatitude = leftBottom.latitude;
		_minLongitude = leftBottom.longitude;
		_maxLatitude = rightTop.latitude;
		_maxLongitude = rightTop.longitude;
		//_relativePos = em;
	}
	
	/**
	 * 扩大描述区域的范围，
	 * @param z 扩大倍数 1 为不扩大，小于1为缩小
	 */
	public void zoom(double z)
	{
		double latcenter = (_minLatitude+_maxLatitude)/2;
		double longcenter = (_minLongitude+_maxLongitude)/2;
		double latpan = Math.abs(_maxLatitude-_minLatitude)*z/2;
		double longpan = Math.abs(_maxLongitude-_minLongitude)*z/2;
		_minLatitude = (latcenter-latpan);
		_maxLatitude = (latcenter+latpan);
		_minLongitude = (longcenter-longpan);
		_maxLongitude = (longcenter+longpan);
	}
	
	public LatLng getBlockCenter(){
		return new LatLng((_minLatitude+_maxLatitude)/2, 
				(_minLongitude+_maxLongitude)/2);
	}
	
	public LatLng getLeftBottomPT(){
		return new LatLng(_minLatitude, _minLongitude);
	}
	
	public LatLng getRightTopPT(){
		return new LatLng(_maxLatitude,_maxLongitude);
	}

	public double get_minLatitude() {
		return _minLatitude;
	}

	public void set_minLatitude(int _minLatitude) {
		this._minLatitude = _minLatitude;
	}

	public double get_maxLatitude() {
		return _maxLatitude;
	}

	public void set_maxLatitude(int _maxLatitude) {
		this._maxLatitude = _maxLatitude;
	}

	public double get_minLongitude() {
		return _minLongitude;
	}

	public void set_minLongitude(int _minLongitude) {
		this._minLongitude = _minLongitude;
	}

	public double get_maxLongitude() {
		return _maxLongitude;
	}

	public void set_maxLongitude(int _maxLongitude) {
		this._maxLongitude = _maxLongitude;
	}

	

}
