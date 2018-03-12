package com.jy.environment.anime;


public class Rain {
	public Coordinate coordinate;
	public int speed;
	public int bitmap ;
	
	public Rain(int x, int y, int speed){
		coordinate = new Coordinate(x, y);
		this.speed = speed;
		if(this.speed == 0) {
			this.speed =1;
		}
	}

	public int getBitmap() {
		return bitmap;
	}

	public void setBitmap(int bitmap) {
		this.bitmap = bitmap;
	}
	
	
}
