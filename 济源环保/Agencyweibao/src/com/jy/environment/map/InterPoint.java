package com.jy.environment.map;


public class InterPoint {
	private Point _point;
	private double _weight;
	private Point _point1;
	
	public InterPoint(Point p,Point p1,double w){
		this._point=p;
		_point1=p1;
		_weight=w;
	}
	public Point get_point() {
		return _point;
	}
	public void set_point(Point _point) {
		this._point = _point;
	}

	public double get_weight() {
		return _weight;
	}
	public void set_weight(double _weight) {
		this._weight = _weight;
	}
	//按照距离排序
	public int compare(InterPoint p){
		if(this.distance()<p.distance()){
			return -1;
		}
		else if(this.distance()>p.distance()){
			return 1;
		}
		else{
			return 0;
		}
	}
	
	//两点之间的距离公式
	public double distance(){
		return Math.sqrt((_point.x-_point1.x)*(_point.x-_point1.x)+(_point.y-_point1.y)*(_point.y-_point1.y));
	}

}


