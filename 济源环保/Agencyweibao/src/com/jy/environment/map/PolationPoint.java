package com.jy.environment.map;


public class PolationPoint {
	private Point _point;
	public Point get_point() {
		return _point;
	}
	public void set_point(Point _point) {
		this._point = _point;
	}
	private double _weight;

	public double get_weight() {
		return _weight;
	}
	public void set_weight(double _weight) {
		this._weight = _weight;
	}
	public PolationPoint(Point p,double w){
		_point=p;
		_weight=w;
	}
	
	public int compare(PolationPoint p){
		if(this._point.y<p.get_point().y){
			return -1;
		}
		else if(this._point.y>p.get_point().y){
			return 1;
		}
		else{
			return 0;
		}
	}
}
